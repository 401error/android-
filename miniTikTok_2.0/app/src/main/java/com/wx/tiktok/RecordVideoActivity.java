package com.wx.tiktok;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RecordVideoActivity extends AppCompatActivity {

    private SurfaceView mSurfaceView;
    private SurfaceHolder surfaceHolder;
    private Camera mCamera;
    private Button startBtn;
    private Button shiftCameraBtn;
    private Intent goMainActivityIntent;
    private Intent sendIntent;
    private Button sendBtn;
    private File video;
    private static String videoPath;
    private int CAMERA_TYPE = Camera.CameraInfo.CAMERA_FACING_BACK;
    private boolean isRecording = false;

    private int rotationDegree = 0;
    private boolean isTurn=false;
    private int cameraToward=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        ActivityCompat.requestPermissions(RecordVideoActivity.this,new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        setContentView(R.layout.activity_record_video);
        mCamera=getCamera(CAMERA_TYPE);
        mSurfaceView = findViewById(R.id.surfaceView);
        sendBtn=findViewById(R.id.buttonSend);
        sendBtn.setVisibility(View.INVISIBLE);
        //todo 给SurfaceHolder添加Callback
        surfaceHolder=mSurfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    mCamera.setPreviewDisplay(holder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mCamera.stopPreview();
                mCamera.release();
                mCamera=null;
            }
        });
        startBtn=findViewById(R.id.start);
        shiftCameraBtn=findViewById(R.id.shiftCameraBtn);
        //开始录制按钮
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isRecording){
                    releaseMediaRecorder();
                    Log.d("aa", "onClick() called with: v = [" + v + "]");
                    isRecording=false;
                    sendBtn.setVisibility(View.VISIBLE);
                    startBtn.setBackgroundResource(R.drawable.btn_circle_shape_origin);
                    startBtn.setText("拍摄");
                }else{

                    boolean isPrepared=prepareVideoRecorder();
                    Log.d("ee", "onClick() called with: v = [" + v + "]");
                    isRecording=true;
                    sendBtn.setVisibility(View.INVISIBLE);
                    startBtn.setBackgroundResource(R.drawable.btn_circle_shape);
                    startBtn.setText("拍摄中...");
                }
            }
        });
        int cameraCount=Camera.getNumberOfCameras();
        if(cameraCount<2){
            shiftCameraBtn.setVisibility(View.INVISIBLE);
        }else{
            shiftCameraBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //释放当前摄像头的资源
                    releaseCameraAndPreview();
                    if(isTurn==true){
                        cameraToward=Camera.CameraInfo.CAMERA_FACING_BACK;
                    }else{
                        cameraToward=Camera.CameraInfo.CAMERA_FACING_FRONT;
                    }

                    mCamera=getCamera(cameraToward);
                    try {
                        mCamera.setPreviewDisplay(surfaceHolder);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mCamera.startPreview();
                    isTurn=!isTurn;
                }
            });
        }

        //发送视频本地地址到发送页面
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendIntent=new Intent(RecordVideoActivity.this,readySendviedeo.class);
                sendIntent.putExtra("dizhi",videoPath);
                sendIntent .putExtra("type",2);
                System.out.println(videoPath);
                Log.d("tt", "onClick: "+videoPath);
                startActivity(sendIntent);
                releaseMediaRecorder();
                //releaseCameraAndPreview();
                finish();
            }
        });

    }
    //处理对焦事件
    private float mDist;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Camera.Parameters parameters=mCamera.getParameters();
        int action=event.getAction();

        if (event.getPointerCount() > 1) {
            //多点触摸
            if(action==MotionEvent.ACTION_POINTER_DOWN){
                mDist=getFingerSpacing(event);
            }else if(action==MotionEvent.ACTION_MOVE&&parameters.isZoomSupported()){
                mCamera.cancelAutoFocus();
                handleZoom(event,parameters);
            }
        }else{
            //单点触摸自动对焦
            //mCamera.autoFocus(null);
            handleFocus(event,parameters);
        }
        return super.onTouchEvent(event);
    }
    private void handleZoom(MotionEvent event, Camera.Parameters params) {
        int maxZoom = params.getMaxZoom();
        int zoom = params.getZoom();
        float newDist = getFingerSpacing(event);
        if (newDist > mDist) {
            //zoom in
            if (zoom < maxZoom)
                zoom++;
        } else if (newDist < mDist) {
            //zoom out
            if (zoom > 0)
                zoom--;
        }
        mDist = newDist;
        params.setZoom(zoom);
        mCamera.setParameters(params);
    }
    public void handleFocus(MotionEvent event, Camera.Parameters params) {
        int pointerId = event.getPointerId(0);
        int pointerIndex = event.findPointerIndex(pointerId);
        // Get the pointer's current position
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        List<String> supportedFocusModes = params.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean b, Camera camera) {
                    // currently set to auto-focus on single touch
                }
            });
        }
    }
    /** Determine the space between the first two fingers */
    private float getFingerSpacing(MotionEvent event) {
        // ...
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }
    public Camera getCamera(int position) {
        CAMERA_TYPE = position;
        if (mCamera != null) {
            releaseCameraAndPreview();
        }
        Camera cam = Camera.open(position);
        rotationDegree=getCameraDisplayOrientation(position);
        cam.setDisplayOrientation(rotationDegree);
        cam.getParameters().setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        return cam;
    }
    private static final int DEGREE_90 = 90;
    private static final int DEGREE_180 = 180;
    private static final int DEGREE_270 = 270;
    private static final int DEGREE_360 = 360;

    private int getCameraDisplayOrientation(int cameraId) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = DEGREE_90;
                break;
            case Surface.ROTATION_180:
                degrees = DEGREE_180;
                break;
            case Surface.ROTATION_270:
                degrees = DEGREE_270;
                break;
            default:
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % DEGREE_360;
            result = (DEGREE_360 - result) % DEGREE_360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + DEGREE_360) % DEGREE_360;
        }
        return result;
    }


    private void releaseCameraAndPreview() {
        if(mCamera!=null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }
    private MediaRecorder mMediaRecorder;


    private boolean prepareVideoRecorder() {

        mMediaRecorder=new MediaRecorder();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        video=Utils.getOutputMediaFile(Utils.MEDIA_TYPE_VIDEO);
        System.out.println(videoPath);
        videoPath=video.toURI().toString();
        mMediaRecorder.setOutputFile(video.toString());
        mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
        mMediaRecorder.setOrientationHint(rotationDegree);

        try{
            mMediaRecorder.prepare();

            mMediaRecorder.start();
            Log.d("222", "prepareVideoRecorder() called");
        }catch (Exception e){
            Log.d("4444", "142422422424d");
//            releaseMediaRecorder();
        }
        Log.d("record", "start");
        return true;
    }


    private void releaseMediaRecorder() {
        if(mMediaRecorder!=null){
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder=null;
            mCamera.lock();
            Log.d("record", "stop");
        }
    }



    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = Math.min(w, h);

        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

}

package com.wx.tiktok;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.wx.tiktok.BEAN.Post;
import com.wx.tiktok.BEAN.ResourceUtils;
import com.wx.tiktok.NET.PostVideo;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Url;

public class readySendviedeo extends AppCompatActivity {

    private int PICK_VIEDEO = 1;
    private int PICK_IMAGE = 2;
    private Uri mSelectedVideo;
    private Uri mSelectedImage;
    private VideoView videoView;
    private MediaPlayer player;
    private  SurfaceView surfaceView;
    private  SurfaceHolder holder;
    private Button select_button;
    private Button select_m;
    private ImageView imageView;
    private static Bundle outState;
    private Button post_btn;
    private  Uri PLAY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_sendviedeo);
        requestReadExternalStoragePermission("select a video");
        select_button = findViewById(R.id.select_video);
        videoView = findViewById(R.id.videoplay);
        videoView.setVisibility(View.INVISIBLE);
        imageView = findViewById(R.id.read_image);
        select_m = findViewById(R.id.select_image);
        post_btn=findViewById(R.id.post);
//        if(outState!=null) {
//
//            Log.d("sssss", "onCreate() called with: savedInstanceState = [" +mSelectedVideo + "]");
//            mSelectedVideo=Uri.parse(outState.getString("uir_viedeo"));
//            mSelectedImage=Uri.parse(outState.getString("uir_image"));
//            if(mSelectedVideo!=null){
//
//             videoView.setVideoURI(mSelectedVideo);
//             videoView.start();
//            }
//            if(mSelectedVideo!=null) {
//                videoView.start();
//
//            }




        Intent getUri = getIntent();
        int type =getUri.getIntExtra("type",1);
        if(type==2)
        {

            String URI =  getUri.getStringExtra("dizhi");
            PLAY = Uri.parse(URI);
            if(URI!=null) {
                videoView.setVisibility(View.VISIBLE);
                mSelectedVideo=PLAY;
                videoView.setVideoURI(mSelectedVideo);
                Log.d("URI", "onCreate() called with: savedInstanceState = [" + URI + "]");
                videoView.start();
            }
        }


        select_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestReadExternalStoragePermission("select_viedio")) {
                    chooseVideo();
//                    mSelectedVideo=PLAY;
//                    videoView.setVideoURI(PLAY);
//                    Log.d("URI", "onCreate() called with: savedInstanceState = [" + PLAY + "]");
//                    videoView.start();

                }
            }
        });
        select_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedImage!=null&&mSelectedVideo!=null) {
                    Toast.makeText(getApplicationContext(),"正在上传",Toast.LENGTH_LONG).show();
                    postVideo();
                    post_btn.setEnabled(false);
                }
                else {
                    Toast.makeText(getApplicationContext(),"请上传视频",Toast.LENGTH_LONG).show();
                }
            }
        });




    }



    private boolean requestReadExternalStoragePermission(String explanation) {
        if (ActivityCompat.checkSelfPermission(readySendviedeo.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(readySendviedeo.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "You should grant external storage permission to continue " + explanation, Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(readySendviedeo.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, 1);
            }
            return false;
        } else {
            return true;
        }
    }


    public void chooseVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIEDEO);
    }

    public void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == PICK_VIEDEO && resultCode == RESULT_OK) {

            mSelectedVideo = intent.getData();
            Log.d("pick", "onActivityResult() called with: requestCode = [" + mSelectedVideo + "], resultCode = [" + resultCode + "], intent = [" + intent + "]");
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(mSelectedVideo);
            videoView.start();

        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            mSelectedImage = intent.getData();
            imageView.setImageURI(mSelectedImage);
            if(mSelectedVideo!=null)
            {
                videoView.setVideoURI(mSelectedVideo);
                videoView.start();


                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mPlayer) {
                        videoView.setVideoURI(mSelectedVideo);
                        videoView.start();
                    }
                });
            }


        }
    }


    @Override
    protected void onPause() {
        super.onPause();
//        {   if (outState != null)
//            outState.clear();
//    }
//           outState = new Bundle();
//
//        outState = null;
//        if(mSelectedImage!=null) {
//            outState.putString("uir_image", mSelectedImage.toString());
//        }
//        else
//
//        if(mSelectedVideo!=null) {
//            outState.putString("uir_viedeo", mSelectedVideo.toString());
//        }
//        Log.d("onpause", "onPause() called,out= "+ outState +"");
    }




    private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
        // if NullPointerException thrown, try to allow storage permission in system settings
        File f = new File(ResourceUtils.getRealPath(readySendviedeo.this, uri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
        return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
    }


    private void postVideo() {

        Log.d("aa", "xxxxx called with");

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .build();

        MultipartBody.Part body=getMultipartFromUri("cover_image",mSelectedImage);
        MultipartBody.Part body2=getMultipartFromUri("video",mSelectedVideo);

        PostVideo service = retrofit.create(PostVideo.class);
        Call<Post> call = service.createVideo("zhw","1120161991",body,body2);
        Log.d("ll", "postVideo: ");
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.d("dd", "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                Toast.makeText(getApplicationContext(),"发送成功",Toast.LENGTH_SHORT).show();
                post_btn.setEnabled(true);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

                Log.d("dd", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }




}



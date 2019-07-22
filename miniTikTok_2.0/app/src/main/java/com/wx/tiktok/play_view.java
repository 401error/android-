package com.wx.tiktok;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;

public class play_view extends AppCompatActivity {
 private MediaPlayer player;
 private SurfaceView surfaceView;
 private SurfaceHolder holder;
 private LottieAnimationView lottieAnimationView;
 private LottieAnimationView lottieAnimationView2;
 private LottieAnimationView lottieAnimationView3;

 private GestureDetector detector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_play_view);
        surfaceView=findViewById(R.id.surfaceview);
//        surfaceView.setZOrderMediaOverlay(true);
        detector=new GestureDetector(new MyGesture());
        lottieAnimationView=findViewById(R.id.likeAnimation);
        lottieAnimationView2=findViewById(R.id.loading);
        lottieAnimationView3=findViewById(R.id.stop);
        lottieAnimationView2.bringToFront();
        surfaceView.setVisibility(View.INVISIBLE);
        lottieAnimationView.pauseAnimation();
        lottieAnimationView.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        Uri uri =Uri.parse(name);

        try {
                        player=new MediaPlayer();
                        player.setDataSource(this,uri);
                        holder = surfaceView.getHolder();
                        holder.addCallback(new PlayerCallBack() {

                            @Override
                            public void surfaceDestroyed(SurfaceHolder holder) {
                                super.surfaceDestroyed(holder);
                                if (player != null) {
                                    player.stop();
                                    player.release();
                                }
                            }
                        });
                        player.prepareAsync();
                        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                surfaceView.setVisibility(View.VISIBLE);
                                lottieAnimationView2.pauseAnimation();
                                lottieAnimationView2.setVisibility(View.INVISIBLE);
                                player.start();
                                // 初始化准备c好，立刻播放
                                boolean bool=player.isPlaying();
                                Log.d("ssss", "onPrepared() called with: mp = ["+bool +"]");
                                player.setLooping(true); // 循环播放
                            }

                        });

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }
    class MyGesture extends GestureDetector.SimpleOnGestureListener{
        //按需实现自己关心的手势

//        @Override
//        public boolean onSingleTapUp(MotionEvent e) {
//            if(player!=null){
//                if(player.isPlaying()){
//                    player.pause();
//                }else{
//                    player.start();
//                }
//            }
//
//            return super.onSingleTapUp(e);
//        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.bringToFront();
            lottieAnimationView.playAnimation();
            lottieAnimationView.setSpeed(4);
            ObjectAnimator animatorLottie=ObjectAnimator.ofFloat(lottieAnimationView,"alpha",1.0f,0.0f);
            animatorLottie.setDuration(1000);
            animatorLottie.start();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.d("dj", "onSingleTapConfirmed: ");
            if(player!=null){
                if(player.isPlaying()){

                    lottieAnimationView3.setVisibility(View.VISIBLE);
                    lottieAnimationView3.bringToFront();
                    lottieAnimationView3.playAnimation();
                    player.pause();


                }else{
                    lottieAnimationView3.setVisibility(View.INVISIBLE);
                    lottieAnimationView3.pauseAnimation();
                    player.start();
                }
            }
            return super.onSingleTapConfirmed(e);
        }


    }

    @Override
    protected void onPause() {
        goMainActivity();
        super.onPause();
    }
    public void goMainActivity(){
        Intent mIntent=new Intent(play_view.this,MainActivity.class);
        startActivity(mIntent);
    }

    private class PlayerCallBack implements
                SurfaceHolder.Callback {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {

            player.setDisplay(holder);
                Log.d("creat", "surfaceCreated() called with: holder = [" + holder + "]");
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int
                    format, int width, int height) {


            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d("surface" ,"surfaceDestroyed() called with: holder = [" + holder + "]");
                if (player != null) {
                    player.stop();
                    player.release();
                }
            }

        }

}

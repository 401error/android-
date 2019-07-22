package com.wx.tiktok;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class StartLoadingActivity extends AppCompatActivity {

    private Button skipButton;
    private Intent mIntent;
    private Timer timer;
    private int i;
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch(msg.what){
                case 200:
                    skipButton.setText("跳过 : "+ i);
                    i--;
                    if (i<0){
                        //关闭定时器
                        timer.cancel();
                        //跳往主界面
                        goMainActivity();
                    }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_start_loading);
        skipButton=findViewById(R.id.skipButton);
        /**
         * 正常情况下不点击，计时3s后跳转
         */
        Countdown();

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                goMainActivity();
            }
        });
    }

    public void goMainActivity(){
        mIntent=new Intent(StartLoadingActivity.this,MainActivity.class);
        startActivity(mIntent);
        //overridePendingTransition(R.anim.fade_in,R.anim.fade_out);

        finish();
    }

    private void Countdown() {
        //初始倒计时3秒
        i = 2;
        //定时器
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //向handler发送状态值
                handler.sendEmptyMessage(200);
            }
        };
        //开启定时器，时间差值为1000毫秒
        timer.schedule(task,1,1000);
    }
}

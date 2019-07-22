package com.wx.tiktok;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.wx.tiktok.BEAN.Cat;
import com.wx.tiktok.BEAN.imageresponse;
import com.wx.tiktok.BEAN.myRecycleradatper;
import com.wx.tiktok.NET.ICatService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyFragmentAdapter myFragmentAdapter;
    private Button recordButton;
    private Intent intent;

    private RecyclerView recyclerView;
    private List<Cat>cats= new ArrayList<Cat>();
    private final int UPDATE_UI=1;
    private myRecycleradatper Recycleradatper;
    private MediaPlayer player;
    private SurfaceView playSurface;
    private SurfaceHolder holder;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        viewPager=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabLayout);
        recordButton=findViewById(R.id.recordButton);
        myFragmentAdapter=new MyFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(MainActivity.this,readySendviedeo.class);
                intent.putExtra("type",1);
                startActivity(intent);
                finish();
            }
        });


    }



    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);



    }

    private class PlayerCallBack implements
            SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {

//            player.setDisplay(holder);
            Log.d("creat", "surfaceCreated() called with: holder = [" + holder + "]");
        }
        @Override
        public void surfaceChanged(SurfaceHolder holder, int
                format, int width, int height) {
            player.setDisplay(holder);

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


    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    @Override
    public boolean releaseInstance() {
        return super.releaseInstance();
    }


}

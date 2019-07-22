package com.wx.tiktok;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wx.tiktok.BEAN.Cat;
import com.wx.tiktok.BEAN.imageresponse;
import com.wx.tiktok.BEAN.myRecycleradatper;
import com.wx.tiktok.NET.ICatService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragement extends Fragment {

    private RecyclerView recyclerView;
    private List<Cat> cats= new ArrayList<Cat>();
    private final int UPDATE_UI=1;
    private myRecycleradatper Recycleradatper;
    private SurfaceView playSurface;
    private Button button;
    private  Button button2;
    public MainPageFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_main_page_fragement, container, false);
        button=view.findViewById(R.id.recordNowBtn);
        button2=view.findViewById(R.id.refreshBtn);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycle);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {


        super.onActivityCreated(savedInstanceState);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),RecordVideoActivity.class);
                startActivity(intent);
            }
        });

        handler.sendEmptyMessage(UPDATE_UI);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(UPDATE_UI);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);
        Log.d("putin", "onCreate() called with: savedInstanceState = [" + this + "]");

        Recycleradatper = new myRecycleradatper(this.getActivity()) {
            @Override
            public void onBindViewHolder(@NonNull MviewHolder mviewHolder, int i) {
                super.onBindViewHolder(mviewHolder, i);
                if (cats.size() != 0) {
                    String url = cats.get(i).getImage_url();
//                    Log.d("xxx", "onBindViewHolder() called with: mviewHolder = [" + mviewHolder + "], i = [" + url + "]");
                    ImageView imageView = mviewHolder.getTv1();
                    TextView textView_w =mviewHolder.getTextView();
                    textView_w.setText("作者："+cats.get(i).getUser_name()+"\n"+cats.get(i).getCreatedAt());
//                    mviewHolder.S1.setVisibility(View.INVISIBLE);
//                    playSurface = mviewHolder.S1;


                    Glide.with(imageView.getContext()).load(url).into(imageView);
                }


            }


            @Override
            public int getItemCount() {

                return cats.size();
            }
        };
        Log.d("adatoer", "onCreate() called with: savedInstanceState = [" + Recycleradatper + "]");

        recyclerView.setAdapter(Recycleradatper);
        final Context context;
        context=this.getActivity();
        Recycleradatper.setOnItemClickListener(new myRecycleradatper.OnRecyclerViewItemClickListener() {
            @Override
            public void onClick(View view, myRecycleradatper.ViewName viewName, int position) {

                if (viewName == myRecycleradatper.ViewName.ITEM) {
//                    ImageView imageView = view.findViewById(R.id.image);
//                    imageView.setVisibility(View.INVISIBLE);
//                    SurfaceView surfaceView = view.findViewById(R.id.surface);
//                    surfaceView.setVisibility(View.VISIBLE);
//                    view.findViewById(R.id.button).setVisibility(View.INVISIBLE);

                    Intent intent = new Intent();
                    intent.putExtra("name", cats.get(position).getVideo_url());
                    intent.setClass(context, play_view.class);
                    startActivity(intent);


                }


            }


        });
    }



    public void requestData() {
        Log.d("dd", "requestData() called");

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://test.androidcamp.bytedance.com/")
                .build();


        ICatService service = retrofit.create(ICatService.class);
        Call<imageresponse> call = service.fechout();

        call.enqueue(new Callback<imageresponse>() {
            @Override
            public void onResponse(Call<imageresponse> call, Response<imageresponse> response) {

                cats=response.body().getFeeds();
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d("ttt", "onResponse() called with: call = [" + cats + "], response = [" + response + "]");

            }

            @Override
            public void onFailure(Call<imageresponse> call, Throwable t) {
                Log.d("mm", "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_UI:

                    requestData();
                    break;
            }
        }
    };



}

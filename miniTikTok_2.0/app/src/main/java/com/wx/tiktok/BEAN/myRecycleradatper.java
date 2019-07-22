package com.wx.tiktok.BEAN;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wx.tiktok.R;

public class myRecycleradatper extends RecyclerView.Adapter<myRecycleradatper.MviewHolder> implements View.OnClickListener {

    private Context mContext;
    private LayoutInflater inflater;

    public myRecycleradatper(Context context){
        this. mContext=context;
        inflater= LayoutInflater. from(mContext);

    }


    public class MviewHolder extends RecyclerView.ViewHolder {
        public ImageView tv1;
        public TextView textView;
        public MviewHolder(View root) {
            super(root);
            tv1 = (ImageView) root.findViewById(R.id.image);
            textView=(TextView)root.findViewById(R.id.writer) ;
//             S1 =(SurfaceView)root.findViewById(R.id.surface);
             root.setOnClickListener(myRecycleradatper.this);
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageView getTv1() {
            return tv1;
        }
    }

    @NonNull

    @Override
    public MviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = inflater.inflate(R.layout.recycle_item,viewGroup,false);
        MviewHolder mviewhodle=new MviewHolder(view);
        return mviewhodle;
    }

    @Override
    public void onBindViewHolder(@NonNull MviewHolder mviewHolder, int i) {

        mviewHolder.itemView.setTag(i);
//        mviewHolderCtv1.setText("ssss");
//        mviewHolder.tv2.setBackgroundColor(Color.RED);
//        mviewHolder.tv1.setBackgroundColor(Color.BLUE);

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    ///////////////////////////////item点击事件///////////////////////////////////////////////////////


    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    /** item里面有多个控件可以点击 */
    public enum ViewName {
        ITEM,
        PRACTISE
    }

    public interface OnRecyclerViewItemClickListener {
        void onClick(View view, ViewName viewName, int position);
    }

    @Override
    public void onClick(View v) {
        //注意这里使用getTag方法获取数据
        int position = (int) v.getTag();
        if (mOnItemClickListener != null) {
            switch (v.getId()){
//                case R.id.button:
//                    mOnItemClickListener.onClick(v, ViewName.PRACTISE, position);
//                break;
                default:
                    mOnItemClickListener.onClick(v, ViewName.ITEM, position);
                    break;
            }
        }
    }



}

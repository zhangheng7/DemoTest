package com.example.administrator.demotest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.administrator.demotest.R;

import java.util.List;

/**
 * @author zhangheng
 * @date 2018/6/26
 */

public class RecycleAdapter extends RecyclerView.Adapter {
    private List<Integer> list;
    private Context context;

    public RecycleAdapter(List<Integer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycleitem,null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).mImage1.setImageResource(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView mImage1;
//        ImageView mImage2;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImage1 = itemView.findViewById(R.id.iv_item1);
//            mImage2 = itemView.findViewById(R.id.iv_item2);
        }
    }
}

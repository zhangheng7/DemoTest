package com.example.administrator.demotest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.adapter.RecycleAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhangheng
 * @date 2018/6/26
 */

public class RecycleViewActivity extends AppCompatActivity {
    private List<Integer> list = new ArrayList<Integer>();
    private RecyclerView recyclerView;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclview);
        recyclerView = (RecyclerView) findViewById(R.id.recyclview);
        list.add(R.drawable.ic_launcher);
        list.add(R.drawable.red1);
        list.add(R.drawable.red2);
        list.add(R.drawable.white2);
        list.add(R.drawable.white3);
        list.add(R.drawable.blue_gou);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

//        recyclerView.setLayoutManager(new GridLayoutManager(this, list.size()));
        recyclerView.setHorizontalScrollBarEnabled(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new RecycleAdapter(list, this));
    }
}

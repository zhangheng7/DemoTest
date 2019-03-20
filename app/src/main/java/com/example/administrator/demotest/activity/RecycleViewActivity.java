package com.example.administrator.demotest.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.administrator.demotest.R;
import com.example.administrator.demotest.service.CodeTimerService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhangheng
 * @date 2018/6/26
 */

public class RecycleViewActivity extends BaseActivity {
    private List<Integer> list = new ArrayList<Integer>();
    private TextView text;
    private LottieAnimationView lottie;
    //    private RecyclerView recyclerView;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclview);
//        recyclerView = findViewById(R.id.recyclview);
//        list.add(R.drawable.ic_launcher);
//        list.add(R.drawable.red1);
//        list.add(R.drawable.red2);
//        list.add(R.drawable.white2);
//        list.add(R.drawable.white3);
//        list.add(R.drawable.blue_gou);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

//        recyclerView.setLayoutManager(new GridLayoutManager(this, list.size()));
//        recyclerView.setHorizontalScrollBarEnabled(true);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(new RecycleAdapter(list, this));

        text = findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(RecycleViewActivity.this, CodeTimerService.class));
            }
        });
    }

    // 注册广播
    private static IntentFilter updateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CodeTimerService.IN_RUNNING);
        intentFilter.addAction(CodeTimerService.END_RUNNING);
        return intentFilter;
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 注册广播
        registerReceiver(mUpdateReceiver, updateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 移除注册
        unregisterReceiver(mUpdateReceiver);
    }

    // 广播接收者
    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch (action) {
                case CodeTimerService.IN_RUNNING:
                    String time = intent.getStringExtra("time");
                    text.setTextColor(Color.GRAY);
                    text.setEnabled(false);
                    // 正在倒计时
                    text.setText(time + "秒");
//                    Log.e(TAG, "倒计时中(" + intent.getStringExtra("time") + ")");
                    break;
                case CodeTimerService.END_RUNNING:
                    text.setTextColor(getResources().getColor(R.color.btn_red_bg_color));
                    // 完成倒计时
                    text.setEnabled(true);
                    text.setText("停止");

                    break;
            }
        }
    };
}

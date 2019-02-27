package com.example.administrator.demotest.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.administrator.demotest.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThreadPoolActivity extends AppCompatActivity {
    private static final String TAG = "ThreadPoolActivity";
    @BindView(R.id.btn_threadpool)
    Button btnThreadpool;
    @BindView(R.id.list)
    ListView listView;
    List list;
    private ThreadPoolExecutor threadPoolExecutor;
    private ExecutorService fixedThreadPool;
    private ExecutorService cachedThreadPool;
    private ScheduledExecutorService scheduledThreadPool;
    private ExecutorService singleThreadPool;
    private ArrayAdapter<String> arrayAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            list = (ArrayList) msg.obj;
            listView.setAdapter(arrayAdapter);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_pool_activity);
        ButterKnife.bind(this);
        list = new ArrayList();
        threadPoolExecutor = new ThreadPoolExecutor(3, 5, 2, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
        //只有核心线程，线程数为核心线程数
        fixedThreadPool = Executors.newFixedThreadPool(3);
        //没有核心线程，线程数无数,并发,适合耗时短，量大的任务
        cachedThreadPool = Executors.newCachedThreadPool();
        //initialDelay（延时执行），period（间隔时间执行，（每段时间执行一次））
        scheduledThreadPool = Executors.newScheduledThreadPool(3);
        //只有一个核心线程
        singleThreadPool = Executors.newSingleThreadExecutor();
        arrayAdapter = new ArrayAdapter<String>(ThreadPoolActivity.this, android.R.layout.simple_list_item_1, list);
        arrayAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.btn_threadpool)
    public void onViewClicked() {
        list.clear();
        for (int i = 0; i < 1; i++) {
            final int index = i;
//            Runnable runnable = new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    Log.e(TAG, "线程："+threadName+",正在执行第" + index + "个任务");
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            };
//            threadPoolExecutor.execute(runnable);
//            fixedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    Log.e(TAG, "线程：" + threadName + ",正在执行第" + index + "个任务");
//                    String s = "线程：" + threadName + ",正在执行第" + index + "个任务";
//                    list.add(s);
//                    Message message = new Message();
//                    message.obj = list;
//                    handler.sendMessage(message);
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            cachedThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    Log.e(TAG, "线程：" + threadName + ",正在执行第" + index + "个任务");
//                    String s = "线程：" + threadName + ",正在执行第" + index + "个任务";
//                    list.add(s);
//                    Message message = new Message();
//                    message.obj = list;
//                    handler.sendMessage(message);
//                    try {
//                        Thread.sleep(2000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
//                @Override
//                public void run() {
//                    String threadName = Thread.currentThread().getName();
//                    Log.e(TAG, "线程：" + threadName + ",正在执行第" + index + "个任务");
//                    String s = "线程：" + threadName + ",正在执行第" + index + "个任务";
//                    list.add(s);
//                    Message message = new Message();
//                    message.obj = list;
//                    handler.sendMessage(message);
//                }
//            }, 3, 3, TimeUnit.SECONDS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}

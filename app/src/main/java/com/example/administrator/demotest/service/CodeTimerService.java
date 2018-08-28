package com.example.administrator.demotest.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * @author zhangheng
 * @date 2018/8/28
 */

public class CodeTimerService extends Service {

    private CountDownTimer downTimer;
    public static final String IN_RUNNING = "start";
    public static final String END_RUNNING = "stop";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        downTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // 广播剩余时间
                broadcastUpdate(IN_RUNNING, millisUntilFinished / 1000 + "");

            }

            @Override
            public void onFinish() {
                broadcastUpdate(END_RUNNING);
            }
        };
        downTimer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    private void broadcastUpdate(final String action, String time) {
        final Intent intent = new Intent(action);
        intent.putExtra("time", time);
        sendBroadcast(intent);
    }

    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
}

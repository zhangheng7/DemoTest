package com.example.administrator.demotest.Utils;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.Collection;

/**
 * @author zhangheng
 * @date 2018/5/31
 */

public class UIHandler {
    private static Handler sHandler;

    static {
        init();
    }

    /**
     * Must ensure this is called on UI thread.
     */
    private static void init() {
        if (sHandler == null) {
            sHandler = new Handler(Looper.getMainLooper());
        }
    }

    public static void destroy() {
        if (sHandler != null) {
            // If token is null, all callbacks and messages will be removed.
            sHandler.removeCallbacksAndMessages(null);
            sHandler = null;
        }
    }

    public static boolean nowOn() {
        CheckUtil.d(sHandler != null);
        return Looper.myLooper() == sHandler.getLooper();
    }

    public static Handler get() {
        CheckUtil.r(sHandler != null);
        return sHandler;
    }

    public static Handler noCheckGet() {
        return sHandler;
    }

    public static boolean post(Runnable r) {
        return post(r, false);
    }

    public static boolean rePost(Runnable r) {
        return post(r, false);
    }

    public static boolean post(Runnable r, boolean repost) {
        if (sHandler != null) {
            if (Looper.myLooper() == sHandler.getLooper()) {
                r.run();
            } else {
                if (repost) {
                    sHandler.removeCallbacks(r);
                }
                sHandler.post(r);
            }
            return true;
        }
        return false;
    }

    public static void postDelayed(Runnable r, long delayMillis) {
        postDelayed(r, delayMillis, false);
    }

    public static void postDelayed(Runnable r, long delayMillis, boolean repost) {
        if (sHandler != null) {
            if (repost) {
                sHandler.removeCallbacks(r);
            }
            sHandler.postAtTime(r, SystemClock.uptimeMillis() + delayMillis);
        }
    }

    public static void postAtFront(Runnable r) {
        postAtFront(r, false);
    }

    public static void postAtFront(Runnable r, boolean repost) {
        if (sHandler != null) {
            if (repost) {
                sHandler.removeCallbacks(r);
            }
            sHandler.postAtFrontOfQueue(r);
        }
    }

    public static void removeCallbacks(Runnable r) {
        if (sHandler != null) {
            sHandler.removeCallbacks(r);
        }
    }

    public static void removeCallbacks(Collection<Runnable> rs) {
        if (sHandler != null) {
            for (Runnable r : rs) {
                sHandler.removeCallbacks(r);
            }
        }
    }
}

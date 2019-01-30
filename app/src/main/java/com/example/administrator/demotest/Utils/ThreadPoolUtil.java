package com.example.administrator.demotest.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangheng
 * @date 2018/12/18
 */

public class ThreadPoolUtil {
    private static ThreadPoolUtil instance;
    private static ThreadPoolExecutor mExecutor;

    public static final int CORE_POOL_SIZE = 2;
    public static final int MAXIMUM_POOL_SIZE = 8;
    public static final int KEEP_ALIVE_TIME = 30 * 1000;

    private ThreadPoolUtil() {
        mExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        mExecutor.setCorePoolSize(CORE_POOL_SIZE);
        mExecutor.setMaximumPoolSize(MAXIMUM_POOL_SIZE);
        mExecutor.setKeepAliveTime(KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS);
    }

    public static ThreadPoolUtil getInstance() {
        if (instance == null) {
            instance = new ThreadPoolUtil();
        }
        return instance;
    }

    public ThreadPoolExecutor getExecutor() {
        return mExecutor;
    }

}

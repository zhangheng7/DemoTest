package com.example.administrator.demotest.Mvp2.base;

import android.support.annotation.NonNull;

import com.example.administrator.demotest.Mvp2.listner.HttpResponseListener;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public class BasePresenter<V extends IBaseView, T> extends BaseXPresenter<V> implements IBasePresenter, HttpResponseListener<T> {

    public BasePresenter(@NonNull V view) {
        super(view);
    }

    @Override
    public void cancel(@NonNull Object tag) {
    }

    @Override
    public void cancelAll() {
    }

    /**
     * 来自于 HttpResponseListener
     */
    @Override
    public void onSuccess(Object tag, T t) {

    }

    @Override
    public void onFailure(Object tag, HttpFailure failure) {

    }
}
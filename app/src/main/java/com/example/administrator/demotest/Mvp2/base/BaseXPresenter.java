package com.example.administrator.demotest.Mvp2.base;

import android.support.annotation.NonNull;

import com.example.administrator.demotest.Mvp2.IBaseXView;

import java.lang.ref.WeakReference;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public class BaseXPresenter <V extends IBaseXView> implements IBaseXPresenter {
    // 防止 Activity 不走 onDestory() 方法，所以采用弱引用来防止内存泄漏
    private WeakReference<V> mViewRef;

    public BaseXPresenter(@NonNull V view) {
        attachView(view);
    }

    private void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public V getView() {
        return mViewRef.get();
    }

    @Override
    public boolean isViewAttach() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
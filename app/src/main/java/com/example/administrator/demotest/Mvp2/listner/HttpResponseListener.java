package com.example.administrator.demotest.Mvp2.listner;

import com.example.administrator.demotest.Mvp2.base.HttpFailure;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public interface HttpResponseListener<T> {
    /**
     * 网络请求成功
     *
     * @param tag 请求的标记
     * @param t   返回的数据
     */
    void onSuccess(Object tag, T t);

    /**
     * 网络请求失败
     *
     * @param tag     请求的标记
     * @param failure 请求失败时，返回的信息类
     */
    void onFailure(Object tag, HttpFailure failure);
}

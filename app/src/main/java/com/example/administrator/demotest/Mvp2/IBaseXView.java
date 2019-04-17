package com.example.administrator.demotest.Mvp2;

import android.app.Activity;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public interface IBaseXView {
    /**
     * 获取 Activity 对象
     *
     * @return activity
     */
    <T extends Activity> T getActivity();
}

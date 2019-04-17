package com.example.administrator.demotest.Mvp2.base;

import com.example.administrator.demotest.Mvp2.IBaseXView;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public interface IBaseView extends IBaseXView {

    /**
     * 显示正在加载 view
     */
    void showLoading();

    /**
     * 关闭正在加载 view
     */
    void hideLoading();

    /**
     * 显示提示
     * @param msg
     */
    void showToast(String msg);
}
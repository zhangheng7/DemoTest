package com.example.administrator.demotest.Mvp2.base;

import android.app.Activity;

import com.example.administrator.demotest.Mvp2.IBaseXView;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public abstract class BaseXActivity <P extends IBaseXPresenter> extends Activity implements IBaseXView{
    private P mPresenter;

    /**
     * 创建 Presenter
     *
     * @return
     */
    public abstract P onBindPresenter();

    /**
     * 获取 Presenter 对象，在需要获取时才创建`Presenter`，起到懒加载作用
     */
    public P getPresenter() {
        if (mPresenter == null) {
            mPresenter = onBindPresenter();
        }
        return mPresenter;
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 在生命周期结束时，将 presenter 与 view 之间的联系断开，防止出现内存泄露
         */
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
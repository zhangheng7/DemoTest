package com.example.administrator.demotest.Mvp2.login;

import android.support.annotation.NonNull;

import com.example.administrator.demotest.Mvp2.base.BasePresenter;
import com.example.administrator.demotest.Mvp2.base.HttpFailure;
import com.example.administrator.demotest.Mvp2.listner.HttpResponseListener;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public class LoginPtr extends BasePresenter<LoginContacts.LoginUI, loginBean> implements LoginContacts.LoginPtr, HttpResponseListener<loginBean> {
    private LoginContacts.LoginMdl mLoginMdl;

    public LoginPtr(@NonNull LoginContacts.LoginUI view) {
        super(view);
        // 实例化 Model 层
        mLoginMdl = new LoginMdl();
    }

    @Override
    public void login(String username, String password) {
        //显示进度条
//        showProgress();
        mLoginMdl.login(username, password, this);
    }

    @Override
    public void onSuccess(Object tag, loginBean t) {
        // 先判断是否已经与 View 建立联系
        if (isViewAttach()) {
            // 隐藏进度条
//            dismissProgress();
            // 登录成功调用
            getView().loginSuccess();
        }
    }

    @Override
    public void onFailure(Object tag, HttpFailure failure) {
        if (isViewAttach()) {
            // 隐藏进度条
//            dismissProgress();
            // 登录失败调用
            getView().loginFailure();
        }
    }

}
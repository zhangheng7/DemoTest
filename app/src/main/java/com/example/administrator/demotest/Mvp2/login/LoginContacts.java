package com.example.administrator.demotest.Mvp2.login;

import com.example.administrator.demotest.Mvp2.base.IBasePresenter;
import com.example.administrator.demotest.Mvp2.base.IBaseView;
import com.example.administrator.demotest.Mvp2.listner.HttpResponseListener;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public final class LoginContacts {
    /**
     * view 层接口
     */
    public interface LoginUI extends IBaseView {
        /**
         * 登录成功
         */
        void loginSuccess();

        /**
         * 登录失败
         */
        void loginFailure();
    }

    /**
     * presenter 层接口
     */
    public interface LoginPtr extends IBasePresenter {
        void login(String username, String password);
    }

    /**
     * model 层接口
     */
    public interface LoginMdl {
        void login(String username, String password, HttpResponseListener callbak);
    }
}
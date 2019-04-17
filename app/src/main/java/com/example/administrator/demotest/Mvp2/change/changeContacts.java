package com.example.administrator.demotest.Mvp2.change;

import com.example.administrator.demotest.Mvp2.base.IBasePresenter;
import com.example.administrator.demotest.Mvp2.base.IBaseView;
import com.example.administrator.demotest.Mvp2.listner.HttpResponseListener;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public final class changeContacts {
    public interface changeUI extends IBaseView {
        void changeSuccess(String na);

        void changeFailure();
    }

    public interface changeptr extends IBasePresenter {
        void change(String name);
    }

    public interface changemdl {
        void change(String username,HttpResponseListener callbak);
    }
}

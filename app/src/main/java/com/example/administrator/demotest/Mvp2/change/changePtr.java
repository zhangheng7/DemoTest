package com.example.administrator.demotest.Mvp2.change;

import android.support.annotation.NonNull;

import com.example.administrator.demotest.Mvp2.base.BasePresenter;
import com.example.administrator.demotest.Mvp2.base.HttpFailure;
import com.example.administrator.demotest.Mvp2.listner.HttpResponseListener;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public class changePtr extends BasePresenter<changeContacts.changeUI, changeBean> implements changeContacts.changeptr, HttpResponseListener<changeBean> {

    private final changeContacts.changemdl model;

    public changePtr(@NonNull changeContacts.changeUI view) {
        super(view);
        model = new changeModel();
    }

    @Override
    public void change(String name) {
        model.change(name, this);
    }

    @Override
    public void onSuccess(Object tag, changeBean changeBean) {
        if (isViewAttach()) {
            getView().changeSuccess("张吱吱吱吱吱吱吱吱");
        }
    }

    @Override
    public void onFailure(Object tag, HttpFailure failure) {
        if (isViewAttach()) {
            getView().changeFailure();
        }
    }
}

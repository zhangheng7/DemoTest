package com.example.administrator.demotest.Mvp2.login;

import com.example.administrator.demotest.Mvp2.base.BaseModel;
import com.example.administrator.demotest.Mvp2.listner.HttpResponseListener;
import com.example.administrator.demotest.Utils.MD5Util;

import java.util.HashMap;


/**
 * @author zhangheng
 * @date 2019/3/21
 */

public class LoginMdl extends BaseModel implements LoginContacts.LoginMdl{

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @param callbak  网络请求回调
     */
    @Override
    public void login(String username, String password, HttpResponseListener callbak) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", MD5Util.encrypBy(password));
        //这里请求网络
        callbak.onSuccess("login",new loginBean());
    }

}
package com.example.administrator.demotest.Mvp2.change;

import com.example.administrator.demotest.Mvp2.base.BaseModel;
import com.example.administrator.demotest.Mvp2.listner.HttpResponseListener;
import com.example.administrator.demotest.Utils.MD5Util;

import java.util.HashMap;

/**
 * @author zhangheng
 * @date 2019/3/21
 */

public class changeModel extends BaseModel implements changeContacts.changemdl {
    @Override
    public void change(String username, HttpResponseListener callbak) {
        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        //网络请求
        callbak.onSuccess("1",new changeBean());
    }
}

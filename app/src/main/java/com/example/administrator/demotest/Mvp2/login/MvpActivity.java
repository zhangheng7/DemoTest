package com.example.administrator.demotest.Mvp2.login;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.demotest.Mvp2.change.changeContacts;
import com.example.administrator.demotest.Mvp2.change.changePtr;
import com.example.administrator.demotest.R;
import com.example.administrator.demotest.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MvpActivity extends BaseActivity<LoginContacts.LoginPtr> implements LoginContacts.LoginUI {

    @BindView(R.id.top_contributor)
    TextView topContributor;
    @BindView(R.id.container)
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        ButterKnife.bind(this);
    }

    public void get(View view) {
        getPresenter().login("admin", "123456");
    }

    public void change(View view) {
//        getPresenter().change("1");
    }

//    @Override
//    public changeContacts.changeptr onBindPresenter() {
//        return new changePtr(this);
//    }

//    @Override
//    public void changeSuccess(String na) {
//        topContributor.setText(na);
//    }
//
//    @Override
//    public void changeFailure() {
//
//    }


    @Override
    public void loginSuccess() {
        Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public LoginContacts.LoginPtr onBindPresenter() {
        return new LoginPtr(this);
    }

    @Override
    public void loginFailure() {
        Toast.makeText(this, "登录失败", Toast.LENGTH_LONG).show();
    }

}

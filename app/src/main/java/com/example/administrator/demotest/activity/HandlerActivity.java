package com.example.administrator.demotest.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HandlerActivity extends AppCompatActivity {

    @BindView(R.id.tv_message)
    TextView mTvMessage;
    @BindView(R.id.edt_message)
    EditText mEdtMessage;
    @BindView(R.id.btn_sendMessage)
    Button mBtnSendMessage;
    private ToastUtil toastUtil;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                mTvMessage.setText((String) msg.obj);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        ButterKnife.bind(this);
        toastUtil = new ToastUtil(this);
    }

    @OnClick(R.id.btn_sendMessage)
    public void onViewClicked() {
        Message obtainMessage = handler.obtainMessage();
        obtainMessage.what = 1;
        obtainMessage.obj = mEdtMessage.getText().toString();
        handler.sendMessage(obtainMessage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}

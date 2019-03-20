package com.example.administrator.demotest.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.btn_wu)
    Button btnWu;
    @BindView(R.id.btn_you)
    Button btnYou;
    @BindView(R.id.webview)
    WebView mWebview;
    private ToastUtil toastUtil;
    String name = "张张张张张张张张张张";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        toastUtil = new ToastUtil(this);
        ButterKnife.bind(this);
        WebSettings webSettings = mWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebview.loadUrl("file:///android_asset/test.html");
        mWebview.addJavascriptInterface(this, "android");
    }

    @OnClick({R.id.btn_wu, R.id.btn_you})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wu:
                mWebview.loadUrl("javascript:Androidwucan()");
                break;
            case R.id.btn_you:
                mWebview.loadUrl("javascript:Androidyoucan('" + name + "')");
                break;
        }
    }

    @JavascriptInterface
    public void setMessage() {
        toastUtil.showToast("我弹");
    }

    @JavascriptInterface
    public void setMessage(String name) {
        toastUtil.showToast("我弹弹" + name);
    }
}

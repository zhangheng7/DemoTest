package com.example.administrator.demotest.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.Utils;


/**
 * 自定义Loading对话框
 */
public class HttpLoadingDialog extends ProgressDialog {

    private int mResid;
    /**
     *  GIF动态图片
     */
    private GifView load_gifv; // 广发logo gif图片
    private Context context;
    private TextView mTvProgress; // 文本内容

    /**
     * loading对话框构造方法
     *
     * @param context 上下文
     */
    public HttpLoadingDialog(Context context) {
        super(context, R.style.khqy_http_loading_dialog_style);
        this.context = context;
        this.mResid = R.raw.khqy_loading;
        setCanceledOnTouchOutside(false);//界外不可取消
        setCancelable(false);//不可取消
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        initView();
    }

    @Override
    public void onStart() {
        super.onStart();
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * 页面初始化
     */
    private void initView() {
        load_gifv = (GifView) findViewById(R.id.load_gifv);
        load_gifv.setMovieResource(mResid);
        mTvProgress = (TextView) findViewById(R.id.load_txt_tv);
    }

    /**
     * 设置更新进度
     */
    public void setUpdateTip(String strTip) {
        mTvProgress.setText(strTip);
        mTvProgress.invalidate();
    }

    @Override
    public void show() {
        if (Utils.checkActivityFinish(context)) {
            super.show();
        }
    }
}

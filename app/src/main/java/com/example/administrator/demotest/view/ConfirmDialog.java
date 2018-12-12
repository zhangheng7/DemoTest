package com.example.administrator.demotest.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.DensityUtil;

/**
 * @author zhangheng
 * @date 2018/7/5
 */

public class ConfirmDialog extends BaseDialog {

    private static ConfirmDialog mConfirmDialog;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
    }

    public ConfirmDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ConfirmDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context mContext;
        private String mTitle;
        private String mMessage;
        private String mPositiveText;
        private String mNegativeText;
        private OnClickListener mPositeveClickListner;
        private OnClickListener mNegativeClickListner;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(String mTitle) {
            this.mTitle = mTitle;
            return this;
        }

        public Builder setMessage(String mMessage) {
            this.mMessage = mMessage;
            return this;
        }

        //确认按钮
        public Builder setPositiveButton(String text, OnClickListener mPositeveClickListner) {
            this.mPositiveText = text;
            this.mPositeveClickListner = mPositeveClickListner;
            return this;
        }

        //取消按钮
        public Builder setNegativeButton(String text, OnClickListener mPositeveClickListner) {
            this.mNegativeText = text;
            this.mNegativeClickListner = mPositeveClickListner;
            return this;
        }

        public ConfirmDialog build() {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mConfirmDialog = new ConfirmDialog(mContext, R.style.dialog);
            //设置点击dialog外，不能取消dialog
            mConfirmDialog.setCanceledOnTouchOutside(false);
            //读取布局
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            initConfirmDialog(view, mConfirmDialog);
            //给dialog设置为充满
            mConfirmDialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //将加载出的布局设置到dialog上；
            mConfirmDialog.setContentView(view);
            Window mDialogWindow = mConfirmDialog.getWindow();
            WindowManager.LayoutParams lp = mDialogWindow.getAttributes();
            int width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.8);
            lp.width = width;
            mDialogWindow.setAttributes(lp);

            return mConfirmDialog;
        }

        public void initConfirmDialog(View view, final ConfirmDialog dialog) {
            TextView message = view.findViewById(R.id.content);
            TextView title = view.findViewById(R.id.title);
            TextView btn_ok = view.findViewById(R.id.submit);
            TextView btn_cancel = view.findViewById(R.id.cancel);
            //如果message不为空，
            if (!TextUtils.isEmpty(mMessage)) {
                message.setText(mMessage);
            } else {
                //为空的话
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, DensityUtil.dp2px(mContext, 30), 0, DensityUtil.dp2px(mContext, 30));//四个参数分别是左上右下
                layoutParams.gravity = Gravity.CENTER;
                title.setLayoutParams(layoutParams);
                message.setVisibility(View.GONE);
            }
            //如果title不为空
            if (!TextUtils.isEmpty(mTitle)) {
                title.setText(mTitle);
            } else {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, DensityUtil.dp2px(mContext, 30), 0, DensityUtil.dp2px(mContext, 30));//四个参数分别是左上右下
                layoutParams.gravity = Gravity.CENTER;
                message.setLayoutParams(layoutParams);
                title.setVisibility(View.GONE);
            }

            //如果确定按钮为空，就用默认值为“确定”
            if (TextUtils.isEmpty(mPositiveText)) {
                mPositiveText = "确定";
            }
            btn_ok.setText(mPositiveText);

            //如果取消按钮为空，就用默认值为“取消”
            if (TextUtils.isEmpty(mNegativeText)) {
                mNegativeText = "取消";
            }
            btn_cancel.setText(mNegativeText);
            if (mPositeveClickListner != null) {
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPositeveClickListner.onClick(dialog, BUTTON_POSITIVE);
                    }
                });
            }
            if (mNegativeClickListner != null) {
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mNegativeClickListner.onClick(dialog, BUTTON_NEGATIVE);
                    }
                });
            }
        }
    }

    @Override
    public void show() {
        if (mConfirmDialog.isShowing()) {
            mConfirmDialog.dismiss();
        }
        super.show();
    }
}

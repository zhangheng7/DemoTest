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
 * @date 2018/7/13
 */

public class OnButtonDialog extends BaseDialog {
    public OnButtonDialog(@NonNull Context context) {
        super(context);
    }

    public OnButtonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected OnButtonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context mContext;
        private String mMessage;
        private String mTitle;
        private String okText;
        private OnClickListener okClickListner;

        public Builder(Context mContext) {
            this.mContext = mContext;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setOkButton(String okText, OnClickListener okClickListner) {
            this.okText = okText;
            this.okClickListner = okClickListner;
            return this;
        }

        public OnButtonDialog build() {
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final OnButtonDialog dialog = new OnButtonDialog(mContext, R.style.dialog);
            dialog.setCanceledOnTouchOutside(false);
            View view = mInflater.inflate(R.layout.alert_dialog, null);
            TextView titleTv = (TextView) view.findViewById(R.id.title);
            final TextView messageTv = (TextView) view.findViewById(R.id.content);
            TextView okTv = (TextView) view.findViewById(R.id.ok_tv);
            if (!TextUtils.isEmpty(mTitle)) {
                titleTv.setText(mTitle);
            } else {
                titleTv.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //4个参数按顺序分别是左上右下
                layoutParams.setMargins(0, DensityUtil.dp2px(mContext, 30), 0, DensityUtil.dp2px(mContext, 30));
                messageTv.setLayoutParams(layoutParams);

            }
            if (!TextUtils.isEmpty(mMessage)) {
                //显示的内容
                messageTv.setText(mMessage);
            } else {
                messageTv.setVisibility(View.GONE);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                //4个参数按顺序分别是左上右下
                layoutParams.setMargins(0, DensityUtil.dp2px(mContext, 30), 0, DensityUtil.dp2px(mContext, 30));
                titleTv.setLayoutParams(layoutParams);

            }
            if (!TextUtils.isEmpty(okText)) {
                okTv.setText(okText);
                okTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        okClickListner.onClick(dialog, BUTTON_NEUTRAL);
                    }
                });
            } else {
                okTv.setText(R.string.cancel);
                okTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
            dialog.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.setContentView(view);
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = DensityUtil.dp2px(mContext, 295);
            lp.gravity = Gravity.CENTER;
            dialogWindow.setAttributes(lp);

            return dialog;
        }
    }
}

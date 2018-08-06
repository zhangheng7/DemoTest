package com.example.administrator.demotest.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.demotest.R;

/**
 * @author zhangheng
 * @date 2018/6/14
 */
public class ToastUtil {
    public static final int TOAST_ICON_SUCCESS = 1;
    public static final int TOAST_ICON_FAIL = 2;

    Context context;

    private Toast toast;

    public ToastUtil(Context context) {
        this.context = context;
    }

    public void showToast(String content) {
        showToast(content, 0, "", "", 200, 2000);
    }

    public void showToast(int resId) {
        showToast(context.getString(resId), 0, "", "", 200, 2000);
    }

    public void showToast(int resId, int status) {
        showToast(context.getString(resId), status);
    }

    public void showToast(String content, int status) {
        int iconRes = R.drawable.ic_toast_succes;
        switch (status) {
            case TOAST_ICON_SUCCESS:
                iconRes = R.drawable.ic_toast_succes;
                break;
            case TOAST_ICON_FAIL:
                iconRes = R.mipmap.ic_toast_fail;
                break;
            default:
                break;
        }
        showToast(content, iconRes, "", "", 200, 2000);
    }

    public void showToast(final String content, int iconRes, String iconUrl, final String postion, int alpha, final int duration) {
        View view = null;
        if (0 != iconRes || !TextUtils.isEmpty(iconUrl)) {
            view = LayoutInflater.from(context).inflate(R.layout.view_h5_toast_icon, null);
            ImageView toast_icon = (ImageView) view.findViewById(R.id.toast_icon);
            if (0 != iconRes) {
                toast_icon.setImageResource(iconRes);
                toast_icon.setVisibility(View.VISIBLE);
            } else {
                toast_icon.setVisibility(View.GONE);
            }
        } else {
            view =  LayoutInflater.from(context).inflate(R.layout.view_h5_toast_text, null);
        }

        if (view == null)
            return;

        final TextView toast_text = (TextView) view.findViewById(R.id.toast_text);
        if (TextUtils.isEmpty(content)) {
            toast_text.setVisibility(View.GONE);
        } else {
            toast_text.setText(content);
            toast_text.setVisibility(View.VISIBLE);
        }
        view.getBackground().setAlpha(alpha);

        final View finalView = view;
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                if(toast == null){
                    toast = new Toast(context);
                }
                if ("top".equals(postion)) {
                    toast.setGravity(Gravity.TOP, 0, ScreenUtil.getDisplayHeight(context) / 4);
                } else if ("center".equals(postion)) {
                    toast.setGravity(Gravity.CENTER, 0, 0);
                } else if ("bottom".equals(postion)) {

                }
                toast.setDuration(duration);
                toast.setView(finalView);
                toast.show();
            }
        });
    }

    public void hideToast() {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        });
    }

    public void ShareToast(String content) {
        View view = LayoutInflater.from(context).inflate(R.layout.toastshare, null);
        TextView toast_text = (TextView) view.findViewById(R.id.share_toast_text);
        toast_text.setText(content);
        toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}

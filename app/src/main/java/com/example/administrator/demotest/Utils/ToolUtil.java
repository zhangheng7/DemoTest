package com.example.administrator.demotest.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;

import com.example.administrator.demotest.view.HttpLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 常用工具类
 */
public class ToolUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 整型安全转换
     *
     * @param object       转换对象
     * @param defaultValue 默认返回值
     * @return
     */
    public static int convertToInt(Object object, int defaultValue) {
        if (null == object || "".equals(object.toString().trim())) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(object.toString());
        } catch (Exception e) {
            try {
                return Double.valueOf(object.toString()).intValue();
            } catch (Exception e2) {
                return defaultValue;
            }
        }
    }

    /**
     * 隐藏进度条
     */
    public static void hideWaiting(Dialog dialog) {
        try {
            if (dialog != null) {
                dialog.cancel();
                dialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示进度条
     *
     * @param activity
     */
    public static Dialog showLoading(Activity activity) {
        HttpLoadingDialog mWaitingDlg = new HttpLoadingDialog(activity);
        mWaitingDlg.getWindow().setBackgroundDrawable(new BitmapDrawable());
        mWaitingDlg.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (!mWaitingDlg.isShowing()) {
            mWaitingDlg.show();
        }
        return mWaitingDlg;
    }

    /**
     * 显示进度条
     *
     * @param activity
     */
    public static Dialog showWaiting(Activity activity) {
        HttpLoadingDialog mWaitingDlg = new HttpLoadingDialog(activity);
        mWaitingDlg.getWindow().setBackgroundDrawable(
                new BitmapDrawable());
        mWaitingDlg.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        if (!mWaitingDlg.isShowing()) {
            mWaitingDlg.show();
        }
        return mWaitingDlg;
    }

    /**
     * 日期格式化
     *
     * @param time
     * @return 20160622
     */
    public static String millisToDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(time);
        String result = null;
        result = formatter.format(date);
        return result;
    }

    /**
     * 给JS发送消息
     *
     * @param actionName
     * @param str
     */
    public static void sendToJS(String actionName, String str) {
        JSONObject toJsonObject = new JSONObject();
        try {
            toJsonObject.put("value", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}

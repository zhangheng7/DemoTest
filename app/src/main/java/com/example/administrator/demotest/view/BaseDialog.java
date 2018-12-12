package com.example.administrator.demotest.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.demotest.Utils.UIHandler;
import com.example.administrator.demotest.Utils.Utils;

/**
 * @author zhangheng
 * @date 2018/11/29
 */

public class BaseDialog extends Dialog {
    private static final String TAG = "BaseDialog";
    private Context mContext;

    public BaseDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    public void show() {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (Utils.checkActivityFinish(mContext)) {
                    try {
                        BaseDialog.super.show();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }

    @Override
    public void dismiss() {
        UIHandler.post(new Runnable() {
            @Override
            public void run() {
                if (Utils.checkActivityFinish(mContext)) {
                    try {
                        BaseDialog.super.dismiss();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }
        });
    }
}

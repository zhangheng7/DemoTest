package com.example.administrator.demotest.view;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;

import java.util.Timer;


import static android.hardware.fingerprint.FingerprintManager.FINGERPRINT_ERROR_LOCKOUT;

/**
 * @author zhangheng
 * @date 2018/7/15
 */
public class FingerPrinterDialog extends BaseDialog {

    private String title;
    private String message;
    private String leftButton;
    private String rightButton;
    private Context context;

    public void setOnClickListener(OnclickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLeftButton(String leftButton) {
        this.leftButton = leftButton;
    }

    public void setRightButton(String rightButton) {
        this.rightButton = rightButton;
    }

    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private ToastUtil toastUtil;
    private Button btnLeft;
    private Button btnRight;
    private TextView messageView;
    private TextView titleView;
    private CancellationSignal cancellationSignal;
    private int i = 0;
    private FingerPrinterDialog.OnclickListener onClickListener;
    private View line;
    int seconds = 30;
    private Timer timer;

    public FingerPrinterDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
        toastUtil = new ToastUtil(context);
        keyguardManager = ((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = ((FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE));
        }

        cancellationSignal = new CancellationSignal();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint_dialog_two);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

//        title = context.getString(R.string.please_check_finger);
//        message = context.getString(R.string.my_touchId);
//        leftButton = context.getString(R.string.cancel);
//        rightButton = context.getString(R.string.write_password);

        btnRight = (Button) findViewById(R.id.right);
        btnLeft = (Button) findViewById(R.id.left);
        line = findViewById(R.id.finger_dialog_line);

        titleView = (TextView) findViewById(R.id.title);
        if (!TextUtils.isEmpty(title)) {
            titleView.setText(title);
        }

        messageView = (TextView) findViewById(R.id.message);
        if (!TextUtils.isEmpty(message)) {
            messageView.setText(message);
        }

        if (!TextUtils.isEmpty(leftButton)) {
            btnLeft.setText(leftButton);
        }
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canelFinger();
                if (onClickListener != null) {
                    onClickListener.left();
                }
            }
        });
        if (!TextUtils.isEmpty(rightButton)) {
            btnRight.setText(rightButton);
        }
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.right();
                }
            }
        });
    }

    @Override
    public void show() {
        if (check()) {
            super.show();
        }
    }

    public boolean check() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return false;
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            if (onClickListener != null) {
                onClickListener.onNoSettingFingerprint();
            }
            return false;
        }

        //回调方法
        FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                if (onClickListener != null) {
                    onClickListener.onAuthenticationSucceeded();
                    i = 0;
                }
            }

            @Override
            public void onAuthenticationFailed() {
                i++;
                //指纹登录-再试一次弹窗-取消
                titleView.setText(R.string.try_again);
                if (i >= 1) {
                    messageView.setText(R.string.my_touchId);
                    btnRight.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    btnRight.setText(R.string.use_password);
                    btnRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //指纹登录-再试一次弹窗-手势登录
                            toastUtil.showToast("别的方式登录");
//                            if (context instanceof Activity) {
//                                Activity activity = (Activity) context;
//                                activity.finish();
//                                i = 0;
//                            }
                        }
                    });
                }
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                if (errorCode == FINGERPRINT_ERROR_LOCKOUT) {
                    if (onClickListener != null) {
                        onClickListener.onAuthenticationError();
                    }
                }

            }

            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                if (i == 5 && btnRight != null) {
                    btnRight.setVisibility(View.VISIBLE);
                }

                if (onClickListener != null) {
                    onClickListener.onAuthenticationHelp();
                }
            }
        };

        fingerprintManager.authenticate(null, cancellationSignal, 0, mSelfCancelled, null);
        return true;
    }


    public void canelFinger() {
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }

    public interface OnclickListener {
        void left();

        void right();

        void onAuthenticationSucceeded();

        void onAuthenticationFailed();

        void onAuthenticationHelp();

        void onAuthenticationError();

        void onNoSettingScreenPassword();

        void onNoSettingFingerprint();

        void onLockScreen();


    }
}

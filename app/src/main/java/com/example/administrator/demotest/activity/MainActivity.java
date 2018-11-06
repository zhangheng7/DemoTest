package com.example.administrator.demotest.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;
import com.example.administrator.demotest.Utils.UIHandler;
import com.example.administrator.demotest.view.ConfirmDialog;
import com.example.administrator.demotest.view.FingerPrinterDialog;
import com.example.administrator.demotest.view.OnButtonDialog;
import com.example.administrator.demotest.view.UPMarqueeView;
import com.google.android.gms.common.api.GoogleApiClient;


import java.util.ArrayList;
import java.util.List;

import networklistner.NetWorkChangReceiver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION = 0;

    List<String> data = new ArrayList<>();
    List<View> views = new ArrayList<>();

    private UPMarqueeView upMarqueeView;
    private Button mBtnClickToToSetting;
    private Button mBtnClickToRefresh;
    private Button mClickToNotifation;
    private Button mClickToMapView;
    private Button mClickToBanner;
    private GoogleApiClient client;
    private ToastUtil toastUtil;
    private Button mClickToAlbum;
    private Button mClickToAlert;
    private Dialog dialog;
    private Button mClickFinger;
    private Button mClickAnnnotations;
    private FingerPrinterDialog fingerPrinterDialog;
    //监听网络变化
    private boolean isRegistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //测试
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明或者半透明
            translucentStatusBar(this, false);

//            setStatusBarColor(this,R.color.white);
        }
        toastUtil = new ToastUtil(this);
        initParam();
        initData();
        initView();
        initClick();
        initBroadCast();
    }

    private void initBroadCast() {
        //注册网络状态监听广播
        netWorkChangReceiver = new NetWorkChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
        isRegistered = true;
    }

    private void initClick() {
        upMarqueeView.setOnItemClickListener(new UPMarqueeView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
            }
        });
    }

    //实例化控件

    private void initParam() {
        upMarqueeView = findViewById(R.id.marqueeView);
        mBtnClickToToSetting = findViewById(R.id.click_to_toSetting);
        mBtnClickToRefresh = findViewById(R.id.click_to_refresh);
        mClickToNotifation = findViewById(R.id.click_to_notifation);
        mClickToMapView = findViewById(R.id.click_to_map);
        mClickToAlbum = findViewById(R.id.click_to_xiangce);
        mClickToAlert = findViewById(R.id.click_to_alert);
        mClickFinger = findViewById(R.id.click_finger);
        mClickAnnnotations = findViewById(R.id.click_annotations);
        mClickToBanner = findViewById(R.id.click_to_banner);

        mBtnClickToToSetting.setOnClickListener(this);
        mBtnClickToRefresh.setOnClickListener(this);
        mClickToNotifation.setOnClickListener(this);
        mClickToMapView.setOnClickListener(this);
        mClickToAlbum.setOnClickListener(this);
        mClickToAlert.setOnClickListener(this);
        mClickFinger.setOnClickListener(this);
        mClickAnnnotations.setOnClickListener(this);
        mClickToBanner.setOnClickListener(this);
    }

    @RequiresApi(api = 26)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.click_to_toSetting:
                startActivity(new Intent(MainActivity.this, JumpToSettingActivity.class));
                break;
            case R.id.click_to_refresh:
                startActivity(new Intent(MainActivity.this, RecycleViewActivity.class));
                break;
            case R.id.click_to_notifation:
                showNotifation();
                toastUtil.showToast("标题标题标题标题标题标题");
//                RZAlbum.ofAppName("选择照片")
//                        .setLimitCount(9)
//                        .setSpanCount(3)
//                        .setPreviewOrientation(RZAlbum.ORIENTATION_PORTRATI)
//                        .showCamera(true)
//                        .start(this, 88);
                break;
            case R.id.click_to_map:
                if (checkPermession()) {
                    startActivity(new Intent(MainActivity.this, MapActivity.class));
                }
                break;
            case R.id.click_to_xiangce:
                startActivity(new Intent(MainActivity.this, AlbumActivity.class));
                break;
            case R.id.click_to_alert:
                dialog = new ConfirmDialog.Builder(this)
                        .setTitle("弹个框？")
                        .setMessage("弹个框弹个框弹个框弹个框弹个框弹个框弹个框弹个框弹个框")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                toastUtil.showToast("弹个框弹个框弹个框弹个框弹个框");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                dialog.show();
                break;
            case R.id.click_finger:
                fingerPrinterDialog = new FingerPrinterDialog(this);
                fingerPrinterDialog.setOnClickListener(new FingerPrinterDialog.OnclickListener() {
                    @Override
                    public void left() {
                        Log.e(TAG, "left: 弹框取消");
                        fingerPrinterDialog.dismiss();
                    }

                    @Override
                    public void right() {
                        Log.e(TAG, "right: ");
                    }

                    @Override
                    public void onAuthenticationSucceeded() {
                        Log.e(TAG, "onAuthenticationSucceeded: 验证成功");
                        UIHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fingerPrinterDialog.dismiss();
                                toastUtil.showToast("验证成功");
                            }
                        }, 200);

                    }

                    @Override
                    public void onAuthenticationFailed() {
                        Log.e(TAG, "onAuthenticationFailed: 验证失败");
                        toastUtil.showToast("失败");
                    }

                    @Override
                    public void onAuthenticationHelp() {
                        Log.e(TAG, "onAuthenticationHelp: 、");

                    }

                    @Override
                    public void onAuthenticationError() {
                        Log.e(TAG, "onAuthenticationError: ");
                        fingerPrinterDialog.dismiss();
                        dialog = new OnButtonDialog.Builder(MainActivity.this)
                                .setMessage(getString(R.string.fingertimes_out))
                                .setOkButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        toastUtil.showToast(R.string.fingertimes_out);
                                    }
                                })
                                .build();
                        dialog.show();
                    }

                    @Override
                    public void onNoSettingScreenPassword() {
                        Log.e(TAG, "onNoSettingScreenPassword: ");
                    }

                    @Override
                    public void onNoSettingFingerprint() {
                        Log.e(TAG, "onNoSettingFingerprint: ");
                    }

                    @Override
                    public void onLockScreen() {
                        Log.e(TAG, "onLockScreen: ");
                    }
                });
                fingerPrinterDialog.show();
                break;
            case R.id.click_annotations:
                startActivity(new Intent(this, AnnotationsActivity.class));
                break;
            case R.id.click_to_banner:
                startActivity(new Intent(this, BannerActivity.class));
                break;
            default:
                break;
        }
    }

    // 初始化界面程序

    private void initView() {
        setView();
        upMarqueeView.setViews(views);
    }

    //初始化需要循环的View
    // 为了灵活的使用滚动的View，所以把滚动的内容让用户自定义
    //  假如滚动的是三条或者一条，或者是其他，只需要把对应的布局，和这个方法稍微改改就可以了，
    private void setView() {
        for (int i = 0; i < data.size(); i = i + 2) {
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item, null);
            TextView tv1 = moreView.findViewById(R.id.tv1);
//            TextView tv2 = moreView.findViewById(R.id.tv2);
            tv1.setText(data.get(i).toString());
            if (data.size() > i + 1) {
                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
//                tv2.setText(data.get(i + 1).toString());
            } else {
//                tv2.setVisibility(View.GONE);
            }//添加到循环滚动数组里面去
            views.add(moreView);
        }
    }

    //初始化数据

    private void initData() {
        data = new ArrayList<>();
        data.add("家人给2岁孩子喝这个，孩子智力倒退10岁!!!");
        data.add("iPhone8最感人变化成真，必须买买买买!!!!");
        data.add("简直是白菜价！日本玩家33万甩卖15万张游戏王卡");
        data.add("iPhone7价格曝光了！看完感觉我的腰子有点疼...");
        data.add("主人内疚逃命时没带够，回废墟狂挖30小时！");
    }

    @RequiresApi(api = 26)
    private void showNotifation() {
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= 26) {
            channel = new NotificationChannel("1", "AndroidOChannel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.RED); //小红点颜色
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        channel.setShowBadge(true);

        int notificationId = 0x1234;

        ApplicationInfo info = getApplicationInfo();
        int resId = getResources().getIdentifier("ic_launcher", "drawable", info.packageName);
        Intent intent = new Intent(MainActivity.this, JumpToSettingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this, "1")
                .setSmallIcon(resId)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle("我是标题")
                .setContentText("我是内容我是内容我是内容我是内容")
                .setNumber(3)
                .setTicker("听说有动画？")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setGroup("group")
                .setAutoCancel(true);

        Notification notification = builder.build();
        notificationManager.createNotificationChannel(channel);
        notificationManager.notify(notificationId, notification);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏为透明
            window.setStatusBarColor(Color.TRANSPARENT);
            //设置window的状态栏不可见
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        //view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        //解绑监听网络
        if (isRegistered) {
            unregisterReceiver(netWorkChangReceiver);
        }
    }

    private boolean checkPermession() {
        boolean locatonPermession = this.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (!locatonPermession) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermission();
            }
            return false;
        }
        return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                        /*Manifest.permission.WRITE_EXTERNAL_STORAGE,*/
//                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,},
                REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                dialog = new ConfirmDialog.Builder(this)
                        .setMessage("啊哦，权限被拒绝了，请点击确定开启权限")
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .build();
                dialog.show();
            } else {
                startActivity(new Intent(this, MapActivity.class));
            }
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == 88) {
//            List<String> result = RZAlbum.parseResult(data);
//           dialog =  new OnButtonDialog.Builder(this)
//                    .setMessage(result.toString())
//                   .setOkButton("确定", new DialogInterface.OnClickListener() {
//                       @Override
//                       public void onClick(DialogInterface dialog, int which) {
//                           dialog.dismiss();
//                       }
//                   })
//                   .build();
//           dialog.show();
//        }
//    }

}

package com.example.administrator.demotest.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.demotest.BuildConfig;
import com.example.administrator.demotest.Mvp2.login.MvpActivity;
import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.DensityUtil;
import com.example.administrator.demotest.Utils.ToastUtil;
import com.example.administrator.demotest.Utils.UIHandler;
import com.example.administrator.demotest.view.ConfirmDialog;
import com.example.administrator.demotest.view.FingerPrinterDialog;
import com.example.administrator.demotest.view.MissionSquarePopWindow;
import com.example.administrator.demotest.view.OneButtonDialog;
import com.example.administrator.demotest.view.UPMarqueeView;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.MoveType;
import com.yhao.floatwindow.PermissionListener;


import java.util.ArrayList;
import java.util.List;

import networklistner.NetWorkChangReceiver;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static String TAG = "MainActivity";
    private static final int REQUEST_PERMISSION = 0;
    private static final int CAMERA_REQUIRE_STORAGE_PERMISSION = 10000;

    List<String> data = new ArrayList<>();
    List<View> views = new ArrayList<>();

    private UPMarqueeView upMarqueeView;
    private Button mBtnClickToToSetting;
    private Button mBtnClickToRefresh;
    private Button mClickToNotifation;
    private Button mClickToMapView;
    private Button mClickToBanner;
    private ToastUtil toastUtil;
    private Button mClickToAlbum;
    private Button mClickToAlert;
    private Dialog dialog;
    private Button mClickFinger;
    private Button mClickAnnnotations;
    private Button mClickSelectCity;
    private Button mClickHandler;
    private Button mClickThreadPool;
    private Button mClickBRVAH;
    private Button mClickWebview;
    private Button mClickMvp;
    private Button mClickPop;
    private Button mClickWaterMarke;
    private FingerPrinterDialog fingerPrinterDialog;
    //监听网络变化
    private boolean isRegistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;
    private long exitTime;
    private int maxExitTime = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        //测试
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //透明或者半透明
//            translucentStatusBar(this, false);
//
////            setStatusBarColor(this,R.color.white);
//        }
        toastUtil = new ToastUtil(this);
        initParam();
        initData();
        initView();
        initClick();
        initBroadCast();
//        initFloatWindow();
        String serverHost = BuildConfig.SERVER_HOST;
        Log.e("serverHost", serverHost);
    }


    private void initFloatWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.phone_full, null);
        FloatWindow
                .with(getApplicationContext())
                .setView(view)
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setX(RelativeLayout.ALIGN_PARENT_RIGHT)
                .setY(120)
                .setMoveType(MoveType.slide)
                .setPermissionListener(mPermissionListener)
                .build();
//        startActivity(new Intent(this,JumpToSettingActivity.class));
//        TextView rightButton = (TextView) view.findViewById(R.id.submit);
//        rightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toastUtil.showToast("123");
//            }
//        });
//        TextView leftButton = (TextView) view.findViewById(R.id.cancel);
//        leftButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toastUtil.showToast("456");
//                FloatWindow.get().hide();
//            }
//        });


    }

    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSuccess() {
            Log.d(TAG, "onSuccess");
        }

        @Override
        public void onFail() {
            Log.d(TAG, "onFail");
        }
    };

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
//                toastUtil.showToast(data.get(position).toString());
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
        mClickSelectCity = findViewById(R.id.click_to_letter);
        mClickHandler = findViewById(R.id.click_to_handler);
        mClickThreadPool = findViewById(R.id.click_to_threadpool);
        mClickBRVAH = findViewById(R.id.click_to_BRVAH);
        mClickWebview = findViewById(R.id.click_to_Webview);
        mClickMvp = findViewById(R.id.click_to_Mvp);
        mClickPop = findViewById(R.id.click_to_pop);
        mClickWaterMarke = findViewById(R.id.click_to_watermarke);

        mBtnClickToToSetting.setOnClickListener(this);
        mBtnClickToRefresh.setOnClickListener(this);
        mClickToNotifation.setOnClickListener(this);
        mClickToMapView.setOnClickListener(this);
        mClickToAlbum.setOnClickListener(this);
        mClickToAlert.setOnClickListener(this);
        mClickFinger.setOnClickListener(this);
        mClickAnnnotations.setOnClickListener(this);
        mClickToBanner.setOnClickListener(this);
        mClickSelectCity.setOnClickListener(this);
        mClickHandler.setOnClickListener(this);
        mClickThreadPool.setOnClickListener(this);
        mClickBRVAH.setOnClickListener(this);
        mClickWebview.setOnClickListener(this);
        mClickMvp.setOnClickListener(this);
        mClickPop.setOnClickListener(this);
        mClickWaterMarke.setOnClickListener(this);
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
                if (checkLocationPermession()) {
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
                        dialog = new OneButtonDialog.Builder(MainActivity.this)
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
            case R.id.click_to_letter:
                startActivity(new Intent(this, LetterActivity.class));
                break;
            case R.id.click_to_handler:
                startActivity(new Intent(this, HandlerActivity.class));
                break;
            case R.id.click_to_threadpool:
                startActivity(new Intent(this, ThreadPoolActivity.class));
                break;
            case R.id.click_to_BRVAH:
                startActivity(new Intent(this, BaseRecyclerViewActivity.class));
                break;
            case R.id.click_to_Webview:
                startActivity(new Intent(this, WebViewActivity.class));
                break;
            case R.id.click_to_Mvp:
                startActivity(new Intent(this, MvpActivity.class));
                break;
            case R.id.click_to_pop:
                View view = getWindow().getDecorView().findViewById(android.R.id.content);
                new MissionSquarePopWindow.Builder(this)
                        .setDurtion(5000)
                        .setMissionSquareClickListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toastUtil.showToast("略略略略略略略略略略略略");
                            }
                        })
                        .build().showAtLocation(view, Gravity.BOTTOM, 0, DensityUtil.dp2px(this, 65));
                break;
            case R.id.click_to_watermarke:
                if (checkStoragePermession()) {
                    startActivity(new Intent(this, WaterMarkeActivity.class));
                }
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
            TextView tv2 = moreView.findViewById(R.id.tv2);
            tv1.setText(data.get(i).toString());
            if (data.size() > i + 1) {
                //因为淘宝那儿是两条数据，但是当数据是奇数时就不需要赋值第二个，所以加了一个判断，还应该把第二个布局给隐藏掉
                tv2.setText(data.get(i + 1).toString());
            } else {
                tv2.setVisibility(View.GONE);
            }//添加到循环滚动数组里面去
            views.add(moreView);
        }
    }

    //初始化数据

    private void initData() {
        data = new ArrayList<>();
        data.add("家人给2岁孩子喝这个，孩子智力倒退10岁!!!家人给2岁孩子喝这个，孩子智力倒退10岁!!!");
        data.add("iPhone8最感人变化成真，必须买买买买!!!!");
        data.add("简直是白菜价！日本玩家33万甩卖15万张游戏王卡");
        data.add("iPhone7价格曝光了！看完感觉我的腰子有点疼");
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

    private boolean checkLocationPermession() {
        boolean locationPermession = this.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, this.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (!locationPermession) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
            }
            return false;
        }
        return true;
    }

    private boolean checkStoragePermession() {
        boolean writeStoragePermession = this.getPackageManager().checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, this.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        boolean readStoragePermession = this.getPackageManager().checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, this.getPackageName()) == PackageManager.PERMISSION_GRANTED;
        if (!writeStoragePermession || !readStoragePermession) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CAMERA_REQUIRE_STORAGE_PERMISSION);
            }
            return false;
        }
        return true;
    }

    private void requestPermissions(Activity activity, String[] strings, int cameraRequireStoragePermission) {
        ActivityCompat.requestPermissions(activity, strings, cameraRequireStoragePermission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults.length == 0) {
                dialog = new ConfirmDialog.Builder(this)
                        .setMessage("啊哦，权限被拒绝了，请点击确定开启权限")
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toSelfSetting(getActivity());
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
        } else if (requestCode == CAMERA_REQUIRE_STORAGE_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults.length == 0) {
                toastUtil.showToast("权限拒绝");
            } else {
                startActivity(new Intent(this, WaterMarkeActivity.class));
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > maxExitTime) {
            exitTime = System.currentTimeMillis();
        } else {
            new ConfirmDialog.Builder(this)
                    .setMessage("你确定要离开我这个帅气的App吗？")
                    .setNegativeButton("不，我要留下", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toastUtil.showToast("嗯，帅气的选择！");
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("嗯，我走了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    })
                    .build().show();
        }
    }

    public static void toSelfSetting(Context context) {
        Intent mIntent = new Intent();
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW);
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(mIntent);
    }

}

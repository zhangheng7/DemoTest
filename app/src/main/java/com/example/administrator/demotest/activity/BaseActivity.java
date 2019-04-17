package com.example.administrator.demotest.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.demotest.Mvp2.base.BaseXActivity;
import com.example.administrator.demotest.Mvp2.base.IBasePresenter;
import com.example.administrator.demotest.Mvp2.base.IBaseView;
import com.example.administrator.demotest.Utils.StatusBarUtil;

/**
 * @author zhangheng
 * @date 2019/3/18
 */

public class BaseActivity<P extends IBasePresenter> extends BaseXActivity<P> implements IBaseView {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStatusBarFullTransparent();
//        setHalfTransparent();
//        setFitSystemWindow(true);

//        //设置状态栏透明
//        StatusBarUtils.setTranslucentStatus(getActivity());
//        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
//        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
//        if (!StatusBarUtils.setStatusBarDarkTheme(getActivity(), true)) {
//            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
//            //这样半透明+白=灰, 状态栏的文字能看得清
//            StatusBarUtils.setStatusBarColor(getActivity(), 0x55000000);
//        }
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
//        StatusBarUtils.setRootViewFitsSystemWindows(getActivity(), true);
        StatusBarUtil.setTransparentColor(getActivity(), Color.TRANSPARENT);
        StatusBarUtil.setLightMode(getActivity());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public P onBindPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

//    public void showStatusBar() {
//        StatusBarUtil.setTransparentColor(this, Color.TRANSPARENT);
//        StatusBarUtil.setLightMode(this);
//        StatusBarUtil.setDarkMode(this);
//    }

    /**
     * 全透状态栏
     */
    public void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    public void setHalfTransparent() {

        if (Build.VERSION.SDK_INT >= 21) { //21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentView;

    public void setFitSystemWindow(boolean fitSystemWindow) {
        contentView = getWindow().getDecorView();
        contentView.findViewById(android.R.id.content).setFitsSystemWindows(fitSystemWindow);
    }

    /**
     * 为了兼容4.4的抽屉布局->透明状态栏
     */
//    protected void setDrawerLayoutFitSystemWindow() {
//        if (Build.VERSION.SDK_INT == 19) {//19表示4.4
//            int statusBarHeight = StatusBarUtil.getStatusBarHeight(this);
//            if (contentView == null) {
//                contentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
//            }
//            if (contentView instanceof DrawerLayout) {
//                DrawerLayout drawerLayout = (DrawerLayout) contentView;
//                drawerLayout.setClipToPadding(true);
//                drawerLayout.setFitsSystemWindows(false);
//                for (int i = 0; i < drawerLayout.getChildCount(); i++) {
//                    View child = drawerLayout.getChildAt(i);
//                    child.setFitsSystemWindows(false);
//                    child.setPadding(0,statusBarHeight, 0, 0);
//                }
//
//            }
//        }
//    }
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showToast(String msg) {

    }
}

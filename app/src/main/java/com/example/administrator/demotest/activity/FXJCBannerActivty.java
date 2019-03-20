package com.example.administrator.demotest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.demotest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhangheng
 * @date 2019/3/19
 */

public class FXJCBannerActivty extends BaseActivity {
    @BindView(R.id.banner_viewpager)
    ViewPager bannerViewpager;
    @BindView(R.id.point_group)
    LinearLayout pointGroup;
    @BindView(R.id.banner_main)
    RelativeLayout bannerMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fxjc_banner);
        ButterKnife.bind(this);
    }
}

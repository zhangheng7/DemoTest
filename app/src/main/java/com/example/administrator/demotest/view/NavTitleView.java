package com.example.administrator.demotest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.StatusBarUtil;


/**
 * @author zhangheng
 * @date 2019/3/20
 */

public class NavTitleView extends RelativeLayout {

//    private RelativeLayout mViewTitleBar;
    private View mMainView;
    private RelativeLayout mViewTitleBarView;
//    private FrameLayout mViewTitleBarView;
    private LinearLayout mLLTitleText;
    private TextView mTvTitleText;
    private TextView mTvSubtitleText;

    public NavTitleView(Context context) {
        this(context, null);
    }

    public NavTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mMainView = View.inflate(getContext(), R.layout.view_h5_titlebar, this);
        mViewTitleBarView = mMainView.findViewById(R.id.view_title_bar_main);
//        mViewTitleBar = mMainView.findViewById(R.id.view_title_bar);
        mLLTitleText = mMainView.findViewById(R.id.ll_title_text);
        mTvTitleText = mMainView.findViewById(R.id.tv_title_text);
        mTvSubtitleText = mMainView.findViewById(R.id.tv_subtitle_text);

        int statusBarHeight = StatusBarUtil.getStatusBarHeight(getContext());
        int height = mLLTitleText.getLayoutParams().height;

        LayoutParams lp = (LayoutParams) mViewTitleBarView.getLayoutParams();
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = statusBarHeight + height;
//        mViewTitleBarView.setVisibility(VISIBLE);
    }

}

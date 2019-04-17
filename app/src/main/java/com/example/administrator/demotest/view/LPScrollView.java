package com.example.administrator.demotest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;


/**
 * @author junhong
 */
public class LPScrollView extends ScrollView implements Runnable {
    private OnScrollListener onScrollListener;
    private OnEndScrollListener onEndScrollListener;
    private OnStartScrollListener onStartScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setOnEndScrollListener(OnEndScrollListener onEndScrollListener) {
        this.onEndScrollListener = onEndScrollListener;
    }

    public void setOnStartScrollListener(OnStartScrollListener onStartScrollListener) {
        this.onStartScrollListener = onStartScrollListener;
    }

    public LPScrollView(Context context) {
        this(context, null);
    }

    public LPScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LPScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private long lastScrollUpdate = -1;

    @Override
    public void run() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastScrollUpdate) > 100) {
            lastScrollUpdate = -1;
            removeCallbacks(this);
            if (onEndScrollListener != null) {
                onEndScrollListener.onEndScroll();
            }
        } else {
            postDelayed(this, 100);
        }
    }

    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {
        /**
         * 回调方法，返回LPScrollView滑动的Y方向距离
         *
         * @param scrollY
         */
        void onScroll(int scrollY);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (onScrollListener != null) {
            onScrollListener.onScroll(y);
        }

        if (lastScrollUpdate == -1) {
            if (onStartScrollListener != null) {
                onStartScrollListener.onStartScroll();
            }
            postDelayed(this, 100);
        }

        lastScrollUpdate = System.currentTimeMillis();
    }

    public interface OnEndScrollListener {
        /**
         * 滚动结束
         */
        void onEndScroll();
    }

    public interface OnStartScrollListener {
        /**
         * 滚动开始
         */
        void onStartScroll();
    }
}

package com.example.administrator.demotest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.example.administrator.demotest.R;

import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class UPMarqueeView extends ViewFlipper {

    public OnItemClickListener onItemClickListener;

    /**
     * 点击事件
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item_view的接口
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }

    private Context mContext;
    private boolean isSetAnimDuration = false;
    private int interval = 5000;
    /**
     * 动画时间
     *      
     */
    private int animDuration = 500;

    public UPMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        setFlipInterval(interval);
        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.in);
        if (isSetAnimDuration) {
            animIn.setDuration(animDuration);
        }
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.out);
        if (isSetAnimDuration) {
            animOut.setDuration(animDuration);
        }
        setOutAnimation(animOut);
    }

    public void setViews(List<View> views) {
        if (views == null || views.size() <= 0) {
            return;
        }
        removeAllViews();
        for (int i = 0; i < views.size(); i++) {
            addView(views.get(i));
            final int finalI = i;
            views.get(i).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(finalI, v);
                }
            });
        }
        startFlipping();
    }


}

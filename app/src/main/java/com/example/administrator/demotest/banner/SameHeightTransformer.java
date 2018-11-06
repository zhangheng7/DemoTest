package com.example.administrator.demotest.banner;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author zhangheng
 * @date 2018/6/27
 */
public class SameHeightTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_MIN_ALPHA = 0.5f;
    private float mMinAlpha = DEFAULT_MIN_ALPHA;
    public static final float DEFAULT_CENTER = 0.5f;

    @Override
    public void transformPage(View view, float position) {

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        // hack
        view.setScaleX(0.999f);
        view.setPivotY(pageHeight / 2);
        view.setPivotX(pageWidth / 2);

        if (position < -1) {
            // [-Infinity,-1)
            view.setAlpha(mMinAlpha);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setPivotX(pageWidth);

        } else if (position <= 1) {
            // [-1,1]
            // [0，-1]
            if (position < 0) {
                // [1,min]
                float factor = mMinAlpha + (1 - mMinAlpha) * (1 + position);
                view.setAlpha(factor);
                view.setScaleX(1);
                view.setScaleY(1);
                view.setPivotX(pageWidth * (DEFAULT_CENTER + (DEFAULT_CENTER * -position)));

            } else {
                // [1，0]
                // [min,1]
                float factor = mMinAlpha + (1 - mMinAlpha) * (1 - position);

                view.setAlpha(factor);
                view.setScaleX(1);
                view.setScaleY(1);
                view.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER));

            }
        } else {
            // (1,+Infinity]
            view.setAlpha(mMinAlpha);
            view.setPivotX(0);
            view.setScaleX(1);
            view.setScaleY(1);
        }
    }
}

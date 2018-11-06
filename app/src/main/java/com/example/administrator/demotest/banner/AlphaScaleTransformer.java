package com.example.administrator.demotest.banner;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author: zhangheng
 */
public class AlphaScaleTransformer implements ViewPager.PageTransformer {

    private static final float DEFAULT_MIN_ALPHA = 0.5f;
    private float mMinAlpha = DEFAULT_MIN_ALPHA;
    private static final float DEFAULT_MIN_SCALE = 0.85f;
    private float mMinScale = DEFAULT_MIN_SCALE;
    public static final float DEFAULT_CENTER = 0.5f;

    @Override
    public void transformPage(View view, float position) {

        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        view.setScaleX(0.999f);
        view.setPivotY(pageHeight / 2);
        view.setPivotX(pageWidth / 2);

        if (position < -1) {
            view.setAlpha(mMinAlpha);
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
            view.setPivotX(pageWidth);

        } else if (position <= 1) {
            if (position < 0) {
                float factor = mMinAlpha + (1 - mMinAlpha) * (1 + position);
                float scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale;

                view.setAlpha(factor);
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setPivotX(pageWidth * (DEFAULT_CENTER + (DEFAULT_CENTER * -position)));

            } else {
                float factor = mMinAlpha + (1 - mMinAlpha) * (1 - position);
                float scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale;

                view.setAlpha(factor);
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
                view.setPivotX(pageWidth * ((1 - position) * DEFAULT_CENTER));

            }
        } else {
            view.setAlpha(mMinAlpha);
            view.setPivotX(0);
            view.setScaleX(mMinScale);
            view.setScaleY(mMinScale);
        }
    }
}

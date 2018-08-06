package com.example.administrator.demotest.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.DensityUtil;

/**
 * @author zhangheng
 * @date 2018/7/30
 */

public class MyLetterListView extends View {
    OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    String[] mLetters = {"历", "热", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K",
            "L", "M", "N", "P", "Q", "R", "S", "T", "W", "X",
            "Y", "Z"};
    private int mChoose = -1;
    private int size;
    Paint paint = new Paint();
    boolean showBkg = false;
    private int mHeight;

    public MyLetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MyLetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLetterListView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h - getPaddingTop() - getPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#40000000"));
        }
        int width = getWidth();
        size = mLetters.length;
        int singleHeight = (mHeight / size);

        for (int i = 0; i < size; i++) {
            paint.setColor((getResources().getColor(R.color.letter_text)));
            paint.setTextSize(DensityUtil.sp2px(getContext(), 12));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            /*if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
			}*/
            float xPos = width / 2 - paint.measureText(mLetters[i]) / 2;
            float yPos = singleHeight * i + singleHeight + getPaddingTop();
            canvas.drawText(mLetters[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        float y = event.getY();
        int oldChoose = mChoose;
        if (y < getPaddingTop() || y > mHeight + getPaddingTop()) {
            mChoose = -1;
        } else {
            // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
            mChoose = (int) ((y - getPaddingTop()) / mHeight * size);
        }
        switch (action) {
            case MotionEvent.ACTION_UP:
                mChoose = -1;
                break;
            default:
                if (oldChoose != mChoose && mChoose != -1) {
                    listener.onTouchingLetterChanged(mLetters[mChoose]);
                }
        }
        invalidate();

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
}

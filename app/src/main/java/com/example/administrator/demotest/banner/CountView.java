package com.example.administrator.demotest.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author zhangheng
 * @date 2018/11/6
 */

public class CountView extends View implements View.OnClickListener{

    private int count = 0;

    public CountView(Context context) {
        super(context);
        init();
    }


    public CountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化的方法
    private void init() {

        this.setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        //圆
        canvas.drawCircle(300,300,200,paint);


        paint.setColor(Color.BLACK);
        paint.setTextSize(100);

        String text = String.valueOf(count);

        //拿到文本的宽度和高度
        Rect rect = new Rect();
        paint.getTextBounds(text,0,text.length(),rect);

        canvas.drawText(text,300-rect.width()/2,300+rect.height()/2,paint);

    }

    @Override
    public void onClick(View view) {
        count ++;

        //重新绘制
        postInvalidate();
    }
}

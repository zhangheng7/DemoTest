package com.example.administrator.demotest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.example.administrator.demotest.R;

/**
 * @author zhangheng
 * @date 2018/7/17
 */
public class AnnotationsActivity extends BaseActivity {

    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annotations);
        tv = findViewById(R.id.tv1);
        SpannableStringBuilder builder = new SpannableStringBuilder("这行字是不同颜色的！");

        ForegroundColorSpan red = new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan green = new ForegroundColorSpan(Color.GREEN);
        ForegroundColorSpan gray = new ForegroundColorSpan(Color.GRAY);
        ForegroundColorSpan blue = new ForegroundColorSpan(Color.BLUE);
        ForegroundColorSpan black = new ForegroundColorSpan(Color.BLACK);
        ForegroundColorSpan green2 = new ForegroundColorSpan(Color.GREEN);
        ForegroundColorSpan yellow = new ForegroundColorSpan(Color.YELLOW);
        ForegroundColorSpan ltgray = new ForegroundColorSpan(Color.LTGRAY);
        ForegroundColorSpan cyan = new ForegroundColorSpan(Color.CYAN);
        ForegroundColorSpan red2 = new ForegroundColorSpan(Color.RED);


        builder.setSpan(red, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(green, 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(gray, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(blue, 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(black, 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(green2, 5, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(yellow, 6, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(ltgray, 7, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(cyan, 8, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(red2, 9, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv.setText(builder);
    }


}

package com.example.administrator.demotest.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.UIHandler;


/**
 * @author zhangheng
 * @date 2019/3/26
 */

public class MissionSquarePopWindow extends PopupWindow {
    private static final String TAG = "MissionSquarePopWindow";
    private static RelativeLayout mViewMissonSquare;

    public MissionSquarePopWindow(Context context) {
        super(context);
    }

    public static class Builder {
        private String mMessage;
        private Context context;
        private int durtion = 0;
        private View.OnClickListener clickListener;

        public Builder(Context mContext) {
            this.context = mContext;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        //毫秒
        public Builder setDurtion(int durtion) {
            this.durtion = durtion;
            return this;
        }

        public Builder setMissionSquareClickListner(View.OnClickListener listner) {
            this.clickListener = listner;
            return this;
        }

        public MissionSquarePopWindow build() {
            //设置popWindow的View
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final MissionSquarePopWindow popWindow = new MissionSquarePopWindow(context);
            View mMissonView = mInflater.inflate(R.layout.view_mission_square, null);

//            popWindow.setAnimationStyle(R.style.popwin_anim_style);
            popWindow.setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
            popWindow.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
            popWindow.setContentView(mMissonView);
            //设置属性
//            popWindow.setBackgroundDrawable(null);
            popWindow.setBackgroundDrawable(new BitmapDrawable());
            popWindow.setOutsideTouchable(false);
            //查找Id
            mViewMissonSquare = mMissonView.findViewById(R.id.view_mission_square);
            ImageView mImgMissionSquare = mMissonView.findViewById(R.id.img_mission_square);
            TextView mTextMissionSquare = mMissonView.findViewById(R.id.tv_mission_square);
            if (!TextUtils.isEmpty(mMessage)) {
                mTextMissionSquare.setText(mMessage);
            }
            ObjectAnimator translationX = new ObjectAnimator().ofFloat(mViewMissonSquare, "translationX", 0, 0);
            ObjectAnimator translationY = new ObjectAnimator().ofFloat(mViewMissonSquare, "translationY", 400, 0);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(translationX, translationY);
            animatorSet.setDuration(1000);
            animatorSet.start();
            mViewMissonSquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(v);
                }
            });
            if (durtion != 0) {
                UIHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (popWindow != null && popWindow.isShowing()) {
                            ObjectAnimator translationX = new ObjectAnimator().ofFloat(mViewMissonSquare, "translationX", 0, 0);
                            ObjectAnimator translationY = new ObjectAnimator().ofFloat(mViewMissonSquare, "translationY", 0, 400);
                            AnimatorSet animatorSet = new AnimatorSet();
                            animatorSet.playTogether(translationX, translationY);
                            animatorSet.setDuration(1000);
                            animatorSet.start();
                            animatorSet.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    popWindow.dismiss();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                        }
                    }
                }, durtion + 1000);
            }

            popWindow.update();
            return popWindow;
        }
    }
}

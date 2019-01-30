package com.example.administrator.demotest.banner;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhangheng
 * @date 2018/11/6
 */

public class CustomBanner extends FrameLayout {
    @BindView(R.id.banner_viewpager)
    ViewPager bannerViewPager;
    @BindView(R.id.point_group)
    LinearLayout linearBannner;
    @BindView(R.id.layout_banner)
    RelativeLayout main;
    private List<String> list;
    private int time = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {

                int currentItem = bannerViewPager.getCurrentItem();

                bannerViewPager.setCurrentItem(currentItem + 1);

                //再次发送
                sendEmptyMessageDelayed(0, time * 1000);

            }
        }
    };
    private List<ImageView> listDoc;
    private OnClickLisner onClickLisner;
    private GlideRoundTransform transform;

    public CustomBanner(@NonNull Context context) {
        super(context);
        init();
    }

    public CustomBanner(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomBanner(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        View view = View.inflate(getContext(), R.layout.layout_banner, this);
        transform = new GlideRoundTransform(getContext(), 10);
        ButterKnife.bind(this, view);

    }

    /**
     * 对外提供设置image路径的方法
     */
    public void setImageUrls(List<String> list) {
        this.list = list;

        if (list == null) {
            return;
        }

        //设置适配器
        LunBoAdapter lunBoAdapter = new LunBoAdapter(getContext(), list);
        bannerViewPager.setAdapter(lunBoAdapter);

        initDoc();

        //显示中间某个位置
        bannerViewPager.setCurrentItem(list.size() * 10000);
        bannerViewPager.setOffscreenPageLimit(3);
        ViewPagerScroller pagerScroller = new ViewPagerScroller(getContext(), new AccelerateDecelerateInterpolator());
        pagerScroller.setScrollDuration(500);
        pagerScroller.initViewPagerScroll(bannerViewPager);

        //使用handler自动轮播
        handler.sendEmptyMessageDelayed(0, time * 1000);

        //状态改变的监听事件
        bannerViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //在选中某一页的时候,切换小圆点的背景
                for (int i = 0; i < listDoc.size(); i++) {
                    if (position % listDoc.size() == i) {
                        listDoc.get(i).setBackgroundResource(R.drawable.banner_circle_def);
                    } else {
                        listDoc.get(i).setBackgroundResource(R.drawable.banner_circle_sel);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 初始化小圆点
     */
    private void initDoc() {
        //创建一个集合,记录这些小圆点
        listDoc = new ArrayList<>();
        //清空布局
        linearBannner.removeAllViews();

        for (int i = 0; i < list.size(); i++) {
            ImageView docImage = new ImageView(getContext());
            if (i == 0) {
                docImage.setBackgroundResource(R.drawable.banner_circle_def);
            } else {
                docImage.setBackgroundResource(R.drawable.banner_circle_sel);
            }
            //添加到集合
            listDoc.add(docImage);
            //添加到线性布局
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 0, 5, 0);
            linearBannner.addView(docImage, params);
        }


    }

    /**
     * 对外提供轮播的时间
     */
    public void setTimeSecond(int time) {
        this.time = time;
    }

    /**
     * 提供两侧缩进
     */
    public void setBannerStyle(BannerStyle style) {
        switch (style) {
            case Default: {
//                pointStyle = PointStyle.Line;
                main.setClipChildren(false);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bannerViewPager.getLayoutParams();
                lp.setMargins(DensityUtil.dp2px(getContext(), 20),
                        DensityUtil.dp2px(getContext(), 4),
                        DensityUtil.dp2px(getContext(), 20),
                        8
                );
                bannerViewPager.setLayoutParams(lp);
                bannerViewPager.setPageTransformer(true, new AlphaScaleTransformer());
                bannerViewPager.setPageMargin(DensityUtil.dp2px(getContext(), 8));
                break;
            }
            case One: {
//                pointStyle = PointStyle.Line;
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bannerViewPager.getLayoutParams();
                lp.setMargins(DensityUtil.dp2px(getContext(), 15),
                        DensityUtil.dp2px(getContext(), 0),
                        DensityUtil.dp2px(getContext(), 15),
                        0);
                bannerViewPager.setLayoutParams(lp);
                bannerViewPager.setPageMargin(DensityUtil.dp2px(getContext(), 8));
                bannerViewPager.setPageTransformer(true, new AlphaScaleTransformer());
                break;
            }
            case Two: {
//                pointStyle = PointStyle.Circle;
                bannerViewPager.setPageTransformer(true, new AlphaScaleTransformer());
                break;
            }
            case Three: {
//                pointStyle = PointStyle.Gone;
                main.setClipChildren(false);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bannerViewPager.getLayoutParams();
                lp.setMargins(DensityUtil.dp2px(getContext(), 15),
                        DensityUtil.dp2px(getContext(), 5),
                        DensityUtil.dp2px(getContext(), 15),
                        0
                );
                bannerViewPager.setLayoutParams(lp);
                bannerViewPager.setPageTransformer(true, new SameHeightTransformer());
                bannerViewPager.setPageMargin(DensityUtil.dp2px(getContext(), 8));
                break;
            }

        }
    }

    /**
     * 点击事件
     *
     * @param onClickLisner
     */
    public void setClickListner(OnClickLisner onClickLisner) {

        this.onClickLisner = onClickLisner;
    }

    private class LunBoAdapter extends PagerAdapter {

        private List<String> list;
        private Context context;

        public LunBoAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            //创建imageView
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //加载这张图片
            Glide.with(context)
                    .load(list.get(position % list.size()))
                    .dontAnimate()
                    .transform(transform)
                    .placeholder(R.drawable.bg_image_loading)
                    .into(imageView);


            //点击事件
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    //触发
                    onClickLisner.onItemClick(position % list.size());
                }
            });

            //触摸事件
            imageView.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //取消handler身上的消息和回调
                            handler.removeCallbacksAndMessages(null);

                            break;
                        case MotionEvent.ACTION_MOVE:
                            handler.removeCallbacksAndMessages(null);
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            handler.sendEmptyMessageDelayed(0, time * 1000);
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.sendEmptyMessageDelayed(0, time * 1000);
                            break;
                    }

                    return false;
                }
            });

            //添加到容器
            container.addView(imageView);
            //返回
            return imageView;
        }

        //销毁条目时
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public interface OnClickLisner {
        void onItemClick(int position);
    }

    public enum BannerStyle {
        /**
         * 默认
         */
        Default,
        /**
         * 轮播1
         */
        One,
        /**
         * 轮播2
         */
        Two,
        /**
         * 轮播3
         */
        Three
    }
}

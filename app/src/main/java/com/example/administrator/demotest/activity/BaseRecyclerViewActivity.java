package com.example.administrator.demotest.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;
import com.example.administrator.demotest.Utils.UIHandler;
import com.example.administrator.demotest.adapter.HomeAdapter;
import com.example.administrator.demotest.adapter.Model;
import com.example.administrator.demotest.view.CustomLoadMoreView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BaseRecyclerViewActivity extends BaseActivity {
    List list;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout mSwipereFreshLayout;
    private ToastUtil toastUtil;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recycler_view);
        ButterKnife.bind(this);
        toastUtil = new ToastUtil(this);
        //设置刷新球的颜色
        mSwipereFreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.YELLOW);
        //模拟的数据（实际开发中一般是从网络获取的）
        list = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            model = new Model();
            model.setTitle("我是第" + i + "条标题");
            model.setContent("第" + i + "条内容");
            list.add(model);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(linearLayoutManager);
        final HomeAdapter adapter = new HomeAdapter(R.layout.recycle_item, list);
        mRecycleView.setAdapter(adapter);
        adapter.bindToRecyclerView(mRecycleView);
        //开启动画
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT);
        //设置不展示动画的条目，以下标计算
        adapter.setNotDoAnimationCount(5);
//        //添加headerView
//        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog, null);
//        adapter.addHeaderView(view);
        //设置加载view
        adapter.setLoadMoreView(new CustomLoadMoreView());
        //设置可以上拉
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mRecycleView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.loadMoreEnd();
                    }
                }, 2000);
            }
        }, mRecycleView);
        //因为默认进来就会执行上拉加载，设置这个可以一进来不执行加载
        adapter.disableLoadMoreIfNotFullPage();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                toastUtil.showToast("点击了第" + (position + 1) + "条条目");
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                toastUtil.showToast("长按了第" + (position + 1) + "条条目");
                return false;
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_title) {
                    toastUtil.showToast("点击了第" + (position + 1) + "条子条目的标题");
                } else if (view.getId() == R.id.tv_content) {
                    toastUtil.showToast("点击了第" + (position + 1) + "条子条目的内容");
                } else if (view.getId() == R.id.icon3) {
                    toastUtil.showToast("点击了第" + (position + 1) + "条子条目的图片");
                }
            }
        });
        adapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_title) {
                    toastUtil.showToast("长按了第" + (position + 1) + "条子条目的标题");
                } else if (view.getId() == R.id.tv_content) {
                    toastUtil.showToast("长按了第" + (position + 1) + "条子条目的内容");
                } else if (view.getId() == R.id.icon3) {
                    toastUtil.showToast("长按了第" + (position + 1) + "条子条目的图片");
                }
                return false;
            }
        });
        //下拉刷新
        mSwipereFreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                UIHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipereFreshLayout.setRefreshing(false);
                    }
                },3000);
            }
        });
    }
}

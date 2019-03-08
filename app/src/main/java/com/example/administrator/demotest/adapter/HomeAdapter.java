package com.example.administrator.demotest.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.demotest.R;

import java.util.List;

/**
 * @author zhangheng
 * @date 2019/3/4
 */

public class HomeAdapter extends BaseQuickAdapter<Model, BaseViewHolder> {


    public HomeAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Model item) {
        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_content, item.getContent())
                .addOnClickListener(R.id.icon3)
                .addOnClickListener(R.id.tv_title)
                .addOnClickListener(R.id.tv_content)
                .setImageResource(R.id.icon3, R.drawable.ic_launcher);
    }
}

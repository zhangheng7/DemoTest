package com.example.administrator.demotest.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.StringUtils;
import com.example.administrator.demotest.Utils.ToastUtil;
import com.example.administrator.demotest.view.MyLetterListView;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhangheng
 * @date 2018/11/29
 */

public class LetterActivity extends BaseActivity {
    @BindView(R.id.letter_view)
    MyLetterListView mLetterView;
    private ToastUtil toastUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);
        toastUtil = new ToastUtil(this);
        ButterKnife.bind(this);
        mLetterView.setOnTouchingLetterChangedListener(new MyLetterListView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
//                HashMap<String, Integer> index = getLetterIndex();
                toastUtil.showToast(s);
            }
        });
    }

//    private HashMap<String, Integer> getLetterIndex() {
//        HashMap<String, Integer> firstLetterIndex = new HashMap<>(cityListAdapter.getDatas().size());
//        List<CityModel> lists = cityListAdapter.getDatas();
//        for (int i = 0; i < cityListAdapter.getDatas().size(); i++) {
//            // 当前汉语拼音首字母
//            String currentStr = StringUtils.getFirstLetter(lists.get(i).getPinyin());
//            // 上一个汉语拼音首字母，如果不存在为" "
//            String previewStr = (i - 1) >= 0 ? StringUtils.getFirstLetter(lists.get(i - 1)
//                    .getPinyin()) : " ";
//            if (!previewStr.equals(currentStr)) {
//                String name = StringUtils.getFirstLetter(lists.get(i).getPinyin());
//                firstLetterIndex.put(name, i);
//            }
//        }
//        return firstLetterIndex;
//    }
}

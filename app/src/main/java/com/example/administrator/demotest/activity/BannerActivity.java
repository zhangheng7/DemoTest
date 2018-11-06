package com.example.administrator.demotest.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;
import com.example.administrator.demotest.banner.BannerBean;
import com.example.administrator.demotest.banner.CustomBanner;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BannerActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient;
    String url = "http://image.baidu.com/channel/listjson?pn=0&rn=30&tag1=美女&tag2=全部&ie=utf8";
    private CustomBanner customBanner;
    private ToastUtil toastUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        customBanner = findViewById(R.id.banner_main);
        okHttpClient = new OkHttpClient();
        toastUtil = new ToastUtil(BannerActivity.this);

        Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            private List<String> urlList;

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response != null) {
                    String json = response.body().string();
                    final BannerBean bannerBean = new Gson().fromJson(json, BannerBean.class);
                    urlList = new ArrayList<>();
                    List<BannerBean.DataBean> data = bannerBean.getData();
                    for (int i = 0; i < data.size(); i++) {
                        String imageUrl = data.get(i).getImage_url();
                        urlList.add(imageUrl);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customBanner.setTimeSecond(3);
                            customBanner.setImageUrls(urlList);
                            customBanner.setBannerStyle(CustomBanner.BannerStyle.Default);
                        }
                    });
                }
            }
        });
    }
}

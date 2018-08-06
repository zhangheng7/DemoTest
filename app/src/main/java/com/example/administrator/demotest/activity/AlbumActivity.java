package com.example.administrator.demotest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.ZZImageLoader.AlxListViewCommonAdapter;
import com.example.administrator.demotest.ZZImageLoader.SelectPhotoAdapter;
import com.example.administrator.demotest.ZZImageLoader.ZZImageLoder;

import java.util.ArrayList;

public class AlbumActivity extends AppCompatActivity implements View.OnClickListener {

    private ZZImageLoder zzImageLoder;
    private int screenWidth = 0;
    AlxListViewCommonAdapter<SelectPhotoAdapter.SelectPhotoEntity> photoListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        View bt_add_photo = findViewById(R.id.bt_add_photo);
        bt_add_photo.setOnClickListener(this);
        zzImageLoder = new ZZImageLoder(this);
        screenWidth = SelectPhotoAdapter.getScreenWidth(this);
        photoListViewAdapter = new AlxListViewCommonAdapter<SelectPhotoAdapter.SelectPhotoEntity>(this, R.layout.listview_item, null) {
            @Override
            public void convert(ViewHolder holder, int position, SelectPhotoAdapter.SelectPhotoEntity entity) {
                ImageView imageView = holder.getView(R.id.iv_selected_photo);
                //因为图片太大，所以不要保存缩略图
                zzImageLoder.setAsyncBitmapFromSD(entity.url, imageView, screenWidth, true, true, false);
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_photo:
                Intent intent = new Intent(this, SelectPhotoActivity.class);
                startActivityForResult(intent, 10);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Alex", "mainActivity的onActivityResult req=" + requestCode + "    result=" + requestCode);
        if (data == null || resultCode != SelectPhotoActivity.SELECT_PHOTO_OK) return;
        boolean isFromCamera = data.getBooleanExtra("isFromCamera", false);
        ArrayList<SelectPhotoAdapter.SelectPhotoEntity> selectedPhotos = data.getParcelableArrayListExtra("selectPhotos");
        Log.i("Alex", "选择的图片是" + selectedPhotos);
        ListView lv_photos = (ListView) findViewById(R.id.lv_photos);

        photoListViewAdapter.setmDatas(selectedPhotos);
        lv_photos.setAdapter(photoListViewAdapter);
    }
}
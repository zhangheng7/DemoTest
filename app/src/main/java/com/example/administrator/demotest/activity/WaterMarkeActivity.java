package com.example.administrator.demotest.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.example.administrator.demotest.R;
import com.example.administrator.demotest.Utils.ToastUtil;
import com.example.administrator.demotest.Utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author zhangheng
 * @date 2019/4/1
 */

public class WaterMarkeActivity extends BaseActivity {
    @BindView(R.id.iv_water)
    ImageView ivWater;
    private static final int CAMERA_REQUIRE_STORAGE_PERMISSION = 10000;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watermarke);
        ButterKnife.bind(this);

        InputStream bitmap = null;
        InputStream icLauncher = null;
        try {
            bitmap = this.getAssets().open("start.png"); //参数，取assets文件夹下资源文件的路劲，不包括"assets/"
            icLauncher = this.getResources().openRawResource(R.drawable.ic_launcher);
            bitmap.close();
            icLauncher.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(bitmap);
            Utils.closeQuietly(icLauncher);
        }
        Bitmap bit = BitmapFactory.decodeStream(bitmap);

        Bitmap bitIcLauncher = new BitmapDrawable(icLauncher).getBitmap();
        Bitmap maskImage = createWaterMaskImage(this, bit, bitIcLauncher);
        ivWater.setImageBitmap(maskImage);
    }

    /**
     * 给图片添加水印
     *
     * @param context
     * @param bitmap
     * @param watermark
     * @return
     */
    private Bitmap createWaterMaskImage(final Context context, Bitmap bitmap, Bitmap watermark) {

        String tag = "createBitmap";
        Log.d(tag, "create a new bitmap");
        if (bitmap == null) {
            return null;
        }
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int watermarkWidth = watermark.getWidth();
        int watermarkHeight = watermark.getHeight();
        // create the new blank bitmap
        final Bitmap newBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        int newBitmapWidth = newBitmap.getWidth();
        int newBitmapHeight = newBitmap.getHeight();
        Canvas cv = new Canvas(newBitmap);
        // draw src into
        cv.drawBitmap(bitmap, 0, 0, null);// 在 0，0坐标开始画入src
        // draw watermark into
        cv.drawBitmap(watermark, newBitmapWidth - watermarkWidth - 20, newBitmapHeight - watermarkHeight - 20, null);// 在src的右下角画入水印
        // save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);// 保存
        // store
        cv.restore();// 存储
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (checkStoragePermission(WaterMarkeActivity.this)) {
                    saveImageToGallery(context, newBitmap);
                }
            }
        }).start();
        return newBitmap;
    }

    public static boolean saveImageToGallery(Context context, Bitmap bmp) {
        if (bmp == null) {
            return false;
        }
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File
                .separator + "滴滴滴滴";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".png";
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            //把文件插入到系统图库

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(fos);
        }
        return false;
    }

    private boolean checkStoragePermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            new ToastUtil(this).showToast(R.string.permission_file_fail);
            return false;
        } else {
            return true;
        }
    }
}

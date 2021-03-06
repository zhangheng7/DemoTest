package com.example.administrator.demotest.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.demotest.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * @author zhangheng
 * @date 2018/11/29
 */

public class ImageUtils {
    private static final String TAG = "ImageUtil";

    public static Bitmap base64ToBitmap(String base64Data) {
        Bitmap bitmap = null;

        try {
            byte[] bitmapArray = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static String bitmapToBase64(Bitmap bitmap) {
        if (bitmap == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream bStream = null;
        try {
            bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
            bStream.flush();
            byte[] bytes = bStream.toByteArray();
            sb.append(android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeQuietly(bStream);
        }
        return sb.toString();
    }

    public static void loadImage(ImageView imageView, String host, String url) {
        if (imageView == null) {
            return;
        }

        WeakReference<ImageView> reference = new WeakReference<>(imageView);
        ImageView target = reference.get();
        if (target == null) {
            return;
        }

        target.setImageDrawable(null);

        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(host)) {
            return;
        }
        String imageUrl = host + url;
        Glide.with(imageView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.bg_image_loading)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .error(R.drawable.bg_image_error)
                .dontAnimate()
                .into(target);
        Log.e(TAG, "loadImage url:" + imageUrl);
    }
}

package com.example.administrator.demotest.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static final String TAG = "Utils";

    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null) {
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } /*else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } */ else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri)) {
                return imageUri.getLastPathSegment();
            }
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            closeQuietly(cursor);
        }
        return null;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static boolean checkActivityFinish(Context context) {
        return context != null && (context instanceof Activity) && !((Activity) context).isFinishing();
    }

    /**
     * 压缩图片到指定大小以下
     */
    public static Bitmap compressBitmap(Bitmap bitmap, int size) {
        Bitmap newBitmap = null;
        if (bitmap != null) {
            InputStream is = null;
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inDither = false;
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inSampleSize = 1;
                float bitmapSize = Utils.getSizeOfBitmap(bitmap);
                // 压缩图片到指定大小
                while (bitmapSize > (size + size / 3)) {
                    opt.inSampleSize = opt.inSampleSize + 1;
                    is = Bitmap2IS(bitmap);
                    if (is != null) {
                        bitmap = BitmapFactory.decodeStream(is, null, opt);
                        bitmapSize = Utils.getSizeOfBitmap(bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeQuietly(is);
            }
            newBitmap = bitmap;
        }
        return newBitmap;
    }

    /**
     * 获取并判断魅族手机Flyme版本号是否低于2.4
     */
    public static boolean getFlymeVersion() {
        try {
            String Version = android.os.Build.DISPLAY;
            if (((Build.DEVICE).contains("mx") && (Build.PRODUCT)
                    .contains("meizu_mx"))
                    || (Version != null && Version.contains("Flyme"))) {
                String flymeVersion = android.os.Build.DISPLAY;
                flymeVersion = flymeVersion.substring(9, flymeVersion.length());
                if (flymeVersion != null && flymeVersion.length() > 3) {
                    float version = Float.parseFloat(flymeVersion.substring(0,
                            3));
                    return version < 2.5f;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 隐藏魅族手机smartbar
     */
    public static void hide(View decorView) {
        if (!hasSmartBar()) {
            return;
        }

        try {
            @SuppressWarnings("rawtypes")
            Class[] arrayOfClass = new Class[1];
            arrayOfClass[0] = Integer.TYPE;
            Method localMethod = View.class.getMethod("setSystemUiVisibility",
                    arrayOfClass);
            Field localField = View.class
                    .getField("SYSTEM_UI_FLAG_HIDE_NAVIGATION");
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = localField.get(null);
            localMethod.invoke(decorView, arrayOfObject);
        } catch (Exception e) {
            Log.d(TAG, "hide: " + e);
        }
    }


    /**
     * 判断是否有动态SmartBar
     */
    private static boolean hasSmartBar() {
        try {
            // 新型号可用反射调用Build.hasSmartBar()
            Method method = Class.forName("android.os.Build").getMethod(
                    "hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
            Log.d(TAG, "hasSmartBar: "+e);
        }
        return false;
    }


    /**
     * 获取图片大小
     */
    public static float getSizeOfBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        long length = baos.toByteArray().length / 1024;
        return length;
    }

    private static InputStream Bitmap2IS(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream stream = null;
        try {
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            stream = new ByteArrayInputStream(baos.toByteArray());
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return stream;
    }

    /**
     * 图片转换成base64字符串
     */
    public static String bitmap2String(Bitmap bitmap) {
        String string = "";
        ByteArrayOutputStream bStream = null;
        try {
            bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bStream);
            byte[] bytes = bStream.toByteArray();
            string = Base64.encode(bytes);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            closeQuietly(bStream);
        }
        return string;
    }

    /**
     * 图片转换成base64字符串
     */
    public static String bitmap2String(Bitmap bitmap, int maxSize) {
        if (bitmap == null) {
            return "";
        }

        String str = "";
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 100;
            while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
            }
            byte[] bytes = baos.toByteArray();
            str = Base64.encode(bytes);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            closeQuietly(baos);
        }
        return str;
    }

    public static Bitmap readBitmap(Context context, int resId) {
        InputStream is = null;
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            // 获取资源图片
            is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is, null, opt);
        } finally {
            closeQuietly(is);
        }
    }

    public static String getValueByName(String url, String key) {
        String result = "";
        int index = url.indexOf("?");
        String temp = url.substring(index + 1);
        String[] keyValue = temp.split("&");
        for (String str : keyValue) {
            if (str.contains(key)) {
                result = str.replace(key + "=", "");
                break;
            }
        }
        return result;
    }

    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();
        int height = windowManager.getDefaultDisplay().getHeight();
        int[] result = {width, height};
        return result;
    }

    public static String getDeviceBrand() {
        return getDeviceBrand_private();
    }

    /**
     * 获取手机品牌
     *
     * @return
     */
    private static String getDeviceBrand_private() {
        String brand2 = null;
        String brand = android.os.Build.BRAND;
        if (null != brand) {
            brand = brand.replace(' ', '-');
            if (brand.length() > 8) {
                brand2 = brand.substring(0, 8);
            } else if (brand.length() < 8) {
                brand = brand + "abcdefgh";
                if (brand.length() > 8) {
                    brand2 = brand.substring(0, 8);
                }
            } else if (brand.length() == 8) {
                brand2 = brand;
            }
        } else {
            brand2 = "abcdefgh";
        }
        return brand2;
    }

    /**
     * 图片下载
     */
    public static Bitmap downloadImg(String imgUrl) {
        Bitmap img = null;
        if (!TextUtils.isEmpty(imgUrl)) {
            if (imgUrl.startsWith("http")) {
                InputStream is = null;
                try {
                    URL url = new URL(imgUrl);
                    HttpURLConnection urlconn = (HttpURLConnection) url
                            .openConnection();
                    urlconn.setDoInput(true);
                    urlconn.connect();
                    is = urlconn.getInputStream();
                    img = BitmapFactory.decodeStream(is);
                } catch (Throwable e) {
                    e.printStackTrace();
                } finally {
                    closeQuietly(is);
                }
            } else {
                img = readImages(imgUrl);
            }
        }
        return img;
    }

    /**
     * 获取本地保存图片
     */
    public static Bitmap readImages(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return null;
        }
        String name = parseImgUrl(imagePath);
        imagePath = "savePath" + name;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(imagePath);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    /**
     * 解析图片路径
     */
    public static String parseImgUrl(String imgurl) {
        String imgname = "";
        int n = 0;
        if (!TextUtils.isEmpty(imgurl)) {
            if (imgurl.contains("/")) {
                for (int i = 0; i < imgurl.length(); i++) {
                    char m = imgurl.charAt(i);
                    String m1 = String.valueOf(m);
                    if (!TextUtils.isEmpty(m1) && "/".equals(m1)) {
                        n = i;
                    }
                }
                imgname = imgurl.substring(n + 1, imgurl.length());
            } else {
                imgname = imgurl;
            }
        }
        return imgname;
    }

    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (mActivityManager == null) {
            return "";
        }

        List<ActivityManager.RunningAppProcessInfo> lists = mActivityManager.getRunningAppProcesses();
        if (lists != null && !lists.isEmpty()) {
            for (ActivityManager.RunningAppProcessInfo appProcess : lists) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        }
        return "";
    }

    /**
     * 微信是否安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        boolean installed = false;
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(packageName, 0);
            if (info != null) {
                installed = true;
            }
        } catch (Exception e) {
            installed = false;
        }
        return installed;
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstallAPK(Context context, String packageName) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (info != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSupportTouchId(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isRoot()) {
            FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context);
            //如果没有录入指纹就返回false
            if (!managerCompat.hasEnrolledFingerprints()) {
                return false;
            }
            return managerCompat.isHardwareDetected();
        }
        return false;
    }

    public static boolean justSupportTouchId(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isRoot()) {
            FingerprintManagerCompat managerCompat = FingerprintManagerCompat.from(context);
            return managerCompat.isHardwareDetected();
        }
        return false;
    }

    public static boolean isRoot() {
        boolean boolRoot = false;
        try {
            boolRoot = !((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return boolRoot;
    }

    /**
     * 隐藏statusbar
     *
     * @param context
     */
    public static void hideStatusBar(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams layoutParams = context.getWindow().getAttributes();
            layoutParams.flags = (WindowManager.LayoutParams.FLAG_FULLSCREEN | layoutParams.flags);
        }
    }

    /**
     * 生成指定长度随机数字字符串
     */
    public static String getRandom(int length) {
        String radString = null;
        int[] number = new int[length];
        Random random = new Random();
        number[0] = Math.abs(random.nextInt()) % 10;
        radString = String.valueOf(number[0]);
        for (int i = 1; i < length; i++) {
            number[i] = Math.abs(random.nextInt()) % 10;
            radString = radString + String.valueOf(number[i]);
        }
        return radString;
    }

    /**
     * @param type
     * @return 处理微信web标志
     */
    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    /**
     * 判断第三方App是否安装
     */
    public static boolean appIsInstall(Context context, String packageName) {
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(
                    packageName, 0);
            if (info != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean isNoQualifieOne(String string) {
        String str = "(?!^[0-9]+$)(?!^[A-z]+$)(?!^[^A-z0-9]+$)^.{8,12}$";
        Pattern pattern = Pattern.compile(str);
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }


    /**
     * 显示软键盘
     *
     * @param context
     * @param edit
     */
    public static void showSoftInput(Context context, View edit) {
        if (context == null || edit == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && inputManager.isActive(edit)) {
            inputManager.showSoftInput(edit, 0);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param context
     * @param edit
     */
    public static void hideSoftInput(Context context, View edit) {
        if (context == null || edit == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && inputManager.isActive(edit)) {
            inputManager.hideSoftInputFromWindow(edit.getWindowToken(), 0);
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        if (activity == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null && inputManager.isActive()) {
            View focusView = activity.getCurrentFocus();
            if (focusView != null) {
                inputManager.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    public static boolean isSoftInputShow(Activity activity) {
        boolean isShowed = false;
        try {
            if (activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
                isShowed = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isShowed;
    }

    /**
     * 切换软键盘
     */
    public static void toggleSoftInput(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static Bitmap base64ToBitmap(String base64Data) {
        Bitmap bitmap = null;

        try {
            byte[] bitmapArray = android.util.Base64.decode(base64Data, android.util.Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception var3) {
            var3.printStackTrace();
        }

        return bitmap;
    }

    public static void hideDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

    public static boolean checkDialog(Dialog dialog) {
        return dialog != null && dialog.isShowing();
    }

    public static Bitmap drawBg4Bitmap(int color, Bitmap orginBitmap, int width, int height) {
        int width_max = width > orginBitmap.getWidth() ? width : orginBitmap.getWidth();
        int height_max = height > orginBitmap.getHeight() ? height : orginBitmap.getHeight();
        Paint paint = new Paint();
        paint.setColor(color);
        Bitmap bitmap = Bitmap.createBitmap(width_max, height_max, orginBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawRect(0, 0, width_max, height_max, paint);
        canvas.drawBitmap(orginBitmap, (width_max - orginBitmap.getWidth()) / 2,
                (height_max - orginBitmap.getHeight()) / 2, paint);
        recycleBitmap(orginBitmap);
        return bitmap;
    }

    public static void recycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * 检测是否推到后台
     */
    public static boolean isAppBackground(Context context) {
        List<ActivityManager.RunningAppProcessInfo> appPro = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE))
                .getRunningAppProcesses();
        if (appPro != null) {
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appPro) {
                if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (appProcessInfo.processName.equals(context
                            .getPackageName())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);
        }
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }
        if (hideStatusBarBackground) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //如果为全透明模式，取消设置Window半透明的Flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //设置状态栏为透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            //设置window的状态栏不可见
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //如果为半透明模式，添加设置Window半透明的Flag
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
        //view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            ViewCompat.setFitsSystemWindows(mChildView, false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    public static Bitmap captureScreen(Activity context) {
        View cv = context.getWindow().getDecorView();
        Bitmap bmp = Bitmap.createBitmap(cv.getWidth(), cv.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        cv.draw(canvas);
        return bmp;
    }

    /**
     * Closes {@code closeable}, ignoring any checked exceptions. Does nothing if {@code closeable} is
     * null.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPenLocate(final Context context) {
        boolean isOpen = false;
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            isOpen = true;
        }
        return isOpen;
    }

    public static int getAppIcon(Context context) {
        int resId = context.getApplicationInfo().icon;
        if (resId <= 0) {
            resId = context.getResources().getIdentifier("icon", "drawable", context.getApplicationInfo().packageName);
        }
        return resId;
    }

    public static Double parseDouble(String str) {
        double d = 0;
        try {
            if (!TextUtils.isEmpty(str)) {
                d = Double.parseDouble(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Float parseFloat(String str) {
        float d = 0;
        try {
            if (!TextUtils.isEmpty(str)) {
                d = Float.parseFloat(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }


    public static Long parseLong(String str) {
        long d = 0;
        try {
            if (!TextUtils.isEmpty(str)) {
                d = Long.parseLong(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Integer parseInt(String str) {
        Integer d = 0;
        try {
            if (!TextUtils.isEmpty(str)) {
                d = Integer.parseInt(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    public static Boolean parseBoolean(String str) {
        Boolean d = false;
        try {
            if (!TextUtils.isEmpty(str)) {
                d = Boolean.parseBoolean(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    //判断网络连接是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            //如果仅仅是用来判断网络连接，则可以使用
            NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
            if (allNetworkInfo != null) {
                for (int i = 0; i < allNetworkInfo.length; i++) {
                    if (allNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}


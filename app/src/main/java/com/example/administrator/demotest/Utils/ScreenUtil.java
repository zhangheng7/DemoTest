package com.example.administrator.demotest.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zhangheng
 * @date 2018/5/31
 */

public class ScreenUtil {
    /**
     * dip转换px
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px转换dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getDisplayWidth(Context context) {
        if (context == null) {
            return 0;
        }
        int width = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        try {
            Class<?> cls = Display.class;
            Class<?>[] parameterTypes = {
                    Point.class
            };
            Point parameter = new Point();
            Method method = cls.getMethod("getSize", parameterTypes);
            method.invoke(display, parameter);
            width = parameter.x;
        } catch (Exception e) {
            width = display.getWidth();
        }
        return width;
    }

    /**
     * 获取屏幕高
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int getDisplayHeight(Context context) {
        if (context == null) {
            return 0;
        }
        int height = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        try {
            Class<?> cls = Display.class;
            Class<?>[] parameterTypes = {
                    Point.class
            };
            Point parameter = new Point();
            Method method = cls.getMethod("getSize", parameterTypes);
            method.invoke(display, parameter);
            height = parameter.y;
        } catch (Exception e) {
            height = display.getHeight();
        }
        return height;
    }

    /**
     * 获取系统bar条高
     *
     * @param activity
     * @return
     */
    public static int getStatusBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        int statusHeight = 0;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusHeight = frame.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = activity.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


    public static int getDisplayWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getDisplayHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size.x;
    }

    public int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        return size.y;
    }
    public static boolean isConcaveScreens(Context context) {
        if (Build.VERSION.SDK_INT < 26) {
            return false;
        }

        String model = Build.BRAND.toLowerCase();
        if (context == null || context.getPackageManager() == null || TextUtils.isEmpty(model)) {
            return false;
        }

        if (TextUtils.equals(model, "oppo")) {
            return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
        } else if (TextUtils.equals(model, "vivo")) {
            return checkVivoConcave(context);
        } else if (TextUtils.equals(model, "huawei")) {
            return checkHuaweiConcave(context);
        } else if (TextUtils.equals(model, "xiaomi")) {
            return checkXiaomiConcave();
        }

        return false;
    }
    private static boolean checkXiaomiConcave() {
        return TextUtils.equals(SystemProperties.get("ro.miui.notch"), "1");

    }

    private static boolean checkVivoConcave(Context context) {
        try {
            ClassLoader cl = context.getClassLoader();
            Class FtFeature = cl.loadClass("com.util.FtFeature");
            Method get = FtFeature.getMethod("isFeatureSupport", int.class);
            return (boolean) get.invoke(FtFeature, 0x00000020);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkHuaweiConcave(Context context) {
        try {
            ClassLoader cl = context.getClassLoader();
            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = hwNotchSizeUtil.getMethod("hasNotchInScreen");
            boolean hasNotchInScreen = (boolean) get.invoke(hwNotchSizeUtil);
            boolean isDisplayNotch = isDisplayNotch(context);
            return hasNotchInScreen && isDisplayNotch;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }
    public static class SystemProperties {
        public static String get(String key) {
            String value = "";
            Class<?> cls = null;
            try {
                cls = Class.forName("android.os.SystemProperties");
                Method hideMethod = cls.getMethod("get", String.class);
                Object object = cls.newInstance();
                value = (String) hideMethod.invoke(object, key);
            } catch (ClassNotFoundException e) {
                Log.e("error", e.toString());
            } catch (NoSuchMethodException e) {
                Log.e("error", e.toString());
            } catch (InstantiationException e) {
                Log.e("error", e.toString());
            } catch (IllegalAccessException e) {
                Log.e("error", e.toString());
            } catch (IllegalArgumentException e) {
                Log.e("error", e.toString());
            } catch (InvocationTargetException e) {
                Log.e("error", e.toString());
            } catch (ClassCastException e) {
                Log.e("error", e.toString());
            }
            return value;
        }
    }
    private static boolean isDisplayNotch(Context context) {
        int setting = Settings.Secure.getInt(context.getContentResolver(), "display_notch_status", 0);
        return setting == 0;
    }
    public static int[] getNotchSize(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = hwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(hwNotchSizeUtil);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static int getXiaomiNotchSize(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("notch_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getOPPONotchSize() {
        String mProperty = SystemProperties.get("ro.oppo.screen.heteromorphism");
        return mProperty;
    }
}

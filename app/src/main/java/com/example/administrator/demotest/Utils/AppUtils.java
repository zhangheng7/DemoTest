package com.example.administrator.demotest.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangheng
 * @date 2018/12/6
 */

public class AppUtils {
    private static final String TAG = "AppUtils";
    public static final int INSTALL_PACKAGES_REQUESTCODE = 1111;

    public static boolean openApp(Context context, String packagename) {
        if (TextUtils.isEmpty(packagename)) {
            return false;
        }
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if (packageinfo == null) {
            return false;
        }

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);

        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = context.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);

        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packagename = 参数packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packagename.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // 设置ComponentName参数1:packagename参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);

        }
        return true;
    }

    public static boolean isOPenGPS(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return false;
        }
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return gps || network;
    }

    public static boolean isCaptchaValid(String smsCode) {
        if (StringUtils.emptyString(smsCode)) {
            return false;
        }
        String regEx = "^[0-9]{6}$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(smsCode);
        return matcher.matches();
    }

    public static boolean isAccountValid(String account) {
        if (StringUtils.emptyString(account)) {
            return false;
        }
        String regEx = "^[#~$@!*^<>\"%&]$";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(account);
        return matcher.matches();
    }

    /**
     * 验证密码长度
     *
     * @param pwd
     * @return
     */
    public static boolean isPwdLenValid(String pwd) {
        return !(StringUtils.emptyString(pwd) || pwd.length() < 8 || pwd.length() > 12);
    }

    /**
     * 验证密码是否至少包含数字、字母、符号中两种
     *
     * @param pwd
     * @return
     */
    public static boolean isPwdLetterValid(String pwd) {
        if (StringUtils.emptyString(pwd)) {
            //小于8位不在此判断
            return false;
        }
        // 密码含有数字
        int hasNum = 0;
        // 密码含有字母
        int hasLetter = 0;
        // 密码含有除数字和字母之外的其他字符
        int hasOtherChar = 0;

        int len = pwd.length();
        for (int i = 0; i < len; i++) {
            if ('0' <= pwd.charAt(i) && pwd.charAt(i) <= '9') {
                hasNum = 1;
            } else if (('a' <= pwd.charAt(i) && pwd.charAt(i) <= 'z')
                    || ('A' <= pwd.charAt(i) && pwd.charAt(i) <= 'Z')) {
                hasLetter = 1;
            } else {
                hasOtherChar = 1;
            }
        }
        return hasNum + hasLetter + hasOtherChar >= 2;
    }

    public static boolean isComplexPwd(String pwd) {
        // 设密码长度为m位。M为判断简单的基础值，当m小于等于6时M为6，当m大于6时，M为6 + (m-6) *
        // 1.5。X为判断简单的评估值，初值为零。密码的第n位（1 <= n <=
        // m-1）与第n+1位进行比较：如果第n位与第n+1位相同，则X累加2；如果第n位与第n+1位相差1，则X累加1.5。整个密码比较完毕后，X是否大于等于M为简单密码的判断条件
        if (!StringUtils.emptyString(pwd)) {
            int num = 0; // 重复。
            int decNum = 0;// 递减。
//            int incNum = 0;// 递增。

            int len = pwd.length();
            float m = 0;
            if (len <= 6) {
                m = 6;
            } else {
                m = (float) (6 + (len - 6) * 1.5);
            }
            for (int i = 0; i < len; i++) {
                if (i < len - 1) {
                    if (pwd.charAt(i) == pwd.charAt(i + 1)) {
                        num++;
                    }
                    if (Math.abs(pwd.charAt(i) - pwd.charAt(i + 1)) == 1) {
                        decNum++;
                    }
                    /*if (pwd.charAt(i) == pwd.charAt(i + 1) - 1) {
                        decNum++;
                    }
                    if (pwd.charAt(i) == pwd.charAt(i + 1) + 1) {
                        incNum++;
                    }*/
                }
            }

            if ((num * 2 + (decNum /*+ incNum*/) * 1.5) >= m) {
                return false;
            }
        }
        return true;
    }

    public static boolean isPwdNotContainsName(String pwd, String name) {
        if (!StringUtils.emptyString(pwd) && pwd.length() >= 8) {//小于8位不在此判断
            int len = pwd.length();
            if (!StringUtils.emptyString(name)) {
                if (pwd.contains(name)) {
                    // 整个用户名与密码的一部分连续相同—当用户名长度小于密码时，用户名与密码的某一段连续部分相同
                    return false;
                } else {
                    // 密码的某一段连续的组成字符与用户名的某一段连续的组成字符（含全部）相同，且该连续密码字符段的长度为m-k（m为密码长度，0<=k<=3）
                    for (int k = 3; k >= 0; k--) {
                        if (name.contains(pwd.substring(3 - k, len - k))) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static String getTransFlowNo() {
        return System.currentTimeMillis() + (Math.random() + "00000000000").substring(2, 9);
    }

    public static String getMobileSuffex(String mobile) {
        if (StringUtils.emptyString(mobile) || mobile.length() < 11) {
            return "";
        }
        return mobile.substring(mobile.length() - 4);
    }

    public static String encodeUrl(String data) {
        String result = "";
        try {
            result = URLEncoder.encode(data, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 恢复亮度
     *
     * @param activity
     */
    public static void recoverBrightness(final Activity activity) {
        try {
            float systemBrightness = Settings.System.getInt(
                    activity.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
            Window window = activity.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.screenBrightness = systemBrightness;
            window.setAttributes(lp);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置亮屏
     *
     * @param activity
     * @param brightness
     */
    public static void setBrightness(Activity activity, float brightness) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness;
        window.setAttributes(lp);
    }

    /**
     * 判断某一个类是否存在任务栈里面
     *
     * @return
     */
    public static boolean isExistMainActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
        boolean flag = false;
        if (cmpName != null) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (am != null) {
                List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
                for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                    if (taskInfo.baseActivity.equals(cmpName)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 调用系统安装器安装apk:兼容compileSDKVersion 7.0+
     *
     * @param activity 上下文
     * @param filePath apk文件
     */
    public static void installApk(Activity activity, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(activity, "com.cs_credit_bank.fileprovider", new File(filePath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(new File(filePath));
            intent.setType("application/vnd.android.package-archive");
        }
        intent.setData(data);
        activity.startActivity(intent);
    }

    public static void checkInstallApk(Activity activity, String filePath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean b = activity.getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk(activity, filePath);
            } else {
                //请求安装未知应用来源的权限 INSTALL_PACKAGES_REQUESTCODE
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            installApk(activity, filePath);
        }
    }
}

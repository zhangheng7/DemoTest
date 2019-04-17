package com.example.administrator.demotest.Utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @author zhangheng
 */
public class DeviceUtil {
    private static final String TAG = "DeviceUtil";

    public static String getLocalHostIp() {
        String ipaddress = "";
        Enumeration<NetworkInterface> en;
        try {
            en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress() && ip instanceof Inet4Address) {
                        ipaddress = ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(ipaddress)) {
            ipaddress = "127.0.0.1";
        }
        return ipaddress;
    }

    public static String getMacAddress(Context context) {
        // 获取devicetoken
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi != null) {
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        }
        return "";
    }

    /**
     * 获取系统版本
     *
     * @return
     */
    public static String getDeviceSystemVersion() {
        String version = android.os.Build.VERSION.RELEASE;
        if (null != version) {
            version = version.replace(' ', '-');
        } else {
            version = "";
        }
        return version;
    }

    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getDeviceModel() {
        String model = android.os.Build.MODEL;
        if (null != model) {
            model = model.replace(' ', '-');
        } else {
            model = "";
        }
        return model;
    }

    public static String getUUID(Context context) {
        try {
            PackageManager pkgManager = context.getPackageManager();
            boolean readPhoneStatePermission = pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE,
                    context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
            String token = "";
            String imsi = "";
            String simNumber = "";
            String macAddress = "";

            if (readPhoneStatePermission) {
                // 获取devicetoken
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Activity.TELEPHONY_SERVICE);
                if (tm != null) {
                    token = tm.getDeviceId();
                    // 获取IMSI
                    imsi = tm.getSubscriberId();
                    // 获取SIM卡序列号
                    simNumber = tm.getSimSerialNumber();
                }
                // 获取MAC地址
                WifiManager wifi = (WifiManager) context.getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                if (wifi != null) {
                    WifiInfo info = wifi.getConnectionInfo();
                    macAddress = info.getMacAddress();
                }

                if (!TextUtils.isEmpty(token)) {
                    if (!TextUtils.isEmpty(imsi)) {
                        // 若IMSI不为空，则使用deviceID拼接IMSI号作为设备唯一标识上传，不再拼接其他标识；
                        token = token.concat(imsi.trim());
                    } else if (!TextUtils.isEmpty(simNumber)) {
                        // 若IMSI为空，但sim卡序列号不为空，则使用deviceID拼接sim卡序列号作为设备唯一标识上传，不再拼接其他标识；
                        token = token.concat(simNumber.trim());
                    } else if (!TextUtils.isEmpty(macAddress)) {
                        // 若IMSI和sim卡序列号都为空，但MAC address不为空，则使用deviceID拼接MAC
                        // address作为设备唯一标识上传，不再拼接其他标识；
                        token = token.concat(macAddress.trim());
                    }
                    token = token.replace(' ', '-');
                    token = token.replace(':', '-');
                } else {
                    token = Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                }
            } else {
            }
            token = replacChar(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String replacChar(String str) {
        if (str == null) {
            return "";
        }
        String parseStr = "[`~!@#$%^&*+=|{}':;'\"\\[\\]<>/?%—|{}‘”“’]";
        return str.replaceAll(parseStr, "");
    }
    //版本号s

    public static int getVersionCode(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static String getVersionName(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";

    }

    //获取电话号码
    public static String getNativePhoneNumber(Context context) {
        if (context == null) {
            return "";
        }
        String nativePhoneNumber = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                nativePhoneNumber = telephonyManager.getLine1Number();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nativePhoneNumber;
    }

    //获取IMEL
    public static String getIMEL(Context context) {
        if (context == null) {
            return "";
        }
        String meid = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    meid = telephonyManager.getMeid();
                } else {
                    meid = telephonyManager.getDeviceId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meid;
    }

    /**
     * true为平板，false为手机
     * @param context
     * @return
     */
    public static boolean isPhoneOrPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}

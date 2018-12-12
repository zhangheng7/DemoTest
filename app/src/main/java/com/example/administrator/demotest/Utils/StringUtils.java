package com.example.administrator.demotest.Utils;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * @author zhangheng
 * @date 2018/5/31
 */

public class StringUtils {
    public static String toUtf8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断是否是空字符串
     *
     * @param string 字符串
     * @return true 是； false 否
     */
    public static boolean emptyString(String string) {
        if (string == null || string.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 字符串格式化
     *
     * @param string 字符串
     * @return 非空字符串
     */
    public static String safeString(String string) {
        return (string != null) ? string : "";
    }

    /**
     * 字符串格式化
     *
     * @param string 字符串
     * @return 去除空格的非空字符串
     */
    public static String safeTrimString(String string) {
        if (string == null) {
            return "";
        }
        return string.trim();
    }

    /**
     * 获得汉语拼音首字母
     *
     * @param str
     * @return
     */
    public static String getFirstLetter(String str) {
        if (str == null) {
            return "#";
        }
        if (str.trim().length() == 0) {
            return "#";
        }
        char c = str.trim().substring(0, 1).charAt(0);
        // 正则表达式，判断首字母是否是英文字母
        Pattern pattern = Pattern.compile("^[A-Za-z]+$");
        if (pattern.matcher(c + "").matches()) {
            return (c + "").toUpperCase();
        } else if (str.equals("0")) {
            return "定";
        } else if (str.equals("1")) {
            return "历";
        } else if (str.equals("2")) {
            return "热";
        } else {
            return "#";
        }
    }

    /**
     * 手机号脱敏
     *
     * @param mobileNo
     * @return
     */
    public static String protectMobileNo(String mobileNo) {
        if (TextUtils.isEmpty(mobileNo))
            return "";

        String str1 = mobileNo.substring(0, 3);
        String str2 = "****";
        String str3 = mobileNo.substring(mobileNo.length() - 4, mobileNo.length());
        return str1 + str2 + str3;
    }

    /**
     * 判断是否为网址
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if (TextUtils.isEmpty(url))
            return false;

        Pattern pattern = Pattern
                .compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
        return pattern.matcher(url).matches();
    }

    public static String mergeEvents(String... event) {
        String events = "";
        for (String item : event) {
            events += "|" + item;
        }
        if (events.length() > 0) {
            events = events.substring(1);
        }
        return events;
    }

    public static String getSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                    + String.valueOf((size % 100)) + "GB";
        }
    }

    public static boolean isSimplePwd(String pwd) {
        //简单密码,密码等于6位数,则为简单密码
        if (!StringUtils.emptyString(pwd) && pwd.length() == 6) {
            return true;
        } else {
            return false;
        }
    }

    // 判断是否符合身份证号码的规范
    public static boolean isIDCard(String IDCard) {
        if (IDCard != null) {
            String IDCardRegex = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)";
            return IDCard.matches(IDCardRegex);
        }
        return false;
    }
}


package com.example.administrator.demotest.Utils;

/**
 * @author zhangheng
 * @date 2018/5/31
 */

public class NumberUtil {
    /**
     * string to double
     *
     * @param value String
     * @return double
     */
    public static double parseDouble(String value) {
        double ret = 0.0;

        if (!StringUtils.emptyString(value)) {
            try {
                ret = Double.parseDouble(value);
            } catch (NumberFormatException e) {
            }
        }

        return ret;
    }

    /**
     * string to long
     *
     * @param value String
     * @return long
     */
    public static long parseLong(String value) {
        long ret = 0;

        if (!StringUtils.emptyString(value)) {
            try {
                ret = Long.parseLong(value);
            } catch (NumberFormatException e) {
            }
        }
        return ret;
    }

    /**
     * string to int
     *
     * @param value String
     * @return int
     */
    public static int parseInt(String value) {
        int ret = 0;

        if (!StringUtils.emptyString(value)) {
            try {
                ret = Integer.parseInt(value);
            } catch (NumberFormatException e) {
            }
        }
        return ret;
    }

    /**
     * string to float
     *
     * @param value String
     * @return float
     */
    public static float parseFloat(String value) {
        float ret = 0.0f;

        if (!StringUtils.emptyString(value)) {
            try {
                ret = Float.parseFloat(value);
            } catch (NumberFormatException e) {
            }
        }

        return ret;
    }
}

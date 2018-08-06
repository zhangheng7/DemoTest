package com.example.administrator.demotest.Utils;

import android.os.Looper;
import android.text.TextUtils;

/**
 * @author zhangheng
 * @date 2018/5/31
 */

public class CheckUtil {
    private static boolean sEnable = false;

    /**
     * Check condition for DEBUG
     */
    public static void d(boolean condition) {
        d(condition, "");
    }

    /**
     * Check condition with hint for DEBUG
     */
    public static void d(boolean condition, String hint) {
        if (condition == false) {
            if (sEnable) {
                throw new AssertionError(hint);
            }
        }
    }

    /**
     * Check condition with target object for DEBUG
     */
    public static void d(boolean condition, Object errorTarget) {
        if (condition == false) {
            if (sEnable) {
                throw new AssertionError(errorTarget.toString());
            }
        }
    }

    /**
     * Throw the throwable for DEBUG
     */
    public static void d(String detailMsg, Throwable throwable) {
        if (sEnable) {
            throw new RuntimeException(detailMsg, getRootCause(throwable));
        }
    }

    /**
     * Throw the throwable for DEBUG
     */
    public static void d(Throwable throwable) {
        if (sEnable) {
            throw new RuntimeException(getRootCause(throwable));
        }
    }

    /**
     * Check condition for RELEASE/DEBUG
     */
    public static void r(boolean condition) {
        r(condition, "");
    }

    /**
     * Check condition for RELEASE/DEBUG
     */
    public static void r(boolean condition, String hint) {
        if (condition == false) {
            throw new AssertionError(hint);
        }
    }

    /**
     * Check condition with target object for RELEASE/DEBUG
     */
    public static void r(boolean condition, Object errorTarget) {
        if (condition == false && sEnable) {
            throw new AssertionError(errorTarget.toString());
        }
    }

    /**
     * Throw the throwable for RELEASE/DEBUG
     */
    public static void r(Throwable throwable) {
        throw new RuntimeException(getRootCause(throwable));
    }

    private static Throwable getRootCause(Throwable throwable) {
        while (throwable.getCause() != null && throwable != throwable.getCause()) {
            throwable = throwable.getCause();
        }
        return throwable;
    }

    public static final void mustOk(boolean aOk) {
        mustOk(aOk, null);
    }

    public static final void mustOk(boolean aOk, String aErrMsg) {
        if (sEnable) {
            if (!aOk) {
                if (null != aErrMsg) {
                    assertDie(aErrMsg);
                } else {
                    assertDie();
                }
            }
        }
    }

    public static void mustNotNull(Object aObj) {
        mustNotNull(aObj, null);
    }

    public static void mustNotNull(Object aObj, String aErrMsg) {
        mustOk(null != aObj, aErrMsg);
    }

    public static void fail(String aMsg) {
        mustOk(false, aMsg);
    }

    public static void fail() {
        fail(null);
    }


    public static void mustInUiThread(String aErrMsg) {
        if (sEnable) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                if (null != aErrMsg) {
                    assertDie(aErrMsg);
                } else {
                    assertDie();
                }
            }
        }
    }

    public static void mustNotInUiThread(String aErrMsg) {
        if (sEnable) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                if (null != aErrMsg) {
                    assertDie(aErrMsg);
                } else {
                    assertDie();
                }
            }
        }
    }

    public static void mustNotEmpty(String aStr) {
        mustOk(!TextUtils.isEmpty(aStr));
    }

    private static void assertDie() {
        assertDie(null);
    }

    private static void assertDie(String msg) {
        if (msg == null) {
            throw new Error();
        } else {
            throw new Error(msg);
        }
    }
}

package com.example.administrator.demotest.Utils;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.example.administrator.demotest.banner.GlideCatchConfig;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author zhangheng
 * @date 2019/1/15
 */

public class GlideCatchUtil {
    private static GlideCatchUtil instance;

    public static GlideCatchUtil getInstance() {
        if (null == instance) {
            instance = new GlideCatchUtil();
        }
        return instance;
    }

    public long getTotalCacheSize(Context context) {
        long cacheSize = 0;
        try {
            cacheSize = getFolderSize(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFolderSize(context.getExternalCacheDir());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cacheSize;
    }

    /**
     * 获取Glide磁盘缓存大小
     *
     * @param context
     * @return
     */
    public long getImgCacheSize(Context context) {
        try {
            return getFolderSize(new File(context.getCacheDir() + "/" + GlideCatchConfig.GLIDE_CARCH_DIR));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
     *
     * @param context
     * @return
     */
    public boolean cleanCatchDisk(Context context) {
        return deleteFolderFile(context.getCacheDir() + "/" + GlideCatchConfig.GLIDE_CARCH_DIR, true);
    }

    /**
     * 清除图片磁盘缓存，调用Glide自带方法
     *
     * @param context
     * @return
     */
    public boolean clearCacheDiskSelf(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 清除Glide内存缓存
     *
     * @param context
     * @return
     */
    public boolean clearCacheMemory(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file
     * @return
     * @throws Exception
     */
    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return 0 + "KB";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    /**
     * 按目录删除文件夹文件方法
     *
     * @param filePath
     * @param deleteThisPath
     * @return
     */
    private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        try {
            File file = new File(filePath);
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (File file1 : files) {
                    deleteFolderFile(file1.getAbsolutePath(), true);
                }
            }
            if (deleteThisPath) {
                if (!file.isDirectory()) {
                    file.delete();
                } else {
                    File[] lists = file.listFiles();
                    if (lists != null && lists.length == 0) {
                        file.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}

package com.example.administrator.demotest.Utils;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import okhttp3.internal.Util;

/**
 * @author zhangheng
 * @date 2018/5/31
 */

public class FileUtil {
    private final static String TAG = FileUtil.class.getSimpleName();

    /*
    *
    * 判断文件是否存在
    *
    * **/
    public static boolean isFileExist(String filePath) {
        File f = new File(filePath);
        return f.exists();
    }

    /*
     * 新建文件
     *
     * @param parentFile 文件的上级目录
     * @param fileName 文件的名称
     *
     * **/
    public static void createNewFile(String parentfolder, String fileName) {

        File fileDir = new File(parentfolder);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        } else {
            File f = new File(parentfolder + fileName);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /*
     * 从assets目录下拷贝文件至指定目录
     *
     * @param context
     * @param sourceFileName 源文件名称
     * @param targetFileFolder  目标文件夹
     * @param targetFileName新文件的名称
     *
     * @return boolean true 拷贝成功；false 拷贝失败
     * **/
    public static boolean copyFile(Context context, String sourceFileName, String targetFileFolder, String targetFileName) {

        boolean flag = false;

        //如果文件不存在，则新建文件
        if (!isFileExist(targetFileFolder + targetFileName))
            createNewFile(targetFileFolder, targetFileName);

        InputStream assetsDB = null;
        OutputStream dbOut = null;
        try {
            assetsDB = context.getResources().getAssets().open(sourceFileName);
            dbOut = new FileOutputStream(targetFileFolder + targetFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = assetsDB.read(buffer)) > 0) {
                dbOut.write(buffer, 0, length);
            }

            dbOut.flush();
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(dbOut);
            Util.closeQuietly(assetsDB);
        }

        return flag;
    }

    /**
     * 获取单个文件的MD5is值！
     *
     * @return
     */
    public static String getFileMD5(String filePath) {
        if (!isFileExist(filePath)) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            in = new FileInputStream(new File(filePath));
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            Util.closeQuietly(in);
        }

        byte[] byteArray = digest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        //将加密后的byte数组转换为十六进制的字符串,否则的话生成的字符串会乱码
        for (int i = 0; i < byteArray.length; i++) {
            //
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                //
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                //
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    /**
     * 获取单个文件的MD5值！
     *
     * @return
     */
    public static String getFileMD5(InputStream in) {

        if (in == null) return null;
        MessageDigest digest = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.reset();
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            Util.closeQuietly(in);
        }

        //BigInteger bigInt = new BigInteger(1, digest.digest());
        byte[] byteArray = digest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        //将加密后的byte数组转换为十六进制的字符串,否则的话生成的字符串会乱码
        for (int i = 0; i < byteArray.length; i++) {
            //
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                //
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param path 要删除的根目录
     */
    public static void deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f.getAbsolutePath());
                }
                file.delete();
            }
        } catch (Exception e) {
        }
    }

    /*
     * 压缩zip文件至设定的目录
     *
     * @param zipFileName:待解压缩的zip路径
     * @param outputDirectory:解压缩的目标路径
     *
     * @return
     *
     * **/
    public static void unzip(String zipFileName, String outputDirectory) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(zipFileName);
            Enumeration<? extends ZipEntry> e = zipFile.entries();
            ZipEntry zipEntry = null;
            File dest = new File(outputDirectory);
            dest.mkdirs();
            while (e.hasMoreElements()) {
                zipEntry = e.nextElement();
                String entryName = zipEntry.getName();
                InputStream in = null;
                FileOutputStream out = null;
                try {
                    if (zipEntry.isDirectory()) {
                        String name = zipEntry.getName();
                        name = name.substring(0, name.length() - 1);
                        File f = new File(outputDirectory + File.separator + name);
                        f.mkdirs();
                    } else {
                        int index = entryName.lastIndexOf("\\");
                        if (index != -1) {
                            File df = new File(outputDirectory + File.separator
                                    + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        index = entryName.lastIndexOf("/");
                        if (index != -1) {
                            File df = new File(outputDirectory + File.separator
                                    + entryName.substring(0, index));
                            df.mkdirs();
                        }
                        File f = new File(outputDirectory + File.separator
                                + zipEntry.getName());
                        // f.createNewFile();
                        in = zipFile.getInputStream(zipEntry);
                        out = new FileOutputStream(f);
                        int c;
                        byte[] by = new byte[1024];
                        while ((c = in.read(by)) != -1) {
                            out.write(by, 0, c);
                        }
                        out.flush();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new IOException("解压失败：" + ex.toString());
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ex) {
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            //throw new IOException("解压失败：" + ex.toString());
        } finally {
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    /**
     * 从assets读取文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readAssets(Context context, String fileName) {
        //读取本地预置appList配置
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Util.closeQuietly(is);
        }
        return "";
    }
}

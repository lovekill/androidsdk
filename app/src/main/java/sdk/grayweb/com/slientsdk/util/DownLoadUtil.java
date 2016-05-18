package sdk.grayweb.com.slientsdk.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;

/**
 * 作者： Mr.wrong on 2016/5/12 17 19
 */
public class DownLoadUtil {
    public static final String dir = "downloadapk";

    public static void initDir() {
        String dirName = Environment.getExternalStorageDirectory() + "/" + dir;
        File file = new File(dirName);
        if (file.exists()) return;
        file.mkdir();
    }

    public static boolean hasDown(String newUrl) {
        final String md5Name1 = getMd5(newUrl);
        final String path = Environment.getExternalStorageDirectory() + "/" + dir + "/" + md5Name1 + "." + "apk";
        final File file = new File(path);
        return file.exists();
    }


    public static String getTempFileNameByUrl(String url) {
        final String md5Name1 = getMd5(url);
        String path = Environment.getExternalStorageDirectory() + "/" + dir + "/" + md5Name1 + "_temp." + "apk";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        return path;
    }
    public static String getFileNameByUrl(String url) {
        final String md5Name1 = getMd5(url);
        String path = Environment.getExternalStorageDirectory() + "/" + dir + "/" + md5Name1 +".apk";
        File file = new File(path);
        return path;
    }

    public static int getFileCout() {
        String path = Environment.getExternalStorageDirectory() + "/" + dir;
        File file = new File(path);
        return file.listFiles().length;
    }


    private static MessageDigest md5 = null;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static String getMd5(String str) {
        byte[] bs = md5.digest(str.getBytes());
        StringBuilder sb = new StringBuilder(40);
        for (byte x : bs) {
            if ((x & 0xff) >> 4 == 0) {
                sb.append("0").append(Integer.toHexString(x & 0xff));
            } else {
                sb.append(Integer.toHexString(x & 0xff));
            }
        }
        return sb.toString();
    }


}

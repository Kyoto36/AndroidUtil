package com.ls.comm_util_library;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class AppUtils {
    /**
     * 获取app版本号
     *
     * @param context
     * @return 版本号versionname，不是versioncode
     */
    public static String getAppVerCode(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0";
    }

    /**
     * 获取app名称
     *
     * @param context
     * @param pID     进程ID
     * @return
     */
    public static String getAppName(Context context, int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    /**
     * 获取application下的matedata数据（String数据）
     *
     * @param context
     * @param keyName
     * @return
     */
    public static String getMetaDataStringFromApplication(Context context, String keyName) {
        String value = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取application下的matedata数据(int数据)
     *
     * @param context
     * @param keyName
     * @return
     */
    public static int getMetaDataIntFromApplication(Context context, String keyName) {
        int value = -1;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getInt(keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 获取application下的matedata数据(long数据)
     *
     * @param context
     * @param keyName
     * @return
     */
    public static long getMetaDataLongFromApplication(Context context, String keyName) {
        long value = -1;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getLong(keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 退出app
     */
    public static void exitApp(Context context) {
        exitApp(context, "双击退出");
    }

    private static long sExitTime;

    public static void exitApp(Context context, String msg) {
        if (System.currentTimeMillis() - sExitTime > 2000) {
            Util.Companion.toast(context, msg);
            sExitTime = System.currentTimeMillis();
        } else {
            sExitTime = 0;
            if (context instanceof Activity) {
                UtilActivityManager.exitApp((Activity) context);
            } else {
                UtilActivityManager.exitApp();
            }
        }
    }

    /**
     * 获取APP签名MD5
     * @param context
     * @return
     */
    public static String getAppSignMD5(Context context) {
        try {
            X509Certificate cert = getCertificate(context);
            if(cert != null) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                // 获得公钥
                byte[] b = md.digest(cert.getEncoded());
                //key即为应用签名
                return byte2HexFormatted(b).replace(":", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获得app 的sha1值
     * @param context
     * @return
     */
    public static String getAppSignSha1(Context context) {
        try {
            X509Certificate cert = getCertificate(context);
            if (cert != null) {
                MessageDigest md = MessageDigest.getInstance("SHA1");
                // 获得公钥
                byte[] b = md.digest(cert.getEncoded());
                return byte2HexFormatted(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取APP签名证书
     * @param context
     * @return
     */
    private static X509Certificate getCertificate(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            // X509证书，X.509是一种非常通用的证书格式
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(sign.toByteArray()));
            return cert;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将获取到得编码进行16进制转换
     *
     * @param arr
     * @return
     */
    private static String byte2HexFormatted(byte[] arr) {
        StringBuilder str = new StringBuilder(arr.length * 2);
        for (int i = 0; i < arr.length; i++) {
            String h = Integer.toHexString(arr[i]);
            int l = h.length();
            if (l == 1)
                h = "0" + h;
            if (l > 2)
                h = h.substring(l - 2, l);
            str.append(h.toUpperCase());
            if (i < (arr.length - 1))
                str.append(':');
        }
        return str.toString();


    }
}
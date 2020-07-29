package com.ls.comm_util_library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class IntentUtils {
    /**
     * 模仿Launcher打开app，避免每次打开都是重新启动
     * @param context
     * @return
     */
    public static Intent getLauncherIntent(Context context){
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setComponent(context.getPackageManager().getLaunchIntentForPackage(context.getPackageName()).getComponent());
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }

    /**
     * 有些功能需要点击启动，所以向外提供intent 如下载完成点击安装
     * @param context
     * @param file
     * @param authorities
     * @return
     */
    public static Intent getApkInstallIntent(Context context,File file,String authorities){
        Uri apkUri;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            apkUri = FileProvider.getUriForFile(context, authorities, file);
        }else{
            apkUri = Uri.fromFile(file);
        }
        return getApkInstallIntent(apkUri);
    }

    public static Intent getApkInstallIntent(Uri apkUri){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //判读版本是否在7.0以上
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }else{
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        return intent;
    }

    /**
     *
     * @param context
     * @param file apk文件
     * @param authorities manifest中定义的fileprovider的authorities
     */
    public static void startApkInstall(Context context,File file,String authorities){
        context.startActivity(getApkInstallIntent(context, file, authorities));
    }

    /**
     * 启动activity并且清除栈中其他所有activity
     * 注: 使用了 {@link Intent#FLAG_ACTIVITY_NEW_TASK} 新开一个栈
     * @param context
     * @param clazz
     */
    public static void startActivityAndClearTask(Context context,Class<? extends Activity> clazz){
        Intent intent = new Intent(context,clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

package com.wtjr.lqg.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionUtils {
    /**
     * 是否有打电话权限
     *
     * @param context
     * @return
     */
    public static boolean callPhone(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.CALL_PHONE", context.getPackageName());
        return permission;
    }

    /**
     * 是否有读取手机状态权限
     *
     * @param context
     * @return
     */
    public static boolean readPhoneState(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName());
        return permission;
    }


    /**
     * 是否有定位权限
     *
     * @param context
     * @return
     */
    public static boolean location(Context context) {
        PackageManager pm = context.getPackageManager();
        boolean permission1 = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", context
                .getPackageName());
//        boolean permission2 = PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", context
// .getPackageName());
        Log.v("LogContent", "permission1=" + permission1);
//        Log.v("LogContent", "permission2="+permission2);
        return permission1;
    }

    /**
     * 动态获取6.0版本以上手机存储权限
     */
    public static void writerReadSDcard(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission
                    .WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission
                        .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    /**
     * 动态获取Android6.0版本以上手机电话权限
     */
    public static void requestPhonePermission(Context context) {

        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
    }
}

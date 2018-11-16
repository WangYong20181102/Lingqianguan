package com.wtjr.lqg.sharedpreferences;


import android.content.Context;

/**
 * 第一次安装或者更新的本地状态SP
 *
 * @author dell
 */
public class SaveIsFirstRun {
    /**
     * 获得第一次安装或者更新的本地状态SP
     */
    public static boolean getIsFirstRun(Context context) {
        return SharedPreferencesUtil.getPrefBoolean(context, "IsFirstRun", true);
    }

    /**
     * 设置是第一次安装或者更新的本地状态SP
     * true 打开引导页
     */
    public static void setIsFirstRun(Context context) {
        SharedPreferencesUtil.setPrefBoolean(context, "IsFirstRun", true);
    }

    /**
     * 设置不是第一次安装或者更新的本地状态SP
     * false 不打开引导页
     */
    public static void setUnFirstRun(Context context) {
        SharedPreferencesUtil.setPrefBoolean(context, "IsFirstRun", false);
    }

}

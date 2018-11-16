package com.wtjr.lqg.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    /**
     * 没有网络
     */
    public static final int NETWORK_NONE = 0;
    /**
     * WIFI状态下
     */
    public static final int NETWORK_WIFI = 1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 2;

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断WIFI是否打开
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    /**
     * 判断3G网络是否打开
     *
     * @param context
     * @return
     */
    public static boolean is3Grd(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    /**
     * 判断网络类型
     *
     * @param context
     * @return
     */
    public static int getNetworkState(Context context) {
        if (isWifiEnabled(context)) {
            return NETWORK_WIFI;
        } else if (is3Grd(context)) {
            return NETWORK_MOBILE;
        }
        return NETWORK_NONE;
    }


    /**
     * 判断本版大小
     *
     * @param oldVersion 旧版本
     * @param newVersion 新版本
     * @return -1(newVersion<oldVersion)<p>1(newVersion>oldVersion)<p>0(newVersion=oldVersion)
     */
    public static int compareVersion(String oldVersion, String newVersion) {
        try {
            if (newVersion.equals(oldVersion)) {
                return 0;
            }

            String[] newVersionArray = newVersion.split("\\.");
            String[] oldVersionArray = oldVersion.split("\\.");

            int index = 0;
            int minLen = Math.min(newVersionArray.length, oldVersionArray.length);
            int diff = 0;

            while (index < minLen && (diff = Integer.parseInt(newVersionArray[index]) - Integer.parseInt(oldVersionArray[index])) == 0) {
                index++;
            }

            if (diff == 0) {
                for (int i = index; i < newVersionArray.length; i++) {
                    if (Integer.parseInt(newVersionArray[i]) > 0) {
                        return 1;
                    }
                }

                for (int i = index; i < oldVersionArray.length; i++) {
                    if (Integer.parseInt(oldVersionArray[i]) > 0) {
                        return -1;
                    }
                }

                return 0;
            } else {
                return diff > 0 ? 1 : -1;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}

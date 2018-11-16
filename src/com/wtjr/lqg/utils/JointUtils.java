package com.wtjr.lqg.utils;

import android.os.Bundle;
import android.text.TextUtils;

import com.wtjr.lqg.constants.Constant;

/**
 * Created by WangYong on 2017/3/16.
 */

public class JointUtils {
    /**
     * 服务器当前地址拼接
     *
     * @param url
     * @return
     */
    public static String httpJointInterfaceAddress(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("http")) {
                if (url.substring(0, 1).equals("/")) {
                    url = Constant.INTERFACE_ADDRESS + url;
                } else {
                    url = Constant.INTERFACE_ADDRESS + "/" + url;
                }
            }
        }
        return url;
    }

    /**
     * 服务器图片地址拼接
     *
     * @param url
     * @return
     */
    public static String httpJointImageAddress(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("http")) {
                if (url.substring(0, 1).equals("/")) {
                    url = Constant.IMAGE_ADDRESS + url;
                } else {
                    url = Constant.IMAGE_ADDRESS + "/" + url;
                }
            }
        }
        return url;
    }

    /**
     * uid拼接
     *
     * @param url
     * @return
     */
    public static String httpJointUid(String url, String mUid) {
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("uid=")) {
                if (url.contains("?")) {
                    url = url + "&uid=" + mUid;
                } else {
                    url = url + "?uid=" + mUid;
                }
            }
            if (!url.contains("http")) {
                if (url.substring(0, 1).equals("/")) {
                    url = Constant.INTERFACE_ADDRESS + url;
                } else {
                    url = Constant.INTERFACE_ADDRESS + "/" + url;
                }
            }
        }
        return url;
    }

    /**
     * 将分享四要素放入bundle对象
     */
    public static Bundle shareFourElements(String shareUrl, String shareTitle, String shareContent, String shareBitmapUrl) {
        Bundle bundle = new Bundle();
        bundle.putString("shareUrl", shareUrl);
        bundle.putString("shareTitle", shareTitle);
        bundle.putString("shareContent", shareContent);
        bundle.putString("shareBitmapUrl", shareBitmapUrl);
        return bundle;
    }
}

package com.wtjr.lqg.sharedpreferences;

import android.content.Context;

/**
 * 提现，零妹妹二维码图片url和微信公众号存储工具类
 * @author Myf
 *
 */
public class IsLmmEvmImageUtil {
	/**
	 * 有图片
	 */
	public static void setEvmImage(Context context,String value) {
		SharedPreferencesUtil.setPrefString(context, "evmImage", value);
	}

	/**
	 * 获得图片
	 */
	public static String getEvmImage(Context context) {
		return SharedPreferencesUtil.getPrefString(context, "evmImage", "");
	}
	
	/**
	 * 设置微信号
	 */
	public static void setWeixinNumber(Context context,String value) {
		SharedPreferencesUtil.setPrefString(context, "weixin", value);
	}

	/**
	 * 获得微信号
	 */
	public static String getWeixinNumber(Context context) {
		return SharedPreferencesUtil.getPrefString(context, "weixin", "lingmm01");
	}
	
	
}

package com.wtjr.lqg.sharedpreferences;

import android.content.Context;

/**
 * 保存手势密码的工具
 * @author Myf
 *
 */
public class SaveGesturePasUtil {
	/**
	 * 设置手势密码
	 */
	public static void setGesturePas(Context context,String gesturePas) {
		SharedPreferencesUtil.setPrefString(context, "gesturePas", gesturePas);
	}
	
	/**
	 * 获得手势的方法
	 * @param context
	 * @return
	 */
	public static String getGesturePas(Context context) {
		return SharedPreferencesUtil.getPrefString(context, "gesturePas","");
	}
	
}

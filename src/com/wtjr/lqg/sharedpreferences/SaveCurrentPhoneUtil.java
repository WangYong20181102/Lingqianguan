package com.wtjr.lqg.sharedpreferences;


import android.content.Context;

/**
 * 当前账号
 * @author dell
 *
 */
public class SaveCurrentPhoneUtil {
	/**
	 * 设置当前Uid
	 */
	public static void setCurrentPhone(Context context,String currentPhone) {
		SharedPreferencesUtil.setPrefString(context, "currentPhone", currentPhone);
	}
	
	/**
	 * 获得当前Uid的方法
	 * @param context
	 * @param currentAccount
	 * @return
	 */
	public static String getCurrentPhone(Context context) {
		return SharedPreferencesUtil.getPrefString(context, "currentPhone","");
	}
	
	
}

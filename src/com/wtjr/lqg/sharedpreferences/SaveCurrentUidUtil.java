package com.wtjr.lqg.sharedpreferences;


import android.content.Context;

/**
 * 当前账号
 * @author dell
 *
 */
public class SaveCurrentUidUtil {
	/**
	 * 设置当前Uid
	 */
	public static void setCurrentUid(Context context,String currentAccount) {
		SharedPreferencesUtil.setPrefString(context, "currentUid", currentAccount);
	}
	
	/**
	 * 获得当前Uid的方法
	 * @param context
	 * @param currentAccount
	 * @return
	 */
	public static String getCurrentUid(Context context) {
		return SharedPreferencesUtil.getPrefString(context, "currentUid","");
	}
	
	
}

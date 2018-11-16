package com.wtjr.lqg.sharedpreferences;


import android.content.Context;

/**
 * 当前是否有账号在登录中
 * 
 * @author dell
 * 
 */
public class SaveLoginState {
	/**
	 * 有账号正在登录中
	 */
	public static void isLogin(Context context) {
	    SaveFirstInState.setUnFirstInState(context);
		SharedPreferencesUtil.setPrefBoolean(context, "LoginState", true);
	}

	/**
	 * 没有账号正在登录中
	 */
	public static void unLogin(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "LoginState", false);
	}

	/**
	 * 获得登录状态，默认为false,有账号登陆后为true
	 */
	public static boolean getLoginState(Context context) {
		return SharedPreferencesUtil.getPrefBoolean(context, "LoginState", false);
	}
	
}

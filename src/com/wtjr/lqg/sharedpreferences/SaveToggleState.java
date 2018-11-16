package com.wtjr.lqg.sharedpreferences;


import android.content.Context;

/**
 * 开关状态工具类
 * @author dell
 *
 */
public class SaveToggleState {
	/**
	 * 设置开关状态为开
	 */
	public static void setIsToggle(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "gesturePassword", false);
	}
	/**
	 * 设置开关状态为关
	 */
	public static void setUnToggle(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "gesturePassword", true);
	}
	/**
	 *获得当前开关状态,默认状态是关闭的为true，当开关状态为开的时候为flase
	 */
	public static boolean getToggleState(Context context) {
		return SharedPreferencesUtil.getPrefBoolean(context, "gesturePassword", true);
	}
}

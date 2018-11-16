package com.wtjr.lqg.sharedpreferences;

import android.content.Context;

/**
 * 保存手势密码的工具
 * @author Myf
 *
 */
public class ToggleGesturePasswordState {
	/**
	 *获得当前开关状态
	 */
	public static boolean getGesturePassword(Context context) {
		return SharedPreferencesUtil.getPrefBoolean(context, "toggleGesturePassword", false);
	}
	/**
	 * 打开开关状态
	 */
	public static void openGesturePassword(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "toggleGesturePassword", true);
	}
	
	/**
	 * 关闭开关状态
	 */
	public static void closeGesturePassword(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "toggleGesturePassword", false);
	}
}

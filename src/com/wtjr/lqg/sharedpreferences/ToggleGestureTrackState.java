package com.wtjr.lqg.sharedpreferences;

import android.content.Context;

/**
 * 手势轨迹工具类
 * @author Myf
 *
 */
public class ToggleGestureTrackState {
	/**
	 *获得当前手势轨迹
	 */
	public static boolean getGestureTrack(Context context) {
		return SharedPreferencesUtil.getPrefBoolean(context, "toggleGestureTrack", true);
	}
	/**
	 * 打开手势轨迹
	 */
	public static void openGestureTrack(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "toggleGestureTrack", true);
	}
	/**
	 * 关闭手势轨迹
	 */
	public static void closeGestureTrack(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "toggleGestureTrack", false);
	}

	
}

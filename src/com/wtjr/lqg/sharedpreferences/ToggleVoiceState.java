package com.wtjr.lqg.sharedpreferences;

import android.content.Context;

/**
 * 开关声音
 * 
 * @author Myf
 * 
 */
public class ToggleVoiceState {
	/**
	 * 获得声音开关状态
	 * @param context
	 * @return
	 */
	public static boolean getVoice(Context context) {
		return SharedPreferencesUtil.getPrefBoolean(context, "toggleVoice", false);
	}

	/**
	 * 设置声音开
	 */
	public static void openVoice(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "toggleVoice", true);
	}

	/**
	 * 设置声音关
	 */
	public static void closeVoice(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "toggleVoice", false);

	}
}

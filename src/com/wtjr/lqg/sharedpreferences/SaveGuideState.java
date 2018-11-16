package com.wtjr.lqg.sharedpreferences;


import android.content.Context;

/**
 * 引导页面
 * @author dell
 *
 */
public class SaveGuideState {

	/**
	 * 关闭引导
	 */
	public static void closeGuide(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "guide",false);
	}

	/**
	 * 得到当前引导状态,true为引导页存在，默认是存在的
	 * 
	 * @param context
	 */
	public static boolean getGuideState(Context context) {
		return SharedPreferencesUtil.getPrefBoolean(context,"guide", true);
	}
}

package com.wtjr.lqg.sharedpreferences;


import android.content.Context;

/**
 * 第一次进入工具类
 * @author dell
 *
 */
public class SaveFirstInState {
    /**
     * 获得第一次进入的状态
     */
	public static boolean getFirstInState(Context context) {
		return SharedPreferencesUtil.getPrefBoolean(context, "FirstInState",true);
	}
	/**
	 * 设置不是第一次进入了
	 * 第一次进入成功了，将isFirstIn改为false，表示以后任何账号进来的时候都没有第一次进入的概念了
	 */
	public static void setUnFirstInState(Context context) {
		SharedPreferencesUtil.setPrefBoolean(context, "FirstInState",false);
	}
	
}

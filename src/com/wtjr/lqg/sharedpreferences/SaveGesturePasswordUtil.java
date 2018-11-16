package com.wtjr.lqg.sharedpreferences;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
/**
 * 保存手势密码
 * @author Myf
 *
 */
public class SaveGesturePasswordUtil {
	/**
	 * 设置手势密码
	 */
	public static void setGesturePas(Context context,String gesturePassword) {
		//如果手势密码不等于null,并且长度大于等于4，就可以显示手势
		if (!TextUtils.isEmpty(gesturePassword)&&gesturePassword.length()>=4) {
			//设置开关状态为打开
			ToggleGesturePasswordState.openGesturePassword(context);
			//手势锁处于显示状态
			ToggleGestureTrackState.openGestureTrack(context);
			//保存密码
			SharedPreferencesUtil.setPrefString(context, "gesturePas", gesturePassword);
			Toast.makeText(context,"手势密码设置成功",Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(context,"手势密码设置失败",Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * 获得手势密码
	 */
	public static String getGesturePas(Context context) {
		return SharedPreferencesUtil.getPrefString(context, "gesturePas","");
	}
}

package com.wtjr.lqg.utils;

import org.xutils.x;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;

/**
 * 自定义Toast
 * 
 * @author Administrator
 * 
 */
public class ToastUtil {
	/**
	 * 显示时间短
	 * 
	 * @param context 上下文
	 * @param str 提示的字符串
	 */
	public static void showToastShort(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 显示时间长
	 * 
	 * @param context 上下文
	 * @param str 提示的字符串
	 */
	public static void showToastLong(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 判断网络失败提示语
	 * 
	 * @param context 上下文
	 */
	public static void showNoNet(Context context) {
		Toast.makeText(context, "当前网络不可用\n请检查你的网络设置!", Toast.LENGTH_SHORT).show();
	}
	/**
	 * 请求超时
	 * 
	 * @param context 上下文
	 */
	public static void showLongTime(Context context) {
		Toast.makeText(context, "请求超时!", Toast.LENGTH_SHORT).show();
	}
	/**
	 * 请求失败
	 * 
	 * @param context 上下文
	 */
	public static void showRequestFail(Context context) {
		Toast.makeText(context, "请求失败!", Toast.LENGTH_SHORT).show();
	}
	/**
	 * 没有更多数据了
	 * 
	 * @param context 上下文
	 */
	public static void notMoreData(Context context) {
		Toast.makeText(context, "暂无更多数据!", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 没有数据
	 * 
	 * @param context 上下文
	 */
	public static void notData(Context context) {
		Toast.makeText(context, "暂无数据!", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 
	 * @param context
	 * @param resId:自定义布局
	 * @param contentId:提示语（strings.xml）
	 */
	public static void myToastShow(Context context, int resId, int contentId, int duration){
		Toast toast = new Toast(context);
		toast.setDuration(duration);
		toast.setGravity(Gravity.CENTER, 0 , 0);
		
		LinearLayout toastLayout = new LinearLayout(context);
		
		toastLayout.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		toastLayout.setOrientation(LinearLayout.HORIZONTAL);
		toastLayout.setGravity(Gravity.CENTER);
		toastLayout.setBackgroundResource(resId);
		
		TextView textView = new TextView(context);
		textView.setText(contentId);
		textView.setTextColor(Color.BLACK);
		textView.setTextSize(14);
		textView.setGravity(Gravity.CENTER);
		textView.setMaxWidth(100);
		textView.setTextScaleX(1.1f);
		textView.setMaxLines(2);
		textView.setEllipsize(TruncateAt.END);
		toastLayout.addView(textView);
		
		toast.setView(toastLayout);
		toast.show();
	}
	
	/**
	 * 跳转微信弹出的Toast
	 * @param context
	 */
	public static void wxToast(Context context) {
	    LayoutInflater inflater = LayoutInflater.from(context);
	    View view = inflater.inflate(R.layout.toast_layout, null);
	    TextView tvToastContent = (TextView) view.findViewById(R.id.tv_toastContent);
	    tvToastContent.setText("零钱罐公众号复制成功，\n正在跳转微信...");
	    
//	    LqgApplication app = (LqgApplication) x.app();
//	    android.view.ViewGroup.LayoutParams layoutParams = tvToastContent.getLayoutParams();
//	    layoutParams.width = app.mScreenWidth - app.mScreenWidth/5*2;
	    
	    
	    final Toast toast = new Toast(context);
	    toast.setGravity(Gravity.CENTER, 0, 0);
	    toast.setDuration(Toast.LENGTH_LONG);
	    toast.setView(view);
	    toast.show();
	    
	    
	    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();                
            }
        }, 1300);
    }
}

package com.wtjr.lqg.widget.paykeyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.wtjr.lqg.R;
/**
 * 提示Dialog
 * @author LanYan
 *
 */
public class PayDialog extends Dialog  {
	Activity activity;
	private View view;
	private boolean isOutSideTouch=true;

	public View getView() {
		return view;
	}
	public void setView(View view) {
		this.view = view;
	}
	public boolean isOutSideTouch() {
		return isOutSideTouch;
	}
	public void setOutSideTouch(boolean isOutSideTouch) {
		this.isOutSideTouch = isOutSideTouch;
	}
	public PayDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}
	public PayDialog(Context context) {
		this(context,0);
		// TODO Auto-generated constructor stub
	}
	public PayDialog(Activity activity, View view) {
		super(activity,R.style.PayKeyboardDialog1);
		this.activity = activity;
		this.view=view;
	}
	public PayDialog(Activity activity, View view,int theme) {
		super(activity,theme);
		this.activity = activity;
		this.view=view;
	}
	public PayDialog(Activity activity, View view,int theme,boolean isOutSide) {
		super(activity,theme);
		this.activity = activity;
		this.view=view;
		this.isOutSideTouch=isOutSide;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(view);
		setCanceledOnTouchOutside(isOutSideTouch);
		setCancelable(isOutSideTouch);
		
//		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = this.getWindow().getAttributes();
		this.getWindow().setWindowAnimations(R.style.FromDownToUpStyleAnimation);
		this.getWindow().setGravity(Gravity.BOTTOM);
		lp.width = LayoutParams.MATCH_PARENT;
		lp.height = LayoutParams.WRAP_CONTENT;
		this.getWindow().setAttributes(lp);
		
//		DisplayMetrics dm = new DisplayMetrics();
//		// 取得窗口属性
//		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//		// 窗口的宽度
//		int screenWidth = dm.widthPixels;
//		int screenHeight=dm.heightPixels;
//		WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
//		layoutParams.width = screenWidth;
//		layoutParams.height = screenHeight-60;
//		this.getWindow().setAttributes(layoutParams);
	}
}

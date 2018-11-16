package com.wtjr.lqg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
/**
 * 用于注册
 * @author Myf
 *
 */
public class RegistreLinerLayout extends LinearLayout {

	private boolean isIntercept;

	public RegistreLinerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isIntercept) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * true 拦截，反之不拦截
	 * 
	 * @param flag
	 */
	public void setIntercept(boolean intercept) {
		this.isIntercept = intercept;
	}
}

package com.wtjr.lqg.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.wtjr.lqg.R;

/**
 * 3D图片旋转效果
 * 
 * @author dell
 * 
 */
public class AnimationUtil {

	/**
	 * 开始动画
	 */
	public static Animation startAnimation(Context context, View view) {
		Animation animation = MyAnimationUtil.loadAnimation(context,
				R.anim.rotate3dy);
		// 动画结束时停留在最后一帧
		animation.setFillAfter(true);
		// 动画时间200毫秒
		animation.setDuration(180);
		// 插入器，看起来动画效果更佳平稳
		LinearInterpolator interpolator = new LinearInterpolator();
		animation.setInterpolator(interpolator);
//		view.startAnimation(animation);
		return animation;
	}
	/**
	 * 密码输入框抖动动画
	 */
	public static Animation startShakeAnimation(Context context){
		TranslateAnimation shakeAnimation = new TranslateAnimation(0, 10, 0, 0);
		shakeAnimation.setDuration(300);
		CycleInterpolator cycleInterpolator = new CycleInterpolator(8);
		shakeAnimation.setInterpolator(cycleInterpolator);
		return shakeAnimation;
	}
}

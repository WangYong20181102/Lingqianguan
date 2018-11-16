package com.wtjr.lqg.widget.risenumber;

import java.text.DecimalFormat;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wtjr.lqg.utils.MoneyFormatUtil;

/**
 * 自定义RiseNumberTextView继承TextView，并实现接口RiseNumberBase
 * 
 */
public class RiseNumberTextView extends TextView implements IRiseNumber {

	private static final int STOPPED = 0;

	private static final int RUNNING = 1;

	private int mPlayingState = STOPPED;

	private String startNumber;

	private String endNumber;
	
	/**
	 * 动画播放时长
	 */
	private long duration = 1500;

	private DecimalFormat fnum;

	private EndListener mEndListener = null;

	final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999,
			99999999, 999999999, Integer.MAX_VALUE };

	/**
	 * 构造方法
	 * 
	 * @param context
	 */
	public RiseNumberTextView(Context context) {
		super(context);
	}

	/**
	 * 使用xml布局文件默认的被调用的构造方法
	 * 
	 * @param context
	 * @param attr
	 */
	public RiseNumberTextView(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public RiseNumberTextView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	/**
	 * 判断动画是否正在播放
	 * 
	 * @return
	 */
	public boolean isRunning() {
		return (mPlayingState == RUNNING);
	}
	
	/**
	 * 跑小数动画
	 */
	private void runFloat() {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(Float.valueOf(startNumber),Float.valueOf(endNumber));
		valueAnimator.setDuration(duration);
		
		valueAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator valueAnimator) {
					    
					    //---------------此处为了避免起始值和结束值的差值太小导致显示误差------------------------
					    double parseDouble = Double.parseDouble(valueAnimator.getAnimatedValue().toString());
					    if(Double.parseDouble(endNumber) > Double.parseDouble(startNumber)) {
					        if(parseDouble >= Double.parseDouble(startNumber) && parseDouble <= Double.parseDouble(endNumber)) {
					            String format = fnum.format(parseDouble);
		                        setText(MoneyFormatUtil.format(format));
					        }
					    }else {
					        if(parseDouble >= Double.parseDouble(endNumber) && parseDouble <= Double.parseDouble(startNumber)) {
                                String format = fnum.format(parseDouble);
                                setText(MoneyFormatUtil.format(format));
                            }
					    }
					    //--------------------------------------------------------------------------
					    
						if (valueAnimator.getAnimatedFraction() >= 1) {//数字跑动结束
							mPlayingState = STOPPED;
							
							setText(MoneyFormatUtil.format(endNumber));
							
							if (mEndListener != null)
								mEndListener.onEndFinish();
						}
					}

				});
		
		valueAnimator.start();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		fnum = new DecimalFormat("0.00");
	}
	

	/**
	 * 开始播放动画
	 */
	@Override
	public void start() {

		if (!isRunning()) {
			mPlayingState = RUNNING;
				runFloat();
		}
	}

	/**
	 * 设置数字跑动
	 * 
	 * @param startNumber 起始数
	 * @param endNumber 终止数
	 * @param duration 持续时间(毫秒)
	 */
	@Override
	public RiseNumberTextView withNumber(String startNumber,String endNumber) {

		this.startNumber = startNumber;
		this.endNumber = endNumber;
		
		return this;
	}

	/**
	 * 设置动画结束监听器
	 */
	@Override
	public void setOnEndListener(EndListener callback) {
		mEndListener = callback;
	}

	/**
	 * 定义动画结束接口
	 * 
	 * 
	 */
	public interface EndListener {
		/**
		 * 当动画播放结束时的回调方法
		 */
		public void onEndFinish();
	}

}

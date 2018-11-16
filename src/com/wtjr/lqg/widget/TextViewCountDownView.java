package com.wtjr.lqg.widget;

import org.xutils.x;

import android.content.Context;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.wtjr.lqg.R;
/**
 * 按钮倒计时
 *
 */
public class TextViewCountDownView extends TextView {
	/**
	 * 倒计时
	 */
	private CountDownTimer countDownTimer;
	private Resources resources;


	public TextViewCountDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 resources = x.app().getResources();
		 setBackgroundResource(R.drawable.btn_receive_verification_code);
		 setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.s11));
	}

	/**
	 * 启动倒计时
	 * 
	 * @param millisInFuture(毫秒)
	 *            总共的时间
	 */
	public void startCountDown(long timeRemaining) {

		// 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
		// 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
		// 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
		countDownTimer = new CountDownTimer(timeRemaining, 1000 - 10) {
			@Override
			public void onTick(long time) {
				long time1 = (time + 15) / 1000;
				// 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
				setText(time1 + "'");
				setEnabled(false);
				setBackgroundResource(R.drawable.bg_tv_count_down);
				setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.s11));
			}

			@Override
			public void onFinish() {
				setEnabled(true);
				setText("重新获取");
				setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.s11));
				setBackgroundResource(R.drawable.btn_receive_verification_code);
				if (countDownTimer != null) {
					countDownTimer.cancel();
				}

			}
		};
		
		countDownTimer.start();
		
	}
	/**
	 * 取消倒计时
	 */
	public void cancelCountDown() {
		if (countDownTimer!=null) {
			countDownTimer.cancel();
		}
	}
}

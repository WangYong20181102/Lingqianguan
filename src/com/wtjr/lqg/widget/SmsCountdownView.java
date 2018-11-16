package com.wtjr.lqg.widget;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;

/**
 * 短信倒计时控件
 * 
 * @author Myf
 * 
 */
public class SmsCountdownView extends RelativeLayout {
	/**
	 * 进度条倒计时总体布局
	 */
	private RelativeLayout rlTime;
	/**
	 * 倒计时进度条
	 */
	private RoundProgressBar progressBar;
	/**
	 * 倒计时显示时间
	 */
	private TextView tvTime;
	/**
	 * 点击倒计时的按钮
	 */
	private Button btnCountdown;
	/**
	 * 倒计时
	 */
	private CountDownTimer countDownTimer;
	
	/**
	 * 倒计时时间(秒)
	 */
	private static final int sTime=60;
	
	public SmsCountdownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 初始化控件
	 * 
	 * @param context
	 */
	private void initView(Context context) {
		View view = LayoutInflater.from(context).inflate(R.layout.widget_sms_countdown_view, this, true);
		rlTime = (RelativeLayout) view.findViewById(R.id.rl_time);
		progressBar = (RoundProgressBar) view.findViewById(R.id.pb_time);
		tvTime = (TextView) view.findViewById(R.id.tv_time);
		btnCountdown = (Button) view.findViewById(R.id.btn_countdown);
	}


	/**
	 * 设置控件是显示还是隐藏
	 * 
	 * @param isShow
	 *            true按钮显示，否则隐藏
	 */
	private void showBtnCountdown(boolean isShow) {
		if (isShow) {
			btnCountdown.setVisibility(View.VISIBLE);
			rlTime.setVisibility(View.GONE);
		} else {
			btnCountdown.setVisibility(View.GONE);
			rlTime.setVisibility(View.VISIBLE);
		}

	}

	/**
	 * 启动倒计时
	 * 
	 * @param millisInFuture
	 *            总共的时间
	 */
	public void startCountDown(final long millisInFuture) {
		
		// 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
		// 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
		// 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
		  countDownTimer = new CountDownTimer(millisInFuture, 1000 - 10) {
			@Override
			public void onTick(long time) {
				long time1=(time+15)/1000;
				// 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
				progressBar.setProgress((int)(sTime-time1));
				
				tvTime.setText(time1+"'");
				
				showBtnCountdown(false);
			}

			@Override
			public void onFinish() {
				showBtnCountdown(true);
				if (countDownTimer!=null) {
					countDownTimer.cancel();
				}
				
			}
		};
		countDownTimer.start();
	}

}

package com.wtjr.lqg.widget.wave;

import java.util.ArrayList;
import java.util.List;

import com.wtjr.lqg.utils.L;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
/**
 * 水波帮助类
 * @author Myf
 *
 */
public class WaveHelper {
	private WaveView mWaveView;

	/**
	 * 设置最大高度0.8,表示百分之百的时候还是0.8，为的是效果
	 */
	private final float mMacWaterLevelRatio = 10.1f;
	
	private AnimatorSet mAnimatorSet;
	/**
	 * 设置高度 0-1 float
	 */
	private float mWaterLevelRatio = 0.5f;
	/**
	 * 设置波频率更换的时间
	 */
	private long mWaveShiftRatioDuration = 3000;
	/**
	 * 水波的起伏 0.01f-0.1f 就有很大区别了
	 */
	private float mWaveShiftRatio;

	/**
	 * 
	 * @param waveView
	 * @param waterLevelRatio
	 *            设置高度 0-1 float
	 * @param waveShiftRatioDuration
	 *            设置波频率更换的时间
	 * @param waveShiftRatio
	 *            水波的起伏 0.01f-0.1f 就有很大区别了
	 */
	public WaveHelper(WaveView waveView, float waterLevelRatio,
			long waveShiftRatioDuration, float waveShiftRatio) {
		mWaveView = waveView;
		
		mWaterLevelRatio = waterLevelRatio*mMacWaterLevelRatio;
		L.hintD("mWaterLevelRatio-------------"+mWaterLevelRatio);
		
		mWaveShiftRatioDuration = waveShiftRatioDuration;
		mWaveShiftRatio = waveShiftRatio;
		initAnimation();
	}

	public void start() {
		mWaveView.setShowWave(true);
		if (mAnimatorSet != null) {
			mAnimatorSet.start();
		}
	}

	private void initAnimation() {
		List<Animator> animators = new ArrayList<>();

		// horizontal animation.
		// wave waves infinitely.
		ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(mWaveView,
				"waveShiftRatio", 0f, 1f);
		waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
		waveShiftAnim.setDuration(1500);
		waveShiftAnim.setInterpolator(new LinearInterpolator());
		animators.add(waveShiftAnim);

		// vertical animation.
		// water level increases from 0 to center of WaveView
		ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(mWaveView,
				"waterLevelRatio", 0f, mWaterLevelRatio);
		waterLevelAnim.setDuration(5000);
		waterLevelAnim.setInterpolator(new DecelerateInterpolator());
		animators.add(waterLevelAnim);

        //波浪的大小从大变小，再从小变大。
		ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(mWaveView,
				"amplitudeRatio", 0.0001f, mWaveShiftRatio);
		amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
		amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
		amplitudeAnim.setDuration(mWaveShiftRatioDuration);
		amplitudeAnim.setInterpolator(new LinearInterpolator());
		animators.add(amplitudeAnim);

		mAnimatorSet = new AnimatorSet();
		mAnimatorSet.playTogether(animators);
	}

	// /**
	// * 设置频率
	// * @param waveShiftRatio
	// */
	// public void setWaveShiftRatio(long waveShiftRatio) {
	// mWaveShiftRatio = waveShiftRatio;
	// }
	// /**
	// * 设置高度 0-1 float
	// * @param waterLevelRatio
	// */
	// public void setWaterLevelRatio(float waterLevelRatio) {
	// mWaterLevelRatio = waterLevelRatio;
	// }

	public void cancel() {
		if (mAnimatorSet != null) {
			// mAnimatorSet.cancel();
			mAnimatorSet.end();
		}
	}
}

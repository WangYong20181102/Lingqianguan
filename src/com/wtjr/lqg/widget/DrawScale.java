package com.wtjr.lqg.widget;

import org.xutils.x;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.umeng.socialize.utils.Log;
import com.wtjr.lqg.activities.LqgApplication;
/**
 * 画刻度
 * @author Myf
 *
 */
public class DrawScale extends View {

	private Paint mPaint;
	/**
	 * 像数密度
	 */
	private float mDensity;

	/**
	 * 线的总条数
	 */
	private int mLineCount = 10;
	/**
	 * 刻度尺之间的间距
	 */
	private float mSpacing;
	/**
	 * 最大高度
	 */
	private float mMaxHeight;

	public DrawScale(Context context, AttributeSet attrs) {
		super(context, attrs);

		initData(context);
		initPaint();
	}

	private void initData(Context context) {
		LqgApplication app = (LqgApplication) x.app();
		mDensity=app.mDensity;
		
		mSpacing = mDensity * 10f;
		mMaxHeight = mDensity * 20f;
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(Color.parseColor("#e8e8e8"));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawLine(canvas);
		super.onDraw(canvas);
	}

	/**
	 * 画刻度尺
	 */
	private void drawLine(Canvas canvas) {
		for (int i = 0; i <= mLineCount; i++) {
			if (i == 0 || i == 5 || i == mLineCount) {
				//长
				canvas.drawLine(mSpacing * i, mMaxHeight/5f, mSpacing * i, mMaxHeight,
						mPaint);
			} else {
				//短
				canvas.drawLine(mSpacing * i, mMaxHeight/1.5f, mSpacing * i, mMaxHeight,
						mPaint);
			}
		}

		canvas.drawLine(0, mMaxHeight, mSpacing * mLineCount, mMaxHeight,
				mPaint);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    mMaxHeight = getMeasuredHeight();
		setMeasuredDimension((int) (mSpacing * mLineCount), (int) mMaxHeight);
	}

}

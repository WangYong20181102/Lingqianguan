package com.wtjr.lqg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by WangXu on 2017/11/23.
 */

public class WeekText extends TextView {
    private int mSelfHeight;
    private int mSelfWidth;
    private Paint mTextPaint;
    private int textMoveWidth;
    private float textHeight;
    private float textWidth;
    /**
     * 周期数
     */
    private int intPeriod;

    public WeekText(Context context, AttributeSet attrs) {
        super(context, attrs);

        initPaint();
        getSelfSize();
    }


    /**
     * 初始化画笔
     */
    private void initPaint() {
        //柱状上面的线的画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.parseColor("#bbbbbb"));
        mTextPaint.setTextSize(30);

    }

    /**
     * 获得画图控件总大小
     */
    private void getSelfSize() {

        ViewTreeObserver vto = WeekText.this.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            public boolean onPreDraw() {
                // removeOnPreDrawListener这一条，这个可以保证系统运行一次。
                WeekText.this.getViewTreeObserver().removeOnPreDrawListener(this);

                mSelfHeight = WeekText.this.getMeasuredHeight();
                mSelfWidth = WeekText.this.getMeasuredWidth();
                // 计算出文字的高度
                textHeight = Math.abs(mTextPaint.descent() + mTextPaint.ascent());
                // 计算出文字的宽度
                textWidth = mTextPaint.measureText("第4周");
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {

        textMoveWidth = (mSelfWidth - 240) / intPeriod;
        for (int i = 0; i < intPeriod; i++) {
            if (i == intPeriod - 1) {
                canvas.drawText("第" + (i + 1) + "周", mSelfWidth - textWidth, textHeight / 2 + 20, mTextPaint);
            } else {

                canvas.drawText("第" + (i + 1) + "周", textMoveWidth * (i + 1) - textWidth / 2, textHeight / 2 + 20, mTextPaint);
            }

        }

    }

    /**
     * 设置周期数
     */
    public void setIntPeriod(int intPeriod) {
        this.intPeriod = intPeriod;
    }
}

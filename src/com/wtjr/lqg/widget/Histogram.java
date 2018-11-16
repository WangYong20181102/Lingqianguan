package com.wtjr.lqg.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wtjr.lqg.R;
import com.wtjr.lqg.utils.MoneyFormatUtil;


/**
 * Created by WangXu on 2017/11/22.
 */

public class Histogram extends View {

    private int mLineColor = Color.WHITE;
    private float mLineSize;
    private int mBrokenLineBackgroundStart;
    private int mBrokenLineBackgroundEnd;
    private int mTextLineColor;
    private float mTextLineSize;
    private Paint mBrokenLineBackgroundPaint;
    private Paint mLinePaint;
    private int mSelfHeight;
    private int mSelfWidth;
    private int mYHeight;
    private int mXWidth;
    private Path mLinePath;
    private int lineMoveHeight;
    private int lineMoveWidth;
    private Paint mTextPaint;
    private ValueAnimator mValueAnimator;
    private long mDuration = 3000;
    private int mDrawXWidth;
    private int mDrawYHeight;
    /**
     * 周期数
     */
    private int intPeriod;
    /***
     * 基础利率
     */
    private double baseRate;
    /**
     * 递增利率
     */
    private double increaseRate;
    /**
     * 两倍周期数
     */
    private int twoIntPeriod;

    private boolean anim_running = true;

    public Histogram(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = context.getResources();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Histogram);
        //线条
        mLineColor = array.getColor(R.styleable.Histogram_brokenLineColor1, Color.WHITE);
        mLineSize = array.getDimension(R.styleable.Histogram_brokenLineSize1, 2);
        //背景
        mBrokenLineBackgroundStart = array.getColor(R.styleable.Histogram_brokenLineBackgroundStart1, Color.parseColor("#DDFFC600"));
        mBrokenLineBackgroundEnd = array.getColor(R.styleable.Histogram_brokenLineBackgroundEnd1, Color.parseColor("#00FFC600"));
        //文字
        mTextLineColor = array.getColor(R.styleable.Histogram_textColor1, Color.RED);
        mTextLineSize = array.getDimension(R.styleable.Histogram_textSize1, 20);

//        array.recycle();// 循环再利用

        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        //柱状上面的线的画笔
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mLineColor);
        mLinePaint.setStrokeWidth(mLineSize);

        //背景颜色画笔
        mBrokenLineBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBrokenLineBackgroundPaint.setStyle(Paint.Style.FILL);


        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextLineColor);
        mTextPaint.setTextSize(mTextLineSize);
    }

    /**
     * 初始化一些数据
     */
    private void initData() {

        mYHeight = mSelfHeight;
        mXWidth = mSelfWidth;

        //画除了第一个高度之外，固定其余的高度
        mDrawXWidth = mXWidth - 200;
        mDrawYHeight = mYHeight - 70;

        //等分剩下的Y轴高度
        lineMoveHeight = (mDrawYHeight - 70) / (intPeriod - 1);
        lineMoveWidth = mDrawXWidth / intPeriod;

        LinearGradient lg = new LinearGradient(mXWidth, mDrawYHeight - lineMoveHeight * (intPeriod - 1), mXWidth,
                mYHeight,
                mBrokenLineBackgroundStart, mBrokenLineBackgroundEnd, Shader.TileMode.MIRROR);
        mBrokenLineBackgroundPaint.setShader(lg);// 设置折线图背景色为过渡色

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mSelfHeight = Histogram.this.getMeasuredHeight();
        mSelfWidth = Histogram.this.getMeasuredWidth();

        initData();
    }

    /**
     * 启动折线图动画
     */
    private void startAnimator() {
        Log.d("histogram", "path length:" + mXWidth);

        mValueAnimator = ValueAnimator.ofInt(0, mXWidth + (mDrawYHeight - 70));

        mValueAnimator.setDuration(mDuration);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                anim_running = false;
            }
        });
        mValueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (anim_running) {
            if (mValueAnimator == null) return;

            int running = (int) mValueAnimator.getAnimatedValue();

            //线的路径
            mLinePath = new Path();
            mLinePath.moveTo(0, mDrawYHeight);

            int step = running / (lineMoveWidth + lineMoveHeight);
            for (int i = 1; i <= step; i++) {
                if (i >= intPeriod) {
                    continue;
                }
                mLinePath.lineTo(lineMoveWidth * i, mDrawYHeight - lineMoveHeight * (i - 1));
                mLinePath.lineTo(lineMoveWidth * i, mDrawYHeight - lineMoveHeight * i);

                //画矩形
                canvas.drawRect(lineMoveWidth * (i - 1), mDrawYHeight - (lineMoveHeight * (i - 1)), lineMoveWidth * i, mYHeight,
                        mBrokenLineBackgroundPaint);
            }

            if (running < (lineMoveWidth + step * (lineMoveWidth + lineMoveHeight)) && step < intPeriod) {//横线
                mLinePath.lineTo(running - (lineMoveHeight * step), mDrawYHeight - (lineMoveHeight * step));

                //画矩形
                canvas.drawRect(lineMoveWidth * step, mDrawYHeight - (lineMoveHeight * step), running - (lineMoveHeight * step), mYHeight,
                        mBrokenLineBackgroundPaint);
            } else if (step < (intPeriod - 1)) {//竖线
                mLinePath.lineTo(lineMoveWidth * (step + 1), mDrawYHeight - lineMoveHeight * step);
                //画矩形
                canvas.drawRect(lineMoveWidth * step, mDrawYHeight - (lineMoveHeight * step), lineMoveWidth * (step + 1), mYHeight,
                        mBrokenLineBackgroundPaint);

                mLinePath.lineTo(lineMoveWidth * (step + 1), mDrawYHeight - (running - (lineMoveWidth * (step + 1))));
            } else {
                //补全最后的线
                mLinePath.lineTo(running - (lineMoveHeight * (intPeriod - 1)), mDrawYHeight - lineMoveHeight * (intPeriod - 1));
                //画矩形
                canvas.drawRect(lineMoveWidth * (intPeriod - 1), mDrawYHeight - (lineMoveHeight * (intPeriod - 1)), running - (lineMoveHeight *
                        (intPeriod - 1)), mYHeight, mBrokenLineBackgroundPaint);

            }
            canvas.drawPath(mLinePath, mLinePaint);
        } else {
            //线的路径
            mLinePath = new Path();
            mLinePath.moveTo(0, mYHeight - 70);
            for (int i = 1; i < twoIntPeriod; i++) {
                if (i % 2 == 1) {//单
                    //画线
                    mLinePath.lineTo(lineMoveWidth * ((i + 1) / 2), mDrawYHeight - lineMoveHeight * ((i - 1) / 2));

                    //画矩形
                    canvas.drawRect(lineMoveWidth * ((i - 1) / 2), mDrawYHeight - lineMoveHeight * ((i - 1) / 2),
                            lineMoveWidth * ((i + 1) / 2),
                            mYHeight, mBrokenLineBackgroundPaint);
                    // 计算出文字的高度
                    float textHeight = Math.abs(mTextPaint.descent() + mTextPaint.ascent());
                    // 计算出文字的宽度
                    float textWidth = mTextPaint.measureText("8.00%");
                    float textX = (i - 1) / 2 * lineMoveWidth + (lineMoveWidth - textWidth) / 2;
                    float textY = mDrawYHeight - ((i - 1) / 2 * lineMoveHeight + textHeight / 2 + 5);
                    //画文字
                    canvas.drawText(MoneyFormatUtil.format2(baseRate + (i - 1) / 2 * increaseRate) + "%", textX, textY, mTextPaint);
                } else if (i % 2 == 0) {//双
                    mLinePath.lineTo(lineMoveWidth * (i / 2), mDrawYHeight - lineMoveHeight * (i / 2));
                }
            }

            //补全
            mLinePath.lineTo(mSelfWidth, mDrawYHeight - lineMoveHeight * (intPeriod - 1));
            canvas.drawPath(mLinePath, mLinePaint);
            canvas.drawRect(lineMoveWidth * intPeriod, mDrawYHeight - lineMoveHeight * (intPeriod - 1), mSelfWidth, mYHeight,
                    mBrokenLineBackgroundPaint);
        }

    }

    /**
     * 显示理财收益柱状图
     *
     * @param baseRate
     * @param increaseRate
     * @param intPeriod
     */
    public void initHistogram(double baseRate, double increaseRate, int intPeriod) {
        this.intPeriod = intPeriod;
        this.baseRate = baseRate;
        this.increaseRate = increaseRate;

        twoIntPeriod = intPeriod * 2;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus && mValueAnimator == null) {
            startAnimator();
        }
    }
}

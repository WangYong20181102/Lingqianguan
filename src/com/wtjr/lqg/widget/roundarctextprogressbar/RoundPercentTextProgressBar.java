package com.wtjr.lqg.widget.roundarctextprogressbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.wtjr.lqg.R;

/**
 * 进度条:显示进度条
 *
 * @author wangyong
 */
public class RoundPercentTextProgressBar extends View {
    // 进度条默认的的宽度
    private static final int DEFAULT_WIDTH_REACHED_BAR = 10;
    // 进度条默认的背景的宽度
    private static final int DEFAULT_WIDTH_UNREACHED_BAR = 10;

    /**
     * 进度条的宽度
     */
    private int mReachBarWidth = dp2px(DEFAULT_WIDTH_REACHED_BAR);
    /**
     * 进度条背景的宽度
     */
    private int mUnReachBarWidth = dp2px(DEFAULT_WIDTH_UNREACHED_BAR);
    /**
     * 进度条背景的颜色
     */
    private int mUnReachBarColor;
    /**
     * 进度条颜色
     */
    private int percentTextColorId;

    /**
     * 画笔
     */
    protected Paint mPaint = new Paint();
    /**
     * 默认背景画笔：灰色那部分
     */
    protected Paint mPaintUnReach = new Paint();
    /**
     * 圆半径
     */
    private int mRadius;
    /**
     * 圆总大小
     */
    private double totalSize;
    /**
     * 进度条最大的宽度
     */
    private int mMaxPaintWidth = 0;
    /**
     * 执行进度
     */
    private double mCurrentProgress = 0;
    /**
     * 进度条实际的宽度和高度
     */
    private int mSize = 0;

    private Context context;

    /**
     * 圆半径
     */
    private int cicleRadius;

    public RoundPercentTextProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundPercentTextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        initAttr(attrs);
        initPaint();

        // 计算进度条的半径
        mRadius = dp2px(cicleRadius) / 2;
        // 两个画笔的最大宽度
        mMaxPaintWidth = Math.max(mUnReachBarWidth, mReachBarWidth);
        mRadius = mRadius - mMaxPaintWidth / 2;

        mPaint.setStrokeWidth(mMaxPaintWidth);
        mPaintUnReach.setStrokeWidth(mMaxPaintWidth);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundPercentTextProgressBar);
        cicleRadius = a.getInt(R.styleable.RoundPercentTextProgressBar_circle_radius, 80);      //半径大小
        mUnReachBarColor = a.getColor(R.styleable.RoundPercentTextProgressBar_bg_progressBar, Color.parseColor("#666666"));     //底部颜色
        percentTextColorId = a.getColor(R.styleable.RoundPercentTextProgressBar_circle_progressBar_color, Color.parseColor("#666666"));     //进度颜色
        a.recycle();
    }


    /**
     * 初始化画笔工具
     */
    private void initPaint() {
        // 初始化画笔对象
        mPaint.setStyle(Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Cap.ROUND);     //笔触圆形

        mPaintUnReach.setStyle(Style.STROKE);
        mPaintUnReach.setAntiAlias(true);
        // 设置画笔颜色和宽度
        mPaintUnReach.setColor(mUnReachBarColor);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wSize, hSize = 0;
        hSize = wSize = mRadius * 2 + (mMaxPaintWidth / 2) * 2;
        // 获取宽度和高度的最小值，作为当前view的宽度和高度
        mSize = Math.min(wSize, hSize);
        // 设置进度条的大小:整个view的大小
        setMeasuredDimension(mSize, mSize);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // 由于画笔都是从中间开始画的，所以画圆时要记得减掉画笔宽度的一半
        canvas.rotate(-90, mSize / 2, mSize / 2);
        // 绘制unreachedBar
        canvas.drawCircle(mSize / 2, mSize / 2, mRadius, mPaintUnReach);
        mPaint.setColor(percentTextColorId);
        // 计算当前进度对应的角度
        double sweepAngle = mCurrentProgress * 1.0f / totalSize * 360;
        if (sweepAngle >= 350 && sweepAngle < 360) {
            sweepAngle = sweepAngle - 10;
        }
//        if (sweepAngle > 0 && sweepAngle < 10) {
//            sweepAngle = sweepAngle + 10;
//        }
        // 绘制reachBar
        canvas.drawArc(new RectF(mMaxPaintWidth / 2, mMaxPaintWidth / 2, mSize - mMaxPaintWidth / 2, mSize - mMaxPaintWidth / 2), 0, (float) sweepAngle, false, mPaint);
    }

    /**
     * 设置当前进度
     *
     * @param progress 进度值：需要计算
     */
    public void setProgress(double progress, double total) {

        mCurrentProgress = progress;
        totalSize = total;

        invalidate();
    }

    /**
     * 转变为标准尺寸的(dip)
     *
     * @param dpVal
     * @return
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());
    }
}

package com.wtjr.lqg.widget;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.wtjr.lqg.R;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.ScreenUtils;

import static android.R.attr.defaultHeight;
import static android.R.attr.defaultWidth;
import static android.R.attr.format;
import static android.R.attr.radius;
import static com.wtjr.lqg.R.string.income;

/**
 * 折线图
 *
 * @author dell
 */
public class LineChartView extends View {
    /**
     * 画最底层背景色的笔
     */
    private Paint mBelowChartBackgroundPaint;
    /**
     * 今天的收益数
     */
    private String mNowIncome;
    /**
     * 七天内每天的百分比
     */
    private List<Float> mRatios;
    /**
     * 底部隐藏的高度
     */
    private float mBottomHideHeight;
    /**
     * 画最底层背景色
     */
    private int mBelowChartBackground;
    /**
     * 获得自身控件的高度
     */
    private int mSelfHeight;
    /**
     * 获得自身控件的宽度
     */
    private int mSelfWidth;
    /**
     * 画折线用的笔
     */
    private Paint mBrokenLinePaint;
    /**
     * 画文字用的笔
     */
    private Paint mTextPaint;
    /**
     * 折线的颜色
     */
    private int mBrokenLineColor;
    /**
     * 折线的大小
     */
    private float mBrokenLineSize;
    /**
     * 路径
     */
    private Path mPath;
    /**
     * 画折线的路径
     */
    private Path mBrokenLinePath;
    /**
     * X轴每份的宽度
     */
    private float mEachXWidth;
    /**
     * 收益标签中的文字大小
     */
    private float mIncomeTextSize;
    /**
     * 圆半径大小
     */
    private float mCircleRadius;
    /**
     * 折线图背景色画笔
     */
    private Paint mBrokenLineBackgroundPaint;
    /**
     * 折线图背景色路径
     */
    private Path mBrokenLineBackgroundPath;
    /**
     * 折线图背景色路径-副路径
     */
    private Path mBrokenLineBackgroundPath_F;
    /**
     * Y的轴线画笔
     */
    private Paint mYLinePaint;
    /**
     * Y轴的高度
     */
    private float mYHeight;
    /**
     * 折线图的背景色中过度色的起始色
     */
    private int mBrokenLineBackgroundStart;
    /**
     * 折线图的背景色中过度色的结束色
     */
    private int mBrokenLineBackgroundEnd;
    /**
     * X轴的日期数字的大小
     */
    private float mDsateTextSize;
    /**
     * 折线图底部日期集合
     */
    private List<String> mDateTexts = new ArrayList<String>();
    /**
     * 尾部圆的线宽
     */
    private float mCircleLineSize;
    /**
     * 尾部圆的画笔
     */
    private Paint mCirclePaint;
    /**
     * 折线图动画
     */
    private ValueAnimator mValueAnimator;
    /**
     * 动画持续时长(ms)
     */
    private long mDuration = 2000;
    /**
     * 动画路径长度
     */
    private float mPathLenght;
    /**
     * 动画是否结束
     */
    private boolean isAnimatorEnd;
    /**
     * 路径测量类
     */
    private PathMeasure mPathMeasure;
    /**
     * 当前动画跑到的坐标点数组(0是X，1是Y)
     */
    private float[] mCurrentPosition = new float[2];
    /**
     * 每个收益点的Y值
     */
    private float[] mIncomeYs = new float[7];
    /**
     * 折线图最高点的上方空间的距离
     */
    private float mTopPadding;
    /**
     * 收益文字的颜色
     */
    private int mIncomeTextColor;
    /**
     * 左右两边的空隙值
     */
    private float mLeftAndRightPadding;
    /**
     * Y轴线的颜色
     */
    private int mYAxisColor;
    /**
     * 作为mPath副路径使用
     */
    private Path mPath_F;

    public LineChartView(final Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources resources = context.getResources();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
        mBottomHideHeight = array.getDimension(R.styleable.LineChartView_bottomHideHeight, resources.getDimension(R.dimen.d40));
        mBelowChartBackground = array.getColor(R.styleable.LineChartView_belowChartBackground, Color.TRANSPARENT);
        mBrokenLineColor = array.getColor(R.styleable.LineChartView_brokenLineColor, Color.WHITE);
        mBrokenLineSize = array.getDimension(R.styleable.LineChartView_brokenLineSize, resources.getDimension(R.dimen.d2));
        mIncomeTextSize = array.getDimension(R.styleable.LineChartView_incomeTextSize, resources.getDimension(R.dimen.s10));
        mCircleRadius = array.getDimension(R.styleable.LineChartView_circleRadius, resources.getDimension(R.dimen.d5));
        mBrokenLineBackgroundStart = array.getColor(R.styleable.LineChartView_brokenLineBackgroundStart, Color.parseColor("#DDFFC600"));
        mBrokenLineBackgroundEnd = array.getColor(R.styleable.LineChartView_brokenLineBackgroundEnd, Color.parseColor("#00FFC600"));
        mIncomeTextColor = array.getColor(R.styleable.LineChartView_incomeTextColor, Color.parseColor("#FFC600"));
        mYAxisColor = array.getColor(R.styleable.LineChartView_YAxisColor, Color.parseColor("#000000"));
        mDsateTextSize = array.getDimension(R.styleable.LineChartView_dateTextSize, resources.getDimension(R.dimen.s13));
        mCircleLineSize = array.getDimension(R.styleable.LineChartView_circleLineSize, resources.getDimension(R.dimen.d4));
        mTopPadding = array.getDimension(R.styleable.LineChartView_topPadding, mCircleRadius - mCircleLineSize);

        initPaint();
        getSelfSize();
        initData();

        array.recycle();// 循环再利用
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mBelowChartBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBelowChartBackgroundPaint.setStyle(Paint.Style.FILL);
        mBelowChartBackgroundPaint.setColor(mBelowChartBackground);

        mBrokenLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBrokenLinePaint.setStyle(Paint.Style.STROKE);
        mBrokenLinePaint.setColor(mBrokenLineColor);
        mBrokenLinePaint.setStrokeWidth(mBrokenLineSize);

        // mBrokenLinePaint.setPathEffect(new CornerPathEffect(20));

        mBrokenLineBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBrokenLineBackgroundPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mBrokenLineColor);
        mTextPaint.setTextSize(mIncomeTextSize);

        mYLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mYLinePaint.setStyle(Paint.Style.STROKE);
        mYLinePaint.setColor(mYAxisColor);
        mYLinePaint.setStrokeWidth(2f);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mBrokenLineColor);
    }

    /**
     * 获得画图控件总大小
     */
    private void getSelfSize() {

        ViewTreeObserver vto = LineChartView.this.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                // removeOnPreDrawListener这一条，这个可以保证系统运行一次。
                LineChartView.this.getViewTreeObserver().removeOnPreDrawListener(this);

                mSelfHeight = LineChartView.this.getMeasuredHeight();
                mSelfWidth = LineChartView.this.getMeasuredWidth();

                initData();

                return true;
            }
        });
    }

    /**
     * 初始化一些数据
     */
    private void initData() {
        // mEachXWidth = mSelfWidth/7;
        // mLeftAndRightPadding = mEachXWidth/2;

        float textWidth = mTextPaint.measureText("28日");
        mEachXWidth = (mSelfWidth - textWidth) / 6;
        mLeftAndRightPadding = textWidth / 2;

        mYHeight = mSelfHeight - mTopPadding - mBottomHideHeight;

        LinearGradient lg = new LinearGradient(0, mTopPadding, 0, mSelfHeight - mBottomHideHeight, mBrokenLineBackgroundStart,
                mBrokenLineBackgroundEnd, Shader.TileMode.MIRROR);
        mBrokenLineBackgroundPaint.setShader(lg);// 设置折线图背景色为过渡色
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画背景色
        mBelowChartBackgroundPaint.setColor(mBelowChartBackground);
        canvas.drawRect(0, 0, mSelfWidth, mSelfHeight, mBelowChartBackgroundPaint);

        // 画底部高度背景
        // mBelowChartBackgroundPaint.setColor(Color.parseColor("#20000000"));
        // canvas.drawRect(0, mSelfHeight - mBottomHideHeight, mSelfWidth,
        // mSelfHeight, mBelowChartBackgroundPaint);

        // 画Y轴的线
        drawYAxle(canvas);

        if (mRatios != null && mRatios.size() > 0) {
            // 画底部日期文字
            drawDateText(canvas);
            if (isAnimatorEnd) {
                // 画今日收益数字
                // drawTodayIncomeText(canvas);
                // 画折线背景颜色
                canvas.drawPath(mPath_F, mBrokenLineBackgroundPaint);
                // 画折线图
                canvas.drawPath(mPath, mBrokenLinePaint);
            } else {
                // 画折线背景颜色
                canvas.drawPath(mBrokenLineBackgroundPath, mBrokenLineBackgroundPaint);
                // 画折线图
                canvas.drawPath(mBrokenLinePath, mBrokenLinePaint);
            }

            // 画尾部圆圈
            mCirclePaint.setColor(mBrokenLineColor);
            canvas.drawCircle(mCurrentPosition[0], mCurrentPosition[1], mCircleRadius - mCircleLineSize, mCirclePaint);
        }
        drawIncomeCircle(canvas);
    }

    /**
     * 画Y的轴线
     */
    private void drawYAxle(Canvas canvas) {
        for (int i = 0; i < 7; i++) {
            canvas.drawLine(mLeftAndRightPadding + i * mEachXWidth, mTopPadding, mLeftAndRightPadding + i * mEachXWidth, mSelfHeight
                    - mBottomHideHeight, mYLinePaint);
        }
    }

    /**
     * 绘制底部日期文字
     *
     * @param canvas
     */
    private void drawDateText(Canvas canvas) {
        mTextPaint.setTextSize(mDsateTextSize);
        mTextPaint.setColor(mBrokenLineColor);

        float paddingTop = (mBottomHideHeight - mDsateTextSize) / 2;
        float Y = mSelfHeight - mBottomHideHeight + paddingTop + mDsateTextSize;
        // FontMetrics对象
        FontMetrics fontMetrics2 = mTextPaint.getFontMetrics();
        float bottom = fontMetrics2.bottom;

        float textBottomY = Y - bottom + mDsateTextSize / 3;

        for (int i = 0; i < mDateTexts.size(); i++) {
            float textWidth = mTextPaint.measureText(mDateTexts.get(i));
            canvas.drawText(mDateTexts.get(i), i * mEachXWidth + mLeftAndRightPadding - textWidth / 2, textBottomY, mTextPaint);// 绘制文本
        }
    }

    /**
     * 绘制今日收益文字
     *
     * @param canvas
     */
    private void drawTodayIncomeText(Canvas canvas) {
        mTextPaint.setTextSize(mIncomeTextSize);

        // 计算出文字的高度
        float textHeight = Math.abs(mTextPaint.descent() + mTextPaint.ascent());
        // 计算出文字的宽度
        float textWidth = mTextPaint.measureText(mNowIncome);

        // 计算出文字的绘制坐标
        float textX = mLeftAndRightPadding + 6 * mEachXWidth - textWidth - textHeight / 2;
        float textY = mIncomeYs[6] - textHeight - textHeight / 2;

        // 计算出圆角矩形的坐标
        float rectLeft = textX - textHeight / 2;
        float rectTop = textY - textHeight - textHeight / 2;
        float rectRight = textX + textWidth + textHeight / 2;
        float rectBottom = textY + textHeight / 2;

        // 画收益数字的圆角矩形
        RectF textRectf = new RectF(rectLeft, rectTop, rectRight, rectBottom);
        canvas.drawRoundRect(textRectf, textHeight / 3, textHeight / 3, mTextPaint);

        // 画圆角矩形下方的尾巴
        Path path = new Path();
        path.moveTo(rectRight - textHeight * 2, rectBottom - 1);// -1是为了防止连接处有缝隙
        path.lineTo(rectRight - textHeight, rectBottom - 1);
        path.lineTo(rectRight - textHeight / 2, rectBottom + textHeight * 0.7f);
        canvas.drawPath(path, mTextPaint);

        mTextPaint.setColor(mIncomeTextColor);
        canvas.drawText(mNowIncome, textX, textY, mTextPaint);// 绘制文本
    }

    /**
     * 画每个收益点处的圆点
     */
    private void drawIncomeCircle(Canvas canvas) {
        mCirclePaint.setColor(mBrokenLineColor);

        for (int i = 0; i < mIncomeYs.length; i++) {

            if (mCurrentPosition[0] > i * mEachXWidth + mLeftAndRightPadding) {
                canvas.drawCircle(i * mEachXWidth + mLeftAndRightPadding, mIncomeYs[i], mCircleRadius - mCircleLineSize, mCirclePaint);
            }
        }
    }

    /**
     * 计算每个收益的Y值
     */
    private void computeIncomeYValue() {
        if (mRatios != null) {
            for (int i = 0; i < mRatios.size(); i++) {
                // 计算出每个收益点的长度比
                float b = (float) (1 - mRatios.get(i));

                mIncomeYs[i] = (float) (mYHeight * b) + mTopPadding;
            }
            addPath();
        }
    }

    /**
     * 添加路径
     */
    private void addPath() {
        mPath = new Path();
        mBrokenLinePath = new Path();
        mBrokenLineBackgroundPath = new Path();
        mBrokenLineBackgroundPath_F = new Path();
        mPath_F = new Path();

        for (int i = 0; i < mIncomeYs.length; i++) {
            if (i == 0) {
                mPath.moveTo(mLeftAndRightPadding, mIncomeYs[i]);
                mBrokenLinePath.moveTo(mLeftAndRightPadding, mIncomeYs[i]);

//                mPath.quadTo((mEachXWidth / 2 + mLeftAndRightPadding), mIncomeYs[i] + mIncomeYs[i + 1] / 2, mEachXWidth + mLeftAndRightPadding,
// mIncomeYs[i + 1]);

                mBrokenLineBackgroundPath_F.moveTo(mLeftAndRightPadding, mIncomeYs[i]);
            } else {
//                if (!(mIncomeYs.length == 7)) {
//                    mPath.reset();
//                    mPath.moveTo(mLeftAndRightPadding + mEachXWidth * (i - 1), mIncomeYs[i - 1]);
//                    mPath.quadTo((i - 1) * mEachXWidth + mLeftAndRightPadding + mEachXWidth / 2, (mIncomeYs[i] + mIncomeYs[i + 1]) / 2, i *
// mEachXWidth + mLeftAndRightPadding, mIncomeYs[i]);
//                }
                mPath.lineTo(i * mEachXWidth + mLeftAndRightPadding, mIncomeYs[i]);
            }


        }

        // 最后一点的X值
        float theLastX = (mIncomeYs.length - 1) * mEachXWidth + mLeftAndRightPadding;

        mPath_F.addPath(mPath);
        mPath_F.lineTo(theLastX, mSelfHeight - mBottomHideHeight);
        mPath_F.lineTo(mLeftAndRightPadding, mSelfHeight - mBottomHideHeight);

        mPathMeasure = new PathMeasure();
        mPathMeasure.setPath(mPath, false);
        mPathLenght = mPathMeasure.getLength(); // 计算出路径的总长度
    }

    /**
     * 启动折线图动画
     */
    private void startAnimator() {
        if (mValueAnimator != null) {
            mValueAnimator.cancel();
        }
        mValueAnimator = ValueAnimator.ofFloat(0, mPathLenght);

        mValueAnimator.setDuration(mDuration);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                isAnimatorEnd = false;
                float mCurrentPath = (float) animation.getAnimatedValue();

                mPathMeasure.getPosTan(mCurrentPath, mCurrentPosition, null);

                mBrokenLinePath.lineTo(mCurrentPosition[0], mCurrentPosition[1]);

                mBrokenLineBackgroundPath_F.lineTo(mCurrentPosition[0], mCurrentPosition[1] + mBrokenLineSize / 2);

                mBrokenLineBackgroundPath.reset();// 清除之前的旧路径
                mBrokenLineBackgroundPath.moveTo(0f, mIncomeYs[0]);
                mBrokenLineBackgroundPath.addPath(mBrokenLineBackgroundPath_F);
                mBrokenLineBackgroundPath.lineTo(mCurrentPosition[0], mSelfHeight - mBottomHideHeight);
                mBrokenLineBackgroundPath.lineTo(mLeftAndRightPadding, mSelfHeight - mBottomHideHeight);

                invalidate();
            }
        });
        mValueAnimator.start();

        mValueAnimator.addListener(new AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimatorEnd = true;
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * 加载表中的数据
     *
     * @param incomes 七天内每天的百分比收益数组
     */
    public void dataLoading(List<Double> incomes, long currentTime) {
        Double income = incomes.get(incomes.size() - 1);
        this.mNowIncome = income + "";

        computeRatio(incomes);
        getDateTitleText(currentTime);
        computeIncomeYValue();
        startAnimator();
    }

    /**
     * 根据七日内收益值计算出每日收益所占的百分比
     *
     * @param incomes 七日内的每天收益值
     */
    private void computeRatio(List<Double> incomes) {
        // 获得七日内最大的收益值
        Double max = Collections.max(incomes);
        mRatios = new ArrayList<Float>();

        if (max == 0) {// 所有收益数据都是0
            for (int i = 0; i < 7; i++) {
                mRatios.add(0f);
            }
            return;
        }

        for (int i = 0; i < incomes.size(); i++) {
            double ratio = incomes.get(i) / max;
            NumberFormat nf = new DecimalFormat("#.##");
            String format = nf.format(ratio);
            Float valueOf = Float.valueOf(format);
            mRatios.add(valueOf);
        }
    }

    /**
     * 获得日期标题文本数组(七天)
     */
    private void getDateTitleText(long currentTime) {
        mDateTexts.clear();
        SimpleDateFormat df = new SimpleDateFormat("M-d");

        for (int i = 0; i < 7; i++) {
            if (i != 0) {
                currentTime = currentTime - 3600 * 24 * 1000;
            }
            String format = df.format(currentTime);
            mDateTexts.add(format);
        }

        Collections.reverse(mDateTexts);// 反序
    }

    /**
     * 设置折线图的动画时长
     *
     * @param duration 时长(ms)
     */
    public void setDuration(long duration) {
        this.mDuration = duration;
    }
}
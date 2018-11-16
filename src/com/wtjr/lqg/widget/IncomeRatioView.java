package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.wtjr.lqg.R;
/**
 * 收益比列图
 *
 */
public class IncomeRatioView extends View {
    /**
     * 画笔
     */
    private Paint mPaint;
     /**
     * 圆的最大直径
     */
    private float mCircleMaxDiameter;
    /**
     * 圆的线宽
     */
    private float mCircleLineWidth;
    /**
     * 体验金收益
     */
    private float mTyjIncome;
    /**
     * 投资收益
     */
    private float mInvestmentIncome;
    /**
     * 零钱宝收益
     */
    private float mLqbIncome;
    /**
     * 空隙的角度值
     */
    private float mBlankAngle = 0;
    /**
     * 画到多少度
     */
    private float mToAngle = -90;
    /**
     * 收益比例数组，0是体验金收益比例，1是投资收益比例，2是零钱宝收益比例
     */
    private float[] mIncomeRatio = new float[3];
    /**
     * 总收益
     */
    private float mTotalIncome;
    /**
     * 圆的矩形容器
     */
    private RectF mOval;

    // private int color;

    public IncomeRatioView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public IncomeRatioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        Resources resources = context.getResources();
        
//      resources.getColor(R.color.black);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.IncomeRatioView);

        mCircleLineWidth = ta.getDimension(R.styleable.IncomeRatioView_circleLineWidth, resources.getDimension(R.dimen.d16));
        initPaint();

        ta.recycle();
    }

    public IncomeRatioView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mSelfWidth = getMeasuredWidth();
        int mSelfHeight = getMeasuredHeight();
        if (mSelfWidth < mSelfHeight) {
            mCircleMaxDiameter = mSelfWidth;
        } else {
            mCircleMaxDiameter = mSelfHeight;
        }
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(mCircleLineWidth);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        if(mTyjIncome == 0 && mInvestmentIncome == 0 && mLqbIncome == 0) {
            return;
        }
        
        computeAngleRatio(canvas);
    }
    
    /**
     * 计算圆弧的角度比例
     * @param canvas
     */
    private void computeAngleRatio(Canvas canvas) {
        int count = -1;
        for (int i = 0; i < mIncomeRatio.length; i++) {
            if(mIncomeRatio[i] != 0) {
                count++;
            }
        }
        float angle = 360-count*mBlankAngle;
        
        float tyjAngle = mIncomeRatio[0]*angle;
        float investmentAngle = mIncomeRatio[1]*angle;
        float lqbAngle = mIncomeRatio[2]*angle;
        
        float ovalLeft =  mCircleLineWidth / 2;
        float ovalTop =  mCircleLineWidth / 2;
        float ovalRight = mCircleMaxDiameter - mCircleLineWidth / 2;
        float ovalBottom = mCircleMaxDiameter - mCircleLineWidth / 2;
        mOval = new RectF(ovalLeft, ovalTop, ovalRight, ovalBottom);

        drawRatioCircle(canvas, lqbAngle, Color.parseColor("#fedb41"),count, true);
        drawRatioCircle(canvas, investmentAngle,Color.parseColor("#fd8b49"), count, false);
        drawRatioCircle(canvas, tyjAngle, Color.parseColor("#9ccc46"),count, true);
        mToAngle = -90;
    }

    /**T
     * 画圆弧
     * @param canvas 画布
     * @param angle 画的角度
     * @param color 画笔颜色
     * @param count
     * @param rough 是否是定期标加粗
     */
    private void drawRatioCircle(Canvas canvas,float angle,int color,int count, boolean rough) {
        mPaint.setColor(color);
        if(rough){
            mPaint.setStrokeWidth(24);
        }else{
            mPaint.setStrokeWidth(32);
        }
        if (angle != 0){
            canvas.drawArc(mOval, mToAngle, angle, false, mPaint);
        }
        mToAngle+=angle;
        
        mPaint.setStrokeWidth(24);
        if(angle != 0 && count != 0) {
            mPaint.setColor(Color.TRANSPARENT);
            canvas.drawArc(mOval, mToAngle, mBlankAngle, false, mPaint);
            mToAngle+=mBlankAngle;
        }
    }

    /**
     * 设置体验金收益
     * @param tyjIncome 体验金收益值
     * @return 自身对象
     */
    public IncomeRatioView setTyjIncome(double tyjIncome) {
        this.mTyjIncome = (float)tyjIncome;
        return this;
    }
    
    /**
     * 设置投资收益
     * @param tyjIncome 投资收益值
     * @return 自身对象
     */
    public IncomeRatioView setInvestmentIncome(double investmentIncome) {
        this.mInvestmentIncome = (float)investmentIncome;
        return this;
    }
    
    /**
     * 设置零钱宝收益
     * @param tyjIncome 零钱宝收益值
     * @return 自身对象
     */
    public IncomeRatioView setLqbIncome(double lqbIncome) {
        this.mLqbIncome = (float)lqbIncome;
        return this;
    }
    
    /**
     * 显示
     */
    public void show() {
        if(mTyjIncome == 0 && mInvestmentIncome == 0 && mLqbIncome == 0) {
            mTyjIncome = 1;
            mInvestmentIncome = 1;
            mLqbIncome = 1;
        }
        
        mTotalIncome = mTyjIncome + mInvestmentIncome + mLqbIncome;
        mIncomeRatio[0] = mTyjIncome / mTotalIncome;
        mIncomeRatio[1] = mInvestmentIncome / mTotalIncome;
        mIncomeRatio[2] = mLqbIncome/mTotalIncome;
        invalidate();
    }
}

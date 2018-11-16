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

public class IncomeMoneyView extends View {
    /**
     * 画笔
     */
    private Paint mPaint;
    
    private float[] incomeMoneys = new float[4];

    private int mSelfHeight;

    private Paint mTextPaint;
    
    private float upPadding = 10f;
    private float downPadding = 20f;
    
    
    private float x1 = 100f;
    private float x2 = 100f;
    private float x3 = 100f;
    private float x4 = 100f;
    
    public IncomeMoneyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public IncomeMoneyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        Resources resources = context.getResources();

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.LineChartView);
        
//        mCircleLineWidth = ta.getDimension(R.styleable.IncomeRatioView_circleLineWidth, resources.getDimension(R.dimen.d10));

        initPaint();
        
        ta.recycle();
    }

    public IncomeMoneyView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int mSelfWidth = getMeasuredWidth();
        mSelfHeight = getMeasuredHeight();
//        if (mSelfWidth < mSelfHeight) {
//            mCircleMaxDiameter = mSelfWidth;
//        } else {
//            mCircleMaxDiameter = mSelfHeight;
//        }
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(100f);
        
        
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Style.STROKE);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        
        //计算出文字的高度
        float textHeight = Math.abs(mTextPaint.descent() + mTextPaint.ascent());
        //计算出文字的宽度
//        float textWidth = mTextPaint.measureText("");
        
        float maxHeight = mSelfHeight-2*textHeight-upPadding-downPadding;
        
        
        canvas.drawLine(x1, textHeight+upPadding, x1, textHeight+upPadding+maxHeight, mPaint);
        
        canvas.drawText("123", 0, textHeight, mTextPaint);
        
        canvas.drawText("123", 0, textHeight+upPadding+maxHeight+downPadding+textHeight, mTextPaint);
        
        
//        computeAngleRatio(canvas);
    }
    
    
}

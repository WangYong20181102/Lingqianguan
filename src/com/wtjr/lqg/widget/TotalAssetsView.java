package com.wtjr.lqg.widget;

import com.wtjr.lqg.R;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.MoneyFormatUtil;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TotalAssetsView extends View {

    private Paint mPaint;

    /**
     * 半径差值
     */
    private float mRadiusDifference = 10f;

    /**
     * 最大圆的半径
     */
    private float maxRadius1;
    /**
     * 最二大圆的半径
     */
    private float maxRadius2;
    /**
     * 最三大圆的半径
     */
    private float maxRadius3;
    /**
     * 最四大圆的半径
     */
    private float maxRadius4;
    /**
     * 最五大圆的半径
     */
    private float maxRadius5;

    /**
     * 标注的横线长度
     */
    private float mLineLength = 70f;
    /**
     * 标注的斜线长度
     */
    private float mObliqueLineLength = 50f;
    /**
     * 收益比例数组，0是体验金收益比例，1是投资收益比例，2是零钱宝收益比例
     */
    private float[] mAngles = new float[5];
    private float[] mAngles1 = new float[5];
    private int[] mAngles2 = {0, 1, 2, 3, 4};

    private float[] mRadius = new float[5];

    /**
     * 标注斜线的边线长度
     */
    private float mObliqueLineSideLineLength;
    /**
     * 标注的线宽
     */
    private float mLineWidth = 2f;
    /**
     * 文字大小
     */
    private float mTextSize = 22f;
    /**
     * 标注与文字之间的间隙值
     */
    private float mTextLabelMargin = 5f;

    public TotalAssetsView(Context context, AttributeSet attrs) {
        super(context, attrs);

        Resources resources = context.getResources();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineChartView);
        mTextSize = array.getDimension(R.styleable.TotalAssetsView_tav_textSize, resources.getDimension(R.dimen.s11));
        array.recycle();// 循环再利用
        initPaint();
        initData();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setTextSize(mTextSize);
    }

    private void initData() {
        float textWidth = mPaint.measureText("可用余额");
        mLineLength = textWidth + 10;

        double pow = Math.pow(mObliqueLineLength, 2);
        mObliqueLineSideLineLength = (float) Math.sqrt(pow / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth() / 2, getHeight() / 2);

        maxRadius1 = (float) (getWidth() / 2 - mLineLength - mObliqueLineSideLineLength);
        maxRadius2 = maxRadius1 - mRadiusDifference;
        maxRadius3 = maxRadius2 - mRadiusDifference;
        maxRadius4 = maxRadius3 - mRadiusDifference;
        maxRadius5 = maxRadius4 - mRadiusDifference;

        if (mAngles[0] == 0 && mAngles[1] == 0 && mAngles[2] == 0 && mAngles[3] == 0 && mAngles[4] == 0) {
            mPaint.setColor(getResources().getColor(R.color.FC_FFC65C));
            RectF oval = new RectF();
            oval.left = -maxRadius1;
            oval.top = -maxRadius1;
            oval.right = maxRadius1;
            oval.bottom = maxRadius1;
            canvas.drawArc(oval, 0, 360, true, mPaint);
            return;
        }

        drawArc(canvas);
    }

    private float mProcessAngle;

    /**
     * 画三个扇形圆
     */
    private void drawArc(Canvas canvas) {
        mProcessAngle = 0f;
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 0:
                    mPaint.setColor(getResources().getColor(R.color.FC_FEDB41));// 粉色
                    break;
                case 1:
                    mPaint.setColor(getResources().getColor(R.color.FC_FFA82C));// 黄色
                    break;
                case 2:
                    mPaint.setColor(getResources().getColor(R.color.FC_FD8B49));// 绿色
                    break;
                case 3:
                    mPaint.setColor(getResources().getColor(R.color.FC_FF682C));// 绿色
                    break;
                case 4:
                    mPaint.setColor(getResources().getColor(R.color.FC_FF7C03));
                    break;
            }

            RectF oval = new RectF();
            oval.left = -mRadius[i];
            oval.top = -mRadius[i];
            oval.right = mRadius[i];
            oval.bottom = mRadius[i];

            if (mAngles[i] == 0) {
                continue;
            }

            L.hintI(mProcessAngle + "==========" + mAngles[i]);
            canvas.drawArc(oval, mProcessAngle, mAngles[i], true, mPaint);

            mProcessAngle = mProcessAngle + mAngles[i];

            drawLabel(canvas, mProcessAngle - mAngles[i] / 2, i);
        }
    }

    /**
     * 画标注
     */
    private void drawLabel(Canvas canvas, float angle, int i) {


        float anglePI1 = (float) (angle * Math.PI / 180);

        float x = (float) (mRadius[i] * Math.cos(anglePI1));
        float y = (float) (mRadius[i] * Math.sin(anglePI1));


        if (mAngles[i] == 0) {// 没有该扇形数据，不绘图下面的对应文字
            return;
        }
        if (x >= 0 && y >= 0) {// 在右下的1/4圆中
            canvas.drawLine(x, y, x + mObliqueLineSideLineLength, y + mObliqueLineSideLineLength, mPaint);
            canvas.drawLine(x + mObliqueLineSideLineLength, y + mObliqueLineSideLineLength, x + mObliqueLineSideLineLength + mLineLength, y + mObliqueLineSideLineLength, mPaint);
            drawText(canvas, i, x + mObliqueLineSideLineLength + mLineLength, y + mObliqueLineSideLineLength, 0);
        } else if (x <= 0 && y >= 0) {// 在左下的1/4圆中
            canvas.drawLine(x, y, x - mObliqueLineSideLineLength, y + mObliqueLineSideLineLength, mPaint);
            canvas.drawLine(x - mObliqueLineSideLineLength, y + mObliqueLineSideLineLength, x - mObliqueLineSideLineLength - mLineLength, y + mObliqueLineSideLineLength, mPaint);
            drawText(canvas, i, x - mObliqueLineSideLineLength - mLineLength, y + mObliqueLineSideLineLength, 1);
        } else if (x <= 0 && y <= 0) {// 在左上的1/4圆中
            canvas.drawLine(x, y, x - mObliqueLineSideLineLength, y - mObliqueLineSideLineLength, mPaint);
            canvas.drawLine(x - mObliqueLineSideLineLength, y - mObliqueLineSideLineLength, x - mObliqueLineSideLineLength - mLineLength, y - mObliqueLineSideLineLength, mPaint);
            drawText(canvas, i, x - mObliqueLineSideLineLength - mLineLength, y - mObliqueLineSideLineLength, 2);
        } else {// 在右上的1/4圆中
            canvas.drawLine(x, y, x + mObliqueLineSideLineLength, y - mObliqueLineSideLineLength, mPaint);
            canvas.drawLine(x + mObliqueLineSideLineLength, y - mObliqueLineSideLineLength, x + mObliqueLineSideLineLength + mLineLength, y - mObliqueLineSideLineLength, mPaint);
            drawText(canvas, i, x + mObliqueLineSideLineLength + mLineLength, y - mObliqueLineSideLineLength, 3);
        }
    }

    private void drawText(Canvas canvas, int i, float x, float y, int direction) {
        String r = (MoneyFormatUtil.formatZ(mAngles[i] / 360 * 100));
        String ratio = r + "%";
        float textWidth1 = mPaint.measureText(ratio);        //百分比
        String zi = "";
        float textWidth2 = 0;
        switch (i) {
            case 0:
                zi = "零钱宝";
                break;
            case 1:
                zi = "可用余额";
                break;
            case 2:
                zi = "定期标";
                break;
            case 3:
                zi = "新手标";
                break;
            case 4:
                zi = "周周升";
                break;
        }
        textWidth2 = mPaint.measureText(zi);
        switch (direction) {
            case 0:// 在右下的1/4圆中
            case 3:// 在右上的1/4圆中
                canvas.drawText(ratio, x - textWidth1, y - mTextLabelMargin, mPaint);
                canvas.drawText(zi, x - textWidth2, y + mTextLabelMargin + mTextSize, mPaint);
                break;
            case 1:// 在左下的1/4圆中
            case 2:// 在左上的1/4圆中
                canvas.drawText(ratio, x, y - mTextLabelMargin, mPaint);
                canvas.drawText(zi, x, y + mTextLabelMargin + mTextSize, mPaint);
                break;
        }
    }

    /**
     * 显示(零钱宝，余额，投资三个比例值)
     */
    public void show(float balance, float investment, float lqb, float xsb, float liCaiPlanSR) {

        mAngles[0] = 360 * lqb;
        mAngles[1] = 360 * balance;
        mAngles[2] = 360 * investment;
        mAngles[3] = 360 * xsb;
        mAngles[4] = 360 * liCaiPlanSR;
        for (int i = 0; i < mAngles.length; i++) {
            mAngles1[i] = mAngles[i];//把mAngles的元素添加到mAngles1中
        }
        Log.i("LogContent", mAngles[0] + "----" + mAngles[1] + "----" + mAngles[2] + "----" + mAngles[3]);

        for (int i = 0; i < mAngles1.length - 1; i++) {
            for (int j = i + 1; j < mAngles1.length; j++) {
                if (mAngles1[i] < mAngles1[j]) {
                    float temp = mAngles1[i];
                    mAngles1[i] = mAngles1[j];
                    mAngles1[j] = temp;
                    int numIndex = mAngles2[i];
                    mAngles2[i] = mAngles2[j];
                    mAngles2[j] = numIndex;
                }
            }
        }
        mRadius[mAngles2[0]] = maxRadius1;
        mRadius[mAngles2[1]] = maxRadius2;
        mRadius[mAngles2[2]] = maxRadius3;
        mRadius[mAngles2[3]] = maxRadius4;
        mRadius[mAngles2[4]] = maxRadius5;

        invalidate();
    }

    /**
     * 0是零钱宝，1是可用余额，2是定期标
     */
    public void clickChangeView(int location) {
        float max = mRadius[0];// 最大值
        int maxLocation = 0;// 最大值的位置

        // 取出最大的半径值和位置
        for (int i = 0; i < mRadius.length; i++) {
            if (max < mRadius[i]) {
                max = mRadius[i];
                maxLocation = i;
            }
        }
        // 将最大值和选中的互换
        mRadius[maxLocation] = mRadius[location];
        mRadius[location] = max;
        invalidate();
    }
}

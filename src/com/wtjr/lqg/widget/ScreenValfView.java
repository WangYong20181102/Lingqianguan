package com.wtjr.lqg.widget;

import org.xutils.x;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.utils.ScreenUtils;

/**
 * 获得屏幕一半控件
 *
 * @author Myf
 */
public class ScreenValfView extends View {

    private Context mContext;
    /**
     * 屏幕宽度的一半
     */
    private int mScreenWidthValf;
    private Paint mPaint;

    public ScreenValfView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LqgApplication app = (LqgApplication) x.app();

        mScreenWidthValf = (int) (app.mScreenWidth / 2 - (10 * app.mDensity) * 5 - (10 * app.mDensity));  //因为左右两边留有间距，所以 - (10*app.mDensity)，拆开写容易理解
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.parseColor("#e8e8e8"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, getHeight(), mScreenWidthValf, getHeight(), mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
//		super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(mScreenWidthValf, 1);
    }
}

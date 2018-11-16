package com.wtjr.lqg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.wtjr.lqg.R;

/**
 * 小红点提示
 *
 * @author Myf
 */
public class ImageRedDotView extends ImageView {
    /**
     * 画笔
     */
    private Paint paint;
    /**
     * 密度
     */
    private float density;
    /**
     * 红点半径
     */
    private float redDotRadius = 3;
    /**
     * 红点x坐标
     */
    private float cX = 0;
    /**
     * 红点y坐标
     */
    private float cY = 0;
    /**
     * 默认都是隐藏的
     */
    private Boolean isShow = false;

    public ImageRedDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setColor(Color.parseColor("#FF5A00"));
    }

    /**
     * 初始化数据
     *
     * @param right 控件最右边的位置
     */
    private void initData(int right) {
        redDotRadius = redDotRadius * density;
        cY = redDotRadius;
        cX = right - redDotRadius;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        if (changed) {
            initData(right);
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isShow) {
            //画红点
            canvas.drawCircle(cX, cY + 6, redDotRadius, paint);
        }
        super.onDraw(canvas);
    }

    /**
     * 设置显示红点
     *
     * @param isShow 是显示的
     */
    public void isShowRedDot(Boolean isShow) {
        this.isShow = isShow;
        //刷新
        postInvalidate();
    }
}

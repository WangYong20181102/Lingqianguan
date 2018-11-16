package com.wtjr.lqg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wtjr.lqg.activities.LqgApplication;

import org.xutils.x;

/**
 * Created by WangYong on 2017/4/5.
 */

public class LqbLineChartView extends View {
    /**
     * 画笔
     */
    private Paint paint;

    /**
     * 路径
     */
    private Path path;
    private LqgApplication app;
    public LqbLineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initDate();
    }

    private void initDate() {
        app = (LqgApplication) x.app();

        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);   //消除锯齿
        paint.setStrokeWidth((float) 2.0);      //画笔宽度
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#dddddd"));    //画笔颜色
        //初始化路径
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);      //画布颜色
        path.moveTo(0, 20);                           //起始点
        path.lineTo(150, 20);                           //连线到下一点
        path.lineTo(175, 0);                      //连线到下一点
        path.lineTo(200, 20);                      //连线到下一点
        path.lineTo(app.mScreenWidth, 20);                      //连线到下一点
        canvas.drawPath(path, paint);
    }
}

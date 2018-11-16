package com.wtjr.lqg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.widget.EditText;

import com.wtjr.lqg.R;


public class LineEditText extends EditText {
    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);//空心
        paint.setColor(getResources().getColor(R.color.color_line_3));
        Path path = new Path();
        //通过moveto，lineto的x，y坐标确定虚线实横，纵，还是倾斜
        path.moveTo(0, 10);//Set the beginning of the next contour to the point (x,y)
        path.lineTo(480, 10);//Add a line from the last point to the specified point (x,y).
        //DashPathEffect  可以使用DashPathEffect来创建一个虚线的轮廓(短横线/小圆点)，而不是使用实线
        //float[] { 5, 5, 5, 5 }值控制虚线间距，密度
        PathEffect effects = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
        paint.setPathEffect(effects);
        canvas.drawPath(path, paint);
    }
}
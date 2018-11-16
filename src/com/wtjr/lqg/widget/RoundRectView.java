package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.wtjr.lqg.R;
/**
 * 绘画圆角矩形
 * @author dell
 */
public class RoundRectView extends TextView {
    private Mode mNowMode;
    
    enum Mode{
        /**
         * 按钮模式
         */
        Button,
        /**
         * 单选按钮模式
         */
        RadioButton;
    }
    
    
    /**
     * 计算得到当前控件的宽
     */
	private int measureWidth;
	/**
	 * 计算得到当前控件的高
	 */
	private int measureHeight;
	/**
	 * 内矩形画笔色
	 */
	private Paint paint;
	/**
	 * 外矩形画笔色
	 */
	private Paint borderPaint;
	/**
	 * 矩形对象
	 */
	private RectF rect;
	/**
	 * 内矩形按下时候的颜色
	 */
	private int pressedColor;
	/**
	 * 内矩形不按下时候的颜色
	 */
	private int noPressedColor;
	/**
	 * 外矩形按下时候的颜色
	 */
	private int pressedBorderColor;
	/**
	 * 外矩形不按下时候的颜色
	 */
	private int noPressedBorderColor;
	/**
	 * 内矩形的宽
	 */
	private float roundSizeX;
	/**
	 * 内矩形的高
	 */
	private float roundSizeY;
	/**
	 * 内外之间的间距
	 */
	private float borderSize;
	/**
	 * 外矩形的宽
	 */
	private float roundBorderSizeX;
	/**
	 * 外矩形的高
	 */
	private float roundBorderSizeY;

	public RoundRectView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initData(context, attrs);
		initPaint();
	}


	/**
	 * 初始化数据
	 */
	private void initData(Context context, AttributeSet attrs) {
		TypedArray tpye = context.obtainStyledAttributes(attrs,R.styleable.RoundRectView); 
		pressedColor = tpye.getColor(R.styleable.RoundRectView_pressed_color, Color.parseColor("#4ABCE6"));//
		noPressedColor = tpye.getColor(R.styleable.RoundRectView_no_pressed_color, Color.parseColor("#D6D6D6"));//
		
		pressedBorderColor = tpye.getColor(R.styleable.RoundRectView_pressed_border_color, Color.parseColor("#A2D6EA"));//
		noPressedBorderColor = tpye.getColor(R.styleable.RoundRectView_no_pressed_border_color, Color.TRANSPARENT); 
		
		roundSizeX = tpye.getDimension(R.styleable.RoundRectView_round_size_x, 0); 
		roundSizeY = tpye.getDimension(R.styleable.RoundRectView_round_size_y, 0); 
		
		borderSize = tpye.getDimension(R.styleable.RoundRectView_border_size, 0); 
		
		roundBorderSizeX = tpye.getDimension(R.styleable.RoundRectView_round_border_size_x, 0); 
		roundBorderSizeY = tpye.getDimension(R.styleable.RoundRectView_round_border_size_y, 0); 
		
		mNowMode = Mode.Button;
		setClickable(true);
		tpye.recycle();
		
	}	
	
	
	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		//设置内矩形颜色
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(noPressedColor);

		borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		borderPaint.setStyle(Paint.Style.FILL);
		borderPaint.setColor(noPressedBorderColor);
		
		rect = new RectF();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		measureWidth = getWidth();
		measureHeight = getHeight();

	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (borderSize>0) {
			border(canvas);
		}
		inRectangle(canvas);
		
		super.onDraw(canvas);
	}
	/**
	 * 画外边框
	 * @param canvas
	 */
	private void border(Canvas canvas) {
		rect.left = 0 ;
		rect.top = 0 ;
		rect.right = measureWidth;
		rect.bottom = measureHeight;
		canvas.drawRoundRect(rect, roundBorderSizeX, roundBorderSizeY, borderPaint);
	}
	/**
	 * 画内矩形
	 * @param canvas
	 */
	private void inRectangle(Canvas canvas) {
		rect.left = 0 + borderSize;
		rect.top = 0 + borderSize;
		rect.right = measureWidth - borderSize;
		rect.bottom = measureHeight - borderSize;
		canvas.drawRoundRect(rect, roundSizeX, roundSizeY, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//判断按钮是否是可以处理状态
			if (isEnabled()) {
                paint.setColor(pressedColor);
                borderPaint.setColor(pressedBorderColor);

                invalidate();
			}
			break;

		case MotionEvent.ACTION_MOVE:

			break;

		case MotionEvent.ACTION_UP:
		    switch (mNowMode) {
            case Button:
                paint.setColor(noPressedColor);
                borderPaint.setColor(noPressedBorderColor);
                break;
            }
			invalidate();
			break;

		default:
			break;
		}

		return super.onTouchEvent(event);
	}
	
	
	private void isSelect(boolean state) {

    }
}

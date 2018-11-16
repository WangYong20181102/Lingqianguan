package com.wtjr.lqg.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;

import com.wtjr.lqg.R;
/**
 * 阶梯动画
 * @author Myf
 *
 */
public class LadderView extends View{

	/**
	 * 大圆半径
	 */
	private float mMaxCircleRadius = 0;
	/**
	 * 大圆直径
	 */
	private float mMaxCircleDiameter;
	
	/**
	 * 小圆半径
	 */
	private float mMinCircleRadius=0;
	/**
	 * 小圆直径
	 */
	private float minCircleDiameter ;

	/**
	 * 大圆和大圆之间的线的高度
	 */
	private float mLineHigh = 0;
	/**
	 * 大圆和大圆之间的线的高度的一半
	 */
	private float mLineHighHalf;

	/**
	 * 画笔的宽
	 */
	private float mPaintWidth ;
	/**
	 * 画笔的一半宽
	 */
	private float mPaintWidthHalf ;
	
	/**
	 * 大圆个数
	 */
	private int mMaxCircleNumber = 4;
	
	/**
	 * 线的个数（数量等于大圆的数量减一）
	 */
	private int mLineNumber;
	
	/**
	 * 大圆之间的距离
	 */
	private float mMaxCircleDistance ;
	
	/**
	 * 大圆的启始X轴位置
	 */
	private float mMaxCx ;
	/**
	 * 大圆的启始Y轴位置
	 */
	private float mMaxCy ;
	/**
	 * 进度条前进的x坐标
	 */
	private float mProgressX ;
	/**
	 * 画笔
	 */
	private Paint mPaint;
	
	/**
	 * 文字的画笔
	 */
	private Paint mFontPaint;
	
	/**
	 * 进度条画笔
	 */
	private Paint mProgressPaint;
	
	/**
	 * 生成前景图Bitmap
	 */
	private Bitmap mFgBitmap;
	/**
	 * 新画布
	 */
	private Canvas mCanvas;

	/**
	 * 字体大小，默认是圆的半径
	 */
	private float mTextSize ;
	
	/**
     * 记录最后一次坐标
     */
    private int mLastProgress=1;
    
    /**
     * 设置动画时间
     */
    private long mDuration=0;

	/**
	 * 当前控件宽
	 */
	private int mViewWidth;
	/**
	 * 当前控件高
	 */
	private int mViewHeight;
	/**
	 * 整个控件的背景色
	 */
	private int mBackground;
	/**
	 * 被选中的字体色
	 */
	private int mSelectTextColor;
	/**
	 * 未被选中的字体色
	 */
	private int mUnSelectTextColor;
	/**
	 * 渐变色开始色
	 */
	private int mStartColor;
	/**
	 * 渐变色开始色
	 */
	private int mEndColor;
	
    
    public LadderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context, attrs);
        
    }
    
    
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// 这里面是原始的大小，需要重新计算可以修改本行
//		setMeasuredDimension(mViewWidth, mViewHeight); 
//	}
    
    
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
    		int bottom) {
    	if (changed) {
    		mViewWidth = getWidth();
    		mViewHeight = getHeight();
			// 得到控件宽高马上计算
			initCalculate();

			initBitmapPaint();
			initFontPaint();
			initProgressPaint();
		}
    	super.onLayout(changed, left, top, right, bottom);
    }
 
//    TODO
    @Override
    protected void onDraw(Canvas canvas) {
    	 super.onDraw(canvas);
    	 
        	canvas.drawColor(Color.parseColor("#efefef"));
        	
        	drawProgressLine(canvas);
        	
            // 绘制画布背景
            mCanvas.drawColor(mBackground); 
            //以下是绘制透明图片
        	canvas.drawBitmap(mFgBitmap, 0, 0, null); 
        	 
        	
        	drawMaxCircle(canvas);
        	
        	drawLine();
        	
//        	drawLastRect();
   
    }
    
    
    /**
     * 画进度条线
     */
    private void drawProgressLine(Canvas canvas) {
    	canvas.drawLine(0, mMaxCy, mProgressX+1, mMaxCy, mProgressPaint);
	}
    
    /**
     * 画文字
     */
    private void drawText(Canvas canvas,float cx,float cy,String position) {
    	
    	Integer intNumber = Integer.valueOf(position);
    	//每个圆中心的x
    	float circleCenterX=getLocationX(intNumber)-mMaxCircleRadius;
    	
    	Bitmap decodeResource;
    	if (mProgressX<circleCenterX) {
    		mFontPaint.setColor(mSelectTextColor);
    		 decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.check_unselect);
		}else{
			mFontPaint.setColor(mUnSelectTextColor);
			 decodeResource = BitmapFactory.decodeResource(getResources(), R.drawable.check_select);
		}
    	
		if (position.equals(mMaxCircleNumber + "")) {
			float size=mTextSize/2.3f;
			RectF matrix=new RectF(cx-size,cy-size, cx+size, cy+size);
			canvas.drawBitmap(decodeResource, null, matrix, null);
		}else{
		
		//计算出文字的宽度
		float textWidth = mFontPaint.measureText(position);

		float baseY = cy - ((mFontPaint.descent() + mFontPaint.ascent()) / 2);
		float baseX = cx - textWidth / 2;
		canvas.drawText(position, baseX, baseY, mFontPaint);
		}
	}

    
    /**
     * 通过位置得到当前X坐标
     * @param position
     * @return
     */
    private float getLocationX(int position) {
		return mMaxCx+getAdvanceSize()*(position-1);
	}
    
    /**
     * 画大圆
     */
    private void drawMaxCircle(Canvas canvas) {
    	//因为画圆是最开始画透明的，所以先在这里设置透明色
    	 mPaint.setColor(Color.TRANSPARENT);
    	 
    	float cx=mMaxCx;
    	for (int i = 0; i < mMaxCircleNumber; i++) {
    		//如果是第一个圆就没必要增加
    		if (i!=0) {
    			cx=cx+getAdvanceSize();
			}
    		//每次画的圆都应该是实心的
    		mPaint.setStyle(Style.FILL);
    		//画圆
    		mCanvas.drawCircle(cx, mMaxCy, mMaxCircleRadius, mPaint);
    		//画大圆的时候跟着画弧，mLineNumber要画几条线
    		if (i<mLineNumber) {
    			drawArc(i);
			}
    		
    		drawText(canvas, cx, mMaxCy,i+1+"");
		}
	}
    /**
     * 画中间的线
     */
    private void drawLine() {
        //如果个数大于1才开始绘制线和圆弧
        if (1<mMaxCircleNumber) {
        	mPaint.setStyle(Style.FILL);
        	//画线
        	mCanvas.drawRect(mMaxCx, mMaxCy - mLineHighHalf,mMaxCx+getAdvanceSize()*mLineNumber, mMaxCy + mLineHighHalf, mPaint);
		}
	}
    
    /**
     * 画最后的矩形
     */
    private void drawLastRect() {
    	float rectLeft=mMaxCx+getAdvanceSize()*mLineNumber;
    	float rectTop=mMaxCy-mMaxCircleRadius;
    	float rectRight=rectLeft+mMaxCircleDiameter;
    	float rectBottom=mMaxCy+mMaxCircleRadius;
    	mCanvas.drawRect(rectLeft, rectTop, rectRight, rectBottom, mPaint);

	}
    
    
   /**
    * 画弧形
    * @param canvas
    * @param position 表示移动多少个x坐标
    */
    private void drawArc(int position) {
    	mPaint.setStyle(Style.STROKE);
    	//左
		float  rectLeft=0f;
		//上
		float  rectTop=0f;
		//右
		float  rectRight=0f;
		//下
		float  rectBottom=0f;
		
		for (int i = 0; i < 4; i++) {
			if (i == 0) {//1
				rectLeft = getRectLeft(1);
				rectTop = getRectTop(1);
				rectRight = getRectRight(1);
				rectBottom = getRectBottom(1);
			} else if (i == 1) {//2
				rectLeft = getRectLeft(1);
				rectTop = getRectTop(2);
				rectRight = getRectRight(1);
				rectBottom = getRectBottom(2);
			} else if (i == 2) {//3
				rectLeft = getRectLeft(2);
				rectTop = getRectTop(1);
				rectRight = getRectRight(2);
				rectBottom = getRectBottom(1);
			} else if (i == 3) {//4
				rectLeft = getRectLeft(2);
				rectTop = getRectTop(2);
				rectRight = getRectRight(2);
				rectBottom = getRectBottom(2);
			}  
			rectLeft=rectLeft+position*getAdvanceSize();
			rectRight=rectRight+position*getAdvanceSize();
			
			//在画扇形之前先要画个滑矩形
			RectF oval = new RectF(rectLeft,rectTop ,rectRight, rectBottom);
			mPaint.setStyle(Style.STROKE);
			
			switch (i) {
			case 0://左上
				mCanvas.drawArc(oval, 90, getSweepAngle(), false, mPaint);
				break;
			case 1://左下
				 mCanvas.drawArc(oval, 270-getSweepAngle(), getSweepAngle(), false, mPaint);
				break;
			case 2://右上
				 mCanvas.drawArc(oval, 90-getSweepAngle(), getSweepAngle(), false, mPaint);
				break;
			case 3://右下
				 mCanvas.drawArc(oval, 270, getSweepAngle(), false, mPaint);
				break;
			}
      }
	}
    
    
    /**
     * 第一个大圆的最右边位置
     * @return
     */
    private float firstMaxCircleRX() {
		return mMaxCx+mMaxCircleRadius;
	}
    
   /**
    *  第一个到第下一个大圆的最左边的位置
    */
    private float netxMaxCircleLX() {
		return firstMaxCircleRX()+mMaxCircleDistance;
	}
    
    /**
     *  第一个大圆的最右边位置到下一个大圆的的中心位置
     */
     private float netxMaxCircleCentre() {
 		return netxMaxCircleLX()+mMaxCircleRadius;
 	}
     
     /**
      * 第一个大圆的Y中心位置
      */
     private float firstMaxCircleCentreY() {
 		return mMaxCy;
 	}
     
     /**
      * 获得每次增长长度的大小
      */
     private float getAdvanceSize() {
 		return mMaxCircleDiameter+mMaxCircleDistance;
 	}
     
    
   /**
    * 获得矩形左上角的点
    * @param type 
    */
    private float getRectLeft(int type) {
        //矩形中间+画笔一半
        float left= mMinCircleRadius+mPaintWidthHalf;
        
    	float rectLeft=0f;
    	if (type==1) {
    		//第一个大圆的中心+与小圆的距离
    		rectLeft=mMaxCx+getDistance()-left;
		}else{
			rectLeft=netxMaxCircleCentre()-getDistance()-left;
		}
		return rectLeft;
	}
    
    /**
     * 获得矩形上
     */
    private float getRectTop(int type) {
        float  rectTop=0;
    	if (type==1) {
    		rectTop=firstMaxCircleCentreY()-minCircleDiameter-mLineHighHalf-mPaintWidthHalf;
		}else{
			rectTop=firstMaxCircleCentreY()+mLineHighHalf-mPaintWidthHalf;
		}
		return rectTop;
    }
    /**
     * 获得矩形右
     */
    private float getRectRight(int type) {
        float  rectRight=0;
    	if (type==1) {
    		rectRight=mMaxCx+getDistance()+mMinCircleRadius+mPaintWidthHalf;
		}else{
			rectRight=netxMaxCircleCentre()-getDistance()+mMinCircleRadius+mPaintWidthHalf;
		}
		return rectRight;
    }
    
    /**
     * 获得矩形下
     */
    private float getRectBottom(int type) {
        float  rectBottom=0;
    	if (type==1) {
    		rectBottom=mMaxCy-mLineHighHalf+mPaintWidthHalf;
		}else{
			rectBottom=mMaxCy+mLineHighHalf+mMinCircleRadius*2+mPaintWidthHalf;
		}
		return rectBottom;
    }
    
    /**
     * 大圆与小圆之间的距离
     */
    private float getDistance() {
    	 //通过大圆半径加小圆半径得到三角形的最长边
        float pow1 = (float) Math.pow(mMaxCircleRadius+mMinCircleRadius, 2);
        //得到三角形的高
        float pow2 = (float) Math.pow(mMinCircleRadius+mLineHigh/2, 2);
        //得到大圆和小圆之间的距离
        float distance = (float) Math.sqrt(pow1-pow2);
		return distance;
	}
    
    /**
     * 获得扫描角，弧度
     * @return
     */
    private float getSweepAngle() {
		return (float)(getAcosAngle() /Math.PI * 180);
	}
    
    
    /**
     * 获得三角形之间的acos角度 
     * @return
     */
    private double getAcosAngle() {
    	  double acos = Math.acos((mMinCircleRadius+mLineHighHalf)/(mMaxCircleRadius+mMinCircleRadius));
		return acos;
	}
    
    
    /**
	 * 小到大
	 * @param lastSize 上一次的
	 * @param newCurrent 当前的值
	 */
	private void replyImageUp(final Float lastSize,final Float newCurrent) {
		// 设置动画
		ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(mDuration);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				mProgressX = lastSize + (newCurrent - lastSize)* cVal;
				postInvalidate();
			}
		});
		//弹拉效果
		anim.setInterpolator(new AnticipateOvershootInterpolator());
		anim.start();
	}
	
	/**
	 * 大到小
	 * @param lastSize 上一次的
	 * @param newCurrent 当前的值
	 */
	private void replyImageDown(final Float lastSize,final Float newCurrent) {
		// 设置动画
		ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(mDuration);
		anim.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				float cVal = (Float) animation.getAnimatedValue();
				mProgressX = lastSize - (lastSize-newCurrent)* cVal;
				postInvalidate();
			}
		});
		//弹拉效果
		anim.setInterpolator(new AnticipateOvershootInterpolator());
		anim.start();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData(Context context,AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LadderView);
		//被选中的字体颜色
		mSelectTextColor=a.getColor(R.styleable.LadderView_view_select_textColor, Color.parseColor("#cccccc"));
		//未被选中的字体颜色
		mUnSelectTextColor=a.getColor(R.styleable.LadderView_view_unselect_textColor, Color.WHITE);
		//动画的时间
		mDuration=a.getInt(R.styleable.LadderView_view_duration, 1500);
		//控件的背景色
		mBackground=a.getColor(R.styleable.LadderView_view_background, Color.WHITE);
//		//渐变色开始色
//		mStartColor=a.getColor(R.styleable.LadderView_view_start_color, Color.parseColor("#FA9027"));
//		//渐变色结束色
//		mEndColor=a.getColor(R.styleable.LadderView_view_end_color,Color.parseColor("#FA7B27"));
		//渐变色开始色
		mStartColor=a.getColor(R.styleable.LadderView_view_start_color, Color.parseColor("#fdcb34"));
		//渐变色结束色
		mEndColor=a.getColor(R.styleable.LadderView_view_end_color,Color.parseColor("#fdcb34"));
		a.recycle();
	}
	/**
	 *  TODO 初始化计算值
	 */
	private void initCalculate() {
		//大圆半径，15是因为画的长度有15个半径长
		mMaxCircleRadius=mViewWidth/15;
		//大圆直径
		mMaxCircleDiameter = mMaxCircleRadius * 2;
		//大圆和大圆之间的距离
		mMaxCircleDistance=mMaxCircleDiameter;
		//小圆半径
		mMinCircleRadius= mMaxCircleRadius/2;
		//小圆直径
		minCircleDiameter = mMinCircleRadius * 2;
		//线高---画笔的宽度和线高是一样的
		mLineHigh=mPaintWidth=mMinCircleRadius;
		//线的一半和画笔的一半是一样的
		mLineHighHalf =mPaintWidthHalf= mLineHigh / 2;

		mLineNumber = mMaxCircleNumber - 1;

		mMaxCx = mMaxCircleRadius;
		//为了上下居中
		mMaxCy = mViewHeight/2;
		
		mProgressX = mMaxCx;
		
		mTextSize=mMaxCircleRadius;
	}
    
    /** 
     * 初始化对象 
     */  
    private void initBitmapPaint() {  
        // 实例化画笔并开启其抗锯齿和抗抖动  
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);  
        // 设置画笔透明度为0是关键！我们要让绘制的路径是透明的，然后让该路径与前景的底色混合“抠”出绘制路径  
        mPaint.setColor(Color.TRANSPARENT);
        // 设置混合模式为DST_IN  
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));  
        // 设置画笔风格为描边  
        mPaint.setStyle(Style.STROKE);
        // 设置路径结合处样式  
        mPaint.setStrokeJoin(Paint.Join.ROUND);  
        // 设置笔触类型  
        mPaint.setStrokeCap(Paint.Cap.ROUND);  
        // 设置描边宽度  
        mPaint.setStrokeWidth(mPaintWidth);  
        // 生成前景图Bitmap  
        mFgBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Config.ARGB_8888);  
        // 将其注入画布  
        mCanvas = new Canvas(mFgBitmap);
    
    }  
    
    
    /**
     * 初始化文字画笔
     */
    private void initFontPaint() {
    	 mFontPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); 
         // 设置画笔颜色
         mFontPaint.setColor(mSelectTextColor);
         // 设置字体大小
         mFontPaint.setTextSize(mTextSize);
	}
    
    /**
     * 初始化进度画笔
     */
    private void initProgressPaint() {
    	mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG); 
		// 设置画笔颜色
		mProgressPaint.setColor(Color.YELLOW);
		// 设置画笔的宽度
		mProgressPaint.setStrokeWidth(mMaxCircleDiameter+2);
		// 设置路径结合处样式
		mProgressPaint.setStrokeJoin(Paint.Join.ROUND);
		// 设置笔触类型
		mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
	
		setProgressColor();
	}
    
    /**
     * 设置进度条渐变色
     */
    private void setProgressColor() {
    	//第一个圆的最左边
    	float x0=mMaxCx-mMaxCircleRadius;
    	float y0=0;
    	//最后一个圆的最右边
    	float x1=getLocationX(mMaxCircleNumber)+mMaxCircleDiameter;
    	float y1=0;
    	
    	LinearGradient lg=new LinearGradient(x0, y0, x1, y1, mStartColor, mEndColor, Shader.TileMode.MIRROR) ;
    	mProgressPaint.setShader(lg);
	}
    
    
    /**
     * 设置进度
     * @param progress
     */
    public void setProgress(int progress) {
    	//必须大于1
    	if(progress<1||mMaxCircleNumber<progress){
    		return;
    	}
    	
    	float locationX = getLocationX(progress);
    	float lastLocationX = getLocationX(mLastProgress);
    	//因为最后加了个矩形，所以要延长
    	if (mMaxCircleNumber==progress) {
    		locationX=locationX+mMaxCircleDiameter;
		}
    	//因为最后加了个矩形，所以要延长
    	if (mMaxCircleNumber==mLastProgress) {
    		lastLocationX=lastLocationX+mMaxCircleDiameter;
		}
    	
    	if (mLastProgress<progress) {
    		//小到大
    		replyImageUp(lastLocationX,locationX);
		}else{
			//大到小
			replyImageDown(lastLocationX, locationX);
		}
    		
    	mLastProgress=progress;
    	
	}
    
}

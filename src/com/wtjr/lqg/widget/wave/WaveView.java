/*
 *  Copyright (C) 2015, gelitenight(gelitenight@gmail.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.wtjr.lqg.widget.wave;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
    /**
     * +------------------------+
     * |<--wave length->        |______
     * |   /\          |   /\   |  |
     * |  /  \         |  /  \  | amplitude
     * | /    \        | /    \ |  |
     * |/      \       |/      \|__|____
     * |        \      /        |  |
     * |         \    /         |  |
     * |          \  /          |  |
     * |           \/           | water level
     * |                        |  |
     * |                        |  |
     * +------------------------+__|____
     */
    private static final float DEFAULT_AMPLITUDE_RATIO = 0.05f;
    private static final float DEFAULT_WATER_LEVEL_RATIO = 0.5f;
    private static final float DEFAULT_WAVE_LENGTH_RATIO = 1.0f;
    private static final float DEFAULT_WAVE_SHIFT_RATIO = 0.0f;

    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#28FFFFFF");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#3CFFFFFF");
    public static final ShapeType DEFAULT_WAVE_SHAPE = ShapeType.CIRCLE;

    public enum ShapeType {
        CIRCLE,
        SQUARE
    }

    // if true, the shader will display the wave
    private boolean mShowWave;

    // shader containing repeated waves
    private BitmapShader mWaveShader;
    // shader matrix
    private Matrix mShaderMatrix;
    // paint to draw wave
    private Paint mViewPaint;
    // paint to draw border
    private Paint mBorderPaint;

    private float mDefaultAmplitude;
    private float mDefaultWaterLevel;
    private float mDefaultWaveLength;
    private double mDefaultAngularFrequency;

    private float mAmplitudeRatio = DEFAULT_AMPLITUDE_RATIO;
    private float mWaveLengthRatio = DEFAULT_WAVE_LENGTH_RATIO;
    private float mWaterLevelRatio = DEFAULT_WATER_LEVEL_RATIO;
    private float mWaveShiftRatio = DEFAULT_WAVE_SHIFT_RATIO;

    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR;
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR;
    private ShapeType mShapeType = DEFAULT_WAVE_SHAPE;
    /**
     * 外圆画笔
     */
	private Paint mExternalCirclePaint;
	/**
	 * 设置圆弧度数
	 */
	private float mCircleArcDegree=1;
	/**
	 * 设置圆弧度数
	 */
	private float mMaxCircleArcDegree;
	/**
	 * 使用判断是前进还是后退，true是前进，false是后退,默认是上升
	 */
	private boolean mIsUpOrDown = true;
	/**
	 * 初始化白色小圆点的画笔
	 */
	private Paint mDotPaint;
	/**
	 * 白色圆点的cX
	 */
	private float dotCX;
	/**
	 * 白色圆点的cY
	 */
	private float dotCy;

    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
       
    }

    private void init() {
        mShaderMatrix = new Matrix();
        mViewPaint = new Paint();
        mViewPaint.setAntiAlias(true);
        initExternalCircle();
        initDot();
    }
    
    /**
     * 设置最大弧度数
     */
    public void setMaxCircleArcDegree(float maxCircleArcDegree) {
    	mMaxCircleArcDegree=maxCircleArcDegree;
	}
    
    /**
     * 使用判断是前进还是后退，true是前进，false是后退
     * @param mIsUpOrDown
     */
    public void isUpOrDown(boolean mIsUpOrDown) {
		this.mIsUpOrDown = mIsUpOrDown;
	}
    

    public float getWaveShiftRatio() {
        return mWaveShiftRatio;
    }

    /**
     * Shift the wave horizontally according to <code>waveShiftRatio</code>.
     *
     * @param waveShiftRatio Should be 0 ~ 1. Default to be 0.
     *                       <br/>Result of waveShiftRatio multiples width of WaveView is the length to shift.
     */
    public void setWaveShiftRatio(float waveShiftRatio) {
        if (mWaveShiftRatio != waveShiftRatio) {
            mWaveShiftRatio = waveShiftRatio;
            invalidate();
        }
    }

    public float getWaterLevelRatio() {
        return mWaterLevelRatio;
    }

    /**
     * Set water level according to <code>waterLevelRatio</code>.
     *
     * @param waterLevelRatio Should be 0 ~ 1. Default to be 0.5.
     *                        <br/>Ratio of water level to WaveView height.
     */
    public void setWaterLevelRatio(float waterLevelRatio) {
        if (mWaterLevelRatio != waterLevelRatio) {
            mWaterLevelRatio = waterLevelRatio;
            invalidate();
        }
    }

    public float getAmplitudeRatio() {
        return mAmplitudeRatio;
    }

    /**
     * Set vertical size of wave according to <code>amplitudeRatio</code>
     *
     * @param amplitudeRatio Default to be 0.05. Result of amplitudeRatio + waterLevelRatio should be less than 1.
     *                       <br/>Ratio of amplitude to height of WaveView.
     */
    public void setAmplitudeRatio(float amplitudeRatio) {
        if (mAmplitudeRatio != amplitudeRatio) {
            mAmplitudeRatio = amplitudeRatio;
            invalidate();
        }
    }

    public float getWaveLengthRatio() {
        return mWaveLengthRatio;
    }

    /**
     * Set horizontal size of wave according to <code>waveLengthRatio</code>
     *
     * @param waveLengthRatio Default to be 1.
     *                        <br/>Ratio of wave length to width of WaveView.
     */
    public void setWaveLengthRatio(float waveLengthRatio) {
        mWaveLengthRatio = waveLengthRatio;
    }

    public boolean isShowWave() {
        return mShowWave;
    }

    public void setShowWave(boolean showWave) {
        mShowWave = showWave;
    }

	/**
	 * 设置边境
	 * 
	 * @param width
	 *            宽度
	 * @param color
	 *            颜色
	 */
	public void setBorder(float width, int color) {
		if (mBorderPaint == null) {
			mBorderPaint = new Paint();
			mBorderPaint.setAntiAlias(true);
			mBorderPaint.setStyle(Style.STROKE);
		}
		mBorderPaint.setColor(color);
		mBorderPaint.setStrokeWidth(width);
		setExternalCircleWidth(width);
		invalidate();
	}
    
/**
 * 
 * @param behindWaveColor 后面
 * @param frontWaveColor 前面
 */
    public void setWaveColor(int behindWaveColor, int frontWaveColor) {
        mBehindWaveColor = behindWaveColor;
        mFrontWaveColor = frontWaveColor;

        // need to recreate shader when color changed
        mWaveShader = null;
        createShader();
        invalidate();
    }

	/**
	 * 设置画的类型，是矩形还是圆
	 * 
	 * @param shapeType
	 */
	public void setShapeType(ShapeType shapeType) {
		mShapeType = shapeType;
		invalidate();
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        createShader();
    }

    /**
     * Create the shader with default waves which repeat horizontally, and clamp vertically
     */
    private void createShader() {
        mDefaultAngularFrequency = 2.0f * Math.PI / DEFAULT_WAVE_LENGTH_RATIO / getWidth();
        mDefaultAmplitude = getHeight() * DEFAULT_AMPLITUDE_RATIO;
        mDefaultWaterLevel = getHeight() * DEFAULT_WATER_LEVEL_RATIO;
        mDefaultWaveLength = getWidth();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        Paint wavePaint = new Paint();
        wavePaint.setStrokeWidth(2);
        wavePaint.setAntiAlias(true);

        // Draw default waves into the bitmap
        // y=Asin(蠅x+蠁)+h
        final int endX = getWidth() + 1;
        final int endY = getHeight() + 1;

        float[] waveY = new float[endX];

        wavePaint.setColor(mBehindWaveColor);
        for (int beginX = 0; beginX < endX; beginX++) {
            double wx = beginX * mDefaultAngularFrequency;
            float beginY = (float) (mDefaultWaterLevel + mDefaultAmplitude * Math.sin(wx));
            canvas.drawLine(beginX, beginY, beginX, endY, wavePaint);

            waveY[beginX] = beginY;
        }

        wavePaint.setColor(mFrontWaveColor);
        final int wave2Shift = (int) (mDefaultWaveLength / 4);
        for (int beginX = 0; beginX < endX; beginX++) {
            canvas.drawLine(beginX, waveY[(beginX + wave2Shift) % endX], beginX, endY, wavePaint);
        }

        // use the bitamp to create the shader
        mWaveShader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);
        mViewPaint.setShader(mWaveShader);
    }
//TODO 画
    @Override
    protected void onDraw(Canvas canvas) {
    	
    	canvas.save();
        // modify paint shader according to mShowWave state
        if (mShowWave && mWaveShader != null) {
            // first call after mShowWave, assign it to our paint
            if (mViewPaint.getShader() == null) {
                mViewPaint.setShader(mWaveShader);
            }

            // sacle shader according to mWaveLengthRatio and mAmplitudeRatio
            // this decides the size(mWaveLengthRatio for width, mAmplitudeRatio for height) of waves
            mShaderMatrix.setScale(
                mWaveLengthRatio / DEFAULT_WAVE_LENGTH_RATIO,
                mAmplitudeRatio / DEFAULT_AMPLITUDE_RATIO,
                0,
                mDefaultWaterLevel);
            // translate shader according to mWaveShiftRatio and mWaterLevelRatio
            // this decides the start position(mWaveShiftRatio for x, mWaterLevelRatio for y) of waves
            mShaderMatrix.postTranslate( mWaveShiftRatio * getWidth(),
                (DEFAULT_WATER_LEVEL_RATIO - mWaterLevelRatio) * getHeight());
            // assign matrix to invalidate the shader
            mWaveShader.setLocalMatrix(mShaderMatrix);
            //内圆透明圆宽度
            float borderWidth = mBorderPaint == null ? 0f : mBorderPaint.getStrokeWidth();
            //点半径
            float dotRadius=borderWidth/2;
           
            switch (mShapeType) {
                case CIRCLE://画圆
                    if (borderWidth > 0) {
                        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f,(getWidth() - borderWidth) / 2f - 1f, mBorderPaint);
                    }
                    //内圆半径
                    float radius = getWidth() / 2f - borderWidth;
                    
                    float cX=getWidth() / 2f;
                    float cY=getHeight() / 2f;
                    
                    //内部透明圆
                    canvas.drawCircle(cX, cY, radius, mViewPaint);
                    
                    //外圆的半径和画弧的半径
                    float externalCircRadius=radius+borderWidth/2;
                    
                    //外部灰色的圆
                    mExternalCirclePaint.setColor(Color.parseColor("#ff9550"));
                    mExternalCirclePaint.setStrokeWidth(3);
//                    mExternalCirclePaint.setColor(Color.parseColor("#fca66a"));
                    canvas.drawCircle(cX, cY, externalCircRadius, mExternalCirclePaint);

                    mExternalCirclePaint.setStrokeWidth(6);
                    //画弧形
                    RectF oval = new RectF(cX-externalCircRadius, cY-externalCircRadius, cX+externalCircRadius, cY+externalCircRadius);
                    drawArc(canvas, externalCircRadius, oval);

                    if(mCircleArcDegree < 358) {
                        //画点
                        drawDot(canvas, externalCircRadius, dotRadius);
                    }

                    break;
                case SQUARE://画矩形
                    if (borderWidth > 0) {
                        canvas.drawRect(
                            borderWidth / 2f,
                            borderWidth / 2f,
                            getWidth() - borderWidth / 2f - 0.5f,
                            getHeight() - borderWidth / 2f - 0.5f,
                            mBorderPaint);
                    }
                    canvas.drawRect(borderWidth, borderWidth, getWidth() - borderWidth,
                        getHeight() - borderWidth, mViewPaint);
                    break;
            }
        } else {
            mViewPaint.setShader(null);
        }
    }
    
    
    /**
     * 画跑动的弧形
     * @param canvas
     * @param externalCircRadius 外圆半径，也就是自身的半径
     * @param oval 画弧需要的矩形
     */
    private void drawArc(Canvas canvas,float externalCircRadius,RectF oval) {
    	mExternalCirclePaint.setColor(Color.WHITE);
     	//跑的弧形
    	if(mCircleArcDegree > mMaxCircleArcDegree){
    		mCircleArcDegree = mMaxCircleArcDegree;
    	}
    	if(mCircleArcDegree < 1){
    		mCircleArcDegree = 1;
    	}
    	
        if(mIsUpOrDown){//上升
            if(mCircleArcDegree > 358) {
                mCircleArcDegree = 358;
            }
        	canvas.drawArc(oval, -90,-(mCircleArcDegree+=2), false, mExternalCirclePaint);
        }else{
        	canvas.drawArc(oval, -90,-(mCircleArcDegree-=5), false, mExternalCirclePaint);
        	
            if (mCircleArcDegree <= 2) {// 当下降到头时，mIsUpOrDown = true重新上升
                mIsUpOrDown = true;
            }
        }
	}
    
    /**
     * 画白色点
     * @param canvas 画布
     * @param externalCircRadius 外圆半径，也就是圆点围着转的那根线
     * @param dotRadius 小圆的半径
     */
	private void drawDot(Canvas canvas,float externalCircRadius,float dotRadius) {
		//根据度数得到圆弧的点
		   float hudu = (float) (mCircleArcDegree/180f*Math.PI);
           
           float sin = (float) Math.sin(hudu);
           float cos = (float) Math.cos(hudu);
           
           float cX = externalCircRadius - sin*externalCircRadius;
           float cY = externalCircRadius - cos*externalCircRadius;
           //画圆
           canvas.drawCircle(cX+dotRadius, cY+dotRadius, dotRadius, mDotPaint);
	}
    
	
    /**
     * 初始化外圆
     */
    private void initExternalCircle() {
    	 mExternalCirclePaint = new Paint();
    	 mExternalCirclePaint.setAntiAlias(true);
    	 mExternalCirclePaint.setStyle(Style.STROKE);
    	// 设置路径结合处样式
    	 mExternalCirclePaint.setStrokeJoin(Paint.Join.ROUND);
 		// 设置笔触类型
    	 mExternalCirclePaint.setStrokeCap(Paint.Cap.ROUND);
	}
    
    /**
     * TODO 设置外圆和弧形的宽度
     */
    private void setExternalCircleWidth(float width) {
    	mExternalCirclePaint.setStrokeWidth(width/3f);
	}
    
	/**
	 * 初始化白色小圆点
	 */
	private void initDot() {
		mDotPaint = new Paint();
		mDotPaint.setAntiAlias(true);
		mDotPaint.setStyle(Style.FILL);
		mDotPaint.setColor(Color.WHITE);
	}
    
}

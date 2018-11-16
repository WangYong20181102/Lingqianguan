package com.wtjr.lqg.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by geminiwen on 15/9/25.
 */
public class CubeLeftOutAnimation extends Animation{
    private Camera mCamera;
    private Matrix mMatrix;
    private int mWidth;
    private int mHeight;
    private static final int sFinalDegree = 90;
    /**
     * true是前进，false是后退
     */
    private boolean mDirection;

    public CubeLeftOutAnimation(boolean direction) {
        this.mDirection = direction;
    }
    
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
        mMatrix = new Matrix();
        mWidth = width;
        mHeight = height;
    }
    
    public void setDirection(boolean direction) {
        this.mDirection = direction;
    }
    
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        
        if(mDirection) {
            float rotate = (sFinalDegree - sFinalDegree * interpolatedTime);
            mCamera.save();
            mCamera.translate((-mWidth * interpolatedTime), 0, 0);
            mCamera.rotateY(rotate);
            mCamera.getMatrix(mMatrix);
            mCamera.restore();

            mMatrix.postTranslate(mWidth, mHeight / 2);
            mMatrix.preTranslate(0, -mHeight / 2);

            t.getMatrix().postConcat(mMatrix);
        }else {
            float rotate = sFinalDegree * interpolatedTime;
            mCamera.save();
            mCamera.translate((-mWidth + mWidth * interpolatedTime), 0, 0);
            mCamera.rotateY(rotate);
            mCamera.getMatrix(mMatrix);
            mCamera.restore();

            mMatrix.postTranslate(mWidth, mHeight / 2);
            mMatrix.preTranslate(0, -mHeight / 2);

            t.getMatrix().postConcat(mMatrix);
        }
    }
}

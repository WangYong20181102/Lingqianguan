package com.wtjr.lqg.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.utils.AnimationUtil;

/**
 * Created  by WangXu on 2017/2/10.
 */

public class CircleProgress extends View {

    public static final int MAX_ARC = 330;
    private final Context context;
    private Paint mCriclePain;
    private RectF oval;
    private ValueAnimator mValueAnimator;
    private ValueAnimator mDoneValueAnimator;
    //勾的路径
    private Path path;
    private float mStartAngle;
    private float mSweepAngle;
    //顺时针展开
    private boolean mSpread = true;
    //验证验证码完成
    private boolean done = false;

    private float mDoneStartX;
    private float mDoneStartY;
    private float mDoneMiddleX;
    private float mDoneMiddleY;
    private float mDoneEndX;
    private float mDoneEndY;


    private float mDoneTempX;
    private int mDegree;
    //画板中心
    private float mCenterX;
    private float mCenterY;
    private AddAnimatorEndListener addAnimatorEndListener;

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /*//勾第一点坐标
        mDoneStartX = (float) getWidth() / 4;
        mDoneStartY = (float) getHeight() / 2;
        //勾第二点坐标
        mDoneMiddleX = mDoneStartX + 30f * getWidth() / 160f;
        mDoneMiddleY = mDoneStartY + 20f * getHeight() / 160f;
        //勾第三点坐标
        mDoneEndX = mDoneStartX + 80 * getWidth() / 160f;
        mDoneEndY = mDoneStartY - 30f * getHeight() / 160f;

        //画板中心
        mCenterX = (float) getWidth() / 2;
        mCenterY = (float) getHeight() / 2;*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCriclePain = new Paint();
        mCriclePain.setAntiAlias(true);
        mCriclePain.setStyle(Paint.Style.STROKE);
        mCriclePain.setStrokeCap(Paint.Cap.ROUND);
        mCriclePain.setColor(getResources().getColor(R.color.lqb_head_bg));
        mCriclePain.setStrokeWidth((float) 8);
        //参数一为渐变起初点坐标x位置，参数二为y轴位置，参数三和四分辨对应渐变终点，最后参数为平铺方式，这里设置为镜像
        LinearGradient lg = new LinearGradient(0, 0, 100, 100, Color.parseColor("#ffa000"), Color.parseColor("#ff5a00"), Shader.TileMode.MIRROR);
        mCriclePain.setShader(lg);
        canvas.drawColor(Color.WHITE);
        oval = new RectF(10, 10f, getWidth() - 10f, getHeight() - 10f);

        if (done) {
            path = new Path();
            path.moveTo(mDoneStartX, mDoneStartY);//起点

            if (mDoneTempX < mDoneMiddleX) {
                float mDoneTempY = (mDoneTempX - mDoneStartX) / (mDoneMiddleX - mDoneStartX) * (mDoneMiddleY - mDoneStartY) + mDoneStartY;
                path.lineTo(mDoneTempX, mDoneTempY);
            } else {
                path.lineTo(mDoneMiddleX, mDoneMiddleY);

                float mDoneTempY = (mDoneTempX - mDoneMiddleX) / (mDoneEndX - mDoneMiddleX) * (mDoneEndY - mDoneMiddleY) + mDoneMiddleY;
                path.lineTo(mDoneTempX, mDoneTempY);
            }
            canvas.drawPath(path, mCriclePain);
        } else {
            //画板中心
            mCenterX = (float) getWidth() / 2;
            mCenterY = (float) getHeight() / 2;
            // canvas转起来
            canvas.rotate(mDegree, mCenterX, mCenterY);
            mDegree += 5;
            mDegree = mDegree % 360;
            canvas.save();
        }
        canvas.drawArc(oval, mStartAngle, mSweepAngle, false, mCriclePain);
    }

    /**
     * 开始加载验证中的动画
     */
    public void startProgress(final TextView tvVerifySuccess) {
        if (mValueAnimator != null) {
            mValueAnimator.removeAllListeners();
            mValueAnimator.cancel();
        }
        if (mSpread) {// 伸展
            mValueAnimator = ValueAnimator.ofFloat(0, MAX_ARC);
        } else {// 收缩
            mValueAnimator = ValueAnimator.ofFloat(-60, 270);
        }
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.setDuration(1000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mSpread) {
                    mStartAngle = -60;
                    mSweepAngle = (float) animation.getAnimatedValue();
                } else {
                    mStartAngle = (float) animation.getAnimatedValue();
                    mSweepAngle = 270 - mStartAngle;
                }

                invalidate();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSpread = !mSpread;
                if (mSpread) {
                    startProgress(tvVerifySuccess);
                    if (done) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startDoneAnim(tvVerifySuccess);
                            }
                        }, 1000);
                    }
                } else if (!done) {
                    startProgress(tvVerifySuccess);
                }
            }
        });
        mValueAnimator.start();
    }

    /**
     * 完成验证画动画
     */
    public void checkDone(TextView tvVerifySuccess) {
        done = true;
        mDoneTempX = mDoneStartX;

        if (mSpread) {
            /**
             * 验证完成画一次结束动画
             */
            startDoneAnim(tvVerifySuccess);
        }
    }

    /**
     * 验证完成画一次结束动画
     */
    private void startDoneAnim(final TextView tvVerifySuccess) {
        if (mDoneValueAnimator != null) {
            mDoneValueAnimator.removeAllListeners();
            mDoneValueAnimator.cancel();
        }

        //勾第一点坐标
        mDoneStartX = (float) getWidth() / 4;
        mDoneStartY = (float) getHeight() / 2;
        //勾第二点坐标
        mDoneMiddleX = mDoneStartX + 30f * getWidth() / 160f;
        mDoneMiddleY = mDoneStartY + 20f * getHeight() / 160f;
        //勾第三点坐标
        mDoneEndX = mDoneStartX + 80 * getWidth() / 160f;
        mDoneEndY = mDoneStartY - 30f * getHeight() / 160f;


        mDoneValueAnimator = ValueAnimator.ofFloat(mDoneStartX, mDoneEndX);
        mDoneValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mDoneValueAnimator.setDuration(1000);
        mDoneValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDoneTempX = (float) animation.getAnimatedValue();

                invalidate();
            }
        });

        mDoneValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //验证成功的文字显示和动画
                tvVerifySuccess.setVisibility(View.VISIBLE);
                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.verify_success);
                tvVerifySuccess.startAnimation(animation1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                addAnimatorEndListener.onAnimatorEnd(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        mDoneValueAnimator.start();
    }

    public void setAnimatorStatusListener(AddAnimatorEndListener addAnimatorEndListener) {
        this.addAnimatorEndListener = addAnimatorEndListener;

    }

    /**
     * 停掉验证的所有动画状态设置回初始状态
     */
    public void stop() {
        if (mValueAnimator != null) {
            mValueAnimator.removeAllListeners();
            mValueAnimator.cancel();
        }
        if (mDoneValueAnimator != null) {
            mDoneValueAnimator.removeAllListeners();
            mDoneValueAnimator.cancel();
        }
        mSpread = true;
        done = false;
        mDegree = 0;
    }

    public interface AddAnimatorEndListener {
        void onAnimatorEnd(boolean animatorEnd);
    }
}
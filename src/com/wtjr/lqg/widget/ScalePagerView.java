package com.wtjr.lqg.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wtjr.lqg.base.ActivityData;

import java.util.List;

/**
 * Created by WangXu on 2017/11/06.
 * 首页显示三个item的无限轮播
 */

public class ScalePagerView extends ViewPager {

    public static final String TAG = "ScalePagerView";
    private static final float MIN_SCALE = 0.75f;
    private static final float MIN_X_SCALE = 0.95f;
    private static final float MIN_Y_SCALE = 0.70f;
    private static final float MIN_ALPHA = 0.5f;

    int lastX = -1;
    int lastY = -1;

    private Handler mHandler;
    /**
     * 首页viewPager的父布局ScrollView
     */
    private ObservableScrollView mScrollView;
    private List<ActivityData> activityDatas;

    public ScalePagerView(Context context) {
        this(context, null);
    }

    public ScalePagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setActivityDatas(List<ActivityData> activityDatas) {
        this.activityDatas = activityDatas;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public void bindScrollView(ObservableScrollView osvHome) {
        this.mScrollView = osvHome;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        int dealtX = 0;
        int dealtY = 0;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                if (mScrollView != null) {
                    // 保证子View能够接收到Action_move事件
                    mScrollView.setPullRefreshEnable(false);
                }

                if (mHandler != null) {
                    mHandler.removeMessages(1);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                dealtX = Math.abs(x - lastX);
                dealtY = Math.abs(y - lastY);
                if (mScrollView != null) {
                    // 根据滑动xy轴的距离让父ScrollView是否能滑动
                    if (dealtY > 10 && dealtY >= dealtX * 2) {
                        mScrollView.setPullRefreshEnable(true);
                    } else {
                        mScrollView.setPullRefreshEnable(false);
                    }
                }
                if (mHandler != null) {
                    mHandler.removeMessages(1);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mScrollView != null) {
                    mScrollView.setPullRefreshEnable(true);
                }

                if (activityDatas != null) {
                    if (mHandler != null && activityDatas.size() > 1) {
                        mHandler.removeMessages(1);
                        mHandler.sendEmptyMessageDelayed(1, 3000);
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void init() {
        /**
         * 自定义PageTransformer实现个性的切换动画
         */
        this.setPageTransformer(true, new PageTransformer() {
            @Override
            public void transformPage(View page, float position) {

                if (position < -1 || position > 1) {
                    page.setAlpha(MIN_ALPHA);
                    page.setScaleX(MIN_X_SCALE);
                    page.setScaleY(MIN_Y_SCALE);
                } else if (position <= 1) { // [-1,1]
                    float scaleFactor = Math.max(MIN_X_SCALE, 1 - Math.abs(position));
                    if (position < 0) {
                        float scaleX = 1 + (1 - MIN_X_SCALE) * position;
                        float scaleY = 1 + (1 - MIN_Y_SCALE) * position;
                        page.setScaleX(scaleX);
                        page.setScaleY(scaleY);
                    } else {
                        float scaleX = 1 - (1 - MIN_X_SCALE) * position;
                        float scaleY = 1 - (1 - MIN_Y_SCALE) * position;
                        page.setScaleX(scaleX);
                        page.setScaleY(scaleY);
                    }
                    page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_X_SCALE) / (1 - MIN_X_SCALE) * (1 - MIN_ALPHA));
                }
            }
        });
    }

}
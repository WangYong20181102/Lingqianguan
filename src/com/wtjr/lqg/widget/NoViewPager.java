package com.wtjr.lqg.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.wtjr.lqg.base.ActivityData;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;

public class NoViewPager extends ViewPager {

    private float mDOWNx;
    private PullableListView pullableListView;
    private Handler mHandler;
    /**
     * 投资活动轮播图中的数据集合
     */
    private List<ActivityData> activityDatas = new ArrayList<ActivityData>();

    public NoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoViewPager(Context context) {
        super(context);
    }

    public void setActivityDatas(List<ActivityData> activityDatas) {
        this.activityDatas = activityDatas;
    }

    public void setHandler(Handler handler) {
        this.mHandler = handler;
    }

    public void setListView(PullableListView pullableListView) {
        this.pullableListView = pullableListView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //禁止下拉刷新上拉加载，保证轮播图能滑动
                mDOWNx = ev.getX();

                if (pullableListView != null) {
                    pullableListView.setPullRefreshEnable(false);
                    pullableListView.setPullLoadEnable(false);
                }

                if (mHandler != null) {
                    mHandler.removeMessages(0);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                //轮播图中的活动只有一个的时候不允许滑动
                if (activityDatas.size() <= 1) {
                    return true;
                }

                //滑动时候横滑值大于20表示左右滑动
                if (Math.abs(ev.getX() - mDOWNx) > 20) {
                    if (pullableListView != null) {
                        pullableListView.setPullRefreshEnable(false);
                        pullableListView.setPullLoadEnable(false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                pullableListView.setPullRefreshEnable(true);
                //当item条目数大于1的时候表示是有数据的可以上拉加载的，因为默认的下拉零妹妹算一条数据
                if (pullableListView.getAdapter().getCount() > 1) {
                    if (pullableListView != null) {
                        pullableListView.setPullLoadEnable(true);
                    }
                }

                if (mHandler != null && activityDatas.size() > 1) {
                    mHandler.sendEmptyMessageDelayed(0, 3000);
                }

                break;

            default:
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

}

package com.wtjr.lqg.widget;

import com.wtjr.lqg.widget.pullRefresh.Pullable;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * ScrollView滚动监听
 *
 * @author Myf
 */
public class ObservableScrollView extends ScrollView implements Pullable {
    private ScrollViewListener scrollViewListener = null;

    /**
     * false禁止下拉刷新
     */
    private boolean mEnablePullRefresh = true;
    /**
     * false禁止上拉加载
     */
    private boolean mEnablePullLoad = false;

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 获取ScrollView滚动路径
     *
     * @param scrollViewListener
     */
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }

    /**
     * 滚动监听接口
     *
     * @author Myf
     */
    public interface ScrollViewListener {

        void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    }

    /**
     * 设置可以下拉刷新
     */
    public void setPullRefreshEnable(boolean isPullRefresh) {
        mEnablePullRefresh = isPullRefresh;

    }

    public void setPullLoadEnable(boolean isPullLoad) {
        mEnablePullLoad = isPullLoad;

    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return mEnablePullRefresh;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
            return mEnablePullLoad;
        else
            return false;
    }

}

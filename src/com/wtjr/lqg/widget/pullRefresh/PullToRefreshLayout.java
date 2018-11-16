package com.wtjr.lqg.widget.pullRefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.utils.AnimationUtil;
import com.wtjr.lqg.utils.L;

import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifImageView;

/**
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 *
 * @author 陈靖
 */
public class PullToRefreshLayout extends RelativeLayout {
    /**
     * last y
     */
    private int mLastMotionY;
    /**
     * last x
     */
    private int mLastMotionX;
    // 初始状态
    public static final int INIT = 0;
    // 释放刷新
    public static final int RELEASE_TO_REFRESH = 1;
    // 正在刷新
    public static final int REFRESHING = 2;
    // 释放加载
    public static final int RELEASE_TO_LOAD = 3;
    // 正在加载
    public static final int LOADING = 4;
    // 操作完毕
    public static final int DONE = 5;
    // 当前状态
    private int state = INIT;
    // 刷新回调接口
    private OnRefreshListener mListener;
    // 刷新成功
    public static final int SUCCEED = 0;
    // 数据加载完毕
    public static final int LOADED = 2;

    // 刷新成功停留时间
    public static final int SUCCEED_TIME = 800;

    // 返回效果的时间
    public static final int BACK_TIME = 10;

    // 刷新失败
    public static final int FAIL = 1;
    // 按下Y坐标，上一个事件点Y坐标
    private float downY, lastY;

    // 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
    public float pullDownY = 0;
    // 上拉的距离
    private float pullUpY = 0;

    // 释放刷新的距离
    private float refreshDist = 200;
    // 释放加载的距离
    private float loadmoreDist = 200;

    private MyTimer timer;
    // 回滚速度
    public float MOVE_SPEED = 8;
    // 第一次执行布局
    private boolean isLayout = false;
    // 在刷新过程中滑动操作
    private boolean isTouch = false;
    // 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
    private float radio = 2;

    // // 下拉箭头的转180°动画
    // private RotateAnimation rotateAnimation;
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    // 下拉头
    private View refreshView;
    // 正在刷新的图标
    private ImageView refreshingView;
    // 刷新结果：成功或失败
    private TextView refreshStateTextView;

    // 上拉头
    private View loadmoreView;
    // 正在加载的图标
    private ImageView loadingView;
    // 加载结果：成功或失败
    private TextView loadStateTextView;

    // 实现了Pullable接口的View
    private View pullableView;
    // 过滤多点触碰
    private int mEvents;
    // 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
    private boolean canPullDown = true;
    private boolean canPullUp = true;

    // 上下文
    private Context context;

    // 3D旋转动画
    private Animation m3DRotateDownAnim;
//	/**
//	 * 底部选择栏中的稳通宝能否点击
//	 */
//	private boolean yesOrNoClick = true;

    /**
     * 标记点击次数，点击在多，只有状态等于1的时候进入
     */
    public int currentState = 0;

    // private Button abutton;

    /**
     * 界面是否在处于用户操作状态(比如正在滑动中、或按下中)
     */
    public static boolean TRIGGER = true;
    /**
     * 下拉刷新头部当前显示的距离
     */
    private int mHeight;
    /**
     * 屏幕的高度
     */
    private int windowHeight;

    /**
     * 下拉的最大距离
     */
    private float pullDownMax;
    /**
     * 上拉的最大距离
     */
    private float pullUpMax;
    /**
     * 下拉刷新头部文字颜色
     */
    private int mRefreshHeadTextColor;

    /**
     * 执行自动回滚的handler
     */
    Handler updateHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            // 回弹速度随下拉距离moveDeltaY增大而增大
            MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
                    / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
            if (!isTouch) {
                // 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
                if (state == REFRESHING && pullDownY <= refreshDist) {
                    pullDownY = refreshDist;
                    timer.cancel();
                } else if (state == LOADING && -pullUpY <= loadmoreDist) {
                    pullUpY = -loadmoreDist;
                    timer.cancel();
                }
            }

            if (pullDownY > 0)
                pullDownY -= MOVE_SPEED;
            else if (pullUpY < 0)
                pullUpY += MOVE_SPEED;

            if (pullDownY < 0) {
                currentState = 0;
//				yesOrNoClick = true;
                // 已完成回弹
                pullDownY = 0;
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING) {
                    changeState(INIT);
                }
                timer.cancel();
            }
            if (pullUpY > 0) {
                // 已完成回弹
                pullUpY = 0;
                // 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
                if (state != REFRESHING && state != LOADING) {
                    changeState(INIT);
                }
                timer.cancel();
            }
            // 刷新布局,会自动调用onLayout
            requestLayout();
        }

    };


    /**
     * 设置下拉和上拉刷新监听器
     *
     * @param listener
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public PullToRefreshLayout(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshLayout);
        mRefreshHeadTextColor = array.getColor(R.styleable.PullToRefreshLayout_refresh_head_text_color, Color.parseColor("#FFFFFF"));
        array.recycle();//循环再利用

        initView(context);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        initView(context);
    }

    private void initView(Context context) {
        if (isInEditMode()) {
            return;
        }

        timer = new MyTimer(updateHandler);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
        // 添加匀速转动动画
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);

        LqgApplication app = (LqgApplication) context.getApplicationContext();
        windowHeight = app.mScreenHeight;
    }

    private void hide() {
        timer.schedule(BACK_TIME);
    }

    /**
     * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
     */
    /**
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void refreshFinish(int refreshResult) {
        currentState++;

        if (refreshingView == null) return;

        if (currentState == 1) {
            refreshingView.clearAnimation();
            switch (refreshResult) {
                case SUCCEED:
                    // 刷新成功
                    refreshStateTextView.setText("刷新成功");
                    refreshingView.setImageResource(R.drawable.refresh_img_success);
                    //
                    break;
                case FAIL:
                default:
                    // 刷新失败
                    refreshStateTextView.setText("刷新失败");
                    refreshingView.setImageResource(R.drawable.refresh_img_coin);
                    break;
            }
            // 刷新结果停留1秒
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    changeState(DONE);
                    hide();
                }
            }.sendEmptyMessageDelayed(0, SUCCEED_TIME);

        }

    }

    /**
     * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
     *
     * @param refreshResult PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
     */
    public void loadmoreFinish(int refreshResult) {
        loadingView.clearAnimation();
        switch (refreshResult) {
            case SUCCEED:
                // 加载成功
                loadStateTextView.setText("加载成功");
                loadingView.setImageResource(R.drawable.refresh_img_success);
                break;
            case LOADED:
                // 已全部加载完成
                loadStateTextView.setText("已经全部加载完毕");
                loadingView.setImageResource(R.drawable.refresh_img_success);
                break;
            case FAIL:
            default:
                // 加载失败
                loadStateTextView.setText("加载失败");
                loadingView.setImageResource(R.drawable.refresh_img_coin);
                break;
        }
        // 刷新结果停留1秒
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                changeState(DONE);
                hide();
            }
        }.sendEmptyMessageDelayed(0, SUCCEED_TIME);
    }

    /**
     * 状态切换 TODO
     *
     * @param to
     */
    private void changeState(int to) {
        if (refreshingView == null) {
            return;
        }

        state = to;
        switch (state) {
            case INIT:
                // 下拉布局初始状态
                refreshingView.setImageResource(R.drawable.refresh_img_coin);
                refreshStateTextView.setText("下拉刷新");
                refreshingView.clearAnimation();
                // 上拉布局初始状态
                loadStateTextView.setText("加载更多");
                loadingView.setImageResource(R.drawable.refresh_img_coin);
                break;
            case RELEASE_TO_REFRESH:
                // 释放刷新状态
                refreshStateTextView.setText("松开刷新");
                refreshingView.startAnimation(m3DRotateDownAnim);

                break;
            case REFRESHING:
                // 正在刷新状态
                refreshingView.setBackgroundResource(R.drawable.refresh_img_coin);
                refreshingView.clearAnimation();
                refreshingView.startAnimation(m3DRotateDownAnim);
                refreshStateTextView.setText("正在刷新");
                break;
            case RELEASE_TO_LOAD:
                // 释放加载状态
                loadStateTextView.setText("松开加载");
                break;
            case LOADING:
                // 正在加载状态
                loadingView.startAnimation(refreshingAnimation);
                loadStateTextView.setText("正在加载");
                break;
            case DONE:
                refreshingView.setImageResource(R.drawable.refresh_img_coin);
                // 刷新或加载完毕，啥都不做
                break;
        }
    }

    /**
     * 不限制上拉或下拉
     */
    private void releasePull() {
        canPullDown = true;
        canPullUp = true;
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent e) {
//
////		if (!yesOrNoClick) {
////			return false;
////		}
//
//        int y = (int) e.getRawY();
//        int x = (int) e.getRawX();
//        switch (e.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                // 首先拦截down事件,记录y坐标
//                mLastMotionY = y;
//                mLastMotionX = x;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                // deltaY > 0 是向下运动< 0是向上运动
//                int deltaY = y - mLastMotionY;
//                int deltaX = x - mLastMotionX;
//                if (Math.abs(deltaX * 3) > Math.abs(deltaY)) {
//                    return false;
//                }
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                break;
//        }
//        return false;
//    }

    /*
     * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突 TODO
     *
     * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

//		if(state == INIT ||state == DONE){
//			yesOrNoClick = true;
//		}
//		
//		if (!yesOrNoClick) {
//			return true;
//		}

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                TRIGGER = false;

                if (pullableView instanceof PullableScrollView) {
//				ToastUtil.shortToast("是同一类型");
                } else {
//				ToastUtil.shortToast("不是同一类型");
                    downY = ev.getY();
                    lastY = downY;
                    timer.cancel();
                    mEvents = 0;
                    releasePull();
                    break;
                }

                PullableScrollView scrollView = (PullableScrollView) pullableView;

                int scaleY = scrollView.getScrollY();

                downY = ev.getY();
                lastY = downY;
                timer.cancel();
                mEvents = 0;
                releasePull();

                if (scaleY >= mheadHight) {
                    break;
                }

                float y2 = ev.getY();
                if (y2 < mheadHight) {
                    return false;
                } else {
                    break;
                }

            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                // 过滤多点触碰
                mEvents = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                TRIGGER = false;

                if (mEvents == 0) {
                    Pullable pullable = (Pullable) pullableView;
                    if (pullable == null) {
                        return true;
                    }
                    if (pullable.canPullDown() && canPullDown && state != LOADING) {
                        // 可以下拉，正在加载时不能下拉
                        // 对实际滑动距离做缩小，造成用力拉的感觉
                        pullDownY = pullDownY + (ev.getY() - lastY) / radio;
                        if (pullDownY < 0) {
                            pullDownY = 0;
                            canPullDown = false;
                            canPullUp = true;
                        }

                        if (pullDownY > getMeasuredHeight())
                            pullDownY = getMeasuredHeight();
                        if (state == REFRESHING) {
                            // 正在刷新的时候触摸移动
                            isTouch = true;
                        }
                    } else if (((Pullable) pullableView).canPullUp() && canPullUp
                            && state != REFRESHING) {
                        // 可以上拉，正在刷新时不能上拉
                        pullUpY = pullUpY + (ev.getY() - lastY) / radio;
                        if (pullUpY > 0) {
                            pullUpY = 0;
                            canPullDown = true;
                            canPullUp = false;
                        }
                        if (pullUpY < -getMeasuredHeight())
                            pullUpY = -getMeasuredHeight();
                        if (state == LOADING) {
                            // 正在加载的时候触摸移动
                            isTouch = true;
                        }
                    } else
                        releasePull();
                } else
                    mEvents = 0;
                lastY = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * (pullDownY + Math.abs(pullUpY))));
                requestLayout();
                if (pullDownY <= refreshDist && state == RELEASE_TO_REFRESH) {
                    // 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
                    changeState(INIT);
                }
                if (pullDownY >= refreshDist && state == INIT) {
                    L.hintI("pullDownY======" + pullDownY);

                    // 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
                    changeState(RELEASE_TO_REFRESH);
                }
                // 下面是判断上拉加载的，同上，注意pullUpY是负值
                if (-pullUpY <= loadmoreDist && state == RELEASE_TO_LOAD) {
                    changeState(INIT);
                }
                if (-pullUpY >= loadmoreDist && state == INIT) {
                    changeState(RELEASE_TO_LOAD);
                }

                // 下拉刷新限制距离
                if (pullDownY > pullDownMax) {// 最大下拉距离
                    pullDownY = pullDownMax;
                }
                // 上拉加载限制距离
                if (-pullUpY > pullUpMax) {
                    pullUpY = -pullUpMax;
                }

                // 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
                // Math.abs(pullUpY))就可以不对当前状态作区分了
                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                }
                break;
            case MotionEvent.ACTION_UP:
                TRIGGER = true;

                if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
                    // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
                    isTouch = false;
                if (state == RELEASE_TO_REFRESH) {
                    changeState(REFRESHING);
                    // 刷新操作
                    if (mListener != null)
                        mListener.onRefresh();
                } else if (state == RELEASE_TO_LOAD) {
                    changeState(LOADING);
                    // 加载操作
                    if (mListener != null)
                        mListener.onLoadMore();
                }
                if (pullDownY != 0 || pullUpY != 0) {
                    hide();
                } else {
                    timer.cancel();
                }
//			
            default:
                break;
        }
        // 事件分发交给父类
        super.dispatchTouchEvent(ev);
        return true;
    }

    /**
     * 下拉方法
     */
    private void pullDown() {
//		if (!yesOrNoClick) {
//			return;
//		}

        timer.cancel();
        mEvents = 0;
        releasePull();

        if (pullDownY > refreshDist || -pullUpY > loadmoreDist)
            // 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
            isTouch = false;
        if (state == RELEASE_TO_REFRESH) {
            changeState(REFRESHING);
            // 刷新操作
            if (mListener != null)
                mListener.onRefresh();
        } else if (state == RELEASE_TO_LOAD) {
            changeState(LOADING);
            // 加载操作
            if (mListener != null)
                mListener.onLoadMore();
        }
        hide();
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }
        // 初始化下拉布局
        refreshStateTextView = (TextView) refreshView.findViewById(R.id.state_tv);
        refreshStateTextView.setTextColor(mRefreshHeadTextColor);
        refreshingView = (ImageView) refreshView.findViewById(R.id.refreshing_icon);

        // 初始化上拉布局
        loadStateTextView = (TextView) loadmoreView.findViewById(R.id.loadstate_tv);
        loadingView = (ImageView) loadmoreView.findViewById(R.id.loading_icon);

        // 初始化3D动画旋转
        m3DRotateDownAnim = AnimationUtil.startAnimation(context, refreshingView);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!isLayout) {
            // 这里是第一次进来的时候做一些初始化
            isLayout = true;

            refreshView = getChildAt(0);
            pullableView = getChildAt(1);
            loadmoreView = getChildAt(2);

            refreshDist = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
            loadmoreDist = ((ViewGroup) loadmoreView).getChildAt(0).getMeasuredHeight();

            pullDownMax = refreshDist + 5;
            pullUpMax = refreshDist + 5;

            initView();
        }
        // 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
        mHeight = (int) (pullDownY + pullUpY);

        refreshView.layout(0, (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
                refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));

        pullableView.layout(0, (int) (pullDownY + pullUpY),
                pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight());
        loadmoreView.layout(0, (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight(),
                loadmoreView.getMeasuredWidth(), (int) (pullDownY + pullUpY) + pullableView.getMeasuredHeight() + loadmoreView.getMeasuredHeight());
    }

    class MyTimer {
        private Handler handler;
        private Timer timer;
        private MyTask mTask;

        public MyTimer(Handler handler) {
            this.handler = handler;
            timer = new Timer();
        }

        public void schedule(long period) {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            mTask = new MyTask(handler);

            timer.schedule(mTask, 0, period);
        }

        public void cancel() {
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
        }

        class MyTask extends TimerTask {
            private Handler handler;

            public MyTask(Handler handler) {
                this.handler = handler;
            }

            @Override
            public void run() {
                handler.obtainMessage().sendToTarget();
            }

        }
    }

    private int mheadHight;

    /**
     * 点击自动刷新(模拟下拉)
     */
    public void autoRefresh() {
        //下拉的距离
        int distance = 0;

        MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, 0, 0, 0);
        dispatchTouchEvent(event);

        while (state != PullToRefreshLayout.RELEASE_TO_REFRESH && mHeight == 0) {
            distance += 10;
            event.setAction(MotionEvent.ACTION_MOVE);
            event.setLocation(0, distance);
            dispatchTouchEvent(event);

            if (distance > windowHeight) {
                mHeight = -1;
            }
        }

        event.setAction(MotionEvent.ACTION_UP);
        dispatchTouchEvent(event);

        event.recycle();
    }

    /**
     * 设置头部高度
     *
     * @param headHight
     */
    public void setHeadHight(int headHight) {
        this.mheadHight = headHight;
    }

    /**
     * 获得刷新头的高度
     *
     * @return
     */
    public float getRefreshDistHeight() {
        return refreshDist;
    }

    public int getState() {
        return state;
    }

    public int getHeadHeight() {
        return mHeight;
    }


    /**
     * 刷新加载回调接口
     *
     * @author chenjing
     */
    public interface OnRefreshListener {
        /**
         * 下拉刷新操作
         */
        void onRefresh();

        /**
         * 上拉加载操作
         */
        // void onLoadMore(PullToRefreshLayout pullToRefreshLayout);
        void onLoadMore();
    }

}

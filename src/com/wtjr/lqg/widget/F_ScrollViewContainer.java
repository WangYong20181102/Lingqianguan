package com.wtjr.lqg.widget;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.widget.pullRefresh.Pullable;

/**
 * 包含两个ScrollView的容器 更多详解见博客http://dwtedx.com
 * 
 * @author myf
 * 
 */
public class F_ScrollViewContainer extends RelativeLayout implements Pullable{
	private Resources resources;
	public static  boolean GESTURES_STATE = true;
//	/**
//	 * 界面是否在处于用户操作状态(比如正在滑动中)
//	 */
//	public static  boolean TRIGGER = true;
	/**
	 * 主页底部标题栏的高度
	 */
	private int mainFootHeight;
	/**
	 * 自动上滑
	 */
	public static final int AUTO_UP = 0;
	/**
	 * 自动下滑
	 */
	public static final int AUTO_DOWN = 1;
	/**
	 * 动画完成
	 */
	public static final int DONE = 2;
	/**
	 * 动画速度
	 */
	public static final float SPEED = 6.5f;

	private boolean isMeasured = false;
	/**
	 * 设置上下拖动文字
	 */
	private TextView tvToDown;
	/**
	 * 滑动的图片
	 */
	private ImageView imageToDown;
	/**
	 * 用于计算手滑动的速度
	 */
	private VelocityTracker vt;

	private int mViewHeight;
	private int mViewWidth;

	private View topView;
	private View bottomView;

	public boolean canPullDown;
	private boolean canPullUp;
	private int state = DONE;

	private boolean PullUpRefresh = false;

	/**
	 * 记录当前展示的是哪个view，0是topView，1是bottomView
	 */
	private int mCurrentViewIndex = 0;
	/**
	 * 手滑动距离，这个是控制布局的主要变量
	 */
	private float mMoveLen;
	private MyTimer mTimer;
	private float mLastY;
	/**
	 * 用于控制是否变动布局的另一个条件，mEvents==0时布局可以拖拽了，mEvents==-1时可以舍弃将要到来的第一个move事件，
	 * 这点是去除多点拖动剧变的关键
	 */
	private int mEvents;
	
	private OnPageListener onPageListener;
	
	public interface OnPageListener{
	    void page(int p);
	}
	
	public void setOnPageListener(OnPageListener onPageListener) {
        this.onPageListener = onPageListener;
    }

	private static class MyHandler extends Handler {  
        private WeakReference<F_ScrollViewContainer> mStatus;  
  
        public MyHandler(F_ScrollViewContainer scrollViewContainer) {  
            mStatus = new WeakReference<F_ScrollViewContainer>(scrollViewContainer);  
        }  
  
        @Override  
        public void handleMessage(Message msg) {  
        	F_ScrollViewContainer scrollViewContainer = mStatus.get();  
            if (scrollViewContainer == null) {  
                return;  
            }  
            
        	if (scrollViewContainer.mMoveLen != 0) {
				if (scrollViewContainer.state == AUTO_UP) {
					scrollViewContainer.mMoveLen -= SPEED;
					if (scrollViewContainer.mMoveLen <= -scrollViewContainer.mViewHeight) {
						scrollViewContainer.mMoveLen = -scrollViewContainer.mViewHeight;
						scrollViewContainer.state = DONE;
						scrollViewContainer.mCurrentViewIndex = 1;
						
						if(scrollViewContainer.onPageListener != null) {
						    scrollViewContainer.onPageListener.page(scrollViewContainer.mCurrentViewIndex);
						}
						GESTURES_STATE=true;
					}
				} else if (scrollViewContainer.state == AUTO_DOWN) {
					scrollViewContainer.mMoveLen += SPEED;
					if (scrollViewContainer.mMoveLen >= 0) {
						scrollViewContainer.mMoveLen = 0;
						scrollViewContainer.state = DONE;
						scrollViewContainer.mCurrentViewIndex = 0;
						
						if(scrollViewContainer.onPageListener != null) {
                            scrollViewContainer.onPageListener.page(scrollViewContainer.mCurrentViewIndex);
                        }
					}
				} else {
					scrollViewContainer.mTimer.cancel();
				}
			} else {
				scrollViewContainer.mTimer.cancel();
			}
        	scrollViewContainer.requestLayout();
        }  
	}  
	

	public F_ScrollViewContainer(Context context) {
		super(context);
		init(context);
	}

	public F_ScrollViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public F_ScrollViewContainer(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		resources = context.getApplicationContext().getResources();
		mainFootHeight = (int) resources.getDimension(R.dimen.d55);
		
		
		MyHandler handler = new MyHandler(this);
		mTimer = new MyTimer(handler);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			
			GESTURES_STATE=false;
//			TRIGGER=false;
			if (vt == null)
				vt = VelocityTracker.obtain();
			else
				vt.clear();
			mLastY = ev.getY();
			vt.addMovement(ev);
			mEvents = 0;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:

		case MotionEvent.ACTION_POINTER_UP:
			// 多一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			
			GESTURES_STATE=false;
//			TRIGGER=false;
			if (vt == null) {
				vt = VelocityTracker.obtain();
			}
			vt.addMovement(ev);
			if (canPullUp && mCurrentViewIndex == 0 && mEvents == 0) {
				tvToDown.setText("继续拖动，查看安全保障");
				imageToDown.setImageResource(R.drawable.naving_up_down_page);
				mMoveLen += (ev.getY() - mLastY);
				// 防止上下越界
				if (mMoveLen > 0) {
					mMoveLen = 0;
					mCurrentViewIndex = 0;
				} else if (mMoveLen < -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;

				}
				if (mMoveLen < -8) {
					// 防止事件冲突
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else if (canPullDown && mCurrentViewIndex == 1 && mEvents == 0) {
				tvToDown.setText("继续拖动，回到上一页");
				imageToDown.setImageResource(R.drawable.naving_up_down_page);
				mMoveLen += (ev.getY() - mLastY);
				// 防止上下越界
				if (mMoveLen < -mViewHeight) {
					mMoveLen = -mViewHeight;
					mCurrentViewIndex = 1;
				} else if (mMoveLen > 0) {
					mMoveLen = 0;
					mCurrentViewIndex = 0;
				}
				if (mMoveLen > 8 - mViewHeight) {
					// 防止事件冲突
					ev.setAction(MotionEvent.ACTION_CANCEL);
				}
			} else
				mEvents++;
			mLastY = ev.getY();
			requestLayout();
			break;
		case MotionEvent.ACTION_UP:
			
			GESTURES_STATE=false;
//			TRIGGER=true;
			mLastY = ev.getY();
			vt.addMovement(ev);
			vt.computeCurrentVelocity(700);
			// 获取Y方向的速度
			float mYV = vt.getYVelocity();
			if (mMoveLen == 0 || mMoveLen == -mViewHeight)
				break;
			if (Math.abs(mYV) < 500) {	
			
				// 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
				if (mMoveLen <= -mViewHeight / 2) {
					tvToDown.setText("继续拖动，回到上一页");
					imageToDown.setImageResource(R.drawable.naving_up_down_page);
					state = AUTO_UP;
				} else if (mMoveLen > -mViewHeight / 2) {
					state = AUTO_DOWN;
				}
			} else {
				tvToDown.setText("继续拖动，查看安全保障");
				imageToDown.setImageResource(R.drawable.naving_up_down_page);
				// 抬起手指时速度方向决定两个View往哪移动
				if (mYV < 0) {
					state = AUTO_UP;
				} else {
					state = AUTO_DOWN;
					
				}
			}
			mTimer.schedule(2);
			vt.recycle();
			vt = null;
			// try {
			// vt.recycle();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			break;

		}
		// return true;
		return super.dispatchTouchEvent(ev);
	}

	
	/**
	 * 点击加载更多
	 */
	public void autoDown() {
		mMoveLen = -10;
		state = AUTO_UP;
		mTimer.schedule(2);
	}

	/**
	 * 点击调到第一页
	 */
	public void autoUp() {
		mMoveLen = 10;
		state = AUTO_DOWN;
		mTimer.schedule(2);
	}

	// 11-27 15:56:43.667: E/MessageQueue-JNI(993):
	// java.lang.IllegalStateException: Already in the pool!

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		topView.layout(0, (int) mMoveLen, mViewWidth,topView.getMeasuredHeight() + (int) mMoveLen);

		bottomView.layout(0, topView.getMeasuredHeight() + (int) mMoveLen,
				mViewWidth, topView.getMeasuredHeight() + (int) mMoveLen
						+ bottomView.getMeasuredHeight());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		if (!isMeasured) {
			isMeasured = true;
			// mViewHeight = getMeasuredHeight();
			mViewHeight = getMeasuredHeight() - mainFootHeight;
			mViewWidth = getMeasuredWidth();
			topView = getChildAt(0);
			bottomView = getChildAt(1);

			bottomView.setOnTouchListener(bottomViewTouchListener);
			topView.setOnTouchListener(topViewTouchListener);

		}
	}

	private OnTouchListener topViewTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ScrollView sv = (ScrollView) v;
			if (sv.getScrollY() < 1) {
				PullUpRefresh = true;
			} else {
				PullUpRefresh = false;
			}
			if (sv.getScrollY() == (sv.getChildAt(0).getMeasuredHeight() - sv
					.getMeasuredHeight()) && mCurrentViewIndex == 0) {
				canPullUp = true;
			} else {
				canPullUp = false;
			}

			return false;
		}
	};

	private OnTouchListener bottomViewTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			PullUpRefresh = false;
			ScrollView sv = (ScrollView) v;
			if (sv.getScrollY() == 0 && mCurrentViewIndex == 1) {
				canPullDown = true;
			} else {
				canPullDown = false;
			}
			return false;
		}
	};
	

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

	/**
	 * 重置测量标记
	 */
	public void resetMeasured() {
		isMeasured = false;
	}

	public void setTvToDown(TextView tvToDown,ImageView imageToDown) {
		this.tvToDown = tvToDown;
		this.imageToDown = imageToDown;
	}

	/**
	 * 返回当前是第一页还是第2页
	 * 
	 * @return
	 */
	public int getCurrentViewIndex() {
		return mCurrentViewIndex;
	}

	@Override
	public boolean canPullDown() {
		return PullUpRefresh;
	}

	@Override
	public boolean canPullUp() {
		// TODO Auto-generated method stub
		return false;
	}

}

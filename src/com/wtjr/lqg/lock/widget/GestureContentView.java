package com.wtjr.lqg.lock.widget;

import java.util.ArrayList;
import java.util.List;

import org.xutils.x;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.lock.GesturePoint;
import com.wtjr.lqg.lock.widget.GestureDrawline.GestureCallBack;

/**
 * 手势密码容器类
 * 
 */
public class GestureContentView extends ViewGroup {

	private int baseNum = 7;
	/**
	 * 得到屏幕的宽度
	 */
	private int mScreenDispaly;
	/**
	 * 左右之间的边距，各100
	 */
	private int mLandRMargin = 0;
	/**
	 * 每个点区域的宽度
	 */
	private int blockWidth;
	/**
	 * 声明一个集合用来封装坐标集合
	 */
	private List<GesturePoint> list;
	private Context context;
	private boolean isVerify;
	private GestureDrawline gestureDrawline;
	
	/**
	 * 是否显示轨迹
	 */
	private boolean isGesturesTrackColor;

	/**
	 * 包含9个ImageView的容器，初始化
	 * 
	 * @param context
	 * @param isVerify
	 *            是否为校验手势密码
	 * @param passWord
	 *            用户传入密码
	 * @param callBack
	 *            手势绘制完毕的回调
	 */
	public GestureContentView(Context context, boolean isVerify,
			String passWord, GestureCallBack callBack,boolean gesturesTrackColor) {
		super(context);
		isGesturesTrackColor=gesturesTrackColor;
		LqgApplication app = (LqgApplication) x.app();
		// 设置屏幕宽度
		mScreenDispaly = app.mScreenWidth;
		// 设置左右边距
		mLandRMargin = (int) (110 * app.mDensity);

		blockWidth = (mScreenDispaly - mLandRMargin) / 3;
		this.list = new ArrayList<GesturePoint>();
		this.context = context;
		this.isVerify = isVerify;
		// 添加9个图标
		addChild();
		// 初始化一个可以画线的view
		gestureDrawline = new GestureDrawline(context, list, isVerify,passWord, callBack,gesturesTrackColor);
	}

	private void addChild() {
		for (int i = 0; i < 9; i++) {
			ImageView image = new ImageView(context);
			LayoutParams layoutParams = new LayoutParams(50,50);
			image.setLayoutParams(layoutParams);
			image.setBackgroundResource(R.drawable.gesture_node_normal);
			this.addView(image);
			invalidate();
			int[] location = setLocation(i);
			GesturePoint p = new GesturePoint(location[0], location[1], location[2],location[3], image, i + 1,isGesturesTrackColor);
			this.list.add(p);
		}
	}

	/**
	 * 设置父类控件宽高
	 * 
	 * @param parent
	 */
	public void setParentView(ViewGroup parent) {
		LayoutParams layoutParams = new LayoutParams(mScreenDispaly,
				mScreenDispaly - mLandRMargin);
		this.setLayoutParams(layoutParams);
		gestureDrawline.setLayoutParams(layoutParams);
		parent.addView(gestureDrawline);
		parent.addView(this);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			int[] location = setLocation(i);
			v.layout(location[0], location[1], location[2], location[3]);
		}
	}

	/**
	 * 设置位置
	 * 
	 * @param i
	 * @return
	 */
	private int[] setLocation(int i) {
		// 第几行
		int row = i / 3;
		// 第几列
		int col = i % 3;
		// 距离
		int distance = blockWidth / baseNum;

		int leftX = col * blockWidth + distance + (mLandRMargin / 2);
		int topY = row * blockWidth + distance;
		int rightX = col * blockWidth + blockWidth - distance
				+ (mLandRMargin / 2);
		int bottomY = row * blockWidth + blockWidth - distance;
		int location[] = { leftX, topY, rightX, bottomY };
		return location;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 遍历设置每个子view的大小
		for (int i = 0; i < getChildCount(); i++) {
			View v = getChildAt(i);
			v.measure(widthMeasureSpec, heightMeasureSpec);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * 保留路径delayTime时间长
	 * 
	 * @param delayTime
	 */
	public void clearDrawlineState(long delayTime) {
		gestureDrawline.clearDrawlineState(delayTime);
	}

}

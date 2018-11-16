package com.wtjr.lqg.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.GuideViewPagerAdapter;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.basecommonly.BaseAppManager;
import com.wtjr.lqg.sharedpreferences.SaveGuideState;

/**
 * 引导页
 * 
 * @author Myf
 * 
 */
public class GuideActivity extends BaseActivity {
	/**
	 * 滑动翻页
	 */
	private ViewPager vpGuide;
	/**
	 * 存放点的父类容器
	 */
	private LinearLayout llPointsGuide;
	/**
	 * 装分页显示的view的数组
	 */
	private ArrayList<View> pageViews = new ArrayList<View>();
	private GuideViewPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_guide);
		start();
	}

	@Override
	public void findViewById() {
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		llPointsGuide = (LinearLayout) findViewById(R.id.ll_points_guide);

	}

	@Override
	public void setListener() {
		// 轮播监听
		vpGuide.setOnPageChangeListener(new MyPageChangeListener());
	}

	@Override
	public void initData() {
		// 将要分页显示的View装入数组中
		LayoutInflater inflater = getLayoutInflater();
		pageViews.add(inflater.inflate(R.layout.guide_item1, null));
		pageViews.add(inflater.inflate(R.layout.guide_item2, null));
		View view3 = inflater.inflate(R.layout.guide_item3, null);
		pageViews.add(view3);

		mPagerAdapter = new GuideViewPagerAdapter(this, pageViews);

		vpGuide.setAdapter(mPagerAdapter);

		initDots();
		
		TextView tvEntrance = (TextView) view3.findViewById(R.id.tv_entrance);
		
		tvEntrance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SaveGuideState.closeGuide(app);
				Intent intent=new Intent(GuideActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	

	/**
	 * 初始化dot
	 */
	private void initDots() {
		llPointsGuide.removeAllViews();
		for (int i = 0; i < pageViews.size(); i++) {
			ImageView imageView = new ImageView(this);
			// 设置小圆点imageview的参数
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			layoutParams.leftMargin = 20;
			imageView.setLayoutParams(layoutParams);
			// // 创建一个宽高均为20的布局
			imageView.setBackgroundResource(R.drawable.investment_dot_state);
			llPointsGuide.addView(imageView);
			updateDots();
		}
		updateDots();
	}

	/**
	 * 更新点
	 */
	private void updateDots() {
		int currentPage = vpGuide.getCurrentItem() % pageViews.size();
		for (int i = 0; i < llPointsGuide.getChildCount(); i++) {
			llPointsGuide.getChildAt(i).setEnabled(i == currentPage);// 设置setEnabled为true的话
		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		public void onPageSelected(int position) {
			updateDots();
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}
}

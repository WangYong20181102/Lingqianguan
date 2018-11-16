package com.wtjr.lqg.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @company KCS互联网有限公司
 * 
 * @copyright KCS互联网有限公司版权所有 (c) 2014-2015
 * 
 * @author kincai
 * 
 * @description viewpager适配器
 * 
 * @project Zhuanti_Custom_View_Adv_ViewPager
 * 
 * @package com.kincai.zhuanti_custom_view_adv_viewpager
 * 
 * @time 2015-7-25 上午10:42:53
 * 
 */
public class GuideViewPagerAdapter extends PagerAdapter {

	private List<View> pageViews;
	private Context mContext;

	public GuideViewPagerAdapter(Context context, ArrayList<View> pageViews) {
		this.mContext = context;
		this.pageViews = pageViews;
	}

	/**
	 * 返回多少page
	 */
	@Override
	public int getCount() {
		return pageViews.size();
	}

	/**
	 * 判断当前滑动view等不等进来的对象
	 * 
	 * true: 表示不去创建，使用缓存 false:去重新创建 view： 当前滑动的view
	 * object：将要进入的新创建的view，由instantiateItem方法创建
	 */
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	/**
	 * 类似于BaseAdapger的getView方法 用了将数据设置给view 由于它最多就3个界面，不需要viewHolder
	 */
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
Log.d("myf", position+"==================");
		((ViewPager) container).addView(pageViews.get(position));
		return pageViews.get(position);
	}

	/**
	 * 销毁page position： 当前需要消耗第几个page object:当前需要消耗的page
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}

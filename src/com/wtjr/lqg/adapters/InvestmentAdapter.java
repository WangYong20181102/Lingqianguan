package com.wtjr.lqg.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 投资列表的适配器
 * 
 * @author Myf
 * 
 */
public class InvestmentAdapter extends BaseAdapter {
	/**
	 * 总的item样式数
	 */
	final int VIEW_TYPE_COUNT = 3; // 
	
	/**
	 *  第一个item布局
	 */
	final int TYPE_0 = 0; //
	/**
	 * 第二个item布局
	 */
	final int TYPE_1 = 1; // 
	
	
	
	private LayoutInflater mInflater;
	/**
	 * 集合
	 */
	private List<String> mList;

	public InvestmentAdapter(Context context, List<String> list) {
		mList = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 根据不同的位置，使用不同的布局
	@Override
	public int getItemViewType(int position) {
		if (position==0) {
			return TYPE_0;
		}else{
			return TYPE_1;
		}
	}
	
	 // item样式类型总数
	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE_COUNT;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holder;
		int type = getItemViewType(position); // 获取所有样式类型总数
		if (convertView == null) {
			holder = new HolderView();
//			switch (type) {
//			case TYPE_0:
//				convertView = mInflater.inflate(R.layout.investment_item_type_0, null);
//				break;
//			case TYPE_1:
//				convertView = mInflater.inflate(R.layout.investment_item_type_1, null);
//				break;
//			}
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		
		switch (type) {
		case TYPE_0:
			type0(convertView);
			break;
		case TYPE_1:
			type1(convertView);
			break;
		}
		
		return convertView;
	}
	
	/**
	 * 控件持有者
	 * @author Myf
	 *
	 */
	public class HolderView {
		private TextView textView1;
	}
	
	
	/**
	 * 0类型的
	 * @param convertView
	 */
	private void type0(View convertView) {

	}
	
	/**
	 * 1类型的
	 * @param convertView
	 */
	private void type1(View convertView) {

	}
}

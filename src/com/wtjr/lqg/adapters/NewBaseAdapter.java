package com.wtjr.lqg.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * 配备器工具
 * @author dell
 *
 * @param <T>
 */
public abstract class NewBaseAdapter<T> extends android.widget.BaseAdapter {
	private List<T> list;

	protected Context context;
	

	public NewBaseAdapter(Context context) {
		init(context, new ArrayList<T>());
	}

	public NewBaseAdapter(Context context, List<T> list) {
		init(context, list);
	}

	private void init(Context context, List<T> list) {
		this.list = list;
		this.context = context;
		
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public void clear() {
		this.list.clear();
		notifyDataSetChanged();
	}

	public void addAll(List<T> list) {
		if (list != null) {
			this.list.addAll(list);
			notifyDataSetChanged();
		}
	}
	
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
			if (null == convertView) {
				convertView = inflate(getContentView());
			}
			
			onInitView(convertView, position);

		return convertView;
	}

	/**
	 * 加载布局 
	 * @param layoutResID
	 * @return
	 */
	private View inflate(int layoutResID) {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(layoutResID, null);
		return view;
	}
	
	/**
	 * 设置布局文件
	 * @return 返回设置的布局文件
	 */
	public abstract int getContentView();
	
	/**
	 * 初始化控件
	 * @param view
	 * @param position
	 */
	public abstract void onInitView(View view, int position);
	
	/**
	 * 所有子控件做viewHolder缓存
	 * @param view 父控件
	 * @param id 子控件id
	 * @return 
	 */
	public <T extends View> T getViewChild(View view, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		
		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}
		return (T) childView;
	}
}

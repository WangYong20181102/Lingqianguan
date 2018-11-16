package com.wtjr.lqg.adapters;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.adapters.SimpleAdatper.SimpleViewHolder;

public class SimpleAdatper extends RecyclerView.Adapter<SimpleViewHolder> {
	private List<String> mDatas;
	private Context mContext;
	
	/**
	 * 点击监听
	 * @author Myf
	 *
	 */
	public interface OnItemClickLitener {
		void onItemClick(View view, int position);
	}
	
	/**
	 * 点击监听器
	 */
	private OnItemClickLitener mOnItemClickLitener;
	
	/**
	 * 实现点击监听器接口
	 * @param mOnItemClickLitener
	 */
	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}
	
	
	/**
	 * 长按监听
	 * @author Myf
	 *
	 */
	public interface OnItemLongClickLitener {
		void onItemLongClick(View view, int position);
	}
	/**
	 * 长按监听器
	 */
	private OnItemLongClickLitener mOnItemLongClickLitener;
	/**
	 * 实现长按点击监听器接口
	 * @param mOnItemLongClickLitener
	 */
	public void setOnItemLongClickLitener(OnItemLongClickLitener mOnItemLongClickLitener) {
		this.mOnItemLongClickLitener = mOnItemLongClickLitener;
	}

	
	public SimpleAdatper(Context context, List<String> mDatas) {
		this.mDatas = mDatas;
		this.mContext = context;
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public void onBindViewHolder(final SimpleViewHolder holder,final int position) {
		holder.tv.setText(mDatas.get(position));
		//设置点击回调
		if (mOnItemClickLitener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickLitener.onItemClick(holder.itemView, position);
				}
			});
		}
		//设置长按点击回调
		if (mOnItemLongClickLitener != null) {
			holder.itemView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					mOnItemLongClickLitener.onItemLongClick(holder.itemView,position);
					//设置false,长按点击都有
					return true;
				}
			});
		}
	}

	@Override
	public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
		SimpleViewHolder holder = new SimpleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item, parent, false));
		return holder;
	}
	
	/**
	 * 简单控件持有者
	 * @author Myf
	 *
	 */
	class SimpleViewHolder extends ViewHolder {

		TextView tv;

		public SimpleViewHolder(View view) {
			super(view);
			tv = (TextView) view.findViewById(R.id.id_num);
		}
	}

}

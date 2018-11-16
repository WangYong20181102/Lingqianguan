package com.wtjr.lqg.adapters;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.TyjDetails;
import com.wtjr.lqg.base.TyjDetailsTime;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.widget.pullRefresh.PullableExpandableListView;

public class TyjDetailsExpandableListAdapter extends BaseExpandableListAdapter{
		
		private Context context;
		// 一级标签上的状态图片数据源
		public int child_groupId= -1;
		public int child_childId= -1;
//		-----------------------------------------------------------------
		private List<TyjDetailsTime> tyjDetailsTimes;
		private PullableExpandableListView expandableListView;
		private Resources resources;
		public TyjDetailsExpandableListAdapter(Context context, List<TyjDetailsTime> tyjDetailsTimes, PullableExpandableListView expandableListView) {
			this.context=context;
			this.tyjDetailsTimes=tyjDetailsTimes;
			this.expandableListView=expandableListView;
			
			resources = context.getResources();
		}

		// 重写ExpandableListAdapter中的各个方法
		/**
		 * 获取一级标签总数
		 */
		@Override
		public int getGroupCount() {
			return tyjDetailsTimes.size();
		}

		/**
		 * 获取一级标签内容
		 */
		@Override
		public Object getGroup(int groupPosition) {
			return tyjDetailsTimes.get(groupPosition);
		}

		/**
		 * 获取一级标签的ID
		 */
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		/**
		 * 获取一级标签下二级标签的总数
		 */
		@Override
		public int getChildrenCount(int groupPosition) {
			return tyjDetailsTimes.get(groupPosition).getListSize();
		}

		/**
		 * 获取一级标签下二级标签的内容
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return tyjDetailsTimes.get(groupPosition).getContactItemByIndex(childPosition);
		}

		/**
		 * 获取二级标签的ID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		/**
		 * 指定位置相应的组视图
		 */
		@Override
		public boolean hasStableIds() {
			return true;
		}

		/**
		 * 对一级标签进行设置
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			expandableListView.expandGroup(groupPosition);//展开分组
			TyjDetailsTime tyjDetailsTime = (TyjDetailsTime) getGroup(groupPosition);
			// 为视图对象指定布局
			convertView = (RelativeLayout) RelativeLayout.inflate(context, R.layout.item_fund_log_group, null);
			TextView group_title = (TextView) convertView.findViewById(R.id.group_title);
			group_title.setText(tyjDetailsTime.getTime());
			return convertView;
		}

		/**
		 * 对一级标签 下的二级标签进行设置
		 */
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
		    TyjDetails tyjDetails = (TyjDetails) getChild(groupPosition, childPosition);
			HolderView holder=null;
			if (convertView == null) {
				holder = new HolderView();
				convertView = (RelativeLayout) RelativeLayout.inflate(context, R.layout.item_fund_log_child, null);
				// 新建一个TextView对象，用来显示具体内容
				holder.child_money = (TextView) convertView.findViewById(R.id.child_money);
				holder.child_type = (TextView) convertView.findViewById(R.id.child_type);
				holder.child_time = (TextView) convertView.findViewById(R.id.child_time);
				
				//负责显示隐藏的
				holder.layout = (LinearLayout) convertView.findViewById(R.id.show);
				holder.layout.setVisibility(View.GONE);
				holder.child_dealNumber_content = (TextView) convertView.findViewById(R.id.child_dealNumber_content);
				holder.child_dealTime_content = (TextView) convertView.findViewById(R.id.child_dealTime_content);
				holder.child_remark_title = (TextView) convertView.findViewById(R.id.child_remark_title);
				holder.child_remark_content = (TextView) convertView.findViewById(R.id.child_remark_content);
				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}
			
			//设置点击展开的
//			if (child_groupId == groupPosition && child_childId == childPosition) {
//				holder.layout.setVisibility(View.VISIBLE);
//			} else {
//				holder.layout.setVisibility(View.GONE);
//			}
			
//			holder.layout.setOnClickListener(null);
//			holder.child_dealNumber_content.setText(lqbDetails.lqbOptTime+"");
//			holder.child_dealTime_content.setText(TimeUtil.getY_M_D_Type2(lqbDetails.lqbOptTime));
			
//			if(lqbDetails.lqbUserunid == null || lqbDetails.lqbUserunid.equals("null")){
//				holder.child_remark_title.setVisibility(View.GONE);
//				holder.child_remark_content.setVisibility(View.GONE);
//			}else{
//				holder.child_remark_content.setText(lqbDetails.lqbUserunid);
//			}
			
			holder.child_type.setText(tyjDetails.explain);
			
			setMoneyUpandDown(holder.child_money, tyjDetails.capital);
						
			holder.child_time.setText(TimeUtil.getFullTime(tyjDetails.experLogTime,"MM-dd HH:mm"));
			return convertView;
		}
	      
        /**
         * 设置钱数是加还是减
         */
        private void setMoneyUpandDown(TextView view,double money) {
            if (money >= 0) {
                view.setText("+"+MoneyFormatUtil.format(money));
                view.setTextColor(resources.getColor(R.color.FC_FF6434));
            } else {
                view.setText(MoneyFormatUtil.format(money));
                view.setTextColor(resources.getColor(R.color.FC_B7DE7D));
            }
        }
		
		
		private class HolderView{
			private TextView child_money;
			private TextView child_type;
			private TextView child_time;
			private LinearLayout layout;
			private TextView child_remark_title;
			private TextView child_dealNumber_content;
			private TextView child_dealTime_content;
			private TextView child_remark_content;
		}

		/**
		 * 当选择子节点的时候，调用该方法
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return true;
		}

}

package com.wtjr.lqg.adapters;

import java.util.List;

import org.xutils.x;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.base.MessageFeedBack;
import com.wtjr.lqg.utils.HeadChangeUtil;
import com.wtjr.lqg.utils.ImageTools;
import com.wtjr.lqg.utils.TimeUtil;

/**
 * 反馈信息的适配器
 * 
 * @author dell
 * 
 */
public class FeedBackAdapter extends BaseAdapter {
	private List<MessageFeedBack> backs;
	private LayoutInflater inflater;
	private Context mContext;
	public FeedBackAdapter(Context context, List<MessageFeedBack> backs) {
		this.mContext = context;
		this.backs = backs;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return backs.size();
	}

	@Override
	public Object getItem(int position) {
		return backs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		MessageFeedBack messageFeedBack = backs.get(position);
		return messageFeedBack.userType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MessageFeedBack messageFeedBack = backs.get(position);
		int type = messageFeedBack.userType;
		
		HolderView1 holder1 = null;
		HolderView2 holder2 = null;
		
		if (convertView==null) {
			if (type==1) {
				holder1 = new HolderView1();
				
				convertView=inflater.inflate(R.layout.item_feed_back_l, null);
				holder1.tv_time_l = (TextView) convertView.findViewById(R.id.tv_time_l);
				holder1.image_serivce_head_l=(ImageView) convertView.findViewById(R.id.image_serivce_head_l);
				holder1.tv_serivce_name_l=(TextView) convertView.findViewById(R.id.tv_serivce_name_l);
				holder1.tv_context_l=(TextView) convertView.findViewById(R.id.tv_context_l);
				holder1.ll_l=(LinearLayout) convertView.findViewById(R.id.ll_l);
				convertView.setTag(holder1);
				
			}else{
				holder2 = new HolderView2();
				
				convertView=inflater.inflate(R.layout.item_feed_back_r, null);
				holder2.tv_time_r=(TextView) convertView.findViewById(R.id.tv_time_r);
				holder2.image_my_heand_r=(ImageView) convertView.findViewById(R.id.image_my_heand_r);
				holder2.my_name_r=(TextView) convertView.findViewById(R.id.my_name_r);
				holder2.tv_content_r=(TextView) convertView.findViewById(R.id.tv_content_r);
				holder2.ll_r=(LinearLayout) convertView.findViewById(R.id.ll_r);
				convertView.setTag(holder2);
				
			}
		} else {
			if (type==1) {
				holder1 = (HolderView1) convertView.getTag();
			}else{
				holder2 = (HolderView2) convertView.getTag();
			}
		}
		
		
		if (type==1) {
			holder1.tv_time_l.setText(TimeUtil.getTimeType(messageFeedBack.optTime));
			holder1.image_serivce_head_l.setImageResource(R.drawable.kefu);
			holder1.tv_serivce_name_l.setText("零妹妹");
			holder1.tv_context_l.setText(messageFeedBack.content);
			holder1.ll_l.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				}
			});
		}else{
			holder2.tv_time_r.setText(TimeUtil.getTimeType(messageFeedBack.optTime));
			holder2.image_my_heand_r.setImageResource(R.drawable.ic_launcher);
			String sUserName = ((LqgApplication)x.app()).mAccountData.nickName;
			if (TextUtils.isEmpty(sUserName)) {
				sUserName = ((LqgApplication)x.app()).mAccountData.phone;
			}
			holder2.my_name_r.setText(sUserName);
			holder2.tv_content_r.setText(messageFeedBack.content);
			holder2.ll_r.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				}
			});
			
			holder2.image_my_heand_r.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent setHead = new Intent(mContext, SetHeadActivity.class);
//					mContext.startActivity(setHead);
				}
			});
			
			HeadChangeUtil.requestHeadImage((LqgApplication)x.app(), holder2.image_my_heand_r);
		}
		
		return convertView;
	}
	
	
	/**
	 * 回复者反馈视图容器
	 */
	class HolderView1 {
		TextView tv_time_l;
		ImageView image_serivce_head_l;
		TextView tv_serivce_name_l;
		TextView tv_context_l;
		LinearLayout ll_l;
		
	}
	
	/**
	 * 自己反馈视图容器
	 */
	class HolderView2 {
		/**
		 * 反馈时间
		 */
		TextView tv_time_r;
		/**
		 * 自己的头像
		 */
		ImageView image_my_heand_r;
		/**
		 * 自己的名字
		 */
		TextView my_name_r;
		/**
		 * 自己反馈的内容
		 */
		TextView tv_content_r;
		/**
		 * 反馈时间
		 */
		LinearLayout ll_r;
	}
}

package com.wtjr.lqg.utils.bidstate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.wtjr.lqg.R;

/**
 * 设置标状态工具类
 * 
 * @author Myf
 * 
 */
public class BidStateUtil {
	/**
	 * 根据状态设置图片颜色
	 * @param str 状态
	 * @return
	 */
	public static int setBidStateImageUtil(String str) {
		switch (str) {
			case "满标"://满标
				return R.drawable.investment_item_full;
			case "投标中"://投标中
				return R.drawable.investment_item_cast;
			case "还款中"://还款中
				return R.drawable.investment_item_cast;
			case "收益中"://收益中
				return R.drawable.investment_item_cast;
			case "已还清"://已还清
				return R.drawable.investment_item_end;
			case "已完成"://已完成
				return R.drawable.investment_item_end;
			case "倒计时"://倒计时
				return R.drawable.investment_item_timer;
			case "募集中"://募集中
				return R.drawable.investment_item_cast;
			case "已募满"://已募满
				return R.drawable.investment_item_full;
		default:
			//以上都没有，默认倒计时图片
			return R.drawable.investment_item_timer;
		}
	}
	/**
	 * 根据状态设置字体颜色
	 * @param str 状态
	 * @return
	 */
	public static int getBidStateTextColor(String str) {
		int color;
		// 已还清字体颜色会变
		if (str.equals("已还清") || str.equals("已完成")) {
			color = Color.parseColor("#cccccc");
		} else {// 其他
			color = Color.parseColor("#FF6021");
		}
		return color;

	}
	
	/**
	 * 根据状态设置进度条颜色
	 * @param context 上下文
	 * @param str 状态
	 * @return 图片
	 */
	public static Drawable getBidStateProgressDrawable(Context context,String str){
		int id;
		switch (str) {
		case "已还清":
			id=R.drawable.progress_end;
			break;
		case "还款中":
//			id=R.drawable.progress_repay;
			id=R.drawable.progress_refund;
			break;
		default:
			id=R.drawable.progress_refund;
			break;
		}
		Drawable d = context.getApplicationContext().getResources().getDrawable(id);
		return d;
	}
	
	
}

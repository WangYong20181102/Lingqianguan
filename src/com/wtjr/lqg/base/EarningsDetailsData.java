package com.wtjr.lqg.base;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 预期明日收益请求数据
 * */
@Table(name="EarningsDetailsData")
public class EarningsDetailsData implements Serializable{
	private static final long serialVersionUID = 5527094123256794235L;
	/**
	 * isId = true 是主键，autoGen = false不自增
	 */
	@Column(name = "uId", isId = true, autoGen = false)
	public String uId;

	/**
	 *  累计总收益
	 * */
	@Column(name="totalProfit")
	public double totalProfit;

	
	/**
	 * 项目收益
	 * */
	@Column(name="investmentProfit")
	public  double investmentProfit;
	/**
	 * 零钱宝收益
	 * */
	@Column(name="lpbProfit")
	public double lpbProfit;
	/**
	 * 收益日志
	 * */
	@Column(name="timeStamp")
	public String timeStamp;

	@Override
	public String toString() {
		return "EarningsDetailsData [totalProfit=" + totalProfit
				+ ", investmentProfit=" + investmentProfit + ", lpbProfit="
				+ lpbProfit + ", timeStamp=" + timeStamp + "]";
	}
}





package com.wtjr.lqg.base;

import java.util.Comparator;

/**
 * 钱包列表详情
 * 
 * @author dell
 * 
 */
public class PureseListDetails implements Comparator<PureseListDetails>{
	
	/**
	 * 钱
	 */
	public String money;
	/**
	 * 类型
	 */
	public String type;
	/**
	 * 时间
	 */
	public long addtime;
	
	public String act="0.00";
	
	/**
	 * 流水号
	 */
	public String numsId;
	/**
	 * 备注的内容
	 */
	public String remark;
	
	/**
	 * 主要用于区分体验金是在哪个界面进入的(主页点击进入的是0,零钱宝点击进入的是1),不参与解析
	 */
	public int flag;
	

	public long getAddtime() {
		return addtime;
	}

	public void setAddtime(long addtime) {
		this.addtime = addtime;
	}


	@Override
	public String toString() {
		return "PureseListDetails [money=" + money + ", type=" + type
				+ ", addtime=" + addtime + ", act=" + act + ", numsId="
				+ numsId + "]";
	}

	@Override
	public int compare(PureseListDetails lhs, PureseListDetails rhs) {
//		return rhs.getAddtime().compareTo(lhs.getAddtime());
		return (int) (rhs.getAddtime()-lhs.getAddtime());
	}

}

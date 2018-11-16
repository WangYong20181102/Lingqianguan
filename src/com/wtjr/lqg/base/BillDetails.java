package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * 交易记录详情
 * @author dell
 *
 */
public class BillDetails implements Serializable{
	private static final long serialVersionUID = -3850790922205759134L;
	
	/**
	 * 时间
	 */
	public long time;
	/**
	 * 类型(1、充值 ,2、提现 ,3、转入零钱宝 ,4、转出零钱宝 ,5、投资定期标 ,6、定期标到期收益 ,7、零钱宝获得收益 ,8、 定期标获得收益，9、银行卡转入零钱宝，10、定期标到期还本，11、金额冲正，12、现金奖励)
	 */
	public int type;
	/**
	 * 钱
	 */
	public double money;
	/**
	 * 注释
	 */
	public String remark;
	/**
	 * 流水号
	 */
	public String detailOptTime;
	
//	{
//        "time":1468076996000,
//        "remark":"转出零钱宝",
//        "money":80000,
//        "type":4,
//        "detailOptTime":1468076996000
//    }
	
	
}

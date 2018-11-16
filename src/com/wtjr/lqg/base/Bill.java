package com.wtjr.lqg.base;

import java.io.Serializable;

public class Bill implements Serializable {
    private static final long serialVersionUID = -7278937833728050229L;
    /**
     * 日期
     */
    public long date;

    /**
     * 收益
     */
    public double income;
    /**
     * 充值
     */
    public double charge;
    /**
     * 转入
     */
    public double rollIn;
    /**
     * 转出
     */
    public double rollOut;
    /**
     * 提现
     */
    public double withdraw;
    /**
     * 手续费
     */
    public double poundage;
    /**
     * 冲正
     */
    public double chongzheng;

    @Override
    public String toString() {
	return "Bill [date=" + date + ", income=" + income 
		+ ", charge=" + charge + ", rollIn=" + rollIn 
		+ ", rollOut=" + rollOut + ", withdraw=" 
		+ withdraw + ", poundage=" + poundage 
		+ ", chongzheng=" + chongzheng + "]";
    }
}
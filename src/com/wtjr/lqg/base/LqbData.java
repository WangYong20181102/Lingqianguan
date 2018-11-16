package com.wtjr.lqg.base;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
@Table(name="LqbData")
public class LqbData implements Serializable{
	private static final long serialVersionUID = -6401443424739891848L;
	/**
     * isId = true 是主键，autoGen = false不自增
     */
    @Column(name = "uId", isId = true, autoGen = false)
    public String uId;
    /**
     * 零钱宝利率
     * */
    @Column(name="lqbRate")
    public double lqbRate;
    /**
     * 零钱宝体验金 
     * */
    @Column(name="tyjinlqb")
    public double tyjinlqb;
    /**
     * 投资天数
     */
    @Column(name="investmentDays")
    public int investmentDays;
    /**
     * 零钱宝累计收益
     * */
    @Column(name="lqbTotalIncome")
    public double lqbTotalIncome;
	/**
     * 零钱宝//总资产
     * */
    @Column(name="lqb")
    public double lqb;
    /**
     * 零钱宝昨日收益
     * */
    @Column(name="lqbYesterdayIncome")
    public double lqbYesterdayIncome;

    /**
     * 零钱宝利率最大值
     */
    @Column(name = "maxLqbRate")
    public String maxLqbRate;
    /**
     * 零钱宝利率开始值
     */
    @Column(name = "minLqbRate")
    public String minLqbRate;
    /**
     * 是否有计划（0.暂无计划 1.有计划）
     */
    @Column(name = "planStatus")
    public int planStatus;
    /**
     * 基础利率
     */
    @Column(name = "baseRate")
    public double baseRate;
    /**
     * 最大利率
     */
    @Column(name = "maxRate")
    public double maxRate;
    /**
     * 增长利率
     */
    @Column(name = "rateIncrease")
    public double rateIncrease;
    /**
     * 周周升最小投资金额
     */
    @Column(name = "investMin")
    public double investMin;
    /**
     * 周周升最大投资金额
     */
    @Column(name = "investMax")
    public double investMax;
    public LqbConfig lqbConfig;



    @Override
    public String toString() {
        return "LqbData{" +
                "uId='" + uId + '\'' +
                ", lqbRate=" + lqbRate +
                ", tyjinlqb=" + tyjinlqb +
                ", investmentDays=" + investmentDays +
                ", lqbTotalIncome=" + lqbTotalIncome +
                ", lqb=" + lqb +
                ", lqbYesterdayIncome=" + lqbYesterdayIncome +
                ", maxLqbRate='" + maxLqbRate + '\'' +
                ", minLqbRate='" + minLqbRate + '\'' +
                '}';
    }

}

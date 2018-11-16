package com.wtjr.lqg.base;

public class EarningsDetails {
    /**
     * 零钱宝收益
     */
    public double lpbProfit;
    /**
     * 投资收益
     */
    public double investmentProfit;
    /**
     * 时间
     */
    public long timeStamp;
    
    
    @Override
    public String toString() {
        return "EarningsDetails [lpbProfit=" + lpbProfit
                + ", investmentProfit=" + investmentProfit + ", timeStamp="
                + timeStamp + "]";
    }
}

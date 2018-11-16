package com.wtjr.lqg.base;

import java.util.List;

/**
 * 时间
 * 
 * @author dell
 * 
 */
public class BalanceDetailsTime {
    /**
     * 一级列表时间
     */
    private String time;

    private List<BalanceDetails> balanceDetails;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    

    public List<BalanceDetails> getBalanceDetails() {
        return balanceDetails;
    }

    public void setBalanceDetails(List<BalanceDetails> balanceDetails) {
        this.balanceDetails = balanceDetails;
    }

    /**
     * 获得的总数
     */
    public int getListSize() {
        return balanceDetails.size();
    }

    /**
     * 根据下标得到用户
     * 
     * @param index
     */
    public BalanceDetails getContactItemByIndex(int index) {
        return balanceDetails.get(index);
    }

    @Override
    public String toString() {
        return "PureseListTime [time=" + time + ", balanceDetails=" + balanceDetails + "]";
    }
}

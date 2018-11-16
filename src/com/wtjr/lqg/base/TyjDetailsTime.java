package com.wtjr.lqg.base;

import java.util.List;

/**
 * 时间
 * 
 * @author dell
 * 
 */
public class TyjDetailsTime {
    /**
     * 一级列表时间
     */
    private String time;

    private List<TyjDetails> tyjDetails;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public List<TyjDetails> getTyjDetails() {
        return tyjDetails;
    }

    public void setTyjDetails(List<TyjDetails> tyjDetails) {
        this.tyjDetails = tyjDetails;
    }

    /**
     * 获得的总数
     */
    public int getListSize() {
        return tyjDetails.size();
    }

    /**
     * 根据下标得到用户
     * 
     * @param index
     */
    public TyjDetails getContactItemByIndex(int index) {
        return tyjDetails.get(index);
    }

    @Override
    public String toString() {
        return "PureseListTime [time=" + time + ", tyjDetails=" + tyjDetails + "]";
    }
}

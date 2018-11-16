package com.wtjr.lqg.base;

import java.util.List;

/**
 * 时间
 * 
 * @author dell
 * 
 */
public class LqbDetailsTime {
    /**
     * 一级列表时间
     */
    private String time;

    private List<LqbDetails> lqbDetails;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<LqbDetails> getPureseListDetails() {
        return lqbDetails;
    }

    public void setPureseListDetails(List<LqbDetails> lqbDetails) {
        this.lqbDetails = lqbDetails;
    }

    /**
     * 获得的总数
     */
    public int getListSize() {
        return lqbDetails.size();
    }

    /**
     * 根据下标得到用户
     * 
     * @param index
     */
    public LqbDetails getContactItemByIndex(int index) {
        return lqbDetails.get(index);
    }

    @Override
    public String toString() {
        return "PureseListTime [time=" + time + ", lqbDetails=" + lqbDetails + "]";
    }
}

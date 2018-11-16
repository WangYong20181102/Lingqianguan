package com.wtjr.lqg.base;

import java.util.List;

public class BillDetailsTime {
    /**
     * 一级列表时间
     */
    private String time;
    /**
     * 交易记录详情
     */
    private List<BillDetails> billDetails;

    public String getTime() {
	return time;
    }

    public void setTime(String time) {
	this.time = time;
    }

    public List<BillDetails> getBillDetails() {
	return billDetails;
    }

    public void setBillDetails(List<BillDetails> billDetails) {
	this.billDetails = billDetails;
    }

    /**
     * 获得的总数
     */
    public int getListSize() {
	return billDetails.size();
    }

    public void setClear() {
	billDetails.clear();
    }

    /**
     * 根据下标得到用户
     * 
     * @param index
     */
    public BillDetails getContactItemByIndex(int index) {
	return billDetails.get(index);
    }
}

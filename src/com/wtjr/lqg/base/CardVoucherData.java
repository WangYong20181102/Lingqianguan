package com.wtjr.lqg.base;

/**
 * Created by WangYong on 2017/11/3.
 */

public class CardVoucherData {
    /**
     * 卡券id
     */
    public int cardId;
    /**
     * 使用时间
     */
    public long useTime;
    /**
     * 卡券发放时间
     */
    public long sendTime;
    /**
     * 状态
     */
    public int status;
    /**
     * 提单订单号
     */
    public int withdrawId;
    /**
     * 有效期截止时间
     */
    public long validTime;
    /**
     * 活动id
     */
    public int activityId;
    /**
     * 卡券类型
     */
    public int type;
    /**
     * 价值
     */
    public String value;
    /**
     * 信息1
     */
    public String remark1;
    /**
     * 信息2
     */
    public String remark2;
    /**
     * 是否过期（1即将过期、2不过期）
     */
    public String isSoonExpired;
}

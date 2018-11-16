package com.wtjr.lqg.base;

/**
 * Created by WangYong on 2017/11/20.
 */

public class BorrowingListData {
    /**
     * 出借人
     */
    public String borrowerName;
    /**
     * 匹配借款
     */
    public String investMoney;
    /**
     * 匹配时间
     */
    public long investTime;
    /**
     * 状态:0.审核失败  1.待审核 2.待复核 3.预发布 4.募集中 募集完成(5.待放款 6.还款中 7.已完成 )
     */
    public int investStatus;
    /**
     *标状态,1 : 已添加未审核,2 : 已审核未发布 , 3: 已发布未开始 ,4 : 已开始未满标 ,5 : 已满标未复审(待复审) , 6: 已复审（还款中） , 7 : 还款中, 8 : 已还完
     */
    public int status;
    public String investId;

}

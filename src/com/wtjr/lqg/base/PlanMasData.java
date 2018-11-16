package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * Created by WangYong on 2017/11/21.
 * 计划产品
 */

public class PlanMasData implements Serializable {

    /**
     * 计划id
     */
    public int planId;
    /**
     * 计划编号
     */
    public String planCode;
    /**
     * 计划名称
     */
    public String planName;
    /**
     * 产品编号
     */
    public String productCode;
    /**
     * 状态:0.审核失败  1.待审核 2.待复核 3.预发布 4.募集中 募集完成(5.待放款 6.还款中 7.已完成 )
     */
    public int status;
    /**
     * 预募集总额
     */
    public double preCollectAmount;
    /**
     * 实际募集
     */
    public double factCollectAmount;
    /**
     * 募集期
     */
    public int collectPeriod;
    /**
     * 投资人数
     */
    public int investUserCount;
    /**
     * 创建时间
     */
    public long createTime;
    /**
     * 初审时间
     */
    public long firstAuditTime;
    /**
     * 复审时间
     */
    public long secondAuditTime;
    /**
     * 更新时间
     */
    public long updateTime;
    /**
     * 发布时间
     */
    public long publishTime;
    /**
     * 集满时间
     */
    public long fullTime;
    /**
     * 基础利率
     */
    public double baseRate;
    /**
     * 最高利率
     */
    public double maxRate;
    /**
     * 递增利率
     */
    public double increaseRate;
    /**
     * 是否加息
     */
    public int isAddRate;
    /**
     * 加息利率
     */
    public double addRate;
    /**
     * 加息天数
     */
    public int addRateDays;
    /**
     * 单位周期
     */
    public int unitCycle;
    /**
     * 周期数
     */
    public int intPeriod;
    /**
     * 投资天数
     */
    public int intPeriodDays;
    /**
     * 起投金额
     */
    public long investMin;
    /**
     * 投资上限
     */
    public long investMax;
    /**
     * 计划说明
     */
    public String planRemark;
    /**
     * 是否锁定
     */
    public int isLock;
    /**
     * 锁定期限
     */
    public int lockDays;
    /**
     * 锁定期限单位（D,M,Y）
     */
    public String lockDaysUnits;
    /**
     * 审核说明
     */
    public String auditRemark;
    /**
     * 协议ID
     */
    public int protocolId;
    /**
     * 数据版本号
     */
    public int pvcode;
    /**
     * 匹配状态
     */
    public int matchStatus;

}

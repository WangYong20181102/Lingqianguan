package com.wtjr.lqg.base;

import java.io.Serializable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 投资列表
 *
 * @author JoLie
 */
@Table(name = "Investment")
public class Investment implements Serializable {
    private static final long serialVersionUID = -3025233114281745809L;
    /**
     * isId = true 是主键，autoGen = false不自增
     */
    @Column(name = "uId", isId = true, autoGen = false)
    public int bId;
    /**
     * 投资ID
     */
    @Column(name = "investId")
    public String investId;
    /**
     * 标名称
     */
    @Column(name = "bName")
    public String bName;
    /**
     * 抢标时间
     */
    @Column(name = "bCountdown")
    public long bCountdown;
    /**
     * 标状态,1 : 已添加未审核,2 : 已审核未发布 , 3: 已发布未开始 ,4 : 已开始未满标 ,5 : 已满标未复审 , 6 : 已复审 ,
     * 7 : 还款 , 8 : 结束 (1~3是倒计时，4是投标中，5~6是满标，7是还款中，8是已还清)
     */
    @Column(name = "bStates")
    public int bStates;
    /**
     * 状态:0.审核失败  1.待审核 2.待复核 3.预发布 4.募集中 募集完成(5.待放款 6.还款中 7.已完成 )',
     */
    @Column(name = "planStatus")
    public int planStatus;
    /**
     * 借款金额
     */
    @Column(name = "bLoanMoney")
    public double bLoanMoney;
    /**
     * 利率
     */
    @Column(name = "bYearRate")
    public Double bYearRate;
    /**
     * 0为定期标，1新手标 2 周周升
     */
    @Column(name = "bType")
    public int bType;

    /**
     * 倒计时剩余时间(秒)
     */
    public int timer;
    /**
     * 标已投金额
     */
    @Column(name = "bInvestedMoney")
    public double bInvestedMoney;
    /**
     * 用户投了多少钱
     */
    @Column(name = "investMoney")
    public double investMoney;
    /**
     * 借款期限
     */
    @Column(name = "bLoanDays")
    public int bLoanDays;
    /**
     * 以还款天数
     */
    @Column(name = "bRepaymentDay")
    public int bRepaymentDay;
    /**
     * 倒计时还款天数
     */
    @Column(name = "bCountDownDay")
    public int bCountDownDay;
    /**
     * 标显示时间
     */
    @Column(name = "bReleaseTime")
    public long bReleaseTime;
    /**
     * 最小投资金额
     */
    @Column(name = "bMiniMoney")
    public double bMiniMoney;
    /**
     * 最高投资金额
     */
    @Column(name = "bHigMoney")
    public int bHigMoney;
    /**
     * 到期还款日
     */
    public long computeDate;
    /**
     * 待获收益
     */
    public double incomeMoney;
    /**
     * 投资时间
     */
    public long investTime;
    /**
     * 退出方式
     */
    public String quitWay;
    /**
     * 收益方式
     */
    public String profitWay;
    /**
     * 复核时间
     */
    public long bReviewTime;
    /**
     * 产品编号
     */
    public String productCode;
    /**
     * 预计募集总数
     */
    public double preCollectAmount;
    /**
     * 实际募集总数
     */
    public double factCollectAmount;
    /**
     * 当前利率
     */
    public double currentRate;
    /**
     * 计划名称
     */
    public String planName;
    /**
     * 是否加息
     */
    public String isAddRate;
    /**
     * 锁定期
     */
    public String lockDays;
    /**
     * 锁定状态（1.锁定中 0.没锁定）
     */
    public int isLock;
    /**
     * 最高利率
     */
    public String maxRate;
    /**
     * 基础利率
     */
    public String baseRate;
    /**
     * 计划编号
     */
    public String planCode;
    /**
     * 发标时间
     */
    public String releaseTime;

    /**
     * 投资状态  0.失效 1.有效 ，2申请退出中
     */
    public String investStatus;

    /**
     * 递增利率
     */
    public String increaseRate;
    /**
     * 加息利率
     */
    public String addRate;
    /**
     * 加息时长
     */
    public String addRateDays;
    /**
     * 计息日期
     */
    public long startDate;
    /**
     * 已获收益
     */
    public String profit;
    public String bLoanPurpose;

    public int bBorrowerId;

    public long bCreateTime;

    public String bExamineReview;

    public int bRepaymentWay;

    public int bRepaymentMoney;

    public int bValidDay;

    public long bFullTime;

    public String bReviewUser;

    public String bIp;

    public int bReviewType;

    public String bExamineUser;

    public String bResouce;

    public int bWhetherTop;

    public long bExamineTime;

    public String bControlMsg;

    public String bReviewRemark;

    public int bExamineRemark;

    @Override
    public String toString() {
        return "Investment [bName=" + bName + ", bStates=" + bStates + "]";
    }


}
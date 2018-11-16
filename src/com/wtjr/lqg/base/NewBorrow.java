package com.wtjr.lqg.base;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by wangxu on 2017/8/11.
 * 新手标
 */

@Table(name = "NewBorrow")
public class NewBorrow implements Serializable {
    /**
     * 标ID， isId = true 是主键，autoGen = false不自增
     */
    @Column(name = "bId", isId = true, autoGen = false)
    public int bId;

    /**
     * 新手标标名
     */
    @Column(name = "bName")
    public String bName;

    public long bCreateTime;

    public long bFullTime;

    public long bReleaseTime;
    /**
     * 抢标时间
     */
    @Column(name = "bCountdown")
    public long bCountdown;

    public int bWhetherTop;
    /**
     * 标状态,1 : 已添加未审核,2 : 已审核未发布 , 3: 已发布未开始 ,4 : 已开始未满标 ,5 : 已满标未复审 , 6 : 已复审 ,
     * 7 : 还款 , 8 : 结束 (1~3是倒计时，4是投标中，5~6是满标，7是还款中，8是已还清)
     */
    @Column(name = "bStates")
    public int bStates;
    /**
     * 倒计时剩余时间(秒)
     */
    @Column(name = "timer")
    public int timer;

    public int bBorrowerId;

    public String bResouce;

    public String bExamineUser;

    public long bExamineTime;

    public int bExamineRemark;
    public int bReviewType;
    @Column(name = "investId")
    public String investId;

    /**
     * 借款金额
     */
    @Column(name = "bLoanMoney")
    public double bLoanMoney;
    @Column(name = "bLoanDays")
    public int bLoanDays;
    public int bRepaymentMoney;
    @Column(name = "bMiniMoney")
    public double bMiniMoney;
    @Column(name = "bHigMoney")
    public double bHigMoney;
    /**
     * 利率
     */
    @Column(name = "bYearRate")
    public Double bYearRate;
    public String bReviewUser;
    public long bReviewTime;
    public int bValidDay;
    public String bIp;
    public String bLoanPurpose;
    public String bReviewRemark;
    public String bExamineReview;
    public String bControlMsg;
    public int bRepaymentWay;
    public int bVCode;
    /**
     * 标已投金额
     */
    @Column(name = "bInvestedMoney")
    public double bInvestedMoney;
    public int bIsWithdraw;
    public int bType;

    public void setbId(int bId) {
        this.bId = bId;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public void setbCreateTime(long bCreateTime) {
        this.bCreateTime = bCreateTime;
    }

    public void setbFullTime(long bFullTime) {
        this.bFullTime = bFullTime;
    }

    public void setbReleaseTime(long bReleaseTime) {
        this.bReleaseTime = bReleaseTime;
    }

    public void setbCountdown(long bCountdown) {
        this.bCountdown = bCountdown;
    }

    public void setbWhetherTop(int bWhetherTop) {
        this.bWhetherTop = bWhetherTop;
    }

    public void setbStates(int bStates) {
        this.bStates = bStates;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public void setbBorrowerId(int bBorrowerId) {
        this.bBorrowerId = bBorrowerId;
    }

    public void setbResouce(String bResouce) {
        this.bResouce = bResouce;
    }

    public void setbExamineUser(String bExamineUser) {
        this.bExamineUser = bExamineUser;
    }

    public void setbExamineTime(long bExamineTime) {
        this.bExamineTime = bExamineTime;
    }

    public void setbExamineRemark(int bExamineRemark) {
        this.bExamineRemark = bExamineRemark;
    }

    public void setbReviewType(int bReviewType) {
        this.bReviewType = bReviewType;
    }

    public void setbLoanMoney(double bLoanMoney) {
        this.bLoanMoney = bLoanMoney;
    }

    public void setbLoanDays(int bLoanDays) {
        this.bLoanDays = bLoanDays;
    }

    public void setbRepaymentMoney(int bRepaymentMoney) {
        this.bRepaymentMoney = bRepaymentMoney;
    }

    public void setbMiniMoney(double bMiniMoney) {
        this.bMiniMoney = bMiniMoney;
    }

    public void setbHigMoney(int bHigMoney) {
        this.bHigMoney = bHigMoney;
    }

    public void setbYearRate(Double bYearRate) {
        this.bYearRate = bYearRate;
    }

    public void setbReviewUser(String bReviewUser) {
        this.bReviewUser = bReviewUser;
    }

    public void setbReviewTime(long bReviewTime) {
        this.bReviewTime = bReviewTime;
    }

    public void setbValidDay(int bValidDay) {
        this.bValidDay = bValidDay;
    }

    public void setbIp(String bIp) {
        this.bIp = bIp;
    }

    public void setbLoanPurpose(String bLoanPurpose) {
        this.bLoanPurpose = bLoanPurpose;
    }

    public void setbReviewRemark(String bReviewRemark) {
        this.bReviewRemark = bReviewRemark;
    }

    public void setbExamineReview(String bExamineReview) {
        this.bExamineReview = bExamineReview;
    }

    public void setbControlMsg(String bControlMsg) {
        this.bControlMsg = bControlMsg;
    }

    public void setbRepaymentWay(int bRepaymentWay) {
        this.bRepaymentWay = bRepaymentWay;
    }

    public void setbVCode(int bVCode) {
        this.bVCode = bVCode;
    }

    public void setbInvestedMoney(double bInvestedMoney) {
        this.bInvestedMoney = bInvestedMoney;
    }

    public void setbIsWithdraw(int bIsWithdraw) {
        this.bIsWithdraw = bIsWithdraw;
    }

    public void setbType(int bType) {
        this.bType = bType;
    }
    public void setInvestId(String investId) {
        this.investId = investId;
    }
    @Override
    public String toString() {
        return "NewBorrow{" +
                "bName='" + bName + '\'' +
                ", bId=" + bId +
                ", bCreateTime=" + bCreateTime +
                ", bFullTime=" + bFullTime +
                ", bReleaseTime=" + bReleaseTime +
                ", bCountdown=" + bCountdown +
                ", bWhetherTop=" + bWhetherTop +
                ", bStates=" + bStates +
                ", timer=" + timer +
                ", bBorrowerId=" + bBorrowerId +
                ", bResouce='" + bResouce + '\'' +
                ", bExamineUser='" + bExamineUser + '\'' +
                ", bExamineTime=" + bExamineTime +
                ", bExamineRemark=" + bExamineRemark +
                ", bReviewType=" + bReviewType +
                ", bLoanMoney=" + bLoanMoney +
                ", bLoanDays=" + bLoanDays +
                ", bRepaymentMoney=" + bRepaymentMoney +
                ", bMiniMoney=" + bMiniMoney +
                ", bHigMoney=" + bHigMoney +
                ", bYearRate=" + bYearRate +
                ", bReviewUser='" + bReviewUser + '\'' +
                ", bReviewTime=" + bReviewTime +
                ", bValidDay=" + bValidDay +
                ", bIp='" + bIp + '\'' +
                ", bLoanPurpose='" + bLoanPurpose + '\'' +
                ", bReviewRemark='" + bReviewRemark + '\'' +
                ", bExamineReview='" + bExamineReview + '\'' +
                ", bControlMsg='" + bControlMsg + '\'' +
                ", bRepaymentWay=" + bRepaymentWay +
                ", bVCode=" + bVCode +
                ", bInvestedMoney=" + bInvestedMoney +
                ", bIsWithdraw=" + bIsWithdraw +
                ", bType=" + bType +
                '}';
    }

}

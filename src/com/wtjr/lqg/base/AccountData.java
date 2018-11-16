package com.wtjr.lqg.base;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

@Table(name = "AccountData")
public class AccountData implements Serializable {
    private static final long serialVersionUID = 3481485393088189414L;
    /**
     * isId = true 是主键，autoGen = false不自增
     */
    @Column(name = "uId", isId = true, autoGen = false)
    public String uId;
    /**
     * 总资产
     */
    @Column(name = "total")
    public double total;
    /**
     * 累计收益
     */
    @Column(name = "total_income")
    public double total_income;
    /**
     * 用户手机号
     */
    @Column(name = "phone")
    public String phone;
    /**
     * 用户邀请码
     */
    @Column(name = "invitation")
    public String invitation;
    /**
     * 用户分享二维码
     */
    @Column(name = "code")
    public String code;
    /**
     * 昵称
     */
    @Column(name = "nickName")
    public String nickName;
    /**
     * 是否有未读消息  “0”无  “1” 有
     */
    @Column(name = "isMsgRead")
    public String isMsgRead;
    /**
     * 是否有邀请有奖  “0”无  “1” 有
     */
    @Column(name = "isInviteRead")
    public String isInviteRead;
    /**
     * 首页顶部图片链接
     */
    @Column(name = "mainPage_topImageUrl")
    public String mainPage_topImageUrl;
    /**
     * 投资总额
     */
    @Column(name = "investment")
    public double investment;
    /**
     * 零钱宝
     */
    @Column(name = "lqb")
    public double lqb;
    /**
     * 可用余额
     */
    @Column(name = "available_money")
    public double available_money;
    /**
     * 新手标
     */
    @Column(name = "xinshouInvest")
    public double xinshouInvest;
    /**
     * 周周升
     */
    @Column(name = "lcjhInvest")
    public double lcjhInvest;
    /**
     * 投资待获收益总额
     */
    @Column(name = "investment_income")
    public double investment_income;
    /**
     * 是否有未读活动   “0”无  “1” 有
     */
    @Column(name = "isActRead")
    public String isActRead;
    /**
     * 七日收益字符串(不参与解析)
     */
    @Column(name = "sevenDayEarnings")
    public String sevenDayEarnings;
    /**
     * 系统时间
     */
    @Column(name = "sysDate")
    public long sysDate;

    /**
     * （七日收益）该字段不参与存储，集合无法保存，不用给@Column
     * 所以用sevenDayEarnings来保存
     */
    public List<SevenDayEarnings> recent_income;

    /**
     * 未使用体验金
     */
    @Column(name = "usableExpMoney")
    public double usableExpMoney;

    /**
     * 可一键转入零钱宝的可用余额
     */
    @Column(name = "availiableMoneyFastIntoLqb")
    public double availiableMoneyFastIntoLqb;
    /**
     * 首页下面标的type 1 是新手标 2 是定期标
     */
    @Column(name = "borrowType")
    public int borrowType;
    /**
     * 加入人数
     */
    @Column(name = "borrowNum")
    public int borrowNum;
    /**
     * 首页下面标的内容
     */
    @Column(name = "planMas")
    public String planMas;

    /**
     * 头像URL
     */
    @Column(name = "headPortraitUrl")
    public String headPortraitUrl;

    /**
     * 银行卡号
     */
    @Column(name = "bankNum")
    public String bankNum;

    /**
     * 银行名称
     */
    @Column(name = "bankName")
    public String bankName;

    /**
     * 总预计明日收益
     */
    @Column(name = "tomorrowInterest")
    public double tomorrowInterest;
    /**
     * 预计明日收益中的投资收益
     */
    @Column(name = "borrowIncome")
    public double borrowIncome;

    /**
     * 预计明日收益中的零钱宝收益
     */
    @Column(name = "lqbIncome")
    public double lqbIncome;

    /**
     * 预计明日收益中的体验金收益
     */
    @Column(name = "experienceIncome")
    public double experienceIncome;

    /**
     * 分享内容
     *
     * @return
     */
    @Column(name = "activeShareContent")
    public String activeShareContent;
    /**
     * 分享标题
     *
     * @return
     */
    @Column(name = "activeShareTitle")
    public String activeShareTitle;
    /**
     * 分享图片
     *
     * @return
     */
    @Column(name = "activeShareImage")
    public String activeShareImage;

    /**
     * 新手活动
     */
    @Column(name = "noviceUrl")
    public String noviceUrl;
    /**
     * 日常活动
     */
    @Column(name = "dailyUrl")
    public String dailyUrl;
    /**
     * 邀请有礼
     */
    @Column(name = "InvitationUrl")
    public String InvitationUrl;
    /**
     * 是否显示首页邀请悬浮按钮（1显示   0不显示）
     */
    @Column(name = "isInvitationActive")
    public String isInvitationActive;
    /**
     * 零钱宝利率最大值
     */
    @Column(name = "maxLqbRate")
    public String maxLqbRate;
    /**
     * 零钱宝利率开始值
     */
    @Column(name = "minLqbRate")
    public String minLqbRate;
    /**
     * 是否开启预约
     */
    public String bookDialog;
    public String isBook;
    public String tyj;
    public String content;

    @Override
    public String toString() {
        return "AccountData{" +
                "uId='" + uId + '\'' +
                ", total=" + total +
                ", total_income=" + total_income +
                ", phone='" + phone + '\'' +
                ", invitation='" + invitation + '\'' +
                ", code='" + code + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isMsgRead='" + isMsgRead + '\'' +
                ", isInviteRead='" + isInviteRead + '\'' +
                ", mainPage_topImageUrl='" + mainPage_topImageUrl + '\'' +
                ", investment=" + investment +
                ", lqb=" + lqb +
                ", available_money=" + available_money +
                ", xinshouInvest=" + xinshouInvest +
                ", investment_income=" + investment_income +
                ", isActRead='" + isActRead + '\'' +
                ", sevenDayEarnings='" + sevenDayEarnings + '\'' +
                ", sysDate=" + sysDate +
                ", recent_income=" + recent_income +
                ", usableExpMoney=" + usableExpMoney +
                ", headPortraitUrl='" + headPortraitUrl + '\'' +
                ", bankNum='" + bankNum + '\'' +
                ", bankName='" + bankName + '\'' +
                ", borrowIncome=" + borrowIncome +
                ", lqbIncome=" + lqbIncome +
                ", experienceIncome=" + experienceIncome +
                ", activeShareContent='" + activeShareContent + '\'' +
                ", activeShareTitle='" + activeShareTitle + '\'' +
                ", activeShareImage='" + activeShareImage + '\'' +
                ", noviceUrl='" + noviceUrl + '\'' +
                ", dailyUrl='" + dailyUrl + '\'' +
                ", InvitationUrl='" + InvitationUrl + '\'' +
                ", isInvitationActive='" + isInvitationActive + '\'' +
                ", maxLqbRate='" + maxLqbRate + '\'' +
                ", minLqbRate='" + minLqbRate + '\'' +
                ", maxInvestRate='" + maxInvestRate + '\'' +
                ", minInvestRate='" + minInvestRate + '\'' +
                '}';
    }

    /**
     * 投资最大利率
     */
    @Column(name = "maxInvestRate")
    public String maxInvestRate;
    /**
     * 投资最小利率
     */
    @Column(name = "minInvestRate")
    public String minInvestRate;
}
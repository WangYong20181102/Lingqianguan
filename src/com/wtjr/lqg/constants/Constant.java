package com.wtjr.lqg.constants;

import android.os.Environment;

/**
 * 常量
 */
public class Constant {
    /**
     * 渠道
     */
    public static String CHANNEL = "official";

    /**
     * 是否关闭日志，打包App时为：false
     */
    public static boolean isLog = false;
    /**
     * 是否可以切换接口的请求地址
     */
    public static boolean isChangeRequestAddress = false;
    /**
     * 是否保存000异常错误日志到手机内存(当‘isLog = true’这个变量才会生效)
     */
    public static boolean isSaveErrorLog = false;

    /**
     * 头像
     */
    public static int SELECT = -1;
    public static boolean isSelect = false;
    public static boolean isEnableClick = false;

    public static final String TEST1 = "http://192.168.1.86:8080";
    public static final String TEST2 = "http://192.168.1.123:8080";
    public static final String TEST3 = "http://192.168.1.37:8080";
    public static final String TEST4 = "http://192.168.1.239:80";
    public static final String TEST5 = "http://192.168.1.25:8080";
    public static final String TEST7 = "http://120.77.253.142:9908";
    public static final String TEST8 = "http://192.168.1.86:8080";
    public static final String TEST9 = "http://192.168.1.64:8080";
    public static final String TEST10 = "http://192.168.1.141:8080";
    public static final String TEST11 = "http://192.168.1.24:8080";

    public static final String TEST6 = "https://test.lqgapp.com";//https仿真环境

    public static final String HTTPS_PRODUCTION = "https://manager.lqgapp.com";//https生产

    public static final String TEST = "http://192.168.1.129:9908";

//    https://manager.lqgapp.com/updates_packages/20170913/lingqianguan_v3.5.apk    //官网下载地址
//    http://manager.lqgapp.com:8080/updates_packages/20170720/lingqianguan_v3.2.apk
//    http://down.lqgapp.com/download/lingqianguan_v3.2.apk         //备用地址
    /**
     * apk下载地址
     */
    public static final String DOWNLOAD_ADDRESS = "https://manager.lqgapp.com";
    /**
     * 当前链接的服务器地址
     */
    public static String CURRENT_ADDRESS = HTTPS_PRODUCTION;
    /**
     * 地址拼接
     */
//    public static String PJ_ADDRESS = "/LQGWeb";
    public static String PJ_ADDRESS = "/LQG";
    /**
     * 服务器图片地址
     */
    public static String IMAGE_ADDRESS = CURRENT_ADDRESS;
    /**
     * 当前接口地址
     */
    public static String INTERFACE_ADDRESS = CURRENT_ADDRESS + PJ_ADDRESS;
    /**
     * 启动页活动图片
     */
    public static String LAUNCH_IMAGEURL = INTERFACE_ADDRESS + "/homePage/startPage";
    /**
     * 注册请求短信验证码
     */
    public static String USERINFO_SENDVERIFYCODEREGISTER = INTERFACE_ADDRESS + "/userInfo/sendVerifyCodeRegister";
    /**
     * 请求短信验证码
     */
    public static String USERINFO_SENDVERIFYCODE = INTERFACE_ADDRESS + "/userInfo/sendVerifyCode";
    /**
     * 注册验证短信验证
     */
    public static String USERINFO_CHECKVERIFYCODE = INTERFACE_ADDRESS + "/userInfo/checkVerifyCode";
    /**
     * 更换手机号码
     */
    public static String USERINFO_CHANGELOGINPHONE = INTERFACE_ADDRESS + "/userInfo/changeLoginPhone";
    /**
     * 注册提交密码
     */
    public static String USERINFO_REGISTER = INTERFACE_ADDRESS + "/userInfo/register";
    /**
     * 修改登录密码
     */
    public static String USERINFO_FINDPASSWORDBACK = INTERFACE_ADDRESS + "/userInfo/findPasswordBack";
    /**
     * 登录
     */
    public static String USERINFO_USERLOGIN = INTERFACE_ADDRESS + "/userInfo/userLogin";
    /**
     * 活动消息邀请有奖的小红点
     */
    public static String REFRESH_MYREDDOT = INTERFACE_ADDRESS + "/accessAppInterface/refreshMyRedDot";
    /**
     * 投资页
     */
    public static String INVESTMENT_REFRESHINVESTMENT = INTERFACE_ADDRESS + "/investment/refreshInvestment";
    /**
     * 首页刷新数据
     */
    public static String ACCESSAPPINTERFACE_INDEXREFRESH = INTERFACE_ADDRESS + "/accessAppInterface/indexrefresh";
    /**
     * 风控信息
     */
    public static String INVESTMENT_VIEWRISKCONTROLINFORMATION = INTERFACE_ADDRESS + "/investment/viewRiskControlInformation";
    /**
     * 风控信息(匹配标)
     */
    public static String LQG_QUERYMATCHBORROWRISKINFO = INTERFACE_ADDRESS + "/lqb/queryMatchBorrowRiskInfo";
    /**
     * 昵称
     */
    public static String SETTINGUP_SETNICKNAME = INTERFACE_ADDRESS + "/settingUp/setNickname";
    /**
     * 头像
     */
    public static String SETTINGUP_SETHEADPORTRAIT = INTERFACE_ADDRESS + "/settingUp/setHeadPortrait";
    /**
     * 体验金转入零钱宝
     */
    public static String LQB_EXPMONEYINTOLQB = INTERFACE_ADDRESS + "/lqb/expMoneyIntoLqb";
    /**
     * 可用余额转入零钱宝
     */
    public static String LQB_AVAILIABLEMONEYINTOLQB = INTERFACE_ADDRESS + "/lqb/availiableMoneyIntoLqb";
    /**
     * 一键转入
     */
    public static String LQB_AVAILIABLEMONEYFASTINTOLQB = INTERFACE_ADDRESS + "/lqb/availiableMoneyFastIntoLqb";
    /**
     * 转出
     */
    public static String LQB_LQBROLLOUT = INTERFACE_ADDRESS + "/lqb/lqbRollOut";
    /**
     * 通过手机验证码方式重设密码
     */
    public static String USERINFO_RESETPASSWORD = INTERFACE_ADDRESS + "/userInfo/resetPassword";
    /**
     * 通过登录密码方式重设密码
     */
    public static String USERINFO_EDITUSERPASSWORD = INTERFACE_ADDRESS + "/userInfo/editUserPassword";
    /**
     * 零钱宝的记录信息
     */
    public static String LQB_QUERYLQBOPERATIONINFO = INTERFACE_ADDRESS + "/lqb/queryLqbOperationInfo";
    /**
     * 投资记录
     */
    public static String INVESTMENT_INVESTMENTINFO = INTERFACE_ADDRESS + "/investment/2_2/investmentInfo";
    /**
     * 周周升投资记录
     */
    public static String PLANINVEST_INVESTMENTINFO = INTERFACE_ADDRESS + "/planInvest/investmentInfo";
    /**
     * 投资记录(匹配标)
     */
    public static String LQB_QUERYMATCHBORROWRECORD = INTERFACE_ADDRESS + "/lqb/queryMatchBorrowRecord";
    /**
     * 福利卡券
     */
    public static String LQB_USERCARD = INTERFACE_ADDRESS + "/card/userCard";
    /**
     * 还款计划
     */
    public static String INVESTMENT_REPAYMENTPLAN = INTERFACE_ADDRESS + "/investment/repaymentPlan";
    /**
     * 还款计划(匹配标)
     */
    public static String LQB_QUERYMATCHBORROWPAYBACKPLAN = INTERFACE_ADDRESS + "/lqb/queryMatchBorrowPayBackPlan";
    /**
     * 立即投资
     */
    public static String INVESTMENT_IMMEDIATEINVESTMENT = INTERFACE_ADDRESS + "/investment/immediateInvestment_2_4";
    /**
     * 周周升投资
     */
    public static String PLANINVEST_IMMEDIATEINVESTMENT = INTERFACE_ADDRESS + "/planInvest/immediateInvestment_2_4";
    /**
     * 更换银行卡信息
     */
    public static String SETTINGUP_REPLACEBANKCARD = INTERFACE_ADDRESS + "/settingUp/replaceBankCard";
    /**
     * 设置/修改支付密码
     */
    public static String SETTINGUP_SETPAYMENTCODE = INTERFACE_ADDRESS + "/settingUp/setPaymentCode";
    /**
     * 是否设置过支付密码
     */
    public static String SETTINGUP_ESTIMATEISPAYMENTCODE = INTERFACE_ADDRESS + "/settingUp/estimateIsPaymentCode";

    /**
     * 获取消息推送权限列表
     */
    public static String GETPERMISSION = INTERFACE_ADDRESS + "/accessAppInterface/getPermission";
    /**
     * 设置消息推送权限列表
     */
    public static String SETPERMISSION = INTERFACE_ADDRESS + "/accessAppInterface/setPermissionByUserId";

    /**
     * 支付通道列表接口
     */
    public static String SETPAYCHANNEL = INTERFACE_ADDRESS + "/setPayChannel/load";
    /**
     * 保存修改的支付通道
     */
    public static String SAVEPAYCHANNEL = INTERFACE_ADDRESS + "/setPayChannel/saveUserPayChannel";
    /**
     * 验证旧支付密码是否正确
     */
    public static String SETTINGUP_VERIFICATIONUSEDPAYMENTCODE = INTERFACE_ADDRESS + "/settingUp/verificationUsedPaymentCode";
    /**
     * 我的投资
     */
    public static String HOMEPAGE_MYINVESTMENT = INTERFACE_ADDRESS + "/homePage/myInvestment";
    /**
     * 我的投资新接口
     */
    public static String PLANINVEST_MYINVESTMENT = INTERFACE_ADDRESS + "/planInvest/myInvestment";
    /**
     * 债权转让
     */
    public static String INVERSTMENT_REQUESTQUITPLAN = INTERFACE_ADDRESS + "/planInvest/requestQuitPlan";
    /**
     * 查询借款人
     */
    public static String PLANINVEST_QUERYMYBORROWER = INTERFACE_ADDRESS + "/planInvest/queryMyBorrower";
    /**
     * 账单
     */
    public static String HOMEPAGE_MYBILL = INTERFACE_ADDRESS + "/homePage/myBill";
    /**
     * 账单明细
     */
    public static String HOMEPAGE_MYBILLLIST = INTERFACE_ADDRESS + "/homePage/myBillList";
    /**
     * 月账单
     */
    public static String MONTH_BILL = INTERFACE_ADDRESS + "/homePage/monthBill";
    /**
     * 账单详情
     */
    public static String HOMEPAGE_BILLINGDETAILS = INTERFACE_ADDRESS + "/homePage/billingDetails";
    /**
     * 消息
     */
    public static String SETTINGUP_MSGPAGE = INTERFACE_ADDRESS + "/settingUp/msgpage";
    /**
     * 体验金详情
     */
    public static String HOMEPAGE_EXPERIENCEGOLD = INTERFACE_ADDRESS + "/homePage/experienceGold";
    /**
     * 零钱宝数据
     */
    public static String LQB_QUERYLQBINFO = INTERFACE_ADDRESS + "/lqb/queryLqbInfo";
    /**
     * 可用余额数据
     */
    public static String HOMEPAGE_AVAILABLEBALANCE = INTERFACE_ADDRESS + "/homePage/availableBalance";
    /**
     * 忘记支付密码 获取验证码
     */
    public static String SETTINGUP_FORGETPASSWORDOBTAINVERIFICATIONCODE = INTERFACE_ADDRESS + "/settingUp/forgetPasswordObtainVerificationCode";
    /**
     * 收益详情
     */
    public static String HOMEPAGE_REVENUEDETAILS = INTERFACE_ADDRESS + "/homePage/revenueDetails";
    /**
     * 获取分享零钱罐数据
     */
    public static String SETTINGUP_SHARELQG = INTERFACE_ADDRESS + "/settingUp/shareLqg";
    /**
     * 分享零钱罐成功回调
     */
    public static String SETTINGUP_ADDSHARELQGLOG = INTERFACE_ADDRESS + "/settingUp/addShareLqgLog";
    /**
     * 情人节分享成功回调
     */
    public static String LUCKYSAMBO_VALENTINESHARE = INTERFACE_ADDRESS + "/luckySamBo/valentineShare";
    /**
     * 活动接口(1：新手活动,2：日常活动,3：轮播)
     */
    public static String SETTINGUP_ACTIVITY = INTERFACE_ADDRESS + "/settingUp/activity";
    /**
     * 签到
     */
    public static String H5_SING_IN = INTERFACE_ADDRESS + "/userInfo/h5InitUserSign";

    /**
     * 1充值判断送体验金
     */
    public static String PAY_CHECKCHARGESTATUS = INTERFACE_ADDRESS + "/pay/checkChargeStatus";
    /**
     * 2首次充值请求第三方短信（充值到余额绑卡）
     */
    public static String PAY_FIRSTTIMECHARGEINDEBIT = INTERFACE_ADDRESS + "/pay/firstTimeChargeInDebit";
    /**
     * 2首次充值请求第三方短信（充值到零钱宝绑卡）
     */
    public static String LQB_BANKCARDINTOLQB = INTERFACE_ADDRESS + "/lqb/bankCardIntoLqb";
    /**
     * 3．首次充值验证第三方短信和6.已绑卡短信验证调用的是一个
     */
    public static String PAY_CONFIRMPAYMENT = INTERFACE_ADDRESS + "/pay/confirmPayment";
    /**
     * 充值重新获取验证码
     */
    public static String PAY_SENDPAYMSG = INTERFACE_ADDRESS + "/pay/sendPayMsg";

    /**
     * 5．已经绑卡充值请求第三方短信(充值到余额)
     */
    public static String PAY_BINDEDCHARGEINDEBIT = INTERFACE_ADDRESS + "/pay/bindedChargeInDebit";
    /**
     * 5．已经绑卡充值请求第三方短信(充值到零钱宝)
     */
    public static String LQB_BINDEDBANKCARDINTOLQB = INTERFACE_ADDRESS + "/lqb/bindedBankCardIntoLqb";
    /**
     * 更换银行卡
     */
    public static String LQB_QUERYUSERNAMEANDIDNUM = INTERFACE_ADDRESS + "/userInfo/queryUserNameAndIdNum";
    /**
     * 7.提现
     */
    public static String PAY_WITHDRAWDEPOSIT = INTERFACE_ADDRESS + "/pay/withdrawDeposit_2_4";

    /**
     * 8.提现获取手续费和最大提现金额
     */
    public static String PAY_CHECKPOUNDAGEANDAVAILABLE = INTERFACE_ADDRESS + "/pay/checkPoundageAndAvailable";

    /**
     * 获取渠道和充值最大金额
     */
    public static String PAY_QUERYMAXCHARGEANDCHANNEL = INTERFACE_ADDRESS + "/pay/queryMaxChargeAndChannel";
    /**
     * 我的银行卡
     */
    public static String PAY_QUERYBANKCARDINFO = INTERFACE_ADDRESS + "/pay/queryBankCardInfo";
    /**
     * 更换银行预留手机号发送短信验证码
     */
    public static String PAY_SENDCHANGEBANKPHONECODE = INTERFACE_ADDRESS + "/pay/sendChangeBankPhoneCode";
    /**
     * 更换银行预留手机号
     */
    public static String PAY_SPAYCHANGEPAYPHONE = INTERFACE_ADDRESS + "/pay/changePayPhone";
    /**
     * 用户反馈
     */
    public static String PAY_MESSAGEFEEDBACK = INTERFACE_ADDRESS + "/settingUp/messageFeedback";

    /**
     * 打开APP(登录并绑卡)
     */
    public static String ACCESSAPPINTERFACE_OPENAPP = INTERFACE_ADDRESS + "/accessAppInterface/openApp";
    /**
     * 打开APP(没登录)
     */
    public static String ACCESSAPPINTERFACE_UNREGISTEREDOPENAPP = INTERFACE_ADDRESS + "/accessAppInterface/unregisteredOpenApp";
    /**
     * 打开APP(登录未绑卡)
     */
    public static String ACCESSAPPINTERFACE_NOTTIEDUPCARDOPENAPP = INTERFACE_ADDRESS + "/accessAppInterface/notTiedUpCardOpenApp";
    /**
     * 提现的联系零妹妹
     */
    public static String AUXILIARY_WEIXIN = INTERFACE_ADDRESS + "/auxiliary/weixin";
    /**
     * 提现获取短信
     */
    public static String PAY_VERIFICATIONCODE = INTERFACE_ADDRESS + "/pay/verificationCode";
    /**
     * 首页弹窗接口
     */
    public static String HOMEPAGE_TOPPOPUPPAGE = INTERFACE_ADDRESS + "/homePage/topPopupPage";
    /**
     * 首页弹窗接口
     */
    public static String HOMEPAGE_TOPPOPUPPAGES = INTERFACE_ADDRESS + "/homePage/topPopupPages";
    /**
     * 零钱宝匹配标接口
     */
    public static String LQB_QUERYLQBMATCHBORROWINFO = INTERFACE_ADDRESS + "/lqb/queryLqbMatchBorrowInfo";
    /**
     * 版本检测
     */
    public static String USERINFO_QUERYLATESTVERSION = INTERFACE_ADDRESS + "/userInfo/queryLatestVersion";
    /**
     * 应用市场下载
     */
    public static String LQB_ANDROIDMARKET = INTERFACE_ADDRESS + "/hfivepage/androidmarket";
    /**
     * 零钱宝借款协议
     */
    public static String LQB_QUERYLOANPROTOCOL = INTERFACE_ADDRESS + "/lqb/queryLoanProtocol";
    /**
     * 周周升项目详情借款协议
     */
    public static String WEEKWEEKUP_XIYI = INTERFACE_ADDRESS + "/pages/zhouzhoushenxieyi.html";
    /**
     * 零钱宝转让协议
     */
    public static String LQB_QUERYTRANSFERPROTOCOL = INTERFACE_ADDRESS + "/lqb/queryTransferProtocol";
    /**
     * 投资服务协议
     */
    public static String INVESTMENT_QUERYIFADADAUSERTEMPLATE = INTERFACE_ADDRESS + "/investment/queryIfadadaUserTemplate";
    /**
     * 周周升借款人服务协议
     */
    public static String INVESTMENT_QUERYZZSUSERTEMPLATE = INTERFACE_ADDRESS + "/investment/queryZzsUserTemplate";
    /**
     * 周周升协议
     */
    public static String INVESTMENT_QUERYIFADADAZZSTEMPLATE = INTERFACE_ADDRESS + "/investment/queryIfadadazzsTemplate";
    /**
     * 周周升预约
     */
    public static String PLANBOOK_STARTBOOK = INTERFACE_ADDRESS + "/planBook/startBook";
    /**
     * 周周升协议
     */
    public static String PLANBOOK_TOPROTOCOL = INTERFACE_ADDRESS + "/pages/manguan.html";
    /**
     * 周周升产品说明
     */
    public static String PLANBOOK_ZHOUZHOUSHEN = INTERFACE_ADDRESS + "/pages/zhouzhoushen.html";
    /**
     * 周周升风险提示告知书
     */
    public static String PLANBOOK_FENGXIAN = INTERFACE_ADDRESS + "/pages/fengxian.html";
    /**
     * 周周升项目投资列表
     */
    public static String PLANPROJECT_QUERYBORROWER = INTERFACE_ADDRESS + "/planProject/queryBorrower";
    /**
     * 周周升借款项目详情
     */
    public static String PLANPROJECT_BORROWDETAIL = INTERFACE_ADDRESS + "/planProject/borrowerDetail";

    // ----------------------------------------------------HTML5-------------------------------------------------------------------
    /**
     * 分享零钱罐
     */
    public static String H5_SHARE_LQG = INTERFACE_ADDRESS + "/hfivepage/invitation";
    /**
     * 充值问题
     */
    public static String H5_RECHARGE = INTERFACE_ADDRESS + "/pages/chongzhi_question/chongzhi_question.html";
    /**
     * 充值限额
     */
    public static String H5_CZXE = INTERFACE_ADDRESS + "/hfivepage/myCard";
    /**
     * 提现问题
     */
    public static String H5_TIXIAN = INTERFACE_ADDRESS + "/pages/tixian_question/tixian_question.html";
    /**
     * 定期标收益计算说明
     */
    public static String H5_DQB_CALC = INTERFACE_ADDRESS + "/pages/dingqibiao.html";
    /**
     * 什么是体验金
     */
    public static String H5_WHATTYJ = INTERFACE_ADDRESS + "/pages/ti.html";
    /**
     * 信息披露
     */
    public static String H5_WE = INTERFACE_ADDRESS + "/pages/index.html";
    /**
     * 媒体报道
     */
    public static String H5_MEDIAL = INTERFACE_ADDRESS + "/hfivepage/goMediaReport";
    /**
     * 公司资质  6
     */
    public static String H5_COMPANY = INTERFACE_ADDRESS + "/hfivepage/goQualification";
    /**
     * 零钱罐公益
     */
    public static String H5_ACCESSAPPINTERFACE_GOLQGWELFARE = INTERFACE_ADDRESS + "/accessAppInterface/goLqgWelfare";
    /**
     * 零钱罐故事
     */
    public static String H5_ACCESSAPPINTERFACE_GOLQGSTORY = INTERFACE_ADDRESS + "/accessAppInterface/goLqgStory";
    /**
     * PV PU 统计
     */
    public static String LQB_STATISTICS = INTERFACE_ADDRESS + "/SubLQGbehaviour/statistics";
    /**
     * 150秒了解零钱罐
     */
    public static String H5_ABOUT_US = INTERFACE_ADDRESS + "/hfivepage/videoLink";
    /**
     * 新手红包
     */
    public static String H5_NOVICE_RED_PACKET = INTERFACE_ADDRESS + "/apph5/new_red_envelopes.html";
    /**
     * 安全保障
     */
    public static String H5_SAFE = INTERFACE_ADDRESS + "/pages/safety_guarantee.html";
    /**
     * 安全保障
     */
    public static String H5_NEWSEVCE = INTERFACE_ADDRESS + "/pages/newsevce.html";
    /**
     * 新手攻略
     */
    public static String H5_NOVICE_STRATEGY = INTERFACE_ADDRESS + "/apph5/new_strategy.html";
    /**
     * 风险评测
     */
    public static String H5_CALENDARSACTIVITY_GORISKGRADE = INTERFACE_ADDRESS + "/calendarsActivity/goRiskGrade";
    /**
     * 零钱宝问题（帮助）
     */
    public static String H5_LQB_HELP = INTERFACE_ADDRESS + "/pages/lingqian_question/lingqian_question.html";
    /**
     * 零钱罐服务协议
     */
    public static String H5_LQG_SERVE_AGREEMENT = INTERFACE_ADDRESS + "/pages/service_agreement.html";
    /**
     * 我的客服
     */
    public static String H5_LQG_MYKEFU = INTERFACE_ADDRESS + "/pages/mykefu.html";
    /**
     * 提现帮助
     */
    public static String H5_WITHDRAWAL_HELP = INTERFACE_ADDRESS + "/pages/tixian_question/tixian_question.html";
    /**
     * 关注微信页面
     */
    public static String H5_ATTENTION_WEIXIN = INTERFACE_ADDRESS + "/pages/weixin.html";
    /**
     * 零钱罐微博
     */
    public static final String H5_LQG_WEIBO = "http://weibo.com/u/5822298426";
    /**
     * 呼叫汪星人
     */
    public static final String H5_GOTODOG1 = "lqg://app.com/gotodog1";
    /**
     * 元宵分享
     */
    public static final String H5_GOTOYUANXIAO = "lqg://app.com/gotoyuanxiao";
    /**
     * 我要发狗粮
     */
    public static final String H5_GOTODOG2 = "lqg://app.com/gotodog2";
    // ---------------------------------------------------------------------------------------------------------------------

    // -------------------------------------------------------H5跳转链接-----------------------------------------------
    /**
     * 关于我们中跳转按钮链接
     */
    public static final String H5_WE_GOTO_TOUZI = "lqg://app.com/gototouzi";
    /**
     * 关于我们中跳转按钮链接
     */
    public static final String H5_ATTENTION_WEIXIN_GOTO_OPEN_WEIXIN = "lqg://app.com/gotoweixin/?wx=lqgjinrong";
    /**
     * 新手活动充值100
     */
    public static final String H5_GOTO_RECHARGE100 = "lqg://app.com/gotorecharge100";
    /**
     * 新手活动充值1000
     */
    public static final String H5_GOTO_RECHARGE1000 = "lqg://app.com/gotorecharge1000";
    /**
     * 新手活动分享
     */
    public static final String H5_GOTO_SHARE = "lqg://app.com/gotoshare";
    /**
     * 绑定右上角分享四要素
     */
    public static final String H5_GOTO_QIU_SHARE = "lqg://app.com/gotoqiushare";
    /**
     * 新手活动分享
     */
    public static final String H5_GOTO_TYJ = "lqg://app.com/gotoApp3";
    /**
     * 常见问题点击H5轮播图到播放视频
     */
    public static final String H5_GOTO_VIDEO = "lqg://app.com/gotovideo";
    /**
     * 关注微信公众号
     */
    public static final String H5_GOTO_APP1 = "lqg://app.com/gotoApp1";
    /**
     * 如何关注领体验金
     */
    public static final String H5_GOTO_APP2 = "lqg://app.com/gotoApp2";
    /**
     * 关注微信公众号
     */
    public static String H5_HFIVEPAGE = INTERFACE_ADDRESS + "/hfivepage/toFocusWechatPage";
    /**
     * 如何关注公众号
     */
    public static String H5_INSTRUCTIONS = INTERFACE_ADDRESS + "/hfivepage/instructions";
    /**
     * 邀请好友成功注册去分享零钱罐
     */
    public static final String H5_GOTO_SHARE1 = "lqg://app.com/gotoshare1";
    /**
     * 3位邀请好友单笔投资1000去分享零钱罐
     */
    public static final String H5_GOTO_SHARE2 = "lqg://app.com/gotoshare2";
    /**
     * 5位邀请好友单笔投资1000去分享零钱罐
     */
    public static final String H5_GOTO_SHARE3 = "lqg://app.com/gotoshare3";

    /**
     * 新手活动查看体验金详情
     */
    public static final String H5_GOTO_EXPERIENCEDETAIL = "lqg://app.com/gotoExperienceDetail";
    /**
     * 智齿客服
     */
    public static final String H5_GOTO_ZHICHI = "lqg://app.com/gotozhichi";
    // ---------------------------------------------------------------------------------------------------------------------

    // ----------------------------------------------------广播-------------------------------------------------------------------
    /**
     * 头像变动广播的Action
     */
    public static final String HEADPICTURECHANGE = "wtjr.intent.action.HEADPICTURECHANGE";
    /**
     * 头像变动广播的Action
     */
    public static final String HEADPICTURECHANGE1 = "wtjr.intent.action.HEADPICTURECHANGE1";
    /**
     * 头像变动广播的Action
     */
    public static final String HEADPICTURECHANGE2 = "wtjr.intent.action.HEADPICTURECHANGE2";

    /**
     * 账户数据更新广播的Action
     */
    public static final String UPDATE_ACCOUNTDATA = "Update.AccountData";
    /**
     * 零钱宝数据更新广播的Action
     */
    public static final String UPDATE_LQBDATA = "Update.LqbData";
    /**
     * 账户数据和零钱宝数据更新广播的Action
     */
    public static final String UPDATE_ACCOUNTDATA_LQBDATA = "Update.AccountData.LqbData";
    /**
     * 账户数据、零钱宝数据、投资更新广播的Action
     */
    public static final String UPDATE_ALL = "Update.All";
    /**
     * 我的界面更新首页数据无下拉动作
     */
    public static final String UPDATE_MEDATA = "Update.MeData";

    // --------------------------------------------------------------------------------------------------------------------------

    // --------------------------------------------------文件存储路径-----------------------------------------------------------------
    /**
     * 文件的存储根路径
     */
    public static final String SAVE_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/lingqianguan/";
    /**
     * 协议的存储路径
     */
    public static final String AGREEMENT_PATH = SAVE_FILE_PATH + "agreement/";
    /**
     * apk的存储路径
     */
    public static final String APK_PATH = SAVE_FILE_PATH + "apk/";
    /**
     * 头像图片的存储路径
     */
    public static final String PHOTO_PATH = SAVE_FILE_PATH + "photo/";
    /**
     * 等待处理的头像图片的存储路径(拍照得到的照片)
     */
    public static final String PENDING_PATH = PHOTO_PATH + "pending/";
    // -----------------------------------------------------------------------------------------------------------------------------

    // --------------------------------------------------------String-----------------------------------------------------------
    /**
     * 微信包名
     */
    public static final String WEIXIN_PACKAGE = "com.tencent.mm";
    /**
     * 微信包名
     */
    public static final String WEIXIN_PUBLIC_NUMBER = "com.tencent.mm";
    /**
     * 注册倒计时Key
     */
    public static final String DOWN_TIMER_REGISTER = "down_timer_register";
    /**
     * 设置密码倒计时Key
     */
    public static final String DOWN_TIMER_SET_PASSWORD = "down_timer_set_password";
    /**
     * 转盘活动加密的公钥
     */
    public static final String ZP_ENCRYPT_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCP+NLzUvyB" +
                    "+RLjF9A6scTupXJdvp5aD1VsIgcHXnDZACD66MQJx5XABWo8JciNB1NRQ359xafIeUrlRzux1hiFCM3VO0MKiBEtGAe2J5nW5jJoG6yG7IozGDTEMKQkN65q99ehR" +
                    "+OmourbJaBAMpuIhaD0hyPHGsYvpwc3dykDHwIDAQAB";
    /**
     * 转盘活动加密用到的code
     */
    public static final String ZP_CODE = "KPSL98I";
    /**
     * 要分享的内容
     */
    public static final String shareContent = "自从发现了它，我每天都能赚点钱，每周都能升级，绝了！";
    /**
     * 分享的url
     */
    public static final String shareURL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.wtjr.lqg";
    /**
     * 分享的标题
     */
    public static final String shareTitle = "大家都在抢，就差你了哟~";

    // -----------------------------------------------------------------------------------------------------------------------------

    /**
     * 更改请求地址
     *
     * @param address
     */
    public static void changeRequestAddress(String address) {
        CURRENT_ADDRESS = address;
        IMAGE_ADDRESS = CURRENT_ADDRESS;
        INTERFACE_ADDRESS = CURRENT_ADDRESS + PJ_ADDRESS;
        // ------------------------------------接口-------------------------------------
        LAUNCH_IMAGEURL = INTERFACE_ADDRESS + "/homePage/startPage";
        USERINFO_SENDVERIFYCODE = INTERFACE_ADDRESS + "/userInfo/sendVerifyCode";
        USERINFO_SENDVERIFYCODEREGISTER = INTERFACE_ADDRESS + "/userInfo/sendVerifyCodeRegister";
        USERINFO_CHECKVERIFYCODE = INTERFACE_ADDRESS + "/userInfo/checkVerifyCode";
        USERINFO_CHANGELOGINPHONE = INTERFACE_ADDRESS + "/userInfo/changeLoginPhone";
        USERINFO_REGISTER = INTERFACE_ADDRESS + "/userInfo/register";
        USERINFO_FINDPASSWORDBACK = INTERFACE_ADDRESS + "/userInfo/findPasswordBack";
        USERINFO_USERLOGIN = INTERFACE_ADDRESS + "/userInfo/userLogin";
        REFRESH_MYREDDOT = INTERFACE_ADDRESS + "/accessAppInterface/refreshMyRedDot";
        INVESTMENT_REFRESHINVESTMENT = INTERFACE_ADDRESS + "/investment/refreshInvestment";
        ACCESSAPPINTERFACE_INDEXREFRESH = INTERFACE_ADDRESS + "/accessAppInterface/indexrefresh";
        INVESTMENT_VIEWRISKCONTROLINFORMATION = INTERFACE_ADDRESS + "/investment/viewRiskControlInformation";
        SETTINGUP_SETNICKNAME = INTERFACE_ADDRESS + "/settingUp/setNickname";
        SETPAYCHANNEL = INTERFACE_ADDRESS + "/setPayChannel/load";
        SAVEPAYCHANNEL = INTERFACE_ADDRESS + "/setPayChannel/saveUserPayChannel";
        SETTINGUP_SETHEADPORTRAIT = INTERFACE_ADDRESS + "/settingUp/setHeadPortrait";
        LQB_EXPMONEYINTOLQB = INTERFACE_ADDRESS + "/lqb/expMoneyIntoLqb";
        LQB_AVAILIABLEMONEYINTOLQB = INTERFACE_ADDRESS + "/lqb/availiableMoneyIntoLqb";
        LQB_AVAILIABLEMONEYFASTINTOLQB = INTERFACE_ADDRESS + "/lqb/availiableMoneyFastIntoLqb";
        LQB_LQBROLLOUT = INTERFACE_ADDRESS + "/lqb/lqbRollOut";
        USERINFO_RESETPASSWORD = INTERFACE_ADDRESS + "/userInfo/resetPassword";
        USERINFO_EDITUSERPASSWORD = INTERFACE_ADDRESS + "/userInfo/editUserPassword";
        LQB_QUERYLQBOPERATIONINFO = INTERFACE_ADDRESS + "/lqb/queryLqbOperationInfo";
        INVESTMENT_INVESTMENTINFO = INTERFACE_ADDRESS + "/investment/2_2/investmentInfo";
        PLANINVEST_INVESTMENTINFO = INTERFACE_ADDRESS + "/planInvest/investmentInfo";
        INVESTMENT_REPAYMENTPLAN = INTERFACE_ADDRESS + "/investment/repaymentPlan";
        INVESTMENT_IMMEDIATEINVESTMENT = INTERFACE_ADDRESS + "/investment/immediateInvestment_2_4";
        PLANINVEST_IMMEDIATEINVESTMENT = INTERFACE_ADDRESS + "/planInvest/immediateInvestment_2_4";
        SETTINGUP_REPLACEBANKCARD = INTERFACE_ADDRESS + "/settingUp/replaceBankCard";
        SETTINGUP_SETPAYMENTCODE = INTERFACE_ADDRESS + "/settingUp/setPaymentCode";
        SETTINGUP_ESTIMATEISPAYMENTCODE = INTERFACE_ADDRESS + "/settingUp/estimateIsPaymentCode";
        GETPERMISSION = INTERFACE_ADDRESS + "/accessAppInterface/getPermission";
        SETPERMISSION = INTERFACE_ADDRESS + "/accessAppInterface/setPermissionByUserId";
        SETTINGUP_VERIFICATIONUSEDPAYMENTCODE = INTERFACE_ADDRESS + "/settingUp/verificationUsedPaymentCode";
        HOMEPAGE_MYINVESTMENT = INTERFACE_ADDRESS + "/homePage/myInvestment";
        PLANINVEST_MYINVESTMENT = INTERFACE_ADDRESS + "/planInvest/myInvestment";
        INVERSTMENT_REQUESTQUITPLAN = INTERFACE_ADDRESS + "/planInvest/requestQuitPlan";
        PLANINVEST_QUERYMYBORROWER = INTERFACE_ADDRESS + "/planInvest/queryMyBorrower";
        HOMEPAGE_MYBILL = INTERFACE_ADDRESS + "/homePage/myBill";
        HOMEPAGE_MYBILLLIST = INTERFACE_ADDRESS + "/homePage/myBillList";
        MONTH_BILL = INTERFACE_ADDRESS + "/homePage/monthBill";
        HOMEPAGE_BILLINGDETAILS = INTERFACE_ADDRESS + "/homePage/billingDetails";
        SETTINGUP_MSGPAGE = INTERFACE_ADDRESS + "/settingUp/msgpage";
        HOMEPAGE_EXPERIENCEGOLD = INTERFACE_ADDRESS + "/homePage/experienceGold";
        LQB_QUERYLQBINFO = INTERFACE_ADDRESS + "/lqb/queryLqbInfo";
        HOMEPAGE_AVAILABLEBALANCE = INTERFACE_ADDRESS + "/homePage/availableBalance";
        SETTINGUP_FORGETPASSWORDOBTAINVERIFICATIONCODE = INTERFACE_ADDRESS + "/settingUp/forgetPasswordObtainVerificationCode";
        HOMEPAGE_REVENUEDETAILS = INTERFACE_ADDRESS + "/homePage/revenueDetails";
        SETTINGUP_SHARELQG = INTERFACE_ADDRESS + "/settingUp/shareLqg";
        SETTINGUP_ADDSHARELQGLOG = INTERFACE_ADDRESS + "/settingUp/addShareLqgLog";
        LUCKYSAMBO_VALENTINESHARE = INTERFACE_ADDRESS + "/luckySamBo/valentineShare";
        SETTINGUP_ACTIVITY = INTERFACE_ADDRESS + "/settingUp/activity";
        PAY_CHECKCHARGESTATUS = INTERFACE_ADDRESS + "/pay/checkChargeStatus";
        PAY_FIRSTTIMECHARGEINDEBIT = INTERFACE_ADDRESS + "/pay/firstTimeChargeInDebit";
        LQB_BANKCARDINTOLQB = INTERFACE_ADDRESS + "/lqb/bankCardIntoLqb";
        PAY_CONFIRMPAYMENT = INTERFACE_ADDRESS + "/pay/confirmPayment";
        PAY_SENDPAYMSG = INTERFACE_ADDRESS + "/pay/sendPayMsg";
        PAY_BINDEDCHARGEINDEBIT = INTERFACE_ADDRESS + "/pay/bindedChargeInDebit";
        LQB_BINDEDBANKCARDINTOLQB = INTERFACE_ADDRESS + "/lqb/bindedBankCardIntoLqb";
        LQB_QUERYUSERNAMEANDIDNUM = INTERFACE_ADDRESS + "/userInfo/queryUserNameAndIdNum";
        PAY_WITHDRAWDEPOSIT = INTERFACE_ADDRESS + "/pay/withdrawDeposit_2_4";
        PAY_CHECKPOUNDAGEANDAVAILABLE = INTERFACE_ADDRESS + "/pay/checkPoundageAndAvailable";
        PAY_QUERYMAXCHARGEANDCHANNEL = INTERFACE_ADDRESS + "/pay/queryMaxChargeAndChannel";
        PAY_QUERYBANKCARDINFO = INTERFACE_ADDRESS + "/pay/queryBankCardInfo";
        PAY_SENDCHANGEBANKPHONECODE = INTERFACE_ADDRESS + "/pay/sendChangeBankPhoneCode";
        PAY_SPAYCHANGEPAYPHONE = INTERFACE_ADDRESS + "/pay/changePayPhone";
        PAY_MESSAGEFEEDBACK = INTERFACE_ADDRESS + "/settingUp/messageFeedback";
        ACCESSAPPINTERFACE_OPENAPP = INTERFACE_ADDRESS + "/accessAppInterface/openApp";
        ACCESSAPPINTERFACE_UNREGISTEREDOPENAPP = INTERFACE_ADDRESS + "/accessAppInterface/unregisteredOpenApp";
        ACCESSAPPINTERFACE_NOTTIEDUPCARDOPENAPP = INTERFACE_ADDRESS + "/accessAppInterface/notTiedUpCardOpenApp";
        AUXILIARY_WEIXIN = INTERFACE_ADDRESS + "/auxiliary/weixin";
        PAY_VERIFICATIONCODE = INTERFACE_ADDRESS + "/pay/verificationCode";
        HOMEPAGE_TOPPOPUPPAGE = INTERFACE_ADDRESS + "/homePage/topPopupPage";
        HOMEPAGE_TOPPOPUPPAGES = INTERFACE_ADDRESS + "/homePage/topPopupPages";
        LQB_QUERYLQBMATCHBORROWINFO = INTERFACE_ADDRESS + "/lqb/queryLqbMatchBorrowInfo";
        USERINFO_QUERYLATESTVERSION = INTERFACE_ADDRESS + "/userInfo/queryLatestVersion";
        LQB_ANDROIDMARKET = INTERFACE_ADDRESS + "/hfivepage/androidmarket";
        LQB_QUERYLOANPROTOCOL = INTERFACE_ADDRESS + "/lqb/queryLoanProtocol";
        WEEKWEEKUP_XIYI = INTERFACE_ADDRESS + "/pages/zhouzhoushenxieyi.html";
        LQB_QUERYTRANSFERPROTOCOL = INTERFACE_ADDRESS + "/lqb/queryTransferProtocol";
        INVESTMENT_QUERYIFADADAUSERTEMPLATE = INTERFACE_ADDRESS + "/investment/queryIfadadaUserTemplate";
        INVESTMENT_QUERYZZSUSERTEMPLATE = INTERFACE_ADDRESS + "/investment/queryZzsUserTemplate";
        INVESTMENT_QUERYIFADADAZZSTEMPLATE = INTERFACE_ADDRESS + "/investment/queryIfadadazzsTemplate";
        PLANBOOK_STARTBOOK = INTERFACE_ADDRESS + "/planBook/startBook";
        PLANBOOK_TOPROTOCOL = INTERFACE_ADDRESS + "/pages/manguan.html";
        PLANBOOK_ZHOUZHOUSHEN = INTERFACE_ADDRESS + "/pages/zhouzhoushen.html";
        PLANBOOK_FENGXIAN = INTERFACE_ADDRESS + "/pages/fengxian.html";
        PLANPROJECT_QUERYBORROWER = INTERFACE_ADDRESS + "/planProject/queryBorrower";
        PLANPROJECT_BORROWDETAIL = INTERFACE_ADDRESS + "/planProject/borrowerDetail";
        LQG_QUERYMATCHBORROWRISKINFO = INTERFACE_ADDRESS + "/lqb/queryMatchBorrowRiskInfo";
        LQB_QUERYMATCHBORROWPAYBACKPLAN = INTERFACE_ADDRESS + "/lqb/queryMatchBorrowPayBackPlan";
        LQB_QUERYMATCHBORROWRECORD = INTERFACE_ADDRESS + "/lqb/queryMatchBorrowRecord";
        LQB_USERCARD = INTERFACE_ADDRESS + "/card/userCard";
        // --------------------------------------------------------------------------------

        // ----------------------------------------H5----------------------------------------
        H5_SHARE_LQG = INTERFACE_ADDRESS + "/hfivepage/invitation";
        H5_WE = INTERFACE_ADDRESS + "/pages/index.html";
        H5_SING_IN = INTERFACE_ADDRESS + "/userInfo/h5InitUserSign";
        H5_MEDIAL = INTERFACE_ADDRESS + "/hfivepage/goMediaReport";
        H5_COMPANY = INTERFACE_ADDRESS + "/hfivepage/goQualification";
        H5_ACCESSAPPINTERFACE_GOLQGWELFARE = INTERFACE_ADDRESS + "/accessAppInterface/goLqgWelfare";
        H5_ACCESSAPPINTERFACE_GOLQGSTORY = INTERFACE_ADDRESS + "/accessAppInterface/goLqgStory";
        LQB_STATISTICS = INTERFACE_ADDRESS + "/SubLQGbehaviour/statistics";
        H5_ABOUT_US = INTERFACE_ADDRESS + "/hfivepage/videoLink";
        H5_RECHARGE = INTERFACE_ADDRESS + "/pages/chongzhi_question/chongzhi_question.html";
        H5_CZXE = INTERFACE_ADDRESS + "/hfivepage/myCard";
        H5_TIXIAN = INTERFACE_ADDRESS + "/pages/tixian_question/tixian_question.html";
        H5_DQB_CALC = INTERFACE_ADDRESS + "/pages/dingqibiao.html";
        H5_NOVICE_RED_PACKET = INTERFACE_ADDRESS + "/apph5/new_red_envelopes.html";
        H5_SAFE = INTERFACE_ADDRESS + "/pages/safety_guarantee.html";
        H5_NEWSEVCE = INTERFACE_ADDRESS + "/pages/newsevce.html";
        H5_WHATTYJ = INTERFACE_ADDRESS + "/pages/ti.html";
        H5_NOVICE_STRATEGY = INTERFACE_ADDRESS + "/apph5/new_strategy.html";
        H5_CALENDARSACTIVITY_GORISKGRADE = INTERFACE_ADDRESS + "/calendarsActivity/goRiskGrade";
        H5_LQB_HELP = INTERFACE_ADDRESS + "/pages/lingqian_question/lingqian_question.html";
        H5_LQG_SERVE_AGREEMENT = INTERFACE_ADDRESS + "/pages/service_agreement.html";
        H5_LQG_MYKEFU = INTERFACE_ADDRESS + "/pages/mykefu.html";
        H5_WITHDRAWAL_HELP = INTERFACE_ADDRESS + "/pages/tixian_question/tixian_question.html";
        H5_ATTENTION_WEIXIN = INTERFACE_ADDRESS + "/pages/weixin.html";
        H5_HFIVEPAGE = INTERFACE_ADDRESS + "/hfivepage/toFocusWechatPage";
        H5_INSTRUCTIONS = INTERFACE_ADDRESS + "/hfivepage/instructions";
        // --------------------------------------------------------------------------------
    }
}

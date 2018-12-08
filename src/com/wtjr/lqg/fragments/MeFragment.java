package com.wtjr.lqg.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.AboutLQGActivity;
import com.wtjr.lqg.activities.ActivityWebViewActivity;
import com.wtjr.lqg.activities.AvailableBalanceActivity;
import com.wtjr.lqg.activities.BillDetailsActivity;
import com.wtjr.lqg.activities.EarningsDetailsActivity;
import com.wtjr.lqg.activities.HaveAcountsLoginActivity;
import com.wtjr.lqg.activities.HeadAmendActivity;
import com.wtjr.lqg.activities.LoginActivity;
import com.wtjr.lqg.activities.MessageActivity;
import com.wtjr.lqg.activities.MyBankActivity;
import com.wtjr.lqg.activities.MyInvestmentActivity;
import com.wtjr.lqg.activities.SetingActivity;
import com.wtjr.lqg.activities.TotalAssetsActivity;
import com.wtjr.lqg.activities.TyjDetailsActivity1;
import com.wtjr.lqg.activities.WebViewActivity;
import com.wtjr.lqg.activities.WelfareCardVoucherActivity;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseFragment;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.AnimationShowState;
import com.wtjr.lqg.enums.PayPasswordType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.BankImageUtil;
import com.wtjr.lqg.utils.HeadChangeUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.widget.CircleImageView;
import com.wtjr.lqg.widget.NavigationBarItem;
import com.wtjr.lqg.widget.ObservableScrollView;
import com.wtjr.lqg.widget.UsuallyLayoutItem5;

import org.xutils.http.RequestParams;

/**
 * Created by WangYong on 2017/11/7.
 */

public class MeFragment extends BaseFragment implements View.OnClickListener, HttpUtil.HttpRequesListener {
    /**
     * 我的资产
     */
    private RelativeLayout uliMeAssets;

    /**
     * 消息
     */
    private UsuallyLayoutItem5 uliMessage;
    /**
     * 风险评测
     */
    private UsuallyLayoutItem5 uliRiskAssessment;

    /**
     * 关于零钱罐
     */
    private UsuallyLayoutItem5 uliLqgService;
    /**
     * 可用余额
     */
    private LinearLayout llAvailableBalance;
    private TextView tvAvailableBalance;
    /**
     * 累计收益
     */
    private LinearLayout llTotalEarnings;
    private TextView tvLjsy;
    /**
     * 体验金
     */
    private LinearLayout llExperienceMoney;
    private TextView tvTyj;
    /**
     * 我的投资
     */
    private UsuallyLayoutItem5 uliMyInvestment;
    /**
     * 资金记录
     */
    private UsuallyLayoutItem5 uliAssets;
    /**
     * 福利卡券
     */
    private UsuallyLayoutItem5 uliFuliCard;

    /**
     * 福利任务
     */
    private ImageView ivFuliTask;
    /**
     * 运营数据
     */
    private ImageView ivOperationData;
    /**
     * 我的客服
     */
    private ImageView ivLqgMyService;
    /**
     * 设置
     */
    private UsuallyLayoutItem5 uliLqgSetting;

    /**
     * 圆形头像
     */
    private CircleImageView civHead;

    /**
     * 手机号码
     */
    private TextView tvPhone;

    private LinearLayout llMe;

    private NavigationBarItem nbiMe;
    /**
     * 总资产
     */
    private TextView uliTvAssets;
    /**
     * 银行卡
     */
    private UsuallyLayoutItem5 uliBank;
    /**
     * 我的界面滑动
     */
    private ObservableScrollView observableScrollView;
    private LinearLayout llHead;
    private static final int SCROLLLIMIT = 5;
    /**
     * 我的界面标题栏背景
     */
    private RelativeLayout rlMeTitleBgColor;
    /**
     * 头像是否变化
     */
    private boolean bChange = false;
    private float meTittleHeight;
    private LinearLayout llShowPhoneNickname;
    private LinearLayout llShowLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        start(view);
        return view;
    }

    @Override
    public void findViewById(View view) {
        civHead = (CircleImageView) view.findViewById(R.id.civ_head);
        tvPhone = (TextView) view.findViewById(R.id.uli_tv_phone);
        uliTvAssets = (TextView) view.findViewById(R.id.uli_me_tv_assets);
        uliBank = (UsuallyLayoutItem5) view.findViewById(R.id.uli_bank);
        uliMeAssets = (RelativeLayout) view.findViewById(R.id.uli_me_assets);
        uliMessage = (UsuallyLayoutItem5) view.findViewById(R.id.uli_message);
        uliRiskAssessment = (UsuallyLayoutItem5) view.findViewById(R.id.uli_risk_assessment);
        uliLqgService = (UsuallyLayoutItem5) view.findViewById(R.id.uli_lqg_service);
        ivLqgMyService = (ImageView) view.findViewById(R.id.uli_lqg_my_services);
        ivOperationData = (ImageView) view.findViewById(R.id.me_operation_data);
        ivFuliTask = (ImageView) view.findViewById(R.id.me_fuli_task);
        uliLqgSetting = (UsuallyLayoutItem5) view.findViewById(R.id.uli_lqg_setting);
        llHead = (LinearLayout) view.findViewById(R.id.ll_head);
        llAvailableBalance = (LinearLayout) view.findViewById(R.id.me_available_balance);
        llTotalEarnings = (LinearLayout) view.findViewById(R.id.me_total_earnings);
        llExperienceMoney = (LinearLayout) view.findViewById(R.id.me_experience_money);
        llShowPhoneNickname = (LinearLayout) view.findViewById(R.id.ll_show_phone_nickname);
        llShowLogin = (LinearLayout) view.findViewById(R.id.ll_show_login);
        tvAvailableBalance = (TextView) view.findViewById(R.id.tv_me_kyye);
        tvLjsy = (TextView) view.findViewById(R.id.tv_me_ljsy);
        tvTyj = (TextView) view.findViewById(R.id.tv_me_tyj);
        uliMyInvestment = (UsuallyLayoutItem5) view.findViewById(R.id.uli_my_investment);
        uliAssets = (UsuallyLayoutItem5) view.findViewById(R.id.uli_assets);
        uliFuliCard = (UsuallyLayoutItem5) view.findViewById(R.id.uli_fuli_card);
        observableScrollView = (ObservableScrollView) view.findViewById(R.id.scrollview_me);
        rlMeTitleBgColor = (RelativeLayout) view.findViewById(R.id.rl_me_title);
        llMe = (LinearLayout) view.findViewById(R.id.ll_me);
        mHttpUtil.setHttpRequesListener(this);
    }

    @Override
    public void setListener() {
        // 设置
        uliLqgSetting.setOnClickListener(this);
        // 点击头像部分
        civHead.setOnClickListener(this);
        // 我的资产
        uliMeAssets.setOnClickListener(this);
        // 消息
        uliMessage.setOnClickListener(this);
        // 风险评测
        uliRiskAssessment.setOnClickListener(this);
        // 关于零钱罐
        uliLqgService.setOnClickListener(this);
        // 我的客服
        ivLqgMyService.setOnClickListener(this);
        // 分享
        ivOperationData.setOnClickListener(this);
        //可用余额
        llAvailableBalance.setOnClickListener(this);
        //累计收益
        llTotalEarnings.setOnClickListener(this);
        //体验金
        llExperienceMoney.setOnClickListener(this);
        //我的投资
        uliMyInvestment.setOnClickListener(this);
        //资金记录
        uliAssets.setOnClickListener(this);
        //福利卡券
        uliFuliCard.setOnClickListener(this);
        //福利任务
        ivFuliTask.setOnClickListener(this);
        llMe.setOnClickListener(this);
        uliBank.setOnClickListener(this);
        mHttpUtil.setHttpRequesListener(this);
        llShowLogin.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
    }

    @Override
    public void initData() {
        LoginState(isLogin);

//        /**
//         * action:ZhiChiConstants.sobot_unreadCountBrocast
//         * 注册智齿广播
//         */
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ZhiChiConstant.sobot_unreadCountBrocast);
//        getActivity().registerReceiver(new SobotReceiver(), filter);
//
//        SobotApi.setNotificationFlag(getActivity(), true, R.drawable.sobot_launcher, R.drawable.ic_launcher);

    }

//    /**
//     * 头像随屏幕活动缩放动画
//     */
//    private void startTittleImageAnimation(float x, float endX, float y, float endY) {
//        ScaleAnimation scaleAnimation = new ScaleAnimation(x, endX, y, endY, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        scaleAnimation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
//        scaleAnimation.setDuration(300);
//        llHead.startAnimation(scaleAnimation);
//    }

//    /**
//     * 设置标题背景透明化
//     */
//    public void setTitleBgAlpha(int oldy) {
//        if (oldy >= meTittleHeight) {
//            oldy = (int) meTittleHeight;
//        }
//        float alpha = (float) oldy / meTittleHeight;
//        int a = (int) (255 * alpha);
//        int color = Color.argb(a, 255, 255, 255);
//        rlMeTitleBgColor.setBackgroundColor(color); //动态改变标题栏背景
//    }

    public class SobotReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int noReadNum = intent.getIntExtra("noReadCount", 0);
            String content = intent.getStringExtra("content"); //未读消息数
//            if (noReadNum > 0) {
//                ivLqgMyService.setRedPromptDot("new");
//            }
            //新消息内容
//            LogUtils.i("新消息内容:" + content);

        }
    }

    /**
     * 有账号登录的情况
     */
    private void LoginState(boolean isLogin) {
        if (isLogin && app.mAccountData != null) {
            llShowPhoneNickname.setVisibility(View.VISIBLE);
            llShowLogin.setVisibility(View.GONE);
        } else {
            llShowPhoneNickname.setVisibility(View.GONE);
            llShowLogin.setVisibility(View.VISIBLE);
            return;
        }

    }


    @Override
    public void onStart() {
        if (isLogin && app.mAccountData != null) {
            if (!TextUtils.isEmpty(app.mAccountData.nickName)) {
                tvPhone.setMaxEms(6);
                tvPhone.setText(app.mAccountData.nickName);
            } else {
                tvPhone.setMaxEms(Integer.MAX_VALUE);
                tvPhone.setText(StringUtil.setBlurryPhone(mCurrentPhone));
            }
            if (!TextUtils.isEmpty(app.mAccountData.bankName)) {
                BankImageUtil.setBankImage(app.mAccountData.bankName, uliBank.getNextLeftImage());
            }
            HeadChangeUtil.requestHeadImage(app, civHead);
            tvAvailableBalance.setText(MoneyFormatUtil.format(app.mAccountData.available_money));       //可用余额
            tvLjsy.setText(MoneyFormatUtil.format(app.mAccountData.total_income));  //累计收益
            tvTyj.setText(MoneyFormatUtil.format(app.mAccountData.usableExpMoney)); //体验金
            String meAssets = MoneyFormatUtil.format(app.mAccountData.total);
            uliTvAssets.setText(meAssets);
        } else {
            uliTvAssets.setText("0.00");
        }
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        if (!isLogin) {// 没账号登录
            if (!mFirstInState) {// 不是第一次使用（有账号登录过）
                startActivity(new Intent(getActivity(), HaveAcountsLoginActivity.class));
            } else {// 不是第一次使用（没有账号登录过）
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
            return;
        }

        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_show_login:// 登录注册
                if (!isLogin) {// 没账号登录
                    if (!mFirstInState) {// 不是第一次使用（有账号登录过）
                        startActivity(new Intent(getActivity(), HaveAcountsLoginActivity.class));
                    } else {// 不是第一次使用（没有账号登录过）
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                    return;
                }
                break;
            case R.id.uli_lqg_setting:// 点击设置
                goActivity(SetingActivity.class);
                break;
            case R.id.civ_head:// 点击头像部分
                goActivity(HeadAmendActivity.class);
                break;
            case R.id.uli_me_assets:// 我的资产
                goActivity(TotalAssetsActivity.class);
                break;
            case R.id.uli_bank:// 银行
                intent = new Intent(getActivity(), MyBankActivity.class);
                intent.putExtra(PayPasswordType.class.getName(), PayPasswordType.PayPasswordNotUnequivocal);
                startActivity(intent);
                break;
            case R.id.me_fuli_task:// 福利任务
                intent = new Intent(getActivity(), ActivityWebViewActivity.class);
                intent.putExtra("bDailActivityOrNewActiity", false);
                startActivity(intent);
                break;
            case R.id.uli_message:// 消息
                goActivity(MessageActivity.class);
                // 点击消息直接将未读活动置为空并将红点消掉
                SharedPreferencesUtil.setPrefString(getActivity(), "isMsgRead", "0");
                // 点击消息去掉小红点
                uliMessage.setRedPromptDot(false);

                break;
            case R.id.uli_risk_assessment:// 风险评测

                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", Constant.H5_CALENDARSACTIVITY_GORISKGRADE + "?uid=" + mUid);
                intent.putExtra("TitleName", "风险评测");
                startActivity(intent);

                break;
            case R.id.uli_lqg_my_services:// 我的客服
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", Constant.H5_LQG_MYKEFU);
                intent.putExtra("showMyKeFu", "showMyKeFu");
                intent.putExtra("TitleName", "我的客服");
                startActivity(intent);
                break;
            case R.id.uli_lqg_service:// 关于零钱罐
                intent = new Intent(getActivity(), AboutLQGActivity.class);
                startActivity(intent);
                break;
            case R.id.me_available_balance:// 可用余额
                goActivity(AvailableBalanceActivity.class);
                break;
            case R.id.me_total_earnings:// 累计收益
                startActivity(new Intent(getActivity(), EarningsDetailsActivity.class));
                break;
            case R.id.me_experience_money:// 体验金
                intent = new Intent(getActivity(), TyjDetailsActivity1.class);
                if (app.mAccountData != null && app.mAccountData.usableExpMoney > 0) {
                    intent.putExtra(AnimationShowState.class.getName(), AnimationShowState.hintAnimation);
                } else {
                    intent.putExtra(AnimationShowState.class.getName(), AnimationShowState.showAnimation);
                }
                startActivity(intent);

                break;
            case R.id.uli_my_investment:// 我的投资
                goActivity(MyInvestmentActivity.class);
                break;
            case R.id.uli_assets:// 资金记录
                startActivity(new Intent(getActivity(), BillDetailsActivity.class));
                break;
            case R.id.uli_fuli_card:// 福利卡券
                intent = new Intent(getActivity(), WelfareCardVoucherActivity.class);
                startActivity(intent);
                break;
            case R.id.me_operation_data: // 运营数据
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "https://ncifang.epub360.com.cn/v2/manage/book/hbyuh3/");
                intent.putExtra("TitleName", "第四季度运营报告");
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    /**
     * 点击通知栏消掉我的客服的new
     */
    public static void dissNewTab() {
//        ivLqgMyService.setRedPromptDot("");
    }

    @Override
    public void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onPageStart("MeFragment");

        nbiMe = (NavigationBarItem) getActivity().findViewById(R.id.nbi_me);
        if (isLogin) {
            /**
             * 请求和设置我的界面的红点
             */
            loadRedDotState();
        }
    }

    /**
     * 请求和设置我的界面的红点
     */
    private void loadRedDotState() {

        RequestParams params = new RequestParams(Constant.REFRESH_MYREDDOT);
        params.addBodyParameter("user_userunid", mUid);
        mHttpUtil.sendRequest(params, RefreshType.RefreshNoPull, getActivity());

    }

    @Override
    public void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPageEnd("MeFragment");
    }

    @Override
    public void onFailure(String url, String errorContent) {

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {

        String isMsgRead = JSON.parseObject(jsonBase.getDisposeResult()).getString("isMsgRead");
        // 设置消息数字红点
        if (isMsgRead.equals("1")) {
            uliMessage.setRedPromptDot(true);
        } else {
            uliMessage.setRedPromptDot(false);
        }
        if (isMsgRead.equals("0")) {
            // 不显示红点
            nbiMe.cancelRedDot();
        } else {
            // 显示红点
            nbiMe.showRedDot();
        }
    }
}

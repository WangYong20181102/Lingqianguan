package com.wtjr.lqg.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.AboutLQGLayoutItem;

import org.xutils.http.RequestParams;

/**
 * Created by WangYong on 2017/11/20.
 */

public class WeekWeekUpActivity extends BaseActivity implements View.OnClickListener, HttpUtil.HttpRequesListener {
    /**
     * 标题
     */
    private TextView tvTitleName;
    /**
     * 返回
     */
    private ImageButton imgBtnBack;
    /**
     * 标题栏背景
     */
    private RelativeLayout rlTittleBg;
    /**
     * 标题栏下划线
     */
    private View viewLine;
    /**
     * 服务协议
     */
    private AboutLQGLayoutItem alliServiceAgreement;
    /**
     * 借款列表
     */
    private AboutLQGLayoutItem alliJKList;
    /**
     * 转出
     */
    private TextView btnRollOut;
    /**
     * 标利率
     */
    private TextView tvBidRate;
    /**
     * 增长利率
     */
    private TextView tvAddRate;
    /**
     * 投资金额
     */
    private TextView tvInvestmentMoney;
    /**
     * 累计收益
     */
    private TextView tvAccumulatedEarnings;
    /**
     * 待获收益
     */
    private TextView tvObtained;
//    /**
//     * 交易ID
//     */
//    private TextView tvTradingId;
    /**
     * 投资时间
     */
    private TextView tvInvestmentData;
    /**
     * 当前利率
     */
    private TextView tvCurrentRate;
    /**
     * 计息日期
     */
    private TextView tvInterestData;
    /**
     * 到期时间
     */
    private TextView tvDueToTime;
    /**
     * 收益方式
     */
    private TextView tvEarringType;
    /**
     * 退出方式
     */
    private TextView tvExitType;
    /**
     * 锁定期
     */
    private TextView tvLockData;

    /**
     * 我的投资数据
     */
    private Investment investment;
    /**
     * 加息幅度 时长
     */
    private TextView tvAddRateTime;
    private LinearLayout llActivityExplain;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_week_up_layout);
        start();
    }

    @Override
    public void initData() {
        if (investment.baseRate.equals(investment.maxRate)){
            tvBidRate.setText(MoneyFormatUtil.format2(investment.baseRate));
        }else {
            tvBidRate.setText(MoneyFormatUtil.format2(investment.baseRate) + "-" + MoneyFormatUtil.format2(investment.maxRate));
        }
        if (Double.parseDouble(investment.isAddRate) == 1) {
            tvAddRate.setVisibility(View.VISIBLE);
            llActivityExplain.setVisibility(View.VISIBLE);
            tvAddRate.setText("+" + MoneyFormatUtil.formatW(investment.addRate) + "%");
            tvAddRateTime.setText("加息幅度-" + MoneyFormatUtil.formatW(investment.addRate) + "%\n加息时长-即日起，" + investment.addRateDays + "天");
        } else {
            llActivityExplain.setVisibility(View.GONE);
            tvAddRate.setVisibility(View.GONE);
        }
        if (investment.investStatus.equals("2")||investment.investStatus.equals("0")) {
            btnRollOut.setVisibility(View.GONE);
        } else {
            btnRollOut.setVisibility(View.VISIBLE);
        }
        if (investment.isLock == 1) {//锁定中
            tvLockData.setText(investment.lockDays + "天锁定中");
            btnRollOut.setEnabled(false);
            ToastUtil.showToastShort(this, "锁定期内，暂不能转出!");
        } else if (investment.isLock == 2) {//没匹配
            tvLockData.setText("");
            btnRollOut.setEnabled(false);
            ToastUtil.showToastShort(this, "锁定期内，暂不能转出!");
        } else if (investment.planStatus == 4) {//募集中不能转出
            tvLockData.setText("");
            btnRollOut.setEnabled(false);
            ToastUtil.showToastShort(this, "锁定期内，暂不能转出!");
        } else if (investment.planStatus == 7) {//计划已完成不能转出
            tvLockData.setText("");
            btnRollOut.setVisibility(View.GONE);
        }  else {
            tvLockData.setText("");
            btnRollOut.setEnabled(true);
        }
        tvInvestmentMoney.setText("¥" + MoneyFormatUtil.format(investment.investMoney));
        tvAccumulatedEarnings.setText("¥" + MoneyFormatUtil.format(investment.profit));
        tvObtained.setText("¥" + MoneyFormatUtil.format(investment.incomeMoney));
        tvInvestmentData.setText(TimeUtil.getTimeType3(investment.investTime));
        tvCurrentRate.setText(investment.currentRate + "%");
        tvInterestData.setText(TimeUtil.getY_M_D_Type3(investment.startDate));
        tvDueToTime.setText(TimeUtil.getY_M_D_Type3(investment.computeDate));
        tvEarringType.setText(investment.profitWay);
        tvExitType.setText(investment.quitWay);
    }

    @Override
    public void initIntent() {
        investment = (Investment) getIntent().getSerializableExtra(Investment.class.getName());
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        rlTittleBg = (RelativeLayout) findViewById(R.id.rl_title);
        viewLine = findViewById(R.id.title_bottom_line);
        //借款列表
        alliJKList = (AboutLQGLayoutItem) findViewById(R.id.alli_wwu_jiekuan_list);
        //服务协议
        alliServiceAgreement = (AboutLQGLayoutItem) findViewById(R.id.alli_wwu_service_agreement);
        //转出
        btnRollOut = (TextView) findViewById(R.id.btn_roll_out);
        //标利率
        tvBidRate = (TextView) findViewById(R.id.tv_bid_rate);
        //增长利率
        tvAddRate = (TextView) findViewById(R.id.tv_add_rate);
        //锁定期
        tvLockData = (TextView) findViewById(R.id.tv_lock_data);
        //投资金额
        tvInvestmentMoney = (TextView) findViewById(R.id.tv_investment_money);
        //累计收益
        tvAccumulatedEarnings = (TextView) findViewById(R.id.tv_accumulated_earnings);
        //待获收益
        tvObtained = (TextView) findViewById(R.id.tv_obtained);
//        //交易ID
//        tvTradingId = (TextView) findViewById(R.id.tv_trading_id);
        //投资时间
        tvInvestmentData = (TextView) findViewById(R.id.tv_investment_data);
        //当前利率
        tvCurrentRate = (TextView) findViewById(R.id.tv_current_rate);
        //计息日期
        tvInterestData = (TextView) findViewById(R.id.tv_interest_data);
        //到期时间
        tvDueToTime = (TextView) findViewById(R.id.tv_due_to_time);
        //收益方式
        tvEarringType = (TextView) findViewById(R.id.tv_earring_type);
        //退出方式
        tvExitType = (TextView) findViewById(R.id.tv_exit_type);
        tvAddRateTime = (TextView) findViewById(R.id.tv_add_rate_time);
        llActivityExplain = (LinearLayout) findViewById(R.id.ll_activity_explain);

    }


    @Override
    public void setTitle() {
        rlTittleBg.setBackground(getResources().getDrawable(R.drawable.wwe_gradual_change_bg));
        viewLine.setVisibility(View.GONE);
        //计划名称
        tvTitleName.setText(investment.planName);
        tvTitleName.setTextColor(Color.WHITE);
        imgBtnBack.setVisibility(View.VISIBLE);
        imgBtnBack.setImageResource(R.drawable.back);
    }

    @Override
    public void setListener() {
        //返回
        imgBtnBack.setOnClickListener(this);
        //借款列表
        alliJKList.setOnClickListener(this);
        //服务协议
        alliServiceAgreement.setOnClickListener(this);
        btnRollOut.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:  //返回
                finish();
                break;
            case R.id.alli_wwu_jiekuan_list:  //借款列表
                intent = new Intent(this, BorrowingListActivity.class);
                intent.putExtra("planCode", investment.planCode);
                break;
            case R.id.alli_wwu_service_agreement:  //服务协议
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.INVESTMENT_QUERYIFADADAZZSTEMPLATE + "?uid=" + mUid + "&zzsId=" + investment.planCode);
                intent.putExtra("TitleName", "服务协议");
                break;
            case R.id.btn_roll_out:  //转出
                DialogUtil.transferConfirmationDialog(this, MoneyFormatUtil.format(investment.incomeMoney), new DialogUtil.OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        /**
                         * 债权转让，退出申请
                         */
                        sendRequestQuitPlan();

                    }
                }, new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {


                    }
                });
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * 债权转让，退出申请
     */
    private void sendRequestQuitPlan() {
        RequestParams params = new RequestParams(Constant.INVERSTMENT_REQUESTQUITPLAN);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("planCode", investment.planCode);
        params.addBodyParameter("investId", investment.investId);

        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        ToastUtil.showToastShort(this, "已标记转出");
        btnRollOut.setVisibility(View.GONE);
        MyInvestmentActivity.isGetBack(true);
    }
}

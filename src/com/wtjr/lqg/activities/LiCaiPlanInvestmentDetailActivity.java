package com.wtjr.lqg.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.PlanMasData;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.StartInvestmentType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.RequestHttpPVPUStatisticsUtils;
import com.wtjr.lqg.utils.bidstate.BidStateUtil;
import com.wtjr.lqg.widget.AboutLQGLayoutItem;

/**
 * Created by WangYong on 2017/11/20.
 * 周周升投资详情
 */

public class LiCaiPlanInvestmentDetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 标题
     */
    private TextView tvTitleName;
    /**
     * 返回
     */
    private ImageButton imgBtnBack;
    /**
     * 安全保障
     */
    private RelativeLayout rlSafetySafeguard;
    /**
     * 项目列表
     */
    private RelativeLayout rlProjectList;
    /**
     * 产品说明
     */
    private RelativeLayout rlProductSpecification;
    /**
     * 投资记录
     */
    private RelativeLayout rlInvestmentLog;
    /**
     * 开始投资
     */
    private Button btStartInvestment;
    /**
     * 标名称
     */
    private TextView tvBidName;
    /**
     * 标的利率
     */
    private TextView tvBidRate;
    /**
     * 利率后面的百分号
     */
    private TextView tvPerCen;
    /**
     * 是从哪个地方跳转投资详情
     */
    private InvestmentDetailsType mInvestmentDetailsType;
    /**
     * 起投金额
     */
    private TextView tvProjectName;
    /**
     * 倒计时
     */
    private CountDownTimer mTimer;
    /**
     * 周周升数据类
     */
    private PlanMasData planMasData;
    /**
     * 风险提示告知书
     */
    private AboutLQGLayoutItem aliRiskWarning;
    /**
     * 锁定天数
     */
    private TextView tvLock;
    /**
     * 涨息利率
     */
    private TextView tvFinancingLimit;
    /**
     * 加息幅度 时长
     */
    private TextView tvAddRateTime;
    private LinearLayout llActivityExplain;
    private ImageView ivLicaiPlanDetaiInterest;
    /**
     * 加息
     */
    private TextView tvAddRate;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_licai_plan_investment_details);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        rlSafetySafeguard = (RelativeLayout) findViewById(R.id.rl_safety_safeguard);
        rlInvestmentLog = (RelativeLayout) findViewById(R.id.rl_investment_log);
        rlProjectList = (RelativeLayout) findViewById(R.id.rl_project_list);
        rlProductSpecification = (RelativeLayout) findViewById(R.id.rl_product_specification);
        aliRiskWarning = (AboutLQGLayoutItem) findViewById(R.id.ali_risk_warning);

        btStartInvestment = (Button) findViewById(R.id.bt_start_investment);

        // 标名称
        tvBidName = (TextView) findViewById(R.id.tv_bid_name);
        // 标的利率
        tvBidRate = (TextView) findViewById(R.id.tv_bid_rate);
        // 利率后面的百分号
        tvPerCen = (TextView) findViewById(R.id.tv_per_cen);
        //锁定天数
        tvLock = (TextView) findViewById(R.id.tv_year_earnings);
        //涨息
        tvFinancingLimit = (TextView) findViewById(R.id.tv_financing_limit);
        //起投金额
        tvProjectName = (TextView) findViewById(R.id.tv_project_name);
        tvAddRateTime = (TextView) findViewById(R.id.tv_add_rate_time);
        llActivityExplain = (LinearLayout) findViewById(R.id.ll_activity_explain);
        tvAddRate = (TextView) findViewById(R.id.tv_add_rate);
        ivLicaiPlanDetaiInterest = findViewById(R.id.iv_licai_plan_detail_interest);
    }

    @Override
    public void initIntent() {

        //PU、PV统计
        String subLqgDomain = SharedPreferencesUtil.getPrefString(this, "subLqgDomain", "");
        RequestHttpPVPUStatisticsUtils.getInstance().requestType(this, mUid, 1, subLqgDomain); //统计次数


        planMasData = (PlanMasData) getIntent().getSerializableExtra(PlanMasData.class.getName());
        mInvestmentDetailsType = (InvestmentDetailsType) getIntent().getSerializableExtra(InvestmentDetailsType.class.getName());
    }

    @Override
    public void setTitle() {
        //标题
        tvTitleName.setText(R.string.investment_details_name);
        imgBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        initInvestmentData();

    }

    private void initInvestmentData() {

        tvBidName.setText(planMasData.planName);
        tvBidRate.setText(MoneyFormatUtil.format2(planMasData.baseRate) + "-" + MoneyFormatUtil.format2(planMasData.maxRate));
        tvProjectName.setText("最低" + MoneyFormatUtil.format2(planMasData.investMin) + "元即可预约");
        tvLock.setText(planMasData.lockDays + "天锁定期，锁定期后自由转出");
        tvFinancingLimit.setText("当日计息，每周涨息" + planMasData.increaseRate + "%");
        //加息利率(0不加息、1加息)
        if (planMasData.isAddRate == 1) {
            tvAddRate.setVisibility(View.VISIBLE);
            llActivityExplain.setVisibility(View.VISIBLE);
            tvAddRate.setText("+" + MoneyFormatUtil.formatW(planMasData.addRate) + "%");
            tvAddRateTime.setText("加息幅度-" + MoneyFormatUtil.formatW(planMasData.addRate) + "%\n加息时长-即日起，" + planMasData.addRateDays + "天");
            ivLicaiPlanDetaiInterest.setImageResource(R.drawable.week_up_addrate_detail_table);
        } else {
            tvAddRate.setVisibility(View.GONE);
            llActivityExplain.setVisibility(View.GONE);
            ivLicaiPlanDetaiInterest.setImageResource(R.drawable.week_up_detail_table);
        }

        int bStates = planMasData.status;
        switch (bStates) {
            case 1:
            case 2:
            case 3:
                break;
            case 4:
                setBidState("募集中");
                break;
            case 5:
            case 6:
                setBidState("已募满");
                break;
            case 7:
                setBidState("还款中");
                break;
            case 8:
                setBidState("已完成");
                break;
        }

    }
    /**
     * 设置标状态
     * @param bidState 状态(满标,投标中,还款中,已还清,倒计时)
     */
    private void setBidState(String bidState) {
        btStartInvestment.setText(bidState);
        if (bidState.equals("募集中")) {
            btStartInvestment.setEnabled(true);
            btStartInvestment.setText("立即预约");
        } else {
            btStartInvestment.setEnabled(false);
        }

    }

    /**
     * 投标方式选择的对话框
     */
    public void showInvestmentTypeDialog() {

        if (app.mAccountData.lqb == 0 && app.mAccountData.available_money > 0) {
            goBalanceInvestment();
            return;
        } else if (app.mAccountData.lqb > 0 && app.mAccountData.available_money == 0) {
            goLqgInvestment();
            return;
        } else if (app.mAccountData.lqb == 0 && app.mAccountData.available_money == 0) {
            goBalanceInvestment();
            return;
        }

        final Dialog dialog = new Dialog(this, R.style.dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_investment_type, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);

        ImageView closeIV = (ImageView) view.findViewById(R.id.iv_close);

        // 零钱宝
        RelativeLayout ll_balance_shiftTo = (RelativeLayout) view.findViewById(R.id.ll_balance_shiftTo);
        TextView tv_lqgBalance = (TextView) view.findViewById(R.id.tv_lqgBalance);

        // 可用余额
        RelativeLayout ll_balance_shiftTo2 = (RelativeLayout) view.findViewById(R.id.ll_balance_shiftTo2);
        TextView tv_useMoneyBalance = (TextView) view.findViewById(R.id.tv_useMoneyBalance);

        // 零钱宝
        tv_lqgBalance.setText("余额:" + MoneyFormatUtil.format(app.mAccountData.lqb));
        // 可用余额
        tv_useMoneyBalance.setText("余额:" + MoneyFormatUtil.format(app.mAccountData.available_money));

        // 点击关闭Dialog
        closeIV.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 点击零钱宝投标
        ll_balance_shiftTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goLqgInvestment();
                dialog.dismiss();
            }
        });

        // 点击可用余额投标
        ll_balance_shiftTo2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                goBalanceInvestment();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 去零钱罐投资
     */
    private void goLqgInvestment() {
        Intent intent = new Intent(this, StartInvestmentActivity.class);
        intent.putExtra(StartInvestmentType.class.getName(), StartInvestmentType.Lqb);
        intent.putExtra(InvestmentDetailsType.class.getName(), mInvestmentDetailsType);
        intent.putExtra(Investment.class.getName(), planMasData);
        intent.putExtra("borrowState", 3);
        startActivity(intent);
    }

    /**
     * 去可用余额投资
     */
    private void goBalanceInvestment() {
        Intent intent = new Intent(this, StartInvestmentActivity.class);
        intent.putExtra(StartInvestmentType.class.getName(), StartInvestmentType.Balance);
        intent.putExtra(InvestmentDetailsType.class.getName(), mInvestmentDetailsType);
        intent.putExtra(Investment.class.getName(), planMasData);
        intent.putExtra("borrowState", 3);
        startActivity(intent);
    }


    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        rlSafetySafeguard.setOnClickListener(this);
        rlInvestmentLog.setOnClickListener(this);
        rlProjectList.setOnClickListener(this);
        rlProductSpecification.setOnClickListener(this);
        btStartInvestment.setOnClickListener(this);
        aliRiskWarning.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:
                finish();
                break;
            case R.id.rl_safety_safeguard:// 安全保障
                intent = new Intent(LiCaiPlanInvestmentDetailActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_SAFE);
                intent.putExtra("TitleName", "安全保障");
                break;
            case R.id.rl_investment_log:// 投资记录
                intent = new Intent(LiCaiPlanInvestmentDetailActivity.this, InvestmentLogActivity.class);
                intent.putExtra(InvestmentDetailsType.class.getName(), mInvestmentDetailsType);
                intent.putExtra(Investment.class.getName(), planMasData);
                intent.putExtra("borrowState", 3);
                break;
            case R.id.rl_project_list:// 项目列表
                intent = new Intent(LiCaiPlanInvestmentDetailActivity.this, BorrowingProjectActivity.class);
                break;
            case R.id.rl_product_specification:// 产品说明
                intent = new Intent(LiCaiPlanInvestmentDetailActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.PLANBOOK_ZHOUZHOUSHEN);
                intent.putExtra("TitleName", "产品说明");
                break;
            case R.id.ali_risk_warning:// 风险提示告知书
                intent = new Intent(LiCaiPlanInvestmentDetailActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.PLANBOOK_FENGXIAN);
                intent.putExtra("TitleName", "风险提示告知书");
                break;
            case R.id.bt_start_investment:// 开始投资
                showInvestmentTypeDialog();
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(LiCaiPlanInvestmentDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            return true;
        }
        return super.dispatchKeyEvent(event);
    }
}

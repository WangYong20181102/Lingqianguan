package com.wtjr.lqg.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.SimpleAdatper;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.NewBorrow;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.enums.StartInvestmentType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.RequestHttpPVPUStatisticsUtils;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.bidstate.BidStateUtil;
import com.wtjr.lqg.widget.UpDownTextView;
import com.wtjr.lqg.widget.XCRecyclerView;

import org.xutils.http.RequestParams;

import java.util.ArrayList;

/**
 * 投资详情
 *
 * @author JoLie
 */
public class NewBorrowDetailsActivity extends BaseActivity implements OnClickListener, HttpUtil.HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;

    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 风控信息
     */
    private RelativeLayout rlRiskMessage;
    /**
     * 安全保障
     */
    private RelativeLayout rlSafetySafeguard;
    /**
     * 还款计划
     */
    private RelativeLayout rlRefundPlan;
    /**
     * 投资记录
     */
    private RelativeLayout rlInvestmentLog;
    /**
     * 开始投资
     */
    private Button btStartInvestment;
    /**
     * 刻度尺
     */
    private XCRecyclerView rvLv;
    /**
     * 刻度尺最大金额100000
     */
    private float MAX_MONEY_100000 = 200000.00f;
    /**
     * 收益柱状图最大的高度
     */
    private double mMaxH;
    /**
     * 零钱宝定期每元钱的高度
     */
    private double perH;
    /**
     * 银行，宝宝类，零钱宝，零钱宝定期各自的年化收益率
     */
    private double l1 = 0.0035, l2 = 0.0243, l3 = 0.08, l4 = 0.1288;
    /**
     * 刻度尺数据
     */
    private ArrayList<String> mDatas;
    /**
     * 刻度尺数据
     */
    private SimpleAdatper mSimpleAdatper;
    /**
     * 获取屏幕一半的宽度
     */
    private int screenWidthHalf;
    /**
     * 得到手机像素密度
     */
    private float mDensity;
    /**
     * 显示刻度值的TextView
     */
    private EditText tvScaleValue;
    /**
     * 投资数据类
     */
//    private Investment mInvestment;
    /**
     * 标状态
     */
    private TextView tvBidState;
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
     * 投资期限
     */
    private UpDownTextView udtvInvestmentHorizon;
    /**
     * 融资金额
     */
    private UpDownTextView udtvFinancingMoney;
    /**
     * 进度条
     */
    private ProgressBar pbInvestmentProgress;
    /**
     * 进度条百分比
     */
    private TextView tvInvestmentProgress;
    /**
     * 标名称
     */
    private TextView tvProjectName;
    /**
     * 标的利率
     */
    private TextView tvYearEarnings;
    /**
     * 投资期限
     */
    private TextView tvFinancingLimit;
    /**
     * 融资金额
     */
    private TextView tvFinancingMoney;
    /**
     * 开始时间
     */
    private TextView tvStartTime;
    /**
     * 还款方式
     */
    private TextView tvRefundWay;
    /**
     * 起息时间
     */
    private TextView tvInterestTime;
    /**
     * 还款状态
     */
    private TextView tvRefundState;
    /**
     * 投资金额
     */
    private TextView tvInvestAndEarnings;
    /**
     * 下部分的4个柱状图上方的收益值
     */
    private TextView tvLqbDownPillarValue1, tvLqbDownPillarValue2, tvLqbDownPillarValue3, tvLqbDownPillarValue4;
    /**
     * 下部分界面的柱状图（银行）
     */
    private ImageView ivLqbDownPillar1;
    /**
     * 下部分界面的柱状图（宝宝类）
     */
    private ImageView ivLqbDownPillar2;
    /**
     * 下部分界面的柱状图（零钱宝）
     */
    private ImageView ivLqbDownPillar3;
    /**
     * 下部分界面的柱状图（零钱宝定期）
     */
    private ImageView ivLqbDownPillar4;
    /**
     * 下部分的4个柱状图的LayoutParams
     */
    private LayoutParams layoutParams1, layoutParams2, layoutParams3, layoutParams4;
    /**
     * 底部柱状图父布局
     */
    private LinearLayout linearBottomHistogram;
    /**
     * 是从哪个地方跳转投资详情
     */
    private InvestmentDetailsType mInvestmentDetailsType;
    /**
     * 收益比例图
     */
    private ViewStub vsEarningsView;
    /**
     * 协议
     */
    private TextView tvNext;
    /**
     * 倒计时
     */
    private CountDownTimer mTimer;
    /**
     * 协议
     */
    private Dialog mProtocolDialog;
    /**
     * 投资进度条父布局
     */
    private RelativeLayout rlInvestmentProgress;
    private LinearLayout linearLayout;
    // 屏幕中心位置
    private int screenCentre;

    /**
     * 判断是手动输入还是滑动
     */
    private boolean bInputOrSlide = false;
    /**
     * 新手标数据类
     */
    private NewBorrow newBorrow;
    private RelativeLayout rlInvestmentDtailBg;
    private RelativeLayout rlAddRate;
    private TextView tvAddRate;
    private LinearLayout llActivityMoney;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_investment_details);
        start();
    }

    @Override
    public void initIntent() {

        //PU、PV统计
        String subLqgDomain = SharedPreferencesUtil.getPrefString(this, "subLqgDomain", "");
        RequestHttpPVPUStatisticsUtils.getInstance().requestType(this, mUid, 1, subLqgDomain); //统计次数


        newBorrow = (NewBorrow) getIntent().getSerializableExtra(NewBorrow.class.getName());
        mInvestmentDetailsType = (InvestmentDetailsType) getIntent().getSerializableExtra(InvestmentDetailsType.class.getName());
    }

    @Override
    public void findViewById() {
        //显示新手标仅限首次投资用户购买
        LinearLayout llNewborrowRemind = (LinearLayout) findViewById(R.id.ll_newborrow_remind);
        llNewborrowRemind.setVisibility(View.VISIBLE);

        rlInvestmentDtailBg = (RelativeLayout) findViewById(R.id.rl_investment_bg);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvNext = (TextView) findViewById(R.id.tv_next);
        rlRiskMessage = (RelativeLayout) findViewById(R.id.rl_risk_message);
        rlSafetySafeguard = (RelativeLayout) findViewById(R.id.rl_safety_safeguard);
        rlRefundPlan = (RelativeLayout) findViewById(R.id.rl_refund_plan);
        rlInvestmentLog = (RelativeLayout) findViewById(R.id.rl_investment_log);

        btStartInvestment = (Button) findViewById(R.id.bt_start_investment);

        vsEarningsView = (ViewStub) findViewById(R.id.vs_earningsView);

        // 开始时间
        tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        // 还款方式
        tvRefundWay = (TextView) findViewById(R.id.tv_refund_way);
        // 起息时间
        tvInterestTime = (TextView) findViewById(R.id.tv_interest_time);
        // 还款状态
        tvRefundState = (TextView) findViewById(R.id.tv_refund_state);

        // 标名称
        tvBidName = (TextView) findViewById(R.id.tv_bid_name);
        tvProjectName = (TextView) findViewById(R.id.tv_project_name);
        // 标的利率
        tvBidRate = (TextView) findViewById(R.id.tv_bid_rate);
        tvYearEarnings = (TextView) findViewById(R.id.tv_year_earnings);
        // 标的状态有，投标中，满标，倒计时，还款中，已还清
        tvBidState = (TextView) findViewById(R.id.tv_bid_state);
        // 利率后面的百分号
        tvPerCen = (TextView) findViewById(R.id.tv_per_cen);
        // 投资期限
        udtvInvestmentHorizon = (UpDownTextView) findViewById(R.id.udtv_investment_horizon);
        // 投资金额
        tvFinancingMoney = (TextView) findViewById(R.id.tv_financing_money);

        tvFinancingLimit = (TextView) findViewById(R.id.tv_financing_limit);
        // 融资金额
        udtvFinancingMoney = (UpDownTextView) findViewById(R.id.udtv_financing_money);

        // 投资进度条
        pbInvestmentProgress = (ProgressBar) findViewById(R.id.pb_investment_progress);
        // 投资进度百分百
        tvInvestmentProgress = (TextView) findViewById(R.id.tv_investment_progress);
        // 投资进度条父布局
        rlInvestmentProgress = (RelativeLayout) findViewById(R.id.rl_investment_progress);
        //加息控件父布局
        rlAddRate = (RelativeLayout)findViewById(R.id.rl_add_rate);
        tvAddRate = (TextView)findViewById(R.id.tv_add_rate);
        llActivityMoney = (LinearLayout)findViewById(R.id.ll_activity_money);
    }

    /**
     * 设置投资进度
     *
     * @param progress 当前进度
     */
    private void setInvestmentProgress(String progress) {
        pbInvestmentProgress.setProgress((int) Double.parseDouble(progress));
        tvInvestmentProgress.setText(progress + "%");
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        rlRiskMessage.setOnClickListener(this);
        rlSafetySafeguard.setOnClickListener(this);
        rlRefundPlan.setOnClickListener(this);
        rlInvestmentLog.setOnClickListener(this);
        btStartInvestment.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);

    }

    /**
     * 文字监听
     */
    private TextWatcher textWatcher = new TextWatcher() {


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (TextUtils.isEmpty(charSequence)) {
                return;
            }
            int dNum = Integer.parseInt(charSequence.toString());
            if (dNum > (int) newBorrow.bLoanMoney) {
                dNum = (int) newBorrow.bLoanMoney;
                tvScaleValue.setText(dNum + "");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    public void setTitle() {
        // 设置名字为:投资详情
        tvTitleName.setText(R.string.investment_details_name);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);


        if (mInvestmentDetailsType != InvestmentDetailsType.Investment && newBorrow.bStates > 5) {
            tvNext.setText("协议");
            tvNext.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initData() {
        initInvestmentData();
        if (mInvestmentDetailsType != InvestmentDetailsType.LqbInvestment) {
            vsEarningsView.setVisibility(View.VISIBLE);
            initScaleData();
        }
    }

    private void initInvestmentData() {

        MAX_MONEY_100000 = (float) newBorrow.bLoanMoney;

        if (app.mLqbData != null) {
            l3 = app.mLqbData.lqbRate / 100;
        }
        l4 = newBorrow.bYearRate / 100;

        tvProjectName.setText(newBorrow.bName);
        tvBidName.setText(newBorrow.bName);
//        tvYearEarnings.setText(MoneyFormatUtil.format2(newBorrow.bYearRate) + "%");
        tvYearEarnings.setText("11.00%");
//        tvBidRate.setText(MoneyFormatUtil.format2(newBorrow.bYearRate) + "");
        tvBidRate.setText("11.00");

        if (mInvestmentDetailsType == InvestmentDetailsType.LqbInvestment) {
            udtvInvestmentHorizon.setUpTextContent(newBorrow.bLoanDays + "个月");
            tvFinancingLimit.setText(newBorrow.bLoanDays + "个月");
        } else {
            udtvInvestmentHorizon.setUpTextContent(newBorrow.bLoanDays + "天");
            tvFinancingLimit.setText(newBorrow.bLoanDays + "天");
        }

        udtvFinancingMoney.setUpTextContent(MoneyFormatUtil.formatW2(newBorrow.bLoanMoney));
        tvFinancingMoney.setText(MoneyFormatUtil.format(newBorrow.bLoanMoney) + "元");

        tvStartTime.setText(TimeUtil.getY_M_D_Type2(newBorrow.bCountdown));
        tvRefundWay.setText("先息后本");

        // tvInterestTime.setText(TimeUtil.getH_M_S(newBorrow.bCountdown));
        tvRefundState.setText(getRepaymentState(newBorrow.bStates));
        rlInvestmentDtailBg.setBackgroundResource(R.drawable.investment_xinshou_bg);
        llActivityMoney.setVisibility(View.VISIBLE);
        rlAddRate.setVisibility(View.VISIBLE);
        tvAddRate.setText("+2%");

        int bStates = newBorrow.bStates;
        String investmentProgress = "0.0";
        switch (bStates) {
            case 1:
            case 2:
            case 3:
                setBidState("倒计时");
                if (newBorrow.timer == -1) {// 断网时查看倒计时的标
                    tvBidState.setText("-1s");
                } else {

                    startTimer();

                }
                investmentProgress = "0";
                break;
            case 4:
                setBidState("投标中");
                // 投资进度
                investmentProgress = (int) (newBorrow.bInvestedMoney * 100d / newBorrow.bLoanMoney) + "";
                break;
            case 5:
            case 6:
                setBidState("满标");
                investmentProgress = "100";
                break;
            case 7:
                setBidState("还款中");

                investmentProgress = "100";
                break;
            case 8:
                setBidState("已完成");
                investmentProgress = "100";
                break;
        }


        if (mInvestmentDetailsType == InvestmentDetailsType.LqbInvestment) {
            investmentProgress = (int) (newBorrow.bInvestedMoney * 100d / newBorrow.bLoanMoney) + "";
            investmentProgress = "100";
            setInvestmentProgress(investmentProgress);
        } else {
            setInvestmentProgress(investmentProgress);
        }
    }

    /**
     * 启动倒计时
     */
    private void startTimer() {
        mTimer = new CountDownTimer(newBorrow.timer * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                if (newBorrow.timer > 1800) {        //如果大于30分钟显示开始，小于等于显示倒计时
                    tvBidState.setText(TimeUtil.getH_M(newBorrow.bCountdown * 1000 / 1000) + "开始");
                } else {
                    tvBidState.setText(TimeUtil.formatSecond(millisUntilFinished / 1000));
                }
            }

            public void onFinish() {
                setBidState("投标中");
                // 投资进度
                String investmentProgress = (int) (newBorrow.bInvestedMoney * 100d / newBorrow.bLoanMoney) + "";
                setInvestmentProgress(investmentProgress);
            }
        }.start();
    }

    /**
     * 停止倒计时
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
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
        closeIV.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 点击零钱宝投标
        ll_balance_shiftTo.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                goLqgInvestment();
                dialog.dismiss();
            }
        });

        // 点击可用余额投标
        ll_balance_shiftTo2.setOnClickListener(new OnClickListener() {
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
        intent.putExtra(Investment.class.getName(), newBorrow);
        intent.putExtra("borrowState", 2);
        startActivity(intent);
    }

    /**
     * 去可用余额投资
     */
    private void goBalanceInvestment() {
        Intent intent = new Intent(this, StartInvestmentActivity.class);
        intent.putExtra(StartInvestmentType.class.getName(), StartInvestmentType.Balance);
        intent.putExtra(InvestmentDetailsType.class.getName(), mInvestmentDetailsType);
        intent.putExtra(Investment.class.getName(), newBorrow);
        intent.putExtra("borrowState", 2);
        startActivity(intent);
    }

    /**
     * 设置标状态
     * <p>
     * 控件
     *
     * @param bidState 状态(满标,投标中,还款中,已还清,倒计时)
     */
    private void setBidState(String bidState) {
        // 倒梯形状态背景色
        tvBidState.setBackgroundResource(BidStateUtil.setBidStateImageUtil(bidState));
        // 内容
        tvBidState.setText(bidState);

        setBidStateTextColor(bidState);

        setBidStateProgressDrawable(bidState);

        btStartInvestment.setText(bidState);
        if (bidState.equals("投标中")) {
            btStartInvestment.setEnabled(true);
            btStartInvestment.setText("马上投标");
        } else {
            btStartInvestment.setEnabled(false);
        }

        if (bidState.equals("倒计时")) {
            btStartInvestment.setText("马上开始");
        }

        tvRefundState.setText(bidState);
        if (bidState.equals("倒计时")) {
            tvRefundState.setText("倒计时中");
        }
    }

    /**
     * 设置标状态字体色
     *
     * @param bidState 状态
     */
    private void setBidStateTextColor(String bidState) {
        int color = BidStateUtil.getBidStateTextColor(bidState);
        tvBidRate.setTextColor(color);
        tvPerCen.setTextColor(color);
    }

    /**
     * 更具状态设置进度的颜色
     *
     * @param bidState 状态
     */
    private void setBidStateProgressDrawable(String bidState) {
        Drawable d = BidStateUtil.getBidStateProgressDrawable(this, bidState);
        pbInvestmentProgress.setProgressDrawable(d);
    }

    /**
     * 初始化刻度尺数据
     */
    private void initScaleData() {
        ivLqbDownPillar1 = (ImageView) findViewById(R.id.iv_lqb_down_pillar1);
        ivLqbDownPillar2 = (ImageView) findViewById(R.id.iv_lqb_down_pillar2);
        ivLqbDownPillar3 = (ImageView) findViewById(R.id.iv_lqb_down_pillar3);
        ivLqbDownPillar4 = (ImageView) findViewById(R.id.iv_lqb_down_pillar4);
        linearBottomHistogram = (LinearLayout) findViewById(R.id.ll_bottom_histogram);

        tvLqbDownPillarValue1 = (TextView) findViewById(R.id.tv_lqb_down_pillarValue1);
        tvLqbDownPillarValue2 = (TextView) findViewById(R.id.tv_lqb_down_pillarValue2);
        tvLqbDownPillarValue3 = (TextView) findViewById(R.id.tv_lqb_down_pillarValue3);
        tvLqbDownPillarValue4 = (TextView) findViewById(R.id.tv_lqb_down_pillarValue4);

        linearBottomHistogram.setVisibility(View.GONE);

        layoutParams1 = (LayoutParams) ivLqbDownPillar1.getLayoutParams();
        layoutParams2 = (LayoutParams) ivLqbDownPillar2.getLayoutParams();
        layoutParams3 = (LayoutParams) ivLqbDownPillar3.getLayoutParams();
        layoutParams4 = (LayoutParams) ivLqbDownPillar4.getLayoutParams();

        tvInvestAndEarnings = (TextView) findViewById(R.id.tv_investAndEarnings);
        linearLayout = (LinearLayout) findViewById(R.id.ll_earningsView);
        rvLv = (XCRecyclerView) findViewById(R.id.rv_lv);
        tvScaleValue = (EditText) findViewById(R.id.tv_scale_value);
//        tvScaleValue.setOnClickListener(this);
        linearLayout.setOnClickListener(this);
        //edittext父布局的点击事件
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                /*隐藏软键盘*/
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(linearLayout.getWindowToken(), 0);
                return false;
            }
        });
        //edittext监听输入内容的变化
        tvScaleValue.addTextChangedListener(textWatcher);

        Spanned fromHtml = Html.fromHtml("投资" + "<font color=#fa8c1e>" + 100000 + "</font>" + "元，");
        Spanned fromHtml2 = Html.fromHtml("30天最多可赚" + "<font color=#fa8c1e>" + MoneyFormatUtil.sEarnings(100000,newBorrow.bYearRate / 100,360,30) + "</font>" + "元");

        tvInvestAndEarnings.setText(fromHtml);
        tvInvestAndEarnings.append(fromHtml2);

        mMaxH = app.getResources().getDimension(R.dimen.d60);
        perH = mMaxH / MAX_MONEY_100000;
        rvLv.post(new Runnable() {
            @Override
            public void run() {
                // 刻度尺中一个Item的长度
                float widthItem = app.getResources().getDimension(R.dimen.d100);
                float totalWidth = widthItem * MAX_MONEY_100000 / 5000 / 2;
                rvLv.scrollBy((int) totalWidth, 0);
            }
        });

        // 得到像素密度
        mDensity = app.mDensity;
        // 得到屏幕宽的一半
        screenWidthHalf = app.mScreenWidth / 2;

        mDatas = new ArrayList<String>();
        for (int i = 0; i < MAX_MONEY_100000 / 5000 + 1; i++) {
            if (i == 0) {
                mDatas.add("100");
            } else {
                mDatas.add("" + i * 5000);
            }
        }

        mSimpleAdatper = new SimpleAdatper(this, mDatas);

        rvLv.setLayoutManager(new LinearLayoutManager(this));

        View mHeaderView = LayoutInflater.from(this).inflate(R.layout.head_foot_xc_recycler, rvLv, false);
        View mFooterView = LayoutInflater.from(this).inflate(R.layout.head_foot_xc_recycler, rvLv, false);
        rvLv.addHeaderView(mHeaderView);
        rvLv.addFooterView(mFooterView);

        rvLv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvLv.setAdapter(mSimpleAdatper);

        rvLv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            // 屏幕中心位置值
            private int screenCentreValue;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 获得中心红线的位置
                screenCentre += 50 * dx;
                // 获得中心红线的位置值
                screenCentreValue = (int) (screenCentre / mDensity);

                if (screenCentreValue < 100) {
                    screenCentreValue = 100;
                }
                if (bInputOrSlide) {
                    bInputOrSlide = false;
                } else {
                    tvScaleValue.setText(screenCentreValue + "");
                    earningsShow(screenCentreValue);
                }
            }
        });
    }

    /**
     * 用于计算收益柱状图的数据
     *
     * @param newCurrent
     */
    private void earningsShow(int newCurrent) {

        layoutParams4.height = (int) (newCurrent * perH);
        ivLqbDownPillar4.setLayoutParams(layoutParams4);

        layoutParams3.height = (int) (l3 / l4 * newCurrent * perH);
        ivLqbDownPillar3.setLayoutParams(layoutParams3);

        layoutParams2.height = (int) (l2 / l4 * newCurrent * perH);
        ivLqbDownPillar2.setLayoutParams(layoutParams2);

        layoutParams1.height = (int) (l1 / l4 * newCurrent * perH);
        ivLqbDownPillar1.setLayoutParams(layoutParams1);

        tvLqbDownPillarValue1.setText(MoneyFormatUtil.sEarnings2(newCurrent, l1,365,newBorrow.bLoanDays));
        tvLqbDownPillarValue2.setText(MoneyFormatUtil.sEarnings2(newCurrent, l2,365,newBorrow.bLoanDays));
        tvLqbDownPillarValue3.setText(MoneyFormatUtil.sEarnings2(newCurrent, l3,365,newBorrow.bLoanDays));
        tvLqbDownPillarValue4.setText(MoneyFormatUtil.sEarnings2(newCurrent, l4,365,newBorrow.bLoanDays));

        Spanned fromHtml = Html.fromHtml("投资" + "<font color=#fa8c1e>" + newCurrent + "</font>" + "元，");
        Spanned fromHtml2 = Html.fromHtml("<font color=#fa8c1e>" + newBorrow.bLoanDays + "</font>" + "天最多可赚" + "<font color=#fa8c1e>" + MoneyFormatUtil.sEarnings2(newCurrent, l4,365,newBorrow.bLoanDays) + "</font>" + "元");
        tvInvestAndEarnings.setText(fromHtml);
        tvInvestAndEarnings.append(fromHtml2);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.rl_risk_message:// 风控信息
                intent = new Intent(NewBorrowDetailsActivity.this, RiskMessageActivity.class);
                intent.putExtra(InvestmentDetailsType.class.getName(), mInvestmentDetailsType);
                intent.putExtra(Investment.class.getName(), newBorrow);
                intent.putExtra("borrowState", 2);
                break;
            case R.id.rl_safety_safeguard:// 安全保障
                intent = new Intent(NewBorrowDetailsActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_SAFE);
                intent.putExtra("TitleName", "安全保障");
                break;
            case R.id.rl_refund_plan:// 还款计划
                intent = new Intent(NewBorrowDetailsActivity.this, RefundPlanActivity.class);
                intent.putExtra(InvestmentDetailsType.class.getName(), mInvestmentDetailsType);
                intent.putExtra(Investment.class.getName(), newBorrow);
                intent.putExtra("borrowState", 2);
                break;
            case R.id.rl_investment_log:// 投资记录
                intent = new Intent(NewBorrowDetailsActivity.this, InvestmentLogActivity.class);
                intent.putExtra(InvestmentDetailsType.class.getName(), mInvestmentDetailsType);
                intent.putExtra(Investment.class.getName(), newBorrow);
                intent.putExtra("borrowState", 2);
                break;
            case R.id.bt_start_investment:// 开始投资
                showInvestmentTypeDialog();
                break;
            case R.id.tv_next:// 协议
                switch (mInvestmentDetailsType) {
                    case LqbInvestment:
                        showProtocolDialog();
                        break;
                    case MyInvestment:
                        sendAgreementRequest();
                        break;
                }
                break;
            case R.id.bt_popmenu_exit:// 取消
                closeProtocolDialog();
                break;
            case R.id.bt_queryLoanProtocol:// 零钱宝借款协议
                closeProtocolDialog();
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.LQB_QUERYLOANPROTOCOL + "?mbId=" + newBorrow.bId + "&user_userunid=" + mUid);
                intent.putExtra("TitleName", "零钱宝借款协议");
                break;
            case R.id.bt_queryTransferProtocol:// 零钱宝转让协议
                closeProtocolDialog();
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.LQB_QUERYTRANSFERPROTOCOL + "?mbId=" + newBorrow.bId + "&user_userunid=" + mUid);
                intent.putExtra("TitleName", "零钱宝转让协议");
                break;
            case R.id.ll_earningsView:
                setBottomKaChi();
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
    /**
     * 发送协议请求
     */
    private void sendAgreementRequest() {
        RequestParams params = new RequestParams(Constant.INVESTMENT_QUERYIFADADAUSERTEMPLATE);
        params.addBodyParameter("uid", mUid);
        params.addBodyParameter("investId", newBorrow.investId);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);
    }
    /**
     * 设置底部卡尺数据
     */
    private void setBottomKaChi() {
        bInputOrSlide = true;
        String strInputNum = tvScaleValue.getText().toString();
        int numMoney;
        if (TextUtils.isEmpty(strInputNum)) {
            numMoney = 100;
        } else {
            numMoney = Integer.parseInt(strInputNum);
            if (numMoney < 100) {
                numMoney = 100;
            }
        }
        tvScaleValue.setText(numMoney + "");
        earningsShow(numMoney);
        final int finalDNum = numMoney;
        rvLv.post(new Runnable() {
            @Override
            public void run() {
                // 刻度尺中一个Item的长度
                float widthItem = app.getResources().getDimension(R.dimen.d100);
                float totalWidth = widthItem * finalDNum / 5000;
                totalWidth = totalWidth - (screenCentre / 50);
                rvLv.scrollBy((int) totalWidth, 0);
            }
        });
    }

    /**
     * 显示协议的菜单对话框
     */
    private void showProtocolDialog() {
        mProtocolDialog = DialogUtil.upSlideDialog(this, R.layout.dialog_protocol_popmenu);

        Button btPopmenuExit = (Button) mProtocolDialog.findViewById(R.id.bt_popmenu_exit);
        // 零钱宝借款协议
        Button btQueryLoanProtocol = (Button) mProtocolDialog.findViewById(R.id.bt_queryLoanProtocol);
        // 零钱宝转让协议
        Button btQueryTransferProtocol = (Button) mProtocolDialog.findViewById(R.id.bt_queryTransferProtocol);

        btPopmenuExit.setOnClickListener(this);// 取消按钮的监听
        btQueryLoanProtocol.setOnClickListener(this);// 零钱宝借款协议
        btQueryTransferProtocol.setOnClickListener(this);// 零钱宝转让协议
    }

    /**
     * 关闭协议的菜单对话框
     */
    private void closeProtocolDialog() {
        if (mProtocolDialog != null) {
            mProtocolDialog.dismiss();
        }
    }

    /**
     * 获得还款状态
     *
     * @param value
     * @return
     */
    private String getRepaymentState(int value) {
        String stateName = "";
        switch (value) {
            case 1:
                stateName = "已添加未审核";
                break;
            case 2:
                stateName = "已审核未发布";
                break;
            case 3:
                stateName = "已发布未开始";
                break;
            case 4:
                stateName = "已开始未满标";
                break;
            case 5:
                stateName = "已满标未复审";
                break;
            case 6:
                stateName = "已复审";
                break;
            case 7:
                stateName = "还款";
                break;
            case 8:
                stateName = "结束";
                break;
        }
        return stateName;
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(NewBorrowDetailsActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            setBottomKaChi();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", JSON.parseObject(jsonBase.getDisposeResult()).getString("fddUrl"));
        intent.putExtra("TitleName", "投资服务协议");
        startActivity(intent);
    }
}

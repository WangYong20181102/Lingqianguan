package com.wtjr.lqg.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.NewBorrow;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.bidstate.BidStateUtil;
import com.wtjr.lqg.widget.UpDownTextView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;

import org.xutils.http.RequestParams;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYong on 2017/11/20.
 * 我的投资
 */
public class MyInvestmentActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnItemClickListener, OnRefreshListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;

    private View headView;

    private InvestmentLogListAdapter<Investment> mAdapter;

    private ArrayList<Investment> mInvestments = new ArrayList<Investment>();

    private RelativeLayout rlBackgroundStateIcon;

    /**
     * 标的状态有，投标中，满标，倒计时，还款中，已还清
     */
    private TextView tvBidState;
    /**
     * 还款中倒计时天数
     */
    private TextView tvBidState1;
    /**
     * 标的利率
     */
    private TextView tvBidRate;
    /**
     * 利率后面的百分号
     */
    private TextView tvPerCen;
    /**
     * 进度条
     */
    private ProgressBar pbInvestmentProgress;
    /**
     * 进度条百分比
     */
    private TextView tvInvestmentProgress;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView lvInvestment;
    /**
     * 待获收益
     */
    private UpDownTextView udtvNrcyIncome;
    /**
     * 待收本金
     */
    private UpDownTextView udtvNrcyPrinciple;
    private int mPage = 1;
    private RefreshType mRefreshType;
    private ImageButton imgBtnNext;
    private RelativeLayout rlProgress;
    /**
     * 待获收益
     */
    private TextView tvIncomeMoney;
    /**
     * 到期还款日
     */
    private TextView tvComputeDate;
    /**
     * 返回的每条投资的数据
     */
    private Investment item;
    /**
     * 记录加载条数
     */
    private int loadNumber = 0;
    /**
     * 是否是转出之后的返回
     */
    private static boolean isBack = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_investment);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        imgBtnNext.setVisibility(View.VISIBLE);
        headView = getLayoutInflater().inflate(R.layout.head_my_investment, null);

        // 待获收益
        udtvNrcyIncome = (UpDownTextView) headView.findViewById(R.id.udtv_nrcyIncome);
        // 待收本金
        udtvNrcyPrinciple = (UpDownTextView) headView.findViewById(R.id.udtv_nrcyPrinciple);

        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
        lvInvestment = (PullableListView) findViewById(R.id.pullable_listView);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        imgBtnNext.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
        lvInvestment.setOnItemClickListener(this);
        // 下拉刷新监听
        pullToRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:活动
        tvTitleName.setText("我的投资");

    }

    @Override
    public void initData() {
        addHeader();
        sendMyInvestmentRequest(RefreshType.RefreshNoPull);
    }

    /**
     * 添加头
     */
    private void addHeader() {
        lvInvestment.setAdapter(null);
        lvInvestment.removeHeaderView(headView);
        lvInvestment.addHeaderView(headView);
        lvInvestment.setAdapter(mAdapter);
    }

    class InvestmentLogListAdapter<T> extends NewBaseAdapter<T> {

        public InvestmentLogListAdapter(Context context, List<T> list) {
            super(context, list);
        }

        @Override
        public int getContentView() {
            return R.layout.myinvestment_item;
        }

        @Override
        public void onInitView(View view, int position) {
            item = (Investment) getItem(position);

            //加息利率
            RelativeLayout rlAddRate = getViewChild(view, R.id.rl_add_rate);
            TextView tvAddRate = getViewChild(view, R.id.tv_add_rate);


            // 标名称
            TextView tvBidName = getViewChild(view, R.id.tv_bid_name);
            // 标的状态有，投标中，满标，倒计时，还款中，已还清
            tvBidState = getViewChild(view, R.id.tv_bid_state);
            tvBidState1 = getViewChild(view, R.id.tv_bid_state1);
            rlBackgroundStateIcon = getViewChild(view, R.id.rl_bg_state);
            LinearLayout llInvestmentBg = getViewChild(view, R.id.ll1);
            if (item.bType == 1) {
                llInvestmentBg.setBackgroundResource(R.drawable.investment_xinshou_bg);
            } else {
                llInvestmentBg.setBackgroundResource(R.drawable.investment_dingqi_bg);
            }
            // 标的利率
            tvBidRate = getViewChild(view, R.id.tv_bid_rate);
            // 利率后面的百分号
            tvPerCen = getViewChild(view, R.id.tv_per_cen);
            // 投资期限
            UpDownTextView udtvInvestmentHorizon = getViewChild(view, R.id.udtv_investment_horizon);
            if (item.bType == 2) {   //周周升
                tvBidName.setText(item.planName);
                if (Double.parseDouble(item.isAddRate) == 1) {
                    rlAddRate.setVisibility(View.VISIBLE);
                    tvAddRate.setText("+" + MoneyFormatUtil.formatW(item.addRate) + "%");
                } else {
                    rlAddRate.setVisibility(View.GONE);
                }
                if (item.lockDays.equals(item.bLoanDays + "") || TextUtils.isEmpty(item.lockDays)) {
                    udtvInvestmentHorizon.setVisibility(View.VISIBLE);
                    udtvInvestmentHorizon.setUpTextContent(item.bLoanDays + "天");
                } else {
                    udtvInvestmentHorizon.setVisibility(View.INVISIBLE);
                }
                if (item.baseRate.equals(item.maxRate)) {
                    tvBidRate.setText(MoneyFormatUtil.format2(item.baseRate));
                } else {
                    tvBidRate.setText(MoneyFormatUtil.format2(item.baseRate) + "-" + MoneyFormatUtil.format2(item.maxRate));
                }
            } else {
                tvBidName.setText(item.bName);
                udtvInvestmentHorizon.setVisibility(View.VISIBLE);
                udtvInvestmentHorizon.setUpTextContent(item.bLoanDays + "天");
                rlAddRate.setVisibility(View.GONE);
                if (item.bType == 1) {
                    tvBidRate.setText("11.00");
                    rlAddRate.setVisibility(View.VISIBLE);
                    tvAddRate.setText("+2%");
                } else {
                    tvBidRate.setText(MoneyFormatUtil.format2(item.bYearRate));
                    rlAddRate.setVisibility(View.GONE);
                }
            }
//            // 融资金额
//            UpDownTextView udtv_financing_money = getViewChild(view, R.id.udtv_financing_money);
//            udtv_financing_money.setUpTextContent(MoneyFormatUtil.formatW2(item.bLoanMoney));
            // 投资进度条
            pbInvestmentProgress = getViewChild(view, R.id.pb_investment_progress);
            // 投资进度百分百
            tvInvestmentProgress = getViewChild(view, R.id.tv_investment_progress);
            //进度条的父控件
            rlProgress = getViewChild(view, R.id.rl_investment_progress);
            //到期还款日和待获收益
            LinearLayout llMyIncome = getViewChild(view, R.id.ll_my_income);
            tvIncomeMoney = getViewChild(view, R.id.tv_income_money);
            tvComputeDate = getViewChild(view, R.id.tv_compute_date);
//            View viewChild = getViewChild(view, R.id.v_line_h);
//            TextView tvAlreadyInvestment = getViewChild(view, R.id.tv_already_investment);

            llMyIncome.setVisibility(View.VISIBLE);
            rlProgress.setVisibility(View.VISIBLE);
            tvComputeDate.setVisibility(View.VISIBLE);
            int bStates = item.bStates;
            String investmentProgress = "0.0";
            if (item.bType == 2) {
                rlProgress.setVisibility(View.GONE);
                if (item.investStatus.equals("0")) {
                    setBidState("已完成");
                } else {
                    switch (item.planStatus) {
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            setBidState("募集中");
                            tvComputeDate.setVisibility(View.GONE);
                            break;
                        case 5:
                            setBidState("已募满");
                            break;
                        case 6:
                            setBidState("收益中");
                            break;
                        case 7:
                            setBidState("已完成");
                            break;
                    }
                }
            } else {
                switch (bStates) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        llMyIncome.setVisibility(View.GONE);
                        setBidState("投标中");
                        // 投资进度
                        investmentProgress = (int) (item.bInvestedMoney * 100d / item.bLoanMoney) + "";
                        setInvestmentProgress(investmentProgress);
                        break;
                    case 5:
                    case 6:
                        setBidState("满标");
                        rlProgress.setVisibility(View.GONE);
                        break;
                    case 7:
                        setBidState("还款中");
                        rlProgress.setVisibility(View.GONE);
                        break;
                    case 8:
                        setBidState("已完成");
                        rlProgress.setVisibility(View.GONE);
                        break;
                }
            }

        }
    }

    /**
     * 设置标状态
     *
     * @param bidState 状态(满标,投标中,还款中,已还清,倒计时)
     */
    private void setBidState(String bidState) {

        if (bidState.equals("还款中")) {
            tvBidState.setVisibility(View.INVISIBLE);
            rlBackgroundStateIcon.setVisibility(View.VISIBLE);
            tvBidState1.setText(item.bCountDownDay + "天");
            setBidStateTextColor(bidState);
        } else {
            rlBackgroundStateIcon.setVisibility(View.INVISIBLE);
            tvBidState.setVisibility(View.VISIBLE);
            // 倒梯形状态背景色
            tvBidState.setBackgroundResource(BidStateUtil.setBidStateImageUtil(bidState));
            // 内容
            tvBidState.setText(bidState);

            setBidStateTextColor(bidState);
        }
        if (bidState.equals("投标中") || bidState.equals("募集中")) {
            setBidStateProgressDrawable(bidState);
            if (item.bType == 2) {
                setBottomText(tvIncomeMoney, item.bType, tvComputeDate);
            }
        } else {
            setBottomText(tvIncomeMoney, item.bType, tvComputeDate);
        }

    }

    /**
     * 底部展示
     */
    private void setBottomText(TextView incomeMoney, int type, TextView computeDate) {
        Spanned fromHtml = Html.fromHtml("<font color=#3f1e00>" + "已投" + "</font>" + "<font color=#ff9900>" + MoneyFormatUtil.format(item
                .investMoney) + "</font>" + "<font color=#3f1e00>" + "元 | " + "</font>");
        Spanned fromHtm2;
        if (type == 2) {
            fromHtm2 = Html.fromHtml("<font color=#3f1e00>" + "已获" + "</font>" + "<font color=#ff9900>" + MoneyFormatUtil.format(item.profit) +
                    "</font>" + "<font color=#3f1e00>" + "元" + "</font>");
        } else {
            fromHtm2 = Html.fromHtml("<font color=#3f1e00>" + "待获" + "</font>" + "<font color=#ff9900>" + MoneyFormatUtil.format(item.incomeMoney)
                    + "</font>" + "<font color=#3f1e00>" + "元" + "</font>");
        }
        incomeMoney.setText(fromHtml);
        incomeMoney.append(fromHtm2);
        computeDate.setText(Html.fromHtml("<font color=#bbbbbb>" + "到期还款日" + TimeUtil.getY_M_D_Type2(item.computeDate) + "</font>"));
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
     * 设置投资进度
     *
     * @param progress 当前进度
     */
    private void setInvestmentProgress(String progress) {
        pbInvestmentProgress.setProgress((int) Double.parseDouble(progress));
        tvInvestmentProgress.setText(progress + "%");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {// 点击的是ListView头
            return;
        }
        position = position - 1;

        Investment investment = mInvestments.get(position);

        if (investment.bStates != 8) {// 不是已还清的标
            if (investment.bType == 1) {
                NewBorrow newBorrow = new NewBorrow();
                newBorrow.setbLoanMoney(investment.bLoanMoney);
                newBorrow.setbStates(investment.bStates);
                newBorrow.setbYearRate(investment.bYearRate);
                newBorrow.setbName(investment.bName);
                newBorrow.setbLoanDays(investment.bLoanDays);
                newBorrow.setbCountdown(investment.bCountdown);
                newBorrow.setTimer(investment.timer);
                newBorrow.setbInvestedMoney(investment.bInvestedMoney);
                newBorrow.setbId(investment.bId);
                newBorrow.setbMiniMoney(investment.bMiniMoney);
                newBorrow.setbHigMoney(investment.bHigMoney);
                newBorrow.setInvestId(investment.investId);
                Intent intent = new Intent(this, NewBorrowDetailsActivity.class);
                intent.putExtra(NewBorrow.class.getName(), newBorrow);
                intent.putExtra(InvestmentDetailsType.class.getName(), InvestmentDetailsType.MyInvestment);
                startActivity(intent);
            } else if (investment.bType == 2) {
                if (investment.planStatus != 7) {    //周周升不是已完成的标
                    Intent intent = new Intent(this, WeekWeekUpActivity.class);
                    intent.putExtra(Investment.class.getName(), investment);
                    startActivity(intent);
                }
            } else if (investment.bType == 0) {
                Intent intent = new Intent(this, InvestmentDetailsActivity.class);
                intent.putExtra(Investment.class.getName(), investment);
                intent.putExtra(InvestmentDetailsType.class.getName(), InvestmentDetailsType.MyInvestment);
                startActivity(intent);
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.imgBtn_next:// 帮助按钮
                //跳转到帮助按钮
                // TODO 跳转到帮助按钮
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_DQB_CALC);
                intent.putExtra("TitleName", "定期标收益计算说明");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        sendMyInvestmentRequest(RefreshType.RefreshPull);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        sendMyInvestmentRequest(RefreshType.RefreshPull);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isBack) {
            mPage = 1;
            sendMyInvestmentRequest(RefreshType.RefreshPull);
            isBack = false;
        }

    }

    public static void isGetBack(boolean isGetBack) {
        isBack = isGetBack;
    }

    /**
     * 发送我的投资请求
     *
     * @param refreshType
     */
    private void sendMyInvestmentRequest(RefreshType refreshType) {
        mRefreshType = refreshType;

        RequestParams params = new RequestParams(Constant.PLANINVEST_MYINVESTMENT);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("pageNum", mPage + "");
        params.addBodyParameter("size", "10");
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPage == 1) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        noNetAndData(this, errorContent, mInvestments);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPage == 1) {
                mInvestments.clear();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        String investmentLog = jsonObject.getString("investmentLog");

        mInvestments.addAll(JSON.parseArray(investmentLog, Investment.class));

        if (mPage == 1) {
            double nrcyIncome = jsonObject.getDouble("nrcyIncome");// 待获收益
            double nrcyPrinciple = jsonObject.getDouble("nrcyPrinciple");// 待收本金
            udtvNrcyIncome.setDownTextContent(MoneyFormatUtil.format(nrcyIncome));
            udtvNrcyPrinciple.setDownTextContent(MoneyFormatUtil.format(nrcyPrinciple));
        }

        if (mPage != 1 && loadNumber == mInvestments.size()) {   //如果当前加载条数等于最后一次记录加载条数，代表数据已加载完成
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.LOADED);
        }
        if (mAdapter == null) {
            mAdapter = new InvestmentLogListAdapter<Investment>(this, mInvestments);
            lvInvestment.setAdapter(mAdapter);
        } else {
            if (mPage == 1) {
                lvInvestment.setAdapter(mAdapter);
            }
            mAdapter.notifyDataSetChanged();
        }

        // 服务器时间
        Long sysdate = jsonObject.getLong("sysDate");

        for (Investment investment : mInvestments) {
            if (investment.bStates < 4) {// 是否是小于投标中的状态，也就是倒计时状态
                investment.bStates = 4;// 将倒计时状态改为投标中（我的投资中不存在倒计时的标）
            }

            String startDate = TimeUtil.getTimeType2(investment.bCountdown);
            String countDownDay = TimeUtil.getTimeType2(investment.computeDate);
            String endDate = TimeUtil.getTimeType2(sysdate);

            try {
                // 计算以还款天数
                investment.bRepaymentDay = TimeUtil.daysBetween(startDate, endDate);
                investment.bCountDownDay = TimeUtil.daysBetween(endDate, countDownDay);
                if (investment.bRepaymentDay < 0) {
                    investment.bRepaymentDay = 0;
                }
                if (investment.bCountDownDay < 0) {
                    investment.bCountDownDay = 0;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        loadNumber = mInvestments.size();   //记录最后一次加载条数
        sortInvestment();
        noNetAndData(this, jsonBase, mInvestments);
    }

    /**
     * 排序投资数据
     */
    private void sortInvestment() {
        // (1~3是倒计时，4是投标中，5~6是满标，7是还款中，8是已还清)
        List<Investment> investmentDJS = new ArrayList<Investment>();
        List<Investment> investmentTZZ = new ArrayList<Investment>();
        List<Investment> investmentMB = new ArrayList<Investment>();
        List<Investment> investmentHKZ = new ArrayList<Investment>();
        List<Investment> investmentYHQ = new ArrayList<Investment>();
        for (int i = 0; i < mInvestments.size(); i++) {
            switch (mInvestments.get(i).bStates) {
                case 1:// 倒计时
                case 2:// 倒计时
                case 3:// 倒计时
                    investmentDJS.add(mInvestments.get(i));
                    break;
                case 4:// 投标中
                    investmentTZZ.add(mInvestments.get(i));
                    break;
                case 5:// 满标
                case 6:// 满标
                    investmentMB.add(mInvestments.get(i));
                    break;
                case 7:// 还款中
                    investmentHKZ.add(mInvestments.get(i));
                    break;
                case 8:// 已还清
                    investmentYHQ.add(mInvestments.get(i));
                    break;
            }
        }
        mInvestments.clear();
        mInvestments.addAll(investmentTZZ);
        mInvestments.addAll(investmentMB);
        mInvestments.addAll(bubbleSort(investmentHKZ));
//		mInvestments.addAll(investmentDJS);
        mInvestments.addAll(investmentYHQ);
    }

    /**
     * 冒泡排序(倒计时天数由小到大)
     *
     * @param investmentHKZ
     */
    private List<Investment> bubbleSort(List<Investment> investmentHKZ) {
        for (int i = 0; i < investmentHKZ.size() - 1; i++) {
            for (int j = 0; j < investmentHKZ.size() - 1 - i; j++) {
                Investment investment = null;
                if (investmentHKZ.get(j).bCountDownDay > investmentHKZ.get(j + 1).bCountDownDay) {
                    investment = investmentHKZ.get(j + 1);
                    investmentHKZ.set(j + 1, investmentHKZ.get(j));
                    investmentHKZ.set(j, investment);
                }
            }
        }
        return investmentHKZ;
    }
}

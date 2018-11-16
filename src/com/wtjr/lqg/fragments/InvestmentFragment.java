package com.wtjr.lqg.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.HaveAcountsLoginActivity;
import com.wtjr.lqg.activities.InvestmentDetailsActivity;
import com.wtjr.lqg.activities.LiCaiPlanInvestmentDetailActivity;
import com.wtjr.lqg.activities.LoginActivity;
import com.wtjr.lqg.activities.NewBorrowDetailsActivity;
import com.wtjr.lqg.activities.WebViewActivity;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.NewBorrow;
import com.wtjr.lqg.base.PlanMasData;
import com.wtjr.lqg.basecommonly.BaseFragment;
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

import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYong on 2017/11/8.
 */
public class InvestmentFragment extends BaseFragment implements OnClickListener, HttpRequesListener, OnItemClickListener, OnRefreshListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;

    /**
     * 投资列表view
     */
    private PullableListView lvInvestment;


    /**
     * 保存投资条目
     */
    private List<Investment> mInvestments = new ArrayList<Investment>();

    /**
     * 新手标
     */
    private View investmentXSB;
    /**
     * 周周升
     */
    private View investmentLiCaiPlan;

    /**
     * 控制滑动时候的背景色的
     */
    private LinearLayout llTitleBg;


    private InvestmentAdapter<Investment> mAdapter;

    /**
     * 下拉刷新最外层
     */
    private PullToRefreshLayout pullToRefreshLayout;

    /**
     * 标的状态有，投标中，满标，倒计时，还款中，已还清
     */
    private TextView tvBidState;
    /**
     * 新手标标的状态有，投标中，满标，倒计时，还款中，已还清
     */
    private TextView newTvBidState;

    /**
     * 标的利率
     */
    private TextView tvBidRate;

    /**
     * 利率后面的百分号
     */
    private TextView tvPerCen;
    /**
     * 新手标利率后面的百分号
     */
    private TextView newTvPerCen;

    /**
     * 进度条
     */
    private ProgressBar pbInvestmentProgress;
    /**
     * 进度条
     */
    private ProgressBar newPbInvestmentProgress;

    /**
     * 进度条百分比
     */
    private TextView tvInvestmentProgress;
    /**
     * 进度条百分比
     */
    private TextView newTvInvestmentProgress;

    private WakeLock wakeLock;
    private View tittleBottomLine;
    /**
     * 记录加载条数
     */
    private int loadNumber = 0;
    /**
     * 新手标数据
     */
    private NewBorrow newBorrow;
    /**
     * 服务器时间
     */
    private Long sysdate;
    /**
     * 新手标标名
     */
    private TextView newTvBidName;
    /**
     * 投资期限
     */
    private UpDownTextView udtvInvestmentHorizon;
    /**
     * 新手标投资期限
     */
    private UpDownTextView newUdtvInvestmentHorizon;
    /**
     * 新手标标的利率
     */
    private TextView newTvBidRate;
    /**
     * 新手标融资金额
     */
    private UpDownTextView newUdtvFinancingMoney;
    /**
     * 新手标倒计时
     */
    private CountDownTimer mNewBorrowTimer;
    /**
     * 新手标卡片父布局
     */
    private RelativeLayout newLll;
    /**
     * 周周升数据
     */
    private PlanMasData planMasData;
    /**
     * 周周升卡片父布局
     */
    private RelativeLayout planMasLll;
    /**
     * 周周升标名
     */
    private TextView planMasTvBidName;
    /**
     * 周周升投资期限
     */
    private UpDownTextView planMasUdtvInvestmentHorizon;
    /**
     * 周周升标的利率
     */
    private TextView planMasTvBidRate;
    /**
     * 周周升融资金额
     */
    private UpDownTextView planMasUdtvFinancingMoney;
    /**
     * 周周升倒计时
     */
    private CountDownTimer planMasBorrowTimer;
    /**
     * 进度条
     */
    private ProgressBar planMasPbInvestmentProgress;
    /**
     * 进度条百分比
     */
    private TextView planMasTvInvestmentProgress;
    /**
     * 周周升标的状态有，投标中，满标，倒计时，还款中，已还清
     */
    private TextView planMasTvBidState;
    /**
     * 周周升利率后面的百分号
     */
    private TextView planMasTvPerCen;
    /**
     * 加息父布局
     */
    private RelativeLayout rlAddRate;
    /**
     * 加息利率
     */
    private TextView tvAddRate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_investment, container, false);

        start(view);

        PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        // * FULL_WAKE_LOCK 保持屏幕全亮、键盘背光灯点亮和CPU运行。
        // * SCREEN_BRIGHT_WAKE_LOCK 保持屏幕全亮和CPU运行。
        // * SCREEN_DIM_WAKE_LOCK 保持屏幕开启（但是让它变暗）和CPU运行。
        // * PARTIAL_WAKE_LOCK 保持CPU运行。
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");// 保持CPU运行是为了保证倒计时一直运行
        wakeLock.acquire();

        return view;
    }

    @Override
    public void findViewById(View view) {
        //新手标
        investmentXSB = LayoutInflater.from(getActivity()).inflate(R.layout.investment_xsb, null);

        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pullTo_refreshLayout);
        tvTitleName = (TextView) view.findViewById(R.id.tv_title_name);
        lvInvestment = (PullableListView) view.findViewById(R.id.pullable_listView);
        llTitleBg = (LinearLayout) view.findViewById(R.id.ll_title_bg);
        tittleBottomLine = view.findViewById(R.id.title_bottom_line);
        newLll = (RelativeLayout) investmentXSB.findViewById(R.id.ll_xsb);
        newTvBidName = (TextView) investmentXSB.findViewById(R.id.tv_bid_name);
        newUdtvInvestmentHorizon = (UpDownTextView) investmentXSB.findViewById(R.id.udtv_investment_horizon);
        newTvBidRate = (TextView) investmentXSB.findViewById(R.id.tv_bid_rate);
        newUdtvFinancingMoney = (UpDownTextView) investmentXSB.findViewById(R.id.udtv_financing_money);
        // 投资进度条
        newPbInvestmentProgress = (ProgressBar) investmentXSB.findViewById(R.id.pb_investment_progress);
        // 投资进度百分比
        newTvInvestmentProgress = (TextView) investmentXSB.findViewById(R.id.tv_investment_progress);
        // 标的状态有，投标中，满标，倒计时，还款中，已还清
        newTvBidState = (TextView) investmentXSB.findViewById(R.id.tv_bid_state);
        // 利率后面的百分号
        newTvPerCen = (TextView) investmentXSB.findViewById(R.id.tv_per_cen);

        //初始化周周升
        initDataLiCaiPlan();
    }

    /**
     * 初始化周周升
     */
    private void initDataLiCaiPlan() {
        //周周升
        investmentLiCaiPlan = LayoutInflater.from(getActivity()).inflate(R.layout.investment_dingqi, null);
        planMasLll = (RelativeLayout) investmentLiCaiPlan.findViewById(R.id.ll_xsb);
        planMasTvBidName = (TextView) investmentLiCaiPlan.findViewById(R.id.tv_bid_name);
        planMasUdtvInvestmentHorizon = (UpDownTextView) investmentLiCaiPlan.findViewById(R.id.udtv_investment_horizon);
        planMasTvBidRate = (TextView) investmentLiCaiPlan.findViewById(R.id.tv_bid_rate);
        planMasUdtvFinancingMoney = (UpDownTextView) investmentLiCaiPlan.findViewById(R.id.udtv_financing_money);
        // 投资进度条
        planMasPbInvestmentProgress = (ProgressBar) investmentLiCaiPlan.findViewById(R.id.pb_investment_progress);
        // 投资进度百分比
        planMasTvInvestmentProgress = (TextView) investmentLiCaiPlan.findViewById(R.id.tv_investment_progress);
        // 标的状态有，投标中，满标，倒计时，还款中，已还清
        planMasTvBidState = (TextView) investmentLiCaiPlan.findViewById(R.id.tv_bid_state);
        // 利率后面的百分号
        planMasTvPerCen = (TextView) investmentLiCaiPlan.findViewById(R.id.tv_per_cen);
//        加息控件父布局
        rlAddRate = (RelativeLayout) investmentLiCaiPlan.findViewById(R.id.rl_add_rate);
        tvAddRate = (TextView) investmentLiCaiPlan.findViewById(R.id.tv_add_rate);

    }

    @Override
    public void setListener() {
        // 网络监听
        mHttpUtil.setHttpRequesListener(this);
        // 列表点击监听
        lvInvestment.setOnItemClickListener(this);
        // 下拉刷新监听
        pullToRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:投资
        tvTitleName.setText(R.string.investment_name);
        tittleBottomLine.setVisibility(View.GONE);
    }

    /**
     * 记录第一次点击的时间
     */
    private long clickTime = 0;

    @Override
    public void refreshData(RefreshType refreshType) {
        // 先判断控件是否有null
        if (getActivity() == null || lvInvestment == null || pullToRefreshLayout == null) {
            return;
        }

        switch (refreshType) {
            case RefreshPull:
                // 那就自动滚动到顶部
                lvInvestment.smoothScrollToPosition(0);

                if (PullToRefreshLayout.TRIGGER && pullToRefreshLayout.getHeadHeight() == 0) {
                    if ((System.currentTimeMillis() - clickTime) > 2000) {
                        if (pullToRefreshLayout != null) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pullToRefreshLayout.autoRefresh();
                                }
                            }, 500);
                        }
                        clickTime = System.currentTimeMillis();
                    }
                }
                break;

            case RefreshNoPull:
                sendInvestmentRequest(RefreshType.RefreshNoPull);
                break;
        }
    }

    @Override
    public void initData() {

        try {
            List<Investment> investments = app.getDb().selector(Investment.class).orderBy("bCountdown", true).findAll();// 以投标时间的降序排列
            if (investments != null) {
                for (int i = 0; i < investments.size(); i++) {
                    // 将数据库中的倒计时标转换成不合法时间从而达到断网倒计时的标无法投的效果
                    switch (investments.get(i).bStates) {
                        case 1:
                        case 2:
                        case 3:
                            investments.get(i).timer = -1;
                            break;
                    }
                }
                mInvestments.addAll(investments);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }

        mAdapter = new InvestmentAdapter<Investment>(getActivity(), mInvestments);
        lvInvestment.setAdapter(mAdapter);

        try {
            newBorrow = app.getDb().selector(NewBorrow.class).findFirst();// 以投标时间的降序排列
            if (newBorrow != null) {
                // 将数据库中的倒计时标转换成不合法时间从而达到断网倒计时的标无法投的效果
                switch (newBorrow.bStates) {
                    case 1:
                    case 2:
                    case 3:
                        newBorrow.timer = -1;
                        break;
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (newBorrow != null) {
            setNewBorrowData(newBorrow);
        }
        addHeader();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_investment_banner:// 没登录时候投资头部的banner
                if (isLogin) {
                    Intent intent2 = new Intent(getActivity(), WebViewActivity.class);
                    intent2.putExtra("TitleName", "新手活动");
                    intent2.putExtra("url", Constant.INTERFACE_ADDRESS + "/hfivepage/h5.do?uid=" + mUid + "&code=123456");
                    startActivity(intent2);
                } else {
                    if (!mFirstInState) {// 不是第一次使用（有账号登录过）
                        startActivity(new Intent(getActivity(), HaveAcountsLoginActivity.class));
                    } else {// 不是第一次使用（没有账号登录过）
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                }
                break;

            default:
                break;
        }
    }

    // --------------------------------------------Adapter-------------------------------------------------
    class InvestmentAdapter<T> extends NewBaseAdapter<T> {

        public InvestmentAdapter(Context context, List<T> list) {
            super(context, list);
        }

        @Override
        public int getContentView() {
            return R.layout.item_investment;
        }

        @Override
        public void onInitView(View view, int position) {
            Investment item = (Investment) getItem(position);

            // 标名称
            TextView tvBidName = getViewChild(view, R.id.tv_bid_name);
            tvBidName.setText(item.bName);
            // 标的状态有，投标中，满标，倒计时，还款中，已还清
            tvBidState = getViewChild(view, R.id.tv_bid_state);
            // 标的利率
            tvBidRate = getViewChild(view, R.id.tv_bid_rate);
            tvBidRate.setText(MoneyFormatUtil.format2(item.bYearRate) + "");
            // 利率后面的百分号
            tvPerCen = getViewChild(view, R.id.tv_per_cen);
            // 投资期限
            udtvInvestmentHorizon = getViewChild(view, R.id.udtv_investment_horizon);
            udtvInvestmentHorizon.setUpTextContent(item.bLoanDays + "天");
            // 融资金额
            UpDownTextView udtv_financing_money = getViewChild(view, R.id.udtv_financing_money);
            udtv_financing_money.setUpTextContent(MoneyFormatUtil.formatW2(item.bLoanMoney));
            // 投资进度条
            pbInvestmentProgress = getViewChild(view, R.id.pb_investment_progress);
            // 投资进度百分比
            tvInvestmentProgress = getViewChild(view, R.id.tv_investment_progress);

            int bStates = item.bStates;
            String investmentProgress = "0.0";
            switch (bStates) {
                case 1:
                case 2:
                case 3:
                    if (item.timer < -1) {
                        setBidState("投标中", 1);
                        item.bStates = 4;
                        mInvestments.set(position, item);
                        investmentProgress = (int) (item.bInvestedMoney * 100d / item.bLoanMoney) + "";
                    } else {
                        setBidState("倒计时", 1);
                        if (item.timer == -1) {
                            tvBidState.setText("-1");
                        } else {
                            if (item.timer > 1800) {     //如果大于30分钟显示开始，小于等于显示倒计时
                                tvBidState.setText(TimeUtil.getH_M(item.bCountdown * 1000 / 1000) + "开始");
                            } else {
                                tvBidState.setText(TimeUtil.formatSecond(item.timer * 1000 / 1000));
                            }

                        }
                        investmentProgress = "0";
                    }
                    break;
                case 4:
                    setBidState("投标中", 1);
                    // 投资进度
                    investmentProgress = (int) (item.bInvestedMoney * 100d / item.bLoanMoney) + "";
                    break;
                case 5:
                case 6:
                    setBidState("满标", 1);
                    investmentProgress = "100";
                    break;
                case 7:
                    setBidState("还款中", 1);
                    investmentProgress = "100";
                    break;
                case 8:
                    setBidState("已还清", 1);
                    investmentProgress = "100";
                    break;
            }

            setInvestmentProgress(investmentProgress, 1);

            // 判断有没有正在倒计时的Item
            for (int index = 0; index < mInvestments.size() && mTimer == null; index++) {
                Investment investment = mInvestments.get(index);
                int timeM = investment.timer;
                if (timeM > 0) {// 有倒计时的Item
                    startTimer();// 启动倒计时
                    break;
                }
            }
        }
    }

    private CountDownTimer mTimer;

    private RefreshType mRefreshType;

    private int mPageNum = 1;

    /**
     * 启动倒计时
     */
    private void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        mTimer = new CountDownTimer(Integer.MAX_VALUE, 1000) {
            public void onTick(long millisUntilFinished) {
                // 是否需要倒计时
                boolean isNeedCountTime = false;
                // ①：其实在这块需要精确计算当前时间
                for (int index = 0; index < mInvestments.size(); index++) {
                    Investment investment = mInvestments.get(index);
                    int timeM = investment.timer;
                    if (timeM >= 1) {// 判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                        // isNeedCountTime = true;
                        investment.timer = timeM - 1;
                        isNeedCountTime = true;
                    } else {// 倒计时结束（判断是否是倒计时的标，是转换成投标状态）
                        switch (investment.bStates) {
                            case 1:// 倒计时状态
                            case 2:// 倒计时状态
                            case 3:// 倒计时状态
                                investment.bStates = 4; // 转换成投标状态
                                try {
                                    app.getDb().saveOrUpdate(investment);// 更新数据库中的该条数据

                                } catch (DbException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }

                if (!isNeedCountTime) {// 没有需要倒计时的了
                    stopTimer();
                }

                mAdapter.notifyDataSetChanged();
            }

            public void onFinish() {
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
     * 启动新手标倒计时
     */
    private void startNewBorrowTimer() {
        if (mNewBorrowTimer != null) {
            mNewBorrowTimer.cancel();
            mNewBorrowTimer = null;
        }

        mNewBorrowTimer = new CountDownTimer(newBorrow.timer * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                // 是否需要倒计时
                boolean isNewBorrowCountTime = false;
                int timeM = newBorrow.timer;
                if (timeM >= 1) {// 判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                    newBorrow.timer = timeM - 1;
                    isNewBorrowCountTime = true;

                    if (newBorrow.timer > 1800) {        //如果大于30分钟显示开始，小于等于显示倒计时
                        newTvBidState.setText(TimeUtil.getH_M(newBorrow.bCountdown * 1000 / 1000) + "开始");
                    } else {
                        newTvBidState.setText(TimeUtil.formatSecond(millisUntilFinished / 1000));
                    }
                    try {
                        app.getDb().saveOrUpdate(newBorrow);// 更新数据库中的该条数据

                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }

                if (!isNewBorrowCountTime) {// 没有需要倒计时的了
                    stopNewBorrowTimer();
                }
            }

            public void onFinish() {
                setBidState("投标中", 2);
                // 投资进度
                String investmentProgress = (int) (newBorrow.bInvestedMoney * 100d / newBorrow.bLoanMoney) + "";
                setInvestmentProgress(investmentProgress, 2);
            }

        }.start();
    }

    /**
     * 停止新手标倒计时
     */
    private void stopNewBorrowTimer() {
        if (mNewBorrowTimer != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 延迟取消倒计时。否则取消不了
                    mNewBorrowTimer.cancel();
                    mNewBorrowTimer = null;
                }
            }, 10);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isLogin) {// 没账号登录
            if (!mFirstInState) {// 不是第一次使用（有账号登录过）
                startActivity(new Intent(getActivity(), HaveAcountsLoginActivity.class));
            } else {// 不是第一次使用（没有账号登录过）
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
            return;
        }
        if (position == 0) {// 周周升
            if (planMasData != null) {
                Intent intent = new Intent(getActivity(), LiCaiPlanInvestmentDetailActivity.class);
                intent.putExtra(PlanMasData.class.getName(), planMasData);
                intent.putExtra(InvestmentDetailsType.class.getName(), InvestmentDetailsType.Investment);
                startActivity(intent);
            }
            return;
        } else if (position == 1) { //点击的是新手标
            if (newBorrow != null) {
                Intent intent = new Intent(getActivity(), NewBorrowDetailsActivity.class);
                intent.putExtra(NewBorrow.class.getName(), newBorrow);
                intent.putExtra(InvestmentDetailsType.class.getName(), InvestmentDetailsType.Investment);
                startActivity(intent);
            }
            return;
        }
        position = position - 2;

        Investment investment = mInvestments.get(position);
        Intent intent = new Intent(getActivity(), InvestmentDetailsActivity.class);
        intent.putExtra(Investment.class.getName(), investment);
        intent.putExtra(InvestmentDetailsType.class.getName(), InvestmentDetailsType.Investment);
        startActivity(intent);
    }

    /**
     * 设置标状态
     *
     * @param bidState    状态(满标,投标中,还款中,已还清,倒计时)
     * @param bInvestment (1为普通标，2为新手标,3周周升)
     */
    private void setBidState(String bidState, int bInvestment) {
        if (bInvestment == 1) {
            // 倒梯形状态背景色
            tvBidState.setBackgroundResource(BidStateUtil.setBidStateImageUtil(bidState));
            // 内容
            tvBidState.setText(bidState);
        } else if (bInvestment == 2) {
            // 倒梯形状态背景色
            newTvBidState.setBackgroundResource(BidStateUtil.setBidStateImageUtil(bidState));
            // 内容
            newTvBidState.setText(bidState);
        } else if (bInvestment == 3) {
            // 倒梯形状态背景色
            planMasTvBidState.setBackgroundResource(BidStateUtil.setBidStateImageUtil(bidState));
            // 内容
            planMasTvBidState.setText(bidState);
        }

        setBidStateTextColor(bidState, bInvestment);

        setBidStateProgressDrawable(bidState, bInvestment);
    }

    /**
     * 设置标状态字体色
     *
     * @param bidState (1为普通标，2为新手标,3周周升)
     */
    private void setBidStateTextColor(String bidState, int bInvestment) {
        int color = BidStateUtil.getBidStateTextColor(bidState);
        if (bInvestment == 1) {
            tvPerCen.setTextColor(color);
        } else if (bInvestment == 2) {
            newTvPerCen.setTextColor(color);
        } else if (bInvestment == 3) {
            planMasTvPerCen.setTextColor(color);
        }
    }

    /**
     * 更具状态设置进度的颜色
     *
     * @param bidState 状态(1为普通标，2为新手标,3周周升)
     */
    private void setBidStateProgressDrawable(String bidState, int bInvestment) {
        Drawable d = BidStateUtil.getBidStateProgressDrawable(getActivity(), bidState);
        if (bInvestment == 1) {
            pbInvestmentProgress.setProgressDrawable(d);
        } else if (bInvestment == 2) {
            newPbInvestmentProgress.setProgressDrawable(d);
        } else if (bInvestment == 3) {
            planMasPbInvestmentProgress.setProgressDrawable(d);

        }
    }

    /**
     * 设置投资进度
     *
     * @param progress 状态(1为普通标，2为新手标,3周周升)
     */
    private void setInvestmentProgress(String progress, int bInvestment) {
        if (bInvestment == 1) {
            pbInvestmentProgress.setProgress((int) Double.parseDouble(progress));
            tvInvestmentProgress.setText(progress + "%");
        } else if (bInvestment == 2) {
            newPbInvestmentProgress.setProgress((int) Double.parseDouble(progress));
            newTvInvestmentProgress.setText(progress + "%");

        } else if (bInvestment == 3) {
            planMasPbInvestmentProgress.setProgress((int) Double.parseDouble(progress));
            planMasTvInvestmentProgress.setText(progress + "%");

        }
    }


    /**
     * 添加头
     */
    private void addHeader() {
        lvInvestment.setAdapter(null);
        lvInvestment.removeHeaderView(investmentLiCaiPlan);
        lvInvestment.removeHeaderView(investmentXSB);
        lvInvestment.addHeaderView(investmentLiCaiPlan);
        lvInvestment.addHeaderView(investmentXSB);
        lvInvestment.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mPageNum = 1;
        pullToRefreshLayout.currentState = 0;
        sendInvestmentRequest(RefreshType.RefreshPull);
    }

    @Override
    public void onLoadMore() {
        mPageNum++;
        sendInvestmentRequest(RefreshType.RefreshPull);
    }

    /**
     * 投资列表的请求
     */
    private void sendInvestmentRequest(RefreshType type) {
        mRefreshType = type;
        RequestParams params = new RequestParams(Constant.INVESTMENT_REFRESHINVESTMENT);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("pageNum", mPageNum + "");
        params.addBodyParameter("pageSize", "20");
        mHttpUtil.sendRequest(params, type, getActivity());
    }


    @Override
    public void onFailure(String url, String errorContent) {
        if (mRefreshType == RefreshType.RefreshPull) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        }

        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPageNum == 1) {
                pullToRefreshLayout.currentState = 0;
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (mInvestments.size() > 0) {
                    lvInvestment.setPullLoadEnable(true);
                } else {
                    lvInvestment.setPullLoadEnable(false);
                }
            } else {
                mPageNum--;
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPageNum == 1) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        if (mPageNum == 1) {
            mInvestments.clear();
        }
        String b_list = jsonObject.getString("b_list");
        // 服务器时间
        sysdate = jsonObject.getLong("sysdate");
        //新手标
        newBorrow = JSON.parseObject(jsonObject.getString("newBorrow"), NewBorrow.class);
        //周周升
        planMasData = JSON.parseObject(jsonObject.getString("planMas"), PlanMasData.class);

        mInvestments.addAll(JSON.parseArray(b_list, Investment.class));

        // 计算倒计时的时长
        for (Investment investment : mInvestments) {
            // 毫秒转换成秒
            investment.timer = (int) ((investment.bCountdown - sysdate) / 1000) + 1;
            if (investment.timer <= 0 && (investment.bStates >= 1 && investment.bStates <= 3)) {
                investment.bStates = 4;
            }

        }
        if (newBorrow != null) {
            // 毫秒转换成秒
            newBorrow.timer = (int) ((newBorrow.bCountdown - sysdate) / 1000) + 1;
            if (newBorrow.timer <= 0 && (newBorrow.bStates >= 1 && newBorrow.bStates <= 3)) {
                newBorrow.bStates = 4;
            }
        }
        /**
         * 排序投资数据
         */
        sortInvestment();

        if (mPageNum != 1 && loadNumber == mInvestments.size()) {   //如果当前加载条数等于最后一次记录加载条数，代表数据已加载完成
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.LOADED);
        }
        if (mAdapter == null) {
            mAdapter = new InvestmentAdapter<Investment>(getActivity(), mInvestments);
            lvInvestment.setAdapter(mAdapter);
        } else {
            if (mPageNum == 1) {
                lvInvestment.setAdapter(mAdapter);
            }
            mAdapter.notifyDataSetChanged();

        }

        if (newBorrow == null) {
            newLll.setVisibility(View.GONE);
        } else {
            newLll.setVisibility(View.VISIBLE);
        }
        if (planMasData == null) {
            planMasLll.setVisibility(View.GONE);
        } else {
            planMasLll.setVisibility(View.VISIBLE);
        }
        loadNumber = mInvestments.size();   //记录最后一次加载条数
        saveData(mInvestments);
        if (newBorrow != null) {
            setNewBorrowData(newBorrow);
            saveNewBorrowData(newBorrow);
        }
        if (planMasData != null) {
            setPlanMasData(planMasData);
        }
        mMainActivity.noNetAndData(getActivity(), jsonBase, mInvestments);
    }

    /**
     * 设置新手标数据
     *
     * @param newBorrow 新手标数据
     */
    private void setNewBorrowData(NewBorrow newBorrow) {
        //新手标标名

        newTvBidName.setText(newBorrow.bName);
        //新手标投资期限
        newUdtvInvestmentHorizon.setUpTextContent(newBorrow.bLoanDays + "天");
        //新手标利率
//        newTvBidRate.setText(MoneyFormatUtil.format2(newBorrow.bYearRate) + "");
        newTvBidRate.setText("11.00");
        // 新手标融资金额
        newUdtvFinancingMoney.setUpTextContent(MoneyFormatUtil.formatW2(newBorrow.bLoanMoney));
        int bStates = newBorrow.bStates;
        String investmentProgress = "0.0";
        switch (bStates) {
            case 1:
            case 2:
            case 3:
                if (newBorrow.timer < -1) {
                    setBidState("投标中", 2);
                    newBorrow.bStates = 4;
                    investmentProgress = (int) (newBorrow.bInvestedMoney * 100d / newBorrow.bLoanMoney) + "";
                } else {
                    setBidState("倒计时", 2);
                    if (newBorrow.timer == -1) {
                        newTvBidState.setText("-1");
                    } else {
                        if (newBorrow.timer > 1800) {     //如果大于30分钟显示开始，小于等于显示倒计时
                            newTvBidState.setText(TimeUtil.getH_M(newBorrow.bCountdown * 1000 / 1000) + "开始");
                        } else {
                            newTvBidState.setText(TimeUtil.formatSecond(newBorrow.timer * 1000 / 1000));
                        }

                    }
                    investmentProgress = "0";
                }
                break;
            case 4:
                setBidState("投标中", 2);
                // 投资进度
                investmentProgress = (int) (newBorrow.bInvestedMoney * 100d / newBorrow.bLoanMoney) + "";
                break;
            case 5:
            case 6:
                setBidState("满标", 2);
                investmentProgress = "100";
                break;
            case 7:
                setBidState("还款中", 2);
                investmentProgress = "100";
                break;
            case 8:
                setBidState("已还清", 2);
                investmentProgress = "100";
                break;
        }

        setInvestmentProgress(investmentProgress, 2);
        if (newBorrow.timer > 0) {// 新手标有倒计时时
            startNewBorrowTimer();// 启动新手标倒计时
        }
    }

    /**
     * 设置周周升数据
     *
     * @param planmasdata 周周升数据
     */
    private void setPlanMasData(PlanMasData planmasdata) {
        //周周升标名
        planMasTvBidName.setText(planmasdata.planName);
        //周周升投资期限
        planMasUdtvInvestmentHorizon.setUpTextContent(planMasData.lockDays + "-" + planMasData.intPeriodDays + "天");
        //周周升利率
        planMasTvBidRate.setText(MoneyFormatUtil.format2(planmasdata.baseRate) + "-" + MoneyFormatUtil.format2(planmasdata.maxRate));
        // 周周升融资金额
        planMasUdtvFinancingMoney.setUpTextContent(MoneyFormatUtil.formatW2(planmasdata.preCollectAmount));

        //加息利率(0不加息、1加息)
        if (planmasdata.isAddRate == 1) {
            rlAddRate.setVisibility(View.VISIBLE);
            tvAddRate.setText("+" + MoneyFormatUtil.formatW(planMasData.addRate)+ "%");
        } else {
            rlAddRate.setVisibility(View.GONE);
        }


        int bStates = planmasdata.status;
        String investmentProgress = "0.0";
        switch (bStates)

        {
            case 1:
            case 2:
            case 3:
                break;
            case 4:
                setBidState("募集中", 3);
                // 投资进度
                investmentProgress = (int) (planmasdata.factCollectAmount * 100d / planmasdata.preCollectAmount) + "";
                break;
            case 5:
            case 6:
                setBidState("已募满", 3);
                investmentProgress = "100";
                break;
            case 7:
                setBidState("还款中", 3);
                investmentProgress = "100";
                break;
            case 8:
                setBidState("已还清", 3);
                investmentProgress = "100";
                break;
        }

        setInvestmentProgress(investmentProgress, 3);

    }

    /**
     * 数据库保存数据
     */
    private void saveData(List<Investment> investments) {
        try {
            app.getDb().delete(Investment.class);
            app.getDb().save(investments);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新手标数据库保存数据
     */
    private void saveNewBorrowData(NewBorrow newBorrow) {
        try {
            app.getDb().delete(NewBorrow.class);
            app.getDb().save(newBorrow);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        stopTimer();
        stopNewBorrowTimer();
        if (wakeLock != null) {
            wakeLock.release();
        }
        super.onDestroy();
    }

    /**
     * 排序投资数据
     */
    private void sortInvestment() {
        // (1~3是倒计时，4是投标中，5~6是满标，7是还款中，8是已还清)
        List<Investment> investmentTBZ = new ArrayList<Investment>();
        List<Investment> investmentDJS = new ArrayList<Investment>();
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
                    investmentTBZ.add(mInvestments.get(i));
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
        mInvestments.addAll(investmentTBZ);
        mInvestments.addAll(bubbleSort(investmentDJS));
        mInvestments.addAll(investmentMB);
        mInvestments.addAll(investmentHKZ);
        mInvestments.addAll(investmentYHQ);
    }

    /**
     * 冒泡排序(抢标时间开始早的放前边)
     *
     * @param investmentDJS
     */
    private List<Investment> bubbleSort(List<Investment> investmentDJS) {
        for (int i = 0; i < investmentDJS.size() - 1; i++) {
            for (int j = 0; j < investmentDJS.size() - 1 - i; j++) {
                Investment investment = null;
                if (investmentDJS.get(j).bCountdown > investmentDJS.get(j + 1).bCountdown) {
                    investment = investmentDJS.get(j + 1);
                    investmentDJS.set(j + 1, investmentDJS.get(j));
                    investmentDJS.set(j, investment);
                }
            }
        }
        return investmentDJS;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onPageStart("InvestmentFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPageEnd("InvestmentFragment");
    }
}

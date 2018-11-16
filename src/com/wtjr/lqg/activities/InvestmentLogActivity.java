package com.wtjr.lqg.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.InvestmentLog;
import com.wtjr.lqg.base.InvestmentLogTopThree;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.NewBorrow;
import com.wtjr.lqg.base.PlanMasData;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

/**
 * 投资记录
 *
 * @author JoLie
 */
public class InvestmentLogActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnRefreshListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    private View headView;
    private InvestmentLogListAdapter<InvestmentLog> mAdapter;
    private ArrayList<InvestmentLog> investmentLogs = new ArrayList<InvestmentLog>();
    private ArrayList<InvestmentLogTopThree> investmentLogTopThree = new ArrayList<InvestmentLogTopThree>();
    private Investment mInvestment;


    private PullableListView pullableListView;

    /**
     * 下拉刷新最外层
     */
    private PullToRefreshLayout pullToRefreshLayout;
    /**
     * 排名第一
     */
    private TextView tvInvestmentOneName;
    /**
     * 排名第二
     */
    private TextView tvInvestmentTwoName;
    /**
     * 排名第三
     */
    private TextView tvInvestmentThreeName;
    /**
     * 刷新类型（区分是下拉操作的刷新，还是无下拉操作的刷新）
     */
    private RefreshType mRefreshType;
    /**
     * 判断是新手标（true）还是普通标(false)
     */
    private int borrowState;
    /**
     * 排名第一的金额
     */
    private TextView tvInvestmentOneMoney;
    /**
     * 排名第二的金额
     */
    private TextView tvInvestmentTwoMoney;
    /**
     * 排名第三的金额
     */
    private TextView tvInvestmentThreeMoney;
    private InvestmentDetailsType mInvestmentDetailsType;
    private LinearLayout llPaiming;
    private NewBorrow newBorrow;
    /**
     * 周周升
     */
    private PlanMasData planMasData;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_investment_log);
        start();
    }

    @Override
    public void initIntent() {

        borrowState = getIntent().getIntExtra("borrowState", 1);
        if (borrowState == 2) {//新手标过来
            newBorrow = (NewBorrow) getIntent().getSerializableExtra(Investment.class.getName());
        } else if (borrowState == 1) {
            mInvestment = (Investment) getIntent().getSerializableExtra(Investment.class.getName());
        } else if (borrowState == 3) {//周周升
            planMasData = (PlanMasData) getIntent().getSerializableExtra(Investment.class.getName());
        }
        mInvestmentDetailsType = (InvestmentDetailsType) getIntent().getSerializableExtra(InvestmentDetailsType.class.getName());
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

//		listView1 = (ListView) findViewById(R.id.listView1);

        pullableListView = (PullableListView) findViewById(R.id.pullable_listView);

        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);

        headView = getLayoutInflater().inflate(R.layout.head_investment_log, null);
        llPaiming = (LinearLayout) headView.findViewById(R.id.ll_paiming);

        tvInvestmentOneName = (TextView) headView.findViewById(R.id.tv_investment_one_name);
        tvInvestmentTwoName = (TextView) headView.findViewById(R.id.tv_investment_two_name);
        tvInvestmentThreeName = (TextView) headView.findViewById(R.id.tv_investment_three_name);

        tvInvestmentOneMoney = (TextView) headView.findViewById(R.id.tv_investment_one_money);
        tvInvestmentTwoMoney = (TextView) headView.findViewById(R.id.tv_investment_two_money);
        tvInvestmentThreeMoney = (TextView) headView.findViewById(R.id.tv_investment_three_money);

        tvInvestmentOneMoney.setOnClickListener(null);
        tvInvestmentTwoMoney.setOnClickListener(null);
        tvInvestmentThreeMoney.setOnClickListener(null);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
        // 下拉刷新监听
        pullToRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:投资记录
        tvTitleName.setText("投资记录");
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        addHeader();

        //是否可以上拉加载
        pullableListView.setPullLoadEnable(false);

        sendInvestmentLogRequest(RefreshType.RefreshNoPull);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 添加头
     */
    private void addHeader() {
        pullableListView.setAdapter(null);
        pullableListView.removeHeaderView(headView);
        pullableListView.addHeaderView(headView);
        pullableListView.setAdapter(mAdapter);

        //设置轮播适配器
        if (mAdapter == null) {
            mAdapter = new InvestmentLogListAdapter<InvestmentLog>(this, investmentLogs);
            pullableListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 投资记录适配器
     *
     * @param <T>
     * @author JoLie
     */
    class InvestmentLogListAdapter<T> extends NewBaseAdapter<T> {

        public InvestmentLogListAdapter(Context context, List<T> list) {
            super(context, list);
        }

        @Override
        public int getContentView() {
            return R.layout.item_investment_log;
        }

        @Override
        public void onInitView(View view, int position) {
            InvestmentLog investmentLog = (InvestmentLog) getItem(position);

            String time = TimeUtil.getFullTime(investmentLog.it, "yyyy/MM/dd\nHH:mm:ss");

            TextView tvMoney = getViewChild(view, R.id.tv_money);

            tvMoney.setText(MoneyFormatUtil.format(investmentLog.im) + "元");

            TextView tvUsername = getViewChild(view, R.id.tv_username);
            tvUsername.setText(StringUtil.setBlurryPhone(investmentLog.up));


            TextView tvTime = getViewChild(view, R.id.tv_time);
            tvTime.setText(time);

            LinearLayout ll_item = getViewChild(view, R.id.ll_item);
            if (position % 2 == 0) {
                ll_item.setBackgroundColor(Color.parseColor("#f2f2f2"));
            } else {
                ll_item.setBackgroundColor(Color.parseColor("#ffffff"));
            }
        }
    }


    @Override
    public void onRefresh() {
        sendInvestmentLogRequest(RefreshType.RefreshPull);
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
    }


    /**
     * 发送投资记录请求
     */
    public void sendInvestmentLogRequest(RefreshType type) {
        mRefreshType = type;

        RequestParams params = null;
        if (planMasData != null && borrowState == 3) {
            params = new RequestParams(Constant.PLANINVEST_INVESTMENTINFO);
            params.addBodyParameter("plan_code", planMasData.planCode);
        } else {
            if (mInvestmentDetailsType == InvestmentDetailsType.LqbInvestment) {
                params = new RequestParams(Constant.LQB_QUERYMATCHBORROWRECORD);
            } else {
                params = new RequestParams(Constant.INVESTMENT_INVESTMENTINFO);
            }
        }

        params.addBodyParameter("user_userunid", mUid);
        if (mInvestment != null && borrowState == 1) {
            params.addBodyParameter("borrow_id", mInvestment.bId + "");
        }
        if (newBorrow != null && borrowState == 2) {
            params.addBodyParameter("borrow_id", newBorrow.bId + "");
        }
        httpUtil.sendRequest(params, type, this);
    }


    @Override
    public void onFailure(String url, String errorContent) {
        switch (mRefreshType) {
            case RefreshPull:
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
                break;
            case RefreshNoPull:
                break;
        }
        if (errorContent.contains("已下架")) {
            DialogUtil.promptDialog(InvestmentLogActivity.this, DialogUtil.HEAD_SERVICE, "温馨提示", errorContent, new DialogUtil.OnClickYesListener() {
                @Override
                public void onClickYes() {
                    Intent intent = new Intent(InvestmentLogActivity.this, MainActivity.class);
                    startActivity(intent);
                    // 更新前3个界面
                    Intent mIntent = new Intent(Constant.UPDATE_ALL);
                    mIntent.putExtra("ShowFragmentLocation", 2);
                    sendBroadcast(mIntent, Manifest.permission.receiver_permission);
                }
            });
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        switch (mRefreshType) {
            case RefreshPull:
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                break;
            case RefreshNoPull:
                break;
        }

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        String charge_money = jsonObject.getString("charge_money");
        investmentLogs.clear();
        investmentLogTopThree.clear();
        investmentLogs.addAll(JSON.parseArray(charge_money, InvestmentLog.class));
        if (mInvestmentDetailsType != InvestmentDetailsType.LqbInvestment) {
            String borrowRankList = jsonObject.getString("borrowRankList");
            investmentLogTopThree.addAll(JSON.parseArray(borrowRankList, InvestmentLogTopThree.class));
        }
        if ((mInvestment != null && mInvestment.bStates > 4) || (newBorrow != null && newBorrow.bStates > 4) || (planMasData != null && planMasData.status > 4)) {//满标以上的状态

            for (int i = 0; i < investmentLogTopThree.size(); i++) {
                switch (i) {
                    case 0:
                        tvInvestmentOneName.setText(StringUtil.setBlurryPhone(investmentLogTopThree.get(i).up));
                        tvInvestmentOneMoney.setText(MoneyFormatUtil.format(investmentLogTopThree.get(i).im));
                        break;
                    case 1:
                        tvInvestmentTwoName.setText(StringUtil.setBlurryPhone(investmentLogTopThree.get(i).up));
                        tvInvestmentTwoMoney.setText(MoneyFormatUtil.format(investmentLogTopThree.get(i).im));
                        break;
                    case 2:
                        tvInvestmentThreeName.setText(StringUtil.setBlurryPhone(investmentLogTopThree.get(i).up));
                        tvInvestmentThreeMoney.setText(MoneyFormatUtil.format(investmentLogTopThree.get(i).im));
                        break;
                }
            }

        }

        mAdapter.notifyDataSetChanged();
    }

}

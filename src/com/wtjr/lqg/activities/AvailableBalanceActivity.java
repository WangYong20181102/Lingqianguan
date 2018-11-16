package com.wtjr.lqg.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.BalanceDetailsExpandableListAdapter;
import com.wtjr.lqg.base.BalanceDetails;
import com.wtjr.lqg.base.BalanceDetailsTime;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.DatePicker.CustomDatePicker;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableExpandableListView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

import static com.umeng.socialize.utils.DeviceConfig.context;
import static com.wtjr.lqg.R.id.ll;
import static com.wtjr.lqg.R.id.view;
import static com.wtjr.lqg.R.style.dialog;

/**
 * 可用余额
 *
 * @author Myf
 */
public class AvailableBalanceActivity extends BaseActivity implements OnClickListener, OnRefreshListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    private boolean isTitleMenuShow;
    private LinearLayout llTitleMenu;
    private ImageView ivTitle;

    private RadioGroup group1;
    private RadioGroup group2;
    private RadioGroup group3;
    private RadioGroup group4;
    /**
     * 全部
     */
    private RadioButton bt_titleMenu_all;
    /**
     * 提现
     */
    private RadioButton bt_titleMenu_withdraw;
    /**
     * 项目投资
     */
    private RadioButton bt_titleMenu_investment;
    /**
     * 转入零钱
     */
    private RadioButton bt_titleMenu_shiftTo;
    /**
     * 充值
     */
    private RadioButton bt_titleMenu_recharge;
    /**
     * 还本
     */
    private RadioButton bt_titleMenu_repay;
    /**
     * 收益
     */
    private RadioButton bt_titleMenu_earnings;
    /**
     * 零钱宝转出
     */
    private RadioButton bt_titleMenu_rolOut;
    /**
     * 现金奖励
     */
    private RadioButton bt_titleMenu_award;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableExpandableListView pullableExpandableListView;
    private RefreshType mRefreshType;

    /**
     * 是否是第一次进入(为了避免第一次进入后箭头出现偏移旋转)
     */
    private boolean isFirst = true;

    private List<BalanceDetailsTime> balanceDetailsTimes = new ArrayList<BalanceDetailsTime>();
    private List<BalanceDetails> balanceDetails = new ArrayList<BalanceDetails>();
    private BalanceDetailsExpandableListAdapter mAdapter;
    private int mPage = 1;
    private String mDataType;
    private View tittleBottomLine;
    /**
     * 列表头试图中的总额
     */
    private TextView tvExperience;
    /**
     * 列表头试图中的标题
     */
    private TextView tvTypeTitle;
    private View headerView;
    /**
     * 阴影部分
     */
    private View viewHide;
    /**
     * 筛选
     */
    private ImageButton imgBtnNext;
    private CustomDatePicker customDatePicker1;
    /**
     * 筛选体验金的月份
     */
    private String mFilterDate = "three";
    /**
     * 记录加载条数
     */
    private int loadNumber = 0;
    /**
     * 默认显示
     */
    private String type = "";
    /**
     * 显示隐藏充值登录
     */
    private LinearLayout llShowRechange;
    /**
     * 充值
     */
    private LinearLayout llRechange;
    /**
     * 提现
     */
    private LinearLayout llWithDrawal;
    /**
     * 同意
     */
//    private TextView tvXieyi;
//    private Button btnOpen;
//    private RelativeLayout rlLiCai;
//    private TextView tvTyjMoney;
//    private String tyj;
//    private String strContent;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_available_balance);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        llTitleMenu = (LinearLayout) findViewById(R.id.ll_titleMenu);
        tittleBottomLine = findViewById(R.id.title_bottom_line);

        group1 = (RadioGroup) findViewById(R.id.radioGroup1);
        group2 = (RadioGroup) findViewById(R.id.radioGroup2);
        group3 = (RadioGroup) findViewById(R.id.radioGroup3);
        group4 = (RadioGroup) findViewById(R.id.radioGroup4);

        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);
        // 全部
        bt_titleMenu_all = (RadioButton) findViewById(R.id.bt_titleMenu_all);

        bt_titleMenu_withdraw = (RadioButton) findViewById(R.id.bt_titleMenu_withdraw);
        bt_titleMenu_investment = (RadioButton) findViewById(R.id.bt_titleMenu_investment);
        bt_titleMenu_shiftTo = (RadioButton) findViewById(R.id.bt_titleMenu_shiftTo);
        bt_titleMenu_recharge = (RadioButton) findViewById(R.id.bt_titleMenu_recharge);
        bt_titleMenu_repay = (RadioButton) findViewById(R.id.bt_titleMenu_repay);
        bt_titleMenu_earnings = (RadioButton) findViewById(R.id.bt_titleMenu_earnings);
        bt_titleMenu_rolOut = (RadioButton) findViewById(R.id.bt_titleMenu_rolOut);
        bt_titleMenu_award = (RadioButton) findViewById(R.id.bt_titleMenu_award);

        headerView = LayoutInflater.from(this).inflate(R.layout.scroll_availe_banlance_new, null);
        tvExperience = (TextView) headerView.findViewById(R.id.tv_experience);
        tvTypeTitle = (TextView) headerView.findViewById(R.id.tv_typeTitle);
        llShowRechange = (LinearLayout) headerView.findViewById(R.id.ll_available_balance_rechange_withdraw);
        llRechange = (LinearLayout) headerView.findViewById(R.id.ll_rechange);
        llWithDrawal = (LinearLayout) headerView.findViewById(R.id.ll_withdrawal);

        viewHide = findViewById(R.id.view_hide);

        // 标题图标
        ivTitle = (ImageView) findViewById(R.id.image_title);

        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
        // 新建一个ExpandableListView
        pullableExpandableListView = (PullableExpandableListView) findViewById(R.id.pullable_Expandable_ListView);
        //协议
//        tvXieyi = findViewById(R.id.tv_user_xieyi);
//        btnOpen = findViewById(R.id.btn_open);
//        rlLiCai = findViewById(R.id.rl_licai);
//        tvTyjMoney = findViewById(R.id.tv_tyj_money);

    }

    @Override
    public void initIntent() {
        type = getIntent().getStringExtra("totalAssetsType");
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        tvTitleName.setOnClickListener(this);
        ivTitle.setOnClickListener(this);
        pullToRefreshLayout.setOnRefreshListener(this);
        //筛选
        imgBtnNext.setOnClickListener(this);

        httpUtil.setHttpRequesListener(this);
        viewHide.setOnClickListener(this);
        //充值
        llRechange.setOnClickListener(this);
        //提现
        llWithDrawal.setOnClickListener(this);
//        tvXieyi.setOnClickListener(this);
//        btnOpen.setOnClickListener(this);

        setCheckedChangeListener();
    }

    @Override
    public void setTitle() {
        // 设置名字为:可用余额
        tvTitleName.setText(R.string.available_balance_name);
        ivTitle.setVisibility(View.VISIBLE);
        tittleBottomLine.setVisibility(View.GONE);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置头视图金额
        tvExperience.setText(MoneyFormatUtil.format(app.mAccountData.available_money));
    }

    @Override
    public void initData() {
        if (TextUtils.isEmpty(type)) {   //默认全部
            bt_titleMenu_all.setChecked(true);
        } else if (type.equals("5")) {    //项目投资
            bt_titleMenu_investment.setChecked(true);
        } else if (type.equals("3")) {    //转入零钱宝
            bt_titleMenu_shiftTo.setChecked(true);
        }
        imgBtnNext.setVisibility(View.VISIBLE);
        imgBtnNext.setImageResource(R.drawable.filtrate);
        initDatePicker();
    }

    /**
     * 初始化时间筛选器
     */
    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        String now = sdf.format(new Date());

        customDatePicker1 = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                if (time.contains("0001")) {
                    mFilterDate = "three";
                } else {
                    mFilterDate = time;
                }
                mPage = 1;
                sendHttpReques(RefreshType.RefreshPull, mDataType);
            }
        }, "2016-03", now); // 初始化日期格式请用：yyyy-MM，否则不能正常运行
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                if (isTitleMenuShow) {
                    llTitleMenu.setVisibility(View.GONE);
                    isTitleMenuShow = false;
                    showAnimation(ivTitle);
                } else {
                    finish();
                }
                break;

            case R.id.tv_title_name:// 标题名
            case R.id.image_title:
                showOrHideTitleMenu();
                break;
            case R.id.view_hide:// 标题名
                showOrHideTitleMenu();
                break;
            case R.id.ll_rechange:// 充值
                Intent intent = new Intent(AvailableBalanceActivity.this, RechargeActivity.class);
                intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                startActivity(intent);
                break;
            case R.id.ll_withdrawal:// 提现
                startActivity(new Intent(AvailableBalanceActivity.this, WithdrawalActivity.class));
                break;
            case R.id.imgBtn_next:
                // 弹出筛选框，日期格式为yyyy-MM
                customDatePicker1.show(new SimpleDateFormat("yyyy-MM", Locale.CHINA).format(new Date()));
                break;
//            case R.id.tv_user_xieyi:    //协议
//                Intent intent1 = new Intent(this, WebViewActivity.class);
//                intent1.putExtra("url", Constant.PLANBOOK_TOPROTOCOL);
//                intent1.putExtra("TitleName", "周周升服务协议");
//                startActivity(intent1);
//                break;
//            case R.id.btn_open: //预约开启
//                DialogUtil.liCaiPlanSubscribeDialog(this, strContent, tyj, new DialogUtil.OnClickYesListener() {
//                    @Override
//                    public void onClickYes() {
//                        sendBookRequest(RefreshType.RefreshNoPull, "1");
//                    }
//                }, new DialogUtil.OnClickNoListener() {
//                    @Override
//                    public void onClickNo() {
//                        sendBookRequest(RefreshType.RefreshNoPull, "0");
//                    }
//                });
//                break;
            default:
                break;
        }
    }

    /**
     * 预约开启
     *
     * @param refreshNoPull
     */
//    private void sendBookRequest(RefreshType refreshNoPull, String isBook) {
//        RequestParams params = new RequestParams(Constant.PLANBOOK_STARTBOOK);
//        params.addBodyParameter("user_userunid", mUid);
//        params.addBodyParameter("isBook", isBook);
//        httpUtil.sendRequest(params, refreshNoPull, this);
//    }

    /**
     * 显示或隐藏标题菜单
     */
    public void showOrHideTitleMenu() {
        showAnimation(ivTitle);
        if (isTitleMenuShow) {
            llTitleMenu.setVisibility(View.GONE);
            isTitleMenuShow = false;

        } else {
            llTitleMenu.setVisibility(View.VISIBLE);
            isTitleMenuShow = true;

        }
    }

    public void showAnimation(View mView) {
        if (isFirst) {// 避免第一次进入后箭头出现偏移旋转
            isFirst = false;
            return;
        }

        final float centerX = mView.getWidth() / 2.0f;
        final float centerY = mView.getHeight() / 2.0f;
        // 这个是设置需要旋转的角度，我设置的是180度
        if (!isTitleMenuShow) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, 180, centerX, centerY);
            rotateAnimation.setDuration(500);// 旋转时长
            rotateAnimation.setFillAfter(true);
            mView.startAnimation(rotateAnimation);
        } else {
            RotateAnimation rotateAnimation = new RotateAnimation(180, 360, centerX, centerY);
            rotateAnimation.setDuration(500);// 旋转时长
            rotateAnimation.setFillAfter(true);
            mView.startAnimation(rotateAnimation);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isTitleMenuShow) {
                llTitleMenu.setVisibility(View.GONE);
                isTitleMenuShow = false;
                showAnimation(ivTitle);
            } else {
                finish();
            }
        }
        return false;
    }

    private void setCheckedChangeListener() {
        group1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) group.getChildAt(i);
                    if (childAt.isChecked()) {
                        group2.clearCheck();
                        group3.clearCheck();
                        group4.clearCheck();
                    }
                }
                if (bt_titleMenu_all.isChecked()) {
                    setSelect("可用余额", "", bt_titleMenu_all, true);
                }
            }
        });
        group2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) group.getChildAt(i);
                    if (childAt.isChecked()) {
                        group1.clearCheck();
                        group3.clearCheck();
                        group4.clearCheck();
                    }
                }
                if (bt_titleMenu_withdraw.isChecked()) {

                    setSelect("提现", "2", bt_titleMenu_withdraw, true);
                } else if (bt_titleMenu_investment.isChecked()) {
                    setSelect("项目投资", "5", bt_titleMenu_investment, true);
                } else if (bt_titleMenu_shiftTo.isChecked()) {
                    setSelect("转入零钱宝", "3", bt_titleMenu_shiftTo, true);
                }

            }
        });
        group3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) group.getChildAt(i);
                    if (childAt.isChecked()) {
                        group1.clearCheck();
                        group2.clearCheck();
                        group4.clearCheck();
                    }
                }
                if (bt_titleMenu_recharge.isChecked()) {
                    setSelect("充值", "1", bt_titleMenu_recharge, true);
                } else if (bt_titleMenu_repay.isChecked()) {
                    setSelect("还本", "6", bt_titleMenu_repay, true);
                } else if (bt_titleMenu_earnings.isChecked()) {
                    setSelect("收益", "7", bt_titleMenu_earnings, true);
                }

            }
        });
        group4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) group.getChildAt(i);
                    if (childAt.isChecked()) {
                        group1.clearCheck();
                        group2.clearCheck();
                        group3.clearCheck();
                    }
                }
                if (bt_titleMenu_rolOut.isChecked()) {
                    setSelect("零钱宝转出", "4", bt_titleMenu_rolOut, true);
                } else if (bt_titleMenu_award.isChecked()) {
                    setSelect("现金奖励", "12", bt_titleMenu_award, true);
                }
            }
        });
    }

    /**
     * 设置选择项
     *
     * @param title
     */
    private void setSelect(String title, String dataType, RadioButton radioButton, boolean event) {
        if (TextUtils.isEmpty(dataType)) {   //显示隐藏登录注册
            llShowRechange.setVisibility(View.VISIBLE);
        } else {
            llShowRechange.setVisibility(View.GONE);
        }
        httpUtil.closeRequest();

        pullableExpandableListView.setSelection(0);
        tvTitleName.setText(title);
        tvTypeTitle.setText(title + "(元)");
        mPage = 1;
        mDataType = dataType;

        isTitleMenuShow = true;
        setRadioButtonColor(radioButton);
        showOrHideTitleMenu();
        sendHttpReques(RefreshType.RefreshNoPull, dataType);
    }

    /**
     * 设置单选按钮字体颜色
     *
     * @param view
     */
    private void setRadioButtonColor(RadioButton view) {
        int parseColor = x.app().getResources().getColor(R.color.FC_CAN_SELECT);
        bt_titleMenu_all.setTextColor(parseColor);
        bt_titleMenu_withdraw.setTextColor(parseColor);
        bt_titleMenu_investment.setTextColor(parseColor);
        bt_titleMenu_shiftTo.setTextColor(parseColor);
        bt_titleMenu_recharge.setTextColor(parseColor);
        bt_titleMenu_repay.setTextColor(parseColor);
        bt_titleMenu_earnings.setTextColor(parseColor);
        bt_titleMenu_rolOut.setTextColor(parseColor);
        bt_titleMenu_award.setTextColor(parseColor);

        view.setTextColor(Color.WHITE);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        sendHttpReques(RefreshType.RefreshPull, mDataType);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        sendHttpReques(RefreshType.RefreshPull, mDataType);
    }

    public void sendHttpReques(RefreshType refreshType, String dataType) {
        mRefreshType = refreshType;

        RequestParams params = new RequestParams(Constant.HOMEPAGE_AVAILABLEBALANCE);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("type", dataType);
        params.addBodyParameter("filterDate", mFilterDate);
        params.addBodyParameter("pageNum", mPage + "");
        params.addBodyParameter("size", "10");
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPage == 1) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                if (balanceDetailsTimes.size() > 0) {
                    pullableExpandableListView.setPullLoadEnable(true);
                } else {
                    pullableExpandableListView.setPullLoadEnable(false);
                }
            } else {
                mPage--;
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        if (!errorContent.equals("Cancelled")) {
            noNetAndData(this, errorContent, balanceDetails);
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPage == 1) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        if (url.equals(Constant.HOMEPAGE_AVAILABLEBALANCE)) {

            if (mPage == 1) {
                balanceDetails.clear();
                mAdapter = null;
                pullableExpandableListView.setAdapter(mAdapter);
                pullableExpandableListView.removeHeaderView(headerView);
                pullableExpandableListView.addHeaderView(headerView);
            }

            String availableMoneyLog = jsonObject.getString("availableMoneyLog");
//            String isBook = jsonObject.getString("isBook");
//            tyj = jsonObject.getString("tyj");
//            strContent = jsonObject.getString("content");

            if (TextUtils.isEmpty(jsonObject.getString("availableMoney"))) {
                tvExperience.setText("0.00");
            } else {
                tvExperience.setText(MoneyFormatUtil.format(jsonObject.getString("availableMoney")));
            }
//            if (!TextUtils.isEmpty(isBook)){
//                if (isBook.equals("0")) {    //周周升是否开启预约
//                    tittleBottomLine.setVisibility(View.VISIBLE);
//                    rlLiCai.setVisibility(View.VISIBLE);
//                    tvTyjMoney.setText("领" + tyj + "元体验金");
//                } else {
//                    tittleBottomLine.setVisibility(View.GONE);
//                    rlLiCai.setVisibility(View.GONE);
//                }
//            }

            balanceDetails.addAll(JSON.parseArray(availableMoneyLog, BalanceDetails.class));

            changeList(balanceDetails);
            if (mPage != 1 && loadNumber == balanceDetails.size()) {   //如果当前加载条数等于最后一次记录加载条数，代表数据已加载完成
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.LOADED);
            }
            if (mAdapter == null) {
                mAdapter = new BalanceDetailsExpandableListAdapter(this, balanceDetailsTimes, pullableExpandableListView);
                pullableExpandableListView.setAdapter(mAdapter);
            } else {
                if (mPage == 1) {
                    pullableExpandableListView.setAdapter(mAdapter);
                }
                mAdapter.notifyDataSetChanged();
            }

            loadNumber = balanceDetails.size();
            if (balanceDetails.size() == 0) {// 禁止上拉
                pullableExpandableListView.setPullLoadEnable(false);
            } else {// 取消禁止上拉
                pullableExpandableListView.setPullLoadEnable(true);
            }

            noNetAndData(this, jsonBase, balanceDetails);
        }
//        else if (url.equals(Constant.PLANBOOK_STARTBOOK)) {
//            if (jsonObject.getString("status").equals("0")) {
//                DialogUtil.liCaiPlanSubscribeDialog(AvailableBalanceActivity.this, tyj + "元体验金已到账！", new DialogUtil.OnClickYesListener() {
//                    @Override
//                    public void onClickYes() {
//                        sendHttpReques(RefreshType.RefreshNoPull, mDataType);
//                    }
//                });
//            }
//        }
    }

    /**
     * 将一级集合转化成二级集合
     */
    private void changeList(List<BalanceDetails> balanceDetails) {
        ArrayList<String> strListTime = new ArrayList<String>();
        strListTime.clear();
        BalanceDetailsTime balanceDetailsTime = null;
        List<BalanceDetails> parseArray2 = null;
        balanceDetailsTimes.clear();
        for (int i = 0; i < balanceDetails.size(); i++) {
            String addtime = TimeUtil.getFullTime(balanceDetails.get(i).detailOptTime, "yyyy年MM月");

            if (i == 0) {
                balanceDetailsTime = new BalanceDetailsTime();
                parseArray2 = new ArrayList<BalanceDetails>();
                strListTime.add(addtime);
                balanceDetailsTime.setTime(addtime);
                parseArray2.add(balanceDetails.get(i));
            } else {
                if (!strListTime.contains(addtime)) {
                    balanceDetailsTime.setBalanceDetails(parseArray2);
                    balanceDetailsTimes.add(balanceDetailsTime);
                    parseArray2 = new ArrayList<BalanceDetails>();
                    balanceDetailsTime = new BalanceDetailsTime();
                    strListTime.add(addtime);
                    balanceDetailsTime.setTime(addtime);
                    parseArray2.add(balanceDetails.get(i));

                } else {
                    parseArray2.add(balanceDetails.get(i));
                }
            }

            if (i == (balanceDetails.size() - 1)) {
                balanceDetailsTime.setBalanceDetails(parseArray2);
                balanceDetailsTimes.add(balanceDetailsTime);
            }
        }

    }
}

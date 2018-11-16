package com.wtjr.lqg.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.LqbDetailsExpandableListAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.LqbDetails;
import com.wtjr.lqg.base.LqbDetailsTime;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.AnimationShowState;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.DatePicker.CustomDatePicker;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;
import com.wtjr.lqg.widget.pullRefresh.PullableExpandableListView;

import org.xutils.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 体验金
 */
public class TyjDetailsActivity1 extends BaseActivity implements
        OnClickListener, HttpRequesListener, OnRefreshListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    private RefreshType mRefreshType;

    private RadioGroup group1;
    private RadioGroup group2;
    /**
     * 赠送体验金
     */
    private RadioButton rbTyjAward;
    /**
     * 体验金转入
     */
    private RadioButton rbTyjInvestment;
    /**
     * 体验金到期
     */
    private RadioButton rbTyjExpire;
    /**
     * 全部
     */
    private RadioButton rbAll;
    /**
     * 标题菜单是否在显示
     */
    private boolean isTitleMenuShow = false;
    /**
     * 点击阴影，隐藏数据
     */
    private View viewHide;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableExpandableListView pullableExpandableListView;

    private LinearLayout ll_titleMenu;
    /**
     * 是否是第一次进入(为了避免第一次进入后箭头出现偏移旋转)
     */
    private boolean isFirst = true;

    /**
     * 立即获取体验金
     */
    private TextView textAcquireTYJ;
    private LinearLayout llAcquireTYJ;
    /**
     * 标题图标
     */
    private ImageView imageTitle;

    private List<LqbDetailsTime> listTimes = new ArrayList<LqbDetailsTime>();

    private List<LqbDetails> lqbDetails = new ArrayList<LqbDetails>();


    private LqbDetailsExpandableListAdapter mAdapter;
    private int mPage = 1;
    private String mDataType;
    private AnimationShowState animationState;
    /**
     * 问题反馈
     */
    private ImageButton imgBtnNext;
    private View headerView;
    /**
     * 列表头试图中的总额
     */
    private TextView tvExperience;
    /**
     * 列表头试图中的标题
     */
    private TextView tvTypeTitle;
    /**
     * 立即转入零钱宝引导页
     */
    private ViewStub rlGuide6;
    /**
     * 立即转入零钱宝引导页，立即转入按钮
     */
    private TextView tvIntoTyj;
    private boolean bFirst = true;
    private View tittleBottomLine;
    /**
     * 体验金帮助
     */
    private ImageView ivTyjHelp;
    private CustomDatePicker customDatePicker1;

    /**
     * 筛选体验金的月份
     */
    private String mFilterDate = "three";
    /**
     * 记录加载条数
     */
    private int loadNumber = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_experience_money);
        start();


    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        // 标题图标
        imageTitle = (ImageView) findViewById(R.id.image_title);

        viewHide = (View) findViewById(R.id.view_hide);
        tittleBottomLine = findViewById(R.id.title_bottom_line);

        ll_titleMenu = (LinearLayout) findViewById(R.id.ll_titleMenu);
        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);

        group1 = (RadioGroup) findViewById(R.id.radioGroup1);
        group2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rbTyjAward = (RadioButton) findViewById(R.id.rd_tyj_award);
        rbTyjInvestment = (RadioButton) findViewById(R.id.rd_tyj_investment);
        rbTyjExpire = (RadioButton) findViewById(R.id.rd_tyj_expire);
        textAcquireTYJ = (TextView) findViewById(R.id.tv_acquire_tyj);
        llAcquireTYJ = (LinearLayout) findViewById(R.id.linear_bottom);

        headerView = LayoutInflater.from(this).inflate(R.layout.scroll_availe_banlance, null);
        tvExperience = (TextView) headerView.findViewById(R.id.tv_experience);
        tvTypeTitle = (TextView) headerView.findViewById(R.id.tv_typeTitle);
        ivTyjHelp = (ImageView) headerView.findViewById(R.id.iv_tyj_help);
        //筛选
        imgBtnNext.setOnClickListener(this);
        //常见问题按钮监听器
        ivTyjHelp.setOnClickListener(this);
        rbAll = (RadioButton) findViewById(R.id.rb_all);

        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
        // 新建一个ExpandableListView
        pullableExpandableListView = (PullableExpandableListView) findViewById(R.id.pullable_Expandable_ListView);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
        setCheckedChangeListener();
        pullToRefreshLayout.setOnRefreshListener(this);
        tvTitleName.setOnClickListener(this);
        viewHide.setOnClickListener(this);
        llAcquireTYJ.setOnClickListener(this);
        imageTitle.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:体验金
        tvTitleName.setText(R.string.experience_money_name);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        imageTitle.setVisibility(View.VISIBLE);
        tittleBottomLine.setVisibility(View.GONE);

        // 设置头视图金额
        tvExperience.setText(MoneyFormatUtil.format(app.mAccountData.usableExpMoney));

        switch (animationState) {
            case showAnimation:
                llAcquireTYJ.setVisibility(View.GONE);
                textAcquireTYJ.setText("立即获取体验金");
                break;
            case hintAnimation:
                llAcquireTYJ.setVisibility(View.VISIBLE);
                textAcquireTYJ.setText("立即转入");
                if (!SharedPreferencesUtil.getPrefBoolean(this, "NoviceGuide2", false)) {
                    rlGuide6 = (ViewStub) findViewById(R.id.vs_guide6);
                    rlGuide6.setVisibility(View.VISIBLE);
                    tvIntoTyj = (TextView) findViewById(R.id.tv_into_tyj);
                    tvIntoTyj.setOnClickListener(this);
                    SharedPreferencesUtil.setPrefBoolean(this, "NoviceGuide2", true);
                }
                break;
        }
    }

    @Override
    public void initIntent() {
        animationState = (AnimationShowState) getIntent().getSerializableExtra(AnimationShowState.class.getName());
    }

    @Override
    public void initData() {
        imgBtnNext.setVisibility(View.VISIBLE);
        imgBtnNext.setImageResource(R.drawable.filtrate);
        ivTyjHelp.setVisibility(View.VISIBLE);
        rbAll.setChecked(true);

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
                    ll_titleMenu.setVisibility(View.GONE);
                    isTitleMenuShow = false;
                    showAnimation(imageTitle);
                } else {
                    finish();
                }
                break;
            case R.id.tv_title_name:// 点击标题文字
            case R.id.view_hide:// 点击标题后的箭头
            case R.id.image_title:// 点击标题后的箭头
                showOrHideTitleMenu();
                break;
            case R.id.imgBtn_next:
                // 弹出筛选框，日期格式为yyyy-MM
                customDatePicker1.show(new SimpleDateFormat("yyyy-MM", Locale.CHINA).format(new Date()));

                break;
            case R.id.tv_into_tyj:// 点击引导页立即转入按钮
                /**
                 * 发送体验金转入请求
                 */
                rlGuide6.setVisibility(View.GONE);
                sendTyjRollInRequest(RefreshType.RefreshNoPull);
                break;

            case R.id.linear_bottom:
                switch (animationState) {
                    case hintAnimation:
                        DialogUtil.promptDialog(TyjDetailsActivity1.this, "立即转入" + MoneyFormatUtil.format2(app.mAccountData.usableExpMoney) +
                                "元体验金", new DialogUtil.OnClickYesListener() {
                            @Override
                            public void onClickYes() {
                                sendTyjRollInRequest(RefreshType.RefreshNoPull);
                            }
                        }, new DialogUtil.OnClickNoListener() {
                            @Override
                            public void onClickNo() {

                            }
                        }).setCancelable(true);
                        break;
                    case showAnimation:
                        startActivity(new Intent(TyjDetailsActivity1.this, ActivityWebViewActivity.class));
                        break;
                }
                break;
            case R.id.iv_tyj_help:// 体验金问题
                Intent intent = new Intent(TyjDetailsActivity1.this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_WHATTYJ);
                intent.putExtra("TitleName", "什么是体验金");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 发送体验金转入请求
     */
    public void sendTyjRollInRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.LQB_EXPMONEYINTOLQB);
        params.addBodyParameter("user_userunid", app.mAccountData.uId);
        params.addBodyParameter("expMoney2lqb", app.mAccountData.usableExpMoney + "");// 体验金转入
        httpUtil.sendRequest(params, refreshType, this);
    }


    /**
     * 显示或隐藏标题菜单
     */
    public void showOrHideTitleMenu() {
        showAnimation(imageTitle);
        if (isTitleMenuShow) {
            ll_titleMenu.setVisibility(View.GONE);
            isTitleMenuShow = false;

        } else {
            ll_titleMenu.setVisibility(View.VISIBLE);
            isTitleMenuShow = true;
        }
    }

    /**
     * 显示标题动画旋转
     *
     * @param mView
     */
    public void showAnimation(View mView) {
        if (isFirst) {//避免第一次进入后箭头出现偏移旋转
            isFirst = false;
            return;
        }

        final float centerX = mView.getWidth() / 2.0f;
        final float centerY = mView.getHeight() / 2.0f;
        // 这个是设置需要旋转的角度，我设置的是180度
        if (!isTitleMenuShow) {
            RotateAnimation rotateAnimation = new RotateAnimation(0, 180,
                    centerX, centerY);
            rotateAnimation.setDuration(500);// 旋转时长
            rotateAnimation.setFillAfter(true);
            mView.startAnimation(rotateAnimation);
        } else {
            RotateAnimation rotateAnimation = new RotateAnimation(180, 360,
                    centerX, centerY);
            rotateAnimation.setDuration(500);// 旋转时长
            rotateAnimation.setFillAfter(true);
            mView.startAnimation(rotateAnimation);
        }

    }

    private void setCheckedChangeListener() {
        group1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) group.getChildAt(i);
                    if (childAt.isChecked()) {
                        group2.clearCheck();
                    }
                }


                if (rbTyjAward.isChecked()) {// 赠送体验金
                    setSelect("6", rbTyjAward);
                } else if (rbTyjInvestment.isChecked()) {// 体验金转入
                    setSelect("7", rbTyjInvestment);
                } else if (rbTyjExpire.isChecked()) {// 体验金到期
                    setSelect("8", rbTyjExpire);
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
                    }
                }
                if (rbAll.isChecked()) {// 全部
                    setSelect("5", rbAll);
                }
            }
        });
    }

    /**
     * 设置选择项
     *
     * @param dataType 全部-->3,赠送体验金-->6,体验金转入-->7,体验金到期-->8
     */
    private void setSelect(String dataType, RadioButton radioButton) {
        if (TextUtils.isEmpty(dataType)) {
            tvTitleName.setText("体验金");
            tvTypeTitle.setText("可用体验金(元)");
        } else {
            switch (dataType) {
                case "5":
                    tvTitleName.setText("体验金");
                    tvTypeTitle.setText("可用体验金(元)");
                    break;
                case "6":
                    tvTitleName.setText("赠送体验金");
                    tvTypeTitle.setText("累计获得赠送体验金(元)");
                    break;
                case "7":
                    tvTitleName.setText("体验金转入");
                    tvTypeTitle.setText("当前零钱宝体验金(元)");
                    break;
                case "8":
                    tvTitleName.setText("体验金到期");
                    tvTypeTitle.setText("体验金到期(元)");
                    break;
            }
        }

        mPage = 1;
        isTitleMenuShow = true;
        setRadioButtonColor(radioButton);
        showOrHideTitleMenu();
        sendHttpReques(RefreshType.RefreshNoPull, dataType);
    }


    /**
     * 设置单选按钮字体颜色
     */
    private void setRadioButtonColor(RadioButton radioButton) {
        int parseColor = app.getResources().getColor(R.color.FC_CAN_SELECT);
        rbTyjAward.setTextColor(parseColor);
        rbTyjExpire.setTextColor(parseColor);
        rbTyjInvestment.setTextColor(parseColor);
        rbAll.setTextColor(parseColor);

        radioButton.setTextColor(Color.WHITE);
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
        mDataType = dataType;
        RequestParams params = new RequestParams(Constant.LQB_QUERYLQBOPERATIONINFO);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("check_Type", dataType);
        params.addBodyParameter("filterDate", mFilterDate);
        params.addBodyParameter("pageNum", mPage + "");
        params.addBodyParameter("pageSize", "20");
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPage == 1) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                mPage--;
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        noNetAndData(this, errorContent, listTimes);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.LQB_EXPMONEYINTOLQB)) {
            sendHttpReques(RefreshType.RefreshNoPull, "5");
            bFirst = false;
            // 发送更新数据广播
            Intent mIntent = new Intent(Constant.UPDATE_MEDATA);
            sendBroadcast(mIntent, Manifest.permission.receiver_permission);
            animationState = AnimationShowState.showAnimation;
            llAcquireTYJ.setVisibility(View.GONE);
            tvExperience.setText("0.00");
            //刷新数据
        } else if (url.equals(Constant.LQB_QUERYLQBOPERATIONINFO)) {
            if (mRefreshType == RefreshType.RefreshPull) {
                if (mPage == 1) {
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }


            if (mPage == 1) {
                lqbDetails.clear();
                mAdapter = null;
                pullableExpandableListView.setAdapter(mAdapter);
                pullableExpandableListView.removeHeaderView(headerView);
                pullableExpandableListView.addHeaderView(headerView);
            }
            JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            String dataList = jsonObject.getString("dataList");

            if (mDataType.equals("5")) {
                if (app.mAccountData.usableExpMoney > 0 && bFirst) {
                    tvExperience.setText(MoneyFormatUtil.format(app.mAccountData.usableExpMoney));
                } else {
                    tvExperience.setText("0.00");
                }
            } else {
                if (TextUtils.isEmpty(jsonObject.getString("experienceMoney"))) {
                    tvExperience.setText("0.00");
                } else {
                    tvExperience.setText(MoneyFormatUtil.format(jsonObject.getString("experienceMoney")));
                }
            }

            lqbDetails.addAll(JSON.parseArray(dataList, LqbDetails.class));

            changeList(lqbDetails);
            if (mPage != 1 && loadNumber == lqbDetails.size()) {   //如果当前加载条数等于最后一次记录加载条数，代表数据已加载完成
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.LOADED);
            }
            if (mAdapter == null) {
                mAdapter = new LqbDetailsExpandableListAdapter(this, listTimes, pullableExpandableListView);
                pullableExpandableListView.setAdapter(mAdapter);
            } else {
                if (mPage == 1) {
                    pullableExpandableListView.setAdapter(mAdapter);
                }
                mAdapter.notifyDataSetChanged();
            }
            loadNumber = lqbDetails.size();
            noNetAndData(this, jsonBase, lqbDetails);
        }
    }


    /**
     * 将一级集合转化成二级集合
     */
    private void changeList(List<LqbDetails> parseArray) {
        ArrayList<String> strListTime = new ArrayList<String>();
        strListTime.clear();
        LqbDetailsTime pureseListTime = null;
        List<LqbDetails> parseArray2 = null;
        listTimes.clear();
        for (int i = 0; i < parseArray.size(); i++) {
            String addtime = TimeUtil.getFullTime(parseArray.get(i).optTime, "yyyy年MM月");

            if (i == 0) {
                pureseListTime = new LqbDetailsTime();
                parseArray2 = new ArrayList<LqbDetails>();
                strListTime.add(addtime);
                pureseListTime.setTime(addtime);
                parseArray2.add(parseArray.get(i));
            } else {
                if (!strListTime.contains(addtime)) {
                    pureseListTime.setPureseListDetails(parseArray2);
                    listTimes.add(pureseListTime);
                    parseArray2 = new ArrayList<LqbDetails>();
                    pureseListTime = new LqbDetailsTime();
                    strListTime.add(addtime);
                    pureseListTime.setTime(addtime);
                    parseArray2.add(parseArray.get(i));

                } else {
                    parseArray2.add(parseArray.get(i));
                }
            }

            if (i == (parseArray.size() - 1)) {
                pureseListTime.setPureseListDetails(parseArray2);
                listTimes.add(pureseListTime);
            }
        }

    }
}
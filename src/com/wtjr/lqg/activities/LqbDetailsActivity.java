package com.wtjr.lqg.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.LqbDetailsExpandableListAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.LqbDetails;
import com.wtjr.lqg.base.LqbDetailsTime;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.LqbDetailsType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
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
 * 零钱宝资金记录
 */
public class LqbDetailsActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnRefreshListener {
    private RadioGroup group1;
    private RadioGroup group2;
    /**
     * 转入
     */
    private RadioButton rd_in_cost;
    /**
     * 转出
     */
    private RadioButton rb_roll_out;
    /**
     * 累计收益
     */
    private RadioButton rb_earnings;
    /**
     * 投资
     */
    private RadioButton rb_investment;
    /**
     * 体验金
     */
    private RadioButton rb_experience;
    /**
     * 全部
     */
    private RadioButton rb_all;
    /**
     * 标题
     */
//	private TextView tvTitle;
    /**
     * 返回
     */
    private ImageButton imgbtnBack;
    /**
     * 标题图标
     */
    private ImageView imageTitle;
    /**
     * 标题菜单是否在显示
     */
    private boolean isTitleMenuShow = false;

//	private boolean isInit = false;

    private LinearLayout ll_titleMenu;
    /**
     * 点击阴影，隐藏数据
     */
    private View viewHide;

    private List<LqbDetailsTime> listTimes = new ArrayList<LqbDetailsTime>();

    private List<LqbDetails> lqbDetails = new ArrayList<LqbDetails>();

    private PullToRefreshLayout pullToRefreshLayout;
    private LqbDetailsExpandableListAdapter mAdapter;
    private PullableExpandableListView pullableExpandableListView;
//	private int user_id;
    /**
     * 当前数据类型
     */
    private LqbDetailsType mLqbDataType;
    //	private LinearLayout ll_title;
    private RefreshType mRefreshType;
//	private View headerView;
    /**
     * 是否是第一次进入(为了避免第一次进入后箭头出现偏移旋转)
     */
    private boolean isFirst = true;
    private TextView tvTitleName;
    private View headerView;
    /**
     * 头部视图中的标题
     */
    private TextView tvHeadTitle;
    /**
     * 头部视图中的钱
     */
    private TextView tvHeadMoney;
    private int mPage = 1;
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

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_lqb_details);
        start();
    }

    @Override
    public void findViewById() {
//		ll_title = (LinearLayout) findViewById(R.id.ll_title);
        // 标题 TODO
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        // 返回
        imgbtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        // 标题图标
        imageTitle = (ImageView) findViewById(R.id.image_title);

        headerView = LayoutInflater.from(this).inflate(R.layout.head_lqb_details, null);

        tvHeadTitle = (TextView) findViewById(R.id.tv_headTitle);
        tvHeadMoney = (TextView) findViewById(R.id.tv_headMoney);

        viewHide = (View) findViewById(R.id.view_hide);
        ll_titleMenu = (LinearLayout) findViewById(R.id.ll_titleMenu);
        group1 = (RadioGroup) findViewById(R.id.radioGroup1);
        group2 = (RadioGroup) findViewById(R.id.radioGroup2);
        rd_in_cost = (RadioButton) findViewById(R.id.rd_in_cost);
        rb_roll_out = (RadioButton) findViewById(R.id.rb_roll_out);
        rb_earnings = (RadioButton) findViewById(R.id.rb_earnings);
        rb_investment = (RadioButton) findViewById(R.id.rb_investment);
        rb_experience = (RadioButton) findViewById(R.id.rb_experience);
        rb_all = (RadioButton) findViewById(R.id.rb_all);

        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
        //
        // 新建一个ExpandableListView
        pullableExpandableListView = (PullableExpandableListView) findViewById(R.id.pullable_Expandable_ListView);
//		//收益列表中的头视图
//		headerView = LayoutInflater.from(this).inflate(R.layout.scroll_availe_banlance, null);
//		TextView typeTitle = (TextView) headerView.findViewById(R.id.tv_typeTitle);
//		TextView experience = (TextView) headerView.findViewById(R.id.tv_experience);
//		typeTitle.setText("累计收益(元)");
//		experience.setText(MoneyFormat.format(app.purse.lqg));
    }

    @Override
    public void setTitle() {
//		ll_title.setBackgroundResource(R.drawable.ic_launcher);
//		imageTitle.setVisibility(View.VISIBLE);    //显示标题后面小箭头
        // 返回
        imgbtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void initIntent() {
        mLqbDataType = (LqbDetailsType) getIntent().getSerializableExtra(LqbDetailsType.class.getName());
    }

    @Override
    public void initData() {
//		if (app.information != null) {
//			user_id = app.information.user_id;
//		}

        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);
        imgBtnNext.setVisibility(View.VISIBLE);
        imgBtnNext.setImageResource(R.drawable.filtrate);
        imgBtnNext.setOnClickListener(this);

        // 禁止上拉加载
//		pullableExpandableListView.setPullLoadEnable(false);
        switch (mLqbDataType) {
            case All:
                tvTitleName.setText(LqbDetailsType.All.getTitle());
                rb_all.setChecked(true);

                break;
            case Earnings:
                tvTitleName.setText(LqbDetailsType.Earnings.getTitle());
                rb_earnings.setChecked(true);
                break;
            case TYJ:
                tvTitleName.setText(LqbDetailsType.TYJ.getTitle());
                rb_experience.setChecked(true);
                break;
        }

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
                sendHttpReques(mLqbDataType, RefreshType.RefreshPull);
            }
        }, "2016-03", now); // 初始化日期格式请用：yyyy-MM，否则不能正常运行
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
    }

    @Override
    public void setListener() {
        httpUtil.setHttpRequesListener(this);
        setCheckedChangeListener();
        pullToRefreshLayout.setOnRefreshListener(this);
        imgbtnBack.setOnClickListener(this);
//		tvTitleName.setOnClickListener(this);
        imageTitle.setOnClickListener(this);
        viewHide.setOnClickListener(this);

        pullableExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return false;
            }
        });

        // 设置二级item点击的监听器
        pullableExpandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View view,
                                        int groupPosition, int childPosition, long id) {

                // ImageView imageView = (ImageView) view.findViewById(R.id.iv);
                // imageView.setVisibility(View.VISIBLE);

                if (mAdapter.child_groupId == groupPosition
                        && mAdapter.child_childId == childPosition) {
                    mAdapter.child_groupId = -1;
                    mAdapter.child_childId = -1;

                } else {
                    // 将被点击的一丶二级标签的位置记录下来
                    mAdapter.child_groupId = groupPosition;
                    mAdapter.child_childId = childPosition;
                }

                // 刷新界面
                mAdapter.notifyDataSetChanged();

                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
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
            default:
                break;
        }
    }

    /**
     * 设置选择项
     *
     * @param type
     */
    private void setSelect(LqbDetailsType type, RadioButton view) {
        setRadioButtonColor(view);
//		pullableExpandableListView.setSelection(0);
        tvTitleName.setText(type.getTitle());
        isTitleMenuShow = true;
        showOrHideTitleMenu();
        mLqbDataType = type;

        mPage = 1;


//		if(currentType.equals(LQGINTEREST)){
//			adapter = null;
//			pullableExpandableListView.setAdapter(adapter);
//			pullableExpandableListView.addHeaderView(headerView);
//		}else{
//			pullableExpandableListView.removeHeaderView(headerView);
//		}

        sendHttpReques(mLqbDataType, RefreshType.RefreshNoPull);
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
                if (rd_in_cost.isChecked()) {// 转入
                    setSelect(LqbDetailsType.RollIn, rd_in_cost);
                } else if (rb_roll_out.isChecked()) {// 转出
                    setSelect(LqbDetailsType.RollOut, rb_roll_out);
                } else if (rb_earnings.isChecked()) {
                    setSelect(LqbDetailsType.Earnings, rb_earnings);
                } else {

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
                if (rb_investment.isChecked()) {
                    setSelect(LqbDetailsType.Investment, rb_investment);
                } else if (rb_experience.isChecked()) {
                    setSelect(LqbDetailsType.TYJ, rb_experience);
                } else if (rb_all.isChecked()) {
                    setSelect(LqbDetailsType.All, rb_all);
                }
            }
        });
    }


    /**
     * 设置单选按钮字体颜色
     *
     * @param view
     */
    private void setRadioButtonColor(RadioButton view) {
        int parseColor = app.getResources().getColor(R.color.lqb_head_bg);
        rd_in_cost.setTextColor(parseColor);
        rb_roll_out.setTextColor(parseColor);
        rb_earnings.setTextColor(parseColor);
        rb_investment.setTextColor(parseColor);
        rb_experience.setTextColor(parseColor);
        rb_all.setTextColor(parseColor);

        view.setTextColor(Color.WHITE);
    }


    /**
     * 将一级集合转化成二级集合
     *
     * @param parseArray
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


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isTitleMenuShow) {
                ll_titleMenu.setVisibility(View.GONE);
                isTitleMenuShow = false;
                showAnimation(imageTitle);
            } else {
                finish();
            }
        }
        return false;
    }


    @Override
    public void onRefresh() {
        mPage = 1;
        sendHttpReques(mLqbDataType, RefreshType.RefreshPull);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        sendHttpReques(mLqbDataType, RefreshType.RefreshPull);
    }

    public void sendHttpReques(LqbDetailsType type, RefreshType refreshType) {
        mRefreshType = refreshType;

        RequestParams params = new RequestParams(Constant.LQB_QUERYLQBOPERATIONINFO);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("check_Type", type.getParams());
        params.addBodyParameter("filterDate", mFilterDate);
        params.addBodyParameter("pageNum", mPage + "");
        params.addBodyParameter("pageSize", "20");
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String msg) {
        if (mRefreshType == RefreshType.RefreshPull) {
            if (mPage == 1) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            } else {
                mPage--;
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        }

        noNetAndData(this, msg, listTimes);
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

        if (mPage == 1) {
            lqbDetails.clear();
        }

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        String dataList = jsonObject.getString("dataList");


        // if(mPage==1){
        // mAdapter = null;
        // pullableExpandableListView.setAdapter(mAdapter);
        // pullableExpandableListView.removeHeaderView(headerView);
        // pullableExpandableListView.addHeaderView(headerView);
        // }

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

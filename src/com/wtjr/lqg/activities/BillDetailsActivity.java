package com.wtjr.lqg.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.xutils.http.RequestParams;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.BillDetailsExpandableListAdapter;
import com.wtjr.lqg.adapters.BillExpandableListAdapter;
import com.wtjr.lqg.base.BillDetails;
import com.wtjr.lqg.base.BillDetailsTime;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.DatePicker.CustomDatePicker;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableExpandableListView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

/**
 * 账单详情
 *
 * @author JoLie
 */
public class BillDetailsActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnRefreshListener {

    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 设置标题栏背景色
     */
    private RelativeLayout rlTitle;
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    private ImageButton imgbtnBack;
    /**
     * 记录加载条数
     */
    private int loadNumber = 0;

    // 这个数组是用来存储一级item的点击次数的，根据点击次数设置一级标签的选中、为选中状态(为了更改向下和向上箭头)
    private List<BillDetailsTime> mBillDetailsTimes = new ArrayList<BillDetailsTime>();
    private List<BillDetails> mBillDetails = new ArrayList<BillDetails>();

    private PullToRefreshLayout pullToRefreshLayout;
    /**
     * 页数
     */
    private int mPage = 1;
    /**
     * 每页条目数
     */
    private int mPageSize = 10;
    /**
     * 获取当前登录中的账户信息
     */
    private String userId;
    private BillDetailsExpandableListAdapter adapter;
    private PullableExpandableListView pullableExpaListView;
    private RefreshType mRefreshType;
    /**
     * 筛选
     */
    private ImageButton imgBtnNext;
    private CustomDatePicker customDatePicker1;
    /**
     * 筛选体验金的月份
     */
    private String mFilterDate = "three";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_bill);
        start();

    }

    @Override
    public void findViewById() {
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);
        imgBtnNext.setVisibility(View.VISIBLE);
        imgBtnNext.setImageResource(R.drawable.filtrate);
        imgBtnNext.setOnClickListener(this);

        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
        //
        pullableExpaListView = (PullableExpandableListView) findViewById(R.id.pullable_Expandable_ListView);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
        pullToRefreshLayout.setOnRefreshListener(this);

        // 设置一级item点击的监听器
        pullableExpaListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
        // 设置二级item点击的监听器
        pullableExpaListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                if (adapter.child_groupId == groupPosition && adapter.child_childId == childPosition) {
                    adapter.child_groupId = -1;
                    adapter.child_childId = -1;

                } else {
                    // 将被点击的一丶二级标签的位置记录下来
                    adapter.child_groupId = groupPosition;
                    adapter.child_childId = childPosition;
                }
                // 刷新界面
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        imgBtnBack.setImageResource(R.drawable.back2);
        // 设置名字为:资金记录
        tvTitleName.setText(R.string.me_lqg_assets);

        tvTitleName.setTextColor(Color.BLACK);
        rlTitle.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void initData() {
        sendBillDetailsRequest(RefreshType.RefreshNoPull);
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
                sendBillDetailsRequest(RefreshType.RefreshPull);
            }
        }, "2016-03", now); // 初始化日期格式请用：yyyy-MM，否则不能正常运行
        customDatePicker1.setIsLoop(false); // 不允许循环滚动
    }

    public void click(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
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
     * 将一级集合转化成二级集合
     *
     * @param parseArray
     */
    private void changeList(List<BillDetails> parseArray) {
        ArrayList<String> strListTime = new ArrayList<String>();
        // strListTime.clear();
        BillDetailsTime billDetailsTime = null;
        List<BillDetails> parseArray2 = null;
        mBillDetailsTimes.clear();
        for (int i = 0; i < parseArray.size(); i++) {

            String addtime = TimeUtil.getFullTime(parseArray.get(i).time, "yyyy年MM月");

            // 先得到时间
            // String addtime = .substring(0, 7);
            if (i == 0) {
                billDetailsTime = new BillDetailsTime();
                parseArray2 = new ArrayList<BillDetails>();
                strListTime.add(addtime);
                billDetailsTime.setTime(addtime);
                parseArray2.add(parseArray.get(i));
            } else {
                if (!strListTime.contains(addtime)) {
                    billDetailsTime.setBillDetails(parseArray2);
                    mBillDetailsTimes.add(billDetailsTime);
                    parseArray2 = new ArrayList<BillDetails>();
                    billDetailsTime = new BillDetailsTime();
                    strListTime.add(addtime);
                    billDetailsTime.setTime(addtime);
                    parseArray2.add(parseArray.get(i));

                } else {
                    parseArray2.add(parseArray.get(i));
                }
            }

            if (i == (parseArray.size() - 1)) {
                billDetailsTime.setBillDetails(parseArray2);
                mBillDetailsTimes.add(billDetailsTime);
            }
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        sendBillDetailsRequest(RefreshType.RefreshPull);

    }

    @Override
    public void onLoadMore() {
        mPage++;
        sendBillDetailsRequest(RefreshType.RefreshPull);
    }

    /**
     * 发送账单详情请求
     *
     * @param refreshType
     */
    private void sendBillDetailsRequest(RefreshType refreshType) {
        mRefreshType = refreshType;
        RequestParams params = new RequestParams(Constant.HOMEPAGE_BILLINGDETAILS);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("pageNum", mPage + "");
        params.addBodyParameter("size", mPageSize + "");
        params.addBodyParameter("filterDate", mFilterDate);
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

        noNetAndData(this, errorContent, mBillDetails);
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
            mBillDetails.clear();
        }

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        String availableMoneyLog = jsonObject.getString("availableMoneyLog");

        mBillDetails.addAll(JSON.parseArray(availableMoneyLog, BillDetails.class));

        changeList(mBillDetails);

        if (mBillDetailsTimes.size() > 0) {
            pullableExpaListView.setPullLoadEnable(true);
        }
        if (mPage != 1 && loadNumber == mBillDetails.size()) {   //如果当前加载条数等于最后一次记录加载条数，代表数据已加载完成
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.LOADED);
        }
        if (adapter == null) {
            // 新建一个ExpandableListView
            adapter = new BillDetailsExpandableListAdapter(this, mBillDetailsTimes, pullableExpaListView);
            // 为列表绑定数据源
            pullableExpaListView.setAdapter(adapter);
        } else {
            if (mPage == 1) {
                pullableExpaListView.setAdapter(adapter);
            }
            // 刷新界面
            adapter.notifyDataSetChanged();
        }
        loadNumber = mBillDetails.size();
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            pullableExpaListView.expandGroup(i);// 展开分组
        }

        noNetAndData(this, jsonBase, mBillDetails);
    }
}

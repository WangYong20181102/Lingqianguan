package com.wtjr.lqg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.BorrowingProjectListData;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYong on 2018/1/22.
 * 待借款项目
 */

public class BorrowingProjectActivity extends BaseActivity implements View.OnClickListener, PullToRefreshLayout.OnRefreshListener, HttpUtil
        .HttpRequesListener, AdapterView.OnItemClickListener {
    /**
     * 标题
     */
    private TextView tvTitleName;
    /**
     * 返回
     */
    private ImageButton imgBtnBack;
    /**
     * 借款列表
     */
    private PullableListView listBorrowing;
    private BorrowingListAdapter adapter;
    private List<BorrowingProjectListData> listDatas = new ArrayList<>();
    /**
     * 刷新
     */
    private PullToRefreshLayout pullToRefreshLayout;
    /**
     * 底部提示语
     */
    private View viewBottom;
    private int mPage = 1;
    private RefreshType mRefreshType;
    /**
     * 记录加载条数
     */
    private int loadNumber = 0;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_borrowing_project_list_layout);
        start();
    }

    @Override
    public void initData() {
        addLstFoot();
        setBorrowingListRequest(RefreshType.RefreshNoPull);
    }

    /**
     * 添加底部提示语
     */
    private void addLstFoot() {
        listBorrowing.setAdapter(null);
        listBorrowing.removeFooterView(viewBottom);
        listBorrowing.addFooterView(viewBottom);
        listBorrowing.setAdapter(adapter);
    }

    @Override
    public void findViewById() {
        //标题
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        //返回
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        listBorrowing = (PullableListView) findViewById(R.id.list_borrowing);
        //底部提示语
        viewBottom = View.inflate(this, R.layout.borrowing_project_list_bottom_context, null);
        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
    }

    @Override
    public void setTitle() {
        tvTitleName.setText("待借款项目");
        imgBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void setListener() {
        imgBtnBack.setOnClickListener(this);
        pullToRefreshLayout.setOnRefreshListener(this);
        //网络请求
        httpUtil.setHttpRequesListener(this);
        listBorrowing.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:  //返回
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        setBorrowingListRequest(RefreshType.RefreshPull);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        setBorrowingListRequest(RefreshType.RefreshPull);
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
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        if (url.equals(Constant.PLANPROJECT_QUERYBORROWER)) {
            if (mRefreshType == RefreshType.RefreshPull) {
                if (mPage == 1) {
                    listDatas.clear();
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                } else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }

            String borrowerList = jsonObject.getString("list");
            listDatas.addAll(JSON.parseArray(borrowerList, BorrowingProjectListData.class));
            if (mPage != 1 && loadNumber == listDatas.size()) {   //如果当前加载条数等于最后一次记录加载条数，代表数据已加载完成
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.LOADED);
            }
            if (adapter == null) {
                adapter = new BorrowingListAdapter(this, listDatas);
                listBorrowing.setAdapter(adapter);
            } else {
                if (mPage == 1) {
                    listBorrowing.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
            loadNumber = listDatas.size();   //记录最后一次加载条数
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != listDatas.size()) {
            Intent intent = new Intent(this, WeekProjectDetailActivity.class);
            intent.putExtra("bid", listDatas.get(position).bId);
            startActivity(intent);
        }
    }

    private void setBorrowingListRequest(RefreshType refreshType) {
        mRefreshType = refreshType;
        RequestParams params = new RequestParams(Constant.PLANPROJECT_QUERYBORROWER);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("pageNum", mPage + "");
        params.addBodyParameter("pageSize", "20");
        httpUtil.sendRequest(params, refreshType, this);
    }

    public class BorrowingListAdapter extends NewBaseAdapter {

        public BorrowingListAdapter(Context context, List<BorrowingProjectListData> list) {
            super(context, list);
        }

        @Override
        public int getContentView() {
            return R.layout.borrowing_project_list_adapter_item;
        }

        @Override
        public void onInitView(View view, int position) {
            BorrowingProjectListData borrowingListData = listDatas.get(position);
            //出借人
            TextView tvBorrowingHuman = (TextView) getViewChild(view, R.id.tv_borrowing_human);
            tvBorrowingHuman.setText(StringUtil.setBlurryName(borrowingListData.borrowerName));
            //待投资总额
            TextView tvTotalMoney = (TextView) getViewChild(view, R.id.tv_total_money);
            tvTotalMoney.setText(MoneyFormatUtil.format(borrowingListData.loanMoney));
            //借款期限
            TextView tvBorrowingTime = (TextView) getViewChild(view, R.id.tv_borrowing_time);
            tvBorrowingTime.setText(borrowingListData.loanDays + "");

        }
    }
}

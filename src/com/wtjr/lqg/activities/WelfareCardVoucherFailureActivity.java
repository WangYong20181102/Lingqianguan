package com.wtjr.lqg.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.CardVoucherData;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableExpandableListView;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangYong on 2017/11/3.
 */

public class WelfareCardVoucherFailureActivity extends BaseActivity implements HttpUtil.HttpRequesListener, View.OnClickListener, PullToRefreshLayout.OnRefreshListener {
    private PullableListView pullableListView;
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 下拉刷新最外层
     */
    private PullToRefreshLayout pullToRefreshLayout;
    private int mPage = 1;
    private RefreshType mRefreshType;
    private WelfareCardVoucherFailureAdapter mAdapter;
    private List<CardVoucherData> cardVoucherDatas = new ArrayList<CardVoucherData>();
    /**
     * 记录加载条数
     */
    private int loadNumber = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_card_voucher_failure);
        start();
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:失效卡券
        tvTitleName.setText(R.string.failure_welfare_cord_quan);
    }

    @Override
    public void findViewById() {
        // 新建一个ExpandableListView
        pullableListView = (PullableListView) findViewById(R.id.pullable_listView);
        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
    }

    @Override
    public void setListener() {
        //网络监听
        httpUtil.setHttpRequesListener(this);
        // 下拉刷新监听
        pullToRefreshLayout.setOnRefreshListener(this);
        imgBtnBack.setOnClickListener(this);
    }

    @Override
    public void initData() {
        sendUserCardRequest(RefreshType.RefreshNoPull);
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
            cardVoucherDatas.clear();
            mAdapter = null;
            pullableListView.setAdapter(mAdapter);
        }

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        cardVoucherDatas.addAll(JSON.parseArray(jsonObject.getString("list"), CardVoucherData.class));
        if (mPage != 1 && loadNumber == cardVoucherDatas.size()) {   //如果当前加载条数等于最后一次记录加载条数，代表数据已加载完成
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.LOADED);
        }
        if (mAdapter == null) {
            mAdapter = new WelfareCardVoucherFailureAdapter(WelfareCardVoucherFailureActivity.this,cardVoucherDatas);
            pullableListView.setAdapter(mAdapter);
        } else {
            if (mPage == 1) {
                pullableListView.setAdapter(mAdapter);
            }
            mAdapter.notifyDataSetChanged();
        }
        loadNumber = cardVoucherDatas.size();
        if (cardVoucherDatas.size() == 0) {  //禁止上拉
            pullableListView.setPullLoadEnable(false);
        } else { //允许上拉
            pullableListView.setPullLoadEnable(true);

        }

        noNetAndData(this, jsonBase, cardVoucherDatas);
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
        sendUserCardRequest(RefreshType.RefreshPull);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        sendUserCardRequest(RefreshType.RefreshPull);
    }
    /**
     * 发送网络请求
     */
    private void sendUserCardRequest(RefreshType type) {
        mRefreshType = type;
        RequestParams params = new RequestParams(Constant.LQB_USERCARD);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("usable", "2");
        params.addBodyParameter("pageNo", mPage + "");
        params.addBodyParameter("pageSize", "10");
        httpUtil.sendRequest(params, mRefreshType, this);
    }
    class WelfareCardVoucherFailureAdapter<T> extends NewBaseAdapter<T> {


        public WelfareCardVoucherFailureAdapter(Context context,List<T> list) {
            super(context,list);
        }

        @Override
        public int getContentView() {
            return R.layout.failure_card_quan_adapter_layout;
        }

        @Override
        public void onInitView(View view, int position) {
            CardVoucherData cardVoucherData = cardVoucherDatas.get(position);
            //金额
            TextView tvCardQuanMoney = getViewChild(view, R.id.card_quan_money_text);
            //提现券文本
            TextView tvCardQuanWithdraw = getViewChild(view, R.id.card_quan_withdraw_text);
            //显示内容
            LinearLayout linearLayout = getViewChild(view,R.id.card_quan_linear2);
            TextView tvCardQuan1 = getViewChild(view, R.id.card_quan_text1);
            TextView tvCardQuan2 = getViewChild(view, R.id.card_quan_text2);
            //有效期
            TextView tvCardQuanDateTime = getViewChild(view, R.id.card_quan_date_time);
            //已失效
            ImageView imageCardQuanAlreadyFailure = getViewChild(view, R.id.card_quan_already_failure_image);
            if (cardVoucherData.type == 1){
                tvCardQuanMoney.setText("¥ " + cardVoucherData.value);
                tvCardQuanWithdraw.setText("提现券");
                linearLayout.setVisibility(View.INVISIBLE);
                imageCardQuanAlreadyFailure.setImageResource(R.drawable.card_quan_already_failure);
            }else if (cardVoucherData.type == 2){
                tvCardQuanMoney.setText(cardVoucherData.value+" %");
                tvCardQuanWithdraw.setText("加息券");
                linearLayout.setVisibility(View.VISIBLE);
                tvCardQuan2.setText(cardVoucherData.remark2);
                imageCardQuanAlreadyFailure.setImageResource(R.drawable.card_quan_use);
            }
            tvCardQuan1.setText(cardVoucherData.remark1);
            tvCardQuanDateTime.setText("有效期至"+ TimeUtil.getTimeType2(cardVoucherData.validTime));

        }
    }
}

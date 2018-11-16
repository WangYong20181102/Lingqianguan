package com.wtjr.lqg.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.CardVoucherData;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by WangYong on 2017/11/3.
 */

public class WelfareCardVoucherActivity extends BaseActivity implements HttpUtil.HttpRequesListener, PullToRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemClickListener {
    private PullableListView pullableListView;
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    private RefreshType mRefreshType;
    /**
     * 查看失效卡券父布局
     */
    private LinearLayout llLookCardQuan;
    /**
     * 下拉刷新最外层
     */
    private PullToRefreshLayout pullToRefreshLayout;
    private int mPage = 1;
    private List<CardVoucherData> cardVoucherDataslist = new ArrayList<CardVoucherData>();
    private WelfareCardVoucherAdapter mAdapter;
    /**
     * 记录加载条数
     */
    private int loadNumber = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welfare_card_voucher);
        start();
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:福利卡券
        tvTitleName.setText(R.string.welfare_cord_quan);
    }

    @Override
    public void findViewById() {
        // 新建一个ExpandableListView
        pullableListView = (PullableListView) findViewById(R.id.pullable_listView);
        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
        llLookCardQuan = (LinearLayout) findViewById(R.id.linear_look_card_quan);
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
        llLookCardQuan.setOnClickListener(this);
        //listView点击事件
        pullableListView.setOnItemClickListener(this);
    }

    @Override
    public void initData() {

        sendUserCardRequest(RefreshType.RefreshNoPull);
    }

    /**
     * 发送网络请求
     */
    private void sendUserCardRequest(RefreshType type) {
        mRefreshType = type;
        RequestParams params = new RequestParams(Constant.LQB_USERCARD);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("usable", "1");
        params.addBodyParameter("pageNo", mPage + "");
        params.addBodyParameter("pageSize", "10");
        httpUtil.sendRequest(params, mRefreshType, this);
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
            cardVoucherDataslist.clear();
            mAdapter = null;
            pullableListView.setAdapter(mAdapter);
        }

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        cardVoucherDataslist.addAll(JSON.parseArray(jsonObject.getString("list"), CardVoucherData.class));
        if (mPage != 1 && loadNumber == cardVoucherDataslist.size()) {   //如果当前加载条数等于最后一次记录加载条数，代表数据已加载完成
            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.LOADED);
        }
        if (mAdapter == null) {
            mAdapter = new WelfareCardVoucherAdapter(WelfareCardVoucherActivity.this,cardVoucherDataslist);
            pullableListView.setAdapter(mAdapter);
        } else {
            if (mPage == 1) {
                pullableListView.setAdapter(mAdapter);
            }
            mAdapter.notifyDataSetChanged();
        }
        loadNumber = cardVoucherDataslist.size();
        if (cardVoucherDataslist.size() == 0) {  //禁止上拉
            pullableListView.setPullLoadEnable(false);
        } else { //允许上拉
            pullableListView.setPullLoadEnable(true);

        }

        noNetAndData(this, jsonBase, cardVoucherDataslist);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:  //返回
                finish();
                break;
            case R.id.linear_look_card_quan:  //查看失效卡券
                startActivity(new Intent(WelfareCardVoucherActivity.this, WelfareCardVoucherFailureActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CardVoucherData cardData = cardVoucherDataslist.get(position);
        if (cardData.type == 2){    //加息券跳转投资界面
            Intent intent = new Intent(WelfareCardVoucherActivity.this, MainActivity.class);
            startActivity(intent);

            // 更新前3个界面
            Intent mIntent = new Intent(Constant.UPDATE_ALL);
            mIntent.putExtra("ShowFragmentLocation", 2);
            sendBroadcast(mIntent, Manifest.permission.receiver_permission);
        }else if (cardData.type == 1){ //提现券吐丝提示
            ToastUtil.showToastLong(this,"提现券可在你做提现操作时选取抵扣手续费！");
        }
    }


    class WelfareCardVoucherAdapter<T> extends NewBaseAdapter<T> {


        public WelfareCardVoucherAdapter(Context context,List<T> list) {
            super(context,list);
        }

        @Override
        public int getContentView() {
            return R.layout.card_quan_adapter_layout;
        }

        @Override
        public void onInitView(View view, int position) {
            //金额
            TextView tvCardQuanMoney = getViewChild(view, R.id.card_quan_money_text);
            CardVoucherData cardVoucherData = cardVoucherDataslist.get(position);
            //背景
            LinearLayout llCardQuanBg = getViewChild(view, R.id.card_quan_linear);
            //提现券文本
            TextView tvCardQuanWithdraw = getViewChild(view, R.id.card_quan_withdraw_text);
            //即将过期
            ImageView imageCardQuanWillExpire = getViewChild(view, R.id.card_quan_will_expire_image);
            //显示内容
            LinearLayout linearLayout = getViewChild(view,R.id.card_quan_linear2);
            TextView tvCardQuan1 = getViewChild(view, R.id.card_quan_text1);
            TextView tvCardQuan2 = getViewChild(view, R.id.card_quan_text2);
            //有效期
            TextView tvCardQuanDateTime = getViewChild(view, R.id.card_quan_date_time);
            //去投资
            ImageView imageCardQuanGoInvestment = getViewChild(view, R.id.card_quan_go_investment_image);
            if (cardVoucherData.type == 1){
                tvCardQuanMoney.setText("¥ " + cardVoucherData.value);
                llCardQuanBg.setBackgroundResource(R.drawable.card_quan_withdraw);
                tvCardQuanWithdraw.setText("提现券");
                imageCardQuanWillExpire.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.INVISIBLE);
            }else if (cardVoucherData.type == 2){
                tvCardQuanMoney.setText(cardVoucherData.value+" %");
                llCardQuanBg.setBackgroundResource(R.drawable.card_quan_jiaxi);
                tvCardQuanWithdraw.setText("加息券");
                linearLayout.setVisibility(View.VISIBLE);
                tvCardQuan2.setText(cardVoucherData.remark2);
                imageCardQuanGoInvestment.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(cardVoucherData.isSoonExpired) && cardVoucherData.isSoonExpired.equals("1")){
                    imageCardQuanWillExpire.setVisibility(View.VISIBLE);
                }else{
                    imageCardQuanWillExpire.setVisibility(View.INVISIBLE);
                }
            }
            tvCardQuan1.setText(cardVoucherData.remark1);
            tvCardQuanDateTime.setText("有效期至"+ TimeUtil.getTimeType2(cardVoucherData.validTime));

        }
    }

}

package com.wtjr.lqg.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.PayChannelList;
import com.wtjr.lqg.base.SetPayChannel;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置第三方支付通道
 */
public class PayThirdpartyActivity extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener, HttpUtil.HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 保存
     */
    private TextView tvNext;
    /**
     * 支付通道列表
     */
    private ListView lvRechargeChannel;
    /**
     * 保存支付通道
     */
    private List<PayChannelList> payChannelList = new ArrayList<PayChannelList>();
    /**
     * 选中支付通道的id
     */
    private int payIdSelect;
    /**
     * 点击支付通道的id
     */
    private String channelPayId;
    /**
     * 支付通道列表适配器
     */
    private RechargeAisleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_pay_thirdparty);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvNext = (TextView) findViewById(R.id.tv_next);

        lvRechargeChannel = (ListView) findViewById(R.id.lv_recharge_aisle);

    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        lvRechargeChannel.setOnItemClickListener(this);
        tvNext.setOnClickListener(this);
        // 设置网络监听
        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        tvTitleName.setText("设置充值通道");
        tvNext.setText("保存");
        tvNext.setTextColor(Color.parseColor("#999999"));
        tvNext.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        /**
         * 请求支付通道列表
         */
        sendRechargeChannel();
        tvNext.setClickable(false);
    }

    /**
     * 请求支付通道列表
     */
    private void sendRechargeChannel() {
        RequestParams params = new RequestParams(Constant.SETPAYCHANNEL);
        params.addBodyParameter("userId", mUid);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 后退按钮点击操作
                // 结束当前的Activity
                finish();
                break;

            case R.id.tv_next://保存支付通道
                /**
                 * 保存支付通道
                 */
                sendSavePayChannel();

                break;
            default:
                break;
        }
    }

    /**
     * 保存支付通道
     */
    private void sendSavePayChannel() {
        RequestParams params = new RequestParams(Constant.SAVEPAYCHANNEL);
        params.addBodyParameter("userId", mUid);
        params.addBodyParameter("channelPayId", channelPayId);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tvNext.setTextColor(Color.parseColor("#ff9900"));
        tvNext.setClickable(true);
        channelPayId = payChannelList.get(position).SetPayChannel.payId + "";
        payIdSelect = payChannelList.get(position).SetPayChannel.payId;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_REGISTER, errorContent, null);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        if (url.equals(Constant.SETPAYCHANNEL)) {
            payIdSelect = jsonObject.getIntValue("payId");
            payChannelList = JSON.parseArray(jsonObject.getString("payChannelList"), PayChannelList.class);

            mAdapter = new RechargeAisleAdapter(this, payChannelList);
            lvRechargeChannel.setAdapter(mAdapter);
        }
        if (url.equals(Constant.SAVEPAYCHANNEL)) {
            finish();
        }

    }

    /**
     * 支付通道适配器
     */
    private class RechargeAisleAdapter extends NewBaseAdapter {

        public RechargeAisleAdapter(Context context, List list) {
            super(context, list);
        }

        @Override
        public int getContentView() {
            return R.layout.activity_recharge_channel;
        }

        @Override
        public void onInitView(View view, int position) {

            SetPayChannel setPayChannel = payChannelList.get(position).SetPayChannel;
            //支付通道图标
            ImageView payLogo = (ImageView) view.findViewById(R.id.iv_payLogo);
            //选中的支付通道
            ImageView ivPaySelect = (ImageView) view.findViewById(R.id.iv_pay_select);
            //支付通道名字
            TextView tvpayName = (TextView) view.findViewById(R.id.tv_payName);
            //单笔限额，单日限额
            TextView tvLimitExplain = (TextView) view.findViewById(R.id.tv_limitExplain);

            tvpayName.setText(setPayChannel.payName);
            tvLimitExplain.setText(payChannelList.get(position).limitExplain);
            String imagUrl = Constant.IMAGE_ADDRESS + setPayChannel.payLogo;
            app.setDisplayImage(imagUrl, payLogo);
            if (payIdSelect == setPayChannel.payId) {
                ivPaySelect.setVisibility(View.VISIBLE);
            } else {
                ivPaySelect.setVisibility(View.GONE);
            }
        }
    }
}

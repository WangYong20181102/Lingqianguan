package com.wtjr.lqg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.widget.ListViewForScrollView;

import org.xutils.http.RequestParams;

/**
 * Created by WangXu on 2018/1/22.
 */

public class WeekProjectDetailActivity extends BaseActivity implements View.OnClickListener, HttpUtil.HttpRequesListener {
    private ImageButton imgBtnBack;
    private TextView tvTitleName;
    private TextView tvNext;
    /**
     * 标id
     */
    private String bid;
    /**
     * 待投资总额
     */
    private TextView tvWeekTotal;
    /**
     * 借款期限
     */
    private TextView tvWeekBorrowDays;
    /**
     * 借款人
     */
    private TextView tvWeekBorrowName;
    /**
     * 借款详情
     */
    private TextView tvWeekBorrowDetail;
    private ListViewForScrollView lvMaterial;
    private JSONArray checkList;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_weekprojrct_detail);
        start();
    }


    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tvWeekTotal = (TextView) findViewById(R.id.tv_week_total);
        tvWeekBorrowDays = (TextView) findViewById(R.id.tv_week_borrow_days);
        tvWeekBorrowName = (TextView) findViewById(R.id.tv_week_borrow_name);
        tvWeekBorrowDetail = (TextView) findViewById(R.id.tv_week_borrow_detail);
        lvMaterial = (ListViewForScrollView) findViewById(R.id.lv_material);
    }

    @Override
    public void setTitle() {
        tvTitleName.setText("项目详情");
        imgBtnBack.setVisibility(View.VISIBLE);
        tvNext.setVisibility(View.VISIBLE);
        tvNext.setText("借款协议");
    }

    @Override
    public void setListener() {
        imgBtnBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void initIntent() {
        bid = getIntent().getStringExtra("bid");

    }

    @Override
    public void initData() {

        sendProjectDetail();
    }

    /**
     * 请求周周升项目详情
     */
    private void sendProjectDetail() {

        RequestParams params = new RequestParams(Constant.PLANPROJECT_BORROWDETAIL);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("bid", bid);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
                finish();
                break;

            case R.id.tv_next:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.WEEKWEEKUP_XIYI);
                intent.putExtra("TitleName", "借款协议");
                startActivity(intent);

                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(String url, String errorContent) {

        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        JSONObject brrowDetail = JSON.parseObject(jsonObject.getString("borrowerDetail"));
        tvWeekTotal.setText(brrowDetail.getString("loanMoney") + "");
        tvWeekBorrowDays.setText(brrowDetail.getString("loanDays") + "");
        String name = StringUtil.setBlurryName(brrowDetail.getString("borrowerName"));
        tvWeekBorrowName.setText(name);
        tvWeekBorrowDetail.setText(brrowDetail.getString("controlMsg"));

        checkList = jsonObject.getJSONArray("checkList");
        lvMaterial.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return checkList.size();
            }

            @Override
            public Object getItem(int position) {
                return checkList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_week_brrowdetail, null);
                }

                TextView tvMaterial = (TextView) convertView.findViewById(R.id.tv_week_material);
                tvMaterial.setText(checkList.get(position) + "");
                return convertView;
            }
        });

    }
}

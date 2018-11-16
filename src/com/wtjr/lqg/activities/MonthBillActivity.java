package com.wtjr.lqg.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.MonthBill;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.NetworkUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.widget.BillDetailDialog;
import com.wtjr.lqg.widget.BillLineChartView;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by WangXu on 2016/12/21.
 */
public class MonthBillActivity extends BaseActivity implements View.OnClickListener, HttpUtil.HttpRequesListener {
    /**
     * 返回按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 标题
     */
    private TextView tvTitleName;
    /**
     * 折线图
     */
    private BillLineChartView billLineChartView;
    /**
     * 充值金额
     */
    private LinearLayout llBill0;
    private View vBill0;
    /**
     * 提现金额
     */
    private LinearLayout llBill1;
    private View vBill1;
    /**
     * 定期标投资金额
     */
    private LinearLayout llBill2;
    private View vBill2;
    /**
     * 收益
     */
    private LinearLayout llBill3;
    private View vBill3;
    /**
     * 获得体验金
     */
    private LinearLayout llBill4;
    private View vBill4;
    /**
     * 点击弹出对话框的图片
     */
    private RelativeLayout rlMmBill;
    /**
     * 近五个月的弹出对话框
     */
    private Dialog dialog;
    /**
     * 对话框的阴影部分
     */
    private RelativeLayout rlBillDetailDialog;
    /**
     * 对话框的第一个月
     */
    private TextView tvMonth0;
    /**
     * 对话框的第二个月
     */
    private TextView tvMonth1;
    /**
     * 对话框的第三个月
     */
    private TextView tvMonth2;
    /**
     * 对话框的第四个月
     */
    private TextView tvMonth3;
    /**
     * 对话框的第五个月
     */
    private TextView tvMonth4;
    /**
     * 选中的月份的位置(默认选中最后一个月)
     */
    private int selectMonth = 4;
    /**
     * 选中的月份请求的参数(默认是当前月份)
     */
    private String yearAndMonth = "";
    /**
     * 充值金额
     */
    private TextView tvChargeSum;
    /**
     * 提现金额
     */
    private TextView tvWithdrawSum;
    /**
     * 定期标投资金额
     */
    private TextView tvInvestSum;
    /**
     * 收益
     */
    private TextView tvProfitSum;
    /**
     * 获得体验金
     */
    private TextView tvExpSum;
    /**
     * 选中的月份的展示控件
     */
    private TextView tvSelectMonth;
    /**
     * 账单分类情况的控件
     */
    private TextView tvBillCase;
    /**
     * 月份和金额的集合
     */
    private List<MonthBill> dataList = new ArrayList<MonthBill>();
    private String selectType = "";

    private RefreshType mRefreshType;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_month_bill);
        start();
    }

    @Override
    public void findViewById() {
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);

        rlMmBill = (RelativeLayout) findViewById(R.id.rl_mm_bill);

        tvSelectMonth = (TextView) findViewById(R.id.tv_select_month);
        tvBillCase = (TextView) findViewById(R.id.tv_bill_case);

        tvChargeSum = (TextView) findViewById(R.id.tv_chargeSum);
        tvWithdrawSum = (TextView) findViewById(R.id.tv_withdrawSum);
        tvInvestSum = (TextView) findViewById(R.id.tv_investSum);
        tvProfitSum = (TextView) findViewById(R.id.tv_profitSum);
        tvExpSum = (TextView) findViewById(R.id.tv_expSum);

        llBill0 = (LinearLayout) findViewById(R.id.ll_bill0);
        vBill0 = (View) findViewById(R.id.v_bill0);
        llBill1 = (LinearLayout) findViewById(R.id.ll_bill1);
        vBill1 = (View) findViewById(R.id.v_bill1);
        llBill2 = (LinearLayout) findViewById(R.id.ll_bill2);
        vBill2 = (View) findViewById(R.id.v_bill2);
        llBill3 = (LinearLayout) findViewById(R.id.ll_bill3);
        vBill3 = (View) findViewById(R.id.v_bill3);
        llBill4 = (LinearLayout) findViewById(R.id.ll_bill4);
        vBill4 = (View) findViewById(R.id.v_bill4);

        billLineChartView = (BillLineChartView) findViewById(R.id.bill_lineChartView);
        billLineChartView.setLineCircleColor(R.color.FC_FFC966);
    }

    @Override
    public void setTitle() {
        tvTitleName.setText("月账单");

        imgBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void setListener() {
        imgBtnBack.setOnClickListener(this);
        rlMmBill.setOnClickListener(this);
        llBill0.setOnClickListener(this);
        llBill1.setOnClickListener(this);
        llBill2.setOnClickListener(this);
        llBill3.setOnClickListener(this);
        llBill4.setOnClickListener(this);

        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void initData() {
        /**
         * 默认点击充值金额
         */
        llBill0.performClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
                finish();
                break;
            case R.id.rl_mm_bill:
                if (NetworkUtil.isNetworkAvailable(this)) {
                    showBillDetailDialog();
                }
                break;
            case R.id.rl_bill_detail_dialog:
                dialog.dismiss();
                break;
            case R.id.ll_bill0:
                /**
                 * 清除选中状态
                 */
                clearAllStatus();
                tvBillCase.setText("充值金额情况");
                llBill0.setBackgroundResource(R.color.FC_F4F5F9);
                vBill0.setVisibility(View.VISIBLE);
                billLineChartView.setLineCircleColor(getResources().getColor(R.color.FC_FFC966));
                selectType = "1";
                sendMonthBill(RefreshType.RefreshPull);
                break;
            case R.id.ll_bill1:
                /**
                 * 清除选中状态
                 */
                clearAllStatus();
                tvBillCase.setText("提现金额情况");
                llBill1.setBackgroundResource(R.color.FC_F4F5F9);
                vBill1.setVisibility(View.VISIBLE);
                billLineChartView.setLineCircleColor(getResources().getColor(R.color.FC_66CCAD));
                selectType = "2";
                sendMonthBill(RefreshType.RefreshNoPull);
                break;
            case R.id.ll_bill2:
                /**
                 * 清除选中状态
                 */
                clearAllStatus();
                tvBillCase.setText("定期标投资金额情况");
                llBill2.setBackgroundResource(R.color.FC_F4F5F9);
                vBill2.setVisibility(View.VISIBLE);
                billLineChartView.setLineCircleColor(getResources().getColor(R.color.FC_FE8080));
                selectType = "3";
                sendMonthBill(RefreshType.RefreshNoPull);
                break;
            case R.id.ll_bill3:
                /**
                 * 清除选中状态
                 */
                clearAllStatus();
                tvBillCase.setText("收益情况");
                llBill3.setBackgroundResource(R.color.FC_F4F5F9);
                vBill3.setVisibility(View.VISIBLE);
                billLineChartView.setLineCircleColor(getResources().getColor(R.color.FC_A49EFC));
                selectType = "4";
                sendMonthBill(RefreshType.RefreshNoPull);
                break;
            case R.id.ll_bill4:
                /**
                 * 清除选中状态
                 */
                clearAllStatus();
                tvBillCase.setText("获得体验金情况");
                llBill4.setBackgroundResource(R.color.FC_F4F5F9);
                vBill4.setVisibility(View.VISIBLE);
                billLineChartView.setLineCircleColor(getResources().getColor(R.color.FC_66D6Ff));
                selectType = "5";
                sendMonthBill(RefreshType.RefreshNoPull);
                break;

            case R.id.tv_month0:
                selectMonth = 4;
                if (dataList != null) {
                    yearAndMonth = dataList.get(4).mon;
                }
                dialog.dismiss();
                tvSelectMonth.setText("本月");
//                clearAllStatus();
//                llBill0.setBackgroundResource(R.color.FC_F4F5F9);
//                vBill0.setVisibility(View.VISIBLE);
//                billLineChartView.setLineCircleColor(getResources().getColor(R.color.FC_FFC966));
                sendMonthBill(RefreshType.RefreshNoPull);
                break;
            case R.id.tv_month1:
                selectMonth = 3;
                if (dataList != null) {
                    yearAndMonth = dataList.get(3).mon;
                    tvSelectMonth.setText(TimeUtil.formatYYYYMM(yearAndMonth));
                }
                dialog.dismiss();
                sendMonthBill(RefreshType.RefreshNoPull);
                break;
            case R.id.tv_month2:
                selectMonth = 2;
                if (dataList != null) {
                    yearAndMonth = dataList.get(2).mon;
                    tvSelectMonth.setText(TimeUtil.formatYYYYMM(yearAndMonth));
                }
                dialog.dismiss();
                sendMonthBill(RefreshType.RefreshNoPull);
                break;
            case R.id.tv_month3:
                selectMonth = 1;
                if (dataList != null) {
                    yearAndMonth = dataList.get(1).mon;
                    tvSelectMonth.setText(TimeUtil.formatYYYYMM(yearAndMonth));
                }
                dialog.dismiss();
                sendMonthBill(RefreshType.RefreshNoPull);
                break;
            case R.id.tv_month4:
                selectMonth = 0;
                if (dataList != null) {
                    yearAndMonth = dataList.get(0).mon;
                    tvSelectMonth.setText(TimeUtil.formatYYYYMM(yearAndMonth));
                }
                dialog.dismiss();
                sendMonthBill(RefreshType.RefreshNoPull);
                break;
            default:
                break;
        }
    }

    /**
     * 选择月份弹出框
     */
    private void showBillDetailDialog() {
        dialog = BillDetailDialog.showBillMmDialog(this);
        rlBillDetailDialog = (RelativeLayout) dialog.findViewById(R.id.rl_bill_detail_dialog);
        rlBillDetailDialog.setOnClickListener(this);

        tvMonth0 = (TextView) dialog.findViewById(R.id.tv_month0);
        tvMonth1 = (TextView) dialog.findViewById(R.id.tv_month1);
        tvMonth2 = (TextView) dialog.findViewById(R.id.tv_month2);
        tvMonth3 = (TextView) dialog.findViewById(R.id.tv_month3);
        tvMonth4 = (TextView) dialog.findViewById(R.id.tv_month4);
        if (dataList != null) {
            tvMonth0.setText(TimeUtil.formatYYYYMM(dataList.get(4).mon));
            tvMonth1.setText(TimeUtil.formatYYYYMM(dataList.get(3).mon));
            tvMonth2.setText(TimeUtil.formatYYYYMM(dataList.get(2).mon));
            tvMonth3.setText(TimeUtil.formatYYYYMM(dataList.get(1).mon));
            tvMonth4.setText(TimeUtil.formatYYYYMM(dataList.get(0).mon));
        }
        tvMonth0.setOnClickListener(this);
        tvMonth1.setOnClickListener(this);
        tvMonth2.setOnClickListener(this);
        tvMonth3.setOnClickListener(this);
        tvMonth4.setOnClickListener(this);

    }

    /**
     * 发送月账单的请求
     *
     * @param refreshType
     */
    private void sendMonthBill(RefreshType refreshType) {
        mRefreshType = refreshType;
        RequestParams params = new RequestParams(Constant.MONTH_BILL);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("optType", selectType);
        params.addBodyParameter("yearAndMonth", yearAndMonth);
        httpUtil.sendRequest(params, refreshType, this);

    }

    /**
     * 传入数据重新画折线图
     */
    private void redrawChartByType(final int selectMonth) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 显示折线收益图
                billLineChartView.dataLoading(dataList, selectMonth);
            }
        }, 50);
    }

    /**
     * 清除选中状态
     */
    private void clearAllStatus() {
        llBill0.setBackgroundResource(R.color.white);
        vBill0.setVisibility(View.INVISIBLE);
        llBill1.setBackgroundResource(R.color.white);
        vBill1.setVisibility(View.INVISIBLE);
        llBill2.setBackgroundResource(R.color.white);
        vBill2.setVisibility(View.INVISIBLE);
        llBill3.setBackgroundResource(R.color.white);
        vBill3.setVisibility(View.INVISIBLE);
        llBill4.setBackgroundResource(R.color.white);
        vBill4.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());

        tvChargeSum.setText(MoneyFormatUtil.format(jsonObject.getString("chargeSum")));
        tvWithdrawSum.setText(MoneyFormatUtil.format(jsonObject.getString("withdrawSum")));
        tvInvestSum.setText(MoneyFormatUtil.format(jsonObject.getString("investSum")));
        tvProfitSum.setText(MoneyFormatUtil.format(jsonObject.getString("profitSum")));
        tvExpSum.setText(MoneyFormatUtil.format(jsonObject.getString("expSum")));

        dataList = JSON.parseArray(jsonObject.getString("dataList"), MonthBill.class);
        /**
         * 传入数据重新画折线图
         */
        if (dataList.size() == 5) {
            redrawChartByType(selectMonth);
        }

        noNetAndData(this, jsonBase, jsonObject);
    }
}

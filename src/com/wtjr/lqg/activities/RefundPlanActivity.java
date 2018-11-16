package com.wtjr.lqg.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.adapters.NewBaseAdapter;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.NewBorrow;
import com.wtjr.lqg.base.PlanMasData;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;

/**
 * 还款计划
 *
 * @author JoLie
 */
public class RefundPlanActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 还款期次
     */
    private TextView tvRepayMent;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 还款计划的列表
     */
    private ListView lvRefundLog;
    /**
     * 投资数据类
     */
    private Investment mInvestment;
    /**
     * 判断是新手标（true）还是普通标(false)
     */
    private int borrowState;

    private RefreshType mRefreshType;
    private InvestmentDetailsType mInvestmentDetailsType;
    private NewBorrow newBorrow;
    /**
     * 周周升
     */
    private PlanMasData planMasData;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_refund_plan);
        start();

    }

    @Override
    public void initIntent() {
        borrowState = getIntent().getIntExtra("borrowState",1);
        if (borrowState == 2) {//新手标过来
            newBorrow = (NewBorrow) getIntent().getSerializableExtra(Investment.class.getName());
        } else if (borrowState == 1){
            mInvestment = (Investment) getIntent().getSerializableExtra(Investment.class.getName());
        }else if (borrowState == 3){
            planMasData = (PlanMasData) getIntent().getSerializableExtra(Investment.class.getName());
        }
        mInvestmentDetailsType = (InvestmentDetailsType) getIntent().getSerializableExtra(InvestmentDetailsType.class.getName());
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvRepayMent = (TextView) findViewById(R.id.tv_repaymentTime);
        lvRefundLog = (ListView) findViewById(R.id.lv_refundLog);

        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:活动
        tvTitleName.setText("还款计划");

    }

    @Override
    public void initData() {
        sendActivityReques(RefreshType.RefreshNoPull);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * 发送活动请求
     *
     * @param refreshType
     */
    public void sendActivityReques(RefreshType refreshType) {
        mRefreshType = refreshType;
        RequestParams params = null;
        switch (mInvestmentDetailsType) {
            case LqbInvestment:
                params = new RequestParams(Constant.LQB_QUERYMATCHBORROWPAYBACKPLAN);
                params.addBodyParameter("user_userunid", mUid);
                break;
            default:
                params = new RequestParams(Constant.INVESTMENT_REPAYMENTPLAN);
                break;
        }
        if (mInvestment != null && borrowState == 1) {
            params.addBodyParameter("borrow_id", mInvestment.bId + "");
        }
        if (newBorrow != null && borrowState == 2) {
            params.addBodyParameter("borrow_id", newBorrow.bId + "");
        }
        if (planMasData !=null && borrowState == 3){
            params.addBodyParameter("planId", planMasData.planId + "");
        }
        httpUtil.sendRequest(params, refreshType, this);
    }

    class RefundPlanListAdapter<T> extends NewBaseAdapter<T> {

        private JSONObject jsonObject;

        public RefundPlanListAdapter(Context context, List<T> list, JSONObject jsonObject) {
            super(context, list);
            this.jsonObject = jsonObject;
        }

        @Override
        public int getContentView() {
            return R.layout.item_refund_plan;
        }

        @Override
        public void onInitView(View view, int position) {
            String interest = (String) getItem(position);       //利息

            TextView viewChild2 = getViewChild(view, R.id.tv_money);
            TextView viewChild = getViewChild(view, R.id.tv_time);

            Long long1 = jsonObject.getLong("repaymentBeginTime");
            String strInterest = jsonObject.getString("repaymentMoney");
            int iRepayMentTime = jsonObject.getInteger("repaymentTime");

            // 还款日期
            if (long1 == -1) {
                viewChild.setText("00-00-00");
            } else {
                Date d = new Date(long1);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(d);

                switch (mInvestmentDetailsType) {
                    case LqbInvestment:
                        // 先息后本最后本金
                        if (position == iRepayMentTime) {
                            calendar.add(Calendar.MONTH, position - 1);
                            viewChild.setText(sdf.format(calendar.getTime()));
                            viewChild2.setText(interest);
                        } else {
                            calendar.add(Calendar.MONTH, position);
                            viewChild.setText(sdf.format(calendar.getTime()));
                        }
                        break;
                    default:
                        // 先息后本最后本金
                        if (position == iRepayMentTime) {
                            calendar.add(Calendar.DATE, position - 1);
                            viewChild.setText(sdf.format(calendar.getTime()));
                            viewChild2.setText(interest);
                        } else {
                            calendar.add(Calendar.DATE, position);
                            viewChild.setText(sdf.format(calendar.getTime()));
                        }
                        break;
                }
            }

            // 还款利息
            viewChild2.setText(interest);
        }
    }

    @Override
    public void onFailure(String url, String errorContent) {
        // TODO Auto-generated method stub
        if (errorContent.contains("已下架")) {
            DialogUtil.promptDialog(RefundPlanActivity.this, DialogUtil.HEAD_SERVICE, "温馨提示", errorContent, new DialogUtil.OnClickYesListener() {
                @Override
                public void onClickYes() {
                    Intent intent = new Intent(RefundPlanActivity.this, MainActivity.class);
                    startActivity(intent);
                    // 更新前3个界面
                    Intent mIntent = new Intent(Constant.UPDATE_ALL);
                    mIntent.putExtra("ShowFragmentLocation", 2);
                    sendBroadcast(mIntent, Manifest.permission.receiver_permission);
                }
            });
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());

        setShowData(jsonObject);
    }

    /**
     * 显示数据
     *
     * @param jsonObject
     */
    private void setShowData(JSONObject jsonObject) {
        int repaymentTime = jsonObject.getInteger("repaymentTime");
        tvRepayMent.setText(repaymentTime + "次");

        ArrayList<String> refundPlanList = new ArrayList<String>();
        for (int i = 0; i < repaymentTime + 1; i++) {

            if (i == repaymentTime) {
                refundPlanList.add("本金：" + MoneyFormatUtil.format(jsonObject.getString("bMoney")) + "元");
                break;
            }
            refundPlanList.add("利息：" + MoneyFormatUtil.format(jsonObject.getString("repaymentMoney")) + "元");

        }

        RefundPlanListAdapter<String> adapter = new RefundPlanListAdapter<String>(this, refundPlanList, jsonObject);
        lvRefundLog.setAdapter(adapter);
    }

}

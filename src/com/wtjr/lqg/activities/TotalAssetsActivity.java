package com.wtjr.lqg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.utils.ArithUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.widget.TotalAssetsView;
import com.wtjr.lqg.widget.UpDownTextView;

/**
 * 总资产
 * Created by WangYong on 2017/11/13.
 */
public class TotalAssetsActivity extends BaseActivity implements OnClickListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 收益比列图
     */
    private TotalAssetsView totalAssetsView;
    /**
     * 可用余额
     */
    private TextView tvBalance;
    /**
     * 定期标
     */
    private TextView tvInvestment;
    /**
     * 零钱宝
     */
    private TextView tvLqb;
    /**
     * 新手标
     */
    private TextView tvXsb;
    /**
     * 周周升
     */
    private TextView tvLiCaiPlan;

    private UpDownTextView udtvYesterdayIncome;
    private float balanceR;
    private float lqbR;
    private float investmentR;
    private float newStandardR;
    private float liCaiPlandR;
    private TextView tvNext;

    /**
     * 可用余额RelativeLayout
     */
    private RelativeLayout llAvailable;
    /**
     * 定期标RelativeLayout
     */
    private RelativeLayout llRegular;
    /**
     * 零钱宝RelativeLayout
     */
    private RelativeLayout llLoose;
    /**
     * 新手标RelativeLayout
     */
    private RelativeLayout llNewStandard;
    /**
     * 周周升RelativeLayout
     */
    private RelativeLayout llLiCaiPlan;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_total_assets);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        totalAssetsView = (TotalAssetsView) findViewById(R.id.tav_totalAssetsView);

        llAvailable = (RelativeLayout) findViewById(R.id.ll1);
        llRegular = (RelativeLayout) findViewById(R.id.ll2);
        llLoose = (RelativeLayout) findViewById(R.id.ll3);
        llNewStandard = (RelativeLayout) findViewById(R.id.ll4);
        llLiCaiPlan = (RelativeLayout) findViewById(R.id.ll5);

        tvBalance = (TextView) findViewById(R.id.tv_balance); // 可用余额
        tvInvestment = (TextView) findViewById(R.id.tv_investment); // 定期标
        tvLqb = (TextView) findViewById(R.id.tv_lqb); // 零钱宝
        tvXsb = (TextView) findViewById(R.id.tv_xsb); // 新手标
        tvLiCaiPlan = (TextView) findViewById(R.id.tv_licai_plan);

        udtvYesterdayIncome = (UpDownTextView) findViewById(R.id.udtv_yesterdayIncome);

        tvNext = (TextView) findViewById(R.id.tv_next);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        tvNext.setOnClickListener(this);

        llAvailable.setOnClickListener(this);
        llRegular.setOnClickListener(this);
        llLoose.setOnClickListener(this);
        llNewStandard.setOnClickListener(this);
        llLiCaiPlan.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:总资产
        tvTitleName.setText(R.string.total_assets_name);

        tvNext.setText("资金记录");
        tvNext.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {

        if (app.mAccountData == null) {
            return;
        }

        udtvYesterdayIncome.setUpTextContent(MoneyFormatUtil.format(app.mAccountData.total));

        double add = ArithUtil.add(app.mAccountData.available_money, app.mAccountData.investment);
        final double totel = ArithUtil.add(add, app.mAccountData.lqb);

        String balanceSR = MoneyFormatUtil.format2(app.mAccountData.available_money / totel);
        String lqbSR = MoneyFormatUtil.format2(app.mAccountData.lqb / totel);
        double dInvestmentSR = app.mAccountData.investment - app.mAccountData.xinshouInvest - app.mAccountData.lcjhInvest;    //定期标 = 我的投资总金额 - 新手标金额 -周周升
        String investmentSR = MoneyFormatUtil.format2(dInvestmentSR / totel);
        String newStandardSR = MoneyFormatUtil.format2(app.mAccountData.xinshouInvest / totel);
        final String liCaiPlanSR = MoneyFormatUtil.format2(app.mAccountData.lcjhInvest / totel);

        balanceR = Float.parseFloat(balanceSR);
        lqbR = Float.parseFloat(lqbSR);
        investmentR = Float.parseFloat(investmentSR);
        newStandardR = Float.parseFloat(newStandardSR);
        liCaiPlandR = Float.parseFloat(liCaiPlanSR);
        float cha1 = 1 - balanceR - lqbR - investmentR - newStandardR - liCaiPlandR;
        float cha = Float.parseFloat(MoneyFormatUtil.format2(cha1));

        if (cha != 0) {
            balanceR += cha;
        }

        changeTotalAssetsMoneyColor("#ffa82c", MoneyFormatUtil.format(app.mAccountData.available_money), tvBalance);
        changeTotalAssetsMoneyColor("#fd9a61", MoneyFormatUtil.format(dInvestmentSR), tvInvestment);
        changeTotalAssetsMoneyColor("#fedb41", MoneyFormatUtil.format(app.mAccountData.lqb), tvLqb);
        changeTotalAssetsMoneyColor("#ff682c", MoneyFormatUtil.format(app.mAccountData.xinshouInvest), tvXsb);
        changeTotalAssetsMoneyColor("#ff7c03", MoneyFormatUtil.format(app.mAccountData.lcjhInvest), tvLiCaiPlan);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (totel == 0) {
                    totalAssetsView.show(0, 0, 0, 0, 0);
                } else {
                    totalAssetsView.show(balanceR, investmentR, lqbR, newStandardR, liCaiPlandR);
                }
            }
        }, 500);
    }

    /**
     * 设置总资产界面金额颜色
     */
    private void changeTotalAssetsMoneyColor(String color, String money, TextView textView) {
        Spanned fromHtml = Html.fromHtml("<font color=" + color + ">" + money + "</font>");
        Spanned fromHtml2 = Html.fromHtml("<font color=#999999>" + "元" + "</font>");
        textView.setText(fromHtml);
        textView.append(fromHtml2);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.tv_next:// 资金记录
                startActivity(new Intent(this, BillDetailsActivity.class));
                break;
            case R.id.ll1:      //可用余额
//                intent = new Intent(TotalAssetsActivity.this, AvailableBalanceActivity.class);
                break;
            case R.id.ll2:  //定期标
//                intent = new Intent(TotalAssetsActivity.this, AvailableBalanceActivity.class);
//                intent.putExtra("totalAssetsType", "5");
                break;
            case R.id.ll3:  //零钱宝
//                intent = new Intent(TotalAssetsActivity.this, AvailableBalanceActivity.class);
//                intent.putExtra("totalAssetsType", "3");
                break;
            case R.id.ll4:  //新手标
//                intent = new Intent(TotalAssetsActivity.this, AvailableBalanceActivity.class);
//                intent.putExtra("totalAssetsType", "5");
                break;
            case R.id.ll5:  //周周升
//                intent = new Intent(TotalAssetsActivity.this, AvailableBalanceActivity.class);
//                intent.putExtra("totalAssetsType", "5");
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
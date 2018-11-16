package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.MoneyEditText;
import com.wtjr.lqg.widget.MoneyEditText.OnTextChangeListener;

/**
 * 转出
 *
 * @author JoLie
 */
public class RollOutActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnTextChangeListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 输入转出金额
     */
    private MoneyEditText etRollOutMoney;
    /**
     * 确认转出
     */
    private Button btYesRollOut;
    /**
     * 转出金额
     */
    private String mRollOutMoney;
    /**
     * 可转出金额
     */
    private TextView tvCanRollOutMoney;
    /**
     * 预计少赚收益
     */
    private TextView tvInterest;
    private View tittleBottomLine;
    private String minLqbRate;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_roll_out);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tittleBottomLine = findViewById(R.id.title_bottom_line);
        etRollOutMoney = (MoneyEditText) findViewById(R.id.et_rollOutMoney);
        btYesRollOut = (Button) findViewById(R.id.bt_yesRollOut);

        tvCanRollOutMoney = (TextView) findViewById(R.id.tv_canRollOutMoney);
        //预计少赚收益
        tvInterest = (TextView) findViewById(R.id.tv_interest);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 设置网络监听
        httpUtil.setHttpRequesListener(this);
        //确认转入按钮监听器
        btYesRollOut.setOnClickListener(this);
        //金钱输入监听
        etRollOutMoney.setOnTextChangeListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:转出
        tvTitleName.setText(R.string.roll_out_name);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        tittleBottomLine.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        tvCanRollOutMoney.setText(Html.fromHtml("<font color='#3f1e00'>可转出金额(元): </font>" + "<font color='#fabd46'>" + MoneyFormatUtil.format(app.mLqbData.lqb) + "</font>"));
        etRollOutMoney.setInputMaxMoney(app.mLqbData.lqb);

        btYesRollOut.setEnabled(false);
    }

    @Override
    public void initIntent() {
        minLqbRate = SharedPreferencesUtil.getPrefString(this, "minLqbRate", "");
    }

    /**
     * 金额输入回调监听
     */
    @Override
    public void setTextChange(String money) {

        //计算30天收益
        Double interest = Double.valueOf(Double.parseDouble(minLqbRate) / 100);
        Double outMoney = Double.valueOf(money);
        tvInterest.setText(MoneyFormatUtil.sEarnings(outMoney,interest,365,30));

        if (TextUtils.isEmpty(money) || Double.parseDouble(money) == 0) {
            btYesRollOut.setEnabled(false);
        } else {
            btYesRollOut.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.bt_yesRollOut:// 确认转出
                sendRollOutRequest(RefreshType.RefreshNoPull);
                break;

            default:
                break;
        }
    }


    /**
     * 发送转出请求
     */
    public void sendRollOutRequest(RefreshType refreshType) {
        mRollOutMoney = etRollOutMoney.getText().toString();
        if (TextUtils.isEmpty(mRollOutMoney) || Double.parseDouble(mRollOutMoney) == 0) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请输入正确的转出金额");
            return;
        }


        RequestParams params = new RequestParams(Constant.LQB_LQBROLLOUT);
        params.addBodyParameter("user_userunid", app.mAccountData.uId);
        params.addBodyParameter("lqb2useMoney", mRollOutMoney);
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        finish();

        // 发送账户数据和零钱宝数据更新广播
        Intent mIntent = new Intent(Constant.UPDATE_ACCOUNTDATA_LQBDATA);
        mIntent.putExtra("ShowFragmentLocation", 0);
        sendBroadcast(mIntent, Manifest.permission.receiver_permission);
    }

}
package com.wtjr.lqg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.basecommonly.BaseAppManager;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.utils.BankImageUtil;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.StringUtil;

/**
 * 提现成功
 *
 * @author JoLie
 */
public class WithdrawalSuccessActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 银行名称控件
     */
    private TextView tvBankName;
    //银行名称
    private String bankName;
    /**
     * 银行卡图片控件
     */
    private ImageView tvBankCardImage;
    /**
     * 银行卡号控件
     */
    private TextView tvBankNum;
    //银行卡号
    private String bankNum;
    /**
     * 用户姓名控件
     */
    private TextView tvBankUseName;
    //用户姓名
    private String bankUserName;
    /**
     * 申请金额
     */
    private TextView tvWithdrawMoney;
    //申请金额
    private double withdrawMoney;
    /**
     * 提现手续费控件
     */
    private TextView tvWithPoundage;
    //提现手续费
    private double withPoundage;
    /**
     * 到账金额控件
     */
    private TextView tvWithToAccount;
    //到账金额
    private double withToAccount;
    /**
     * 完成
     */
    private Button btnNext;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_withdrawal_success);
        start();
    }

    @Override
    public void initIntent() {
        bankName = getIntent().getStringExtra("bankName"); //银行名称
        bankNum = getIntent().getStringExtra("bankNum");//银行卡号
        bankUserName = getIntent().getStringExtra("bankUserName");//用户姓名
        withdrawMoney = getIntent().getDoubleExtra("withdrawMoney", 0);//申请金额
        withPoundage = getIntent().getDoubleExtra("withPoundage", 0);//提现手续费
        withToAccount = getIntent().getDoubleExtra("withToAccount", 0);//到账金额
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);

        //银行名称
        tvBankName = (TextView) findViewById(R.id.tv_bankCardName);
        //银行卡图片
        tvBankCardImage = (ImageView) findViewById(R.id.tv_bankCardImage);
        //银行卡号
        tvBankNum = (TextView) findViewById(R.id.tv_bank_num);
        //用户姓名
        tvBankUseName = (TextView) findViewById(R.id.tv_bank_usename);
        //申请金额
        tvWithdrawMoney = (TextView) findViewById(R.id.tv_withdrawMoney);
        //提现手续费
        tvWithPoundage = (TextView) findViewById(R.id.tv_withPoundage);
        //到账金额
        tvWithToAccount = (TextView) findViewById(R.id.tv_withToAccount);

        btnNext = (Button) findViewById(R.id.btn_next);
    }

    @Override
    public void setListener() {
        httpUtil.setHttpRequesListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:活动
        tvTitleName.setText("提现");
    }

    @Override
    public void initData() {
        BankImageUtil.setBankImage(bankName, tvBankCardImage);
        if (!TextUtils.isEmpty(bankName)) {

            tvBankName.setText(bankName);
            tvBankNum.setText(StringUtil.setBlurryBankNumber2(bankNum));
            tvBankUseName.setText(StringUtil.setBlurryName(bankUserName));
            tvWithdrawMoney.setText(MoneyFormatUtil.format(withdrawMoney) + "元");
            if (MoneyFormatUtil.format(withPoundage).equals("0.00")) {
                tvWithPoundage.setText("免手续费");
            } else {
                tvWithPoundage.setText(MoneyFormatUtil.format(withPoundage) + "元");
            }
            tvWithToAccount.setText(MoneyFormatUtil.format(withToAccount) + "元");
        }
    }

    @Override
    public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.imgBtn_back:// 返回按钮点击操作
//			// 结束当前的Activity
//			finish();
//			break;
//		case R.id.btn_next:// 完成按钮点击操作
//		    finish();
//            // 发送账户数据更新广播
//            Intent mIntent = new Intent(Constant.UPDATE_ACCOUNTDATA);
//            mIntent.putExtra("ShowFragmentLocation", 0);
//            sendBroadcast(mIntent, Manifest.permission.receiver_permission);
//            break;
//		default:
//			break;
//		}
        goActivity();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            goActivity();
        }
        return false;
    }

    @Override
    public void goActivity() {
        startActivity(new Intent(this, MainActivity.class));
        Intent mIntent = new Intent(Constant.UPDATE_ACCOUNTDATA);
        mIntent.putExtra("ShowFragmentLocation", 0);
        sendBroadcast(mIntent, Manifest.permission.receiver_permission);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {

    }

}

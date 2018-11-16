package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.TextViewCountDownView;

/**
 * 绑卡短信验证码填写验证码页面
 *
 * @author Myf
 */
public class BandBankVerificationCodeActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 倒计时的时间
     */
    private long mTimeRemaining = 180000;
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 显示的手机号
     */
    private TextView tvPhone;
    /**
     * 提交按钮
     */
    private Button btDetermine;
    /**
     * 短信验证码
     */
    private EditText etSms;
    /**
     * 倒计时
     */
    private TextViewCountDownView tcdvSms;

    /**
     * 状态是绑卡的Ture,反之false
     */
    private boolean isBandBank;
    /**
     * 订单号
     */
    private String mOrderNo;
    /**
     * 充值金额
     */
    private String mChargeMoney;
    /**
     * 银行卡号
     */
    private String mCardNo;
    /**
     * 持卡人
     */
    private String mOwner;
    /**
     * 身份证
     */
    private String mCertNo;
    /**
     * 预留手机号
     */
    private String mReservedPhone;
    /**
     * 开户支行
     */
    private String mOpenAccountBranch;
    /**
     * 卡的id
     */
    private String mBindId;
    /**
     * 用户的id
     */
    private String mUserId;
    /**
     * 省和城市
     */
    private String mOpenAccountLocation;
    /**
     * 第三方验证码
     */
    private String mChargeMsg;
    /**
     * 本次充值渠道
     */
    private String mChargeChannel;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_band_bank_verification_code);
        start();

    }

    @Override
    public void initIntent() {
        // 状态是绑卡的Ture,反之false
        isBandBank = getIntent().getBooleanExtra("isBandBank", false);

        mUserId = getIntent().getStringExtra("user_userunid");
        // 订单号
        mOrderNo = getIntent().getStringExtra("orderNo");
        // 充值金额
        mChargeMoney = getIntent().getStringExtra("charge_money");
        // 卡id
        mBindId = getIntent().getStringExtra("bind_id");
        // 银行卡号
        mCardNo = getIntent().getStringExtra("card_no");
        // 持卡人姓名
        mOwner = getIntent().getStringExtra("owner");
        // 身份证
        mCertNo = getIntent().getStringExtra("cert_no");
        // 预留手机号码
        mReservedPhone = getIntent().getStringExtra("reserved_phone");
        // 开户行所在地
        mOpenAccountLocation = getIntent().getStringExtra("openAccount_location");
        // 开户支行
        mOpenAccountBranch = getIntent().getStringExtra("openAccount_branch");
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);

        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        etSms = (EditText) findViewById(R.id.ledt_sms);

        tcdvSms = (TextViewCountDownView) findViewById(R.id.tcdv_sms);

        tvPhone = (TextView) findViewById(R.id.tv_phone);

        btDetermine = (Button) findViewById(R.id.bt_determine);

    }

    @Override
    public void setListener() {
        // 网络请求
        httpUtil.setHttpRequesListener(this);
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 重新发送
        tcdvSms.setOnClickListener(this);
        // 提交
        btDetermine.setOnClickListener(this);

        etSms.addTextChangedListener(new ButtonEnabledListener(btDetermine, etSms));
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:活动
        tvTitleName.setText(R.string.verification_code_name);

    }

    @Override
    public void initData() {
        // 启动计时前先取消
        tcdvSms.cancelCountDown();
        // 倒计时时间
        tcdvSms.startCountDown(mTimeRemaining);

        tvPhone.setText(StringUtil.setBlurryPhone(mReservedPhone));
    }

    /**
     * 设置提示对话框更改图形验证码
     */
    private void setPromptDialog(String str1) {
        DialogUtil.promptDialog(BandBankVerificationCodeActivity.this, DialogUtil.HEAD_REGISTER, str1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.bt_determine:// 提交
                sendBandVerification(RefreshType.RefreshNoPull);
                break;
            case R.id.tcdv_sms:// 重新发送
                etSms.setText("");
                repeatSendSms(RefreshType.RefreshNoPull);
                break;
        }
    }

    /**
     * 提交短信验证
     */
    private void sendBandVerification(RefreshType type) {
        mChargeMsg = etSms.getText().toString();
        if (TextUtils.isEmpty(mChargeMsg)) {
            setPromptDialog("请输入验证码");
            return;
        }

        RequestParams params = new RequestParams(Constant.PAY_CONFIRMPAYMENT);

        if (TextUtils.isEmpty(app.mAccountData.bankNum)) {// 没绑卡
            // 绑卡的id
            params.addBodyParameter("bind_id", mBindId);
            // 订单号
            params.addBodyParameter("orderNo", mOrderNo);
            // 用户唯一标识
            params.addBodyParameter("user_userunid", mUserId);
            // 充值金额
            params.addBodyParameter("charge_money", mChargeMoney);
            // 银行卡号
            params.addBodyParameter("card_no", mCardNo);
            // 持卡人姓名
            params.addBodyParameter("owner", mOwner);
            // 身份证号
            params.addBodyParameter("cert_no", mCertNo);
            // 银行预留手机号
            params.addBodyParameter("reserved_phone", mReservedPhone);
            // 开户行所在地
            params.addBodyParameter("openAccount_location", mOpenAccountLocation);
            // 开户支行
            params.addBodyParameter("openAccount_branch", mOpenAccountBranch);
            // 短信验证码
            params.addBodyParameter("charge_msg", mChargeMsg);
        } else {// 已绑卡
            // 用户ID
            params.addBodyParameter("user_userunid", mUid);
            // 订单号
            params.addBodyParameter("orderNo", mOrderNo);
            // 短信验证码
            params.addBodyParameter("charge_msg", mChargeMsg);
        }

        httpUtil.sendRequest(params, type, this);

    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    /**
     * 重新发送短信验证码
     */
    private void repeatSendSms(RefreshType type) {

        RequestParams params = new RequestParams(Constant.PAY_SENDPAYMSG);
        // 用户唯一标识
        params.addBodyParameter("user_userunid", mUid);
        // 订单号
        params.addBodyParameter("orderNo", mOrderNo);

        httpUtil.sendRequest(params, type, this);

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.PAY_CONFIRMPAYMENT)) {
            JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            if (jsonObject != null) {
                mChargeChannel = jsonObject.getString("chargeChannel");
                if (jsonObject.getInteger("channelStatus") != null && jsonObject.getInteger("channelStatus") == 1) {
                    /**
                     * 设置支付通道弹窗
                     */
                    showChannelDialog();

                }
                String strState = jsonObject.getString("wy");
                if (!TextUtils.isEmpty(strState) && strState.equals("-2")){
                    DialogUtil.promptDialog(BandBankVerificationCodeActivity.this, DialogUtil.HEAD_REGISTER, "交易处理中", new OnClickYesListener() {
                        @Override
                        public void onClickYes() {
                            goActivity();
                        }
                    });
                }
            } else {
                if (TextUtils.isEmpty(app.mAccountData.bankNum)) {
                    DialogUtil.promptDialog(BandBankVerificationCodeActivity.this, DialogUtil.HEAD_REGISTER, "祝贺你开通快捷支付并充值"+mChargeMoney+"元成功啦!", new OnClickYesListener() {
                        @Override
                        public void onClickYes() {
                            goActivity();
                        }
                    });
                }else{
                    DialogUtil.promptDialog(BandBankVerificationCodeActivity.this, DialogUtil.HEAD_REGISTER, "成功充值"+mChargeMoney+"元", new OnClickYesListener() {
                        @Override
                        public void onClickYes() {
                            goActivity();
                        }
                    });
                }
            }
        } else {// 重新获取短信
            // 启动计时前先取消
            tcdvSms.cancelCountDown();
            // 倒计时时间
            tcdvSms.startCountDown(mTimeRemaining);
        }
    }

    /**
     * 设置支付通道弹窗
     */
    private void showChannelDialog() {
        DialogUtil.paymentChannelsDialogPrompt(this, mChargeChannel, new DialogUtil.OnClickYesListener() {
            @Override
            public void onClickYes() {
                startActivity(new Intent(BandBankVerificationCodeActivity.this, PayThirdpartyActivity.class));
            }
        });

    }

    @Override
    public void goActivity() {
        // 发送更新数据广播
        Intent mIntent = new Intent(Constant.UPDATE_ACCOUNTDATA);
        sendBroadcast(mIntent, Manifest.permission.receiver_permission);
        // 充值成功跳转到首页
        Intent intent = new Intent(BandBankVerificationCodeActivity.this, MainActivity.class);
        startActivity(intent);
    }

}

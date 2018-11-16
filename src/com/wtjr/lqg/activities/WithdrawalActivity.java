package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.PayPasswordType;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.IsLmmEvmImageUtil;
import com.wtjr.lqg.utils.BankImageUtil;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CircleImageView;
import com.wtjr.lqg.widget.MoneyEditText;
import com.wtjr.lqg.widget.TextViewCountDownView;
import com.wtjr.lqg.widget.paykeyboard.PayDialog;

/**
 * 提现页面
 *
 * @author Myf
 */
public class WithdrawalActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
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
     * 下一步
     */
    private Button btNext;
    /**
     * 银行的圆形图标
     */
    private CircleImageView civBank;
    /**
     * 银行的名字
     */
    private TextView tvBankName;
    /**
     * 银行的名字
     */
    private TextView tvBankCardNumber;
    /**
     * 显示充值的金额
     */
    private TextView tvRechargeMoney;

    /**
     * 短信验证码
     */
    private EditText etSms;
    /**
     * 倒计时
     */
    private TextViewCountDownView tcdvSms;
    /**
     * 提现金额
     */
    private String mWithdrawMoney;
    /**
     * 提现金额输入框
     */
    private MoneyEditText meWithdrawal;
    /**
     * 充值最大金额
     */
    private String maxWithDrawMoney;
    // /**
    // * 免费提现次数
    // */
    // private String mFreeWithdrawCount;

    /**
     * 支付密码的对话框
     */
    private PayDialog payDialog;
    /**
     * 支付密码的圆点
     */
    private LinearLayout llPasswordCircle;
    /**
     * 提现手续费
     */
    private TextView tvPoundage;
    /**
     * 提现手续费
     */
    private int mWithdrawalPoundage;
    /**
     * 提现券数量
     */
    private int mWithdrawalCount;
    private Intent intent;
    private ImageButton imgBtnNext;
    /**
     * 提现对话框内容
     */
    private String tixianMsg;
    /**
     * 剩余提现券文字
     */
    private TextView tvTixianNumber;
    /**
     * 是否使用提现券的勾选图片
     */
    private ImageView ivTixianNumber;
    /**
     * 默认不使用提现券
     */
    private int selectWithDraw = 0;
    /**
     * 是否使用提现券
     */
    private boolean bWithDraw = false;
    /**
     * 是否使用提现券的父布局
     */
    private LinearLayout llTixianNumber;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_withdrawal);
        start();

    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);

        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        civBank = (CircleImageView) findViewById(R.id.civ_bank);
        tvRechargeMoney = (TextView) findViewById(R.id.tv_recharge_money);
        tvBankName = (TextView) findViewById(R.id.tv_bank_name);
        tvBankCardNumber = (TextView) findViewById(R.id.tv_bank_card_number);
        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);
        meWithdrawal = (MoneyEditText) findViewById(R.id.me_withdrawal);

        etSms = (EditText) findViewById(R.id.et_sms);

        tcdvSms = (TextViewCountDownView) findViewById(R.id.tcdv_sms);

        btNext = (Button) findViewById(R.id.btn_next);

        tvPoundage = (TextView) findViewById(R.id.tv_poundage);
        tvTixianNumber = (TextView) findViewById(R.id.tv_tixian_number);
        ivTixianNumber = (ImageView) findViewById(R.id.iv_tixian_number);
        llTixianNumber = (LinearLayout) findViewById(R.id.ll_tixian_number);
    }

    @Override
    public void setListener() {
        // 网络请求
        httpUtil.setHttpRequesListener(this);
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 下一步
        btNext.setOnClickListener(this);
        //常见问题按钮监听器
        imgBtnNext.setOnClickListener(this);
        // 重新发送
        tcdvSms.setOnClickListener(this);
        //是否使用提现券
//        ivTixianNumber.setOnClickListener(this);
        llTixianNumber.setOnClickListener(this);

        meWithdrawal.addTextChangedListener(new ButtonEnabledListener(btNext, meWithdrawal));
    }

    @Override
    public void setTitle() {
        // 设置名字为:提现
        tvTitleName.setText(R.string.withdrawal_name);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        imgBtnNext.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(app.mAccountData.bankNum)) {// 没绑卡
            DialogUtil.promptDialog(this, DialogUtil.HEAD_REGISTER, "您还未绑定银行卡!", new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    finish();
                    Intent intent = new Intent(WithdrawalActivity.this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    startActivity(intent);
                }
            });
            return;
        } else {
            int length = app.mAccountData.bankNum.length();
            String bankNum = app.mAccountData.bankNum.substring(length - 4, length);
            tvBankCardNumber.setText("储蓄卡(" + bankNum + ")");

            tvBankName.setText(app.mAccountData.bankName);
            BankImageUtil.setBankImage(app.mAccountData.bankName, civBank);
        }

        tcdvSms.setText("获取短信");
        meWithdrawal.setTextView(tvRechargeMoney);
        meWithdrawal.setHint("可用余额:" + MoneyFormatUtil.format(app.mAccountData.available_money) + "元");

        sendPoundageAvailableRequest(RefreshType.RefreshNoPull);
        sendIsSetPayPasswordRequest(RefreshType.RefreshNoPull);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.tcdv_sms:// 获取短信
                // 启动计时前先取消
                // tcdvSms.cancelCountDown();
                // //倒计时时间
                // tcdvSms.startCountDown(mTimeRemaining);
                // sendGetSmsRequest(RefreshType.RefreshNoPull);
                break;
            case R.id.imgBtn_next:// 提现问题
                intent = new Intent(WithdrawalActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_TIXIAN);
                intent.putExtra("TitleName", "提现问题");
                startActivity(intent);
                break;
            case R.id.ll_tixian_number://是否使用提现券
                selectWithDraw++;
                if (selectWithDraw % 2 == 1) {
                    ivTixianNumber.setImageResource(R.drawable.ic_unselect);
                    tvPoundage.setText(Html.fromHtml("<font color='#3f1e00'>提现手续费</font>" + "<font color='#fabd46'>" + mWithdrawalPoundage + "</font>" +
                            "<font color='#3f1e00'>元</font>"));
                    bWithDraw = false;
                } else {
                    ivTixianNumber.setImageResource(R.drawable.ic_select);
                    tvPoundage.setText(Html.fromHtml("<font color='#3f1e00'>提现手续费</font>" + "<font color='#fabd46'>" + 0 + "</font>" +
                            "<font color='#3f1e00'>元</font>"));
                    bWithDraw = true;
                }


                break;
            case R.id.btn_next:// 下一步
                mWithdrawMoney = meWithdrawal.getText().toString();
                if (TextUtils.isEmpty(mWithdrawMoney)) {
                    DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请输入提现金额");
                    return;
                } else {
                    if (Double.parseDouble(mWithdrawMoney) == 0) {
                        DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "提现金额需大于0");
                        return;
                    }

                    if (Double.parseDouble(mWithdrawMoney) <= mWithdrawalPoundage && bWithDraw == false) {
                        DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "提现金额需大于提现手续费");
                        return;
                    }

                    if (Double.parseDouble(mWithdrawMoney) > app.mAccountData.available_money) {
                        DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "提现金额不可大于可用余额");
                        return;
                    }
                }
                // 判断是否重新获取验证码
                final boolean countdownTimerState = TimeUtil.getCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
                if (mWithdrawalPoundage > 0 && !TextUtils.isEmpty(tixianMsg)) {
                    DialogUtil.selectDialog(WithdrawalActivity.this, DialogUtil.HEAD_SERVICE, tixianMsg, "确认提现", new OnClickYesListener() {
                        @Override
                        public void onClickYes() {
                            if (countdownTimerState) {
                                sendGetSmsRequest(RefreshType.RefreshNoPull);
                            } else {
                                Intent intent = new Intent(WithdrawalActivity.this, WithdrawalSMSVerifyActivity.class);
                                intent.putExtra("WithdrawMoney", Double.parseDouble(mWithdrawMoney));
                                intent.putExtra("isUseCard", bWithDraw);
                                startActivity(intent);
                            }
                        }
                    }, "取消", new DialogUtil.OnClickNoListener() {
                        @Override
                        public void onClickNo() {

                        }
                    });
                } else {
                    if (countdownTimerState) {
                        sendGetSmsRequest(RefreshType.RefreshNoPull);
                    } else {
                        Intent intent = new Intent(this, WithdrawalSMSVerifyActivity.class);
                        intent.putExtra("WithdrawMoney", Double.parseDouble(mWithdrawMoney));
                        intent.putExtra("isUseCard", bWithDraw);
                        startActivity(intent);
                    }
                }
                break;
        }
    }

    // /**
    // * 金额输入框回调
    // */
    // @Override
    // public void setTextChange(String money) {
    // if(TextUtils.isEmpty(money) || Double.parseDouble(money) == 0) {
    // btNext.setEnabled(false);
    // }else {
    // btNext.setEnabled(true);
    // }
    // }

    /**
     * 提现获取手续费和最大提现金额
     */
    private void sendPoundageAvailableRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.PAY_CHECKPOUNDAGEANDAVAILABLE);
        // 用户唯一标识
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }


    /**
     * 发送获取短信请求
     */
    private void sendGetSmsRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.PAY_VERIFICATIONCODE);
        // 用户唯一标识
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 判断是否设置过支付密码
     */
    public void sendIsSetPayPasswordRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (url.equals(Constant.PAY_CHECKPOUNDAGEANDAVAILABLE)) {
            finish();
        } else if (url.equals(Constant.PAY_WITHDRAWDEPOSIT)) {// 提现失败
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
        } else if (url.equals(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE)) {// 是否设置过支付密码
            finish();
        } else if (url.equals(Constant.PAY_VERIFICATIONCODE)) {// 获取短信
            // //启动计时前先取消
            // tcdvSms.cancelCountDown();
            // //倒计时时间
            // tcdvSms.startCountDown(mTimeRemaining);

            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
        } else if (url.equals(Constant.USERINFO_CHECKVERIFYCODE)) {// 短信验证通过
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());

        // 手续费
        if (url.equals(Constant.PAY_CHECKPOUNDAGEANDAVAILABLE)) {
            // mFreeWithdrawCount=jsonObject.getString("freeWithdrawCount");
            maxWithDrawMoney = jsonObject.getString("maxWithDrawMoney");
            String withdrawCount = jsonObject.getString("withdrawCount");
            tixianMsg = jsonObject.getString("tixianMsg");
            String withPoundage = jsonObject.getString("withPoundage");
            if (TextUtils.isEmpty(withPoundage)) {
                mWithdrawalPoundage = 0;
            } else {
                mWithdrawalPoundage = Integer.parseInt(withPoundage);
            }
            if (TextUtils.isEmpty(withdrawCount)) {
                mWithdrawalCount = 0;
            } else {
                mWithdrawalCount = Integer.parseInt(withdrawCount);
            }
            if (mWithdrawalCount <= 0) {
                ivTixianNumber.setImageResource(R.drawable.ic_unselect);
                bWithDraw = false;
                llTixianNumber.setClickable(false);
                tvPoundage.setText(Html.fromHtml("<font color='#3f1e00'>提现手续费</font>" + "<font color='#fabd46'>" + mWithdrawalPoundage + "</font>" + "<font color='#3f1e00'>元</font>"));
            }else {
                ivTixianNumber.setImageResource(R.drawable.ic_select);
                bWithDraw = true;
                llTixianNumber.setClickable(true);
                tvPoundage.setText(Html.fromHtml("<font color='#3f1e00'>提现手续费</font>" + "<font color='#fabd46'>" + 0 + "</font>" + "<font color='#3f1e00'>元</font>"));
            }
            tvTixianNumber.setText(Html.fromHtml("<font color='#3f1e00'>使用提现券(剩余</font>" + "<font color='#fabd46'>" + mWithdrawalCount + "</font>" + "<font color='#3f1e00'>张)</font>"));
            meWithdrawal.setHint("可用余额:" + MoneyFormatUtil.format(maxWithDrawMoney) + "元");

        } else if (url.equals(Constant.PAY_VERIFICATIONCODE)) {// 获取短信
            // //启动计时前先取消
            // tcdvSms.cancelCountDown();
            // //倒计时时间
            // tcdvSms.startCountDown(mTimeRemaining);
            // 存储倒计时的时间
            TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            Intent intent = new Intent(this, WithdrawalSMSVerifyActivity.class);
            intent.putExtra("WithdrawMoney", Double.parseDouble(mWithdrawMoney));
            intent.putExtra("isUseCard", bWithDraw);
            startActivity(intent);

        } else if (url.equals(Constant.PAY_WITHDRAWDEPOSIT)) {// 提交提现请求
            finish();

            Intent intent = new Intent(this, WithdrawalSuccessActivity.class);
            intent.putExtra("bankName", jsonObject.getString("bankName"));//银行名称
            intent.putExtra("bankNum", jsonObject.getString("bankNum"));//银行卡号
            intent.putExtra("bankUserName", jsonObject.getString("bankUserName"));//用户姓名
            intent.putExtra("withdrawMoney", jsonObject.getDouble("withdrawMoney"));//申请金额
            intent.putExtra("withPoundage", jsonObject.getDouble("withPoundage"));//提现手续费
            intent.putExtra("withToAccount", jsonObject.getDouble("withToAccount"));//到账金额
            startActivity(intent);

        } else if (url.equals(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE)) {// 是否设置过支付密码
            // true有设置过支付密码，false没有设置过支付密码
            Boolean isPayPassword = jsonObject.getBoolean("payPasswordisExist");

            if (!isPayPassword) {// 没设置过支付密码
                DialogUtil.promptDialog(this, DialogUtil.HEAD_REGISTER, "还未设置过支付密码", new OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        finish();
                        Intent intent = new Intent(WithdrawalActivity.this, PayPasswordSetActivity.class);
                        intent.putExtra(PayPasswordType.class.getName(), PayPasswordType.PayPasswordNotHaveSet);
                        startActivity(intent);
                    }
                });
            }
        }
    }

}
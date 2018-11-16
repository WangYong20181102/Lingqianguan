package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.AnimationUtil;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.SystemUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CircleProgress;
import com.wtjr.lqg.widget.TextViewCountDownView;
import com.wtjr.lqg.widget.paykeyboard.PayDialog;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView.OnPayListener;

/**
 * 短信验证码验证界面(修改手机号)
 *
 * @author Myf
 */
public class WithdrawalSMSVerifyActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
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
     * 确定按钮
     */
    private Button btDetermine;
    /**
     * 显示手机号
     */
    private TextView tvPhone;
    /**
     * 短信验证码
     */
    private EditText etSms;
    /**
     * 短信验证码
     */
    private String mMessageCode;
    /**
     * 验证码倒计时
     */
    private TextViewCountDownView tcdvSms;


    private double mWithdrawMoney;

    /**
     * 支付密码的对话框
     */
    private PayDialog payDialog;
    /**
     * 支付密码的圆点
     */
    private LinearLayout llPasswordCircle;
    private LinearLayout llCircleprogress;
    private CircleProgress circleProgress;
    private LinearLayout payKeyBoard;
    private LinearLayout payPasswordErrorProblem;
    private PayPasswordView payPasswordView;
    private TextView payPasswordError;
    private boolean isUseCard;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_password_verification_code);
        start();

    }

    @Override
    public void initIntent() {
        mWithdrawMoney = getIntent().getDoubleExtra("WithdrawMoney", 0);
        isUseCard = getIntent().getBooleanExtra("isUseCard",false);
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        etSms = (EditText) findViewById(R.id.ledt_sms);
        tcdvSms = (TextViewCountDownView) findViewById(R.id.tcdv_sms);
        btDetermine = (Button) findViewById(R.id.bt_determine);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        //设置网络监听
        httpUtil.setHttpRequesListener(this);
        //重新发送
        tcdvSms.setOnClickListener(this);
        //提交
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
        tvPhone.setText(StringUtil.setBlurryPhone(mCurrentPhone));
        //倒计时
        tcdvSms.cancelCountDown();

        //判断是否有倒计时
        boolean timerState = TimeUtil.getCountdownTimerState(this, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
        if (!timerState) {
            Long timeRemaining = TimeUtil.getTimeRemaining(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            tcdvSms.cancelCountDown();
            //倒计时时间
            tcdvSms.startCountDown(timeRemaining);
        }
    }

    /**
     * 设置提示对话框更改图形验证码
     */
    private void setPromptDialog(String str1) {
        DialogUtil.promptDialog(WithdrawalSMSVerifyActivity.this, DialogUtil.HEAD_REGISTER, str1, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.tcdv_sms:// 重新发送短信验证
                etSms.setText("");
                sendGetSmsRequest(RefreshType.RefreshNoPull);
                break;
            case R.id.bt_determine:// 短信验证码提交
                String sms = etSms.getText().toString();
                if (TextUtils.isEmpty(sms)) {
                    DialogUtil.promptDialog(this, "请填写短信验证码");
                    return;
                }

                sendSmsVerifyRequest(RefreshType.RefreshNoPull, sms);
                break;
            default:
                break;
        }
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
     * 发送短信验证请求
     */
    private void sendSmsVerifyRequest(RefreshType refreshType, String sms) {
        RequestParams params = new RequestParams(Constant.USERINFO_CHECKVERIFYCODE);
        // 用户唯一标识
        params.addBodyParameter("login_phone", mCurrentPhone);
        params.addBodyParameter("message_Code", sms);
        params.addBodyParameter("optType", "4");
        httpUtil.sendRequest(params, refreshType, this);
    }


    /**
     * 支付密码密码输入对话框
     */
    private void showPayDialog() {
        //防止系统键盘是弹出状态，会覆盖支付键盘
        SystemUtil.closeKeybord(btDetermine, this);

        View view = getDecorViewDialog();
        llPasswordCircle = (LinearLayout) view.findViewById(R.id.ll_passwordCircle);
        TextView tvPayType = (TextView) view.findViewById(R.id.tv_payType);
        TextView tv_paymentValue = (TextView) view.findViewById(R.id.tv_paymentValue);
        payPasswordError = (TextView) view.findViewById(R.id.pay_password_error_text);

        llCircleprogress = (LinearLayout) view.findViewById(R.id.ll_circleprogress);
        circleProgress = (CircleProgress) view.findViewById(R.id.circleprogress);
        payKeyBoard = (LinearLayout) view.findViewById(R.id.keyboard);
        payPasswordErrorProblem = (LinearLayout) view.findViewById(R.id.pay_error_problem_linear);
        payKeyBoard.setVisibility(View.VISIBLE);
        payPasswordErrorProblem.setVisibility(View.GONE);

        tvPayType.setVisibility(View.GONE);
        tv_paymentValue.setVisibility(View.GONE);

        if (payDialog == null || !payDialog.isShowing()) {
            payDialog = new PayDialog(this, view);
        }
        payDialog.show();
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation = AnimationUtils.loadAnimation(WithdrawalSMSVerifyActivity.this, R.anim.anim_set_in_down_up);
        payKeyBoard.startAnimation(animation);
    }

    private View getDecorViewDialog() {
        payPasswordView = PayPasswordView.getInstance(this, new OnPayListener() {
            @Override
            public void onSurePay(final String password) {

                payKeyBoard.setVisibility(View.GONE);
                llCircleprogress.setVisibility(View.VISIBLE);
                /**
                 * 开始验证验证码的进度动画
                 */
                payPasswordView.startProgressAnimator();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendWithdrawMoneyRequest(RefreshType.RefreshNoPull, password);
                    }
                }, 1000);

            }

            @Override
            public void onCancelPay() {//关闭支付对话框
                payDialog.dismiss();
                payDialog = null;
            }

            @Override
            public void onContent(String content) {
                String password = content.toString();
                int length = password.length();

                for (int i = 0; i < 6; i++) {
                    llPasswordCircle.getChildAt(i).setVisibility(View.INVISIBLE);
                }

                for (int i = 0; i < length; i++) {
                    llPasswordCircle.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }
        });
        return payPasswordView.getView();
    }

    /**
     * 发送提现请求
     */
    private void sendWithdrawMoneyRequest(RefreshType refreshType, String password) {
        RequestParams params = new RequestParams(Constant.PAY_WITHDRAWDEPOSIT);
        // 用户唯一标识
        params.addBodyParameter("user_userunid", mUid);
        // 提现金额
        params.addBodyParameter("withdraw_money", mWithdrawMoney + "");
        // 提现密码
        params.addBodyParameter("card_no", password);
        //是否使用提现券（0：不使用  1：使用）
        if (isUseCard){
            params.addBodyParameter("is_use_card", "1");
        }else {
            params.addBodyParameter("is_use_card", "0");
        }

        httpUtil.sendRequest(params, refreshType, this);
    }


    @Override
    public void onFailure(String url, String errorContent) {
        if (url.equals(Constant.PAY_WITHDRAWDEPOSIT)) {
            circleProgress.stop();
            if (payDialog != null) {
                payDialog.dismiss();
                payDialog = null;
            }
            if (errorContent.contains("锁定")) {
                DialogUtil.selectDialog(WithdrawalSMSVerifyActivity.this, DialogUtil.HEAD_LOGIN, "", errorContent, "找回密码", new DialogUtil.OnClickYesListener() {

                    @Override
                    public void onClickYes() {
                        startActivity(new Intent(WithdrawalSMSVerifyActivity.this, PayBackPasswordActivity.class));
                    }
                }, "取消", new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
            } else {
                DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
            }
        } else {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
        }

    }

    @Override
    public void onSuccess(String url, final JSONBase jsonBase) {
        if (url.equals(Constant.PAY_VERIFICATIONCODE)) {//发送短信
            //存储倒计时的时间
            TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            //倒计时
            tcdvSms.cancelCountDown();
            tcdvSms.startCountDown(mTimeRemaining);
        } else if (url.equals(Constant.USERINFO_CHECKVERIFYCODE)) {//短信验证通过
            showPayDialog();
        } else if (url.equals(Constant.PAY_WITHDRAWDEPOSIT)) {//支付密码验证通过
            final JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            String payPasswordisRight = jsonObject.getString("payPasswordisRight");
            String strPayPasswordError = jsonObject.getString("pwdErrMsg");
            if (payPasswordisRight.equals("1") || payPasswordisRight.equals("true")) {

//                TimeUtil.cleanTimeRemaining(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);      //提现成功将倒计时置为0
                payKeyBoard.setVisibility(View.GONE);
                /**
                 * 结束验证验证码的进度动画
                 */
                payPasswordView.endProgressAnimator(false);
                circleProgress.setAnimatorStatusListener(new CircleProgress.AddAnimatorEndListener() {
                    @Override
                    public void onAnimatorEnd(boolean animatorEnd) {
                        if (payDialog != null) {
                            payDialog.dismiss();
                            payDialog = null;
                            Intent intent = new Intent(WithdrawalSMSVerifyActivity.this, WithdrawalSuccessActivity.class);
                            intent.putExtra("bankName", jsonObject.getString("bankName"));//银行名称
                            intent.putExtra("bankNum", jsonObject.getString("bankNum"));//银行卡号
                            intent.putExtra("bankUserName", jsonObject.getString("bankUserName"));//用户姓名
                            intent.putExtra("withdrawMoney", jsonObject.getDouble("withdrawMoney"));//申请金额
                            intent.putExtra("withPoundage", jsonObject.getDouble("withPoundage"));//提现手续费
                            intent.putExtra("withToAccount", jsonObject.getDouble("withToAccount"));//到账金额
                            startActivity(intent);
                        }
                    }
                });
            } else if (payPasswordisRight.equals("0") || payPasswordisRight.equals("false")) {
                //停掉验证的所有动画状态设置回初始状态
                circleProgress.stop();
                llCircleprogress.setVisibility(View.GONE);
                payKeyBoard.setVisibility(View.GONE);
                payPasswordErrorProblem.setVisibility(View.VISIBLE);
                payPasswordError.setText(strPayPasswordError);
                payPasswordView.clearPassword();
                llPasswordCircle.setBackgroundResource(R.drawable.password_textfield_error);
                llPasswordCircle.startAnimation(AnimationUtil.startShakeAnimation(WithdrawalSMSVerifyActivity.this));
            } else if (payPasswordisRight.equals("3")) {
                circleProgress.stop();
                if (payDialog != null) {
                    payDialog.dismiss();
                    payDialog = null;
                }
                DialogUtil.selectDialog(WithdrawalSMSVerifyActivity.this, DialogUtil.HEAD_LOGIN, "", strPayPasswordError, "找回密码", new DialogUtil.OnClickYesListener() {

                    @Override
                    public void onClickYes() {
                        startActivity(new Intent(WithdrawalSMSVerifyActivity.this, PayBackPasswordActivity.class));
                    }
                }, "取消", new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
            }


        }
    }

}

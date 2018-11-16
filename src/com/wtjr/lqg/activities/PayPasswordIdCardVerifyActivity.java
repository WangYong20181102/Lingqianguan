package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.app.Dialog;
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

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.AnimationUtil;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.SystemUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.widget.CircleProgress;
import com.wtjr.lqg.widget.TextViewCountDownView;
import com.wtjr.lqg.widget.paykeyboard.PayDialog;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView.OnPayListener;

/**
 * 忘记支付密码时身份证验证
 */
public class PayPasswordIdCardVerifyActivity extends BaseActivity implements
        OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    private EditText ledtSms;
    private TextViewCountDownView tcdvSms;
    /**
     * 姓名
     */
    private EditText edtName;
    /**
     * 身份证
     */
    private EditText edtIdCard;
    private Button btNext;
    //    private String userPhone;
    private LinearLayout ll_passwordCircle;
    private Dialog payDialog;
    private LinearLayout llCircleprogress;
    private CircleProgress circleProgress;
    private LinearLayout payKeyBoard;
    private LinearLayout payPasswordErrorProblem;
    private PayPasswordView payPasswordView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_id_card_verify);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        //姓名
        edtName = (EditText) findViewById(R.id.edt_name);
        //身份证
        edtIdCard = (EditText) findViewById(R.id.edt_id_card);

        //验证码
        ledtSms = (EditText) findViewById(R.id.ledt_sms);
        ledtSms.clearFocus();

        tcdvSms = (TextViewCountDownView) findViewById(R.id.tcdv_sms);

        btNext = (Button) findViewById(R.id.bt_next);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        //重新发送
        tcdvSms.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);

        btNext.setOnClickListener(this);


        edtName.addTextChangedListener(new ButtonEnabledListener(btNext, edtName, edtIdCard, ledtSms));
        edtIdCard.addTextChangedListener(new ButtonEnabledListener(btNext, edtName, edtIdCard, ledtSms));
        ledtSms.addTextChangedListener(new ButtonEnabledListener(btNext, edtName, edtIdCard, ledtSms));

    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:身份证验证
        tvTitleName.setText(R.string.id_card_verify_name);
    }

    @Override
    public void initData() {
        //关闭倒计时
        tcdvSms.cancelCountDown();

        boolean timerState = TimeUtil.getCountdownTimerState(this, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
        if (!timerState) {
            Long timeRemaining = TimeUtil.getTimeRemaining(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            tcdvSms.startCountDown(timeRemaining);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.tcdv_sms://重新发送短信
                ledtSms.setText("");
                sendRepeatSMSRequest(RefreshType.RefreshNoPull);
                break;
            case R.id.bt_next://下一步
                sendVerifySMSRequest(RefreshType.RefreshNoPull);
                break;
            default:
                break;
        }
    }


    /**
     * 支付密码密码输入对话框
     */
    private void showPayDialog() {
        //防止系统键盘是弹出状态，会覆盖支付键盘
        SystemUtil.closeKeybord(btNext, this);

        View view = getDecorViewDialog();
        ll_passwordCircle = (LinearLayout) view.findViewById(R.id.ll_passwordCircle);
        TextView tvPayType = (TextView) view.findViewById(R.id.tv_payType);
        TextView tv_paymentValue = (TextView) view.findViewById(R.id.tv_paymentValue);

        llCircleprogress = (LinearLayout) view.findViewById(R.id.ll_circleprogress);
        circleProgress = (CircleProgress) view.findViewById(R.id.circleprogress);
        payKeyBoard = (LinearLayout) view.findViewById(R.id.keyboard);
        payPasswordErrorProblem = (LinearLayout) view.findViewById(R.id.pay_error_problem_linear);
        payKeyBoard.setVisibility(View.VISIBLE);
        payPasswordErrorProblem.setVisibility(View.GONE);

        tvPayType.setVisibility(View.GONE);
        tv_paymentValue.setVisibility(View.GONE);

        TextView tv_title1 = (TextView) view.findViewById(R.id.tv_title1);

        if (TextUtils.isEmpty(oldPassword)) {
            tv_title1.setText("请输入新的支付密码");
        } else {
            tv_title1.setText("请再次输入新的支付密码");
        }

        if (payDialog == null || !payDialog.isShowing()) {
            payDialog = new PayDialog(this, view);
        }
        payDialog.show();
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation = AnimationUtils.loadAnimation(PayPasswordIdCardVerifyActivity.this, R.anim.anim_set_in_down_up);
        payKeyBoard.startAnimation(animation);
    }

    private String oldPassword;

    private View getDecorViewDialog() {
        // TODO Auto-generated method stub
        payPasswordView = PayPasswordView.getInstance(this, new OnPayListener() {

            @Override
            public void onSurePay(final String password) {
                // TODO Auto-generated method stub

                if (TextUtils.isEmpty(oldPassword)) {//第一次输入密码
                    payDialog.dismiss();
                    payDialog = null;
                    oldPassword = password;
                    showPayDialog();
                } else {//已第二次输入密码
                    if (password.equals(oldPassword)) {//第一次输入密码与第二次输入的密码相同
                        payKeyBoard.setVisibility(View.GONE);
                        llCircleprogress.setVisibility(View.VISIBLE);
                        /**
                         * 开始验证验证码的进度动画
                         */
                        payPasswordView.startProgressAnimator();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendSetPayPasswordRequest(password, RefreshType.RefreshNoPull);
                                oldPassword = null;
                            }
                        }, 1000);
                    } else {//第一次输入密码与第二次输入的密码不同
                        payDialog.dismiss();
                        payDialog = null;
                        DialogUtil.promptDialog(PayPasswordIdCardVerifyActivity.this, DialogUtil.HEAD_BAND_BANK, "两次输入密码不一致");
                        oldPassword = null;
                    }
                }
            }

            @Override
            public void onCancelPay() {//关闭支付对话框
                oldPassword = null;

                payDialog.dismiss();
                payDialog = null;
            }

            @Override
            public void onContent(String content) {
                String password = content.toString();
                int length = password.length();

                for (int i = 0; i < 6; i++) {
                    ll_passwordCircle.getChildAt(i).setVisibility(View.INVISIBLE);
                }

                for (int i = 0; i < length; i++) {
                    ll_passwordCircle.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }
        });
        return payPasswordView.getView();
    }

    /**
     * 发送设置支付密码网络请求
     */
    public void sendSetPayPasswordRequest(String password, RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_SETPAYMENTCODE);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("isforgetPassword", "1");
        params.addBodyParameter("new_paypassword", password);
        httpUtil.sendRequest(params, refreshType, this);
    }


    /**
     * 发送短信请求
     */
    public void sendRepeatSMSRequest(RefreshType refreshType) {
        String name = edtName.getText().toString();
        String idCard = edtIdCard.getText().toString();

//         name="韩梅梅";
//         idCard="210302196001012114";

        if (TextUtils.isEmpty(name)) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请填写姓名");
        }

        if (TextUtils.isEmpty(idCard)) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请填写身份证号");
        }

        RequestParams params = new RequestParams(Constant.SETTINGUP_FORGETPASSWORDOBTAINVERIFICATIONCODE);
        params.addBodyParameter("realName", name);
        params.addBodyParameter("identityCard", idCard);
        params.addBodyParameter("user_userunid", mUid);

        httpUtil.sendRequest(params, refreshType, this);
    }


    /**
     * 发送验证短信请求
     */
    public void sendVerifySMSRequest(RefreshType refreshType) {
        String sms = ledtSms.getText().toString();

        if (TextUtils.isEmpty(sms)) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请填写验证码");
            return;
        }

        RequestParams params = new RequestParams(Constant.USERINFO_CHECKVERIFYCODE);
        params.addBodyParameter("message_Code", sms);
        params.addBodyParameter("login_phone", mCurrentPhone);
        params.addBodyParameter("optType", "3");
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (url.equals(Constant.SETTINGUP_SETPAYMENTCODE)) {
            //停掉验证的所有动画状态设置回初始状态
            circleProgress.stop();
            llCircleprogress.setVisibility(View.GONE);
            payKeyBoard.setVisibility(View.GONE);
            payPasswordErrorProblem.setVisibility(View.VISIBLE);
            payPasswordView.clearPassword();
            ll_passwordCircle.setBackgroundResource(R.drawable.password_textfield_error);
            ll_passwordCircle.startAnimation(AnimationUtil.startShakeAnimation(PayPasswordIdCardVerifyActivity.this));
        } else {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.SETTINGUP_FORGETPASSWORDOBTAINVERIFICATIONCODE)) {
//            JSONObject jsonObject = JSON.pars  eObject(jsonBase.getDisposeResult());
//            userPhone = jsonObject.getString("userPhone");

            //存储倒计时的时间
            TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            Long timeRemaining = TimeUtil.getTimeRemaining(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            tcdvSms.startCountDown(timeRemaining);
        } else if (url.equals(Constant.USERINFO_CHECKVERIFYCODE)) {
//            Intent intent = new Intent(PayPasswordIdCardVerifyActivity.this,PayPasswordResetActivity.class);
//            startActivity(intent);
            showPayDialog();
        } else if (url.equals(Constant.SETTINGUP_SETPAYMENTCODE)) {
            payKeyBoard.setVisibility(View.GONE);
            /**
             * 结束验证验证码的进度动画
             */
            payPasswordView.endProgressAnimator(true);
            circleProgress.setAnimatorStatusListener(new CircleProgress.AddAnimatorEndListener() {
                @Override
                public void onAnimatorEnd(boolean animatorEnd) {
                    if (payDialog != null) {
                        payDialog.dismiss();
                        payDialog = null;
                        DialogUtil.promptDialog(PayPasswordIdCardVerifyActivity.this, "设置支付密码成功", new OnClickYesListener() {
                            @Override
                            public void onClickYes() {
                                Intent intent = new Intent(PayPasswordIdCardVerifyActivity.this, SetingActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });

        }
    }
}

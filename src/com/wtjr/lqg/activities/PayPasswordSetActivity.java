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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.PayPasswordType;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.AnimationUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.SystemUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CircleProgress;
import com.wtjr.lqg.widget.UsuallyLayoutItem;
import com.wtjr.lqg.widget.paykeyboard.PayDialog;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView.OnPayListener;

/**
 * 设置支付密码页面
 */
public class PayPasswordSetActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 忘记支付密码
     */
    private UsuallyLayoutItem uliForgetPas;
    /**
     * 修改支付密码
     */
    private UsuallyLayoutItem uliAmendPas;
    /**
     * 设置支付密码
     */
    private UsuallyLayoutItem uliPayPasswordSet;
    /**
     * 修改支付密码的布局
     */
    private LinearLayout llPayPasswordAlter;
    /**
     * 有没有设置过支付密码
     */
    private PayPasswordType mPasswordType;

    /**
     * 是设置密码
     */
    private boolean isSetPwd = true;
    private LinearLayout llCircleprogress;
    private CircleProgress circleProgress;
    private LinearLayout payKeyBoard;
    private LinearLayout payPasswordErrorProblem;
    private PayPasswordView payPasswordView;
    private TextView payPasswordError;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_set_pay_password);
        start();
    }

    @Override
    public void initIntent() {
        mPasswordType = (PayPasswordType) getIntent().getSerializableExtra(PayPasswordType.class.getName());
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        llPayPasswordAlter = (LinearLayout) findViewById(R.id.ll_pay_password_alter);

        uliForgetPas = (UsuallyLayoutItem) findViewById(R.id.uli_forget_pas);
        uliAmendPas = (UsuallyLayoutItem) findViewById(R.id.uli_amend_pas);
        uliPayPasswordSet = (UsuallyLayoutItem) findViewById(R.id.uli_pay_password_set);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 忘记支付密码
        uliForgetPas.setOnClickListener(this);
        // 修改支付密码
        uliAmendPas.setOnClickListener(this);
        //设置支付密码
        uliPayPasswordSet.setOnClickListener(this);
        //设置网络监听
        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:设置支付密码
        tvTitleName.setText(R.string.user_set_pay_password_name);
    }

    @Override
    public void initData() {
        //去除左侧的隐藏图片
        uliForgetPas.setImageContentrResourceL(0);
        uliAmendPas.setImageContentrResourceL(0);
        uliPayPasswordSet.setImageContentrResourceL(0);

        if (TextUtils.isEmpty(app.mAccountData.bankNum)) {//没绑卡
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "温馨提示", "您还未绑卡,请先去绑卡充值!", new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    finish();
                    Intent intent = new Intent(PayPasswordSetActivity.this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    startActivity(intent);
                }
            });
            return;
        }

        switch (mPasswordType) {
            case PasswordHaveSetPay://有设置过支付密码
                llPayPasswordAlter.setVisibility(View.VISIBLE);
                uliPayPasswordSet.setVisibility(View.GONE);
                break;
            case PayPasswordNotHaveSet://没有设置过支付密码

                break;

            case PayPasswordNotUnequivocal://不明确是否设置过支付密码
                sendIsSetPayPasswordRequest(RefreshType.RefreshNoPull);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.uli_forget_pas:// 忘记支付密码
                //判断是否重新获取验证码
//            boolean countdownTimerState = TimeUtil.getCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
//            if (countdownTimerState) {
//                sendSMSRequest(RefreshType.RefreshNoPull);
//            }else{
//                startActivity(new Intent(this,PayPasswordIdCardVerifyActivity.class));
//            }

                startActivity(new Intent(this, PayPasswordIdCardVerifyActivity.class));
                break;
            case R.id.uli_amend_pas:// 修改支付密码
//		    sendAlterPayPasswordRequest();
                isSetPwd = false;
                showPayDialog(1);
                break;
            case R.id.uli_pay_password_set:// 设置支付密码
//            sendSetPayPasswordRequest();
                isSetPwd = true;
                showPayDialog(0);
                break;

            default:
                break;
        }
    }

    private PayDialog payDialog;
    private LinearLayout ll_passwordCircle;
    /**
     * 第一次输入密码的内容
     */
    private String firstSetPassword;
    private String oldPassword;

    /**
     * 支付密码密码输入对话框
     *
     * @param mode 0-->设置密码,1-->修改密码
     */
    private void showPayDialog(int mode) {
        //防止系统键盘是弹出状态，会覆盖支付键盘
        SystemUtil.closeKeybord(uliPayPasswordSet, this);

        View view = getDecorViewDialog(mode);
        ll_passwordCircle = (LinearLayout) view.findViewById(R.id.ll_passwordCircle);
        payPasswordError = (TextView) view.findViewById(R.id.pay_password_error_text);
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

        if (isSetPwd) {
            if (TextUtils.isEmpty(firstSetPassword)) {
                tv_title1.setText("请输入支付密码");
            } else {
                tv_title1.setText("请再次输入支付密码");
            }
        } else {
            if (TextUtils.isEmpty(firstSetPassword)) {
                tv_title1.setText("请输入新的支付密码");
            } else {
                tv_title1.setText("请再次输入新的支付密码");
            }
        }

        if (mode == 1) {
            tv_title1.setText("请输入旧的支付密码");
        }

        if (payDialog == null || !payDialog.isShowing()) {
            payDialog = new PayDialog(PayPasswordSetActivity.this, view);
        }
        payDialog.show();
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation = AnimationUtils.loadAnimation(PayPasswordSetActivity.this, R.anim.anim_set_in_down_up);
        payKeyBoard.startAnimation(animation);
    }


    private View getDecorViewDialog(final int mode) {
        // TODO Auto-generated method stub
        payPasswordView = PayPasswordView.getInstance(this, new OnPayListener() {
            @Override
            public void onSurePay(final String password) {
                // TODO Auto-generated method stub

                if (mode == 0) {//设置密码
                    if (TextUtils.isEmpty(firstSetPassword)) {//第一次输入密码
                        payDialog.dismiss();
                        payDialog = null;
                        firstSetPassword = password;

                        if (!TextUtils.isEmpty(oldPassword)) {//有保存旧密码,证明是要修改密码

                            if (oldPassword.equals(password)) {//新旧密码一致
                                firstSetPassword = null;
                                DialogUtil.promptDialog(PayPasswordSetActivity.this, DialogUtil.HEAD_BAND_BANK, "", "新密码与旧密码一致", new OnClickYesListener() {
                                    @Override
                                    public void onClickYes() {
                                        // 发送代码+进度展示-->填写验证码
                                        showPayDialog(0);
                                    }
                                });
                            } else {
                                showPayDialog(0);
//                                sendAlterPayPasswordRequest(password,RefreshType.RefreshNoPull);
                            }
                        } else {
                            showPayDialog(0);
                        }

                    } else {//已第二次输入密码
                        if (password.equals(firstSetPassword)) {//第一次输入密码与第二次输入的密码相同

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
                                    firstSetPassword = null;
                                }
                            }, 1000);

                        } else {//第一次输入密码与第二次输入的密码不同
                            payDialog.dismiss();
                            payDialog = null;
                            DialogUtil.promptDialog(PayPasswordSetActivity.this, DialogUtil.HEAD_BAND_BANK, "两次输入密码不一致");
                            firstSetPassword = null;
                        }
                    }
                } else if (mode == 1) {//修改密码
                    oldPassword = password;
                    payKeyBoard.setVisibility(View.GONE);
                    llCircleprogress.setVisibility(View.VISIBLE);
                    /**
                     * 开始验证验证码的进度动画
                     */
                    payPasswordView.startProgressAnimator();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendOldPayPasswordVerificationRequest(oldPassword, RefreshType.RefreshNoPull);
                        }
                    }, 1000);

                }
            }

            @Override
            public void onCancelPay() {//关闭支付对话框
                firstSetPassword = null;

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
     * 判断是否设置过支付密码
     */
    public void sendIsSetPayPasswordRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }


    /**
     * 发送设置支付密码网络请求
     */
    public void sendSetPayPasswordRequest(String password, RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_SETPAYMENTCODE);
        params.addBodyParameter("user_userunid", mUid);
        //登录密码
        params.addBodyParameter("login_password", "123456a");//没用的参数
        //旧支付密码
        params.addBodyParameter("paypassword", oldPassword);
        //新支付密码
        params.addBodyParameter("new_paypassword", password);
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 判断旧的支付密码是否正确
     */
    public void sendOldPayPasswordVerificationRequest(String password, RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_VERIFICATIONUSEDPAYMENTCODE);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("code_type", "2");
        //旧支付密码
        params.addBodyParameter("payPassword", oldPassword);
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 忘记支付密码发送短信请求
     */
//    public void sendSMSRequest(RefreshType refreshType) {
//         RequestParams params = new RequestParams(Constant.USERINFO_SENDVERIFYCODE);
//         params.addBodyParameter("login_phone", mCurrentPhone);
//         params.addBodyParameter("optType", "2");
//         httpUtil.sendRequest(params,refreshType,this);
//     }
    @Override
    public void onFailure(String url, String errorContent) {
        if (url.equals(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE)) {//判断是否设置过支付密码
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent, new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    finish();
                }
            });
        } else if (url.equals(Constant.SETTINGUP_VERIFICATIONUSEDPAYMENTCODE)) {
            circleProgress.stop();
            if (payDialog != null) {
                payDialog.dismiss();
                payDialog = null;
            }
            if (errorContent.contains("锁定")) {
                DialogUtil.selectDialog(PayPasswordSetActivity.this, DialogUtil.HEAD_LOGIN, "", errorContent, "找回密码", new DialogUtil.OnClickYesListener() {

                    @Override
                    public void onClickYes() {
                        startActivity(new Intent(PayPasswordSetActivity.this, PayBackPasswordActivity.class));
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
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE)) {//判断是否设置过支付密码
            JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            //true有设置过支付密码，false没有设置过支付密码
            Boolean isPayPassword = jsonObject.getBoolean("payPasswordisExist");

            if (isPayPassword) {
                llPayPasswordAlter.setVisibility(View.VISIBLE);
                uliPayPasswordSet.setVisibility(View.GONE);
            }
        } else if (url.equals(Constant.USERINFO_SENDVERIFYCODE)) {//忘记支付密码发送短信请求
            //存储倒计时的时间
            TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            startActivity(new Intent(this, PayPasswordIdCardVerifyActivity.class));
        } else if (url.equals(Constant.SETTINGUP_SETPAYMENTCODE)) {//设置支付密码成功
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
                        llPayPasswordAlter.setVisibility(View.VISIBLE);
                        uliPayPasswordSet.setVisibility(View.GONE);
                    } else {
                        llPayPasswordAlter.setVisibility(View.VISIBLE);
                        uliPayPasswordSet.setVisibility(View.GONE);
                    }
                }
            });
        } else if (url.equals(Constant.SETTINGUP_VERIFICATIONUSEDPAYMENTCODE)) {//旧的支付密码验证
            JSONObject parseObject = JSON.parseObject(jsonBase.getDisposeResult());
            String payPasswordisRight = parseObject.getString("payPasswordisRight");
            String strPayPasswordError = parseObject.getString("pwdErrMsg");
            if (payDialog != null) {
                if (payPasswordisRight.equals("1") || payPasswordisRight.equals("true")) {
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
                                showPayDialog(0);
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
                    ll_passwordCircle.setBackgroundResource(R.drawable.password_textfield_error);
                    ll_passwordCircle.startAnimation(AnimationUtil.startShakeAnimation(PayPasswordSetActivity.this));
                } else if(payPasswordisRight.equals("3")){
                    circleProgress.stop();
                    if (payDialog != null) {
                        payDialog.dismiss();
                        payDialog = null;
                    }
                    DialogUtil.selectDialog(PayPasswordSetActivity.this, DialogUtil.HEAD_LOGIN, "", strPayPasswordError, "找回密码", new DialogUtil.OnClickYesListener() {

                        @Override
                        public void onClickYes() {
                            startActivity(new Intent(PayPasswordSetActivity.this, PayBackPasswordActivity.class));
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
}
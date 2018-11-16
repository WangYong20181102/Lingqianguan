package com.wtjr.lqg.activities;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.CubeLeftOutAnimation;
import com.wtjr.lqg.utils.CubeRightInAnimation;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.LoginOrExitUtils;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CheckBoxShowOrHide;
import com.wtjr.lqg.widget.LadderView;
import com.wtjr.lqg.widget.LineEditTextInvitationCode;
import com.wtjr.lqg.widget.LineEditTextPhone;
import com.wtjr.lqg.widget.RegistreLinerLayout;
import com.wtjr.lqg.widget.TextViewCountDownView;

import cn.jpush.android.api.JPushInterface;

/**
 * 注册页面
 */
public class RegisterActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
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
     * 注册进度
     */
    private LadderView lvProgress;

    /**
     * X返回上一步
     */
    private ImageButton imgBtnNext;

    /**
     * 当前位置
     */
    private int mCurrLocation = 1;

    /**
     * 输入手机号页面
     */
    private RegistreLinerLayout llRegisterPhone;

    /**
     * 第一步输入框输入账号
     */
    private EditText ledtInputPhone;

    /**
     * 第一步显示电话号码的
     */
    private TextView tvShowPhone;

    /**
     * 第一步的零钱罐服务协议
     */
    private TextView tvAgreement;
    /**
     * 第二步的零钱罐服务协议
     */
    private TextView tvSmsAgreement;

    /**
     * 输入手机号，下一步按钮
     */
    private Button btnPhoneNext;

    /**
     * 短信验证码页面
     */
    private RegistreLinerLayout llRegisterSms;

    /**
     * 第二层的短信输入框
     */
    private EditText ledtSms;

    /**
     * 短信验证码下一步按钮
     */
    private Button btnSmsNext;

    /**
     * 输入密码页面
     */
    private RegistreLinerLayout llRegisterPas;

    /**
     * 第三步输入密码
     */
    private EditText ledtInputPas;

    /**
     * 设置密码完成下一步按钮
     */
    private Button btnPasswordOk;

    /**
     * 复选框
     */
    private CheckBoxShowOrHide cbShowHide;

    /**
     * 动画时间
     */
    private long mDurationMillis = 800;

    /**
     * 手机号
     */
    private String mPhone;

    /**
     * 邀请码
     */
    private String mInvatation;

    /**
     * 短信验证码
     */
    private String mSMS;

    /**
     * 密码
     */
    private String mPwd;

    /**
     * 短信验证码倒计时
     */
    private TextViewCountDownView tcdvSms;

    /**
     * 第一步的时候发送获取短信验证码接口
     */
    private boolean isFristSendSms;

    private LineEditTextPhone lineEditTextPhone;

    private EditText ledtInputInvatation;

    private LineEditTextInvitationCode lineEditTextInvatation;
    /**
     * title下面的线
     */
    private View titlebottomline;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_register);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        titlebottomline = (View) findViewById(R.id.title_bottom_line);
        titlebottomline.setVisibility(View.GONE);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);
        lvProgress = (LadderView) findViewById(R.id.lv_progress);

        lineEditTextPhone = (LineEditTextPhone) findViewById(R.id.lineEditTextPhone);
        lineEditTextInvatation = (LineEditTextInvitationCode) findViewById(R.id.lineEditTextInvatation);

        findRegisterPhone();
        findRegisterSms();
        findRegisterPas();
    }

    /**
     * 注册输入手机号码
     */
    private void findRegisterPhone() {
        llRegisterPhone = (RegistreLinerLayout) findViewById(R.id.ll_register_phone);
        ledtInputPhone = (EditText) findViewById(R.id.ledt_input_phone);
        btnPhoneNext = (Button) findViewById(R.id.btn_phone_next);
        tvShowPhone = (TextView) findViewById(R.id.tv_show_phone);
        ledtInputInvatation = (EditText) llRegisterPhone.findViewById(R.id.ledt_input_invatation_code);
        tvAgreement = (TextView) findViewById(R.id.tv_agreement);
        tvSmsAgreement = (TextView) findViewById(R.id.tv_sms_agreement);
    }

    /**
     * 注册输入短信验证码
     */
    private void findRegisterSms() {
        llRegisterSms = (RegistreLinerLayout) findViewById(R.id.ll_register_sms);
        btnSmsNext = (Button) findViewById(R.id.btn_sms_next);
        ledtSms = (EditText) findViewById(R.id.ledt_sms);
        tcdvSms = (TextViewCountDownView) findViewById(R.id.tcdv_sms);

    }

    /**
     * 注册输入密码
     */
    private void findRegisterPas() {
        llRegisterPas = (RegistreLinerLayout) findViewById(R.id.ll_register_pas);
        btnPasswordOk = (Button) findViewById(R.id.btn_password_ok);
        ledtInputPas = (EditText) findViewById(R.id.ledt_input_pas);
        cbShowHide = (CheckBoxShowOrHide) findViewById(R.id.cb_show_hide);
        // ledtInputPas.setCheckBox(cbShowHide);
        // cbShowHide.setEditText(ledtInputPas);
    }

    @Override
    public void setListener() {
        // 设置网络监听
        httpUtil.setHttpRequesListener(this);

        imgBtnBack.setOnClickListener(this);
        imgBtnNext.setOnClickListener(this);

        btnSmsNext.setOnClickListener(this);
        btnPhoneNext.setOnClickListener(this);
        btnPasswordOk.setOnClickListener(this);
        // 重新获取
        tcdvSms.setOnClickListener(this);
        // 零钱罐服务协议
        tvAgreement.setOnClickListener(this);
        tvSmsAgreement.setOnClickListener(this);

        ledtInputPhone.addTextChangedListener(new ButtonEnabledListener(btnPhoneNext, ledtInputPhone));
//        ledtInputInvatation.addTextChangedListener(new ButtonEnabledListener(btnPhoneNext, ledtInputInvatation));
        ledtSms.addTextChangedListener(new ButtonEnabledListener(btnSmsNext, ledtSms));
        ledtInputPas.addTextChangedListener(new ButtonEnabledListener(btnPasswordOk, ledtInputPas));
    }

    @Override
    public void setTitle() {
        // 返回
        imgBtnBack.setVisibility(View.VISIBLE);
        imgBtnBack.setImageResource(R.drawable.back2);
        // 设置名字为:注册
        tvTitleName.setText(R.string.register_name);
        // 返回
//        imgBtnNext.setVisibility(View.VISIBLE);
//        imgBtnNext.setImageResource(R.drawable.close);
    }

    @Override
    public void initData() {
        // 时间拦截防止继续传递
        isIntercept(1);
        // 文本框随输入框内容变化而变化
        lineEditTextPhone.setTextView(tvShowPhone);
        lineEditTextInvatation.setTextView(tvShowPhone);
        // 输入框获得焦点
        getFocus(ledtInputPhone);
    }

    /**
     * 第一步，发送网络请求前先判断
     */
    private void sendSMS() {
        isFristSendSms = true;

        // 不为null
        if (TextUtils.isEmpty(mPhone)) {
            setPromptDialog(getString(R.string.phone_null));
            return;
        }
        // 是手机号码
        if (!StringUtil.isPhone(mPhone)) {
            setPromptDialog(getString(R.string.phone_error));
            return;
        }

        // 获得注册倒计时，返回
        if (TimeUtil.getCountdownTimerState(RegisterActivity.this, Constant.DOWN_TIMER_REGISTER, mPhone)) {
            sendSMSRequest(RefreshType.RefreshNoPull);
        } else {
            nextOrBack(true);
        }
    }

    /**
     * 第一步，发送短信请求
     */
    public void sendSMSRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.USERINFO_SENDVERIFYCODEREGISTER);
        params.addBodyParameter("login_phone", mPhone);
        params.addBodyParameter("invitation_flag", mInvatation);
        params.addBodyParameter("optType", "1");
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 第二步，发送短信验证请求
     */
    public void sendSMSVerifyRequest(RefreshType refreshType) {
        mSMS = ledtSms.getText().toString();
        if (TextUtils.isEmpty(mSMS) || mSMS.length() < 4) {
            setPromptDialog("验证码长度不够");
            return;
        }
        RequestParams params = new RequestParams(Constant.USERINFO_CHECKVERIFYCODE);
        params.addBodyParameter("login_phone", mPhone);
        params.addBodyParameter("message_Code", mSMS);
        params.addBodyParameter("optType", "1");
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 第二步,发送注册请求
     */
    public void sendRegisterRequest(RefreshType refreshType) {
        mPwd = ledtInputPas.getText().toString();
        if (TextUtils.isEmpty(mPwd)) {
            setPromptDialog("请输入密码");
            return;
        }

        if (!StringUtil.isPasswordRegist(mPwd)) {
            setPromptDialog(getString(R.string.password_format_error));
            return;
        }
        RequestParams params = new RequestParams(Constant.USERINFO_REGISTER);
        params.addBodyParameter("login_phone", mPhone);
        params.addBodyParameter("message_Code", mSMS);
        params.addBodyParameter("login_password", mPwd);
        params.addBodyParameter("regId", JPushInterface.getRegistrationID(this));
        params.addBodyParameter("invitation_flag", mInvatation);
        params.addBodyParameter("reg_way", Constant.CHANNEL);
        params.addBodyParameter("optType", "1");
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 设置提示对话框更改图形验证码
     */
    private void setPromptDialog(String str1) {
        DialogUtil.promptDialog(RegisterActivity.this, DialogUtil.HEAD_REGISTER, str1, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                nextOrBack(false);
                break;
//            case R.id.imgBtn_next:// X返回上一步操作
//                nextOrBack(false);
//                break;
            case R.id.btn_phone_next:// 1下一步
                mPhone = ledtInputPhone.getText().toString();
                mInvatation = ledtInputInvatation.getText().toString();
                if (TimeUtil.getCountdownTimerState(RegisterActivity.this, Constant.DOWN_TIMER_REGISTER, mPhone)) {
                    sendSMS();
                } else {
                    Long timeRemaining = TimeUtil.getTimeRemaining(this, Constant.DOWN_TIMER_REGISTER, mPhone);
                    tcdvSms.cancelCountDown();
                    // 倒计时时间
                    tcdvSms.startCountDown(timeRemaining);

                    nextOrBack(true);
                }
                break;
            case R.id.btn_sms_next:// 2下一步
                sendSMSVerifyRequest(RefreshType.RefreshNoPull);
                break;
            case R.id.btn_password_ok:// 3完成
                // ledtInputInvatation = (EditText)
                // llRegisterPas.findViewById(R.id.ledt_input_invatation_code);
                // mInvatation = ledtInputInvatation.getText().toString();
                // Log.d("content1", mInvatation+"-------完成--------");
                sendRegisterRequest(RefreshType.RefreshNoPull);
                break;
            case R.id.tcdv_sms:// 重新获取
                isFristSendSms = false;
                ledtSms.setText("");
                // 重新发送不需要判断了
                sendSMSRequest(RefreshType.RefreshNoPull);
                break;
            case R.id.tv_agreement:// 零钱罐服务协议
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_LQG_SERVE_AGREEMENT);
                intent.putExtra("TitleName", "零钱罐服务协议");
                startActivity(intent);
                break;
            case R.id.tv_sms_agreement:// 零钱罐服务协议
                Intent intent1 = new Intent(this, WebViewActivity.class);
                intent1.putExtra("url", Constant.H5_LQG_SERVE_AGREEMENT);
                intent1.putExtra("TitleName", "零钱罐服务协议");
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void goActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginOrExitUtils.loginSuccess(RegisterActivity.this, app, LoginOrExitUtils.STATE_LOGIN, mUId, mPhone);
                finish();
            }
        }, 1000);
    }

    /**
     * 物理返回按钮
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            nextOrBack(false);
        }
        return false;
    }

    /**
     * 记录第一次点击的时间
     */
    private long clickTime = 0;

    private String mUId;

    /**
     * 下一步或者返回
     */
    private void nextOrBack(boolean isNext) {
        // 防止点击频率过高
        if ((System.currentTimeMillis() - clickTime) > mDurationMillis + 200) {
            if (isNext) {
                // 表示下一步
                mCurrLocation++;

                // 大于4表示等于4
                if (mCurrLocation > 3) {
                    mCurrLocation = 3;
                }

                nextAnimation(mCurrLocation);
            } else {
                // 返回
                mCurrLocation--;
                // 小于1结束
                if (mCurrLocation < 1) {
                    finish();
                } else {
                    backAnimation(mCurrLocation);
                }

            }
            changeTitle(mCurrLocation);
            ledtInputPas.setText("");
            clickTime = System.currentTimeMillis();
        }
    }

    /**
     * 改变标题内容
     */
    private void changeTitle(int location) {
        // 设置进度
        lvProgress.setProgress(location);
        // 对事件进行拦截
        isIntercept(location);
        switch (location) {
            case 1:
                // 设置不同返回图标
//                imgBtnBack.setImageResource(R.drawable.close);
                tvTitleName.setText("注册");
                getFocus(ledtInputPhone);
                ledtSms.setFocusable(false);
                ledtInputPas.setFocusable(false);
                break;
            case 2:
                // 设置不同返回图标
//                imgBtnBack.setImageResource(R.drawable.naving_back_state);
                tvTitleName.setText("短信验证码");
                getFocus(ledtSms);
                ledtInputPhone.setFocusable(false);
                ledtInputPas.setFocusable(false);
                break;
            case 3:
                // 设置不同返回图标
//                imgBtnBack.setImageResource(R.drawable.naving_back_state);
                tvTitleName.setText("设置密码");
                getFocus(ledtInputPas);
                ledtSms.setFocusable(false);
                ledtInputPhone.setFocusable(false);
                break;
            default:
                break;
        }
    }

    /**
     * 让输入框获得焦点
     *
     * @param view
     */
    private void getFocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

    /**
     * 下一步动画
     *
     * @param location
     */
    private void nextAnimation(final int location) {
        CubeLeftOutAnimation cubeLeftOutAnimation = new CubeLeftOutAnimation(true);
        cubeLeftOutAnimation.setDuration(mDurationMillis);
        cubeLeftOutAnimation.setFillAfter(true);

        CubeRightInAnimation cubeRightInAnimation = new CubeRightInAnimation(true);
        cubeRightInAnimation.setDuration(mDurationMillis);
        cubeRightInAnimation.setFillAfter(true);
        if (location == 2) {
            llRegisterPhone.startAnimation(cubeRightInAnimation);
            llRegisterSms.startAnimation(cubeLeftOutAnimation);
        } else if (location == 3) {
            llRegisterPas.setVisibility(View.VISIBLE);
            llRegisterSms.startAnimation(cubeRightInAnimation);
            llRegisterPas.startAnimation(cubeLeftOutAnimation);
        }
    }

    /**
     * 返回动画
     *
     * @param location
     */
    private void backAnimation(final int location) {
        CubeLeftOutAnimation cubeLeftOutAnimation2 = new CubeLeftOutAnimation(false);
        cubeLeftOutAnimation2.setDuration(mDurationMillis);
        cubeLeftOutAnimation2.setFillAfter(true);

        CubeRightInAnimation cubeRightInAnimation2 = new CubeRightInAnimation(false);
        cubeRightInAnimation2.setDuration(mDurationMillis);
        cubeRightInAnimation2.setFillAfter(true);
        if (location == 2) {
            llRegisterSms.startAnimation(cubeRightInAnimation2);
            llRegisterPas.startAnimation(cubeLeftOutAnimation2);
        } else if (location == 1) {
            llRegisterPhone.startAnimation(cubeRightInAnimation2);
            llRegisterSms.startAnimation(cubeLeftOutAnimation2);
        }
    }

    /**
     * 是否拦截
     */
    private void isIntercept(int location) {
        switch (location) {
            case 1:
                llRegisterPhone.setIntercept(false);
                llRegisterSms.setIntercept(true);
                llRegisterPas.setIntercept(true);
                break;
            case 2:
                llRegisterPhone.setIntercept(true);
                llRegisterSms.setIntercept(false);
                llRegisterPas.setIntercept(true);
                break;
            case 3:
                llRegisterPhone.setIntercept(true);
                llRegisterSms.setIntercept(true);
                llRegisterPas.setIntercept(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.USERINFO_SENDVERIFYCODEREGISTER)) {
            // 设置倒计时时间
            TimeUtil.setCountdownTimerState(RegisterActivity.this, Constant.DOWN_TIMER_REGISTER, mPhone);
            // 启动计时前先取消
            tcdvSms.cancelCountDown();
            // 倒计时时间
            tcdvSms.startCountDown(mTimeRemaining);
            // 如果是重新获取短信验证码就不需要下一步，反之需要
            if (isFristSendSms) {
                nextOrBack(true);
            }
        } else if (url.equals(Constant.USERINFO_CHECKVERIFYCODE)) {
            nextOrBack(true);
            // 验证码验证成功取消计时器
            // tcdvSms.cancelCountDown();
            // 成功清空输入框内容
            ledtInputPhone.setText("");
            ledtInputInvatation.setText("");
            ledtSms.setText("");
        } else if (url.equals(Constant.USERINFO_REGISTER)) {
            lvProgress.setProgress(4);
            // 成功清空输入框内容
            ledtInputPas.setText("");

            try {
                JSONObject jsonObject = new JSONObject(jsonBase.getDisposeResult());
                mUId = jsonObject.getString("uId");
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            goActivity();

            SharedPreferencesUtil.setPrefBoolean(this, "NoviceGuide2", false);
        }
    }
}
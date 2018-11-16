package com.wtjr.lqg.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.widget.LineEditTextPhoneNumber;
import com.wtjr.lqg.widget.TextViewCountDownView;

import org.xutils.http.RequestParams;


/**
 * 更换银行预留手机号
 * Created by WangYong on 2017/2/10.
 */

public class ChangeBankObligatePhoneNumActivity extends BaseActivity implements View.OnClickListener, HttpUtil.HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 旧手机号
     */
    private EditText oldPhone;
    private LineEditTextPhoneNumber lineEditTextPhoneNumber;

    /**
     * 新手机号
     */
    private EditText newPhone;
    private LineEditTextPhoneNumber lineEditTextNewPhoneNumber;

    /**
     * 短信验证码
     */
    private EditText ledtEms;

    /**
     * 确认
     */
    private Button btnSure;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 获取短信
     */
    private TextViewCountDownView tcdvSms;
    /**
     * 支付密码
     */
    private String payPassword;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_change_bank_obligate_phone_num);
        start();
    }

    @Override
    public void initIntent() {
        payPassword = getIntent().getStringExtra("password");
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        ledtEms = (EditText) findViewById(R.id.ledt_sms);
        btnSure = (Button) findViewById(R.id.bt_next);
        tcdvSms = (TextViewCountDownView) findViewById(R.id.tcdv_sms);
        lineEditTextPhoneNumber = (LineEditTextPhoneNumber) findViewById(R.id.line_edit_text_phone);
        lineEditTextNewPhoneNumber = (LineEditTextPhoneNumber) findViewById(R.id.line_edit_text_new_phone);
        oldPhone = (EditText) lineEditTextPhoneNumber.findViewById(R.id.ledt_input_phone);
        newPhone = (EditText) lineEditTextNewPhoneNumber.findViewById(R.id.ledt_input_phone);

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
    public void setTitle() {
        // 一开始的时候是隐藏的
        imgBtnBack.setVisibility(View.VISIBLE);
        tvTitleName.setText("更换银行预留手机号");   //标题
    }

    @Override
    public void setListener() {
        btnSure.setOnClickListener(this);
        imgBtnBack.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
        //重新发送
        tcdvSms.setOnClickListener(this);
        oldPhone.addTextChangedListener(new ButtonEnabledListener(btnSure, oldPhone, newPhone, ledtEms));
        newPhone.addTextChangedListener(new ButtonEnabledListener(btnSure, oldPhone, newPhone, ledtEms));
        ledtEms.addTextChangedListener(new ButtonEnabledListener(btnSure, oldPhone, newPhone, ledtEms));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imgBtn_back:  //返回
                finish();
                break;
            case R.id.bt_next:  //确认
                sendChangePayPhone(RefreshType.RefreshNoPull);
                break;
            case R.id.tcdv_sms://重新发送短信
                ledtEms.setText("");
                sendRepeatSMSRequest(RefreshType.RefreshNoPull);
                break;
            default:

                break;
        }
    }

    /**
     * 发送短信请求
     * @param refreshType
     */
    public void sendRepeatSMSRequest(RefreshType refreshType) {
        String  strOldPhone = oldPhone.getText().toString();
        String strNewPhone = newPhone.getText().toString();

        if(TextUtils.isEmpty(strOldPhone) || TextUtils.isEmpty(strNewPhone)) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, "【原手机号码、新手机号码、短信验证码】\n\n输入错误，请确认后重新输入。");
            return;
        }

        RequestParams params = new RequestParams(Constant.PAY_SENDCHANGEBANKPHONECODE);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("reserved_phone",strOldPhone);
        params.addBodyParameter("bank_phone", strNewPhone);

        httpUtil.sendRequest(params,refreshType,this);
    }
    /**
     * 更改手机号
     * @param refreshType
     */
    public void sendChangePayPhone(RefreshType refreshType) {
        String  strOldPhone = oldPhone.getText().toString();
        String strNewPhone = newPhone.getText().toString();
        String strMsgCode = ledtEms.getText().toString();

        RequestParams params = new RequestParams(Constant.PAY_SPAYCHANGEPAYPHONE);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("reserved_phone",strOldPhone);
        params.addBodyParameter("bank_phone", strNewPhone);
        params.addBodyParameter("pay_pwd", payPassword);
        params.addBodyParameter("msg_code", strMsgCode);
        httpUtil.sendRequest(params,refreshType,this);
    }
    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }
    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if(url.equals(Constant.PAY_SENDCHANGEBANKPHONECODE)) {

            //存储倒计时的时间
            TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            Long timeRemaining = TimeUtil.getTimeRemaining(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            tcdvSms.startCountDown(timeRemaining);
        }else if (url.equals(Constant.PAY_SPAYCHANGEPAYPHONE)){
            DialogUtil.promptDialog(ChangeBankObligatePhoneNumActivity.this, "更换成功", new DialogUtil.OnClickYesListener() {
                @Override
                public void onClickYes() {
                    finish();
                }
            });
        }
    }
}

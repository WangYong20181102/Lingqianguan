package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;

/**
 * 更换手机号
 *
 * @author Myf
 */
public class ChangePhoneNewNumberActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;

    private TextView tView1;

    private TextView tView2;

    /**
     * 新手机号码
     */
    private EditText etNewPhone;
    /**
     * 提交新号码按钮
     */
    private Button btnSubmit;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;

    private String mNewPhone;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_change_phone_number_code);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tView1 = (TextView) findViewById(R.id.textView1);
        tView2 = (TextView) findViewById(R.id.textView2);
        etNewPhone = (EditText) findViewById(R.id.edt_new_phone);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 提交新号码按钮
        btnSubmit.setOnClickListener(this);

        httpUtil.setHttpRequesListener(this);
        etNewPhone.addTextChangedListener(new ButtonEnabledListener(btnSubmit, etNewPhone));

    }

    @Override
    public void initIntent() {
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
//        imgBtnBack.setImageResource(R.drawable.back2);
        // 设置名字为:更换手机号码
        tvTitleName.setText(R.string.change_phone_number_code_name);
//        tvTitleName.setTextColor(Color.BLACK);
    }

    @Override
    public void initData() {
        tView1.setText(StringUtil.setBlurryPhone(mCurrentPhone));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.btn_submit:
                mNewPhone = etNewPhone.getText().toString();
                if (TextUtils.isEmpty(mNewPhone)) {
                    DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "手机号不能为空");
                    return;
                }

                if (!StringUtil.isPhone(mNewPhone)) {
                    DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "手机号不正确");
                    return;
                }

                // 判断是否重新获取验证码
                boolean countdownTimerState = TimeUtil.getCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
                if (countdownTimerState) {
                    sendSMSRequest(RefreshType.RefreshNoPull);
                } else {
                    goActivity();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 发送短信请求
     */
    public void sendSMSRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.USERINFO_SENDVERIFYCODE);
        params.addBodyParameter("login_phone", mNewPhone);
        params.addBodyParameter("optType", "5");
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 设置提示对话框
     */
    private void setPromptDialog(String str1) {
        DialogUtil.promptDialog(ChangePhoneNewNumberActivity.this, DialogUtil.HEAD_REGISTER, str1, null);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        setPromptDialog(errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        // 存储倒计时的时间
        TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
        goActivity();
    }

    @Override
    public void goActivity() {
        Intent intent = new Intent(this, ChangePhoneSMSVerifyActivity.class);
        intent.putExtra("NewPhone", mNewPhone);
        startActivity(intent);
    }
}

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
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SaveFirstInState;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.LoginOrExitUtils;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CircleImageView;

import cn.jpush.android.api.JPushInterface;

/**
 * 登录页面
 *
 * @author Myf
 */
public class LoginActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;

    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 右边X返回的图片按钮
     */
    private ImageButton imgBtnNext;

    /**
     * 头像
     */
    private CircleImageView civHead;

    /**
     * 输入账号
     */
    private EditText ledtInputPhone;

    /**
     * 输入密码
     */
    private EditText ledtInputPas;

    /**
     * 复选按钮
     */
    // private CheckBoxShowOrHide cbShowHide;
    /**
     * 登录按钮
     */
    private Button btnLogin;

    /**
     * 忘记密码
     */
    private TextView tvForgetPassword;

    /**
     * 下一步
     */
    private TextView tvNext;

    /**
     * 用户的userId
     */
    private String userUId;

    /**
     * 手机号，账号
     */
    private String mPhone;

    /**
     * 切换环境
     */
    private TextView tvChangeRequestAddress;
    /**
     * 注册
     */
    private TextView tvRegister;
    private View tittleBottomLine;
    /**
     * 是否是第一次的状态(没登录过就是第一次true,登录过就是一直是false)
     */
    public boolean mFirstInState = true;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        mFirstInState = SaveFirstInState.getFirstInState(app);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);
        tvNext = (TextView) findViewById(R.id.tv_next);
        tittleBottomLine = findViewById(R.id.title_bottom_line);
        tvRegister = (TextView) findViewById(R.id.tv_register);
        civHead = (CircleImageView) findViewById(R.id.civ_head);
        ledtInputPhone = (EditText) findViewById(R.id.ledt_input_phone);
        ledtInputPas = (EditText) findViewById(R.id.ledt_input_pas);
        // cbShowHide = (CheckBoxShowOrHide) findViewById(R.id.cb_show_hide);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);

        tvChangeRequestAddress = (TextView) findViewById(R.id.tv_changeRequestAddress);
    }

    @Override
    public void setListener() {
        // 网络请求
        httpUtil.setHttpRequesListener(this);
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 返回按钮监听器
        imgBtnNext.setOnClickListener(this);

        tvNext.setOnClickListener(this);
        // 注册
        tvRegister.setOnClickListener(this);
        // 登录按钮
        btnLogin.setOnClickListener(this);
        // 忘记密码
        tvForgetPassword.setOnClickListener(this);

        tvChangeRequestAddress.setOnClickListener(this);

        ledtInputPhone.addTextChangedListener(new ButtonEnabledListener(btnLogin, ledtInputPhone, ledtInputPas));
        ledtInputPas.addTextChangedListener(new ButtonEnabledListener(btnLogin, ledtInputPhone, ledtInputPas));
    }

    @Override
    public void setTitle() {

        if (mFirstInState){
            // 返回
            imgBtnBack.setVisibility(View.VISIBLE);
            imgBtnBack.setImageResource(R.drawable.close);
        }else{
            imgBtnBack.setVisibility(View.VISIBLE);
            imgBtnBack.setImageResource(R.drawable.back2);
        }

//        imgBtnNext.setVisibility(View.VISIBLE);
//        imgBtnNext.setImageResource(R.drawable.close);
        tittleBottomLine.setVisibility(View.GONE);
        // 设置名字为:登录
//        tvTitleName.setText(R.string.login_name);
        tvTitleName.setText("");
        // 注册
//        tvNext.setVisibility(View.VISIBLE);
//        tvNext.setText(R.string.register_name);
    }

    @Override
    public void initData() {
        // ledtInputPas.setCheckBox(cbShowHide);
        // cbShowHide.setEditText(ledtInputPas);

        if (Constant.isChangeRequestAddress) {
            tvChangeRequestAddress.setVisibility(View.VISIBLE);
        }
    }

    public void sendRequest(RefreshType type) {
        // 手机号，账号
        mPhone = ledtInputPhone.getText().toString();
        // 密码
        String pas = ledtInputPas.getText().toString();

        if (TextUtils.isEmpty(mPhone)) {
            setPromptDialog("请输入账号");
            return;
        }
        // 是手机号码
        if (!StringUtil.isPhone(mPhone)) {
            setPromptDialog(getString(R.string.phone_error));
            return;
        }
        if (TextUtils.isEmpty(pas)) {
            setPromptDialog("请输入密码");
            return;
        }

        if (!StringUtil.isPassword(pas)) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, "登录密码错误");
            return;
        }

        // 进度条
        RequestParams params = new RequestParams(Constant.USERINFO_USERLOGIN);
        params.addBodyParameter("login_phone", mPhone);
        params.addBodyParameter("login_password", pas);
        params.addBodyParameter("regId", JPushInterface.getRegistrationID(this));
        httpUtil.sendRequest(params, type, this);
    }

    /**
     * 设置提示对话框更改图形验证码
     */
    private void setPromptDialog(String str1) {
        DialogUtil.promptDialog(LoginActivity.this,DialogUtil.HEAD_REGISTER,"温馨提示",str1,null);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.imgBtn_next:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.tv_register:// 注册
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:// 登陆
                sendRequest(RefreshType.RefreshNoPull);
                break;
            case R.id.tv_forget_password:// 忘记密码
                intent = new Intent(LoginActivity.this, PasswordForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_changeRequestAddress:// 切换环境
                intent = new Intent(LoginActivity.this, ChangeRequestAddressActivity.class);
                startActivity(intent);
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
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        userUId = jsonObject.getString("uId");
        goActivity();
    }

    @Override
    public void goActivity() {
        LoginOrExitUtils.loginSuccess(LoginActivity.this, app, LoginOrExitUtils.STATE_LOGIN, userUId, mPhone);
        finish();
    }

}
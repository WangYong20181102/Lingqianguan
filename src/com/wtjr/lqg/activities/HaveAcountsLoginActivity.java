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
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.ForgetLoginPasswordType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HeadChangeUtil;
import com.wtjr.lqg.utils.LoginOrExitUtils;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CircleImageView;

import cn.jpush.android.api.JPushInterface;

/**
 * 有账号登录
 *
 * @author Myf
 */
public class HaveAcountsLoginActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 登录按钮
     */
    private Button btnLogin;

    /**
     * 账号
     */
    private TextView tvAccount;

    /**
     * 密码输入框
     */
    private EditText ledtInputPas;
    /**
     * 忘记密码
     */
    private TextView tvForgetPassword;
    /**
     * 切换账号
     */
    private TextView tvSwitchAccount;
    /**
     * 用户的userID
     */
    private String userUId;
//    /**
//     * 注册
//     */
//    private TextView tvNext;
    /**
     * X返回首页
     */
    private ImageButton ivNext;
    private CircleImageView civHeadPicture;
    /**
     * 注册
     */
    private TextView tvAccountRegist;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_have_acounts_login);
        start();
    }

    @Override
    protected void onStart() {
        super.onStart();

        HeadChangeUtil.requestHeadImage(app, civHeadPicture);
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        tvAccount = (TextView) findViewById(R.id.tv_account);
        ledtInputPas = (EditText) findViewById(R.id.ledt_input_pas);
        btnLogin = (Button) findViewById(R.id.btn_login);

        tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
        tvSwitchAccount = (TextView) findViewById(R.id.tv_switch_account);

        tvAccountRegist = (TextView) findViewById(R.id.tv_account_regist);

//        tvNext = (TextView) findViewById(R.id.tv_next);
        ivNext = (ImageButton) findViewById(R.id.imgBtn_next);

        civHeadPicture = (CircleImageView) findViewById(R.id.civ_head_picture);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // X返回首页按钮监听器
        ivNext.setOnClickListener(this);
        // 登录按钮
        btnLogin.setOnClickListener(this);
        // 网络请求
        httpUtil.setHttpRequesListener(this);
        // 忘记密码
        tvForgetPassword.setOnClickListener(this);
        // 切换账号
        tvSwitchAccount.setOnClickListener(this);
        // 注册
//        tvNext.setOnClickListener(this);
        // 注册
        tvAccountRegist.setOnClickListener(this);

        ledtInputPas.addTextChangedListener(new ButtonEnabledListener(btnLogin, ledtInputPas));
    }

    @Override
    public void setTitle() {
        // 设置名字为:登录
//        tvTitleName.setText(R.string.login_name);
        tvTitleName.setVisibility(View.GONE);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        imgBtnBack.setImageResource(R.drawable.close);

//        // 注册
//        tvNext.setVisibility(View.VISIBLE);
//        tvNext.setText(R.string.register_name);

//        ivNext.setVisibility(View.VISIBLE);
//        ivNext.setImageResource(R.drawable.close);
        TitleNoLine();
    }

    @Override
    public void initData() {
        tvAccount.setText(StringUtil.setBlurryPhone(mCurrentPhone));
    }

    /**
     * 设置提示对话框更改图形验证码
     */
    private void setPromptDialog(String str1) {
        DialogUtil.promptDialog(HaveAcountsLoginActivity.this, DialogUtil.HEAD_REGISTER, "温馨提示",str1, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // TODO 结束当前的Activity T
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.imgBtn_next:// X返回按钮点击操作
                // TODO 结束当前的Activity T
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.btn_login:// 登录
                // 保存状态为登陆
                sendLoginRequest(RefreshType.RefreshNoPull);
                break;
            case R.id.tv_forget_password:// 忘记密码
//                DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, "我们将发送短信验证码到\n" + StringUtil.setBlurryPhone(mCurrentPhone), new
//                        OnClickYesListener() {
//                            @Override
//                            public void onClickYes() {
//                                goSMSActivity();
//                            }
//                        }).setCancelable(true);
                goSMSActivity();
                break;
            case R.id.tv_switch_account:// 切换账号
                startActivity(new Intent(HaveAcountsLoginActivity.this, LoginActivity.class));
                break;
//            case R.id.tv_next:// 注册
//                Intent intent = new Intent(HaveAcountsLoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
//                break;
            case R.id.tv_account_regist:// 注册
                Intent intent = new Intent(HaveAcountsLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 跳转发送短信的界面
     */
    private void goSMSActivity() {
        // 判断是否重新获取验证码
        boolean countdownTimerState = TimeUtil.getCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
        if (countdownTimerState) {
            sendSMSRequest(RefreshType.RefreshNoPull);
        } else {
            Intent intent = new Intent(this, PasswordVerificationCodeActivity.class);
            intent.putExtra(ForgetLoginPasswordType.class.getName(), ForgetLoginPasswordType.ForgetLoginPasswordNotLogin1);
            startActivity(intent);
        }
    }

    /**
     * 发送短信请求
     */
    public void sendSMSRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.USERINFO_SENDVERIFYCODE);
        params.addBodyParameter("login_phone", mCurrentPhone);
        params.addBodyParameter("optType", "2");
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 发送登录请求
     *
     * @param type
     */
    public void sendLoginRequest(RefreshType type) {
        String pwd = ledtInputPas.getText().toString();

        if (TextUtils.isEmpty(pwd)) {
            setPromptDialog("请输入密码");
            return;
        }

        if (!StringUtil.isPassword(pwd)) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, "登录密码错误");
            return;
        }

        RequestParams params = new RequestParams(Constant.USERINFO_USERLOGIN);
        // TODO 手机号需要动态获得
        params.addBodyParameter("login_phone", mCurrentPhone);
        params.addBodyParameter("login_password", pwd);
        params.addBodyParameter("regId", JPushInterface.getRegistrationID(this));
        httpUtil.sendRequest(params, type, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.USERINFO_USERLOGIN)) {// 登录
            JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            userUId = jsonObject.getString("uId");
            goActivity();

        } else if (url.equals(Constant.USERINFO_SENDVERIFYCODE)) {// 忘记登录密码发送短信
            // 存储倒计时的时间
            TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            Intent intent = new Intent(this, PasswordVerificationCodeActivity.class);
            intent.putExtra(ForgetLoginPasswordType.class.getName(), ForgetLoginPasswordType.ForgetLoginPasswordNotLogin1);
            startActivity(intent);
        }
    }

    @Override
    public void goActivity() {
        LoginOrExitUtils.loginSuccess(HaveAcountsLoginActivity.this, app, LoginOrExitUtils.STATE_HAVEACOUNT_LOGIN, userUId, mCurrentPhone);
        finish();
    }
}
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

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.ForgetLoginPasswordType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.LoginOrExitUtils;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CheckBoxShowOrHide;

/**
 * 重新设置密码
 * 
 * @author Myf
 * 
 */
public class PasswordResetActivity extends BaseActivity implements
		OnClickListener, HttpRequesListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 确认密码输入框
	 */
	private EditText etPassword;
	/**
	 * 密码输入框
	 */
	private EditText etPassword2;
	/**
	 * 确定按钮
	 */
	private Button btnDetermine;
	/**
	 * 短信验证码
	 */
	private String mMessageCode;
	/**
	 * 旧密码
	 */
	private String mOldPassword;
	
	/**
	 * 切换密码的明文与暗文的CheckBox
	 */
	private CheckBoxShowOrHide cbSwitch;
	/**
	 * 切换确认密码的明文与暗文的CheckBox
	 */
	private CheckBoxShowOrHide cbSwitch2;
	/**
	 * 区分是登录app后内部操作进行忘记密码的，还是没登录app在外部进行忘记密码的
	 */
	private ForgetLoginPasswordType mForgetLoginPasswordType;

	// /**
	// * 手机号
	// */
	// private String mPhone;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_password_reset);
		start();
	}

	@Override
	public void initIntent() {
		mMessageCode = getIntent().getStringExtra("MessageCode");
		mOldPassword = getIntent().getStringExtra("OldPassword");
		mForgetLoginPasswordType = (ForgetLoginPasswordType) getIntent()
				.getSerializableExtra(ForgetLoginPasswordType.class.getName());

		if (mForgetLoginPasswordType == ForgetLoginPasswordType.ForgetLoginPasswordNotLogin2) {
			mCurrentPhone = getIntent().getStringExtra("Phone");
		}
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);

		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

		etPassword = (EditText) findViewById(R.id.et_password);

		etPassword2 = (EditText) findViewById(R.id.et_password2);

		btnDetermine = (Button) findViewById(R.id.btn_determine);

		cbSwitch = (CheckBoxShowOrHide) findViewById(R.id.cb_switch);

		cbSwitch2 = (CheckBoxShowOrHide) findViewById(R.id.cb_switch2);
		
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		// 设置网络监听
		httpUtil.setHttpRequesListener(this);
		// 确定
		btnDetermine.setOnClickListener(this);
		
		etPassword.addTextChangedListener(new ButtonEnabledListener(btnDetermine,etPassword,etPassword2));
		etPassword2.addTextChangedListener(new ButtonEnabledListener(btnDetermine,etPassword,etPassword2));
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:重新设置密码
		tvTitleName.setText(R.string.user_password_rese_name);
	}

	@Override
	public void initData() {
		cbSwitch.setEditText(etPassword);
		cbSwitch2.setEditText(etPassword2);
	}

	/**
	 * 设置提示对话框更改图形验证码
	 * 
	 */
	private void setPromptDialog(String str1) {
		DialogUtil.promptDialog(PasswordResetActivity.this, DialogUtil.HEAD_REGISTER, str1, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
		case R.id.btn_determine:// 确定
			sendResetPasswordRequest(RefreshType.RefreshNoPull);
			break;
			
		default:
			break;
		}
	}

	/**
	 * 发送重设密码请求
	 */
	public void sendResetPasswordRequest(RefreshType refreshType) {
		String password = etPassword.getText().toString();
		String password2 = etPassword2.getText().toString();
		if (TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)) {
			setPromptDialog("请输入密码");
			return;
		}

		if (!StringUtil.isPasswordRegist(password) || !StringUtil.isPasswordRegist(password2)) {
			setPromptDialog(getString(R.string.password_format_error));
			return;
		}
		
		if (!password.equals(password2)) {
			setPromptDialog("两次输入密码不匹配");
			return;
		}
		
		if (TextUtils.isEmpty(mMessageCode)) {// 没有短信验证码，是登录密码修改
			RequestParams params = new RequestParams(Constant.USERINFO_EDITUSERPASSWORD);
			params.addBodyParameter("user_userunid", mUid);
			params.addBodyParameter("old_password", mOldPassword);
			params.addBodyParameter("new_password", password);
			httpUtil.sendRequest(params, refreshType, this);
		} else {// 有短信验证码，是短信修改
			RequestParams params = new RequestParams(Constant.USERINFO_RESETPASSWORD);
			params.addBodyParameter("login_phone", mCurrentPhone);
			params.addBodyParameter("message_Code", mMessageCode + "");
			params.addBodyParameter("login_password", password);
			httpUtil.sendRequest(params, refreshType, this);
		}
	}

	@Override
	public void onFailure(String url, String errorContent) {
		DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
	}

	@Override
	public void onSuccess(String url, JSONBase jsonBase) {
	    DialogUtil.promptDialog(this, "修改密码成功,请重新登录!", new OnClickYesListener() {
            @Override
            public void onClickYes() {
                Intent intent = null;
                switch (mForgetLoginPasswordType) {
                case ForgetLoginPasswordHaveLogin:
                    LoginOrExitUtils.exitSuccess(PasswordResetActivity.this, app);
                    break;
                case ForgetLoginPasswordNotLogin1:
                    intent = new Intent(PasswordResetActivity.this,HaveAcountsLoginActivity.class);
                    break;
                case ForgetLoginPasswordNotLogin2:
                    intent = new Intent(PasswordResetActivity.this, LoginActivity.class);
                    break;
                }
                if(intent != null) {
                    startActivity(intent);
                }
            }
        });
	}
}

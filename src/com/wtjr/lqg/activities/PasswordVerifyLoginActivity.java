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
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CheckBoxShowOrHide;

/**
 * 登陆密码验证界面(修改密码)
 * 
 */
public class PasswordVerifyLoginActivity extends BaseActivity implements
		OnClickListener,HttpRequesListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 用来显示和隐藏密码
	 */
	private CheckBoxShowOrHide cbSwitch;
	/**
	 * 验证密码输入
	 */
	private EditText etPassword;
	/**
	 * 确认按钮
	 */
	private Button btnDetermine;
	/**
	 * 密码
	 */
	private String mPwd;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_password_verify_login);
		start();
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		etPassword = (EditText) findViewById(R.id.et_password);
		cbSwitch = (CheckBoxShowOrHide) findViewById(R.id.cb_switch);
		btnDetermine = (Button) findViewById(R.id.btn_determine);
	
	}

	@Override
	public void setListener() {
		//设置网络监听
		httpUtil.setHttpRequesListener(this);
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		//按钮确认
		btnDetermine.setOnClickListener(this);
		
		etPassword.addTextChangedListener(new ButtonEnabledListener(btnDetermine,etPassword));
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:安全验证
		tvTitleName.setText(R.string.user_security_verify);
	}

	@Override
	public void initData() {
		cbSwitch.setEditText(etPassword);
	}

	/**
	 * 设置提示对话框更改图形验证码
	 * 
	 */
	private void setPromptDialog(String str1) {
		DialogUtil.promptDialog(PasswordVerifyLoginActivity.this, DialogUtil.HEAD_REGISTER,str1,null);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
		case R.id.btn_determine:// 按钮确认
			sendHttpRequest(RefreshType.RefreshNoPull);
			break;

		default:
			break;
		}
	}
	
	public void sendHttpRequest(RefreshType refreshType) {
		mPwd = etPassword.getText().toString();

		if (TextUtils.isEmpty(mPwd)) {
			setPromptDialog("请输入密码");
			return;
		}

		if (!StringUtil.isPassword(mPwd)) {
			DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, "登录密码错误");
			return;
		}
		

		RequestParams params = new RequestParams(Constant.USERINFO_USERLOGIN);
		params.addBodyParameter("login_phone", mCurrentPhone);
		params.addBodyParameter("login_password", mPwd);
		httpUtil.sendRequest(params, refreshType, this);
	}

	@Override
	public void onFailure(String url, String errorContent) {
	    setPromptDialog(errorContent);
	}

	@Override
	public void onSuccess(String url, JSONBase jsonBase) {
	    goActivity();
	}
	
    @Override
    public void goActivity() {
    	Intent intent = new Intent(PasswordVerifyLoginActivity.this,PasswordResetActivity.class);
    	intent.putExtra("OldPassword", mPwd);
    	intent.putExtra(ForgetLoginPasswordType.class.getName(), ForgetLoginPasswordType.ForgetLoginPasswordHaveLogin);
        startActivity(intent);
    }

}

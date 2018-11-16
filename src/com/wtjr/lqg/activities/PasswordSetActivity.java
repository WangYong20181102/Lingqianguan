package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.ForgetLoginPasswordType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.UsuallyLayoutItem;

/**
 * 密码设置界面
 * 
 * @author Myf
 *  
 */
public class PasswordSetActivity extends BaseActivity implements OnClickListener,HttpRequesListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 通过登陆密码验证
	 */
	private UsuallyLayoutItem uliLoginPassword;
	/**
	 * 通过手机短信验证
	 */
	private UsuallyLayoutItem uliPhoneSms;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_set_password);
		start();
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		uliLoginPassword = (UsuallyLayoutItem) findViewById(R.id.uli_login_password);
		uliPhoneSms = (UsuallyLayoutItem) findViewById(R.id.uli_phone_sms);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		// 通过登陆密码验证
		uliLoginPassword.setOnClickListener(this);
		// 通过手机短信验证
		uliPhoneSms.setOnClickListener(this);
		//设置网络监听
		httpUtil.setHttpRequesListener(this);
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:密码设置
		tvTitleName.setText(R.string.user_set_password_name);
	}

	@Override
	public void initData() {
	    //去除左侧的隐藏图片
	    uliLoginPassword.setImageContentrResourceL(0);
	    uliPhoneSms.setImageContentrResourceL(0);
	}


	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
		case R.id.uli_login_password:// 通过登陆密码验证
			intent = new Intent(PasswordSetActivity.this,PasswordVerifyLoginActivity.class);
			startActivity(intent);
			break;
		case R.id.uli_phone_sms:// 通过手机短信验证
			//判断是否重新获取验证码
			boolean countdownTimerState = TimeUtil.getCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
			if (countdownTimerState) {
				sendSMSRequest(RefreshType.RefreshNoPull);
			}else{
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
         params.addBodyParameter("login_phone", mCurrentPhone);
         params.addBodyParameter("optType", "2");
         httpUtil.sendRequest(params,refreshType,this);
     }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
	public void onSuccess(String url, JSONBase jsonBase) {
    	//存储倒计时的时间
    	TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
		goActivity();
	}

	@Override
	public void goActivity() {
		Intent intent = new Intent(PasswordSetActivity.this,PasswordVerificationCodeActivity.class);
	    intent.putExtra(ForgetLoginPasswordType.class.getName(), ForgetLoginPasswordType.ForgetLoginPasswordHaveLogin);
		startActivity(intent);
	}

}
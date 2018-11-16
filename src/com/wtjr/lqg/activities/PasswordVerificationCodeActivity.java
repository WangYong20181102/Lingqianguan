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
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.TextViewCountDownView;

/**
 * 短信验证码验证界面(修改密码)
 * 
 * @author Myf
 * 
 */
public class PasswordVerificationCodeActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
	/**
	 * 倒计时的时间
	 */
	private long mTimeRemaining=180000;
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
    
    /**
     * 确定按钮
     */
    private Button btDetermine;
    /**
     * 显示手机号
     */
    private TextView tvPhone;
	 /**
     * 短信验证码
     */
	private EditText etSms;
	/**
	 * 短信验证码
	 */
	private String mMessageCode;
	/**
	 * 验证码倒计时
	 */
	private TextViewCountDownView tcdvSms;
	/**
	 * 区分是登录app后内部操作进行忘记密码的，还是没登录app在外部进行忘记密码的
	 */
    private ForgetLoginPasswordType mForgetLoginPasswordType;
    /**
     * 手机号
     */
    private String mPhone;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_password_verification_code);
		start();

	}
	
    @Override
    public void initIntent() {
        mForgetLoginPasswordType = (ForgetLoginPasswordType) getIntent().getSerializableExtra(ForgetLoginPasswordType.class.getName());

        if(mForgetLoginPasswordType == ForgetLoginPasswordType.ForgetLoginPasswordNotLogin2) {
            mCurrentPhone = getIntent().getStringExtra("Phone");
        }
    }

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		
		etSms = (EditText) findViewById(R.id.ledt_sms);
		
		tcdvSms = (TextViewCountDownView) findViewById(R.id.tcdv_sms);
		
		btDetermine = (Button) findViewById(R.id.bt_determine);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
		//设置网络监听
		httpUtil.setHttpRequesListener(this);
		//重新发送
		tcdvSms.setOnClickListener(this);
		//提交
		btDetermine.setOnClickListener(this);
		
		etSms.addTextChangedListener(new ButtonEnabledListener(btDetermine,etSms));
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:活动
		tvTitleName.setText(R.string.verification_code_name);

	}

	@Override
	public void initData() {
	    tvPhone.setText(StringUtil.setBlurryPhone(mCurrentPhone));
	    //倒计时
	    tcdvSms.cancelCountDown();
	    Long timeRemaining = TimeUtil.getTimeRemaining(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
	    tcdvSms.startCountDown(timeRemaining);
	    
	    L.hintD("timeRemaining============="+timeRemaining);
	}

	/**
	 * 设置提示对话框更改图形验证码
	 * 
	 */
	private void setPromptDialog(String str1) {
		DialogUtil.promptDialog(PasswordVerificationCodeActivity.this, DialogUtil.HEAD_REGISTER,str1, null);
	}
	
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imgBtn_back:// 返回按钮点击操作
            // 结束当前的Activity
            finish();
            break;
        case R.id.tcdv_sms:// 重新发送短信验证
			etSms.setText("");
        	sendRepeatSMSRequest(RefreshType.RefreshNoPull);
        	break;
        case R.id.bt_determine:// 短信验证码提交
            sendVerifySMSRequest(RefreshType.RefreshNoPull);
            break;
        default:
            break;
        }
    }
    
	/**
     * 发送短信请求
     */
	public void sendRepeatSMSRequest(RefreshType refreshType) {
         RequestParams params = new RequestParams(Constant.USERINFO_SENDVERIFYCODE);
         params.addBodyParameter("login_phone", mCurrentPhone);
         params.addBodyParameter("optType", "2");
         httpUtil.sendRequest(params,refreshType,this);
     }
    
	
	/**
     * 短信验证码提交
     */
    public void sendVerifySMSRequest(RefreshType refreshType) {
        mMessageCode = etSms.getText().toString();
		if (TextUtils.isEmpty(mMessageCode)) {
			setPromptDialog("请输入验证码");
			return ;
		}
		
		
        RequestParams params = new RequestParams(Constant.USERINFO_CHECKVERIFYCODE);
        params.addBodyParameter("login_phone", mCurrentPhone);
        params.addBodyParameter("message_Code", mMessageCode);
        params.addBodyParameter("optType", "2");
        httpUtil.sendRequest(params,refreshType,this);
    }


	@Override
	public void onFailure(String url, String errorContent) {
	    DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
	}

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
    	if (url.equals(Constant.USERINFO_CHECKVERIFYCODE)) {//提交短信验证码
    		//请求成功清理倒计时
    		TimeUtil.cleanTimeRemaining(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
    		goActivity();
		}else {//发送短信
		    //倒计时
	        tcdvSms.cancelCountDown();
	        TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
	        tcdvSms.startCountDown(mTimeRemaining);
		}
        
    }
    
    @Override
    public void goActivity() {
    	Intent intent = new Intent(PasswordVerificationCodeActivity.this,PasswordResetActivity.class);
        intent.putExtra("MessageCode", mMessageCode);
        intent.putExtra("Phone", mCurrentPhone);
        intent.putExtra(ForgetLoginPasswordType.class.getName(), mForgetLoginPasswordType);
        startActivity(intent);
    }
}

package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.ClearEditText;

/**
 * 密码忘记界面
 * 
 * @author Myf
 * 
 */
public class PasswordForgetActivity extends BaseActivity implements OnClickListener,HttpRequesListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 手机号
	 */
    private ClearEditText cetPhone;
    /**
     * 下一步
     */
    private Button btnNext;
    private String mPhone;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_user_forget_password);
		start();
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		
		cetPhone = (ClearEditText) findViewById(R.id.cet_phone);
		btnNext = (Button) findViewById(R.id.btn_next);
	}
	

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
		
		cetPhone.addTextChangedListener(new ButtonEnabledListener(btnNext,cetPhone));
	}

	@Override
	public void setTitle() {
		// 设置名字为:密码忘记
		tvTitleName.setText(R.string.user_forget_password_name);
	    // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
	    
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
	    case R.id.btn_next:// 下一步按钮点击操作
	        mPhone = cetPhone.getText().toString();
	        
	        if(TextUtils.isEmpty(mPhone)) {
	            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请输入手机号");
	            return;
	        }
	        
	        if(!StringUtil.isPhone(mPhone)) {
	            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请输入正确的手机号");	
	            return;
	        }
	        
	        //判断是否重新获取验证码
	        boolean countdownTimerState = TimeUtil.getCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mPhone);
	        if (countdownTimerState) {
	            sendSMSRequest(RefreshType.RefreshNoPull);
	        }else{
	            Intent intent = new Intent(this,PasswordVerificationCodeActivity.class);
	            intent.putExtra("Phone", mPhone);
	            intent.putExtra(ForgetLoginPasswordType.class.getName(), ForgetLoginPasswordType.ForgetLoginPasswordNotLogin2);
	            startActivity(intent);     
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
         params.addBodyParameter("login_phone", mPhone);
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
        TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mPhone);
        
        Intent intent = new Intent(this,PasswordVerificationCodeActivity.class);
        intent.putExtra("Phone", mPhone);
        intent.putExtra(ForgetLoginPasswordType.class.getName(), ForgetLoginPasswordType.ForgetLoginPasswordNotLogin2);
        startActivity(intent);             
    }
}
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
import com.wtjr.lqg.basecommonly.BaseAppManager;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.sharedpreferences.SaveLoginState;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.TextViewCountDownView;

/**
 * 短信验证码验证界面(修改手机号)
 * 
 * @author Myf
 * 
 */
public class ChangePhoneSMSVerifyActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
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
    private String mNewPhone;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_password_verification_code);
		start();

	}
	
    @Override
    public void initIntent() {
        mNewPhone = getIntent().getStringExtra("NewPhone");
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
		
		etSms.addTextChangedListener(new ButtonEnabledListener(btDetermine, etSms));
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
//		imgBtnBack.setImageResource(R.drawable.back2);
		// 设置名字为:活动
		tvTitleName.setText(R.string.verification_code_name);
//		tvTitleName.setTextColor(Color.BLACK);

	}

	@Override
	public void initData() {
	    tvPhone.setText(StringUtil.setBlurryPhone(mNewPhone));
	    //倒计时
	    tcdvSms.cancelCountDown();
	    
	    //判断是否重新获取验证码
        Long timeRemaining = TimeUtil.getTimeRemaining(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
        tcdvSms.startCountDown(timeRemaining);
	}

	/**
	 * 设置提示对话框更改图形验证码
	 * 
	 */
	private void setPromptDialog(String str1) {
		DialogUtil.promptDialog(ChangePhoneSMSVerifyActivity.this, DialogUtil.HEAD_REGISTER,str1, null);
	}
	
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imgBtn_back:// 返回按钮点击操作
            // 结束当前的Activity
            finish();
            break;
        case R.id.tcdv_sms:// 重新发送短信验证
        	sendRepeatSMSRequest(RefreshType.RefreshNoPull);
        	break;
        case R.id.bt_determine:// 短信验证码提交
            sendNewNumberSubmit(RefreshType.RefreshNoPull);
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
         params.addBodyParameter("login_phone", mNewPhone);
         params.addBodyParameter("optType", "5");
         httpUtil.sendRequest(params,refreshType,this);
     }
    
	

    /**
     * 新手机号码提交
     * @param refreshnopull
     */
    private void sendNewNumberSubmit(RefreshType refreshnopull) {
        mMessageCode = etSms.getText().toString();
        
        if (TextUtils.isEmpty(mMessageCode)) {
            setPromptDialog("验证码有误，请输入验证码");
            return;
        }
        
        RequestParams params = new RequestParams(Constant.USERINFO_CHANGELOGINPHONE);
        params.addBodyParameter("newPhone", mNewPhone);
        params.addBodyParameter("phone", mCurrentPhone);
        params.addBodyParameter("msgCode", mMessageCode);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params,refreshnopull,this);
    }


	@Override
	public void onFailure(String url, String errorContent) {
	    DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
	}

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
    	if (url.equals(Constant.USERINFO_SENDVERIFYCODE)) {//发送短信
    	    //存储倒计时的时间
            TimeUtil.setCountdownTimerState(app, Constant.DOWN_TIMER_SET_PASSWORD, mCurrentPhone);
            //倒计时
            tcdvSms.cancelCountDown();
            tcdvSms.startCountDown(mTimeRemaining);
		}else {
		    goActivity();
		}
    }
    
    @Override
    public void goActivity() {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "手机号修改成功需重新登陆", new OnClickYesListener() {
            
            @Override
            public void onClickYes() {
                // 保存状态为退出
                SaveLoginState.unLogin(app);
                //推出uid为null
                SaveCurrentUidUtil.setCurrentUid(app, "");
                Intent intent = new Intent(ChangePhoneSMSVerifyActivity.this, LoginActivity.class);
                startActivity(intent);
                BaseAppManager.getAppManager().finishAllActivity();                
            }
        });
    }
}

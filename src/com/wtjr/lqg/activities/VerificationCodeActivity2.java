package com.wtjr.lqg.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.TextViewCountDownView;

/**
 * 填写验证码页面
 * 
 * @author Myf
 * 
 */
public class VerificationCodeActivity2 extends BaseActivity implements
		OnClickListener, HttpRequesListener{
	
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
    /**
     * 显示的手机号
     */
    private TextView tvPhone;
    /**
     * 提交按钮
     */
    private Button btDetermine;
    /**
     * 短信验证码
     */
	private EditText ledtSms;
	/**
	 * 倒计时
	 */
	private TextViewCountDownView tcdvSms;
	


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_password_verification_code);
		start();

	}
	
	@Override
	public void initIntent() {
		
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		
		ledtSms = (EditText) findViewById(R.id.ledt_sms);
		
		tcdvSms = (TextViewCountDownView) findViewById(R.id.tcdv_sms);
		
		tvPhone = (TextView) findViewById(R.id.tv_phone); 
		
		btDetermine = (Button) findViewById(R.id.bt_determine); 
	}

	@Override
	public void setListener() {
		// 网络请求
		httpUtil.setHttpRequesListener(this);
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		// 重新发送
		tcdvSms.setOnClickListener(this);
		// 提交
		btDetermine.setOnClickListener(this);
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
	}

	public void sendRequest() {
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
		case R.id.bt_determine://提交
			
            break;
		case R.id.tcdv_sms://重新发送
			
			break;
		}
	}

	@Override
	public void onFailure(String url, String errorContent) {

	}
	
    @Override
    public void onSuccess(String url, JSONBase jsonBase) {

    }
}

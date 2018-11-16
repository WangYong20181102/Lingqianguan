package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;

/**
 * 短信验证码验证
 * 
 * @author Myf
 * 
 */
public class PasswordVerifySmsActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
    private Bundle mBundle;
    private String mSMS;
    /**
     * 确定按钮
     */
    private Button btDetermine;
    /**
     * 显示手机号
     */
    private TextView tvPhone;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_verification_code);
		start();

	}
	
	@Override
	public void initIntent() {
	    mBundle = getIntent().getExtras();
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		
		tvPhone = (TextView) findViewById(R.id.tv_phone);
		
		
		btDetermine = (Button) findViewById(R.id.bt_determine);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
		//设置网络监听
		httpUtil.setHttpRequesListener(this);
		
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
	    tvPhone.setText(mBundle.getString("Phone"));
	}

	public void click(View view) {

	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imgBtn_back:// 返回按钮点击操作
            // 结束当前的Activity
            finish();
            break;
        case R.id.bt_determine:// 确定
            sendVerifySMSRequest(RefreshType.RefreshNoPull);
            break;
        default:
            break;
        }
    }
	
	/**
     * 发送验证短信请求
     */
    public void sendVerifySMSRequest(RefreshType refreshType) {
        if (mSMS.length() < 6) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "验证码有误");
            return;
        }
        
        
        RequestParams params = new RequestParams(Constant.USERINFO_CHECKVERIFYCODE);
        params.addBodyParameter("login_phone", mBundle.getString("Phone"));
        params.addBodyParameter("message_Code", mSMS);
        params.addBodyParameter("optType", "2");
        httpUtil.sendRequest(params,refreshType,this);
    }


	@Override
	public void onFailure(String url, String errorContent) {
	    DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
	}

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        Intent intent = new Intent(PasswordVerifySmsActivity.this,PasswordResetActivity.class);
        mBundle.putString("SMS", mSMS);
        intent.putExtras(mBundle);
        startActivity(intent);
    }
}

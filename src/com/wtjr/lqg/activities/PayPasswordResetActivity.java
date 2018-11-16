package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView.OnPayListener;

/**
 * 忘记支付密码后重置支付密码页面
 * 
 */
public class PayPasswordResetActivity extends BaseActivity implements OnClickListener,HttpRequesListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
    private TextView tv_title;
    /**
     * 密码圆点
     */
    private LinearLayout ll_passwordCircle;
    /**
     * 第一次输入密码的内容
     */
    private String oldPassword;
    private PayPasswordView passwordView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_reset_pay_password);
		start();
	}
	
	@Override
	public void initIntent() {
	    
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		//设置网络监听
		httpUtil.setHttpRequesListener(this);
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:设置支付密码
		tvTitleName.setText(R.string.user_set_pay_password_name);
	}

    @Override
    public void initData() {
        LinearLayout lllllllllll = (LinearLayout) findViewById(R.id.ll_payPasswordView);
        
        View view = getDecorViewDialog(1);
        lllllllllll.addView(view);
        
        ll_passwordCircle  = (LinearLayout) view.findViewById(R.id.ll_passwordCircle);
        
        TextView tvPayType = (TextView) view.findViewById(R.id.tv_payType);
        TextView tv_paymentValue = (TextView) view.findViewById(R.id.tv_paymentValue);
        ImageButton bt_closePayment = (ImageButton) view.findViewById(R.id.bt_closePayment);
        
        
        tvPayType.setVisibility(View.INVISIBLE);
        tv_paymentValue.setVisibility(View.INVISIBLE);
        bt_closePayment.setVisibility(View.INVISIBLE);
        
        tv_title = (TextView) view.findViewById(R.id.tv_title1);
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;

		default:
			break;
		}
	}


    
    private View getDecorViewDialog(final int mode) {
          passwordView = PayPasswordView.getInstance(this,new OnPayListener() {
            @Override
            public void onSurePay(String password) {
                
                if(TextUtils.isEmpty(oldPassword)) {
                    passwordView.clearPassword();
                }else {
                    if(password.equals(oldPassword)) {//密码一致
                        sendSetPayPasswordRequest(password, RefreshType.RefreshNoPull);
                    }else {//两次密码不一致
                        DialogUtil.promptDialog(PayPasswordResetActivity.this, DialogUtil.HEAD_BAND_BANK, "两次密码不一致", new OnClickYesListener() {

                            @Override
                            public void onClickYes() {
                                oldPassword = null;
                                passwordView.clearPassword();
                                tv_title.setText("请输入支付密码");
                            }
                        });
                    }
                }
                
                oldPassword = password;
                tv_title.setText("请再次输入支付密码");
            }
            
            @Override
            public void onCancelPay() {
                
            }

            @Override
            public void onContent(String content) {
                String password =content.toString();
                int length = password.length();
                
                for (int i = 0; i < 6; i++) {
                    ll_passwordCircle.getChildAt(i).setVisibility(View.INVISIBLE);
                }
                
                for (int i = 0; i < length; i++) {
                    ll_passwordCircle.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }
        });
         
         return passwordView.getView();
    }

    
    /**
     * 发送设置支付密码网络请求
     */
    public void sendSetPayPasswordRequest(String password,RefreshType refreshType) {
         RequestParams params = new RequestParams(Constant.SETTINGUP_SETPAYMENTCODE);
         params.addBodyParameter("user_userunid", mUid);
         params.addBodyParameter("isforgetPassword", "1");
         params.addBodyParameter("new_paypassword", password);
         httpUtil.sendRequest(params,refreshType,this);
     }
    

    @Override
    public void onFailure(String url, String errorContent) {
        if(url.equals(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE)){//判断是否设置过支付密码
            DialogUtil.promptDialog(this, DialogUtil.HEAD_REGISTER, errorContent, new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    oldPassword = null;
                    passwordView.clearPassword();
                    tv_title.setText("请输入支付密码");
                }
            });
        }
    }

    
    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        DialogUtil.promptDialog(this, "设置支付密码成功",new OnClickYesListener() {
            
            @Override
            public void onClickYes() {
                Intent intent = new Intent(PayPasswordResetActivity.this,SetingActivity.class);
                startActivity(intent);
                
            }
        });
    }
}
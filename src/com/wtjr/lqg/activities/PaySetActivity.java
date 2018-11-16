package com.wtjr.lqg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.enums.PayPasswordType;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.UsuallyLayoutItem;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

/**
 * 支付设置
 */
public class PaySetActivity extends BaseActivity implements OnClickListener,OnRefreshListener, HttpRequesListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 支付密码
	 */
    private UsuallyLayoutItem uliPayPassword;
    /**
     * 第三方充值通道
     */
    private UsuallyLayoutItem uliRechargePassageway;
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_pay_set);
		start();
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		
		uliPayPassword = (UsuallyLayoutItem) findViewById(R.id.uli_pay_password);
		uliRechargePassageway = (UsuallyLayoutItem) findViewById(R.id.uli_rechargePassageway);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
	    
	    httpUtil.setHttpRequesListener(this);
	    
	    uliPayPassword.setOnClickListener(this);
	    uliRechargePassageway.setOnClickListener(this);
	}

	@Override
	public void setTitle() {
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		
		tvTitleName.setText("支付设置");
	}

	@Override
	public void initData() {
	    uliPayPassword.setImageContentrResourceL(0);
	    uliRechargePassageway.setImageContentrResourceL(0);
	}


	@Override
	public void onClick(View v) {
	    Intent intent = null;
		switch (v.getId()) {
		case R.id.imgBtn_back:// 返回按钮点击操作
			// 结束当前的Activity
			finish();
			break;
		case R.id.uli_pay_password:// 支付密码
		    intent = new Intent(PaySetActivity.this, PayPasswordSetActivity.class);
            intent.putExtra(PayPasswordType.class.getName(), PayPasswordType.PayPasswordNotUnequivocal);
            break;  
		case R.id.uli_rechargePassageway:// 第三方充值通道
            intent = new Intent(PaySetActivity.this, PayThirdpartyActivity.class);
            break;  
		}
		
		if(intent != null) {
		    startActivity(intent);
		}
	}
	
	
	 /**
     * 发送消息请求
     */
//    public void sendMessageRequest(RefreshType refreshType) {
//        RequestParams params = new RequestParams(Constant.SETTINGUP_MSGPAGE);
//        params.addBodyParameter("user_userunid", mUid);
//        httpUtil.sendRequest(params,refreshType,this);
//    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onFailure(String url, String errorContent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
    }
}

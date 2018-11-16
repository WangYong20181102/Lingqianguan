package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.enums.RollInType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.MoneyEditText;
import com.wtjr.lqg.widget.MoneyEditText.OnTextChangeListener;

/**
 * 转入
 * 
 * @author Myf
 * 
 */
public class RollInActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnTextChangeListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 输入转入金额
	 */
    private MoneyEditText etRollInMoney;
    /**
     * 转入金额
     */
    private String mRollInMoney;
    private Button btYesRollIn;
    
    /**
     * 当前转入类型
     */
    private RollInType mRollInType;
    /**
     * 可转入金额
     */
    private TextView tvCanRollInMoney;
    /**
     * 预计收益
     */
    private TextView tvEarnings;
    /**
     * 全部转入
     */
    private Button btnAllRollIn;
    /**
     * 最大转入金额
     */
    private double mMaxRollInMoney;

    /**
     * 标题栏底部横线
     */
    private View tittleBottomLine;
    /**
     * 年化收益最小值
     */
    private String minLqbRate;

    @Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_roll_in);
		start();
	}
	
	
	@Override
	public void initIntent() {
	    mRollInType = (RollInType) getIntent().getSerializableExtra("RollInType");
        minLqbRate = SharedPreferencesUtil.getPrefString(this, "minLqbRate", "");
	    mRollInType = RollInType.Balance;
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		etRollInMoney = (MoneyEditText) findViewById(R.id.et_rollInMoney);
		btYesRollIn = (Button) findViewById(R.id.bt_yesRollIn);
        tittleBottomLine = findViewById(R.id.title_bottom_line);
		
		//可转入金额
		tvCanRollInMoney = (TextView) findViewById(R.id.tv_canRollInMoney);
		//预计收益
		tvEarnings = (TextView) findViewById(R.id.tv_earnings);
		
		btnAllRollIn = (Button) findViewById(R.id.btn_allRollIn);
	}

	@Override
	public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 设置网络监听
        httpUtil.setHttpRequesListener(this);
        //确认转入按钮监听器
        btYesRollIn.setOnClickListener(this);
        
        etRollInMoney.setOnTextChangeListener(this);
        
        btnAllRollIn.setOnClickListener(this);
	}

	@Override
	public void setTitle() {
		// 设置名字为:转入
		tvTitleName.setText("余额转入");
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        tittleBottomLine.setVisibility(View.GONE);
	}

	@Override
	public void initData() {
	    switch (mRollInType) {
        case TYJ:
            mMaxRollInMoney = app.mAccountData.usableExpMoney;
            tvCanRollInMoney.setText(Html.fromHtml("<font color='#3f1e00'>可转入金额(元)：</font>" + "<font color='#fabd46'>" + MoneyFormatUtil.format(app.mAccountData.usableExpMoney) + "</font>"));
            break;
        case Balance:
            mMaxRollInMoney = app.mAccountData.available_money;
            tvCanRollInMoney.setText(Html.fromHtml("<font color='#3f1e00'>可转入金额(元)：</font>" + "<font color='#fabd46'>" + MoneyFormatUtil.format(app.mAccountData.available_money) + "</font>"));
            break;
        }
	    
	    etRollInMoney.setInputMaxMoney(app.mAccountData.available_money);
	    
	    btYesRollIn.setEnabled(false);
	}

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imgBtn_back:// 返回按钮点击操作
            // 结束当前的Activity
            finish();
            break;
        case R.id.bt_yesRollIn:// 确认转入
            sendRollInRequest(RefreshType.RefreshNoPull);
            break;
        case R.id.btn_allRollIn:// 全部转入
            etRollInMoney.setText(MoneyFormatUtil.format2(mMaxRollInMoney));
            break;
        default:
            break;
        }
    }
    
    /**
     * 金额输入回调监听
     */
    @Override
    public void setTextChange(String money) {
        //计算30天收益
        Double interest = Double.valueOf(Double.parseDouble(minLqbRate) / 100);
        Double outMoney = Double.valueOf(money);
        tvEarnings.setText(MoneyFormatUtil.sEarnings(outMoney,interest,365,30));
        
        
        if(TextUtils.isEmpty(money) || Double.parseDouble(money) == 0) {
            btYesRollIn.setEnabled(false);
        }else {
            btYesRollIn.setEnabled(true); 
        }
    }
	
	
    /**
     * 发送转入请求
     */
    public void sendRollInRequest(RefreshType refreshType) {
        mRollInMoney = etRollInMoney.getText().toString();
        if (TextUtils.isEmpty(mRollInMoney) || Double.parseDouble(mRollInMoney) == 0) {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请输入正确的转入金额");
            return;
        }
        

        switch (mRollInType) {
        case TYJ:
            RequestParams params = new RequestParams(Constant.LQB_EXPMONEYINTOLQB);
            params.addBodyParameter("user_userunid", app.mAccountData.uId);
            params.addBodyParameter("expMoney2lqb", mRollInMoney);//体验金转入
            httpUtil.sendRequest(params,refreshType,this);
            break;
        case Balance:
            RequestParams params2 = new RequestParams(Constant.LQB_AVAILIABLEMONEYINTOLQB);
            params2.addBodyParameter("user_userunid", app.mAccountData.uId);
            params2.addBodyParameter("useMoney2lqb", mRollInMoney);//余额转入
            httpUtil.sendRequest(params2,refreshType,this);
            break;
        }
    }
    

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        // 发送账户数据和零钱宝数据更新广播
        Intent mIntent = new Intent(Constant.UPDATE_ACCOUNTDATA_LQBDATA);
        mIntent.putExtra("ShowFragmentLocation", 1);
        sendBroadcast(mIntent, Manifest.permission.receiver_permission);

        finish();
    }

}
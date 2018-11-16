package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.ClearEditText;

/**
 * 昵称
 * 
 * @author Myf
 * 
 */
public class NickNameActivity extends BaseActivity implements OnClickListener,
		HttpRequesListener, TextWatcher {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	/**
	 * 昵称输入框
	 */
	private ClearEditText edtNickName;
	/**
	 * 保存
	 */
	private TextView tvNext;
	/**
	 * 昵称
	 */
	private String mNickName;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_nick_name);
		start();
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		tvNext = (TextView) findViewById(R.id.tv_next);
		edtNickName = (ClearEditText) findViewById(R.id.edt_nick_name);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		// 保存
		tvNext.setOnClickListener(this);
		// 设置网络监听
		httpUtil.setHttpRequesListener(this);
		
		edtNickName.addTextChangedListener(this);
		 
	}

	@Override
	public void setTitle() {
		tvNext.setText(R.string.save);
		tvNext.setVisibility(View.VISIBLE);
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
		// 设置名字为:昵称
		tvTitleName.setText(R.string.nick_name_name);
		tvNext.setTextColor(getResources().getColor(R.color.FC_999999));
		tvNext.setEnabled(false);

		if (TextUtils.isEmpty(app.mAccountData.nickName)) {
			return;
		}
		edtNickName.setHint(app.mAccountData.nickName);
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

		case R.id.tv_next:// 保存

			sendRiskMessageRequest(RefreshType.RefreshNoPull);
			break;

		default:
			break;
		}
	}

	/**
	 * 发送网络请求
	 */
	public void sendRiskMessageRequest(RefreshType refreshType) {
		mNickName = edtNickName.getText().toString();
		if (TextUtils.isEmpty(mNickName)) {
			DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请填写昵称");
			return;
		}
		
		if (!StringUtil.isSpecialCharacters(mNickName)) {
			DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "昵称不能包含特殊符号!");
			return;
		}
		RequestParams params = new RequestParams(Constant.SETTINGUP_SETNICKNAME);
		params.addBodyParameter("user_userunid", mUid);
		params.addBodyParameter("nickname", mNickName);
		httpUtil.sendRequest(params, refreshType, this);
	}

	@Override
	public void onFailure(String url, String errorContent) {
		DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
	}

	@Override
	public void onSuccess(String url, JSONBase jsonBase) {
		app.mAccountData.nickName = mNickName;

		DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK,
				jsonBase.getMessage(), new OnClickYesListener() {
					@Override
					public void onClickYes() {
						finish();
					}
				});
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		if (!TextUtils.isEmpty(arg0)) {
			tvNext.setTextColor(getResources().getColor(R.color.FC_333333));
			tvNext.setEnabled(true);
		}else {
			tvNext.setTextColor(getResources().getColor(R.color.FC_999999));
			tvNext.setEnabled(false);
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

}

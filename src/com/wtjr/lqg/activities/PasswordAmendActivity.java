package com.wtjr.lqg.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;

/**
 * 用户修改密码
 * 
 * @author Myf
 * 
 */
public class PasswordAmendActivity extends BaseActivity implements
		OnClickListener {
	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_user_amend_password);
		start();
	}

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
		imgBtnBack.setVisibility(View.VISIBLE);
	}

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
	}

	@Override
	public void setTitle() {
		// 设置名字为:修改密码
		tvTitleName.setText(R.string.user_amend_password_name);
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

		default:
			break;
		}
	}
}
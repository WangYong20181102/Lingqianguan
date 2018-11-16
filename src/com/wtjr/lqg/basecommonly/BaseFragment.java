package com.wtjr.lqg.basecommonly;

import org.xutils.x;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.wtjr.lqg.activities.LoginActivity;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SaveCurrentPhoneUtil;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.sharedpreferences.SaveFirstInState;
import com.wtjr.lqg.sharedpreferences.SaveLoginState;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.HttpUtil;

public class BaseFragment extends Fragment implements IBaseListener {
	/**
	 * 是否登录isLogin是true是登录状态，反之是未登录
	 */
	public boolean isLogin = false;
	/**
	 * 用户的uid
	 */
	public String mUid;
	/**
	 * 当前的手机号码
	 */
	public String mCurrentPhone;
	
	public HttpUtil mHttpUtil;
	public LqgApplication app;
	public MainActivity mMainActivity;
	/**
	 * 是否是第一次的状态(没登录过就是第一次true,登录过就是一直是false)
	 */
	public boolean mFirstInState = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHttpUtil = new HttpUtil();
		app = (LqgApplication) x.app();

		mMainActivity = (MainActivity) getActivity();
		
		isLogin = SaveLoginState.getLoginState(app);
		
		mUid = SaveCurrentUidUtil.getCurrentUid(app);
		
		mCurrentPhone = SaveCurrentPhoneUtil.getCurrentPhone(app);
		
		mFirstInState = SaveFirstInState.getFirstInState(app);
	}

	@Override
	public void start(View view) {
		initIntent();
		findViewById(view);
		setListener();
		setTitle();
		initData();
		refreshData(RefreshType.RefreshPull);
	}

	@Override
	public void start() {
	}

	@Override
	public void initIntent() {
	}

	@Override
	public void findViewById(View view) {
	}

	@Override
	public void findViewById() {
	}

	@Override
	public void setListener() {
	}

	@Override
	public void setTitle() {
	}

	@Override
	public void initData() {

	}


    @Override
    public void refreshData() {
        
    }

	
	public void refreshData(RefreshType refreshType) {
	}

	@Override
	public void goActivity() {
	}

	/**
	 * 更具是否登陆跳转到别的activity,未登录直接跳转到Login
	 */
	public void goActivity(Class<?> cls) {
		Intent intent = null;
		if (isLogin) {
			intent = new Intent(getActivity(), cls);
		} else {
			intent = new Intent(getActivity(), LoginActivity.class);
		}
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHttpUtil.closeRequest();
	}

}

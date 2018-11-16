package com.wtjr.lqg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.basecommonly.BaseAppManager;
import com.wtjr.lqg.lock.widget.GestureContentView;
import com.wtjr.lqg.lock.widget.GestureDrawline.GestureCallBack;
import com.wtjr.lqg.sharedpreferences.SaveGesturePasswordUtil;
import com.wtjr.lqg.sharedpreferences.SaveLoginState;
import com.wtjr.lqg.sharedpreferences.ToggleGesturePasswordState;
import com.wtjr.lqg.sharedpreferences.ToggleGestureTrackState;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HeadChangeUtil;
import com.wtjr.lqg.utils.LoginOrExitUtils;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.widget.CircleImageView;

/**
 * 
 * 手势绘制/校验界面
 * 
 */
public class GestureVerifyActivity extends BaseActivity implements OnClickListener {
    /** 手机号码 */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /** 意图 */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    private RelativeLayout mTopLayout;
    private TextView mTextTitle;
    /**
     * 圆形头像
     */
    private CircleImageView mCivHead;
    private TextView mTextPhoneNumber;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextForget;
    private String mParamPhoneNumber;
    private long mExitTime = 0;
    private int mParamIntentCode;
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 密码错误次数
     */
    private int mErrorCount = 0;
    /**
     * 是否是关闭手势密码
     */
    private boolean isCloseGesture = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_gesture_verify);
	start();
    }

    @Override
    public void initIntent() {
	mParamPhoneNumber = getIntent().getStringExtra(PARAM_PHONE_NUMBER);
	mParamIntentCode = getIntent().getIntExtra(PARAM_INTENT_CODE, 0);
	isCloseGesture = getIntent().getBooleanExtra("CloseGesture", false);
    }

    @Override
    public void findViewById() {
	tvTitleName = (TextView) findViewById(R.id.tv_title_name);
	imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

	mTopLayout = (RelativeLayout) findViewById(R.id.top_layout);
	mTextTitle = (TextView) findViewById(R.id.text_title);
	mCivHead = (CircleImageView) findViewById(R.id.civ_headPicture);
	mTextPhoneNumber = (TextView) findViewById(R.id.text_phone_number);
	mTextTip = (TextView) findViewById(R.id.text_tip);
	mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
	mTextForget = (TextView) findViewById(R.id.tv_forget_gesture);

    }

    @Override
    public void setListener() {
	imgBtnBack.setOnClickListener(this);
	mTextForget.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
	// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
	if (isCloseGesture) {
	    imgBtnBack.setVisibility(View.VISIBLE);
	}
	// imgBtnBack.setImageResource(R.drawable.close);
	// 设置名字为:绘制解锁密码
	tvTitleName.setText(R.string.gesture_verify_name);
    }

    @Override
    public void initData() {
	if (isLogin && app.mAccountData != null) {
	    HeadChangeUtil.requestHeadImage(app, mCivHead);
	    mTextPhoneNumber.setText(StringUtil.setBlurryPhone(mCurrentPhone));
	}

	if (isCloseGesture) {
	    mTextForget.setVisibility(View.GONE);
	}

	boolean gestureTrack = ToggleGestureTrackState.getGestureTrack(app);
	String gesturePas = SaveGesturePasswordUtil.getGesturePas(app);
	// 初始化一个显示各个点的viewGroup
	mGestureContentView = new GestureContentView(this, true, gesturePas, new GestureCallBack() {

	    @Override
	    public void onGestureCodeInput(String inputCode) {

	    }

	    @Override
	    public void checkedSuccess() {
		if (isCloseGesture) {// 是要关闭手势锁
		    // 清楚手势锁
		    ToggleGesturePasswordState.closeGesturePassword(app);
		}

		mGestureContentView.clearDrawlineState(0L);

		startActivity(new Intent(GestureVerifyActivity.this, MainActivity.class));
		finish();
	    }

	    @Override
	    public void checkedFail() {
		// 统计错误次数
		mErrorCount++;
		mGestureContentView.clearDrawlineState(1000L);

		mTextTip.setVisibility(View.VISIBLE);
		mTextTip.setText(Html.fromHtml("<font color='#c70c1e'>密码错误  </font>" + mErrorCount + "<font color='#c70c1e'> 次</font>"));
		// 左右移动动画
		Animation shakeAnimation = AnimationUtils.loadAnimation(GestureVerifyActivity.this, R.anim.shake);
		mTextTip.startAnimation(shakeAnimation);

		if (mErrorCount > 4) {// 手势输入错误达到5次
		    DialogUtil.promptDialog(GestureVerifyActivity.this, DialogUtil.HEAD_BAND_BANK, "手势密码已失效", "请重新登录零钱罐，登录后可在\"我-设置\"中管理手势密码",
			    new OnClickYesListener() {
				@Override
				public void onClickYes() {
				    if (isCloseGesture) {// 是要关闭手势锁
					// 清楚手势锁
					ToggleGesturePasswordState.closeGesturePassword(app);
					// 退出登录状态
					LoginOrExitUtils.exitSuccess(GestureVerifyActivity.this, app);
				    } else {
					goActivity();
				    }
				}
			    });
		}
	    }
	}, gestureTrack);
	// 设置手势解锁显示到哪个布局里面
	mGestureContentView.setParentView(mGestureContainer);

    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.imgBtn_back:
	    finish();
	    // BaseAppManager.getAppManager().AppExit(app);
	    break;
	case R.id.tv_forget_gesture:
	    goActivity();
	    break;
	default:
	    break;
	}
    }

    @Override
    public void goActivity() {
	// 先关闭手势
	ToggleGesturePasswordState.closeGesturePassword(app);
	// 登陆状态变成退出
	SaveLoginState.unLogin(app);
	startActivity(new Intent(GestureVerifyActivity.this, HaveAcountsLoginActivity.class));
	finish();
    }

    /**
     * 物理返回按钮
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	    BaseAppManager.getAppManager().AppExit(app);
	}
	return false;
    }

}

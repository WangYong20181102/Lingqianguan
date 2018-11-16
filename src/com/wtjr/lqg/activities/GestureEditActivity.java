package com.wtjr.lqg.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.basecommonly.BaseAppManager;
import com.wtjr.lqg.lock.widget.GestureContentView;
import com.wtjr.lqg.lock.widget.LockIndicator;
import com.wtjr.lqg.lock.widget.GestureDrawline.GestureCallBack;
import com.wtjr.lqg.sharedpreferences.SaveGesturePasswordUtil;
import com.wtjr.lqg.sharedpreferences.ToggleGesturePasswordState;
import com.wtjr.lqg.utils.L;

/**
 * 创建手势密码界面
 */
public class GestureEditActivity extends BaseActivity implements
        OnClickListener {
    /**
     * 手机号码
     */
    public static final String PARAM_PHONE_NUMBER = "PARAM_PHONE_NUMBER";
    /**
     * 意图
     */
    public static final String PARAM_INTENT_CODE = "PARAM_INTENT_CODE";
    /**
     * 首次提示绘制手势密码，可以选择跳过
     */
    public static final String PARAM_IS_FIRST_ADVICE = "PARAM_IS_FIRST_ADVICE";
    // private TextView mTextTitle;
    // private TextView mTextCancel;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextReset;
    private String mParamSetUpcode = null;
    private String mParamPhoneNumber;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private String mConfirmPassword = null;
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
     * 下一步
     */
    private TextView tvNext;
    /**
     * 显示返回按钮和物理返回键有效果，反之false
     */
    private boolean isShowBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_edit);
        start();
    }

    @Override
    public void initIntent() {
        isShowBack = getIntent().getBooleanExtra("isShowBack", true);
    }

    @Override
    public void findViewById() {

        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvNext = (TextView) findViewById(R.id.tv_next);

        mTextReset = (TextView) findViewById(R.id.text_reset);
        mTextReset.setClickable(false);
        mLockIndicator = (LockIndicator) findViewById(R.id.lock_indicator);
        mTextTip = (TextView) findViewById(R.id.text_tip);
        mGestureContainer = (FrameLayout) findViewById(R.id.gesture_container);
    }

    @Override
    public void setListener() {
        tvNext.setOnClickListener(this);
        imgBtnBack.setOnClickListener(this);
        mTextReset.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
        //根据上个页面传递过来的内容来判断是否显示返回按钮
        if (isShowBack) {
            tvNext.setVisibility(View.GONE);
            imgBtnBack.setVisibility(View.VISIBLE);
        } else {
            tvNext.setVisibility(View.VISIBLE);
            imgBtnBack.setVisibility(View.GONE);
        }

        tvNext.setText(R.string.skip);
        tvNext.setTextColor(Color.parseColor("#ff9900"));
        // 设置名字为:设置手势密码
        tvTitleName.setText(R.string.gesture_edit_name);

    }

    @Override
    public void initData() {
        //手势颜色

        // 初始化一个显示各个点的viewGroup
        mGestureContentView = new GestureContentView(this, false, "",
                new GestureCallBack() {
                    @Override
                    public void onGestureCodeInput(String inputCode) {
                        if (!isInputPassValidate(inputCode)) {
                            mTextTip.setText(Html
                                    .fromHtml("<font color='#c70c1e'>最少链接4个点, 请重新输入</font>"));
                            mGestureContentView.clearDrawlineState(0L);
                            return;
                        }
                        if (mIsFirstInput) {
                            L.hintD("--------111111--------");

                            mFirstPassword = inputCode;
                            updateCodeList(inputCode);
                            mGestureContentView.clearDrawlineState(0L);
                            mTextReset.setClickable(true);
                            mTextReset.setText(getString(R.string.reset_gesture_code));

                            mIsFirstInput = false;
                        } else {
                            L.hintD("--------222222--------");

                            if (inputCode.equals(mFirstPassword)) {
                                L.hintD("--------333333--------");
                                //存储手势密码
                                SaveGesturePasswordUtil.setGesturePas(app, mFirstPassword);

                                mGestureContentView.clearDrawlineState(0L);
                                goActivity();
                            } else {
                                L.hintD("--------44444--------");

                                mTextTip.setText(Html
                                        .fromHtml("<font color='#c70c1e'>与上一次绘制不一致，请重新绘制</font>"));
                                // 左右移动动画
                                Animation shakeAnimation = AnimationUtils
                                        .loadAnimation(
                                                GestureEditActivity.this,
                                                R.anim.shake);
                                mTextTip.startAnimation(shakeAnimation);
                                // 保持绘制的线，1.5秒后清除
                                mGestureContentView.clearDrawlineState(1500);

                                shakeAnimation.setAnimationListener(new AnimationListener() {

                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                        mIsFirstInput = true;
                                        mFirstPassword = "";
                                        updateCodeList("");
                                        mTextTip.setText(getString(R.string.set_gesture_pattern));
                                    }
                                });
                            }
                        }
//						mIsFirstInput = false;
                    }

                    @Override
                    public void checkedSuccess() {
                        goActivity();
                    }

                    @Override
                    public void checkedFail() {

                    }
                }, true);
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(mGestureContainer);
        updateCodeList("");

    }

    private void updateCodeList(String inputCode) {
        // 更新选择的图案
        mLockIndicator.setPath(inputCode);
    }

    /**
     * 物理返回按钮
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //就是关闭手势
            ToggleGesturePasswordState.closeGesturePassword(app);
            if (isShowBack) {
                //返回上一页
                finish();
            } else {
                //双击退出
                BaseAppManager.getAppManager().exit(GestureEditActivity.this);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back://返回
                finish();
                break;
            case R.id.tv_next://跳过
                //就是关闭手势
                ToggleGesturePasswordState.closeGesturePassword(app);
                goActivity();
                break;
            case R.id.text_reset://重新绘制
                mIsFirstInput = true;
                updateCodeList("");
                mTextTip.setText(getString(R.string.set_gesture_pattern));
                break;
            default:
                break;
        }
    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }


    @Override
    public void goActivity() {
        //注册进来的，先关闭所有activity,在进行跳转，那么MainActivity就能重新初始化
        if (!isShowBack) {
            Intent intent = new Intent(GestureEditActivity.this, MainActivity.class);
            startActivity(intent);
            BaseAppManager.getAppManager().finishAllActivity();
        }
        finish();
    }

}

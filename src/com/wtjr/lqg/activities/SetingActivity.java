package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.ToggleGesturePasswordState;
import com.wtjr.lqg.sharedpreferences.ToggleGestureTrackState;
import com.wtjr.lqg.sharedpreferences.ToggleVoiceState;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.LoginOrExitUtils;
import com.wtjr.lqg.utils.NetworkUtil;
import com.wtjr.lqg.utils.PermissionUtils;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.SystemUtil;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.UsuallyLayoutItem;
import com.wtjr.lqg.widget.toggle.ToggleButton;
import com.wtjr.lqg.widget.toggle.ToggleButton.OnToggleChanged;

/**
 * 设置
 *
 * @author Myf
 */
public class SetingActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 登录手机号
     */
    private UsuallyLayoutItem uliLoginPhone;
    /**
     * 登录密码
     */
    private UsuallyLayoutItem uliLoginPassword;
    /**
     * 支付密码
     */
    private UsuallyLayoutItem uliPayPassword;
    /**
     * 通知设置
     */
    private UsuallyLayoutItem uliNotifySet;

    /**
     * 版本更新
     */
    private UsuallyLayoutItem uliVersionUpdate;
    /**
     * 应用市场下载
     */
    private UsuallyLayoutItem uliDownload;
    /**
     * 手势密码
     */
    private ToggleButton tbGesturePassword;
    /**
     * 手势轨迹
     */
    private ToggleButton tbGesturesTrack;
    /**
     * 用来控制显示还是隐藏手势控件
     */
    private RelativeLayout rlGesturesTrack;
    /**
     * 声音
     */
    private ToggleButton tbVoice;
    /**
     * 退出
     */
    private Button btnExit;
    private Dialog dialog;
    /**
     * 应用市场下载
     */
    public static boolean bApplicationMarketDownload = false;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_seting);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        uliLoginPhone = (UsuallyLayoutItem) findViewById(R.id.uli_login_phone);
        uliLoginPassword = (UsuallyLayoutItem) findViewById(R.id.uli_login_password);
        uliPayPassword = (UsuallyLayoutItem) findViewById(R.id.uli_pay_password);
        uliNotifySet = (UsuallyLayoutItem) findViewById(R.id.uli_notify_set);
        uliVersionUpdate = (UsuallyLayoutItem) findViewById(R.id.uli_version_update);
        uliDownload = (UsuallyLayoutItem) findViewById(R.id.uli_download);
        // 手势密码
        tbGesturePassword = (ToggleButton) findViewById(R.id.tb_gesture_password);
        tbGesturesTrack = (ToggleButton) findViewById(R.id.tb_gestures_track);
        rlGesturesTrack = (RelativeLayout) findViewById(R.id.rl_gestures_track);
        tbVoice = (ToggleButton) findViewById(R.id.tb_voice);
        // 退出
        btnExit = (Button) findViewById(R.id.btn_exit);

    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 登录手机号
        uliLoginPhone.setOnClickListener(this);
        // 登录密码
        uliLoginPassword.setOnClickListener(this);
        // 支付密码
        uliPayPassword.setOnClickListener(this);
        //通知设置
        uliNotifySet.setOnClickListener(this);
        // 版本更新
        uliVersionUpdate.setOnClickListener(this);
        uliDownload.setOnClickListener(this);
        // 退出
        btnExit.setOnClickListener(this);
        toggleChangedListener();

        httpUtil.setHttpRequesListener(this);
    }


    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:设置
        tvTitleName.setText(R.string.seting_name);
    }

    @Override
    public void initData() {
        uliLoginPhone.setTextTvContentR(StringUtil.setBlurryPhone(mCurrentPhone), 0);

        uliVersionUpdate.setTextTvContentR("当前版本:" + SystemUtil.getAppVersionName(this), 0);

        //去除左侧的隐藏图片
        uliVersionUpdate.setImageContentrResourceL(0);
        uliDownload.setImageContentrResourceL(0);
        uliLoginPhone.setImageContentrResourceL(0);
        uliLoginPassword.setImageContentrResourceL(0);
        uliPayPassword.setImageContentrResourceL(0);
        uliNotifySet.setImageContentrResourceL(0);
    }

    @Override
    protected void onStart() {
        setToggleState();
        super.onStart();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.uli_login_phone:// 登录手机号
                intent = new Intent(SetingActivity.this, ChangePhoneNumberActivity.class);
                startActivity(intent);
                break;
            case R.id.uli_login_password:// 登录密码
                intent = new Intent(SetingActivity.this, PasswordSetActivity.class);
                startActivity(intent);
                break;
            case R.id.uli_pay_password:// 支付设置
                if (TextUtils.isEmpty(app.mAccountData.bankNum)) {//没绑卡
                    DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "温馨提示", "您还未绑卡,请先去绑卡充值!", new OnClickYesListener() {
                        @Override
                        public void onClickYes() {
                            finish();
                            Intent intent = new Intent(SetingActivity.this, RechargeActivity.class);
                            intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                            startActivity(intent);
                        }
                    }).setCancelable(true);
                    return;
                }
                intent = new Intent(SetingActivity.this, PaySetActivity.class);
                startActivity(intent);
                break;
            case R.id.uli_notify_set:// 通知设置
                intent = new Intent(this, NotfiySetActivity.class);
                startActivity(intent);
                break;
            case R.id.uli_version_update:// 版本更新
                /**
                 * 动态获取6.0版本以上手机存储权限
                 */
                PermissionUtils.writerReadSDcard(this);
                app.isUpdate = true;
                sendVersionRequest();
                break;
            case R.id.uli_download:// 应用市场下载
                if (bApplicationMarketDownload){
                    intent = new Intent(this, WebViewActivity.class);
                    intent.putExtra("url", Constant.LQB_ANDROIDMARKET);
                    intent.putExtra("TitleName", "应用市场下载");
                    startActivity(intent);
                }else{
                    ToastUtil.showToastShort(this, "已是最新版本");
                }

                break;
            case R.id.btn_exit:// 退出
                DialogUtil.selectDialog(this, DialogUtil.HEAD_REGISTER, null, "是否立即退出", "是", new OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        LoginOrExitUtils.exitSuccess(SetingActivity.this, app);
                    }
                }, "否", null);
                break;
            default:
                break;
        }

    }

    /**
     * 设置开关状态
     */
    private void setToggleState() {
        //获得开关密码的状态
        boolean gesturePassword = ToggleGesturePasswordState.getGesturePassword(app);
        if (gesturePassword) {
            tbGesturePassword.setToggleOn();
            rlGesturesTrack.setVisibility(View.VISIBLE);
        } else {
            tbGesturePassword.setToggleOff();
            rlGesturesTrack.setVisibility(View.GONE);
        }

        // 手势轨迹的开关状态
        boolean gestureTrack = ToggleGestureTrackState.getGestureTrack(app);
        if (gestureTrack) {
            tbGesturesTrack.setToggleOn();
        } else {
            tbGesturesTrack.setToggleOff();
        }

        // 声音的开关状态
        boolean voice = ToggleVoiceState.getVoice(app);
        if (voice) {
            tbVoice.setToggleOn();
        } else {
            tbVoice.setToggleOff();
        }

    }

    /**
     * 开关状态变化监听
     */
    private void toggleChangedListener() {
        // 手势密码
        tbGesturePassword.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                if (on) {
                    goGestureActivity();
                } else {
//				    showEditTextDialog();
                    Intent intent = new Intent(SetingActivity.this, GestureVerifyActivity.class);
                    intent.putExtra("CloseGesture", true);

                    startActivity(intent);
                }
            }
        });
        //手势轨迹
        tbGesturesTrack.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                if (on) {//打开轨迹
                    ToggleGestureTrackState.openGestureTrack(app);
                } else {//关闭轨迹
                    ToggleGestureTrackState.closeGestureTrack(app);
                }
            }
        });

        //声音
        tbVoice.setOnToggleChanged(new OnToggleChanged() {

            @Override
            public void onToggle(boolean on) {
                if (on) {//打开声音
                    ToggleVoiceState.openVoice(app);
                } else {//关闭声音
                    ToggleVoiceState.closeVoice(app);
                }
            }
        });
    }


    /**
     * 显示输入对话框
     */
    private void showEditTextDialog() {
        dialog = new Dialog(SetingActivity.this, R.style.dialog);
        View view = LayoutInflater.from(SetingActivity.this).inflate(R.layout.dialog_edittext, null);
        Button bt_yesSend = (Button) view.findViewById(R.id.bt_yesSend);
        Button bt_noSend = (Button) view.findViewById(R.id.bt_noSend);
        final EditText edContext = (EditText) view.findViewById(R.id.ed_context);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();

        //确定按钮
        bt_yesSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();

                String password = edContext.getText().toString();// 得到了输入框里面的密码，如果密码是正确的就关闭

                //判断密码格式是否是空
                if (TextUtils.isEmpty(password)) {
                    DialogUtil.promptDialog(SetingActivity.this, DialogUtil.HEAD_LOGIN, "密码不能为空", new OnClickYesListener() {

                        @Override
                        public void onClickYes() {
                            dialog.show();
                        }
                    }).setCancelable(false);
                    return;
                }

                //判断密码格式是否错误
                if (!StringUtil.isPassword(password)) {
                    DialogUtil.promptDialog(SetingActivity.this, DialogUtil.HEAD_LOGIN, getString(R.string.password_format_error), new OnClickYesListener() {

                        @Override
                        public void onClickYes() {
                            dialog.show();
                        }
                    }).setCancelable(false);
                    return;
                }

                if (!NetworkUtil.isNetworkAvailable(SetingActivity.this)) {
                    DialogUtil.promptDialog(SetingActivity.this, DialogUtil.HEAD_BAND_BANK, "当前网络不可用，\n请检查你的网络设置");
                    return;
                }

                sendVerifyLoginPasswordRequest(RefreshType.RefreshNoPull, password);
            }
        });

        //取消按钮
        bt_noSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tbGesturePassword.setToggleOn();
                dialog.dismiss();
            }
        });
    }


    /**
     * 请求校验登录密码
     *
     * @param refreshType
     * @param password
     */
    public void sendVerifyLoginPasswordRequest(RefreshType refreshType, String password) {
        RequestParams params = new RequestParams(Constant.USERINFO_USERLOGIN);
        params.addBodyParameter("login_phone", mCurrentPhone);
        params.addBodyParameter("login_password", password);
        httpUtil.sendRequest(params, refreshType, this);
    }


    /**
     * 版本检测请求
     */
    public void sendVersionRequest() {
        RequestParams params = new RequestParams(Constant.USERINFO_QUERYLATESTVERSION);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);
    }


    /**
     * 去手势页面
     */
    private void goGestureActivity() {
        Intent intent = new Intent(SetingActivity.this, GestureEditActivity.class);
        intent.putExtra("isShowBack", true);
        startActivity(intent);
    }


    @Override
    public void onFailure(String url, String errorContent) {
        if (url.equals(Constant.USERINFO_USERLOGIN)) {//验证登录密码成功失败
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent, new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    //登录密码错误，需从新显示密码校验对话框
                    if (dialog != null) {
                        dialog.show();
                    } else {
                        showEditTextDialog();
                    }
                }
            });
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.USERINFO_USERLOGIN)) {//验证登录密码成功
            //隐藏手势轨迹那条栏
            rlGesturesTrack.setVisibility(View.GONE);
            //清楚手势锁
            ToggleGesturePasswordState.closeGesturePassword(app);
        } else if (url.equals(Constant.USERINFO_QUERYLATESTVERSION)) {//版本检测

        }
    }
}
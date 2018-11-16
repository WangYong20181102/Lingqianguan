package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.AnimationUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HeadChangeUtil;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CircleImageView;
import com.wtjr.lqg.widget.CircleProgress;
import com.wtjr.lqg.widget.paykeyboard.PayDialog;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView.OnPayListener;

import static com.umeng.socialize.utils.DeviceConfig.context;
import static com.wtjr.lqg.R.id.pay_password_error_text;

/**
 * 更换手机号
 *
 * @author Myf
 */
public class ChangePhoneNumberActivity extends BaseActivity implements OnClickListener, HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 手机号码
     */
    private TextView tvPhone;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 更换手机号点击事件
     */
    private Button btnChangePhone;
    private CircleImageView civHeadPicture;
    private PayDialog payDialog;
    private LinearLayout llPasswordCircle;
    /**
     * 数字密码键盘
     */
    private LinearLayout payKeyBoard;
    private LinearLayout payPasswordErrorProblem;
    private PayPasswordView payPasswordView;
    private LinearLayout llCircleprogress;
    private CircleProgress circleProgress;
    private TextView payPasswordError;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_change_phone_number);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvPhone = (TextView) findViewById(R.id.tv_phone);
        btnChangePhone = (Button) findViewById(R.id.button1);

        civHeadPicture = (CircleImageView) findViewById(R.id.civ_headPicture);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 更换手机号按钮监听
        btnChangePhone.setOnClickListener(this);

        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:更换手机号
        tvTitleName.setText(R.string.change_phone_number_name);
        // 设置当前手机号
        tvPhone.setText(StringUtil.setBlurryPhone(mCurrentPhone));
    }

    @Override
    public void initData() {
        HeadChangeUtil.requestHeadImage(app, civHeadPicture);

        if (TextUtils.isEmpty(app.mAccountData.bankNum)) {// 没绑卡
            btnChangePhone.setVisibility(View.GONE);
            return;
        }
        sendIsSetPayPasswordRequest(RefreshType.RefreshNoPull);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.button1:// 更换手机号点击操作
//                startActivity(new Intent(ChangePhoneNumberActivity.this,ChangeBankObligatePhoneNumActivity.class));
                showPayDialog();
                break;

            default:
                break;
        }
    }

    /**
     * 支付密码密码输入对话框
     */
    private void showPayDialog() {
        View view = getDecorViewDialog();
        llPasswordCircle = (LinearLayout) view.findViewById(R.id.ll_passwordCircle);
        TextView tvPaymentValue = (TextView) view.findViewById(R.id.tv_paymentValue);
        TextView payType = (TextView) view.findViewById(R.id.tv_payType);
        payPasswordError = (TextView)view.findViewById(R.id.pay_password_error_text);

        llCircleprogress = (LinearLayout) view.findViewById(R.id.ll_circleprogress);
        circleProgress = (CircleProgress) view.findViewById(R.id.circleprogress);
        payKeyBoard = (LinearLayout) view.findViewById(R.id.keyboard);
        payPasswordErrorProblem = (LinearLayout) view.findViewById(R.id.pay_error_problem_linear);
        payKeyBoard.setVisibility(View.VISIBLE);
        payPasswordErrorProblem.setVisibility(View.GONE);

        payType.setVisibility(View.GONE);
        tvPaymentValue.setVisibility(View.GONE);

        if (payDialog == null || !payDialog.isShowing()) {
            payDialog = new PayDialog(this, view);
        }
        payDialog.show();
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation = AnimationUtils.loadAnimation(ChangePhoneNumberActivity.this, R.anim.anim_set_in_down_up);
        payKeyBoard.startAnimation(animation);
    }

    private View getDecorViewDialog() {
        payPasswordView = PayPasswordView.getInstance(this, new OnPayListener() {

            @Override
            public void onSurePay(final String password) {
                payKeyBoard.setVisibility(View.GONE);
                llCircleprogress.setVisibility(View.VISIBLE);
                /**
                 * 开始验证验证码的进度动画
                 */
                payPasswordView.startProgressAnimator();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendOldPayPasswordVerificationRequest(password, RefreshType.RefreshNoPull);
                    }
                }, 1000);


            }

            @Override
            public void onCancelPay() {
                payDialog.dismiss();
                payDialog = null;
            }

            @Override
            public void onContent(String content) {
                String password = content.toString();
                int length = password.length();

                for (int i = 0; i < 6; i++) {
                    llPasswordCircle.getChildAt(i).setVisibility(View.INVISIBLE);
                }

                for (int i = 0; i < length; i++) {
                    llPasswordCircle.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }
        });
        return payPasswordView.getView();
    }

    /**
     * 判支付密码是否正确
     */
    public void sendOldPayPasswordVerificationRequest(String password, RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_VERIFICATIONUSEDPAYMENTCODE);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("payPassword", password);
        params.addBodyParameter("code_type", "1");
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 判断是否设置过支付密码
     */
    public void sendIsSetPayPasswordRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 发送短信请求
     */
    public void sendSMSRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.USERINFO_SENDVERIFYCODE);
        params.addBodyParameter("login_phone", mCurrentPhone);
        params.addBodyParameter("optType", "5");
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (url.equals(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE)) {// 判断是否设置过支付密码
            btnChangePhone.setVisibility(View.GONE);
        } else if (url.equals(Constant.SETTINGUP_VERIFICATIONUSEDPAYMENTCODE)){
            circleProgress.stop();
            if (payDialog != null) {
                payDialog.dismiss();
                payDialog = null;
            }
            if (errorContent.contains("锁定")){
                DialogUtil.selectDialog(ChangePhoneNumberActivity.this, DialogUtil.HEAD_LOGIN, "", errorContent, "找回密码", new DialogUtil.OnClickYesListener() {

                    @Override
                    public void onClickYes() {
                        startActivity(new Intent(ChangePhoneNumberActivity.this, PayBackPasswordActivity.class));
                    }
                }, "取消", new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
            }else{
                DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
            }
        }else {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE)) {// 判断是否设置过支付密码
            JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            // true有设置过支付密码，false没有设置过支付密码
            Boolean isPayPassword = jsonObject.getBoolean("payPasswordisExist");

            if (!isPayPassword) {
                btnChangePhone.setVisibility(View.GONE);
            }
        } else if (url.equals(Constant.SETTINGUP_VERIFICATIONUSEDPAYMENTCODE)) {// 支付密码正确
            JSONObject parseObject = JSON.parseObject(jsonBase.getDisposeResult());
            String payPasswordisRight = parseObject.getString("payPasswordisRight");
            String strPayPasswordError = parseObject.getString("pwdErrMsg");

            if (payPasswordisRight.equals("1") || payPasswordisRight.equals("true")) {
                payKeyBoard.setVisibility(View.GONE);
                /**
                 * 结束验证验证码的进度动画
                 */
                payPasswordView.endProgressAnimator(false);
                circleProgress.setAnimatorStatusListener(new CircleProgress.AddAnimatorEndListener() {
                    @Override
                    public void onAnimatorEnd(boolean animatorEnd) {
                        if (payDialog != null) {
                            payDialog.dismiss();
                            payDialog = null;
                            goActivity();
                        }
                    }
                });

            } else if(payPasswordisRight.equals("0") || payPasswordisRight.equals("false")){
                //停掉验证的所有动画状态设置回初始状态
                circleProgress.stop();
                llCircleprogress.setVisibility(View.GONE);
                payKeyBoard.setVisibility(View.GONE);
                payPasswordErrorProblem.setVisibility(View.VISIBLE);

                payPasswordError.setText(strPayPasswordError);
                payPasswordView.clearPassword();
                llPasswordCircle.setBackgroundResource(R.drawable.password_textfield_error);
                llPasswordCircle.startAnimation(AnimationUtil.startShakeAnimation(ChangePhoneNumberActivity.this));
//                DialogUtil.promptDialog(this, DialogUtil.HEAD_TOP_UP, "支付密码错误");

            }else if(payPasswordisRight.equals("3")){
                circleProgress.stop();
                if (payDialog != null) {
                    payDialog.dismiss();
                    payDialog = null;
                }
                DialogUtil.selectDialog(ChangePhoneNumberActivity.this, DialogUtil.HEAD_LOGIN,"",strPayPasswordError,"找回密码", new DialogUtil.OnClickYesListener() {

                    @Override
                    public void onClickYes() {
                        startActivity(new Intent(ChangePhoneNumberActivity.this, PayBackPasswordActivity.class));
                    }
                },"取消", new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
            }
        }
    }

    @Override
    public void goActivity() {
        Intent intent = new Intent(ChangePhoneNumberActivity.this, ChangePhoneNewNumberActivity.class);
        startActivity(intent);
    }
}

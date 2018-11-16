package com.wtjr.lqg.widget.paykeyboard;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.PayBackPasswordActivity;
import com.wtjr.lqg.widget.CircleProgress;
import com.wtjr.lqg.widget.paykeyboard.KeyboardEnum.ActionEnum;

import static com.wtjr.lqg.R.id.view;

public class PayPasswordView implements OnClickListener, OnLongClickListener {

    //	@ViewInject(R.id.pay_keyboard_del)
    private LinearLayout del;

    //	@ViewInject(R.id.pay_keyboard_zero)
    private TextView zero;

    //	@ViewInject(R.id.pay_keyboard_one)
    private TextView one;

    //	@ViewInject(R.id.pay_keyboard_two)
    private TextView two;

    //	@ViewInject(R.id.pay_keyboard_three)
    private TextView three;

    //	@ViewInject(R.id.pay_keyboard_four)
    private TextView four;

    //	@ViewInject(R.id.pay_keyboard_five)
    private TextView five;

    //	@ViewInject(R.id.pay_keyboard_sex)
    private TextView sex;

    //	@ViewInject(R.id.pay_keyboard_seven)
    private TextView seven;

    //	@ViewInject(R.id.pay_keyboard_eight)
    private TextView eight;

    //	@ViewInject(R.id.pay_keyboard_nine)
    private TextView nine;

    //	@ViewInject(R.id.bt_closePayment)
    private ImageButton closePay;

    private TextView resumeLoad;

    private ArrayList<String> mList = new ArrayList<String>();
    private View mView;
    private OnPayListener listener;
    private LinearLayout payKeyBoard;
    private LinearLayout payPasswordErrorProblem;
    private Context context;
    private LinearLayout llPasswordCircle;
    private LinearLayout llCircleprogress;
    private CircleProgress Circleprogress;
    /**
     * 忘记密码
     */
    private TextView forgetPassword;
    /**
     * 验证成功文字
     */
    private TextView tvVerifySuccess;

    public PayPasswordView(Context mContext, OnPayListener listener) {
        getDecorView(mContext, listener);
    }

    public static PayPasswordView getInstance(Context mContext, OnPayListener listener) {
        return new PayPasswordView(mContext, listener);
    }

    public void getDecorView(Context mContext, OnPayListener listener) {
        this.listener = listener;
        this.context = mContext;
        mView = LayoutInflater.from(mContext).inflate(R.layout.pay_keyboard, null);

        llCircleprogress = (LinearLayout) mView.findViewById(R.id.ll_circleprogress);
        Circleprogress = (CircleProgress) mView.findViewById(R.id.circleprogress);
        tvVerifySuccess = (TextView) mView.findViewById(R.id.tv_verify_success);

        del = (LinearLayout) mView.findViewById(R.id.pay_keyboard_del);
        zero = (TextView) mView.findViewById(R.id.pay_keyboard_zero);
        one = (TextView) mView.findViewById(R.id.pay_keyboard_one);
        two = (TextView) mView.findViewById(R.id.pay_keyboard_two);
        three = (TextView) mView.findViewById(R.id.pay_keyboard_three);
        four = (TextView) mView.findViewById(R.id.pay_keyboard_four);
        five = (TextView) mView.findViewById(R.id.pay_keyboard_five);
        sex = (TextView) mView.findViewById(R.id.pay_keyboard_sex);
        seven = (TextView) mView.findViewById(R.id.pay_keyboard_seven);
        eight = (TextView) mView.findViewById(R.id.pay_keyboard_eight);
        nine = (TextView) mView.findViewById(R.id.pay_keyboard_nine);
        closePay = (ImageButton) mView.findViewById(R.id.bt_closePayment);
        resumeLoad = (TextView) mView.findViewById(R.id.resume_load);
        forgetPassword = (TextView) mView.findViewById(R.id.forget_password);
        payKeyBoard = (LinearLayout) mView.findViewById(R.id.keyboard);
        payPasswordErrorProblem = (LinearLayout) mView.findViewById(R.id.pay_error_problem_linear);
        llPasswordCircle = (LinearLayout) mView.findViewById(R.id.ll_passwordCircle);

        del.setOnClickListener(this);
        zero.setOnClickListener(this);
        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        sex.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        closePay.setOnClickListener(this);
        resumeLoad.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);

        del.setOnLongClickListener(this);
    }

    /**
     * 开始验证验证码的进度动画
     */
    public void startProgressAnimator() {
        llCircleprogress.setVisibility(View.VISIBLE);
        Circleprogress.startProgress(tvVerifySuccess);

    }

    /**
     * 结束验证验证码的进度动画
     *
     * @param isSet 下面是显示true =“设置成功”还是false =“验证成功”
     */
    public void endProgressAnimator(boolean isSet) {
        Circleprogress.checkDone(tvVerifySuccess);
        if (isSet) {
            tvVerifySuccess.setText("设置成功");
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_keyboard_del:
                parseActionType(KeyboardEnum.del);
                break;
            case R.id.pay_keyboard_zero:
                parseActionType(KeyboardEnum.zero);
                break;
            case R.id.pay_keyboard_one:
                parseActionType(KeyboardEnum.one);
                break;
            case R.id.pay_keyboard_two:
                parseActionType(KeyboardEnum.two);
                break;
            case R.id.pay_keyboard_three:
                parseActionType(KeyboardEnum.three);
                break;
            case R.id.pay_keyboard_four:
                parseActionType(KeyboardEnum.four);
                break;
            case R.id.pay_keyboard_five:
                parseActionType(KeyboardEnum.five);
                break;
            case R.id.pay_keyboard_sex:
                parseActionType(KeyboardEnum.sex);
                break;
            case R.id.pay_keyboard_seven:
                parseActionType(KeyboardEnum.seven);
                break;
            case R.id.pay_keyboard_eight:
                parseActionType(KeyboardEnum.eight);
                break;
            case R.id.pay_keyboard_nine:
                parseActionType(KeyboardEnum.nine);
                break;
            case R.id.resume_load:
                clearPassword();
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_set_in_down_up);
                payKeyBoard.startAnimation(animation);
                payKeyBoard.setVisibility(View.VISIBLE);
                llPasswordCircle.setBackgroundResource(R.drawable.password_textfield);
                payPasswordErrorProblem.setVisibility(View.GONE);
                break;
            case R.id.forget_password:
                context.startActivity(new Intent(context, PayBackPasswordActivity.class));

                break;
            case R.id.bt_closePayment:  //关闭支付键盘
                if (listener != null) {
                    listener.onCancelPay();
                }
                break;
            default:
                break;
        }
    }

    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        parseActionType(KeyboardEnum.longdel);
        return false;
    }

    private void parseActionType(KeyboardEnum type) {
        if (type.getType() == ActionEnum.add) {
            if (mList.size() < 6) {
                mList.add(type.getValue());
            }
        } else if (type.getType() == ActionEnum.delete) {
            if (mList.size() > 0) {
                mList.remove(mList.get(mList.size() - 1));
            }
        } else if (type.getType() == ActionEnum.longClick) {
            mList.clear();
        }

        String payValue = "";
        for (int i = 0; i < mList.size(); i++) {
            payValue += mList.get(i);
        }
        listener.onContent(payValue);

        if (mList.size() == 6) {
            listener.onSurePay(payValue);//确认支付
        }
    }

    public interface OnPayListener {
        void onCancelPay();

        void onSurePay(String password);

        void onContent(String content);
    }

    public View getView() {
        return mView;
    }

    /**
     * 清除密码
     */
    public void clearPassword() {
        mList.clear();
        String payValue = "";
        for (int i = 0; i < mList.size(); i++) {
            payValue += mList.get(i);
        }
        listener.onContent(payValue);
    }

}

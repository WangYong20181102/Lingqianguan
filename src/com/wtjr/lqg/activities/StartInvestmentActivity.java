package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.ActivityData;
import com.wtjr.lqg.base.Investment;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.NewBorrow;
import com.wtjr.lqg.base.PlanMasData;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.PayPasswordType;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.enums.StartInvestmentType;
import com.wtjr.lqg.utils.AnimationUtil;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickNoListener;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.RSAUtils;
import com.wtjr.lqg.widget.CircleProgress;
import com.wtjr.lqg.widget.MoneyEditText;
import com.wtjr.lqg.widget.MoneyEditText.OnTextChangeListener;
import com.wtjr.lqg.widget.paykeyboard.PayDialog;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView;
import com.wtjr.lqg.widget.paykeyboard.PayPasswordView.OnPayListener;

/**
 * 开始投资
 *
 * @author JoLie
 */
public class StartInvestmentActivity extends BaseActivity implements
        OnClickListener, HttpRequesListener, OnTextChangeListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;

    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;

    /**
     * 确定按钮
     */
    private Button btOk;

    /**
     * 金钱输入框
     */
    private MoneyEditText etInvestMoney;

    /**
     * 投资类型
     */
    private StartInvestmentType mInvestmentType;

    private Investment mInvestment;

    /**
     * 用户余额
     */
    private TextView tvBalanceHint;

    /**
     * 预计收益值
     */
    private TextView tvAnticipatedIncomeValue;

    /**
     * 可投金额
     */
    private TextView tvResidueMoney;


    private LinearLayout llPasswordCircle;

    private PayDialog payDialog;
    /**
     * 全投
     */
    private Button btAllCast;
    /**
     * 最大可投金额
     */
    private double accountMoney;
    /**
     * 投资金额
     */
    private String mInvestMoney;

    private String mTitleZP = " ";
    /**
     * 判断是新手标（2）还是定期标(1)还是周周升（3）
     */
    private int borrowState;
    /**
     * 是从哪个地方跳转投资详情
     */
    private InvestmentDetailsType mInvestmentDetailsType;
    private LinearLayout llCircleprogress;
    private CircleProgress circleProgress;
    private LinearLayout payKeyBoard;
    private LinearLayout payPasswordErrorProblem;
    private PayPasswordView payPasswordView;
    private TextView payPasswordError;
    private NewBorrow newBorrow;
    /**
     * 周周升
     */
    private PlanMasData planMasData;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_start_investment);
        start();
    }


    @Override
    public void initIntent() {
        borrowState = getIntent().getIntExtra("borrowState", 1);
        if (borrowState == 2) {//新手标过来
            newBorrow = (NewBorrow) getIntent().getSerializableExtra(Investment.class.getName());
        } else if (borrowState == 1) {
            mInvestment = (Investment) getIntent().getSerializableExtra(Investment.class.getName());
        } else if (borrowState == 3) {//周周升
            planMasData = (PlanMasData) getIntent().getSerializableExtra(Investment.class.getName());
        }
        mInvestmentType = (StartInvestmentType) getIntent().getSerializableExtra(StartInvestmentType.class.getName());
        mInvestmentDetailsType = (InvestmentDetailsType) getIntent().getSerializableExtra(InvestmentDetailsType.class.getName());
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        etInvestMoney = (MoneyEditText) findViewById(R.id.et_investMoney);
        // 确认按钮
        btOk = (Button) findViewById(R.id.bt_ok);

        tvBalanceHint = (TextView) findViewById(R.id.tv_balance_hint);

        tvAnticipatedIncomeValue = (TextView) findViewById(R.id.tv_anticipatedIncomeValue);

        tvResidueMoney = (TextView) findViewById(R.id.tv_residueMoney);

        btAllCast = (Button) findViewById(R.id.bt_allCast);

    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
        btOk.setOnClickListener(this);
        etInvestMoney.setOnTextChangeListener(this);

        btAllCast.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
        // 设置名字为:投资
        tvTitleName.setText(R.string.investment_money);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        accountMoney = 0;
        switch (mInvestmentType) {
            case Lqb:
                accountMoney = app.mAccountData.lqb;
                break;
            case Balance:
                accountMoney = app.mAccountData.available_money;
                break;
        }
        etInvestMoney.setInputMaxMoney(accountMoney);

        if (mInvestment != null && borrowState == 1) {
            if (accountMoney < mInvestment.bMiniMoney) {
                btOk.setText("我要充值");
                tvBalanceHint.setText("余额不足(" + MoneyFormatUtil.format(mInvestment.bMiniMoney) + "元起投)");
            } else {
                btOk.setEnabled(false);
                etInvestMoney.addTextChangedListener(new ButtonEnabledListener(btOk, etInvestMoney));
                btOk.setText("立即投资");
                tvBalanceHint.setText(MoneyFormatUtil.format(mInvestment.bMiniMoney) + "元起投");
            }


            // 计算剩余的可投金额
            double residueMoney = mInvestment.bLoanMoney - mInvestment.bInvestedMoney;
            tvResidueMoney.setText(MoneyFormatUtil.format(residueMoney));
            etInvestMoney.setHint(MoneyFormatUtil.format(accountMoney));
        }
        if (newBorrow != null && borrowState == 2) {
            if (accountMoney < newBorrow.bMiniMoney) {
                btOk.setText("我要充值");
                tvBalanceHint.setText("余额不足(" + MoneyFormatUtil.format(newBorrow.bMiniMoney) + "元起投)");
            } else {
                btOk.setEnabled(false);
                etInvestMoney.addTextChangedListener(new ButtonEnabledListener(btOk, etInvestMoney));
                btOk.setText("立即投资");
                tvBalanceHint.setText(MoneyFormatUtil.format(newBorrow.bMiniMoney) + "(元)起投,限投" + MoneyFormatUtil.format(newBorrow
                        .bHigMoney) + "(元)");
            }

            // 计算剩余的可投金额
            double residueMoney = newBorrow.bLoanMoney - newBorrow.bInvestedMoney;
            tvResidueMoney.setText(MoneyFormatUtil.format(residueMoney));
            etInvestMoney.setHint(MoneyFormatUtil.format(accountMoney));
        }
        if (planMasData != null && borrowState == 3) {
            if (accountMoney < planMasData.investMin) {
                btOk.setText("我要充值");
                tvBalanceHint.setText("余额不足(" + MoneyFormatUtil.format(planMasData.investMin) + "元起投)");
            } else {
                btOk.setEnabled(false);
                etInvestMoney.addTextChangedListener(new ButtonEnabledListener(btOk, etInvestMoney));
                btOk.setText("立即投资");
                tvBalanceHint.setText(MoneyFormatUtil.format(planMasData.investMin) + "(元)起投,限投" + MoneyFormatUtil.format(planMasData.investMax) +
                        "(元)");
            }

            // 计算剩余的可投金额
            double residueMoney = planMasData.preCollectAmount - planMasData.factCollectAmount;
            tvResidueMoney.setText(MoneyFormatUtil.format(residueMoney));
            etInvestMoney.setHint(MoneyFormatUtil.format(accountMoney));
        }

    }

    /**
     * 金额输入框的监听回调
     */
    @Override
    public void setTextChange(String text) {
        // 获得输入框中的金额
        double money = Double.parseDouble(text);
        // 计算收益值
        double income = 0;
        if (mInvestment != null && borrowState == 1) {
            income = (mInvestment.bYearRate / 100 * money) / 365 * mInvestment.bLoanDays;
        }
        if (newBorrow != null && borrowState == 2) {
            income = (newBorrow.bYearRate / 100 * money) / 365 * newBorrow.bLoanDays;
        }
        if (planMasData != null && borrowState == 3) {
            income = Double.parseDouble(MoneyFormatUtil.sEarnings4(planMasData.intPeriod,planMasData.unitCycle,money,planMasData.baseRate / 100,planMasData.maxRate / 100,planMasData.increaseRate / 100,0));
        }
        // 显示预计收益值
        tvAnticipatedIncomeValue.setText(MoneyFormatUtil.format(income));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.bt_ok:// 确定按钮
                if (btOk.getText().toString().equals("我要充值")) {
                    Intent intent = new Intent(this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    startActivity(intent);
                } else {
                    if (TextUtils.isEmpty(etInvestMoney.getText().toString())) {
                        DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "请输入投资金额");
                        return;
                    }

                    if (mInvestment != null && borrowState == 1) {
                        if (Double.parseDouble(etInvestMoney.getText().toString()) < mInvestment.bMiniMoney) {
                            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "投资金额不能小于" + MoneyFormatUtil.format(mInvestment.bMiniMoney) +
                                    "元");
                            return;
                        }
                    }
                    if (newBorrow != null && borrowState == 2) {
                        if (Double.parseDouble(etInvestMoney.getText().toString()) < newBorrow.bMiniMoney) {
                            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "投资金额不能小于" + MoneyFormatUtil.format(newBorrow.bMiniMoney) + "元");
                            return;
                        }
                        if (Double.parseDouble(etInvestMoney.getText().toString()) > newBorrow.bHigMoney) {
                            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "投资金额不能大于" + MoneyFormatUtil.format(newBorrow.bHigMoney) + "元");
                            return;
                        }
                    }
                    if (planMasData != null && borrowState == 3) {
                        if (Double.parseDouble(etInvestMoney.getText().toString()) < planMasData.investMin) {
                            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "投资金额不能小于" + MoneyFormatUtil.format(planMasData.investMin) +
                                    "元");
                            return;
                        }
                        if (Double.parseDouble(etInvestMoney.getText().toString()) > planMasData.investMax) {
                            DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, "投资金额不能大于" + MoneyFormatUtil.format(planMasData.investMax) +
                                    "元");
                            return;
                        }
                    }

                    sendIsSetPayPasswordRequest(RefreshType.RefreshNoPull);
                }
                break;
            case R.id.bt_allCast:// 全投
                etInvestMoney.setText(MoneyFormatUtil.format2(accountMoney));
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
        payPasswordError = (TextView) view.findViewById(R.id.pay_password_error_text);
        llCircleprogress = (LinearLayout) view.findViewById(R.id.ll_circleprogress);
        circleProgress = (CircleProgress) view.findViewById(R.id.circleprogress);
        payKeyBoard = (LinearLayout) view.findViewById(R.id.keyboard);
        payPasswordErrorProblem = (LinearLayout) view.findViewById(R.id.pay_error_problem_linear);
        payKeyBoard.setVisibility(View.VISIBLE);
        payPasswordErrorProblem.setVisibility(View.GONE);

        switch (mInvestmentType) {
            case Lqb:
                payType.setText("从零钱宝投资");
                break;
            case Balance:
                payType.setText("从可用余额投资");
                break;
        }

        tvPaymentValue.setText(MoneyFormatUtil.format(etInvestMoney.getText().toString()) + "元");

        if (payDialog == null || !payDialog.isShowing()) {
            payDialog = new PayDialog(StartInvestmentActivity.this, view);
        }
        payDialog.show();
        //通过加载XML动画设置文件来创建一个Animation对象；
        Animation animation = AnimationUtils.loadAnimation(StartInvestmentActivity.this, R.anim.anim_set_in_down_up);
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
                        if (planMasData != null && borrowState == 3) {
                            sendDingqiInvestmentRequest(RefreshType.RefreshNoPull, password);
                        } else {
                            sendInvestmentRequest(RefreshType.RefreshNoPull, password);
                        }
                    }
                }, 1000);

            }

            @Override
            public void onCancelPay() {
                payDialog.dismiss();
                payDialog = null;
//                //关闭网络请求
//                httpUtil.closeRequest();
            }

            @Override
            public void onContent(String content) {
                String password = content.toString();
                int length = password.length();

                for (int i = 0; i < 6; i++) {
                    llPasswordCircle.getChildAt(i)
                            .setVisibility(View.INVISIBLE);
                }

                for (int i = 0; i < length; i++) {
                    llPasswordCircle.getChildAt(i).setVisibility(View.VISIBLE);
                }
            }
        });
        return payPasswordView.getView();
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
     * 发送投资请求
     */
    public void sendInvestmentRequest(RefreshType refreshType, String password) {
        mInvestMoney = etInvestMoney.getText().toString();

        RequestParams params = new RequestParams(Constant.INVESTMENT_IMMEDIATEINVESTMENT);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("invest_money", mInvestMoney);
        params.addBodyParameter("invest_way", mInvestmentType.getParams());
        params.addBodyParameter("pay_password", password);
        if (mInvestment != null && borrowState == 1) {
            params.addBodyParameter("borrowId", mInvestment.bId + "");
        }
        if (newBorrow != null && borrowState == 2) {
            params.addBodyParameter("borrowId", newBorrow.bId + "");
        }

        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 发送投资请求
     */
    public void sendDingqiInvestmentRequest(RefreshType refreshType, String password) {
        mInvestMoney = etInvestMoney.getText().toString();

        RequestParams params = new RequestParams(Constant.PLANINVEST_IMMEDIATEINVESTMENT);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("invest_money", mInvestMoney);
        params.addBodyParameter("invest_way", mInvestmentType.getParams());
        params.addBodyParameter("pay_password", password);
        params.addBodyParameter("planId", planMasData.planId + "");
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (url.equals(Constant.INVESTMENT_IMMEDIATEINVESTMENT) || url.equals(Constant.PLANINVEST_IMMEDIATEINVESTMENT)) {
            circleProgress.stop();
            if (payDialog != null) {
                payDialog.dismiss();
                payDialog = null;
            }

            if (errorContent.contains("锁定")) {
                DialogUtil.selectDialog(StartInvestmentActivity.this, DialogUtil.HEAD_LOGIN, "", errorContent, "找回密码", new DialogUtil
                        .OnClickYesListener() {

                    @Override
                    public void onClickYes() {
                        startActivity(new Intent(StartInvestmentActivity.this, PayBackPasswordActivity.class));
                    }
                }, "取消", new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
            } else if (errorContent.contains("已下架")) {
                DialogUtil.promptDialog(StartInvestmentActivity.this, DialogUtil.HEAD_SERVICE, "温馨提示", errorContent, new DialogUtil
                        .OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        Intent intent = new Intent(StartInvestmentActivity.this, MainActivity.class);
                        startActivity(intent);
                        // 更新前3个界面
                        Intent mIntent = new Intent(Constant.UPDATE_ALL);
                        mIntent.putExtra("ShowFragmentLocation", 2);
                        sendBroadcast(mIntent, Manifest.permission.receiver_permission);
                    }
                });
            } else {
                DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
            }
        } else {
            if (errorContent.contains("已下架")) {
                DialogUtil.promptDialog(StartInvestmentActivity.this, DialogUtil.HEAD_SERVICE, "温馨提示", errorContent, new DialogUtil
                        .OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        Intent intent = new Intent(StartInvestmentActivity.this, MainActivity.class);
                        startActivity(intent);
                        // 更新前3个界面
                        Intent mIntent = new Intent(Constant.UPDATE_ALL);
                        mIntent.putExtra("ShowFragmentLocation", 2);
                        sendBroadcast(mIntent, Manifest.permission.receiver_permission);
                    }
                });
            } else {
                DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
            }
        }
    }


    @Override
    public void onSuccess(String url, final JSONBase jsonBase) {
        final JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        if (url.equals(Constant.SETTINGUP_ESTIMATEISPAYMENTCODE)) {// 是否设置过是否密码
            // true有设置过支付密码，false没有设置过支付密码
            Boolean isPayPassword = jsonObject.getBoolean("payPasswordisExist");

            if (!isPayPassword) {// 没设置过支付密码
                DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK,
                        "您还没有设置过支付密码", new OnClickYesListener() {
                            @Override
                            public void onClickYes() {
                                Intent intent = new Intent(
                                        StartInvestmentActivity.this,
                                        PayPasswordSetActivity.class);
                                intent.putExtra(
                                        PayPasswordType.class.getName(),
                                        PayPasswordType.PayPasswordNotHaveSet);
                                startActivity(intent);
                            }
                        });
            } else {// 设置过支付密码
                showPayDialog();
            }
        } else if (url.equals(Constant.INVESTMENT_IMMEDIATEINVESTMENT) || url.equals(Constant.PLANINVEST_IMMEDIATEINVESTMENT)) {// 投资请求
            String payPasswordisRight = jsonObject.getString("payPasswordisRight");
            String strPayPasswordError = jsonObject.getString("pwdErrMsg");
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
                            try {
                                //1是开启转盘了活动
                                int isOpen = jsonObject.getInteger("isOpen");
                                //抽奖次数
                                String chance = jsonObject.getString("chance");
                                int iChance = Integer.parseInt(chance);
                                //提示语
                                String chanceExplain = jsonObject.getString("chanceExplain");
                                //投资钱数
                                double investMoney = jsonObject.getDouble("investMoney");
                                //转盘活动地址
                                String urlZP = jsonObject.getString("url");
                                //转盘活动标题
                                mTitleZP = jsonObject.getString("title");

                                if (isOpen == 1 && iChance > 0 && !TextUtils.isEmpty(urlZP) && !TextUtils.isEmpty(chanceExplain)) {//显示转盘活动
                                    showZPActivityDialog("成功投资" + MoneyFormatUtil.format2(investMoney) + "元", chanceExplain, urlZP, jsonObject);
                                    return;
                                }
                            } catch (Exception e) {

                            }

                            DialogUtil.promptDialog(StartInvestmentActivity.this, DialogUtil.HEAD_BAND_BANK, jsonBase.getMessage(), new
                                    OnClickYesListener() {
                                        @Override
                                        public void onClickYes() {
                                            goActivity(null, null);
                                        }
                                    });
                        }
                    }
                });
            } else if (payPasswordisRight.equals("0") || payPasswordisRight.equals("false")) {
                //停掉验证的所有动画状态设置回初始状态
                circleProgress.stop();
                llCircleprogress.setVisibility(View.GONE);
                payKeyBoard.setVisibility(View.GONE);
                payPasswordErrorProblem.setVisibility(View.VISIBLE);
                payPasswordError.setText(strPayPasswordError);
                payPasswordView.clearPassword();
                llPasswordCircle.setBackgroundResource(R.drawable.password_textfield_error);
                llPasswordCircle.startAnimation(AnimationUtil.startShakeAnimation(StartInvestmentActivity.this));
            } else if (payPasswordisRight.equals("3")) {
                circleProgress.stop();
                if (payDialog != null) {
                    payDialog.dismiss();
                    payDialog = null;
                }
                DialogUtil.selectDialog(StartInvestmentActivity.this, DialogUtil.HEAD_LOGIN, "", strPayPasswordError, "找回密码", new DialogUtil
                        .OnClickYesListener() {

                    @Override
                    public void onClickYes() {
                        startActivity(new Intent(StartInvestmentActivity.this, PayBackPasswordActivity.class));
                    }
                }, "取消", new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
            }


        }
    }

    /**
     * 显示转盘活动的对话框
     *
     * @param dialogTitle   对话框标题
     * @param dialogContent 对话框内容
     * @param urlZP         活动链接
     */
    private void showZPActivityDialog(String dialogTitle, String dialogContent, final String urlZP, final JSONObject jsonObject1) {
        Dialog promptDialog = DialogUtil.selectDialog(this, DialogUtil.HEAD_BAND_BANK, dialogTitle, dialogContent,
                "去参加", new OnClickYesListener() {

                    @Override
                    public void onClickYes() {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("ukey", mUid);
                        jsonObject.put("code", Constant.ZP_CODE);

                        String sign = "";
                        try {
                            sign = RSAUtils.encrypt(jsonObject.toString(), Constant.ZP_ENCRYPT_KEY);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }

                        String url = Constant.INTERFACE_ADDRESS + "/" + urlZP + "?ukey=" + mUid + "&code=" + Constant.ZP_CODE + "&sign=" + sign;
                        goActivity(url, jsonObject1);
                    }
                }, "先等等", new OnClickNoListener() {

                    @Override
                    public void onClickNo() {
                        goActivity(null, null);
                    }
                });


        //标题
        TextView tvmTitle = (TextView) promptDialog.findViewById(R.id.tvm_title);
        tvmTitle.setTextColor(getResources().getColor(R.color.FC_999999));
        //内容
        TextView tvmContext = (TextView) promptDialog.findViewById(R.id.tvm_context);
        tvmContext.setTextColor(getResources().getColor(R.color.lqb_head_bg));
        tvmContext.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.s16));
        //确定按钮
        Button btYesSend = (Button) promptDialog.findViewById(R.id.bt_yesSend);
        btYesSend.setText("去抽奖");
    }


    /**
     * 投资成功后的跳转
     *
     * @param url 如果有则跳转到WebView
     */
    public void goActivity(final String url, final JSONObject jsonObject) {
        Intent intent = new Intent(StartInvestmentActivity.this, MainActivity.class);
        startActivity(intent);

        // 更新前3个界面
        Intent mIntent = new Intent(Constant.UPDATE_ALL);
        if (mInvestmentDetailsType == InvestmentDetailsType.MyInvestment) {//我的投资进入进行投资的
            mIntent.putExtra("ShowFragmentLocation", 0);
        } else {
            mIntent.putExtra("ShowFragmentLocation", 2);
        }
        sendBroadcast(mIntent, Manifest.permission.receiver_permission);

        if (!TextUtils.isEmpty(url)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ActivityData activityData = new ActivityData();
                    activityData.activeUrl = url;
                    activityData.activeTitle = mTitleZP;
                    activityData.activeFlag = jsonObject.getInteger("activeFlag");
                    //分享四要素
                    activityData.activeShareContent = jsonObject.getString("activeShareContent");
                    activityData.activeShareImage = jsonObject.getString("activeShareImage");
                    activityData.activeShareTitle = jsonObject.getString("activeShareTitle");
                    activityData.activeShareUrl = jsonObject.getString("activeShareUrl");

                    Intent intent2 = new Intent(StartInvestmentActivity.this, WebViewActivity.class);
                    intent2.putExtra(ActivityData.class.getName(), activityData);
                    startActivity(intent2);
                }
            }, 300);
        }
    }
}

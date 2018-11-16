package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.BankImageUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.CircleImageView;
import com.wtjr.lqg.widget.MoneyEditText;
import com.wtjr.lqg.widget.MoneyEditText.OnTextChangeListener;

/**
 * 充值页面
 */
public class RechargeActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnTextChangeListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 问题反馈
     */
    private ImageButton imgBtnNext;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;

    /**
     * 充值100或者1000默认的隐藏控件
     */
    private ViewStub vsSelectRecharge;
    /**
     * 银行的圆形图标
     */
    private CircleImageView civBank;
    /**
     * 银行的名字
     */
    private TextView tvBankName;
    /**
     * 银行的名字
     */
    private TextView tvBankCardNumber;
    /**
     * 充值金额的输入框
     */
    private MoneyEditText meRechargeMoney;
    /**
     * 点击下一步的按钮
     */
    private Button btnNext;
    /**
     * 显示充值的金额
     */
    private TextView tvRechargeMoney;
    /**
     * 显示或者隐藏体验金,服务器返回的状态
     */
    private int mChargeStatus = 3;
    /**
     * 显示充值100的勾
     */
    private ImageView imageSelect100;
    /**
     * 显示充值1000的勾
     */
    private ImageView imageSelect1000;
    /**
     * 本次充值渠道
     */
    private String mChargeChannel;
    /**
     * 单笔限额
     */
    private String singleLimitation;
    /**
     * 限额描述
     */
    private String description;
    /**
     * 充值类型(充到余额额还是零钱宝)
     */
    private RechargeType mRechargeType;
    /**
     * 充值金额
     */
    private String mRechargeMoney;
    /**
     * 充值类型(充到余额额还是零钱宝)
     */
    private TextView tvType;
    /**
     * 最小充值金额
     */
    private double mMinRechargeMoney = 100;
    /**
     * 新手活动充值100
     */
    private boolean isRecharge100 = false;
    private Intent intent;
    /**
     * 银行logo
     */
    private CircleImageView circleImageView;
    /**
     * 充值限额
     */
    private TextView tvRechargeQuota;
    private TextView tvBalance;
    private RelativeLayout reImageBankBackground;
    private ImageView circleBankIconPig;
    private TextView tvLqb;
    private TextView tvTopUp100;
    private TextView tvTopUp1000;
    private TextView tvTyj1000;
    private TextView tvTyj2000;
    private int showChangeStatus;
    /**
     * 各银行限额
     */
    private RelativeLayout rlLimitsTable;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_recharge);
        start();
    }

    @Override
    public void initIntent() {
        mRechargeType = (RechargeType) getIntent().getSerializableExtra(RechargeType.class.getName());
        isRecharge100 = getIntent().getBooleanExtra("IsRecharge100", false);
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);

        civBank = (CircleImageView) findViewById(R.id.civ_bank);
        tvRechargeMoney = (TextView) findViewById(R.id.tv_recharge_money);
        tvBankName = (TextView) findViewById(R.id.tv_bank_name);
        tvBankCardNumber = (TextView) findViewById(R.id.tv_bank_card_number);
        meRechargeMoney = (MoneyEditText) findViewById(R.id.me_recharge_money);
        circleImageView = (CircleImageView) findViewById(R.id.image_lqg);
        tvBalance = (TextView) findViewById(R.id.tv_balance);
        tvRechargeQuota = (TextView) findViewById(R.id.recharge_quota);
        circleBankIconPig = (ImageView) findViewById(R.id.image_lqg_pig);
        tvLqb = (TextView) findViewById(R.id.tv_balance_lqb);
        btnNext = (Button) findViewById(R.id.btn_next);

        reImageBankBackground = (RelativeLayout) findViewById(R.id.re_image_bank_background);

        vsSelectRecharge = (ViewStub) findViewById(R.id.vs_select_recharge);

        tvType = (TextView) findViewById(R.id.tv_type);
        rlLimitsTable = (RelativeLayout) findViewById(R.id.rl_limits_table);

    }

    @Override
    public void setListener() {
        // 网络请求监听
        httpUtil.setHttpRequesListener(this);
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        //常见问题按钮监听器
        imgBtnNext.setOnClickListener(this);
        // 下一步按钮
        btnNext.setOnClickListener(this);
        // 金额输入文本框变化监听
        meRechargeMoney.setOnTextChangeListener(this);
        rlLimitsTable.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
        imgBtnNext.setVisibility(View.VISIBLE);
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:充值
        tvTitleName.setText(R.string.top_up_name);
    }

    @Override
    public void initData() {
        if (Constant.isChangeRequestAddress) {
            mMinRechargeMoney = 0;// 测试的时候最小充值金额不限制
        }

        meRechargeMoney.setTextView(tvRechargeMoney);
        meRechargeMoney.setHint("最低充值额度100.00元");

        if (TextUtils.isEmpty(app.mAccountData.bankNum)) {// 没绑卡
            btnNext.setText("开通快捷支付");
        } else {
            int length = app.mAccountData.bankNum.length();
            String bankNum = app.mAccountData.bankNum.substring(length - 4, length);
            tvBankCardNumber.setText("储蓄卡(" + bankNum + ")");
            tvBankName.setText(app.mAccountData.bankName);
            BankImageUtil.setBankImage(app.mAccountData.bankName, civBank);
        }

        switch (mRechargeType) {
            case Balance:
                circleImageView.setVisibility(View.VISIBLE);
                circleBankIconPig.setVisibility(View.GONE);
//                circleImageView.setImageResource(R.drawable.image_recharge_lqb);
                reImageBankBackground.setBackgroundResource(R.drawable.image_bank_backbround);
                tvType.setText("(可用余额)");
                break;
            case Lqb:
                circleImageView.setVisibility(View.GONE);
                circleBankIconPig.setVisibility(View.VISIBLE);
                tvBalance.setVisibility(View.INVISIBLE);
                tvType.setVisibility(View.INVISIBLE);
                tvLqb.setVisibility(View.VISIBLE);
                reImageBankBackground.setBackgroundResource(R.drawable.image_bank_backbround);
                break;
        }

        sendTyjRequest(RefreshType.RefreshNoPull);
        sendMaxChargeAndChannel(RefreshType.RefreshNoPull);
    }

    /**
     * 1.发送体验金请求
     */
    public void sendTyjRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.PAY_CHECKCHARGESTATUS);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 2.获取渠道和充值最大金额
     */
    public void sendMaxChargeAndChannel(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.PAY_QUERYMAXCHARGEANDCHANNEL);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 3.充值到余额或零钱宝
     */
    public void sendRechargeRequest(RefreshType refreshType) {
        RequestParams params = null;
        switch (mRechargeType) {
            case Balance:// 充值到余额
                L.hintI("------------充值到余额-------------");
                params = new RequestParams(Constant.PAY_BINDEDCHARGEINDEBIT);
                params.addBodyParameter("charge_money", mRechargeMoney);
                break;
            case Lqb:// 充值到零钱宝
                L.hintI("------------充值到零钱宝-------------");
                params = new RequestParams(Constant.LQB_BINDEDBANKCARDINTOLQB);
                params.addBodyParameter("intoMoney", mRechargeMoney);
                break;
        }
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }

    /**
     * 设置提示对话框更改图形验证码
     */
    private void setPromptDialog(String str1) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_REGISTER, str1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.imgBtn_next:// 充值问题
                intent = new Intent(RechargeActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_RECHARGE);
                intent.putExtra("TitleName", "充值问题");
                startActivity(intent);
                break;
            case R.id.rl_top_up_100:
                setMoney(100);
                break;
            case R.id.rl_top_up_1000:
                setMoney(1000);
                break;
            case R.id.btn_next:// 下一步
                // 充值金额
                mRechargeMoney = meRechargeMoney.getText().toString();
                if (TextUtils.isEmpty(mRechargeMoney)) {
                    setPromptDialog("请输入金额");
                    return;
                }

                if (Double.parseDouble(mRechargeMoney) < mMinRechargeMoney) {
                    setPromptDialog("最低充值额度100.00元");
                    return;
                }

                goActivity();

                break;
            case R.id.rl_limits_table://充值限额

                Intent intent = new Intent(RechargeActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_CZXE);
                intent.putExtra("TitleName", "银行限额表");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 判断是否绑过银行卡
     */
    private void sendBandBankExist(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.LQB_QUERYUSERNAMEANDIDNUM);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void setTextChange(String text) {
        double money = 0.00;
        if (!TextUtils.isEmpty(text)) {
            money = Double.parseDouble(text);
        }
        setSelectMoney(money);

        if (money == 0) {
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
        }

    }

    /**
     * 显示或者隐藏体验金
     *
     * @param chargeStatus 0：未充值，1：首次充值100,2：首次充值1000,3：已首次充值100和1000
     */
    private void showOrHideTyj(int chargeStatus) {
        showChangeStatus = chargeStatus;
        if (chargeStatus != 3) {
            vsSelectRecharge.setVisibility(View.VISIBLE);

            RelativeLayout rlRecharge100 = (RelativeLayout) findViewById(R.id.rl_top_up_100);
            RelativeLayout rlRecharge1000 = (RelativeLayout) findViewById(R.id.rl_top_up_1000);
            imageSelect100 = (ImageView) findViewById(R.id.image_select_100);
            imageSelect1000 = (ImageView) findViewById(R.id.image_select_1000);
            ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
            ImageView imageView2 = (ImageView) findViewById(R.id.image_tyj_1000);
            tvTopUp100 = (TextView) findViewById(R.id.tv_top_up100);
            tvTopUp1000 = (TextView) findViewById(R.id.tv_top_up1000);
            tvTyj2000 = (TextView) findViewById(R.id.tv_tyj_2000);
            tvTyj1000 = (TextView) findViewById(R.id.tv_tyj_100);
            rlRecharge100.setOnClickListener(this);
            rlRecharge1000.setOnClickListener(this);
            int rechargeMoney = 0;
            switch (chargeStatus) {
                case 0:
                    rechargeMoney = 1000;
                    break;
                case 1:
                    rlRecharge100.setClickable(false);
                    tvTopUp100.setTextColor(getResources().getColor(R.color.FC_999999));
                    tvTyj1000.setTextColor(getResources().getColor(R.color.FC_999999));
                    imageView1.setImageResource(R.drawable.un_image_top_up100);
                    rechargeMoney = 1000;
                    break;
                case 2:
                    rlRecharge1000.setClickable(false);
                    tvTopUp1000.setTextColor(getResources().getColor(R.color.FC_999999));
                    tvTyj2000.setTextColor(getResources().getColor(R.color.FC_999999));
                    imageView2.setImageResource(R.drawable.un_image_top_up1000);
                    rechargeMoney = 100;
                    break;
                default:
                    break;
            }

            setMoney(rechargeMoney);

        }
    }

    /**
     * 设置金额
     *
     * @param rechargeMoney 需要充值的金额
     */
    private void setMoney(int rechargeMoney) {
        setSelectMoney(rechargeMoney);
        meRechargeMoney.setText(MoneyFormatUtil.format2(rechargeMoney));
        tvRechargeMoney.setText(MoneyFormatUtil.format(rechargeMoney));
    }

    /**
     * 根据金额选择勾
     *
     * @param money
     */
    private void setSelectMoney(double money) {
        if (imageSelect100 == null && imageSelect1000 == null) {
            return;
        }
        // 小于100，两个都不显示
        if (money < 100) {
            imageSelect100.setVisibility(View.GONE);
            imageSelect1000.setVisibility(View.GONE);
        } else if (money < 1000) {         // 如果输入的金额大于等于100，小于1000显示100的勾
            if (showChangeStatus != 1) {
                imageSelect100.setVisibility(View.VISIBLE);
            } else {
                imageSelect100.setVisibility(View.GONE);
            }
            imageSelect1000.setVisibility(View.GONE);
        } else {      // 如果输入的金额大于等于1000，显示1000的勾
            if (showChangeStatus != 2) {
                imageSelect1000.setVisibility(View.VISIBLE);
            } else {
                imageSelect1000.setVisibility(View.GONE);
            }
            imageSelect100.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String url, String errorContent) {
        // 充值判断送体验金 和 获取渠道和充值最大金额
        if (url.equals(Constant.PAY_QUERYMAXCHARGEANDCHANNEL)) {
            finish();
        } else {
            setPromptDialog(errorContent);
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        if (url.equals(Constant.PAY_CHECKCHARGESTATUS)) {// 充值判断送体验金
            // 显示或者隐藏体验金,服务器返回的状态
            mChargeStatus = jsonObject.getIntValue("chargeStatus");
            showOrHideTyj(mChargeStatus);
        } else if (url.equals(Constant.PAY_QUERYMAXCHARGEANDCHANNEL)) {// 获取渠道和充值最大金额
            mChargeChannel = jsonObject.getString("chargeChannel");
//            singleLimitation = jsonObject.getString("singleLimitation");
            description = jsonObject.getString("description");
            meRechargeMoney.setTextContext(mChargeChannel); //设置充值通道
            if (!TextUtils.isEmpty(description)) {
                tvRechargeQuota.setVisibility(View.VISIBLE);
                tvRechargeQuota.setText(description);
            } else {
                tvRechargeQuota.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mChargeChannel)) {     //当前充值通道
                tvRechargeMoney.setText(mChargeChannel);
            }
//            if (!TextUtils.isEmpty(singleLimitation)){    //充值限额文本框
//                meRechargeMoney.setInputMaxMoney(Double.parseDouble(singleLimitation));
//            }
        } else if (url.equals(Constant.PAY_BINDEDCHARGEINDEBIT)) {// 充值到余额
            if (jsonObject.getInteger("channelStatus") != null && jsonObject.getInteger("channelStatus") == 1) {
                /**
                 * 设置支付通道弹窗
                 */
                showChannelDialog();

            } else {
                // 跳转到短信验证
                Intent intent = new Intent(RechargeActivity.this, BandBankVerificationCodeActivity.class);
                intent.putExtra("orderNo", jsonObject.getString("orderNo"));
                intent.putExtra("reserved_phone", jsonObject.getString("phone"));
                intent.putExtra("charge_money", mRechargeMoney);
                startActivity(intent);
            }
        } else if (url.equals(Constant.LQB_BINDEDBANKCARDINTOLQB)) {// 充值到零钱宝
            if (jsonObject.getInteger("channelStatus") != null && jsonObject.getInteger("channelStatus") == 1) {
                /**
                 * 设置支付通道弹窗
                 */
                showChannelDialog();

            } else {

                // 跳转到短信验证
                Intent intent = new Intent(RechargeActivity.this, BandBankVerificationCodeActivity.class);
                intent.putExtra("orderNo", jsonObject.getString("orderNo"));
                intent.putExtra("reserved_phone", jsonObject.getString("phone"));
                intent.putExtra("charge_money", mRechargeMoney);
                startActivity(intent);
            }
        } else if (url.equals(Constant.LQB_QUERYUSERNAMEANDIDNUM)) {  //判断是否绑过银行卡
            int everBinded = jsonObject.getInteger("everBinded");
            if (everBinded == 0) {   //未绑过卡
                L.hintI("------------没绑卡-------------");
                Intent intent = new Intent(RechargeActivity.this, BandBankActivity.class);
                intent.putExtra("rechargeMoney", mRechargeMoney);
                intent.putExtra(RechargeType.class.getName(), mRechargeType);
                startActivity(intent);
            } else if (everBinded == 1) { //已绑过卡更换银行卡
                L.hintI("------------更换银行卡-------------");
                Intent intent = new Intent(RechargeActivity.this, ChangeBankCardActivity.class);
                intent.putExtra("userName", jsonObject.getString("userName"));
                intent.putExtra("IdNum", jsonObject.getString("IdNum"));
                intent.putExtra("rechargeMoney", mRechargeMoney);
                intent.putExtra(RechargeType.class.getName(), mRechargeType);
                startActivity(intent);
            }
        }
    }


    /**
     * 跳转Activity
     */
    public void goActivity() {
        if (TextUtils.isEmpty(app.mAccountData.bankNum)) {// 没绑卡
            L.hintI("------------没绑卡-------------");
            sendBandBankExist(RefreshType.RefreshNoPull);
        } else {// 已绑卡
            L.hintI("------------已绑卡-------------");
            sendRechargeRequest(RefreshType.RefreshNoPull);
        }
    }

    /**
     * 设置支付通道弹窗
     */
    private void showChannelDialog() {
        DialogUtil.paymentChannelsDialogPrompt(this, mChargeChannel, new DialogUtil.OnClickYesListener() {
            @Override
            public void onClickYes() {
                startActivity(new Intent(RechargeActivity.this, PayThirdpartyActivity.class));
            }
        });

    }
}

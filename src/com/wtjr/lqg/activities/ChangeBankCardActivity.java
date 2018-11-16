package com.wtjr.lqg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.BandBankType;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.ButtonEnabledListener;
import com.wtjr.lqg.utils.DecollatorUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.widget.BandBankEdit;
import com.wtjr.lqg.widget.BandBankEditNoClearContext;
import com.wtjr.lqg.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

/**
 * Created by WangYong on 2017/4/6.
 */

public class ChangeBankCardActivity extends BaseActivity implements View.OnClickListener, HttpUtil.HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 银行卡号
     */
    private BandBankEdit bbeCardNo;
    /**
     * 开户名字
     */
    private BandBankEditNoClearContext bbeOwner;
    /**
     * 身份证号
     */
    private BandBankEditNoClearContext bbeCertNo;
    /**
     * 预留手机号输入框
     */
    private BandBankEdit edtLocalPhone;
//    /**
//     * 预留手机号点击按钮直接获取
//     */
//    private Button btnLocalPhone;
    /**
     * 开户支行
     */
    private BandBankEdit bbOpenAccountBranch;
    /**
     * 立即绑定
     */
    private Button btnBbankImmediately;
    /**
     * 状态是绑卡的Ture,反之false
     */
    private boolean isBandBank=false;


    /**
     * 充值金额
     */
    private String mChargeMoney;
    /**
     * 银行卡号
     */
    private String mCardNo;
    /**
     * 持卡人
     */
    private String userName;
    /**
     * 身份证
     */
    private String IdNum;
    /**
     * 预留手机号
     */
    private String mReservedPhone;
    /**
     * 开户支行
     */
    private String mOpenAccountBranch;
    /**
     * 卡的id
     */
    private String mBindId;
    /**
     * 省和城市
     */
    private String mOpenAccountLocation;
    /**
     * 订单号
     */
    private String mOrderNo;
    /**
     * 充值类型(充到余额额还是零钱宝)
     */
    private RechargeType mRechargeType;
    private BandBankEdit bbeSelectAddress;
    private RelativeLayout rlSelectAddress;

    private ClearEditText bbeCardNoEditText;
    private EditText bbeOwnerEditText;
    private EditText bbeCertNoEditText;
    private ClearEditText edtLocalPhoneEditText;
    private ClearEditText bbeSelectAddressEditText;
    private ClearEditText bbOpenAccountBranchEditText;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_change_bank_card);
        start();
    }

    @Override
    public void initIntent() {
        mChargeMoney = getIntent().getStringExtra("rechargeMoney");
        userName = getIntent().getStringExtra("userName");
        IdNum = getIntent().getStringExtra("IdNum");
        mRechargeType = (RechargeType) getIntent().getSerializableExtra(RechargeType.class.getName());
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        // 银行卡号
        bbeCardNo = (BandBankEdit) findViewById(R.id.bbe_card_no);
        bbeCardNo.setInputTypeNumber();

        DecollatorUtil util = new DecollatorUtil();
        bbeCardNoEditText = (ClearEditText) bbeCardNo.findViewById(R.id.edt_right);
        util.decollatorNumber(bbeCardNoEditText);

        // 开户名字
        bbeOwner = (BandBankEditNoClearContext) findViewById(R.id.bbe_owner);
        bbeOwnerEditText = (EditText) bbeOwner.findViewById(R.id.edt_right);

        // 身份证号
        bbeCertNo = (BandBankEditNoClearContext) findViewById(R.id.bbe_cert_no);
        bbeCertNoEditText = (EditText) bbeCertNo.findViewById(R.id.edt_right);
        bbeCertNo.setInputTypeIdCard();
        // 预留手机号
        edtLocalPhone = (BandBankEdit) findViewById(R.id.edt_local_phone);
        edtLocalPhoneEditText = (ClearEditText) edtLocalPhone.findViewById(R.id.edt_right);

//        btnLocalPhone = (Button) findViewById(R.id.btn_local_phone);
        edtLocalPhone.setInputTypeNumber();

        // 开户省市
        bbeSelectAddress = (BandBankEdit) findViewById(R.id.bbe_select_address);
        bbeSelectAddressEditText = (ClearEditText) bbeSelectAddress.findViewById(R.id.edt_right);
        rlSelectAddress = (RelativeLayout) findViewById(R.id.rl_select_address);

        // 开户支行
        bbOpenAccountBranch = (BandBankEdit) findViewById(R.id.bb_open_account_branch);
        bbOpenAccountBranchEditText = (ClearEditText) bbOpenAccountBranch.findViewById(R.id.edt_right);

        // 立即绑定
        btnBbankImmediately = (Button) findViewById(R.id.btn_next);

//		bbeCardNo.setTextRight("6214242710498301");
//		bbeOwner.setTextRight("韩梅梅");
//		bbeCertNo.setTextRight("210302196001012114");
//		edtLocalPhone.setTextRight("13220482188");
//		bbeSelectAddress.setTextRight("深圳福田区");
//		bbOpenAccountBranch.setTextRight("建设银行");
    }

    @Override
    public void setListener() {
        //网络请求
        httpUtil.setHttpRequesListener(this);
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 开户省市
        rlSelectAddress.setOnClickListener(this);
//        //点击按钮获取本地号码
//        btnLocalPhone.setOnClickListener(this);
        // 立即绑定
        btnBbankImmediately.setOnClickListener(this);


        EditText[] editTexts = {bbeCardNoEditText,bbeOwnerEditText,bbeCertNoEditText,
                edtLocalPhoneEditText,bbeSelectAddressEditText,bbOpenAccountBranchEditText};
        bbeCardNoEditText.addTextChangedListener(new ButtonEnabledListener(btnBbankImmediately,editTexts));
        bbeOwnerEditText.addTextChangedListener(new ButtonEnabledListener(btnBbankImmediately,editTexts));
        bbeCertNoEditText.addTextChangedListener(new ButtonEnabledListener(btnBbankImmediately,editTexts));
        edtLocalPhoneEditText.addTextChangedListener(new ButtonEnabledListener(btnBbankImmediately,editTexts));
        bbeSelectAddressEditText.addTextChangedListener(new ButtonEnabledListener(btnBbankImmediately,editTexts));
        bbOpenAccountBranchEditText.addTextChangedListener(new ButtonEnabledListener(btnBbankImmediately,editTexts));
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:绑定银行卡
        tvTitleName.setText(R.string.change_bank_name);
        bbeOwnerEditText.setText(userName);
        bbeCertNoEditText.setText(IdNum);
    }

    @Override
    public void initData() {


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String province = intent.getStringExtra("province");
            String city = intent.getStringExtra("city");
            if(TextUtils.isEmpty(city)) {
                mOpenAccountLocation=province;
            }else {
                mOpenAccountLocation=province + " " + city;
            }
            bbeSelectAddress.setTextRight(mOpenAccountLocation);
        }
    }

    /**
     * 设置提示对话框更改图形验证码
     *
     */
    private void setPromptDialog(String str1) {
        DialogUtil.promptDialog(ChangeBankCardActivity.this, DialogUtil.HEAD_REGISTER,str1, null);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.rl_select_address:// 开户省市
                intent = new Intent(ChangeBankCardActivity.this,SelectBankAddressActivity.class);
                intent.putExtra(BandBankType.class.getName(),BandBankType.yesBandBank);
                startActivity(intent);
                break;
//            case R.id.btn_local_phone:// 点击按钮获取本地号码
//                // TODO 查询本地号码放入
//                edtLocalPhone.setTextRight(mCurrentPhone);
//                break;
            case R.id.btn_next:// 立即绑定
                sendHttpRequest(RefreshType.RefreshNoPull);
                break;

            default:
                break;
        }

    }


    /**
     * 重新发送短信验证码
     */
    public void sendHttpRequest(RefreshType type) {
        mCardNo = bbeCardNo.getTextRight().replace(" ", "");
        //预留手机号
        mReservedPhone = edtLocalPhone.getTextRight();
        //开户支行
        mOpenAccountBranch = bbOpenAccountBranch.getTextRight();


        if (TextUtils.isEmpty(mCardNo)) {
            setPromptDialog("请输入银行卡号");
            return;
        }

        if (mCardNo.length() < 15) {
            setPromptDialog("银行卡号不能小于15位");
            return;
        }

        if (TextUtils.isEmpty(mReservedPhone)) {
            setPromptDialog("请输入银行预留手机号码");
            return;
        }
        if (TextUtils.isEmpty(mOpenAccountLocation)) {
            setPromptDialog("请输入开户省市");
            return;
        }
        if (TextUtils.isEmpty(mOpenAccountBranch)) {
            setPromptDialog("请输入开户支行");
            return;
        }

        RequestParams params = null;
        if (TextUtils.isEmpty(app.mAccountData.bankNum)) {//没绑卡
            L.hintI("------------没绑卡-------------");
            switch (mRechargeType) {
                case Balance:// 充值到余额
                    L.hintI("------------充值到余额绑卡-------------");
                    params = new RequestParams(Constant.PAY_FIRSTTIMECHARGEINDEBIT);
                    break;
                case Lqb:// 充值到零钱宝
                    L.hintI("------------充值到零钱宝绑卡-------------");
                    params = new RequestParams(Constant.LQB_BANKCARDINTOLQB);
                    break;
            }
        } else {
            L.hintI("------------已绑卡-------------");
            switch (mRechargeType) {
                case Balance:// 充值到余额
                    L.hintI("------------充值到余额-------------");
                    params = new RequestParams(Constant.PAY_BINDEDCHARGEINDEBIT);
                    break;
                case Lqb:// 充值到零钱宝
                    L.hintI("------------充值到零钱宝-------------");
                    params = new RequestParams(Constant.LQB_BINDEDBANKCARDINTOLQB);
                    break;
            }
        }

        // 用户唯一标识
        params.addBodyParameter("user_userunid", mUid);
        // 充值金额
        params.addBodyParameter("charge_money", mChargeMoney);
        // 银行卡号
        params.addBodyParameter("card_no", mCardNo);
        // 持卡人姓名
        params.addBodyParameter("owner", userName);
        // 身份证号
        params.addBodyParameter("cert_no", IdNum);
        // 银行预留手机号
        params.addBodyParameter("reserved_phone", mReservedPhone);
        // 开户行所在地
        params.addBodyParameter("openAccount_location", mOpenAccountLocation);
        // 开户支行
        params.addBodyParameter("openAccount_branch", mOpenAccountBranch);

        httpUtil.sendRequest(params,type,this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        setPromptDialog(errorContent);
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        try {
            JSONObject jsonObject=new JSONObject(jsonBase.getDisposeResult());
            //订单号
            mOrderNo=jsonObject.getString("orderNo");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        goActivity();
    }



    @Override
    public void goActivity() {
        //跳转到短信验证
        Intent intent=new Intent(ChangeBankCardActivity.this, BandBankVerificationCodeActivity.class);
        // 用户唯一标识
        intent.putExtra("user_userunid", mUid);
        //订单号
        intent.putExtra("orderNo", mOrderNo);
        // 状态是绑卡的Ture,反之false
        intent.putExtra("isBandBank", isBandBank);
        // 充值金额
        intent.putExtra("charge_money", mChargeMoney);
        //卡id
        intent.putExtra("bind_id", mBindId);
        //银行卡号
        intent.putExtra("card_no", mCardNo);
        //持卡人姓名
        intent.putExtra("owner", userName);
        //身份证
        intent.putExtra("cert_no", IdNum);
        //预留手机号码
        intent.putExtra("reserved_phone", mReservedPhone);
        // 开户行所在地
        intent.putExtra("openAccount_location", mOpenAccountLocation);
        // 开户支行
        intent.putExtra("openAccount_branch", mOpenAccountBranch);
        startActivity(intent);
    }
}

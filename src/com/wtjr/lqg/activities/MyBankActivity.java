package com.wtjr.lqg.activities;

import org.xutils.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.BankImageUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickNoListener;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.PermissionUtils;
import com.wtjr.lqg.utils.StringUtil;
import com.wtjr.lqg.widget.CircleImageView;
import com.wtjr.lqg.widget.UsuallyLayoutItem;
import com.wtjr.lqg.R;

/**
 * 我的银行卡
 */
public class MyBankActivity extends BaseActivity implements OnClickListener,
        HttpRequesListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 下一步图标
     */
    private ImageButton imgBtnNext;
    /**
     * 银行的圆形图片
     */
    private CircleImageView civBankImage;
    /**
     * 银行卡名字
     */
    private TextView tvBankName;
    /**
     * 银行卡卡号
     */
    private TextView tvBankCardNumber;
    /**
     * 持卡人
     */
    private TextView tvCardHolder;
    /**
     * 开户行所在地
     */
    private UsuallyLayoutItem uliOpenAccountAddress;
    /**
     * 开户支行
     */
    private UsuallyLayoutItem uliOpenAccountBank;
    /**
     * 更换银行卡
     */
    private TextView btnChangeBank;
    /**
     * 底部提示语（打电话）
     */
    private TextView tvMarkedWords;
    /**
     * 后退
     */
    private RefreshType mRefreshType;

    private JSONObject jsonObject;
    //银行卡背景
    private LinearLayout llBankCard;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_my_bank);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        imgBtnNext = (ImageButton) findViewById(R.id.imgBtn_next);

        llBankCard = (LinearLayout) findViewById(R.id.ll_bank_card);

        civBankImage = (CircleImageView) findViewById(R.id.civ_bank_image);
        tvBankName = (TextView) findViewById(R.id.tv_bank_name);
        tvBankCardNumber = (TextView) findViewById(R.id.tv_bank_card_number);
        tvCardHolder = (TextView) findViewById(R.id.tv_cardholder);
        tvMarkedWords = (TextView) findViewById(R.id.my_bank_marked_words);
        uliOpenAccountAddress = (UsuallyLayoutItem) findViewById(R.id.uli_open_account_address);
        uliOpenAccountBank = (UsuallyLayoutItem) findViewById(R.id.uli_open_account_bank);

        btnChangeBank = (TextView) findViewById(R.id.btn_change_bank);

    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        // 下一步按钮监听器
        imgBtnNext.setOnClickListener(this);

        btnChangeBank.setOnClickListener(this);

        httpUtil.setHttpRequesListener(this);

    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        imgBtnBack.setImageResource(R.drawable.back2);
        // 设置名字为:我的银行卡
        tvTitleName.setText(R.string.my_bank_name);
        tvTitleName.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    public void initData() {
        sendActivityReques(RefreshType.RefreshNoPull);
        uliOpenAccountAddress.setImageContentrResourceL(0);
        uliOpenAccountBank.setImageContentrResourceL(0);

        if (TextUtils.isEmpty(app.mAccountData.bankNum)) {
            DialogUtil.selectDialog(this, DialogUtil.HEAD_REGISTER, "温馨提示", "您还未绑卡，请先去绑卡充值", "好哒", new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    Intent intent = new Intent(MyBankActivity.this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    startActivity(intent);
                    finish();
                }
            }, "取消", new OnClickNoListener() {

                @Override
                public void onClickNo() {
                    finish();
                }
            });
            return;
        }


        if (app.mAccountData.bankName != null) {
            BankImageUtil.setBankImage(app.mAccountData.bankName, civBankImage);
            // 银行卡号
            tvBankCardNumber.setText(StringUtil.setBlurryBankNumber(app.mAccountData.bankName));
            // 银行名称
            tvBankName.setText(app.mAccountData.bankName);
        }

        SpannableString spannableString = new SpannableString(getResources().getString(R.string.bank_prompt));
        spannableString.setSpan(new MyClickText(this), spannableString.length() - 13, spannableString.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMarkedWords.setText(spannableString);
        tvMarkedWords.setMovementMethod(LinkMovementMethod.getInstance());//不设置 没有点击事件
        tvMarkedWords.setHighlightColor(Color.TRANSPARENT); //设置点击后的颜色为透明


    }

    class MyClickText extends ClickableSpan {

        private Context context;

        public MyClickText(Context context) {
            this.context = context;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            //设置文本的颜色
            ds.setColor(getResources().getColor(R.color.FC_FF9900));
            //超链接形式的下划线，false 表示不显示下划线，true表示显示下划线
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            if (PermissionUtils.callPhone(MyBankActivity.this)) {
                DialogUtil.promptDialog(MyBankActivity.this, DialogUtil.HEAD_SERVICE, "是否确定呼叫", "服务热线:\n4006685753", new OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006685753"));
                        startActivity(phoneIntent);
                    }
                }).setCancelable(true);
            } else {
                DialogUtil.promptDialog(MyBankActivity.this, DialogUtil.HEAD_BAND_BANK, "应用未获得拨打电话权限");
            }
        }
    }

    /**
     * 发送我的银行卡请求
     *
     * @param refreshType
     */
    public void sendActivityReques(RefreshType refreshType) {
        mRefreshType = refreshType;
        RequestParams params = new RequestParams(Constant.PAY_QUERYBANKCARDINFO);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        if (errorContent.contains("暂未绑定")){
            DialogUtil.selectDialog(this, DialogUtil.HEAD_REGISTER, "温馨提示", "您还未绑卡，请先去绑卡充值", "好哒", new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    Intent intent = new Intent(MyBankActivity.this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    startActivity(intent);
                    finish();
                }
            }, "取消", new OnClickNoListener() {

                @Override
                public void onClickNo() {
                    finish();
                }
            });
        }else {
            DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
        }

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        if (url.equals(Constant.PAY_QUERYBANKCARDINFO)) {
            setShowData();
        }
    }

    /**
     * 设置显示的数据
     */
    private void setShowData() {
        // 银行图片设置
        BankImageUtil.setBankImage(jsonObject.getString("bankName"), civBankImage);
        // 银行名称
        tvBankName.setText(jsonObject.getString("bankName"));
        // 银行卡号
        tvBankCardNumber.setText(StringUtil.setBlurryBankNumber(jsonObject.getString("bankNum")));
        // 持卡人
        tvCardHolder.setText("持卡人: " + StringUtil.setBlurryName(jsonObject.getString("bankUserName")));
        // 开户行所在地
        uliOpenAccountAddress.setTextTvContentR(jsonObject.getString("bankProvince") + " " + jsonObject.getString("bankCity"), 0);
        // 开户支行
        uliOpenAccountBank.setTextTvContentR(jsonObject.getString("bankBranch"), 0);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.btn_change_bank:// 更换银行卡
                startActivity(new Intent(this, ReplaceBankCardActivity.class));
                break;

            default:
                break;
        }
    }
}

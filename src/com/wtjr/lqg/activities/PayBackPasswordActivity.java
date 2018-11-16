package com.wtjr.lqg.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.PermissionUtils;
import com.wtjr.lqg.utils.StringUtil;

/**
 * Created by WangYong on 2017/2/13.
 * 找回支付密码
 */

public class PayBackPasswordActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 标题
     *
     */
    private TextView tvTitleName;

    /**
     * 返回按钮
     *
     */
    private ImageButton imgBtnBack;

    /**
     * 可以接收
     *
     */
    private Button btnCanReceive;

    /**
     * 无法接收
     *
     */
    private Button btnNoReceive;

    /**
     * 手机号
     *
     */
    private TextView tvPhoneNumber;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_pay_back_password);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        btnCanReceive = (Button) findViewById(R.id.btn_can_receive);
        btnNoReceive = (Button) findViewById(R.id.btn_no_receive);
        tvPhoneNumber = (TextView) findViewById(R.id.tv_phone_number);
    }

    @Override
    public void initData() {
        tvPhoneNumber.setText(StringUtil.setBlurryPhone(mCurrentPhone));
    }

    @Override
    public void setListener() {
        imgBtnBack.setOnClickListener(this);
        btnCanReceive.setOnClickListener(this);
        btnNoReceive.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
        imgBtnBack.setVisibility(View.VISIBLE);
        tvTitleName.setText("找回支付密码");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtn_back:
                finish();      //结束当前activity
                break;
            case R.id.btn_can_receive:  //可以接收短信
                startActivity(new Intent(PayBackPasswordActivity.this, PayPasswordIdCardVerifyActivity.class));
                break;
            case R.id.btn_no_receive:   //不能接收短信
                DialogUtil.selectDialog(PayBackPasswordActivity.this, 1, "你需要联系零妹妹上传资料来确认你的身份", "400-668-5753", "呼叫", new DialogUtil.OnClickYesListener() {
                    @SuppressWarnings("MissingPermission")
                    @Override
                    public void onClickYes() {
                        if (!PermissionUtils.callPhone(PayBackPasswordActivity.this)) {
                            DialogUtil.promptDialog(PayBackPasswordActivity.this, DialogUtil.HEAD_BAND_BANK, "应用未获得拨打电话权限");
                        } else {
                            Intent phoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4006685753"));
                            startActivity(phoneIntent);
                        }

                    }
                }, "取消", new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {

                    }
                });
                break;
            default:

                break;
        }
    }
}

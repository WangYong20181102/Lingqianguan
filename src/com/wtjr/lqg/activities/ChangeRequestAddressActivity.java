package com.wtjr.lqg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.basecommonly.BaseAppManager;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.DataCleanManager;
import com.wtjr.lqg.utils.DialogUtil;

/**
 * 更换请求地址
 */
public class ChangeRequestAddressActivity extends BaseActivity implements OnClickListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 清空缓存
     */
    private Button btnDataClean;
    private Button btnChangeRequestAddress;
    private EditText etChangeRequestAddress;
    private TextView tvRequestAddress;
    private TextView tvAddress1;
    private TextView tvAddress2;
    private TextView tvAddress3;
    private TextView tvAddress4;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_change_request_address);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        tvRequestAddress = (TextView) findViewById(R.id.tv_requestAddress);
        btnDataClean = (Button) findViewById(R.id.btn_dataClean);
        btnChangeRequestAddress = (Button) findViewById(R.id.btn_changeRequestAddress);
        etChangeRequestAddress = (EditText) findViewById(R.id.et_changeRequestAddress);

        tvAddress1 = (TextView) findViewById(R.id.tv_address1);
        tvAddress2 = (TextView) findViewById(R.id.tv_address2);
        tvAddress3 = (TextView) findViewById(R.id.tv_address3);
        tvAddress4 = (TextView) findViewById(R.id.tv_address4);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        btnDataClean.setOnClickListener(this);
        btnChangeRequestAddress.setOnClickListener(this);

        tvAddress1.setOnClickListener(this);
        tvAddress2.setOnClickListener(this);
        tvAddress3.setOnClickListener(this);
        tvAddress4.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        tvTitleName.setText("切换环境");
    }

    @Override
    public void initData() {
        String address = SharedPreferencesUtil.getPrefString(this, "RequestAddress", "http://192.168.1.129:9908");
        tvRequestAddress.setText("当前的环境:" + address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:// 返回按钮点击操作
                // 结束当前的Activity
                finish();
                break;

            case R.id.btn_dataClean:// 清空缓存
                DataCleanManager.cleanApplicationData(this, "");
                restartApp();
                break;
            case R.id.btn_changeRequestAddress:// 确认更换环境
                final String address = etChangeRequestAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    DialogUtil.promptDialog(this, "地址为空哦！");
                    return;
                }

                SharedPreferencesUtil.setPrefString(ChangeRequestAddressActivity.this, "RequestAddress", address);
                restartApp();

                break;
            case R.id.tv_address1://
                etChangeRequestAddress.setText(Constant.TEST);
                etChangeRequestAddress.setSelection(Constant.TEST.length());
                break;
            case R.id.tv_address2:
                etChangeRequestAddress.setText(Constant.HTTPS_PRODUCTION);
                etChangeRequestAddress.setSelection(Constant.HTTPS_PRODUCTION.length());
                break;
            case R.id.tv_address3:
                etChangeRequestAddress.setText(Constant.TEST1);
                etChangeRequestAddress.setSelection(Constant.TEST1.length());
                break;
            case R.id.tv_address4:
                etChangeRequestAddress.setText(Constant.TEST6);
                etChangeRequestAddress.setSelection(Constant.TEST6.length());
                break;
            default:
                break;
        }
    }

    /**
     * 重启APP
     */
    public void restartApp() {
        Intent intent = new Intent(this, LaunchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        BaseAppManager.getAppManager().AppExit(this);
    }
}

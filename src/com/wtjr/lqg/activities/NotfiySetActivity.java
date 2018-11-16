package com.wtjr.lqg.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.widget.toggle.ToggleButton;

import org.apache.http.params.HttpParams;
import org.xutils.http.RequestParams;

/**
 * Created by WangXu on 2017/2/27.
 * 通知设置
 */
public class NotfiySetActivity extends BaseActivity implements View.OnClickListener, HttpUtil.HttpRequesListener {
    private TextView tvTitleName;
    private ImageButton imgBtnBack;
    /**
     * 活动消息推送
     */
    private ToggleButton tbNotifyActivity;
    /**
     * 到期还款推送
     */
    private ToggleButton tbNotifyDq;
    /**
     * 每日收益推送
     */
    private ToggleButton tbNotifyIncome;
    /**
     * 体验金到账提醒
     */
    private ToggleButton tbNotifyTyj;
    /**
     * 推送权限索引
     */
    private String permissionIndex;
    /**
     * 推送权限值
     */
    private String permissionValue;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_notify_set);
        start();
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tbNotifyActivity = (ToggleButton) findViewById(R.id.tb_notify_activity);
        tbNotifyDq = (ToggleButton) findViewById(R.id.tb_notify_dq);
        tbNotifyIncome = (ToggleButton) findViewById(R.id.tb_notify_income);
        tbNotifyTyj = (ToggleButton) findViewById(R.id.tb_notify_tyj);

    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:设置
        tvTitleName.setText(R.string.seting_name);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);

        // 设置网络监听
        httpUtil.setHttpRequesListener(this);

        toggleChangedListener();
    }

    @Override
    public void initData() {
        /**
         * 获取消息推送权限列表请求
         */
        sendGetPermissionRequest();

    }

    /**
     * 获取消息推送权限列表请求
     */
    private void sendGetPermissionRequest() {
        RequestParams params = new RequestParams(Constant.GETPERMISSION);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);
    }

    /**
     * 设置消息接收权限请求
     */
    private void sendSetPermissionRequest() {
        RequestParams params = new RequestParams(Constant.SETPERMISSION);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("permissionIndex", permissionIndex);
        params.addBodyParameter("permissionValue", permissionValue);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);
    }

    /**
     * 开关状态变化监听
     */
    private void toggleChangedListener() {
        tbNotifyActivity.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                permissionIndex = "3";
                if (on) {
                    permissionValue = "1";
                } else {
                    permissionValue = "0";
                }
                /**
                 * 设置消息接收权限请求
                 */
                sendSetPermissionRequest();
            }
        });

        tbNotifyDq.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                permissionIndex = "6";
                if (on) {
                    permissionValue = "1";
                } else {
                    permissionValue = "0";
                }
                /**
                 * 设置消息接收权限请求
                 */
                sendSetPermissionRequest();
            }
        });

        tbNotifyIncome.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                permissionIndex = "7";
                if (on) {
                    permissionValue = "1";
                } else {
                    permissionValue = "0";
                }
                /**
                 * 设置消息接收权限请求
                 */
                sendSetPermissionRequest();
            }
        });

        tbNotifyTyj.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                permissionIndex = "4";
                if (on) {
                    permissionValue = "1";
                } else {
                    permissionValue = "0";
                }
                /**
                 * 设置消息接收权限请求
                 */
                sendSetPermissionRequest();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onFailure(String url, String errorContent) {
        //请求失败之后拿上次设置的状态
        /**
         * 获取推送设置状态的缓存
         */
        getCacheNotfiyset();
    }

    /**
     * 获取推送设置状态的缓存
     */
    private void getCacheNotfiyset() {
        if (SharedPreferencesUtil.getPrefString(this, "NotifyActivity", "1").equals("1")) {
            tbNotifyActivity.setToggleOn();
        } else {
            tbNotifyActivity.setToggleOff();
        }
        if (SharedPreferencesUtil.getPrefString(this, "NotifyDq", "1").equals("1")) {
            tbNotifyDq.setToggleOn();
        } else {
            tbNotifyDq.setToggleOff();
        }
        if (SharedPreferencesUtil.getPrefString(this, "NotifyIncome", "1").equals("1")) {
            tbNotifyIncome.setToggleOn();
        } else {
            tbNotifyIncome.setToggleOff();
        }
        if (SharedPreferencesUtil.getPrefString(this, "NotifyTyj", "1").equals("1")) {
            tbNotifyTyj.setToggleOn();
        } else {
            tbNotifyTyj.setToggleOff();
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (url.equals(Constant.GETPERMISSION)) {
            /**
             * 0   未开启 ;
             * 1   开启
             * 3   活动消息权限
             * 4   体验金消息权限
             * 6   到期还款推送消息权限
             * 7   收益消息权限
             */
            JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            if (jsonObject.getString("3").equals("1")) {
                tbNotifyActivity.setToggleOn();
                SharedPreferencesUtil.setPrefString(this, "NotifyActivity", "1");
            } else {
                tbNotifyActivity.setToggleOff();
                SharedPreferencesUtil.setPrefString(this, "NotifyActivity", "0");
            }
            if (jsonObject.getString("6").equals("1")) {
                tbNotifyDq.setToggleOn();
                SharedPreferencesUtil.setPrefString(this, "NotifyDq", "1");
            } else {
                tbNotifyDq.setToggleOff();
                SharedPreferencesUtil.setPrefString(this, "NotifyDq", "0");
            }
            if (jsonObject.getString("7").equals("1")) {
                tbNotifyIncome.setToggleOn();
                SharedPreferencesUtil.setPrefString(this, "NotifyIncome", "1");
            } else {
                tbNotifyIncome.setToggleOff();
                SharedPreferencesUtil.setPrefString(this, "NotifyIncome", "0");
            }
            if (jsonObject.getString("4").equals("1")) {
                tbNotifyTyj.setToggleOn();
                SharedPreferencesUtil.setPrefString(this, "NotifyTyj", "1");
            } else {
                tbNotifyTyj.setToggleOff();
                SharedPreferencesUtil.setPrefString(this, "NotifyTyj", "0");
            }
        }

        if (url.equals(Constant.SETPERMISSION)) {

        }
    }
}

package com.wtjr.lqg.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SaveFirstInState;
import com.wtjr.lqg.sharedpreferences.SaveIsFirstRun;
import com.wtjr.lqg.sharedpreferences.SaveLoginState;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.sharedpreferences.ToggleGesturePasswordState;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.SystemUtil;

import org.xutils.http.RequestParams;

import cn.jpush.android.api.JPushInterface;
import pl.droidsonroids.gif.GifImageView;

/**
 * 启动
 *
 * @author Myf
 */
public class LaunchActivity extends BaseActivity implements HttpRequesListener {

    private LinearLayout layout;
    /**
     * 活动图片是否加载完成
     */
    private boolean isLoadingComplete = true;
    private String activeImage;
    /**
     * 启动gif图片
     */
    private GifImageView gifLaunch;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_launch);

        if (Constant.isChangeRequestAddress) {
            String address = SharedPreferencesUtil.getPrefString(this, "RequestAddress", "http://192.168.1.129:9908");
            Constant.changeRequestAddress(address);
        }

        start();
    }

    @Override
    public void findViewById() {
        layout = (LinearLayout) findViewById(R.id.ll);
        gifLaunch = (GifImageView) findViewById(R.id.gif_launch);
    }

    @Override
    public void setListener() {
        httpUtil.setHttpRequesListener(this);
    }

    @Override
    public void setTitle() {
    }

    @Override
    public void initData() {
        //改变webview对话框显示状态，防止分享URL注册成功跳转app无法点击邀请分享按钮
        WebViewActivity.MARKDIALOH = 1;
        // 友盟统计
        MobclickAgent.openActivityDurationTrack(false);
        /** 设置是否对日志信息进行加密, 默认false(不加密). 友盟统计 */
        MobclickAgent.enableEncrypt(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /**
                 * 请求启动页活动图片
                 */
                sendLaunchImage();
            }
        }, 2000);
    }

    /**
     * 请求启动页活动图片
     */
    private void sendLaunchImage() {
        RequestParams params = new RequestParams(Constant.LAUNCH_IMAGEURL);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, LaunchActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLoadingComplete) {
                    goActivity();
                }
            }
        }, 2000);
    }

    @Override
    public void goActivity() {
        /**
         * 比对当前版本号和上次存本地的版本号
         */
        boolean upVersion = SystemUtil.getAppVersionName(app).equals(SharedPreferencesUtil.getPrefString(app, "oldVersion", "0"));
        /**
         * 首次安装或者更新了app，打开引导页
         */
        if (SaveIsFirstRun.getIsFirstRun(app) || !upVersion) {
            startActivity(new Intent(LaunchActivity.this, GuidePageActivity.class));
            SaveIsFirstRun.setUnFirstRun(app);
        } else if (SaveFirstInState.getFirstInState(app)) {// 是第一次使用（一直都没有登录过）
            startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        } else {// 不是第一次使用（有登录过）
            if (SaveLoginState.getLoginState(app)) {// 账号处于登录的状态
                // 得到手势开个存储的状态
                boolean gesturePassword = ToggleGesturePasswordState.getGesturePassword(app);
                // 如果不等于null就跳到解锁
                if (gesturePassword) {
                    startActivity(new Intent(LaunchActivity.this, GestureVerifyActivity.class));
                } else {
                    startActivity(new Intent(LaunchActivity.this, MainActivity.class));
                }
            } else {// 账号没处于登录的状态
                startActivity(new Intent(LaunchActivity.this, HaveAcountsLoginActivity.class));
            }
        }
        finish();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        app.mScreenContentHeight = layout.getHeight();
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onFailure(String url, String errorContent) {
//        goActivity();
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        L.hintE(jsonBase);
        if (url.equals(Constant.LAUNCH_IMAGEURL)) {
            JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            if (jsonObject == null) {
                return;
            }
            activeImage = jsonObject.getString("activityImage");
            SharedPreferencesUtil.setPrefString(this,"subLqgDomain",jsonObject.getString("subLqgDomain"));
            if (activeImage.equals("")) {
                return;
            } else {
                loadLaunchImg(Constant.IMAGE_ADDRESS + activeImage);
            }
        }
    }

    private void loadLaunchImg(String imgurl) {
        gifLaunch.setScaleType(ImageView.ScaleType.FIT_XY);
        app.setDisplayImage(imgurl, gifLaunch, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                isLoadingComplete = false;
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                isLoadingComplete = false;
                goActivity();
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                isLoadingComplete = false;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goActivity();
                    }
                }, 2000);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);

    }
}
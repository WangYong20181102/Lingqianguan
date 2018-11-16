package com.wtjr.lqg.basecommonly;

import java.util.List;

import org.xutils.x;

import android.app.Activity;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.wtjr.lqg.activities.LaunchActivity;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.sharedpreferences.SaveCurrentPhoneUtil;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.sharedpreferences.SaveFirstInState;
import com.wtjr.lqg.sharedpreferences.SaveLoginState;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.widget.pullRefresh.PullableExpandableListView;
import com.wtjr.lqg.widget.pullRefresh.PullableListView;
import com.wtjr.lqg.R;

public class BaseActivity extends FragmentActivity implements IBaseListener {
    /**
     * 是否登录isLogin是true是登录状态，反之是未登录
     */
    public boolean isLogin = false;

    /**
     * 用户的uid
     */
    public String mUid;

    /**
     * 当前的手机号码
     */
    public String mCurrentPhone;

    public HttpUtil httpUtil;

    public static LqgApplication app;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        BaseAppManager.getAppManager().addActivity(this);
        setImmersionStatus();

        httpUtil = new HttpUtil();
        app = (LqgApplication) x.app();

        isLogin = SaveLoginState.getLoginState(app);

        mUid = SaveCurrentUidUtil.getCurrentUid(app);
        mCurrentPhone = SaveCurrentPhoneUtil.getCurrentPhone(app);
    }

    /**
     * 设置浸没状态效果
     */
    private void setImmersionStatus() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    @Override
    public void start() {
        initIntent();
        findViewById();
        setListener();
        setTitle();
        initData();
    }

    @Override
    public void start(View view) {
    }

    @Override
    public void initIntent() {
    }

    @Override
    public void findViewById(View view) {
    }

    @Override
    public void findViewById() {
    }

    @Override
    public void setListener() {
    }

    @Override
    public void setTitle() {
    }

    @Override
    public void initData() {
    }

    @Override
    public void refreshData() {
    }

    @Override
    public void goActivity() {
    }

    /**
     * 隐藏标题栏下面的线
     */
    public void TitleNoLine() {
        View line = findViewById(R.id.title_bottom_line);
        line.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        String name = getClass().getName();
        httpUtil.closeRequest();
        // 不能是启动页的时候销毁，会出现第一次启动app时首页弹出的对话框被干掉
        if (DialogUtil.customDialog != null && name != LaunchActivity.class.getName()) {
            DialogUtil.customDialog.dismiss();
        }
        BaseAppManager.getAppManager().finishActivity(this);
    }

    /**
     * 暂无数据
     *
     * @param activity
     * @param e        判断是JSONBase类型，还是String类型，如果是String需要有对话框提示错误信息
     * @param t        网络请求后JSON解析出来的内容(数据对象或数据集合)
     */
    public <T, E> void noNetAndData(Activity activity, E e, T t) {
        PullableListView pullableListView = (PullableListView) findViewById(R.id.pullable_listView);

        PullableExpandableListView pullableExpandableListView = (PullableExpandableListView) findViewById(R.id.pullable_Expandable_ListView);

        // //传过来的e不是BackCode对象表示是失败的,失败显示对话框
        if (e instanceof String) {
            DialogUtil.promptDialog(activity, DialogUtil.HEAD_BAND_BANK, (String) e);
        }

        // 传过来的T不等于null表示有内容
        if (t != null) {
            if (t instanceof List) {
                if (((List) t).size() == 0) {
                    if (pullableListView != null) {
                        pullableListView.showNoData(activity);
                    } else {
                        pullableExpandableListView.showNoData(activity);
                    }
                }
            }
        } else {
            if (pullableListView != null) {
                pullableListView.showNoData(activity);
            } else {
                pullableExpandableListView.showNoData(activity);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPause(this);
    }
}

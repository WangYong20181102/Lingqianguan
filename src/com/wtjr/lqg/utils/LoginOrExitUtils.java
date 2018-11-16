package com.wtjr.lqg.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.sobot.chat.SobotApi;
import com.umeng.analytics.MobclickAgent;
import com.wtjr.lqg.activities.GestureEditActivity;
import com.wtjr.lqg.activities.HaveAcountsLoginActivity;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.basecommonly.BaseAppManager;
import com.wtjr.lqg.sharedpreferences.SaveCurrentPhoneUtil;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.sharedpreferences.SaveLoginState;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.sharedpreferences.ToggleGesturePasswordState;

import cn.jpush.android.api.JPushInterface;

/**
 * 登陆或者退出工具类
 *
 * @author Myf
 */
public class LoginOrExitUtils {
    /**
     * 状态为登录
     */
    public final static int STATE_LOGIN = 1;

    /**
     * 状态为注册成功时直接登陆
     */
    public final static int STATE_REGISTER = 2;

    /**
     * 状态为已有账号的登陆页
     */
    public final static int STATE_HAVEACOUNT_LOGIN = 3;

    /**
     * 登录成功
     */
    public static void loginSuccess(Context context, LqgApplication app, int type, String userUId, String phone) {
        // 保存当前的userId
        SaveCurrentUidUtil.setCurrentUid(app, userUId);
        //保存当前的手机号码
        SaveCurrentPhoneUtil.setCurrentPhone(app, phone);
        // 保存状态为登陆
        SaveLoginState.isLogin(app);

        Intent intent = null;
        switch (type) {
            case STATE_LOGIN:
            case STATE_REGISTER:
                // 不管有没有锁每次都要去设置锁
                intent = new Intent(context, GestureEditActivity.class);
                intent.putExtra("isShowBack", false);
                context.startActivity(intent);
                break;
            case STATE_HAVEACOUNT_LOGIN:
                //有账号登陆过获得手势密码锁状态
                boolean gesturePassword = ToggleGesturePasswordState.getGesturePassword(app);
                if (gesturePassword) {
                    //有锁直接去主页
                    context.startActivity(new Intent(context, MainActivity.class));
                } else {
                    //没锁去设置
                    intent = new Intent(context, GestureEditActivity.class);
                    intent.putExtra("isShowBack", false);
                    context.startActivity(intent);
                }
                break;
            default:
                break;
        }
        //设置手势密码前，清空所有的活动
        BaseAppManager.getAppManager().finishAllActivity();
    }

    /**
     * 退出成功
     */
    public static void exitSuccess(Context context, LqgApplication app) {
        /**
         * 智齿客服
         * 用户在应用中退出登陆时需要调用 SDK 的注销操作（只在切换账号时调用），
         * 该操作会通知服务器进行推送信息的解绑，避免用户已退出但推送依然发送到当前设备的情况发生。
         * 当用于用户退出登录时调用以下方法：
         */
        SobotApi.exitSobotChat(context);

        // 保存状态为退出
        SaveLoginState.unLogin(app);
        //推出uid为null
        SaveCurrentUidUtil.setCurrentUid(app, "");
        Intent intent = new Intent(context, HaveAcountsLoginActivity.class);
        context.startActivity(intent);
        BaseAppManager.getAppManager().finishAllActivity();

        //退出成功要把首页显示资产状态置为true，防止用户多帐号登录出现显示隐藏混乱 true为显示
        SharedPreferencesUtil.setPrefBoolean(app, "showAssetStatus", true);
        //退出成功极光推送Alias置空
        JPushInterface.setAlias(context, "", null);
        // 账号登出时需调用此接口，调用之后不再发送账号相关内容（友盟统计）
        MobclickAgent.onProfileSignOff();
    }

}

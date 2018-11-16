package com.wtjr.lqg.SobotReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sobot.chat.SobotApi;
import com.sobot.chat.api.model.Information;
import com.wtjr.lqg.fragments.MeFragment;

import static com.wtjr.lqg.basecommonly.BaseActivity.app;


/**
 * Created by Wangxu on 2017/10/16.
 */

public class MySobotReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        //点击通知栏消掉我的客服的new
        MeFragment.dissNewTab();

        Information info = new Information();
        info.setAppkey("ce00f9474d614e1a9735c72ec58355ae");
        //注意：uid为用户唯一标识，不能传入一样的值
        info.setUid(app.mAccountData.uId);
        //用户昵称，选填
        info.setUname(app.mAccountData.nickName);
        //是否使用语音功能 true使用 false不使用   默认为true
        info.setUseVoice(false);
        //是否使用机器人语音功能 true使用 false不使用 默认为false
        info.setUseRobotVoice(false);
        info.setColor("#ffca60");   //标题栏背景
        //                    //返回时是否弹出满意度评价
//                    info.setShowSatisfaction(false);
        /**
         * @param context 上下文对象
         * @param information 初始化参数
         */
        SobotApi.startSobotChat(context, info);


    }


}

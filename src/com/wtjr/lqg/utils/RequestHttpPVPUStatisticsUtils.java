package com.wtjr.lqg.utils;

import android.content.Context;

import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;

import org.xutils.http.RequestParams;

/**
 * Created by WangYong on 2017/4/7.
 */

public class RequestHttpPVPUStatisticsUtils implements HttpUtil.HttpRequesListener {
    private HttpUtil httpUtil;
    /**
     * 按钮名称
     */
    private String buttomName;

    public RequestHttpPVPUStatisticsUtils() {

    }
    private static RequestHttpPVPUStatisticsUtils single = null;
    //静态工厂方法
    public static RequestHttpPVPUStatisticsUtils getInstance() {
        if (single == null) {
            single = new RequestHttpPVPUStatisticsUtils();
        }
        return single;
    }

    /**
     * 请求后台统计按钮类型
     * @param context
     * @param mUid
     * @param typeName
     */
    public void requestType(Context context, String mUid, int typeName,String url) {
        if (typeName == 1) {
            buttomName = "/queryBorrowDetail";
        } else if (typeName == 2) {
            buttomName = "/clickLqgService";
        } else if (typeName == 3) {
            buttomName = "/clickBannerOfWeibo";
        }
        requestStatistics(context, mUid,url);
    }

    private void requestStatistics(Context context, String mUid,String url) {
        httpUtil = new HttpUtil();
        httpUtil.setHttpRequesListener(this);
        RequestParams requestParams = new RequestParams(url);
        requestParams.addBodyParameter("osType", "0");
        requestParams.addBodyParameter("userId", mUid);
        requestParams.addBodyParameter("beUrl", buttomName);
        httpUtil.sendRequest(requestParams, RefreshType.RefreshNoPull, context);
    }

    @Override
    public void onFailure(String url, String errorContent) {

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {

    }
}

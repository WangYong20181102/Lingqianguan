package com.wtjr.lqg.utils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;

import org.xutils.http.RequestParams;

/**
 * 截图的友盟分享
 *
 * @author wangxu
 */

public class UMShare implements HttpRequesListener {

    private String wherefrom;

    private View view;

    private Activity mActivity;

    private UMImage image;
    /**
     * 分享连接
     */
    private String mShareUrl;
    /**
     * 分享标题
     */
    private String mShareTitle;
    /**
     * 分享内容
     */
    private String mShareContent;
    /**
     * 分享图片
     */
    private String mShareIcon;
    /**
     * 从哪个界面进入
     */
    private String mUid;
    private Bundle bundle;

    private HttpUtil mHttpUtil = new HttpUtil();


    public UMShare(Activity mActivity, String wherefrom, View view, Bundle bundle) {
        this.mActivity = mActivity;
        this.wherefrom = wherefrom;
        this.view = view;
        this.bundle = bundle;

        mUid = SaveCurrentUidUtil.getCurrentUid(mActivity);

        if (bundle != null) {
            mShareContent = bundle.getString("shareContent");
            mShareTitle = bundle.getString("shareTitle");
            mShareUrl = bundle.getString("shareUrl");
            mShareIcon = bundle.getString("shareBitmapUrl");
            if (TextUtils.isEmpty(mShareIcon)) {     //图片的URL为空就使用默认图片
                image = new UMImage(mActivity, R.drawable.ic_launcher);
            } else {
                image = new UMImage(mActivity, mShareIcon);// bitmap文件
            }

        }

        mHttpUtil.setHttpRequesListener(this);

        /**
         * 分享View的视图到各个平台
         */
        toShareImage();
    }

    private void toShareImage() {
        if (wherefrom != null) {
            UMWeb web = new UMWeb(mShareUrl);
            web.setTitle(mShareTitle);//标题
            web.setThumb(image);  //缩略图
            web.setDescription(mShareContent);//描述
            switch (wherefrom) {
                case "WEIXIN":
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN).withMedia(web).setCallback(umShareListener).share();
                    break;

                case "WEIXIN_CIRCLE":
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).withMedia(web).setCallback(umShareListener).share();

                    break;
                case "QQZONE":
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QZONE).withMedia(web).setCallback(umShareListener).share();
                    break;
                case "QQ":
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.QQ).withMedia(web).setCallback(umShareListener).share();
                    break;
                case "SINA":
                    new ShareAction(mActivity).setPlatform(SHARE_MEDIA.SINA).withMedia(web).setCallback(umShareListener).share();
                    break;

                default:
                    break;
            }
        }

    }

    private UMShareListener umShareListener = new UMShareListener() {
        private SHARE_MEDIA mPlatform;

        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            L.hintE("platform" + platform);

            Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT).show();

            mPlatform = platform;
            startThreadDelayedRequest();

        }

        protected void startThreadDelayedRequest() {
            new Thread() {
                public void run() {
                    handler.sendEmptyMessageDelayed(0, (long) (Math.random() * 1000 + 1));
                }
            }.start();

        }

        private Handler handler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (mPlatform == SHARE_MEDIA.WEIXIN) {
                    sendShareSuccessRequest("wechat");
//                    if (type.equals("4") || type.equals("5")) {
//                        sendShareSuccessRequest();
//                    }
                } else if (mPlatform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    sendShareSuccessRequest("moments");
//                    if (type.equals("4") || type.equals("5")) {
//                        sendShareSuccessRequest();
//                    }
                } else if (mPlatform == SHARE_MEDIA.QQ) {
                    sendShareSuccessRequest("qq");
//                    if (type.equals("4") || type.equals("5")) {
//                        sendShareSuccessRequest();
//                    }
                } else if (mPlatform == SHARE_MEDIA.QZONE) {
                    sendShareSuccessRequest("qqzone");
//                    if (type.equals("4") || type.equals("5")) {
//                        sendShareSuccessRequest();
//                    }
                } else if (mPlatform == SHARE_MEDIA.SINA) {
                    sendShareSuccessRequest("sina");
//                    if (type.equals("4") || type.equals("5")) {
//                        sendShareSuccessRequest();
//                    }
                }
            }
        };

        /**
         * 分享零钱罐成功送体验金
         */
        private void sendShareSuccessRequest(String ditch) {
            RequestParams params = new RequestParams(Constant.SETTINGUP_ADDSHARELQGLOG);
            params.addBodyParameter("user_userunid", mUid);
            params.addBodyParameter("ditch", ditch);
            L.hintE("==============" + ditch);
            mHttpUtil.sendRequest(params, RefreshType.RefreshNoPull, mActivity);
        }

        /**
         * 情人节分享后加次数
         */
        private void sendShareSuccessRequest() {
            RequestParams params = new RequestParams(Constant.LUCKYSAMBO_VALENTINESHARE);
            params.addBodyParameter("user_userunid", mUid);
            mHttpUtil.sendRequest(params, RefreshType.RefreshNoPull, mActivity);
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(mActivity, "分享失败", Toast.LENGTH_SHORT).show();
            if (t != null) {
                Log.d("throw", "throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(mActivity, "分享取消", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onFailure(String url, String errorContent) {

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {

    }
}

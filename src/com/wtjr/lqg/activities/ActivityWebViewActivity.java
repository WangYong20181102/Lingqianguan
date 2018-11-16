package com.wtjr.lqg.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.socialize.UMShareAPI;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.ActivityData;
import com.wtjr.lqg.base.PopupPage;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.AnimationShowState;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.ImageTools;
import com.wtjr.lqg.utils.JointUtils;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.NetworkUtil;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.ProgressWebView;
import com.wtjr.lqg.widget.ProgressWebView.OnScrollChangedCallback;

import org.xutils.HttpManager;
import org.xutils.x;

import java.lang.ref.WeakReference;


/**
 * 显示网页webview版的活动
 */
public class ActivityWebViewActivity extends BaseActivity implements OnClickListener {
    public static int MARKDIALOH = 1;
    public static ProgressWebView webView;

    /**
     * 访问的网址
     */
    private String mUrl;

    /**
     * 是否访问的是协议(0不是,1是零钱罐借款协议,2是零钱罐债权转让协议)
     */
    private int isAgreement;

    /**
     * 标名
     */
    private String name;

    /**
     * 协议文件保存路径
     */
    private String path;

    /**
     * 网页加载失败显示的控件
     */
    private LinearLayout llNoData;

    private static final String APP_CACAHE_DIRNAME = "/webcache";

    /**
     * 是否是网页加载失败
     */
    private boolean isFailure;

    private int progress;

    /**
     * 网页链接超时时长(秒)
     */
    private static final int Timeout = 20;

    private TimeoutThread thread;

    private MyHandler mHandler;

    /**
     * 显示的标题
     */
    private String titleName = "";

    private Resources resources;

    /**
     * 标题栏
     */
    private RelativeLayout rlTitle;

    /**
     * 标题下面的线
     */
    private View titleLine;

    /**
     * 分享图片
     */
    private Bitmap shareBitmap;

    /**
     * 分享图片的url
     */
    private String shareBitmapUrl;

    /**
     * 分享内容
     */
    private String shareContent;

    /**
     * 分享标题
     */
    private String shareTitle;

    /**
     * 分享链接
     */
    private String shareUrl;

    /**
     * 活动
     */
    private ActivityData mActivityData;

    /**
     * 广告
     */
    private PopupPage mPopupPage;

    private RadioButton rbDailyActivity;

    private RadioButton rbNoviceActivity;

    private ImageButton ibBack;


    /*
     * 是否是新手活动界面
     */
    private boolean isRecharge = true;

    /**
     * 分享零钱罐弹出框
     */
    private Dialog dialog;
    private Bundle bundle = new Bundle();
    /**
     * 新手活动
     */
    private String noviceUrl;
    /**
     * 日常活动
     */
    private String dailyUrl;
    /**
     * 邀请有奖
     */
    private String invitationUrl;

    /**
     * 日常活动或者新手活动(false 新手活动  true 日常活动)
     */
    private boolean bDailActivityOrNewActiity = false;

    private static class MyHandler extends Handler {
        private WeakReference<ActivityWebViewActivity> mStatus;

        public MyHandler(ActivityWebViewActivity activity) {
            mStatus = new WeakReference<ActivityWebViewActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityWebViewActivity activity = mStatus.get();
            if (activity == null) {
                return;
            }
            int what = msg.what;
            if (what == 1) {
                activity.progress = activity.webView.getProgress();
            } else {
                DialogUtil.promptDialog(activity, DialogUtil.HEAD_SERVICE, "网络连接超时!");

                activity.webView.setVisibility(View.GONE);
                activity.llNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.activity_activity_webview);

        resources = app.getResources();

        mHandler = new MyHandler(this);

        start();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    @Override
    public void initIntent() {
        mActivityData = (ActivityData) getIntent().getSerializableExtra(ActivityData.class.getName());
        mPopupPage = (PopupPage) getIntent().getSerializableExtra(PopupPage.class.getName());
        bDailActivityOrNewActiity = getIntent().getBooleanExtra("bDailActivityOrNewActiity", false);

        if (mActivityData != null) {
            mUrl = JointUtils.httpJointInterfaceAddress(mActivityData.activeUrl);
            //分享四要素
            shareUrl = JointUtils.httpJointUid(mActivityData.activeShareUrl, mUid);
            shareTitle = mActivityData.activeShareTitle;
            shareContent = mActivityData.activeShareContent;
            shareBitmapUrl = JointUtils.httpJointImageAddress(mActivityData.activeShareImage);
            bundle = JointUtils.shareFourElements(shareUrl, shareTitle, shareContent, shareBitmapUrl);
            return;
        }

        if (mPopupPage != null) {
            mUrl = mPopupPage.pageUrl;
            return;
        }

        noviceUrl = SharedPreferencesUtil.getPrefString(this, "noviceUrl", "") + "?uid=" + mUid + "&code=123456";
        dailyUrl = SharedPreferencesUtil.getPrefString(this, "dailyUrl", "") + "?uid=" + mUid + "&code=123456";
        invitationUrl = SharedPreferencesUtil.getPrefString(this, "InvitationUrl", "") + "?uid=" + mUid;
        if (bDailActivityOrNewActiity) {
            mUrl = dailyUrl;
        } else {
            mUrl = noviceUrl;
        }
    }

    @Override
    public void findViewById() {
        webView = (ProgressWebView) findViewById(R.id.webView1);

        llNoData = (LinearLayout) findViewById(R.id.ll_noData);

        ibBack = (ImageButton) findViewById(R.id.ib_activity_back);
        rbNoviceActivity = (RadioButton) findViewById(R.id.rb_noviceActivity);
        rbDailyActivity = (RadioButton) findViewById(R.id.rb_dailyActivity);
    }

    @Override
    public void setListener() {
        ibBack.setOnClickListener(this);

        rbNoviceActivity.setOnClickListener(this);
        rbDailyActivity.setOnClickListener(this);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                L.hintD("mUrl===========" + url);

                if (url.equals(Constant.H5_GOTO_RECHARGE1000)) {// 新手活动充值1000
                    Intent intent = new Intent(ActivityWebViewActivity.this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    startActivity(intent);
                } else if (url.equals(Constant.H5_GOTO_RECHARGE100)) {// 新手活动充值100
                    Intent intent = new Intent(ActivityWebViewActivity.this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    intent.putExtra("IsRecharge100", true);
                    startActivity(intent);
                } else if (url.equals(Constant.H5_GOTO_APP1)) {// 关注微信公众号
                    Intent intent = new Intent(ActivityWebViewActivity.this, WebViewActivity.class);
                    intent.putExtra("url", Constant.H5_HFIVEPAGE + "?uid=" + mUid);
                    intent.putExtra("TitleName", "零钱罐微信");
                    startActivity(intent);
                } else if (url.equals(Constant.H5_GOTO_SHARE)) {// 新手活动邀请有奖
                    String shareJson = SharedPreferencesUtil.getPrefString(ActivityWebViewActivity.this, "shareJson", "");
                    JSONObject jsonObject = JSON.parseObject(shareJson);
                    //判断需要分享URL是否包含uid，不包含 拼接uid
                    shareUrl = JointUtils.httpJointUid(jsonObject.getString("code"), mUid);
                    shareTitle = jsonObject.getString("activeShareTitle");
                    shareContent = jsonObject.getString("activeShareContent");
                    shareBitmapUrl = JointUtils.httpJointImageAddress(jsonObject.getString("activeShareImage"));
                    //将分享四要素放到bundle对象里面，直接传过去
                    bundle = JointUtils.shareFourElements(shareUrl, shareTitle, shareContent, shareBitmapUrl);
                    /**
                     * 分享零钱罐的弹出框
                     */
                    if (MARKDIALOH == 1) {
                        dialog = DialogUtil.upSlideShareDialog(ActivityWebViewActivity.this, "ActivityWebViewActivity", bundle);
                        MARKDIALOH++;
                    }
                    webView.setClickable(false);

                } else if (url.equals(Constant.H5_GOTO_QIU_SHARE)) {
                    dialog = DialogUtil.upSlideShareDialog(ActivityWebViewActivity.this, "ActivityWebViewActivity", bundle);
                } else if (url.equals(Constant.H5_GOTO_EXPERIENCEDETAIL)) {// 新手活动查看体验金详情
                    Intent intent = new Intent(ActivityWebViewActivity.this, TyjDetailsActivity1.class);
                    intent.putExtra(AnimationShowState.class.getName(), AnimationShowState.showAnimation);
                    startActivity(intent);
                } else if (url.equals(Constant.H5_GOTO_SHARE1) || url.equals(Constant.H5_GOTO_SHARE2) || url.equals(Constant.H5_GOTO_SHARE3)) {

                    Intent intent = new Intent(ActivityWebViewActivity.this, WebViewActivity.class);
                    intent.putExtra("url", invitationUrl);
                    intent.putExtra("TitleName", "邀请好友");
                    startActivity(intent);

                } else {
                    webview.loadUrl(url);
                }

                return true;
            }

            /**
             * 页面开始加载
             */
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                isFailure = true;

                if (thread != null) {
                    thread.stopRun();
                    thread = null;
                }

                thread = new TimeoutThread();
                thread.start();
            }

            /**
             * 页面加载完成
             */
            public void onPageFinished(WebView view, String url) {
                if (thread != null) {
                    thread.stopRun();
                    thread = null;
                }

                if (isFailure) {
                    webView.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                isFailure = false;
                webView.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            }
        });

        llNoData.setOnClickListener(this);
    }

    class TimeoutThread extends Thread {

        private boolean isTrue = true;

        public void stopRun() {
            this.isTrue = false;
        }

        public void run() {
            int time = 0;
            while (time <= Timeout && isTrue && progress < 100) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                time++;

                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);

                L.hintD("time=============" + time);

                if (time == 20 && progress == 0) {
                    Message msg2 = new Message();
                    msg2.what = 2;
                    mHandler.sendMessage(msg2);
                }
            }
        }
    }

    /**
     * 复制微信号
     */
    @SuppressWarnings("deprecation")
    private void copyWeiXinNumber(String str) {
        if (!TextUtils.isEmpty(str)) {
            ToastUtil.showToastShort(this, "零钱罐微信号已复制成功");
            // 获取剪贴板管理服务
            ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本数据复制到剪贴板
            cm.setText("lqgjinrong");
        }
    }

    @Override
    public void setTitle() {
        if (isLogin) {
            if (bDailActivityOrNewActiity) {
                rbDailyActivity.setChecked(true);
                isRecharge = false;
            } else {
                rbNoviceActivity.setChecked(true);
            }
        }
    }

    @Override
    public void initData() {

        if (!NetworkUtil.isNetworkAvailable(this)) {// 无网络
            webView.setVisibility(View.GONE);
            llNoData.setVisibility(View.VISIBLE);
            isFailure = false;
        }

        if (Build.VERSION.SDK_INT >= 19) {// 为了避免报错--->java.util.concurrent.TimeoutException:
            // android.view.ThreadedRenderer.finalize()
            // timed out after 10 seconds
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        //从Android5.0开始，WebView默认不支持同时加载Https和Http混合模式,如需加载添加以下代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.loadUrl(mUrl);

        initWebView();

        L.hintI("shareBitmapUrl=============" + shareBitmapUrl);
    }

    @SuppressWarnings("deprecation")
    private void initWebView() {
        // 扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        webView.getSettings().setLoadWithOverviewMode(true);

        if (isAgreement != 0) {
            // 设置可以支持缩放
            webView.getSettings().setSupportZoom(true);
            // 设置出现缩放工具
            webView.getSettings().setBuiltInZoomControls(true);
        }

        // 支持javascript123
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setRenderPriority(RenderPriority.HIGH);

        // 设置 缓存模式
        if (NetworkUtil.isNetworkAvailable(ActivityWebViewActivity.this)) {
            // 有网络的时候，使用这个
            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        } else {
            // 没有网络的时候使用这个，具体用法参照webview5个参数(不使用网络，只读取本地缓存数据)
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
        }

        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        // 开启 database storage API 功能
        webView.getSettings().setDatabaseEnabled(true);
        String cacheDirPath = getFilesDir().getAbsolutePath() + APP_CACAHE_DIRNAME;
        // String cacheDirPath
        // =getCacheDir().getAbsolutePath()+Constant.APP_DB_DIRNAME;
        webView.getSettings().setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:// 关闭
                if (thread != null) {
                    thread.stopRun();
                    thread = null;
                }
                finish();

                break;
            case R.id.tv_next:// 前进

                if (isAgreement == 0) {// 前进
                    webView.goForward();
                } else if (isAgreement == 1) {// 下载零钱罐借款协议
                    String substring = mUrl.substring(0, mUrl.length() - 1);
                    String downloadUrl = substring + "3";
                    download(downloadUrl);
                } else if (isAgreement == 2) {// 下载零钱罐债权转让协议
                    String substring = mUrl.substring(0, mUrl.length() - 1);
                    String downloadUrl = substring + "4";
                    download(downloadUrl);
                } else if (isAgreement == 7) {// 下载体验金协议
                    String substring = mUrl.substring(0, mUrl.length() - 1);
                    String downloadUrl = substring + "8";
                    download(downloadUrl);
                } else if (isAgreement == 5) {// 下载借款协议活期
                    String substring = mUrl.substring(0, mUrl.length() - 1);
                    String downloadUrl = substring + "6";
                    download(downloadUrl);
                }

                break;
            case R.id.ll_noData:// 点击没数据的零妹妹
                // DialogUtil.lmmDialog(this);
                break;

            case R.id.ib_activity_back:// 后退按钮点击操作
                // 结束当前的Activity
                finish();
                break;
            case R.id.rb_noviceActivity:// 新手活动
                isRecharge = true;
                mUrl = noviceUrl;
                L.hintD("mUrl---------" + mUrl);
                webView.loadUrl(mUrl);
                break;
            case R.id.rb_dailyActivity:// 日常活动
                isRecharge = false;
                mUrl = dailyUrl;
                L.hintD("mUrl---------" + mUrl);
                webView.loadUrl(mUrl);
                break;

            default:
                break;
        }
    }

    /**
     * 下载文件
     *
     * @param downloadUrl 下载地址
     */
    private void download(String downloadUrl) {
        L.hintD(downloadUrl);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 没有sdcard
            DialogUtil.promptDialog(this, DialogUtil.HEAD_REGISTER, "没有可用的SD卡");
            return;
        }

        if (isAgreement == 1) {// 下载借款协议定期
            path = Constant.AGREEMENT_PATH + name + "_借款协议" + ".pdf";
        } else if (isAgreement == 2) {// 下载债权协议
            path = Constant.AGREEMENT_PATH + name + "_债权转让协议" + ".pdf";
        } else if (isAgreement == 7) {// 下载体验金协议
            path = Constant.AGREEMENT_PATH + "体验金协议.pdf";
        } else if (isAgreement == 5) {// 下载借款协议活期
            path = Constant.AGREEMENT_PATH + name + "_借款协议.pdf";
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        webView.setClickable(true);
        MARKDIALOH = 1;
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();// 返回前一个页面
            return true;
        }

        if (thread != null) {
            thread.stopRun();
            thread = null;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 友盟分享如果使用的是qq或者新浪精简版jar，需要在您使用分享或授权的Activity（fragment不行）中添加如下回调代码
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }
}

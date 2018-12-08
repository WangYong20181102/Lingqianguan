package com.wtjr.lqg.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
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
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.utils.Log;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.ActivityData;
import com.wtjr.lqg.base.MessageBase;
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

import java.lang.ref.WeakReference;


/**
 * 显示网页
 */

/**
 * @author JoLie
 */
public class WebViewActivity extends BaseActivity implements OnClickListener, DownloadListener {
    public static ProgressWebView webView;

    private TextView tvTitle;

    /**
     * 关闭
     */
    private TextView tvCancel;

    /**
     * 前进
     */
    private TextView tvNext;

    /**
     * 访问的网址
     */
    private String mUrl;

    /**
     * 是否访问的是协议(0不是,1是零钱罐借款协议,2是零钱罐债权转让协议,3是投资服务协议)
     */
    private int mProtocolType;

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
    private static final int Timeout = 15;

    private TimeoutThread thread;

    private MyHandler mHandler;

    /**
     * 显示的标题
     */
    private String titleName = " ";
    /**
     * 我的客服
     */
    private String showMyKeFu = "";

    private Resources resources;

    /**
     * 标题栏
     */
    private RelativeLayout rlTitle;

    /**
     * 分享图片的url
     */
    private String shareBitmapUrl;

    /**
     * 分享内容
     */
    private String shareContent;
    /**
     * 哪个界面分享（0->新年砸金蛋，1->春节分享，2->元宵分享，3->情人节分享）
     */
    private String typeParams;

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
     * 加载分享图片的控件
     */
    private ImageView ivLinshiImg;
    /**
     * 分享零钱罐弹出框
     */
    private Dialog dialog;

    /**
     * 广告
     */
    private PopupPage mPopupPage;

    private MessageBase mMessageBase;
    /**
     * 标记是否第一次点击H5的邀请好友按钮
     */
    public static int MARKDIALOH = 1;
    /**
     * 是否显示分享按钮（0  不显示 1 显示）
     */
    private int showShareFlag;
    private Bundle bundle = new Bundle();

    private static class MyHandler extends Handler {
        private WeakReference<WebViewActivity> mStatus;

        public MyHandler(WebViewActivity activity) {
            mStatus = new WeakReference<WebViewActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WebViewActivity activity = mStatus.get();
            if (activity == null) {
                return;
            }
            int what = msg.what;

            if (what == 1) {
                activity.progress = activity.webView.getProgress();
            } else {
                DialogUtil.promptDialog(activity, DialogUtil.HEAD_BAND_BANK, "网络连接超时!");

                activity.webView.setVisibility(View.GONE);
                activity.llNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        setContentView(R.layout.activity_webview);

        resources = app.getResources();
        mHandler = new MyHandler(this);

        start();
    }

    @Override
    public void initIntent() {

        mMessageBase = (MessageBase) getIntent().getSerializableExtra(MessageBase.class.getName());
        mActivityData = (ActivityData) getIntent().getSerializableExtra(ActivityData.class.getName());
        mPopupPage = (PopupPage) getIntent().getSerializableExtra(PopupPage.class.getName());

        mUrl = getIntent().getStringExtra("url");
        titleName = getIntent().getStringExtra("TitleName");
        showMyKeFu = getIntent().getStringExtra("showMyKeFu");
        typeParams = getIntent().getStringExtra("typeParams");

        if (mMessageBase != null) {
            titleName = mMessageBase.newsTitle;
            mUrl = mMessageBase.newsUrl;
            showShareFlag = mMessageBase.newsFlag;
            //分享四要素
            shareUrl = JointUtils.httpJointUid(mMessageBase.newsShareUrl, mUid);
            shareTitle = mMessageBase.newsShareTitle;
            shareContent = mMessageBase.newsShareContent;
            shareBitmapUrl = JointUtils.httpJointImageAddress(mMessageBase.newsShareImage);

            bundle = JointUtils.shareFourElements(shareUrl, shareTitle, shareContent, shareBitmapUrl);
        }

        if (mActivityData != null) {
            mUrl = mActivityData.activeUrl;
            titleName = mActivityData.activeTitle;
            showShareFlag = mActivityData.activeFlag;
            //分享四要素
            shareUrl = JointUtils.httpJointUid(mActivityData.activeShareUrl, mUid);
            shareTitle = mActivityData.activeShareTitle;
            shareContent = mActivityData.activeShareContent;
            shareBitmapUrl = JointUtils.httpJointImageAddress(mActivityData.activeShareImage);

            bundle = JointUtils.shareFourElements(shareUrl, shareTitle, shareContent, shareBitmapUrl);
        }

        if (mPopupPage != null) {
            mUrl = mPopupPage.pageUrl;
            titleName = mPopupPage.pageTitle;
            showShareFlag = mPopupPage.pageFlag;
            //分享四要素
            shareUrl = JointUtils.httpJointUid(mPopupPage.pageShareUrl, mUid);
            shareTitle = mPopupPage.pageShareTitle;
            shareContent = mPopupPage.pageShareContent;
            shareBitmapUrl = JointUtils.httpJointImageAddress(mPopupPage.pageShareImage);
            //将分享四要素传入bundle对象
            bundle = JointUtils.shareFourElements(shareUrl, shareTitle, shareContent, shareBitmapUrl);

        }
        mUrl = JointUtils.httpJointInterfaceAddress(mUrl);
        Log.i("LogContent", "连接可用============================" + mUrl);

    }

    @Override
    public void findViewById() {
        // 标题
        tvTitle = (TextView) findViewById(R.id.tv_title_name);
        // 关闭
        tvCancel = (TextView) findViewById(R.id.tv_back);
        // 前进
        tvNext = (TextView) findViewById(R.id.tv_next);

        webView = (ProgressWebView) findViewById(R.id.webView1);

        llNoData = (LinearLayout) findViewById(R.id.ll_noData);

        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);

        ivLinshiImg = (ImageView) findViewById(R.id.iv_linshi_img);
    }

    @Override
    public void setListener() {
        webView.setDownloadListener(this);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onLoadResource(WebView view, String url) {
                L.hintD("(onLoadResource)url---------------" + url);
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
                L.hintD("(shouldOverrideUrlLoading)url---------------" + url);
                if (url != null && url.contains("/?wx=")) {// 零钱罐微信中的按钮url
                    // 截取前面一截用来判断url
                    String wxUrl = url.substring(0, url.indexOf("/?wx="));
                    // 微信号
                    String weiXingHao = url.substring(url.indexOf("/?wx=") + 5, url.length());

                    // 打开微信
                    boolean checkApkExist = checkApkExist(WebViewActivity.this, Constant.WEIXIN_PACKAGE);
                    if (checkApkExist) {// 已安装微信
                        copyWeiXinNumber(weiXingHao);
                    } else {
                        DialogUtil.promptDialog(WebViewActivity.this, DialogUtil.HEAD_BAND_BANK, "尚未安装微信");
                    }
                } else if (url.equals(Constant.H5_WE_GOTO_TOUZI)) {// 关于我们中的按钮跳转url
                    Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                    startActivity(intent);

                    // 更新前3个界面
                    Intent mIntent = new Intent(Constant.UPDATE_ALL);
                    mIntent.putExtra("ShowFragmentLocation", 2);
                    sendBroadcast(mIntent, Manifest.permission.receiver_permission);
                } else if (url.equals(Constant.H5_GOTO_EXPERIENCEDETAIL)) {// 新手活动查看体验金详情
                    Intent intent = new Intent(WebViewActivity.this, TyjDetailsActivity1.class);
                    intent.putExtra(AnimationShowState.class.getName(), AnimationShowState.showAnimation);
                    startActivity(intent);
                } else if (url.equals(Constant.H5_GOTO_RECHARGE1000)) {// 新手活动充值1000
                    Intent intent = new Intent(WebViewActivity.this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    startActivity(intent);
                } else if (url.equals(Constant.H5_GOTO_RECHARGE100)) {// 新手活动充值100
                    Intent intent = new Intent(WebViewActivity.this, RechargeActivity.class);
                    intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                    intent.putExtra("IsRecharge100", true);
                    startActivity(intent);
                } else if (url.equals(Constant.H5_GOTO_SHARE)) {// 邀请有奖
                    String shareJson = SharedPreferencesUtil.getPrefString(WebViewActivity.this, "shareJson", "");
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
                        dialog = DialogUtil.upSlideShareDialog(WebViewActivity.this, "WebViewActivity", bundle);
                        MARKDIALOH++;
                    }
                    webview.setClickable(false);

                } else if (url.equals(Constant.H5_GOTO_QIU_SHARE)) {
                    dialog = DialogUtil.upSlideShareDialog(WebViewActivity.this, "WebViewActivity", bundle);
                } else if (url.equals(Constant.H5_GOTO_APP2)) {// 如何关注领体验金
                    Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
                    intent.putExtra("url", Constant.H5_INSTRUCTIONS);
                    intent.putExtra("TitleName", "如何关注公众号");
                    startActivity(intent);


                } else if (url.equals(Constant.H5_GOTO_TYJ)) {// 立即获取体验金
                    startActivity(new Intent(WebViewActivity.this, ActivityWebViewActivity.class));
                } else if (url.equals(Constant.H5_GOTO_SHARE1) || url.equals(Constant.H5_GOTO_SHARE2) || url.equals(Constant.H5_GOTO_SHARE3)) {

                    Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
                    intent.putExtra("url", Constant.H5_SHARE_LQG + "?uid=" + mUid);
                    intent.putExtra("TitleName", "邀请好友");
                    startActivity(intent);

                } else if (url.equals(Constant.H5_GOTO_VIDEO)) {// 常见问题点击H5轮播图到播放视频
                    Intent intent = new Intent(WebViewActivity.this, WebViewActivity.class);
                    intent.putExtra("url", Constant.H5_ABOUT_US);
                    intent.putExtra("TitleName", "150了解零钱罐");
                    startActivity(intent);
                } else if (url.equals(Constant.H5_GOTO_ZHICHI)) {// 智齿客服
//                    Information info = new Information();
//                    info.setAppkey("ce00f9474d614e1a9735c72ec58355ae");
//                    //注意：uid为用户唯一标识，不能传入一样的值
//                    info.setUid(mUid);
//                    //用户昵称，选填
//                    info.setUname(app.mAccountData.nickName);
//                    //是否使用语音功能 true使用 false不使用   默认为true
//                    info.setUseVoice(false);
//                    //是否使用机器人语音功能 true使用 false不使用 默认为false
//                    info.setUseRobotVoice(false);
//                    info.setColor("#ffca60");   //标题栏背景
//                    //返回时是否弹出满意度评价
//                    info.setShowSatisfaction(false);
                    /**
                     * @param context 上下文对象
                     * @param information 初始化参数
                     */
//                    SobotApi.startSobotChat(WebViewActivity.this, info);

                    ToastUtil.showToastShort(WebViewActivity.this,"敬请期待。。。");


                } else if (url.startsWith("tel:")) {// 常见问题 提现问题拨打电话跳转
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } else if (url.contains("lqg://app.com?changeTitle")) {
                    Uri uri1 = Uri.parse(url);
                    tvTitle.setText(uri1.getQueryParameter("changeTitle"));
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

                if (!TextUtils.isEmpty(showMyKeFu)) {        //我的客服标题显示
                    if (url.contains("/pages/mykefu.html")) {
                        tvTitle.setText(titleName);
                    } else {
                        tvTitle.setText("");
                    }
                } else {
                    tvTitle.setText(titleName);
                }


                if (thread != null) {
                    thread.stopRun();
                    thread = null;
                }

                if (isFailure) {
                    webView.setVisibility(View.VISIBLE);
                    llNoData.setVisibility(View.GONE);
                }
                // 不是协议并且不是活动并
                if (mProtocolType == 0 && mActivityData == null) {
                    if (webView.canGoForward()) {// 可以前进
                        tvNext.setVisibility(View.VISIBLE);
                        tvNext.setText("前进＞");
                    } else {
                        if (mActivityData != null || mMessageBase != null || mPopupPage != null) {
                            if (showShareFlag == 1) {
                                tvNext.setText("分享");
                                tvNext.setVisibility(View.VISIBLE);
                            } else {
                                tvNext.setVisibility(View.GONE);
                            }
                        } else {
                            tvNext.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                L.hintD("===========onReceivedError============");

                isFailure = false;
                //
                webView.setVisibility(View.GONE);
                llNoData.setVisibility(View.VISIBLE);
            }
        });

        tvCancel.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        llNoData.setOnClickListener(this);
    }

    /**
     * 根据包名判断应用是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
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

                L.hintD(progress + "=============" + time);

                // 网页请求超时
                if (time >= Timeout && progress <= 10) {
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
    private void copyWeiXinNumber(String wxNumber) {
        if (!TextUtils.isEmpty(wxNumber)) {
            ToastUtil.wxToast(this);

            // 获取剪贴板管理服务
            ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本数据复制到剪贴板
            cm.setText(wxNumber);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = getPackageManager().getLaunchIntentForPackage(Constant.WEIXIN_PACKAGE);
                    startActivity(intent);
                }
            }, 1500);
        }
    }

    @Override
    public void setTitle() {
        if (!TextUtils.isEmpty(titleName) && titleName.equals("新手红包")) {
            rlTitle.setBackgroundColor(resources.getColor(R.color.lqb_head_bg));
//            titleLine.setVisibility(View.GONE);
        } else {
            rlTitle.setBackgroundColor(resources.getColor(R.color.white));
            tvCancel.setTextColor(resources.getColor(R.color.black));
            tvNext.setTextColor(resources.getColor(R.color.black));
            tvTitle.setTextColor(resources.getColor(R.color.black));
        }
        tvCancel.setText("关闭");
        tvCancel.setVisibility(View.VISIBLE);
        tvNext.setText("前进＞");

        tvTitle.setText(titleName);

        if (mActivityData != null) {
            tvTitle.setText(mActivityData.activeTitle);
        }

        if (mPopupPage != null) {
            tvTitle.setText(mPopupPage.pageTitle);
        }

        if (TextUtils.isEmpty(mUrl)){
            return;
        }
       if (mUrl.equals(Constant.H5_COMPANY)) {
            mProtocolType = 8;//为了不出现前进按钮
        }else if (mUrl.contains(Constant.LQB_QUERYLOANPROTOCOL)) {
            tvNext.setText("下载协议");
            tvNext.setTextColor(Color.BLACK);
            mProtocolType = 1;
        } else if (mUrl.contains(Constant.LQB_QUERYTRANSFERPROTOCOL)) {
            tvNext.setText("下载协议");
            tvNext.setTextColor(Color.BLACK);
            mProtocolType = 2;
        } else if (mUrl.contains(Constant.INVESTMENT_QUERYIFADADAUSERTEMPLATE)) {
            tvNext.setText("下载协议");
            tvNext.setTextColor(Color.BLACK);
            mProtocolType = 3;
        } else if (mUrl.contains(Constant.LQB_ANDROIDMARKET)) {
            mProtocolType = 8;  //防止出现前进按钮
        }

        // 判断是否是活动界面跳转过来
        if (mActivityData != null || mMessageBase != null || mPopupPage != null) {
            if (showShareFlag == 1) {
                tvNext.setText("分享");
                tvNext.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void initData() {
        tvCancel.setVisibility(View.VISIBLE);

        if (!NetworkUtil.isNetworkAvailable(this)) {// 无网络
            webView.setVisibility(View.GONE);
            llNoData.setVisibility(View.VISIBLE);
            isFailure = false;
        }

  /*      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {// 为了避免报错--->java.util.concurrent.TimeoutException:
            // android.view.ThreadedRenderer.finalize()
            // timed out after 10 seconds
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }*/
        //从Android5.0开始，WebView默认不支持同时加载Https和Http混合模式,如需加载添加以下代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        initWebView();

        webView.loadUrl(mUrl);


        Bitmap shareBitmap = ImageTools.getBitmap(this, R.drawable.ic_launcher);


        app.setDisplayImage(shareBitmapUrl, ivLinshiImg, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // menuShareUtils = new UmengMenuShareUtils(
                // WebViewActivity.this, mUid, mUrl, shareTitle,
                // shareContent, loadedImage, false, httpUtil);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    @SuppressWarnings("deprecation")
    private void initWebView() {
        // 扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        // 自适应屏幕
        webView.getSettings().setLoadWithOverviewMode(true);

        if (mProtocolType != 0) {
            // 设置可以支持缩放
            webView.getSettings().setSupportZoom(true);
            // 设置出现缩放工具
            webView.getSettings().setBuiltInZoomControls(true);
        }

        // 支持javascript123
        webView.getSettings().setJavaScriptEnabled(true);


        webView.getSettings().setRenderPriority(RenderPriority.HIGH);

        webView.setWebChromeClient(new WebChromeClient());


        // 设置 缓存模式
        if (NetworkUtil.isNetworkAvailable(WebViewActivity.this)) {
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
                String string = tvNext.getText().toString();
                if (string.equals("分享")) {

                    dialog = DialogUtil.upSlideShareDialog(WebViewActivity.this, "WebViewActivity", bundle);

                } else {
                    if (mProtocolType == 0) {// 前进
                        webView.goForward();
                    } else if (mProtocolType == 1) {// 下载零钱罐借款协议
                        String substring = mUrl.substring(0, mUrl.length() - 1);
                        String downloadUrl = substring + "3";
                        download(downloadUrl);
                    } else if (mProtocolType == 2) {// 下载零钱罐债权转让协议
                        String substring = mUrl.substring(0, mUrl.length() - 1);
                        String downloadUrl = substring + "4";
                        download(downloadUrl);
                    } else if (mProtocolType == 7) {// 下载体验金协议
                        String substring = mUrl.substring(0, mUrl.length() - 1);
                        String downloadUrl = substring + "8";
                        download(downloadUrl);
                    } else if (mProtocolType == 5) {// 下载借款协议活期
                        String substring = mUrl.substring(0, mUrl.length() - 1);
                        String downloadUrl = substring + "6";
                        download(downloadUrl);
                    }
                }


                break;
            case R.id.ll_noData:// 点击没数据的零妹妹
                // DialogUtil.lmmDialog(this);
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
            DialogUtil.promptDialog(WebViewActivity.this, DialogUtil.HEAD_BAND_BANK, "没有可用的SD卡");
            return;
        }

        if (mProtocolType == 1) {// 下载借款协议定期
            path = Constant.AGREEMENT_PATH + name + "_借款协议" + ".pdf";
        } else if (mProtocolType == 2) {// 下载债权协议
            path = Constant.AGREEMENT_PATH + name + "_债权转让协议" + ".pdf";
        } else if (mProtocolType == 7) {// 下载体验金协议
            path = Constant.AGREEMENT_PATH + "体验金协议.pdf";
        } else if (mProtocolType == 5) {// 下载借款协议活期
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

    @Override
    public void onPause() {// 继承自Activity
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onResume() {// 继承自Activity
        super.onResume();
        webView.onResume();
    }
}

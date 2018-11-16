package com.wtjr.lqg.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.ChangePhoneNumberActivity;
import com.wtjr.lqg.activities.LaunchActivity;
import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.activities.MyBankActivity;
import com.wtjr.lqg.activities.PayPasswordIdCardVerifyActivity;
import com.wtjr.lqg.activities.PayPasswordSetActivity;
import com.wtjr.lqg.activities.SetingActivity;
import com.wtjr.lqg.activities.StartInvestmentActivity;
import com.wtjr.lqg.activities.WithdrawalSMSVerifyActivity;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.VersionBase;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SaveIsFirstRun;
import com.wtjr.lqg.utils.DialogUtil.OnClickNoListener;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.widget.CircleImageView;

import org.xutils.common.Callback;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.KeyValue;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    private String secretKey = "WODETIdfd43";

    private HttpRequesListener mHttpRequesListener;

    private Cancelable cancelable;

    private Context mContext;

    private Dialog mLoadingDialog;

    /**
     * 刷新类型（区分是下拉操作的刷新，还是无下拉操作的刷新）</br> -有下拉操作的刷新的时候网络加载进度对话框不会显示</br>
     * -无下拉操作的刷新的时候网络加载进度对话框会显示
     */
    private RefreshType mRefreshType;

    private Dialog versionDialog;

    private static int mCount = 1;

    /**
     * APK保存的路径
     */
    private String apkPath;

    /**
     * 下载APK的时候进度条对话框
     */
    private Dialog progressDialog;

    private LqgApplication app;
    /**
     * 下载失败，再去备用地址下载的状态
     */
    private boolean loadSparedApk = true;
    /**
     * xutils请求参数
     */
    private RequestParams params;
    /**
     * App最新版本号
     */
    private String newVersion;
    /**
     * 系统公告对话框
     */
    private Dialog systemNoticeDialog;


    public HttpUtil() {
        app = (LqgApplication) x.app();
    }

    /**
     * 网络请求监听接口
     */
    public interface HttpRequesListener {
        void onFailure(String url, String errorContent);

        void onSuccess(String url, JSONBase jsonBase);
    }

    /**
     * 设置网络监听器
     *
     * @param httpRequesListener
     */
    public void setHttpRequesListener(HttpRequesListener httpRequesListener) {
        this.mHttpRequesListener = httpRequesListener;

        mRefreshType = null;
    }

    /**
     * 发送网络请求前的一些配置
     *
     * @param params
     * @throws Exception
     */
    public Dialog sendRequest(RequestParams params, RefreshType type, Context context) {


        if (mHttpRequesListener == null) {
            throw new RuntimeException("---> 没设置网络监听  <---");
        }

        this.mContext = context;
        this.mRefreshType = type;


        // 判断网络是否链接
        if (!NetworkUtil.isNetworkAvailable(context.getApplicationContext())) {
            mHttpRequesListener.onFailure(params.getUri(), "当前网络不可用\n请检查你的网络设置");
            return mLoadingDialog;
        }

        Activity activity = (Activity) mContext;
        // 加载进度对话框(不是靠拉到产生的请求 并且 不是首页或者启动页--启动页和首页没有进度对话框)
        if (mRefreshType == RefreshType.RefreshNoPull && !(activity instanceof MainActivity) && !(activity instanceof
                LaunchActivity)
                && !(activity instanceof ChangePhoneNumberActivity) && !(activity instanceof MyBankActivity) && !
                (activity instanceof StartInvestmentActivity)
                && !(activity instanceof WithdrawalSMSVerifyActivity) && !(activity instanceof
                PayPasswordIdCardVerifyActivity) && !(activity instanceof
                PayPasswordSetActivity)) {
            // 分享成功回调发送请求不弹出对话框
            if (params.toString().contains(Constant.SETTINGUP_ADDSHARELQGLOG)) {

            } else {
                mLoadingDialog = DialogUtil.loadingDialog(context);
            }
        }

        // 设备类型
        params.addBodyParameter("user_device_type", "Android");

        String phoneBrand = SystemUtil.getPhoneBrand(mContext);
        if (TextUtils.isEmpty(phoneBrand)) {
            // 设备型号
            params.addBodyParameter("user_device_num", "nabudao");
        } else {
            // 设备型号
            params.addBodyParameter("user_device_num", SystemUtil.getPhoneBrand(mContext));
        }
        // 设备识别码
        params.addBodyParameter("user_device_imei", SystemUtil.getMacAddress(mContext));
        // 系统版本
        params.addBodyParameter("user_os_version", SystemUtil.getSystemVersion());

        params.addBodyParameter("user_reg_version", SystemUtil.getAppVersionName(mContext));
        // 当前使用版本
        params.addBodyParameter("user_version", SystemUtil.getAppVersionName(mContext));
        // IP地址
        params.addBodyParameter("user_reg_ip", SystemUtil.getIpAddress());

        if (app.location == null) {
            // 经度
            params.addBodyParameter("login_longtitude", "0");
            // 纬度
            params.addBodyParameter("login_latitude", "0");
        } else {
            // 经度
            params.addBodyParameter("login_longtitude", app.location.getLongitude() + "");
            // 纬度
            params.addBodyParameter("login_latitude", app.location.getLatitude() + "");
        }

        Map<String, String> sArray = new HashMap<String, String>();
        List<KeyValue> stringParams = params.getStringParams();
        for (int i = 0; i < stringParams.size(); i++) {
            sArray.put(stringParams.get(i).key, (String) stringParams.get(i).value);
        }

        String sign = SignUtil.BuildAppsign(sArray, secretKey);
        params.addBodyParameter("sign", sign);

        // 请求超时时长
        params.setConnectTimeout(30000);
        // 发送post请求
        postRequest(params);
        return mLoadingDialog;
    }

    /**
     * 发送网络请求
     *
     * @param params
     */
    private void postRequest(final RequestParams params) {

        cancelable = x.http().post(params, new CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                Activity activity = (Activity) mContext;
                // 判断发送请求的Activity是否还存在
                if (activity.isFinishing()) {
                    return;
                }

                // 关闭进度对话框 (不是靠拉到产生的请求 并且 不是首页)
                if (mRefreshType == RefreshType.RefreshNoPull && !(activity instanceof MainActivity)) {
                    DialogUtil.cancelDialog();
                }

                StringBuffer buffer = new StringBuffer();
                List<KeyValue> stringParams = params.getStringParams();
                for (int i = 0; i < stringParams.size(); i++) {
                    buffer.append(stringParams.get(i).key + "=" + stringParams.get(i).value + "  ");// 拼接需要的sign
                }

                if (mCount % 2 == 0) {
                    L.hintI("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
                    L.hintI("◆URL◆ → " + params.getUri());
                    L.hintI("◆参数◆ → " + buffer.toString());
                    L.hintI("◆JSON◆ → " + result);
                    L.hintI("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
                } else {
                    L.hintD("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
                    L.hintD("◆URL◆ → " + params.getUri());
                    L.hintD("◆参数◆ → " + buffer.toString());
                    L.hintD("◆JSON◆ → " + result);
                    L.hintD("■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■■");
                }
                mCount++;

                // 获得解析的基类
                JSONBase jsonBase = JSONBaseUtil.getJSONBase(result);

                // 判断解析的基类是否是null、如果是null证明后台返回数据异常
                if (jsonBase == null) {
                    mHttpRequesListener.onFailure(params.getUri(), "返回数据异常");
                    return;
                }

                if (jsonBase.getAlertCode().equals("-2")) {// 另一设备登陆
                    if (!(params.getUri().equals(Constant.LAUNCH_IMAGEURL)) && !(params.getUri().equals(Constant.HOMEPAGE_TOPPOPUPPAGE))
                            && !(params.getUri().equals(Constant.ACCESSAPPINTERFACE_NOTTIEDUPCARDOPENAPP))) {  //过滤防止首页多次弹窗
                        DialogUtil.promptDialog(mContext, DialogUtil.HEAD_BAND_BANK, jsonBase.getMessage(), new OnClickYesListener() {
                                    @Override
                                    public void onClickYes() {
                                        LoginOrExitUtils.exitSuccess(mContext, app);
                                    }
                                });
                    }

                    return;
                }

                VersionBase versionBase = JSONBaseUtil.getVersionBase(jsonBase.getVersionInfo());

                String oldVersion = SystemUtil.getAppVersionName(mContext);
                newVersion = versionBase.getAndroidVersion().replace("v", "").replace("V", "");
                int compareVersion = NetworkUtil.compareVersion(oldVersion, newVersion);


                if (versionBase.AndroidIsDo == 0) {//优先弹出系统公告
                    if (!(params.getUri().equals(Constant.LAUNCH_IMAGEURL)) && !(params.getUri().equals(Constant.HOMEPAGE_TOPPOPUPPAGE))
                            && !(params.getUri().equals(Constant.ACCESSAPPINTERFACE_NOTTIEDUPCARDOPENAPP))) {
                        if (systemNoticeDialog != null) {
                            systemNoticeDialog.dismiss();
                        }
                        systemNoticeDialog = DialogUtil.dialogSystemNotice(mContext, versionBase.AndroidPromptContent);
                        return;
                    }
                } else {
                    if (compareVersion == 1) {// 需要更新
                        SetingActivity.bApplicationMarketDownload = true;
                        if (versionBase.getForceUpdate() == 1) {// 强更
                            if (!(params.getUri().equals(Constant.LAUNCH_IMAGEURL)) && !(params.getUri().equals(Constant
                                    .HOMEPAGE_TOPPOPUPPAGE)) && !(params
                                    .getUri().equals(Constant.ACCESSAPPINTERFACE_NOTTIEDUPCARDOPENAPP))) {
                                /**
                                 * 动态获取6.0版本以上手机存储权限
                                 */
                                PermissionUtils.writerReadSDcard(mContext);

                                showVersionDialog(versionBase, jsonBase);
                            }
                            return;
                        } else {
                            if (app.isUpdate) {// 控制非强制更新每次登陆app只显示一次
                                if (!(params.getUri().equals(Constant.LAUNCH_IMAGEURL)) && !(params.getUri().equals
                                        (Constant.HOMEPAGE_TOPPOPUPPAGE)) && !(params
                                        .getUri().equals(Constant.ACCESSAPPINTERFACE_NOTTIEDUPCARDOPENAPP))) {
                                    /**
                                     * 动态获取6.0版本以上手机存储权限
                                     */
                                    PermissionUtils.writerReadSDcard(mContext);

                                    showVersionDialog(versionBase, jsonBase);
                                }
                            }
                        }
                    } else {
                        SetingActivity.bApplicationMarketDownload = false;
                        if (params.getUri().equals(Constant.USERINFO_QUERYLATESTVERSION)) {// 版本检测
                            ToastUtil.showToastShort(mContext, "已是最新版本");
                            return;
                        }
                    }
                }

                if (jsonBase.getInvoking().equals("success")) {
                    mHttpRequesListener.onSuccess(params.getUri(), jsonBase);
                } else {
                    mHttpRequesListener.onFailure(params.getUri(), jsonBase.getMessage());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                // 判断发送请求的Activity是否还存在
                Activity activity = (Activity) mContext;
                if (activity.isFinishing()) {
                    return;
                }

                // 关闭进度对话框 (不是靠拉到产生的请求 并且 不是首页)
                if (mRefreshType == RefreshType.RefreshNoPull && !(activity instanceof MainActivity)) {
                    DialogUtil.cancelDialog();
                }

                L.hintE("◆URL◆ →" + params.getUri());
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String responseMsg = httpEx.getMessage();
                    String errorResult = httpEx.getResult();
                    L.hintE("responseCode=" + responseCode);
                    L.hintE("responseMsg=" + responseMsg);
                    mHttpRequesListener.onFailure(params.getUri(), "网络请求失败!");


                } else { // 其他错误
                    L.hintE("◆Error◆ →" + ex);

                    if (ex.toString().contains("SocketTimeoutException")) {
                        mHttpRequesListener.onFailure(params.getUri(), "网络连接超时!");
                    } else {
                        mHttpRequesListener.onFailure(params.getUri(), "网络连接错误");
                    }
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                mHttpRequesListener.onFailure(params.getUri(), "Cancelled");
            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 显示更新对话框
     */
    private void showVersionDialog(final VersionBase versionBase, JSONBase jsonObject) {
        Activity activity = (Activity) mContext;
        JSONObject jsonObject1 = JSON.parseObject(jsonObject.getVersionInfo());
        String strUpdateContext = jsonObject1.getString("AndroidUpdateContent");
        if (versionDialog != null) {
            versionDialog.dismiss();
        }
        if (versionBase.getForceUpdate() == 1) {// 强更

            versionDialog = DialogUtil.dialogVersionUpdate(mContext, new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    versionDialog.dismiss();
                    downloadAPKDialog(versionBase.getUrl(), 0);
                }
            }, new OnClickNoListener() {
                @Override
                public void onClickNo() {

                }
            });
        } else {// 非强制更新
            versionDialog = DialogUtil.dialogVersionUpdate(mContext, new OnClickYesListener() {
                @Override
                public void onClickYes() {
                    versionDialog.dismiss();
                    downloadAPKDialog(versionBase.getUrl(), 0);
                }
            }, new OnClickNoListener() {
                @Override
                public void onClickNo() {
                    app.isUpdate = false;
                }
            });
        }


        TextView tvContext = (TextView) versionDialog.findViewById(R.id.text_version_update_context);
        tvContext.setText(strUpdateContext);
        // 判断Activity是否还存在
        if (!activity.isFinishing() && !versionDialog.isShowing()) {
            versionDialog.dismiss();
            versionDialog.show();
        }
    }

    /**
     * 下载软件的进度对话框
     *
     * @param versionUrl
     * @param tag        0 是正常地址， 1是备用地址
     */
    private void downloadAPKDialog(final String versionUrl, int tag) {
        progressDialog = new Dialog(mContext, R.style.dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_download, null);
        // 头像
        CircleImageView imageHead = (CircleImageView) view.findViewById(R.id.circleImageView);
        imageHead.setImageResource(R.drawable.lingmeimei);
        // 标题
        final TextView tvmTitle = (TextView) view.findViewById(R.id.tvm_title);
        // 内容
        // TextView tvmContext = (TextView) view.findViewById(R.id.tvm_context);
        Button btYesSend = (Button) view.findViewById(R.id.bt_yesSend);
        Button btmNoSend = (Button) view.findViewById(R.id.btm_noSend);
        btmNoSend.setVisibility(View.GONE);
        btYesSend.setText("取消");

        final ProgressBar pbDownloadProgress = (ProgressBar) view.findViewById(R.id.pb_downloadProgress);

        progressDialog.setContentView(view);
        progressDialog.setCancelable(false);


        // 在URL中提取出文件名
        String[] split = versionUrl.split("/");
        String fileName = "lingqianguan.apk";
        if (split != null && split.length > 0) {
            fileName = split[split.length - 1];
        }

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 没有sdcard
            progressDialog.dismiss();
            DialogUtil.promptDialog(mContext, "外部存储不可用");
            return;
        }

        // APK保存的路径
        apkPath = Constant.APK_PATH + fileName;

        if (tag == 0) {//正常地址下载
            params = new RequestParams(Constant.DOWNLOAD_ADDRESS + versionUrl);
            Log.d("LogContent", "apkPath=" + apkPath);
            Log.i("LogContent", "下载地址---->" + Constant.DOWNLOAD_ADDRESS + versionUrl);
        } else if (tag == 1) {//备用地址下载
            params = new RequestParams(versionUrl);
            Log.d("LogContent", "apkPath=" + apkPath);
            Log.i("LogContent", "下载地址---->" + versionUrl);
        }
        // 设置断点续传
        params.setAutoResume(true);
        // 设置文件保存路径
        params.setSaveFilePath(apkPath);
        // 超时
        params.setConnectTimeout(15000);

        final Cancelable cancelable = x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onSuccess(File result) {
                Log.i("LogContent", "下载APK==========onSuccess");
                openFile(result);
                progressDialog.dismiss();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("LogContent", "下载APK==========" + ex.toString());
                Log.e("LogContent", "下载APK==========" + ex.getMessage());

                if (ex.getMessage().equals("maybe the file has downloaded completely")) {// 文件已经存在
                    showFileExistDialog(versionUrl);
                } else {
                    /**
                     * 下载失败再去备用地址下载APK包
                     */
                    if (loadSparedApk) {
                        downloadAPKDialog("http://down.lqgapp.com/download/lingqianguan_v" + newVersion + ".apk", 1);
                        loadSparedApk = false;
                    } else {
                        //判断是否获得读取储存权限
                        PackageManager pm = mContext.getPackageManager();
                        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE",
                                "com.wtjr.lqg"));
                        if (permission) {
                            DialogUtil.promptDialog(mContext, DialogUtil.HEAD_BAND_BANK, "下载失败!");
                        } else {
                            DialogUtil.promptDialog(mContext, DialogUtil.HEAD_BAND_BANK,
                                    "下载失败\n点击确定获得手机存储权限", new OnClickYesListener() {
                                        @Override
                                        public void onClickYes() {
                                            if (Build.VERSION.SDK_INT >= 23) {
                                                /**
                                                 * 动态获取6.0版本以上手机存储权限
                                                 */
                                                PermissionUtils.writerReadSDcard(mContext);
                                            } else {
                                                File apkFile = new File(apkPath);
                                                openFile(apkFile);
                                            }
                                        }
                                    });
                        }
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(CancelledException arg0) {
                Log.v("LogContent", "下载APK==========onCancelled");

            }

            @Override
            public void onFinished() {
                Log.v("LogContent", "下载APK==========onFinished");
            }

            @Override
            public void onLoading(long total, long current, boolean arg2) {
                int progress = (int) (current * 100 / total);
                pbDownloadProgress.setProgress(progress);
                tvmTitle.setText("APP正在下载中(" + progress + "%)");

                progressDialog.show();
                if (progress == 100) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onStarted() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onWaiting() {
                // TODO Auto-generated method stub

            }
        });

        // 取消下载
        btYesSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelable.cancel();
                progressDialog.dismiss();

                loadSparedApk = true;
            }
        });
    }

    /**
     * 运行文件
     *
     * @param apkFile 要运行的文件
     */
    private void openFile(File apkFile) {
        //保存切换为更新的状态，再次启动app打开引导页
        SaveIsFirstRun.setIsFirstRun(app);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileProvider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }

    /**
     * 显示文件已存在对话框
     */
    private void showFileExistDialog(final String versionUrl) {
        DialogUtil.selectDialog(mContext, DialogUtil.HEAD_REGISTER, "温馨提示", "文件已存在", "安装", new OnClickYesListener() {
            @Override
            public void onClickYes() {
                File file = new File(apkPath);
                openFile(file);
            }
        }, "重新下载", new OnClickNoListener() {

            @Override
            public void onClickNo() {
                File file = new File(apkPath);
                file.delete();
                downloadAPKDialog(versionUrl, 0);
            }
        });
    }

    /**
     * 取消请求
     */
    public void closeRequest() {
        if (cancelable != null) {
            cancelable.cancel();
        }
    }
}

package com.wtjr.lqg.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.ActivityWebViewActivity;
import com.wtjr.lqg.activities.PayBackPasswordActivity;
import com.wtjr.lqg.activities.WebViewActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RollInType;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.widget.CircleImageView;
import com.wtjr.lqg.widget.MoneyEditText;

import pl.droidsonroids.gif.GifImageView;

import static com.wtjr.lqg.R.id.bt_yesSend;
import static com.wtjr.lqg.R.style.dialog;

/**
 * 简单的对话框提示工具类
 *
 * @author dell
 */
public class DialogUtil {

    private static TextView shareMoney;
    private static Button btYesSend;

    // 因为本类不是activity所以通过继承接口的方法获取到点击的事件
    public interface OnClickYesListener {
        abstract void onClickYes();
    }

    public interface OnClickNoListener {
        abstract void onClickNo();
    }

    /**
     * 回调输入框金额
     */
    public interface OnSetMoneyListener {
        abstract void getMoney(String money);
    }

    /**
     * 对话框的点击事件
     *
     * @param dialog
     * @param bt_yesSend
     * @param bt_noSend
     * @param listenerYes
     * @param listenerNo
     */
    private static void myOnClickListener(final Dialog dialog, Button bt_yesSend, Button bt_noSend, final OnClickYesListener listenerYes,
                                          final OnClickNoListener listenerNo) {
        /**
         * 正确
         */
        bt_yesSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerYes != null) {
                    listenerYes.onClickYes();
                }
                dialog.dismiss();
            }
        });
        /**
         * 错误
         */
        bt_noSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 如果取消被点击
                if (listenerNo != null) {
                    listenerNo.onClickNo();
                }
                dialog.dismiss();
            }
        });

    }

    /**
     * 判断是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    // -----------------------------------------------------------加载进度条--------------------------------------------------------------
    private static Dialog loadingDialog;

    /**
     * 加载进度条对话框
     */
    public static Dialog loadingDialog(Context context) {

        if (loadingDialog != null && loadingDialog.isShowing()) {
            return loadingDialog;
        }

        loadingDialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        loadingDialog.setContentView(view);
        loadingDialog.setCancelable(false);
        // loadingDialog.setCancelable(true);//可取消的

        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            loadingDialog.show();
        } else {
            return null;
        }

        if (customDialog != null && customDialog.isShowing() && !activity.isFinishing()) {
            customDialog.dismiss();
            customDialog = null;
        }

        return loadingDialog;
    }

    /**
     * 取消加载进度条对话框
     */
    public static void cancelDialog() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
                loadingDialog = null;
            }
        }
    }

    // -----------------------------------------------------------加载进度条--------------------------------------------------------------

    // ------------------------------------------------------------- TODO
    // 自定义零妹妹对话框------------------------------------------------------------------
    /**
     * 登陆头像
     */
    public final static int HEAD_LOGIN = 1;

    /**
     * 注册设置头像
     */
    public final static int HEAD_REGISTER = 2;

    /**
     * 充值
     */
    public final static int HEAD_TOP_UP = 3;

    /**
     * 充值未绑卡头像
     */
    public final static int HEAD_BAND_BANK = 4;

    /**
     * 客服头像
     */
    public final static int HEAD_SERVICE = 5;

    /**
     * 得到零妹妹头像
     */
    public static int getLmmHead(int resId) {
        switch (resId) {
            case HEAD_LOGIN:// 登陆头像
                return R.drawable.kefu;
            case HEAD_REGISTER:// 注册头像
                return R.drawable.kefu;
            case HEAD_TOP_UP:// 提现充值
                return R.drawable.kefu;
            case HEAD_BAND_BANK:// 充值未绑卡
                return R.drawable.kefu;
            case HEAD_SERVICE:// 客服头像
                return R.drawable.kefu;
            default:
                break;
        }
        return resId;

    }

    /**
     * 一个按钮的对话框提示
     */
    public static final int PROMPT_DIALOG = 1;

    /**
     * 两个按钮的对话框提示
     */
    public static final int SELECT_DIALOG = 2;

    /**
     * 一个按钮的对话框提示
     *
     * @param context     上下文
     * @param title       标题
     * @param content     标题下面的内容
     * @param listenerYes 确定按钮监听器，传入null不响应事件
     * @return
     */
    public static Dialog promptDialog(Context context, int resId, String title, String content, OnClickYesListener listenerYes) {
        return customDialog(context, PROMPT_DIALOG, HEAD_SERVICE, title, content, null, listenerYes, null, null);
    }

    /**
     * 一个按钮的对话框提示
     *
     * @param context     上下文
     * @param title       标题
     * @param content     标题下面的内容
     * @param listenerYes 确定按钮监听器，传入null不响应事件
     * @return
     */
    public static Dialog promptDialog(Context context, int resId, String title, String content, String sure, OnClickYesListener listenerYes) {
        return customDialog(context, PROMPT_DIALOG, HEAD_SERVICE, null, content, sure, listenerYes, null, null);
    }

    /**
     * 一个按钮的对话框提示
     *
     * @param context     上下文
     * @param listenerYes 确定按钮监听器，传入null不响应事件
     * @return
     */
    public static Dialog promptDialog(Context context, int resId, String content, OnClickYesListener listenerYes) {
        return customDialog(context, PROMPT_DIALOG, resId, null, content, null, listenerYes, null, null);
    }

    /**
     * 一个按钮的对话框提示
     *
     * @param context 上下文
     * @param content 内容
     * @return
     */
    public static Dialog promptDialog(Context context, int resId, String content) {
        return customDialog(context, PROMPT_DIALOG, resId, null, content, null, null, null, null);
    }

    /**
     * 网络请求失败默认的
     *
     * @param context 上下文
     * @param content 内容
     * @return
     */
    public static Dialog promptDialog(Context context, String content, OnClickYesListener listenerYes, OnClickNoListener listenerNo) {
        return customDialog(context, SELECT_DIALOG, HEAD_SERVICE, null, content, null, listenerYes, null, listenerNo);
    }

    /**
     * 网络请求失败默认的
     *
     * @param context 上下文
     * @param content 内容
     * @return
     */
    public static Dialog promptDialog(Context context, String content, OnClickYesListener listenerYes) {
        return customDialog(context, PROMPT_DIALOG, HEAD_SERVICE, null, content, null, listenerYes, null, null);
    }

    /**
     * 网络请求失败默认的
     *
     * @param context 上下文
     * @param content 内容
     * @return
     */
    public static Dialog promptDialog2(Context context, String content, OnClickYesListener listenerYes) {
        return customDialog(context, SELECT_DIALOG, HEAD_SERVICE, null, content, null, listenerYes, null, null);
    }

    /**
     * 网络请求失败默认的
     *
     * @param context 上下文
     * @param content 内容
     * @return
     */
    public static Dialog promptDialog(Context context, String content) {
        return customDialog(context, PROMPT_DIALOG, HEAD_SERVICE, null, content, null, null, null, null);
    }

    /**
     * 两个按钮的对话框提示
     *
     * @param context     上下文
     * @param title       标题
     * @param listenerYes 确定按钮监听器，传入null不响应事件
     * @param listenerNo  取消去按钮监听器，传入null不响应事件
     * @return
     */
    public static Dialog selectDialog(Context context, int resId, String title, String text, final OnClickYesListener listenerYes,
                                      final OnClickNoListener listenerNo) {
        return customDialog(context, SELECT_DIALOG, resId, title, text, null, listenerYes, null, listenerNo);
    }

    /**
     * 两个按钮的对话框提示
     *
     * @param context     上下文
     * @param title       标题
     * @param listenerYes 确定按钮监听器，传入null不响应事件
     * @param listenerNo  取消去按钮监听器，传入null不响应事件
     * @return
     */
    public static Dialog selectDialog(Context context, int resId, String title, String text, String sysSendName,
                                      final OnClickYesListener listenerYes, String noSendName, final OnClickNoListener listenerNo) {

        return customDialog(context, SELECT_DIALOG, resId, title, text, sysSendName, listenerYes, noSendName, listenerNo);
    }

    /**
     * 两个按钮的对话框提示
     *
     * @param context     上下文
     * @param listenerYes 确定按钮监听器，传入null不响应事件
     * @param listenerNo  取消去按钮监听器，传入null不响应事件
     * @return
     */
    public static Dialog selectDialog(Context context, int resId, String text, String sysSendName,
                                      final OnClickYesListener listenerYes, String noSendName, final OnClickNoListener listenerNo) {

        return customDialog(context, SELECT_DIALOG, resId, null, text, sysSendName, listenerYes, noSendName, listenerNo);
    }

    /**
     * 提示对话框
     */
    public static Dialog customDialog;

    /**
     * 上次提示对话框所显示的Activity
     */
    public static String lastDialogActivityName = "";

    private static ImageButton tv_share_back;

    private static ImageView iv_yq_code;

    private static TextView tv_yq_code;

    private static Button btnWeixin;

    private static Button btnWeixinCircle;

    private static Button btnQzone;

    private static Button btnQq;

    private static Button btnSina;

    /**
     * 友盟分享类
     */
    private static UMShare mUMShare;

    private static LinearLayout ll_yq;

    private static RelativeLayout llShareCode;

    private static Button btnCopyUrl;

    private static String code;

    /**
     * @param context
     * @param dialogStyle
     * @param resId
     * @param title
     * @param text
     * @param sysSendName
     * @param listenerYes
     * @param noSendName
     * @param listenerNo
     * @return
     */
    public static Dialog customDialog(Context context, int dialogStyle, int resId, String title, String text, String sysSendName,
                                      final OnClickYesListener listenerYes, String noSendName, final OnClickNoListener listenerNo) {

        String currentDialogActivityName = context.getClass().getName();
        Activity activity = (Activity) context;
        // 为了避免同一个Activity多次弹出对话框
        // 如果上次对话框的Activity和当前的对话框Activity是同一个,就先关闭上次对话框再显示当前的对话框
        if (currentDialogActivityName.equals(lastDialogActivityName)) {
            if (customDialog != null && customDialog.isShowing()) {
                customDialog.dismiss();
                customDialog = null;
            }
        }
        lastDialogActivityName = currentDialogActivityName;

        resId = getLmmHead(resId);
        customDialog = new Dialog(context, R.style.dialog);
        View view = null;
        if (activity instanceof PayBackPasswordActivity) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_make_new, null);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_make, null);
        }
        // 头像
        CircleImageView imageHead = (CircleImageView) view.findViewById(R.id.circleImageView);
        imageHead.setImageResource(resId);
        // 标题
        TextView tvmTitle = (TextView) view.findViewById(R.id.tvm_title);
        // 内容
        TextView tvmContext = (TextView) view.findViewById(R.id.tvm_context);

        LinearLayout oneLinearLayout = (LinearLayout) view.findViewById(R.id.one_btn_linear);
        LinearLayout twoLinearLayout = (LinearLayout) view.findViewById(R.id.two_btn_linear);
        if (dialogStyle == PROMPT_DIALOG) {
            btYesSend = (Button) oneLinearLayout.findViewById(bt_yesSend);
        } else {
            btYesSend = (Button) twoLinearLayout.findViewById(bt_yesSend);
        }

        Button btmNoSend = (Button) view.findViewById(R.id.btm_noSend);

        // 设置标题
        if (!TextUtils.isEmpty(title)) {
            tvmTitle.setVisibility(View.VISIBLE);
            tvmTitle.setText(title);
        } else {
            tvmTitle.setVisibility(View.GONE);
        }
        // 设置内容
        if (!TextUtils.isEmpty(text)) {
            tvmContext.setText(text);
        }
        // 设置确认按钮文字
        if (!TextUtils.isEmpty(sysSendName)) {
            btYesSend.setText(sysSendName);
        }
        // 设置取消按钮文字
        if (!TextUtils.isEmpty(noSendName)) {
            btmNoSend.setText(noSendName);
        }

        // 单选隐藏一个取消按钮
        if (dialogStyle == PROMPT_DIALOG) {
            oneLinearLayout.setVisibility(View.VISIBLE);
            twoLinearLayout.setVisibility(View.GONE);
        } else {
            oneLinearLayout.setVisibility(View.GONE);
            twoLinearLayout.setVisibility(View.VISIBLE);
        }

        customDialog.setContentView(view);
        customDialog.setCancelable(false);

        // 判断Activity是否被销毁
        if (!activity.isFinishing()) {
            customDialog.dismiss();
            customDialog.show();
        }

        myOnClickListener(customDialog, btYesSend, btmNoSend, listenerYes, listenerNo);
        return customDialog;
    }

    /**
     * 底部往上滑动对话框
     *
     * @param context
     * @param layoutID
     * @return
     */
    public static Dialog upSlideDialog(Context context, int layoutID) {
        Dialog dialog = new Dialog(context, R.style.HeadChangeDialog);
        dialog.setContentView(layoutID);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setWindowAnimations(R.style.FromDownToUpStyleAnimation);
        window.setGravity(Gravity.BOTTOM);
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            dialog.show();
        }

        return dialog;
    }

    /**
     * 底部往上滑动分享零钱罐对话框
     *
     * @return
     */
    public static Dialog upSlideShareDialog(final Activity mActivity, final String forweb, final Bundle bundle) {
        final String mUid = SaveCurrentUidUtil.getCurrentUid(mActivity);
        final Dialog dialog = new Dialog(mActivity, R.style.HeadChangeDialog);
        dialog.setContentView(R.layout.dialog_share_lqg);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setWindowAnimations(R.style.FromDownToUpStyleAnimation);
        window.setGravity(Gravity.BOTTOM);
        lp.width = LayoutParams.MATCH_PARENT;
        lp.height = LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //弹出对话框点击物理返回按钮之后改变H5点击事件，防止重复弹出对话框
                if (forweb.equals("WebViewActivity")) {
                    WebViewActivity.MARKDIALOH = 1;
                    WebViewActivity.webView.setClickable(true);
                }
                if (forweb.equals("ActivityWebViewActivity")) {
                    ActivityWebViewActivity.MARKDIALOH = 1;
                    ActivityWebViewActivity.webView.setClickable(true);
                }
            }
        });
        Activity activity = mActivity;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            dialog.show();
        }
        // 要分享的父布局
        llShareCode = (RelativeLayout) dialog.findViewById(R.id.ll_share_code);

        ll_yq = (LinearLayout) dialog.findViewById(R.id.ll_yq);
        shareMoney = (TextView) dialog.findViewById(R.id.share_money);
        iv_yq_code = (ImageView) dialog.findViewById(R.id.iv_yq_code);
        tv_yq_code = (TextView) dialog.findViewById(R.id.tv_yq_code);
        btnWeixin = (Button) dialog.findViewById(R.id.btn_weixin);
        btnWeixinCircle = (Button) dialog.findViewById(R.id.btn_weixin_circle);
        btnQzone = (Button) dialog.findViewById(R.id.btn_Qzone);
        btnQq = (Button) dialog.findViewById(R.id.btn_qq);
        btnSina = (Button) dialog.findViewById(R.id.btn_sina);
        btnCopyUrl = (Button) dialog.findViewById(R.id.btn_copyurl);

        // 点击透明背景弹窗消失
        ll_yq.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    //dismiss 之后改变H5点击事件，防止重复弹出对话框
                    if (forweb.equals("WebViewActivity")) {
                        WebViewActivity.MARKDIALOH = 1;
                        WebViewActivity.webView.setClickable(true);
                    }
                    if (forweb.equals("ActivityWebViewActivity")) {
                        ActivityWebViewActivity.MARKDIALOH = 1;
                        ActivityWebViewActivity.webView.setClickable(true);
                    }
                }
            }
        });

        // 分享微信好友的监听
        btnWeixin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mUMShare = new UMShare(mActivity, "WEIXIN", llShareCode, bundle);
            }
        });
        // 分享微信朋友圈的监听
        btnWeixinCircle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mUMShare = new UMShare(mActivity, "WEIXIN_CIRCLE", llShareCode, bundle);
            }
        });
        // 分享QQ空间的监听
        btnQzone.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mUMShare = new UMShare(mActivity, "QQZONE", llShareCode, bundle);
            }
        });
        // 分享腾讯QQ的监听
        btnQq.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mUMShare = new UMShare(mActivity, "QQ", llShareCode, bundle);
            }
        });
        // 分享新浪微博的监听
        btnSina.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                mUMShare = new UMShare(mActivity, "SINA", llShareCode, bundle);
            }
        });

        // 点击复制链接的监听
        btnCopyUrl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                ClipboardManager c = (ClipboardManager) mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
                c.setText(bundle.getString("shareUrl"));

                ToastUtil.showToastLong(mActivity, "链接复制成功");
            }
        });

        return dialog;
    }

    /**
     * 联系零妹妹对话框
     *
     * @param context
     * @return
     */
    public static Dialog dialogFristTx(Context context) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_frist_tx, null);
        dialog.setContentView(view);

        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            dialog.show();
        }
        context = null;
        return dialog;
    }

    /**
     * 更新版本对话框
     */
    public static Dialog dialogVersionUpdate(final Context context, final OnClickYesListener listenerYes, final OnClickNoListener listenerNo) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_version_update_layout, null);

        ImageView imageClose = (ImageView) view.findViewById(R.id.image_version_icon_close);
        Button btnSure = (Button) view.findViewById(R.id.btn_version_update_sure);
        TextView tvDownload = (TextView) view.findViewById(R.id.tv_download);
        dialog.setContentView(view);
        imageClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listenerNo != null) {
                    listenerNo.onClickNo();
                }
                dialog.dismiss();
            }
        });
        btnSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerYes != null) {
                    listenerYes.onClickYes();
                }
                dialog.dismiss();
            }
        });
        tvDownload.setOnClickListener(new OnClickListener() {   //应用市场下载链接
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", Constant.LQB_ANDROIDMARKET);
                intent.putExtra("TitleName", "应用市场下载");
                context.startActivity(intent);
                dialog.dismiss();
            }
        });

//        // 判断Activity是否被销毁
//        Activity activity = (Activity) context;
//        if (!activity.isFinishing()) {
//            dialog.dismiss();
//            dialog.show();
//        }

        return dialog;
    }

    /**
     * 系统公告对话框
     */
    public static Dialog dialogSystemNotice(Context context, String promptContent) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_system_notice_layout, null);

        TextView tvSystemNoticeContext = (TextView) view.findViewById(R.id.tv_system_notice_context);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        tvSystemNoticeContext.setText(promptContent);


        // 判断Activity是否被销毁
        Activity activity = (Activity) context;
        if (!activity.isFinishing()) {
            dialog.dismiss();
            dialog.show();
        }

        return dialog;
    }

    /**
     * 切换支付通道对话框提示
     *
     * @param promptContent 支付通道
     */
    public static Dialog paymentChannelsDialogPrompt(Context context, String promptContent, final OnClickYesListener listenerYes) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.payment_channels_dialog, null);

        TextView tvPromptContent = (TextView) view.findViewById(R.id.tvm_context);
        Button btnSet = (Button) view.findViewById(R.id.bt_yesSend);
        ImageView payPromptClose = (ImageView) view.findViewById(R.id.pay_prompt_close);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        tvPromptContent.setText(promptContent + "通道升级中，请切换通道继续");
        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            dialog.show();
        }
        payPromptClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        btnSet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listenerYes != null) {
                    listenerYes.onClickYes();
                }
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * 可用余额和体验金一键转入零钱宝的对话框
     *
     * @param context
     * @param rollInType
     * @param usableExpMoney
     */
    public static Dialog rollInLqbDialog(Context context, RollInType rollInType, double usableExpMoney) {
        final Dialog dialog = new Dialog(context, R.style.dialog_full_screen);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_rollin_lqb, null);

        RelativeLayout rlDialogFull = (RelativeLayout) view.findViewById(R.id.rl_dialog_full);
        TextView tvRollinLqb = (TextView) view.findViewById(R.id.tv_rollin_lqb);
        GifImageView gifHomeRollin = (GifImageView) view.findViewById(R.id.gif_home_rollin);

        dialog.setContentView(view);
        Activity activity = (Activity) context;

        // 发送更新零钱宝数据广播
        Intent mIntent = new Intent(Constant.UPDATE_LQBDATA);
        activity.sendBroadcast(mIntent, Manifest.permission.receiver_permission);

        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            dialog.show();
        }
        rlDialogFull.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (rollInType.equals(RollInType.TYJ)) {
            gifHomeRollin.setImageResource(R.drawable.home_tyj_rollin);
        }

        if (rollInType.equals(RollInType.Balance)) {
            gifHomeRollin.setImageResource(R.drawable.home_balance_rollin);
        }
        tvRollinLqb.setText(usableExpMoney + "元转入并开始计息");
        context = null;
        return dialog;
    }

    /**
     * 转入周周升对话框
     *
     * @param context
     * @param lqbMoney 零钱宝资金
     */
    public static Dialog rollInLiCaiPlanDialog(Context context, final double lqbMoney, final OnSetMoneyListener setMoneyListener) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setCancelable(false);    //对话框以外区域不可点击
        View view = LayoutInflater.from(context).inflate(R.layout.input_licai_plane_dialog, null);
        //预计收益
        final TextView tvExpectedEarnings = (TextView) view.findViewById(R.id.tv_expected_earnings);
        //金额输入框
        final MoneyEditText moneyEditText = (MoneyEditText) view.findViewById(R.id.et_rollInMoney);
        moneyEditText.setInputMaxMoney(lqbMoney);
        //周周升利率
        TextView tvRate = (TextView) view.findViewById(R.id.tv_wwu_rate);
        tvRate.setText("7.88%～11.88%");
        //零钱宝资金
        TextView tvLqbMoney = (TextView) view.findViewById(R.id.tv_lqb_money);
        Spanned fromHtml = Html.fromHtml("<font color=#a5a5a5>" + "零钱宝资金: " + "</font>" + "<font color=#f5a019>" + MoneyFormatUtil.format(lqbMoney)
                + "</font>" + "<font color=#a5a5a5>" + "元" + "</font>");
        tvLqbMoney.setText(fromHtml);
        //全部转入按钮
        Button btnRollAll = (Button) view.findViewById(R.id.btn_allRollIn);
        dialog.setContentView(view);
        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            dialog.show();
        }
        btnRollAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                moneyEditText.setText(lqbMoney + "");
            }
        });
        //确定按钮
        final Button btnSure = (Button) view.findViewById(R.id.btn_sure);
        btnSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setMoneyListener.getMoney(moneyEditText.getText().toString());
            }
        });
        moneyEditText.setOnTextChangeListener(new MoneyEditText.OnTextChangeListener() {
            @Override
            public void setTextChange(String text) {
                if (!TextUtils.isEmpty(text) && !text.equals("0.00")) {
                    btnSure.setEnabled(true);
                } else {
                    btnSure.setEnabled(false);
                    tvExpectedEarnings.setText("0.00");
                    return;
                }
                String s = MoneyFormatUtil.sEarnings4(52,7,Double.parseDouble(text),0.0788,0.1188,0.005,0);
                tvExpectedEarnings.setText(s);
            }
        });
        //关闭按钮
        ImageButton btnClose = (ImageButton) view.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }

    /**
     * 转让确认对话框
     *
     * @param context
     * @param strMoney 收益金额
     */
    public static void transferConfirmationDialog(Context context, String strMoney, final OnClickYesListener onClickYesListener, final
    OnClickNoListener onClickNoListener) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setCancelable(false);    //对话框以外区域不可点击
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_make_transfer_confirmation, null);
        //收益金额
        TextView tvEarnings = (TextView) view.findViewById(R.id.tv_earnings);
        Spanned fromHtml = Html.fromHtml("<font color=#ff7c03>" + strMoney + "</font>" + "<font color=#414141>" + "元后续收益。"
                + "</font>");
        tvEarnings.setText(fromHtml);

        dialog.setContentView(view);
        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            dialog.show();
        }

        //确认转让
        Button btnSure = (Button) view.findViewById(R.id.btn_yesSend);
        btnSure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickYesListener.onClickYes();
                dialog.dismiss();
            }
        });
        //取消转让
        Button btnCancle = (Button) view.findViewById(R.id.btn_noSend);
        btnCancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNoListener.onClickNo();
                dialog.dismiss();
            }
        });
    }

    private static boolean bAgree = true;

    /**
     * 周周升预约
     *
     * @param context
     * @param onClickYesListener
     * @param onClickNoListener
     */
    public static void liCaiPlanDialog(Context context, String strContent, String money,OnClickYesListener onClickYesListener, OnClickNoListener onClickNoListener, int btnSize) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setCancelable(false);
        View view = LayoutInflater.from(context).inflate(R.layout.licaiplan_subscribe_dialog, null);
        //头像
        ImageView imageView = (ImageView) view.findViewById(R.id.image_licai_head);

        //标题
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_licai_plan_title);
        //内容
        TextView tvContent = (TextView) view.findViewById(R.id.tv_licai_plan_content);
        TextView tvContent1 = (TextView) view.findViewById(R.id.tv_licai_content);
        //两个按钮父布局
        RelativeLayout rlTwoBtn = (RelativeLayout) view.findViewById(R.id.rl_two_btn);
        Button btnRefuse = (Button) view.findViewById(R.id.btn_refuse);
        Button btnOpen = (Button) view.findViewById(R.id.btn_open);
        Button btnSure = (Button) view.findViewById(R.id.btn_sure);
        TextView tvTyjMoney = view.findViewById(R.id.tv_tyj_money);
        //一按钮
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.ll_agree_xieyi);
        ImageButton imageAgreeCheck = (ImageButton) view.findViewById(R.id.image_agree_check);
        TextView tvAgree = (TextView) view.findViewById(R.id.tv_agree);
        //协议
        TextView tvXieyi = (TextView) view.findViewById(R.id.tv_user_xieyi);
        //关闭按钮
        ImageButton imageBtnClose = (ImageButton) view.findViewById(R.id.btn_close);
        if (btnSize == 1) {
            rlTwoBtn.setVisibility(View.GONE);
            btnSure.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.licai_plan_subscribe_success_icon);
            tvTitle.setText(R.string.licai_plan_subscribe_yuyue_success);
            tvContent.setText(R.string.licai_plan_subscribe_yuyue_success_content);
            tvContent1.setVisibility(View.VISIBLE);
            tvContent1.setText(strContent);
            linearLayout.setVisibility(View.GONE);
            imageBtnClose.setVisibility(View.GONE);
        } else {
            rlTwoBtn.setVisibility(View.VISIBLE);
            btnSure.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.licai_plan_subscribe_icon);
            tvTitle.setText(R.string.licai_plan_subscribe_yuyue);
            tvTyjMoney.setText("领"+money+"元体验金");
            tvContent.setText(strContent);
            tvContent1.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
            imageBtnClose.setVisibility(View.VISIBLE);
        }

        dialog.setContentView(view);
        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            dialog.show();
        }

        btnOpen.setOnClickListener(new OnMyClickListener(context, onClickYesListener, onClickNoListener, dialog));
        btnRefuse.setOnClickListener(new OnMyClickListener(context, onClickYesListener, onClickNoListener, dialog));
        btnSure.setOnClickListener(new OnMyClickListener(context, onClickYesListener, onClickNoListener, dialog));
        imageAgreeCheck.setOnClickListener(new OnMyClickListener(context, onClickYesListener, onClickNoListener, imageAgreeCheck, btnOpen, dialog));
        tvAgree.setOnClickListener(new OnMyClickListener(context, onClickYesListener, onClickNoListener, imageAgreeCheck, btnOpen, dialog));
        tvXieyi.setOnClickListener(new OnMyClickListener(context, onClickYesListener, onClickNoListener, dialog));
        imageBtnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });


    }

    static class OnMyClickListener implements OnClickListener {

        private Context context;
        private ImageButton imageAgreeCheck;
        private Button btnOpen;
        private OnClickYesListener onClickYesListener;
        private OnClickNoListener onClickNoListener;
        private Dialog dialog;
        private Activity activity;

        public OnMyClickListener(Context context, OnClickYesListener onClickYesListener, OnClickNoListener onClickNoListener, Dialog dialog) {
            this.context = context;
            this.onClickYesListener = onClickYesListener;
            this.onClickNoListener = onClickNoListener;
            this.dialog = dialog;
            this.activity = (Activity) context;
        }

        public OnMyClickListener(Context context, OnClickYesListener onClickYesListener, OnClickNoListener onClickNoListener, ImageButton
                imageAgreeCheck, Button btnOpen, Dialog dialog) {
            this.context = context;
            this.imageAgreeCheck = imageAgreeCheck;
            this.btnOpen = btnOpen;
            this.onClickYesListener = onClickYesListener;
            this.onClickNoListener = onClickNoListener;
            this.dialog = dialog;
            this.activity = (Activity) context;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_refuse:   //拒绝
                    onClickNoListener.onClickNo();
                    dialog.dismiss();
                    break;
                case R.id.btn_open: //预约开启
                    onClickYesListener.onClickYes();
                    dialog.dismiss();
                    break;
                case R.id.btn_sure://知道了
                    onClickYesListener.onClickYes();
                    dialog.dismiss();
                    break;
                case R.id.image_agree_check:    //同意
                case R.id.tv_agree:
                    if (bAgree) {
                        bAgree = false;
                        imageAgreeCheck.setImageResource(R.drawable.open_account_agree_icon_yes);
                        btnOpen.setEnabled(true);
                    } else {
                        bAgree = true;
                        imageAgreeCheck.setImageResource(R.drawable.open_account_agree_icon_no);
                        btnOpen.setEnabled(false);
                    }
                    break;
                case R.id.tv_user_xieyi:    //协议
                    Intent intent = new Intent(activity, WebViewActivity.class);
                    intent.putExtra("url", Constant.PLANBOOK_TOPROTOCOL);
                    intent.putExtra("TitleName", "大满罐服务协议");
                    activity.startActivity(intent);
                    break;
            }
        }
    }

    /**
     * 一个按钮对话框
     *
     * @param context
     * @param onClickYesListener
     */
    public static void liCaiPlanSubscribeDialog(Context context, String strContent, OnClickYesListener onClickYesListener) {
        liCaiPlanDialog(context, strContent, "",onClickYesListener, null, 1);
    }

    /**
     * 两个按钮对话框
     *
     * @param context
     * @param onClickYesListener
     * @param onClickNoListener
     */
    public static void liCaiPlanSubscribeDialog(Context context, String strContent, String money,OnClickYesListener onClickYesListener, OnClickNoListener onClickNoListener) {
        liCaiPlanDialog(context, strContent, money,onClickYesListener, onClickNoListener, 2);
    }
}

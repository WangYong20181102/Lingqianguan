package com.wtjr.lqg.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.wtjr.lqg.R;

/**
 * Created by wangxu on 2016/12/20.
 */

public class BillDetailDialog {


    private static Dialog billMmDialog;
    private static String lastDialogActivityName = "";

    public BillDetailDialog() {
    }

    public static Dialog showBillMmDialog(Context context) {
        if (billMmDialog != null && billMmDialog.isShowing()) {
            return billMmDialog;
        }
        String currentDialogActivityName = context.getClass().getName();
        // 为了避免同一个Activity多次弹出对话框
        // 如果上次对话框的Activity和当前的对话框Activity是同一个,就先关闭上次对话框再显示当前的对话框
        if (currentDialogActivityName.equals(lastDialogActivityName)) {
            if (billMmDialog != null && billMmDialog.isShowing()) {
                billMmDialog.dismiss();
                billMmDialog = null;
            }
        }
        lastDialogActivityName = currentDialogActivityName;

        billMmDialog = new Dialog(context, R.style.BillDetailDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bill_mm, null);
        billMmDialog.setContentView(view);
        billMmDialog.setCancelable(true);
        Window window = billMmDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        Activity activity = (Activity) context;
        // 判断Activity是否还存在
        if (!activity.isFinishing()) {
            billMmDialog.show();
        } else {
            return null;
        }
        return billMmDialog;
    }
}

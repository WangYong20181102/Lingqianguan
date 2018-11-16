package com.wtjr.lqg.utils;

import android.text.TextUtils;
import android.widget.ImageView;

import com.wtjr.lqg.R;

/**
 * 更具银行的名字选择图片
 * 
 * @author dell
 * 
 */
public class BankImageUtil {
    public static void setBankImage(String bankName, ImageView imageView) {
        if (TextUtils.isEmpty(bankName)){
            return;
        }
        if (bankName.contains("建设")) {
            bankName = "中国建设银行";
        } else if (bankName.contains("邮政")) {
            bankName = "中国邮政储蓄银行";
        } else if (bankName.contains("工商")) {
            bankName = "中国工商银行";
        } else if (bankName.contains("民生")) {
            bankName = "中国民生银行";
        } else if (bankName.contains("农业")) {
            bankName = "中国农业银行";
        } else if (bankName.contains("招商")) {
            bankName = "招商银行";
        } else if (bankName.contains("浦发")) {
            bankName = "上海浦东发展银行";
        }

        switch (bankName) {
        case "北京银行":
            imageView.setImageResource(R.drawable.image_bank_beijing);
            break;
        case "中国工商银行":
            imageView.setImageResource(R.drawable.image_bank_gongshang);
            break;
        case "光大银行":
            imageView.setImageResource(R.drawable.image_bank_guangda);
            break;
        case "广发银行":
            imageView.setImageResource(R.drawable.image_bank_guangfa);
            break;
        case "华夏银行":
            imageView.setImageResource(R.drawable.image_bank_huaxia);
            break;
        case "中国建设银行":
            imageView.setImageResource(R.drawable.image_bank_jianshe);
            break;
        case "交通银行":
            imageView.setImageResource(R.drawable.image_bank_jiaotong);
            break;
        case "中国民生银行":
            imageView.setImageResource(R.drawable.image_bank_mingsheng);
            break;
        case "中国农业银行":
            imageView.setImageResource(R.drawable.image_bank_nongye);
            break;
        case "平安银行":
            imageView.setImageResource(R.drawable.image_bank_pingan);
            break;
        case "上海浦东发展银行":
            imageView.setImageResource(R.drawable.image_bank_pufa);
            break;
        case "上海银行":
            imageView.setImageResource(R.drawable.image_bank_shanghai);
            break;
        case "兴业银行":
            imageView.setImageResource(R.drawable.image_bank_xingye);
            break;
        case "中国邮政储蓄银行":
            imageView.setImageResource(R.drawable.image_bank_youzheng);
            break;
        case "招商银行":
            imageView.setImageResource(R.drawable.image_bank_zhaoshang);
            break;
        case "中信银行":
            imageView.setImageResource(R.drawable.image_bank_zhongxing);
            break;
        case "中国银行":
            imageView.setImageResource(R.drawable.image_bank_zhongguo);
            break;
        default:
            imageView.setImageResource(R.drawable.image_bank_moreng);
            break;
        }
    }

}

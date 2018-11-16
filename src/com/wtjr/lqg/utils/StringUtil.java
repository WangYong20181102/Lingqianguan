package com.wtjr.lqg.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    private StringUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为纯字母
     *
     * @param str
     * @return
     */
    public static boolean isAlphabet(String str) {
        Pattern pattern = Pattern.compile("[a-zA-Z]");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为纯汉字
     *
     * @param str
     * @return
     */
    public static boolean isChineseCharacters(String str) {
        Pattern pattern = Pattern.compile("[\u4e00-\u9fa5]");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否包含特殊符号
     *
     * @param str
     * @return
     */
    public static boolean isSpecialCharacters(String str) {
        Pattern pattern = Pattern.compile("^[a-zA-Z\u4E00-\u9FA5\\d]*$");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }

    /**
     * 判断是否为手机号
     *
     * @param mobiles
     * @return
     */
    public static boolean isPhone(String mobiles) {
        String str = "^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1})|(17[0-9]{1})|(14[0-9]{1})|(16[6])|(19[9]))+\\d{8})$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断密码是否符合生产密码的条件，不能是纯数字，不能输纯字母，由数字加字母或下划线结合的6到16位
     *
     * @param password 密码
     */
    public static boolean isPassword(String password) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)(?![_]+$)[0-9A-Za-z_]{6,16}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 判断密码是否符合生产密码的条件，不能是纯数字，不能输纯字母，由数字加字母或下划线结合的8到16位
     *
     * @param password 设置注册密码
     */
    public static boolean isPasswordRegist(String password) {
        Pattern p = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)(?![_]+$)[0-9A-Za-z_]{8,16}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 去掉特殊字符
     *
     * @param str
     * @return
     */
    public static boolean isExcptional(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 设置模糊手机号码
     *
     * @return
     */
    public static String setBlurryPhone(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(phoneNumber.subSequence(0, 3));
        builder.append("******");
        builder.append(phoneNumber.subSequence(9, 11));
        return builder.toString();
    }

    /**
     * 设置模糊姓名
     *
     * @return
     */
    public static String setBlurryName(String sName) {
        if (TextUtils.isEmpty(sName)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(sName.subSequence(0, 1));
        for (int i = 1; i < sName.length(); i++) {
            builder.append("*");
            if (i >= 10) {
                builder.setLength(0);
                builder.append(sName.subSequence(0, 1)).append("*********...");
                break;
            }
        }
        return builder.toString();
    }

    /**
     * 设置模糊银行卡号
     *
     * @return
     */
    public static String setBlurryBankNumber(String bankNumber) {
        if (TextUtils.isEmpty(bankNumber)) {
            return "";
        } else {
            int length = bankNumber.length();
            String blurryPhone = null;
            if (length >= 11) {
                blurryPhone = bankNumber.substring(0, length - (length - 4))
                        + "   ****   ****   " + bankNumber.substring((length - 3), length);
            } else {
                blurryPhone = "";
            }
            return blurryPhone;
        }
    }

    /**
     * 设置模糊银行卡号
     *
     * @return
     */
    public static String setBlurryBankNumber2(String bankNumber) {
        if (TextUtils.isEmpty(bankNumber)) {
            return "";
        } else {
            int length = bankNumber.length();
            String blurryPhone = null;
            if (length >= 11) {
                blurryPhone = bankNumber.substring(0, length - (length - 4))
                        + " **** **** " + bankNumber.substring((length - 3), length);
            } else {
                blurryPhone = "";
            }
            return blurryPhone;
        }
    }
}

package com.wtjr.lqg.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.text.TextUtils;

/**
 * 货币格式化
 *
 * @author dell
 */
public class MoneyFormatUtil {
    /**
     * 金钱格式化，保留两位小数,格式化后有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String format(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0.00";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("#,##0.00");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     * 金钱格式化，保留两位小数,格式化后没有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String format2(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null") || moneyStr.equals("NaN")) {
            return "0.00";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("0.00");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     * 金钱格式化，保留一位小数,格式化后没有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String format3(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0.0";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("0.0");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     *
     */
    public static <T> String formatW(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0";
        }
        NumberFormat nf = new DecimalFormat("0.#");
        String testStr = nf.format(Double.parseDouble(moneyStr));

        return testStr;
    }


    /**
     * 将金钱格式化成带有万字单位结尾
     *
     * @param t 金额 小于1万会以0.X万字单位结尾
     * @return
     */
    public static <T> String formatW2(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0.00";
        }

        double money = Double.parseDouble(moneyStr);
        double w = money / 10000;
        NumberFormat nf = new DecimalFormat("0.##");
        String testStr = nf.format(w);

        return testStr + "万";
    }


    /**
     * 金钱格式化，保留两位小数,格式化后有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String formatZ(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("0");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     * 金钱格式化，保留两位小数,格式化后有逗号的
     *
     * @param <T>
     * @param t   需要转换的金额
     * @return 格式化后的金额
     */
    public static <T> String formatPersonNum(T t) {
        String moneyStr = t + "";

        if (TextUtils.isEmpty(moneyStr) || moneyStr.equals("null")) {
            return "0.00";
        }

        double money = Double.parseDouble(moneyStr);
        NumberFormat nf = new DecimalFormat("#,###");
        String testStr = nf.format(money);

        return testStr;
    }

    /**
     * 收益金额
     *
     * @param num    金额
     * @param dL     年化利率
     * @param numDay 天数
     */
    public static String sEarnings(double num, double dL, int numDay, int investmentDay) {
        // 金额乘以
        double div2 = ArithUtil.mul(num, dL);
        // 30天标
        double div = ArithUtil.div(div2, numDay, 2);
        return MoneyFormatUtil.format(ArithUtil.mul(div, investmentDay));
    }

    /**
     * 收益金额
     *
     * @param num    金额
     * @param dL     年化利率
     * @param numDay 天数
     */
    public static String sEarnings2(double num, double dL, int numDay, int investmentDay) {
        // 金额乘以
        double div2 = ArithUtil.mul(num, dL);
        // 30天标
        double div = ArithUtil.div(div2, numDay, 2);
        return MoneyFormatUtil.format2(ArithUtil.mul(div, investmentDay));
    }

    /**
     * 周周升收益金额
     *
     * @param num   金额
     * @param dL    年化利率
     * @param addDL 增长利率
     */
    public static String sEarnings3(double num, double dL, double addDL) {

        double dbSum = 0;
        double div2;
        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                div2 = ArithUtil.mul(num, dL);
            } else {
                div2 = ArithUtil.mul(num, ArithUtil.add(dL, ArithUtil.mul(addDL, i)));
            }
            // 30天标
            double div = ArithUtil.div(div2, 365);
            double mul = ArithUtil.mul(div, 7);
            dbSum = ArithUtil.add(mul, dbSum);
        }
        return MoneyFormatUtil.format2(ArithUtil.round(dbSum, 2));
    }

    /**
     * 周周升收益计算公式
     *
     * @param n     计划周期数
     * @param m     单位周期数
     * @param money 投资金额
     * @param a     基础利率
     * @param b     最高利率
     * @param c     递增利率
     * @param temp  加息利息
     * @return
     */
    public static String sEarnings4(int n, int m, double money, double a, double b, double c, double temp) {
        //总利息
        double total = 0;
        //计算利率
        double r = 0;
        for (int index = 1; index <= n; index++) {
            if ((a + c * (index - 1) >= b)) {
                r = b;
            } else {
                r = (a + c * (index - 1));
            }
            total = ArithUtil.round(total + (r * (money * m) + temp) / 365, 2);

        }
        return MoneyFormatUtil.format2(ArithUtil.round(total, 2));
    }
}

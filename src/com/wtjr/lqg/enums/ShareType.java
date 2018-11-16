package com.wtjr.lqg.enums;

/**
 * Created by WangYong on 2017/1/6.
 */

public enum ShareType {
    /**
     * 新年砸金蛋
     */
    NewYearsShare("新年砸金蛋","0"),
    /**
     * 翻倍新春包
     */
    SpringFestivalShare("翻倍新春包","1"),
    /**
     * 邀友闹元宵
     */
    SweetDumplingsShare("邀友闹元宵","2"),
    /**
     * 缘来爱情标
     */
    ValentineShare("缘来爱情标","3"),
    /**
     * 情人节分享（呼叫汪星人）
     */
    ValentineShareCall("呼叫汪星人","4"),
    /**
     * 情人节分享（发狗粮）
     */
    ValentineShareSend("发狗粮","5");

    private String tittle;
    private String params;
    private ShareType(String tittle,String params){
        this.tittle = tittle;
        this.params = params;
    }

    public String getParams() {
        return params;
    }

    public String getTittle() {
        return tittle;
    }
}

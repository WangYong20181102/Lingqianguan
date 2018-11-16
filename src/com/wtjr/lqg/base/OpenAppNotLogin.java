package com.wtjr.lqg.base;

import java.io.Serializable;

public class OpenAppNotLogin implements Serializable {
    private static final long serialVersionUID = 2075554661891657658L;
    /**
     * 首页顶部图片
     */
    public String mainPage_topimage;
    /**
     * 提示弹窗
     */
    public String alertWindow;
    /**
     * 
     */
    public String maxLqbRate;
    /**
     * 其他投资理财利率
     */
    public String otherRate;

    public String maxInvestRate;
    /**
     * 首页活动图片
     */
    public String mainPage_activeImage;

    public String minLqbRate;

    public String minInvestRate;
    /**
     * 停服公告
     */
    public String pauseService;

    @Override
    public String toString() {
        return "NotLoginOpenApp [mainPage_topimage=" + mainPage_topimage
                + ", alertWindow=" + alertWindow + ", maxLqbRate=" + maxLqbRate
                + ", otherRate=" + otherRate + ", maxInvestRate="
                + maxInvestRate + ", mainPage_activeImage="
                + mainPage_activeImage + ", minLqbRate=" + minLqbRate
                + ", minInvestRate=" + minInvestRate + ", pauseService="
                + pauseService + "]";
    }
}
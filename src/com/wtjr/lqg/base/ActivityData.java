package com.wtjr.lqg.base;

import java.io.Serializable;

public class ActivityData implements Serializable {
    private static final long serialVersionUID = -2824153951390961692L;

    public String activeTitle;

    public long activeEndTime;

    public long activeStartTime;

    /**
     * 是否可以分享（0 不可以 1 可以）
     */
    public int activeFlag;
    /**
     * 分享标题
     */
    public String activeShareTitle;
    /**
     * 轮播图片url
     */
    public String activeImage;

    public int activeId;

    /**
     * 分享内容
     */
    public String activeShareContent;

    public String activeType;

    /**
     * 分享URL
     */
    public String activeShareUrl;

    public String activeUserType;

    public int activeShareCoun;
    /**
     * 活动地址
     */
    public String activeUrl;
    /**
     * 备用活动地址
     */
    public String activeImageUrl;

    public String activeContent;

    /**
     * 分享图片
     */
    public String activeShareImage;

    public int activeTop;

    public long sysdate;
    /**
     * 1是转盘活动，其余是其他活动
     */
    public int activeTtype = 0;


    @Override
    public String toString() {
        return "ActivityData [activeTitle=" + activeTitle + ", activeUrl="
                + activeUrl + ", activeTtype=" + activeTtype + "]";
    }
}
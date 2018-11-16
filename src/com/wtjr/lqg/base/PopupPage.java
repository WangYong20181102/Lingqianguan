package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * 首页弹窗广告
 * 
 * @author JoLie
 * 
 */
public class PopupPage implements Serializable {
    private static final long serialVersionUID = -4999724533433621528L;

    public long pageEndTime;

    public String pageTitle;
    /**
     * 是否需要分享  （0 不需要   1需要）
     */
    public int pageFlag;
    /**
     * 分享内容
     */
    public String pageShareContent;
    /**
     * 分享地址
     */
    public String pageShareUrl;
    /**
     * 分享图片
     */
    public String pageShareImage;
    /**
     * 分享标题
     */
    public String pageShareTitle;


    public long pageStartTime;

    public String pageImage;

    public String pageUrl;

    public String pageContent;

    public int pageId;
    
    public int isTurntableActivity;
}

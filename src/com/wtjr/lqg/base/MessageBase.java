package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * 消息解析类
 * @author JoLie
 *
 */
public class MessageBase implements Serializable {
    private static final long serialVersionUID = -186809178385891253L;


    /**
     * 是否需要分享  （0 不需要   1需要）
     */
    public int newsFlag;
    /**
     * 分享标题
     */
    public String newsShareTitle;
    /**
     * 分享图片地址
     */
    public String newsShareImage;
    /**
     * 分享内容
     */
    public String newsShareContent;
    /**
     * 分享url
     */
    public String newsShareUrl;

    public String newsImage;

    public int newsId;

    public long newsTime;

    public String newsTitle;

    public String newContent;
    
    public String newsUrl;

    @Override
    public String toString() {
        return "MessageBase [newsImage=" + newsImage + ", newsId=" + newsId
                + ", newsTime=" + newsTime + ", newsTitle=" + newsTitle
                + ", newContent=" + newContent + "]";
    }
}
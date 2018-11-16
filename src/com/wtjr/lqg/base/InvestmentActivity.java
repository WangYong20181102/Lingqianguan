package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * 投资页面活动
 *
 * @author JoLie
 */
public class InvestmentActivity implements Serializable {
    /**
     * 序列化地址
     */
    private static final long serialVersionUID = 4741434193392216836L;
    /**
     * 活动跳转网址(url)
     */
    public String url;
    /**
     * 活动的图片(image)
     */

    public int image;
    /**
     * 活动的内容(content)
     */

    public String content;
    /**
     * 分享的图片(shareimage)
     */

    public String shareimage;
    /**
     * 分享的内容(sharecontent)
     */

    public String sharecontent;
    /**
     * 分享的标题(sharetitle)
     */

    public String sharetitle;
    /**
     * 活动是否已经结束(end)
     */
    public String end;

    @Override
    public String toString() {

        return "ActivityBase [url=" + url + ", image=" + image + ", content="
                + content + ", shareimage=" + shareimage + ", sharecontent="
                + sharecontent + ", sharetitle=" + sharetitle + ", end=" + end
                + "]";
    }

}

package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * 消息解析类
 * @author JoLie
 *
 */
public class Message implements Serializable {
    private static final long serialVersionUID = -186809178385891253L;

    public String newsImage;

    public int newsId;

    public long newsTime;

    public String newsTitle;

    public Share share;

  public class Share implements Serializable {
        private static final long serialVersionUID = 7502793029296015308L;

        public String shareContent;

        public String shareTitle;

        public String shareUrl;

        public String asString;

        public String sharePicture;

        @Override
        public String toString() {
            return "Share [shareContent=" + shareContent + ", shareTitle="
                    + shareTitle + ", shareUrl=" + shareUrl + ", asString="
                    + asString + ", sharePicture=" + sharePicture + "]";
        }
    }

    @Override
    public String toString() {
        return "Message [newsImage=" + newsImage + ", newsId=" + newsId
                + ", newsTime=" + newsTime + ", newsTitle=" + newsTitle
                + ", share=" + share + "]";
    }
}
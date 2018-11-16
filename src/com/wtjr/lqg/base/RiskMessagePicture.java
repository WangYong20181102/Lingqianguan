package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * 风控信息图片信息
 *
 * @author JoLie
 */
public class RiskMessagePicture implements Serializable {
    private static final long serialVersionUID = -7354041304350438231L;

    public String dataId;

    public String dataImagesSmallurl;

    public String dataImagesUrl;

    public String dataType;

    public int dataBorrowerId;

    public String dataRemarks;

    @Override
    public String toString() {
        return "RiskMessagePicture [dataId=" + dataId + ", dataImagesSmallurl="
                + dataImagesSmallurl + ", dataImagesUrl=" + dataImagesUrl
                + ", dataType=" + dataType + ", dataBorrowerId="
                + dataBorrowerId + ", dataRemarks=" + dataRemarks + "]";
    }
}
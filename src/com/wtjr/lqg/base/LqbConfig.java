package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * Created by WangYong on 2017/11/23.
 */

public class LqbConfig implements Serializable {
    public String notice;
    /**
     * 显示转入还是转到周周升
     */
    public int isOpenTransferinto;
    /**
     * 是否显示通知（1显示 0不显示）
     */
    public int isOpenNotice;
}

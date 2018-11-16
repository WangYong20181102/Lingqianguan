package com.wtjr.lqg.base;

import java.io.Serializable;

public class VersionBase implements Serializable {
    private static final long serialVersionUID = 5900535619406277445L;

    private int forceUpdate;

    private String updateContent;
    /**
     * 0不允许操作只能查看 , 1都可以
     */
    public int AndroidIsDo;
    /**
     * 系统公告提示内容
     */
    public String AndroidPromptContent;

    private String AndroidVersion;

    private String url;

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getAndroidVersion() {
        return AndroidVersion;
    }

    public void setAndroidVersion(String AndroidVersion) {
        this.AndroidVersion = AndroidVersion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "VersionBase [forceUpdate=" + forceUpdate + ", updateContent="
                + updateContent + ", version=" + AndroidVersion + ", url=" + url + "]";
    }

    public String getAndroidPromptContent() {
        return AndroidPromptContent;
    }

    public void setAndroidPromptContent(String androidPromptContent) {
        AndroidPromptContent = androidPromptContent;
    }

    public int getAndroidIsDo() {
        return AndroidIsDo;
    }

    public void setAndroidIsDo(int androidIsDo) {
        AndroidIsDo = androidIsDo;
    }
}
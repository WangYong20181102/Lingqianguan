package com.wtjr.lqg.base;

import java.io.Serializable;

public class JSONBase implements Serializable {
    private static final long serialVersionUID = -2802865310097108227L;
    
    private String message;

    private String invoking;

    private String alertCode;
    
    private String disposeResult;
    
    private String userType;
    
    private String versionInfo;
    

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setInvoking(String invoking) {
        this.invoking = invoking;
    }

    public String getInvoking() {
        return this.invoking;
    }

    public void setAlertCode(String alertCode) {
        this.alertCode = alertCode;
    }

    public String getAlertCode() {
        return this.alertCode;
    }
    
    public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
     * 处理数据
     * @return
     */
    public String getDisposeResult() {
        return disposeResult;
    }

    public void setDisposeResult(String disposeResult) {
        this.disposeResult = disposeResult;
    }
    

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    @Override
    public String toString() {
        return "JSONBase [message=" + message + ", invoking=" + invoking
                + ", alert_Code=" + alertCode + ", disposeResult="
                + disposeResult + "]";
    }
}
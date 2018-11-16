package com.wtjr.lqg.base;

import java.io.Serializable;

/**
 * 消息反馈
 * 
 * @author dell
 * 
 */
public class MessageFeedBack implements Serializable {
	private static final long serialVersionUID = 8997640215419915583L;
	/**
	 * 自己反馈的内容
	 */
	public String content;
	/**
	 * 自己反馈内容的时间
	 */
	public long optTime;
	/**
	 * 回复反馈的内容
	 */
	public String revert;
	/**
	 * 回复反馈内容的时间
	 */
	public long reverttime;
	/**
	 * 自己反馈0，回复反馈1
	 */
	public int userType;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getOptTime() {
		return optTime;
	}

	public void setOptTime(long optTime) {
		this.optTime = optTime;
	}

	public String getRevert() {
		return revert;
	}

	public void setRevert(String revert) {
		this.revert = revert;
	}

	public long getReverttime() {
		return reverttime;
	}

	public void setReverttime(long reverttime) {
		this.reverttime = reverttime;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	// @Override
	// public String toString() {
	// return "MessageFeedBack [content=" + content + ", addtime=" + addtime
	// + ", revert=" + revert + ", reverttime=" + reverttime
	// + ", type=" + type + "]";
	// }
}

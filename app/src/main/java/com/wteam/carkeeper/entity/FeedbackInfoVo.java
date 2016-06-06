package com.wteam.carkeeper.entity;

public class FeedbackInfoVo {

	/**
	 * 用户反馈内容
	 */
	private String content;
	/**
	 * 用户联系信息
	 */
	private String contactInformation;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContactInformation() {
		return contactInformation;
	}
	public void setContactInformation(String contactInformation) {
		this.contactInformation = contactInformation;
	}
}

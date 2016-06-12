package com.wteam.carkeeper.entity;

public class SystemInfoVo {
	/**
	 * 系统信息标题
	 */
	private String title;
	/**
	 * 系统消息内容
	 */
	private String Content;
	/**
	 * 系统消息创建时间
	 */
	private String createTime;
	/**
	 * 日期
	 */
	private String date;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}

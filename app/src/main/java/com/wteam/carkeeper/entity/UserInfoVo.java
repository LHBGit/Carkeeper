package com.wteam.carkeeper.entity;

public class UserInfoVo {
	/**
	 * 用户邮箱
	 */
	private String email;
	/**
	 * 用户头像链接地址
	 */
	private String iconUrl;
	/**
	 * 用户性别
	 */
	private String gender;
	/**
	 * 用户个性签名
	 */
	private String signature;
	/**
	 * 用户驾车车龄
	 */
	private String carAge;


	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getCarAge() {
		return carAge;
	}
	public void setCarAge(String carAge) {
		this.carAge = carAge;
	}
}

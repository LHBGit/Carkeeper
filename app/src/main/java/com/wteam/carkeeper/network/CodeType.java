package com.wteam.carkeeper.network;

public enum CodeType {
	
	//操作成功
	OPERATION_SUCCESS("250"),
	//参数错误
	ARGUMENT_ERROR("450"),
	//账户已存在
	ACCOUNT_REPEAT("451"),
	//认证失败
	AUTHC_FAIL("452"),
	//session过期
	SESSION_TIMEOUT("453"),
	//无操作权限
	UNAUTHORIZED("454"),
	//refreshtoken过期
	REFRESH_TOKEN_TIMEOUT("455"),
	//未知账户
	UNKNOW_ACCOUNT("456"),
	//未知验证码
	UNKNOW_CHECKCODE("457"),
	//操作失败
	OPERATION_FAILURE("458"),
	//未知错误
	UNKNOW_ERROR("550"),
	//服务器错误
	SERVER_ERROR("551");
	
	private String code;
	
	
	CodeType(String code){
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}

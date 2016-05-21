package com.wteam.carkeeper.network;

/**
 * Created by lhb on 2016/5/20.
 */
public class UrlManagement {
    /**
     * 服务器ip地址
     */
    public static final String BASE_URL = "http://172.16.12.98:8080/carkeeper";

    /**
     * 用户名密码注册地址
     */
    public static final String REGISTER_BY_USER_INFO = BASE_URL + "/registerByPersonInfo.action";

    /**
     * 用户名密码登录地址
     */
    public static final String LOGIN_BY_USER_INFO = BASE_URL + "/loginWithUserInfo.action";

    /**
     * 获取验证码地址
     */
    public static final String GET_CHECK_CODE = BASE_URL + "/getCheckCode.action";

    /**
     * 手机号登录并注册地址
     */
    public static final String LOGIN_BY_TELENUM = BASE_URL + "/loginWithTeleNum.action";

}

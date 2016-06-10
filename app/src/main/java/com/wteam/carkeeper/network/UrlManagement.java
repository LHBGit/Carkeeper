package com.wteam.carkeeper.network;

/**
 * Created by lhb on 2016/5/20.
 */
public class UrlManagement {
    /**
     * 服务器ip地址
     */
    public static final String BASE_URL = "http://192.168.253.1:8080/carkeeper";

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

    /**
     * 自动登录地址
     */
    public static final String LOGIN_AUTO = BASE_URL + "/autoLogin.action";

    /**
     * 获取修改密码的短信验证码地址
     */
    public static final String GET_UPDATE_PASSWORD_CHECK_CODE = BASE_URL + "/getUpdatePasswordCheckCode.action";

    /**
     * 更新密码地址
     */
    public static final String UPDATE_PASSWORD = BASE_URL + "/updatePassword.action";

    /**
     * 获取附近加油站地址
     */
    public static final String GET_NEARLY_GAS_STATION = "http://apis.juhe.cn/oil/local";

    /**
     * 获取更新手机号码的验证码地址
     */
    public static final String GET_UPDATE_TELEPHONE_NUM_CHECK_CODE = BASE_URL + "/getUpdateTelephoneNumCheckCode.action";

    /**
     * 更新手机号码地址
     */
    public static final String UPDATE_TELEPHONE_NUM = BASE_URL + "/updateTelephone.action";

    /**
     * 登陆后更新密码地址
     */
    public static final String UPDATE_PASSWORD_AFTER_LOGIN = BASE_URL + "/updatePasswordAfterLogin.action";

    /**
     * 保存反馈信息接口
     */
    public static final String SAVE_FEEDBACK_INFO = BASE_URL + "/saveFeedbackInfo.action";

    /**
     * 保存订单接口
     */
    public static final String SAVE_GAS_ORDER = BASE_URL + "/saveGasOrder.action";

    /**
     * 获取订单信息接口
     */
    public static final String GET_GAS_ORDER_ORDER＿BY_TIME_ASC = BASE_URL + "/queryGasOrderDaoOrderByTime.action";

    /**
     * 通过订单ID获取订单接口
     */
    public static final String GET_GAS_ORDER_BY_ORDER_ID = BASE_URL + "/queryGasOrderByGasOrderId.action";
}

package com.wteam.carkeeper.entity;

/**
 * Created by lhb on 2016/5/20.
 */
public class SysUserVo {

    /**
     * 用户帐户
     */
    private String account;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户手机号码
     */
    private String telephoneNum;
    /**
     * 短信验证码
     */
    private String teleCheckCode;


    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getTelephoneNum() {
        return telephoneNum;
    }
    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }
    public String getTeleCheckCode() {
        return teleCheckCode;
    }
    public void setTeleCheckCode(String teleCheckCode) {
        this.teleCheckCode = teleCheckCode;
    }
}

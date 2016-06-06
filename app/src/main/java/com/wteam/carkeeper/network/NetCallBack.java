package com.wteam.carkeeper.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.SysUserVo;
import com.wteam.carkeeper.entity.UserInfoVo;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/5/31.
 */
public abstract class NetCallBack  extends TextHttpResponseHandler {

    private String url;
    private RequestParams requestParams;
    private SharedPreferences sharedPreferences;
    public static final String AUTO_LOGIN_FAIL_By_TOKEN_OR_ACCOUNT = "1";
    public static final String CONNECT_TIMEOUT = "2";

    public NetCallBack(String url,RequestParams requestParams){
        this.url = url;
        this.requestParams = requestParams;
        this.sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        if(statusCode == 200)
            if (null != responseString) {
                ResultMessage resultMessage = JSON.parseObject(responseString, ResultMessage.class);

                /**
                 * session过期自动重新登录
                 */
                if(CodeType.SESSION_TIMEOUT.getCode().equals(resultMessage.getCode())) {
                    Log.e("sessionTimeout","sessionTimeout");
                    String account = this.sharedPreferences.getString("account","");
                    String refreshtoken = this.sharedPreferences.getString("refreshtoken","");

                    RequestParams autoLoginRequestParams = new RequestParams();
                    SysUserVo sysUserVo = new SysUserVo();
                    sysUserVo.setAccount(account);
                    sysUserVo.setRefreshtoken(refreshtoken);
                    String json = JSON.toJSONString(sysUserVo);
                    autoLoginRequestParams.add("sysUserVo",json);

                    Log.e("json",json);
                    HttpUtil.post(UrlManagement.LOGIN_AUTO, autoLoginRequestParams, new TextHttpResponseHandler() {

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.e("自动登陆失败","自动登陆失败");
                            failure(statusCode,headers,responseString,CONNECT_TIMEOUT);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            if(statusCode == 200) {
                                if(null != responseString) {

                                    ResultMessage resultMessage1 = JSON.parseObject(responseString,ResultMessage.class);
                                    /**
                                     * 自动登录成功后，重新执行原来的请求
                                     */
                                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage1.getCode())) {
                                        Map<String,Object> map = resultMessage1.getResultParm();
                                        if(map != null) {
                                            String sysUserVoJson = (String) map.get("sysUserVo");
                                            String userInfoVoJson = (String) map.get("userInfoVo");
                                            if(null != sysUserVoJson && null != userInfoVoJson) {
                                                SysUserVo sysUserVoResult = JSON.parseObject(sysUserVoJson,SysUserVo.class);
                                                UserInfoVo userInfoResult = JSON.parseObject(userInfoVoJson,UserInfoVo.class);

                                                SharedPreferences sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("account",sysUserVoResult.getAccount());
                                                editor.putString("telephoneNum",sysUserVoResult.getTelephoneNum());
                                                editor.putString("refreshtoken",sysUserVoResult.getRefreshtoken());
                                                editor.putString("email",userInfoResult.getEmail());
                                                editor.putString("iconUrl",userInfoResult.getIconUrl());
                                                editor.putString("gender",userInfoResult.getGender());
                                                editor.putString("signature",userInfoResult.getSignature());
                                                editor.putString("carAge",userInfoResult.getCarAge());
                                                editor.commit();
                                            }
                                        }

                                        Log.e("自动登陆成功","自动登陆成功");
                                        HttpUtil.post(url, requestParams, new TextHttpResponseHandler() {
                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                failure(statusCode,headers,responseString,CONNECT_TIMEOUT);
                                                Log.e("重新请求失败！","重新请求失败");
                                            }

                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                success(statusCode,headers,responseString);
                                                Log.e("重新请求成功！","重新请求成功");
                                            }
                                        });
                                    } else {
                                        failure(statusCode, headers, responseString, AUTO_LOGIN_FAIL_By_TOKEN_OR_ACCOUNT);
                                    }
                                }
                            }
                        }
                    });
                } else {
                    Log.e("session正常","session正常");
                    success(statusCode,headers,responseString);
                }
            }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        failure(statusCode,headers,responseString,CONNECT_TIMEOUT);
    }

    public abstract void success(int statusCode, Header[] headers, String responseString);

    public abstract void failure(int statusCode, Header[] headers, String responseString, String errorCode);
}

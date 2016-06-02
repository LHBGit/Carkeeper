package com.wteam.carkeeper.network;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.SysUserVo;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/5/31.
 */
public abstract class NetCallBack  extends TextHttpResponseHandler {

    private String url;
    private RequestParams requestParams;

    private NetCallBack(String url,RequestParams requestParams){
        this.url = url;
        this.requestParams = requestParams;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, String responseString) {
        if(statusCode == 200)
            if (null != responseString) {
                ResultMessage resultMessage = JSON.parseObject(responseString, ResultMessage.class);
                /**
                 * session过期自动重新登录
                 */
                if(CodeType.SESSION_TIMEOUT.equals(resultMessage.getCode())) {
                    RequestParams autoLoginRequestParams = new RequestParams();
                    SysUserVo sysUserVo = new SysUserVo();
                    sysUserVo.setAccount("Account");
                    sysUserVo.setRefreshtoken("Refreshtoken");
                    String json = JSON.toJSONString(sysUserVo);
                    autoLoginRequestParams.add("sysUserVo",json);
                    HttpUtil.post(UrlManagement.LOGIN_AUTO, autoLoginRequestParams, new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            failure(statusCode,headers,responseString,throwable);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            if(statusCode == 200) {
                                if(null != responseString) {
                                    ResultMessage resultMessage1 = JSON.parseObject(responseString,ResultMessage.class);
                                    /**
                                     * 自动登录成功后，重新执行原来的请求
                                     */
                                    if(CodeType.OPERATION_SUCCESS.equals(resultMessage1.getCode())) {
                                        HttpUtil.post(url, requestParams, new TextHttpResponseHandler() {
                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                                failure(statusCode,headers,responseString,throwable);
                                            }

                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                                success(statusCode,headers,responseString);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    });
                } else {
                    success(statusCode,headers,responseString);
                }
            }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        failure(statusCode,headers,responseString,throwable);
    }

    public abstract void success(int statusCode, Header[] headers, String responseString);

    public abstract void failure(int statusCode, Header[] headers, String responseString, Throwable throwable);
}

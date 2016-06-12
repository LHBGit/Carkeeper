package com.wteam.carkeeper.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wteam.carkeeper.MainActivity;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.SysUserVo;
import com.wteam.carkeeper.entity.UserInfoVo;
import com.wteam.carkeeper.network.CarkeeperApplication;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.UrlManagement;

import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/6/11.
 */
public class StartActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        String account = this.sharedPreferences.getString("account","");
        String refreshtoken = this.sharedPreferences.getString("refreshtoken","");

        if("".equals(account) || "".equals(refreshtoken)) {
            gotoActivity(LoginActivity.class);
        }

        RequestParams autoLoginRequestParams = new RequestParams();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setAccount(account);
        sysUserVo.setRefreshtoken(refreshtoken);
        String json = JSON.toJSONString(sysUserVo);
        autoLoginRequestParams.add("sysUserVo",json);

        HttpUtil.post(UrlManagement.LOGIN_AUTO, autoLoginRequestParams, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("自动登陆失败","自动登陆失败");
                gotoActivity(LoginActivity.class);
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

                                    JPushInterface.setAlias(CarkeeperApplication.getContext(),sysUserVoResult.getAccount(), new TagAliasCallback() {
                                        @Override
                                        public void gotResult(int i, String s, Set<String> set) {
                                            Log.e("设置别名结果：",i==0?"成功":"失败");
                                        }
                                    });

                                    gotoActivity(MainActivity.class);
                                    return;
                                }
                            }
                        } else {
                            gotoActivity(LoginActivity.class);
                        }
                    }
                }
            }
        });
    }


    private void gotoActivity(Class c) {
        Intent intent = new Intent(StartActivity.this,c);
        startActivity(intent);
        finish();
    }
}

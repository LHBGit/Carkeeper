package com.wteam.carkeeper.personcenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andexert.library.RippleView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.SysUserVo;
import com.wteam.carkeeper.entity.UserInfoVo;
import com.wteam.carkeeper.network.CarkeeperApplication;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.UrlManagement;

import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/4/29.
 */
public class LoginWithUserInfoFragment extends Fragment implements RippleView.OnRippleCompleteListener{

    private RippleView rv_login_with_user_info;
    private AutoCompleteTextView login_with_user_info_account;
    private AutoCompleteTextView login_with_user_info_password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_with_userinfo,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rv_login_with_user_info = (RippleView) view.findViewById(R.id.rv_login_with_user_info);
        login_with_user_info_account = (AutoCompleteTextView) view.findViewById(R.id.login_with_user_info_account);
        login_with_user_info_password = (AutoCompleteTextView) view.findViewById(R.id.login_with_user_info_password);
        rv_login_with_user_info.setOnRippleCompleteListener(this);
    }

    @Override
    public void onComplete(final RippleView rippleView) {
        rippleView.setClickable(false);

        final String textAccount = login_with_user_info_account.getText().toString().trim();
        final String textPassword = login_with_user_info_password.getText().toString().trim();

        /**
         * 参数校验
         */
        if("".equals(textAccount)) {
            Toast.makeText(getActivity(), "账号不能为空！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if(textAccount.length() < 6){
            Toast.makeText(getActivity(), "账号长度不能小于6位！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if("".equals(textPassword)) {
            Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if(textPassword.length() < 6) {
            Toast.makeText(getActivity(), "密码长度不能小于6位！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }

        RequestParams requestParams = new RequestParams();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setAccount(textAccount);
        sysUserVo.setPassword(textPassword);
        String json = JSON.toJSON(sysUserVo).toString();
        requestParams.add("sysUserVo",json);

        HttpUtil.post(UrlManagement.LOGIN_BY_USER_INFO, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(), statusCode + responseString , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(getActivity(), statusCode + responseString, Toast.LENGTH_LONG).show();
                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        Map<String, Object> map = resultMessage.getResultParm();
                        if (map != null) {
                            String sysUserVoJson = (String) map.get("sysUserVo");
                            String userInfoVoJson = (String) map.get("userInfoVo");
                            if (null != sysUserVoJson && null != userInfoVoJson) {
                                SysUserVo sysUserVoResult = JSON.parseObject(sysUserVoJson, SysUserVo.class);
                                UserInfoVo userInfoResult = JSON.parseObject(userInfoVoJson, UserInfoVo.class);

                                SharedPreferences sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("account", sysUserVoResult.getAccount());
                                editor.putString("telephoneNum", sysUserVoResult.getTelephoneNum());
                                editor.putString("refreshtoken", sysUserVoResult.getRefreshtoken());
                                editor.putString("email", userInfoResult.getEmail());
                                editor.putString("iconUrl", userInfoResult.getIconUrl());
                                editor.putString("gender", userInfoResult.getGender());
                                editor.putString("signature", userInfoResult.getSignature());
                                editor.putString("carAge", userInfoResult.getCarAge());
                                editor.commit();
                            }
                        }
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                login_with_user_info_account.setText("");
                login_with_user_info_password.setText("");

                rippleView.setClickable(true);
            }
        });
    }
}

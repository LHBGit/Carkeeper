package com.wteam.carkeeper.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andexert.library.RippleView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wteam.carkeeper.MainActivity;
import com.wteam.carkeeper.R;
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
 * Created by lhb on 2016/4/29.
 */
public class LoginWithTelephoneNumFragment extends Fragment implements View.OnClickListener,RippleView.OnRippleCompleteListener{

    private TextView get_code;
    private AutoCompleteTextView login_with_telephone_num_mobile_num;
    private AutoCompleteTextView login_with_telephone_num_check_code;
    private RippleView rv_login_with_telephone_num;

    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_with_telephone_num,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        get_code = (TextView) view.findViewById(R.id.get_code);
        login_with_telephone_num_mobile_num = (AutoCompleteTextView) view.findViewById(R.id.login_with_telephone_num_mobile_num);
        login_with_telephone_num_check_code = (AutoCompleteTextView) view.findViewById(R.id.login_with_telephone_num_check_code);
        rv_login_with_telephone_num = (RippleView) view.findViewById(R.id.rv_login_with_telephone_num);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (0 == msg.arg1){
                    get_code.setBackgroundResource(R.drawable.get_code_light);
                    get_code.setText("");
                } else {
                    get_code.setText("剩余"+ msg.arg1 + "s");
                }
            }
        };

        get_code.setOnClickListener(this);
        rv_login_with_telephone_num.setOnRippleCompleteListener(this);
    }

    @Override
    public void onClick(final View v) {
        v.setClickable(false);

        String mobileNum = login_with_telephone_num_mobile_num.getText().toString().trim();
        if("".equals(mobileNum)) {
            Toast.makeText(getActivity(),"手机号码不能为空！",Toast.LENGTH_SHORT).show();
            v.setClickable(true);
            return;
        }

        if(!mobileNum.matches("[1][358]\\d{9}")) {
            Toast.makeText(getActivity(),"手机号码格式不正确！",Toast.LENGTH_SHORT).show();
            v.setClickable(true);
            return;
        }

        get_code.setBackgroundResource(R.drawable.get_code);
        get_code.setText("正在发送...");

        RequestParams requestParams = new RequestParams();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setTelephoneNum(mobileNum);
        String json = JSON.toJSON(sysUserVo).toString();
        requestParams.add("sysUserVo",json);

        HttpUtil.post(UrlManagement.GET_CHECK_CODE, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(),"网络连接超时，请查看确认网络是否正常连接！" , Toast.LENGTH_LONG).show();
                get_code.setBackgroundResource(R.drawable.get_code_light);
                get_code.setText("");
                v.setClickable(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString, ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode()))  {
                        new Thread() {
                            @Override
                            public void run() {
                                for(int i=60;i>=0;i--) {
                                    try {
                                        Message message = handler.obtainMessage();
                                        message.arg1 = i;
                                        message.sendToTarget();
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                v.setClickable(true);
                            }
                        }.start();
                    } else if(CodeType.OPERATION_FAILURE.getCode().equals(resultMessage.getCode())) {
                        Toast.makeText(getActivity(),"重复获取验证码次数过多，请一小时后再试！",Toast.LENGTH_LONG).show();
                        login_with_telephone_num_check_code.setBackgroundResource(R.drawable.get_code_light);
                        login_with_telephone_num_check_code.setText("");
                        v.setClickable(true);
                    } else {
                        Toast.makeText(getActivity(),"验证码获取失败！",Toast.LENGTH_LONG).show();
                        get_code.setBackgroundResource(R.drawable.get_code_light);
                        get_code.setText("");
                        v.setClickable(true);
                    }
                }
            }
        });
    }

    @Override
    public void onComplete(final RippleView rippleView) {
        rippleView.setClickable(false);

        String mobileNum = login_with_telephone_num_mobile_num.getText().toString().trim();
        String checkCode = login_with_telephone_num_check_code.getText().toString().trim();

        if("".equals(mobileNum)) {
            Toast.makeText(getActivity(),"手机号码不能为空！",Toast.LENGTH_SHORT).show();
            rippleView.setClickable(true);
            return;
        }

        if(!mobileNum.matches("[1][358]\\d{9}")) {
            Toast.makeText(getActivity(),"手机号码格式不正确！",Toast.LENGTH_SHORT).show();
            rippleView.setClickable(true);
            return;
        }

        if("".equals(checkCode)) {
            Toast.makeText(getActivity(), "验证码不能为空！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if(!checkCode.matches("\\d{6}")) {
            Toast.makeText(getActivity(), "验证码格式不正确！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }

        RequestParams requestParams = new RequestParams();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setTelephoneNum(mobileNum);
        sysUserVo.setTeleCheckCode(checkCode);
        String json = JSON.toJSON(sysUserVo).toString();
        requestParams.add("sysUserVo",json);

        HttpUtil.post(UrlManagement.LOGIN_BY_TELENUM, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getActivity(),"网络连接超时，请查看确认网络是否正常连接！" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        Map<String,Object> map = resultMessage.getResultParm();
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
                            }
                        }
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }else if(CodeType.AUTHC_FAIL.getCode().equals(resultMessage.getCode())) {
                        Toast.makeText(getActivity(), "验证码错误或超时！" , Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "登陆失败！" , Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                login_with_telephone_num_mobile_num.setText("");
                login_with_telephone_num_check_code.setText("");
                rippleView.setClickable(true);
            }
        });
    }
}

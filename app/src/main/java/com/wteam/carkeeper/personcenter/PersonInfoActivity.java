package com.wteam.carkeeper.personcenter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andexert.library.RippleView;
import com.loopj.android.http.RequestParams;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.SysUserVo;
import com.wteam.carkeeper.network.CarkeeperApplication;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.NetCallBack;
import com.wteam.carkeeper.network.UrlManagement;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lhb on 2016/5/4.
 */
public class PersonInfoActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,RippleView.OnRippleCompleteListener,View.OnClickListener{

    private TopBar person_info_top_bar;
    private RippleView rv_telephone;
    private RippleView rv_email;
    private RippleView rv_wechat;
    private RippleView rv_change_psw;
    private SharedPreferences sharedPreferences;
    private CircleImageView person_center_cycle_image;
    private TextView nameAndGender;
    private TextView person_info_car_age;
    private TextView person_info_username;

    private AutoCompleteTextView login_with_telephone_num_mobile_num;
    private AutoCompleteTextView login_with_telephone_num_check_code;
    private RippleView rv_login_with_telephone_num;
    private TextView get_code;
    private Button btn_login_with_telephone_num;

    private Handler handler;

    /**
     * dialog事件处理标志位
     */
    private boolean isAccess = false;

    private AutoCompleteTextView dialog_change_password_password;
    private RippleView rv_dialog_change_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initView();
        init();
    }

    private void initView() {
        person_info_top_bar = (TopBar) findViewById(R.id.person_info_top_bar);
        rv_telephone = (RippleView) findViewById(R.id.rv_telephone);
        rv_email = (RippleView) findViewById(R.id.rv_email);
        rv_wechat = (RippleView) findViewById(R.id.rv_wechat);
        rv_change_psw = (RippleView) findViewById(R.id.rv_change_psw);
        person_center_cycle_image = (CircleImageView) findViewById(R.id.person_center_cycle_image);
        nameAndGender = (TextView) findViewById(R.id.nameAndGender);
        person_info_car_age = (TextView) findViewById(R.id.person_info_car_age);
        person_info_username = (TextView) findViewById(R.id.person_info_username);

        person_info_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        rv_telephone.setOnRippleCompleteListener(this);
        rv_email.setOnRippleCompleteListener(this);
        rv_wechat.setOnRippleCompleteListener(this);
        rv_change_psw.setOnRippleCompleteListener(this);
        person_center_cycle_image.setOnClickListener(this);
        nameAndGender.setOnClickListener(this);
        person_info_car_age.setOnClickListener(this);
    }

    private void init() {
        sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        String gender = sharedPreferences.getString("gender","未知");
        if(gender.equals("男")) {
            nameAndGender.setCompoundDrawables(null,null,getResources().getDrawable(R.drawable.male),null);
        } else if(gender.equals("女")) {
            nameAndGender.setCompoundDrawables(null,null,getResources().getDrawable(R.drawable.female),null);
        } else {
            nameAndGender.setCompoundDrawables(null,null,null,null);
        }

        person_info_username.setText(sharedPreferences.getString("account","Carkeeper"));
        String carAge = sharedPreferences.getString("carAge","0");
        person_info_car_age.setText(carAge + "年");
    }

    @Override
    public void onComplete(RippleView rippleView) {
        rippleView.setClickable(false);
        switch (rippleView.getId()) {
            case R.id.rv_telephone:
                doTelephone(rippleView);
                break;
            case R.id.rv_email:
                doEmail(rippleView);
                break;
            case R.id.rv_wechat:
                doWechat(rippleView);
                break;
            case R.id.rv_change_psw:
                doChangePsw(rippleView);
                break;
            default:
                break;
        }
        rippleView.setClickable(true);
    }

    private void doTelephone(RippleView rippleView) {
        if(!isAccess) {
            isAccess = !isAccess;
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.fragment_login_with_telephone_num,null);
            login_with_telephone_num_mobile_num = (AutoCompleteTextView) view.findViewById(R.id.login_with_telephone_num_mobile_num);
            login_with_telephone_num_check_code = (AutoCompleteTextView) view.findViewById(R.id.login_with_telephone_num_check_code);
            rv_login_with_telephone_num = (RippleView) view.findViewById(R.id.rv_login_with_telephone_num);
            btn_login_with_telephone_num = (Button) view.findViewById(R.id.btn_login_with_telephone_num);
            get_code = (TextView) view.findViewById(R.id.get_code);
            btn_login_with_telephone_num.setText("确认绑定");
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

            get_code.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    v.setClickable(false);

                    String mobileNum = login_with_telephone_num_mobile_num.getText().toString().trim();
                    if("".equals(mobileNum)) {
                        Toast.makeText(PersonInfoActivity.this,"手机号码不能为空！",Toast.LENGTH_SHORT).show();
                        v.setClickable(true);
                        return;
                    }

                    if(!mobileNum.matches("[1][358]\\d{9}")) {
                        Toast.makeText(PersonInfoActivity.this,"手机号码格式不正确！",Toast.LENGTH_SHORT).show();
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

                    HttpUtil.post(UrlManagement.GET_UPDATE_TELEPHONE_NUM_CHECK_CODE, requestParams, new NetCallBack(UrlManagement.GET_UPDATE_TELEPHONE_NUM_CHECK_CODE, requestParams) {
                        @Override
                        public void success(int statusCode, Header[] headers, String responseString) {
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
                                    Toast.makeText(PersonInfoActivity.this,"重复获取验证码次数过多，请一小时后再试！",Toast.LENGTH_LONG).show();
                                    login_with_telephone_num_check_code.setBackgroundResource(R.drawable.get_code_light);
                                    login_with_telephone_num_check_code.setText("");
                                    v.setClickable(true);
                                } else {
                                    Toast.makeText(PersonInfoActivity.this,"验证码获取失败！",Toast.LENGTH_LONG).show();
                                    Log.e("session",responseString);
                                    get_code.setBackgroundResource(R.drawable.get_code_light);
                                    get_code.setText("");
                                    v.setClickable(true);
                                }
                            }
                        }

                        @Override
                        public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                            Toast.makeText(PersonInfoActivity.this,"网络连接超时，请检查网络后再试！" , Toast.LENGTH_LONG).show();
                            get_code.setBackgroundResource(R.drawable.get_code_light);
                            get_code.setText("");
                            v.setClickable(true);
                        }
                    });
                }
            });


            rv_login_with_telephone_num.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(final RippleView rippleView) {
                    rippleView.setClickable(false);

                    String mobileNum = login_with_telephone_num_mobile_num.getText().toString().trim();
                    String checkCode = login_with_telephone_num_check_code.getText().toString().trim();

                    if("".equals(mobileNum)) {
                        Toast.makeText(PersonInfoActivity.this,"手机号码不能为空！",Toast.LENGTH_SHORT).show();
                        rippleView.setClickable(true);
                        return;
                    }

                    if(!mobileNum.matches("[1][358]\\d{9}")) {
                        Toast.makeText(PersonInfoActivity.this,"手机号码格式不正确！",Toast.LENGTH_SHORT).show();
                        rippleView.setClickable(true);
                        return;
                    }

                    if("".equals(checkCode)) {
                        Toast.makeText(PersonInfoActivity.this, "验证码不能为空！", Toast.LENGTH_LONG).show();
                        rippleView.setClickable(true);
                        return;
                    }
                    if(!checkCode.matches("\\d{6}")) {
                        Toast.makeText(PersonInfoActivity.this, "验证码格式不正确！", Toast.LENGTH_LONG).show();
                        rippleView.setClickable(true);
                        return;
                    }

                    RequestParams requestParams = new RequestParams();
                    SysUserVo sysUserVo = new SysUserVo();
                    sysUserVo.setTelephoneNum(mobileNum);
                    sysUserVo.setTeleCheckCode(checkCode);
                    String json = JSON.toJSON(sysUserVo).toString();
                    requestParams.add("sysUserVo",json);

                    HttpUtil.post(UrlManagement.UPDATE_TELEPHONE_NUM, requestParams, new NetCallBack(UrlManagement.UPDATE_TELEPHONE_NUM, requestParams) {
                        @Override
                        public void success(int statusCode, Header[] headers, String responseString) {
                            Toast.makeText(PersonInfoActivity.this, statusCode + responseString, Toast.LENGTH_LONG).show();
                            if(null != responseString) {
                                ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                                if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                                    Toast.makeText(PersonInfoActivity.this, "手机号码修改成功！" , Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(PersonInfoActivity.this, "验证码错误！" , Toast.LENGTH_LONG).show();
                                }
                                login_with_telephone_num_mobile_num.setText("");
                                login_with_telephone_num_check_code.setText("");
                                rippleView.setClickable(true);
                            }
                        }

                        @Override
                        public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                            Toast.makeText(PersonInfoActivity.this,"网络连接超时，请检查网络后再试！" , Toast.LENGTH_LONG).show();
                            login_with_telephone_num_mobile_num.setText("");
                            login_with_telephone_num_check_code.setText("");
                            rippleView.setClickable(true);
                        }
                    });
                }
            });

            dialog.setContentView(view);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isAccess = !isAccess;
                    login_with_telephone_num_mobile_num = null;
                    login_with_telephone_num_check_code = null;
                    rv_login_with_telephone_num = null;
                    btn_login_with_telephone_num = null;
                }
            });
            dialog.show();
        }
    }

    private void gotoActivity(Class c) {
        Intent intent = new Intent(PersonInfoActivity.this,c);
        startActivity(intent);
    }

    private void doEmail(RippleView rippleView) {
        Toast.makeText(PersonInfoActivity.this,"功能正在开发中！",Toast.LENGTH_SHORT).show();
    }

    private void doWechat(RippleView rippleView) {
        Toast.makeText(PersonInfoActivity.this,"功能正在开发中！",Toast.LENGTH_SHORT).show();
    }

    private void doChangePsw(RippleView rippleView) {
        if(!isAccess) {
            isAccess = !isAccess;
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater layoutInflater = LayoutInflater.from(this);
            View view = layoutInflater.inflate(R.layout.dialog_change_password,null);
            dialog_change_password_password = (AutoCompleteTextView) view.findViewById(R.id.dialog_change_password_password);
            rv_dialog_change_password = (RippleView) view.findViewById(R.id.rv_dialog_change_password);

            rv_dialog_change_password.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(final RippleView rippleView) {
                    rippleView.setClickable(false);

                    String password = dialog_change_password_password.getText().toString().trim();
                    if("".equals(password)) {
                        Toast.makeText(PersonInfoActivity.this,"密码不能为空！",Toast.LENGTH_LONG).show();
                        rippleView.setClickable(true);
                        return;
                    }

                    if(password.length() < 6) {
                        Toast.makeText(PersonInfoActivity.this,"密码不能小于6位！",Toast.LENGTH_LONG).show();
                        rippleView.setClickable(true);
                        return;
                    }

                    RequestParams requestParams = new RequestParams();
                    SysUserVo sysUserVo = new SysUserVo();
                    sysUserVo.setPassword(password);
                    String json = JSON.toJSON(sysUserVo).toString();
                    requestParams.add("sysUserVo",json);

                    HttpUtil.post(UrlManagement.UPDATE_PASSWORD_AFTER_LOGIN, requestParams, new NetCallBack(UrlManagement.UPDATE_PASSWORD_AFTER_LOGIN, requestParams) {
                        @Override
                        public void success(int statusCode, Header[] headers, String responseString) {

                            if(null != responseString) {
                                ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                                if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                                    Toast.makeText(PersonInfoActivity.this, "密码修改成功！" , Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }

                                rippleView.setClickable(true);
                            }
                        }

                        @Override
                        public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                            Toast.makeText(PersonInfoActivity.this,"网络连接超时，请检查网络后再试！" , Toast.LENGTH_LONG).show();
                            rippleView.setClickable(true);
                        }
                    });

                }
            });

            dialog.setContentView(view);
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isAccess = !isAccess;
                    login_with_telephone_num_mobile_num = null;
                    login_with_telephone_num_check_code = null;
                    rv_login_with_telephone_num = null;
                    btn_login_with_telephone_num = null;
                }
            });
            dialog.show();
        }
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.person_center_cycle_image:
                doCycleImage();
                break;
            case R.id.nameAndGender:
                doChangeGender();
                break;
            case R.id.person_info_car_age:
                doChangeCarAge();
                break;
            default:
                break;
        }
    }

    private void doChangeCarAge() {
    }

    private void doChangeGender() {
    }

    private void doCycleImage() {
    }
}

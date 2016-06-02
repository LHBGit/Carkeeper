package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andexert.library.RippleView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tandong.swichlayout.BaseEffects;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.SysUserVo;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.UrlManagement;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/4/29.
 */
public class RecoverPasswordActivity extends AppCompatActivity implements SwichLayoutInterFace,TopBar.Top_bar_tv_1_ClickListener ,View.OnClickListener,RippleView.OnRippleCompleteListener{

    private TopBar recover_password_top_bar;
    private AutoCompleteTextView recover_password_telephone_num;
    private AutoCompleteTextView recover_password_password;
    private AutoCompleteTextView recover_password_check_code;
    private TextView recover_password_get_check_code;
    private RippleView rv_recover_password_commit;

    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        initView();
        setEnterSwichLayout();
    }

    private void initView() {
        recover_password_top_bar = (TopBar) findViewById(R.id.recover_password_top_bar);
        recover_password_telephone_num = (AutoCompleteTextView) findViewById(R.id.recover_password_telephone_num);
        recover_password_password = (AutoCompleteTextView) findViewById(R.id.recover_password_password);
        recover_password_check_code = (AutoCompleteTextView) findViewById(R.id.recover_password_check_code);
        recover_password_get_check_code = (TextView) findViewById(R.id.recover_password_get_check_code);

        rv_recover_password_commit = (RippleView) findViewById(R.id.rv_recover_password_commit);
        LinearLayout activity_recover_password = (LinearLayout) findViewById(R.id.activity_recover_password);
        LinearLayout input_linearLayout = (LinearLayout) findViewById(R.id.input_linearLayout);

        recover_password_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        recover_password_get_check_code.setOnClickListener(this);
        rv_recover_password_commit.setOnRippleCompleteListener(this);

        controlKeyboardLayout(activity_recover_password,input_linearLayout);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (0 == msg.arg1){
                    recover_password_get_check_code.setBackgroundResource(R.drawable.get_code_light);
                    recover_password_get_check_code.setText("");
                } else {
                    recover_password_get_check_code.setText("剩余"+ msg.arg1 + "s");
                }
            }
        };
    }

    private void gotoActivity(Class c) {
        Intent intent = new Intent(RecoverPasswordActivity.this,c);
        startActivity(intent);
    }

    private void controlKeyboardLayout(final View root, final View toTopView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                root.getWindowVisibleDisplayFrame(rect);
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;

                if (rootInvisibleHeight > 100) {
                    Rect rect1 = new Rect();
                    int srollHeight = toTopView.getTop() - 10;
                    root.scrollTo(0, srollHeight);
                } else {
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        setExitSwichLayout();
    }

    @Override
    public void setEnterSwichLayout() {
        SwitchLayout.getSlideFromBottom(this,false, BaseEffects.getReScrollEffect());
    }

    @Override
    public void setExitSwichLayout() {
        SwitchLayout.getSlideToBottom(this,true,BaseEffects.getMoreSlowEffect());
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        setExitSwichLayout();
        view.setClickable(true);
    }

    @Override
    public void onClick(final View v) {
        v.setClickable(false);

        String mobileNum = recover_password_telephone_num.getText().toString().trim();
        if("".equals(mobileNum)) {
            Toast.makeText(this,"手机号码不能为空！",Toast.LENGTH_SHORT).show();
            v.setClickable(true);
            return;
        }

        if(!mobileNum.matches("[1][358]\\d{9}")) {
            Toast.makeText(this,"手机号码格式不正确！",Toast.LENGTH_SHORT).show();
            v.setClickable(true);
            return;
        }

        recover_password_get_check_code.setBackgroundResource(R.drawable.get_code);
        recover_password_get_check_code.setText("正在发送...");

        RequestParams requestParams = new RequestParams();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setTelephoneNum(mobileNum);
        String json = JSON.toJSON(sysUserVo).toString();
        requestParams.add("sysUserVo",json);

        HttpUtil.post(UrlManagement.GET_UPDATE_PASSWORD_CHECK_CODE, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RecoverPasswordActivity.this,"网络连接超时，请查看确认网络是否正常连接！" , Toast.LENGTH_LONG).show();
                recover_password_get_check_code.setBackgroundResource(R.drawable.get_code_light);
                recover_password_get_check_code.setText("");
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
                        Toast.makeText(RecoverPasswordActivity.this,"重复获取验证码次数过多，请一小时后再试！",Toast.LENGTH_LONG).show();
                        recover_password_get_check_code.setBackgroundResource(R.drawable.get_code_light);
                        recover_password_get_check_code.setText("");
                        v.setClickable(true);
                    } else {
                        Toast.makeText(RecoverPasswordActivity.this,"验证码获取失败！",Toast.LENGTH_LONG).show();
                        recover_password_get_check_code.setBackgroundResource(R.drawable.get_code_light);
                        recover_password_get_check_code.setText("");
                        v.setClickable(true);
                    }
                }
            }
        });
    }

    @Override
    public void onComplete(final RippleView rippleView) {
        rippleView.setClickable(false);

        String mobileNum = recover_password_telephone_num.getText().toString().trim();
        String password = recover_password_password.getText().toString().trim();
        String checkCode = recover_password_check_code.getText().toString().trim();

        if("".equals(mobileNum)) {
            Toast.makeText(this,"手机号码不能为空！",Toast.LENGTH_SHORT).show();
            rippleView.setClickable(true);
            return;
        }

        if(!mobileNum.matches("[1][358]\\d{9}")) {
            Toast.makeText(this,"手机号码格式不正确！",Toast.LENGTH_SHORT).show();
            rippleView.setClickable(true);
            return;
        }

        if("".equals(password)) {
            Toast.makeText(this,"密码不能为空！",Toast.LENGTH_SHORT).show();
            rippleView.setClickable(true);
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(this,"密码长度不能小于6位！",Toast.LENGTH_SHORT).show();
            rippleView.setClickable(true);
            return;
        }

        if("".equals(checkCode)) {
            Toast.makeText(this, "验证码不能为空！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if(!checkCode.matches("\\d{6}")) {
            Toast.makeText(this, "验证码格式不正确！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }

        RequestParams requestParams = new RequestParams();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setTelephoneNum(mobileNum);
        sysUserVo.setTeleCheckCode(checkCode);
        sysUserVo.setPassword(password);
        String json = JSON.toJSON(sysUserVo).toString();
        requestParams.add("sysUserVo",json);

        HttpUtil.post(UrlManagement.UPDATE_PASSWORD, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RecoverPasswordActivity.this,"网络连接超时，请查看确认网络是否正常连接！" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        Toast.makeText(RecoverPasswordActivity.this, "密码更新成功！", Toast.LENGTH_LONG).show();
                        RecoverPasswordActivity.this.finish();
                    } else {
                        Toast.makeText(RecoverPasswordActivity.this, "操作失败！", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                recover_password_telephone_num.setText("");
                recover_password_check_code.setText("");
                rippleView.setClickable(true);
            }
        });
    }
}

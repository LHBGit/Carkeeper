package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andexert.library.RippleView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.SysUserVo;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.UrlManagement;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/5/3.
 */
public class RegisterActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,RippleView.OnRippleCompleteListener{

    private TopBar topBar;
    private LinearLayout activity_register;
    private LinearLayout register_input;
    private RippleView rv_register_with_user_info;
    private AutoCompleteTextView register_account;
    private AutoCompleteTextView register_password_first;
    private AutoCompleteTextView register_password_second;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        controlKeyboardLayout(activity_register,register_input);
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.register_top_bar);
        activity_register = (LinearLayout) findViewById(R.id.activity_register);
        register_input =(LinearLayout) findViewById(R.id.register_input);
        rv_register_with_user_info = (RippleView) findViewById(R.id.rv_register_with_user_info);
        register_account = (AutoCompleteTextView) findViewById(R.id.register_account);
        register_password_first = (AutoCompleteTextView) findViewById(R.id.register_password_first);
        register_password_second = (AutoCompleteTextView) findViewById(R.id.register_password_second);


        topBar.setOnTop_bar_tv_1_ClickListener(this);
        rv_register_with_user_info.setOnRippleCompleteListener(this);
    }

    private void gotoActivity(Class c) {
        Intent intent = new Intent(RegisterActivity.this,c);
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
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }

    @Override
    public void onComplete(final RippleView rippleView) {
        rippleView.setClickable(false);

        String textAccount = register_account.getText().toString().trim();
        String textPwd1 = register_password_first.getText().toString().trim();
        String textPwd2 = register_password_second.getText().toString().trim();

        /**
         * 参数校验
         */
        if("".equals(textAccount)) {
            Toast.makeText(RegisterActivity.this, "账号不能为空！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if(textAccount.length() < 6){
            Toast.makeText(RegisterActivity.this, "账号长度不能小于6位！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if("".equals(textPwd1)) {
            Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if(textPwd1.length() < 6) {
            Toast.makeText(RegisterActivity.this, "密码长度不能小于6位！", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }
        if(!textPwd1.equals(textPwd2)) {
            Toast.makeText(RegisterActivity.this, "密码校验不一致", Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }

        RequestParams requestParams = new RequestParams();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setAccount(textAccount);
        sysUserVo.setPassword(textPwd1);
        String json = JSON.toJSON(sysUserVo).toString();
        requestParams.add("sysUserVo",json);

        HttpUtil.post(UrlManagement.REGISTER_BY_USER_INFO, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RegisterActivity.this,"网络连接超时，请查看确认网络是否正常连接！" , Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        Toast.makeText(RegisterActivity.this,"注册成功！", Toast.LENGTH_LONG).show();
                        finish();
                    } else if(CodeType.ACCOUNT_REPEAT.getCode().equals(resultMessage.getCode())) {
                        Toast.makeText(RegisterActivity.this,"用户名已存在！", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(RegisterActivity.this,"注册失败！", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                register_account.setText("");
                register_password_first.setText("");
                register_password_second.setText("");
                rippleView.setClickable(true);
            }
        });
    }
}

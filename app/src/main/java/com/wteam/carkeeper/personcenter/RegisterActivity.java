package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andexert.library.RippleView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.SysUserVo;
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
    public void onComplete(RippleView rippleView) {
        rippleView.setClickable(false);

        if(null == register_account) {
            Toast.makeText(RegisterActivity.this, "账号不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if(register_account.getText().toString().trim().length() < 6){
            Toast.makeText(RegisterActivity.this, "账号长度不能小于6位！", Toast.LENGTH_LONG).show();
            return;
        }

        if((null == register_password_first) || (null == register_password_second)) {
            Toast.makeText(RegisterActivity.this, "密码不能为空！", Toast.LENGTH_LONG).show();
            return;
        } else if(register_password_first.getText().toString().trim().length() < 6){
            Toast.makeText(RegisterActivity.this, "密码长度不能小于6位！", Toast.LENGTH_LONG).show();
            return;
        } else if(!register_password_first.getText().toString().trim().equals(register_password_second.getText().toString().trim())) {
            Toast.makeText(RegisterActivity.this, "密码校验不一致", Toast.LENGTH_LONG).show();
            return;
        }

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setAccount(register_account.getText().toString().trim());
        sysUserVo.setPassword(register_password_first.getText().toString());
        String json = JSON.toJSON(sysUserVo).toString();
        Log.e("json",json);
        requestParams.add("sysUserVo",json);
        //requestParams.add("sysUserVo.account",register_account.getText().toString().trim());
        //requestParams.add("sysUserVo.password",register_password_first.getText().toString());

        asyncHttpClient.post(this, UrlManagement.REGISTER_BY_USER_INFO, requestParams, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(RegisterActivity.this, "注册失败：" + statusCode, Toast.LENGTH_LONG).show();
                Log.e("error",throwable.getMessage());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(RegisterActivity.this, "注册成功："+ responseString, Toast.LENGTH_LONG).show();
                finish();
            }
        });

        rippleView.setClickable(true);
    }
}

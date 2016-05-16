package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/5/4.
 */
public class PersonInfoActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,RippleView.OnRippleCompleteListener{

    private TopBar person_info_top_bar;
    private RippleView rv_telephone;
    private RippleView rv_email;
    private RippleView rv_wechat;
    private RippleView rv_change_psw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        initView();
    }

    private void initView() {
        person_info_top_bar = (TopBar) findViewById(R.id.person_info_top_bar);
        rv_telephone = (RippleView) findViewById(R.id.rv_telephone);
        rv_email = (RippleView) findViewById(R.id.rv_email);
        rv_wechat = (RippleView) findViewById(R.id.rv_wechat);
        rv_change_psw = (RippleView) findViewById(R.id.rv_change_psw);

        person_info_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        rv_telephone.setOnRippleCompleteListener(this);
        rv_email.setOnRippleCompleteListener(this);
        rv_wechat.setOnRippleCompleteListener(this);
        rv_change_psw.setOnRippleCompleteListener(this);
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
        Toast.makeText(PersonInfoActivity.this,"功能正在开发中。。。",Toast.LENGTH_SHORT).show();
    }

    private void doEmail(RippleView rippleView) {
        Toast.makeText(PersonInfoActivity.this,"功能正在开发中。。。",Toast.LENGTH_SHORT).show();
    }

    private void doWechat(RippleView rippleView) {
        Toast.makeText(PersonInfoActivity.this,"功能正在开发中。。。",Toast.LENGTH_SHORT).show();
    }

    private void doChangePsw(RippleView rippleView) {
        gotoActivity(RecoverPasswordActivity.class);
    }

    private void gotoActivity(Class target) {
        Intent intent = new Intent(PersonInfoActivity.this,target);
        startActivity(intent);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }
}

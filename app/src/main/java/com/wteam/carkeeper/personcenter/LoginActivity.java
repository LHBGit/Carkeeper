package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/4/28.
 */
public class LoginActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener,TopBar.Top_bar_tv_5_ClickListener, SwichLayoutInterFace {

    private FragmentManager fragmentManager;
    private LoginWithTelephoneNumFragment loginWithTelephoneNumFragment;
    private LoginWithUserInfoFragment loginWithUserInfoFragment;
    private Fragment current;

    private RadioGroup radioGroup;
    private LinearLayout activity_login_main;
    private TextView textView;
    private TopBar topBar;
    private FrameLayout login_container;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        initView();

        setEnterSwichLayout();
    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.login_switch);
        activity_login_main= (LinearLayout) findViewById(R.id.activity_login_main);
        textView  = (TextView) findViewById(R.id.forget_password);
        topBar = (TopBar) findViewById(R.id.login_top_bar);
        fragmentManager = getSupportFragmentManager();
        loginWithTelephoneNumFragment = new LoginWithTelephoneNumFragment();
        loginWithUserInfoFragment = new LoginWithUserInfoFragment();
        login_container = (FrameLayout) findViewById(R.id.login_container);

        radioGroup.setOnCheckedChangeListener(this);
        textView.setOnClickListener(this);
        topBar.setOnTop_bar_tv_5_ClickListener(this);

        controlKeyboardLayout(activity_login_main,radioGroup);
        switchContent(current,loginWithUserInfoFragment);
    }

    private void gotoActivity(Class c) {
        Intent intent = new Intent(LoginActivity.this,c);
        startActivity(intent);
    }

    private void controlKeyboardLayout(final View root,final View toTopView) {
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rdobtn_login_with_person_info:
                switchContent(current,loginWithUserInfoFragment);
                break;
            case R.id.rdobtn_login_with_telephone_num:
                switchContent(current,loginWithTelephoneNumFragment);
                break;
        }
    }

    /**
     * 切换Fragment
     */
    private void switchContent(Fragment from, Fragment to) {
        if(null != to && !to.equals(from)) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if(!to.isAdded()) {
                if(null == from) {
                    fragmentTransaction.add(R.id.login_container, to).commit();
                } else {
                    fragmentTransaction.hide(from).add(R.id.login_container, to).commit();
                }
            } else {
                if(null == from) {
                    fragmentTransaction.show(to).commit();
                } else {
                    fragmentTransaction.hide(from).show(to).commit();
                }
            }
            current = to;
        }
    }

    @Override
    public void onClick(View v) {
        v.setClickable(false);
        gotoActivity(RecoverPasswordActivity.class);
        v.setClickable(true);
    }

    @Override
    public void top_bar_tv_5_click(View view) {
        view.setClickable(false);
        gotoActivity(RegisterActivity.class);
        view.setClickable(true);
    }

    @Override
    public void setEnterSwichLayout() {
        //SwitchLayout.ScaleBigLeftTop(this, false, null);
        SwitchLayout.RotateCenterIn(this,false,null);
    }

    @Override
    public void setExitSwichLayout() {
        //SwitchLayout.ScaleSmallLeftTop(this, true, null);
        SwitchLayout.RotateCenterOut(this,true,null);
    }
}

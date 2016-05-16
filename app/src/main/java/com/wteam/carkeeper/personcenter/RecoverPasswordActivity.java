package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.tandong.swichlayout.BaseEffects;
import com.tandong.swichlayout.SwichLayoutInterFace;
import com.tandong.swichlayout.SwitchLayout;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/4/29.
 */
public class RecoverPasswordActivity extends AppCompatActivity implements SwichLayoutInterFace,TopBar.Top_bar_tv_1_ClickListener {

    private TopBar topBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);
        initView();
        setEnterSwichLayout();
    }

    private void initView() {
        topBar = (TopBar) findViewById(R.id.recover_password_top_bar);
        LinearLayout activity_recover_password = (LinearLayout) findViewById(R.id.activity_recover_password);
        LinearLayout input_linearLayout = (LinearLayout) findViewById(R.id.input_linearLayout);

        topBar.setOnTop_bar_tv_1_ClickListener(this);
        controlKeyboardLayout(activity_recover_password,input_linearLayout);
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
}

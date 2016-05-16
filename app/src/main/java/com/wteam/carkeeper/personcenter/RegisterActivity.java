package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/5/3.
 */
public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final TopBar topBar = (TopBar) findViewById(R.id.register_top_bar);
        topBar.setOnTop_bar_tv_1_ClickListener(new TopBar.Top_bar_tv_1_ClickListener() {
            @Override
            public void top_bar_tv_1_click(View view) {
                view.setClickable(false);
                finish();
                view.setClickable(true);
            }
        });

        LinearLayout activity_register = (LinearLayout) findViewById(R.id.activity_register);
        LinearLayout register_input =(LinearLayout) findViewById(R.id.register_input);
        controlKeyboardLayout(activity_register,register_input);
    }

    private void gotoActivity(Class c) {
        Intent intent = new Intent(RegisterActivity.this,c);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}

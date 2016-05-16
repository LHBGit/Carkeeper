package com.wteam.carkeeper.personcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/5/9.
 */
public class OrderDetailActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener{

    private TopBar order_detail_top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
    }

    private void initView() {
        order_detail_top_bar = (TopBar) findViewById(R.id.order_detail_top_bar);
        order_detail_top_bar.setOnTop_bar_tv_1_ClickListener(this);
    }


    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }
}

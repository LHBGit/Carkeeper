package com.wteam.carkeeper.personcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/5/11.
 */
public class CarInfoDetailActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener{

    private TopBar car_info_detail_top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info_detail);
        initView();
    }

    private void initView() {
        car_info_detail_top_bar = (TopBar) findViewById(R.id.car_info_detail_top_bar);
        car_info_detail_top_bar.setOnTop_bar_tv_1_ClickListener(this);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }
}

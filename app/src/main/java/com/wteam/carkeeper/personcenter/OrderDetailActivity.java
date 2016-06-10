package com.wteam.carkeeper.personcenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.GasOrderVo;
import com.wteam.carkeeper.network.CarkeeperApplication;
import com.wteam.carkeeper.network.UrlManagement;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

/**
 * Created by lhb on 2016/5/9.
 */
public class OrderDetailActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener{

    private TopBar order_detail_top_bar;
    private TextView order_detail_num;
    private TextView order_detail_reservation_time;
    private ImageView order_detail_qr_code;
    private TextView order_detail_user_name;
    private TextView order_detail_gas_station;
    private TextView order_detail_gas_type;
    private TextView order_detail_gas_number;
    private TextView order_detail_stute;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        init();
    }

    private void init() {
        sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);

        GasOrderVo gasOrderVo = (GasOrderVo) getIntent().getExtras().get("gasOrderVo");
        order_detail_num.setText(gasOrderVo.getOrderNum());
        order_detail_reservation_time.setText(gasOrderVo.getReserveTime());
        order_detail_user_name.setText("");
        order_detail_gas_station.setText(gasOrderVo.getGasStationName());
        order_detail_gas_type.setText(gasOrderVo.getGasType());
        order_detail_gas_number.setText(gasOrderVo.getAmountOfGasoline() + "元");
        order_detail_user_name.setText(sharedPreferences.getString("account",""));

        if("0".equals(gasOrderVo.getStatu())) {
            order_detail_stute.setText("未完成");
        } else {
            order_detail_stute.setText("已完成");
        }

        String qrCodeString = UrlManagement.GET_GAS_ORDER_BY_ORDER_ID + "?gasOrderVo={\"gasOrderId\":\"" + gasOrderVo.getGasOrderId() + "\"}";
        Bitmap bitmap = EncodingUtils.createQRCode(qrCodeString,500,500,null);
        order_detail_qr_code.setImageBitmap(bitmap);
    }

    private void initView() {
        order_detail_top_bar = (TopBar) findViewById(R.id.order_detail_top_bar);
        order_detail_num = (TextView) findViewById(R.id.order_detail_num);
        order_detail_reservation_time = (TextView) findViewById(R.id.order_detail_reservation_time);
        order_detail_qr_code = (ImageView) findViewById(R.id.order_detail_qr_code);
        order_detail_user_name = (TextView) findViewById(R.id.order_detail_user_name);
        order_detail_gas_station = (TextView) findViewById(R.id.order_detail_gas_station);
        order_detail_gas_type = (TextView) findViewById(R.id.order_detail_gas_type);
        order_detail_gas_number = (TextView) findViewById(R.id.order_detail_gas_number);
        order_detail_stute = (TextView) findViewById(R.id.order_detail_stute); 
        
        order_detail_top_bar.setOnTop_bar_tv_1_ClickListener(this);
    }


    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }
}

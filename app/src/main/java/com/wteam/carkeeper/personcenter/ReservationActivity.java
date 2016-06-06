package com.wteam.carkeeper.personcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.StationInfo;
import com.wteam.carkeeper.network.CarkeeperApplication;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by lhb on 2016/5/9.
 */
public class ReservationActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener
        , View.OnClickListener {

    private TopBar reservation_top_bar;
    private TextView reservation_gas_station_name;
    private TextView reservation_username;
    private ImageView change_gas_station;
    private TextView reservation_gas_station_time;
    private int[] radioButtonIds = {R.id.radio_button_1, R.id.radio_button_2, R.id.radio_button_3, R.id.radio_button_4, R.id.radio_button_5,
            R.id.radio_button_6, R.id.radio_button_7, R.id.radio_button_8, R.id.radio_button_9, R.id.radio_button_10, R.id.radio_button_11,
            R.id.radio_button_12, R.id.radio_button_13, R.id.radio_button_14, R.id.radio_button_15};
    private RadioButton[] radioButtons = new RadioButton[15];
    private RadioButton preRadioButton;
    ;
    private StationInfo.ResultBean.DataBean dataBean;
    private String stationInfoJson;
    private SharedPreferences sharedPreferences;
    private Map<String,String> gasTypeAndPrice = new HashMap<String,String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        initView();
        init();

    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            Log.e("reservationActivity","bundle不能为空");
            return;
        }

        /**
         * 读取用户数据
         */
        sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        reservation_username.setText(sharedPreferences.getString("account","Carkeeper"));
        reservation_gas_station_time.setText(new Timestamp(System.currentTimeMillis()).toString());

        dataBean = (StationInfo.ResultBean.DataBean) bundle.get("dataBean");
        reservation_gas_station_name.setText(dataBean.getName());
        stationInfoJson = (String) bundle.get("stationInfoJson");
        initRadioButtons(dataBean);
    }

    private void initRadioButtons(StationInfo.ResultBean.DataBean dataBean) {
        gasTypeAndPrice.clear();

        String price = dataBean.getPrice();
        String gastPrice = dataBean.getGastprice();
        if(price != null && !"".equals(price) && !"{}".equals(price)) {
            price = price.replace("{","");
            price = price.replace("}","");
            price = price.replace("\"","");
            String[] typeAndPrice = price.split(",");
            for(int i=0;i<typeAndPrice.length;i++) {
                String[] temp = typeAndPrice[i].split(":");
                gasTypeAndPrice.put(temp[0],temp[1]);
            }
        }

        if(gastPrice != null && !"".equals(gastPrice) && !"{}".equals(gastPrice)) {
            gastPrice = gastPrice.replace("{","");
            gastPrice = gastPrice.replace("}","");
            gastPrice = gastPrice.replace("\"","");
            String[] typeAndGasPrice = gastPrice.split(",");
            for(int i=0;i<typeAndGasPrice.length;i++) {
                String[] temp = typeAndGasPrice[i].split(":");
                gasTypeAndPrice.put(temp[0],temp[1]);
            }
        }

        Set<String> set = gasTypeAndPrice.keySet();
        Object[] keys = set.toArray();
        int mod = set.size() % 3;

        for(int i=0;i<radioButtons.length;i++) {
            if(i<set.size()) {
                radioButtons[i].setText((String)keys[i]);
            } else if(mod != 0 && i>=set.size() && i<set.size()+(3-mod)) {
                radioButtons[i].setVisibility(View.INVISIBLE);
            } else{
                radioButtons[i].setVisibility(View.GONE);
            }
        }
    }

    private void initView() {
        reservation_top_bar = (TopBar) findViewById(R.id.reservation_top_bar);
        reservation_gas_station_name = (TextView) findViewById(R.id.reservation_gas_station_name);
        reservation_username = (TextView) findViewById(R.id.reservation_username);
        change_gas_station = (ImageView) findViewById(R.id.change_gas_station);
        reservation_gas_station_time = (TextView) findViewById(R.id.reservation_gas_station_time);

        for (int i = 0; i < radioButtonIds.length; i++) {
            radioButtons[i] = (RadioButton) findViewById(radioButtonIds[i]);
            radioButtons[i].setOnClickListener(this);
        }

        reservation_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        change_gas_station.setOnClickListener(this);

    }

    @Override
    public void top_bar_tv_1_click(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_gas_station:
                if(null != preRadioButton) {
                    preRadioButton.setChecked(false);
                    preRadioButton.setGravity(Gravity.CENTER);
                    preRadioButton = null;
                }
                Intent intent = new Intent(ReservationActivity.this, PeripheryGasStationActivity.class);
                intent.putExtra("stationInfoJson",stationInfoJson);
                startActivityForResult(intent, 2);
                break;
            default:
                if(null != preRadioButton) {
                    preRadioButton.setChecked(false);
                    preRadioButton.setGravity(Gravity.CENTER);
                }
                RadioButton radioButton = (RadioButton) v;
                radioButton.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
                preRadioButton = radioButton;
                radioButton.setChecked(true);
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==  2 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if(bundle == null) {
                Log.e("ReservationActivity","Bundle不能为空！");
                return;
            }
            StationInfo.ResultBean.DataBean dataBean = (StationInfo.ResultBean.DataBean) bundle.get("dataBean");
            reservation_gas_station_name.setText(dataBean.getName());
            initRadioButtons(dataBean);
        }
    }
}

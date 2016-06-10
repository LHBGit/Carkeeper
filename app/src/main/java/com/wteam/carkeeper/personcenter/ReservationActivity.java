package com.wteam.carkeeper.personcenter;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andexert.library.RippleView;
import com.loopj.android.http.RequestParams;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.GasOrderVo;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.StationInfo;
import com.wteam.carkeeper.network.CarkeeperApplication;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.NetCallBack;
import com.wteam.carkeeper.network.UrlManagement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/5/9.
 */
public class ReservationActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener
        , View.OnClickListener,RippleView.OnRippleCompleteListener {

    private TopBar reservation_top_bar;
    private TextView reservation_gas_station_name;
    private TextView reservation_username;
    private ImageView change_gas_station;
    private TextView reservation_gas_station_time;
    private EditText number_of_gas;

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

    private RippleView rv_reservation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        initView();
        init();

    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        /**
         * 读取用户数据
         */
        sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        reservation_username.setText(sharedPreferences.getString("account","Carkeeper"));
        Date now = new Date();
        String month = "" +(now.getMonth()+1);
        String day = "" + now.getDate();
        if(now.getMonth() < 9) {
            month = "0" + month;
        }
        if(now.getDate() < 10) {
            day = "0" + day;
        }

        reservation_gas_station_time.setText((now.getYear()+1900) + "-" + month + "-" + day);
        reservation_gas_station_time.setOnClickListener(this);

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
        rv_reservation = (RippleView) findViewById(R.id.rv_reservation);
        number_of_gas = (EditText) findViewById(R.id.number_of_gas);

        for (int i = 0; i < radioButtonIds.length; i++) {
            radioButtons[i] = (RadioButton) findViewById(radioButtonIds[i]);
            radioButtons[i].setOnClickListener(this);
        }

        reservation_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        change_gas_station.setOnClickListener(this);
        rv_reservation.setOnRippleCompleteListener(this);

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
            case R.id.reservation_gas_station_time:
                final Date date = new Date();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReservationActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        if(year < date.getYear()+1900 || monthOfYear <date.getMonth() || dayOfMonth < date.getDate()) {
                            Toast.makeText(ReservationActivity.this,"预约日期不能早于当前日期！",Toast.LENGTH_LONG).show();
                            return;
                        }

                        String month = "" + (monthOfYear+1);
                        String day = "" + dayOfMonth;
                        if(monthOfYear < 9) {
                            month = "0" + (monthOfYear+1);
                        }

                        if(dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        }

                        reservation_gas_station_time.setText(year + "-" + month + "-" + day);
                    }
                },(date.getYear()+1900),date.getMonth(),date.getDate());

                datePickerDialog.show();
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

    @Override
    public void onComplete(final RippleView rippleView) {
        rippleView.setClickable(false);

        if(preRadioButton == null) {
            Toast.makeText(ReservationActivity.this,"请选择加油类型！",Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }

        String numOfGas = number_of_gas.getText().toString().trim();

        if("".equals(numOfGas)) {
            Toast.makeText(ReservationActivity.this,"请输入加油数量！",Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }

        if(!numOfGas.matches("^[1-9]\\d*")) {
            Toast.makeText(ReservationActivity.this,"加油数量必须由数字组成！",Toast.LENGTH_LONG).show();
            rippleView.setClickable(true);
            return;
        }


        RequestParams requestParams = new RequestParams();

        GasOrderVo gasOrderVo = new GasOrderVo();
        gasOrderVo.setAmountOfGasoline(numOfGas);
        gasOrderVo.setGasType(preRadioButton.getText().toString().trim());
        gasOrderVo.setGasStationName(reservation_gas_station_name.getText().toString().trim());
        gasOrderVo.setReserveTime(reservation_gas_station_time.getText().toString().trim());

        String json = JSON.toJSON(gasOrderVo).toString();
        requestParams.add("gasOrderVo",json);

        HttpUtil.post(UrlManagement.SAVE_GAS_ORDER, requestParams, new NetCallBack(UrlManagement.SAVE_GAS_ORDER, requestParams) {
            @Override
            public void success(int statusCode, Header[] headers, String responseString) {

                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        Toast.makeText(ReservationActivity.this,"预约成功！" , Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                rippleView.setClickable(true);
            }

            @Override
            public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                Toast.makeText(ReservationActivity.this,"网络连接超时，请检查网络后再试！" , Toast.LENGTH_LONG).show();
                rippleView.setClickable(true);
            }
        });

    }
}

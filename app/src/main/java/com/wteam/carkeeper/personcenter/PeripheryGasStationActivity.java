package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.custom.TopBar.Top_bar_tv_1_ClickListener;
import com.wteam.carkeeper.entity.StationInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhb on 2016/5/9.
 */
public class PeripheryGasStationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,Top_bar_tv_1_ClickListener{

    private ListView periphery_gas_station_list;
    private List<StationInfo.ResultBean.DataBean> dataBeans;
    private TopBar periphery_gas_station_top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periphery_gas_station);
        initView();
        init();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            return;
        }
        String stationInfoJson = (String) bundle.get("stationInfoJson");
        StationInfo stationInfo = JSON.parseObject(stationInfoJson, StationInfo.class);
        dataBeans = stationInfo.getResult().getData();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();

        for (StationInfo.ResultBean.DataBean dataBean : dataBeans) {
            /**
             * 拼接价格字符串
             */
            StringBuffer stringBuffer = new StringBuffer();
            String price = dataBean.getPrice();
            String gastPrice = dataBean.getGastprice();
            float sum = 0;
            int typeNum = 0;
            String[] typeAndPrice = null;
            String[] typeAndGasPrice = null;

            if(price != null && !"".equals(price) && !"{}".equals(price)) {
                price = price.replace("{","");
                price = price.replace("}","");
                price = price.replace("\"","");
                typeAndPrice = price.split(",");
                for(int i=0;i<typeAndPrice.length;i++) {
                    stringBuffer.append("[" + typeAndPrice[i]+ "]");
                    sum += Float.valueOf(typeAndPrice[i].split(":")[1]);

                    typeNum += 1;
                    if(typeNum % 3 == 0) {
                        stringBuffer.append("\n");
                    }
                }
            }

            if(gastPrice != null && !"".equals(gastPrice) && !"{}".equals(gastPrice)) {
                gastPrice = gastPrice.replace("{","");
                gastPrice = gastPrice.replace("}","");
                gastPrice = gastPrice.replace("\"","");
                typeAndGasPrice = gastPrice.split(",");
                for(int i=0;i<typeAndGasPrice.length;i++) {
                    stringBuffer.append("[" + typeAndGasPrice[i]+ "]");
                    sum += Float.valueOf(typeAndGasPrice[i].split(":")[1]);

                    typeNum += 1;
                    if(typeNum % 3 == 0) {
                        stringBuffer.append("\n");
                    }
                }
            }

            Map<String,Object> map = new HashMap<>();
            map.put("item_periphery_gas_station_name",dataBean.getName());
            if(typeAndPrice != null && typeAndGasPrice != null) {
                map.put("item_periphery_gas_station_price",((int)(sum/(typeAndPrice.length + typeAndGasPrice.length) * 100)/((float)100)) + "");
            } else {
                map.put("item_periphery_gas_station_price","");
            }
            map.put("periphery_gas_station_address",dataBean.getAddress());
            map.put("periphery_gas_station_distance",dataBean.getDistance() + "米");
            if(stringBuffer.toString().endsWith("\n")) {
                map.put("periphery_gas_station_type",stringBuffer.substring(0,stringBuffer.length()-1));
            }
            map.put("periphery_gas_station_belong",dataBean.getBrandname());
            list.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list,R.layout.item_periphery_gas_station,
                new String[]{"item_periphery_gas_station_name","item_periphery_gas_station_price",
                        "periphery_gas_station_address","periphery_gas_station_distance",
                        "periphery_gas_station_type","periphery_gas_station_belong"},
                new int[]{R.id.item_periphery_gas_station_name,R.id.item_periphery_gas_station_price,R.id.periphery_gas_station_address,
                        R.id.periphery_gas_station_distance,R.id.periphery_gas_station_type,R.id.periphery_gas_station_belong});
        periphery_gas_station_list.setAdapter(simpleAdapter);
    }

    private void initView() {
        periphery_gas_station_list = (ListView) findViewById(R.id.periphery_gas_station_list);
        periphery_gas_station_top_bar = (TopBar) findViewById(R.id.periphery_gas_station_top_bar);

        periphery_gas_station_list.setOnItemClickListener(this);
        periphery_gas_station_top_bar.setOnTop_bar_tv_1_ClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("dataBean",dataBeans.get(position));
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        finish();
    }
}

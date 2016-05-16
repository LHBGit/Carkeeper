package com.wteam.carkeeper.personcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wteam.carkeeper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhb on 2016/5/9.
 */
public class PeripheryGasStationActivity extends AppCompatActivity {

    private ListView periphery_gas_station_list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periphery_gas_station);
        initView();
    }

    private void initView() {
        periphery_gas_station_list = (ListView) findViewById(R.id.periphery_gas_station_list);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 50; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("item_periphery_gas_station_name","加油站名字"+i);
            map.put("item_periphery_gas_station_price",(1.02 + i) + "");
            map.put("periphery_gas_station_address","加油站地址" + i);
            map.put("periphery_gas_station_distance",(0.11 + i) + "");
            map.put("periphery_gas_station_type","#93/#95/#0");
            list.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,list,R.layout.item_periphery_gas_station,new String[]{},new int[]{});
        periphery_gas_station_list.setAdapter(simpleAdapter);
    }
}

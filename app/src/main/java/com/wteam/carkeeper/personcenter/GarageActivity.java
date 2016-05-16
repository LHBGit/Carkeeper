package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhb on 2016/5/10.
 */
public class GarageActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,TopBar.Top_bar_tv_5_ClickListener,AdapterView.OnItemClickListener{

    private ListView garage_info_list;
    private TopBar garage_top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        initView();
    }

    private void initView() {
        garage_info_list = (ListView) findViewById(R.id.garage_info_list);
        garage_top_bar = (TopBar) findViewById(R.id.garage_top_bar);
        garage_info_list.setOnItemClickListener(this);
        garage_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        garage_top_bar.setOnTop_bar_tv_5_ClickListener(this);

        List<Map<String,Object>> arrayList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 20; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("car_sequence","NO."+(i+1));
            map.put("garage_brand","奥迪R" + (i+1));
            map.put("model_number","4." + i +"V" + i +"FSI");
            map.put("license_plate_number","粤A-3" + i + "806");
            arrayList.add(map);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.item_garage,new String[]{"car_sequence","garage_brand","model_number","license_plate_number"},
                new int[]{R.id.car_sequence,R.id.garage_brand,R.id.model_number,R.id.license_plate_number});
        garage_info_list.setAdapter(simpleAdapter);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }

    @Override
    public void top_bar_tv_5_click(View view) {
        view.setClickable(false);
        startActivityForResult(new Intent(GarageActivity.this, CaptureActivity.class),0);
        view.setClickable(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = bundle.getString("result");
            Toast.makeText(GarageActivity.this,result,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        gotoActivity(CarInfoDetailActivity.class);
    }

    private void gotoActivity(Class c) {
        Intent intent = new Intent(GarageActivity.this,c);
        startActivity(intent);
    }
}

package com.wteam.carkeeper.personcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lhb on 2016/5/10.
 */
public class IllegalInfoActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener{

    private ListView illegal_info_list;
    private TopBar illegal_info_top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illegal_info);
        initView();
    }

    private void initView() {
        illegal_info_list = (ListView) findViewById(R.id.illegal_info_list);
        illegal_info_top_bar = (TopBar) findViewById(R.id.illegal_info_top_bar);
        illegal_info_top_bar.setOnTop_bar_tv_1_ClickListener(this);

        List<Map<String,Object>> arrayList = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 50; i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("illegal_car_num","晋JH***7" + i);
            map.put("illegal_behavior","6023驾驶机动车在高速公路、城市高速公路以外的道路上不按规定车道行驶" + (i+1) + "次");
            map.put("illegal_address","盛泽路出人民路北约"+ i + "米");
            map.put("illegal_time","2016-03-25 08:09PM");
            map.put("illegal_fine","" + (200+ i));
            map.put("illegal_marking","-" + i);
            arrayList.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this,arrayList,R.layout.item_illegal_info,
                new String[]{"illegal_car_num","illegal_behavior","illegal_address","illegal_time","illegal_fine","illegal_marking"},
                new int[]{R.id.illegal_car_num,R.id.illegal_behavior,R.id.illegal_address,R.id.illegal_time,R.id.illegal_fine,R.id.illegal_marking});
        illegal_info_list.setAdapter(simpleAdapter);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }
}

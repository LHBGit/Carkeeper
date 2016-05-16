package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.OrderInfoAdapter;
import com.wteam.carkeeper.custom.PinnedSectionListView;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.OrderInfo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lhb on 2016/5/9.
 */
public class OrderManagementActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,TabLayout.OnTabSelectedListener,AdapterView.OnItemClickListener{
    private TopBar order_management_top_bar;
    private TabLayout order_management_tab_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management_main);
        initView();

        ArrayList<OrderInfo> arrayList = new ArrayList<>();
        for(int i = 0;i<50;i++) {
            OrderInfo orderInfo = new OrderInfo();
            orderInfo.setCarNum("粤B-" + i);
            orderInfo.setGasStationName("油站" + i);
            orderInfo.setDate(new Date());
            orderInfo.setOrderNum("xxDD00FF" + i);
            arrayList.add(orderInfo);
        }

        OrderInfoAdapter orderInfoAdapter = new OrderInfoAdapter(this,arrayList);
        final PinnedSectionListView pinnedSectionListView = (PinnedSectionListView) findViewById(android.R.id.list);
        pinnedSectionListView.setAdapter(orderInfoAdapter);
        pinnedSectionListView.setOnItemClickListener(this);
    }

    private void initView() {
        order_management_top_bar = (TopBar) findViewById(R.id.order_management_top_bar);
        order_management_tab_layout = (TabLayout) findViewById(R.id.order_management_tab_layout);
        order_management_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        order_management_tab_layout.setOnTabSelectedListener(this);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Toast.makeText(OrderManagementActivity.this,"onTabSelected",Toast.LENGTH_SHORT).show();
        Log.e("select","onTabSelected" + tab.getText());
        //Do my logic in this
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Toast.makeText(OrderManagementActivity.this,"onTabUnselected",Toast.LENGTH_SHORT).show();
        Log.e("select","onTabUnselected"+ tab.getText());
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Toast.makeText(OrderManagementActivity.this,"onTabReselected",Toast.LENGTH_SHORT).show();
        Log.e("select","onTabReselected"+ tab.getText());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        gotoActivity(OrderDetailActivity.class);
    }

    private void gotoActivity(Class c) {
        Intent intent = new Intent(OrderManagementActivity.this,c);
        startActivity(intent);
    }
}

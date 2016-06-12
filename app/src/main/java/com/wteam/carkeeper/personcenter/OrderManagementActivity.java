package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.OrderInfoAdapter;
import com.wteam.carkeeper.custom.PinnedSectionListView;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.GasOrderVo;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.NetCallBack;
import com.wteam.carkeeper.network.UrlManagement;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/5/9.
 */
public class OrderManagementActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,TabLayout.OnTabSelectedListener,AdapterView.OnItemClickListener{
    private TopBar order_management_top_bar;
    private TabLayout order_management_tab_layout;
    private OrderInfoAdapter orderInfoAdapter;
    private List<GasOrderVo> gasOrderVos;
    private List<GasOrderVo> gasOrderVos_Finished;
    private List<GasOrderVo> gasOrderVos_Un_Finished;
    private PinnedSectionListView pinnedSectionListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_management_main);
        initView();
        init();
    }

    private void init() {
        gasOrderVos = new ArrayList<>();
        gasOrderVos_Finished = new ArrayList<>();
        gasOrderVos_Un_Finished = new ArrayList<>();

        orderInfoAdapter = new OrderInfoAdapter(this,gasOrderVos);
        pinnedSectionListView = (PinnedSectionListView) findViewById(android.R.id.list);
        pinnedSectionListView.setAdapter(orderInfoAdapter);
        pinnedSectionListView.setOnItemClickListener(this);

        HttpUtil.post(UrlManagement.GET_GAS_ORDER_ORDER＿BY_TIME_ASC, null, new NetCallBack(UrlManagement.GET_GAS_ORDER_ORDER＿BY_TIME_ASC, null) {
            @Override
            public void success(int statusCode, Header[] headers, String responseString) {
                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        List<GasOrderVo> list = JSON.parseArray(resultMessage.getResultParm().get("gasOrderVos").toString(),GasOrderVo.class);

                        for(GasOrderVo vo:list) {
                            gasOrderVos.add(vo);
                            if("0".equals(vo.getStatu())) {
                                gasOrderVos_Un_Finished.add(vo);
                            } else {
                                gasOrderVos_Finished.add(vo);
                            }
                        }
                    }
                        orderInfoAdapter = new OrderInfoAdapter(OrderManagementActivity.this,gasOrderVos);
                        pinnedSectionListView.setAdapter(orderInfoAdapter);
                }
            }

            @Override
            public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                Toast.makeText(OrderManagementActivity.this,"网络连接超时，数据加载失败！" , Toast.LENGTH_LONG).show();
            }
        });
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
        switch (tab.getPosition()) {
            case 0:
                orderInfoAdapter = new OrderInfoAdapter(OrderManagementActivity.this,gasOrderVos);
                pinnedSectionListView.setAdapter(orderInfoAdapter);
                break;
            case 1:
                orderInfoAdapter = new OrderInfoAdapter(OrderManagementActivity.this,gasOrderVos_Un_Finished);
                pinnedSectionListView.setAdapter(orderInfoAdapter);
                break;
            case 2:
                orderInfoAdapter = new OrderInfoAdapter(OrderManagementActivity.this,gasOrderVos_Finished);
                pinnedSectionListView.setAdapter(orderInfoAdapter);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(orderInfoAdapter.getItem(position) instanceof String) {
            return;
        }
        Intent intent = new Intent(OrderManagementActivity.this,OrderDetailActivity.class);
        intent.putExtra("gasOrderVo",(GasOrderVo)orderInfoAdapter.getItem(position));
        startActivity(intent);
    }
}

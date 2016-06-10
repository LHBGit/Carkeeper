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

        /*String json = "{\"code\":\"250\",\"resultInfo\":\"操作成功\",\"resultParm\":{\"gasOrderVos\":[{\"amountOfGasoline\":\"1.0\",\"createTime\":\"2016-06-07\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97\",\"orderNum\":\"08853b70-b7d0-427d-accb-643aec33ee1f\",\"reserveTime\":\"2016-06-06\",\"statu\":\"0\"},{\"amountOfGasoline\":\"2.0\",\"createTime\":\"2016-06-07\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97\",\"orderNum\":\"5d9e299f-a508-402e-807a-1c6578f7131b\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"3.0\",\"createTime\":\"2016-06-07\",\"gasStationName\":\"东华加油站\",\"gasType\":\"0#车柴\",\"orderNum\":\"c1694b7f-8d6e-4949-95de-e029776a3f48\",\"reserveTime\":\"2016-06-08\",\"statu\":\"0\"},{\"amountOfGasoline\":\"4.0\",\"createTime\":\"2016-06-07\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"c50683f2-b9ae-4566-b113-427335534fa4\",\"reserveTime\":\"2016-06-09\",\"statu\":\"0\"},{\"amountOfGasoline\":\"5.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"9510c0cf-c961-4be5-97fe-b29df7417e44\",\"reserveTime\":\"2016-06-09\",\"statu\":\"0\"},{\"amountOfGasoline\":\"6.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"16f86e69-483d-42cf-9cf2-3573dc7afca5\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"7.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"1fa4d7db-9b73-4019-891d-deda919a4174\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"8.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"3cf9aabf-bb10-477d-9c69-5a481cedd580\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"9.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"495304db-4ea4-4118-855a-a0836b92e195\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"10.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"ae5245c7-1a76-4e9c-8b28-e3bf214ad337\",\"reserveTime\":\"2016-06-10\",\"statu\":\"0\"},{\"amountOfGasoline\":\"11.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"7624f763-ba2b-4829-b341-6eaee7f6d206\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"12.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"148b9da7-3f94-45d2-8dca-939aeb8122c1\",\"reserveTime\":\"2016-07-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"13.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"cb19c4bd-5727-412f-ad2d-8eedd7130a2f\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"14.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"b01978c1-ef9d-45c5-a3a6-9742955a8eee\",\"reserveTime\":\"2016-07-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"15.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"0558f971-5790-4416-97f5-ddab56dde626\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"16.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"ec6cb2dc-cddd-4c6c-8c09-f4f50c5bd8a0\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"17.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"e5e837b2-cec7-4205-889f-ae0edd2f434b\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"18.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"22e1b65e-d3e0-49fc-8db9-8c736a835236\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"19.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"1230af9e-6b1d-4ec0-90b0-9751ceabc2e0\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"20.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"237eef8e-9e02-4261-aa49-320fdb267b0e\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"21.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"81302abc-a382-4235-942e-efc0bad928e5\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"22.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"3bb08d19-579a-4646-a7eb-be2085fac0be\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"23.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"05208651-97fa-4ce0-bc83-6137b24aa561\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"},{\"amountOfGasoline\":\"24.0\",\"createTime\":\"2016-06-08\",\"gasStationName\":\"中华加油站\",\"gasType\":\"97*sdsadf\",\"orderNum\":\"a36b432d-93cb-4767-abfe-ce6ee50d4b22\",\"reserveTime\":\"2016-06-07\",\"statu\":\"0\"}]}}";
        ResultMessage resultMessage = JSON.parseObject(json,ResultMessage.class);
        List<GasOrderVo> array = JSON.parseArray(resultMessage.getResultParm().get("gasOrderVos").toString(),GasOrderVo.class);*/


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

package com.wteam.carkeeper.personcenter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.PinnedSectionListView;
import com.wteam.carkeeper.custom.SystemInfoAdapter;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.entity.SystemInfoVo;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.NetCallBack;
import com.wteam.carkeeper.network.UrlManagement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/5/6.
 */
public class SystemMessageActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,TopBar.Top_bar_tv_5_ClickListener{

    private TopBar system_msg_top_bar;
    private List<SystemInfoVo> systemInfoVos;
    private SystemInfoAdapter systemInfoAdapter;
    private PinnedSectionListView pinnedSectionListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_msg);
        initView();
        init();
        //滚动到固定位置pinnedSectionListView.setSelection(15);

    }

    private void init() {
        systemInfoVos = new ArrayList<SystemInfoVo>();
        systemInfoAdapter = new SystemInfoAdapter(this,systemInfoVos);
        pinnedSectionListView = (PinnedSectionListView) findViewById(android.R.id.list);
        pinnedSectionListView.setAdapter(systemInfoAdapter);

        HttpUtil.post(UrlManagement.QUERY_SYSTEM_INFO_ORDER_BY_TIME, null, new NetCallBack(UrlManagement.QUERY_SYSTEM_INFO_ORDER_BY_TIME, null) {
            @Override
            public void success(int statusCode, Header[] headers, String responseString) {
                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        List<SystemInfoVo> list = JSON.parseArray(resultMessage.getResultParm().get("systemInfoVos").toString(),SystemInfoVo.class);
                        systemInfoAdapter = new SystemInfoAdapter(SystemMessageActivity.this,list);
                        pinnedSectionListView.setAdapter(systemInfoAdapter);
                    }
                }
            }

            @Override
            public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                Toast.makeText(SystemMessageActivity.this,"网络连接超时，数据加载失败！" , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        system_msg_top_bar = (TopBar) findViewById(R.id.system_msg_top_bar);

        system_msg_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        system_msg_top_bar.setOnTop_bar_tv_5_ClickListener(this);
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
        final Date date = new Date();
        DatePickerDialog datePickerDialog = new DatePickerDialog(SystemMessageActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                if(year < date.getYear()+1900 || monthOfYear <date.getMonth() || dayOfMonth < date.getDate()) {
                    Toast.makeText(SystemMessageActivity.this,"预约日期不能早于当前日期！",Toast.LENGTH_LONG).show();
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

                String target = year + "-" + month + "-" +  day;
                int temp = 0;

                //小于（最接近）等于选择的日期
                for(int i=0;i<systemInfoAdapter.getCount();i++) {
                    if(systemInfoAdapter.getItem(i) instanceof String) {
                        String title = (String) systemInfoAdapter.getItem(i);
                        int flag = title.compareTo(target);
                        if(flag <= 0) {
                            temp = i;
                            break;
                        }
                        if(flag > 0) {
                            break;
                        }

                    }
                }

                pinnedSectionListView.setSelection(temp);

            }
        },(date.getYear()+1900),date.getMonth(),date.getDate());
        datePickerDialog.show();
        view.setClickable(true);
    }
}

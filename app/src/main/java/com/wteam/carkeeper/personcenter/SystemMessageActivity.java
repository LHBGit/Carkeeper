package com.wteam.carkeeper.personcenter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.PinnedSectionListView;
import com.wteam.carkeeper.custom.SystemInfoAdapter;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.SystemInfo;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lhb on 2016/5/6.
 */
public class SystemMessageActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,TopBar.Top_bar_tv_5_ClickListener{

    private TopBar system_msg_top_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_msg);
        initView();

        ArrayList<SystemInfo> arrayList = new ArrayList<>();
        for(int i = 0;i<50;i++) {
            SystemInfo systemInfo = new SystemInfo();
            systemInfo.setContent("内容");
            systemInfo.setTitle("标题"+i);
            systemInfo.setDate(new Date(System.currentTimeMillis()));
            arrayList.add(systemInfo);
        }
        SystemInfoAdapter systemInfoAdapter = new SystemInfoAdapter(this,arrayList);
        final PinnedSectionListView pinnedSectionListView = (PinnedSectionListView) findViewById(android.R.id.list);
        pinnedSectionListView.setAdapter(systemInfoAdapter);
        //滚动到固定位置pinnedSectionListView.setSelection(15);

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

                Toast.makeText(SystemMessageActivity.this,year + "-" + month + "-" +  day,Toast.LENGTH_LONG).show();

            }
        },(date.getYear()+1900),date.getMonth(),date.getDate());
        datePickerDialog.show();
        view.setClickable(true);
    }
}

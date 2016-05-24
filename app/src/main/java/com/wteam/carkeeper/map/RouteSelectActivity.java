package com.wteam.carkeeper.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/5/24.
 */
public class RouteSelectActivity extends AppCompatActivity implements
        TopBar.Top_bar_tv_1_ClickListener,TopBar.Top_bar_tv_2_ClickListener,TopBar.Top_bar_tv_4_ClickListener,View.OnClickListener{

    private TopBar route_select_top_bar;
    private EditText route_select_start_place;
    private EditText passing_point_1;
    private EditText passing_point_2;
    private EditText passing_point_3;
    private EditText route_select_end_place;
    private TextView route_select_search;
    private LinearLayout divider_passing_point_1;
    private LinearLayout divider_passing_point_2;
    private LinearLayout divider_passing_point_3;
    private ImageView divider_start_end_img_left;
    private ImageView divider_start_end_img_right;
    private ImageView divider_passing_point_1_img_left;
    private ImageView divider_passing_point_2_img_left;

    private TextView top_bar_tv_2;
    private TextView top_bar_tv_4;

    private int passingPointNum = 0;
    private int wayFlag = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_route_select);
        initView();
    }

    private void initView() {
        route_select_top_bar = (TopBar) findViewById(R.id.route_select_top_bar);
        route_select_start_place = (EditText) findViewById(R.id.route_select_start_place);
        passing_point_1 = (EditText) findViewById(R.id.passing_point_1);
        passing_point_2 = (EditText) findViewById(R.id.passing_point_2);
        passing_point_3 = (EditText) findViewById(R.id.passing_point_3);
        route_select_end_place = (EditText) findViewById(R.id.route_select_end_place);
        route_select_search = (TextView) findViewById(R.id.route_select_search);
        divider_passing_point_1 = (LinearLayout) findViewById(R.id.divider_passing_point_1);
        divider_passing_point_2 = (LinearLayout) findViewById(R.id.divider_passing_point_2);
        divider_passing_point_3 = (LinearLayout) findViewById(R.id.divider_passing_point_3);
        divider_start_end_img_left = (ImageView) findViewById(R.id.divider_start_end_img_left);
        divider_start_end_img_right = (ImageView) findViewById(R.id.divider_start_end_img_right);
        divider_passing_point_1_img_left = (ImageView) findViewById(R.id.divider_passing_point_1_img_left);
        divider_passing_point_2_img_left = (ImageView) findViewById(R.id.divider_passing_point_2_img_left);
        top_bar_tv_2 = (TextView) route_select_top_bar.findViewById(R.id.top_bar_tv_2);
        top_bar_tv_4 = (TextView) route_select_top_bar.findViewById(R.id.top_bar_tv_4);

        route_select_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        route_select_top_bar.setOnTop_bar_tv_2_ClickListener(this);
        route_select_top_bar.setOnTop_bar_tv_4_ClickListener(this);
        divider_start_end_img_left.setOnClickListener(this);
        divider_start_end_img_right.setOnClickListener(this);
        divider_passing_point_1_img_left.setOnClickListener(this);
        divider_passing_point_2_img_left.setOnClickListener(this);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        finish();
    }

    @Override
    public void top_bar_tv_2_click(View view) {
        if(wayFlag != 2) {
            top_bar_tv_2.setBackgroundResource(R.drawable.car_light);
            top_bar_tv_4.setBackgroundResource(R.drawable.walk);
            divider_start_end_img_left.setVisibility(View.VISIBLE);
            wayFlag = 2;
            route_select_search.setText("开 车 去");
        }
    }

    @Override
    public void top_bar_tv_4_click(View view) {
        if(wayFlag != 4) {
            for(int i=passingPointNum;i>0;i--) {
                doReducePassingPoint();
            }

            top_bar_tv_2.setBackgroundResource(R.drawable.car);
            top_bar_tv_4.setBackgroundResource(R.drawable.walk_light);
            divider_start_end_img_left.setVisibility(View.INVISIBLE);
            wayFlag = 4;
            route_select_search.setText("走 路 去");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.divider_start_end_img_left:
                doPassingPoint(divider_start_end_img_left.getTag().toString());
                break;
            case R.id.divider_passing_point_1_img_left:
                doPassingPoint(divider_passing_point_1_img_left.getTag().toString());
                break;
            case R.id.divider_passing_point_2_img_left:
                doPassingPoint(divider_passing_point_2_img_left.getTag().toString());
                break;
            case R.id.divider_start_end_img_right:
                swapStartAndEnd();
                break;
            default:break;
        }
    }

    private void swapStartAndEnd() {
        String startLocation = route_select_start_place.getText().toString().trim();
        String endLocation = route_select_end_place.getText().toString().trim();
        if(startLocation == null || "".equals(startLocation)) {
            Toast.makeText(RouteSelectActivity.this,"起始点不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(endLocation == null || "".equals(endLocation)) {
            Toast.makeText(RouteSelectActivity.this,"起始点不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        route_select_start_place.setText(endLocation);
        route_select_end_place.setText(startLocation);
    }

    private void doPassingPoint(String tag) {
        if("R.drawable.add_place".equals(tag)) {
            doAddPassingPoint();
        } else {
            doReducePassingPoint();
        }
    }

    private void doAddPassingPoint() {
        if(passingPointNum == 0) {
            passing_point_1.setVisibility(View.VISIBLE);
            divider_passing_point_1.setVisibility(View.VISIBLE);
            divider_start_end_img_left.setImageResource(R.drawable.reduce_place);
            divider_start_end_img_left.setTag("R.drawable.reduce_place");
            passingPointNum++;
            return;
        }

        if(passingPointNum == 1) {
            passing_point_2.setVisibility(View.VISIBLE);
            divider_passing_point_2.setVisibility(View.VISIBLE);
            divider_passing_point_1_img_left.setImageResource(R.drawable.reduce_place);
            divider_passing_point_1_img_left.setTag("R.drawable.reduce_place");
            passingPointNum++;
            return;
        }

        if(passingPointNum == 2) {
            divider_passing_point_2_img_left.setImageResource(R.drawable.reduce_place);
            divider_passing_point_2_img_left.setTag("R.drawable.reduce_place");
            passing_point_3.setVisibility(View.VISIBLE);
            divider_passing_point_3.setVisibility(View.VISIBLE);
            passingPointNum++;
            return;
        }
    }

    private void doReducePassingPoint() {
        if(passingPointNum == 3) {
            divider_passing_point_3.setVisibility(View.GONE);
            passing_point_3.setVisibility(View.GONE);
            divider_passing_point_2_img_left.setImageResource(R.drawable.add_place);
            divider_passing_point_2_img_left.setTag("R.drawable.add_place");
            passingPointNum--;
            return;
        }

        if(passingPointNum == 2) {
            passing_point_2.setVisibility(View.GONE);
            divider_passing_point_2.setVisibility(View.GONE);
            divider_passing_point_1_img_left.setImageResource(R.drawable.add_place);
            divider_passing_point_1_img_left.setTag("R.drawable.add_place");
            passingPointNum--;
            return;
        }

        if(passingPointNum == 1) {
            passing_point_1.setVisibility(View.GONE);
            divider_passing_point_1.setVisibility(View.GONE);
            divider_start_end_img_left.setImageResource(R.drawable.add_place);
            divider_start_end_img_left.setTag("R.drawable.add_place");
            passingPointNum--;
            return;
        }
    }
}

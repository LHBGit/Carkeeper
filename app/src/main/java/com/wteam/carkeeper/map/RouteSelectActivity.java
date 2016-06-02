package com.wteam.carkeeper.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhb on 2016/5/24.
 */
public class RouteSelectActivity extends AppCompatActivity implements
        TopBar.Top_bar_tv_1_ClickListener,TopBar.Top_bar_tv_2_ClickListener,
        View.OnFocusChangeListener,TopBar.Top_bar_tv_4_ClickListener,View.OnClickListener,
        TextWatcher,Inputtips.InputtipsListener,AdapterView.OnItemClickListener {

    private TopBar route_select_top_bar;
    private AutoCompleteTextView route_select_start_place;
    private AutoCompleteTextView passing_point_1;
    private AutoCompleteTextView passing_point_2;
    private AutoCompleteTextView passing_point_3;
    private AutoCompleteTextView route_select_end_place;
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

    public static final int WAY_FLAG_DRIVE = 2;
    public static final int WAT_FLAG_WALK = 4;

    /**
     * 途经点个数
     */
    private int passingPointNum = 0;
    /**
     * 2：汽车出行方式
     * 4：步行出行方式
     */
    private int wayFlag = WAY_FLAG_DRIVE;
    /**
     * 0:非AutoCompleteTextView获取焦点
     * 1：起点输入框
     * 2：途经点1输入框
     * 3;途经点2输入框
     * 4：途经点3输入框
     * 5：终点输入框
     */
    private int focus = 0;
    private LatLonPoint[] latLonPoints = new LatLonPoint[5];
    private List<Tip> tipList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_route_select);
        initView();
        init();
    }

    private void init() {
        latLonPoints[0] = new LatLonPoint(-1,-1);
    }

    private void initView() {
        route_select_top_bar = (TopBar) findViewById(R.id.route_select_top_bar);
        route_select_start_place = (AutoCompleteTextView) findViewById(R.id.route_select_start_place);
        passing_point_1 = (AutoCompleteTextView) findViewById(R.id.passing_point_1);
        passing_point_2 = (AutoCompleteTextView) findViewById(R.id.passing_point_2);
        passing_point_3 = (AutoCompleteTextView) findViewById(R.id.passing_point_3);
        route_select_end_place = (AutoCompleteTextView) findViewById(R.id.route_select_end_place);
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

        route_select_start_place.setOnFocusChangeListener(this);
        passing_point_1.setOnFocusChangeListener(this);
        passing_point_2.setOnFocusChangeListener(this);
        passing_point_3.setOnFocusChangeListener(this);
        route_select_end_place.setOnFocusChangeListener(this);

        route_select_search.setOnClickListener(this);

        route_select_start_place.addTextChangedListener(this);
        passing_point_1.addTextChangedListener(this);
        passing_point_2.addTextChangedListener(this);
        passing_point_3.addTextChangedListener(this);
        route_select_end_place.addTextChangedListener(this);

        route_select_start_place.setOnItemClickListener(this);
        passing_point_1.setOnItemClickListener(this);
        passing_point_2.setOnItemClickListener(this);
        passing_point_3.setOnItemClickListener(this);
        route_select_end_place.setOnItemClickListener(this);

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
            wayFlag = WAY_FLAG_DRIVE;
            route_select_search.setText("开 车 去");
        }
    }

    @Override
    public void top_bar_tv_4_click(View view) {
        if(wayFlag != 4) {
            for(int i=passingPointNum;i>0;i--) {
                switch (i) {
                    case 3:
                        doReducePassingPoint(divider_passing_point_2_img_left);
                        break;
                    case 2:
                        doReducePassingPoint(divider_passing_point_1_img_left);
                        break;
                    case 1:
                        doReducePassingPoint(divider_start_end_img_left);
                        break;
                }
            }

            top_bar_tv_2.setBackgroundResource(R.drawable.car);
            top_bar_tv_4.setBackgroundResource(R.drawable.walk_light);
            divider_start_end_img_left.setVisibility(View.INVISIBLE);
            wayFlag = WAT_FLAG_WALK;
            route_select_search.setText("走 路 去");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.divider_start_end_img_left:
                doPassingPoint(divider_start_end_img_left.getTag().toString(),v);
                break;
            case R.id.divider_passing_point_1_img_left:
                doPassingPoint(divider_passing_point_1_img_left.getTag().toString(),v);
                break;
            case R.id.divider_passing_point_2_img_left:
                doPassingPoint(divider_passing_point_2_img_left.getTag().toString(),v);
                break;
            case R.id.divider_start_end_img_right:
                swapStartAndEnd();
                break;
            case R.id.route_select_search:
                doSearch();
                break;
            default:break;
        }
    }

    private void doSearch() {
        if(latLonPoints[0] == null) {
            Toast.makeText(RouteSelectActivity.this,"起始点不为空！",Toast.LENGTH_LONG).show();
            return;
        }
        if(latLonPoints[4] == null) {
            Toast.makeText(RouteSelectActivity.this,"终点不为空！",Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("wayFlag",wayFlag);
        for(int i=0;i<5;i++) {
            intent.putExtra("point_" + i,latLonPoints[i]);
        }
        setResult(RESULT_OK,intent);
        finish();
    }

    private void swapStartAndEnd() {
        String startLocation = route_select_start_place.getText().toString().trim();
        String endLocation = route_select_end_place.getText().toString().trim();
        if(startLocation == null || "".equals(startLocation)) {
            Toast.makeText(RouteSelectActivity.this,"起始点不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(endLocation == null || "".equals(endLocation)) {
            Toast.makeText(RouteSelectActivity.this,"终点不能为空！",Toast.LENGTH_SHORT).show();
            return;
        }
        route_select_start_place.removeTextChangedListener(this);
        route_select_end_place.removeTextChangedListener(this);
        route_select_start_place.setAdapter(null);
        route_select_end_place.setAdapter(null);
        route_select_start_place.setText(endLocation);
        route_select_end_place.setText(startLocation);

        LatLonPoint temp = latLonPoints[0];
        latLonPoints[0] = latLonPoints[4];
        latLonPoints[4] = temp;

        route_select_start_place.addTextChangedListener(this);
        route_select_end_place.addTextChangedListener(this);
    }

    private void doPassingPoint(String tag,View view) {
        if("R.drawable.add_place".equals(tag)) {
            doAddPassingPoint();
        } else {
            doReducePassingPoint(view);
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

    private void doReducePassingPoint(View view) {

        switch (view.getId()) {
            case R.id.divider_start_end_img_left:
                passing_point_1.setText(passing_point_2.getText().toString().trim());
                passing_point_2.setText(passing_point_3.getText().toString().trim());
                passing_point_3.setText("");
                latLonPoints[1] = latLonPoints[2];
                latLonPoints[2] = latLonPoints[3];
                latLonPoints[3] = null;
                break;
            case R.id.divider_passing_point_1_img_left:
                passing_point_2.setText(passing_point_3.getText().toString().trim());
                passing_point_3.setText("");
                latLonPoints[2] = latLonPoints[3];
                latLonPoints[3] = null;
                break;
            case R.id.divider_passing_point_2_img_left:
                passing_point_3.setText("");
                latLonPoints[3] = null;
                break;
            default:
                break;
        }

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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus) {
            switch (v.getId()) {
                case R.id.route_select_start_place:
                    this.focus = 1;
                    break;
                case R.id.passing_point_1:
                    this.focus = 2;
                    break;
                case R.id.passing_point_2:
                    this.focus = 3;
                    break;
                case R.id.passing_point_3:
                    this.focus = 4;
                    break;
                case R.id.route_select_end_place:
                    this.focus = 5;
                    break;
                default:
                    this.focus = 0;
                    break;
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        if (!"".equals(newText)) {
            InputtipsQuery inputQuery = new InputtipsQuery(newText,"");
            Inputtips inputTips = new Inputtips(RouteSelectActivity.this, inputQuery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == 1000) {// 正确返回
            this.tipList = tipList;
            List<String> listString = new ArrayList<String>();
            listString.add("我的位置");
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
                    getApplicationContext(),
                    R.layout.item_route_inputs, listString);
            switch (focus) {
                case 1:
                    route_select_start_place.setAdapter(aAdapter);
                    break;
                case 2:
                    passing_point_1.setAdapter(aAdapter);
                    break;
                case 3:
                    passing_point_2.setAdapter(aAdapter);
                    break;
                case 4:
                    passing_point_3.setAdapter(aAdapter);
                    break;
                case 5:
                    route_select_end_place.setAdapter(aAdapter);
                    break;
            }
            aAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this,"提示查询错误码:"+rCode,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(focus != 0) {
            if(position == 0) {
                latLonPoints[focus-1] = new LatLonPoint(-1,-1);
            } else {
                latLonPoints[focus-1] = tipList.get(position-1).getPoint();
            }
        }
    }
}

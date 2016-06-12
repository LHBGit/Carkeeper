package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.CarInfoAdapter;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.CarInfoVo;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.NetCallBack;
import com.wteam.carkeeper.network.UrlManagement;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/5/10.
 */
public class GarageActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,TopBar.Top_bar_tv_5_ClickListener,AdapterView.OnItemClickListener{

    private ListView garage_info_list;
    private TopBar garage_top_bar;
    private List<CarInfoVo> carInfoVos;
    private CarInfoAdapter carInfoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage);
        initView();
        init();
    }

    private void init() {
        carInfoVos = new ArrayList<CarInfoVo>();

/*        for(int i=0;i<10;i++) {
            CarInfoVo carInfoVo = new CarInfoVo();
            carInfoVo.setCarBrand("法拉利" + i);
            carInfoVo.setCarImageUrl("/car-logo/"+(i+1)+".jpg");
            carInfoVo.setCarLogo("/car-logo/"+(i+1)+".jpg");
            carInfoVo.setCarNum("粤A-8888" + i);
            carInfoVo.setCarType("R7-T3" + i);
            carInfoVos.add(carInfoVo);
        }*/

        carInfoAdapter = new CarInfoAdapter(this,carInfoVos);
        garage_info_list.setAdapter(carInfoAdapter);

        HttpUtil.post(UrlManagement.QUERY_CAR_INFO_BY_SYSUSER_ID, null, new NetCallBack(UrlManagement.SAVE_CAR_INFO, null) {
            @Override
            public void success(int statusCode, Header[] headers, String responseString) {
                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        String json = resultMessage.getResultParm().get("carInfoVos").toString();
                        List<CarInfoVo> list = JSON.parseArray(json,CarInfoVo.class);
                        carInfoVos.clear();
                        carInfoVos.addAll(list);
                        carInfoAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                Toast.makeText(GarageActivity.this,"网络连接超时，请检查网络后再试！" , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initView() {
        garage_info_list = (ListView) findViewById(R.id.garage_info_list);
        garage_top_bar = (TopBar) findViewById(R.id.garage_top_bar);
        garage_info_list.setOnItemClickListener(this);
        garage_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        garage_top_bar.setOnTop_bar_tv_5_ClickListener(this);
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

            if(!result.matches("\\{"
                    + "(\"(.*)\":(.*))?((,\"(.*)\":(.*))*)" + "\\}")) {
                Toast.makeText(GarageActivity.this, "二维码参数错误！" , Toast.LENGTH_LONG).show();
                return;
            }

            RequestParams requestParams = new RequestParams();
            requestParams.add("carInfoVo",result);

            HttpUtil.post(UrlManagement.SAVE_CAR_INFO, requestParams, new NetCallBack(UrlManagement.SAVE_CAR_INFO, requestParams) {
                @Override
                public void success(int statusCode, Header[] headers, String responseString) {
                    if(null != responseString) {
                        ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                        if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                            Toast.makeText(GarageActivity.this, "添加车辆成功！" , Toast.LENGTH_LONG).show();
                            HttpUtil.post(UrlManagement.QUERY_CAR_INFO_BY_SYSUSER_ID, null, new NetCallBack(UrlManagement.SAVE_CAR_INFO, null) {
                                @Override
                                public void success(int statusCode, Header[] headers, String responseString) {
                                    if(null != responseString) {
                                        ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                                        if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                                            String json = resultMessage.getResultParm().get("carInfoVos").toString();
                                            List<CarInfoVo> list = JSON.parseArray(json,CarInfoVo.class);
                                            carInfoVos.clear();
                                            carInfoVos.addAll(list);
                                            carInfoAdapter.notifyDataSetChanged();
                                        }
                                    }
                                }

                                @Override
                                public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                                    Toast.makeText(GarageActivity.this,"网络连接超时，请检查网络后再试！" , Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if(CodeType.ARGUMENT_ERROR.getCode().equals(resultMessage.getCode())) {
                            Toast.makeText(GarageActivity.this, "参数错误，添加失败！" , Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                    Toast.makeText(GarageActivity.this,"网络连接超时，请检查网络后再试！" , Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(GarageActivity.this,CarInfoDetailActivity.class);
        intent.putExtra("carInfoVo",carInfoVos.get(position));
        startActivity(intent);
    }
}

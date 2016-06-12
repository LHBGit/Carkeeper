package com.wteam.carkeeper.personcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.CarInfoVo;
import com.wteam.carkeeper.network.UrlManagement;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lhb on 2016/5/11.
 */
public class CarInfoDetailActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener{

    private TopBar car_info_detail_top_bar;
    private CarInfoVo carInfoVo;
    private ImageView car_info_car_image;
    private CircleImageView car_info_detail_logo;
    private TextView car_info_detail_car_type;
    private TextView car_info_detail_car_num;
    private TextView car_info_detail_engine_num;
    private TextView car_info_detail_car_level;
    private TextView car_info_detail_current_amount;
    private TextView car_info_detail_amount_of_gas;
    private TextView car_info_detail_engine_performance;
    private TextView car_info_detail_transmission_performance;
    private TextView car_info_detail_head_light_performance;

    private ImageLoader imageLoader;
    private DisplayImageOptions options_car;        // DisplayImageOptions是用于设置图片显示的类
    private DisplayImageOptions options_logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info_detail);
        initView();
        init();
    }

    private void init() {

        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        options_car = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_car)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_car)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_car)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();                                   // 创建配置过得DisplayImageOption对象

        options_logo = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.default_person_icon)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_person_icon)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_person_icon)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                .build();                                   // 创建配置过得DisplayImageOption对象

        carInfoVo = (CarInfoVo) getIntent().getExtras().get("carInfoVo");

        if(carInfoVo != null) {
            imageLoader.displayImage(UrlManagement.BASE_URL + carInfoVo.getCarImageUrl(), car_info_car_image, options_car);
            imageLoader.displayImage(UrlManagement.BASE_URL + carInfoVo.getCarLogo(), car_info_detail_logo, options_logo);
            car_info_detail_car_type.setText(carInfoVo.getCarType());
            car_info_detail_car_num.setText(carInfoVo.getCarNum());
            car_info_detail_engine_num.setText(carInfoVo.getEngineNum());
            car_info_detail_car_level.setText(carInfoVo.getNumOfDoor() + "门" + carInfoVo.getNumOfSeat() + "座");
            car_info_detail_current_amount.setText(carInfoVo.getMileage() + " KM");
            car_info_detail_amount_of_gas.setText(carInfoVo.getCurrentAmountOfGasoline() + " L / " + carInfoVo.getTotalAmountOfGasoline() + " L");

            if(true == carInfoVo.getEnginePerformance()) {
                car_info_detail_engine_performance.setText("正常");
            } else {
                car_info_detail_engine_performance.setText("异常");
                car_info_detail_head_light_performance.setTextColor(getResources().getColor(R.color.abnormal_color));
            }

            if( true == carInfoVo.getTransmissionPerformance()) {
                car_info_detail_transmission_performance.setText("正常");
            } else {
                car_info_detail_transmission_performance.setText("异常");
                car_info_detail_head_light_performance.setTextColor(getResources().getColor(R.color.abnormal_color));
            }

            if(true == carInfoVo.getHeadlightStatu()) {
                car_info_detail_head_light_performance.setText("正常");
            } else {
                car_info_detail_head_light_performance.setText("异常");
                car_info_detail_head_light_performance.setTextColor(getResources().getColor(R.color.abnormal_color));
            }
        }
    }

    private void initView() {
        car_info_detail_top_bar = (TopBar) findViewById(R.id.car_info_detail_top_bar);
        car_info_car_image = (ImageView) findViewById(R.id.car_info_car_image);
        car_info_detail_logo = (CircleImageView) findViewById(R.id.car_info_detail_logo);
        car_info_detail_car_type = (TextView) findViewById(R.id.car_info_detail_car_type);
        car_info_detail_car_num = (TextView) findViewById(R.id.car_info_detail_car_num);
        car_info_detail_engine_num = (TextView) findViewById(R.id.car_info_detail_engine_num);
        car_info_detail_car_level = (TextView) findViewById(R.id.car_info_detail_car_level);
        car_info_detail_current_amount = (TextView) findViewById(R.id.car_info_detail_current_amount);
        car_info_detail_amount_of_gas = (TextView) findViewById(R.id.car_info_detail_amount_of_gas);
        car_info_detail_engine_performance = (TextView) findViewById(R.id.car_info_detail_engine_performance);
        car_info_detail_transmission_performance = (TextView) findViewById(R.id.car_info_detail_transmission_performance);
        car_info_detail_head_light_performance = (TextView) findViewById(R.id.car_info_detail_head_light_performance);

        car_info_detail_top_bar.setOnTop_bar_tv_1_ClickListener(this);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }
}

package com.wteam.carkeeper.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.entity.CarInfoVo;
import com.wteam.carkeeper.network.UrlManagement;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lhb on 2016/6/10.
 */
public class CarInfoAdapter extends BaseAdapter {

    private List<CarInfoVo> carInfoVos;
    private DisplayImageOptions options_car;        // DisplayImageOptions是用于设置图片显示的类
    private DisplayImageOptions options_logo;
    private Context context;
    private ImageLoader imageLoader;
    private ImageLoadingListener animateFirstListener;

    public CarInfoAdapter(Context context, List<CarInfoVo> carInfoVos) {
        this.carInfoVos = carInfoVos;
        this.context = context;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new AnimateFirstDisplayListener();
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
    }
    @Override
    public int getCount() {
        return carInfoVos.size();
    }

    @Override
    public Object getItem(int position) {
        return carInfoVos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder;
        CarInfoVo carInfoVo = carInfoVos.get(position);
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_garage,null);
            holder = new ViewHolder();
            holder.garage_image = (ImageView) view.findViewById(R.id.garage_image);
            holder.car_sequence = (TextView) view.findViewById(R.id.car_sequence);
            holder.car_logo = (CircleImageView) view.findViewById(R.id.car_logo);
            holder.garage_brand = (TextView) view.findViewById(R.id.garage_brand) ;
            holder.model_number = (TextView) view.findViewById(R.id.model_number);
            holder.license_plate_number = (TextView) view.findViewById(R.id.license_plate_number);
            view.setTag(holder);        // 给View添加一个格外的数据
        } else {
            holder = (ViewHolder) view.getTag(); // 把数据取出来
        }

        imageLoader.displayImage(UrlManagement.BASE_URL + carInfoVo.getCarImageUrl(), holder.garage_image, options_car, animateFirstListener);
        holder.car_sequence.setText("NO." + (position+1));
        imageLoader.displayImage(UrlManagement.BASE_URL + carInfoVo.getCarLogo(), holder.car_logo, options_logo, animateFirstListener);
        holder.garage_brand.setText(carInfoVo.getCarBrand());
        holder.model_number.setText(carInfoVo.getCarType());
        holder.license_plate_number.setText(carInfoVo.getCarNum());
        return view;
    }

    class ViewHolder {
        ImageView garage_image;
        TextView car_sequence;
        CircleImageView car_logo;
        TextView garage_brand;
        TextView model_number;
        TextView license_plate_number;
    }

    /**
     * 图片加载第一次显示监听器
     * @author Administrator
     *
     */
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}

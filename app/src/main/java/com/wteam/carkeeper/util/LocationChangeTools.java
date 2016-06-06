package com.wteam.carkeeper.util;

import com.amap.api.maps.model.LatLng;

/**
 * Created by lhb on 2016/6/3.
 */
public class LocationChangeTools {

    private static final  double x_pi = 3.14159265358979324 * 3000.0 / 180.0;;

    /**
     * 百度坐标转高德坐标
     */
    public static LatLng bd2gd(LatLng baidu) {
        double x = baidu.longitude - 0.0065;
        double y = baidu.latitude - 0.006;
        double z = Math.sqrt(x*x + y*y) - 0.00002 * Math.sin(y*x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gaodeLat = z * Math.sin(theta);
        double gaodeLng = z * Math.cos(theta);

        return new LatLng(gaodeLat,gaodeLng);
    }

    /**
     * 高德坐标转百度坐标
     */
    public static LatLng gd2bd(LatLng gaode) {
        double x = gaode.longitude;
        double y = gaode.latitude;
        double z = Math.sqrt(x*x + y*y) + 0.00002 * Math.sin(y + x_pi);
        double theta = Math.atan2(y,x) + 0.000003 * Math.cos(x * x_pi);
        double baiduLng = z * Math.cos(theta) + 0.0065;
        double baiduLat = z * Math.sin(theta) + 0.006;

        return new LatLng(baiduLat,baiduLng);
    }
}

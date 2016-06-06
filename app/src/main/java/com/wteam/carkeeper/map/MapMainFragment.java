package com.wteam.carkeeper.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.entity.StationInfo;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.UrlManagement;
import com.wteam.carkeeper.personcenter.ReservationActivity;
import com.wteam.carkeeper.util.LocationChangeTools;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by lhb on 2016/4/26.
 */
public class MapMainFragment extends Fragment implements LocationSource,
        AMapLocationListener,View.OnClickListener,RouteSearch.OnRouteSearchListener,
        AMap.OnMarkerClickListener,AMap.OnInfoWindowClickListener{

    private AMap aMap;
    private SupportMapFragment supportMapFragment;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Button gasStation;
    private Button route;
    private Button zoomIn;
    private Button zoomOut;
    private Button btnNavi;
    private Button btnClear;
    private Button btnEmulator;

    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private WalkRouteResult mWalkRouteResult;
    private DrivingRouteOverlay mDrivingRouteOverlay;
    private WalkRouteOverlay mWalkRouteOverlay;

    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private ArrayList<LatLonPoint> mPassPoints;
    private int wayFlag;
    private LatLonPoint myLocation;
    private Marker marker1;
    private Marker marker2;
    private Marker marker3;
    private LatLonPoint[] latLonPoints = new LatLonPoint[5];

    private boolean isShowGasStatioin = false;
    private String stationInfoJson;
    private Marker preMarker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_main,container,false);
        //初始化aMap
        Fragment fragment = getChildFragmentManager().getFragments().get(0);
        supportMapFragment = (SupportMapFragment) fragment;
        aMap = supportMapFragment.getMap();
        setUpMap();
        initView(view);
        return view;
    }

    private void initView(View view) {
        gasStation = (Button) view.findViewById(R.id.gas_station);
        route = (Button) view.findViewById(R.id.route);
        zoomIn = (Button) view.findViewById(R.id.zoom_in);
        zoomOut = (Button) view.findViewById(R.id.zoom_out);
        btnNavi = (Button) view.findViewById(R.id.btn_navi);
        btnClear= (Button) view.findViewById(R.id.btn_clear);
        btnEmulator = (Button) view.findViewById(R.id.btnEmulator);

        gasStation.setOnClickListener(this);
        route.setOnClickListener(this);
        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);
        btnNavi.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnEmulator.setOnClickListener(this);

        mRouteSearch = new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEmulator:
                doEmulator();
                break;
            case R.id.btn_navi:
                doNavi();
                break;
            case R.id.btn_clear:
                doClear();
                break;
            case R.id.zoom_in:
                aMap.animateCamera(CameraUpdateFactory.zoomBy(1));
                break;
            case R.id.zoom_out:
                aMap.animateCamera(CameraUpdateFactory.zoomBy(-1));
                break;
            case R.id.gas_station:
                doGasStation(v);
                break;
            case R.id.route:
                Intent intent = new Intent(getActivity(),RouteSelectActivity.class);
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
    }

    private void doGasStation(final View v) {
        v.setClickable(false);
        if(isShowGasStatioin) {
            Log.e("clear--ture","clear" + isShowGasStatioin);
            aMap.clear();
            aMap.setMyLocationEnabled(true);
            isShowGasStatioin = !isShowGasStatioin;
            v.setClickable(true);
        } else {
            if(myLocation != null) {
                LatLng latLng = LocationChangeTools.gd2bd(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()));;

                RequestParams requestParams = new RequestParams();
                requestParams.put("key","b3d026896621d18b250a6fc12163f922");
                requestParams.put("lon",latLng.longitude);
                requestParams.put("lat",latLng.latitude);
                requestParams.put("format","1");
                requestParams.put("r","5000");
                final CoordinateConverter converter = new CoordinateConverter(getActivity());
                converter.from(CoordinateConverter.CoordType.BAIDU);

                HttpUtil.post(UrlManagement.GET_NEARLY_GAS_STATION, requestParams, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Toast.makeText(getActivity(),"网络连接超时，请确认网络正常连接！",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        if(statusCode == 200) {
                            if(null != responseString) {
                                StationInfo stationInfo = JSON.parseObject(responseString, StationInfo.class);

                                if(0 == stationInfo.getError_code() && ("200".equals(stationInfo.getResultcode()))) {
                                    Log.e("clear--false","clear" + isShowGasStatioin);
                                    isShowGasStatioin = !isShowGasStatioin;
                                    mlocationClient.stopLocation();
                                    stationInfoJson = responseString;

                                    List<StationInfo.ResultBean.DataBean> dataBeans = stationInfo.getResult().getData();
                                    for (StationInfo.ResultBean.DataBean dataBean : dataBeans) {
                                        double lat = Double.valueOf(dataBean.getLat());
                                        double lng = Double.valueOf(dataBean.getLon());
                                        LatLng latlng = converter.coord(new LatLng(lat, lng)).convert();

                                        /**
                                         * 拼接价格字符串
                                         */
                                        StringBuffer stringBuffer = new StringBuffer();
                                        String price = dataBean.getPrice();
                                        String gastPrice = dataBean.getGastprice();
                                        if(price != null && !"".equals(price) && !"{}".equals(price)) {
                                            price = price.replace("{","");
                                            price = price.replace("}","");
                                            price = price.replace("\"","");
                                            String[] typeAndPrice = price.split(",");
                                            for(int i=0;i<typeAndPrice.length;i++) {
                                                stringBuffer.append("[" + typeAndPrice[i]+ "]");
                                            }
                                        }

                                        if(gastPrice != null && !"".equals(gastPrice) && !"{}".equals(gastPrice)) {
                                            gastPrice = gastPrice.replace("{","");
                                            gastPrice = gastPrice.replace("}","");
                                            gastPrice = gastPrice.replace("\"","");
                                            String[] typeAndGasPrice = gastPrice.split(",");
                                            for(int i=0;i<typeAndGasPrice.length;i++) {
                                                stringBuffer.append("[" + typeAndGasPrice[i]+ "]");
                                            }
                                        }

                                        aMap.addMarker(new MarkerOptions().position(latlng)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gas_station)).
                                                        title(dataBean.getName() + "\t距离：" + dataBean.getDistance())
                                                        .snippet("\t折扣信息：" + dataBean.getDiscount() + "\t价格：" + stringBuffer.toString()
                                                        + "\n地址：" + dataBean.getAddress())).setObject(dataBean);
                                    }
                                }
                            } else {
                                Toast.makeText(getActivity(),"定位失败，请稍后再试！",Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(),"定位失败，请稍后再试！",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        v.setClickable(true);
                    }
                });
            } else {
                Toast.makeText(getActivity(),"定位失败，请稍后再试！",Toast.LENGTH_LONG).show();
                v.setClickable(true);
            }
        }
    }

    private void doEmulator() {
        Intent intent = new Intent(getActivity(),NaviActivity.class);
        intent.putExtra("naviType", NaviType.EMULATOR);
        intent.putExtra("wayFlag",wayFlag);
        intent.putExtra("point_0",mStartPoint);
        intent.putExtra("point_4",mEndPoint);
        for(int i=1;i<4;i++) {
            intent.putExtra("point_"+i,latLonPoints[i]);
        }
        startActivity(intent);
    }

    private void doNavi() {
        Intent intent = new Intent(getActivity(),NaviActivity.class);
        intent.putExtra("naviType", NaviType.GPS);
        intent.putExtra("wayFlag",wayFlag);
        intent.putExtra("point_0",mStartPoint);
        intent.putExtra("point_4",mEndPoint);
        for(int i=1;i<4;i++) {
            intent.putExtra("point_"+i,latLonPoints[i]);
        }
        startActivity(intent);
    }

    private void doClear() {
        if(null != mDrivingRouteOverlay) {
            mDrivingRouteOverlay.removeFromMap();
            btnNavi.setVisibility(View.GONE);
            btnClear.setVisibility(View.GONE);
            btnEmulator.setVisibility(View.GONE);
        }

        if(null != mWalkRouteOverlay) {
            mWalkRouteOverlay.removeFromMap();
            btnNavi.setVisibility(View.GONE);
            btnClear.setVisibility(View.GONE);
            btnEmulator.setVisibility(View.GONE);
        }

        if(null != marker1) {
            marker1.remove();
        }
        if(null != marker2) {
            marker2.remove();
        }
        if(null != marker3) {
            marker3.remove();
        }
        aMap.setMyLocationEnabled(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == getActivity().RESULT_OK) {
            wayFlag = (int) data.getExtras().get("wayFlag");
            mPassPoints = new ArrayList<>();

            mStartPoint = (LatLonPoint) data.getExtras().get("point_0");
            if((mStartPoint.getLatitude() == -1) && (mStartPoint.getLongitude() == -1)) {
                mStartPoint = myLocation;
            }
            latLonPoints[0] = mStartPoint;

            mEndPoint = (LatLonPoint) data.getExtras().get("point_4");
            if((mEndPoint.getLatitude() == -1) && (mEndPoint.getLongitude() == -1)) {
                mEndPoint = myLocation;
            }
            latLonPoints[4] = mEndPoint;

            for (int i=1;i<4;i++) {
                LatLonPoint latLonPoint = (LatLonPoint) data.getExtras().get("point_" + i);
                if(latLonPoint != null) {
                    if((latLonPoint.getLatitude() == -1) && (latLonPoint.getLongitude() == -1)) {
                        latLonPoint = myLocation;
                    }
                    mPassPoints.add(latLonPoint);
                }
                latLonPoints[i] = latLonPoint;
            }

            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                    mStartPoint, mEndPoint);
            if(wayFlag == RouteSelectActivity.WAY_FLAG_DRIVE) {
                RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, mPassPoints,
                        null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
                mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询

            }

            if(wayFlag == RouteSelectActivity.WAT_FLAG_WALK) {
                RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo,RouteSearch.WalkDefault);
                mRouteSearch.calculateWalkRouteAsyn(query);
            }
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        //aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);

        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                myLocation = new LatLonPoint(aMapLocation.getLatitude(),aMapLocation.getLongitude());
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                Log.e("location",aMapLocation.toStr());

            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Toast.makeText(getActivity(),errText,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(10000);
            mLocationOption.setNeedAddress(false);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    /*
    * 根据fragment切换和显示确定是否定位
    * */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {
            deactivate();
        } else {
            aMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != supportMapFragment) {
            supportMapFragment.onResume();
        }

        if(!isHidden()) {
            aMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(null != supportMapFragment) {
            supportMapFragment.onPause();
        }
        deactivate();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(null != supportMapFragment) {
            supportMapFragment.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            aMap.setMyLocationEnabled(false);
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    mDrivingRouteOverlay = new DrivingRouteOverlay(
                            getActivity(), aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos());

                    mDrivingRouteOverlay.removeFromMap();
                    mDrivingRouteOverlay.addToMap();
                    mDrivingRouteOverlay.zoomToSpan();
                    btnNavi.setVisibility(View.VISIBLE);
                    btnClear.setVisibility(View.VISIBLE);
                    btnEmulator.setVisibility(View.VISIBLE);

                    LatLonPoint passPoint1 = mPassPoints.get(0);
                    if(null != passPoint1) {
                        marker1 = aMap.addMarker(new MarkerOptions().position(new LatLng(
                                passPoint1.getLatitude(),passPoint1.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pass_point_1)));
                    }
                    LatLonPoint passPoint2 = mPassPoints.get(1);
                    if(null != passPoint2) {
                        marker2 = aMap.addMarker( new MarkerOptions().position(new LatLng(
                                passPoint2.getLatitude(),passPoint2.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pass_point_2)));
                    }
                    LatLonPoint passPoint3 = mPassPoints.get(2);
                    if(null != passPoint3) {
                        marker3 =  aMap.addMarker(new MarkerOptions().position(new LatLng(
                                passPoint3.getLatitude(),passPoint3.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pass_point_3)));
                    }
                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(getActivity(),"无结果",Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getActivity(),"无结果",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(),"错误码："+errorCode,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            aMap.setMyLocationEnabled(false);
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                     WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    mWalkRouteOverlay = new WalkRouteOverlay(
                            getActivity(),aMap,walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());

                    mWalkRouteOverlay.removeFromMap();
                    mWalkRouteOverlay.addToMap();
                    mWalkRouteOverlay.zoomToSpan();
                    btnNavi.setVisibility(View.VISIBLE);
                    btnClear.setVisibility(View.VISIBLE);
                    btnEmulator.setVisibility(View.VISIBLE);

                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(getActivity(),"无结果",Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getActivity(),"无结果",Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(),"错误码："+errorCode,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        StationInfo.ResultBean.DataBean dataBean = (StationInfo.ResultBean.DataBean) marker.getObject();
        Intent intent = new Intent(getActivity(), ReservationActivity.class);
        intent.putExtra("dataBean",dataBean);
        intent.putExtra("stationInfoJson",stationInfoJson);
        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(!marker.isInfoWindowShown()) {
            if(null != preMarker) {
                preMarker.hideInfoWindow();
            }
            marker.showInfoWindow();
        } else {
            marker.hideInfoWindow();
        }
        return false;
    }
}

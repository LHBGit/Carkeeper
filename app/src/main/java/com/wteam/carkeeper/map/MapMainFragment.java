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

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.wteam.carkeeper.R;


/**
 * Created by lhb on 2016/4/26.
 */
public class MapMainFragment extends Fragment implements LocationSource,
        AMapLocationListener,View.OnClickListener,RouteSearch.OnRouteSearchListener{

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

    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private DrivingRouteOverlay drivingRouteOverlay;

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

        gasStation.setOnClickListener(this);
        route.setOnClickListener(this);
        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);
        btnNavi.setOnClickListener(this);
        btnClear.setOnClickListener(this);

        mRouteSearch = new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_navi:
                Intent intent1 = new Intent(getActivity(),NaviActivity.class);
                startActivity(intent1);
                drivingRouteOverlay.removeFromMap();
                btnNavi.setVisibility(View.GONE);
                btnClear.setVisibility(View.GONE);
                break;
            case R.id.btn_clear:
                if(null != drivingRouteOverlay) {
                    drivingRouteOverlay.removeFromMap();
                    btnNavi.setVisibility(View.GONE);
                    btnClear.setVisibility(View.GONE);
                }
                break;
            case R.id.zoom_in:
                aMap.animateCamera(CameraUpdateFactory.zoomBy(1));
                break;
            case R.id.zoom_out:
                aMap.animateCamera(CameraUpdateFactory.zoomBy(-1));
                break;
            case R.id.gas_station:
                Toast.makeText(getActivity(),"gas_station",Toast.LENGTH_LONG).show();
                break;
            case R.id.route:
                Intent intent = new Intent(getActivity(),RouteSelectActivity.class);
                startActivityForResult(intent,1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(),"" + requestCode,Toast.LENGTH_LONG).show();
        //NaviLatLng mEndLatlng = new NaviLatLng(21.149657 ,110.31150);
        //NaviLatLng mStartLatlng = new NaviLatLng(21.149727, 110.30128);
        LatLonPoint mStartPoint = new LatLonPoint(21.149727 , 110.30128);
        LatLonPoint mEndPoint = new LatLonPoint(21.149657 ,110.31150);
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null,
                null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                Log.e("position:",aMapLocation.toStr());
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode()+ ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr",errText);
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
            mLocationOption.setInterval(5000);
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
            setUpMap();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null != supportMapFragment) {
            supportMapFragment.onResume();
        }

        if(!isHidden()) {
            setUpMap();
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
        if (errorCode == 0) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    drivingRouteOverlay = new DrivingRouteOverlay(
                            getActivity(), aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos());
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
                    btnNavi.setVisibility(View.VISIBLE);
                    btnClear.setVisibility(View.VISIBLE);
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
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }
}

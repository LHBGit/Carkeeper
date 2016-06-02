package com.wteam.carkeeper.map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.enums.PathPlanningStrategy;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.LatLonPoint;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.util.TTSController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhb on 2016/5/23.
 */
public class NaviActivity extends AppCompatActivity implements AMapNaviListener, AMapNaviViewListener {

    private AMapNaviView mAMapNaviView;
    private AMapNavi mAMapNavi;
    private TTSController mTtsManager;
    private List<NaviLatLng> mStartList = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mEndList = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();
    private int wayFlag;
    private int naviType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        for(int i=0;i<5;i++) {
            LatLonPoint latLonPoint = (LatLonPoint) getIntent().getExtras().get("point_" + i);
            if(i == 0) {
                mStartList.add(new NaviLatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude()));
            } else if(i == 4) {
                mEndList.add(new NaviLatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude()));
            } else {
                if(latLonPoint != null) {
                    mWayPointList.add(new NaviLatLng(latLonPoint.getLatitude(),latLonPoint.getLongitude()));
                }
            }
        }
        wayFlag = (int) getIntent().getExtras().get("wayFlag");
        naviType = (int) getIntent().getExtras().get("naviType");

        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();
        mTtsManager.startSpeaking();

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.startGPS();
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.setEmulatorNaviSpeed(200);
        setContentView(R.layout.activity_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
/*        mStartList.add(mStartLatlng);
        mEndList.add(mEndLatlng);*/
        mAMapNaviView.setNaviMode(AMapNaviView.CAR_UP_MODE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

//        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        mTtsManager.stopSpeaking();
//
//        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
//        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        //since 1.6.0
        //不再在naviview destroy的时候自动执行AMapNavi.stopNavi();
        //请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.stopGPS();
        mAMapNavi.destroy();
        mTtsManager.destroy();
    }

    @Override
    public void onInitNaviFailure() {
        Toast.makeText(this, "init navi Failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInitNaviSuccess() {
        if(wayFlag == RouteSelectActivity.WAY_FLAG_DRIVE) {
            mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
        }

        if(wayFlag == RouteSelectActivity.WAT_FLAG_WALK) {
            mAMapNavi.calculateWalkRoute(mStartList.get(0), mEndList.get(0));
        }
    }

    @Override
    public void onStartNavi(int type) {
        Log.e("tag","onStartNavi");
    }

    @Override
    public void onTrafficStatusUpdate() {
        Log.e("tag","onTrafficStatusUpdate");
    }

    @Override
    public void onLocationChange(AMapNaviLocation location) {
        Toast.makeText(this,"onLocationChange",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetNavigationText(int type, String text) {
        Log.e("tag","onGetNavigationText");
    }

    @Override
    public void onEndEmulatorNavi() {
        Log.e("tag","onEndEmulatorNavi");
    }

    @Override
    public void onArriveDestination() {
        Log.e("tag","onArriveDestination");
    }

    @Override
    public void onCalculateRouteSuccess() {
        if(naviType == NaviType.EMULATOR) {
            mAMapNavi.startNavi(NaviType.EMULATOR);
        }

        if(naviType == NaviType.GPS) {
            mAMapNavi.startNavi(NaviType.GPS);
        }
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int wayID) {

    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
    }

    @Override
    public void onNaviSetting() {
    }


    @Override
    public void onNaviMapMode(int isLock) {
    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {
    }

    @Override
    public void onNextRoadClick() {
    }


    @Override
    public void onScanViewButtonClick() {
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
    }

    @Override
    public void hideCross() {
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
    }

    @Override
    public void hideLaneInfo() {
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
    }

    @Override
    public void notifyParallelRoad(int i) {
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
    }


    @Override
    public void onLockMap(boolean isLock) {
    }

    @Override
    public void onNaviViewLoaded() {
        Log.d("wlx", "导航页面加载成功");
        Log.d("wlx", "请不要使用AMapNaviView.getMap().setOnMapLoadedListener();会overwrite导航SDK内部画线逻辑");
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }
}

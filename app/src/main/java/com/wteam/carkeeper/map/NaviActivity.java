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
import com.autonavi.tbt.TrafficFacilityInfo;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.util.TTSController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhb on 2016/5/23.
 */
public class NaviActivity extends AppCompatActivity implements AMapNaviListener, AMapNaviViewListener {

    AMapNaviView mAMapNaviView;
    AMapNavi mAMapNavi;
    TTSController mTtsManager;
    NaviLatLng mEndLatlng = new NaviLatLng(21.149657 ,110.31150);
    NaviLatLng mStartLatlng = new NaviLatLng(21.149727, 110.30128);
    List<NaviLatLng> mStartList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> mEndList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> mWayPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();
        mTtsManager.startSpeaking();

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.setEmulatorNaviSpeed(150);
        mAMapNavi.startGPS();
        setContentView(R.layout.activity_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
        mStartList.add(mStartLatlng);
        mEndList.add(mEndLatlng);
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
        mWayPointList = new ArrayList<NaviLatLng>();
        NaviLatLng wayPoint1 = new NaviLatLng(21.149627, 110.31128);
        NaviLatLng wayPoint2 = new NaviLatLng(21.149527, 110.30128);
        NaviLatLng wayPoint3 = new NaviLatLng(21.147627, 110.32128);
        mWayPointList.add(wayPoint1);
        mWayPointList.add(wayPoint2);
        mWayPointList.add(wayPoint3);
        mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
        //mAMapNavi.calculateWalkRoute(mStartList.get(0), mEndList.get(0));
        //noStartCalculate();
    }
    /**
     * 如果使用无起点算路，请这样写
     */
    private void noStartCalculate() {
        mAMapNavi.calculateDriveRoute(mEndList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
        //无起点算路须知：
        //AMapNavi在构造的时候，会startGPS，但是GPS启动需要一定时间
        //在刚构造好AMapNavi类之后立刻进行无起点算路，会立刻返回false
        //给人造成一种等待很久，依然没有算路成功 算路失败回调的错觉
        //因此，建议，提前获得AMapNavi对象实例，并判断GPS是否准备就绪
/*        if (mAMapNavi.isGpsReady()) {
            Log.e("mAMapNavi.isGpsReady()","" + mAMapNavi.isGpsReady());
            mAMapNavi.calculateDriveRoute(mEndList, mWayPointList, PathPlanningStrategy.DRIVING_DEFAULT);
        }*/

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
        Log.e("location",""+location.toString());
        Toast.makeText(this,"onLocationChange",Toast.LENGTH_LONG).show();
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
        mAMapNavi.startNavi(NaviType.EMULATOR);
        //mAMapNavi.startNavi(NaviType.GPS);
        Log.e("tag","onCalculateRouteSuccess");
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        Log.e("tag","onCalculateRouteFailure");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        Log.e("tag","onReCalculateRouteForYaw");

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        Log.e("tag","onReCalculateRouteForTrafficJam");

    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        Log.e("tag","onArrivedWayPoint");

    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        Log.e("tag","onGpsOpenStatus"+enabled);
    }

    @Override
    public void onNaviSetting() {
        Log.e("tag","onNaviSetting");
    }


    @Override
    public void onNaviMapMode(int isLock) {
        Log.e("tag","onNaviMapMode");
    }

    @Override
    public void onNaviCancel() {
        finish();
    }


    @Override
    public void onNaviTurnClick() {
        Log.e("tag","onNaviTurnClick");
    }

    @Override
    public void onNextRoadClick() {
        Log.e("tag","onNextRoadClick");
    }


    @Override
    public void onScanViewButtonClick() {
        Log.e("tag","onScanViewButtonClick");
    }

    @Deprecated
    @Override
    public void onNaviInfoUpdated(AMapNaviInfo naviInfo) {
        Log.e("tag","onNaviInfoUpdated");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {
        Log.e("tag","onNaviInfoUpdate");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        Log.e("tag","OnUpdateTrafficFacility");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        Log.e("tag","OnUpdateTrafficFacility");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        Log.e("tag","showCross");
    }

    @Override
    public void hideCross() {
        Log.e("tag","hideCross");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {
        Log.e("tag","showLaneInfo");
    }

    @Override
    public void hideLaneInfo() {
        Log.e("tag","hideLaneInfo");
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {
        Log.e("tag","onCalculateMultipleRoutesSuccess");
    }

    @Override
    public void notifyParallelRoad(int i) {
        Log.e("tag","notifyParallelRoad");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        Log.e("tag","OnUpdateTrafficFacility");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        Log.e("tag","updateAimlessModeStatistics");
    }


    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        Log.e("tag","updateAimlessModeCongestionInfo");
    }


    @Override
    public void onLockMap(boolean isLock) {
        Log.e("tag","onLockMap");
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

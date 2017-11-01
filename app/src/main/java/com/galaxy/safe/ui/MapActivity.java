package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.SpUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2016/6/16.
 */
public class MapActivity extends Activity implements View.OnClickListener {


    MapView mMapView = null;
    BaiduMap mBaiduMap = null;
    BDLocation location;
    private ImageView iv_request;
    private Toolbar tl_bar;
    private PopupWindow ppw;
    private View inflate;

    private MyLocationConfiguration.LocationMode mCurrentMode;
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private String a;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                a = (String) msg.obj;
                Toast.makeText(MapActivity.this, "我的位置是：" + a, Toast.LENGTH_SHORT).show();
            } else if (msg.what == 2) {
                Toast.makeText(MapActivity.this, "定位失败了,请检查.....", Toast.LENGTH_SHORT).show();

            }

        }
    };
    private TextView tv_name, tv_date, adress, mlocation;
    private String la, lo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        initView();
        initToolBar();
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mMapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(7.0f);
        mBaiduMap.setMapStatus(msu);

        mBaiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(this);     //声明LocationClie
        mLocationClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setIsNeedAddress(true);//返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的
        option.setIsNeedLocationDescribe(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedLocationPoiList(true);
        mLocationClient.setLocOption(option);
        //开个新线程
        new Thread() {
            @Override
            public void run() {
                mLocationClient.start();
            }
        }.start();


    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("签到");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tl_bar.inflateMenu(R.menu.menu_map);
        tl_bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.arrive:
                        show();
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 弹窗签到
     */
    private void show() {

        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.arrive, null);
        ppw = new PopupWindow(inflate, RelativeLayout.LayoutParams.FILL_PARENT, 300, true);
        ppw.setAnimationStyle(R.style.popupAnimation);

        inflate.findViewById(R.id.bt_arrive).setOnClickListener(this);
        inflate.findViewById(R.id.bt_refresh).setOnClickListener(this);
        tv_name = (TextView) inflate.findViewById(R.id.tv_name);
        tv_name.setText("签到姓名：" + SpUtils.getString(this, "user", null));

        tv_date = (TextView) inflate.findViewById(R.id.tv_date);
        tv_date.setText("签到时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        mlocation = (TextView) inflate.findViewById(R.id.location);
        mlocation.setText("签到坐标：" + lo + "," + la);

        adress = (TextView) inflate.findViewById(R.id.adress);
        adress.setText("签到地点：" + a);
        ppw.setBackgroundDrawable(new ColorDrawable(
                Color.TRANSPARENT));
        ppw.setOutsideTouchable(false);
        ppw.showAtLocation(iv_request, Gravity.BOTTOM, 0, 0);

//        //获取当前Activity所在的窗体
//        Window dialogWindow = dialog.;
//        //设置Dialog从窗体底部弹出
//        dialogWindow.setGravity(Gravity.BOTTOM);
//        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        lp.y = 20;//设置Dialog距离底部的距离
////       将属性设置给窗体
//        dialogWindow.setAttributes(lp);
//        dialog.show();//显示对话框

    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocationClient.stop();
        mLocationClient.registerLocationListener(myListener);
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        iv_request = (ImageView) findViewById(R.id.request);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        iv_request.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.request:
                mLocationClient.requestLocation();
                break;
            case R.id.bt_arrive:
                Toast.makeText(MapActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                ppw.dismiss();
                break;
            case R.id.bt_refresh:
                final ProgressDialog dialog2 = new ProgressDialog(this);
                dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                dialog2.setMessage("刷新中......");
                dialog2.setCancelable(true);// 设置是否可以通过点击Back键取消
                dialog2.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                dialog2.show();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        int i = 0;
                        while (i < 5) {
                            try {
                                Thread.sleep(200);
                                // 更新进度条的进度,可以在子线程中更新进度条进度
                                i++;
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }

                        dialog2.dismiss();
                        mLocationClient.requestLocation();
                    }
                }).start();
                tv_date.setText("签到时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                break;
        }

    }

    /**
     * 实现接口
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            MapActivity.this.location = location;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());

            la = String.valueOf(location.getLatitude());
            lo = String.valueOf(location.getLongitude());
            SpUtils.putString(MapActivity.this, "Latitude", String.valueOf(location.getLatitude()));
            sb.append("\nlontitude : ");
            SpUtils.putString(MapActivity.this, "Longitude", String.valueOf(location.getLongitude()));
            if (mlocation != null) {
                mlocation.setText("签到坐标：" + String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));
            }
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());

            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            Message ms = Message.obtain();
            if (location.getLocationDescribe() != null) {
                ms.what = 1;
                ms.obj = location.getLocationDescribe();
                mHandler.sendMessage(ms);
                if (adress != null) {
                    adress.setText("签到地点：" + location.getLocationDescribe());
                }
                //  tv_location.setText(location.getLocationDescribe());
                SpUtils.putString(MapActivity.this, "LocationDescribe", location.getLocationDescribe());
            } else {
                ms.what = 2;
                mHandler.sendMessage(ms);
                SpUtils.putString(MapActivity.this, "LocationDescribe", null);
            }
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            // 构造定位数据

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
// 设置定位数据
            mBaiduMap.setMyLocationData(locData);

            LatLng ll = new LatLng(location.getLatitude(),
                    location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

            LatLng point1 = new LatLng(22.568475, 113.867388);
            LatLng point2 = new LatLng(22.578416, 113.837337);
            LatLng point3 = new LatLng(22.588416, 113.817337);
            LatLng point4 = new LatLng(22.598416, 113.877337);
            LatLng point5 = new LatLng(22.558416, 113.817337);

            List<LatLng> point = new ArrayList<LatLng>();
            point.add(point1);
            point.add(point2);
            point.add(point3);
            point.add(point4);
            point.add(point5);
            for (int i = 0; i < point.size(); i++) {

                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.mark);
                OverlayOptions option = new MarkerOptions()
                        .position(point.get(i))
                        .icon(bitmap).title("设备:" + i);

                mBaiduMap.addOverlay(option);
            }
            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {


                    Button button = new Button(getApplicationContext());
                    button.setTextColor(MapActivity.this.getResources().getColor(R.color.black));
                    button.setText(marker.getTitle());

                    button.setBackgroundResource(R.drawable.input_over);
//定义用于显示该InfoWindow的坐标点

//创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                    InfoWindow mInfoWindow = new InfoWindow(button, marker.getPosition(), -47);
//显示InfoWindow
                    mBaiduMap.showInfoWindow(mInfoWindow);

                    Toast.makeText(MapActivity.this, marker.getPosition().toString() + marker.getTitle(), Toast.LENGTH_SHORT).show();

                    return false;
                }
            });









       /*     myLocationOverlay = new LocationOverlay(mMapView);//定位图层初始化

// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）

         BitmapDescriptorFactory
                .fromResource(R.drawable.icon_geo);
        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
        mBaiduMap.setMyLocationConfiguration();*/
// 当不需要定位图层时关闭定位图层
//        mBaiduMap.setMyLocationEnabled(false);

        }
    }
}

/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.galaxy.safe.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.galaxy.safe.R;
import com.galaxy.safe.adapt.ContentAdapt;
import com.galaxy.safe.base.BaseFragment;
import com.galaxy.safe.ui.CardbatchListActivity;
import com.galaxy.safe.ui.CardtypeListActivity;
import com.galaxy.safe.ui.CompanyListActivity;
import com.galaxy.safe.ui.CurveListActivity;
import com.galaxy.safe.ui.HomeActivity;
import com.galaxy.safe.ui.ItemsInfoActivity;
import com.galaxy.safe.ui.ItemsTypeActivity;
import com.galaxy.safe.ui.PersonListActivity;
import com.galaxy.safe.ui.SampleNameActivity;
import com.galaxy.safe.ui.SampletypeActivity;
import com.galaxy.safe.ui.SampletypetopActivity;
import com.galaxy.safe.ui.UnitSettingActivity;
import com.galaxy.safe.utils.NetUtils;
import com.galaxy.safe.utils.SpUtils;
import com.galaxy.safe.utils.UpdateUtils;
import com.galaxy.safe.utils.UpUtils;
import com.galaxy.safe.utils.UserUtils;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.HttpUtils;
import com.nineoldandroids.view.ViewHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class CenterFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {


    private DrawerLayout dl;
    private FrameLayout fl_right;
    private SlidingMenu sm;
    private ViewPager vp_check;
    private List<BaseFragment> list;
    private HttpUtils httpUtils;
    private RadioGroup rg_task;
    private HomeFragment firstFragment;
    private SecondFragment secondFragment;
    private HelpFragment helpFragment;
    private SettingFragment settingFragment;
    private String guest;

    private SQLiteDatabase sdb;
    private TextView tv_user;//右菜单
    private GridView gv_setting;//右菜单的
    //    private Button out;//右菜单中的退出
    private LinearLayout ll_location;//定位
    private TextView tv_location;
    private String[] names = {"单位设置", "人员信息", "企业信息", "样品大类", "样品细类", "样品信息", "项目种类", "项目信息", "卡类型", "卡批次", "标准曲线",};
    private int[] icons = {R.drawable.danwei, R.drawable.person,
            R.drawable.company, R.drawable.item_type, R.drawable.sample_type, R.drawable.sample_info,
            R.drawable.device, R.drawable.item_info, R.drawable.cardt, R.drawable.cardp, R.drawable.curve};




    private LinearLayout bt_up;//同步上传
    private LinearLayout bt_down;//同步下载

    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String a = (String) msg.obj;
                tv_location.setText(a);
            } else if (msg.what == 2) {
                tv_location.setText("定位失败了,请检查......");
            }

        }
    };

    @Override
    public View initview(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.center, null);
        HomeActivity activity = (HomeActivity) mactivity;
        sm = activity.getSlidingMenu();
        fl_right = (FrameLayout) view.findViewById(R.id.fl_right);
        dl = (DrawerLayout) view.findViewById(R.id.dl);
//        tl_bar = (Toolbar) view.findViewById(R.id.tl_bar);
        vp_check = (ViewPager) view.findViewById(R.id.vp_check);
        rg_task = (RadioGroup) view.findViewById(R.id.rg_task);
//        mIndicator = (TabPageIndicator) view.findViewById(R.id.tpi_menu);
        guest = SpUtils.getString(mactivity, "guest", "AD");
        httpUtils = new HttpUtils();
        return view;
    }
    public void openDrawerLayout() {

        if (dl != null) {
            dl.openDrawer(Gravity.RIGHT);
        }

    }


    /**
     * 初始化数据 在fragment中oncreatactivity周期中
     */
    @Override
    public void initDate() {

        mLocationClient = new LocationClient(getContext());     //声明LocationClie
        mLocationClient.registerLocationListener(myListener);
        location();
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数
        initRightView();
        initEvent();
        list = new ArrayList<BaseFragment>();

        helpFragment = new HelpFragment();
        list.add(helpFragment);
        firstFragment = new HomeFragment();
        list.add(firstFragment);
        secondFragment = new SecondFragment();
        list.add(secondFragment);
        settingFragment = new SettingFragment();
        list.add(settingFragment);

        HomeActivity activity = (HomeActivity) mactivity;
        FragmentPagerAdapter adapter1 = new FragmentPagerAdapter(activity.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };
        vp_check.setAdapter(adapter1);
        vp_check.setOnPageChangeListener(this);
        vp_check.setCurrentItem(1);
        rg_task.check(R.id.rb_home);
        rg_task.setOnCheckedChangeListener(this);
    }

    private void location() {
        //开个新线程
        new Thread() {
            @Override
            public void run() {
                LocationClientOption option = new LocationClientOption();
                option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
                option.setIsNeedAddress(true);//返回的定位结果包含地址信息
                option.setNeedDeviceDirect(true);//返回的定位结果包含手机机头的
                option.setIsNeedLocationDescribe(true);
                option.setIsNeedLocationPoiList(true);
                option.setCoorType("bd09ll"); // 设置坐标类型
                mLocationClient.setLocOption(option);
                mLocationClient.start();
            }
        }.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        sdb.close();
        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(myListener);
    }

    @Override
    public void onStart() {
        tv_user.setText(new UserUtils(sdb, mactivity).getPerson_name() + "(" + new UserUtils(sdb, mactivity).getPorperty() + ")");
        super.onStart();
    }



    /**
     * 初始化右菜单界面
     */
    private void initRightView() {

        View view = View.inflate(mactivity, R.layout.right_menu, null);

        tv_user = (TextView) view.findViewById(R.id.tv_user);
        gv_setting = (GridView) view.findViewById(R.id.gv_setting);
//        out = (Button) view.findViewById(R.id.bt_out);
        ll_location = (LinearLayout) view.findViewById(R.id.ll_location);
        tv_location = (TextView) view.findViewById(R.id.tv_location);

        bt_down = (LinearLayout) view.findViewById(R.id.bt_down);
        bt_up = (LinearLayout) view.findViewById(R.id.bt_up);
        ll_location.setOnClickListener(this);
        bt_down.setOnClickListener(this);
        bt_up.setOnClickListener(this);
        ContentAdapt gAdapt = new ContentAdapt(mactivity, names, icons);

        gv_setting.setAdapter(gAdapt);
        gv_setting.setOnItemClickListener(this);
        fl_right.removeAllViews();
        fl_right.addView(view);
    }

    /**
     * 右页面初始化
     */
    private void initEvent() {

        dl.setScrimColor(0x00ffffff);
        dl.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mView = dl.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                ViewHelper.setTranslationX(mView, -mMenu.getMeasuredWidth() * slideOffset);
                ViewHelper.setPivotX(mView, mView.getMeasuredWidth());
                ViewHelper.setPivotY(mView, mView.getMeasuredHeight() / 2);
                mView.invalidate();
                ViewHelper.setScaleX(mView, rightScale);
                ViewHelper.setScaleY(mView, rightScale);
            }

            @Override
            public void onDrawerOpened(View arg0) {
                // TODO Auto-generated method stub
              /*  HomeActivity activity = (HomeActivity) mactivity;
                sm = activity.getSlidingMenu();*/
                sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
            }

            @Override
            public void onDrawerClosed(View arg0) {
                // TODO Auto-generated method stub
                // dl.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
                // Gravity.RIGHT);
                sm.setTouchModeAbove(SlidingMenu.LEFT);

            }
        });
    }

    ProgressDialog dialog3;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ll_location:
                tv_location.setText("");
                final ProgressDialog dialog2 = new ProgressDialog(mactivity);
                dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                dialog2.setTitle("当前位置定位");
                dialog2.setMessage("定位中......");
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
                break;
            case R.id.bt_down:
                dialog3 = new ProgressDialog(mactivity);
                dialog3.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                dialog3.setTitle("同步服务器数据");
                dialog3.setMessage("同步中......");
                dialog3.setCancelable(true);// 设置是否可以通过点击Back键取消
                dialog3.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                dialog3.show();

                getResult("GETT galaxy_card_batch," + guest + "#", 1);//卡批次
                getResult("GETT galaxy_detection_item," + guest + "#", 2);//检测项目
                getResult("GETT galaxy_standard_curve," + guest + "#", 3);//曲线
                getResult("GETT galaxy_card_type," + guest + "#", 4);//卡类型
                getResult("GETT galaxy_unit," + guest + "#", 5);//单位更新
                getResult("GETT galaxy_sample_type," + guest + "#", 6);//样品类型更新
                getResult("GETT galaxy_sample_typedetail," + guest + "#", 7);//样品更新
                break;
            case R.id.bt_up:

                dialog3 = new ProgressDialog(mactivity);
                dialog3.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                dialog3.setTitle("同步本地数据");
                dialog3.setMessage("同步中......");
                dialog3.setCancelable(true);// 设置是否可以通过点击Back键取消
                dialog3.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                dialog3.show();
                File file = new File(Environment.getExternalStorageDirectory() + "/" + "UP");
                if (!file.exists()) {
                    file.mkdirs();
                }

                new UpUtils(mactivity, sdb, "SETT galaxy_sample_typedetail," + guest + ",", 7, dialog3, file).up();
                new UpUtils(mactivity, sdb, "SETT galaxy_card_type," + guest + ",", 4, dialog3, file).up();
                new UpUtils(mactivity, sdb, "SETT galaxy_card_batch," + guest + ",", 1, dialog3, file).up();
                new UpUtils(mactivity, sdb, "SETT galaxy_detection_item," + guest + ",", 2, dialog3, file).up();
                new UpUtils(mactivity, sdb, "SETT galaxy_standard_curve," + guest + ",", 3, dialog3, file).up();
                new UpUtils(mactivity, sdb, "SETT galaxy_unit," + guest + ",", 5, dialog3, file).up();
                new UpUtils(mactivity, sdb, "SETT galaxy_sample_type," + guest + ",", 6, dialog3, file).up();

                break;
        }
    }

    /**
     * 数据下拉服务器数据
     *
     * @param s
     * @param i
     */
    private void getResult(final String s, final int i) {

        new Thread() {
            @Override
            public void run() {
                Socket socket = null;
                socket = new Socket();
                String ip = SpUtils.getString(mactivity, "ip", "121.41.38.46");
                String port = SpUtils.getString(mactivity, "port", "9999");
                try {
                    socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 3000);
                    OutputStream os = socket.getOutputStream();
                    PrintWriter pw = new PrintWriter(os);
                    //输入流
                    InputStream is = socket.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    //3.利用流按照一定的操作，对socket进行读写操作
//                    String info = "GETT galaxy_card_batch,ZD#";
                    pw.write(s);
                    pw.flush();
                    socket.shutdownOutput();
                    //接收服务器的相应
                    String reply = null;
                    while (!((reply = br.readLine()) == null)) {
                        String result = reply.replace("#", "");
                        Log.i("66", result);
                        new UpdateUtils(mactivity, sdb, result, i, dialog3).parsave();
                    }
                    //4.关闭资源
                    br.close();
                    is.close();
                    pw.close();
                    os.close();
                    socket.close();

                } catch (final IOException e) {
//                    dialog3.dismiss();
                    if (i == 1) {
                        mactivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog3.dismiss();
                                Toast.makeText(mactivity, "网络连接错误" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } finally {

                }
            }

        }.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {

            case 0://单位设置界面

                mactivity.startActivity(new Intent(mactivity, UnitSettingActivity.class));
                break;
            case 1://人员信息

                mactivity.startActivity(new Intent(mactivity, PersonListActivity.class));
                break;
            case 2://企业信息界面
                mactivity.startActivity(new Intent(mactivity, CompanyListActivity.class));
                break;
            case 3://样品大类
                mactivity.startActivity(new Intent(mactivity, SampletypetopActivity.class));
                break;
            case 4://样本细类
                mactivity.startActivity(new Intent(mactivity, SampletypeActivity.class));
                break;
            case 5://样本名称
                mactivity.startActivity(new Intent(mactivity, SampleNameActivity.class));
                break;
            case 6://项目种类界面
                mactivity.startActivity(new Intent(mactivity, ItemsTypeActivity.class));
                break;
            case 7://项目信息界面
                mactivity.startActivity(new Intent(mactivity, ItemsInfoActivity.class));
                break;
            case 9://卡批次
                mactivity.startActivity(new Intent(mactivity, CardbatchListActivity.class));

                break;
            case 8://卡类型

                mactivity.startActivity(new Intent(mactivity, CardtypeListActivity.class));
                break;
            case 10://标准曲线

                mactivity.startActivity(new Intent(mactivity, CurveListActivity.class));
                break;

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                rg_task.check(R.id.rb_help);
                break;
            case 1:
                rg_task.check(R.id.rb_home);
                break;
            case 2:
                rg_task.check(R.id.rb_admin);
                break;
            case 3:
                rg_task.check(R.id.rb_setting);
                break;

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int index = -1;
        switch (checkedId) {
            case R.id.rb_help:
                index = 0;
                break;
            case R.id.rb_home:
                index = 1;
                break;
            case R.id.rb_admin:
                index = 2;
                break;
            case R.id.rb_setting:
                index = 3;
                break;
        }
        if (index != -1) {
            vp_check.setCurrentItem(index, false);
//            fList.get(index).initDate();
//                fList.get(index).initData(); // 初始化数据
        }
    }

    /**
     * 实现接口
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            SpUtils.putString(mactivity, "Latitude", String.valueOf(location.getLatitude()));
            sb.append("\nlontitude : ");
            SpUtils.putString(mactivity, "Longitude", String.valueOf(location.getLongitude()));
            String device = SpUtils.getString(mactivity, "device", "");
            if (!device.equals("")) {
                new NetUtils().updateState(device, String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()));
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
                //  tv_location.setText(location.getLocationDescribe());
                SpUtils.putString(mactivity, "LocationDescribe", location.getLocationDescribe());
            } else {
                ms.what = 2;
                mHandler.sendMessage(ms);
                SpUtils.putString(mactivity, "LocationDescribe", null);
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
//            Log.i("66", sb.toString());
        }
    }

}

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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.Bean.Acurve;
import com.galaxy.safe.R;
import com.galaxy.safe.base.BaseFragment;
import com.galaxy.safe.ui.AboutActivity;
import com.galaxy.safe.ui.HomeActivity;
import com.galaxy.safe.ui.MapActivity;
import com.galaxy.safe.ui.SettingsActivity;
import com.galaxy.safe.ui.SplashActivity;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.SaveCurveUtils;
import com.galaxy.safe.utils.SpUtils;
import com.galaxy.safe.utils.StreamUtils;
import com.google.gson.Gson;
import com.mylhyl.superdialog.SuperDialog;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SettingFragment extends BaseFragment implements View.OnClickListener {


    private Toolbar tl_bar;
    private LinearLayout ll_zidian;
    private LinearLayout ll_device;
    private LinearLayout ll_map;
    private LinearLayout ll_about;
    private LinearLayout ll_sao;
    private LinearLayout ll_close;
    private LinearLayout ll_update;
    private LinearLayout ll_net;
    private PackageManager packageManager;
    PackageInfo packInfo;
    private TextView tv_version;
    private TextView tv_wifi;
    private TextView tv_device;
    private int clientVersionCode;
    private WifiManager wifiManager;
    private Switch sw_wifi;
    String ssid;
    WifiInfo connectionInfo;
    private ProgressDialog pd;

    /**
     * 重写父类方法 初始化控件
     *
     * @param inflater
     * @return
     */
    @Override
    public View initview(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.setting, null);
        tl_bar = (Toolbar) view.findViewById(R.id.tl_bar);
        tv_version = (TextView) view.findViewById(R.id.tv_version);
        tv_wifi = (TextView) view.findViewById(R.id.tv_wifi);
        tv_device = (TextView) view.findViewById(R.id.tv_device);
        ll_zidian = (LinearLayout) view.findViewById(R.id.ll_zidian);
        ll_device = (LinearLayout) view.findViewById(R.id.ll_device);
        ll_map = (LinearLayout) view.findViewById(R.id.ll_map);
        ll_about = (LinearLayout) view.findViewById(R.id.ll_about);
        ll_sao = (LinearLayout) view.findViewById(R.id.ll_sao);
        ll_close = (LinearLayout) view.findViewById(R.id.ll_close);
        ll_update = (LinearLayout) view.findViewById(R.id.ll_update);
        ll_net = (LinearLayout) view.findViewById(R.id.ll_net);
        sw_wifi = (Switch) view.findViewById(R.id.sw_wifi);

        tv_device.setText("仪器编号：" + SpUtils.getString(mactivity, "device", ""));
        initToolbar();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        int wifiState = wifiManager.getWifiState();
        if (wifiState == 1 || wifiState == 0) {
            sw_wifi.setChecked(false);
            tv_wifi.setTextColor(Color.GRAY);
            tv_wifi.setText("当前网络关闭");

        } else {
            sw_wifi.setChecked(true);
            connectionInfo = wifiManager.getConnectionInfo();
            String ssid = connectionInfo.getSSID();
            tv_wifi.setTextColor(Color.BLUE);
            tv_wifi.setText(ssid);
        }
    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        tl_bar.setTitleTextColor(getResources().getColor(R.color.white));
        tl_bar.setTitle("设置");

    }

    @Override
    public void initDate() {
        super.initDate();
        packageManager = mactivity.getPackageManager();
        wifiManager = (WifiManager) mactivity.getSystemService(Context.WIFI_SERVICE);
        try {
            packInfo = packageManager.getPackageInfo(
                    mactivity.getPackageName(), 0);
            tv_version.setText("(v" + packInfo.versionName + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        ll_zidian.setOnClickListener(this);
        ll_device.setOnClickListener(this);
        ll_map.setOnClickListener(this);
        ll_about.setOnClickListener(this);
        ll_sao.setOnClickListener(this);
        ll_close.setOnClickListener(this);
        ll_update.setOnClickListener(this);
        ll_net.setOnClickListener(this);
        sw_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wifiManager.setWifiEnabled(true);

                    connectionInfo = wifiManager.getConnectionInfo();

                    ssid = connectionInfo.getSSID();
                    tv_wifi.setText(ssid);
                  /*  if (ssid != null) {
                        tv_wifi.setTextColor(Color.BLUE);
                        tv_wifi.setText(ssid);
                    } else {
                        tv_wifi.setTextColor(Color.GRAY);
                        tv_wifi.setText("当前没有可用网络");
                    }*/
                } else {
                    wifiManager.setWifiEnabled(false);
                    tv_wifi.setTextColor(Color.GRAY);
                    tv_wifi.setText("当前网络关闭");
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            // 获取返回结果
            final String result = data.getStringExtra("result");

            if (result.startsWith("http://")) {
                try {
                    Intent intent = new Intent();
                    intent.setData(Uri.parse(result));//Url 就是你要打开的网址
                    intent.setAction(Intent.ACTION_VIEW);
                    this.startActivity(intent); //启动浏览器
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String type = jsonObject.get("type").toString();
                    switch (type) {
                        case "curve":
                            final AlertDialog.Builder build2 = new AlertDialog.Builder(mactivity);
                            build2.setTitle("扫描结果").setMessage("扫描结果为一条曲线，是否保存？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                /**
                                 * @param dialog
                                 * @param which
                                 */
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    Gson gson = new Gson();
                                    Acurve acurve = gson.fromJson(result, Acurve.class);

                                    SaveCurveUtils sc = new SaveCurveUtils(acurve);
                                    sc.saveCardtype();
                                    sc.saveCardbatch();
                                    sc.saveCurve();
                                    Snackbar.make(tl_bar, "此曲线添加成功", Snackbar.LENGTH_SHORT).show();

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mactivity, "扫描的结果：" + result, Toast.LENGTH_SHORT).show();
                }
            }
            // 将结果设置给TextView
//            Toast.makeText(mactivity, "扫描的结果：" + result, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_zidian:
                HomeActivity activity = (HomeActivity) mactivity;
                CenterFragment cf = activity.getCenterMenuFragment();
                cf.openDrawerLayout();
                break;
            case R.id.ll_device:
                mactivity.startActivity(new Intent(mactivity, SettingsActivity.class));
                break;
            case R.id.ll_map:
                mactivity.startActivity(new Intent(mactivity, MapActivity.class));
                break;
            case R.id.ll_about:
                mactivity.startActivity(new Intent(mactivity, AboutActivity.class));
                break;
            case R.id.ll_update:
                checkVersion();
                break;
            case R.id.ll_sao:
                Intent intent = new Intent(mactivity, CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.ll_net:
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
//                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                break;
            case R.id.ll_close:
                new SuperDialog.Builder((FragmentActivity) mactivity).setRadius(16)
                        .setMessage("确定退出登录?")
                        .setPositiveButton("确定", new SuperDialog.OnClickPositiveListener() {
                            @Override
                            public void onClick(View v) {
                                SpUtils.putBoolean(mactivity, "isok", false);
                                mactivity.finish();
                            }
                        }).setNegativeButton("取消", null).build();

                break;
        }
    }

    /**
     * 检查版本
     */
    private void checkVersion() {

        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Constants.UPDATE_URL);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("GET");

                    clientVersionCode = packInfo.versionCode;
                    conn.setConnectTimeout(2000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();// json文本
                        String json = StreamUtils.readStream(is);
                        if (TextUtils.isEmpty(json)) {
                            mactivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(mactivity, "已是最新版本", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            JSONObject jsonObj = new JSONObject(json);
                            int serverVersionCode = jsonObj.getInt("version");
                            if (clientVersionCode == serverVersionCode) {
                                // 相同，进入应用程序主界面
                                mactivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mactivity, "已经是最新版本", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Intent intent = new Intent(mactivity, SplashActivity.class);
                                intent.putExtra("update", true);
                                mactivity.startActivity(intent);
                            }
                        }
                    } else {
                        mactivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(mactivity, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } catch (final Exception e) {
                    e.printStackTrace();

                    mactivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mactivity, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        }.start();

    }
}

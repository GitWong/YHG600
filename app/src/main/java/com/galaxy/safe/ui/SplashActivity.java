package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.R;
import com.galaxy.safe.Service.TaskService;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.SpUtils;
import com.galaxy.safe.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Dell on 2016/6/24.
 */
public class SplashActivity extends Activity {

    protected static final int SHOW_UPDATE_DIALOG = 1;
    private static final int LOAD_MAINUI = 2;
    private TextView tv_splash_version;
    private TextView tv_info;
    private PackageManager packageManager;
    private int clientVersionCode;

    // 服务器新资源描述信息
    private String desc;
    // 服务器资源的下载路径
    private String downloadurl;
    private Boolean update;
    private ProgressDialog pd;
    AlertDialog.Builder ad;


    // 消息处理器
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LOAD_MAINUI:
                    loadMainUi();
                    break;
                case SHOW_UPDATE_DIALOG:
                    // 因为对话框是activity的一部分显示对话框 必须指定activity的环境（令牌）
                    AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                    builder.setTitle("更新提醒");
                    builder.setMessage(desc);
                    // builder.setCancelable(false);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            loadMainUi();
                        }
                    });
                    builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            loadMainUi();
                        }
                    });
                    builder.setPositiveButton("立刻更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            download(downloadurl);
                        }

                    });
                    builder.show();
                    break;

            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initData();
      /*  if (SpUtils.getboolean(this, "nofix", true)) {
            fixdevice();
            return;
        }*/

        if (SpUtils.getboolean(this, "nofast", true)) {
            createShortCut();
        }
        update = getIntent().getBooleanExtra("update", false);
        if (update) {
            checkVersion();
            return;
        }

        boolean isTask = SpUtils.getboolean(this, "task", false);
        if (isTask) {
            this.startService(new Intent(this, TaskService.class));
        } else {
            this.stopService(new Intent(this, TaskService.class));
        }

        boolean autoupdate = SpUtils.getboolean(this, "update", true);
        if (autoupdate) {
            checkVersion();
        } else {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loadMainUi();
                }
            }.start();
        }
    }

    private void fixdevice() {


        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("设备未经校准，是否进入校准流程，取消将无法保准结果的准确性");
        builder.setCancelable(false);

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadMainUi();
            }
        });
        builder.setPositiveButton("校准", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                fix();
            }
        });
        builder.show();

    }

    /**
     * 校准方法
     */
    private void fix() {

        ad = new AlertDialog.Builder(SplashActivity.this);
        ad.setTitle("请插入校准卡");
        ad.setPositiveButton("开始校准", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(SplashActivity.this, "校准中。。。", Toast.LENGTH_SHORT).show();
                SpUtils.putBoolean(SplashActivity.this, "nofix", false);
                loadMainUi();
            }
        });
        ad.show();

    }


    /**
     * 创建应用快捷方式
     */
    private void createShortCut() {


        Intent shortCutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //添加快捷方式的名字
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        //不允许重复添加
        shortCutIntent.putExtra("duplicate", false);

        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent()
                .setAction(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setClass(SplashActivity.this, SplashActivity.class));
        //添加快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.logo);
        shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        SplashActivity.this.sendBroadcast(shortCutIntent);
        SpUtils.putBoolean(this, "nofast", false);

       /* Intent shortcutIntent = new Intent(this, SplashActivity.class); //启动首页（launcher Activity)
        Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name).trim());//快捷键名字可以任意，不过最好为app名称
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(this, R.drawable.logo);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
        shortcutIntent.putExtra("duplicate", false);//不允许重复创建
        intent.putExtra("duplicate", false);//不允许重复创建
        sendBroadcast(intent);//发送广播创建快捷键
        SpUtils.putBoolean(this, "nofast", false);*/

    }

    /**
     * 多线程的下载器
     *
     * @param downloadurl
     */
    private void download(String downloadurl) {


        pd = new ProgressDialog(SplashActivity.this);
        pd.setTitle("下载");
        pd.setMessage("正在下载,请稍后...");
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);

        // 多线程断点下载。
        HttpUtils http = new HttpUtils();
        http.download(downloadurl, "/mnt/sdcard/temp.apk",
                new RequestCallBack<File>() {
                    @Override
                    public void onSuccess(ResponseInfo<File> arg0) {
                        pd.dismiss();
                        Toast.makeText(SplashActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        intent.addCategory("android.intent.category.DEFAULT");

                        intent.setDataAndType(Uri.fromFile(new File(Environment
                                        .getExternalStorageDirectory(), "temp.apk")),
                                "application/vnd.android.package-archive");
                        startActivityForResult(intent, 0);
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(SplashActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                        loadMainUi();
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        Message msg = Message.obtain();

//                        DecimalFormat fnum = new DecimalFormat("##0.00");
                        final float t = (float) total / 1048576;
                        final float c = (float) current / 1048576;
                   /*     String t1 = fnum.format(t);
                        String c1 = fnum.format(c);*/
                        Bundle b = new Bundle();

                        b.putString("c", String.valueOf(total / 1048576));
                        b.putString("t", String.valueOf(current / 1048576));
                        msg.setData(b);

                        pd.setProgress(Integer.valueOf(String.valueOf(current / 1048576)));
                        pd.setMax(Integer.valueOf(String.valueOf(total / 1048576)));


                     /*   tv_info.setTextColor(SplashActivity.this.getResources().getColor(R.color.red));
                        tv_info.setText("正在下载中：" + c1 + "M/" + t1 + "M");*/


                        super.onLoading(total, current, isUploading);
                    }
                });
        pd.show();
    }

    /**
     * 检查版本
     */
    private void checkVersion() {

        new Thread() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    URL url = new URL(Constants.UPDATE_URL);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setRequestMethod("GET");

                    conn.setConnectTimeout(2000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream is = conn.getInputStream();// json文本
                        String json = StreamUtils.readStream(is);
                        if (TextUtils.isEmpty(json)) {
                            msg.what = LOAD_MAINUI;
                        } else {
                            JSONObject jsonObj = new JSONObject(json);
                            downloadurl = jsonObj.getString("downloadurl");
                            int serverVersionCode = jsonObj.getInt("version");
                            desc = jsonObj.getString("desc");

                            if (clientVersionCode == serverVersionCode) {
                                // 相同，进入应用程序主界面
                                msg.what = LOAD_MAINUI;
                            } else {
                                // 不同，弹出更新提醒对话框
                                msg.what = SHOW_UPDATE_DIALOG;
                            }
                        }
                    } else {
                        msg.what = LOAD_MAINUI;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    msg.what = LOAD_MAINUI;
                } finally {
                    long endtime = System.currentTimeMillis();
                    long dtime = endtime - startTime;
                    if (dtime > 1000) {
                        handler.sendMessage(msg);
                    } else {
                        try {
                            Thread.sleep(1000 - dtime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendMessage(msg);
                    }
                }
            }
        }.start();

    }

    /**
     * 进入
     */
    private void loadMainUi() {
        this.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            String version = packInfo.versionName;
            clientVersionCode = packInfo.versionCode;
//            tv_splash_version.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化控件
     */
    private void initView() {
        tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
        tv_info = (TextView) findViewById(R.id.tv_info);
        packageManager = getPackageManager();
        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        ScaleAnimation sa = new ScaleAnimation(1.06f, 1f, 1.06f, 1f);
        AnimationSet as = new AnimationSet(false);
        as.addAnimation(aa);
        as.addAnimation(sa);
        as.setDuration(1600);
        findViewById(R.id.rl_splash_root).startAnimation(as);

    }
}

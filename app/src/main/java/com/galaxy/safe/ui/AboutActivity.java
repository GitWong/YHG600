package com.galaxy.safe.ui;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.galaxy.safe.R;

/**
 * Created by Dell on 2016/4/12.
 */
public class AboutActivity extends Activity {


    private Toolbar tl_bar;
    private PackageManager packageManager;
    private TextView tv_version;
    CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        tv_version = (TextView) findViewById(R.id.tv_version);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("关于我们");
        initToolBar();
        packageManager = getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(
                    getPackageName(), 0);
            String version = packInfo.versionName;

            tv_version.setText("软件版本：" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {

        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("关于我们");
//        tl_bar.setSubtitle("卡类型详情");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

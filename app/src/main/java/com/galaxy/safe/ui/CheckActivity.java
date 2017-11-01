package com.galaxy.safe.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.safe.R;
import com.galaxy.safe.fragment.FirstFragment;
import com.galaxy.safe.fragment.SettingFragment;
import com.galaxy.safe.fragment.UnTaskFragment;

/**
 * Created by Dell on 2016/4/12.
 */
public class CheckActivity extends FragmentActivity implements View.OnClickListener {

    //    private Toolbar tl_bar;
    private TextView tv_locked;
    private TextView tv_unlock;
    private FragmentManager fm;
    private FirstFragment firstFragment;
    private UnTaskFragment unTaskFragment;
    private ImageView iv_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check);
//        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
//        initToolBar();

        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        tv_locked = (TextView) findViewById(R.id.tv_locked);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_locked.setOnClickListener(this);
        tv_unlock.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        //初始化帧管理器。
        fm = getSupportFragmentManager();
        firstFragment = new FirstFragment();
        unTaskFragment = new UnTaskFragment();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_container, firstFragment);
        ft.commit();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
/* */

    /**
     * 初始化toolbar
     *//*
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("检测");
//        tl_bar.setSubtitle("卡类型详情");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/
    @Override
    public void onClick(View v) {
        FragmentTransaction ft = fm.beginTransaction();
        switch (v.getId()) {
            case R.id.tv_locked:
                tv_locked.setBackgroundResource(R.drawable.tab_right_pressed);
                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                ft.replace(R.id.fl_container, unTaskFragment);
                break;
            case R.id.tv_unlock:
                tv_locked.setBackgroundResource(R.drawable.tab_right_default);
                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                ft.replace(R.id.fl_container, firstFragment);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
        ft.commit();//提交事务。
    }
}

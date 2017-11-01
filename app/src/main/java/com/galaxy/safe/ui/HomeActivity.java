package com.galaxy.safe.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Window;

import com.galaxy.safe.R;
import com.galaxy.safe.fragment.CenterFragment;
import com.galaxy.safe.fragment.LeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

/**
 * Created by Dell on 2016/3/17.
 */
public class HomeActivity extends SlidingFragmentActivity {

    private LeftFragment leftFragment;
    private CenterFragment centerFragment;
    private final String LEFT_MENU_FRAGMENT_TAG = "left_menu";
    // 主界面fragment的tag
    private final String MAIN_CONTENT_FRAGMENT_TAG = "center_content";


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
//                    System.loadLibrary("image_proc");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home);
        setBehindContentView(R.layout.left_frame);
        SlidingMenu sm = getSlidingMenu();
        sm.setBehindOffset(100);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        init();
    }

    /**
     * 初始化opencv
     */
    @Override
    protected void onResume() {
        super.onResume();

        try {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_8, this, mLoaderCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {

        FragmentTransaction t = this.getSupportFragmentManager()
                .beginTransaction();
        leftFragment = new LeftFragment();
        t.replace(R.id.left_frame, leftFragment, LEFT_MENU_FRAGMENT_TAG);

        centerFragment = new CenterFragment();
        t.replace(R.id.fl_main_menu, centerFragment, MAIN_CONTENT_FRAGMENT_TAG);
        t.commit();
    }

    public LeftFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftFragment leftMenuFragment = (LeftFragment) fm.findFragmentByTag(LEFT_MENU_FRAGMENT_TAG);
        return leftMenuFragment;
    }

    public CenterFragment getCenterMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        CenterFragment centerFragment = (CenterFragment) fm.findFragmentByTag(MAIN_CONTENT_FRAGMENT_TAG);
        return centerFragment;
    }

}

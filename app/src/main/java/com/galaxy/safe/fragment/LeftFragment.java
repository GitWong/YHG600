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

import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.galaxy.safe.R;
import com.galaxy.safe.base.BaseFragment;
import com.galaxy.safe.ui.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class LeftFragment extends BaseFragment implements View.OnTouchListener, View.OnClickListener {

    private int downX;
    private SlidingMenu sm;
    private ImageView bt_togg;

    private Button bt_bai;
    private Button bt_jia;
    private Button bt_non;
    private VideoView tv;
    private ScrollView sv_video;
    private ImageView iv_play;
    private ImageView iv_stop;
    private TextView tv_title;
    private static final String url = Environment.getExternalStorageDirectory() + "/" + "video" + "/" + "a.mp4";
    private static final String url2 = Environment.getExternalStorageDirectory() + "/" + "video" + "/" + "b.mp4";
    private static final String url3 = Environment.getExternalStorageDirectory() + "/" + "video" + "/" + "c.mp4";


    @Override
    public View initview(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.left, null);
        HomeActivity activity = (HomeActivity) mactivity;
        sm = activity.getSlidingMenu();
        sm.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                if(tv.isPlaying()){
                tv.pause();
            }}
        });

        bt_togg = (ImageView) view.findViewById(R.id.bt_togg);
        tv = (VideoView) view.findViewById(R.id.video_view1);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv.setMediaController(new MediaController(mactivity));
        sv_video = (ScrollView) view.findViewById(R.id.sl_video);
        sv_video.fullScroll(ScrollView.FOCUS_UP);
        bt_bai = (Button) view.findViewById(R.id.bt_bai);
        bt_jia = (Button) view.findViewById(R.id.bt_jia);
        bt_non = (Button) view.findViewById(R.id.bt_non);
        iv_play = (ImageView) view.findViewById(R.id.iv_play);
        iv_stop = (ImageView) view.findViewById(R.id.iv_stop);
        iv_stop.setOnClickListener(this);
        iv_play.setOnClickListener(this);
        bt_bai.setOnClickListener(this);
        bt_jia.setOnClickListener(this);
        bt_non.setOnClickListener(this);

        bt_togg.setOnClickListener(this);
        view.setOnTouchListener(this);
        return view;
    }

    @Override
    public void initDate() {


        super.initDate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                if (downX - moveX > 100) {
                    sm.toggle();
                }
                break;
        }
       return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.bt_togg:
                if (sm != null) sm.toggle();

                break;
            case R.id.bt_bai:
                iv_play.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                iv_stop.setVisibility(View.VISIBLE);
                tv_title.setText("210吊白块讲解：");
                tv.setVideoURI(Uri.parse(url));
                tv.start();
                break;
            case R.id.bt_jia:
                iv_play.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                iv_stop.setVisibility(View.VISIBLE);
                tv_title.setText("210甲醛讲解：");
                tv.setVideoURI(Uri.parse(url2));
                tv.start();
                break;
            case R.id.bt_non:
                iv_play.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                iv_stop.setVisibility(View.VISIBLE);
                tv_title.setText("210农药残留讲解：");
                tv.setVideoURI(Uri.parse(url3));
                tv.start();
                break;
            case R.id.iv_play:
                iv_play.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                iv_stop.setVisibility(View.VISIBLE);
                tv_title.setText("210吊白块讲解：");
                tv.setVideoURI(Uri.parse(url));
                tv.start();
                break;
            case R.id.iv_stop:
                iv_play.setVisibility(View.VISIBLE);
                tv.setVisibility(View.GONE);
                iv_stop.setVisibility(View.GONE);
                tv_title.setText("视频讲解：");
                tv.stopPlayback();
                break;
        }

    }
}

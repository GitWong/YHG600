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

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.galaxy.safe.Bean.News;
import com.galaxy.safe.R;
import com.galaxy.safe.base.BaseFragment;
import com.galaxy.safe.ui.HomeActivity;
import com.galaxy.safe.ui.NewsActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


public class HelpFragment extends BaseFragment implements View.OnClickListener {


    private ImageView iv_cam;
    private Toolbar tl_bar;
    private SlidingMenu sm;
    private LinearLayout ll_past;
    private LinearLayout ll_news;


    /**
     * 重写父类方法 初始化控件
     *
     * @param inflater
     * @return
     */
    @Override
    public View initview(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.help, null);
        ll_past = (LinearLayout) view.findViewById(R.id.ll_past);
        ll_news = (LinearLayout) view.findViewById(R.id.ll_news);

        ll_news.setOnClickListener(this);
        ll_past.setOnClickListener(this);
        tl_bar = (Toolbar) view.findViewById(R.id.tl_bar);
        HomeActivity activity = (HomeActivity) mactivity;
        sm = activity.getSlidingMenu();

        initToolbar();
        return view;
    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        tl_bar.setTitleTextColor(getResources().getColor(R.color.white));
        tl_bar.setNavigationIcon(R.drawable.study);
        tl_bar.setTitle("帮助信息");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sm != null) {
                    sm.toggle();
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_past:
                Snackbar.make(tl_bar, "敬请期待", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.ll_news:
                mactivity.startActivity(new Intent(mactivity, NewsActivity.class));
                break;
        }

    }
}

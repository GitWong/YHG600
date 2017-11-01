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

import android.content.Intent;
import android.graphics.Paint;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.galaxy.safe.R;
import com.galaxy.safe.base.BaseFragment;
import com.galaxy.safe.ui.ChartsActivity;
import com.galaxy.safe.ui.CheckActivity;
import com.galaxy.safe.ui.EntrustListActivity;
import com.galaxy.safe.ui.SampleListActivity;
import com.galaxy.safe.ui.StationActivity;
import com.galaxy.safe.ui.TaskActivity;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.SpUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;


public class HomeFragment extends BaseFragment implements View.OnClickListener {


    private LinearLayout mv_box1;
    private LinearLayout mv_box2;
    private LinearLayout mv_box3;
    private LinearLayout mv_box4;
    private ImageView iv_taks;
    private TextView tv_station;
    ScaleAnimation sa;
    private HttpUtils httpUtils;

    /**
     * 重写父类方法 初始化控件
     *
     * @param inflater
     * @return
     */
    @Override
    public View initview(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.homefragment, null);
        mv_box1 = (LinearLayout) view.findViewById(R.id.mv_box1);
        mv_box2 = (LinearLayout) view.findViewById(R.id.mv_box2);
        mv_box3 = (LinearLayout) view.findViewById(R.id.mv_box3);
        mv_box4 = (LinearLayout) view.findViewById(R.id.mv_box4);
        iv_taks = (ImageView) view.findViewById(R.id.ntasks);
        tv_station = (TextView) view.findViewById(R.id.tv_station);

        ininData();
        return view;
    }

    @Override
    public void onDestroy() {

        httpUtils = null;
        super.onDestroy();
    }

    @Override
    public void onStart() {
        tv_station.setText("●" + SpUtils.getString(mactivity, "sname", "请点击此处设置站点信息"));

        //添加下划线
        tv_station.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_station.getPaint().setAntiAlias(true);//抗锯齿

        httpUtils = new HttpUtils();
        RequestParams rp = new RequestParams();
        String s = SpUtils.getString(mactivity, "sbh", null);
        if (s != null) {
            rp.addQueryStringParameter("station_bh", s);
            rp.addQueryStringParameter("last", SpUtils.getString(mactivity, "last", "2000-1-10 15:48:43"));
            httpUtils.send(HttpRequest.HttpMethod.GET, Constants.TASK_URL, rp, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    if (!responseInfo.result.contains("获取数据失败")) {
                        iv_taks.setImageResource(R.drawable.taskn);
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                }
            });
        } else {
            Snackbar.make(tv_station, "补充站点信息，获取最新任务！！！", Snackbar.LENGTH_SHORT).show();
        }

        super.onStart();
    }

    private void ininData() {
        sa = new ScaleAnimation(0.92f, 1.0f, 0.92f,
                1.0f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0.66f);
        sa.setDuration(100);

        mv_box1.setOnClickListener(this);
        mv_box2.setOnClickListener(this);
        mv_box3.setOnClickListener(this);
        mv_box4.setOnClickListener(this);
        iv_taks.setOnClickListener(this);
        tv_station.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.mv_box1:
                mv_box1.startAnimation(sa);
                mactivity.startActivity(new Intent(mactivity, CheckActivity.class));
                break;
            case R.id.mv_box2:
                mv_box2.startAnimation(sa);
                mactivity.startActivity(new Intent(mactivity, SampleListActivity.class));
                break;
            case R.id.mv_box3:
                mv_box3.startAnimation(sa);
                mactivity.startActivity(new Intent(mactivity, EntrustListActivity.class));
                break;
            case R.id.mv_box4:
                mv_box4.startAnimation(sa);
                mactivity.startActivity(new Intent(mactivity, ChartsActivity.class));
                break;
            case R.id.ntasks:
                mactivity.startActivity(new Intent(mactivity, TaskActivity.class));
                break;
            case R.id.tv_station:
                mactivity.startActivity(new Intent(mactivity, StationActivity.class));
                break;

        }
    }
}

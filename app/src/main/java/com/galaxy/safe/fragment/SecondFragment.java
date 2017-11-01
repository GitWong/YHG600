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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.galaxy.safe.Bean.Task;
import com.galaxy.safe.R;
import com.galaxy.safe.base.BaseFragment;
import com.galaxy.safe.ui.ApplylistActivity;
import com.galaxy.safe.ui.EntrustListActivity;
import com.galaxy.safe.ui.FxjcListActivity;
import com.galaxy.safe.ui.JcListActivity;
import com.galaxy.safe.ui.LbListActivity;
import com.galaxy.safe.ui.MainActivity;
import com.galaxy.safe.ui.PrintListActivity;
import com.galaxy.safe.ui.QtListActivity;
import com.galaxy.safe.ui.RecordListActivity;
import com.galaxy.safe.ui.SearchActivity;
import com.galaxy.safe.ui.TaskActivity;

import com.galaxy.safe.ui.UnPrintListActivity;
import com.galaxy.safe.ui.UnUpLoadListActivity;
import com.galaxy.safe.ui.UpLoadListActivity;
import com.galaxy.safe.ui.VsListActivity;
import com.galaxy.safe.ui.WtListActivity;
import com.galaxy.safe.ui.ZcListActivity;
import com.galaxy.safe.ui.ZjListActivity;


public class SecondFragment extends BaseFragment implements View.OnClickListener {
    private Button bt_weituo;
    private TextView bt_applylist;
    private LinearLayout ll_record;
    private TextView tv_number;//记录数目
    private Toolbar tl_bar;
//    private Toolbar tl;

    private LinearLayout ll_task;//任务
    private TextView tv_task;
    private LinearLayout ll_weituo;//委托
    private TextView tv_weituo;
    private LinearLayout ll_zhongcai;//仲裁
    private TextView tv_zhongcai;
    private LinearLayout ll_jiancha;//监查
    private TextView tv_jiancha;
    private LinearLayout ll_jiance;//监测
    private TextView tv_jiance;
    private LinearLayout ll_vs;//对比
    private TextView tv_vs;
    private LinearLayout ll_lianbing;//练兵
    private TextView tv_lianbing;
    private LinearLayout ll_zijian;//自检
    private TextView tv_zijian;
    private LinearLayout ll_other;//其他
    private TextView tv_other;
    private LinearLayout ll_upload;//已上传
    private TextView tv_upload;
    private LinearLayout ll_unupload;//未上传
    private TextView tv_unupload;
    private LinearLayout ll_print;//已打印
    private TextView tv_print;
    private LinearLayout ll_unprint;//未打印
    private TextView tv_unprint;
    private LinearLayout ll_search;


    private SQLiteDatabase sdb;

    private Cursor cursor;
    ScaleAnimation sa;

    @Override
    public View initview(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.second, null);
        bt_weituo = (Button) view.findViewById(R.id.bt_weituo);
        bt_applylist = (TextView) view.findViewById(R.id.bt_applylist);
//        ll_task = (LinearLayout) view.findViewById(R.id.ll_task);
        ll_record = (LinearLayout) view.findViewById(R.id.ll_record);
        ll_search = (LinearLayout) view.findViewById(R.id.lv_search);
        ll_weituo = (LinearLayout) view.findViewById(R.id.ll_weituo);
        ll_zhongcai = (LinearLayout) view.findViewById(R.id.ll_zhongcai);
        ll_jiancha = (LinearLayout) view.findViewById(R.id.ll_jiancha);
        ll_jiance = (LinearLayout) view.findViewById(R.id.ll_jiance);
        ll_vs = (LinearLayout) view.findViewById(R.id.ll_vs);
        ll_lianbing = (LinearLayout) view.findViewById(R.id.ll_lianbing);
        ll_zijian = (LinearLayout) view.findViewById(R.id.ll_zijian);
        ll_other = (LinearLayout) view.findViewById(R.id.ll_other);
        ll_upload = (LinearLayout) view.findViewById(R.id.ll_upload);
        ll_unupload = (LinearLayout) view.findViewById(R.id.ll_unupload);
        ll_print = (LinearLayout) view.findViewById(R.id.ll_print);
        ll_unprint = (LinearLayout) view.findViewById(R.id.ll_unprint);
        tv_number = (TextView) view.findViewById(R.id.tv_number);
        tl_bar = (Toolbar) view.findViewById(R.id.tl_bar);
//        tv_entrust = (TextView) view.findViewById(R.id.tv_entrustnum);
//        tv_applynum = (TextView) view.findViewById(R.id.tv_applynum);
        tv_weituo = (TextView) view.findViewById(R.id.tv_weituo);
        tv_zhongcai = (TextView) view.findViewById(R.id.tv_zhongcai);
        tv_jiancha = (TextView) view.findViewById(R.id.tv_jiancha);
        tv_jiance = (TextView) view.findViewById(R.id.tv_jiance);
        tv_vs = (TextView) view.findViewById(R.id.tv_vs);
        tv_lianbing = (TextView) view.findViewById(R.id.tv_lianbing);
        tv_zijian = (TextView) view.findViewById(R.id.tv_zijian);
        tv_other = (TextView) view.findViewById(R.id.tv_other);
        tv_upload = (TextView) view.findViewById(R.id.tv_upload);
        tv_unupload = (TextView) view.findViewById(R.id.tv_unupload);
        tv_print = (TextView) view.findViewById(R.id.tv_print);
        tv_unprint = (TextView) view.findViewById(R.id.tv_unprint);

        bt_weituo.setOnClickListener(this);
//        ll_task.setOnClickListener(this);
        bt_applylist.setOnClickListener(this);
        ll_record.setOnClickListener(this);
        ll_search.setOnClickListener(this);
        ll_weituo.setOnClickListener(this);
        ll_zhongcai.setOnClickListener(this);
        ll_jiancha.setOnClickListener(this);
        ll_jiance.setOnClickListener(this);
        ll_vs.setOnClickListener(this);
        ll_lianbing.setOnClickListener(this);
        ll_zijian.setOnClickListener(this);
        ll_other.setOnClickListener(this);
        ll_upload.setOnClickListener(this);
        ll_unupload.setOnClickListener(this);
        ll_print.setOnClickListener(this);
        ll_unprint.setOnClickListener(this);
//        tl = (Toolbar) view.findViewById(R.id.tl_bar);
        initToolbar();
        return view;
    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        tl_bar.setTitleTextColor(getResources().getColor(R.color.white));
        tl_bar.setTitle("记录");

    }

    @Override
    public void initDate() {
        super.initDate();

        sa = new ScaleAnimation(0.95f, 1.0f, 0.95f,
                1.0f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(200);

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            cursor = sdb.query("galaxy_detection_record", new String[]{"detection_bh"}, null, null, null, null, null);
            if (cursor.isAfterLast()) {
                tv_number.setText("");
            }
            while (cursor.moveToNext()) {
                tv_number.setText("全部" + cursor.getCount() + "条  ▶▶");
            }
            cursor.close();
            cursor = sdb.query("galaxy_detection_record", new String[]{"upload_valid"}, "upload_valid=?", new String[]{"1"}, null, null, null);
            if (cursor.isAfterLast()) {
                tv_upload.setText("已上传");
            }
            while (cursor.moveToNext()) {
                tv_upload.setText("已上传" + "(" + cursor.getCount() + ")");
            }
            cursor.close();

            cursor = sdb.query("galaxy_detection_record", new String[]{"upload_valid"}, "upload_valid=?", new String[]{"0"}, null, null, null);
            if (cursor.isAfterLast()) {
                tv_unupload.setText("未上传");
            }
            while (cursor.moveToNext()) {

                tv_unupload.setText("未上传" + "(" + cursor.getCount() + ")");
            }
            cursor.close();

            cursor = sdb.query("galaxy_detection_record", new String[]{"is_printed"}, "is_printed=?", new String[]{"1"}, null, null, null);
            if (cursor.isAfterLast()) {
                tv_print.setText("已打印");
            }
            while (cursor.moveToNext()) {

                tv_print.setText("已打印" + "(" + cursor.getCount() + ")");
            }
            cursor.close();
            cursor = sdb.query("galaxy_detection_record", new String[]{"is_printed"}, "is_printed=?", new String[]{"0"}, null, null, null);
            if (cursor.isAfterLast()) {
                tv_unprint.setText("未打印");
            }
            while (cursor.moveToNext()) {

                tv_unprint.setText("未打印" + "(" + cursor.getCount() + ")");
            }
            cursor.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  Cursor cursor1 = sdb.query("galaxy_task_delegation", new String[]{"weituo_number", "use_valid"}, "use_valid=?", new String[]{"0"}, null, null, null);
        if (cursor1.isAfterLast()) {
            tv_entrust.setVisibility(View.GONE);
        }
        while (cursor1.moveToNext()) {
            tv_entrust.setVisibility(View.VISIBLE);
            tv_entrust.setText(cursor1.getCount() + "");
        }
        cursor1.close();*/
//        cursor = sdb.query("galaxy_application_form", new String[]{"testing_no"}, null, null, null, null, null);
       /* if (cursor2.isAfterLast()) {
            tv_applynum.setVisibility(View.GONE);
        }*/
   /*     while (cursor.moveToNext()) {
//            tv_applynum.setVisibility(View.VISIBLE);
            SpannableStringBuilder builder = new SpannableStringBuilder("检测单记录           全部" + cursor.getCount() + "条  »»");
            ForegroundColorSpan greenSpan = new ForegroundColorSpan(Color.DKGRAY);
            builder.setSpan(greenSpan, 6, builder.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            bt_applylist.setText(builder);
//            bt_applylist.setText("检测单记录 〓〔" + builder + " 〕");
//            tv_applynum.setText(cursor2.getCount() + "");
        }
        cursor.close();

        cursor = sdb.query("galaxy_application_form", new String[]{"tasks_type"}, "tasks_type=?", new String[]{"4"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_weituo.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            tv_weituo.setVisibility(View.VISIBLE);
            tv_weituo.setText("/" + cursor.getCount());
        }
        cursor.close();

        cursor = sdb.query("galaxy_application_form", new String[]{"tasks_type"}, "tasks_type=?", new String[]{"5"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_zhongcai.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            tv_zhongcai.setVisibility(View.VISIBLE);
            tv_zhongcai.setText("/" + cursor.getCount());
        }
        cursor.close();

        cursor = sdb.query("galaxy_application_form", new String[]{"tasks_type"}, "tasks_type=?", new String[]{"1"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_jiancha.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            tv_jiancha.setVisibility(View.VISIBLE);
            tv_jiancha.setText("/" + cursor.getCount());
        }
        cursor.close();

        cursor = sdb.query("galaxy_application_form", new String[]{"tasks_type"}, "tasks_type=?", new String[]{"2"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_jiance.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            tv_jiance.setVisibility(View.VISIBLE);
            tv_jiance.setText("/" + cursor.getCount());
        }
        cursor.close();

        cursor = sdb.query("galaxy_application_form", new String[]{"tasks_type"}, "tasks_type=?", new String[]{"6"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_vs.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            tv_vs.setVisibility(View.VISIBLE);
            tv_vs.setText("/" + cursor.getCount());
        }
        cursor.close();

        cursor = sdb.query("galaxy_application_form", new String[]{"tasks_type"}, "tasks_type=?", new String[]{"7"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_lianbing.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            tv_lianbing.setVisibility(View.VISIBLE);
            tv_lianbing.setText("/" + cursor.getCount());
        }
        cursor.close();

        cursor = sdb.query("galaxy_application_form", new String[]{"tasks_type"}, "tasks_type=?", new String[]{"3"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_zijian.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            tv_zijian.setVisibility(View.VISIBLE);
            tv_zijian.setText("/" + cursor.getCount());
        }
        cursor.close();

        cursor = sdb.query("galaxy_application_form", new String[]{"tasks_type"}, "tasks_type=?", new String[]{"8"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_other.setVisibility(View.GONE);
        }
        while (cursor.moveToNext()) {
            tv_other.setVisibility(View.VISIBLE);
            tv_other.setText("/" + cursor.getCount());
        }
        cursor.close();*/

        //此处要恢复

       cursor = sdb.query("galaxy_detection_record", new String[]{"detection_bh"}, null, null, null, null, null);
        if (cursor.isAfterLast()) {
            tv_number.setText("");
        }
        while (cursor.moveToNext()) {
            tv_number.setText("全部" + cursor.getCount() + "条  ▶▶");
        }
        cursor.close();
        cursor = sdb.query("galaxy_detection_record", new String[]{"upload_valid"}, "upload_valid=?", new String[]{"1"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_upload.setText("已上传");
        }
        while (cursor.moveToNext()) {
            tv_upload.setText("已上传" + "(" + cursor.getCount() + ")");
        }
        cursor.close();

        cursor = sdb.query("galaxy_detection_record", new String[]{"upload_valid"}, "upload_valid=?", new String[]{"0"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_unupload.setText("未上传");
        }
        while (cursor.moveToNext()) {

            tv_unupload.setText("未上传" + "(" + cursor.getCount() + ")");
        }
        cursor.close();

        cursor = sdb.query("galaxy_detection_record", new String[]{"is_printed"}, "is_printed=?", new String[]{"1"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_print.setText("已打印");
        }
        while (cursor.moveToNext()) {

            tv_print.setText("已打印" + "(" + cursor.getCount() + ")");
        }
        cursor.close();
        cursor = sdb.query("galaxy_detection_record", new String[]{"is_printed"}, "is_printed=?", new String[]{"0"}, null, null, null);
        if (cursor.isAfterLast()) {
            tv_unprint.setText("未打印");
        }
        while (cursor.moveToNext()) {

            tv_unprint.setText("未打印" + "(" + cursor.getCount() + ")");
        }
        cursor.close();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_weituo:
                bt_weituo.startAnimation(sa);
                mactivity.startActivity(new Intent(mactivity, TaskActivity.class));
                break;
            case R.id.ll_record:
                mactivity.startActivity(new Intent(mactivity, RecordListActivity.class));
                break;
            case R.id.bt_applylist:
                bt_applylist.startAnimation(sa);
                mactivity.startActivity(new Intent(mactivity, ApplylistActivity.class));
                break;
            case R.id.ll_weituo:
                mactivity.startActivity(new Intent(mactivity, WtListActivity.class));
                break;
            case R.id.ll_zhongcai:
                mactivity.startActivity(new Intent(mactivity, ZcListActivity.class));
                break;
            case R.id.ll_jiancha:
                mactivity.startActivity(new Intent(mactivity, JcListActivity.class));
                break;
            case R.id.ll_jiance:
                mactivity.startActivity(new Intent(mactivity, FxjcListActivity.class));
                break;
            case R.id.ll_vs:
                mactivity.startActivity(new Intent(mactivity, VsListActivity.class));
                break;
            case R.id.ll_lianbing:
                mactivity.startActivity(new Intent(mactivity, LbListActivity.class));
                break;
            case R.id.ll_zijian:
                mactivity.startActivity(new Intent(mactivity, ZjListActivity.class));
                break;
            case R.id.ll_other:
                mactivity.startActivity(new Intent(mactivity, QtListActivity.class));
                break;
            case R.id.ll_upload:
                mactivity.startActivity(new Intent(mactivity, UpLoadListActivity.class));
                break;
            case R.id.ll_unupload:
                mactivity.startActivity(new Intent(mactivity, UnUpLoadListActivity.class));
                break;
            case R.id.ll_print:
                mactivity.startActivity(new Intent(mactivity, PrintListActivity.class));
                break;
            case R.id.ll_unprint:
                mactivity.startActivity(new Intent(mactivity, UnPrintListActivity.class));
                break;
            case R.id.lv_search:
                ll_search.startAnimation(sa);
                mactivity.startActivity(new Intent(mactivity, SearchActivity.class));
                break;

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sdb.close();
     /*   if (!cursor.isClosed()) {
            cursor.close();
        }*/
    }
}

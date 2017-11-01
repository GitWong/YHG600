package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Dell on 2016/4/1.
 */
public class ChartsActivity extends Activity implements View.OnClickListener {

    private Toolbar tl_bar;
    private LinearLayout ll_checkinfo;
    private LinearLayout ll_zhonghe;
    private LinearLayout ll_sos;
    private int ying;
    private int yang;
    private int yanged;
    private SQLiteDatabase sdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charts);
        initView();
        initToolBar();




        Cursor c = sdb.query("galaxy_detection_record", new String[]{"detection_conclusion"}, "detection_conclusion=?", new String[]{"阴性"}, null, null, "detection_bh desc");
        while (c.moveToNext()) {
            ying = c.getCount();
        }
        c.close();
        Cursor c1 = sdb.query("galaxy_detection_record", new String[]{"detection_conclusion"}, "detection_conclusion=?", new String[]{"疑似阳性"}, null, null, "detection_bh desc");
        while (c1.moveToNext()) {
            yanged = c1.getCount();
        }
        c1.close();
        Cursor c2 = sdb.query("galaxy_detection_record", new String[]{"detection_conclusion"}, "detection_conclusion=?", new String[]{"阳性"}, null, null, "detection_bh desc");
        while (c2.moveToNext()) {
            yang = c2.getCount();
        }
        c2.close();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();

    }
    /**
     * 初始化控件
     */
    private void initView() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        ll_checkinfo = (LinearLayout) findViewById(R.id.ll_checkinfo);
        ll_zhonghe = (LinearLayout) findViewById(R.id.ll_zhonghe);
        ll_sos = (LinearLayout) findViewById(R.id.ll_sos);
        ll_checkinfo.setOnClickListener(this);
        ll_zhonghe.setOnClickListener(this);
        ll_sos.setOnClickListener(this);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("报表管理");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private View view2;//对话框ui
    private AlertDialog dialog3;
    private PieChartView chart;//饼状图
    private PieChartData data;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_checkinfo:
                AlertDialog.Builder build2 = new AlertDialog.Builder(this);
                view2 = View.inflate(this, R.layout.edit_total, null);

                chart = (PieChartView) view2.findViewById(R.id.chart);
                chart.setOnValueTouchListener(new ValueTouchListener());
                generateData();
                view2.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
                build2.setView(view2);
                dialog3 = build2.show();
                break;

            case R.id.ll_zhonghe:

                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_sos:

                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    private void generateData() {

        List<SliceValue> values = new ArrayList<SliceValue>();

        values.add(new SliceValue((float) yang, ChartsActivity.this.getResources().getColor(R.color.red)));
        values.add(new SliceValue((float) yanged, ChartsActivity.this.getResources().getColor(R.color.colorAccent)));
        values.add(new SliceValue((float) ying, ChartsActivity.this.getResources().getColor(R.color.green)));


        data = new PieChartData(values);
        data.setHasLabels(true);

        data.setHasLabelsOutside(true);

        chart.setPieChartData(data);

    }

    /**
     * 触摸显示内容
     */
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            if (arcIndex == 0) {
                Toast.makeText(ChartsActivity.this, "阳性数量: " + (int) value.getValue(), Toast.LENGTH_SHORT).show();
            } else if (arcIndex == 1) {
                Toast.makeText(ChartsActivity.this, "疑似阳性数量: " + (int) value.getValue(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ChartsActivity.this, "阴性数量: " + (int) value.getValue(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}

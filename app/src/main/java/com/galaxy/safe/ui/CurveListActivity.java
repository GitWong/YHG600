package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.galaxy.safe.Bean.Curve;
import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.List;

public class CurveListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<Curve> cList;
    private ListView lv_curve;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private CurveAdapt curveAdapt;
    private int currentId;//当前ID
    private int preId = 0;     //上一次id
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curvelist);
        initView();
        initToolbar();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        lv_curve = (ListView) findViewById(R.id.lv_curve);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        iv_add.setOnClickListener(this);


    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("标准曲线表页面");
        tl_bar.setSubtitle("标准曲线表");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_standard_curve", new String[]{"detection_item_id", "card_batch_id", "curve_id"}, null, null, null, null, "curve_id desc");

        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<Curve>();
        }
        while (cursor.moveToNext()) {

            Curve cv = new Curve();
            String id = cursor.getString(2);
            currentId = Integer.valueOf(id);

            if (currentId != preId) {
                preId = Integer.valueOf(id);
                cv.setId(id);
                String detection_item_id = cursor.getString(0);
                cv.setItem(i2name(detection_item_id));
              /*  String type_id = cursor.getString(1);
                cv.setSample(s2name(type_id));*/
                String card_batch_id = cursor.getString(1);
                cv.setCard_batch(b2name(card_batch_id));
                cList.add(cv);
            }
        }
        preId = 0;
        curveAdapt = new CurveAdapt();
        lv_curve.setAdapter(curveAdapt);
        if (cPosition != 0) {
            lv_curve.setSelection(cPosition);
        }
        lv_curve.setOnItemClickListener(this);
        cursor.close();

    }

    /**
     * 项目ID转名称
     *
     * @param detection_item_id
     * @return
     */
    private String i2name(String detection_item_id) {
        if (detection_item_id != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_item", new String[]{"detection_item_id", "detection_item_name"}, "detection_item_id=?", new String[]{detection_item_id}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_item_name = cursor2.getString(1);
                return detection_item_name;
            }
        }
        return null;
    }

    private String s2name(String type_id) {
        if (type_id != null) {
            Cursor cursor2 = sdb.query("galaxy_sample_type", new String[]{"type_id", "type_name"}, "type_id=?", new String[]{type_id}, null, null, null);
            while (cursor2.moveToNext()) {
                String type_name = cursor2.getString(1);
                return type_name;
            }
        }
        return null;
    }

    private String b2name(String card_batch_id) {
        if (card_batch_id != null) {
            Cursor cursor2 = sdb.query("galaxy_card_batch", new String[]{"card_batch_id", "card_batch"}, "card_batch_id=?", new String[]{card_batch_id}, null, null, null);
            while (cursor2.moveToNext()) {
                String card_batch = cursor2.getString(1);
                return card_batch;
            }
        }
        return null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_add:
                startActivity(new Intent(this, CurveSettingActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition = position;
        Intent i = new Intent(this, CurveSettingActivity.class);
        String name = cList.get(position).getId();
        i.putExtra("curve_id", name);
        startActivity(i);
    }

    /**
     * 适配器
     */
    class CurveAdapt extends BaseAdapter {


        @Override
        public int getCount() {
            return cList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(CurveListActivity.this, R.layout.item_curve, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
//                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(cList.get(position).getItem() + "  (No:" + cList.get(position).getId() + ")");
//            holder.tv_nature.setText(cList.get(position).getSample());
            holder.tv_company.setText(cList.get(position).getCard_batch());


            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog.Builder build2 = new AlertDialog.Builder(CurveListActivity.this);
                    build2.setTitle("删除曲线").setMessage("确定删除这个曲线").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_standard_curve", "curve_id=?", new String[]{cList.get(position).getId()});
                            initData();
                            lv_curve.setSelection(position);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView iv_delete;
            TextView tv_name;
            //            TextView tv_nature;
            TextView tv_company;

        }
    }

    /**
     * 由ID转换为名称
     *
     * @param company
     * @return
     */
    private String toname(String company) {
        if (company != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_id"}, "company_id=?", new String[]{company}, null, null, null);
            while (cursor2.moveToNext()) {
                String company_name = cursor2.getString(0);
                return company_name;
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
        cursor.close();
    }

}

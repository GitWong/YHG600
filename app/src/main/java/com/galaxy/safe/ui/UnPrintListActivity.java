package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.safe.Bean.Record;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class UnPrintListActivity extends Activity implements AdapterView.OnItemClickListener, RefreshListView.OnRefreshListen {

    private List<Record> cList;
    private RefreshListView lv_recordlist;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private RecordListAdapt adapt;
    private String item;
    private int position = 1;
    private int many = 30;
    private int count;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordlist);

        initView();
        initToolbar();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
        cursor.close();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        lv_recordlist = (RefreshListView) findViewById(R.id.lv_recordlist);
        lv_recordlist.isEnabledPullDownRefresh(false);
        lv_recordlist.setOnRefreshListen(this);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);


    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("未打印记录表页面");
        tl_bar.setSubtitle("未打印");
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
        if (cursor != null) {
            cursor.close();
        }
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_detection_record", new String[]{"sample_bh", "detection_bh", "detection_item_name", "detection_conclusion", "is_printed"},  "is_printed=?", new String[]{"0"}, null, null, "detection_bh desc", String.valueOf(position * many));
        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<Record>();
        }


        while (cursor.moveToNext()) {
            Record rc = new Record();
            String sample_bh = cursor.getString(0);
            rc.setSample_bh(sample_bh);

            String detection_bh = cursor.getString(1);
            rc.setSampling_no(detection_bh);
            rc.setReport_conclusion(cursor.getString(3));
            rc.setItem(cursor.getString(2));
            cList.add(rc);
        }
        cursor.close();

        if (cList.size() < 13) {
            lv_recordlist.isEnabledLoadingMore(false);
        }
        adapt = new RecordListAdapt();
        lv_recordlist.setAdapter(adapt);
        if (cPosition != 0) {
            lv_recordlist.setSelection(cPosition);
        }
        lv_recordlist.setOnItemClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition = position;
        Intent i = new Intent(this, RecordDetailActivity.class);
        i.putExtra("sampling_no", cList.get(position - 1).getSampling_no());
        startActivity(i);
    }

    @Override
    public void onPullDownRefresh() {

    }

    @Override
    public void onLoadingMore() {
        new Handler().postDelayed(new Runnable() {

            public void run() {
                //execute the task
                if (position * many >= count) {
                    Snackbar.make(tl_bar, "别上拉了，我是有底线的...", Snackbar.LENGTH_SHORT).show();
                } else {
                    position++;
                    initData();
                    lv_recordlist.setSelection((position - 1) * many);
                }
                lv_recordlist.onRefreshFinish();
            }

        }, 800);

    }

    /**
     * 适配器
     */
    class RecordListAdapt extends BaseAdapter {

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
                convertView = View.inflate(UnPrintListActivity.this, R.layout.item_recordlist, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(cList.get(position).getItem());
            holder.tv_nature.setText(cList.get(position).getSample_bh());
            if (cList.get(position).getReport_conclusion().equals("阴性")) {
                holder.tv_company.setTextColor(Color.GREEN);
                holder.tv_company.setText(cList.get(position).getReport_conclusion());
            } else {
                holder.tv_company.setTextColor(Color.RED);
                holder.tv_company.setText(cList.get(position).getReport_conclusion());
            }
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(UnPrintListActivity.this);
                    build2.setTitle("删除检测记录").setMessage("确定删除这个检测记录").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_detection_record", "detection_bh=?", new String[]{cList.get(position).getSampling_no()});
                            sdb.delete("galaxy_detection_recordcb", "detection_bh=?", new String[]{cList.get(position).getSampling_no()});
                            initData();
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
            TextView tv_name;
            TextView tv_nature;
            TextView tv_company;
            ImageView iv_delete;
        }
    }
}
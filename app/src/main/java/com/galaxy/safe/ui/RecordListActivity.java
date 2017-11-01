package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.Bean.Record;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class RecordListActivity extends Activity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, View.OnClickListener, RefreshListView.OnRefreshListen {

    private List<Record> cList;
    private RefreshListView lv_recordlist;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private RecordListAdapt adapt;
    private String item;
    private SearchView sv;
    private ImageView bt_total;
    private Button bt_backup;
    private Menu menu;
    private String[] mStrings = {"阴性", "阳性", "疑似阳性"};
    private int ying;
    private int yang;
    private int yanged;
    private int position = 1;
    private int many = 30;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordlist);

        initView();
        initToolbar();
        initData();


        cursor = sdb.query("galaxy_detection_record", new String[]{"detection_conclusion"}, "detection_conclusion=?", new String[]{"阴性"}, null, null, "detection_bh desc");
        while (cursor.moveToNext()) {
            ying = cursor.getCount();
        }
        cursor.close();
        cursor = sdb.query("galaxy_detection_record", new String[]{"detection_conclusion"}, "detection_conclusion=?", new String[]{"疑似阳性"}, null, null, "detection_bh desc");
        while (cursor.moveToNext()) {
            yanged = cursor.getCount();
        }
        cursor.close();
        cursor = sdb.query("galaxy_detection_record", new String[]{"detection_conclusion"}, "detection_conclusion=?", new String[]{"阳性"}, null, null, "detection_bh desc");
        while (cursor.moveToNext()) {
            yang = cursor.getCount();
        }
        cursor.close();
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
        lv_recordlist.setTextFilterEnabled(true);
        bt_backup = (Button) findViewById(R.id.bt_backup);
        bt_total = (ImageView) findViewById(R.id.bt_totle);
        bt_total.setVisibility(View.VISIBLE);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        bt_backup.setOnClickListener(this);
        bt_total.setOnClickListener(this);
        lv_recordlist.setOnRefreshListen(this);

    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_record, menu);

         sv = (SearchView) menu.findItem(
                R.id.action_search).getActionView();
        lv_recordlist.setTextFilterEnabled(true);
        sv.setIconifiedByDefault(false);
        sv.setQueryHint("查询记录");
        sv.setOnQueryTextListener(this);// 搜索的监听

        return true;
    }*/

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("检测记录表页面");
        tl_bar.setSubtitle("检测记录");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tl_bar.inflateMenu(R.menu.menu_record);
        menu = tl_bar.getMenu();
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        sv = (SearchView) MenuItemCompat.getActionView(menuItem);


        sv.setSubmitButtonEnabled(true);
        sv.setIconifiedByDefault(false);
        sv.setIconified(false);
        sv.clearFocus();
        sv.setOnQueryTextListener(this);
        sv.setQueryHint("查询记录");


    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (cursor != null) {
            cursor.close();
        }
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_detection_record", new String[]{"sample_bh", "detection_bh", "detection_item_name", "detection_conclusion"}, null, null, null, null, "detection_bh  desc", String.valueOf(position * many));
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
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        lv_recordlist.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_expandable_list_item_1, mStrings));

        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
            adapt.getFilter().filter(null);
//            lv_recordlist.clearTextFilter();
        } else {
            // 使用用户输入的内容对ListView的列表项进行过滤
            adapt.getFilter().filter(newText);
//            lv_recordlist.setFilterText(newText);
        }
        return true;
    }

    private View view2;//对话框ui
    private AlertDialog dialog3;
    private PieChartView chart;//饼状图
    private PieChartData data;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_totle:
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
            case R.id.bt_backup:
                final ProgressDialog dialog2 = new ProgressDialog(this);
                dialog2.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置进度条的形式为圆形转动的进度条
                dialog2.setMax(100);
                dialog2.setTitle("正在备份...");
                dialog2.setCancelable(true);// 设置是否可以通过点击Back键取消
                dialog2.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                dialog2.show();
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        int i = 0;
                        while (i < 10) {
                            try {
                                Thread.sleep(200);
                                // 更新进度条的进度,可以在子线程中更新进度条进度
                                dialog2.incrementProgressBy(15);
                                // dialog.incrementSecondaryProgressBy(10)//二级进度条更新方式
                                i++;
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                        }
                        // 在进度条走完时删除Dialog
                        dialog2.dismiss();
                        RecordListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(RecordListActivity.this, "备份成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).start();
                break;
        }
    }

    private void generateData() {


        List<SliceValue> values = new ArrayList<SliceValue>();


        values.add(new SliceValue((float) yang, RecordListActivity.this.getResources().getColor(R.color.red)));
        values.add(new SliceValue((float) yanged, RecordListActivity.this.getResources().getColor(R.color.colorAccent)));
        values.add(new SliceValue((float) ying, RecordListActivity.this.getResources().getColor(R.color.green)));


        data = new PieChartData(values);
        data.setHasLabels(true);

        data.setHasLabelsOutside(true);

        chart.setPieChartData(data);

    }

    @Override
    public void onPullDownRefresh() {

    }

    @Override
    public void onLoadingMore() {

        new Handler().postDelayed(new Runnable() {

            public void run() {
                //execute the task
                if (position * many >= yang + yanged + ying) {
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
     * 触摸显示内容
     */
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            if (arcIndex == 0) {
                Toast.makeText(RecordListActivity.this, "阳性数量: " + (int) value.getValue(), Toast.LENGTH_SHORT).show();
            } else if (arcIndex == 1) {
                Toast.makeText(RecordListActivity.this, "疑似阳性数量: " + (int) value.getValue(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RecordListActivity.this, "阴性数量: " + (int) value.getValue(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }

    /**
     * 适配器
     */
    class RecordListAdapt extends BaseAdapter implements Filterable {

        private ArrayList<Record> mOriginalValues;
        private String keyWrold = "";
        private final Object mLock = new Object();

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
                convertView = View.inflate(RecordListActivity.this, R.layout.item_recordlist, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (cList.get(position).getReport_conclusion().equals("阴性") || cList.get(position).getReport_conclusion().equals("合格")) {
                holder.tv_company.setTextColor(Color.GREEN);
                holder.tv_company.setText(cList.get(position).getReport_conclusion());
            } else {
                holder.tv_company.setTextColor(Color.RED);
                holder.tv_company.setText(cList.get(position).getReport_conclusion());
            }
            holder.tv_name.setText(cList.get(position).getItem());
            holder.tv_nature.setText(cList.get(position).getSample_bh());
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(RecordListActivity.this);
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


        @Override
        public Filter getFilter() {
            return mFilter;
        }

        Filter mFilter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                cList = (List<Record>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                // TODO Auto-generated method stub
                FilterResults filterResults = new FilterResults();

                if (mOriginalValues == null) {
                    synchronized (mLock) {
                        mOriginalValues = new ArrayList<Record>(cList);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    ArrayList<Record> list;
                    synchronized (mLock) {
                        list = new ArrayList<Record>(mOriginalValues);
                    }
                    filterResults.values = list;
                    filterResults.count = list.size();
                    keyWrold = "";
                } else {
                    String prefixString = prefix.toString().toLowerCase();
                    ArrayList<Record> values;
                    synchronized (mLock) {
                        values = new ArrayList<Record>(mOriginalValues);
                    }

                    final int count = values.size();
                    final ArrayList<Record> newValues = new ArrayList<Record>();

                    for (int i = 0; i < count; i++) {
                        final Record value = values.get(i);
                        final String valueText = value.getReport_conclusion().toString().toLowerCase();
                        final String valueText1 = value.getItem().toString().toLowerCase();
                        final String valueText2 = value.getSample_bh().toString().toLowerCase();

                        // First match against the whole, non-splitted value
                        if (valueText.contains(prefixString) || valueText1.contains(prefixString) || valueText2.contains(prefixString)) {
                            newValues.add(value);
                            keyWrold = prefixString;
                        }
                    }

                    filterResults.values = newValues;
                    filterResults.count = newValues.size();
                }

                return filterResults;
            }

        };

        class ViewHolder {
            TextView tv_name;
            TextView tv_nature;
            TextView tv_company;
            ImageView iv_delete;
        }
    }

}
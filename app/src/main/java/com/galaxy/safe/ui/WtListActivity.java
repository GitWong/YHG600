package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.galaxy.safe.Bean.ApplyInfo;
import com.galaxy.safe.R;
import com.galaxy.safe.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class WtListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, RefreshListView.OnRefreshListen {

    private List<ApplyInfo> cList;
    private RefreshListView lv_applylist;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private ApplyAdapt adapt;
    private int position = 1;
    private int many = 30;
    private int count;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applylist);
        initView();
        initToolbar();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        lv_applylist = (RefreshListView) findViewById(R.id.lv_applylist);
        lv_applylist.isEnabledPullDownRefresh(false);
        lv_applylist.setOnRefreshListen(this);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);

        iv_add.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
        cursor.close();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("委托列表页面");
        tl_bar.setSubtitle("委托");
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
        cursor = sdb.query("galaxy_application_form", new String[]{"testing_no", "weituo_number", "tasks_type"}, "tasks_type=?", new String[]{"4"}, null, null, "testing_no desc", String.valueOf(position * many));

        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<ApplyInfo>();
        }
        while (cursor.moveToNext()) {
            count = cursor.getColumnCount();
            ApplyInfo al = new ApplyInfo();
            String testing_no = cursor.getString(0);
            al.setTesting_no(testing_no);
            String weituo_number = cursor.getString(1);
            al.setWeituo_number(weituo_number);

            if (cursor.getString(2) != null) {
                switch (cursor.getString(2)) {

                    case "1":
                        al.setTasks_type("监督抽查");
                        break;
                    case "2":
                        al.setTasks_type("风险监测");
                        break;
                    case "3":
                        al.setTasks_type("市场开办者自检");
                        break;
                    case "4":
                        al.setTasks_type("委托");
                        break;
                    case "5":
                        al.setTasks_type("仲裁");
                        break;
                    case "6":
                        al.setTasks_type("比对");
                        break;
                    case "7":
                        al.setTasks_type("练兵");
                        break;
                    case "8":
                        al.setTasks_type("其他");
                        break;
                }
            }
            cList.add(al);
        }
        adapt = new ApplyAdapt();
        lv_applylist.setAdapter(adapt);
        if (cPosition != 0) {
            lv_applylist.setSelection(cPosition);
        }
        lv_applylist.setOnItemClickListener(this);
        cursor.close();

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
                startActivity(new Intent(this, ApplySettingActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition = position;
        Intent i = new Intent(this, ApplySettingActivity.class);
        i.putExtra("testing_no", cList.get(position - 1).getTesting_no());
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
                    lv_applylist.setSelection((position - 1) * many);
                }
                lv_applylist.onRefreshFinish();
            }

        }, 800);
    }

    /**
     * 适配器
     */
    class ApplyAdapt extends BaseAdapter {

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
                convertView = View.inflate(WtListActivity.this, R.layout.item_applylist, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(cList.get(position).getTesting_no());
            holder.tv_nature.setText(cList.get(position).getWeituo_number());
            holder.tv_company.setText(cList.get(position).getTasks_type());

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(WtListActivity.this);
                    build2.setTitle("删除抽样").setMessage("确定删除这个抽样单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_application_form", "testing_no=?", new String[]{cList.get(position).getTesting_no()});
                            sdb.delete("galaxy_application_formcb", "testing_no=?", new String[]{cList.get(position).getTesting_no()});
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
            ImageView iv_delete;
            TextView tv_name;
            TextView tv_nature;
            TextView tv_company;

        }
    }
}

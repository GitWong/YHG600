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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.galaxy.safe.Bean.Company;
import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.List;

public class CompanyListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<Company> cList;
    private ListView lv_company;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private CompanyAdapt companyAdapt;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_companylist);
        initView();
        initToolbar();
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
        cursor.close();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_property"}, null, null, null, null, null);

        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<Company>();
        }
        while (cursor.moveToNext()) {
            Company cp = new Company();
            String part = cursor.getString(0);
            cp.setCompany_name(part);
            String name = cursor.getString(1);
            cp.setCompany_property(name);
            cList.add(cp);
        }
        companyAdapt = new CompanyAdapt();
        lv_company.setAdapter(companyAdapt);
        lv_company.setSelection(cPosition);
        lv_company.setOnItemClickListener(this);

    }

    /**
     * 初始化控件
     */
    private void initView() {

        lv_company = (ListView) findViewById(R.id.lv_company);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);

        iv_add.setOnClickListener(this);
    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("企业信息页面");
        tl_bar.setSubtitle("企业信息");

      /*  tl_bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting:
                        dl.openDrawer(Gravity.RIGHT);
                        break;
                }
                return false;
            }
        });*/
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_add:
                startActivity(new Intent(this, CompanySettingActivity.class));
                break;
        }


    }

    /**
     * 点击每个项目响应
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition = position;
        Intent i = new Intent(this, CompanySettingActivity.class);
        String company_name = cList.get(position).getCompany_name();
        i.putExtra("company_name", company_name);
        startActivity(i);
    }

    /**
     * 适配器
     */
    class CompanyAdapt extends BaseAdapter {


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
                convertView = View.inflate(CompanyListActivity.this, R.layout.item_company, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(cList.get(position).getCompany_name());
            holder.tv_nature.setText(cList.get(position).getCompany_property());
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(CompanyListActivity.this);
                    build2.setTitle("删除企业").setMessage("确定删除" + cList.get(position).getCompany_name()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_detection_company", "company_name=?", new String[]{cList.get(position).getCompany_name()});
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
        }
    }

}

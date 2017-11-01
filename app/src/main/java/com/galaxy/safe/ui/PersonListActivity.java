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

import com.galaxy.safe.Bean.Person;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

public class PersonListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private List<Person> cList;
    private ListView lv_person;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private PersonAdapt personAdapt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personlist);
        initView();
        initToolbar();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        lv_person = (ListView) findViewById(R.id.lv_person);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        if (!new UserUtils(sdb, PersonListActivity.this).getPorperty().equals("系统管理员")) {
            iv_add.setVisibility(View.GONE);
        }
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
        tl_bar.setTitle("人员信息页面");
        tl_bar.setSubtitle("人员信息");
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

        cursor = sdb.query("galaxy_detection_person", new String[]{"person_name", "person_property", "company_id", "user_name"}, null, null, null, null, null);

        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<Person>();
        }
        while (cursor.moveToNext()) {

            if (!cursor.getString(3).equals("admin")) {
                Person ps = new Person();
                String person_name = cursor.getString(0);
                ps.setName(person_name);
                String person_property = cursor.getString(1);
                ps.setNature(person_property);
                String company_id = cursor.getString(2);
                ps.setCompany(toname(company_id));
                cList.add(ps);
            }
        }
        personAdapt = new PersonAdapt();
        lv_person.setAdapter(personAdapt);
        lv_person.setOnItemClickListener(this);
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
                startActivity(new Intent(this, PersonSettingActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(this, PersonSettingActivity.class);
        String name = cList.get(position).getName();
        i.putExtra("person_name", name);
        startActivity(i);
    }

    /**
     * 适配器
     */
    class PersonAdapt extends BaseAdapter {


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
                convertView = View.inflate(PersonListActivity.this, R.layout.item_person, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(cList.get(position).getName());
            holder.tv_nature.setText(cList.get(position).getNature());
            holder.tv_company.setText(cList.get(position).getCompany());


            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(PersonListActivity.this);
                    build2.setTitle("删除人员").setMessage("确定删除" + cList.get(position).getName()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_detection_person", "person_name=?", new String[]{cList.get(position).getName()});
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


}

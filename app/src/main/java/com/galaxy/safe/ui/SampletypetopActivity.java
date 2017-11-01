package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.Bean.SampleType;
import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.List;

public class SampletypetopActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView lv_sample_type;
    private ImageView bt_add;

    private SQLiteDatabase sdb;
    private List<SampleType> type_list;
    private SampleTypeAdapt adapter;
    private AlertDialog dialog1;
    private EditText et_name;//名称
    private EditText et_des;//描述
    private Toolbar tl_bar;
    private View view;//d对话框布局

    private String name1;//点击条目的名称
    private Integer max = 0;


    private Cursor cursor;//游标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sampletypetop);
        initView();
        initToolbar();
        initData();
        lv_sample_type.setOnItemClickListener(this);
        bt_add.setOnClickListener(this);


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
        lv_sample_type = (ListView) findViewById(R.id.lv_sample_type);
        bt_add = (ImageView) findViewById(R.id.bt_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_sample_typetop", new String[]{"typetop_name", "typetop_desc", "typetop_id"}, null, null, null, null, null);
        type_list = new ArrayList<SampleType>();
        while (cursor.moveToNext()) {

            if (cursor.isLast()) {
                if (cursor.getString(2) == null) {
                    max = 0;
                }
                max = Integer.valueOf(cursor.getString(2));
            }
            SampleType st = new SampleType();
            String name = cursor.getString(0);
            st.setSample_name(name);
            String des = cursor.getString(1);
            st.setSample_des(des);
            type_list.add(st);
        }
        adapter = new SampleTypeAdapt();
        lv_sample_type.setAdapter(adapter);
    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("样品大类页面");
        tl_bar.setSubtitle("样品大类");
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
            case R.id.bt_add:
                AlertDialog.Builder build1 = new AlertDialog.Builder(this);
                view = View.inflate(this, R.layout.edit_dialog_sampletype, null);
                et_name = (EditText) view.findViewById(R.id.et_sample_name);
                et_des = (EditText) view.findViewById(R.id.et_sample_des);

                view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(et_name.getText().toString().trim())) {

                            String name = et_name.getText().toString().trim();
                            String des = et_des.getText().toString().trim();

                            ContentValues cv = new ContentValues();
                            cv.put("typetop_valid", 1);
                            cv.put("typetop_name", name);
                            cv.put("typetop_desc", des);
                            cv.put("typetop_id", max + 1);


                            long d = sdb.insert("galaxy_sample_typetop", null, cv);
                            if (d == -1) {
                                Toast.makeText(SampletypetopActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SampletypetopActivity.this, "储存数据库成功", Toast.LENGTH_SHORT).show();
                            }
                            initData();
                            dialog1.dismiss();
                        } else {
                            Toast.makeText(SampletypetopActivity.this, "名称不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                view.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                build1.setView(view);
                dialog1 = build1.show();
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        AlertDialog.Builder build1 = new AlertDialog.Builder(this);
        view = View.inflate(this, R.layout.edit_dialog_sampletype, null);
        et_name = (EditText) view.findViewById(R.id.et_sample_name);
        et_des = (EditText) view.findViewById(R.id.et_sample_des);

        name1 = type_list.get(position).getSample_name();
        et_des.setText(type_list.get(position).getSample_des());
        et_name.setText(name1);

        view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String des = et_des.getText().toString().trim();
                ContentValues cv1 = new ContentValues();
                cv1.put("typetop_name", name);
                cv1.put("typetop_desc", des);
                int d = sdb.update("galaxy_sample_typetop", cv1, "type_name=?", new String[]{name1});
                if (d == -1) {
                    Toast.makeText(SampletypetopActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SampletypetopActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                initData();
                dialog1.dismiss();
            }
        });
        view.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        build1.setView(view);
        dialog1 = build1.show();


    }

    /**
     * 适配器
     */
    class SampleTypeAdapt extends BaseAdapter {

        @Override
        public int getCount() {
            return type_list.size();
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
                convertView = View.inflate(SampletypetopActivity.this, R.layout.item_sample_type, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_des = (TextView) convertView.findViewById(R.id.tv_des);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(type_list.get(position).getSample_name());
            holder.tv_des.setText(type_list.get(position).getSample_des());
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(SampletypetopActivity.this);
                    build2.setTitle("删除样本种类").setMessage("确定删除" + type_list.get(position).getSample_name()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_sample_typetop", "typetop_name=?", new String[]{type_list.get(position).getSample_name()});
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
            TextView tv_des;
        }
    }
}

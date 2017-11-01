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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/4/5.
 */
public class ItemsTypeActivity extends Activity implements View.OnClickListener {

    private ListView lv_items_type;
    private ImageView bt_add;

    private SQLiteDatabase sdb;
    private List<String> type_list;
    private ItemTypeAdapt adapter;
    private AlertDialog dialog1;
    private EditText et_name;//名称
    private Toolbar tl_bar;
    private View view;//d对话框布局

    private Cursor cursor;//游标
    private String max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_itemstype);

        initView();
        initToolbar();
        initData();
        bt_add.setOnClickListener(this);
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("项目种类页面");
        tl_bar.setSubtitle("项目种类");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv_items_type = (ListView) findViewById(R.id.lv_item_type);
        bt_add = (ImageView) findViewById(R.id.bt_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_itemtype", new String[]{"itemtype", "itemtype_id"}, null, null, null, null, null);
        type_list = new ArrayList<String>();

        while (cursor.moveToNext()) {
            String item = cursor.getString(0);
            if (cursor.isLast()) {
                max = cursor.getString(1);
            }
            type_list.add(item);
        }
        adapter = new ItemTypeAdapt();
        lv_items_type.setAdapter(adapter);
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
        cursor.close();
    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder build1 = new AlertDialog.Builder(this);
        view = View.inflate(this, R.layout.edit_dialog_itemtype, null);
        et_name = (EditText) view.findViewById(R.id.et_name);
        view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_name.getText().toString().trim())) {
                    String name = et_name.getText().toString().trim();
                    ContentValues cv = new ContentValues();
                    cv.put("itemtype", name);
                    if (max != null) {
                        cv.put("itemtype_id", Integer.valueOf(max) + 1);
                    }
                    long d = sdb.insert("galaxy_itemtype", null, cv);
                    if (d == -1) {
                        Toast.makeText(ItemsTypeActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ItemsTypeActivity.this, "储存数据库成功", Toast.LENGTH_SHORT).show();
                    }
                    initData();
                    dialog1.dismiss();
                } else {
                    Toast.makeText(ItemsTypeActivity.this, "种类不能为空", Toast.LENGTH_SHORT).show();
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
    }

    /**
     * 适配器
     */
    class ItemTypeAdapt extends BaseAdapter {

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
                convertView = View.inflate(ItemsTypeActivity.this, R.layout.item_type, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(type_list.get(position).toString());
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(ItemsTypeActivity.this);
                    build2.setTitle("删除项目种类").setMessage("确定删除"+type_list.get(position).toString()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_itemtype", "itemtype=?", new String[]{type_list.get(position).toString()});
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
        }
    }
}

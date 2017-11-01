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
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Dell on 2016/3/29.
 */
public class UnitSettingActivity extends Activity implements View.OnClickListener {
    private GridView lv_unit;
    private ImageView bt_add;
    private SQLiteDatabase sdb;
    private List<String> check_list;
    private UnitAdapt adapter;
    private AlertDialog dialog1;
    private EditText et_unit;//单位
    private Toolbar tl_bar;
    private List<String> newList;//新建的单位
    private Map<String, String> editMap;//修改的集合
    private String t;//查询的标题
    private int positon;//查询的位置

    private Cursor cursor;//游标
    private String max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unitsetting);
        initView();
        initToolbar();
        initData();
        registerForContextMenu(lv_unit);

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
        lv_unit = (GridView) findViewById(R.id.lv_unit);
        bt_add = (ImageView) findViewById(R.id.bt_add);

        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_unit", new String[]{"limited_unit", "limited_unit_id"}, null, null, null, null, null);
        check_list = new ArrayList<String>();
        while (cursor.moveToNext()) {
            if (cursor.isLast()) {
                max = cursor.getString(1);
            }
            String part = cursor.getString(0);
            check_list.add(part);
        }
        adapter = new UnitAdapt();

        lv_unit.setAdapter(adapter);
        newList = new ArrayList<String>();
        bt_add.setOnClickListener(this);

    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("单位设置页面");
        tl_bar.setSubtitle("单位设置");
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
                View view = View.inflate(this, R.layout.edit_dialog_unit, null);
                et_unit = (EditText) view.findViewById(R.id.et_report);
                view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(et_unit.getText().toString().trim())) {
                            String unit = et_unit.getText().toString().trim();
                            newList.add(unit);
                            check_list.add(unit);
                            adapter.notifyDataSetChanged();
                            ContentValues cv = new ContentValues();
                            cv.put("limited_unit", unit);
                            cv.put("limited_unit_id", Integer.valueOf(max) + 1);
                            long d = sdb.insert("galaxy_unit", null, cv);
                            if (d == -1) {
                                Toast.makeText(UnitSettingActivity.this, "增加失败", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(UnitSettingActivity.this, "增加成功", Toast.LENGTH_SHORT).show();
                            }


                            dialog1.dismiss();
                        } else {
                            Toast.makeText(UnitSettingActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
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

    /**
     * 创建上下文菜单
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(0, 1, 0, "编辑");
        menu.add(0, 2, 0, "删除");

    }

    /**
     * 上下文菜单的选择处理
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        t = check_list.get(info.position);
        positon = info.position;
        switch (item.getItemId()) {
            case 1://编辑
                AlertDialog.Builder build1 = new AlertDialog.Builder(this);
                View view = View.inflate(this, R.layout.edit_dialog_unit, null);
                et_unit = (EditText) view.findViewById(R.id.et_report);
                et_unit.setText(t);
                view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String unit = et_unit.getText().toString().trim();
                        ContentValues cv = new ContentValues();
                        cv.put("limited_unit", unit);
                        sdb.update("galaxy_unit", cv, "limited_unit=?", new String[]{t});
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
                break;
            case 2://删除
                final AlertDialog.Builder build2 = new AlertDialog.Builder(this);
                build2.setTitle("删除单位").setMessage("确定删除这个单位").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sdb.delete("galaxy_unit", "limited_unit=?", new String[]{t});
                        initData();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    class UnitAdapt extends BaseAdapter {

        @Override
        public int getCount() {
            return check_list.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(UnitSettingActivity.this, R.layout.item_unit, null);
            TextView tv = (TextView) convertView.findViewById(R.id.tv_unit);
            tv.setText(check_list.get(position).toString());
            return convertView;
        }
    }
}

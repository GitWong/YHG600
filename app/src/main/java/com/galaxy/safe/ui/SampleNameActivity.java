package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.Bean.SampleInfo;
import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.List;

public class SampleNameActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<SampleInfo> cList;
    private ListView lv_sample_info;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private SamplefoAdapt adapt;

    private EditText et_name;//名称
    private EditText et_item_type;//项目类型
    private EditText et_des;//项目描述

    private List<String> check_list;
    private PopupWindow popupWindow;
    private ArrayAdapter<String> madapter;
    private AlertDialog dialog1;
    private View view;//对话框ui
    private String name2;//所选的item
    private String max;//最大id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_name);
        initView();
        initToolbar();
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_sample_typedetail", new String[]{"sample_name", "type_id", "sample_desc", "sample_id"}, null, null, null, null, null);
        cList = new ArrayList<SampleInfo>();

        while (cursor.moveToNext()) {

            if (cursor.isLast()) {
                max = cursor.getString(3);
            }
            SampleInfo info = new SampleInfo();
            String name = cursor.getString(0);
            info.setSample_name(name);
            String type = toname(cursor.getString(1));
            info.setSample_type(type);
            String des = cursor.getString(2);
            info.setSample_des(des);
            cList.add(info);
        }
        adapt = new SamplefoAdapt();
        lv_sample_info.setAdapter(adapt);
    }

    private String toname(String name) {

        if (name != null) {
            Cursor cursor2 = sdb.query("galaxy_sample_type", new String[]{"type_id", "type_name"}, "type_id=?", new String[]{name}, null, null, null);
            while (cursor2.moveToNext()) {
                String type_name = cursor2.getString(1);
                return type_name;
            }
            cursor2.close();
        }

        return null;
    }

    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("样品信息页面");
        tl_bar.setSubtitle("样品信息");
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

        lv_sample_info = (ListView) findViewById(R.id.lv_sample_info);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        iv_add.setOnClickListener(this);
        lv_sample_info.setOnItemClickListener(this);
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
        view = View.inflate(this, R.layout.edit_dialog_sampleinfo, null);
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_item_type = (EditText) view.findViewById(R.id.et_item_type);
        et_des = (EditText) view.findViewById(R.id.et_des);
        view.findViewById(R.id.iv_item).setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {

                                                                   View popview = View.inflate(SampleNameActivity.this, R.layout.popup_item, null);
                                                                   ListView lv_content = (ListView) popview.findViewById(R.id.lv_content);

                                                                   cursor = sdb.query("galaxy_sample_type", new String[]{"type_name"}, null, null, null, null, null);
                                                                   check_list = new ArrayList<String>();
                                                                   while (cursor.moveToNext()) {
                                                                       String part = cursor.getString(0);
                                                                       check_list.add(part);
                                                                   }
                                                                   madapter = new ArrayAdapter<String>(SampleNameActivity.this, android.R.layout.simple_list_item_1, check_list);
                                                                   lv_content.setAdapter(madapter);

                                                                   popupWindow = new PopupWindow(popview, et_item_type.getWidth(),
                                                                           -2, true);
                                                                   popupWindow.setBackgroundDrawable(new

                                                                                   ColorDrawable(
                                                                                   Color.TRANSPARENT)

                                                                   );
                                                                   popupWindow.showAsDropDown(et_item_type);
                                                                   popupWindow.setOutsideTouchable(false);
                                                                   cursor.close();
                                                                   lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                                                                                     {
                                                                                                         @Override
                                                                                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                                                                             et_item_type.setText(check_list.get(position).toString());
                                                                                                             popupWindow.dismiss();
                                                                                                             popupWindow = null;
                                                                                                             madapter = null;
                                                                                                         }
                                                                                                     }

                                                                   );


                                                               }
                                                           }

        );

        view.findViewById(R.id.bt_ok).

                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (!TextUtils.isEmpty(et_name.getText().toString().trim()) && !TextUtils.isEmpty(et_item_type.getText().toString())) {
                                               String name = et_name.getText().toString().trim();
                                               String type = et_item_type.getText().toString().trim();
                                               String des = et_des.getText().toString().trim();
                                               ContentValues cv = new ContentValues();

                                               cv.put("sample_valid", 1);
                                               cv.put("sample_name", name);
                                               cv.put("type_id", toid(type));
                                               cv.put("sample_desc", des);
                                               cv.put("sample_id", Integer.valueOf(max) + 1);

                                               long d = sdb.insert("galaxy_sample_typedetail", null, cv);
                                               if (d == -1) {
                                                   Toast.makeText(SampleNameActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                               } else {
                                                   Toast.makeText(SampleNameActivity.this, "储存数据库成功", Toast.LENGTH_SHORT).show();
                                               }
                                               initData();
                                               lv_sample_info.setSelection(cList.size());
                                               dialog1.dismiss();
                                           } else {
                                               Toast.makeText(SampleNameActivity.this, "样品名称类型不能为空", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   }

                );
        view.findViewById(R.id.bt_cancel).

                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           dialog1.dismiss();
                                       }
                                   }

                );
        build1.setView(view);
        dialog1 = build1.show();


    }

    private String toid(String type) {
        Log.i("66", type);
        if (type != null) {
            Cursor cursor2 = sdb.query("galaxy_sample_type", new String[]{"type_name", "type_id"}, "type_name=?", new String[]{type}, null, null, null);
            while (cursor2.moveToNext()) {
                String type_id = cursor2.getString(1);
                return type_id;
            }
            cursor2.close();
        }

        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder build1 = new AlertDialog.Builder(this);
        view = View.inflate(this, R.layout.edit_dialog_sampleinfo, null);
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_item_type = (EditText) view.findViewById(R.id.et_item_type);
        et_des = (EditText) view.findViewById(R.id.et_des);

        name2 = cList.get(position).getSample_name();
        et_name.setText(name2);
        et_item_type.setText(cList.get(position).getSample_type());
        et_des.setText(cList.get(position).getSample_des());

        view.findViewById(R.id.iv_item).setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {

                                                                   View popview = View.inflate(SampleNameActivity.this, R.layout.popup_item, null);
                                                                   ListView lv_content = (ListView) popview.findViewById(R.id.lv_content);

                                                                   cursor = sdb.query("galaxy_sample_type", new String[]{"type_name"}, null, null, null, null, null);
                                                                   check_list = new ArrayList<String>();
                                                                   while (cursor.moveToNext()) {
                                                                       String part = cursor.getString(0);
                                                                       check_list.add(part);
                                                                   }
                                                                   madapter = new ArrayAdapter<String>(SampleNameActivity.this, android.R.layout.simple_list_item_1, check_list);
                                                                   lv_content.setAdapter(madapter);

                                                                   popupWindow = new PopupWindow(popview, et_item_type.getWidth(),
                                                                           -2, true);
                                                                   popupWindow.setBackgroundDrawable(new

                                                                                   ColorDrawable(
                                                                                   Color.TRANSPARENT)

                                                                   );
                                                                   popupWindow.showAsDropDown(et_item_type);
                                                                   popupWindow.setOutsideTouchable(false);
                                                                   cursor.close();
                                                                   lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener()

                                                                                                     {
                                                                                                         @Override
                                                                                                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                                                                             et_item_type.setText(check_list.get(position).toString());
                                                                                                             popupWindow.dismiss();
                                                                                                             popupWindow = null;
                                                                                                             madapter = null;
                                                                                                         }
                                                                                                     }
                                                                   );
                                                               }
                                                           }

        );

        view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString().trim();
                String type = et_item_type.getText().toString().trim();
                String des = et_des.getText().toString().trim();
                ContentValues cv1 = new ContentValues();

                cv1.put("sample_name", name);
                cv1.put("type_id", toid(type));
                cv1.put("sample_desc", des);

                int d = sdb.update("galaxy_sample_typedetail", cv1, "sample_name=?", new String[]{name2});
                if (d == -1) {
                    Toast.makeText(SampleNameActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SampleNameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                initData();
                lv_sample_info.setSelection(position);
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
    class SamplefoAdapt extends BaseAdapter {


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
                convertView = View.inflate(SampleNameActivity.this, R.layout.item_company, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_name.setText(cList.get(position).getSample_name());
            holder.tv_type.setText(cList.get(position).getSample_type());
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(SampleNameActivity.this);
                    build2.setTitle("删除样品信息").setMessage("确定删除" + cList.get(position).getSample_name()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_sample_typedetail", "sample_name=?", new String[]{cList.get(position).getSample_name()});
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
            TextView tv_type;
        }
    }

}

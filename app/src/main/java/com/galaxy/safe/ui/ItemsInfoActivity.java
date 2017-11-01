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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.Bean.Company;
import com.galaxy.safe.Bean.ItemInfo;
import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsInfoActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<ItemInfo> cList;
    private ListView lv_item_info;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private IteminfoAdapt adapt;

    private EditText et_name;//名称
    private EditText et_item_type;//项目类型
    private EditText et_des;//项目描述

    private List<String> check_list;
    private PopupWindow popupWindow;
    private ArrayAdapter<String> madapter;
    private AlertDialog dialog1;
    private View view;//对话框ui
    private String name2;//所选的item

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_items);
        initView();
        initToolbar();
        initData();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_detection_item", new String[]{"detection_item_name", "detection_itemtype_id", "detection_item_desc"}, null, null, null, null, null);
        cList = new ArrayList<ItemInfo>();

        while (cursor.moveToNext()) {
            ItemInfo info = new ItemInfo();
            String part = cursor.getString(0);
            info.setItem_name(part);
            String name = cursor.getString(1);
            info.setItem_type(toname(name));
            String des = cursor.getString(2);
            info.setItem_des(des);
            cList.add(info);
        }
        adapt = new IteminfoAdapt();
        lv_item_info.setAdapter(adapt);
    }

    private String toname(String name) {

        if (name != null) {
            Cursor cursor2 = sdb.query("galaxy_itemtype", new String[]{"itemtype_id", "itemtype"}, "itemtype_id=?", new String[]{name}, null, null, null);
            while (cursor2.moveToNext()) {
                String item_name = cursor2.getString(1);
                return item_name;
            }
            cursor2.close();
        }

        return null;
    }

    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("项目信息页面");
        tl_bar.setSubtitle("项目信息");
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

        lv_item_info = (ListView) findViewById(R.id.lv_item_info);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        iv_add.setOnClickListener(this);
        lv_item_info.setOnItemClickListener(this);
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
        view = View.inflate(this, R.layout.edit_dialog_iteminfo, null);
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_item_type = (EditText) view.findViewById(R.id.et_item_type);
        et_des = (EditText) view.findViewById(R.id.et_des);
        view.findViewById(R.id.iv_item).setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {

                                                                   View popview = View.inflate(ItemsInfoActivity.this, R.layout.popup_item, null);
                                                                   ListView lv_content = (ListView) popview.findViewById(R.id.lv_content);

                                                                   cursor = sdb.query("galaxy_itemtype", new String[]{"itemtype"}, null, null, null, null, null);
                                                                   check_list = new ArrayList<String>();
                                                                   while (cursor.moveToNext()) {
                                                                       String part = cursor.getString(0);
                                                                       check_list.add(part);
                                                                   }
                                                                   madapter = new ArrayAdapter<String>(ItemsInfoActivity.this, android.R.layout.simple_list_item_1, check_list);
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
                                           if (!TextUtils.isEmpty(et_name.getText().toString().trim()) && !TextUtils.isEmpty(et_item_type.getText().toString().trim())) {
                                               String name = et_name.getText().toString().trim();
                                               String type = et_item_type.getText().toString().trim();
                                               String des = et_des.getText().toString().trim();
                                               ContentValues cv = new ContentValues();
                                               cv.put("detection_item_name", name);
                                               cv.put("detection_itemtype_id", toid(type));
                                               cv.put("detection_item_desc", des);

                                               long d = sdb.insert("galaxy_detection_item", null, cv);
                                               if (d == -1) {
                                                   Toast.makeText(ItemsInfoActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                               } else {
                                                   Toast.makeText(ItemsInfoActivity.this, "储存数据库成功", Toast.LENGTH_SHORT).show();
                                               }
                                               initData();
                                               lv_item_info.setSelection(cList.size());
                                               dialog1.dismiss();
                                           } else {
                                               Toast.makeText(ItemsInfoActivity.this, "项目名称和种类不能为空", Toast.LENGTH_SHORT).show();
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

        if (type != null) {
            Cursor cursor2 = sdb.query("galaxy_itemtype", new String[]{"itemtype_id", "itemtype"}, "itemtype=?", new String[]{type}, null, null, null);
            while (cursor2.moveToNext()) {
                String item_name = cursor2.getString(0);
                return item_name;
            }
            cursor2.close();
        }

        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder build1 = new AlertDialog.Builder(this);
        view = View.inflate(this, R.layout.edit_dialog_iteminfo, null);
        et_name = (EditText) view.findViewById(R.id.et_name);
        et_item_type = (EditText) view.findViewById(R.id.et_item_type);
        et_des = (EditText) view.findViewById(R.id.et_des);

        name2 = cList.get(position).getItem_name();
        et_name.setText(name2);
        et_item_type.setText(cList.get(position).getItem_type());
        et_des.setText(cList.get(position).getItem_des());

        view.findViewById(R.id.iv_item).setOnClickListener(new View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View v) {

                                                                   View popview = View.inflate(ItemsInfoActivity.this, R.layout.popup_item, null);
                                                                   ListView lv_content = (ListView) popview.findViewById(R.id.lv_content);

                                                                   cursor = sdb.query("galaxy_itemtype", new String[]{"itemtype"}, null, null, null, null, null);
                                                                   check_list = new ArrayList<String>();
                                                                   while (cursor.moveToNext()) {
                                                                       String part = cursor.getString(0);
                                                                       check_list.add(part);
                                                                   }
                                                                   madapter = new ArrayAdapter<String>(ItemsInfoActivity.this, android.R.layout.simple_list_item_1, check_list);
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

                cv1.put("detection_item_name", name);
                cv1.put("detection_itemtype_id", toid(type));
                cv1.put("detection_item_desc", des);
                int d = sdb.update("galaxy_detection_item", cv1, "detection_item_name=?", new String[]{name2});
                if (d == -1) {
                    Toast.makeText(ItemsInfoActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ItemsInfoActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
                initData();
                lv_item_info.setSelection(position);
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
    class IteminfoAdapt extends BaseAdapter {


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
                convertView = View.inflate(ItemsInfoActivity.this, R.layout.item_company, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(cList.get(position).getItem_name());
            holder.tv_type.setText(cList.get(position).getItem_type());
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(ItemsInfoActivity.this);
                    build2.setTitle("删除项目").setMessage("确定删除"+cList.get(position).getItem_name()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_detection_item", "detection_item_name=?", new String[]{cList.get(position).getItem_name()});
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

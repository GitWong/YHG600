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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.galaxy.safe.Bean.CardType;
import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.List;

public class CardtypeListActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private List<CardType> cList;
    private ListView lv_card_type;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private CardTypeAdapt typeAdapt;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardtype_list);
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
        cursor = sdb.query("galaxy_card_type", new String[]{"card_type", "line_number"}, null, null, null, null, null);

        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<CardType>();
        }
        while (cursor.moveToNext()) {
            CardType ct = new CardType();
            String card_type = cursor.getString(0);
            ct.setCard_type(card_type);
            String line_number = cursor.getString(1);
            ct.setLine_num(line_number);
            cList.add(ct);
        }
        typeAdapt = new CardTypeAdapt();
        lv_card_type.setAdapter(typeAdapt);
        if(cPosition!=0){lv_card_type.setSelection(cPosition);}
        lv_card_type.setOnItemClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        lv_card_type = (ListView) findViewById(R.id.lv_card_type);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        iv_add.setOnClickListener(this);

    }

    /**
     * 初始化 toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("卡类型表页面");
        tl_bar.setSubtitle("卡类型信息");

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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition=position;
        Intent i = new Intent(this, CardTypeSettingActivity.class);
        String name=cList.get(position).getCard_type();
        i.putExtra("card_type",name);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, CardTypeSettingActivity.class));
    }

    /**
     * 适配器
     */
    class CardTypeAdapt extends BaseAdapter {


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
                convertView = View.inflate(CardtypeListActivity.this, R.layout.item_company, null);
                holder = new ViewHolder();
                holder.tv_type = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_line = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_type.setText(cList.get(position).getCard_type());
            holder.tv_line.setText(cList.get(position).getLine_num());
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(CardtypeListActivity.this);
                    build2.setTitle("删除卡类型").setMessage("确定删除"+cList.get(position).getCard_type()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_card_type", "card_type=?", new String[]{cList.get(position).getCard_type()});
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
            TextView tv_type;
            TextView tv_line;
        }
    }

}

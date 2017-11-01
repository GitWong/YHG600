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

import com.galaxy.safe.Bean.CardBatch;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.Id2nameUtils;

import java.util.ArrayList;
import java.util.List;

public class CardbatchListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<CardBatch> cList;
    private ListView lv_card_batch;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private CardBatchAdapt cardBatchAdapt;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardbatch_list);
        initView();
        initToolbar();
        initData();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        lv_card_batch = (ListView) findViewById(R.id.lv_card_batch);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        iv_add.setOnClickListener(this);
        lv_card_batch.setOnItemClickListener(this);
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
    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch", "card_type_id", "supplier_id"}, null, null, null, null, null);

        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<CardBatch>();
        }
        while (cursor.moveToNext()) {
            CardBatch cb = new CardBatch();
            String card_batch = cursor.getString(0);
            cb.setCard_batch(card_batch);
            String card_type_id = cursor.getString(1);
            cb.setCare_type(t2name(card_type_id));
            String company = Id2nameUtils.company2name(cursor.getString(2), sdb);
            cb.setCompany(company);
            cList.add(cb);
        }
        cardBatchAdapt = new CardBatchAdapt();
        lv_card_batch.setAdapter(cardBatchAdapt);
        if (cPosition != 0) {
            lv_card_batch.setSelection(cPosition);
        }
//        lv_person.setOnItemClickListener(this);
        cursor.close();

    }

    private String i2name(String detection_item_id1) {
        if (detection_item_id1 != null) {
            Cursor cursor3 = sdb.query("galaxy_detection_item", new String[]{"detection_item_id", "detection_item_name"}, "detection_item_id=?", new String[]{detection_item_id1}, null, null, null);
            while (cursor3.moveToNext()) {
                String detection_item_name = cursor3.getString(1);
                return detection_item_name;
            }
        }
        return null;
    }

    private String t2name(String card_type_id) {
        if (card_type_id != null) {
            Cursor cursor2 = sdb.query("galaxy_card_type", new String[]{"card_type_id", "card_type"}, "card_type_id=?", new String[]{card_type_id}, null, null, null);
            while (cursor2.moveToNext()) {
                String card_type = cursor2.getString(1);
                return card_type;
            }
        }

        return null;
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("卡批次信息页面");
        tl_bar.setSubtitle("卡批次信息");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, CardBatchSettingActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition = position;
        Intent i = new Intent(this, CardBatchSettingActivity.class);
        String name = cList.get(position).getCard_batch();
        i.putExtra("card_batch", name);
        startActivity(i);

    }

    /**
     * 适配器
     */
    class CardBatchAdapt extends BaseAdapter {


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
                convertView = View.inflate(CardbatchListActivity.this, R.layout.item_cardbatch, null);
                holder = new ViewHolder();
                holder.tv_cardbatch = (TextView) convertView.findViewById(R.id.tv_cardbatch);
                holder.tv_cardtype = (TextView) convertView.findViewById(R.id.tv_cardtype);
                holder.tv_item = (TextView) convertView.findViewById(R.id.tv_item);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_cardbatch.setText(cList.get(position).getCard_batch());
            holder.tv_cardtype.setText(cList.get(position).getCare_type());
            holder.tv_item.setText(cList.get(position).getCompany());


            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(CardbatchListActivity.this);
                    build2.setTitle("删除卡批次").setMessage("确定删除" + cList.get(position).getCard_batch()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_card_batch", "card_batch=?", new String[]{cList.get(position).getCard_batch()});
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
            TextView tv_cardbatch;
            TextView tv_cardtype;
            TextView tv_item;

        }
    }

}

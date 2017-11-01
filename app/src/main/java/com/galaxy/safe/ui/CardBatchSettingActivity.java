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
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.galaxy.safe.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dell on 2016/4/1.
 */
public class CardBatchSettingActivity extends Activity implements View.OnClickListener {


    private EditText et_card_batch;//卡批次号
    private EditText et_safe_time;//保质期
    private EditText et_card_type;//卡类型
    private ImageView iv_card_type;
    private EditText et_offer;//提供商
    private ImageView iv_offer;
    private EditText et_data;//生产日期
    private ImageView iv_data;
    private EditText et_card_num;//卡数量
    private EditText et_use_num;//使用数量


    private Button bt_save;
    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private AnimationSet set;
    private String card_batch;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catdbatchsetting);
        initView();
        initAnimotion();
        initToolBar();
        initData();
        card_batch = getIntent().getStringExtra("card_batch");
        if (card_batch != null) {
            fillData();
        }

        iv_card_type.setOnClickListener(this);
        iv_offer.setOnClickListener(this);
        iv_data.setOnClickListener(this);


        bt_save.setOnClickListener(this);

    }

    private void fillData() {
        bt_save.setText("更改卡批次信息");
        cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch", "supplier_id",
                "valid_date", "card_type_id", "produce_date",
                "card_num", "use_num",}, "card_batch=?", new String[]{card_batch}, null, null, null);

        while (cursor.moveToNext()) {
            et_card_batch.setText(cursor.getString(0));
            et_offer.setText(s2name(cursor.getString(1)));
            et_safe_time.setText(cursor.getString(2));
            et_card_type.setText(t2name(cursor.getString(3)));
            et_data.setText(cursor.getString(4));
            et_card_num.setText(cursor.getString(5));
            et_use_num.setText(cursor.getString(6));

        }

    }

    private String s2name(String s) {
        if (s != null) {
            Cursor cursor3 = sdb.query("galaxy_detection_company", new String[]{"company_id", "company_name"}, "company_id=?", new String[]{s}, null, null, null);
            while (cursor3.moveToNext()) {
                String company_name = cursor3.getString(1);
                return company_name;
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
            cursor2.close();
        }

        return null;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);

        et_card_batch = (EditText) findViewById(R.id.et_card_batch);
        et_safe_time = (EditText) findViewById(R.id.et_safe_time);
        et_card_type = (EditText) findViewById(R.id.et_card_type);

        iv_card_type = (ImageView) findViewById(R.id.iv_card_type);
        iv_offer = (ImageView) findViewById(R.id.iv_offer);
        iv_data = (ImageView) findViewById(R.id.iv_data);


        et_offer = (EditText) findViewById(R.id.et_offer);
        et_data = (EditText) findViewById(R.id.et_data);
        et_card_num = (EditText) findViewById(R.id.et_card_num);
        et_use_num = (EditText) findViewById(R.id.et_use_num);

        bt_save = (Button) findViewById(R.id.bt_save);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch_id"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isLast()) {
                max = Integer.valueOf(cursor.getString(0));
            }
        }


    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("卡批次详细信息页面");
        tl_bar.setSubtitle("卡批次详情");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 初始化动画
     */
    private void initAnimotion() {
        ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f,
                1.0f, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(200);
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(200);
        set = new AnimationSet(false);
        set.addAnimation(aa);
        set.addAnimation(sa);
    }

    /**
     * 数据库关闭
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
        cursor.close();
    }

    private Cursor cursor;
    private List<String> check_list;
    private ArrayAdapter<String> adapter;

    private View popview;
    private PopupWindow popupWindow;
    private ListView lv_content;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_card_type:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_card_type", new String[]{"card_type"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_card_type.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_card_type);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_card_type.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;

            case R.id.iv_data:

                View view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker2 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker2.setVisibility(View.GONE);
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                datepicker.init(year, month, day, null);
                System.out.println(year + month + day + "");

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(view);
                builder.setTitle("生产日期选择");

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker.getYear();
                        int s_month = datepicker.getMonth();
                        int s_day = datepicker.getDayOfMonth();
                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                        String currentDate = String.valueOf(sb);
                        System.out.println(currentDate);
                        et_data.setText(currentDate);
                    }
                });
                builder.show();

                break;
            case R.id.iv_offer:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_property"}, "company_property=?", new String[]{"供应商"}, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_offer.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_offer);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_offer.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;

            case R.id.bt_save:
                save2dataBase();
                break;
        }
    }

    private void save2dataBase() {
        if (!TextUtils.isEmpty(et_card_batch.getText().toString())) {

            ContentValues cv = new ContentValues();
            cv.put("card_batch", et_card_batch.getText().toString());
            cv.put("supplier_id", s2id(et_offer.getText().toString()));
            cv.put("valid_date", et_safe_time.getText().toString());
            cv.put("card_type_id", c2id(et_card_type.getText().toString()));
            cv.put("produce_date", et_data.getText().toString());
            cv.put("card_num", et_card_num.getText().toString());
            cv.put("use_num", et_use_num.getText().toString());
            cv.put("card_batch_valid", 1);
//            cv.put("client_code", 1);
            cv.put("table_id", 2);

            if (card_batch != null) {
                int d = sdb.update("galaxy_card_batch", cv, "card_batch=?", new String[]{card_batch});
                if (d == -1) {
                    Toast.makeText(this, "数据更改失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "数据更改成功", Toast.LENGTH_SHORT).show();
                }
                finish();
                return;
            }
            cv.put("card_batch_id", Integer.valueOf(max) + 1);
            long d = sdb.insert("galaxy_card_batch", null, cv);
            if (d == -1) {
                Toast.makeText(this, "储存数据库失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "储存数据库成功", Toast.LENGTH_SHORT).show();
            }

            finish();
        } else {
            Toast.makeText(CardBatchSettingActivity.this, "卡批次不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private String i2id(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_item", new String[]{"detection_item_id", "detection_item_name"}, "detection_item_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_item_id = cursor2.getString(0);
                return detection_item_id;
            }
        }
        return null;
    }

    private String c2id(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_card_type", new String[]{"card_type", "card_type_id"}, "card_type=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String company_id = cursor2.getString(1);
                return company_id;
            }
        }
        return null;
    }

    /**
     * 名称转换为id
     *
     * @param s
     * @return
     */
    private String s2id(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_id"}, "company_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String company_id = cursor2.getString(1);
                return company_id;
            }
        }
        return null;
    }


}

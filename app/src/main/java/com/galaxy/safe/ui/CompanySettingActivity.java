package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.galaxy.safe.R;
import com.galaxy.safe.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/3/30.
 */
public class CompanySettingActivity extends Activity implements View.OnClickListener {

    private EditText et_company_name;//公司名称
    private EditText et_company_jc;//公司简称
    private EditText et_company_sx;//公司缩写
    private EditText et_company_zz;//公司执照
    private EditText et_www;//公司网址

    private EditText et_company_nature;//属性
    private ImageView iv_company_nature;//
    private EditText et_company_part;//所属监测点
    private ImageView iv_company_part;//
    private EditText et_company_provience;//所属省份
    private ImageView iv_company_provience;//
    private EditText et_company_city;//所属城市
    private ImageView iv_company_city;//
    private EditText et_company_country;//所属县
    private ImageView iv_company_country;//
    private EditText et_company_town;//所属镇
    private ImageView iv_company_town;//
    private EditText et_company_village;//所属村
    private ImageView iv_company_village;//

    private EditText et_company_door;//所属门牌号
    private EditText et_contact;//联系人
    private EditText et_contact_phone;//联系人电话
    private EditText et_contact_mobiephone;//联系人手机电话
    private EditText et_contact_mail;//联系人邮箱

    private Button bt_save;


    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private AnimationSet set;
    private String company_name;//公司名称传过来的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companysetting);
        initView();
        initAnimotion();
        initToolBar();
        initData();
        company_name = getIntent().getStringExtra("company_name");
        if (company_name != null) {
            fillData();
        }
        iv_company_nature.setOnClickListener(this);
        iv_company_part.setOnClickListener(this);
        iv_company_provience.setOnClickListener(this);
        iv_company_city.setOnClickListener(this);
        iv_company_country.setOnClickListener(this);
        iv_company_town.setOnClickListener(this);
        iv_company_village.setOnClickListener(this);
        bt_save.setOnClickListener(this);
    }

    /**
     * 填充数据
     */
    private void fillData() {
        bt_save.setText("更改企业信息");
        cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_short",
                "company_ea", "company_license", "company_property",
                "company_province", "company_city", "company_county"
                , "company_towns", "company_village",
                "company_doorplate", "company_www", "company_contacts",
                "company_telephone", "company_phone", "company_email"}, "company_name=?", new String[]{company_name}, null, null, null);
        while (cursor.moveToNext()) {
            et_company_name.setText(cursor.getString(0));
            et_company_jc.setText(cursor.getString(1));
            et_company_sx.setText(cursor.getString(2));
            et_company_zz.setText(cursor.getString(3));
            et_company_nature.setText(cursor.getString(4));
            et_company_provience.setText(cursor.getString(5));
            et_company_city.setText(cursor.getString(6));
            et_company_country.setText(cursor.getString(7));
            et_company_town.setText(cursor.getString(8));
            et_company_village.setText(cursor.getString(9));
            et_company_door.setText(cursor.getString(10));
            et_www.setText(cursor.getString(11));
            et_contact.setText(cursor.getString(12));
            et_contact_phone.setText(cursor.getString(13));
            et_contact_mobiephone.setText(cursor.getString(14));
            et_contact_mail.setText(cursor.getString(15));

        }


    }

    /**
     * 数据库关闭
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();

    }

    /**
     * 初始化些数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库


    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("企业单位详细信息页面");
        tl_bar.setSubtitle("企业详情");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 动画
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
     * 初始化控件
     */
    private void initView() {
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        et_company_name = (EditText) findViewById(R.id.et_company_name);
        et_company_jc = (EditText) findViewById(R.id.et_company_jc);
        et_company_sx = (EditText) findViewById(R.id.et_company_sx);
        et_company_zz = (EditText) findViewById(R.id.et_company_zz);
        et_www = (EditText) findViewById(R.id.et_www);
        et_company_nature = (EditText) findViewById(R.id.et_company_nature);
        et_company_part = (EditText) findViewById(R.id.et_company_part);
        et_company_provience = (EditText) findViewById(R.id.et_company_provience);
        et_company_city = (EditText) findViewById(R.id.et_company_city);
        et_company_country = (EditText) findViewById(R.id.et_company_country);
        et_company_town = (EditText) findViewById(R.id.et_company_town);
        et_company_village = (EditText) findViewById(R.id.et_company_village);
        et_company_door = (EditText) findViewById(R.id.et_company_door);
        et_contact = (EditText) findViewById(R.id.et_contact);
        et_contact_phone = (EditText) findViewById(R.id.et_contact_phone);
        et_contact_mobiephone = (EditText) findViewById(R.id.et_contact_mobiephone);
        et_contact_mail = (EditText) findViewById(R.id.et_contact_mail);

        iv_company_nature = (ImageView) findViewById(R.id.iv_company_nature);
        iv_company_part = (ImageView) findViewById(R.id.iv_company_part);
        iv_company_provience = (ImageView) findViewById(R.id.iv_company_provience);
        iv_company_city = (ImageView) findViewById(R.id.iv_company_city);
        iv_company_country = (ImageView) findViewById(R.id.iv_company_country);
        iv_company_town = (ImageView) findViewById(R.id.iv_company_town);
        iv_company_village = (ImageView) findViewById(R.id.iv_company_village);

        bt_save = (Button) findViewById(R.id.bt_save);


    }

    private Cursor cursor;
    private List<String> check_list;
    private ArrayAdapter<String> adapter;

    private View popview;
    private PopupWindow popupWindow;
    private ListView lv_content;

    private AlertDialog dialog1;

    String provience_id;
    String city_id;
    String county_id;
    String town_id;

    /**
     * 点击事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_company_nature:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_company_property", new String[]{"company_property"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_company_nature.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_company_nature);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_company_nature.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_company_part:

                break;
            case R.id.iv_company_provience:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_position_province", new String[]{"province_name"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_company_provience.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_company_provience);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_company_provience.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_company_city:

                String provience = et_company_provience.getText().toString();
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);

                if (!TextUtils.isEmpty(provience)) {
                    cursor = sdb.query("galaxy_position_province", new String[]{"province_name", "province_id"}, "province_name=?", new String[]{provience}, null, null, null);
                    while ((cursor.moveToNext())) {
                        provience_id = cursor.getString(1);
                    }
                    cursor = sdb.query("galaxy_position_city", new String[]{"city_name", "province_id"}, "province_id=?", new String[]{provience_id}, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                    lv_content.setAdapter(adapter);
                } else {
                    cursor = sdb.query("galaxy_position_city", new String[]{"city_name"}, null, null, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                    lv_content.setAdapter(adapter);
                }
                popupWindow = new PopupWindow(popview, et_company_city.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_company_city);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_company_city.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_company_country:
                String city = et_company_city.getText().toString();
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);

                if (!TextUtils.isEmpty(city)) {
                    cursor = sdb.query("galaxy_position_city", new String[]{"city_name", "city_id"}, "city_name=?", new String[]{city}, null, null, null);
                    while ((cursor.moveToNext())) {
                        city_id = cursor.getString(1);
                    }
                    cursor = sdb.query("galaxy_position_county", new String[]{"county_name", "city_id"}, "city_id=?", new String[]{city_id}, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                    lv_content.setAdapter(adapter);
                } else {
                    cursor = sdb.query("galaxy_position_county", new String[]{"county_name"}, null, null, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                    lv_content.setAdapter(adapter);
                }
                popupWindow = new PopupWindow(popview, et_company_country.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_company_country);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_company_country.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;

                    }
                });
                break;
            case R.id.iv_company_town:
                String county = et_company_country.getText().toString();
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                if (!TextUtils.isEmpty(county)) {
                    cursor = sdb.query("galaxy_position_county", new String[]{"county_name", "county_id"}, "county_name=?", new String[]{county}, null, null, null);
                    while ((cursor.moveToNext())) {
                        county_id = cursor.getString(1);
                    }
                    cursor = sdb.query("galaxy_position_town", new String[]{"town_name", "county_id"}, "county_id=?", new String[]{county_id}, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                    lv_content.setAdapter(adapter);
                } else {
                    cursor = sdb.query("galaxy_position_town", new String[]{"town_name"}, null, null, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                    lv_content.setAdapter(adapter);
                }
                popupWindow = new PopupWindow(popview, et_company_town.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_company_town);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_company_town.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });

                break;
            case R.id.iv_company_village:
                String town = et_company_town.getText().toString();
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                if (!TextUtils.isEmpty(town)) {
                    cursor = sdb.query("galaxy_position_town", new String[]{"town_name", "town_id"}, "town_name=?", new String[]{town}, null, null, null);
                    while ((cursor.moveToNext())) {
                        town_id = cursor.getString(1);
                    }
                    cursor = sdb.query("galaxy_position_village", new String[]{"village_name", "town_id"}, "town_id=?", new String[]{town_id}, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                    lv_content.setAdapter(adapter);
                } else {
                    cursor = sdb.query("galaxy_position_town", new String[]{"town_name"}, null, null, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                    lv_content.setAdapter(adapter);
                }
                popupWindow = new PopupWindow(popview, et_company_village.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_company_village);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_company_village.setText(check_list.get(position));
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

    /**
     * 保存到数据库
     */
    private void save2dataBase() {

        if (!TextUtils.isEmpty(et_company_name.getText().toString())) {
            ContentValues cv = new ContentValues();
            cv.put("company_name", et_company_name.getText().toString());
            cv.put("company_short", et_company_jc.getText().toString());
            cv.put("company_ea", et_company_sx.getText().toString());
            cv.put("company_license", et_company_zz.getText().toString());
            cv.put("company_property", et_company_nature.getText().toString());
//        cv.put("company_address", et_company_name.getText().toString());
            cv.put("company_nation", "中国");
            cv.put("company_province", et_company_provience.getText().toString());
            cv.put("company_city", et_company_city.getText().toString());
            cv.put("company_county", et_company_country.getText().toString());
            cv.put("company_towns", et_company_town.getText().toString());
            cv.put("company_village", et_company_village.getText().toString());
            cv.put("company_doorplate", et_company_door.getText().toString());
            cv.put("company_www", et_www.getText().toString());
            cv.put("company_contacts", et_contact.getText().toString());
            cv.put("company_telephone", et_contact_phone.getText().toString());
            cv.put("company_phone", et_contact_mobiephone.getText().toString());
            cv.put("company_email", et_contact_mail.getText().toString());

            if (company_name != null) {
                int d = sdb.update("galaxy_detection_company", cv, "company_name=?", new String[]{company_name});
                if (d == -1) {
                    Toast.makeText(this, "数据更改失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "数据更改成功", Toast.LENGTH_SHORT).show();
                }
                finish();
                return;
            }
            long d = sdb.insert("galaxy_detection_company", null, cv);
            if (d == -1) {
                Toast.makeText(this, "储存数据库失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "储存数据库成功", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(this, "公司名称不能为空", Toast.LENGTH_SHORT).show();
        }
    }
}

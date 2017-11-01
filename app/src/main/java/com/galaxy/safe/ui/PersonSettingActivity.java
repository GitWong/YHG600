package com.galaxy.safe.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
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
import com.galaxy.safe.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/4/1.
 */
public class PersonSettingActivity extends Activity implements View.OnClickListener {


    private EditText et_person_no;//人员编号
    private EditText et_person_name;//人员姓名
    private EditText et_person_sex;//人员性别
    private ImageView iv_person_sex;
    private EditText et_person_actor;//人员角色
    private ImageView iv_person_actor;
    private EditText et_zjType;//证件类型
    private ImageView iv_zjType;
    private EditText et_zjNo;//证件号码
    private EditText et_user;//用户名
    private EditText et_person_company;//所在公司
    private ImageView iv_person_company;
    private EditText et_person_phone;//人员电话
    private EditText et_person_mobiephone;//人员手机
    private EditText et_person_password;//密码
    private EditText et_person_mail;//个人邮箱

    private Button bt_save;
    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private AnimationSet set;
    private String person_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personsetting);
        initView();
        initAnimotion();
        initToolBar();
        initData();
        person_name = getIntent().getStringExtra("person_name");
        if (person_name != null) {
            fillData();
        }

        iv_person_sex.setOnClickListener(this);
        iv_person_actor.setOnClickListener(this);
        iv_zjType.setOnClickListener(this);
        iv_person_company.setOnClickListener(this);
        bt_save.setOnClickListener(this);

    }

    private void fillData() {
        bt_save.setText("更改人员信息");
        cursor = sdb.query("galaxy_detection_person", new String[]{"person_bh", "person_name",
                "person_sex", "person_property", "certificate_type",
                "person_certificate", "user_name", "company_id"
                , "person_telephone", "person_passdword",
                "person_email", "person_phone"}, "person_name=?", new String[]{person_name}, null, null, null);

        while (cursor.moveToNext()) {
            et_person_no.setText(cursor.getString(0));
            et_person_name.setText(cursor.getString(1));
            et_person_sex.setText(cursor.getString(2));
            et_person_actor.setText(cursor.getString(3));
            et_zjType.setText(cursor.getString(4));
            et_zjNo.setText(cursor.getString(5));
            et_user.setText(cursor.getString(6));
            et_person_company.setText(toname(cursor.getString(7)));
            et_person_phone.setText(cursor.getString(8));
            et_person_password.setText(cursor.getString(9));
            et_person_mail.setText(cursor.getString(10));
            et_person_mobiephone.setText(cursor.getString(11));

        }


    }

    /**
     * 初始化控件
     */
    private void initView() {
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);

        et_person_no = (EditText) findViewById(R.id.et_person_no);
        et_person_name = (EditText) findViewById(R.id.et_person_name);
        et_person_sex = (EditText) findViewById(R.id.et_person_sex);

        iv_person_sex = (ImageView) findViewById(R.id.iv_person_sex);
        iv_person_actor = (ImageView) findViewById(R.id.iv_person_actor);
        iv_zjType = (ImageView) findViewById(R.id.iv_zjType);
        iv_person_company = (ImageView) findViewById(R.id.iv_person_company);

        et_person_actor = (EditText) findViewById(R.id.et_person_actor);
        et_zjType = (EditText) findViewById(R.id.et_zjType);
        et_zjNo = (EditText) findViewById(R.id.et_zjNo);
        et_user = (EditText) findViewById(R.id.et_user);
        et_person_company = (EditText) findViewById(R.id.et_person_company);
        et_person_phone = (EditText) findViewById(R.id.et_person_phone);
        et_person_mobiephone = (EditText) findViewById(R.id.et_person_mobiephone);
        et_person_password = (EditText) findViewById(R.id.et_person_password);
        et_person_mail = (EditText) findViewById(R.id.et_person_mail);
        bt_save = (Button) findViewById(R.id.bt_save);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        if (!new UserUtils(sdb, PersonSettingActivity.this).getPorperty().equals("系统管理员")) {
            bt_save.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("人员详细信息页面");
        tl_bar.setSubtitle("人员详情");
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
            case R.id.iv_person_sex:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_person_gender", new String[]{"gender"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_person_sex.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_person_sex);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_person_sex.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_person_actor:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_person_property", new String[]{"person_property"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_person_actor.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_person_actor);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_person_actor.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_zjType:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_zhengjian", new String[]{"certificate_type"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_zjType.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_zjType);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_zjType.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });

                break;
            case R.id.iv_person_company:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_company", new String[]{"company_name"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_person_company.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_person_company);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_person_company.setText(check_list.get(position));
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
        if (!TextUtils.isEmpty(et_person_name.getText().toString().trim())) {

            ContentValues cv = new ContentValues();

            cv.put("person_bh", et_person_no.getText().toString());
            cv.put("person_name", et_person_name.getText().toString());
            cv.put("user_name", et_user.getText().toString());
            cv.put("person_passdword", et_person_password.getText().toString());
            cv.put("person_sex", et_person_sex.getText().toString());
            cv.put("company_id", toid(et_person_company.getText().toString()));
            cv.put("person_telephone", et_person_phone.getText().toString());
            cv.put("person_phone", et_person_mobiephone.getText().toString());
            cv.put("person_email", et_person_mail.getText().toString());
            cv.put("person_property", et_person_actor.getText().toString());
            cv.put("certificate_type", et_zjType.getText().toString());
            cv.put("person_certificate", et_zjNo.getText().toString());

            if (person_name != null) {
                int d = sdb.update("galaxy_detection_person", cv, "person_name=?", new String[]{person_name});
                if (d == -1) {
                    Toast.makeText(this, "数据更改失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "数据更改成功", Toast.LENGTH_SHORT).show();
                }
                finish();
                return;
            }
            long d = sdb.insert("galaxy_detection_person", null, cv);
            if (d == -1) {
                Toast.makeText(this, "储存数据库失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "储存数据库成功", Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(this, "人员姓名不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 名称转换为id
     *
     * @param s
     * @return
     */
    private String toid(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_id"}, "company_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String company_id = cursor2.getString(1);
                return company_id;
            }
        }
        return null;

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

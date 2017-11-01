package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.galaxy.safe.R;
import com.galaxy.safe.Service.TaskService;
import com.galaxy.safe.utils.NetUtils;
import com.galaxy.safe.utils.SpUtils;

import java.util.Calendar;

/**
 * Created by Dell on 2016/4/1.
 */
public class StationActivity extends Activity implements View.OnClickListener {

    private EditText et_name;//ip
    private EditText et_bh;//端口号
    private EditText et_person;//访问域名
    private EditText et_phone;//客户代码
    private EditText et_email;//客户代码
    private EditText et_fax;//客户代码

    private Button bt_save;
    private Toolbar tl_bar;


    EditText et_you;
    EditText et_provice;
    EditText et_city;
    EditText et_county;
    EditText et_village;
    EditText et_other;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sation);
        initView();
        initToolBar();
        initDate();
        bt_save.setOnClickListener(this);
    }

    private void initDate() {
        et_name.setText(SpUtils.getString(this, "sname", " "));
        et_bh.setText(SpUtils.getString(this, "sbh", " "));
        et_person.setText(SpUtils.getString(this, "sperson", " "));
        et_phone.setText(SpUtils.getString(this, "sphone", " "));
        et_email.setText(SpUtils.getString(this, "semail", " "));
        et_fax.setText(SpUtils.getString(this, "sfax", " "));
        et_you.setText(SpUtils.getString(this, "syou", " "));
        et_provice.setText(SpUtils.getString(this, "sprovice", " "));
        et_city.setText(SpUtils.getString(this, "scity", " "));
        et_county.setText(SpUtils.getString(this, "scounty", " "));
        et_village.setText(SpUtils.getString(this, "svillage", " "));
        et_other.setText(SpUtils.getString(this, "sother", " "));
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        et_name = (EditText) findViewById(R.id.et_name);
        et_bh = (EditText) findViewById(R.id.et_bh);
        et_person = (EditText) findViewById(R.id.et_person);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_email = (EditText) findViewById(R.id.et_email);
        et_fax = (EditText) findViewById(R.id.et_fax);
        et_you = (EditText) findViewById(R.id.et_you);
        et_provice = (EditText) findViewById(R.id.et_provice);
        et_city = (EditText) findViewById(R.id.et_city);
        et_county = (EditText) findViewById(R.id.et_county);
        et_village = (EditText) findViewById(R.id.et_village);
        et_other = (EditText) findViewById(R.id.et_other);
        bt_save = (Button) findViewById(R.id.bt_save);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("站点信息");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(et_name.getText().toString().trim()) && !TextUtils.isEmpty(et_bh.getText().toString().trim())) {
            SpUtils.putString(this, "sname", et_name.getText().toString().trim());
            SpUtils.putString(this, "sbh", et_bh.getText().toString().trim());
            SpUtils.putString(this, "sperson", et_person.getText().toString().trim());
            SpUtils.putString(this, "sphone", et_phone.getText().toString().trim());
            SpUtils.putString(this, "semail", et_email.getText().toString().trim());
            SpUtils.putString(this, "sfax", et_fax.getText().toString().trim());
            SpUtils.putString(this, "syou", et_you.getText().toString().trim());
            SpUtils.putString(this, "sprovice", et_provice.getText().toString().trim());
            SpUtils.putString(this, "scity", et_city.getText().toString().trim());
            SpUtils.putString(this, "scounty", et_county.getText().toString().trim());
            SpUtils.putString(this, "svillage", et_village.getText().toString().trim());
            SpUtils.putString(this, "sother", et_other.getText().toString().trim());
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "请填写站点名称和编号", Toast.LENGTH_SHORT).show();
        }
    }
}

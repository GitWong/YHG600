package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dell on 2016/4/1.
 */
public class SettingsActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText et_ip;//ip
    private EditText et_port;//端口号
    private EditText et_www;//访问域名
    private EditText et_guest;//客户代码
    private TextView et_pdate;//出厂日期
    private TextView et_deviceno;//仪器编号

    private Switch st_update;//自动更新
    private Switch st_task;//自动接受任务


    private Button bt_save;
    private Toolbar tl_bar;

    private Boolean isupdate;//是否更新程序
    private Boolean istask;//是否接收任务
    EditText et_device;
    EditText et_date;
    ImageView iv_date;
    private AlertDialog dialog1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initView();
        initToolBar();

        String device = SpUtils.getString(this, "device", "");
        String date = SpUtils.getString(this, "date2", "出厂日期");

        if (device.equals("")) {
            AlertDialog.Builder build1 = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.edit_dialog_device, null);
            et_device = (EditText) view.findViewById(R.id.et_report);
            et_date = (EditText) view.findViewById(R.id.et_date);
            iv_date = (ImageView) view.findViewById(R.id.iv_date);
            iv_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View view = View.inflate(SettingsActivity.this, R.layout.date_view, null);
                    final DatePicker datepicker2 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                    final TimePicker timePicker2 = (TimePicker) view.findViewById(R.id.timepicker);
                    timePicker2.setVisibility(View.GONE);
                    final Calendar c2 = Calendar.getInstance();
                    int year2 = c2.get(Calendar.YEAR);
                    int month2 = c2.get(Calendar.MONTH);
                    int day2 = c2.get(Calendar.DAY_OF_MONTH);
                    datepicker2.init(year2, month2, day2, null);

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingsActivity.this);
                    builder2.setView(view);
                    builder2.setTitle("出厂日期选择");

                    builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            int s_year = datepicker2.getYear();
                            int s_month = datepicker2.getMonth();
                            int s_day = datepicker2.getDayOfMonth();

                            int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                            int sDay = Integer.parseInt(String.valueOf(s_day));
                            StringBuffer sb = new StringBuffer();
                            sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                            String currentDate = String.valueOf(sb);
                            System.out.println(currentDate);
                            et_date.setText(currentDate);
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder2.show();
                }
            });

            view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(et_device.getText().toString().trim()) && !TextUtils.isEmpty(et_date.getText().toString().trim())) {
                        String device = et_device.getText().toString().trim();
                        String date = et_date.getText().toString().trim();
                        new NetUtils().insertDevice(SettingsActivity.this, device, "113.864412", "22.568911", SpUtils.getString(SettingsActivity.this, "guest", "AD"), date, "1");
                        et_deviceno.setText(device);
                        et_pdate.setText(date);
                        dialog1.dismiss();
                    } else {
                        Toast.makeText(SettingsActivity.this, "不能为空", Toast.LENGTH_SHORT).show();
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

        et_deviceno.setText(device);
        et_pdate.setText(date);


        String ip = SpUtils.getString(this, "ip", "121.41.38.46");
        String port = SpUtils.getString(this, "port", "9999");
        String www = SpUtils.getString(this, "www", "http://www.baidu.com");
        String guest = SpUtils.getString(this, "guest", "AD");
        et_ip.setText(ip);
        et_port.setText(port);
        et_www.setText(www);
        et_guest.setText(guest);
        st_update.setOnCheckedChangeListener(this);
        st_task.setOnCheckedChangeListener(this);
        isupdate = SpUtils.getboolean(this, "update", true);
        if (isupdate) {
            st_update.setChecked(true);
        } else {
            st_update.setChecked(false);
        }
        istask = SpUtils.getboolean(this, "task", true);
        if (istask) {
            st_task.setChecked(true);
        } else {
            st_task.setChecked(false);
        }

        bt_save.setOnClickListener(this);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        et_ip = (EditText) findViewById(R.id.et_ip);
        et_port = (EditText) findViewById(R.id.et_port);
        et_guest = (EditText) findViewById(R.id.et_guest);
        et_www = (EditText) findViewById(R.id.et_www);
        et_pdate = (TextView) findViewById(R.id.et_pdate);
        et_deviceno = (TextView) findViewById(R.id.et_deviceno);

        st_update = (Switch) findViewById(R.id.st_update);
        st_task = (Switch) findViewById(R.id.st_task);
        bt_save = (Button) findViewById(R.id.bt_save);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("通用设置");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!TextUtils.isEmpty(et_ip.getText().toString()) && !TextUtils.isEmpty(et_port.getText().toString())) {
            SpUtils.putString(this, "ip", et_ip.getText().toString());
            SpUtils.putString(this, "port", et_port.getText().toString());
            SpUtils.putString(this, "www", et_www.getText().toString());
            SpUtils.putString(this, "guest", et_guest.getText().toString());
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "不能为空，先完善数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.st_update:

                if (isChecked) {
                    st_update.setTextColor(this.getResources().getColor(R.color.green));
                    st_update.setText("提示更新开启");
                    SpUtils.putBoolean(this, "update", true);
                } else {
                    st_update.setTextColor(this.getResources().getColor(R.color.red));
                    st_update.setText("提示更新关闭");
                    SpUtils.putBoolean(this, "update", false);
                }
                break;
            case R.id.st_task:
                if (isChecked) {
                    st_task.setTextColor(this.getResources().getColor(R.color.green));
                    st_task.setText("自动接收开启");
                    SpUtils.putBoolean(this, "task", true);
                    this.startService(new Intent(this, TaskService.class));
                } else {
                    st_task.setTextColor(this.getResources().getColor(R.color.red));
                    st_task.setText("自动接收关闭");
                    SpUtils.putBoolean(this, "task", false);
                    this.stopService(new Intent(this, TaskService.class));
                }


                break;
        }
    }
}

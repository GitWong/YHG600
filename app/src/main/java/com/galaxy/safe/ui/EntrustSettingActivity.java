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
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View.MeasureSpec;

import com.galaxy.safe.R;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.utils.SpUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dell on 2016/4/28.
 */
public class EntrustSettingActivity extends Activity implements View.OnClickListener {

    private EditText et_weituoNo;//任务单号
    private EditText et_ednum;//完成数量
    private EditText et_num;//批次数量
    private EditText et_entrustcompany;//发布单位
    private EditText et_startdate;//开始日期
    private EditText et_enddate;//结束日期
    private EditText et_sample_name;//样品名称
    private EditText et_sampling_type;//样品类型
    private EditText et_item;//项目名称
    private Button bt_save;//保存


    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private Cursor cursor;

    private String weituo_number;


    private String TaskNo;
    private String ChouyangType;
    private String Entrust_company;
    private String Entrusted_company;
    private String Entrust_person;
    private String Entrusted_person;
    private String Start_date;
    private String End_date;
    private String Entrust_date;
    private String Invalid_date;
    private String Weituo_des;
    private String Sample_id;
    private String item;
    private String zone;
    private String detection_sample_name;
    private String batch_num;
    private String completed_num;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrustsetting);
        initView();
        initToolBar();

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        weituo_number = getIntent().getStringExtra("weituo_number");
        if (weituo_number != null) {
            et_weituoNo.setFocusable(false);
            et_ednum.setFocusable(false);
            et_num.setFocusable(false);
            et_entrustcompany.setFocusable(false);
            et_startdate.setFocusable(false);
            et_enddate.setFocusable(false);
            et_sample_name.setFocusable(false);
            et_sampling_type.setFocusable(false);
            et_item.setFocusable(false);
            fillData();
        }
        TaskNo = getIntent().getStringExtra("task_bh");
        if (TaskNo != null) {
            et_weituoNo.setText(TaskNo);
            bt_save.setVisibility(View.VISIBLE);
        }

        Entrust_company = getIntent().getStringExtra("publisher");
        if (Entrust_company != null) {
            et_entrustcompany.setText(Entrust_company);
        }

        Start_date = getIntent().getStringExtra("publish_date");
        if (Start_date != null) {
            et_startdate.setText(Start_date);
        }
        End_date = getIntent().getStringExtra("deadline");
        if (End_date != null) {
            et_enddate.setText(End_date);
        }

        Sample_id = getIntent().getStringExtra("detection_sample_type");
        if (Sample_id != null) {
            et_sampling_type.setText(Sample_id);
        }
        detection_sample_name = getIntent().getStringExtra("detection_sample_name");
        if (detection_sample_name != null) {
            et_sample_name.setText(detection_sample_name);
        }
        batch_num = getIntent().getStringExtra("batch_num");
        if (batch_num != null) {
            et_num.setText(batch_num);
        }
        completed_num = getIntent().getStringExtra("completed_num");
        if (completed_num != null) {
            et_ednum.setText(completed_num);
        }
        item = getIntent().getStringExtra("detection_item");
        if (item != null) {
            et_item.setText(item);
        }
        bt_save.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();

    }

    private void fillData() {

//        bt_save.setText("更改任务单信息");
        cursor = sdb.query("galaxy_detection_task", new String[]{"task_valid", "task_bh",
                "detection_item", "detection_sample_type", "detection_sample_name",
                "batch_num", "completed_num", "publish_date"
                , "publisher", "deadline"
        }, "task_bh=?", new String[]{weituo_number}, null, null, null);

        while (cursor.moveToNext()) {
            et_weituoNo.setText(cursor.getString(1));

            et_item.setText(cursor.getString(2));

            et_sampling_type.setText(cursor.getString(3));
            et_sample_name.setText(cursor.getString(4));
            et_num.setText(cursor.getString(5));
            et_ednum.setText(cursor.getString(6));
            et_startdate.setText(cursor.getString(7));
            et_entrustcompany.setText(cursor.getString(8));
            et_enddate.setText(cursor.getString(9));
        }
        cursor.close();
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("任务单详情页面");
        tl_bar.setSubtitle("任务单详情");
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

        bt_save = (Button) findViewById(R.id.bt_save);
        et_weituoNo = (EditText) findViewById(R.id.et_weituoNo);
        et_sampling_type = (EditText) findViewById(R.id.et_sampling_type);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        et_entrustcompany = (EditText) findViewById(R.id.et_entrustcompany);
        et_ednum = (EditText) findViewById(R.id.et_ednum);
        et_num = (EditText) findViewById(R.id.et_num);
        et_startdate = (EditText) findViewById(R.id.et_startdate);
        et_enddate = (EditText) findViewById(R.id.et_enddate);
        et_sample_name = (EditText) findViewById(R.id.et_sample_name);
        et_item = (EditText) findViewById(R.id.et_item);
    }


    @Override
    public void onClick(View view) {

        ContentValues cv = new ContentValues();
        cv.put("task_bh", et_weituoNo.getText().toString().trim());
        cv.put("detection_item", et_item.getText().toString().trim());
        cv.put("detection_sample_type", et_sampling_type.getText().toString().trim());
        cv.put("detection_sample_name", et_sample_name.getText().toString().trim());
        cv.put("batch_num", et_num.getText().toString().trim());
        cv.put("completed_num", et_ednum.getText().toString().trim());
        cv.put("publish_date", et_startdate.getText().toString().trim());
        cv.put("publisher", et_entrustcompany.getText().toString().trim());
        cv.put("deadline", et_enddate.getText().toString().trim());
        cv.put("task_id", 1);
        long d = sdb.insert("galaxy_detection_task", null, cv);
        if (d == -1) {
            Toast.makeText(this, "接收此任务失败", Toast.LENGTH_SHORT).show();
        } else {
            if (TaskNo != null) {
                SpUtils.putString(EntrustSettingActivity.this, "last", et_startdate.getText().toString());
              /*  HttpUtils httpUtils = new HttpUtils();
                RequestParams rp = new RequestParams();
                rp.addQueryStringParameter("no", TaskNo);

                httpUtils.send(HttpRequest.HttpMethod.GET, Constants.ISOK_URL, rp, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Toast.makeText(EntrustSettingActivity.this, "接收此任务成功", Toast.LENGTH_SHORT).show();
                        SpUtils.putString(EntrustSettingActivity.this, "last", et_startdate.getText().toString());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Toast.makeText(EntrustSettingActivity.this, s, Toast.LENGTH_SHORT).show();
                        Toast.makeText(EntrustSettingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }*/
                finish();
            }
        }
    }


   /* *//**
     * 获取控件实际的高度
     *//*
    public int getMeasureHeight() {
        int width = ll_weituo.getMeasuredWidth();  //  由于宽度不会发生变化  宽度的值取出来
        ll_weituo.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;//  让高度包裹内容

        //    参数1  测量控件mode    参数2  大小

        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);  //  mode+size
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(10000, MeasureSpec.AT_MOST);// 我的高度 最大是1000
        // 测量规则 宽度是一个精确的值width, 高度最大是1000,以实际为准
        ll_weituo.measure(widthMeasureSpec, heightMeasureSpec); // 通过该方法重新测量控件

        return ll_weituo.getMeasuredHeight();
    }*/

}

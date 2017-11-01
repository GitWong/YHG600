package com.galaxy.safe.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.galaxy.safe.R;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.utils.JniUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mylhyl.superdialog.SuperDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2016/4/26.
 */
public class RecordDetailActivity extends FragmentActivity implements View.OnClickListener {


    private EditText et_sampling_no;//检测编号
    private EditText et_sample_bh;//样品编号
    private EditText et_item;//样品项目
    private EditText et_report_conclusion;//检测结论
    private EditText et_detection_time;//检测时间
    private EditText et_standard_value;//检测标准
    private EditText et_detection_result;//检测结果
    EditText et_task;//任务来源
    private EditText et_detection_person_id;//检测人员
    private EditText et_device;//检测设备
    private EditText et_company_sampled;//被检企业
    private EditText et_company_produce;//生产企业
    private EditText et_provice;//来源省
    private EditText et_city;//来源城市
    private EditText et_county;//来源区镇
    private EditText et_village;//来源镇
    private EditText et_station;//检测站点
    private EditText et_stationbh;//站点编号
    private EditText et_longitude;//经度
    private EditText et_latitude;//纬度
    private EditText et_lgray;//左灰度值
    private EditText et_cgray;//c灰度值
    private EditText et_mgray;//中间灰度值
    private EditText et_tgray;//t灰度值
    private EditText et_rgray;//右灰度值

    private Button bt_upload;//数据上传
    private Button bt_sample;//打印
    private Button bt_task;//打印
    private Button bt_print;//打印
    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private AnimationSet set;
    private String sampling_no;

    private Cursor cursor;
    private List<String> check_list;
    private ArrayAdapter<String> adapter;

    private View popview;
    private PopupWindow popupWindow;
    private ListView lv_content;

    private String limit_max;//限值最大值
    private String limit_min;//限值最小值

    private String conclusion1;//结论一
    private String conclusion2;//结论2
    private String conclusion3;//结论3


    //    private String publish_ok;//是否发布
//    private String report_print;//打印次数
    private String curve_id;//曲线id
//    private String approved_result;//核准结果

    private String standard_value;//标准值
    private String detection_item_id;//项目
    private String detection_gist;//检测依据
    private String detection_time;//检测时间
    //    private String report_ok;
    private String report_upload;
    private String sampletype;

    private AlertDialog dialog3;
    private String str;
    private String sample;//样本

    private View view2;//对话框ui
    private EditText result;
    private EditText conclusion;
    //    private Boolean isChecked = false;
    //    private SerialPortUtil sp;
    private JniUtils ju;
    private HttpUtils httpUtils;// 网络工具
    private String samplename;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordinfo);
        initView();
        initToolBar();
        initAnimotion();
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库

        sampling_no = getIntent().getStringExtra("sampling_no");

        if (sampling_no != null) {
            fillData();
        }
        bt_upload.setOnClickListener(this);
        bt_print.setOnClickListener(this);
        bt_sample.setOnClickListener(this);
        bt_task.setOnClickListener(this);
    }

    /**
     * 填充数据
     */
    private void fillData() {

        cursor = sdb.query("galaxy_detection_record", new String[]{"detection_bh", "sample_bh",
                "detection_item_name", "limited_min", "detection_result", "detection_conclusion",
                "task_source", "detection_date", "detection_person_name"
                , "company_sampled", "company_produce", "source_province", "source_city",
                "source_county", "source_town", "detection_station_bh", "detection_station", "longitude", "latitude", "device_bh", "upload_valid"}, "detection_bh=?", new String[]{sampling_no}, null, null, null);
        while (cursor.moveToNext()) {

            et_sampling_no.setText(cursor.getString(0));
            et_sample_bh.setText(cursor.getString(1));


            Cursor cursor1 = sdb.query("galaxy_detection_sample", new String[]{"sample_bh", "type_id", "sample_id"

            }, "sample_bh=?", new String[]{cursor.getString(1)}, null, null, null);
            while (cursor1.moveToNext()) {
                sampletype = Id2nameUtils.sampletype2name(cursor1.getString(1), sdb);
                samplename = Id2nameUtils.sample2name(cursor1.getString(2), sdb);
            }
            cursor1.close();

            et_item.setText(cursor.getString(2));
            et_standard_value.setText(cursor.getString(3));
            et_detection_result.setText(cursor.getString(4));
            et_report_conclusion.setText(cursor.getString(5));
            et_task.setText(cursor.getString(6));
            if (cursor.getString(6) == null) {
                et_task.setText("非任务检测");
                bt_task.setVisibility(View.GONE);
            }
            et_detection_time.setText(cursor.getString(7));
            et_detection_person_id.setText(cursor.getString(8));
            et_company_sampled.setText(cursor.getString(9));
            et_company_produce.setText(cursor.getString(10));
            et_provice.setText(cursor.getString(11));
            et_city.setText(cursor.getString(12));
            et_county.setText(cursor.getString(13));
            et_village.setText(cursor.getString(14));
            et_stationbh.setText(cursor.getString(15));
            et_station.setText(cursor.getString(16));
            et_latitude.setText(cursor.getString(18));
            et_longitude.setText(cursor.getString(17));
            et_device.setText(cursor.getString(19));
            if (cursor.getString(20).equals("1")) {
                bt_upload.setVisibility(View.GONE);
            }
        }
        cursor.close();
        cursor = sdb.query("galaxy_detection_recordcb", new String[]{"detection_bh", "gray_c",
                "gray_t", "gray_cb1", "gray_cb2", "gray_tb1", "gray_tb2"}, "detection_bh=?", new String[]{sampling_no}, null, null, null);
        while (cursor.moveToNext()) {
            et_cgray.setText(cursor.getString(1));
            et_tgray.setText(cursor.getString(2));
            et_lgray.setText(cursor.getString(3));
            et_mgray.setText(cursor.getString(4));
            et_rgray.setText(cursor.getString(6));
        }
        cursor.close();
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("检测记录详情页面");
        tl_bar.setSubtitle("检测记录详情");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        et_sampling_no = (EditText) findViewById(R.id.et_sampling_no);
        et_sample_bh = (EditText) findViewById(R.id.et_sample_bh);
        et_item = (EditText) findViewById(R.id.et_item);
        et_report_conclusion = (EditText) findViewById(R.id.et_report_conclusion);
        et_detection_time = (EditText) findViewById(R.id.et_detection_time);
        et_detection_result = (EditText) findViewById(R.id.et_detection_result);
        et_standard_value = (EditText) findViewById(R.id.et_standard_value);
        et_task = (EditText) findViewById(R.id.et_task);
        et_detection_person_id = (EditText) findViewById(R.id.et_detection_person_id);
        et_device = (EditText) findViewById(R.id.et_device);
        et_company_sampled = (EditText) findViewById(R.id.et_company_sampled);
        et_company_produce = (EditText) findViewById(R.id.et_company_produce);
        et_provice = (EditText) findViewById(R.id.et_provice);
        et_city = (EditText) findViewById(R.id.et_city);
        et_county = (EditText) findViewById(R.id.et_county);
        et_village = (EditText) findViewById(R.id.et_village);
        et_station = (EditText) findViewById(R.id.et_station);
        et_stationbh = (EditText) findViewById(R.id.et_stationbh);
        et_longitude = (EditText) findViewById(R.id.et_longitude);
        et_latitude = (EditText) findViewById(R.id.et_latitude);

        et_lgray = (EditText) findViewById(R.id.et_lgray);
        et_cgray = (EditText) findViewById(R.id.et_cgray);
        et_mgray = (EditText) findViewById(R.id.et_mgray);
        et_tgray = (EditText) findViewById(R.id.et_tgray);
        et_rgray = (EditText) findViewById(R.id.et_rgray);
        bt_upload = (Button) findViewById(R.id.bt_upload);
        bt_print = (Button) findViewById(R.id.bt_print);
        bt_sample = (Button) findViewById(R.id.bt_sample);
        bt_task = (Button) findViewById(R.id.bt_task);
    }

    private AlertDialog dialog1;
    private View view;//对话框ui
    private EditText user;//用户名
    private EditText password;//密码
    private String u;//核准人

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bt_sample:
                Intent in = new Intent(this, MainActivity.class);
                in.putExtra("sampleNo", et_sample_bh.getText().toString());
                startActivity(in);
                break;
            case R.id.bt_task:
                Intent intent = new Intent(this, EntrustSettingActivity.class);
                intent.putExtra("weituo_number", et_task.getText().toString());
                startActivity(intent);
                break;

            case R.id.bt_upload:   //上传


                httpUtils = new HttpUtils();

                httpUtils.configResponseTextCharset("UTF-8");

                RequestParams rp = new RequestParams();

                rp.addBodyParameter("upload_valid", "0");
                rp.addBodyParameter("detection_bh", et_sampling_no.getText().toString());
                rp.addBodyParameter("sample_bh", et_sample_bh.getText().toString());
                rp.addBodyParameter("detection_item_name", et_item.getText().toString());
//                rp.addBodyParameter("limited_min", et_standard_value.getText().toString());
                rp.addBodyParameter("type_name", sampletype);
                rp.addBodyParameter("sample_name", samplename);

//                rp.addBodyParameter("limited_unit", "1");
                rp.addBodyParameter("detection_conclusion", et_report_conclusion.getText().toString());
                rp.addBodyParameter("detection_result", et_detection_result.getText().toString());
                rp.addBodyParameter("task_source", et_task.getText().toString());
                rp.addBodyParameter("detection_date", et_detection_time.getText().toString());


                rp.addBodyParameter("detection_person_name", et_detection_person_id.getText().toString());
                rp.addBodyParameter("company_sampled", et_company_sampled.getText().toString());
                rp.addBodyParameter("company_produce", et_company_produce.getText().toString());
                rp.addBodyParameter("source_province", et_provice.getText().toString());
                rp.addBodyParameter("source_city", et_city.getText().toString());
                rp.addBodyParameter("source_county", et_county.getText().toString());
                rp.addBodyParameter("source_town", et_village.getText().toString());
                rp.addBodyParameter("sample_batch", "0");
                rp.addBodyParameter("submit_date", "2017-2-8");
                rp.addBodyParameter("detection_method", "1");
                rp.addBodyParameter("detection_part", "1");
                rp.addBodyParameter("detection_station_bh", et_stationbh.getText().toString());
                rp.addBodyParameter("detection_station", et_station.getText().toString());
                rp.addBodyParameter("longitude", et_longitude.getText().toString());
                rp.addBodyParameter("latitude", et_latitude.getText().toString());
                rp.addBodyParameter("device_bh", et_device.getText().toString());

                httpUtils.send(HttpRequest.HttpMethod.POST, Constants.INSERT_RECORD, rp, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        final DialogFragment build;
                        try {
                            build = new SuperDialog.Builder(RecordDetailActivity.this).setRadius(10)
                                    .setTitle("提示").setMessage("上传成功")
                                    .build();
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    build.dismiss();
                                }
                            }, 2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ContentValues cv4 = new ContentValues();
                        cv4.put("upload_valid", 1);
                        sdb.update("galaxy_detection_record", cv4, "detection_bh=?", new String[]{sampling_no});

//                        Toast.makeText(RecordDetailActivity.this, responseInfo.result.toString(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        final DialogFragment build;

                        build = new SuperDialog.Builder(RecordDetailActivity.this).setRadius(10)
                                .setTitle("提示").setMessage(e.toString())
                                .build();
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                build.dismiss();
                            }
                        }, 2000);
                    }
                });
                if (!et_task.getText().toString().equals("非任务") && !et_task.getText().toString().equals("")) {
                    RequestParams rp1 = new RequestParams();
                    rp1.addQueryStringParameter("task_bh", et_task.getText().toString());

                    httpUtils.send(HttpRequest.HttpMethod.GET, Constants.ISOK_URL, rp1, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {

                            sdb.execSQL("UPDATE galaxy_detection_task\n" +
                                    "SET completed_num = completed_num + 1\n" +
                                    "WHERE\n" +
                                    "\ttask_bh = '" + et_task.getText().toString() + "'");

                            Toast.makeText(RecordDetailActivity.this, "任务已记录", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onFailure(HttpException e, String s) {
                            Toast.makeText(RecordDetailActivity.this, "任务未记录", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                break;

            case R.id.bt_print://打印

                ju = new JniUtils();
                try {
                    ju.print(sampletype.trim().getBytes("GBK"), et_item.getText().toString().trim().getBytes("GBK"), et_detection_time.getText().toString().trim().getBytes("GBK"), et_report_conclusion.getText().toString().trim().getBytes("GBK"), et_detection_person_id.getText().toString().trim().getBytes("GBK"), et_sampling_no.getText().toString().trim().getBytes("GBK"));
                    ContentValues cv3 = new ContentValues();
                    cv3.put("is_printed", 1);
                    sdb.update("galaxy_detection_record", cv3, "detection_bh=?", new String[]{sampling_no});
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 储存服务器
     */
    private void save2dataBase() {

        ContentValues cv1 = new ContentValues();
        cv1.put("detection_result", result.getText().toString());
        cv1.put("detection_conclusion", conclusion.getText().toString());
        sdb.update("galaxy_detection_recordcb", cv1, "sampling_no=?", new String[]{sampling_no});

        ContentValues cv2 = new ContentValues();
        cv2.put("inspector", Id2nameUtils.user2id(u, sdb));
        cv2.put("approved_result", "1");
        cv2.put("approval_time", new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()));
        sdb.update("galaxy_detection_record", cv2, "sampling_no=?", new String[]{sampling_no});

    }

    private boolean isAdm(String u) {

        cursor = sdb.query("galaxy_detection_person", new String[]{"user_name", "person_property"}, "user_name=?", new String[]{u}, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals("系统管理员") || cursor.getString(1).equals("检测员")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断输入数据是否有误
     *
     * @param user_name
     * @param pass
     * @return
     */
    private boolean checkuser(String user_name, String pass) {
        Cursor cursor = sdb.rawQuery("select * from galaxy_detection_person where user_name=?", new String[]{user_name});
        if (cursor.moveToFirst()) {
            int password = cursor.getColumnIndex("person_passdword");
            String passd = cursor.getString(password);
            cursor.close();
            if (pass.equals(passd)) {
                return true;
            }
        }

        return false;
    }
}
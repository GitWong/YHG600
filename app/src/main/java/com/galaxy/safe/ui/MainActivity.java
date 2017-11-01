package com.galaxy.safe.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
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

import com.galaxy.safe.R;

import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.utils.SpUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Dell on 2016/2/25.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;

    private EditText et_sampleNo;//样本编号
    private EditText et_batch;//样本批次


    private String fast;


    private EditText et_sample_desc;//样品描述


    private EditText et_condition;//状态描述
    private EditText et_detection_part;//检测环节
    private ImageView iv_condition;
    private String[] conditiones = {"固态", "半固态", "液态", "气体", "膏霜状", "粉末状", "其他"};

    private EditText et_vaild_date;//到期日期
    private ImageView iv_vaild_date;//


    private EditText et_sample_trademark;//样品商标
    private ImageView iv_sample_trademark;

    private EditText et_company;//生产厂家
    private ImageView iv_company;//生产厂家

    private EditText et_sampleName;//样本名称
    private ImageView iv_sampleName;//样本名称按钮

    private EditText et_sampleNum;//样本数量

    private EditText et_specification_model;//规格型号


    private EditText et_date;//生产日期
    private ImageView iv_date;//生产日期按钮


    private EditText et_submit_date;//送检日期
    private ImageView iv_submit_date;//送检日期按钮

    private EditText et_pickDate;//采样日期
    private ImageView iv_pickDate;//采样日期按钮

    private EditText et_sendCompany;//送检厂家
    private ImageView iv_sendCompany;//送检厂家


    private String detection_no;

    private SQLiteDatabase sdb;
    private AnimationSet set;


    private int No = 1;//默认为1
    private String sampleNo;//样本编号
    private PopupWindow popupWindow;
    private Button bt_save;
    private Toolbar tl_bar;
    private RelativeLayout rl_sample;
    private ImageView sample_arrow;
    private LinearLayout ll_sample;
    private Integer max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String currentDate = SpUtils.getString(this, "date", null);

        if (currentDate != null) {
            No = currentDate.equals(new SimpleDateFormat("yyyyMMdd").format(new Date())) ? Integer.valueOf(SpUtils.getString(this, "No", String.valueOf(1))) : 1;
        }

        initUi();
        initToolBar();
        initAnimotion();
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        initData();


        sampleNo = getIntent().getStringExtra("sampleNo");
        if (sampleNo != null) {
            fillData();
        }

        fast = getIntent().getStringExtra("fast");

        if (fast != null) {
            bt_save.setText("下一步");
        }

//        iv_sealed_valid.setOnClickListener(this);

        iv_company.setOnClickListener(this);
        iv_sampleName.setOnClickListener(this);
        iv_date.setOnClickListener(this);
        iv_pickDate.setOnClickListener(this);
        iv_submit_date.setOnClickListener(this);
        iv_sendCompany.setOnClickListener(this);

        iv_condition.setOnClickListener(this);
        iv_vaild_date.setOnClickListener(this);
        iv_sample_trademark.setOnClickListener(this);
        rl_sample.setOnClickListener(this);
        bt_save.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();

    }

    private void fillData() {

        bt_save.setText("更改样品信息");
        cursor = sdb.query("galaxy_detection_sample", new String[]{"sample_bh_id", "sample_bh", "type_id",
                "sample_id", "sample_batch",
                "company_produce_id", "company_sampled_id", "sample_condition"
                , "detection_part", "sample_brand",
                "sample_size", "sample_num", "produce_date", "valid_date",
                "sampling_date", "sample_desc", "submit_date"}, "sample_bh=?", new String[]{sampleNo}, null, null, null);

        while (cursor.moveToNext()) {

            et_sampleNo.setText(cursor.getString(1));
            et_sampleName.setText(Id2nameUtils.sample2name(cursor.getString(3), sdb));
            et_batch.setText(cursor.getString(4));
            et_company.setText(Id2nameUtils.company2name(cursor.getString(5), sdb));
            et_sendCompany.setText(Id2nameUtils.company2name(cursor.getString(6), sdb));

            if (cursor.getString(7) != null) {
                switch (cursor.getString(7)) {
                    case "1":
                        et_condition.setText("固态");
                        break;
                    case "2":
                        et_condition.setText("半固态");
                        break;
                    case "3":
                        et_condition.setText("液态");
                        break;
                    case "4":
                        et_condition.setText("气体");
                        break;
                    case "5":
                        et_condition.setText("膏霜状");
                        break;
                    case "6":
                        et_condition.setText("粉末状");
                        break;
                    case "7":
                        et_condition.setText("其他");
                        break;
                }
            }
            et_detection_part.setText(cursor.getString(8));
            et_sample_trademark.setText(cursor.getString(9));
            et_specification_model.setText(cursor.getString(10));
            et_sampleNum.setText(cursor.getString(11));
            et_date.setText(cursor.getString(12));
            et_vaild_date.setText(cursor.getString(13));
            et_pickDate.setText(cursor.getString(14));
            et_sample_desc.setText(cursor.getString(15));
            et_submit_date.setText(cursor.getString(16));
        }
        cursor.close();
    }


    private void initData() {

        String currentTime1 = new SimpleDateFormat("yyyyMMdd").format(new Date());
        SpUtils.putString(this, "date", currentTime1);
        StringBuffer sb = new StringBuffer();

        sb.append(currentTime1);
        sb.append(String.format("%03d", No));
        detection_no = sb.toString();
        et_sampleNo.setText(detection_no);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("检测样品详情页面");
        tl_bar.setSubtitle("检测样品详情");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

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
    private void initUi() {

        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        et_sampleNo = (EditText) findViewById(R.id.et_sampleNo);
        et_batch = (EditText) findViewById(R.id.et_batch);


        et_sample_desc = (EditText) findViewById(R.id.et_sample_desc);
        et_detection_part = (EditText) findViewById(R.id.et_detection_part);


        et_condition = (EditText) findViewById(R.id.et_condition);
        iv_condition = (ImageView) findViewById(R.id.iv_condition);

        et_vaild_date = (EditText) findViewById(R.id.et_vaild_date);
        iv_vaild_date = (ImageView) findViewById(R.id.iv_vaild_date);


        et_sample_trademark = (EditText) findViewById(R.id.et_sample_trademark);
        iv_sample_trademark = (ImageView) findViewById(R.id.iv_sample_trademark);

        et_specification_model = (EditText) findViewById(R.id.et_specification_model);

        et_company = (EditText) findViewById(R.id.et_company);
        iv_company = (ImageView) findViewById(R.id.iv_company);

        et_sampleName = (EditText) findViewById(R.id.et_sampleName);
        iv_sampleName = (ImageView) findViewById(R.id.iv_sampleName);

        et_sampleNum = (EditText) findViewById(R.id.et_sampleNum);

        et_date = (EditText) findViewById(R.id.et_date);
        iv_date = (ImageView) findViewById(R.id.iv_date);

        et_pickDate = (EditText) findViewById(R.id.et_pickDate);
        iv_pickDate = (ImageView) findViewById(R.id.iv_pickDate);

        et_submit_date = (EditText) findViewById(R.id.et_submit_date);
        iv_submit_date = (ImageView) findViewById(R.id.iv_submit_date);

        et_sendCompany = (EditText) findViewById(R.id.et_sendCompany);
        iv_sendCompany = (ImageView) findViewById(R.id.iv_sendCompany);


        rl_sample = (RelativeLayout) findViewById(R.id.rl_sample);
        sample_arrow = (ImageView) findViewById(R.id.sample_arrow);
        ll_sample = (LinearLayout) findViewById(R.id.ll_sample);
        ViewGroup.LayoutParams layoutParams = ll_sample.getLayoutParams();
        layoutParams.height = 0;
        ll_sample.setLayoutParams(layoutParams);
        sample_arrow.setImageResource(R.drawable.arrow_down);

        bt_save = (Button) findViewById(R.id.bt_save);
    }

    private Cursor cursor;
    private List<String> check_list;
    private ArrayAdapter<String> adapter;
    private View view;//日期

    private View popview;
    private ListView lv_content;

    private AlertDialog dialog1;

    String provience_id;
    String city_id;
    String county_id;
    boolean flag = false;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.rl_sample:
                int startHeight;
                int targetHeight;
                if (!flag) {    //  展开的动画
                    startHeight = 0;
                    targetHeight = getMeasureHeight();
                    flag = true;

                    ll_sample.getMeasuredHeight();  //  0
                } else {
                    flag = false;
                    startHeight = getMeasureHeight();
                    targetHeight = 0;
                }
                // 值动画
                ValueAnimator animator = ValueAnimator.ofInt(startHeight, targetHeight);
                final ViewGroup.LayoutParams layoutParams = ll_sample.getLayoutParams();
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  // 监听值的变化

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int value = (Integer) animator.getAnimatedValue();// 运行当前时间点的一个值
                        layoutParams.height = value;
                        ll_sample.setLayoutParams(layoutParams);// 刷新界面
                        System.out.println(value);
                    }
                });

                animator.addListener(new Animator.AnimatorListener() {  // 监听动画执行
                    //当动画开始执行的时候调用
                    @Override
                    public void onAnimationStart(Animator arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animator arg0) {

                    }

                    @Override
                    public void onAnimationEnd(Animator arg0) {
                        if (flag) {
                            sample_arrow.setImageResource(R.drawable.arrow_up);
                        } else {
                            sample_arrow.setImageResource(R.drawable.arrow_down);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {

                    }
                });
                animator.setDuration(500);
                animator.start();
                break;

            case R.id.iv_condition://样品状态
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, conditiones);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_condition.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_condition);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_condition.setText(conditiones[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;

            case R.id.iv_sample_trademark://商标

                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                getAlbum.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(getAlbum, IMAGE_CODE);

                break;
          /*  case R.id.iv_check_time:     //送检日期submit date
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker2 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker2 = (TimePicker) view.findViewById(R.id.timepicker);
                final Calendar c2 = Calendar.getInstance();
                int year2 = c2.get(Calendar.YEAR);
                int month2 = c2.get(Calendar.MONTH);
                int day2 = c2.get(Calendar.DAY_OF_MONTH);
                final int second2 = c2.get(Calendar.SECOND);
                datepicker2.init(year2, month2, day2, null);
                timePicker2.setCurrentHour(c2.get(Calendar.HOUR_OF_DAY));
                timePicker2.setCurrentMinute(c2.get(Calendar.MINUTE));
                timePicker2.setIs24HourView(true);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setView(view);
                builder2.setTitle("送检日期选择");

                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker2.getYear();
                        int s_month = datepicker2.getMonth();
                        int s_day = datepicker2.getDayOfMonth();
                        int hour2 = timePicker2.getCurrentHour();
                        int minute2 = timePicker2.getCurrentMinute();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay).append("-").append(hour2).append(":").append(minute2).append(":").append(second2);
                        String currentDate = String.valueOf(sb);
                        System.out.println(currentDate);
                        et_check_time.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder2.show();

                break;
*/
            case R.id.iv_vaild_date:     //到期日期
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker4 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker4 = (TimePicker) view.findViewById(R.id.timepicker);
                final Calendar c4 = Calendar.getInstance();
                int year4 = c4.get(Calendar.YEAR);
                int month4 = c4.get(Calendar.MONTH);
                int day4 = c4.get(Calendar.DAY_OF_MONTH);
                final int second4 = c4.get(Calendar.SECOND);
                datepicker4.init(year4, month4, day4, null);
                timePicker4.setCurrentHour(c4.get(Calendar.HOUR_OF_DAY));
                timePicker4.setCurrentMinute(c4.get(Calendar.MINUTE));
                timePicker4.setIs24HourView(true);

                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setView(view);
                builder4.setTitle("到期日期选择");

                builder4.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker4.getYear();
                        int s_month = datepicker4.getMonth();
                        int s_day = datepicker4.getDayOfMonth();
                        int hour2 = timePicker4.getCurrentHour();
                        int minute2 = timePicker4.getCurrentMinute();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay).append("-").append(hour2).append(":").append(minute2).append(":").append(second4);
                        String currentDate = String.valueOf(sb);
                        et_vaild_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder4.show();

                break;

            case R.id.iv_submit_date:     //送检日期
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker9 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker9 = (TimePicker) view.findViewById(R.id.timepicker);
                final Calendar c9 = Calendar.getInstance();
                int year9 = c9.get(Calendar.YEAR);
                int month9 = c9.get(Calendar.MONTH);
                int day9 = c9.get(Calendar.DAY_OF_MONTH);
                final int second9 = c9.get(Calendar.SECOND);
                datepicker9.init(year9, month9, day9, null);
                timePicker9.setCurrentHour(c9.get(Calendar.HOUR_OF_DAY));
                timePicker9.setCurrentMinute(c9.get(Calendar.MINUTE));
                timePicker9.setIs24HourView(true);

                AlertDialog.Builder builder9 = new AlertDialog.Builder(this);
                builder9.setView(view);
                builder9.setTitle("到期日期选择");

                builder9.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker9.getYear();
                        int s_month = datepicker9.getMonth();
                        int s_day = datepicker9.getDayOfMonth();
                        int hour2 = timePicker9.getCurrentHour();
                        int minute2 = timePicker9.getCurrentMinute();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay).append("-").append(hour2).append(":").append(minute2).append(":").append(second9);
                        String currentDate = String.valueOf(sb);
                        et_submit_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder9.show();

                break;

            case R.id.iv_company://生产厂家
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_property"}, "company_property=?", new String[]{"生产厂家"}, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_company.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_company);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_company.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });

                break;
            case R.id.iv_sendCompany://送检厂家
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_property"}, "company_property=?", new String[]{"送检单位"}, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_sendCompany.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_sendCompany);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_sendCompany.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;

            case R.id.iv_sampleName://样品名称
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_sample_typedetail", new String[]{"sample_name"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_sampleName.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_sampleName);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_sampleName.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });

                break;
          /*  case R.id.iv_sampleType:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_sample_type", new String[]{"type_name"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_check.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_sampleType);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_sampleType.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });

                break;*/

            case R.id.iv_date://生产日期
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker3 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker3 = (TimePicker) view.findViewById(R.id.timepicker);
                final Calendar c3 = Calendar.getInstance();
                int year3 = c3.get(Calendar.YEAR);
                int month3 = c3.get(Calendar.MONTH);
                int day3 = c3.get(Calendar.DAY_OF_MONTH);
                final int second3 = c3.get(Calendar.SECOND);
                datepicker3.init(year3, month3, day3, null);
                timePicker3.setCurrentHour(c3.get(Calendar.HOUR_OF_DAY));
                timePicker3.setCurrentMinute(c3.get(Calendar.MINUTE));
                timePicker3.setIs24HourView(true);

                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setView(view);
                builder3.setTitle("生产日期选择");

                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker3.getYear();
                        int s_month = datepicker3.getMonth();
                        int s_day = datepicker3.getDayOfMonth();
                        int hour2 = timePicker3.getCurrentHour();
                        int minute2 = timePicker3.getCurrentMinute();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay).append("-").append(hour2).append(":").append(minute2).append(":").append(second3);
                        String currentDate = String.valueOf(sb);
                        et_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder3.show();
                break;
            case R.id.iv_pickDate://采样日期
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker1 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker1 = (TimePicker) view.findViewById(R.id.timepicker);
                final Calendar c1 = Calendar.getInstance();
                int year1 = c1.get(Calendar.YEAR);
                int month1 = c1.get(Calendar.MONTH);
                int day1 = c1.get(Calendar.DAY_OF_MONTH);
                final int second1 = c1.get(Calendar.SECOND);

                datepicker1.init(year1, month1, day1, null);
                timePicker1.setCurrentHour(c1.get(Calendar.HOUR_OF_DAY));
                timePicker1.setCurrentMinute(c1.get(Calendar.MINUTE));
                timePicker1.setIs24HourView(true);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setView(view);
                builder1.setTitle("采样日期选择");

                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker1.getYear();
                        int s_month = datepicker1.getMonth();
                        int s_day = datepicker1.getDayOfMonth();
                        int hour2 = timePicker1.getCurrentHour();
                        int minute2 = timePicker1.getCurrentMinute();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay).append("-").append(hour2).append(":").append(minute2).append(":").append(second1);
                        String currentDate = String.valueOf(sb);
                        System.out.println(currentDate);
                        et_pickDate.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.show();
                break;
            case R.id.bt_save:
                save2dataBase();
                break;

        }
    }


    /**
     * 获取控件实际的高度
     */
    public int getMeasureHeight() {
        int width = ll_sample.getMeasuredWidth();  //  由于宽度不会发生变化  宽度的值取出来
        ll_sample.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;//  让高度包裹内容

        //    参数1  测量控件mode    参数2  大小
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);  //  mode+size
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);// 我的高度 最大是1000
        // 测量规则 宽度是一个精确的值width, 高度最大是1000,以实际为准
        ll_sample.measure(widthMeasureSpec, heightMeasureSpec); // 通过该方法重新测量控件

        return ll_sample.getMeasuredHeight();
    }


    /**
     * 存入数据库
     *
     * @param
     */
    private void save2dataBase() {

      /*  if (sampleNo == null) {
            cursor = sdb.query("galaxy_detection_sample", new String[]{"sample_bh_id"}, null, null, null, null, null);
            while (cursor.moveToLast()) {
                max = Integer.valueOf(cursor.getString(0));
            }
        }
*/

        if (!TextUtils.isEmpty(et_sampleName.getText().toString()) && !TextUtils.isEmpty(et_sampleNo.getText().toString())) {

            ContentValues cv = new ContentValues();

            cv.put("sample_valid", 0);


            cv.put("sample_bh", et_sampleNo.getText().toString().trim());
            if (Id2nameUtils.sample2id(et_sampleName.getText().toString().trim(), sdb) != null) {
                Cursor cursor1 = sdb.query("galaxy_sample_typedetail", new String[]{"type_id", "sample_id"}, "sample_id=?", new String[]{Id2nameUtils.sample2id(et_sampleName.getText().toString().trim(), sdb)}, null, null, null);
                while (cursor1.moveToNext()) {
                    cv.put("type_id", cursor1.getString(0));
                }
                cursor1.close();
            }

            cv.put("sample_id", Id2nameUtils.sample2id(et_sampleName.getText().toString().trim(), sdb));
            cv.put("sample_batch", et_batch.getText().toString().trim());

            cv.put("company_produce_id", Id2nameUtils.company2id(et_company.getText().toString().trim(), sdb));
            cv.put("company_sampled_id", Id2nameUtils.company2id(et_sendCompany.getText().toString().trim(), sdb));

            switch (et_condition.getText().toString()) {

                case "固态":
                    cv.put("sample_condition", 1);
                    break;
                case "半固态":
                    cv.put("sample_condition", 2);
                    break;
                case "液态":
                    cv.put("sample_condition", 3);
                    break;
                case "气体":
                    cv.put("sample_condition", 4);
                    break;
                case "膏霜状":
                    cv.put("sample_condition", 5);
                    break;
                case "粉末状":
                    cv.put("sample_condition", 6);
                    break;
                case "其他":
                    cv.put("sample_condition", 7);
                    break;
            }


            cv.put("detection_part", et_detection_part.getText().toString().trim());

            cv.put("sample_brand", 0);

            cv.put("sample_num", et_sampleNum.getText().toString().trim());
            cv.put("sample_size", et_specification_model.getText().toString().trim());

            cv.put("produce_date", et_date.getText().toString().trim());
            cv.put("valid_date", et_vaild_date.getText().toString().trim());
            cv.put("sampling_date", et_pickDate.getText().toString().trim());
            cv.put("sample_desc", et_sample_desc.getText().toString().trim());
            cv.put("submit_date", et_submit_date.getText().toString().trim());

            if (sampleNo != null) {
                int d = sdb.update("galaxy_detection_sample", cv, "sample_bh=?", new String[]{sampleNo});
                if (d == -1) {
                    Toast.makeText(this, "数据更改失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "数据更改成功", Toast.LENGTH_SHORT).show();
                }
                finish();
                return;
            } else {
//                cv.put("sample_bh_id", max+1);//稍后再搞定
                long d = sdb.insert("galaxy_detection_sample", null, cv);
                if (d == -1) {
                    Toast.makeText(this, "储存数据库失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "储存数据库成功", Toast.LENGTH_SHORT).show();
                    SpUtils.putString(this, "No", String.valueOf(No + 1));
                }
                if (fast != null) {
                    Intent data = new Intent();
                    data.putExtra("sampleNo", et_sampleNo.getText().toString());
                    setResult(3, data);

                }

                finish();
            }

        } else {
            Snackbar.make(ll_sample, "样品名称编号不能为空", Snackbar.LENGTH_SHORT).show();
        }
    }


}


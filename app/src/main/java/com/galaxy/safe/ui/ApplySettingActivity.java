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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Dell on 2016/4/29.
 */
public class ApplySettingActivity extends Activity implements View.OnClickListener {


    private PopupWindow popupWindow;
    private Button bt_save;//保存

    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private AnimationSet set;

    private EditText et_sample_bh;
    private ImageView iv_sample_bh;//样本编号

    private ImageView iv_add;

    private EditText et_testing_no;//检测单号
    private EditText et_tasksource;//任务来源
    private EditText et_mailing_address;//寄 送样地址
    private EditText et_sampling_person1;//抽样人
    private EditText et_sampling_person2;
    private EditText et_sampling_person3;
    private EditText et_examinedcompany_signature;//被抽样单位签字
    private EditText et_producecompany_signature;//s生产单位签字
    private EditText et_remark;//备注
    private EditText et_report_num;//报告数量
    private EditText et_explaination_remark;//其他说明
    private EditText et_total_cost;//总费用
    private EditText et_detection_station_person;//承检单位签名
    private EditText et_client_name;//委托方签名
    private EditText et_fengsamplecheck_person;//检测封样人员

    private EditText et_weituoNo;//委托单号
    private ImageView iv_weituoNo;

    private EditText et_taskType;//任务类型
    private ImageView iv_taskType;

    private String[] tasktypes = {"监督抽查", "风险监测", "市场开办者自检", "委托", "仲裁", "比对", "练兵", "其他"};

    private EditText et_examined_company;//被抽样单位信息
    private ImageView iv_examined_company;

    private EditText et_commission_type;//委托方类型
    private ImageView iv_commission_type;

    private String[] commission_types = {"单位", "个人"};

    private EditText et_commission_company;//委托方信息
    private ImageView iv_commission_company;

    private EditText et_detection_part;//受检环节
    private ImageView iv_detection_part;

    private EditText et_sampling_company;//抽样单位信息
    private ImageView iv_sampling_company;

    private EditText et_sampling_deadline;//送样截止日期
    private ImageView iv_sampling_deadline;

    private EditText et_sampling_date;//抽样日期
    private ImageView iv_sampling_date;

    private EditText et_examinedcompany_opinion;//被抽样单位意见
    private ImageView iv_examinedcompany_opinion;

    private String[] examinedcompany_opinions = {"无异议", "有异议"};

    private EditText et_examinedcompany_date;//被抽样单位签名日期
    private ImageView iv_examinedcompany_date;

    private EditText et_producecompany_opinion;//生产单位意见
    private ImageView iv_producecompany_opinion;

    private EditText et_producecompany_date;//生产单位签名日期
    private ImageView iv_producecompany_date;

    private EditText et_conclusion_judgment;//结论判定
    private ImageView iv_conclusion_judgment;

    private String[] judgmentes = {"做出判断", "出具数据"};

    private EditText et_report_delivery_mode;//交付方式
    private ImageView iv_report_delivery_mode;

    private String[] report_delivery_modes = {"代邮", "自取", "委托代取", "传真", "其他"};

    private EditText et_report_delivery_date;//交付时间
    private ImageView iv_report_delivery_date;

    private EditText et_differential_solution;//差异解决途径
    private ImageView iv_differential_solution;

    private String[] differential_solutiones = {"书面", "口头", "电话", "传真"};

    private EditText et_retained_accessory;//是否保留副本
    private ImageView iv_retained_accessory;

    private String[] retained_accessorys = {"保留", "不保留"};

    private EditText et_paymen_method;//付费方式
    private ImageView iv_paymen_method;

    private String[] paymen_methodes = {"现金", "月结", "协议", "担保"};

    private EditText et_sample_collection;//收样人


    private EditText et_sample_collection_date;//收样日期
    private ImageView iv_sample_collection_date;

    private EditText et_detection_station_name;//承检单位信息
    private ImageView iv_detection_station_name;

    private EditText et_detection_station_date;//承检单位签名日期
    private ImageView iv_detection_station_date;

    private EditText et_client_detection_date;//c委托检测日期
    private ImageView iv_client_detection_date;

    private EditText et_detection_item_id;//检测项目
    private ImageView iv_detection_item_id;

    private EditText et_detection_method_id;//检测方法
    private ImageView iv_detection_method_id;

    private EditText et_detection_gist_id;//检测依据
    private ImageView iv_detection_gist_id;
    private String testing_no;//抽样单号

    private RelativeLayout rl_apply;
    private ImageView apply_arrow;
    private LinearLayout ll_apply;

    private String taskNO;
    private String unTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applysetting);
        initView();
        initToolBar();
        initAnimotion();
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库


        taskNO = getIntent().getStringExtra("taskNo");
        if (taskNO != null) {
            et_testing_no.setText(taskNO);
            et_weituoNo.setText(taskNO);
            bt_save.setText("下一步");
        } else {
            et_testing_no.setText(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
        }

        testing_no = getIntent().getStringExtra("testing_no");
        if (testing_no != null) {
            fillData();
        }


        iv_sample_bh.setOnClickListener(this);
        iv_weituoNo.setOnClickListener(this);
        iv_taskType.setOnClickListener(this);
        iv_examined_company.setOnClickListener(this);
        iv_commission_type.setOnClickListener(this);
        iv_commission_company.setOnClickListener(this);
        iv_detection_part.setOnClickListener(this);
        iv_sampling_company.setOnClickListener(this);
        iv_sampling_deadline.setOnClickListener(this);
        iv_examinedcompany_opinion.setOnClickListener(this);
        iv_sampling_date.setOnClickListener(this);
        iv_examinedcompany_date.setOnClickListener(this);
        iv_producecompany_opinion.setOnClickListener(this);
        iv_producecompany_date.setOnClickListener(this);
        iv_conclusion_judgment.setOnClickListener(this);
        iv_report_delivery_mode.setOnClickListener(this);
        iv_report_delivery_date.setOnClickListener(this);
        iv_differential_solution.setOnClickListener(this);
        iv_retained_accessory.setOnClickListener(this);
        iv_paymen_method.setOnClickListener(this);
        iv_paymen_method.setOnClickListener(this);
        iv_sample_collection_date.setOnClickListener(this);
        iv_detection_station_name.setOnClickListener(this);
        iv_detection_station_date.setOnClickListener(this);
        iv_client_detection_date.setOnClickListener(this);
        iv_detection_item_id.setOnClickListener(this);
        iv_detection_method_id.setOnClickListener(this);
        iv_detection_gist_id.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        rl_apply.setOnClickListener(this);

        unTask = getIntent().getStringExtra("unTask");
        if (unTask != null) {
            et_weituoNo.setText("无");
            iv_weituoNo.setClickable(false);
            bt_save.setText("下一步");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
    }

    /**
     * 如果填充数据
     */
    private void fillData() {

        bt_save.setText("更改检测单信息");

        cursor = sdb.query("galaxy_application_form", new String[]{"testing_no", "weituo_number",
                "tasks_source", "tasks_type", "examined_company",
                "commission_type", "commission_company", "detection_part"
                , "sample_bh", "sampling_company"
                , "mailing_address", "sampling_deadline", "sampling_person1", "sampling_person2", "sampling_person3",
                "sampling_date", "examinedcompany_opinion", "examinedcompany_signature", "examinedcompany_date",
                "producecompany_opinion", "producecompany_signature", "producecompany_date", "remark",
                "conclusion_judgment", "report_delivery_mode", "report_delivery_date", "report_num",
                "differential_solution", "retained_accessory", "explaination_remark", "total_cost",
                "paymen_method", "sample_collection", "sample_collection_date", "detection_station_name",
                "detection_station_person", "detection_station_date", "client_name", "client_detection_date",
                "fengsamplecheck_person"}, "testing_no=?", new String[]{testing_no}, null, null, null);
        while (cursor.moveToNext()) {

            et_testing_no.setText(cursor.getString(0));
            et_weituoNo.setText(cursor.getString(1));
            et_tasksource.setText(cursor.getString(2));
            if (cursor.getString(3) != null) {
                switch (cursor.getString(3)) {

                    case "1":
                        et_taskType.setText("监督抽查");
                        break;
                    case "2":
                        et_taskType.setText("风险监测");
                        break;
                    case "3":
                        et_taskType.setText("市场开办者自检");
                        break;
                    case "4":
                        et_taskType.setText("委托");
                        break;
                    case "5":
                        et_taskType.setText("仲裁");
                        break;
                    case "6":
                        et_taskType.setText("比对");
                        break;
                    case "7":
                        et_taskType.setText("练兵");
                        break;
                    case "8":
                        et_taskType.setText("其他");
                        break;
                }
            }

            et_examined_company.setText(Id2nameUtils.company2name(cursor.getString(4), sdb));
            if (cursor.getString(5) != null) {
                switch (cursor.getString(5)) {

                    case "1":
                        et_commission_type.setText("单位");
                        break;
                    case "0":
                        et_commission_type.setText("个人");
                        break;
                }
            }
            et_commission_company.setText(Id2nameUtils.company2name(cursor.getString(6), sdb));
            et_detection_part.setText(Id2nameUtils.part2name(cursor.getString(7), sdb));
            et_sample_bh.setText(cursor.getString(8));

            et_sampling_company.setText(Id2nameUtils.company2name(cursor.getString(9), sdb));
            et_mailing_address.setText(cursor.getString(10));
            et_sampling_deadline.setText(cursor.getString(11));

            et_sampling_person1.setText(cursor.getString(12));
            et_sampling_person2.setText(cursor.getString(13));
            et_sampling_person3.setText(cursor.getString(14));
            et_sampling_date.setText(cursor.getString(15));

            if (cursor.getString(16) != null) {
                switch (cursor.getString(16)) {

                    case "1":
                        et_examinedcompany_opinion.setText("无异议");
                        break;
                    case "0":
                        et_examinedcompany_opinion.setText("有异议");
                        break;
                }
            }
            et_examinedcompany_signature.setText(cursor.getString(17));
            et_examinedcompany_date.setText(cursor.getString(18));
            if (cursor.getString(19) != null) {
                switch (cursor.getString(19)) {
                    case "1":
                        et_producecompany_opinion.setText("无异议");
                        break;
                    case "0":
                        et_producecompany_opinion.setText("有异议");
                        break;
                }
            }


            et_producecompany_signature.setText(cursor.getString(20));
            et_producecompany_date.setText(cursor.getString(21));
            et_remark.setText(cursor.getString(22));
            if (cursor.getString(23) != null) {
                switch (cursor.getString(23)) {
                    case "1":
                        et_conclusion_judgment.setText("做出判断");
                        break;
                    case "0":
                        et_conclusion_judgment.setText("出具数据");
                        break;
                }
            }
            if (cursor.getString(24) != null) {
                switch (cursor.getString(24)) {

                    case "1":
                        et_report_delivery_mode.setText("代邮");
                        break;
                    case "2":
                        et_report_delivery_mode.setText("自取");
                        break;
                    case "3":
                        et_report_delivery_mode.setText("委托代取");
                        break;
                    case "4":
                        et_report_delivery_mode.setText("传真");
                        break;
                    case "5":
                        et_report_delivery_mode.setText("其他");
                        break;

                }
            }
            et_report_delivery_date.setText(cursor.getString(25));
            et_report_num.setText(cursor.getString(26));

            if (cursor.getString(27) != null) {
                switch (cursor.getString(27)) {

                    case "1":
                        et_differential_solution.setText("书面");
                        break;
                    case "2":
                        et_differential_solution.setText("口头");
                        break;
                    case "3":
                        et_differential_solution.setText("电话");
                        break;
                    case "4":
                        et_differential_solution.setText("传真");
                        break;
                }
            }
            if (cursor.getString(28) != null) {
                switch (cursor.getString(28)) {
                    case "1":
                        et_retained_accessory.setText("保留");
                        break;
                    case "0":
                        et_retained_accessory.setText("不保留");
                        break;
                }
            }
            et_explaination_remark.setText(cursor.getString(29));
            et_total_cost.setText(cursor.getString(30));
            if (cursor.getString(31) != null) {
                switch (cursor.getString(31)) {

                    case "1":
                        et_paymen_method.setText("现金");
                        break;
                    case "2":
                        et_paymen_method.setText("月结");
                        break;
                    case "3":
                        et_paymen_method.setText("协议");
                        break;
                    case "4":
                        et_paymen_method.setText("担保");
                        break;
                }
            }
            et_sample_collection.setText(cursor.getString(32));
            et_sample_collection_date.setText(cursor.getString(33));
            et_detection_station_name.setText(Id2nameUtils.company2name(cursor.getString(34), sdb));
            et_detection_station_person.setText(cursor.getString(35));
            et_detection_station_date.setText(cursor.getString(36));
            et_client_name.setText(cursor.getString(37));
            et_client_detection_date.setText(cursor.getString(38));
            et_fengsamplecheck_person.setText(cursor.getString(39));

            Cursor cursor1 = sdb.query("galaxy_application_formcb", new String[]{"testing_no", "detection_method_id", "detection_gist", "detection_item_id"
            }, "testing_no=?", new String[]{testing_no}, null, null, null);
            while (cursor1.moveToNext()) {
                et_detection_item_id.setText(Id2nameUtils.item2name(cursor1.getString(3), sdb));
                et_detection_method_id.setText(cursor1.getString(1));
            }
            cursor1.close();


        }
    }

    /**
     * 初始化toolbar
     */

    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("检测单详情页面");
        tl_bar.setSubtitle("检测单详情");
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
        bt_save = (Button) findViewById(R.id.bt_save);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        et_sample_bh = (EditText) findViewById(R.id.et_sample_bh);
        iv_sample_bh = (ImageView) findViewById(R.id.iv_sample_bh);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        et_testing_no = (EditText) findViewById(R.id.et_testing_no);
        et_tasksource = (EditText) findViewById(R.id.et_tasksource);
        et_mailing_address = (EditText) findViewById(R.id.et_mailing_address);
        et_sampling_person1 = (EditText) findViewById(R.id.et_sampling_person1);
        et_sampling_person2 = (EditText) findViewById(R.id.et_sampling_person2);
        et_sampling_person3 = (EditText) findViewById(R.id.et_sampling_person3);
        et_examinedcompany_signature = (EditText) findViewById(R.id.et_examinedcompany_signature);
        et_producecompany_signature = (EditText) findViewById(R.id.et_producecompany_signature);
        et_remark = (EditText) findViewById(R.id.et_remark);
        et_report_num = (EditText) findViewById(R.id.et_report_num);
        et_explaination_remark = (EditText) findViewById(R.id.et_explaination_remark);
        et_total_cost = (EditText) findViewById(R.id.et_total_cost);
        et_detection_station_person = (EditText) findViewById(R.id.et_detection_station_person);
        et_client_name = (EditText) findViewById(R.id.et_client_name);
        et_fengsamplecheck_person = (EditText) findViewById(R.id.et_fengsamplecheck_person);


        et_weituoNo = (EditText) findViewById(R.id.et_weituoNo);
        iv_weituoNo = (ImageView) findViewById(R.id.iv_weituoNo);

        et_detection_item_id = (EditText) findViewById(R.id.et_detection_item_id);
        iv_detection_item_id = (ImageView) findViewById(R.id.iv_detection_item_id);

        et_detection_method_id = (EditText) findViewById(R.id.et_detection_method_id);
        iv_detection_method_id = (ImageView) findViewById(R.id.iv_detection_method_id);

        et_detection_gist_id = (EditText) findViewById(R.id.et_detection_gist_id);
        iv_detection_gist_id = (ImageView) findViewById(R.id.iv_detection_gist_id);

        et_taskType = (EditText) findViewById(R.id.et_taskType);
        iv_taskType = (ImageView) findViewById(R.id.iv_taskType);

        et_examined_company = (EditText) findViewById(R.id.et_examined_company);
        iv_examined_company = (ImageView) findViewById(R.id.iv_examined_company);

        et_commission_type = (EditText) findViewById(R.id.et_commission_type);
        iv_commission_type = (ImageView) findViewById(R.id.iv_commission_type);

        et_commission_company = (EditText) findViewById(R.id.et_commission_company);
        iv_commission_company = (ImageView) findViewById(R.id.iv_commission_company);

        et_detection_part = (EditText) findViewById(R.id.et_detection_part);
        iv_detection_part = (ImageView) findViewById(R.id.iv_detection_part);

        et_sampling_company = (EditText) findViewById(R.id.et_sampling_company);
        iv_sampling_company = (ImageView) findViewById(R.id.iv_sampling_company);

        et_sampling_deadline = (EditText) findViewById(R.id.et_sampling_deadline);
        iv_sampling_deadline = (ImageView) findViewById(R.id.iv_sampling_deadline);

        et_sampling_date = (EditText) findViewById(R.id.et_sampling_date);
        iv_sampling_date = (ImageView) findViewById(R.id.iv_sampling_date);

        et_examinedcompany_opinion = (EditText) findViewById(R.id.et_examinedcompany_opinion);
        iv_examinedcompany_opinion = (ImageView) findViewById(R.id.iv_examinedcompany_opinion);

        et_examinedcompany_date = (EditText) findViewById(R.id.et_examinedcompany_date);
        iv_examinedcompany_date = (ImageView) findViewById(R.id.iv_examinedcompany_date);

        et_producecompany_opinion = (EditText) findViewById(R.id.et_producecompany_opinion);
        iv_producecompany_opinion = (ImageView) findViewById(R.id.iv_producecompany_opinion);

        et_producecompany_date = (EditText) findViewById(R.id.et_producecompany_date);
        iv_producecompany_date = (ImageView) findViewById(R.id.iv_producecompany_date);

        et_conclusion_judgment = (EditText) findViewById(R.id.et_conclusion_judgment);
        iv_conclusion_judgment = (ImageView) findViewById(R.id.iv_conclusion_judgment);

        et_report_delivery_mode = (EditText) findViewById(R.id.et_report_delivery_mode);
        iv_report_delivery_mode = (ImageView) findViewById(R.id.iv_report_delivery_mode);

        et_report_delivery_date = (EditText) findViewById(R.id.et_report_delivery_date);
        iv_report_delivery_date = (ImageView) findViewById(R.id.iv_report_delivery_date);

        et_differential_solution = (EditText) findViewById(R.id.et_differential_solution);
        iv_differential_solution = (ImageView) findViewById(R.id.iv_differential_solution);

        et_retained_accessory = (EditText) findViewById(R.id.et_retained_accessory);
        iv_retained_accessory = (ImageView) findViewById(R.id.iv_retained_accessory);

        et_paymen_method = (EditText) findViewById(R.id.et_paymen_method);
        iv_paymen_method = (ImageView) findViewById(R.id.iv_paymen_method);

        et_sample_collection = (EditText) findViewById(R.id.et_sample_collection);


        et_sample_collection_date = (EditText) findViewById(R.id.et_sample_collection_date);
        iv_sample_collection_date = (ImageView) findViewById(R.id.iv_sample_collection_date);

        et_detection_station_name = (EditText) findViewById(R.id.et_detection_station_name);
        iv_detection_station_name = (ImageView) findViewById(R.id.iv_detection_station_name);

        et_detection_station_date = (EditText) findViewById(R.id.et_detection_station_date);
        iv_detection_station_date = (ImageView) findViewById(R.id.iv_detection_station_date);

        et_client_detection_date = (EditText) findViewById(R.id.et_client_detection_date);
        iv_client_detection_date = (ImageView) findViewById(R.id.iv_client_detection_date);

        rl_apply = (RelativeLayout) findViewById(R.id.rl_apply);
        apply_arrow = (ImageView) findViewById(R.id.apply_arrow);
        ll_apply = (LinearLayout) findViewById(R.id.ll_apply);
        ViewGroup.LayoutParams layoutParams = ll_apply.getLayoutParams();
        layoutParams.height = 0;
        ll_apply.setLayoutParams(layoutParams);
        apply_arrow.setImageResource(R.drawable.arrow_down);


        et_weituoNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String et = s.toString();
                if (!et.equals("无") || et != null) {

                    Cursor cs = sdb.query("galaxy_task_delegationcb", new String[]{"weituo_number", "detection_item_id"}, "weituo_number=?", new String[]{et_weituoNo.getText().toString()}, null, null, null);
                    while (cs.moveToNext()) {
                        et_detection_item_id.setText(Id2nameUtils.item2name(cs.getString(1), sdb));
                    }
                } else {
                    et_detection_item_id.setText("");
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 3 && requestCode == 33) {
            String sampleNo = data.getStringExtra("sampleNo");
            et_sample_bh.setText(sampleNo);

        }
    }

    private AlertDialog dialog1;

    private Cursor cursor;
    private List<String> check_list;
    private ArrayAdapter<String> adapter;

    private View popview;
    private ListView lv_content;
    private View view;//日期
    boolean flag = false;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_apply:
                int startHeight;
                int targetHeight;
                if (!flag) {    //  展开的动画
                    startHeight = 0;
                    targetHeight = getMeasureHeight();

                    flag = true;
                    //safe_content.setVisibility(View.VISIBLE);
                    ll_apply.getMeasuredHeight();  //  0
                } else {
                    flag = false;
                    //safe_content.setVisibility(View.GONE);
                    startHeight = getMeasureHeight();
                    targetHeight = 0;
                }
                // 值动画
                ValueAnimator animator = ValueAnimator.ofInt(startHeight, targetHeight);
                final ViewGroup.LayoutParams layoutParams = ll_apply.getLayoutParams();
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  // 监听值的变化

                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        int value = (Integer) animator.getAnimatedValue();// 运行当前时间点的一个值
                        layoutParams.height = value;
                        ll_apply.setLayoutParams(layoutParams);// 刷新界面
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
                            apply_arrow.setImageResource(R.drawable.arrow_up);
                        } else {
                            apply_arrow.setImageResource(R.drawable.arrow_down);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator arg0) {

                    }
                });
                animator.setDuration(500);
                animator.start();
                break;
            case R.id.iv_sample_bh:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_sample", new String[]{"sample_bh"}, null, null, null, null, "sample_bh desc");

                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_sample_bh.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_sample_bh);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_sample_bh.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_add:

                if (!TextUtils.isEmpty(et_weituoNo.getText().toString())) {
                    Intent i = new Intent(this, MainActivity.class);
                    i.putExtra("weituoNo", et_weituoNo.getText().toString());
                    startActivityForResult(i, 33);
                } else {
                    Toast.makeText(this, "请先填写任务单号", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_taskType:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tasktypes);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_taskType.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_taskType);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_taskType.setText(tasktypes[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });

                break;
            case R.id.iv_weituoNo:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_task_delegation", new String[]{"weituo_number", "use_valid"}, "use_valid=?", new String[]{"0"}, null, null, "weituo_number desc");
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_weituoNo.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_weituoNo);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_weituoNo.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_examined_company:

                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_property"}, "company_property!=?", new String[]{"监管单位"}, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_examined_company.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_examined_company);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_examined_company.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });

                break;
            case R.id.iv_commission_type:

                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, commission_types);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_commission_type.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_commission_type);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_commission_type.setText(commission_types[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_commission_company:

                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_property"}, "company_property=?", new String[]{"个人"}, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_commission_company.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_commission_company);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_commission_company.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_detection_part:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_part", new String[]{"detection_part"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_detection_part.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_detection_part);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_detection_part.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_sampling_company:
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
                popupWindow = new PopupWindow(popview, et_sampling_company.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_sampling_company);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_sampling_company.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_sampling_deadline:

                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker2 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker2 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker2.setVisibility(View.GONE);
                final Calendar c2 = Calendar.getInstance();
                int year2 = c2.get(Calendar.YEAR);
                int month2 = c2.get(Calendar.MONTH);
                int day2 = c2.get(Calendar.DAY_OF_MONTH);
                datepicker2.init(year2, month2, day2, null);

                AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
                builder2.setView(view);
                builder2.setTitle("送样截止日期选择");

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
                        et_sampling_deadline.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder2.show();

                break;
            case R.id.iv_sampling_date:
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker4 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker4 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker4.setVisibility(View.GONE);
                final Calendar c4 = Calendar.getInstance();
                int year4 = c4.get(Calendar.YEAR);
                int month4 = c4.get(Calendar.MONTH);
                int day4 = c4.get(Calendar.DAY_OF_MONTH);

                datepicker4.init(year4, month4, day4, null);
                AlertDialog.Builder builder4 = new AlertDialog.Builder(this);
                builder4.setView(view);
                builder4.setTitle("抽样日期选择");
                builder4.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker4.getYear();
                        int s_month = datepicker4.getMonth();
                        int s_day = datepicker4.getDayOfMonth();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                        String currentDate = String.valueOf(sb);
                        et_sampling_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder4.show();
                break;

            case R.id.iv_examinedcompany_opinion:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examinedcompany_opinions);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_examinedcompany_opinion.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_examinedcompany_opinion);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_examinedcompany_opinion.setText(examinedcompany_opinions[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_examinedcompany_date://被抽样方签名日期
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker3 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker3 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker3.setVisibility(View.GONE);
                final Calendar c3 = Calendar.getInstance();
                int year3 = c3.get(Calendar.YEAR);
                int month3 = c3.get(Calendar.MONTH);
                int day3 = c3.get(Calendar.DAY_OF_MONTH);

                datepicker3.init(year3, month3, day3, null);
                AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
                builder3.setView(view);
                builder3.setTitle("被抽样方签字日期选择");

                builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker3.getYear();
                        int s_month = datepicker3.getMonth();
                        int s_day = datepicker3.getDayOfMonth();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                        String currentDate = String.valueOf(sb);
                        et_examinedcompany_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder3.show();
                break;
            case R.id.iv_producecompany_opinion:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examinedcompany_opinions);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_producecompany_opinion.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_producecompany_opinion);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_producecompany_opinion.setText(examinedcompany_opinions[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;

            case R.id.iv_producecompany_date:

                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker1 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker1 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker1.setVisibility(View.GONE);
                final Calendar c1 = Calendar.getInstance();
                int year1 = c1.get(Calendar.YEAR);
                int month1 = c1.get(Calendar.MONTH);
                int day1 = c1.get(Calendar.DAY_OF_MONTH);

                datepicker1.init(year1, month1, day1, null);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setView(view);
                builder1.setTitle("生产方签字日期选择");

                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker1.getYear();
                        int s_month = datepicker1.getMonth();
                        int s_day = datepicker1.getDayOfMonth();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                        String currentDate = String.valueOf(sb);
                        System.out.println(currentDate);
                        et_producecompany_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.show();
                break;
            case R.id.iv_conclusion_judgment:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, judgmentes);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_conclusion_judgment.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_conclusion_judgment);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_conclusion_judgment.setText(judgmentes[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_report_delivery_mode://交付方式

                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, report_delivery_modes);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_report_delivery_mode.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_report_delivery_mode);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_report_delivery_mode.setText(report_delivery_modes[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_report_delivery_date:
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker5 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker5 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker5.setVisibility(View.GONE);
                final Calendar c5 = Calendar.getInstance();
                int year5 = c5.get(Calendar.YEAR);
                int month5 = c5.get(Calendar.MONTH);
                int day5 = c5.get(Calendar.DAY_OF_MONTH);

                datepicker5.init(year5, month5, day5, null);

                AlertDialog.Builder builder5 = new AlertDialog.Builder(this);
                builder5.setView(view);
                builder5.setTitle("交付日期选择");

                builder5.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker5.getYear();
                        int s_month = datepicker5.getMonth();
                        int s_day = datepicker5.getDayOfMonth();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                        String currentDate = String.valueOf(sb);
                        System.out.println(currentDate);
                        et_report_delivery_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder5.show();
                break;
            case R.id.iv_differential_solution:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, differential_solutiones);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_differential_solution.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_differential_solution);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_differential_solution.setText(differential_solutiones[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;

            case R.id.iv_retained_accessory:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, retained_accessorys);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_retained_accessory.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_retained_accessory);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_retained_accessory.setText(retained_accessorys[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_paymen_method:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paymen_methodes);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_paymen_method.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_paymen_method);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_paymen_method.setText(paymen_methodes[position]);
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_sample_collection_date:
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker6 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker6 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker6.setVisibility(View.GONE);
                final Calendar c6 = Calendar.getInstance();
                int year6 = c6.get(Calendar.YEAR);
                int month6 = c6.get(Calendar.MONTH);
                int day6 = c6.get(Calendar.DAY_OF_MONTH);

                datepicker6.init(year6, month6, day6, null);

                AlertDialog.Builder builder6 = new AlertDialog.Builder(this);
                builder6.setView(view);
                builder6.setTitle("收样日期选择");

                builder6.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker6.getYear();
                        int s_month = datepicker6.getMonth();
                        int s_day = datepicker6.getDayOfMonth();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                        String currentDate = String.valueOf(sb);
                        System.out.println(currentDate);
                        et_sample_collection_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder6.show();
                break;
            case R.id.iv_detection_station_name:

                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_property"}, "company_property!=?", new String[]{"监管单位"}, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_detection_station_name.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_detection_station_name);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_detection_station_name.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_detection_station_date:
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker7 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker7 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker7.setVisibility(View.GONE);
                final Calendar c7 = Calendar.getInstance();
                int year7 = c7.get(Calendar.YEAR);
                int month7 = c7.get(Calendar.MONTH);
                int day7 = c7.get(Calendar.DAY_OF_MONTH);

                datepicker7.init(year7, month7, day7, null);
                AlertDialog.Builder builder7 = new AlertDialog.Builder(this);
                builder7.setView(view);
                builder7.setTitle("承检单位签名日期选择");
                builder7.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker7.getYear();
                        int s_month = datepicker7.getMonth();
                        int s_day = datepicker7.getDayOfMonth();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                        String currentDate = String.valueOf(sb);
                        System.out.println(currentDate);
                        et_detection_station_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder7.show();
                break;
            case R.id.iv_client_detection_date:
                view = View.inflate(this, R.layout.date_view, null);
                final DatePicker datepicker8 = (DatePicker) view.findViewById(R.id.new_act_date_picker);
                final TimePicker timePicker8 = (TimePicker) view.findViewById(R.id.timepicker);
                timePicker8.setVisibility(View.GONE);
                final Calendar c8 = Calendar.getInstance();
                int year8 = c8.get(Calendar.YEAR);
                int month8 = c8.get(Calendar.MONTH);
                int day8 = c8.get(Calendar.DAY_OF_MONTH);

                datepicker8.init(year8, month8, day8, null);
                AlertDialog.Builder builder8 = new AlertDialog.Builder(this);
                builder8.setView(view);
                builder8.setTitle("委托检测日期选择");
                builder8.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        int s_year = datepicker8.getYear();
                        int s_month = datepicker8.getMonth();
                        int s_day = datepicker8.getDayOfMonth();

                        int sMonth = Integer.parseInt(String.valueOf(s_month)) + 1;
                        int sDay = Integer.parseInt(String.valueOf(s_day));
                        StringBuffer sb = new StringBuffer();
                        sb.append(s_year).append("-").append(sMonth).append("-").append(sDay);
                        String currentDate = String.valueOf(sb);
                        System.out.println(currentDate);
                        et_client_detection_date.setText(currentDate);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder8.show();

                break;

            case R.id.iv_detection_item_id:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_item", new String[]{"detection_item_name"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_detection_item_id.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_detection_item_id);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_detection_item_id.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_detection_method_id:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_method", new String[]{"detection_method"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_detection_method_id.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_detection_method_id);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_detection_method_id.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });

                break;
            case R.id.iv_detection_gist_id:
                Toast.makeText(this, "暂无", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_save:
                if (!TextUtils.isEmpty(et_testing_no.getText().toString()) && !TextUtils.isEmpty(et_sample_bh.getText().toString()) && !TextUtils.isEmpty(et_detection_item_id.getText().toString())) {
                    ContentValues cv = new ContentValues();
                    ContentValues cv1 = new ContentValues();

                    cv.put("testing_no", et_testing_no.getText().toString().trim());
                    cv1.put("testing_no", et_testing_no.getText().toString().trim());
                    cv1.put("detection_method_id", et_detection_method_id.getText().toString().trim());
                    cv1.put("detection_item_id", Id2nameUtils.item2id(et_detection_item_id.getText().toString().trim(), sdb));
                    cv1.put("testing_no", et_testing_no.getText().toString().trim());

                    if (et_weituoNo.equals("无")) {
                        cv.put("weituo_number", "");
                    } else {
                        cv.put("weituo_number", et_weituoNo.getText().toString().trim());
                    }

                    cv.put("tasks_source", et_tasksource.getText().toString().trim());
                    switch (et_taskType.getText().toString().trim()) {
                        case "监督抽查":
                            cv.put("tasks_type", 1);
                            break;
                        case "风险监测":
                            cv.put("tasks_type", 2);
                            break;
                        case "市场开办者自检":
                            cv.put("tasks_type", 3);
                            break;
                        case "委托":
                            cv.put("tasks_type", 4);
                            break;
                        case "仲裁":
                            cv.put("tasks_type", 5);
                            break;
                        case "比对":
                            cv.put("tasks_type", 6);
                            break;
                        case "练兵":
                            cv.put("tasks_type", 7);
                            break;
                        case "其他":
                            cv.put("tasks_type", 8);
                            break;
                    }

                    cv.put("examined_company", Id2nameUtils.company2id(et_examined_company.getText().toString().trim(), sdb));
                    switch (et_commission_type.getText().toString().trim()) {
                        case "个人":
                            cv.put("commission_type", 0);
                            break;
                        case "单位":
                            cv.put("commission_type", 1);
                            break;
                    }
                    cv.put("commission_company", Id2nameUtils.company2id(et_commission_company.getText().toString().trim(), sdb));
                    cv.put("detection_part", Id2nameUtils.part2id(et_detection_part.getText().toString().trim(), sdb));
                    cv.put("sample_bh", et_sample_bh.getText().toString().trim());
                    cv.put("sampling_company", Id2nameUtils.company2id(et_sampling_company.getText().toString().trim(), sdb));
                    cv.put("mailing_address", et_mailing_address.getText().toString().trim());
                    cv.put("sampling_deadline", et_sampling_deadline.getText().toString().trim());
                    cv.put("sampling_person1", et_sampling_person1.getText().toString().trim());
                    cv.put("sampling_person2", et_sampling_person2.getText().toString().trim());
                    cv.put("sampling_person3", et_sampling_person3.getText().toString().trim());
                    cv.put("sampling_date", et_sampling_date.getText().toString().trim());
                    switch (et_examinedcompany_opinion.getText().toString().trim()) {
                        case "无异议":
                            cv.put("examinedcompany_opinion", 1);
                            break;
                        case "有异议":
                            cv.put("examinedcompany_opinion", 0);
                            break;
                    }
                    cv.put("examinedcompany_signature", et_examinedcompany_signature.getText().toString().trim());
                    cv.put("examinedcompany_date", et_examinedcompany_date.getText().toString().trim());

                    switch (et_producecompany_opinion.getText().toString().trim()) {
                        case "无异议":
                            cv.put("producecompany_opinion", 1);
                            break;
                        case "有异议":
                            cv.put("producecompany_opinion", 0);
                            break;
                    }
                    cv.put("producecompany_signature", et_producecompany_signature.getText().toString().trim());
                    cv.put("producecompany_date", et_producecompany_date.getText().toString().trim());
                    cv.put("remark", et_remark.getText().toString().trim());
                    switch (et_conclusion_judgment.getText().toString().trim()) {
                        case "做出判断":
                            cv.put("conclusion_judgment", 1);
                            break;
                        case "出具数据":
                            cv.put("conclusion_judgment", 0);
                            break;
                    }
                    switch (et_report_delivery_mode.getText().toString().trim()) {
                        case "代邮":
                            cv.put("report_delivery_mode", 1);
                            break;
                        case "自取":
                            cv.put("report_delivery_mode", 2);
                            break;
                        case "委托代取":
                            cv.put("report_delivery_mode", 3);
                            break;
                        case "传真":
                            cv.put("report_delivery_mode", 4);
                            break;
                        case "其他":
                            cv.put("report_delivery_mode", 5);
                            break;
                    }

                    cv.put("report_delivery_date", et_report_delivery_date.getText().toString().trim());
                    cv.put("report_num", et_report_num.getText().toString().trim());

                    switch (et_differential_solution.getText().toString().trim()) {
                        case "书面":
                            cv.put("differential_solution", 1);
                            break;
                        case "口头":
                            cv.put("differential_solution", 2);
                            break;
                        case "电话":
                            cv.put("differential_solution", 3);
                            break;
                        case "传真":
                            cv.put("differential_solution", 4);
                            break;
                    }
                    switch (et_retained_accessory.getText().toString().trim()) {
                        case "保留":
                            cv.put("retained_accessory", 1);
                            break;
                        case "不保留":
                            cv.put("retained_accessory", 0);
                            break;
                    }
                    cv.put("explaination_remark", et_explaination_remark.getText().toString().trim());
                    cv.put("total_cost", et_total_cost.getText().toString().trim());


                    switch (et_paymen_method.getText().toString().trim()) {
                        case "现金":
                            cv.put("paymen_method", 1);
                            break;
                        case "月结":
                            cv.put("paymen_method", 2);
                            break;
                        case "协议":
                            cv.put("paymen_method", 3);
                            break;
                        case "担保":
                            cv.put("paymen_method", 4);
                            break;
                    }
                    cv.put("sample_collection", et_sample_collection.getText().toString().trim());
                    cv.put("sample_collection_date", et_sample_collection_date.getText().toString().trim());
                    cv.put("detection_station_name", Id2nameUtils.company2id(et_detection_station_name.getText().toString().trim(), sdb));
                    cv.put("detection_station_person", et_detection_station_person.getText().toString().trim());
                    cv.put("detection_station_date", et_detection_station_date.getText().toString().trim());
                    cv.put("client_name", et_client_name.getText().toString().trim());
                    cv.put("client_detection_date", et_client_detection_date.getText().toString().trim());
                    cv.put("fengsamplecheck_person", et_fengsamplecheck_person.getText().toString().trim());


                    if (testing_no != null) {
                        int d = sdb.update("galaxy_application_form", cv, "testing_no=?", new String[]{testing_no});
                        sdb.update("galaxy_application_formcb", cv1, "testing_no=?", new String[]{testing_no});
                        if (d == -1) {
                            Toast.makeText(this, "数据更改失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "数据更改成功", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                        return;
                    }

                    long d = sdb.insert("galaxy_application_form", null, cv);
                    sdb.insert("galaxy_application_formcb", null, cv1);
                    if (d == -1) {
                        Toast.makeText(this, "储存数据库失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "储存数据库成功", Toast.LENGTH_SHORT).show();
                        ContentValues cv3 = new ContentValues();
                        cv3.put("use_valid", 1);
                        sdb.update("galaxy_task_delegation", cv3, "weituo_number=?", new String[]{et_weituoNo.getText().toString()});
                    }
                    if (unTask != null) {
                        Intent data = new Intent();
                        data.putExtra("test_no", et_testing_no.getText().toString());
                        setResult(6, data);
                        finish();
                    }
                    if (taskNO != null) {

                        Intent data = new Intent();
                        data.putExtra("test_no", et_testing_no.getText().toString());
                        setResult(8, data);
                        finish();
                    }
                    finish();
                } else {
                    Toast.makeText(ApplySettingActivity.this, "请补充必填项", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     * 获取控件实际的高度
     */
    public int getMeasureHeight() {
        int width = ll_apply.getMeasuredWidth();  //  由于宽度不会发生变化  宽度的值取出来
        ll_apply.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;//  让高度包裹内容

        //    参数1  测量控件mode    参数2  大小
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);  //  mode+size
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);// 我的高度 最大是1000
        // 测量规则 宽度是一个精确的值width, 高度最大是1000,以实际为准
        ll_apply.measure(widthMeasureSpec, heightMeasureSpec); // 通过该方法重新测量控件

        return ll_apply.getMeasuredHeight();
    }

}

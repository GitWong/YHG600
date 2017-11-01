package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.R;
import com.galaxy.safe.adapt.PointAdapt;
import com.galaxy.safe.utils.CameraPreview;
import com.galaxy.safe.utils.CheckBitmapUtils;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.HorizontalUtils;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.utils.ImageProcess;
import com.galaxy.safe.utils.ToGray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by Dell on 2016/4/15.
 */
public class CurveSettingActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText et_item;
    private EditText et_sample;
    private EditText et_card_batch;
    private EditText et_up;
    private EditText et_down;
    private EditText et_unit;
    private EditText et_result1;
    private EditText et_result2;
    private EditText et_result3;
    private EditText et_curve_type;
    private EditText et_method;
    private EditText et_way;
    private EditText et_by;
    private EditText et_part;
    private EditText et_off;
    private EditText et_line;

    private ImageView iv_item;
    private ImageView iv_down;
    private ImageView iv_up;
    //    private ImageView iv_sample;
    private ImageView iv_card_batch;
    private ImageView iv_unit;
    private ImageView iv_result1;
    private ImageView iv_result2;
    private ImageView iv_result3;
    private ImageView iv_curve_type;
    private ImageView iv_method;
    private ImageView iv_way;
    private ImageView iv_device;
    private ImageView iv_line;


    private Button bt_save;
    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private AnimationSet set;
    private String curve_id;
    private GridView gv_point;
    private LineChartView lineChart;
    private ArrayList<PointValue> pList;
    private CheckBox cb_background_valid;

    private EditText et_x;//名称
    private EditText et_y;//描述

    private AlertDialog dialog1;
    private List<String> check_list;
    private PopupWindow popupWindow;
    private ArrayAdapter<String> madapter;

    private View view;//对话框ui

    private String max = "1";

    private int lsh;
    private ScrollView sv_cunve;
    private ProgressDialog pd;
    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;
    private Bitmap grayBitmap;
    private Bitmap recBitmap;
    private Bitmap whiteBitmap;
    private CheckBitmapUtils cb;//这个对象很耗内存回收掉
    String card_typeid;
    private int D;
    String left_x;
    String left_y;
    private int d;

    private String line_space1;
    private String line_space2;
    private String line_space3;
    private String line_space4;
    private int line_number;


    private int tGray;
    /**
     * 启动定时拍照并上传功能
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    openCamera();
                    break;
                case 2:
                    mCamera.autoFocus(new Camera.AutoFocusCallback() {

                        @Override
                        public void onAutoFocus(boolean success, Camera camera) {
                            // 从Camera捕获图片
                            if (success) {
                                pd.setMessage("读取中。。。。。");
                                camera.takePicture(null, null, mPicture);
//                            mHandler.sendEmptyMessageDelayed(1, 5*1000);
                            }
                        }
                    });
                    break;
                case 3:
                    if (recBitmap != null) {
                        pd.setMessage("读取中。。。。。。");
                        pd.setMessage("读取结束");
                        pd.dismiss();

                        cb = new CheckBitmapUtils(recBitmap, CurveSettingActivity.this, left_x, left_y, D);
                        tGray = cb.math();
                        switch (d) {
                            case 1:
                                et_x.setText(tGray + "");
                                break;
                            case 2:
                                et_up.setText(tGray + "");
                                break;
                            case 3:
                                et_down.setText(tGray + "");
                                break;
                        }
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curvesetting);
        initView();
        initAnimotion();
        initToolBar();
        initData();

        curve_id = getIntent().getStringExtra("curve_id");
        if (curve_id != null) {
            fillData();
        }


    }

    /**
     * 填充数据
     */
    private void fillData() {
        bt_save.setText("更改标准曲线信息");

        cursor = sdb.query("galaxy_standard_curve", new String[]{"curve_id", "detection_item_id",
                "card_batch_id", "limited_max",
                "limited_min", "limited_unit_id", "detection_conclusion1"
                , "detection_conclusion2", "detection_conclusion3", "func_type_id",
                "background_valid", "detection_method_id", "const_flag", "standard_value", "standard_basis", "part", "off", "line_jianju"}, "curve_id=?", new String[]{curve_id}, null, null, null);
        while (cursor.moveToNext()) {

            et_item.setText(i2name(cursor.getString(1)));
//            et_sample.setText(t2name(cursor.getString(2)));

            et_card_batch.setText(Id2nameUtils.cardBatch2name(cursor.getString(2), sdb));
            et_up.setText(cursor.getString(3));
            et_down.setText(cursor.getString(4));
            et_unit.setText(u2name(cursor.getString(5)));
            et_result1.setText(Id2nameUtils.con2name(cursor.getString(6), sdb));
            et_result2.setText(Id2nameUtils.con2name(cursor.getString(7), sdb));
            et_result3.setText(Id2nameUtils.con2name(cursor.getString(8), sdb));

            et_curve_type.setText(f2name(cursor.getString(9)));

            if (cursor.getString(10).equals("0")) {
                cb_background_valid.setChecked(false);
            } else {
                cb_background_valid.setChecked(true);
            }
            et_by.setText(cursor.getString(14));
            et_method.setText(m2name(cursor.getString(11)));
            if (cursor.getString(12).equals("0")) {
                et_way.setText("定量");
            } else {
                et_way.setText("定性");
            }

            et_part.setText(cursor.getString(15));
            et_off.setText(cursor.getString(16));
            et_line.setText(cursor.getString(17));
        }
        cursor.close();
        cursor = sdb.query("galaxy_standard_curve_child", new String[]{"curve_id", "x_gray", "y_potency"}, "curve_id=?", new String[]{curve_id}, null, null, null);
        while (cursor.moveToNext()) {
            PointValue p = new PointValue();
            String x = cursor.getString(1).trim();
            String y = cursor.getString(2).trim();
            if (!x.equals("0") && !y.equals("0")) {
                p.set(Float.valueOf(x), Float.valueOf(y));
                pList.add(p);
            }
        }
        cursor.close();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        lineChart = (LineChartView) findViewById(R.id.lc_point);
        gv_point = (GridView) findViewById(R.id.gv_point);
        bt_save = (Button) findViewById(R.id.bt_save);

        et_item = (EditText) findViewById(R.id.et_item);
        et_sample = (EditText) findViewById(R.id.et_sample);
        et_card_batch = (EditText) findViewById(R.id.et_card_batch);
        et_up = (EditText) findViewById(R.id.et_up);
        et_down = (EditText) findViewById(R.id.et_down);
        et_unit = (EditText) findViewById(R.id.et_unit);
        et_result1 = (EditText) findViewById(R.id.et_result1);
        et_result2 = (EditText) findViewById(R.id.et_result2);
        et_result3 = (EditText) findViewById(R.id.et_result3);
        et_curve_type = (EditText) findViewById(R.id.et_curve_type);
        et_method = (EditText) findViewById(R.id.et_method);
        et_way = (EditText) findViewById(R.id.et_way);
        et_by = (EditText) findViewById(R.id.et_by);
        et_part = (EditText) findViewById(R.id.et_part);
        et_off = (EditText) findViewById(R.id.et_off);
        et_line = (EditText) findViewById(R.id.et_line);
        et_way.setText("定性");
        gv_point.setVisibility(View.GONE);
        lineChart.setVisibility(View.GONE);
        et_way.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("定性")) {
                    gv_point.setVisibility(View.GONE);
                    lineChart.setVisibility(View.GONE);
                    et_part.setFocusable(false);
                    et_curve_type.setFocusable(false);
                    iv_curve_type.setClickable(false);

                } else {
                    gv_point.setVisibility(View.VISIBLE);
                    lineChart.setVisibility(View.VISIBLE);
                    et_curve_type.setFocusable(true);
                    et_part.setText("20");
                    iv_curve_type.setClickable(true);
                }

            }
        });


        iv_up = (ImageView) findViewById(R.id.iv_up);
        iv_down = (ImageView) findViewById(R.id.iv_down);
        iv_item = (ImageView) findViewById(R.id.iv_item);
//        iv_sample = (ImageView) findViewById(R.id.iv_sample);
        iv_card_batch = (ImageView) findViewById(R.id.iv_card_batch);
        iv_unit = (ImageView) findViewById(R.id.iv_unit);
        iv_result1 = (ImageView) findViewById(R.id.iv_result1);
        iv_result2 = (ImageView) findViewById(R.id.iv_result2);
        iv_result3 = (ImageView) findViewById(R.id.iv_result3);
        iv_curve_type = (ImageView) findViewById(R.id.iv_curve_type);
        iv_method = (ImageView) findViewById(R.id.iv_method);
        iv_way = (ImageView) findViewById(R.id.iv_way);
        iv_line = (ImageView) findViewById(R.id.iv_line);
//        iv_device = (ImageView) findViewById(R.id.iv_device);
        sv_cunve = (ScrollView) findViewById(R.id.sv_curve);
        cb_background_valid = (CheckBox) findViewById(R.id.cb_background_valid);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        gv_point.setOnItemClickListener(this);
        bt_save.setOnClickListener(this);
        iv_item.setOnClickListener(this);
//        iv_sample.setOnClickListener(this);
        iv_card_batch.setOnClickListener(this);
        iv_unit.setOnClickListener(this);
        iv_result1.setOnClickListener(this);
        iv_result2.setOnClickListener(this);
        iv_result3.setOnClickListener(this);
        iv_curve_type.setOnClickListener(this);
        iv_method.setOnClickListener(this);
        iv_way.setOnClickListener(this);
        iv_up.setOnClickListener(this);
        iv_down.setOnClickListener(this);
        iv_line.setOnClickListener(this);

        cb_background_valid.setChecked(true);
//        iv_device.setOnClickListener(this);
        pList = new ArrayList<PointValue>();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库

        cursor = sdb.query("galaxy_standard_curve", new String[]{"curve_id"}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.isLast()) {
                max = cursor.getString(0);
            }
        }
        //对它进行排序
        Collections.sort(pList, new Comparator<PointValue>() {
            @Override
            public int compare(PointValue lhs, PointValue rhs) {
                float x = lhs.getX();
                float x2 = rhs.getX();
                if (x > x2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        gv_point.setAdapter(new PointAdapt(this, pList));
        Line line = new Line(pList).setColor(Color.YELLOW);
        line.setHasLabels(true);

        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        LineChartData data = new LineChartData();

        data.setValueLabelBackgroundColor(Color.TRANSPARENT); //此处设置坐标点旁边的文字背景
        data.setValueLabelBackgroundEnabled(false);
        data.setValueLabelsTextColor(Color.BLACK);  //此处设置坐标点旁边的文字颜色
        data.setLines(lines);
        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);
        axisX.setTextColor(Color.DKGRAY);
        axisX.setName("检测T值");
        axisX.setMaxLabelChars(4);
        axisX.setHasLines(true);
//        axisX.setValues(mAxisValues);
        data.setAxisXBottom(axisX);
        Axis axisY = new Axis();  //Y轴
        axisY.setName("浓度值");
        axisY.setTextColor(Color.BLACK);
        axisY.setMaxLabelChars(3); //默认是3，只能看最后三个数字
        axisY.setHasLines(true);
        data.setAxisYLeft(axisY);

        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setLineChartData(data);

    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {

        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("标准曲线详细信息页面");
        tl_bar.setSubtitle("标准曲线详情");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tl_bar.inflateMenu(R.menu.menu_curve);
        tl_bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                              @Override
                                              public boolean onMenuItemClick(MenuItem item) {
                                                  switch (item.getItemId()) {
                                                      case R.id.info:
                                                          if (curve_id != null) {
                                                              Intent i = new Intent(CurveSettingActivity.this, TwoActivity.class);
                                                              i.putExtra("curve_id", curve_id);
                                                              startActivity(i);
                                                          } else {
                                                              Snackbar.make(tl_bar, "先保存数据", Snackbar.LENGTH_SHORT).show();
                                                          }
                                                          break;
                                                      case R.id.add_point:

                                                          if (!TextUtils.isEmpty(et_card_batch.getText().toString())) {
                                                              cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch", "card_type_id"}, "card_batch=?", new String[]{et_card_batch.getText().toString()}, null, null, null);
                                                              while (cursor.moveToNext()) {
                                                                  card_typeid = cursor.getString(1);
                                                              }
                                                              if (card_typeid != null) {
                                                                  cursor = sdb.query("galaxy_card_type", new String[]{"white_board", "card_type_id", "left_x", "left_y", "line_space1"}, "card_type_id=?", new String[]{card_typeid}, null, null, null);
                                                                  while (cursor.moveToNext()) {
                                                                      byte[] g = cursor.getBlob(0);
                                                                      if (g != null) {
                                                                          whiteBitmap = BitmapFactory.decodeByteArray(g, 0, g.length);
                                                                      }
                                                                      left_x = cursor.getString(2);
                                                                      left_y = cursor.getString(3);
                                                                  }
                                                              }

                                                          } else {
                                                              Toast.makeText(CurveSettingActivity.this, "增加点之前必须先有卡批次", Toast.LENGTH_SHORT).show();
                                                              return false;
                                                          }

                                                          if (pList.size() >= 8) {
                                                              Toast.makeText(CurveSettingActivity.this, "点个数已达上限", Toast.LENGTH_SHORT).show();
                                                              return false;
                                                          } else {
                                                              AlertDialog.Builder build1 = new AlertDialog.Builder(CurveSettingActivity.this);
                                                              view = View.inflate(CurveSettingActivity.this, R.layout.edit_dialog_point, null);
                                                              et_x = (EditText) view.findViewById(R.id.et_sample_name);
                                                              et_y = (EditText) view.findViewById(R.id.et_sample_des);
                                                              preview = (FrameLayout) view.findViewById(R.id.camera_preview);
                                                              view.findViewById(R.id.iv_math).setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View v) {
                                                                      d = 1;
                                                                      pd = new ProgressDialog(CurveSettingActivity.this);
                                                                      pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                                                                      pd.setTitle("读取提示");
                                                                      pd.setMessage("读取中。。");
                                                                      pd.setCancelable(true);// 设置是否可以通过点击Back键取消
                                                                      pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                                                                      pd.show();
                                                                      mHandler.sendEmptyMessageDelayed(1, 2 * 1000); //7s 后开始启动相机


                                                                  }
                                                              });

                                                              view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View v) {

                                                                      String x = et_x.getText().toString().trim();
                                                                      String y = et_y.getText().toString().trim();

                                                                      PointValue p = new PointValue();
                                                                      if (!TextUtils.isEmpty(x) && !TextUtils.isEmpty(y)) {

                                                                          p.set(Float.parseFloat(x), Float.parseFloat(y));
                                                                          pList.add(p);
                                                                          initData();
                                                                          gv_point.setSelection(0);
                                                                          sv_cunve.fullScroll(ScrollView.FOCUS_UP);
                                                                          dialog1.dismiss();
                                                                      } else {
                                                                          Toast.makeText(CurveSettingActivity.this, "数据不能为空", Toast.LENGTH_SHORT).show();
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
                                                              break;
                                                          }
                                                  }
                                                  return false;
                                              }
                                          }
        );

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

    View popview;
    ListView lv_content;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_line:
                View popview = View.inflate(CurveSettingActivity.this, R.layout.popup_item, null);
                ListView lv_content = (ListView) popview.findViewById(R.id.lv_content);

                check_list = new ArrayList<String>();
                if (line_number == 0) {

                } else if (line_number == 1) {
                    check_list.add("线间距1");
                } else if (line_number == 2) {
                    check_list.add("线间距1");
                    check_list.add("线间距2");
                } else if (line_number == 3) {
                    check_list.add("线间距1");
                    check_list.add("线间距2");
                    check_list.add("线间距3");
                } else if (line_number == 4) {
                    check_list.add("线间距1");
                    check_list.add("线间距2");
                    check_list.add("线间距3");
                    check_list.add("线间距4");
                }

                madapter = new ArrayAdapter<String>(CurveSettingActivity.this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);

                popupWindow = new PopupWindow(popview, et_line.getWidth(),
                        -2, true);
                popupWindow.setBackgroundDrawable(new
                                ColorDrawable(
                                Color.TRANSPARENT)

                );
                popupWindow.showAsDropDown(et_line);
                popupWindow.setOutsideTouchable(false);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                      @Override
                                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                          et_line.setText(check_list.get(position).toString());
                                                          switch (position) {
                                                              case 0:
                                                                  D = Integer.valueOf(line_space1);
                                                                  break;
                                                              case 1:
                                                                  D = Integer.valueOf(line_space2);
                                                                  break;
                                                              case 2:
                                                                  D = Integer.valueOf(line_space3);
                                                                  break;
                                                              case 3:
                                                                  D = Integer.valueOf(line_space4);
                                                                  break;
                                                          }
                                                          popupWindow.dismiss();
                                                          popupWindow = null;
                                                          madapter = null;
                                                      }
                                                  }
                );
                break;
            case R.id.iv_item:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_item", new String[]{"detection_item_name"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_item.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_item);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_item.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });
                break;
            case R.id.iv_card_batch:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_card_batch.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_card_batch);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_card_batch.setText(check_list.get(position));
                        input();
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });
                break;

            case R.id.iv_unit:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_unit", new String[]{"limited_unit"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_unit.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_unit);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_unit.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });
                break;
            case R.id.iv_result1:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_conclusion", new String[]{"detection_conclusion"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_result1.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_result1);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_result1.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });
                break;
            case R.id.iv_result2:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_conclusion", new String[]{"detection_conclusion"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_result2.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_result2);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_result2.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });
                break;
            case R.id.iv_result3:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_conclusion", new String[]{"detection_conclusion"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_result3.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_result3);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_result3.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });
                break;
            case R.id.iv_curve_type:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_curve_type", new String[]{"func_type"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_curve_type.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
//                popupWindow.showAtLocation(et_curve_type, Gravity.BOTTOM, 0, 0);
                popupWindow.showAsDropDown(et_curve_type);

                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_curve_type.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });
                break;

            case R.id.iv_method:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_method", new String[]{"detection_method"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_method.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_method);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_method.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });

                break;
            case R.id.iv_way:
                popview = View.inflate(this, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                check_list = new ArrayList<String>();
                check_list.add("定量");
                check_list.add("定性");
                madapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(madapter);
                popupWindow = new PopupWindow(popview, et_way.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_way);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_way.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        madapter = null;
                    }
                });
                break;
            case R.id.bt_save:
                if (curve_id != null) {
                    cursor = sdb.query("galaxy_standard_curve_child", new String[]{"curve_id", "lsh"}, "curve_id=?", new String[]{curve_id}, null, null, null);
                    if (lsh == 0) {
                        while (cursor.moveToNext()) {
                            if (cursor.isFirst()) {
                                try {
                                    lsh = Integer.parseInt(cursor.getString(1));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                if (!TextUtils.isEmpty(et_item.getText().toString()) && !TextUtils.isEmpty(et_card_batch.getText().toString())) {

                    ContentValues cv1 = new ContentValues();
                    for (int i = 0; i < 8; i++) {
                        if (i < pList.size()) {
                            cv1.put("x_gray", pList.get(i).getX());
                            cv1.put("y_potency", pList.get(i).getY());
                        } else {
                            cv1.put("x_gray", 0);
                            cv1.put("y_potency", 0);
                        }
                        if (curve_id != null) {
                            long d = sdb.update("galaxy_standard_curve_child", cv1, "curve_id=? and lsh=?", new String[]{curve_id, String.valueOf(lsh)});
                            if (d == -1) {
                                Toast.makeText(this, "数据更改失败", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                lsh++;
                            }
                        } else {
                            cv1.put("curve_id", Integer.valueOf(max) + 1);
                            sdb.insert("galaxy_standard_curve_child", null, cv1);
                        }
                    }
                    ContentValues cv = new ContentValues();
                    cv.put("detection_item_id", i2id(et_item.getText().toString()));
//                        cv.put("type_id", t2id(et_sample.getText().toString()));
//                    cv.put("point_count", pList.size());
                    cv.put("part", et_part.getText().toString());
                    cv.put("off", et_off.getText().toString());
                    cv.put("card_batch_id", Id2nameUtils.cardBatch2id(et_card_batch.getText().toString(), sdb));
                    cv.put("limited_max", et_up.getText().toString());
                    cv.put("limited_min", et_down.getText().toString());
                    cv.put("line_jianju", et_line.getText().toString());
                    cv.put("limited_unit_id", u2id(et_unit.getText().toString()));
                    cv.put("detection_conclusion1", Id2nameUtils.con2id(et_result1.getText().toString(), sdb));
                    cv.put("detection_conclusion2", Id2nameUtils.con2id(et_result2.getText().toString(), sdb));
                    cv.put("detection_conclusion3", Id2nameUtils.con2id(et_result3.getText().toString(), sdb));
                    cv.put("func_type_id", f2id(et_curve_type.getText().toString()));
                    if (cb_background_valid.isChecked()) {
                        cv.put("background_valid", 1);
                    } else {
                        cv.put("background_valid", 0);
                    }

                    cv.put("detection_method_id", m2id(et_method.getText().toString()));

                    if (et_way.getText().toString().equals("定量")) {
                        cv.put("const_flag", 0);
                    } else {
                        cv.put("const_flag", 1);
                    }
                    cv.put("standard_basis", et_by.getText().toString().trim());
                    if (curve_id != null) {
                        long d = sdb.update("galaxy_standard_curve", cv, "curve_id=?", new String[]{curve_id});
                        if (d == -1) {
                            Toast.makeText(this, "数据更改失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        cv.put("curve_id", Integer.valueOf(max) + 1);
                        long d = sdb.insert("galaxy_standard_curve", null, cv);
                        if (d == -1) {
                            Toast.makeText(this, "储存数据库失败", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "项目和卡批次不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "成功", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.iv_up:
                d = 2;

                if (!TextUtils.isEmpty(et_card_batch.getText().toString())) {
                    cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch", "card_type_id"}, "card_batch=?", new String[]{et_card_batch.getText().toString()}, null, null, null);
                    while (cursor.moveToNext()) {
                        card_typeid = cursor.getString(1);
                    }
                    if (card_typeid != null) {
                        cursor = sdb.query("galaxy_card_type", new String[]{"white_board", "card_type_id", "left_x", "left_y", "line_space1"}, "card_type_id=?", new String[]{card_typeid}, null, null, null);
                        while (cursor.moveToNext()) {
                            byte[] g = cursor.getBlob(0);
                            if (g != null) {
                                whiteBitmap = BitmapFactory.decodeByteArray(g, 0, g.length);
                            }
                            left_x = cursor.getString(2);
                            left_y = cursor.getString(3);
                        }
                    }

                } else {
                    Toast.makeText(CurveSettingActivity.this, "读取之前必须先有卡批次", Toast.LENGTH_SHORT).show();
                    return;
                }
                pd = new ProgressDialog(CurveSettingActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                pd.setTitle("读取提示");
                pd.setMessage("读取中。。");
                pd.setCancelable(true);// 设置是否可以通过点击Back键取消
                pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                pd.show();
                mHandler.sendEmptyMessageDelayed(1, 2 * 1000); //7s 后开始启动相机
                break;
            case R.id.iv_down:
                d = 3;
                if (!TextUtils.isEmpty(et_card_batch.getText().toString())) {
                    cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch", "card_type_id"}, "card_batch=?", new String[]{et_card_batch.getText().toString()}, null, null, null);
                    while (cursor.moveToNext()) {
                        card_typeid = cursor.getString(1);
                    }
                    if (card_typeid != null) {
                        cursor = sdb.query("galaxy_card_type", new String[]{"white_board", "card_type_id", "left_x", "left_y", "line_space1"}, "card_type_id=?", new String[]{card_typeid}, null, null, null);
                        while (cursor.moveToNext()) {
                            byte[] g = cursor.getBlob(0);
                            if (g != null) {
                                whiteBitmap = BitmapFactory.decodeByteArray(g, 0, g.length);
                            }
                            left_x = cursor.getString(2);
                            left_y = cursor.getString(3);
//                            D = Integer.valueOf(cursor.getString(4));
                        }
                    }

                } else {
                    Toast.makeText(CurveSettingActivity.this, "读取之前必须先有卡批次", Toast.LENGTH_SHORT).show();
                    return;
                }
                pd = new ProgressDialog(CurveSettingActivity.this);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                pd.setTitle("读取提示");
                pd.setMessage("读取中。。");
                pd.setCancelable(true);// 设置是否可以通过点击Back键取消
                pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                pd.show();
                mHandler.sendEmptyMessageDelayed(1, 2 * 1000); //7s 后开始启动相机
                break;
        }

    }

    /**
     * 判断距离
     */
    private void input() {

        if (!TextUtils.isEmpty(et_card_batch.getText().toString())) {
            et_line.setText("间距1");
            cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch", "card_type_id"}, "card_batch=?", new String[]{et_card_batch.getText().toString()}, null, null, null);
            while (cursor.moveToNext()) {
                card_typeid = cursor.getString(1);
            }
            if (card_typeid != null) {
                cursor = sdb.query("galaxy_card_type", new String[]{"line_number", "card_type_id", "line_space1", "line_space2", "line_space3", "line_space4"}, "card_type_id=?", new String[]{card_typeid}, null, null, null);
                while (cursor.moveToNext()) {
                    line_number = Integer.valueOf(cursor.getString(0));
                    line_space1 = cursor.getString(2);
                    D = Integer.valueOf(cursor.getString(2));
                    line_space2 = cursor.getString(3);
                    line_space3 = cursor.getString(4);
                    line_space4 = cursor.getString(5);
                }
            }

        }


    }

    /**
     * 数据转换name转id
     *
     * @param s
     * @return
     */
    private String i2id(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_item", new String[]{"detection_item_id", "detection_item_name"}, "detection_item_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_item_id = cursor2.getString(0);
                return detection_item_id;
            }
            cursor2.close();
        }
        return null;
    }


    private String u2id(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_unit", new String[]{"limited_unit_id", "limited_unit"}, "limited_unit=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String limited_unit_id = cursor2.getString(0);
                return limited_unit_id;
            }
            cursor2.close();
        }
        return null;
    }


    private String f2id(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_curve_type", new String[]{"func_type_id", "func_type"}, "func_type=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String func_type_id = cursor2.getString(0);
                return func_type_id;
            }
            cursor2.close();
        }
        return null;
    }

    private String m2id(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_method", new String[]{"detection_method_id", "detection_method"}, "detection_method=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_method_id = cursor2.getString(0);
                return detection_method_id;
            }
            cursor2.close();
        }
        return null;
    }

    /**
     * 初始化设置Camera.打开摄像头
     * <p>
     * 如果没有前置摄像头的话就打开后置摄像头
     */

    private void openCamera() {
        pd.setMessage("读取中。。。");
        mCamera = getCameraInstance();
        if (mCamera != null) {
            mPreview = new CameraPreview(CurveSettingActivity.this, mCamera);
            preview.removeAllViews();
            preview.addView(mPreview);
            mCamera.startPreview();
        }
//        mHandler.sendEmptyMessage(2);
        mHandler.sendEmptyMessageDelayed(2, 6 * 1000); //4s后拍照

    }

    Canvas canvas;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 获取Jpeg图片，并保存在sd卡上
            try {

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                iv_cam.setImageBitmap(bitmap);
                //mCamera.startPreview();
                double d = new HorizontalUtils(CurveSettingActivity.this, bitmap).getRoat();  //显得到bitmap图片
                Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth() * 300 / 1000, bitmap.getHeight() * 300 / 1000, bitmap.getConfig());
                //2.临摹作画
                //创建画板 ,参考空白纸张的大小，把画板创建出来。
                canvas = new Canvas(copyBitmap);
                //3.作画
                //创建一个画笔
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);//设置默认的颜色。
                Matrix matrix = new Matrix();
                matrix.setRotate((float) -d, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);
                matrix.postScale(0.30f, 0.30f); //长和宽放大缩小的比例
                canvas.drawBitmap(bitmap, matrix, paint);

                grayBitmap = ToGray.toGray(copyBitmap);
                Log.i("22", "图片高度" + grayBitmap.getHeight());
                Log.i("22", "图片宽度" + grayBitmap.getWidth());


                if (whiteBitmap != null && cb_background_valid.isChecked()) {
                    recBitmap = ImageProcess.GetGrayCorrectionImage(whiteBitmap, Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false));
                } else {
                    recBitmap = Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false);
                }

                pd.setMessage("读取中。。。。");
//                iv_cam.setImageBitmap(recBitmap);
                mHandler.sendEmptyMessage(3);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                releaseCamera();
            }
        }
    };

    /**
     * 安全的方法 去访问照相机.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();// 获取照相机实例
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }


    /**
     * 释放摄像头资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 数据转换id转name
     *
     * @param s
     * @return
     */
    private String i2name(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_item", new String[]{"detection_item_id", "detection_item_name"}, "detection_item_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_item_name = cursor2.getString(1);
                return detection_item_name;
            }
            cursor2.close();
        }
        return null;
    }


    private String u2name(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_unit", new String[]{"limited_unit_id", "limited_unit"}, "limited_unit_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String limited_unit = cursor2.getString(1);
                return limited_unit;
            }
            cursor2.close();
        }
        return null;
    }

    private String f2name(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_curve_type", new String[]{"func_type_id", "func_type"}, "func_type_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String func_type = cursor2.getString(1);
                return func_type;
            }
            cursor2.close();
        }
        return null;
    }

    private String m2name(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_method", new String[]{"detection_method_id", "detection_method"}, "detection_method_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_method = cursor2.getString(1);
                return detection_method;
            }
            cursor2.close();
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder build1 = new AlertDialog.Builder(CurveSettingActivity.this);
        view = View.inflate(CurveSettingActivity.this, R.layout.edit_dialog_point, null);
        TextView tv = (TextView) view.findViewById(R.id.tv_title);
        tv.setText("更改点");
        et_x = (EditText) view.findViewById(R.id.et_sample_name);
        et_y = (EditText) view.findViewById(R.id.et_sample_des);
        view.findViewById(R.id.iv_math).setVisibility(View.GONE);

        et_x.setText(String.valueOf(pList.get(position).getX()));
        et_y.setText(String.valueOf(pList.get(position).getY()));
        view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String x = et_x.getText().toString().trim();
                String y = et_y.getText().toString().trim();
                if (!TextUtils.isEmpty(et_x.getText().toString().trim()) && !TextUtils.isEmpty(et_y.getText().toString().trim())) {
                    if (x.equals("0") && y.equals("0")) {
                        pList.remove(pList.get(position));
                    } else if (x.equals("0.0") && y.equals("0.0")) {
                        pList.remove(pList.get(position));
                    } else {
                        pList.get(position).set(Float.parseFloat(x), Float.parseFloat(y));
                    }
                } else {
                    pList.remove(pList.get(position));
                }
                initData();
                dialog1.dismiss();
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
}

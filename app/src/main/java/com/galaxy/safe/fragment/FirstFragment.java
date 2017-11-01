/*
 * Copyright (C) 2012 yueyueniao
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.galaxy.safe.fragment;

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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.R;
import com.galaxy.safe.base.BaseFragment;
import com.galaxy.safe.ui.MainActivity;
import com.galaxy.safe.ui.TaskActivity;
import com.galaxy.safe.utils.CameraPreview;
import com.galaxy.safe.utils.CheckBitmapUtils;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.HorizontalUtils;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.utils.ImageProcess;
import com.galaxy.safe.utils.SpUtils;
import com.galaxy.safe.utils.ToGray;
import com.galaxy.safe.utils.ValueUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.PointValue;


public class FirstFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.iv_tasks)
    ImageView iv_task;
    @BindView(R.id.et_sample_bh)
    EditText etSampleBh;
    @BindView(R.id.iv_sample_bh)
    ImageView iv_sample_bh;
    @BindView(R.id.iv_add)
    ImageView iv_add;
    @BindView(R.id.et_testing_no)
    EditText etTestingNo;
    @BindView(R.id.iv_testing_no)
    ImageView ivTestingNo;
    @BindView(R.id.iv_add1)
    ImageView ivAdd1;
    @BindView(R.id.et_sampleType)
    EditText et_sampleType;
    @BindView(R.id.et_samplename)
    EditText etSamplename;
    @BindView(R.id.et_checked)
    EditText etChecked;
    @BindView(R.id.et_birth)
    EditText et_birth;
    @BindView(R.id.et_item)
    EditText et_item;
    @BindView(R.id.iv_item)
    ImageView iv_item;
    @BindView(R.id.et_card_batch)
    EditText et_card_batch;
    @BindView(R.id.iv_card_batch)
    ImageView iv_card_batch;
    @BindView(R.id.et_tasks)
    EditText et_tasks;

    @BindView(R.id.et_by)
    EditText etBy;
    @BindView(R.id.et_max)
    EditText etMax;
    @BindView(R.id.tv_result)
    TextView tv_result;
    @BindView(R.id.camera_preview)
    FrameLayout preview;
    @BindView(R.id.iv_cam)
    ImageView iv_cam;
    @BindView(R.id.rl_layout)
    RelativeLayout rlLayout;
    @BindView(R.id.lv_check)
    LinearLayout lv_check;


    /* private List<EntrustBean> cList;
     private ListView lv_entrustlist;
     private ImageView iv_cam;
     private LinearLayout lv_check;
     private TextView tv_result;

     private EditText et_sample_bh;//样本编号
     private ImageView iv_sample_bh;
     private ImageView iv_add;

     private EditText et_sampleType;
     private EditText et_samplename;
     private EditText et_checked;
     private EditText et_birth;
     private EditText et_item;
     private ImageView iv_item;//检测项目

     private EditText et_card_batch;
     private ImageView iv_card_batch;//卡批次
     private EditText et_tasks;
     private ImageView iv_tasks;//检测renwu*/
    private Bitmap grayBitmap;
    private String sample_batch;//样本批次
    private String submit_date;//送检日期
    private String detection_part;//受检环节
    private String sample;//样本
    private Boolean isChecked = false;
    private SQLiteDatabase sdb;
    private PopupWindow popupWindow;
    private CheckBitmapUtils cb;//这个对象很耗内存回收掉
    private AnimationSet set;//动画集合
    private int D;
    private String unit;
    private String sampling_no;
    private EditText result;
    private EditText conclusion;
    private Boolean isback;//是否去背景
    //    private Toolbar toolbar;
//    private FrameLayout preview;
    private View view;//对话框ui
    private View view2;//对话框ui
    private EditText user;//用户名
    private EditText password;//密码
    private String us;//核准人
    private int No = 1;//默认为1
    private String cruveId;
    private ArrayList<PointValue> pList;
    int[] grays;//各灰度数组
    private String limit_max;//限值最大值
    private String limit_min;//限值最小值
    private Bitmap bitmap;//从相机得到的原始图片
    //    private ImageView iv_task;
    private ProgressDialog pd;


    private AlertDialog dialog1;
    private AlertDialog dialog3;
    private Cursor cursor;
    private List<String> check_list;
    private ArrayAdapter<String> adapter;
    private View popview;
    private ListView lv_content;

    private Camera mCamera;
    private CameraPreview mPreview;
    String card_typeid;
    private Bitmap recBitmap;
    private Bitmap reBitmap;
    private Bitmap whiteBitmap;

    String left_x;
    String left_y;
    int const_flag;
    int func_type_id;
    private String item;//检测项目

    String card_batch_id;
    String type_id;
    String detection_item_id;

    private String currentItem;//当前项目
    private String preItem;     //上一次项目

    private String item_id;

    private String conclusion1;//结论一
    private String conclusion2;//结论2
    private String conclusion3;//结论3
    private int part;
    private int off;
    private Float pot;//检测结果


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
                                pd.setMessage("正在检测。。。。。");
                                camera.takePicture(null, null, mPicture);
//                            mHandler.sendEmptyMessageDelayed(1, 5*1000);
                            }
                        }
                    });
                    break;
                case 3:
                    if (recBitmap != null) {


                        pd.setMessage("正在检测。。。。。。");

                        grays = new int[5];

                        try {

                            cb = new CheckBitmapUtils(recBitmap, mactivity, iv_cam, left_x, left_y, D, reBitmap, grays);
                            grays = cb.check();
                        } catch (Exception e) {
                            Snackbar.make(iv_task, "请检查标准曲线各个关联可能有错", Snackbar.LENGTH_LONG).show();
                            pd.dismiss();
                            return;
                        }

                        if (grays[3] != 0) {
                            cb = null;
                            if (const_flag == 1) {
                                pot = Float.valueOf(grays[3]);
                            } else {

                                if (func_type_id == 2) {
                                    pot = new ValueUtils(Float.valueOf(limit_min), Float.valueOf(limit_max), part, getYvalue(pList, grays[3] / grays[2])).math();
                                } else if (func_type_id == 1) {
                                    pot = new ValueUtils(Float.valueOf(limit_min), Float.valueOf(limit_max), part, getYvalue(pList, grays[3])).math();
                                }
                            }
                         /*   Log.i("66", "pot" + pot);
                            if (pot < Float.valueOf(limit_max) * 0.1) {
                                pot = (float) 0;
                                tv_result.setText(conclusion1);
                            } else {*/

                            if (pot >= Float.valueOf(limit_max) * (off / 100 + 1)) {

                                tv_result.setText(conclusion3);
                            } else if (pot < Float.valueOf(limit_max) * (off / 100 + 1) && pot >= Float.valueOf(limit_min) * (1 - off / 100)) {

                                tv_result.setText(conclusion2);
                            } else {

                                tv_result.setText(conclusion1);
                            }


                            pd.setMessage("检测完成");
                            pd.dismiss();

                            AlertDialog.Builder build1 = new AlertDialog.Builder(mactivity);
                            view = View.inflate(mactivity, R.layout.edit_dialog_checkinfo, null);
                            EditText item = (EditText) view.findViewById(R.id.et_item);
                            EditText sample = (EditText) view.findViewById(R.id.et_sample);

                            item.setText(et_item.getText().toString());
                            sample.setText(et_sampleType.getText().toString());

                            result = (EditText) view.findViewById(R.id.et_result);

                            conclusion = (EditText) view.findViewById(R.id.et_conclusion);


                            if (const_flag == 1) {
                                result.setText("……");
                            } else {
                                result.setText(pot + "");
                                result.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        double p = 0;
                                        try {
                                            p = Double.valueOf(s.toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        if (p >= Integer.valueOf(limit_max)) {
                                            conclusion.setTextColor(Color.RED);
                                            conclusion.setText(conclusion3);
                                        } else if (p < Integer.valueOf(limit_max) && p >= Integer.valueOf(limit_min)) {
                                            conclusion.setTextColor(Color.RED);
                                            conclusion.setText(conclusion2);
                                        } else {
                                            conclusion.setTextColor(Color.GREEN);
                                            conclusion.setText(conclusion1);
                                        }
                                    }
                                });
                            }


                            if (tv_result.getText().toString().equals("阴性") || tv_result.getText().toString().equals("合格")) {
                                conclusion.setTextColor(Color.GREEN);
                                conclusion.setText(tv_result.getText().toString());
                            } else {
                                conclusion.setTextColor(Color.RED);
                                conclusion.setText(tv_result.getText().toString());
                            }


                            view.findViewById(R.id.bt_pass).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    AlertDialog.Builder build2 = new AlertDialog.Builder(mactivity);
                                    view2 = View.inflate(mactivity, R.layout.edit_dialog_approval, null);

                                    user = (EditText) view2.findViewById(R.id.et_user);
                                    password = (EditText) view2.findViewById(R.id.et_password);

                                    view2.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            us = user.getText().toString().trim();
                                            String p = password.getText().toString().trim();

                                            if (checkuser(us, p) && isAdm(us)) {

                                                isChecked = true;
                                                tv_result.setText(conclusion.getText().toString());
                                                dialog3.dismiss();
                                            } else {
                                                Toast.makeText(mactivity, "密码不对或权限不够", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                    view2.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog3.dismiss();
                                        }
                                    });
                                    build2.setView(view2);
                                    dialog3 = build2.show();
                                }
                            });

                            view.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (isChecked) {

                                        tv_result.setText(conclusion.getText().toString());
                                        dialog1.dismiss();
                                    } else {
                                        dialog1.dismiss();
                                    }
                                    save2dataBase();
                                    isChecked = false;
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
                    break;
            }
        }
    };


    /**
     * 重写父类方法 初始化控件
     *
     * @param
     * @return
     */

    @Override
    public View initview(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.first, null);
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        initAnimotion();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initDate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 3 && requestCode == 33) {
            String sampleNo = data.getStringExtra("sampleNo");
            etSampleBh.setText(sampleNo);
            inputSample(sampleNo);
        }
    }

    private void inputSample(String sampleNo) {

        cursor = sdb.query("galaxy_detection_sample", new String[]{"sample_bh", "type_id",
                "sample_id",
                "company_produce_id", "company_sampled_id", "sample_batch", "submit_date", "detection_part"
        }, "sample_bh=?", new String[]{sampleNo}, null, null, null);
        while (cursor.moveToNext()) {
            et_sampleType.setText(Id2nameUtils.sampletype2name(cursor.getString(1), sdb));
            etSamplename.setText(Id2nameUtils.sample2name(cursor.getString(2), sdb));
            et_birth.setText(Id2nameUtils.company2name(cursor.getString(3), sdb));
            etChecked.setText(Id2nameUtils.company2name(cursor.getString(4), sdb));
            sample_batch = cursor.getString(5);
            submit_date = cursor.getString(6);
            detection_part = cursor.getString(7);

        }
        cursor.close();

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
     * 初始化数据 注意生命周期
     */

    public void initDate() {
        lv_check.setOnClickListener(this);
        iv_sample_bh.setOnClickListener(this);
        iv_task.setOnClickListener(this);
        iv_item.setOnClickListener(this);
        iv_card_batch.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }


    /**
     * 记得资源回收 资源释放
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        sdb.close();
       /* if (grays != null) {
            grays = null;
        }*/
    }


    /**
     * 点击处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.lv_check:

                String currentDate = SpUtils.getString(mactivity, "date1", null);
                if (currentDate != null) {
                    No = currentDate.equals(new SimpleDateFormat("yyyyMMdd").format(new Date())) ? Integer.valueOf(SpUtils.getString(mactivity, "No1", String.valueOf(1))) : 1;
                }

                String currentTime1 = new SimpleDateFormat("yyyyMMdd").format(new Date());
                SpUtils.putString(mactivity, "date1", currentTime1);
                StringBuffer sb = new StringBuffer();
                sb.append(SpUtils.getString(mactivity, "sbh", null));
                sb.append(currentTime1);
                sb.append(String.format("%03d", No));
                sampling_no = sb.toString();

                if (cruveId != null) {

                    pList = new ArrayList<PointValue>();
                    PointValue pv;
                    cursor = sdb.query("galaxy_standard_curve_child", new String[]{"curve_id", "x_gray", "y_potency"}, "curve_id=?", new String[]{cruveId}, null, null, null);
                    while (cursor.moveToNext()) {
                        pv = new PointValue();
                        String x = cursor.getString(1).trim();
                        String y = cursor.getString(2).trim();
                        if (!x.equals("0") && !y.equals("0")) {
                            pv.set(Float.valueOf(x), Float.valueOf(y));
                            pList.add(pv);
                        }
                    }
                    cursor.close();

                    Cursor cursor1 = sdb.query("galaxy_standard_curve", new String[]{"limited_max", "limited_min", "curve_id", "detection_conclusion1", "detection_conclusion2", "detection_conclusion3", "part", "off", "const_flag", "func_type_id", "background_valid"}, "curve_id=?", new String[]{cruveId}, null, null, null);
                    while (cursor1.moveToNext()) {
                        limit_max = cursor1.getString(0);
                        limit_min = cursor1.getString(1);

                        conclusion1 = Id2nameUtils.con2name(cursor1.getString(3), sdb);
                        conclusion2 = Id2nameUtils.con2name(cursor1.getString(4), sdb);
                        conclusion3 = Id2nameUtils.con2name(cursor1.getString(5), sdb);

                        try {

                            off = Integer.valueOf(cursor1.getString(7));
                            part = Integer.valueOf(cursor1.getString(6));
                            const_flag = Integer.valueOf(cursor1.getString(8));
                            func_type_id = Integer.valueOf(cursor1.getString(9));
                            if (cursor1.getString(10).equals("0")) {
                                isback = false;
                            } else {
                                isback = true;
                            }

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                    }
                } else {
                    Toast.makeText(mactivity, "没有匹配的标准曲线", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!TextUtils.isEmpty(et_card_batch.getText().toString())) {
                    pd = new ProgressDialog(mactivity);
                    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条

                    pd.setTitle("检测提示");
                    pd.setMessage("正在检测。。");
                    pd.setCancelable(true);// 设置是否可以通过点击Back键取消
                    pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                    pd.show();
                    mHandler.sendEmptyMessageDelayed(1, 2 * 1000); //7s 后开始启动相机

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
                            D = Integer.valueOf(cursor.getString(4));
                        }
                    }

                } else {
                    Toast.makeText(mactivity, "没有匹配的曲线，请先完善标准曲线", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_item:
                popview = View.inflate(mactivity, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_standard_curve", new String[]{"detection_item_id"}, null, null, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = Id2nameUtils.item2name(cursor.getString(0), sdb);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(mactivity, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
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
                        input(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;

            case R.id.iv_card_batch:
                popview = View.inflate(mactivity, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);

                if (!TextUtils.isEmpty(et_item.getText().toString())) {
//                    String type_id = Id2nameUtils.sampletype2id(et_sampleType.getText().toString(), sdb);
                    item_id = Id2nameUtils.item2id(et_item.getText().toString(), sdb);

                    if (item_id != null) {
                        cursor = sdb.query("galaxy_standard_curve", new String[]{"detection_item_id", "card_batch_id"}, "detection_item_id=? ", new String[]{item_id}, null, null, null);
                        check_list = new ArrayList<String>();
                        while (cursor.moveToNext()) {
                            String card_batch_id = cursor.getString(1);
                            if (card_batch_id != null) {
                                String batch = Id2nameUtils.cardBatch2name(card_batch_id, sdb);
                                currentItem = batch;
                                if (currentItem != null) {
                                    if (!currentItem.equals(preItem)) {
                                        preItem = batch;
                                        check_list.add(batch);
                                    }
                                }
                            }
                        }
                        preItem = null;
                        currentItem = null;
                    }
                } else {
                    cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch"}, null, null, null, null, null);
                    check_list = new ArrayList<String>();
                    while (cursor.moveToNext()) {
                        String part = cursor.getString(0);
                        check_list.add(part);
                    }
                }
                adapter = new ArrayAdapter<String>(mactivity, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_sampleType.getWidth(), -2, true);
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
                        if (!TextUtils.isEmpty(et_item.getText().toString().trim())) {
                            inputCurve(et_item.getText().toString().trim(), check_list.get(position));
                        }

                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;

                    }
                });
                break;

            case R.id.iv_tasks:
                popview = View.inflate(mactivity, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_task", new String[]{"task_bh", "detection_item","publish_date"}, null, null, null, null, "publish_date desc");
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0) + "(检测" + cursor.getString(1) + ")";

                    item = cursor.getString(1);
                    check_list.add(part);
                }
                if (check_list.size() > 0) {
                    check_list.add("非任务");
                }
                adapter = new ArrayAdapter<String>(mactivity, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_tasks.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_tasks);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (check_list.get(position).equals("非任务")) {
                            et_item.setText("");
                            et_card_batch.setText("");
                            etBy.setText("");
                            etMax.setText("");
                            et_tasks.setText(check_list.get(position));
                        } else {
                            et_tasks.setText(check_list.get(position).substring(0, check_list.get(position).indexOf("(")));
                            cursor = sdb.query("galaxy_detection_task", new String[]{"task_bh", "detection_item"}, "task_bh=? ", new String[]{check_list.get(position).substring(0, check_list.get(position).indexOf("("))}, null, null, null);
                            while (cursor.moveToNext()) {
                                input(cursor.getString(1));
                            }
                            cursor.close();
                        }
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_sample_bh:
                popview = View.inflate(mactivity, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_sample", new String[]{"sample_bh"}, null, null, null, null, "sample_bh desc");

                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(mactivity, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, etSampleBh.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(etSampleBh);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        etSampleBh.setText(check_list.get(position));
                        inputSample(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_add:
                Intent i = new Intent(mactivity, MainActivity.class);
                i.putExtra("fast", "0");
                startActivityForResult(i, 33);
                break;
        }
    }

    /**
     * 自动填写s
     *
     * @param item
     */
    private void input(String item) {
        et_item.setText(item);
        et_card_batch.setText("");
        etBy.setText("");
        etMax.setText("");
        item_id = Id2nameUtils.item2id(item, sdb);
        if (item_id != null) {

            cursor = sdb.query("galaxy_standard_curve", new String[]{"detection_item_id", "card_batch_id"}, "detection_item_id=? ", new String[]{item_id}, null, null, null);
            check_list = new ArrayList<String>();
            while (cursor.moveToNext()) {
                String card_batch_id = cursor.getString(1);
                String batch = Id2nameUtils.cardBatch2name(card_batch_id, sdb);
                currentItem = batch;
                if (currentItem != null) {
                    if (!currentItem.equals(preItem)) {
                        preItem = batch;
                        check_list.add(batch);
                    }
                }
            }
            cursor.close();
            preItem = null;
            currentItem = null;
            try {
                if (check_list.size() == 0) {
                    Toast.makeText(mactivity, "没有匹配的曲线，请先完善标准曲线", Toast.LENGTH_SHORT).show();
                    return;
                }
                et_card_batch.setText(check_list.get(0));
                inputCurve(item, check_list.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void inputCurve(String item, String s) {

        cursor = sdb.query("galaxy_standard_curve", new String[]{"card_batch_id", "detection_item_id", "curve_id"}, "card_batch_id=? and detection_item_id=? ", new String[]{Id2nameUtils.cardBatch2id(s, sdb), Id2nameUtils.item2id(item, sdb)}, null, null, null);
        while (cursor.moveToNext()) {
            cruveId = cursor.getString(2);
        }
        if (cruveId != null) {
            cursor = sdb.query("galaxy_standard_curve", new String[]{"curve_id", "limited_min", "standard_basis", "limited_unit_id"}, "curve_id=?", new String[]{cruveId}, null, null, null);
            while (cursor.moveToNext()) {
                etBy.setText(cursor.getString(2));
                unit = Id2nameUtils.u2name(cursor.getString(3), sdb);
                if (unit == null) {
                    etMax.setText(cursor.getString(1));
                } else {
                    etMax.setText(cursor.getString(1) + unit);
                }
            }
        } else {
            Toast.makeText(mactivity, "没有匹配的曲线，请先完善标准曲线", Toast.LENGTH_SHORT).show();
        }

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
            if (pass.equals(passd)) {
                return true;
            }
        }
        cursor.close();
        return false;
    }

    /**
     * 根据t灰度求浓度
     *
     * @param
     * @param x
     * @return
     */
    public float getYvalue(ArrayList<PointValue> pList, float x) {                                           //根据x值求y值
        float result = 0.00F;

        if (pList.size() < 3) {
            result = -1.00F;  //返回值-2表示曲线点集为空
            return result;
        }

        if (x < pList.get(0).getX())//延长线计算1
        {
            float ytemp = (pList.get(0).getX() - pList.get(1).getY()) * (x - pList.get(0).getX()) / (pList.get(0).getX() - pList.get(1).getY()) + pList.get(0).getY();
            result = ytemp > 0.00 ? ytemp : 0.00F;
        }
        if (x > pList.get(pList.size() - 1).getX())//延长线计算2  pList.get(pList.size()-1).getX()
        {
            float ytemp = (pList.get(pList.size() - 1).getY() - pList.get(pList.size() - 2).getY()) * (x - pList.get(pList.size() - 1).getX()) / (pList.get(pList.size() - 1).getX() - pList.get(pList.size() - 2).getX()) + pList.get(pList.size() - 1).getY();
            result = ytemp;
        }
        if (x == pList.get(pList.size() - 1).getX()) {
            result = pList.get(pList.size() - 1).getY();
        }
        if (x < pList.get(pList.size() - 1).getX() && x >= pList.get(0).getX()) {
            for (int i = 0; i < pList.size() - 1; i++) {
                if (x < pList.get(i + 1).getX() && x >= pList.get(i).getX()) {
                    float temp = (x - pList.get(i).getX()) / (pList.get(i + 1).getX() - pList.get(i).getX());
                    if (0 == temp) {
                        result = pList.get(i).getY();
                    } else
                        result = (pList.get(i + 1).getY() - pList.get(i).getY()) * temp + pList.get(i).getY();
                }
            }
        }
        return result;
    }


    /**
     * 存入数据库
     */
    private void save2dataBase() {

        ContentValues cv = new ContentValues();
        ContentValues cv1 = new ContentValues();

        if (isChecked) {
            cv.put("detection_result", result.getText().toString());

            cv.put("detection_conclusion", conclusion.getText().toString());
        } else {
            cv.put("detection_result", result.getText().toString());
            cv.put("detection_conclusion", tv_result.getText().toString());
        }
        cv.put("upload_valid", 0);//未上传
        cv.put("detection_bh", sampling_no);//检测编号
        cv.put("sample_bh", etSampleBh.getText().toString().trim());//样本编号
        cv.put("type_name", et_sampleType.getText().toString().trim());//样本类型
        cv.put("sample_name", etSamplename.getText().toString().trim());//样品名称
        cv.put("detection_item_name", et_item.getText().toString().trim());//检测项目
        cv.put("limited_min", etMax.getText().toString().trim());//检测限值
        cv.put("limited_unit", unit);//单位
        cv.put("task_source", et_tasks.getText().toString().trim());//任务来源
        cv.put("detection_date", new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()));//检测日期
        cv.put("detection_person_name", SpUtils.getString(mactivity, "user", null));//检测人员
        cv.put("company_sampled", etChecked.getText().toString().trim());//被检公司
        cv.put("company_produce", et_birth.getText().toString().trim());//生产公司

        if (!TextUtils.isEmpty(et_birth.getText().toString())) {
            cursor = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_province",
                    "company_city", "company_county", "company_towns", "company_village",
            }, "company_name=?", new String[]{et_birth.getText().toString()}, null, null, null);
            while (cursor.moveToNext()) {
                cv.put("source_province", cursor.getString(1));//来源省
                cv.put("source_city", cursor.getString(2));//来源市
                cv.put("source_county", cursor.getString(3));//来源县
                cv.put("source_town", cursor.getString(4));//来源镇
            }
            cursor.close();

        }

        cv.put("sample_batch", sample_batch);//样本批次
        cv.put("submit_date", submit_date);//送检日期
        cv.put("detection_method", "胶体金法");//检测方法
        cv.put("detection_part", detection_part);
        cv.put("detection_station_bh", SpUtils.getString(mactivity, "sbh", null));
        cv.put("detection_station", SpUtils.getString(mactivity, "sname", null));

        if (SpUtils.getString(mactivity, "LocationDescribe", null) != null) {
            cv.put("longitude", SpUtils.getString(mactivity, "Longitude", null));
            cv.put("latitude", SpUtils.getString(mactivity, "Latitude", null));
        } else {
            cv.put("longitude", "");
            cv.put("latitude", "");
        }

        cv.put("device_bh", SpUtils.getString(mactivity, "device", null));
        cv.put("is_checked", 0);
        cv.put("checker", "1");
        cv.put("report_bh", "1");
        cv.put("report_time", "1");
        cv.put("process_result", "1");
        cv.put("is_printed", 0);
        cv.put("is_reported", 0);

        cv1.put("detection_bh", sampling_no);
        cv1.put("gray_cb1", grays[1]);
        cv1.put("gray_cb2", grays[0]);
        cv1.put("gray_tb1", grays[0]);
        cv1.put("gray_c", grays[2]);
        cv1.put("gray_t", grays[3]);
        cv1.put("gray_tb2", grays[4]);
        cv1.put("curvd_id", cruveId);


        long d = sdb.insert("galaxy_detection_record", null, cv);
        sdb.insert("galaxy_detection_recordcb", null, cv1);

        if (d == -1) {
            Toast.makeText(mactivity, "保存失败", Toast.LENGTH_SHORT).show();
        } else {
            if (grays != null) {
                grays = null;
            }
            Toast.makeText(mactivity, "保存成功", Toast.LENGTH_SHORT).show();
            SpUtils.putString(mactivity, "No1", String.valueOf(No + 1));
        }
    }

    /**
     * 初始化设置Camera.打开摄像头
     * <p/>
     * 如果没有前置摄像头的话就打开后置摄像头
     */

    private void openCamera() {
        pd.setMessage("正在检测。。。");
        mCamera = getCameraInstance();
        if (mCamera != null) {
            mPreview = new CameraPreview(mactivity, mCamera);
            preview.removeAllViews();
            preview.addView(mPreview);
            mCamera.startPreview();
        }
//        mHandler.sendEmptyMessage(2);
        mHandler.sendEmptyMessageDelayed(2, 5 * 1000); //4s后拍照

    }

    Canvas canvas;
    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 获取Jpeg图片，并保存在sd卡上
            try {

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                double d = new HorizontalUtils(mactivity, bitmap).getRoat();  //显得到bitmap图片
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
                if (whiteBitmap != null && isback) {
                    recBitmap = ImageProcess.GetGrayCorrectionImage(whiteBitmap, Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false));
                } else {
                    recBitmap = Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false);
                }
                reBitmap = Bitmap.createBitmap(copyBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false);
                pd.setMessage("正在检测。。。。");
//                iv_cam.setImageBitmap(recBitmap);
                bitmap.recycle();
                copyBitmap.recycle();
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
                mPreview.clearFocus();
                mPreview = null;
                mCamera.setPreviewDisplay(null);
                mCamera.stopPreview();
                mCamera.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);

        tv_result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().equals("阴性") || editable.toString().equals("合格")) {
                    tv_result.setTextColor(Color.GREEN);
                } else {
                    tv_result.setTextColor(Color.RED);
                }

            }
        });
        return rootView;
    }
}

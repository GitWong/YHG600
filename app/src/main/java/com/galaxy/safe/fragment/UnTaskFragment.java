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
import android.content.ContentResolver;
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
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.TextView;
import android.widget.Toast;

import com.galaxy.safe.R;
import com.galaxy.safe.base.BaseFragment;
import com.galaxy.safe.ui.ApplySettingActivity;
import com.galaxy.safe.utils.CameraPreview;
import com.galaxy.safe.utils.CheckBitmapUtils;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.HorizontalUtils;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.utils.ImageProcess;
import com.galaxy.safe.utils.SpUtils;
import com.galaxy.safe.utils.ToGray;
import com.galaxy.safe.utils.ValueUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;


public class UnTaskFragment extends BaseFragment implements View.OnClickListener {


    private ImageView iv_cam;

    private LinearLayout lv_check;

    private TextView tv_result;
    private Boolean isChecked = false;
    private SQLiteDatabase sdb;
    private PopupWindow popupWindow;
    int[] grays;//各灰度数组

    private CheckBitmapUtils cb;//这个对象很耗内存回收掉
    private AnimationSet set;//动画集合

    private EditText et_testing_no;//抽样单号
    private ImageView iv_testing_no;
    private String sample_bh;//样本编号
    private String sample;//样本

    private ImageView iv_add;

    private EditText et_sampleType;
    private ImageView iv_sampleType;//样本类型

    private EditText et_item;
    private ImageView iv_item;//检测项目

    private Bitmap grayBitmap;

    private EditText et_card_batch;
    private ImageView iv_card_batch;//卡批次

    private EditText et_detection_person;
    private ImageView iv_detection_person;//检测人
    private int D;
    int const_flag;
    int func_type_id;

    private String company_id;
    private String company_ea;
    private String sampling_no;
    private EditText result;
    private EditText conclusion;
//    private Toolbar toolbar;


    private FrameLayout preview;
    private View view;//对话框ui
    private View view2;//对话框ui
    private EditText user;//用户名
    private EditText password;//密码
    private String us;//核准人
    private int No = 1;//默认为1
    private String cruveId;
    private ArrayList<PointValue> pList;

    private String limit_max;//限值最大值
    private String limit_min;//限值最小值

    private Bitmap bitmap;//从相机得到的原始图片


    /**
     * 重写父类方法 初始化控件
     *
     * @param
     * @return
     */


    @Override
    public View initview(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.task, null);
        iv_cam = (ImageView) view.findViewById(R.id.iv_cam);
        iv_cam.setImageResource(R.drawable.white);

//        lv_result = (LinearLayout) view.findViewById(R.id.lv_result);
        lv_check = (LinearLayout) view.findViewById(R.id.lv_check);
//        lv_recheck = (LinearLayout) view.findViewById(R.id.lv_recheck);
        tv_result = (TextView) view.findViewById(R.id.tv_des);

        preview = (FrameLayout) view.findViewById(R.id.camera_preview);

        et_testing_no = (EditText) view.findViewById(R.id.et_testing_no);
        iv_testing_no = (ImageView) view.findViewById(R.id.iv_testing_no);
        iv_add = (ImageView) view.findViewById(R.id.iv_add);

        et_sampleType = (EditText) view.findViewById(R.id.et_sampleType);
//        iv_sampleType = (ImageView) view.findViewById(R.id.iv_sampleType);

        et_item = (EditText) view.findViewById(R.id.et_item);
//        iv_item = (ImageView) view.findViewById(R.id.iv_item);

        et_card_batch = (EditText) view.findViewById(R.id.et_card_batch);
        iv_card_batch = (ImageView) view.findViewById(R.id.iv_card_batch);

        et_detection_person = (EditText) view.findViewById(R.id.et_detection_person);
        iv_detection_person = (ImageView) view.findViewById(R.id.iv_detection_person);

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库


        initAnimotion();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (SpUtils.getString(mactivity, "user", "") != null) {
            Cursor cursor1 = sdb.query("galaxy_detection_person", new String[]{"user_name", "person_name"}, "user_name=?", new String[]{SpUtils.getString(mactivity, "user", "")}, null, null, null);
            while (cursor1.moveToNext()) {
                et_detection_person.setText(cursor1.getString(1));
            }
            cursor1.close();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      /*  if (resultCode == -1) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Toast.makeText(mactivity, "获取图片失败", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (resultCode == 6 && requestCode == 16) {
            String test_no = data.getStringExtra("test_no");
            et_testing_no.setText(test_no);
            input(test_no);
        }
        ContentResolver resolver = mactivity.getContentResolver();
        if (requestCode == 66) {

            File f = new File(Environment.getExternalStorageDirectory()
                    + "/" + "pic" + "/" + "hh");
            try {

                Uri u = Uri.parse(MediaStore.Images.Media.insertImage(mactivity.getContentResolver(),
                        f.getAbsolutePath(), null, null));
                bitmap = MediaStore.Images.Media.getBitmap(resolver, u);
                double d = new HorizontalUtils(mactivity, bitmap).getRoat();  //显得到bitmap图片
//                iv_cam.setImageBitmap(bitmap);
//                bitmap=BitmapFactory.decodeResource(mactivity.getResources(),R.drawable.a);
                Log.i("22", "图片高度" + bitmap.getHeight());
                Log.i("22", "图片宽度" + bitmap.getWidth());
                Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth() * 280 / 1000, bitmap.getHeight() * 280 / 1000, bitmap.getConfig());
                //2.临摹作画
                //创建画板 ,参考空白纸张的大小，把画板创建出来。
                Canvas canvas = new Canvas(copyBitmap);
                //3.作画
                //创建一个画笔
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);//设置默认的颜色。
                Matrix matrix = new Matrix();
                matrix.setRotate((float) -d, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);
                matrix.postScale(0.28f, 0.28f); //长和宽放大缩小的比例
                canvas.drawBitmap(bitmap, matrix, paint);

                grayBitmap = ToGray.toGray(copyBitmap);
                Log.i("22", "图片高度" + grayBitmap.getHeight());
                Log.i("22", "图片宽度" + grayBitmap.getWidth());

//                recBitmap = Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false);
                if (whiteBitmap == null) {
                    recBitmap = Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false);
                } else {
                    recBitmap = ImageProcess.GetGrayCorrectionImage(whiteBitmap, Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false));
                }
                reBitmap = Bitmap.createBitmap(copyBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false);
                if (recBitmap != null) {
                    grays = new int[5];
                    try {
                        cb = new CheckBitmapUtils(recBitmap, mactivity, iv_cam, left_x, left_y, D, reBitmap, grays);
                    } catch (Exception e) {
                        Snackbar.make(iv_cam, "请检查标准曲线各个关联可能有错", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    grays = cb.check();

                    if (grays[3] != 0) {
                        final ProgressDialog dialog2 = new ProgressDialog(mactivity);
                        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                        dialog2.setTitle("正在检测...");
                        dialog2.setCancelable(true);// 设置是否可以通过点击Back键取消
                        dialog2.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                        dialog2.show();
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                int i = 0;
                                while (i < 10) {
                                    try {
                                        Thread.sleep(200);
                                        // 更新进度条的进度,可以在子线程中更新进度条进度
                                        dialog2.incrementProgressBy(15);
                                        // dialog.incrementSecondaryProgressBy(10)//二级进度条更新方式
                                        i++;
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                }
                                // 在进度条走完时删除Dialog
                                dialog2.dismiss();
                                mactivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mactivity, "检测完毕", Toast.LENGTH_SHORT).show();
                                        if (const_flag == 1) {
                                            pot = Float.valueOf(grays[3]);
                                        } else {
                                            if (func_type_id == 5) {
                                                pot = new ValueUtils(Float.valueOf(limit_min), Float.valueOf(limit_max), part, getYvalue(pList, grays[3] / grays[2])).math();
                                            } else {
                                                pot = new ValueUtils(Float.valueOf(limit_min), Float.valueOf(limit_max), part, getYvalue(pList, grays[3])).math();
                                            }
                                        }
//                                        pot = new ValueUtils(Float.valueOf(limit_min), Float.valueOf(limit_max), part, getYvalue(pList, grays[3])).math();
                                        Log.i("22", "pot" + pot);

                                        if (pot < Float.valueOf(limit_max) * 0.1) {
                                            pot = (float) 0;
                                            tv_result.setText("不含有目标项目");
                                        } else {

                                            if (pot >= Float.valueOf(limit_max) * (off / 100 + 1)) {
                                                tv_result.setText(conclusion3);
                                            } else if (pot < Float.valueOf(limit_max) * (off / 100 + 1) && pot >= Float.valueOf(limit_min) * (1 - off / 100)) {
                                                tv_result.setText(conclusion2);
                                            } else {
                                                tv_result.setText(conclusion1);
                                            }
                                        }
                                        AlertDialog.Builder build1 = new AlertDialog.Builder(mactivity);
                                        view = View.inflate(mactivity, R.layout.edit_dialog_checkinfo, null);
                                        EditText item = (EditText) view.findViewById(R.id.et_item);
                                        EditText sample = (EditText) view.findViewById(R.id.et_sample);
                                        item.setText(et_item.getText().toString());
                                        sample.setText(et_sampleType.getText().toString());

                                        result = (EditText) view.findViewById(R.id.et_result);
                                        conclusion = (EditText) view.findViewById(R.id.et_conclusion);
                                        result.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                                int p = 0;
                                                try {
                                                    p = Integer.valueOf(s.toString());
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                if (p >= Integer.valueOf(limit_max)) {
                                                    conclusion.setText(conclusion3);
                                                } else if (p < Integer.valueOf(limit_max) && p >= Integer.valueOf(limit_min)) {
                                                    conclusion.setText(conclusion2);
                                                } else {
                                                    conclusion.setText(conclusion1);
                                                }
                                            }
                                        });

                                        result.setText(pot + "");
                                        conclusion.setText(tv_result.getText().toString());
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
                                });
                            }
                        }).start();
                    } else {
                        Toast.makeText(mactivity, "请先采取数据", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mactivity, "请先完善信息", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 自动填写
     *
     * @param testing_no
     */
    private void input(String testing_no) {
        et_card_batch.setText("");
        et_sampleType.setText("");
        et_item.setText("");

        Cursor cursor1 = sdb.query("galaxy_application_form", new String[]{"testing_no", "sample_bh"}, "testing_no=?", new String[]{testing_no}, null, null, null);
        while (cursor1.moveToNext()) {
            sample_bh = cursor1.getString(1);
        }
        Cursor cursor3 = sdb.query("galaxy_detection_sample", new String[]{"sample_id", "sample_bh"}, "sample_bh=?", new String[]{sample_bh}, null, null, null);
        while (cursor3.moveToNext()) {
            sample = cursor3.getString(0);
        }
        if (sample != null) {
            Cursor cursor4 = sdb.query("galaxy_sample_typedetail", new String[]{"sample_id", "type_id"}, "sample_id=?", new String[]{sample}, null, null, null);
            while (cursor4.moveToNext()) {
                et_sampleType.setText(Id2nameUtils.sampletype2name(cursor4.getString(1), sdb));
            }
            cursor4.close();
        }
        Cursor cursor2 = sdb.query("galaxy_application_formcb", new String[]{"testing_no", "detection_item_id"}, "testing_no=?", new String[]{testing_no}, null, null, null);
        while (cursor2.moveToNext()) {
            et_item.setText(Id2nameUtils.item2name(cursor2.getString(1), sdb));
        }
        cursor1.close();
        cursor2.close();
        cursor3.close();

        if (!TextUtils.isEmpty(et_item.getText().toString())) {
//                    String type_id = Id2nameUtils.sampletype2id(et_sampleType.getText().toString(), sdb);
            item_id = Id2nameUtils.item2id(et_item.getText().toString(), sdb);
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
                    }
                    et_card_batch.setText(check_list.get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
     * 初始化数据 注意生命周期
     */

    public void initDate() {


        lv_check.setOnClickListener(this);
//        initToolbar();
      /*  lv_recheck.setOnClickListener(this);
        lv_result.setOnClickListener(this);*/
        iv_testing_no.setOnClickListener(this);
//        iv_sampleType.setOnClickListener(this);
//        iv_item.setOnClickListener(this);
        iv_card_batch.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        iv_detection_person.setOnClickListener(this);


    }


    /**
     * 记得资源回收 资源释放
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        sdb.close();

        if (grays != null) {
            grays = null;
        }

    }


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
                String user1 = SpUtils.getString(mactivity, "user", "");
                if (user1 != null) {
                    Cursor cursor1 = sdb.query("galaxy_detection_person", new String[]{"user_name", "company_id"}, "user_name=?", new String[]{user1}, null, null, null);
                    while (cursor1.moveToNext()) {
                        company_id = cursor1.getString(1);
                    }
                }
                if (company_id != null) {
                    Cursor cursor2 = sdb.query("galaxy_detection_company", new String[]{"company_ea", "company_id"}, "company_id=?", new String[]{company_id}, null, null, null);
                    while (cursor2.moveToNext()) {
                        company_ea = cursor2.getString(0);
                    }
                }
                String currentTime1 = new SimpleDateFormat("yyyyMMdd").format(new Date());
                SpUtils.putString(mactivity, "date1", currentTime1);
                StringBuffer sb = new StringBuffer();
                sb.append(company_ea);
                sb.append(currentTime1);
                sb.append(String.format("%05d", No));
                Log.i("22", No + "");
                sampling_no = sb.toString();

                if (!TextUtils.isEmpty(et_sampleType.getText().toString()) && !TextUtils.isEmpty(et_item.getText().toString()) && !TextUtils.isEmpty(et_card_batch.getText().toString())) {
                    card_batch_id = Id2nameUtils.cardBatch2id(et_card_batch.getText().toString(), sdb);
                    type_id = Id2nameUtils.sampletype2id(et_sampleType.getText().toString(), sdb);
                    detection_item_id = Id2nameUtils.item2id(et_item.getText().toString(), sdb);
                    if (card_batch_id != null && type_id != null && detection_item_id != null) {
                        cursor = sdb.query("galaxy_standard_curve", new String[]{"card_batch_id", "detection_item_id", "curve_id"}, "card_batch_id=? and detection_item_id=? ", new String[]{card_batch_id, detection_item_id}, null, null, null);
                        while (cursor.moveToNext()) {
                            cruveId = cursor.getString(2);
                        }
                    }
                } else {
                    Toast.makeText(mactivity, "请先采取数据", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (cruveId != null) {
                    Cursor cursor1 = sdb.query("galaxy_standard_curve", new String[]{"limited_max", "limited_min", "x_gray", "y_potency", "curve_id", "detection_conclusion1", "detection_conclusion2", "detection_conclusion3", "part", "off", "const_flag", "func_type_id"}, "curve_id=?", new String[]{cruveId}, null, null, null);
                    pList = new ArrayList<PointValue>();

                    PointValue pv;
                    while (cursor1.moveToNext()) {
                        limit_max = cursor1.getString(0);
                        limit_min = cursor1.getString(1);

                        conclusion1 = Id2nameUtils.con2name(cursor1.getString(5), sdb);
                        conclusion2 = Id2nameUtils.con2name(cursor1.getString(6), sdb);
                        conclusion3 = Id2nameUtils.con2name(cursor1.getString(7), sdb);
                        try {
                            part = Integer.valueOf(cursor1.getString(8));
                            off = Integer.valueOf(cursor1.getString(9));
                            const_flag = Integer.valueOf(cursor1.getString(10));
                            func_type_id = Integer.valueOf(cursor1.getString(11));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        pv = new PointValue();
                        if (Float.valueOf(cursor1.getString(2)) != (0.0) && Float.valueOf(cursor1.getString(3)) != (0.0)) {
                            pv.set(Float.valueOf(cursor1.getString(2)), Float.valueOf(cursor1.getString(3)));
                            pList.add(pv);
                        }
                    }
                } else {
                    Toast.makeText(mactivity, "没有匹配的标准曲线", Toast.LENGTH_SHORT).show();
                    return;
                }

                final String card_batch = et_card_batch.getText().toString();


                if (card_batch != null) {

                    String status = Environment.getExternalStorageState();
                    if (status.equals(Environment.MEDIA_MOUNTED)) {

                        File dir = new File(Environment.getExternalStorageDirectory() + "/" + "pic");
                        if (!dir.exists()) dir.mkdirs();
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(dir, "hh");//localTempImgDir和localTempImageFileName是自己定义的名字
                        Uri u = Uri.fromFile(f);
                        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                        startActivityForResult(intent, 66);
                    }
                    cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch", "card_type_id"}, "card_batch=?", new String[]{card_batch}, null, null, null);
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
                    Toast.makeText(mactivity, "请先完善信息", Toast.LENGTH_SHORT).show();
                }
//                openCamera();
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
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;

                    }
                });
                break;
            case R.id.iv_detection_person:
                popview = View.inflate(mactivity, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_detection_person", new String[]{"person_name", "person_property"}, "person_property=? or person_property=?", new String[]{"检测员", "系统管理员"}, null, null, null);
                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(mactivity, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_detection_person.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_detection_person);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_detection_person.setText(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_testing_no:
                popview = View.inflate(mactivity, R.layout.popup_item, null);
                lv_content = (ListView) popview.findViewById(R.id.lv_content);
                cursor = sdb.query("galaxy_application_form", new String[]{"testing_no"}, null, null, null, null, "testing_no desc");

                check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                adapter = new ArrayAdapter<String>(mactivity, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                popupWindow = new PopupWindow(popview, et_testing_no.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_testing_no);
                popupWindow.setOutsideTouchable(false);
                popview.startAnimation(set);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        et_testing_no.setText(check_list.get(position));
                        input(check_list.get(position));
                        popupWindow.dismiss();
                        popupWindow = null;
                        adapter = null;
                    }
                });
                break;
            case R.id.iv_add:
                Intent i = new Intent(mactivity, ApplySettingActivity.class);
                i.putExtra("unTask", "yes");
                startActivityForResult(i, 16);
                break;
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
            cv1.put("detection_result", result.getText().toString());

            cv1.put("detection_conclusion", conclusion.getText().toString());
        } else {
            cv1.put("detection_result", pot);

            cv1.put("detection_conclusion", tv_result.getText().toString());
        }

        cv1.put("detection_item_id", Id2nameUtils.item2id(et_item.getText().toString(), sdb));
        cv1.put("detection_time", new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date()));
        cv1.put("left_background", grays[1]);
        cv1.put("mid_background", grays[0]);
        cv1.put("c_background", grays[2]);
        cv1.put("t_background", grays[3]);
        cv1.put("right_background", grays[4]);

        cv1.put("standard_value", limit_min);
        cv1.put("detection_gist", "");//待补充

        cv.put("report_bh", "");//ok
        cv.put("sample_bh", sample_bh);

        cv.put("sampling_no", sampling_no);
        cv1.put("sampling_no", sampling_no);

        cv.put("authorized_person", "");
        cv.put("inspector", "");//ok
        cv.put("testing_no", et_testing_no.getText().toString());

        cv.put("detection_person_id", Id2nameUtils.person2id(et_detection_person.getText().toString(), sdb));
        cv.put("report_conclusion", 0);
        cv.put("curve_id", cruveId);


        cv.put("approved_result", 0);//ok
        cv.put("approval_time", "");//ok
        cv.put("report_time", "");//ok
        cv.put("report_upload", 0);

        cv.put("publish_ok", "");
        cv.put("report_print", 0);

        if (SpUtils.getString(mactivity, "LocationDescribe", null) != null) {
            cv.put("longitude", SpUtils.getString(mactivity, "Longitude", null));
            cv.put("latitude", SpUtils.getString(mactivity, "Latitude", null));
            cv.put("detection_address", SpUtils.getString(mactivity, "LocationDescribe", null));
        } else {
            cv.put("longitude", "");
            cv.put("latitude", "");
            cv.put("detection_address", "");
        }
        long d = sdb.insert("galaxy_detection_record", null, cv);
        sdb.insert("galaxy_detection_recordcb", null, cv1);

        if (d == -1) {
            Toast.makeText(mactivity, "保存失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mactivity, "保存成功", Toast.LENGTH_SHORT).show();
            SpUtils.putString(mactivity, "No1", String.valueOf(No + 1));
        }
    }

    /**
     * Id转名称
     *
     * @param s
     * @return
     */
    private String stoname(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_sample_typedetail", new String[]{"sample_id", "sample_name"}, "sample_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String sample_name = cursor2.getString(1);
                return sample_name;
            }
            cursor2.close();
        }
        return null;
    }

    private String ptoname(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_person", new String[]{"person_id", "person_name"}, "person_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String person_name = cursor2.getString(1);
                return person_name;
            }
            cursor2.close();
        }

        return null;
    }

    private String ptoid(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_person", new String[]{"person_id", "user_name"}, "user_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String person_id = cursor2.getString(0);
                return person_id;
            }
            cursor2.close();
        }
        return null;
    }

    private String ctoname(String s) {
        if (s != null) {
            Cursor cursor2 = sdb.query("galaxy_detection_company", new String[]{"company_id", "company_name"}, "company_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String company_name = cursor2.getString(1);
                return company_name;
            }
            cursor2.close();
        }

        return null;
    }

    /**
     * 初始化设置Camera.打开摄像头
     * <p/>
     * 如果没有前置摄像头的话就打开后置摄像头
     */

    private void openCamera() {



      /*  mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {

                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                    //mCamera.startPreview();

                    Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth() * 240 / 1000, bitmap.getHeight() * 240 / 1000, bitmap.getConfig());
                    //2.临摹作画
                    //创建画板 ,参考空白纸张的大小，把画板创建出来。
                    Canvas canvas = new Canvas(copyBitmap);
                    //3.作画
                    //创建一个画笔
                    Paint paint = new Paint();
                    paint.setColor(Color.BLACK);//设置默认的颜色。
                    Matrix matrix = new Matrix();
                    matrix.setRotate((float) -1.5, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);
                    matrix.postScale(0.24f, 0.24f); //长和宽放大缩小的比例
                    canvas.drawBitmap(bitmap, matrix, paint);

                    grayBitmap = ToGray.toGray(copyBitmap);
                    Log.i("22", "图片高度" + grayBitmap.getHeight());
                    Log.i("22", "图片宽度" + grayBitmap.getWidth());

                    recBitmap = Bitmap.createBitmap(grayBitmap, 80, 120, 320, 150, null, false);
//                    iv_cam.setImageBitmap(recBitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    releaseCamera();
                }
            }});*/
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(mactivity, mCamera);
        preview.addView(mPreview);

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (mCamera != null) {
                    mCamera.takePicture(null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            try {

                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                //mCamera.startPreview();

                                Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth() * 240 / 1000, bitmap.getHeight() * 240 / 1000, bitmap.getConfig());
                                //2.临摹作画
                                //创建画板 ,参考空白纸张的大小，把画板创建出来。
                                Canvas canvas = new Canvas(copyBitmap);
                                //3.作画
                                //创建一个画笔
                                Paint paint = new Paint();
                                paint.setColor(Color.BLACK);//设置默认的颜色。
                                Matrix matrix = new Matrix();
                                matrix.setRotate((float) -1.5, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);
                                matrix.postScale(0.24f, 0.24f); //长和宽放大缩小的比例
                                canvas.drawBitmap(bitmap, matrix, paint);

                                grayBitmap = ToGray.toGray(copyBitmap);
                                Log.i("22", "图片高度" + grayBitmap.getHeight());
                                Log.i("22", "图片宽度" + grayBitmap.getWidth());

                                recBitmap = Bitmap.createBitmap(grayBitmap, 80, 120, 320, 150, null, false);

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                releaseCamera();
                            }
                        }
                    });
                }
            }
        }.start();
    }


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
                mCamera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

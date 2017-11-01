package com.galaxy.safe.ui;

import android.app.ActionBar;
import android.app.Activity;
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
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.galaxy.safe.Bean.DrawPath;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.CameraPreview;
import com.galaxy.safe.utils.CheckBitmapUtils;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.DensityUtil;
import com.galaxy.safe.utils.GrayUtils;
import com.galaxy.safe.utils.HorizontalUtils;
import com.galaxy.safe.utils.ImageProcess;
import com.galaxy.safe.utils.MatchCUtils;
import com.galaxy.safe.utils.ToGray;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PointValue;


/**
 * Created by Dell on 2016/4/8.
 */
public class CardTypeSettingActivity extends Activity implements View.OnClickListener {

    private EditText et_card_type;
    private EditText et_line_num;
    private EditText et_left_upx;
    private EditText et_left_upy;
    private EditText et_jianju1;
    private EditText et_jianju2;
    private EditText et_jianju3;
    private EditText et_jianju4;

    private Button bt_cam;
    private Button bt_src;
    private Button bt_bc;
    private Button bt_save;

    private ImageView iv_cam;
    private RelativeLayout rl_layout;

    private RadioGroup rg;
    private RadioButton rb_no;
    private RadioButton rb_yes;

    private RadioGroup rg_length;
    private RadioButton rb_1;
    private RadioButton rb_2;
    private RadioButton rb_3;
    private RadioButton rb_4;

    private Camera mCamera;
    private CameraPreview mPreview;
    private FrameLayout preview;

    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private Cursor cursor;

    private Bitmap bitmap;//从相机得到的原始图片
    private Bitmap grayBitmap;//灰度化后处理的图片
    private Bitmap white;//灰度化后处理的图片


    private final String IMAGE_TYPE = "image/*";
    private final int IMAGE_CODE = 0;

    private ImageView rectview;
    private int startX;
    private int startY;
    private int upY;
    private int upX;
    private String type;
    private int max;//弄id用的
    private int D;

    private Bitmap recBitmap;//在屏幕上的初图

    private int x1, y1;//利用算法 求得匹配度最高的坐标
    private int x2, y2;//利用算法 最不匹配的坐标
    RelativeLayout.LayoutParams layoutParams;
    private ProgressDialog pd;
    Canvas canvas;
    Paint paint;
    private ArrayList<DrawPath> paths;
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
                                pd.setMessage("初始化完成，准备拍照读取。。。");
                                camera.takePicture(null, null, mPicture);
//                            mHandler.sendEmptyMessageDelayed(1, 5*1000);
                            }
                        }
                    });
                    break;
            }
        }
    };


//   private static RelativeLayout.LayoutParams layoutParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardtypesetting);
        initView();
        initToolBar();
        initData();

        type = getIntent().getStringExtra("card_type");
        if (type != null) {
            fillData();
        }
        bt_cam.setOnClickListener(this);
        bt_src.setOnClickListener(this);
        bt_bc.setOnClickListener(this);
        bt_save.setOnClickListener(this);
    }

    private void fillData() {
        bt_save.setText("更改卡类型信息");
        cursor = sdb.query("galaxy_card_type", new String[]{"card_type", "line_number",
                "left_x", "left_y", "line_space1",
                "line_space2", "line_space3", "line_space4"
                , "reverse_tc"}, "card_type=?", new String[]{type}, null, null, null);
        while (cursor.moveToNext()) {

            et_card_type.setText(cursor.getString(0));
            et_line_num.setText(cursor.getString(1));
            et_left_upx.setText(cursor.getString(2));
            et_left_upy.setText(cursor.getString(3));
            et_jianju1.setText(cursor.getString(4));
            et_jianju2.setText(cursor.getString(5));
            et_jianju3.setText(cursor.getString(6));
            et_jianju4.setText(cursor.getString(7));
            String c = cursor.getString(8);
            if (c.equals("0")) {
                rb_no.setChecked(true);
                rb_yes.setChecked(false);
            } else {
                rb_yes.setChecked(true);
                rb_no.setChecked(false);
            }
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        iv_cam = (ImageView) findViewById(R.id.iv_cam);
        rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
        preview = (FrameLayout) findViewById(R.id.camera_preview);
        et_card_type = (EditText) findViewById(R.id.et_card_type);
        et_line_num = (EditText) findViewById(R.id.et_line_num);
        et_left_upx = (EditText) findViewById(R.id.et_left_upx);
        et_left_upy = (EditText) findViewById(R.id.et_left_upy);
        et_jianju1 = (EditText) findViewById(R.id.et_jianju1);
        try {
            D = Integer.valueOf(et_jianju1.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        et_jianju2 = (EditText) findViewById(R.id.et_jianju2);
        et_jianju3 = (EditText) findViewById(R.id.et_jianju3);
        et_jianju4 = (EditText) findViewById(R.id.et_jianju4);

        bt_cam = (Button) findViewById(R.id.bt_cam);
        bt_src = (Button) findViewById(R.id.bt_src);
        bt_bc = (Button) findViewById(R.id.bt_bc);
        bt_save = (Button) findViewById(R.id.bt_save);
        rg = (RadioGroup) findViewById(R.id.rg);
        rg_length = (RadioGroup) findViewById(R.id.rg_length);

        rb_no = (RadioButton) findViewById(R.id.rb_no);
        rb_1 = (RadioButton) findViewById(R.id.rb_1);
        rb_2 = (RadioButton) findViewById(R.id.rb_2);
        rb_3 = (RadioButton) findViewById(R.id.rb_3);
        rb_4 = (RadioButton) findViewById(R.id.rb_4);
        rb_yes = (RadioButton) findViewById(R.id.rb_yes);
        rb_no.setChecked(true);

      /*  rg_length.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_1:
                        D = Integer.valueOf(et_jianju1.getText().toString().trim());
                        break;
                    case R.id.rb_2:
                        D = Integer.valueOf(et_jianju2.getText().toString().trim());
                        break;
                    case R.id.rb_3:
                        D = Integer.valueOf(et_jianju3.getText().toString().trim());
                        break;
                    case R.id.rb_4:
                        D = Integer.valueOf(et_jianju4.getText().toString().trim());
                        break;
                }
            }
        });*/


        et_line_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    int num = Integer.valueOf(editable.toString());
                    if (num == 1) {
                        et_jianju2.setText("不可用");
                        et_jianju3.setText("不可用");
                        et_jianju4.setText("不可用");

                    } else if (num == 2) {
                        et_jianju2.setText("0");
                        et_jianju3.setText("不可用");
                        et_jianju4.setText("不可用");

                    } else if (num == 3) {
                        et_jianju2.setText("0");
                        et_jianju3.setText("0");
                        et_jianju4.setText("不可用");
                    } else if (num == 4) {
                        et_jianju2.setText("0");
                        et_jianju3.setText("0");
                        et_jianju4.setText("0");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    List l;
    List list;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != -1) {        //此处的 RESULT_OK 是系统自定义得一个常量
            Toast.makeText(CardTypeSettingActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rectview != null || layoutParams != null) {
            rl_layout.removeView(rectview);
            layoutParams = null;
        }
        ContentResolver resolver = this.getContentResolver();

        if (requestCode == IMAGE_CODE) {
            Uri originalUri = data.getData();        //获得图片的uri
            try {
                double d = new HorizontalUtils(CardTypeSettingActivity.this, MediaStore.Images.Media.getBitmap(resolver, originalUri)).getRoat();  //显得到bitmap图片
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
//                iv_cam.setImageBitmap(bitmap);
                Log.i("66", "原图片高度" + bitmap.getHeight());
                Log.i("66", "原图片宽度" + bitmap.getWidth());
                Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth() * 300 / 1000, bitmap.getHeight() * 300 / 1000, bitmap.getConfig());
                //2.临摹作画
                //创建画板 ,参考空白纸张的大小，把画板创建出来。
                Canvas canvas = new Canvas(copyBitmap);
                //3.作画
                //创建一个画笔
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);//设置默认的颜色。
                Matrix matrix = new Matrix();
                matrix.setRotate((float) -d, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);
                matrix.postScale(0.30f, 0.30f); //长和宽放大缩小的比例
                canvas.drawBitmap(bitmap, matrix, paint);

                grayBitmap = ToGray.toGray(copyBitmap);
                Log.i("66", "灰图片高度" + grayBitmap.getHeight());
                Log.i("66", "灰图片宽度" + grayBitmap.getWidth());
//                list = new MatchCUtils(CardTypeSettingActivity.this, grayBitmap).matchB();

//                recBitmap = Bitmap.createBitmap(grayBitmap, (int) list.get(0) + 16 - 112, (int) list.get(1) - 10 - 46, Constants.W, Constants.H, null, false);
                recBitmap = Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY

                        , Constants.W, Constants.H, null, false);

                iv_cam.setImageBitmap(recBitmap);
                rectview = new ImageView(this);
                rectview.setImageResource(R.drawable.rect);

                l = new MatchCUtils(CardTypeSettingActivity.this, recBitmap).match();

                layoutParams = new RelativeLayout.LayoutParams(
                        -2, -2);

                layoutParams.setMargins((int) l.get(0), (int) l.get(1), 0, 0);
                et_left_upx.setText("" + (int) l.get(0));
                et_left_upy.setText("" + (int) l.get(1));

                rl_layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rectview.getLayoutParams();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                startX = (int) event.getX();
                                startY = (int) event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int move2X = (int) event.getX();
                                int move2Y = (int) event.getY();

                                int difX = (move2X - startX);
                                int difY = (move2Y - startY);

                                if (upX == 0 && upY == 0) {
                                    layoutParams.setMargins(difX, difY, 0, 0);
                                } else {
                                    layoutParams.setMargins(difX + upX, difY + upY, 0, 0);
                                }
                                rectview.requestLayout();
                                break;
                            case MotionEvent.ACTION_UP:
                                upY = rectview.getTop();
                                upX = rectview.getLeft();
                                Log.i("66", "图片5高度" + upY);
                                Log.i("66", "图片5宽度" + upX);
                                et_left_upx.setText(String.valueOf(upX));
                                et_left_upy.setText(String.valueOf(upY));
                                break;
                        }
                        rectview.invalidate();
                        return true;
                    }
                });
                if (rectview != null && layoutParams != null) {
                    et_left_upx.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.toString() != null) {
                                try {
                                    layoutParams.setMargins(Integer.valueOf(s.toString()), Integer.valueOf(et_left_upy.getText().toString()), 0, 0);

                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    et_left_upy.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.toString() != null) {
                                try {
                                    layoutParams.setMargins(Integer.valueOf(et_left_upx.getText().toString()), Integer.valueOf(s.toString()), 0, 0);

                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                }
                rl_layout.addView(rectview, layoutParams);

                originalUri = null;
                copyBitmap = null;
                bitmap = null;
                canvas = null;
                paint = null;
                grayBitmap = null;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
        if (requestCode == 66) {

            File f = new File(Environment.getExternalStorageDirectory()
                    + "/" + "pic" + "/" + "hh");
            try {

                Uri u = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(this.getContentResolver(),
                        f.getAbsolutePath(), null, null));
                double d1 = 0;  //显得到bitmap图片
                try {
                    d1 = new HorizontalUtils(CardTypeSettingActivity.this, MediaStore.Images.Media.getBitmap(resolver, u)).getRoat();
                    bitmap = MediaStore.Images.Media.getBitmap(resolver, u);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                iv_cam.setImageBitmap(bitmap);
//                bitmap=BitmapFactory.decodeResource(mactivity.getResources(),R.drawable.a);
                Log.i("66", "图片高度" + bitmap.getHeight());
                Log.i("66", "图片宽度" + bitmap.getWidth());
                Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth() * 300 / 1000, bitmap.getHeight() * 300 / 1000, bitmap.getConfig());
                //2.临摹作画
                //创建画板 ,参考空白纸张的大小，把画板创建出来。
                Canvas canvas = new Canvas(copyBitmap);
                //3.作画
                //创建一个画笔
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);//设置默认的颜色。
                Matrix matrix = new Matrix();
                matrix.setRotate((float) -d1, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);
                matrix.postScale(0.30f, 0.30f); //长和宽放大缩小的比例
                canvas.drawBitmap(bitmap, matrix, paint);

                grayBitmap = ToGray.toGray(copyBitmap);
                Log.i("66", "图片高度" + grayBitmap.getHeight());
                Log.i("66", "图片宽度" + grayBitmap.getWidth());

                recBitmap = Bitmap.createBitmap(grayBitmap, Constants.DX, Constants.DY, Constants.W, Constants.H, null, false);

                iv_cam.setImageBitmap(recBitmap);
                rectview = new ImageView(this);
                rectview.setImageResource(R.drawable.rect);

                l = new MatchCUtils(CardTypeSettingActivity.this, recBitmap).match();

                layoutParams = new RelativeLayout.LayoutParams(
                        -2, -2);

                layoutParams.setMargins((int) l.get(0), (int) l.get(1), 0, 0);
                et_left_upx.setText("" + (int) l.get(0));
                et_left_upy.setText("" + (int) l.get(1));


                rl_layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rectview.getLayoutParams();
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                startX = (int) event.getX();
                                startY = (int) event.getY();
                                break;
                            case MotionEvent.ACTION_MOVE:
                                int move2X = (int) event.getX();
                                int move2Y = (int) event.getY();

                                int difX = (move2X - startX);
                                int difY = (move2Y - startY);

                                if (upX == 0 && upY == 0) {
                                    layoutParams.setMargins(difX, difY, 0, 0);
                                } else {
                                    layoutParams.setMargins(difX + upX, difY + upY, 0, 0);
                                }
                                rectview.requestLayout();
                                break;
                            case MotionEvent.ACTION_UP:
                                upY = rectview.getTop();
                                upX = rectview.getLeft();
                                Log.i("66", "图片5高度" + upY);
                                Log.i("66", "图片5宽度" + upX);
                                et_left_upx.setText(String.valueOf(upX));
                                et_left_upy.setText(String.valueOf(upY));
                                break;
                        }
                        rectview.invalidate();
                        return true;
                    }
                });

                if (rectview != null && layoutParams != null) {
                    et_left_upx.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.toString() != null) {
                                try {
                                    layoutParams.setMargins(Integer.valueOf(s.toString()), Integer.valueOf(et_left_upy.getText().toString()), 0, 0);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    et_left_upy.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.toString() != null) {
                                try {
                                    layoutParams.setMargins(Integer.valueOf(et_left_upx.getText().toString()), Integer.valueOf(s.toString()), 0, 0);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                }
                rl_layout.addView(rectview, layoutParams);


                copyBitmap = null;
                bitmap = null;
                canvas = null;
                paint = null;
                grayBitmap = null;
                u = null;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
     * 初始化些数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_card_type", new String[]{"card_type_id"}, null, null, null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.isLast()) {
                max = Integer.valueOf(cursor.getString(0));
            }
        }
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("卡类型详情页面");
        tl_bar.setSubtitle("卡类型详情");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tl_bar.inflateMenu(R.menu.menu_cardtype);
        tl_bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                                              @Override
                                              public boolean onMenuItemClick(MenuItem item) {
                                                  switch (item.getItemId()) {
                                                      case R.id.add_white:
                                                          pd = new ProgressDialog(CardTypeSettingActivity.this);
                                                          pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条的形式为圆形转动的进度条
                                                          pd.setTitle("读取白板值提示");
                                                          pd.setMessage("准备读取,初始化中。。。");
                                                          pd.setCancelable(true);// 设置是否可以通过点击Back键取消
                                                          pd.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
                                                          pd.show();
                                                          mHandler.sendEmptyMessageDelayed(1, 2 * 1000); //7s 后开始启动相机
                                                          break;
                                                  }

                                                  return false;
                                              }
                                          }
        );
    }

    /**
     * 初始化设置Camera.打开摄像头
     * <p>
     * 如果没有前置摄像头的话就打开后置摄像头
     */

    private void openCamera() {
        pd.setMessage("初始化相机。。。");
        mCamera = getCameraInstance();
        if (mCamera != null) {
            mPreview = new CameraPreview(CardTypeSettingActivity.this, mCamera);
            preview.removeAllViews();
            preview.addView(mPreview);
            mCamera.startPreview();
        }
//        mHandler.sendEmptyMessage(2);
        mHandler.sendEmptyMessageDelayed(2, 6 * 1000); //4s后拍照

    }

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 获取Jpeg图片，并保存在sd卡上
            try {

                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                iv_cam.setImageBitmap(bitmap);
                //mCamera.startPreview();
                double d = new HorizontalUtils(CardTypeSettingActivity.this, bitmap).getRoat();  //显得到bitmap图片
                Bitmap copyBitmap = Bitmap.createBitmap(bitmap.getWidth() * 280 / 1000, bitmap.getHeight() * 280 / 1000, bitmap.getConfig());
                //2.临摹作画
                //创建画板 ,参考空白纸张的大小，把画板创建出来。
                canvas = new Canvas(copyBitmap);
                //3.作画
                //创建一个画笔
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);//设置默认的颜色。
                Matrix matrix = new Matrix();
                matrix.setRotate((float) -d, copyBitmap.getWidth() / 2, copyBitmap.getHeight() / 2);
                matrix.postScale(0.28f, 0.28f); //长和宽放大缩小的比例
                canvas.drawBitmap(bitmap, matrix, paint);
                white = Bitmap.createBitmap(ToGray.toGray(copyBitmap), Constants.DX, Constants.DY, Constants.W, Constants.H, null, false);
                Snackbar.make(et_card_type, "白板值读取完成,保存才生效", Snackbar.LENGTH_SHORT).show();
                pd.dismiss();
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


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_cam:
                String status = Environment.getExternalStorageState();
                if (status.equals(Environment.MEDIA_MOUNTED)) {

                    File dir = new File(Environment.getExternalStorageDirectory() + "/" + "pic");
                    if (!dir.exists()) dir.mkdirs();
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(dir, "hh");//localTempImgDir和localTempImageFileName是自己定义的名字
                    Uri u = Uri.fromFile(f);
                    intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                    startActivityForResult(intent, 66);
                }
                break;
            case R.id.bt_src:
                Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                getAlbum.setType(IMAGE_TYPE);
                getAlbum.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(getAlbum, IMAGE_CODE);

                break;
            case R.id.bt_bc:


                if (rb_1.isChecked()) {
                    D = Integer.valueOf(et_jianju1.getText().toString());
                } else if (rb_2.isChecked()) {
                    D = Integer.valueOf(et_jianju2.getText().toString());
                } else if (rb_3.isChecked()) {
                    D = Integer.valueOf(et_jianju3.getText().toString());
                } else if (rb_4.isChecked()) {
                    D = Integer.valueOf(et_jianju4.getText().toString());
                } else {
                    D = Integer.valueOf(et_jianju1.getText().toString());
                }

                if (TextUtils.isEmpty(et_left_upx.getText().toString()) || TextUtils.isEmpty(et_left_upy.getText().toString())) {
                    Toast.makeText(this, "数据不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rectview != null) {
                    rl_layout.removeView(rectview);
                }

                if (recBitmap != null) {
                    try {
                        new CheckBitmapUtils(recBitmap, CardTypeSettingActivity.this, iv_cam, et_left_upx.getText().toString(), et_left_upy.getText().toString(), D).draw();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(this, "请先设置好图像信息", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.bt_save:

                if (!TextUtils.isEmpty(et_card_type.getText().toString())) {
                    ContentValues cv = new ContentValues();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    if (white != null) {

                        white.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] g = baos.toByteArray();
                        cv.put("white_board", g);
                    }
                    cv.put("card_type", et_card_type.getText().toString());
                    cv.put("line_number", et_line_num.getText().toString());
                    cv.put("left_x", et_left_upx.getText().toString());
                    cv.put("left_y", et_left_upy.getText().toString());
                    cv.put("line_space1", et_jianju1.getText().toString());
                    cv.put("line_space2", et_jianju2.getText().toString());
                    cv.put("line_space3", et_jianju3.getText().toString());
                    cv.put("line_space4", et_jianju4.getText().toString());
                    cv.put("reverse_tc", rb_no.isChecked() ? String.valueOf(0) : String.valueOf(1));
                    if (type != null) {
                        long d = sdb.update("galaxy_card_type", cv, "card_type=?", new String[]{type});
                        if (d == -1) {
                            Toast.makeText(this, "数据更改失败", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "数据更改成功", Toast.LENGTH_SHORT).show();
                        }
                        finish();
                        return;
                    }
                    cv.put("card_type_id", Integer.valueOf(max) + 1);
                    long d = sdb.insert("galaxy_card_type", null, cv);
                    if (d == -1) {
                        Toast.makeText(this, "储存数据库失败", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "储存数据库成功", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;
                } else {
                    Toast.makeText(this, "卡类型不能为空", Toast.LENGTH_SHORT).show();
                }
        }
    }

}

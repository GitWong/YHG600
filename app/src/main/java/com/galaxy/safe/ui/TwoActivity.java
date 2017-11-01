package com.galaxy.safe.ui;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.safe.R;
import com.galaxy.safe.utils.Id2nameUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dell on 2016/12/8.
 */
public class TwoActivity extends Activity {


    private TextView tvname;
    private TextView tvmsg;
    private ImageView img_code;
    private Toolbar tl_bar;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private String curve_id;
    private String card_batch;//卡批次
    private String type;//卡类型


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    img_code.setImageBitmap((Bitmap) msg.obj);
                    break;

            }


            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycode);
        initView();
        initToolBar();
        initDate();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        curve_id = getIntent().getStringExtra("curve_id");
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库

        tvmsg.setText("曲线编号：" + curve_id);

        new Thread() {
            @Override
            public void run() {

                JSONObject json = new JSONObject();
                try {
                    json.put("type", "curve");


                    JSONArray jsonArrays = new JSONArray();
                    int i = 0;
                    cursor = sdb.query("galaxy_standard_curve_child", new String[]{"curve_id", "x_gray", "y_potency"}, "curve_id=?", new String[]{curve_id}, null, null, null);
                    while (cursor.moveToNext()) {
                        JSONObject point = new JSONObject();
                        point.put("0", cursor.getString(0));
                        point.put("1", cursor.getString(1));
                        point.put("2", cursor.getString(2));
                        jsonArrays.put(i, point);
                        i++;
                    }
                    cursor.close();
                    json.put("point", jsonArrays);

                    JSONObject curve = new JSONObject();
                    cursor = sdb.query("galaxy_standard_curve", new String[]{"curve_id", "card_batch_id",
                            "detection_item_id",
                            "func_type_id", "limited_unit_id", "detection_conclusion1"
                            , "detection_conclusion2", "detection_conclusion3", "limited_max",
                            "limited_min", "background_valid", "detection_method_id", "maximum_value",
                            "line_jianju", "const_flag",
                            "part", "off", "standard_value", "standard_basis"}, "curve_id=?", new String[]{curve_id}, null, null, null);

                    while (cursor.moveToNext()) {


                        tvname.setText("检测" + Id2nameUtils.item2name(cursor.getString(2), sdb) + "的曲线");
                        curve.put("0", curve_id);
                        curve.put("1", cursor.getString(1));
                        curve.put("2", cursor.getString(2));
                        card_batch = cursor.getString(2);

                        curve.put("3", cursor.getString(3));
                        curve.put("4", cursor.getString(4));
                        curve.put("5", cursor.getString(5));
                        curve.put("6", cursor.getString(6));
                        curve.put("7", cursor.getString(7));
                        curve.put("8", cursor.getString(8));
                        curve.put("9", cursor.getString(9));
                        curve.put("10", cursor.getString(10));
                        curve.put("11", cursor.getString(11));
                        curve.put("12", cursor.getString(12));
                        curve.put("13", cursor.getString(13));
                        curve.put("14", cursor.getString(14));
                        curve.put("15", cursor.getString(15));
                        curve.put("16", cursor.getString(16));
                        curve.put("17", cursor.getString(17));
                        curve.put("18", cursor.getString(18));

                    }
                    cursor.close();
                    json.put("curve", curve);

                    JSONObject jcard_batch = new JSONObject();

                    if (card_batch != null) {
                        Log.i("66", card_batch);
                        cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch", "supplier_id",
                                "valid_date", "card_type_id", "produce_date",
                                "card_num", "use_num", "card_batch_id"}, "card_batch_id=?", new String[]{card_batch}, null, null, null);

                        while (cursor.moveToNext()) {
                            Log.i("66", cursor.getString(0));

                            jcard_batch.put("0", cursor.getString(0));
                            jcard_batch.put("1", cursor.getString(1));
                            jcard_batch.put("2", cursor.getString(2));
                            jcard_batch.put("3", cursor.getString(3));
                            type = Id2nameUtils.type2name(cursor.getString(3), sdb);
                            jcard_batch.put("4", cursor.getString(4));
                            jcard_batch.put("5", cursor.getString(5));
                            jcard_batch.put("6", cursor.getString(6));
                        }
                    }
                    cursor.close();

                    Log.i("66", jcard_batch.toString());
                    json.put("card_batch", jcard_batch);


                    JSONObject jcard_type = new JSONObject();
                    if (type != null) {

                        cursor = sdb.query("galaxy_card_type", new String[]{"card_type", "line_number",
                                "left_x", "left_y", "line_space1",
                                "line_space2", "line_space3", "line_space4"
                                , "reverse_tc"}, "card_type=?", new String[]{type}, null, null, null);
                        while (cursor.moveToNext()) {
                            jcard_type.put("0", cursor.getString(0));
                            jcard_type.put("1", cursor.getString(1));
                            jcard_type.put("2", cursor.getString(2));
                            jcard_type.put("3", cursor.getString(3));
                            jcard_type.put("4", cursor.getString(4));
                            jcard_type.put("5", cursor.getString(5));
                            jcard_type.put("6", cursor.getString(6));
                            jcard_type.put("7", cursor.getString(7));
                            jcard_type.put("8", cursor.getString(8));
                        }
                    }
                    cursor.close();
                    json.put("card_type", jcard_type);

                    Log.i("66", json.toString());
                    Message msg = Message.obtain();
                    msg.obj = generateQRCode(json.toString());
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        tvmsg = (TextView) findViewById(R.id.tvmsg);
        tvname = (TextView) findViewById(R.id.tvname);
        img_code = (ImageView) findViewById(R.id.img_code);
    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("标准曲线的二维码");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Bitmap generateQRCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(new String(content.getBytes("UTF-8"), "ISO-8859-1"), BarcodeFormat.QR_CODE,
                    500, 500);
            return bitMatrix2Bitmap(matrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;
                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }
}

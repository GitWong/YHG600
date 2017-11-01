package com.galaxy.safe.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.galaxy.safe.ui.HomeActivity;

import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Dell on 2016/8/3.
 */
public class UpUtils {
    private SQLiteDatabase sdb;
    private String s;
    private int i;
    Context context;
    XmlSerializer xs;
    HomeActivity ha;
    ProgressDialog pd;
    File file;
    FileOutputStream fos;

    public UpUtils(Context context, SQLiteDatabase sdb, String s, int i, ProgressDialog pd, File file) {
        this.sdb = sdb;
        this.s = s;
        this.i = i;
        this.context = context;
        this.pd = pd;
        ha = (HomeActivity) context;
        this.file = file;
//        sdb.beginTransaction();
    }

    public void up() {
        switch (i) {

            case 1:

                final File galaxy_card_batch = new File(file.getAbsolutePath() + File.separator + "galaxy_card_batch.xml");
                try {
                    fos = new FileOutputStream(galaxy_card_batch);
                    //获取xml序列化器
                    xs = Xml.newSerializer();
                    xs.setOutput(fos, "utf-8");
                    //生成xml头
                    xs.startDocument("utf-8", true);
                    xs.startTag(null, "galaxy");
                    xs.startTag(null, "Table");
                    Cursor cursor = sdb.query("galaxy_card_batch", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        xs.startTag(null, "Table");

                        xs.startTag(null, "lsh");

                        if (cursor.getString(cursor.getColumnIndex("lsh")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("lsh")));

                        }
                        xs.endTag(null, "lsh");

                        xs.startTag(null, "table_id");

                        if (cursor.getString(cursor.getColumnIndex("table_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("table_id")));
                        }
                        xs.endTag(null, "table_id");

                        xs.startTag(null, "card_batch_valid");
                        if (cursor.getString(cursor.getColumnIndex("card_batch_valid")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_batch_valid")));
                        }
                        xs.endTag(null, "card_batch_valid");

                        xs.startTag(null, "card_batch_id");
                        if (cursor.getString(cursor.getColumnIndex("card_batch_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_batch_id")));
                        }
                        xs.endTag(null, "card_batch_id");

                        xs.startTag(null, "card_batch");
                        if (cursor.getString(cursor.getColumnIndex("card_batch")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_batch")));
                        }
                        xs.endTag(null, "card_batch");

                        xs.startTag(null, "card_type_id");
                        if (cursor.getString(cursor.getColumnIndex("card_type_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_type_id")));
                        }
                        xs.endTag(null, "card_type_id");

                        xs.startTag(null, "supplier_id");
                        if (cursor.getString(cursor.getColumnIndex("supplier_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("supplier_id")));
                        }
                        xs.endTag(null, "supplier_id");

                        xs.startTag(null, "card_num");
                        if (cursor.getString(cursor.getColumnIndex("card_num")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_num")));
                        }
                        xs.endTag(null, "card_num");
                        xs.startTag(null, "produce_date");
                        if (cursor.getString(cursor.getColumnIndex("produce_date")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("produce_date")));
                        }
                        xs.endTag(null, "produce_date");
                        xs.startTag(null, "valid_date");
                        if (cursor.getString(cursor.getColumnIndex("valid_date")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("valid_date")));
                        }
                        xs.endTag(null, "valid_date");
                        xs.startTag(null, "use_num");
                        if (cursor.getString(cursor.getColumnIndex("use_num")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("use_num")));
                        }
                        xs.endTag(null, "use_num");
                        xs.endTag(null, "Table");
                    }
                    xs.endTag(null, "Table");
                    xs.endTag(null, "galaxy");
                    //生成xml尾
                    xs.endDocument();

                    new Thread() {
                        @Override
                        public void run() {
                            super.run();

                            Socket socket = null;
                            socket = new Socket();
                            String ip = SpUtils.getString(context, "ip", "121.41.38.46");
                            String port = SpUtils.getString(context, "port", "9999");

                            try {

                                socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 3000);
                                StringBuffer sb = new StringBuffer();
                                BufferedReader reader = new BufferedReader(new FileReader(galaxy_card_batch));
                                sb.append(s);
                                String line = reader.readLine();
                                sb.append(line.toString());
                                sb.append("#");
                                byte[] bytes = sb.toString().getBytes("utf-8");
                                OutputStream ou = socket.getOutputStream();
                                ou.write(bytes);
                                ou.close();
                                ha.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("66",2+"");
                                        pd.setMessage("卡批次上传成功");
                                    }
                                });
                            } catch (final Exception e) {
                                ha.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "上传失败" + e.toString(), Toast.LENGTH_LONG).show();
                                        pd.dismiss();
                                    }
                                });
                            }
                        }
                    }.start();

                } catch (final Exception e) {
                    ha.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, "上传失败" + e.toString(), Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        }
                    });

                }
                break;
            case 2:

                final File galaxy_detection_item = new File(file.getAbsolutePath() + File.separator + "galaxy_detection_item.xml");
                try {
                    fos = new FileOutputStream(galaxy_detection_item);
                    //获取xml序列化器
                    xs = Xml.newSerializer();
                    xs.setOutput(fos, "utf-8");
                    //生成xml头
                    xs.startDocument("utf-8", true);
                    xs.startTag(null, "galaxy");
                    xs.startTag(null, "Table");
                    Cursor cursor = sdb.query("galaxy_detection_item", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        xs.startTag(null, "Table");

                        xs.startTag(null, "lsh");
                        if (cursor.getString(cursor.getColumnIndex("lsh")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("lsh")));
                        }

                        xs.endTag(null, "lsh");

                        xs.startTag(null, "table_id");
                        if (cursor.getString(cursor.getColumnIndex("table_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("table_id")));
                        }
                        xs.endTag(null, "table_id");

                        xs.startTag(null, "detection_item_valid");
                        if (cursor.getString(cursor.getColumnIndex("detection_item_valid")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_item_valid")));
                        }
                        xs.endTag(null, "detection_item_valid");

                        xs.startTag(null, "detection_item_id");
                        if (cursor.getString(cursor.getColumnIndex("detection_item_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_item_id")));
                        }
                        xs.endTag(null, "detection_item_id");

                        xs.startTag(null, "detection_item_name");
                        if (cursor.getString(cursor.getColumnIndex("detection_item_name")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_item_name")));
                        }
                        xs.endTag(null, "detection_item_name");

                        xs.startTag(null, "detection_itemtype_id");
                        if (cursor.getString(cursor.getColumnIndex("detection_itemtype_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_itemtype_id")));
                        }
                        xs.endTag(null, "detection_itemtype_id");

                        xs.startTag(null, "detection_item_desc");
                        if (cursor.getString(cursor.getColumnIndex("detection_item_desc")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_item_desc")));
                        }
                        xs.endTag(null, "detection_item_desc");

                        xs.endTag(null, "Table");
                    }

                    xs.endTag(null, "Table");
                    xs.endTag(null, "galaxy");
                    //生成xml尾
                    xs.endDocument();
                    cursor.close();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Socket socket = null;
                            socket = new Socket();
                            String ip = SpUtils.getString(context, "ip", "121.41.38.46");
                            String port = SpUtils.getString(context, "port", "9999");

                            try {
                                socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 3000);

                                StringBuffer sb = new StringBuffer();
                                BufferedReader reader = new BufferedReader(new FileReader(galaxy_detection_item));
                                sb.append(s);
                                String line = reader.readLine();
                                sb.append(line.toString());
                                sb.append("#");
                                byte[] bytes = sb.toString().getBytes("utf-8");
                                OutputStream ou = socket.getOutputStream();
                                ou.write(bytes);
                                ou.close();
                                ha.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("66", 3+"");
                                        pd.setMessage("项目信息上传成功");
                                    }
                                });
                            } catch (IOException e) {
                                Log.i("66", e.toString());
                            }
                        }
                    }.start();

                } catch (Exception e) {
                    Log.i("66", e.toString());
                }

                break;
            case 3:

                final File galaxy_standard_curve = new File(file.getAbsolutePath() + File.separator + "galaxy_standard_curve.xml");
                try {
                    fos = new FileOutputStream(galaxy_standard_curve);
                    //获取xml序列化器
                    xs = Xml.newSerializer();
                    xs.setOutput(fos, "utf-8");
                    //生成xml头
                    xs.startDocument("utf-8", true);
                    xs.startTag(null, "galaxy");
                    xs.startTag(null, "Table");
                    Cursor cursor = sdb.query("galaxy_standard_curve", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {

                        xs.startTag(null, "Table");

                        xs.startTag(null, "lsh");
                        if (cursor.getString(cursor.getColumnIndex("lsh")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("lsh")));
                        }
                        xs.endTag(null, "lsh");

                        xs.startTag(null, "table_id");
                        if (cursor.getString(cursor.getColumnIndex("lsh")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("lsh")));
                        }
                        xs.endTag(null, "table_id");

                        xs.startTag(null, "curve_valid");
                        if (cursor.getString(cursor.getColumnIndex("curve_valid")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("curve_valid")));
                        }
                        xs.endTag(null, "curve_valid");

                        xs.startTag(null, "curve_id");
                        if (cursor.getString(cursor.getColumnIndex("curve_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("curve_id")));
                        }
                        xs.endTag(null, "curve_id");

                        xs.startTag(null, "card_batch_id");
                        if (cursor.getString(cursor.getColumnIndex("card_batch_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_batch_id")));
                        }
                        xs.endTag(null, "card_batch_id");

                        xs.startTag(null, "detection_item_id");
                        if (cursor.getString(cursor.getColumnIndex("detection_item_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_item_id")));
                        }
                        xs.endTag(null, "detection_item_id");

                        xs.startTag(null, "type_id");
                        if (cursor.getString(cursor.getColumnIndex("type_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("type_id")));
                        }
                        xs.endTag(null, "type_id");

                        xs.startTag(null, "func_type_id");
                        if (cursor.getString(cursor.getColumnIndex("func_type_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("func_type_id")));
                        }
                        xs.endTag(null, "func_type_id");

                        xs.startTag(null, "x_gray");
                        if (cursor.getString(cursor.getColumnIndex("x_gray")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("x_gray")));
                        }
                        xs.endTag(null, "x_gray");

                        xs.startTag(null, "y_potency");
                        if (cursor.getString(cursor.getColumnIndex("y_potency")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("y_potency")));
                        }
                        xs.endTag(null, "y_potency");

                        xs.startTag(null, "limited_unit_id");
                        if (cursor.getString(cursor.getColumnIndex("limited_unit_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("limited_unit_id")));
                        }
                        xs.endTag(null, "limited_unit_id");

                        xs.startTag(null, "detection_conclusion1");
                        if (cursor.getString(cursor.getColumnIndex("detection_conclusion1")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_conclusion1")));
                        }
                        xs.endTag(null, "detection_conclusion1");

                        xs.startTag(null, "detection_conclusion2");
                        if (cursor.getString(cursor.getColumnIndex("detection_conclusion2")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_conclusion2")));
                        }
                        xs.endTag(null, "detection_conclusion2");

                        xs.startTag(null, "detection_conclusion3");
                        if (cursor.getString(cursor.getColumnIndex("detection_conclusion3")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_conclusion3")));
                        }
                        xs.endTag(null, "detection_conclusion3");

                        xs.startTag(null, "limited_max");
                        if (cursor.getString(cursor.getColumnIndex("limited_max")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("limited_max")));
                        }
                        xs.endTag(null, "limited_max");

                        xs.startTag(null, "limited_min");
                        if (cursor.getString(cursor.getColumnIndex("limited_min")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("limited_min")));
                        }
                        xs.endTag(null, "limited_min");

                        xs.startTag(null, "point_count");
                        if (cursor.getString(cursor.getColumnIndex("point_count")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("point_count")));
                        }
                        xs.endTag(null, "point_count");

                        xs.startTag(null, "background_valid");
                        if (cursor.getString(cursor.getColumnIndex("background_valid")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("background_valid")));
                        }
                        xs.endTag(null, "background_valid");

                        xs.startTag(null, "detection_method_id");
                        if (cursor.getString(cursor.getColumnIndex("detection_method_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("detection_method_id")));
                        }
                        xs.endTag(null, "detection_method_id");

                        xs.startTag(null, "maximum_value");
                        if (cursor.getString(cursor.getColumnIndex("maximum_value")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("maximum_value")));
                        }
                        xs.endTag(null, "maximum_value");

                        xs.startTag(null, "device_type_id");
                        if (cursor.getString(cursor.getColumnIndex("device_type_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("device_type_id")));
                        }
                        xs.endTag(null, "device_type_id");

                        xs.startTag(null, "const_flag");
                        if (cursor.getString(cursor.getColumnIndex("const_flag")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("const_flag")));
                        }
                        xs.endTag(null, "const_flag");

                        xs.startTag(null, "line_jianju");
                        if (cursor.getString(cursor.getColumnIndex("line_jianju")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("line_jianju")));
                        }
                        xs.endTag(null, "line_jianju");

                        xs.endTag(null, "Table");
                    }
                    xs.endTag(null, "Table");
                    xs.endTag(null, "galaxy");
                    //生成xml尾
                    xs.endDocument();
                    cursor.close();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Socket socket = null;
                            socket = new Socket();
                            String ip = SpUtils.getString(context, "ip", "121.41.38.46");
                            String port = SpUtils.getString(context, "port", "9999");

                            try {
                                socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 3000);

                                StringBuffer sb = new StringBuffer();
                                BufferedReader reader = new BufferedReader(new FileReader(galaxy_standard_curve));
                                sb.append(s);
                                String line = reader.readLine();
                                sb.append(line.toString());
                                sb.append("#");
                                byte[] bytes = sb.toString().getBytes("utf-8");
                                OutputStream ou = socket.getOutputStream();
                                ou.write(bytes);
                                ou.close();
                                ha.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("66", 4+"");
                                        pd.setMessage("标准曲线上传成功");
                                    }
                                });
                            } catch (IOException e) {
                                Log.i("66", e.toString());
                            }
                        }
                    }.start();
                } catch (Exception e) {
                    Log.i("66", e.toString());
                }
                break;
            case 4:

                final File galaxy_card_type = new File(file.getAbsolutePath() + File.separator + "galaxy_card_type.xml");
                try {
                    fos = new FileOutputStream(galaxy_card_type);
                    //获取xml序列化器
                    xs = Xml.newSerializer();
                    xs.setOutput(fos, "utf-8");
                    //生成xml头
                    xs.startDocument("utf-8", true);
                    xs.startTag(null, "galaxy");
                    xs.startTag(null, "Table");
                    Cursor cursor = sdb.query("galaxy_card_type", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        xs.startTag(null, "Table");

                        xs.startTag(null, "lsh");
                        if (cursor.getString(cursor.getColumnIndex("lsh")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("lsh")));
                        }
                        xs.endTag(null, "lsh");

                        xs.startTag(null, "table_id");
                        if (cursor.getString(cursor.getColumnIndex("table_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("table_id")));
                        }
                        xs.endTag(null, "table_id");

                        xs.startTag(null, "card_type_valid");
                        if (cursor.getString(cursor.getColumnIndex("card_type_valid")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_type_valid")));
                        }
                        xs.endTag(null, "card_type_valid");

                        xs.startTag(null, "card_type_id");
                        if (cursor.getString(cursor.getColumnIndex("card_type_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_type_id")));
                        }
                        xs.endTag(null, "card_type_id");

                        xs.startTag(null, "card_type");
                        if (cursor.getString(cursor.getColumnIndex("card_type")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("card_type")));
                        }
                        xs.endTag(null, "card_type");

                        xs.startTag(null, "left_x");
                        if (cursor.getString(cursor.getColumnIndex("left_x")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("left_x")));
                        }
                        xs.endTag(null, "left_x");

                        xs.startTag(null, "left_y");
                        if (cursor.getString(cursor.getColumnIndex("left_y")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("left_y")));
                        }
                        xs.endTag(null, "left_y");

                        xs.startTag(null, "right_x");
                        if (cursor.getString(cursor.getColumnIndex("right_x")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("right_x")));
                        }
                        xs.endTag(null, "right_x");

                        xs.startTag(null, "right_y");
                        if (cursor.getString(cursor.getColumnIndex("right_y")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("right_y")));
                        }
                        xs.endTag(null, "right_y");

                        xs.startTag(null, "line_space1");
                        if (cursor.getString(cursor.getColumnIndex("line_space1")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("line_space1")));
                        }
                        xs.endTag(null, "line_space1");

                        xs.startTag(null, "line_space2");
                        if (cursor.getString(cursor.getColumnIndex("line_space2")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("line_space2")));
                        }
                        xs.endTag(null, "line_space2");

                        xs.startTag(null, "line_space3");
                        if (cursor.getString(cursor.getColumnIndex("line_space3")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("line_space3")));
                        }
                        xs.endTag(null, "line_space3");

                        xs.startTag(null, "line_space4");
                        if (cursor.getString(cursor.getColumnIndex("line_space4")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("line_space4")));
                        }
                        xs.endTag(null, "line_space4");

                        xs.startTag(null, "line_space5");
                        if (cursor.getString(cursor.getColumnIndex("line_space5")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("line_space5")));
                        }
                        xs.endTag(null, "line_space5");

                        xs.startTag(null, "line_number");
                        if (cursor.getString(cursor.getColumnIndex("line_number")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("line_number")));
                        }
                        xs.endTag(null, "line_number");

                        xs.startTag(null, "reverse_tc");
                        if (cursor.getString(cursor.getColumnIndex("reverse_tc")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("reverse_tc")));
                        }
                        xs.endTag(null, "reverse_tc");

                        xs.startTag(null, "white_board");
                       /* if (cursor.getBlob(cursor.getColumnIndex("white_board")) != null) {
                            byte[] blob = cursor.getBlob(cursor.getColumnIndex("white_board"));
                            xs.text(blob.toString());
                        }*/
                        xs.endTag(null, "white_board");

                        xs.endTag(null, "Table");
                    }
                    xs.endTag(null, "Table");
                    xs.endTag(null, "galaxy");
                    //生成xml尾
                    xs.endDocument();
                    cursor.close();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Socket socket = null;
                            socket = new Socket();
                            String ip = SpUtils.getString(context, "ip", "121.41.38.46");
                            String port = SpUtils.getString(context, "port", "9999");

                            try {
                                socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 3000);

                                StringBuffer sb = new StringBuffer();
                                BufferedReader reader = new BufferedReader(new FileReader(galaxy_card_type));
                                sb.append(s);
                                String line = reader.readLine();
                                sb.append(line.toString());
                                sb.append("#");
                                byte[] bytes = sb.toString().getBytes("utf-8");
                                OutputStream ou = socket.getOutputStream();
                                ou.write(bytes);
                                ou.close();

                                ha.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        pd.setMessage("卡类型上传成功");
                                    }
                                });
                            } catch (IOException e) {
                                Log.i("66", e.toString());
                            }
                        }
                    }.start();
                } catch (Exception e) {
                    Log.i("66", e.toString());
                }
                break;
            case 5:
                final File galaxy_unit = new File(file.getAbsolutePath() + File.separator + "galaxy_unit.xml");
                try {
                    fos = new FileOutputStream(galaxy_unit);
                    //获取xml序列化器
                    xs = Xml.newSerializer();
                    xs.setOutput(fos, "utf-8");
                    //生成xml头
                    xs.startDocument("utf-8", true);
                    xs.startTag(null, "galaxy");
                    xs.startTag(null, "Table");
                    Cursor cursor = sdb.query("galaxy_unit", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        xs.startTag(null, "Table");

                        xs.startTag(null, "lsh");
                        if (cursor.getString(cursor.getColumnIndex("lsh")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("lsh")));
                        }
                        xs.endTag(null, "lsh");

                        xs.startTag(null, "table_id");
                        if (cursor.getString(cursor.getColumnIndex("table_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("table_id")));
                        }
                        xs.endTag(null, "table_id");
                        xs.startTag(null, "limited_unit_valid");
                        if (cursor.getString(cursor.getColumnIndex("limited_unit_valid")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("limited_unit_valid")));
                        }
                        xs.endTag(null, "limited_unit_valid");

                        xs.startTag(null, "limited_unit_id");
                        if (cursor.getString(cursor.getColumnIndex("limited_unit_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("limited_unit_id")));
                        }
                        xs.endTag(null, "limited_unit_id");

                        xs.startTag(null, "limited_unit");
                        if (cursor.getString(cursor.getColumnIndex("limited_unit")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("limited_unit")));
                        }
                        xs.endTag(null, "limited_unit");


                        xs.endTag(null, "Table");
                    }
                    xs.endTag(null, "Table");
                    xs.endTag(null, "galaxy");
                    //生成xml尾
                    xs.endDocument();
                    cursor.close();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Socket socket = null;
                            socket = new Socket();
                            String ip = SpUtils.getString(context, "ip", "121.41.38.46");
                            String port = SpUtils.getString(context, "port", "9999");

                            try {
                                socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 3000);

                                StringBuffer sb = new StringBuffer();
                                BufferedReader reader = new BufferedReader(new FileReader(galaxy_unit));
                                sb.append(s);
                                String line = reader.readLine();
                                sb.append(line.toString());
                                sb.append("#");
                                byte[] bytes = sb.toString().getBytes("utf-8");
                                OutputStream ou = socket.getOutputStream();
                                ou.write(bytes);
                                ou.close();
                                ha.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("66", 6+"");
                                        pd.setMessage("单位上传成功");
                                    }
                                });
                            } catch (IOException e) {
                                Log.i("66", e.toString());
                            }
                        }
                    }.start();
                } catch (Exception e) {
                    Log.i("66", e.toString());
                }
                break;
            case 6:
                final File galaxy_sample_type = new File(file.getAbsolutePath() + File.separator + "galaxy_sample_type.xml");
                try {
                    fos = new FileOutputStream(galaxy_sample_type);
                    //获取xml序列化器
                    xs = Xml.newSerializer();
                    xs.setOutput(fos, "utf-8");
                    //生成xml头
                    xs.startDocument("utf-8", true);
                    xs.startTag(null, "galaxy");
                    xs.startTag(null, "Table");
                    Cursor cursor = sdb.query("galaxy_sample_type", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        xs.startTag(null, "Table");

                        xs.startTag(null, "lsh");
                        if (cursor.getString(cursor.getColumnIndex("lsh")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("lsh")));
                        }
                        xs.endTag(null, "lsh");

                        xs.startTag(null, "table_id");
                        if (cursor.getString(cursor.getColumnIndex("table_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("table_id")));
                        }
                        xs.endTag(null, "table_id");

                        xs.startTag(null, "type_valid");
                        if (cursor.getString(cursor.getColumnIndex("type_valid")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("type_valid")));
                        }
                        xs.endTag(null, "type_valid");

                        xs.startTag(null, "type_id");
                        if (cursor.getString(cursor.getColumnIndex("type_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("type_id")));
                        }
                        xs.endTag(null, "type_id");

                        xs.startTag(null, "type_name");
                        if (cursor.getString(cursor.getColumnIndex("type_name")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("type_name")));
                        }
                        xs.endTag(null, "type_name");

                        xs.startTag(null, "type_desc");
                        if (cursor.getString(cursor.getColumnIndex("type_desc")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("type_desc")));
                        }
                        xs.endTag(null, "type_desc");

                        xs.endTag(null, "Table");
                    }
                    xs.endTag(null, "Table");
                    xs.endTag(null, "galaxy");
                    //生成xml尾
                    xs.endDocument();
                    cursor.close();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Socket socket = null;
                            socket = new Socket();
                            String ip = SpUtils.getString(context, "ip", "121.41.38.46");
                            String port = SpUtils.getString(context, "port", "9999");

                            try {
                                socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 3000);

                                StringBuffer sb = new StringBuffer();
                                BufferedReader reader = new BufferedReader(new FileReader(galaxy_sample_type));
                                sb.append(s);
                                String line = reader.readLine();
                                sb.append(line.toString());
                                sb.append("#");
                                byte[] bytes = sb.toString().getBytes("utf-8");
                                OutputStream ou = socket.getOutputStream();
                                ou.write(bytes);
                                ou.close();
                                ha.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("66", 1+"");
                                        pd.setMessage("样品种类上传成功");
                                    }
                                });
                            } catch (IOException e) {
                                Log.i("66", e.toString());
                            }
                        }
                    }.start();
                } catch (Exception e) {
                    Log.i("66", e.toString());
                }
                break;
            case 7:
                final File galaxy_sample_typedetail = new File(file.getAbsolutePath() + File.separator + "galaxy_sample_typedetail.xml");
                try {
                    fos = new FileOutputStream(galaxy_sample_typedetail);
                    //获取xml序列化器
                    xs = Xml.newSerializer();
                    xs.setOutput(fos, "utf-8");
                    //生成xml头
                    xs.startDocument("utf-8", true);
                    xs.startTag(null, "galaxy");
                    xs.startTag(null, "Table");
                    Cursor cursor = sdb.query("galaxy_sample_typedetail", null, null, null, null, null, null);
                    while (cursor.moveToNext()) {
                        xs.startTag(null, "Table");

                        xs.startTag(null, "lsh");
                        if (cursor.getString(cursor.getColumnIndex("lsh")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("lsh")));
                        }
                        xs.endTag(null, "lsh");

                        xs.startTag(null, "table_id");
                        if (cursor.getString(cursor.getColumnIndex("table_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("table_id")));
                        }
                        xs.endTag(null, "table_id");

                        xs.startTag(null, "sample_valid");
                        if (cursor.getString(cursor.getColumnIndex("sample_valid")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("sample_valid")));
                        }
                        xs.endTag(null, "sample_valid");

                        xs.startTag(null, "sample_id");
                        if (cursor.getString(cursor.getColumnIndex("sample_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("sample_id")));
                        }
                        xs.endTag(null, "sample_id");

                        xs.startTag(null, "sample_name");
                        if (cursor.getString(cursor.getColumnIndex("sample_name")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("sample_name")));
                        }
                        xs.endTag(null, "sample_name");

                        xs.startTag(null, "sample_desc");
                        if (cursor.getString(cursor.getColumnIndex("sample_desc")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("sample_desc")));
                        }
                        xs.endTag(null, "sample_desc");

                        xs.startTag(null, "type_id");
                        if (cursor.getString(cursor.getColumnIndex("type_id")) != null) {
                            xs.text(cursor.getString(cursor.getColumnIndex("type_id")));
                        }
                        xs.endTag(null, "type_id");

                        xs.endTag(null, "Table");
                    }
                    xs.endTag(null, "Table");
                    xs.endTag(null, "galaxy");
                    //生成xml尾
                    xs.endDocument();
                    cursor.close();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Socket socket = null;
                            socket = new Socket();
                            String ip = SpUtils.getString(context, "ip", "121.41.38.46");
                            String port = SpUtils.getString(context, "port", "9999");

                            try {
                                socket.connect(new InetSocketAddress(ip, Integer.valueOf(port)), 3000);

                                StringBuffer sb = new StringBuffer();
                                BufferedReader reader = new BufferedReader(new FileReader(galaxy_sample_typedetail));
                                sb.append(s);
                                String line = reader.readLine();
                                sb.append(line.toString());
                                sb.append("#");
                                byte[] bytes = sb.toString().getBytes("utf-8");
                                OutputStream ou = socket.getOutputStream();
                                ou.write(bytes);
                                ou.close();
                                ha.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        pd.setMessage("样品信息上传成功");
                                        pd.dismiss();
                                    }
                                });
                            } catch (IOException e) {
                                Log.i("66", e.toString());
                            }
                        }
                    }.start();
                } catch (Exception e) {
                    Log.i("66", e.toString());
                }
                break;
        }
    }

}

package com.galaxy.safe.utils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.galaxy.safe.Bean.CardBatch;
import com.galaxy.safe.ui.HomeActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/8/3.
 */
public class UpdateUtils {
    private SQLiteDatabase sdb;
    private String s;
    private int i;
    ContentValues cv;
    Context context;
    XmlPullParser xp;
    HomeActivity ha;
    ProgressDialog pd;

    public UpdateUtils(Context context, SQLiteDatabase sdb, String s, int i, ProgressDialog pd) {
        this.sdb = sdb;
        this.s = s;
        this.i = i;
        this.context = context;
        this.pd = pd;
        ha = (HomeActivity) context;
//        sdb.beginTransaction();
    }

    public void parsave() {
        switch (i) {

            case 1:
                sdb.delete("galaxy_card_batch", null, null);
                cv = new ContentValues();
                xp = Xml.newPullParser();
//获取当前节点的事件类型
                try {
                    if (!s.equals("Hello")) {
                        Log.i("66", "3261" + s.getBytes().length);
                        InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));

                        xp.setInput(is, "utf-8");
                        int type = xp.getEventType();
                        while (type != XmlPullParser.END_DOCUMENT) {
                            switch (type) {
                                case XmlPullParser.START_TAG:
                                    //	获取当前节点的名字
                                    if ("Table".equals(xp.getName())) {
                                    } else if ("Table".equals(xp.getName())) {

                                    } else if ("table_id".equals(xp.getName())) {
                                        //获取当前节点的下一个节点的文本
                                        String table_id = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("table_id", table_id);

                                    } else if ("card_batch_valid".equals(xp.getName())) {
                                        String card_batch_valid = xp.nextText();
                                        cv.put("card_batch_valid", card_batch_valid);
                                    } else if ("card_batch_id".equals(xp.getName())) {
                                        String card_batch_id = xp.nextText();
                                        cv.put("card_batch_id", card_batch_id);
                                    } else if ("card_batch".equals(xp.getName())) {
                                        String card_batch = xp.nextText();
                                        cv.put("card_batch", card_batch);
                                    } else if ("card_type_id".equals(xp.getName())) {
                                        String card_type_id = xp.nextText();
                                        cv.put("card_type_id", card_type_id);
                                    } else if ("supplier_id".equals(xp.getName())) {
                                        String supplier_id = xp.nextText();
                                        cv.put("supplier_id", supplier_id);
                                    } else if ("card_num".equals(xp.getName())) {
                                        String card_num = xp.nextText();
                                        cv.put("card_num", card_num);
                                    } else if ("produce_date".equals(xp.getName())) {
                                        String produce_date = xp.nextText();
                                        cv.put("produce_date", produce_date);
                                    } else if ("valid_date".equals(xp.getName())) {
                                        String valid_date = xp.nextText();
                                        cv.put("valid_date", valid_date);
                                    } else if ("use_num".equals(xp.getName())) {
                                        String use_num = xp.nextText();
                                        cv.put("use_num", use_num);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if ("Table".equals(xp.getName())) {
                                        sdb.insert("galaxy_card_batch", null, cv);

                                        cv.clear();
                                    }
                                    break;
                            }
                            type = xp.next();
                        }
                        ha.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.setMessage("卡批次更新完毕");

                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("66", e.toString());
                } finally {
//                    sdb.endTransaction();
//                    dialog.setMessage("卡批次更新完毕");
                }
                break;
            case 2:
//                sdb.delete("galaxy_detection_item", null, null);
                cv = new ContentValues();
                xp = Xml.newPullParser();
//获取当前节点的事件类型
                try {
                    if (!s.equals("Hello")) {
                        Log.i("66", "32" + s.getBytes().length);
                        InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                        xp.setInput(is, "utf-8");
                        int type = xp.getEventType();
                        while (type != XmlPullParser.END_DOCUMENT) {
                            switch (type) {
                                case XmlPullParser.START_TAG:
                                    //	获取当前节点的名字
                                    if ("Table".equals(xp.getName())) {
                                    } else if ("lsh".equals(xp.getName())) {
                                        String lsh = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("lsh", lsh);
                                    } else if ("table_id".equals(xp.getName())) {
                                        //获取当前节点的下一个节点的文本
                                        String table_id = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("table_id", table_id);
                                    } else if ("detection_item_valid".equals(xp.getName())) {
                                        String detection_item_valid = xp.nextText();
                                        cv.put("detection_item_valid", detection_item_valid);
                                    } else if ("detection_item_id".equals(xp.getName())) {
                                        String detection_item_id = xp.nextText();
                                        cv.put("detection_item_id", detection_item_id);
                                    } else if ("detection_item_name".equals(xp.getName())) {
                                        String detection_item_name = xp.nextText();
                                        cv.put("detection_item_name", detection_item_name);
                                    } else if ("detection_itemtype_id".equals(xp.getName())) {
                                        String detection_itemtype_id = xp.nextText();
                                        cv.put("detection_itemtype_id", detection_itemtype_id);
                                    } else if ("detection_item_desc".equals(xp.getName())) {
                                        String detection_item_desc = xp.nextText();
                                        cv.put("detection_item_desc", detection_item_desc);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if ("Table".equals(xp.getName())) {
                                        long d = sdb.insert("galaxy_detection_item", null, cv);
                                        Log.i("66", "d=" + d);
                                        cv.clear();
                                    }
                                    break;
                            }
                            type = xp.next();
                        }
                        ha.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.setMessage("项目信息更新完毕");
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("66", e.toString());
                }
                break;
            case 3:
                sdb.delete("galaxy_standard_curve", null, null);
                cv = new ContentValues();
                xp = Xml.newPullParser();
                try {
                    if (!s.equals("Hello")) {
                        Log.i("66", "32" + s.getBytes().length);
                        InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                        xp.setInput(is, "utf-8");
                        int type = xp.getEventType();
                        while (type != XmlPullParser.END_DOCUMENT) {
                            switch (type) {
                                case XmlPullParser.START_TAG:
                                    //	获取当前节点的名字
                                    if ("Table".equals(xp.getName())) {
                                    } else if ("lsh".equals(xp.getName())) {
                                        String lsh = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("lsh", lsh);
                                    } else if ("table_id".equals(xp.getName())) {
                                        //获取当前节点的下一个节点的文本
                                        String table_id = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("table_id", table_id);
                                    } else if ("curve_valid".equals(xp.getName())) {
                                        String curve_valid = xp.nextText();
                                        cv.put("curve_valid", curve_valid);

                                    } else if ("curve_id".equals(xp.getName())) {
                                        String curve_id = xp.nextText();
                                        cv.put("curve_id", curve_id);
                                    } else if ("card_batch_id".equals(xp.getName())) {
                                        String card_batch_id = xp.nextText();
                                        cv.put("card_batch_id", card_batch_id);
                                    } else if ("detection_item_id".equals(xp.getName())) {
                                        String detection_item_id = xp.nextText();
                                        cv.put("detection_item_id", detection_item_id);
                                    } else if ("type_id".equals(xp.getName())) {
                                        String type_id = xp.nextText();
                                        cv.put("type_id", type_id);
                                    } else if ("func_type_id".equals(xp.getName())) {
                                        String func_type_id = xp.nextText();
                                        cv.put("func_type_id", func_type_id);
                                    } else if ("x_gray".equals(xp.getName())) {
                                        String x_gray = xp.nextText();
                                        cv.put("x_gray", x_gray);
                                    } else if ("y_potency".equals(xp.getName())) {
                                        String y_potency = xp.nextText();
                                        cv.put("y_potency", y_potency);
                                    } else if ("limited_unit_id".equals(xp.getName())) {
                                        String limited_unit_id = xp.nextText();
                                        cv.put("limited_unit_id", limited_unit_id);
                                    } else if ("detection_conclusion1".equals(xp.getName())) {
                                        String detection_conclusion1 = xp.nextText();
                                        cv.put("detection_conclusion1", detection_conclusion1);
                                    } else if ("detection_conclusion2".equals(xp.getName())) {
                                        String detection_conclusion2 = xp.nextText();
                                        cv.put("detection_conclusion2", detection_conclusion2);
                                    } else if ("detection_conclusion3".equals(xp.getName())) {
                                        String detection_conclusion3 = xp.nextText();
                                        cv.put("detection_conclusion3", detection_conclusion3);
                                    } else if ("limited_max".equals(xp.getName())) {
                                        String limited_max = xp.nextText();
                                        cv.put("limited_max", limited_max);
                                    } else if ("limited_min".equals(xp.getName())) {
                                        String limited_min = xp.nextText();
                                        cv.put("limited_min", limited_min);
                                    } else if ("point_count".equals(xp.getName())) {
                                        String point_count = xp.nextText();
                                        cv.put("point_count", point_count);
                                    } else if ("background_valid".equals(xp.getName())) {
                                        String background_valid = xp.nextText();
                                        cv.put("background_valid", background_valid);
                                    } else if ("detection_method_id".equals(xp.getName())) {
                                        String detection_method_id = xp.nextText();
                                        cv.put("detection_method_id", detection_method_id);
                                    } else if ("device_type_id".equals(xp.getName())) {
                                        String device_type_id = xp.nextText();
                                        cv.put("device_type_id", device_type_id);
                                    } else if ("const_flag".equals(xp.getName())) {
                                        String const_flag = xp.nextText();
                                        cv.put("const_flag", const_flag);
                                    } else if ("line_jianju".equals(xp.getName())) {
                                        String line_jianju = xp.nextText();
                                        cv.put("line_jianju", line_jianju);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if ("Table".equals(xp.getName())) {
                                        sdb.insert("galaxy_standard_curve", null, cv);

                                        cv.clear();
                                    }
                                    break;
                            }
                            type = xp.next();
                        }
                        ha.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.setMessage("标准曲线更新完毕");
                                pd.dismiss();
                                Toast.makeText(context, "更新完毕", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("66", e.toString());
                } finally {

                }
                break;
            case 4:
                sdb.delete("galaxy_card_type", null, null);
                cv = new ContentValues();
                xp = Xml.newPullParser();
                try {
                    if (!s.equals("Hello")) {
                        InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                        xp.setInput(is, "utf-8");
                        int type = xp.getEventType();
                        while (type != XmlPullParser.END_DOCUMENT) {
                            switch (type) {
                                case XmlPullParser.START_TAG:
                                    //	获取当前节点的名字
                                    if ("Table".equals(xp.getName())) {
                                    } else if ("lsh".equals(xp.getName())) {
                                        String lsh = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("lsh", lsh);
                                    } else if ("table_id".equals(xp.getName())) {
                                        //获取当前节点的下一个节点的文本
                                        String table_id = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("table_id", table_id);
                                    } else if ("card_type_valid".equals(xp.getName())) {
                                        String card_type_valid = xp.nextText();
                                        cv.put("card_type_valid", card_type_valid);

                                    } else if ("card_type_id".equals(xp.getName())) {
                                        String card_type_id = xp.nextText();
                                        cv.put("card_type_id", card_type_id);
                                    } else if ("card_type".equals(xp.getName())) {
                                        String card_type = xp.nextText();
                                        cv.put("card_type", card_type);
                                    } else if ("left_x".equals(xp.getName())) {
                                        String left_x = xp.nextText();
                                        cv.put("left_x", left_x);
                                    } else if ("left_y".equals(xp.getName())) {
                                        String left_y = xp.nextText();
                                        cv.put("left_y", left_y);
                                    } else if ("right_x".equals(xp.getName())) {
                                        String right_x = xp.nextText();
                                        cv.put("right_x", right_x);
                                    } else if ("right_y".equals(xp.getName())) {
                                        String right_y = xp.nextText();
                                        cv.put("right_y", right_y);
                                    } else if ("line_space1".equals(xp.getName())) {
                                        String line_space1 = xp.nextText();
                                        cv.put("line_space1", line_space1);
                                    } else if ("line_space2".equals(xp.getName())) {
                                        String line_space2 = xp.nextText();
                                        cv.put("line_space2", line_space2);
                                    } else if ("line_space3".equals(xp.getName())) {
                                        String line_space3 = xp.nextText();
                                        cv.put("line_space3", line_space3);
                                    } else if ("line_space4".equals(xp.getName())) {
                                        String line_space4 = xp.nextText();
                                        cv.put("line_space4", line_space4);
                                    } else if ("line_space5".equals(xp.getName())) {
                                        String line_space5 = xp.nextText();
                                        cv.put("line_space5", line_space5);
                                    } else if ("line_number".equals(xp.getName())) {
                                        String line_number = xp.nextText();
                                        cv.put("line_number", line_number);
                                    } else if ("reverse_tc".equals(xp.getName())) {
                                        String reverse_tc = xp.nextText();
                                        cv.put("reverse_tc", reverse_tc);
                                    } else if ("white_board".equals(xp.getName())) {
                                        String white = xp.nextText();
                                        byte[] blob = white.getBytes();
                                        cv.put("white_board", blob);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if ("Table".equals(xp.getName())) {
                                        sdb.insert("galaxy_card_type", null, cv);
                                        cv.clear();
                                    }
                                    break;
                            }
                            type = xp.next();
                        }
                        ha.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.setMessage("卡类型更新完毕");
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("66", e.toString());
                } finally {

                }
                break;
            case 5:
                sdb.delete("galaxy_unit", null, null);
                cv = new ContentValues();
                xp = Xml.newPullParser();
                try {
                    if (!s.equals("Hello")) {
                        InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                        xp.setInput(is, "utf-8");
                        int type = xp.getEventType();
                        while (type != XmlPullParser.END_DOCUMENT) {
                            switch (type) {
                                case XmlPullParser.START_TAG:
                                    //	获取当前节点的名字
                                    if ("Table".equals(xp.getName())) {
                                    } else if ("lsh".equals(xp.getName())) {
                                        String lsh = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("lsh", lsh);
                                    } else if ("table_id".equals(xp.getName())) {
                                        //获取当前节点的下一个节点的文本
                                        String table_id = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("table_id", table_id);
                                    } else if ("limited_unit_valid".equals(xp.getName())) {
                                        String limited_unit_valid = xp.nextText();
                                        cv.put("limited_unit_valid", limited_unit_valid);
                                    } else if ("limited_unit_id".equals(xp.getName())) {
                                        String limited_unit_id = xp.nextText();
                                        cv.put("limited_unit_id", limited_unit_id);
                                    } else if ("limited_unit".equals(xp.getName())) {
                                        String limited_unit = xp.nextText();
                                        cv.put("limited_unit", limited_unit);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if ("Table".equals(xp.getName())) {
                                        sdb.insert("galaxy_unit", null, cv);
                                        cv.clear();
                                    }
                                    break;
                            }
                            type = xp.next();
                        }
                        ha.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.setMessage("单位更新完毕");
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("66", e.toString());
                } finally {

                }
                break;
            case 6:
                sdb.delete("galaxy_sample_type", null, null);
                cv = new ContentValues();
                xp = Xml.newPullParser();
                try {
                    if (!s.equals("Hello")) {
                        InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                        xp.setInput(is, "utf-8");
                        int type = xp.getEventType();
                        while (type != XmlPullParser.END_DOCUMENT) {
                            switch (type) {
                                case XmlPullParser.START_TAG:
                                    //	获取当前节点的名字
                                    if ("Table".equals(xp.getName())) {
                                    } else if ("lsh".equals(xp.getName())) {
                                        String lsh = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("lsh", lsh);
                                    } else if ("table_id".equals(xp.getName())) {
                                        //获取当前节点的下一个节点的文本
                                        String table_id = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("table_id", table_id);
                                    } else if ("type_valid".equals(xp.getName())) {
                                        String type_valid = xp.nextText();
                                        cv.put("type_valid", type_valid);
                                    } else if ("type_id".equals(xp.getName())) {
                                        String type_id = xp.nextText();
                                        cv.put("type_id", type_id);
                                    } else if ("type_name".equals(xp.getName())) {
                                        String type_name = xp.nextText();
                                        cv.put("type_name", type_name);
                                    } else if ("type_desc".equals(xp.getName())) {
                                        String type_desc = xp.nextText();
                                        cv.put("type_desc", type_desc);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if ("Table".equals(xp.getName())) {
                                        sdb.insert("galaxy_sample_type", null, cv);
                                        cv.clear();
                                    }
                                    break;
                            }
                            type = xp.next();
                        }
                        ha.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.setMessage("样品类型更新完毕");
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("66", e.toString());
                } finally {

                }
                break;
            case 7:
                sdb.delete("galaxy_sample_typedetail", null, null);
                cv = new ContentValues();
                xp = Xml.newPullParser();
                try {
                    if (!s.equals("Hello")) {
                        InputStream is = new ByteArrayInputStream(s.getBytes("utf-8"));
                        xp.setInput(is, "utf-8");
                        int type = xp.getEventType();
                        while (type != XmlPullParser.END_DOCUMENT) {
                            switch (type) {
                                case XmlPullParser.START_TAG:
                                    //	获取当前节点的名字
                                    if ("Table".equals(xp.getName())) {
                                    } else if ("lsh".equals(xp.getName())) {
                                        String lsh = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("lsh", lsh);
                                    } else if ("table_id".equals(xp.getName())) {
                                        //获取当前节点的下一个节点的文本
                                        String table_id = xp.nextText();
                                        //把文本保存至对象
                                        cv.put("table_id", table_id);
                                    } else if ("sample_valid".equals(xp.getName())) {
                                        String sample_valid = xp.nextText();
                                        cv.put("sample_valid", sample_valid);
                                    } else if ("sample_id".equals(xp.getName())) {
                                        String sample_id = xp.nextText();
                                        cv.put("sample_id", sample_id);
                                    } else if ("sample_name".equals(xp.getName())) {
                                        String sample_name = xp.nextText();
                                        cv.put("sample_name", sample_name);
                                    } else if ("sample_desc".equals(xp.getName())) {
                                        String sample_desc = xp.nextText();
                                        cv.put("sample_desc", sample_desc);
                                    } else if ("type_id".equals(xp.getName())) {
                                        String type_id = xp.nextText();
                                        cv.put("type_id", type_id);
                                    }
                                    break;
                                case XmlPullParser.END_TAG:
                                    if ("Table".equals(xp.getName())) {
                                        sdb.insert("galaxy_sample_typedetail", null, cv);
                                        cv.clear();
                                    }
                                    break;
                            }
                            type = xp.next();
                        }
                        ha.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pd.setMessage("样品信息更新完毕");
                                pd.dismiss();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("66", e.toString());
                } finally {

                }
                break;
        }
    }

}

package com.galaxy.safe.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.galaxy.safe.Bean.Acurve;

import java.util.List;

/**
 * Created by Dell on 2016/12/9.
 */
public class SaveCurveUtils {

    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Acurve acurve;
    private String cmax;
    private String bmax;
    private String tmax;

    public SaveCurveUtils(Acurve acurve) {
        this.acurve = acurve;
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
    }

    /**
     *
     */
    public void saveCurve() {

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_standard_curve", new String[]{"curve_id"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isLast()) {
                cmax = cursor.getString(0);
            }
        }
        cursor.close();

        Acurve.CurveBean curve = acurve.getCurve();

        ContentValues cv = new ContentValues();
        cv.put("curve_id", Integer.valueOf(cmax) + 1);
        cv.put("card_batch_id", Integer.valueOf(bmax) + 1);
        cv.put("detection_item_id", curve.getValue2());
        cv.put("func_type_id", curve.getValue3());
        cv.put("limited_unit_id", curve.getValue4());
        cv.put("detection_conclusion1", curve.getValue5());
        cv.put("detection_conclusion2", curve.getValue6());
        cv.put("detection_conclusion3", curve.getValue7());
        cv.put("limited_max", curve.getValue8());
        cv.put("limited_min", curve.getValue9());
        cv.put("background_valid", curve.getValue10());
        cv.put("detection_method_id", curve.getValue11());
        cv.put("maximum_value", curve.getValue12());
        cv.put("line_jianju", curve.getValue13());
        cv.put("const_flag", curve.getValue14());
        cv.put("part", curve.getValue15());
        cv.put("off", curve.getValue16());
        cv.put("standard_value", curve.getValue17());
        cv.put("standard_basis", curve.getValue18());
        sdb.insert("galaxy_standard_curve", null, cv);


        List<Acurve.PointBean> point = acurve.getPoint();
        for (int i = 0; i < point.size(); i++) {

            ContentValues cv1 = new ContentValues();
            cv1.put("curve_id", Integer.valueOf(cmax) + 1);
            cv1.put("x_gray", point.get(i).getValue1());
            cv1.put("y_potency", point.get(i).getValue2());
            sdb.insert("galaxy_standard_curve_child", null, cv1);
        }

    }

    public void saveCardbatch() {

        Acurve.CardBatchBean card_batch = acurve.getCard_batch();
        cursor = sdb.query("galaxy_card_batch", new String[]{"card_batch_id"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isLast()) {
                bmax = cursor.getString(0);
            }
        }
        cursor.close();
        ContentValues cv = new ContentValues();
        cv.put("card_batch_id", Integer.valueOf(bmax) + 1);
        cv.put("card_batch", card_batch.getValue0());
        cv.put("supplier_id", card_batch.getValue1());
        cv.put("valid_date", card_batch.getValue2());
        cv.put("card_type_id", Integer.valueOf(tmax) + 1);
        cv.put("produce_date", card_batch.getValue4());
        cv.put("card_num", card_batch.getValue5());
        cv.put("use_num", card_batch.getValue6());
        sdb.insert("galaxy_card_batch", null, cv);
    }

    public void saveCardtype() {

        Acurve.CardTypeBean card_type = acurve.getCard_type();
        cursor = sdb.query("galaxy_card_type", new String[]{"card_type_id"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            if (cursor.isLast()) {
                tmax = cursor.getString(0);
            }
        }
        cursor.close();
        ContentValues cv = new ContentValues();
        cv.put("card_type_id", Integer.valueOf(tmax) + 1);

        cv.put("card_type", card_type.getValue0());
        cv.put("line_number", card_type.getValue1());
        cv.put("left_x", card_type.getValue2());
        cv.put("left_y", card_type.getValue3());
        cv.put("line_space1", card_type.getValue4());
        cv.put("line_space2", card_type.getValue5());
        cv.put("line_space3", card_type.getValue6());
        cv.put("line_space4", card_type.getValue7());
        cv.put("reverse_tc", card_type.getValue8());
        sdb.insert("galaxy_card_type", null, cv);
    }


}

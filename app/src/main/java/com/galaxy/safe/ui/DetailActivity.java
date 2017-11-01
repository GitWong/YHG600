package com.galaxy.safe.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.galaxy.safe.R;
import com.galaxy.safe.utils.DeleteFileUtils;
import com.galaxy.safe.utils.DensityUtil;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.utils.WordToHtml;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.FieldsDocumentPart;
import org.apache.poi.hwpf.usermodel.Fields;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Created by Dell on 2016/3/10.
 */
public class DetailActivity extends Activity {


    private SQLiteDatabase sdb;
    private WebView wv_report;
    private Toolbar tl_bar;
    private String save1;//转成的html保存路径
    private String save;//word文档保存路径
    private String sampleId;


    String tasks_source;
    String detection_station_name;
    String remark;
    String sampling_person1;
    String fengsamplecheck_person;
    String examined_company;
    String sample_collection_date;


    String detection_item_id;
    String standard_value;
    String detection_result;
    String detection_conclusion;
    String detection_gist;


    String inspector;
    String authorized_person;
    String detection_person_id;
    String report_bh;


    String quality_level;
    String chouyang_num;
    String specification_model;
    String sample_trademark;
    String sampling_date;

    String sample_name;
    String number;

    String weituoNum;
    private String entrust_date;
    private String testing_no;
    private File file;
    private String sample_num;
    private String sample_condition;
    private String sampling_no;
    private String sample_bh;
    private LinearLayout ll_tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initUI();
        initToolbar();
        initData();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();

        DeleteFileUtils.DeleteFile(file);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库

        sampling_no = this.getIntent().getStringExtra("sampling_no");


        Cursor cursor5 = sdb.rawQuery("select * from galaxy_detection_recordcb where sampling_no =?", new String[]{sampling_no});
        while (cursor5.moveToNext()) {
            detection_item_id = Id2nameUtils.item2name(cursor5.getString(cursor5.getColumnIndex("detection_item_id")), sdb);
            standard_value = cursor5.getString(cursor5.getColumnIndex("standard_value"));
            detection_result = cursor5.getString(cursor5.getColumnIndex("detection_result"));
            detection_conclusion = cursor5.getString(cursor5.getColumnIndex("detection_conclusion"));
            detection_gist = cursor5.getString(cursor5.getColumnIndex("detection_gist"));
        }
        cursor5.close();
        Cursor cursor = sdb.rawQuery("select * from galaxy_detection_record where sampling_no =?", new String[]{sampling_no});
        while (cursor.moveToNext()) {

            testing_no = cursor.getString(cursor.getColumnIndex("testing_no"));
            sample_bh = cursor.getString(cursor.getColumnIndex("sample_bh"));

            Cursor cursor4 = sdb.query("galaxy_detection_sample", new String[]{"sample_num", "sample_condition", "quality_level", "chouyang_num", "specification_model", "sample_trademark", "sampling_date", "sample_id"}, "sample_bh=?", new String[]{sample_bh}, null, null, null);
            while (cursor4.moveToNext()) {
                sample_num = cursor4.getString(0);
                sample_condition = cursor4.getString(1);
                quality_level = cursor4.getString(2);
                chouyang_num = cursor4.getString(3);
                specification_model = cursor4.getString(4);
                sample_trademark = cursor4.getString(5);
                sampling_date = cursor4.getString(6);
                sampleId = cursor4.getString(7);
            }
            cursor4.close();
            sample_name = Id2nameUtils.sample2name(sampleId, sdb);

            Cursor cursor2 = sdb.query("galaxy_application_form", new String[]{"tasks_source", "detection_station_name", "remark", "sampling_person1", "fengsamplecheck_person", "examined_company", "sample_collection_date", "weituo_number"}, "testing_no=?", new String[]{testing_no}, null, null, null);
            while (cursor2.moveToNext()) {
                tasks_source = cursor2.getString(0);
                detection_station_name = Id2nameUtils.company2name(cursor2.getString(1), sdb);
                remark = cursor2.getString(2);
                sampling_person1 = cursor2.getString(3);
                fengsamplecheck_person = cursor2.getString(4);
                examined_company = Id2nameUtils.company2name(cursor2.getString(5), sdb);
                sample_collection_date = cursor2.getString(6);
                weituoNum = cursor2.getString(7);
            }
            cursor2.close();
            if (weituoNum != null) {
                Cursor cursor7 = sdb.rawQuery("select * from galaxy_task_delegation where weituo_number =?", new String[]{weituoNum});
                while (cursor7.moveToNext()) {
                    entrust_date = cursor7.getString(cursor7.getColumnIndex("entrust_date"));
                }
                cursor7.close();
            }
            if (examined_company != null) {
                Cursor cursor6 = sdb.rawQuery("select * from galaxy_detection_company where company_name =?", new String[]{examined_company});
                while (cursor6.moveToNext()) {
                    number = cursor6.getString(cursor6.getColumnIndex("company_phone"));
                }
                cursor6.close();
            }
            inspector = Id2nameUtils.person2name(cursor.getString(cursor.getColumnIndex("inspector")), sdb);
            authorized_person = Id2nameUtils.person2name(cursor.getString(cursor.getColumnIndex("authorized_person")), sdb);
            detection_person_id = Id2nameUtils.person2name(cursor.getString(cursor.getColumnIndex("detection_person_id")), sdb);
            report_bh = cursor.getString(cursor.getColumnIndex("report_bh"));

        }
        cursor.close();

        HWPFDocument hdt = null;
        try {
            Resources res = super.getResources();
            InputStream in = res.openRawResource(R.raw.report);
            hdt = new HWPFDocument(in);
            Fields fields = hdt.getFields();
            Iterator it = fields.getFields(FieldsDocumentPart.MAIN).iterator();
            //读取word文本内容
            Range range = hdt.getRange();
            if (detection_station_name != null) {
                range.replaceText("${detection_station_name}", detection_station_name);
            } else {
                range.replaceText("${detection_station_name}", "");
            }
            if (report_bh != null) {
                range.replaceText("${report_bh} ", report_bh);
            } else {
                range.replaceText("${report_bh}", "");
            }
            if (sample_name != null) {
                range.replaceText("${sample_name}", sample_name);
            } else {
                range.replaceText("${sample_name}", "");
            }


            if (sample_trademark != null) {
                range.replaceText("${sample_trademark}", sample_trademark);
            } else {
                range.replaceText("${sample_trademark}", "");
            }
            if (specification_model != null) {
                range.replaceText("${specification_model}", specification_model);
            } else {
                range.replaceText("${specification_model}", "");
            }
            if (sampling_no != null) {
                range.replaceText("${sample_No}", sampling_no);
            } else {
                range.replaceText("${sample_No}", "");
            }

            if (quality_level != null) {
                range.replaceText("${quality_level}", quality_level);
            } else {
                range.replaceText("${quality_level}", "");
            }
            if (examined_company != null) {
                range.replaceText("${examined_company}", examined_company);
            } else {
                range.replaceText("${examined_company}", "");
            }
            if (number != null) {
                range.replaceText("${company_contacts}", number);
            } else {
                range.replaceText("${company_contacts}", "");
            }
            if (tasks_source != null) {
                range.replaceText("${tasks_source}", tasks_source);
            } else {
                range.replaceText("${tasks_source}", "");
            }
            if (sampling_person1 != null) {
                range.replaceText("${sampling_person1}", sampling_person1);
            } else {
                range.replaceText("${sampling_person1}", "");
            }
            if (sampling_date != null) {
                range.replaceText("${sampling_date}", sampling_date);
            } else {
                range.replaceText("${sampling_date}", "");
            }
            if (sample_collection_date != null) {
                range.replaceText("${sample_collection_date}", sample_collection_date);
            } else {
                range.replaceText("${sample_collection_date}", "");
            }
            if (sample_num != null) {
                range.replaceText("${sample_num}", sample_num);
            } else {
                range.replaceText("${sample_num}", "");
            }
            if (chouyang_num != null) {
                range.replaceText("${chouyang_num}", chouyang_num);
            } else {
                range.replaceText("${chouyang_num}", "");
            }
            if (testing_no != null) {
                range.replaceText("${testing_no}", testing_no);
            } else {
                range.replaceText("${testing_no}", "");
            }
            if (fengsamplecheck_person != null) {
                range.replaceText("${fengsamplecheck_person}", fengsamplecheck_person);
            } else {
                range.replaceText("${fengsamplecheck_person}", "");
            }
            if (detection_conclusion != null) {
                range.replaceText("${detection_address}", "深圳市");
            } else {
                range.replaceText("${detection_address}", "");
            }
            if (sample_condition != null) {
                range.replaceText("${sample_condition}", sample_condition);
            } else {
                range.replaceText("${sample_condition}", "");
            }
            if (detection_item_id != null) {
                range.replaceText("${detection_item_name}", detection_item_id);
            } else {
                range.replaceText("${detection_item_name}", "");
            }
            if (detection_conclusion != null) {
                range.replaceText("${detection_conclusion}", detection_conclusion);
            } else {
                range.replaceText("${detection_conclusion}", "");
            }
            if (entrust_date != null) {
                range.replaceText("${entrust_date}", entrust_date);
            } else {
                range.replaceText("${entrust_date}", "");
            }
            if (remark != null) {
                range.replaceText("${remark}", remark);
            } else {
                range.replaceText("${remark}", "");
            }
            if (authorized_person != null) {
                range.replaceText("${authorized_person}", authorized_person);
            } else {
                range.replaceText("${authorized_person}", "");
            }
            if (inspector != null) {
                range.replaceText("${inspector}", inspector);
            } else {
                range.replaceText("${inspector}", "");
            }
            if (detection_person_id != null) {
                range.replaceText("${detection_person_id}", detection_person_id);
            } else {
                range.replaceText("${detection_person_id}", "");
            }
            if (standard_value != null) {
                range.replaceText("${standard_value}", standard_value);
            } else {
                range.replaceText("${standard_value}", "");
            }
            if (detection_result != null) {
                range.replaceText("${detection_result}", detection_result);
            } else {
                range.replaceText("${detection_result}", "");
            }

            String name = DateFormat.format("yyyy_MM_dd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".doc";

            String name1 = DateFormat.format("yyyy_MM_dd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".html";
//                File file = new File("/sdcard/saveReport/");
            file = new File(Environment.getExternalStorageDirectory() + "/" + "saveReport");
            if (!file.exists()) {
                file.mkdirs();
            }//创建文件夹保存
            save = file.getPath() + File.separator + name;
            save1 = file.getPath() + File.separator + name1;
            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            FileOutputStream out = new FileOutputStream(save, true);
            hdt.write(ostream);
            //输出字节流
            out.write(ostream.toByteArray());
            out.close();
            ostream.close();
            display2html();
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
    }

    private void display2html() {
        try {
            WordToHtml.convert2Html(save, save1);

        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        WebSettings settings = wv_report.getSettings();
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
//        settings.setRenderPriority(WebSettings.RenderPriority.NORMAL);
        settings.setBlockNetworkImage(true);
//        settings.setDefaultFontSize(16);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        wv_report.setInitialScale(66);
        wv_report.loadUrl("file://" + save1);

    }

    /**
     * 初始化控件
     */
    private void initUI() {
        wv_report = (WebView) findViewById(R.id.wv_report);
        wv_report.setInitialScale(66);
       /* wv_report.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);*/
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        ll_tool = (LinearLayout) findViewById(R.id.ll_tool);
        ll_tool.setVisibility(View.GONE);
    }

    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("抽检检验报告");
        tl_bar.setSubtitle("报告单");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}

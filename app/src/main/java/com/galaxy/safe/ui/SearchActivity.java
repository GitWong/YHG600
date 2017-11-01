package com.galaxy.safe.ui;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import com.galaxy.safe.R;
import com.galaxy.safe.utils.DeleteFileUtils;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.utils.SpUtils;
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
public class SearchActivity extends Activity implements View.OnClickListener {


    private WebView wv_report;
    private Toolbar tl_bar;
    private ImageButton ib_back, ib_go, ib_finish, ib_fresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initUI();
        initToolbar();
        wv_report.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }

        });

        WebSettings settings = wv_report.getSettings();
        settings.setSupportZoom(true);

        settings.setRenderPriority(WebSettings.RenderPriority.NORMAL);
        settings.setBlockNetworkImage(false);
        settings.setJavaScriptEnabled(true);
        settings.setDefaultFontSize(16);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        String www = SpUtils.getString(SearchActivity.this, "www", "http://www.baidu.com");
        if(www!=null){
            wv_report.loadUrl(www);
        }
    }
    /**
     * 初始化控件
     */
    private void initUI() {
        wv_report = (WebView) findViewById(R.id.wv_report);
        wv_report.setInitialScale(66);
       /* wv_report.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);*/
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        ib_back = (ImageButton) findViewById(R.id.ib_back);
        ib_go = (ImageButton) findViewById(R.id.ib_go);
        ib_finish = (ImageButton) findViewById(R.id.ib_finish);
        ib_fresh = (ImageButton) findViewById(R.id.ib_fresh);

        ib_back.setOnClickListener(this);
        ib_go.setOnClickListener(this);
        ib_finish.setOnClickListener(this);
        ib_fresh.setOnClickListener(this);
    }

    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("公司网站");

        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ib_back:
                wv_report.goBack();
                break;
            case R.id.ib_go:
                wv_report.goForward();
                break;
            case R.id.ib_finish:
                finish();
                break;

            case R.id.ib_fresh:
                wv_report.reload();
                break;

            default:
                break;
        }
    }
}

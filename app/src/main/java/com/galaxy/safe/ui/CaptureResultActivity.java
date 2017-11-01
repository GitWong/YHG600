/**
 *
 */
package com.galaxy.safe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.galaxy.safe.R;

/**
 * @author yumin
 */
public class CaptureResultActivity extends Activity {

    /**
     *
     */
    private TextView tvResult;
    private TextView tvResultFormat;
    private TextView tvUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_result);
        initActivity();
        initData();
    }

    private void initActivity() {

        tvResult = (TextView) findViewById(R.id.tv_result);
        tvResultFormat = (TextView) findViewById(R.id.tv_result_format);
        tvUri = (TextView) findViewById(R.id.tv_uri);
    }

    private void initData() {

        Intent intent = getIntent();
        if (null != intent) {
            Bundle extras = intent.getExtras();


                tvResult.setText(extras.getString("result"));
//                tvResultFormat.setText(intent.getStringExtra();
                tvUri.setText(intent.toUri(intent.getFlags()));

        }
    }
}

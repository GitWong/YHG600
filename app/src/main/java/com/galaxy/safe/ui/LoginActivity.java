package com.galaxy.safe.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.galaxy.safe.R;
import com.galaxy.safe.utils.SpUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText et_user;
    private EditText et_pass;
    private Button bt_load;
    private ImageView bt_exit;
    private SQLiteDatabase sdb;
    private ImageView iv_clean;
    private ImageView iv_user;


    private void startLogin() {

        String user = et_user.getText().toString();
        if (TextUtils.isEmpty(user)) {
            Snackbar.make(et_pass, "请输入用户名", Snackbar.LENGTH_SHORT).setActionTextColor(Color.RED).show();
            return;
        }
        String pass = et_pass.getText().toString().trim();
        if (TextUtils.isEmpty(pass)) {
            Snackbar.make(et_pass, "请输入密码", Snackbar.LENGTH_SHORT).setActionTextColor(ColorStateList.valueOf(Color.RED)).show();

            return;
        }
        if (checkuser(user, pass)) {
            SpUtils.putBoolean(this, "isok", true);
            SpUtils.putString(this, "user", user);
            Intent in = new Intent(this, HomeActivity.class);
            startActivity(in);
            finish();
        } else {
            Snackbar.make(et_pass, "用户或密码不正确", Snackbar.LENGTH_SHORT).setActionTextColor(Color.RED).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        copyDB("galaxydb.db");
        initUi();
        Boolean isok = SpUtils.getboolean(this, "isok", false);
        if (isok) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
        bt_load.setOnClickListener(this);
        et_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    startLogin();
                    return true;
                }
                return false;
            }
        });
        et_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    iv_clean.setVisibility(View.VISIBLE);
                }
            }
        });


        bt_exit.setOnClickListener(this);
        iv_clean.setOnClickListener(this);
        iv_user.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initUi() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_pass = (EditText) findViewById(R.id.et_pass);
        bt_load = (Button) findViewById(R.id.bt_load);
        bt_exit = (ImageView) findViewById(R.id.bt_exit);
        iv_clean = (ImageView) findViewById(R.id.iv_clean);
        iv_user = (ImageView) findViewById(R.id.iv_user);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_load:
                startLogin();
                break;
            case R.id.bt_exit:
                finish();
                break;
            case R.id.iv_clean:
                et_pass.setText("");
                iv_clean.setVisibility(View.INVISIBLE);
                break;
            case R.id.iv_user:
                sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READONLY);//打开数据库
                View popview = View.inflate(LoginActivity.this, R.layout.popup_item, null);
                ListView lv_content = (ListView) popview.findViewById(R.id.lv_content);
                Cursor cursor = sdb.query("galaxy_detection_person", new String[]{"user_name"}, null, null, null, null, "user_name desc");
                final List<String> check_list = new ArrayList<String>();
                while (cursor.moveToNext()) {
                    String part = cursor.getString(0);
                    check_list.add(part);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, check_list);
                lv_content.setAdapter(adapter);
                final PopupWindow popupWindow = new PopupWindow(popview, et_user.getWidth(), -2, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT));
                popupWindow.showAsDropDown(et_user);
                popupWindow.setOutsideTouchable(false);
                cursor.close();
                lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        et_user.setText(check_list.get(position));
                        popupWindow.dismiss();
                    }
                });
                break;
        }
    }

    /**
     * 判断输入数据是否有误
     *
     * @param user_name
     * @param pass
     * @return
     */
    private boolean checkuser(String user_name, String pass) {

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READONLY);//打开数据库
        Cursor cursor = sdb.rawQuery("select * from galaxy_detection_person where user_name=?", new String[]{user_name});
        if (cursor.moveToFirst()) {
            int password = cursor.getColumnIndex("person_passdword");
            String passd = cursor.getString(password);
            sdb.close();
            cursor.close();
            if (pass.equals(passd)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 拷贝数据库
     *
     * @param dbname
     */
    private void copyDB(final String dbname) {
        new Thread() {
            public void run() {
                try {
                    File file = new File(getFilesDir(), dbname);
                    if (file.exists() && file.length() > 0) {
                        Log.i("Activity", "数据库是存在的。无需拷贝");
                        return;
                    }
                    InputStream is = getAssets().open(dbname);
                    FileOutputStream fos = openFileOutput(dbname, MODE_PRIVATE);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    is.close();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

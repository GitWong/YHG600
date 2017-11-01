package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.safe.Bean.ApplyInfo;
import com.galaxy.safe.Bean.SampleList;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.ACache;
import com.galaxy.safe.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

public class ApplylistActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, RefreshListView.OnRefreshListen {

    private List<ApplyInfo> cList;
    private RefreshListView lv_applylist;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private ApplyAdapt adapt;
    private Menu menu;
    private SearchView sv;
    private int position = 1;
    private int many = 30;
    private int count;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applylist);
        initView();
        initToolbar();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        lv_applylist = (RefreshListView) findViewById(R.id.lv_applylist);
        lv_applylist.isEnabledPullDownRefresh(false);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        lv_applylist.setOnRefreshListen(this);

        iv_add.setOnClickListener(this);


    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("检测单列表");
        tl_bar.setSubtitle("检测单");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tl_bar.inflateMenu(R.menu.menu_record);
        menu = tl_bar.getMenu();
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        sv = (SearchView) MenuItemCompat.getActionView(menuItem);

        sv.setSubmitButtonEnabled(true);
        sv.setIconifiedByDefault(false);
        sv.setIconified(false);
        sv.clearFocus();
        sv.setOnQueryTextListener(this);
        sv.setQueryHint("查询检测单");


    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_application_form", new String[]{"testing_no", "weituo_number", "tasks_type", String.valueOf(position * many)}, null, null, null, null, "testing_no desc");
        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<ApplyInfo>();
        }

        while (cursor.moveToNext()) {
            count = cursor.getColumnCount();
            ApplyInfo al = new ApplyInfo();
            String testing_no = cursor.getString(0);
            al.setTesting_no(testing_no);
            String weituo_number = cursor.getString(1);
            al.setWeituo_number(weituo_number);

            if (cursor.getString(2) != null) {
                switch (cursor.getString(2)) {

                    case "1":
                        al.setTasks_type("监督抽查");
                        break;
                    case "2":
                        al.setTasks_type("风险监测");
                        break;
                    case "3":
                        al.setTasks_type("市场开办者自检");
                        break;
                    case "4":
                        al.setTasks_type("委托");
                        break;
                    case "5":
                        al.setTasks_type("仲裁");
                        break;
                    case "6":
                        al.setTasks_type("比对");
                        break;
                    case "7":
                        al.setTasks_type("练兵");
                        break;
                    case "8":
                        al.setTasks_type("其他");
                        break;
                }
            }
            cList.add(al);
        }

        if (cList.size() > 20) {
            lv_applylist.isEnabledLoadingMore(true);
        } else {
            lv_applylist.isEnabledLoadingMore(false);
        }
        adapt = new ApplyAdapt();
        lv_applylist.setAdapter(adapt);
        if (cPosition != 0) {
            lv_applylist.setSelection(cPosition);
        }
        lv_applylist.setOnItemClickListener(this);
        cursor.close();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_add:
                startActivity(new Intent(this, ApplySettingActivity.class));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition = position;
        Intent i = new Intent(this, ApplySettingActivity.class);
        i.putExtra("testing_no", cList.get(position - 1).getTesting_no());
        startActivity(i);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (TextUtils.isEmpty(newText)) {
            // 清除ListView的过滤
            adapt.getFilter().filter(null);
//            lv_recordlist.clearTextFilter();
        } else {
            // 使用用户输入的内容对ListView的列表项进行过滤
            adapt.getFilter().filter(newText);
//            lv_recordlist.setFilterText(newText);
        }
        return true;
    }

    @Override
    public void onPullDownRefresh() {

    }

    @Override
    public void onLoadingMore() {

        new Handler().postDelayed(new Runnable() {

            public void run() {
                //execute the task
                if (position * many >= count) {
                    Snackbar.make(tl_bar, "别上拉了，我是有底线的...", Snackbar.LENGTH_SHORT).show();

                } else {
                    position++;
                    initData();
                    lv_applylist.setSelection((position - 1) * many);
                }
                lv_applylist.onRefreshFinish();
            }

        }, 800);

    }

    /**
     * 适配器
     */
    class ApplyAdapt extends BaseAdapter implements Filterable {
        private ArrayList<ApplyInfo> mOriginalValues;
        private String keyWrold = "";
        private final Object mLock = new Object();

        @Override
        public int getCount() {
            return cList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(ApplylistActivity.this, R.layout.item_applylist, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(cList.get(position).getTesting_no());
            if (cList.get(position).getWeituo_number().equals("")) {
                holder.tv_nature.setText("无");
            } else {
                holder.tv_nature.setText(cList.get(position).getWeituo_number());
            }
            holder.tv_company.setText(cList.get(position).getTasks_type());

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(ApplylistActivity.this);
                    build2.setTitle("删除检测单").setMessage("确定删除这个检测单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_application_form", "testing_no=?", new String[]{cList.get(position).getTesting_no()});
                            sdb.delete("galaxy_application_formcb", "testing_no=?", new String[]{cList.get(position).getTesting_no()});
                            initData();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
            return convertView;
        }


        @Override
        public Filter getFilter() {
            return mFilter;
        }

        Filter mFilter = new Filter() {
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                cList = (List<ApplyInfo>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                // TODO Auto-generated method stub
                FilterResults filterResults = new FilterResults();

                if (mOriginalValues == null) {
                    synchronized (mLock) {
                        mOriginalValues = new ArrayList<ApplyInfo>(cList);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    ArrayList<ApplyInfo> list;
                    synchronized (mLock) {
                        list = new ArrayList<ApplyInfo>(mOriginalValues);
                    }
                    filterResults.values = list;
                    filterResults.count = list.size();
                    keyWrold = "";
                } else {
                    String prefixString = prefix.toString().toLowerCase();
                    ArrayList<ApplyInfo> values;
                    synchronized (mLock) {
                        values = new ArrayList<ApplyInfo>(mOriginalValues);
                    }

                    final int count = values.size();
                    final ArrayList<ApplyInfo> newValues = new ArrayList<ApplyInfo>();

                    for (int i = 0; i < count; i++) {
                        final ApplyInfo value = values.get(i);
                        final String valueText = value.getWeituo_number().toString().toLowerCase();
                        final String valueText1 = value.getTesting_no().toString().toLowerCase();
                        final String valueText2 = value.getTasks_type().toString().toLowerCase();

                        // First match against the whole, non-splitted value
                        if (valueText.contains(prefixString) || valueText1.contains(prefixString) || valueText2.contains(prefixString)) {
                            newValues.add(value);
                            keyWrold = prefixString;
                        }
                    }

                    filterResults.values = newValues;
                    filterResults.count = newValues.size();
                }

                return filterResults;
            }

        };

        class ViewHolder {
            ImageView iv_delete;
            TextView tv_name;
            TextView tv_nature;
            TextView tv_company;

        }
    }
}

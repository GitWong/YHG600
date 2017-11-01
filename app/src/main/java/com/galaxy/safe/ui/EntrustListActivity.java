package com.galaxy.safe.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.galaxy.safe.Bean.EntrustBean;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/4/28.
 */
public class EntrustListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, RefreshListView.OnRefreshListen {

    private List<EntrustBean> cList;
    private RefreshListView lv_entrustlist;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private EntrustAdapt adapt;
    private Button bt_task;
    private int use;//完成数量
    private int unuse;//未完成数量
    private Menu menu;
    private SearchView sv;
    private int position = 1;
    private int many = 30;
    private int count;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrustlist);
        initView();
        initToolbar();
        initData();

       /* Cursor c = sdb.query("galaxy_detection_task", new String[]{" use_valid"}, "use_valid=?", new String[]{"0"}, null, null, null);
        while (c.moveToNext()) {
            unuse = c.getCount();
        }
        c.close();
        Cursor c1 = sdb.query("galaxy_detection_task", new String[]{" use_valid"}, "use_valid=?", new String[]{"1"}, null, null, null);
        while (c1.moveToNext()) {
            use = c1.getCount();
        }
        c1.close();*/


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
        cursor.close();
    }

    /**
     * 初始化控件
     */
    private void initView() {

        lv_entrustlist = (RefreshListView) findViewById(R.id.lv_entrustlist);
        lv_entrustlist.isEnabledPullDownRefresh(false);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        iv_add.setVisibility(View.GONE);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        bt_task = (Button) findViewById(R.id.bt_task);
        lv_entrustlist.setOnRefreshListen(this);
        bt_task.setOnClickListener(this);
        iv_add.setOnClickListener(this);

    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("任务列表页面");
        tl_bar.setSubtitle("已接受任务");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tl_bar.inflateMenu(R.menu.menu_task);
        menu = tl_bar.getMenu();
        MenuItem menuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        sv = (SearchView) MenuItemCompat.getActionView(menuItem);

        sv.setSubmitButtonEnabled(true);
        sv.setIconifiedByDefault(false);
        sv.setIconified(false);
        sv.clearFocus();
        sv.setOnQueryTextListener(this);
        sv.setQueryHint("查询检测任务");


        tl_bar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.task:
                        EntrustListActivity.this.startActivity(new Intent(EntrustListActivity.this, TaskActivity.class));
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_detection_task", new String[]{"task_bh", "publisher", "detection_item","publish_date"}, null, null, null, null, "publish_date desc", String.valueOf(position * many));

        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<EntrustBean>();
        }
        while (cursor.moveToNext()) {
            count = cursor.getColumnCount();
            EntrustBean eb = new EntrustBean();
            String weituo_number = cursor.getString(0);
            eb.setWeituo_number(weituo_number);
            String company_entrust_id = cursor.getString(1);
            eb.setEntrust_company(company_entrust_id);
            String item = cursor.getString(2);
            eb.setItem(item);
          /*  String isused = cursor.getString(3);
            if (isused.equals("1")) {
                eb.setIsused("1");
            } else {
                eb.setIsused("0");
            }*/
            cList.add(eb);
        }

        if (cList.size() < 13) {
            lv_entrustlist.isEnabledLoadingMore(false);
        }

        adapt = new EntrustAdapt();
        lv_entrustlist.setAdapter(adapt);
        if (cPosition != 0) {
            lv_entrustlist.setSelection(cPosition);
        }

        lv_entrustlist.setOnItemClickListener(this);
        cursor.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private View view2;//对话框ui
    private AlertDialog dialog3;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_add:
                startActivity(new Intent(this, EntrustSettingActivity.class));
                break;
            case R.id.bt_task:
                AlertDialog.Builder build2 = new AlertDialog.Builder(this);
                view2 = View.inflate(this, R.layout.edit_task, null);
                EditText euse = (EditText) view2.findViewById(R.id.et_use);
                EditText eunuse = (EditText) view2.findViewById(R.id.et_unuse);

                euse.setText("完成数量：" + use);
                eunuse.setText("未完成数量：" + unuse);

                view2.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog3.dismiss();
                    }
                });
                build2.setView(view2);
                dialog3 = build2.show();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition = position;
        Intent i = new Intent(this, EntrustSettingActivity.class);
        i.putExtra("weituo_number", cList.get(position - 1).getWeituo_number());
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

        } else {

            adapt.getFilter().filter(newText);

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
                    lv_entrustlist.setSelection((position - 1) * many);
                }
                lv_entrustlist.onRefreshFinish();
            }

        }, 800);

    }

    /**
     * 适配器
     */
    class EntrustAdapt extends BaseAdapter implements Filterable {


        private ArrayList<EntrustBean> mOriginalValues;
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

                convertView = View.inflate(EntrustListActivity.this, R.layout.item_entrustlist, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
//                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

          /*  if (!cList.get(position).getIsused().equals("1")) {
                holder.tv_name.setTextColor(Color.parseColor("#FF5450"));
            } else {
                holder.tv_name.setTextColor(Color.parseColor("#000000"));
            }*/
            holder.tv_name.setText(cList.get(position).getWeituo_number());
            holder.tv_nature.setText(cList.get(position).getEntrust_company());
            holder.tv_company.setText(cList.get(position).getItem());
          /*  holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(EntrustListActivity.this);
                    build2.setTitle("删除任务单").setMessage("确定删除" + cList.get(position).getWeituo_number()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_detection_task", "task_bh=?", new String[]{cList.get(position).getWeituo_number()});
                            initData();
                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });*/
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
                cList = (List<EntrustBean>) results.values;
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
                        mOriginalValues = new ArrayList<EntrustBean>(cList);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    ArrayList<EntrustBean> list;
                    synchronized (mLock) {
                        list = new ArrayList<EntrustBean>(mOriginalValues);
                    }
                    filterResults.values = list;
                    filterResults.count = list.size();
                    keyWrold = "";
                } else {
                    String prefixString = prefix.toString().toLowerCase();
                    ArrayList<EntrustBean> values;
                    synchronized (mLock) {
                        values = new ArrayList<EntrustBean>(mOriginalValues);
                    }

                    final int count = values.size();
                    final ArrayList<EntrustBean> newValues = new ArrayList<EntrustBean>();

                    for (int i = 0; i < count; i++) {
                        final EntrustBean value = values.get(i);
                        final String valueText = value.getWeituo_number().toString().toLowerCase();
                        final String valueText1 = value.getEntrust_company().toString().toLowerCase();
                        final String valueText2 = value.getItem().toString().toLowerCase();

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
            //            ImageView iv_delete;
            TextView tv_name;
            TextView tv_nature;
            TextView tv_company;
        }
    }
}

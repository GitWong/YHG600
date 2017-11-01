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

import com.galaxy.safe.Bean.SampleList;
import com.galaxy.safe.R;
import com.galaxy.safe.utils.Id2nameUtils;
import com.galaxy.safe.view.RefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016/3/21.
 */
public class SampleListActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, RefreshListView.OnRefreshListen {
    private List<SampleList> cList;
    private RefreshListView lv_samplelist;
    private ImageView iv_add;
    private SQLiteDatabase sdb;
    private Cursor cursor;//游标
    private Toolbar tl_bar;
    private SampleListAdapt adapt;

    private SearchView sv;
    private Menu menu;
    private int position = 1;
    private int many = 30;
    private int count;
    private int cPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_samplelist);
        initView();
        initToolbar();
        initData();
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

        lv_samplelist = (RefreshListView) findViewById(R.id.lv_samplelist);
        lv_samplelist.isEnabledPullDownRefresh(false);
        lv_samplelist.setTextFilterEnabled(true);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        lv_samplelist.setOnRefreshListen(this);
        iv_add.setOnClickListener(this);


    }

    /**
     * 初始化toolbar
     */
    private void initToolbar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("检测样品列表");
        //tl_bar.setSubtitle("检测样品");
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
        sv.setQueryHint("样品查询");
    }

    /**
     * 初始化数据
     */
    private void initData() {

        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        cursor = sdb.query("galaxy_detection_sample", new String[]{"sample_bh", "sample_id"}, null, null, null, null, "sample_bh desc", String.valueOf(position * many));
        if (cList != null && cList.size() > 0) {
            cList.clear();
        } else {
            cList = new ArrayList<SampleList>();
        }

        while (cursor.moveToNext()) {
            count = cursor.getColumnCount();
            SampleList sl = new SampleList();
            String sample_bh = cursor.getString(0);
            sl.setSample_bh(sample_bh);
            String sample_id = cursor.getString(1);
            sl.setSample_name(Id2nameUtils.sample2name(sample_id, sdb));
            if (sample_id != null) {
                Cursor cursor1 = sdb.query("galaxy_sample_typedetail", new String[]{"type_id", "sample_id"}, "sample_id=?", new String[]{sample_id}, null, null, null);
                while (cursor1.moveToNext()) {
                    sl.setSample_type(Id2nameUtils.sampletype2name(cursor1.getString(0), sdb));
                }
                cursor1.close();
            }
            cList.add(sl);
        }
        if (cList.size() < 13) {
            lv_samplelist.isEnabledLoadingMore(false);
        }
        adapt = new SampleListAdapt();
        lv_samplelist.setAdapter(adapt);
        if (cPosition != 0) {
            lv_samplelist.setSelection(cPosition);
        }
        lv_samplelist.setOnItemClickListener(this);
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
                startActivity(new Intent(this, MainActivity.class));
                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        this.cPosition = position;
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("sampleNo", cList.get(position - 1).getSample_bh());
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
//            lv_samplelist.clearTextFilter();
        } else {
            // 使用用户输入的内容对ListView的列表项进行过滤
            adapt.getFilter().filter(newText);
//            lv_samplelist.setFilterText(newText);
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
                    lv_samplelist.setSelection((position - 1) * many);
                }

                lv_samplelist.onRefreshFinish();
            }

        }, 800);


    }

    /**
     * 适配器
     */
    class SampleListAdapt extends BaseAdapter implements Filterable {
        private ArrayList<SampleList> mOriginalValues;
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
                convertView = View.inflate(SampleListActivity.this, R.layout.item_samplelist, null);
                holder = new ViewHolder();
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_nature = (TextView) convertView.findViewById(R.id.tv_nature);
                holder.tv_company = (TextView) convertView.findViewById(R.id.tv_company);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_name.setText(cList.get(position).getSample_bh());
            holder.tv_nature.setText(cList.get(position).getSample_name());
            holder.tv_company.setText(cList.get(position).getSample_type());

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder build2 = new AlertDialog.Builder(SampleListActivity.this);
                    build2.setTitle("删除检测样品").setMessage("确定删除" + cList.get(position).getSample_bh()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sdb.delete("galaxy_detection_sample", "sample_bh=?", new String[]{cList.get(position).getSample_bh()});
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
                cList = (List<SampleList>) results.values;
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
                        mOriginalValues = new ArrayList<SampleList>(cList);
                    }
                }

                if (prefix == null || prefix.length() == 0) {
                    ArrayList<SampleList> list;
                    synchronized (mLock) {
                        list = new ArrayList<SampleList>(mOriginalValues);
                    }
                    filterResults.values = list;
                    filterResults.count = list.size();
                    keyWrold = "";
                } else {
                    String prefixString = prefix.toString().toLowerCase();
                    ArrayList<SampleList> values;
                    synchronized (mLock) {
                        values = new ArrayList<SampleList>(mOriginalValues);
                    }

                    final int count = values.size();
                    final ArrayList<SampleList> newValues = new ArrayList<SampleList>();

                    for (int i = 0; i < count; i++) {
                        final SampleList value = values.get(i);
                        final String valueText = value.getSample_bh().toString().toLowerCase();
                        final String valueText1 = value.getSample_name().toString().toLowerCase();
                        final String valueText2 = value.getSample_type().toString().toLowerCase();

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

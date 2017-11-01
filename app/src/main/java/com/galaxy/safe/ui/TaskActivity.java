package com.galaxy.safe.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.galaxy.safe.Bean.Task;
import com.galaxy.safe.R;
import com.galaxy.safe.adapt.TaskAdapt;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.SpUtils;
import com.galaxy.safe.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;

/**
 * Created by Dell on 2016/4/20.
 */
public class TaskActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener, RefreshListView.OnRefreshListen {

    private Toolbar tl_bar;
    private RefreshListView rl_task;
    private HttpUtils httpUtils;// 网络工具
    private RelativeLayout rl_load;//加载刷新
    private RelativeLayout rl_none;//没有任务
    private Button bt_load;
    private Task task;
    private ProgressBar loading;
    private List<Task.DatasBean> beans;
    private SQLiteDatabase sdb;
    private TaskAdapt ta;
    Gson jon;
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        initView();
        initToolBar();
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(1000 * 5);
        // 设置超时时间
        httpUtils.configTimeout(5 * 1000);
        httpUtils.configSoTimeout(5 * 1000);
        httpUtils.configCurrentHttpCacheExpiry(1000); // 设置缓存5秒,5秒内直接返回上次成功请求的结果。
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sdb.close();
        if (jon != null) {
            jon = null;
        }
        httpUtils = null;

    }

    /**
     * 接收数据任务
     */
    private void initData() {


        RequestParams rp = new RequestParams();
        s = SpUtils.getString(this, "sbh", null);
        rp.addQueryStringParameter("last", SpUtils.getString(TaskActivity.this, "last", "2000-1-10 15:48:43"));
//        Log.i("66", SpUtils.getString(TaskActivity.this, "last", "2000-1-10 15:48:43"));
        if (s != null) {
            rp.addQueryStringParameter("station_bh", s);
            httpUtils.send(HttpRequest.HttpMethod.GET, Constants.TASK_URL, rp, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {

                    try {
                        process(responseInfo.result);
                    } catch (Exception e) {
                        rl_none.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    loading.setVisibility(View.GONE);
                    rl_load.setVisibility(View.VISIBLE);
                    rl_task.setVisibility(View.GONE);
                }
            });
        } else {
            Snackbar.make(rl_load, "站点编号为空不能请求指定任务！！！", Snackbar.LENGTH_SHORT).show();
            loading.setVisibility(View.GONE);
            rl_load.setVisibility(View.VISIBLE);
            rl_task.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        rl_task.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        rl_load.setVisibility(View.GONE);
        initData();
    }

    /**
     * 解析json
     *
     * @param json
     */
    protected void process(String json) {


        jon = new Gson();
        task = jon.fromJson(json, Task.class);
        beans = task.getDatas();
        if (beans.size() < 0) {
            rl_load.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        } else if (beans.size() == 0) {
            rl_none.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        } else {
            rl_task.setVisibility(View.VISIBLE);
            rl_load.setVisibility(View.GONE);
            rl_none.setVisibility(View.GONE);
            loading.setVisibility(View.GONE);
            ta = new TaskAdapt(this, beans, sdb);

//            Log.i("66", beans.get(0).getPublish_date());
            rl_task.setAdapter(ta);
        }

    }

    /**
     * 初始好控件
     */
    private void initView() {


        tl_bar = (Toolbar) findViewById(R.id.tl_bar);
        loading = (ProgressBar) findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        rl_task = (RefreshListView) findViewById(R.id.rv_task);
        rl_task.isEnabledLoadingMore(false);
        rl_load = (RelativeLayout) findViewById(R.id.rl_load);
        rl_none = (RelativeLayout) findViewById(R.id.rl_none);
        bt_load = (Button) findViewById(R.id.bt_load);
        rl_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initData();
            }
        });
        bt_load.setOnClickListener(this);
        rl_task.setOnRefreshListen(this);
        rl_task.setOnItemClickListener(this);

    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("最新任务");
        tl_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onPullDownRefresh() {

        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("station_bh", s);
        rp.addQueryStringParameter("last", SpUtils.getString(TaskActivity.this, "last", "2000-1-10 15:48:43"));

        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.TASK_URL, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                try {
                    process(responseInfo.result);
                } catch (Exception e) {
                    rl_none.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
                new Handler().postDelayed(new Runnable() {

                    public void run() {
                        rl_task.onRefreshFinish();
                        Toast.makeText(TaskActivity.this, "刷新成功,已是最新任务！！！", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Snackbar.make(rl_load, "刷新失败", Snackbar.LENGTH_SHORT).show();
                rl_task.onRefreshFinish();
            }
        });
    }

    @Override
    public void onLoadingMore() {

    }

    @Override
    public void onClick(View v) {
        loading.setVisibility(View.VISIBLE);
        rl_load.setVisibility(View.GONE);
        initData();
       /* httpUtils.send(HttpRequest.HttpMethod.GET, Constants.TASK_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                loading.setVisibility(View.GONE);
                process(responseInfo.result);

                Snackbar.make(rl_load, "加载成功", Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(TaskActivity.this, "加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                loading.setVisibility(View.GONE);
                rl_load.setVisibility(View.VISIBLE);
                rl_task.setVisibility(View.GONE);

            }
        });*/
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            Intent intent = new Intent(this, EntrustSettingActivity.class);
            intent.putExtra("task_bh", beans.get(position - 1).getTask_bh());
            intent.putExtra("publisher", beans.get(position - 1).getPublisher());
            intent.putExtra("detection_item", beans.get(position - 1).getDetection_item());
            intent.putExtra("detection_sample_type", beans.get(position - 1).getDetection_sample_type());
            intent.putExtra("detection_sample_name", beans.get(position - 1).getDetection_sample_name());
            intent.putExtra("batch_num", beans.get(position - 1).getBatch_num());
            intent.putExtra("completed_num", beans.get(position - 1).getCompleted_num());
            intent.putExtra("publish_date", beans.get(position - 1).getPublish_date());
            intent.putExtra("deadline", beans.get(position - 1).getDeadline());

            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

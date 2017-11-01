package com.galaxy.safe.ui;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.galaxy.safe.Bean.News;
import com.galaxy.safe.R;
import com.galaxy.safe.adapt.NewsAdapt;
import com.galaxy.safe.utils.Constants;
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
public class NewsActivity extends Activity implements View.OnClickListener, RefreshListView.OnRefreshListen {

    private Toolbar tl_bar;
    private RefreshListView rl_task;
    private HttpUtils httpUtils;// 网络工具
    private RelativeLayout rl_load;//加载刷新
    private RelativeLayout rl_none;//没有任务
    private Button bt_load;
    private News news;
    private ProgressBar loading;
    private List<News.DatasBean> beans;
    private SQLiteDatabase sdb;
    private NewsAdapt ta;
    Gson jon;
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        initView();
        initToolBar();
        sdb = SQLiteDatabase.openDatabase("/data/data/com.galaxy.safe/files/galaxydb.db", null, SQLiteDatabase.OPEN_READWRITE);//打开数据库
        httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(1000 * 5);
        // 设置超时时间
        httpUtils.configTimeout(5 * 1000);
        httpUtils.configSoTimeout(5 * 1000);
        httpUtils.configCurrentHttpCacheExpiry(5000); // 设置缓存5秒,5秒内直接返回上次成功请求的结果。
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


        rp.addQueryStringParameter("count", "20");
        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.NEWS, rp, new RequestCallBack<String>() {
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

    }


    /**
     * 解析json
     *
     * @param json
     */
    protected void process(String json) {


        jon = new Gson();
        news = jon.fromJson(json, News.class);
        beans = news.getDatas();
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
            ta = new NewsAdapt(this, beans);


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
        bt_load.setOnClickListener(this);
        rl_task.setOnRefreshListen(this);

    }

    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        tl_bar.setNavigationIcon(R.drawable.goback);
        tl_bar.setTitle("消息中心");
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
        rp.addQueryStringParameter("count", "20");
        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.NEWS, rp, new RequestCallBack<String>() {
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
                        Toast.makeText(NewsActivity.this, "刷新成功,已是最新消息！！！", Toast.LENGTH_SHORT).show();
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
    }


}

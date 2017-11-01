package com.galaxy.safe.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Dell on 2016/10/11.
 */
public class NetUtils {

    private static HttpUtils httpUtils;

    public void updateState(String s, String lo, String la) {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("device_bh", s);
        rp.addQueryStringParameter("longitude", lo);
        rp.addQueryStringParameter("latitude", la);

        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.WORKING_URL, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
              Log.i("66",responseInfo.result);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.i("66",s+e.toString());
            }
        });

    }

    public void offState(String s) {

        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("device_bh", s);

        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.OFF_URL, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    public void insertDevice(final Context context, final String s, String lo, String la, String client,
                                final String date, String working) {

        if (httpUtils == null) {
            httpUtils = new HttpUtils();
        }
        RequestParams rp = new RequestParams();
        rp.addQueryStringParameter("device_bh", s);
        rp.addQueryStringParameter("longitude", lo);
        rp.addQueryStringParameter("latitude", la);
//        rp.addQueryStringParameter("client_code", "ZD");
//        rp.addQueryStringParameter("produce_date", date);
        rp.addQueryStringParameter("working", working);
//        Toast.makeText(context, "功"+rp.toString(), Toast.LENGTH_SHORT).show();


        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.DEVICE_URL, rp, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Toast.makeText(context, "设备备份成功" + responseInfo.result, Toast.LENGTH_SHORT).show();
                SpUtils.putString(context, "device", s);
                SpUtils.putString(context, "date2", date);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(context, e.toString() + "设备备份失败" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }
}

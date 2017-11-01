package com.galaxy.safe.Service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.galaxy.safe.Bean.Task;
import com.galaxy.safe.R;
import com.galaxy.safe.ui.TaskActivity;
import com.galaxy.safe.utils.Constants;
import com.galaxy.safe.utils.SpUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

/**
 * Created by Dell on 2016/6/30.
 */
public class TaskService extends Service {


    private NotificationManager manager;
    private Notification notification;
    private PendingIntent pi;
    private boolean flag = true;
    private int ct;
    private int pt;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.flag = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread() {
            @Override
            public void run() {
                while (flag) {
                    RequestParams rp = new RequestParams();
                    String s = SpUtils.getString(getBaseContext(), "sbh", null);
                    rp.addQueryStringParameter("last", SpUtils.getString(getBaseContext(), "last", "2000-1-10 15:48:43"));
                    rp.addQueryStringParameter("station_bh", s);
                    new HttpUtils().send(HttpRequest.HttpMethod.GET, Constants.TASK_URL, rp, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            try {
                                process(responseInfo.result);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                        }
                    });
                    try {
                        Thread.sleep(600000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 解析json
     *
     * @param json
     */
    private void process(String json) {

        Gson jon = new Gson();
        Task task = jon.fromJson(json, Task.class);

        ct = task.getDatas().size();

        if (ct > 0 && ct != pt) {
            String content = task.getDatas().get(0).getDetection_sample_name() + "的" + task.getDatas().get(0).getDetection_item();
            String person = task.getDatas().get(0).getPublisher();
            String time = task.getDatas().get(0).getPublish_date();
            notification(content, person, time);
            pt = ct;
        }
    }
    private void notification(String content, String person, String date) {
        // 获取系统的通知管理器

        manager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
       /* notification = new Notification(R.drawable.add, content,
                System.currentTimeMillis());*/
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(TaskService.this)
                .setSmallIcon(R.drawable.task)
                .setContentTitle(content)
                .setContentText(person);

        mBuilder.setTicker("有新任务了");//第一次提示消息的时候显示在通知栏上
//        mBuilder.setNumber(12);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.newtask));
        mBuilder.setAutoCancel(true);//自己维护通知的消失


        Intent intent = new Intent(TaskService.this, TaskActivity.class);
        pi = PendingIntent.getActivity(TaskService.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pi);
        notification = mBuilder.build();

//       notification = new Notification();
//        notification.tickerText = "6666" + content;
//        notification.when = System.currentTimeMillis();
    /*    notification.defaults = Notification.DEFAULT_ALL; // 使用默认设置，比如铃声、震动、闪灯
        notification.flags = Notification.FLAG_AUTO_CANCEL; // 但用户点击消息后，消息自动在通知栏自动消失
        notification.flags |= Notification.FLAG_NO_CLEAR;// 点击通知栏的删除，消息不会依然不会被删除*/
        // 将消息推送到状态栏

        manager.notify(0, notification);

    }
}

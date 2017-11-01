package com.galaxy.safe.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.galaxy.safe.ui.HomeActivity;
import com.galaxy.safe.ui.SplashActivity;

/**
 * Created by Dell on 2016/5/4.
 */
public class BootReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, SplashActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //使用Receiver直接启动Activity时候需要加入此flag，否则系统会出现异常
        context.startActivity(i);
    }
}

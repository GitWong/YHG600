package com.galaxy.safe.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.galaxy.safe.utils.NetUtils;
import com.galaxy.safe.utils.SpUtils;

/**
 * Created by Dell on 2016/10/12.
 */
public class ShutdownBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SpUtils.putBoolean(context, "isok", false);
        String device = SpUtils.getString(context, "device", "");
        if(!device.equals("")){
            new NetUtils().offState(device);
        }
    }
}

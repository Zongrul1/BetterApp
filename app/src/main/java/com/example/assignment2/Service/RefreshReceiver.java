package com.example.assignment2.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class RefreshReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            context.startForegroundService(new Intent(context,PlanListService.class));
        }else{
            context.startService(new Intent(context,PlanListService.class));
        }
    }
}

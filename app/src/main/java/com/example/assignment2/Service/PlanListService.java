package com.example.assignment2.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.assignment2.MainActivity;
import com.example.assignment2.Model.DayStatus;
import com.example.assignment2.MyApplication;
import com.example.assignment2.Utils.DateUtil;
import com.example.assignment2.Utils.LogUtil;
import com.example.assignment2.db.DayStatusDao;
import com.example.assignment2.db.PlanListItemDao;

import java.util.Calendar;

public class PlanListService extends Service {
    private NotificationManager manager;
    public static final int NOTIFICATION_ID=666;
    public static final String SERVICE_CHANNEL_ID="serviceChannelId";
    public static final String SERVICE_CHANNEL_NAME="serviceChannelName";
    public PlanListService() {
        super();
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    private Notification getNotification(){
        Notification.Builder builder=new Notification.Builder(MyApplication.getContext())
                .setContentText("add")
                .setContentTitle("getNotify")
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(MyApplication.getContext(),111,
                        new Intent(MyApplication.getContext(), MainActivity.class),0));
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            builder.setChannelId(SERVICE_CHANNEL_ID);
        }
        return builder.build();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel=new NotificationChannel(SERVICE_CHANNEL_ID,SERVICE_CHANNEL_NAME,NotificationManager.IMPORTANCE_MIN);
            manager.createNotificationChannel(channel);
        }
        startForeground(NOTIFICATION_ID,getNotification());
        new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.e("service on");
                Calendar tempCalendar=Calendar.getInstance();
                tempCalendar.add(Calendar.DAY_OF_MONTH,-1);
                String time= DateUtil.getYearMonthDayNumberic(tempCalendar.getTime());
              //  LogUtil.e("Date is "+time);
                int status=DayStatusDao.queryStatus(time);
              // LogUtil.e("status="+status);
                if(status == -1){
                    DayStatus dayStatus=PlanListItemDao.updateNoRecord(time);
                    if(dayStatus!=null){
                        DayStatusDao.insertDayStatus(dayStatus);
                    }
                }
                stopForeground(true);
            }
        }).start();
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        Intent tempIntent=new Intent(this,RefreshReceiver.class);
        tempIntent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        LogUtil.e("Service here, destroy complete");
        Intent intent=new Intent(this,RefreshReceiver.class);
        sendBroadcast(intent);
        super.onDestroy();
    }
}

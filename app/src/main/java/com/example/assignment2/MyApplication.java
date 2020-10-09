package com.example.assignment2;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
    private static Context mContext;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        MyApplication.username = username;
    }

    private static String username;
    private static List<Activity> activitylsit=new LinkedList<Activity>();
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        System.out.println(mContext);
    }
    public static void addActivity(Activity activity)
    {
        activitylsit.add(activity);
    }

    public static void exit()
    {
        for(Activity activity:activitylsit){
            activity.finish();
        }
        System.exit(0);
    }
    public static void setContext(Context context){mContext = context;}
    public static Context getContext(){
        return mContext;
    }
}

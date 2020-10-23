package com.example.assignment2;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.util.LinkedList;
import java.util.List;

public class MyApplication extends Application {
    private static Context mContext;
    private static String token;
    private static String username;
    private static int stepCount;
    private static List<Activity> activitylsit=new LinkedList<Activity>();
    private static PedometerFragment pedofragment = new PedometerFragment();

    public static int getStepCount() {
        return stepCount;
    }

    public static void setStepCount(int stepCount) {
        MyApplication.stepCount = stepCount;
    }

    public static PedometerFragment getPedofragment() {
        return pedofragment;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        MyApplication.token = token;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        MyApplication.username = username;
    }
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

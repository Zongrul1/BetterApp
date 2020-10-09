package com.example.assignment2.Utils;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * @ClassName: StepService
 * @Description: step count service
 */
public class StepService extends Service {

    private static final int SAMPLING_PERIOD_US = SensorManager.SENSOR_DELAY_FASTEST;
    public static final String INTENT_ALARM_0_SEPARATE = "intent_alarm_0_separate";
    public static final String INTENT_BOOT_COMPLETED = "intent_boot_completed";
    private SensorManager mSensorManager;
    private StepCounter mStepCounter; //Sensor.TYPE_STEP_COUNTER
    private boolean mIsSeparate = false;
    private boolean mIsBoot = false;
    private int alarmCount;

    @Override
    public void onCreate() {
        super.onCreate();
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        initAlarm();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, new Notification());
        }
        if (null != intent) {
            mIsSeparate = intent.getBooleanExtra(INTENT_ALARM_0_SEPARATE, false);
            mIsBoot = intent.getBooleanExtra(INTENT_BOOT_COMPLETED, false);
        }
        startStepDetector();
        return START_STICKY;
    }

    private void startStepDetector() {
        //android4.4 -> official counter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && getStepCounter()) {
            addStepCounterListener();
        } else {
            StepSPHelper.setSupportStep(this, false);
        }
    }

    private boolean getStepCounter() {
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        boolean isHaveStepCounter = getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER);
        return null != countSensor && isHaveStepCounter;
    }

    private void addStepCounterListener() {
        StepSPHelper.setSupportStep(this, true);
        if (null != mStepCounter) {
            mStepCounter.setZeroAndBoot(mIsSeparate, mIsBoot);
            return;
        }
        Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        mStepCounter = new StepCounter(getApplicationContext(), mIsSeparate, mIsBoot);
        mSensorManager.registerListener(mStepCounter, countSensor, SAMPLING_PERIOD_US);//注册监听
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initAlarm() {
        Intent intent = new Intent(this, StepZeroAlarmReceiver.class);
        intent.setAction("alarm_0_separate");
        PendingIntent pi = PendingIntent.getBroadcast(this, alarmCount++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long firstTime = SystemClock.elapsedRealtime();
        long systemTime = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+10"));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //选择的定时时间
        long selectTime = calendar.getTimeInMillis();
        if (systemTime > selectTime) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            selectTime = calendar.getTimeInMillis();
        }
        long time = selectTime - systemTime;
        long alarmTime = firstTime + time;

        AlarmManager am = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        if (am != null) {
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarmTime, AlarmManager.INTERVAL_DAY, pi);
        }
    }

}

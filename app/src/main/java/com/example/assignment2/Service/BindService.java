package com.example.assignment2.Service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.example.assignment2.StepUtils.Step;
import com.example.assignment2.listener.StepChangeListener;
import com.example.assignment2.listener.StepUpdateUI;

public class BindService extends Service implements SensorEventListener {  //you must implement SensorEventListener to avoid failure in real Android phones

    private LcBinder lcBinder = new LcBinder();
    private int currentSteps = 0;
    private SensorManager sensorManager;
    private Step acc_StepCount;
    private StepUpdateUI stepCallback;
    /**
     * type of step sensors:  Sensor.TYPE_STEP_COUNTER or Sensor.TYPE_STEP_DETECTOR
     */
    private static int stepSensorType = -1;
    private boolean hasRecord = false;
    private int hasStepCount = 0;
    private int preStepCount = 0;
    public BindService() {}

    @Override
    public void onCreate() {
        super.onCreate();
       // Log.i("BindService—onCreate", "Start the stepCount");
        new Thread(new Runnable() {
            @Override
            public void run() {
                startStepDetector();
             //   Log.i("BindService—child thread", "startStepDetector()");
            }
        }).start();
    }

    /**
     * choose sensor type based on SDK_VERSION
     * if SDK >= 19，apply step count sensor，else apply acclerometer sensor
     */
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        int versions = Build.VERSION.SDK_INT;
        if (versions >= 19) {
            addCountStepListener();
            Log.i("Sensor using:","TYPE_STEP_COUNTER");
        } else {
            addAccStepListener();
        }
    }
    /**
     * activate Step Sensor (SDK>=19)
     */
    private void addCountStepListener() {
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (countSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_COUNTER;
            sensorManager.registerListener(BindService.this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Log.i("Type is: ", "Sensor.TYPE_STEP_COUNTER");
        } else if (detectorSensor != null) {
            stepSensorType = Sensor.TYPE_STEP_DETECTOR;
            sensorManager.registerListener(BindService.this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            addAccStepListener();
        }
    }
    /**
     * activate Acceleration Sensor
     */
    private void addAccStepListener() {
        Log.i("BindService", "Acceleration Sensor");
        acc_StepCount = new Step();
        acc_StepCount.setSteps(currentSteps);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean isAvailable = sensorManager.registerListener(acc_StepCount.getStepDetector(), sensor, SensorManager.SENSOR_DELAY_UI);
        acc_StepCount.initListener(new StepChangeListener() {
            @Override
            public void stepChanged(int steps) {
                currentSteps = steps;
                updateNotification();
            }
        });
    }

    private void updateNotification() {
        if (stepCallback != null) {
         //   Log.i("BindService", "Statistics Update");
            stepCallback.updateUi(currentSteps);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return lcBinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            int tempStep = (int) event.values[0];
            if (!hasRecord) {
                hasRecord = true;
                hasStepCount = tempStep;
            } else {
                int thisStepCount = tempStep - hasStepCount;
                int thisStep = thisStepCount - preStepCount;
                currentSteps += (thisStep);
                preStepCount = thisStepCount;
            }
        }
        else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            if (event.values[0] == 1.0) {
                currentSteps++;
            }
        } updateNotification();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public class LcBinder extends Binder {
        public BindService getService() {
            return BindService.this;
        }
    }

    public void registerCallback(StepUpdateUI paramICallback) {
        this.stepCallback = paramICallback;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
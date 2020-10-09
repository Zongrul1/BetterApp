package com.example.assignment2.Utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.SystemClock;

/**
 * @ClassName: StepCounter
 * @Description: Sensor.TYPE_STEP_COUNTER(Android 4.4+)
 */

public class StepCounter implements SensorEventListener {
    private int sOffsetStep;
    private int sCurrStep;
    private String mTodayDate;
    private boolean mIsCleanStep;
    private boolean mIsShutdown;
    private boolean mIsCounterStepReset = true;
    private Context mContext;
    private boolean mIsSeparate;
    private boolean mIsBoot;

    public StepCounter(Context context, boolean separate, boolean boot) {
        mContext = context;
        mIsSeparate = separate;
        mIsBoot = boot;
        sCurrStep = (int) StepSPHelper.getCurrentStep(mContext);
        mIsCleanStep = StepSPHelper.getCleanStep(mContext);
        mTodayDate = StepSPHelper.getStepToday(mContext);
        sOffsetStep = (int) StepSPHelper.getStepOffset(mContext);
        mIsShutdown = StepSPHelper.getShutdown(mContext);
        boolean isShutdown = shutdownBySystemRunningTime();
        if (mIsBoot || isShutdown || mIsShutdown) {
            mIsShutdown = true;
            StepSPHelper.setShutdown(mContext, true);
        }
        initBroadcastReceiver();
        dateChangeCleanStep();
    }

    private void initBroadcastReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_TIME_CHANGED);
        filter.addAction(Intent.ACTION_DATE_CHANGED);
        BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(final Context context, final Intent intent) {
                if (Intent.ACTION_TIME_TICK.equals(intent.getAction())
                        || Intent.ACTION_TIME_CHANGED.equals(intent.getAction())
                        || Intent.ACTION_DATE_CHANGED.equals(intent.getAction())) {
                    dateChangeCleanStep();
                }
            }
        };
        mContext.registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int counterStep = (int) event.values[0];
            if (mIsCleanStep) {
                cleanStep(counterStep);
            } else if (mIsShutdown || shutdownByCounterStep(counterStep)) {
                shutdown(counterStep);
            }
            sCurrStep = counterStep - sOffsetStep;
            if (sCurrStep < 0) {
                cleanStep(counterStep);
            }
            StepSPHelper.setCurrentStep(mContext, sCurrStep);
            StepSPHelper.setElapsedRealTime(mContext, SystemClock.elapsedRealtime());
            StepSPHelper.setLastSensorStep(mContext, counterStep);
            dateChangeCleanStep();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void cleanStep(int counterStep) {
        sCurrStep = 0;
        sOffsetStep = counterStep;
        StepSPHelper.setStepOffset(mContext, sOffsetStep);
        mIsCleanStep = false;
        StepSPHelper.setCleanStep(mContext, false);
    }

    private void shutdown(int counterStep) {
        int tmpCurrStep = (int) StepSPHelper.getCurrentStep(mContext);
        sOffsetStep = counterStep - tmpCurrStep;
        StepSPHelper.setStepOffset(mContext, sOffsetStep);
        mIsShutdown = false;
        StepSPHelper.setShutdown(mContext, false);
    }

    private boolean shutdownByCounterStep(int counterStep) {
        if (mIsCounterStepReset) {
            mIsCounterStepReset = false;
            if (counterStep < StepSPHelper.getLastSensorStep(mContext)) {
                return true;
            }
        }
        return false;
    }

    private boolean shutdownBySystemRunningTime() {
        if (StepSPHelper.getElapsedRealTime(mContext) > SystemClock.elapsedRealtime()) {
            return true;
        }
        return false;
    }

    private synchronized void dateChangeCleanStep() {
        if (!getTodayDate().equals(mTodayDate) || mIsSeparate) {
            mIsCleanStep = true;
            StepSPHelper.setCleanStep(mContext, true);
            mTodayDate = getTodayDate();
            StepSPHelper.setStepToday(mContext, mTodayDate);
            mIsShutdown = false;
            StepSPHelper.setShutdown(mContext, false);
            mIsBoot = false;
            mIsSeparate = false;
            sCurrStep = 0;
            StepSPHelper.setCurrentStep(mContext, sCurrStep);
        }
    }

    private String getTodayDate() {
        return StepDateUtils.getCurrentDate("yyyy-MM-dd");
    }

    public void setZeroAndBoot(boolean separate, boolean boot) {
        mIsSeparate = separate;
        mIsBoot = boot;
    }

}
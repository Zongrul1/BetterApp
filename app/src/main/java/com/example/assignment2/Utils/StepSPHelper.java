package com.example.assignment2.Utils;


import android.content.Context;

public class StepSPHelper {

    // pre step count
    private static final String LAST_SENSOR_TIME = "last_sensor_time";
    // offset
    private static final String STEP_OFFSET = "step_offset";
    // date
    private static final String STEP_TODAY = "step_today";
    // clear step count
    private static final String CLEAN_STEP = "clean_step";
    // current
    private static final String CURR_STEP = "curr_step";
    // shutdown
    private static final String SHUTDOWN = "shutdown";
    // system time
    private static final String ELAPSED_REAL_TIME = "elapsed_real_time";
    // support step count
    private static final String IS_SUPPORT_STEP = "is_support_step";

    /**
     * store pre step count
     *
     * @param context        context
     * @param lastSensorStep pre step
     */
    protected static void setLastSensorStep(Context context, float lastSensorStep) {
        StepSharedPreferencesUtil.setParam(context, LAST_SENSOR_TIME, lastSensorStep);
    }

    /**
     * read pre step count
     *
     * @param context context
     * @return pre step
     */
    protected static float getLastSensorStep(Context context) {
        return (float) StepSharedPreferencesUtil.getParam(context, LAST_SENSOR_TIME, 0.0f);
    }

    protected static void setStepOffset(Context context, float stepOffset) {
        StepSharedPreferencesUtil.setParam(context, STEP_OFFSET, stepOffset);
    }

    protected static float getStepOffset(Context context) {
        return (float) StepSharedPreferencesUtil.getParam(context, STEP_OFFSET, 0.0f);
    }

    protected static void setStepToday(Context context, String stepToday) {
        StepSharedPreferencesUtil.setParam(context, STEP_TODAY, stepToday);
    }

    protected static String getStepToday(Context context) {
        return (String) StepSharedPreferencesUtil.getParam(context, STEP_TODAY, "");
    }

    protected static void setCleanStep(Context context, boolean cleanStep) {
        StepSharedPreferencesUtil.setParam(context, CLEAN_STEP, cleanStep);
    }

    protected static boolean getCleanStep(Context context) {
        return (boolean) StepSharedPreferencesUtil.getParam(context, CLEAN_STEP, true);
    }

    protected static void setCurrentStep(Context context, float currStep) {
        StepSharedPreferencesUtil.setParam(context, CURR_STEP, currStep);
    }

    protected static float getCurrentStep(Context context) {
        return (float) StepSharedPreferencesUtil.getParam(context, CURR_STEP, 0.0f);
    }

    protected static void setShutdown(Context context, boolean shutdown) {
        StepSharedPreferencesUtil.setParam(context, SHUTDOWN, shutdown);
    }

    protected static boolean getShutdown(Context context) {
        return (boolean) StepSharedPreferencesUtil.getParam(context, SHUTDOWN, false);
    }

    protected static void setElapsedRealTime(Context context, long elapsedRealTime) {
        StepSharedPreferencesUtil.setParam(context, ELAPSED_REAL_TIME, elapsedRealTime);
    }

    protected static long getElapsedRealTime(Context context) {
        return (long) StepSharedPreferencesUtil.getParam(context, ELAPSED_REAL_TIME, 0L);
    }

    protected static void setSupportStep(Context context, boolean isSupportStep) {
        StepSharedPreferencesUtil.setParam(context, IS_SUPPORT_STEP, isSupportStep);
    }

    protected static boolean getSupportStep(Context context) {
        return (boolean) StepSharedPreferencesUtil.getParam(context, IS_SUPPORT_STEP, false);
    }

}
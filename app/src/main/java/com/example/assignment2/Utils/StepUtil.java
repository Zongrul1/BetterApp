package com.example.assignment2.Utils;

import android.content.Context;

/**
 * @ClassName: StepUtil
 * @Description: step utility
 */
public class StepUtil {

    /**
     * whether support step count or not
     *
     * @param context context content
     * @return whether or not support step count
     */
    public static boolean isSupportStep(Context context) {
        return StepSPHelper.getSupportStep(context);
    }

    /**
     * Get today step count
     *
     * @param context context content
     * @return today step count
     */
    public static int getTodayStep(Context context) {
        return (int) StepSPHelper.getCurrentStep(context);
    }

}


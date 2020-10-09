package com.example.assignment2.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @ClassName: StepShutdownReceiver
 * @Description: step shut down
 */
public class StepShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            StepSPHelper.setShutdown(context, true);
        }
    }

}


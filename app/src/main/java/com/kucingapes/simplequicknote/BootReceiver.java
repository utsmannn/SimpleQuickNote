package com.kucingapes.simplequicknote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.addNotification(context);
        MainActivity mainActivity = new MainActivity();
        mainActivity.finish();
    }

    public BootReceiver() {
        super();
    }
}
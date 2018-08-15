package com.kucingapes.simplequicknote.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.kucingapes.simplequicknote.Activity.MainActivity;

import static android.content.Context.MODE_PRIVATE;

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
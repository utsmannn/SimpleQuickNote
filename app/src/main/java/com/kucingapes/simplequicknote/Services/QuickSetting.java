package com.kucingapes.simplequicknote.Services;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;

import com.kucingapes.simplequicknote.Activity.MainActivity;

@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickSetting extends TileService {

    @Override
    public void onTileAdded() {
        super.onTileAdded();

        HomeWatcher mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.stopWatch();
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();

        startActivityAndCollapse(new Intent(getApplicationContext(), MainActivity.class));
    }
}

package com.kucingapes.simplequicknote;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;

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

        /*Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("com.kucingapes.simplequicknote.OptionActivity");*/
        startActivityAndCollapse(new Intent(getApplicationContext(), MainActivity.class));
    }
}

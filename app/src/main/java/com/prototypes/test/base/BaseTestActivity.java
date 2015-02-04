package com.prototypes.test.base;

import android.app.Activity;

import com.octo.android.robospice.SpiceManager;
import com.prototypes.test.net.TestSpiceService;

/**
 * Created by renesignoret on 1/30/15.
 */
public class BaseTestActivity  extends Activity {
    private final SpiceManager spiceManager = new SpiceManager(TestSpiceService.class);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return spiceManager;
    }
}

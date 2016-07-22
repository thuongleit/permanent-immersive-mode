package me.thuongle.fullscreenactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class OverlayActivity extends Activity {

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        toggleService();
    }


    private void toggleService() {
        Intent intent = new Intent(this, OverlayService.class);
        // Try to stop the service if it is already running
        // Otherwise start the service
        if (!stopService(intent)) {
            startService(intent);
        }
    }
}
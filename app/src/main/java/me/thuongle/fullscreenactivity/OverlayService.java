package me.thuongle.fullscreenactivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.thuongle.fullscreenactivity.R;

public class OverlayService extends Service implements View.OnSystemUiVisibilityChangeListener, Runnable {

    private static final int IMMERSIVE_MODE_FLAG =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    private static final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
                    | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                    | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, //Covers status bar
            PixelFormat.TRANSLUCENT);

    private WindowManager mWindowManager;
    private ViewGroup mViewGroup;
    private Handler mHandler;

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();

        params.gravity = Gravity.TOP;

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewGroup = (ViewGroup) layoutInflater.inflate(R.layout.windowlayout, null);
        mViewGroup.setSystemUiVisibility(IMMERSIVE_MODE_FLAG);
        mViewGroup.setOnSystemUiVisibilityChangeListener(this);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mViewGroup, params);
        mViewGroup.requestFocus();

        mViewGroup.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowManager.removeView(mViewGroup);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mViewGroup);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            mHandler.postDelayed(this, 500);
        }
    }

    @Override
    public void run() {
        mViewGroup.setSystemUiVisibility(IMMERSIVE_MODE_FLAG);
    }
}
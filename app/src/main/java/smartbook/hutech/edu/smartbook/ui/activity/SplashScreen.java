package smartbook.hutech.edu.smartbook.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import smartbook.hutech.edu.smartbook.R;
import smartbook.hutech.edu.smartbook.common.BaseActivity;

/**
 * Created by hienl on 6/23/2017.
 */

public class SplashScreen extends BaseActivity implements Runnable {
    private static final int DELAY_TIME = 2000;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    /**
     * Init activity
     */
    private void init() {
        mHandler = new Handler();
        mHandler.postDelayed(this, DELAY_TIME);
    }

    @Override
    protected int getResId() {
        return R.layout.activity_splash;
    }

    /**
     * Start main activity
     */
    @Override
    public void run() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Remove splash callback when close app
        mHandler.removeCallbacks(this);
    }
}

package com.detection.diseases.maize.usecases.commons;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.detection.diseases.maize.usecases.MainActivity;
import com.detection.diseases.maize.R;

/**
 * @author Augustine
 *
 * A Splash screen activity
 *
 * Loads before the landing activity
 *
 * Shows the apps motto and logo
 */
public class SplashScreenActivity extends AppCompatActivity {

    /**
     * Creates a thread
     */
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_actitivy);

        handler=new Handler();
        handler.postDelayed(() -> {
            Intent intent=new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        },1000);

    }
}
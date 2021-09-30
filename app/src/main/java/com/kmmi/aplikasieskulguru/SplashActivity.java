package com.kmmi.aplikasieskulguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }

    public void keDashboard(View view) {
        Intent intent   =   new Intent();
        intent.setClass(getApplicationContext(), DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
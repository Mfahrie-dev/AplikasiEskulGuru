package com.kmmi.aplikasieskulguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class DashboardActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void keEskul(View view) {
        Intent intent   =   new Intent();
        intent.setClass(getApplicationContext(),EskulActivity.class);
        startActivity(intent);
    }

    public void kePengumuman(View view) {
        Intent intent   =   new Intent();
        intent.setClass(getApplicationContext(),PengumumanActivity.class);
        startActivity(intent);
    }

    public void keDaftarSiswa(View view) {
        Intent intent   =   new Intent();
        intent.setClass(getApplicationContext(),DaftarSiswaActivity.class);
        startActivity(intent);
    }
}
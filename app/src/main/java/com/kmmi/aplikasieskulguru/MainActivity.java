package com.kmmi.aplikasieskulguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.kmmi.aplikasieskulguru.Adapter.PagetAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout   =   findViewById(R.id.tabLayout);
        viewPager   =   findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new PagetAdapter(getSupportFragmentManager(), MainActivity.this, 2));
    }
}
package com.kmmi.aplikasieskulguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;


public class DashboardActivity extends AppCompatActivity {

    SharedPreferences file;
    private CircleImageView fotoAdmin;
    private int level;
    private CardView eskul,pengumuman,siswa, admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        fotoAdmin   =  findViewById(R.id.circleImageView);
        eskul       =   findViewById(R.id.cardView_eskul);
        pengumuman  =   findViewById(R.id.cardView_pengumuman);
        siswa       =   findViewById(R.id.cardView_siswa);
        admin       =   findViewById(R.id.cardView_admin);
        file        =   getSharedPreferences("datalogin", Activity.MODE_PRIVATE);

        setFoto(file.getString("foto",""));
        cekLevel(level  =   file.getInt("level", 0));
    }
    private void cekLevel(int level) {
        if (level == 1){
            eskul.setVisibility(View.GONE);
            pengumuman.setVisibility(View.GONE);
            admin.setVisibility(View.GONE);
        }
        if (level == 2){
            eskul.setVisibility(View.GONE);
            admin.setVisibility(View.GONE);
        }
    }

    private void setFoto(String link) {
        Glide.with(DashboardActivity.this)
                .load(link)
                .centerCrop()
                .placeholder(R.drawable.profile1)
                .into(fotoAdmin);
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

    public void logout(View view) {
        file.edit().remove("level").apply();
        file.edit().remove("foto").apply();
        Intent intent   =   new Intent();
        intent.setClass(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void keAdmin(View view) {
        Intent intent   =   new Intent();
        intent.setClass(getApplicationContext(), AdminActivity.class);
        startActivity(intent);
    }
}
package com.kmmi.aplikasieskulguru;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSiswaActivity extends AppCompatActivity {

    CircleImageView fotosiswa;
    EditText nisn, nama, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_siswa);

        fotosiswa   =   findViewById(R.id.circleImageView_fotosiswa);
        nisn        =   findViewById(R.id.Edittext_nisn);
        nama        =   findViewById(R.id.Edittext_nama);
        email       =   findViewById(R.id.Edittext_email);

        nisn.setText(String.valueOf(getIntent().getIntExtra("nisn", 0)));
        nama.setText(getIntent().getStringExtra("nama"));
        email.setText(getIntent().getStringExtra("email"));

        String foto = getIntent().getStringExtra("foto");

        Glide.with(ProfileSiswaActivity.this).load(foto).placeholder(R.drawable.profile1).centerCrop().into(fotosiswa);
    }
}
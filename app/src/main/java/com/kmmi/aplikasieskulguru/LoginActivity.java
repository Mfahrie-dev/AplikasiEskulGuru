package com.kmmi.aplikasieskulguru;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kmmi.aplikasieskulguru.Api.AdminApi;
import com.kmmi.aplikasieskulguru.Model.AdminModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private SharedPreferences file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username    =   findViewById(R.id.Edittext_username);
        password    =   findViewById(R.id.Edittext_password);
        file        =   getSharedPreferences("datalogin", Activity.MODE_PRIVATE);

        cekDataLogin();
    }

    private void cekDataLogin() {
        if (!file.getString("foto","").equals("")){
            berhasilLogin();
        }
    }

    public void Login(View view) {
        Retrofit retrofit   =   new Retrofit.Builder()
                                        .baseUrl(getString(R.string.base_url))
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
        AdminApi api        =   retrofit.create(AdminApi.class);
        Call<List<AdminModel>> call = api.getAdmin(username.getText().toString(), password.getText().toString());

        call.enqueue(new Callback<List<AdminModel>>() {
            @Override
            public void onResponse(Call<List<AdminModel>> call, Response<List<AdminModel>> response) {
                if (response.isSuccessful() && response.code()==200  && response.message().equals("OK")){
                    List<AdminModel> list ;
                    list = response.body();
                    file.edit().putInt("level", list.get(0).getLevel()).apply();
                    file.edit().putString("foto", list.get(0).getFotoAdmin()).apply();
                    berhasilLogin();
                }else {
                    Toast.makeText(getApplicationContext(), "Username atau password salah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<AdminModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Username atau password salah", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void berhasilLogin(){
        Intent intent   =   new Intent().setClass(getApplicationContext(), SplashActivity.class);
        startActivity(intent);
        finish();
    }

}
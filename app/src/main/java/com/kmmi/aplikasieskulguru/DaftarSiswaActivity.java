package com.kmmi.aplikasieskulguru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.kmmi.aplikasieskulguru.Adapter.RecycleSiswaAdapter;
import com.kmmi.aplikasieskulguru.Api.SiswaApi;
import com.kmmi.aplikasieskulguru.Model.SiswaModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DaftarSiswaActivity extends AppCompatActivity {


    private List<SiswaModel> list   =   new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_siswa);

        recyclerView =   findViewById(R.id.recycleView);
        refreshLayout=   findViewById(R.id.swipeRefreshLayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getData();
                        refreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        Retrofit retrofit   =   new Retrofit.Builder()
                                        .baseUrl(getString(R.string.base_url))
                                        .addConverterFactory(GsonConverterFactory.create())
                                        .build();
        SiswaApi api    =   retrofit.create(SiswaApi.class);
        Call<List<SiswaModel>> call = api.getAllSiswa();

        call.enqueue(new Callback<List<SiswaModel>>() {
            @Override
            public void onResponse(Call<List<SiswaModel>> call, Response<List<SiswaModel>> response) {
                if (response.isSuccessful()){
                    list.clear();
                    list = response.body();
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(DaftarSiswaActivity.this, RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(new RecycleSiswaAdapter(list, DaftarSiswaActivity.this));
                }else {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SiswaModel>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
package com.kmmi.aplikasieskulguru.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SiswaApi {

    @GET("APLIKASI_ESKUL/SISWA/getAllSiswa.php")
    Call<List<SiswaApi>> getAllSiswa();
}

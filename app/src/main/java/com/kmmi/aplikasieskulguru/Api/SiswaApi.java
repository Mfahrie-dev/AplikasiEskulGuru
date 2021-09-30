package com.kmmi.aplikasieskulguru.Api;

import com.kmmi.aplikasieskulguru.Model.SiswaModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SiswaApi {

    @GET("APLIKASI_ESKUL/SISWA/getAllSiswa.php")
    Call<List<SiswaModel>> getAllSiswa();
}

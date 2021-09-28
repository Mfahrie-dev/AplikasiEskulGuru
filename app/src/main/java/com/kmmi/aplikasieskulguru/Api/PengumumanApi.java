package com.kmmi.aplikasieskulguru.Api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PengumumanApi {
    @FormUrlEncoded
    @POST("APLIKASI_ESKUL/PENGUMUMAN/insertPengumuman.php")
    Call<String> insertPengumuman(
            @Field("judulpengumuman") String judulpengumuman,
            @Field("isipengumuman") String isipengumuman,
            @Field("status") String status,
            @Field("fotopengumuman") String fotopengumuman,
            @Field("ideskul") int ideskul
    );

}

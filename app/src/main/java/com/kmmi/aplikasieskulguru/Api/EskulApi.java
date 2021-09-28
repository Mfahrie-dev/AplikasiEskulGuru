package com.kmmi.aplikasieskulguru.Api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface EskulApi {

    @FormUrlEncoded
    @POST("APLIKASI_ESKUL/ESKUL/insertEskul.php")
    Call<String> insertEskul(
            @Field("nama_eskul") String nama_eskul,
            @Field("bayaran") int bayaran,
            @Field("hari") String hari,
            @Field("jam") String jam,
            @Field("tentang") String tentang
    );
}

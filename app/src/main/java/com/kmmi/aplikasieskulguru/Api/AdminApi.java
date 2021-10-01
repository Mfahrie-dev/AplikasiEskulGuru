package com.kmmi.aplikasieskulguru.Api;

import com.kmmi.aplikasieskulguru.Model.AdminModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AdminApi {

    @GET("APLIKASI_ESKUL/ADMIN/getAdmin.php")
    Call<List<AdminModel>> getAdmin(
            @Query("username")  String username,
            @Query("password")  String password

    );

    @FormUrlEncoded
    @POST("APLIKASI_ESKUL/ADMIN/insertAdmin.php")
    Call<String> insertAdmin(
            @Field("username") String username,
            @Field("password")  String password,
            @Field("nama") String nama,
            @Field("fotoAdmin") String fotoAdmin,
            @Field("level") int level
    );
}

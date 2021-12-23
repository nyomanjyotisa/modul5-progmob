package com.example.praktikum.api;

import com.example.praktikum.model.UserHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPIHelper {
    @GET("user/{id}/detail")
    Call<List<UserHandler>> penggunaRetrieveData(
            @Path("id") Integer id
    );

    @FormUrlEncoded
    @POST("user/login")
    Call<UserHandler> checkUsernamePassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user/register")
    Call<UserHandler> penggunaInsertData(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );
}

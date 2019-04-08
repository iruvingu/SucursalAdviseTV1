package com.example.sucursaladvisetv;

import android.provider.Settings;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface InterfaceRetrofit {

        @PUT("ManagementStatus")
        Call<RetrofitClass.Response> getBeboteService(@Body RetrofitClass.Request request );

        @GET("MediaGet/{id}")
        Call<Response> getMultimediaService(@Path("id") String idScreen );

}

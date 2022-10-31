package com.example.registeration.data.api

import com.example.registeration.data.api.model.LoginModel
import com.example.registeration.data.api.model.UserModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface LoginApi {
    @POST("login/")
     fun Login(
      @Body loginModel: LoginModel
    ): Call<LoginModel>
     @GET("login/")
     fun getUsername(
         @Query("username") username : String
     ): Call<LoginModel>
    companion object{
        operator fun invoke(): LoginApi {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            return Retrofit.Builder()
                .baseUrl("https://sign-login-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(LoginApi::class.java)
        }
    }
}
package com.example.registeration.data.api

import com.example.registeration.data.api.model.UserModel
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface SignUpApi {
    @Multipart
    @POST("create/")
    suspend fun sendUser(
        @Part img : MultipartBody.Part,
        @Part("username") username : RequestBody,
        @Part("password") password : RequestBody,
        @Part("phone") phone : RequestBody,
        @Part("email") email : RequestBody,
    ) :Response<UserModel>

    companion object{
        operator fun invoke(): SignUpApi {
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
                .create(SignUpApi::class.java)
        }
    }

}
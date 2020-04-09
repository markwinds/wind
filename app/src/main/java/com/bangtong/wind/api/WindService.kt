package com.bangtong.wind.api

import com.bangtong.wind.model.User
import com.bangtong.wind.model.UserAddress
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface WindService {

    @GET("user")
    fun logIn(
        @Query("id") id:String,
        @Query("password") password:String
    ):Call<String>

    @POST("user")
    fun signUp(
        @Body user:User
    ):Call<String>

    @POST("address/insert")
    fun insertAddress(
        @Body address:UserAddress
    ):Call<Long>

    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/cloud/"
        var INSTANCE:WindService? = null

        fun getService(): WindService =
            INSTANCE?: synchronized(this){
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC
            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create()) // 这个factory要第一个添加
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WindService::class.java).also { INSTANCE = it }
        }
    }
}
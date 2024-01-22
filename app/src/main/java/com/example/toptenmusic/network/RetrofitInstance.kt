package com.example.toptenmusic.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/"

    private val client = OkHttpClient.Builder().build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val deezerApi: DeezerApi by lazy {
        retrofit.create(DeezerApi::class.java)
    }
}
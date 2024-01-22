package com.example.toptenmusic.network

import com.example.toptenmusic.model.DeezerResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DeezerApi {

    @GET("search")
    fun searchSongs(
        @Query("q") query: String,
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") apiHost: String
    ): Call<DeezerResponse>
}

package com.example.toptenmusic.network

import com.example.toptenmusic.model.DeezerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface SpotifyApiService {

    @GET("chart/0/tracks")
    suspend fun getTopTracks(): Response<DeezerResponse>

    @GET("chart/0/tracks?limit=300")
    suspend fun getTop100Tracks(): Response<DeezerResponse>
}
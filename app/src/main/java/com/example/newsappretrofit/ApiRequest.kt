package com.example.newsappretrofit

import com.example.newsappretrofit.api.GifApiJson
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiRequest {

    @GET("/v1/gifs/search")
    suspend fun getGifs(@Query("api_key") apiKey: String,
                        @Query("q") query: String,
                        @Query("limit") limit: String
    ) : GifApiJson
}
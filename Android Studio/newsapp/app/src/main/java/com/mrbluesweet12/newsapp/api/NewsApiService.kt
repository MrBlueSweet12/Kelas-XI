package com.mrbluesweet12.newsapp.api

import com.mrbluesweet12.newsapp.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("apiKey") apiKey: String,
        @Query("language") language: String = "en",
        @Query("category") category: String? = null
    ): Call<NewsResponse>

    @GET("everything")
    fun getEverything(
        @Query("apiKey") apiKey: String,
        @Query("q") query: String,
        @Query("language") language: String = "en"
    ): Call<NewsResponse>
}

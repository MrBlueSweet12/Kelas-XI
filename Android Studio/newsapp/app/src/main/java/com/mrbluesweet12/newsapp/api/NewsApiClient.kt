package com.mrbluesweet12.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object NewsApiClient {
    private const val BASE_URL = "https://newsapi.org/v2/"
    
    val service: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}

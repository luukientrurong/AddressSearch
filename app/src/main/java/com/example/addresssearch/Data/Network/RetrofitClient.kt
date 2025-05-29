package com.example.addresssearch.Data.Network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient{
    private const val BASE_URL = "https://api.locationiq.com/v1/"
    val apiServie: LocationIQApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationIQApiService::class.java)
    }
}
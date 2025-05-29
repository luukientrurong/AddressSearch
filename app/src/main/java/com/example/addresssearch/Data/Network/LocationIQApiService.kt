package com.example.addresssearch.Data.Network

import com.example.addresssearch.Data.Model.Address
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationIQApiService {
    @GET("autocomplete")
    fun searchAddress(
        @Query("key") apiKey: String,
        @Query("q") keyWord:String,
    ): Call<List<Address>>
}
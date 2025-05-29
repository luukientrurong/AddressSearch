package com.example.addresssearch.Data.Repository

import com.example.addresssearch.Data.Model.Address
import com.example.addresssearch.Data.Network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressRepository {
    private val apiKey = "pk.e7004d048eb9fc33104726ae1c8f038a"
    fun searchAddress(keyWord: String, callback:(List<Address>?, Throwable?)-> Unit){
        val call = RetrofitClient.apiServie.searchAddress(apiKey=apiKey,keyWord=keyWord)
        call.enqueue(object :Callback<List<Address>>{
            override fun onResponse(
                call: Call<List<Address>?>,
                response: Response<List<Address>?>
            ) {
                if(response.isSuccessful){
                    callback(response.body(),null)
                }else{
                    callback(null, Throwable("Api error: ${response.code()}"))
                }
            }

            override fun onFailure(
                call: Call<List<Address>?>,
                t: Throwable
            ) {
                callback(null,t)
            }

        })
    }

}
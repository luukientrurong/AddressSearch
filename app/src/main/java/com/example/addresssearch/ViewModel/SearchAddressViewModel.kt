package com.example.addresssearch.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.addresssearch.Data.Model.Address
import com.example.addresssearch.Data.Repository.AddressRepository
import kotlinx.coroutines.launch


class SearchAddressViewModel: ViewModel() {
    private val repository= AddressRepository()
    private val _address = MutableLiveData<List<Address>>()
    val address: LiveData<List<Address>> get()  = _address
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() =  _isLoading

    fun searchAddress(keyWord:String){
        Log.e("searchCheck","vao search")
        viewModelScope.launch {
            _isLoading.value = true
            repository.searchAddress(keyWord) {addresses, err->
                _isLoading.value = false
                if(addresses!=null){
                    _address.value = addresses
                }else{
                    _address.value = emptyList<Address>()

                }
            }

        }
    }
}
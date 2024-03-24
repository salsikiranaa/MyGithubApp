package com.dicoding.mygithubapp.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.dicoding.mygithubapp.data.response.ItemsItem
import com.dicoding.mygithubapp.data.retrofit.ApiConfig

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> = _listFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading



    private val _error = MutableLiveData<String>()

    companion object{
        private const val TAG = "FollowViewModel"
    }

    fun getListFollowers(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService()
            .getFollowers(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {  _isLoading.value = false
                    if (response.isSuccessful) {
                        _listFollowers.value= response.body()
                    } else {
                        Log.e(TAG, "onFailure aja: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _error.postValue("Terjadi kesalahan: ${t.message}")
                }
            })

    }

    fun getListFollowing(username: String) {
        _isLoading.value = true
        ApiConfig.getApiService()
            .getFollowing(username)
            .enqueue(object : Callback<List<ItemsItem>> {
                override fun onResponse(
                    call: Call<List<ItemsItem>>,
                    response: Response<List<ItemsItem>>
                ) {  _isLoading.value = false
                    if (response.isSuccessful) {
                        _listFollowers.value= response.body()
                    } else {
                        Log.e(TAG, "onFailure : ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                    _error.postValue("Terjadi kesalahan: ${t.message}")
                }
            })

    }


}
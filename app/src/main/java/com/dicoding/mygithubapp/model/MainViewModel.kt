package com.dicoding.mygithubapp.model


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.data.response.GithubResponse
import com.dicoding.mygithubapp.data.response.ItemsItem
import com.dicoding.mygithubapp.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel :  ViewModel (){
    private val _items= MutableLiveData<ItemsItem>()
    val items : LiveData<ItemsItem> = _items

    private val _itemRowUser = MutableLiveData<List<ItemsItem>>()
    val itemRowUser: LiveData<List<ItemsItem>> = _itemRowUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainActivity"
        private const val type = "id"
    }


    init {
        findItems()
    }

    fun findItems() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getItemsItem(type)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
            if (response.isSuccessful) {
                val items = response.body()?.items
                if (items != null && items.isNotEmpty()) {
                    _items.value = items[0]
                    _itemRowUser.value = items as List<ItemsItem>
                }

            } else {
                Log.e(TAG, "onFailure: ${response.message()}")
            }
        }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}

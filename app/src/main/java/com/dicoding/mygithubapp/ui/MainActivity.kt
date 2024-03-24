package com.dicoding.mygithubapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mygithubapp.data.response.GithubResponse
import com.dicoding.mygithubapp.data.response.ItemsItem
import com.dicoding.mygithubapp.data.retrofit.ApiConfig
import com.dicoding.mygithubapp.databinding.ActivityMainBinding
import com.dicoding.mygithubapp.model.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), UserAdapter.RecyclerViewClickListener {


    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    val value = searchView.text.toString()
                    findItems(value)
                    false
                }

        }

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)
        mainViewModel.items.observe(this) { items ->
            setUserData(listOf(items))
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager

        mainViewModel.itemRowUser.observe(this) { ItemsItem ->
            setUserData(ItemsItem)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        findItems("")

    }

    override fun onItemClick(view: View, item: ItemsItem) {
        val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
        intent.putExtra("EXTRA_ID", item.id)
        intent.putExtra("EXTRA_USERNAME", item.login)
        intent.putExtra("EXTRA_AVATARURL", item.avatarUrl)
        intent.putExtra("EXTRA_FOLLOWERS", item.followersUrl)
        intent.putExtra("EXTRA_FOLLOWING", item.followingUrl)
        Log.d(TAG, "onItemClick "+item.login)
        startActivity(intent).apply {
        }
    }

    fun findItems(string: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getItemsItem(string)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody.items)

                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserData(items: List<ItemsItem?>?) {
        val adapter = UserAdapter()
        adapter.submitList(items)
        binding.rvUser.adapter = adapter
        adapter.listener = this
    }



    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}












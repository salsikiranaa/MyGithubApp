package com.dicoding.mygithubapp.data.retrofit

import com.dicoding.mygithubapp.data.response.DetailUserResponse
import retrofit2.Call
import com.dicoding.mygithubapp.data.response.GithubResponse
import com.dicoding.mygithubapp.data.response.ItemsItem
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getItemsItem(
        @Query("q") q: String
    ): Call <GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET ("/users/{username}/followers")
    fun getFollowers(@Path("username") username : String
    ) : Call<List<ItemsItem>>

    @GET ("/users/{username}/following")
    fun getFollowing(@Path("username") username : String
    ) : Call<List<ItemsItem>>
}
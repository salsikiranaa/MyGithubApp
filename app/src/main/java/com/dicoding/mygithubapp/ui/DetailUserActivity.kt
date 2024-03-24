package com.dicoding.mygithubapp.ui

import com.dicoding.mygithubapp.model.DetailUserViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.mygithubapp.R
import com.dicoding.mygithubapp.data.response.DetailUserResponse
import com.dicoding.mygithubapp.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailUserBinding
    lateinit var dataViewModel: DetailUserViewModel


    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )

    }

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val username = intent.getStringExtra("EXTRA_USERNAME")
        dataViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailUserViewModel::class.java)
        dataViewModel.getUserDetail(username.toString())
        dataViewModel.user.observe(this) { ItemsItem ->
            setUserData(ItemsItem)
        }

        dataViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, username.toString())
        val viewPager = binding.viewPager
        viewPager.adapter = sectionPagerAdapter

        val tabLayout: TabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    private fun setUserData(items: DetailUserResponse) {
        binding.tvUsername.text = items.login
        binding.tvName.text = items.name
        Glide.with(this@DetailUserActivity)
            .load(items.avatarUrl)
            .apply(RequestOptions().override(1000, 800))
            .into(binding.userprofile)

        val tvFollowers: TextView = findViewById(R.id.tv_followers)
        val tvFollowing: TextView = findViewById(R.id.tv_following)
        tvFollowers.text = "Followers: ${items.followers}"
        tvFollowing.text = "Following: ${items.following}"

    }
}

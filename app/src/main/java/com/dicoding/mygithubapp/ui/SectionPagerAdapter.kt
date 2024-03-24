package com.dicoding.mygithubapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity, username : String) : FragmentStateAdapter(activity) {

    var username : String = username

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = FollowFragment()
        fragment.arguments = Bundle().apply {
            putInt(FollowFragment.POSITION , position+1)
            putString(FollowFragment.USERNAME , username)

        }
        return (fragment)
    }
}
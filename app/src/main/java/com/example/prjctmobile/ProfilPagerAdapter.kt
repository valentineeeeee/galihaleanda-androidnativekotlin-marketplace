package com.example.prjctmobile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfilePagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DijualFragment() // Create a ProfileFragment for the first tab
            1 -> SellProductFragment() // Create a ReviewFragment for the second tab
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}

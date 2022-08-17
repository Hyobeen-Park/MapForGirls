package com.example.mapforgirls.ui.test

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TestVPAdapter(fragment : FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Test1Fragment()
            1 -> Test2Fragment()
            2-> Test3Fragment()
            else -> Test4Fragment()
        }
    }
}
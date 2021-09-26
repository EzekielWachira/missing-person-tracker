package com.ezzy.missingpersontracker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.ViewPagerAdapter
import com.ezzy.missingpersontracker.databinding.ActivityAuthBinding
import com.ezzy.missingpersontracker.util.Constants.TAB_TITLES
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

//    private fun setUpTabs() {
//        binding.viewPager.adapter = ViewPagerAdapter(this)
//        TabLayoutMediator(
//            binding.tabLayout, binding.viewPager
//        ) { tab: TabLayout.Tab, position: Int ->
//            tab.text = TAB_TITLES[position]
//        }.attach()
//
//        binding.tabLayout.addOnTabSelectedListener(tabSelectedListener)
//    }
//
//    private val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
//        override fun onTabSelected(tab: TabLayout.Tab?) {
//            tab?.let {
//                binding.viewPager.currentItem = tab.position
//            }
//        }
//
//        override fun onTabUnselected(tab: TabLayout.Tab?) {
//            tab?.let { return }
//        }
//
//        override fun onTabReselected(tab: TabLayout.Tab?) {
//            return
//        }
//
//    }

}
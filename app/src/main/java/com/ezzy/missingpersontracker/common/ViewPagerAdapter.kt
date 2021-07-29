package com.ezzy.missingpersontracker.common

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ezzy.missingpersontracker.ui.fragments.auth.login.LoginFragment
import com.ezzy.missingpersontracker.ui.fragments.auth.signup.SignUpFragment

class ViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position) {
            0 -> fragment = LoginFragment()
            1 -> fragment = SignUpFragment()
        }
        return fragment!!
    }


}
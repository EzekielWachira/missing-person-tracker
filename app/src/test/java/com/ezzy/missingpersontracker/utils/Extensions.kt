package com.ezzy.missingpersontracker.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.ezzy.missingpersontracker.R

inline fun <reified T: Fragment> launchInHiltContainer(
    fragmentArgs: Bundle? = null,
    themeResource: Int = R.style.Theme_MaterialComponents_DayNight_DarkActionBar,
    fragmentFactory: FragmentFactory? = null,
    crossinline action: T.() -> Unit
) {

}
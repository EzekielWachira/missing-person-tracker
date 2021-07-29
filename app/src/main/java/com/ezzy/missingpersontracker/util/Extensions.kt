package com.ezzy.missingpersontracker.util

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Activity.showToast(message: String) {
    return Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    return Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
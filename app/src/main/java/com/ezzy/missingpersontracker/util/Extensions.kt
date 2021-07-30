package com.ezzy.missingpersontracker.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ezzy.missingpersontracker.R
import com.google.android.material.snackbar.Snackbar
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

private val PUNCTUATION = listOf(", ", "; ", ": ", " ")

fun String.smartTruncate(length: Int): String {
    val words = split("")
    var added = 0
    var hasMore = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMore = true
            break
        }
        builder.append(word)
        builder.append("")
        added += 1
    }

    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }

    if (hasMore) {
        builder.append("...")
    }

    return builder.toString()
}

fun Activity.showToast(message: String) {
    return Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    return Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


fun Bitmap.convertToUri(context: Context): Uri {
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path: String = MediaStore.Images.Media.insertImage(
        context.contentResolver, this, "photo", null
    )
    return Uri.parse(path)
}

fun Uri.getNameFromUri(context: Context): String {
    var name: String? = null
    if (this.scheme.equals("content")) {
        val cursor: Cursor? = context.contentResolver.query(
            this, null, null, null, null
        )
        try {
            cursor?.let {
                if (it.moveToFirst()) {
                    name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        } finally {
            cursor?.close()
        }
    }
    if (name == null) {
        name = this.path
        val cut = name?.lastIndexOf("/")
        if (cut != -1) {
            name = name?.substring(cut!!.plus(1))
        }
    }
    return name.toString()
}

fun String.makeLowerCase(): String {
    return this.toLowerCase(Locale.getDefault())
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
}

fun ImageView.applyImage(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(
            ContextCompat.getDrawable(
                context, R.drawable.placeholder
            )
        )
        .into(this)
}

fun CircleImageView.applyImage(imageUrl: String) {
    Glide.with(context)
        .load(imageUrl)
        .placeholder(
            ContextCompat.getDrawable(
                context, R.drawable.placeholder
            )
        )
        .into(this)
}

@SuppressLint("SimpleDateFormat")
fun Long.formatTimeToDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
    return format.format(date)
}
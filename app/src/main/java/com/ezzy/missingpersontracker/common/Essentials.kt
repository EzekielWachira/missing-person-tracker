package com.ezzy.missingpersontracker.common

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ezzy.missingpersontracker.util.Constants.CANCEL
import com.ezzy.missingpersontracker.util.Constants.CHOOSE_IMAGE
import com.ezzy.missingpersontracker.util.Constants.PICK_FROM_GALLERY
import com.ezzy.missingpersontracker.util.Constants.PICK_PHOTO_REQUEST_CODE
import com.ezzy.missingpersontracker.util.Constants.REQUEST_PERMISSION_CODE
import com.ezzy.missingpersontracker.util.Constants.TAKE_IMAGE_REQUEST_CODE
import com.ezzy.missingpersontracker.util.Constants.TAKE_PHOTO
import com.ezzy.missingpersontracker.util.convertToUri
import timber.log.Timber

fun <T> selectPicture(activity: Activity) {
    val options = arrayOf( TAKE_PHOTO, PICK_FROM_GALLERY, CANCEL )
    val builder = AlertDialog.Builder(activity)
    builder.apply {
        setTitle(CHOOSE_IMAGE)
        setItems(options) { dialog, which ->
            when (options[which]) {
                TAKE_PHOTO -> {
                    Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE
                    ).apply {
                        activity.startActivityIfNeeded(this, TAKE_IMAGE_REQUEST_CODE)
                    }
                }
                PICK_FROM_GALLERY -> {
                    Intent(
                        Intent.ACTION_GET_CONTENT
                    ).apply {
                        type = "image/*"
                        activity.startActivityIfNeeded(this, PICK_PHOTO_REQUEST_CODE)
                    }
                }
                CANCEL -> {
                    dialog?.dismiss()
                }
            }
        }
        show()
    }
}

fun <T> requestPermission(activity: Activity): Boolean {
    val isPermissionsGranted: Boolean
    val permissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    if (ContextCompat.checkSelfPermission(
            activity.applicationContext, permissions[0]
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            activity,
            permissions[1]
        ) == PackageManager.PERMISSION_GRANTED &&
        ContextCompat.checkSelfPermission(
            activity,
            permissions[2]
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        isPermissionsGranted = true
    } else {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION_CODE)
        isPermissionsGranted = false
    }
    return isPermissionsGranted
}

fun <T> imageResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
    activity: Activity,
    imageView: ImageView
): Uri? {
    var picImageUri: Uri? = null
    if (resultCode != AppCompatActivity.RESULT_CANCELED) {
        when (requestCode) {
            TAKE_IMAGE_REQUEST_CODE -> {
                data?.let {
                    if (resultCode == AppCompatActivity.RESULT_OK) {
                        val bitMap = data.extras?.get("data") as Bitmap
                        imageView.setImageBitmap(bitMap)
                        picImageUri = bitMap.convertToUri(activity)
                        Timber.d("PHOTO: $picImageUri")
                    }
                }
            }
            PICK_PHOTO_REQUEST_CODE -> {
                data?.data?.let {
                    if (resultCode == AppCompatActivity.RESULT_OK) {
                        val imageUri: Uri = it
                        imageUri.let { uri ->
                            imageView.setImageURI(uri)
                            picImageUri = uri
                        }
                    }
                }
            }
        }
    }
    return picImageUri
}
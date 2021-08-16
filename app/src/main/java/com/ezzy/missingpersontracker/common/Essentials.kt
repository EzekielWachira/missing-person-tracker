package com.ezzy.missingpersontracker.common

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.test.platform.app.InstrumentationRegistry
import com.erikagtierrez.multiple_media_picker.Gallery
import com.ezzy.missingpersontracker.util.Constants.CANCEL
import com.ezzy.missingpersontracker.util.Constants.CHOOSE_IMAGE
import com.ezzy.missingpersontracker.util.Constants.PICK_FROM_GALLERY
import com.ezzy.missingpersontracker.util.Constants.PICK_PHOTO_REQUEST_CODE
import com.ezzy.missingpersontracker.util.Constants.REQUEST_PERMISSION_CODE
import com.ezzy.missingpersontracker.util.Constants.TAKE_IMAGE_REQUEST_CODE
import com.ezzy.missingpersontracker.util.Constants.TAKE_PHOTO
import com.ezzy.missingpersontracker.util.convertToUri
import timber.log.Timber
import java.lang.Exception

fun <T> selectPicture(activity: Activity) {
    val options = arrayOf(TAKE_PHOTO, PICK_FROM_GALLERY, CANCEL)
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
                    if (Build.VERSION.SDK_INT < 19) {
                        Intent(
                            Intent.ACTION_GET_CONTENT
                        ).apply {
                            type = "image/*"
                            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                            addCategory(Intent.CATEGORY_OPENABLE)
                            activity.startActivityIfNeeded(
                                Intent.createChooser(this, "Select picture"),
                                PICK_PHOTO_REQUEST_CODE
                            )
                        }
                    } else {
                        Intent(
                            Intent.ACTION_OPEN_DOCUMENT
                        ).apply {
                            type = "image/*"
                            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                            addCategory(Intent.CATEGORY_OPENABLE)
                            activity.startActivityIfNeeded(this, PICK_PHOTO_REQUEST_CODE)
                        }
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
//    imageView: ImageView
): Uri? {
    var picImageUri: Uri? = null
    if (resultCode != RESULT_CANCELED) {
        when (requestCode) {
            TAKE_IMAGE_REQUEST_CODE -> {
                data?.let {
                    if (resultCode == RESULT_OK) {
                        val bitMap = data.extras?.get("data") as Bitmap
//                        imageView.setImageBitmap(bitMap)
                        picImageUri = bitMap.convertToUri(activity)
                        Timber.d("PHOTO: $picImageUri")
                    }
                }
            }
            PICK_PHOTO_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    if (data?.clipData != null) {
                        val count = data.clipData?.itemCount
                        for (i in 0..count!!.minus(1)) {
                            val imageUri = data.clipData?.getItemAt(i)?.uri
                            getPathFromUri(imageUri!!, activity.applicationContext)
                        }
                    } else if (data?.data != null) {
                        val imageUri: Uri = data.data!!
                        val imagePath: String = data.data!!.path!!
                        imageUri.let { uri ->
//                            imageView.setImageURI(uri)
                            picImageUri = uri
                        }

                    }
                }
            }
        }
    }
    return picImageUri
}

fun getPathFromUri(uri: Uri, context: Context): List<String> {

    val path: String = uri.path!!
    val databaseUri: Uri
    val selection: String?
    val selectionArgs: Array<String>?
    lateinit var imagePath: String
    if (path.contains("/document/image:")) {
        databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        selection = "_id=?"
        selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
    } else {
        databaseUri = uri
        selection = null
        selectionArgs = null
    }

    val imagesPathList = mutableListOf<String>()
    try {
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.DATE_TAKEN
        )
        val cursor = context.contentResolver.query(
            databaseUri, projection, selection, selectionArgs, null
        )
        if (cursor?.moveToFirst()!!) {
            val columnIndex = cursor.getColumnIndex(projection[0])
            imagePath = cursor.getString(columnIndex)
            Timber.d("PATH: $imagePath")
            imagesPathList.add(imagePath)
        }
        cursor.close()
        Timber.d("IMAGES $imagesPathList")
        return imagesPathList
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return emptyList()
}
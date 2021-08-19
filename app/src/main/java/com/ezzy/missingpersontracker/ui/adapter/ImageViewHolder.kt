package com.ezzy.missingpersontracker.ui.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.ezzy.missingpersontracker.data.model.ImageItem
import com.ezzy.missingpersontracker.databinding.ImageItemBinding
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.InputStream

class ImageViewHolder(
    private val binding: ImageItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bindItem(imageItem: ImageItem) {
        with(binding) {
            Timber.d(imageItem.toString())
            try {
                val inputStream = binding.root.context
                    .contentResolver.openInputStream(imageItem.uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                personImage.setImageBitmap(bitmap)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

}
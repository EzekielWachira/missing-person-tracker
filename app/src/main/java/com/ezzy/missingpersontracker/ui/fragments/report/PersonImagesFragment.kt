package com.ezzy.missingpersontracker.ui.fragments.report

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.getPathFromUri
import com.ezzy.missingpersontracker.common.imageResult
import com.ezzy.missingpersontracker.common.requestPermission
import com.ezzy.missingpersontracker.common.selectPicture
import com.ezzy.missingpersontracker.databinding.FragmentPersonImagesBinding
import com.ezzy.missingpersontracker.util.Constants
import com.ezzy.missingpersontracker.util.convertToUri
import com.ezzy.missingpersontracker.util.showToast
import timber.log.Timber

class PersonImagesFragment : Fragment() {

    private var _binding: FragmentPersonImagesBinding? = null
    private val binding: FragmentPersonImagesBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonImagesBinding.inflate(inflater, container, false)
        setUpUI()
        return binding.root
    }

    private fun setUpUI() {
        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_personImagesFragment_to_personContactsFragment)
        }
        binding.btnSelectImage.setOnClickListener { requestPermissions() }
    }

    private fun requestPermissions() {
        if (requestPermission<PersonImagesFragment>(requireActivity())) {
            selectImage()
        }
    }

    private fun selectImage() {
        selectPicture<PersonImagesFragment>(requireActivity())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        requestPermissions()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        val imageUri = imageResult<PersonImagesFragment>(requestCode, resultCode, data, requireActivity())
        var picImageUri: Uri? = null
        when (requestCode) {
            Constants.TAKE_IMAGE_REQUEST_CODE -> {
                data?.let {
                    if (resultCode == RESULT_OK) {
                        val bitMap = data.extras?.get("data") as Bitmap
//                        imageView.setImageBitmap(bitMap)
                        picImageUri = bitMap.convertToUri(requireActivity())
                        this.showToast("$bitMap")
                        Timber.d("PHOTO: $picImageUri")
                    }
                }
            }
            Constants.PICK_PHOTO_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    if (data?.clipData != null) {
                        data.clipData.let {
                            val count = it?.itemCount
                            for (i in 0 until count!!.minus(1)) {
                                val imageUri = data.clipData?.getItemAt(i)?.uri
                                getPathFromUri(imageUri!!, requireActivity().applicationContext)
                            }
                        }
                    } else if (data?.data != null) {
                        data.data?.let {
                            Timber.d("IMAGE URI: $it")
                            val imageUri: Uri = it
                            val imagePath: String = it.path!!
                            imageUri.let { uri ->
//                            imageView.setImageURI(uri)
                                picImageUri = uri
                            }
                        }

                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
package com.ezzy.missingpersontracker.ui.fragments.report

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.*
import com.ezzy.missingpersontracker.data.model.ImageItem
import com.ezzy.missingpersontracker.databinding.FragmentPersonImagesBinding
import com.ezzy.missingpersontracker.ui.adapter.PersonImageAdapter
import com.ezzy.missingpersontracker.util.Constants
import com.ezzy.missingpersontracker.util.convertToUri
import com.ezzy.missingpersontracker.util.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PersonImagesFragment : Fragment() {

    private var _binding: FragmentPersonImagesBinding? = null
    private val binding: FragmentPersonImagesBinding get() = _binding!!
    private val viewModel: ReportMissingPersonViewModel by activityViewModels()
    private val personImageAdapter: PersonImageAdapter by lazy { PersonImageAdapter() }
    private val personImages = mutableListOf<ImageItem>()

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                chooseImage()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showPermissionRationale()
                }
            }
        }

    private fun showPermissionRationale() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.rationale_title))
            .setMessage(getString(R.string.rationale_description))
            .setPositiveButton(getString(R.string.pos_text)) { dialog, _ ->
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.neg_text)) { dialog, _ ->
                dialog.cancel()
            }
    }

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val data = result.data
            val uris = ArrayList<Uri>()
            if (data?.data != null) {
                uris.add(data.data!!)
            } else {
                if (data?.clipData != null) {
                    val clipData = data.clipData

                    for (i in 0 until clipData?.itemCount!!) {
                        val image = clipData.getItemAt(i)
                        uris.add(image.uri)
                        personImages.add(ImageItem(image.uri))
                    }
                    viewModel.addPersonImages(personImages)
                }
            }
            val images = ArrayList<ImageItem>()

            for (uri in uris) {
                images.add(ImageItem(uri))
            }
            Timber.i(" size is ${images.toString()}")
            personImageAdapter.submitList(images.toList())
        } else {
            showToast("Failed! Try again.")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPersonImagesBinding.inflate(inflater, container, false)
        setUpUI()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.photoRecyclerView
            .apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                adapter = personImageAdapter
                addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
                addItemDecoration(ItemDecorator(Directions.HORIZONTAL, 5))
            }
    }

    private fun setUpUI() {
        binding.btnNext.setOnClickListener {
            if (personImages.isEmpty()) {
                showToast("You must select at least 3 images")
            } else {
                findNavController().navigate(
                    R.id.action_personImagesFragment_to_personContactsFragment
                )
            }
        }
        binding.btnSelectImage.setOnClickListener { requestPermissions() }
    }

    private fun requestPermissions() {
        if (isPermissionGranted()) {
            chooseImage()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun selectImage() {
        selectPicture<PersonImagesFragment>(requireActivity())
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun chooseImage() {
        val intent = Intent().apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            action = Intent.ACTION_GET_CONTENT
        }
        imagePicker.launch(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
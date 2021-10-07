package com.ezzy.missingpersontracker.ui.activities.run_face_identification

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.MissingPerson
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.data.model.ImageItem
import com.ezzy.missingpersontracker.databinding.ActivityFaceIdentificationBinding
import com.ezzy.missingpersontracker.ui.activities.person_details.PersonDetailsActivity
import com.ezzy.missingpersontracker.util.showErrorDialog
import com.ezzy.missingpersontracker.util.showLoadingDialog
import com.ezzy.missingpersontracker.util.showToast
import com.ezzy.missingpersontracker.util.visible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.text.DecimalFormat
import java.text.NumberFormat

@AndroidEntryPoint
class FaceIdentificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceIdentificationBinding
    private lateinit var progressDialog: SweetAlertDialog
    private lateinit var errorDialog: SweetAlertDialog
    private var imageUri: Uri? = null
    private var missingPerson: MissingPerson? = null

    private val identificationViewModel: FaceIdentificationViewModel by viewModels()

    val localModel = LocalModel.Builder()
        .setAssetFilePath("model/model-export_icn_tflite-missing_person_16_20210921101644-2021-09-22T09_07_42.523520Z_model.tflite")
        .build()

    val customImageLabelerOptions = CustomImageLabelerOptions.Builder(localModel)
        .setConfidenceThreshold(0.5f)
        .setMaxResultCount(5)
        .build()
    val labeler = ImageLabeling.getClient(customImageLabelerOptions)

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                chooseImage()
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showPermissionRationale()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceIdentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Run image scanner"
            setDisplayHomeAsUpEnabled(true)
        }

        progressDialog = showLoadingDialog("Searching person in the cloud server...")
        errorDialog = showErrorDialog("Error", "Person not found in the servers")

        setUpUI()
        subscribeToUI()
    }

    private fun setUpUI() {
        with(binding) {
            takePhotoBtn.setOnClickListener {
                requestPermissions()
            }

            processPhotoBtn.setOnClickListener {
                if (missingPerson != null) {
                    startActivity(
                        Intent(
                            this@FaceIdentificationActivity, PersonDetailsActivity::class.java
                        ).apply {
                            putExtra("missingPerson", missingPerson)
                        }
                    )
                } else errorDialog.show()
            }
        }
    }

    private fun subscribeToUI() {
        val intent = Intent(this, PersonDetailsActivity::class.java)
        lifecycleScope.launch {
            identificationViewModel.missingPersons.collect { state ->
                when (state) {
                    is Resource.Loading -> progressDialog.show()
                    is Resource.Success -> {
                        missingPerson = state.data[0]
                        progressDialog.hide()
                    }
                    is Resource.Failure -> {
                        progressDialog.hide()
                        errorDialog.show()
                    }
                }
            }
        }
    }

    /**
     * process image for image recognition
     * @param imageUri
     * */
    private fun processImage(imageUri: Uri) {
        val image: InputImage
        try {
            image = InputImage.fromFilePath(this, imageUri)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        val index = label.index
                        val formatter = DecimalFormat("#.##")
                        val percentage = formatter.format(confidence.times(100))
                        with(binding) {
                            display.visible()
                            nameTextView.text = text
                            accuracyTextView.text = percentage.toString() + "%"
                        }

                        if (text.isNotEmpty()) {
                            identificationViewModel.searchMissingPersonByName(text)
                        } else {
                            errorDialog.show()
                        }

                    }
                }
                .addOnFailureListener { e ->
                    Timber.e("ERRRROR: ${e.message.toString()}")
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun showPermissionRationale() {
        MaterialAlertDialogBuilder(this)
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

    private val imageFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            imageUri?.let { uri ->
                binding.imageView.setImageURI(uri)
            }
        }
    }

    private fun takeImage() {
        lifecycleScope.launchWhenStarted {

        }
    }

    /**
     * select an image from gallery
     * */
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

                    }
//                    viewModel.addPersonImages(personImages)
                }
            }
            val images = ArrayList<ImageItem>()

            for (uri in uris) {
                images.add(ImageItem(uri))
            }
            binding.imageView.setImageURI(uris[0])
            processImage(uris[0])
            Timber.i(" size is ${images.toString()}")
//            personImageAdapter.submitList(images.toList())
        } else {
            showToast("Failed! Try again.")
        }
    }

    private fun requestPermissions() {
        if (isPermissionGranted()) {
            chooseImage()
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun chooseImage() {
        val intent = Intent().apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            action = Intent.ACTION_GET_CONTENT
        }
        imagePicker.launch(intent)
    }

}
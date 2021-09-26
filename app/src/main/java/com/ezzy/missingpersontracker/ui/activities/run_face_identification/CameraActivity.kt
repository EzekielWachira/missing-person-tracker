package com.ezzy.missingpersontracker.ui.activities.run_face_identification

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.ezzy.missingpersontracker.databinding.ActivityCamera2Binding
import com.ezzy.missingpersontracker.databinding.ActivityCameraBinding
import com.ezzy.missingpersontracker.ml.PersonModel
import com.ezzy.missingpersontracker.ml.YuvToRgbConverter
import com.ezzy.missingpersontracker.ml.model.Recognition
import org.tensorflow.lite.gpu.CompatibilityList
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.model.Model
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCamera2Binding
    private lateinit var preview: Preview
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var camera: Camera

    private val cameraExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamera2Binding.inflate(layoutInflater)
        setContentView(binding.root)

    }












//
//    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(
//            baseContext, it
//        ) == PackageManager.PERMISSION_GRANTED
//    }


    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(224, 224))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysisUseCase ->
                    analysisUseCase.setAnalyzer(cameraExecutor, {

                    })
                }

            val cameraSelector = if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

//                preview.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private class ImageAnalyzer(ctx: Context, private val listener: RecognitionListener) :
    ImageAnalysis.Analyzer {
        private val personModel: PersonModel by lazy {
            val compatList = CompatibilityList()
            val options = if (compatList.isDelegateSupportedOnThisDevice)
                Model.Options.Builder().setDevice(Model.Device.GPU).build()
            else Model.Options.Builder().setNumThreads(4).build()

            PersonModel.newInstance(ctx, options)
        }

        override fun analyze(image: ImageProxy) {
            val items = mutableListOf<Recognition>()
            val tfImage = TensorImage.fromBitmap(toBitmap(image))
//            val outputs = personModel.process(tfImage.tensorBuffer)
//                .probabilityAsCategoryList.apply {
//                    sortByDescending { it.score } // Sort with highest confidence first
//                }.take(MAX_RESULT_DISPLAY) // take the top results

            // TODO 4: Converting the top probability items into a list of recognitions
//            for (output in outputs) {
//                items.add(Recognition(output.label, output.score))
//            }

//            // START - Placeholder code at the start of the codelab. Comment this block of code out.
//            for (i in 0 until MAX_RESULT_DISPLAY){
//                items.add(Recognition("Fake label $i", Random.nextFloat()))
//            }
//            // END - Placeholder code at the start of the codelab. Comment this block of code out.

            // Return the result
//            listener(items.toList())

            // Close the image,this tells CameraX to feed the next image to the analyzer
            image.close()

        }

        private val yuvToRgbConverter = YuvToRgbConverter(ctx)
        private lateinit var bitmapBuffer: Bitmap
        private lateinit var rotationMatrix: Matrix

        @SuppressLint("UnsafeExperimentalUsageError")
        private fun toBitmap(imageProxy: ImageProxy): Bitmap? {

//            val image = imageProxy.image ?: return null

            // Initialise Buffer
            if (!::bitmapBuffer.isInitialized) {
                // The image rotation and RGB image buffer are initialized only once
                rotationMatrix = Matrix()
                rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                bitmapBuffer = Bitmap.createBitmap(
                    imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
                )
            }

            // Pass image to an image analyser
//            yuvToRgbConverter.yuvToRgb(image, bitmapBuffer)

            // Create the Bitmap in the correct orientation
            return Bitmap.createBitmap(
                bitmapBuffer,
                0,
                0,
                bitmapBuffer.width,
                bitmapBuffer.height,
                rotationMatrix,
                false
            )
        }
    }



}
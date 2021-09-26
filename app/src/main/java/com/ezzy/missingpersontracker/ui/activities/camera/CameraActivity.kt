package com.ezzy.missingpersontracker.ui.activities.camera

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.ActivityCamera2Binding
import com.ezzy.missingpersontracker.databinding.ActivityCameraBinding

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivityCamera2Binding.inflate(layoutInflater)
        setContentView(R.layout.activity_camera)
    }
}
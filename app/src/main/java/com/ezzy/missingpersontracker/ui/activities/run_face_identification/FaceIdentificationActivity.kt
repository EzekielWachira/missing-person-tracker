package com.ezzy.missingpersontracker.ui.activities.run_face_identification

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ezzy.missingpersontracker.databinding.ActivityFaceIdentificationBinding

class FaceIdentificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaceIdentificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFaceIdentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Run face scanner"
            setDisplayHomeAsUpEnabled(true)
        }
    }

}
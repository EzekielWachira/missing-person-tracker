package com.ezzy.missingpersontracker.ui.activities.person_details

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezzy.missingpersontracker.databinding.ActivityPersonDetailsBinding
import com.ezzy.missingpersontracker.ui.activities.report_found_person.ReportFoundPersonActivity
import dagger.hilt.android.AndroidEntryPoint

class PersonDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Person details"
            setDisplayHomeAsUpEnabled(true)
        }

        setUpUI()
    }

    private fun setUpUI() {
        binding.reportFoundFab.setOnClickListener {
            startActivity(Intent(
                this, ReportFoundPersonActivity::class.java
            ))
        }
    }
}
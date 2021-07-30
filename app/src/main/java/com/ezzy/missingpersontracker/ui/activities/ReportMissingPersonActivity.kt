package com.ezzy.missingpersontracker.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.ActivityReportMissingPersonBinding

class ReportMissingPersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportMissingPersonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportMissingPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.report_missing_person)
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
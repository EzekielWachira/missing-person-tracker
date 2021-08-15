package com.ezzy.missingpersontracker.ui.activities.report_found_person

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezzy.missingpersontracker.databinding.ActivityReportFoundPersonBinding

class ReportFoundPersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportFoundPersonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportFoundPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = "Report Found Person"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}
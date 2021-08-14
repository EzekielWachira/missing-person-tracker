package com.ezzy.missingpersontracker.ui.activities.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.ActivitySearchMissingPersonBinding
import dagger.hilt.android.AndroidEntryPoint

class SearchMissingPersonActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchMissingPersonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchMissingPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        actionBar?.hide()
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_chevron_left)
        }

        binding.search.requestFocus()

    }
}
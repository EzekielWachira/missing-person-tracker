package com.ezzy.missingpersontracker.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.ActivityMainBinding
import com.ezzy.missingpersontracker.util.showToast

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = this.findNavController(R.id.mainNavHostContainer)
        binding.bottomNavigation.setupWithNavController(navController)

        setUpUI()
    }

    private fun setUpUI() {
        binding.fab.setOnClickListener {
            startActivity(Intent(
                this,
                ReportMissingPersonActivity::class.java
            ))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_search -> this.showToast("Search selected")
            R.id.action_logout -> this.showToast("Logout selected")
        }
        return super.onOptionsItemSelected(item)
    }
}
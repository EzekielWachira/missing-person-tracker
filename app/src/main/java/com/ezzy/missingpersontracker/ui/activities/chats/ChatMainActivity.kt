package com.ezzy.missingpersontracker.ui.activities.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ezzy.missingpersontracker.databinding.ActivityChatMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
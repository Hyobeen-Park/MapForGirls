package com.example.mapforgirls.ui.main.chatting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapforgirls.databinding.ActivityChattingDetailBinding

class ChattingDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityChattingDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
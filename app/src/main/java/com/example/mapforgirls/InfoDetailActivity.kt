package com.example.mapforgirls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapforgirls.databinding.ActivityInfoDetailBinding


class InfoDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityInfoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
package com.example.mapforgirls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapforgirls.databinding.ActivityColumnDetailBinding


class ColumnDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityColumnDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColumnDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
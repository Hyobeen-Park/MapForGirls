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

        var column = intent.getSerializableExtra("column") as ColumnData
        binding.columnDetailCoverIv.setImageResource(column.cover!!)
        binding.columnDetailAuthorTv.text = column.author
        binding.columnDetailTitleTv.text = column.title
        binding.columnDetailContentTv.text = column.content
    }
}
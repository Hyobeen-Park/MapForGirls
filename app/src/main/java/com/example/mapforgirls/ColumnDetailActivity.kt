package com.example.mapforgirls

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mapforgirls.databinding.ActivityColumnDetailBinding


class ColumnDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityColumnDetailBinding
    var isScrapped : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityColumnDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var column = intent.getSerializableExtra("column") as ColumnData
        binding.columnDetailCoverIv.setImageResource(column.cover!!)
        binding.columnDetailAuthorTv.text = column.author
        binding.columnDetailTitleTv.text = column.title
        binding.columnDetailContentTv.text = column.content

        isScrapped = isScrappedColumn(column.sectionName!!, column.columnId!!)

        setViews()
        setOnClickListeners(column)
    }

    private fun setViews() {
        if(isScrapped) {
            binding.columnDetailScrapIv.setImageResource(R.drawable.ic_launcher_background)
        } else {
            binding.columnDetailScrapIv.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    private fun setOnClickListeners(column: ColumnData) {
        binding.columnDetailScrapIv.setOnClickListener {
            if(isScrapped) {
                binding.columnDetailScrapIv.setImageResource(R.drawable.ic_launcher_foreground)
                cancelScrap(column.sectionName!!, column.columnId!!)
            } else {
                binding.columnDetailScrapIv.setImageResource(R.drawable.ic_launcher_background)
                scrapColumn(column.sectionName!!, column.columnId!!)
            }

            isScrapped = !isScrapped
        }
    }

    private fun isScrappedColumn(sectionName : String, columnId: String): Boolean {
        val columnDB = ColumnDatabase.getInstance(this@ColumnDetailActivity)!!

        val isScrapped: Int? = columnDB.columnDao().isScrapedColumn(sectionName, columnId)

        return isScrapped != null
    }

    private fun scrapColumn(sectionName: String, columnId: String) {
        val columnDB = ColumnDatabase.getInstance(this@ColumnDetailActivity)!!
        columnDB.columnDao().scrapColumn(Scrap(sectionName, columnId))
    }

    private fun cancelScrap(sectionName: String, columnId: String) {
        val columnDB = ColumnDatabase.getInstance(this@ColumnDetailActivity)!!
        columnDB.columnDao().cancelScrap(sectionName, columnId)
    }
}
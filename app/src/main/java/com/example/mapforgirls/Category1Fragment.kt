package com.example.mapforgirls

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mapforgirls.databinding.FragmentCategory1Binding
import com.google.firebase.database.*
import java.util.*
import com.google.firebase.database.DatabaseReference


class Category1Fragment : Fragment() {
    lateinit var binding: FragmentCategory1Binding
    var columnList = ArrayList<ColumnData>()
    private var database : DatabaseReference = FirebaseDatabase.getInstance().reference
    var isScrapped : Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategory1Binding.inflate(inflater, container, false)

        //카테고리 이름 받아오기
        val sharedPreferences = (context as MainActivity).getSharedPreferences("category", Context.MODE_PRIVATE)
        val category = sharedPreferences.getString("category", null)
        binding.category1TitleTv.text = category.toString()

        isScrapped = isScrappedColumn("section1", "a1")

        setViews()
        connectDatabase()
        setOnClickListeners()

        return binding.root
    }

    private fun setViews() {
        if(isScrapped) {
            binding.category1ScrapIv.setImageResource(R.drawable.ic_launcher_background)
        } else {
            binding.category1ScrapIv.setImageResource(R.drawable.ic_launcher_foreground)
        }
    }

    private fun setOnClickListeners() {
        binding.category1ScrapIv.setOnClickListener {
            if(isScrapped) {
                binding.category1ScrapIv.setImageResource(R.drawable.ic_launcher_foreground)
                cancelScrap("section1", "00")
            } else {
                binding.category1ScrapIv.setImageResource(R.drawable.ic_launcher_background)
                scrapColumn("section1", "00")
            }

            isScrapped = !isScrapped
        }
    }

    private fun isScrappedColumn(sectionName : String, columnId: String): Boolean {
        val columnDB = ColumnDatabase.getInstance(requireContext())!!

        val isScrapped: Int? = columnDB.columnDao().isScrapedColumn(sectionName, columnId)

        return isScrapped != null
    }

    private fun scrapColumn(sectionName: String, columnId: String) {
        val columnDB = ColumnDatabase.getInstance(requireContext())!!
        columnDB.columnDao().scrapColumn(Scrap(sectionName, columnId))
    }

    private fun cancelScrap(sectionName: String, columnId: String) {
        val columnDB = ColumnDatabase.getInstance(requireContext())!!
        columnDB.columnDao().cancelScrap(sectionName, columnId)
    }

    private fun connectDatabase() {
        // 데이터베이스 연결
        database.child("column").child("section1").child("items").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children) {
                    columnList.add(ColumnData(
                        i.ref.parent?.parent?.key, i.key, R.drawable.example, i.child("title").value.toString(),
                        i.child("author").value.toString(), i.child("content").value.toString()))
                }
                //리사이클러뷰 어댑터 적용
                val columnsRVAdapter = ColumnsRVAdapter(columnList)
                binding.category1ColumnRv.adapter = columnsRVAdapter

                // 리사이클러뷰 클릭 이벤트
                columnsRVAdapter.setMyItemClickListener(object : ColumnsRVAdapter.MyItemClickListener {
                    override fun onItemClick(column: ColumnData) {
                        activity.let {
                            val intent = Intent(context, ColumnDetailActivity::class.java)
                            intent.putExtra("column", column)
                            startActivity(intent)
                        }
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "Error : " + error.toString())
            }
        })
    }

}
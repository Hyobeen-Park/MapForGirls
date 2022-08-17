package com.example.mapforgirls.ui.main.columns

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mapforgirls.MainActivity
import com.example.mapforgirls.data.entities.ColumnData
import com.example.mapforgirls.databinding.FragmentCategory1Binding
import com.google.firebase.database.*
import java.util.*
import com.google.firebase.database.DatabaseReference


class Category1Fragment : Fragment() {
    lateinit var binding: FragmentCategory1Binding
    var columnList = ArrayList<ColumnData>()
    private var database : DatabaseReference = FirebaseDatabase.getInstance().reference

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

        connectDatabase()
        setOnClickListeners()

        return binding.root
    }


    private fun setOnClickListeners() {
        binding.category1WhatIsThis2Cv.setOnClickListener {
            val intent = Intent(context, ColumnDetailActivity::class.java)
            intent.putExtra("column", columnList[0])
            startActivity(intent)
        }
    }

    private fun connectDatabase() {
        // 데이터베이스 연결
        database.child("column").child("section1").child("items").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children) {
                    var img = activity?.resources!!.getIdentifier(i.child("image").value.toString(), "drawable", activity?.packageName)
                    columnList.add(
                        ColumnData(
                        i.ref.parent?.parent?.key, i.key, img, i.child("title").value.toString(), i.child("subTitle").value.toString(),
                        i.child("author").value.toString(), i.child("content").value.toString())
                    )
                }
                //리사이클러뷰 어댑터 적용
                val columnsRVAdapter = ColumnsRVAdapter(columnList)
                binding.category1ColumnRv.adapter = columnsRVAdapter

                // 리사이클러뷰 클릭 이벤트
                columnsRVAdapter.setMyItemClickListener(object :
                    ColumnsRVAdapter.MyItemClickListener {
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
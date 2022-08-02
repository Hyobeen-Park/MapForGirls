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


class Category1Fragment : Fragment() {
    lateinit var binding: FragmentCategory1Binding
    private var infoList = ArrayList<Information>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategory1Binding.inflate(inflater, container, false)

        //카테고리 이름 받아오기
        val sharedPreferences = (context as MainActivity).getSharedPreferences("category", Context.MODE_PRIVATE)
        val category = sharedPreferences.getString("category", null)

        Log.d("receive", category.toString())
        binding.category1TitleTv.text = category.toString()

//        binding.category1ExampleIv.setOnClickListener {
//            activity.let {
//                val intent = Intent(context, InfoDetailActivity::class.java)
//                startActivity(intent)
//            }
//        }

        infoList.apply {
            add(Information(R.drawable.example, "220730 오늘의 일기"))
            add(Information(R.drawable.example, "220731 오늘의 일기"))
            add(Information(R.drawable.example, "220801 오늘의 일기"))
        }

        val infoRVAdapter = InfoRVAdapter(infoList)
        binding.category1ColumnRv.adapter = infoRVAdapter

        infoRVAdapter.setMyItemClickListener(object : InfoRVAdapter.MyItemClickListener {
            override fun onItemClick(info: Information) {
                activity.let {
                    val intent = Intent(context, ColumnDetailActivity::class.java)
                    intent.putExtra("information", info)
                    startActivity(intent)
                }
            }
        })

        return binding.root
    }
}
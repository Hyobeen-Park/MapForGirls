package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
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
        binding.category1CalumnRv.adapter = infoRVAdapter

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
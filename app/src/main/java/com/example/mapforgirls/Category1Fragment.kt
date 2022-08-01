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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategory1Binding.inflate(inflater, container, false)

        binding.category1ExampleIv.setOnClickListener {
            activity.let {
                val intent = Intent(context, InfoDetailActivity::class.java)
                startActivity(intent)
            }
        }

        return binding.root
    }
}
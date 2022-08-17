package com.example.mapforgirls.ui.test

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mapforgirls.MainActivity
import com.example.mapforgirls.databinding.FragmentTestTamponBinding

class TestResult4Fragment: Fragment() {
    lateinit var binding : FragmentTestTamponBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestTamponBinding.inflate(inflater, container, false)

        binding.testEndTampon.setOnClickListener {
            var intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}
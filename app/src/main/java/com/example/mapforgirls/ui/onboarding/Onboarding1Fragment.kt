package com.example.mapforgirls.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mapforgirls.LoginActivity
import com.example.mapforgirls.databinding.FragmentOnboarding1Binding

class Onboarding1Fragment : Fragment() {
    lateinit var binding: FragmentOnboarding1Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboarding1Binding.inflate(inflater, container, false)

        binding.onboarding1Start.setOnClickListener {
            var intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}
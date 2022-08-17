package com.example.mapforgirls.ui.test

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mapforgirls.R
import com.example.mapforgirls.databinding.FragmentTest1Binding

class Test1Fragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentTest1Binding
    lateinit var testActivity : TestActivity
    var point1 = 0
    var point2 = 0
    var point3 = 0
    var point4 = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTest1Binding.inflate(inflater, container, false)
        testActivity = activity as TestActivity

        binding.test1NextBtn.isEnabled = false

        binding.test1NextBtn.setOnClickListener(this)
        binding.test1CheckOk.setOnClickListener(this)
        binding.test1CheckNo.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.test1_check_ok -> {
                binding.test1NextBtn.isEnabled = true
                binding.test1NextBtn.setBackgroundResource(R.drawable.round_red)
                binding.test1NextBtn.setTextColor(Color.WHITE)
                binding.test1CheckOk.setBackgroundResource(R.drawable.test_ok_select)
                binding.test1CheckNo.setBackgroundResource(R.drawable.test_no)
                point2 = 1
                point4 = 1
            }
            R.id.test1_check_no -> {
                binding.test1NextBtn.isEnabled = true
                binding.test1NextBtn.setBackgroundResource(R.drawable.round_red)
                binding.test1CheckOk.setBackgroundResource(R.drawable.test_ok)
                binding.test1CheckNo.setBackgroundResource(R.drawable.test_no_select)
                point1 = 1
                point3 = 1
            }
            R.id.test1_next_btn -> {
                testActivity.addPoint(point1, point2, point3, point4)
                testActivity.changeFragment(2)
            }
        }
    }

}
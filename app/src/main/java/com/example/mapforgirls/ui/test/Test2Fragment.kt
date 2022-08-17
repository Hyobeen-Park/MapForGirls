package com.example.mapforgirls.ui.test

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mapforgirls.R
import com.example.mapforgirls.databinding.FragmentTest2Binding

class Test2Fragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentTest2Binding
    lateinit var testActivity : TestActivity

    var point1 = 0
    var point2 = 0
    var point3 = 0
    var point4 = 0
    var a1 = false
    var a2 = false
    var a3 = false
    var a4 = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTest2Binding.inflate(inflater, container, false)
        testActivity = activity as TestActivity

        binding.test2NextBtn.isEnabled = false

        binding.test2NextBtn.setOnClickListener(this)
        binding.test2Answer1Btn.setOnClickListener(this)
        binding.test2Answer2Btn.setOnClickListener(this)
        binding.test2Answer3Btn.setOnClickListener(this)
        binding.test2Answer4Btn.setOnClickListener(this)


        return binding.root
    }

    override fun onClick(v: View?) {
        binding.test2NextBtn.isEnabled = true
        when(v?.id) {
            R.id.test2_answer1_btn -> {
                if(a1) {
//                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.testcheck_before1)
                    binding.test2Answer1Btn.setBackgroundColor(Color.TRANSPARENT)
                    point1 -= 1
                    point3 -= 1
                    point4 -= 1
                } else {
                    binding.test2Answer1Btn.setBackgroundResource(R.drawable.round_green)
                    point1 += 1
                    point3 += 1
                    point4 += 1
                }
                a1 = !a1
                setBtn()
            }
            R.id.test2_answer2_btn -> {
                if(a2) {
//                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.testcheck_before1)
                    binding.test2Answer2Btn.setBackgroundColor(Color.TRANSPARENT)
                    point1 -= 1
                    point2 -= 1
                    point3 -= 1
                } else {
                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.round_green)
                    point1 += 1
                    point2 += 1
                    point3 += 1
                }
                a2 = !a2
                setBtn()
            }
            R.id.test2_answer3_btn -> {
                if(a3) {
//                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.testcheck_before1)
                    binding.test2Answer3Btn.setBackgroundColor(Color.TRANSPARENT)
                    point2 -= 1
                    point4 -= 1
                } else {
                    binding.test2Answer3Btn.setBackgroundResource(R.drawable.round_green)
                    point2 += 1
                    point4 += 1
                }
                a3 = !a3
                setBtn()
            }
            R.id.test2_answer4_btn -> {
                if(a4) {
//                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.testcheck_before1)
                    binding.test2Answer4Btn.setBackgroundColor(Color.TRANSPARENT)
                    point1 -= 1
                    point2 -= 1
                    point3 -= 1
                    point4 -= 1
                } else {
                    binding.test2Answer4Btn.setBackgroundResource(R.drawable.round_green)
                    point1 += 1
                    point2 += 1
                    point3 += 1
                    point4 += 1
                }
                a4 = !a4
                setBtn()
            }
            R.id.test2_next_btn -> {
                testActivity.addPoint(point1, point2, point3, point4)
                testActivity.changeFragment(3)
            }
        }
    }

    fun setBtn() {
        binding.test2NextBtn.isEnabled = !(!a1 && !a2 && !a3 && !a4)
        if (binding.test2NextBtn.isEnabled) {
            binding.test2NextBtn.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext, R.color.red1))
        } else {
            binding.test2NextBtn.setBackgroundColor(Color.TRANSPARENT)
        }
    }
}
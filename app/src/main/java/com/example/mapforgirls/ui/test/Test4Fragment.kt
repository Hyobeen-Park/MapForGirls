package com.example.mapforgirls.ui.test

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mapforgirls.R
import com.example.mapforgirls.databinding.FragmentTest2Binding
import com.example.mapforgirls.databinding.FragmentTest4Binding

class Test4Fragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentTest4Binding
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
        binding = FragmentTest4Binding.inflate(inflater, container, false)
        testActivity = activity as TestActivity

        binding.test4NextBtn.isEnabled = false

        binding.test4NextBtn.setOnClickListener(this)
        binding.test4Answer1Btn.setOnClickListener(this)
        binding.test4Answer2Btn.setOnClickListener(this)
        binding.test4Answer3Btn.setOnClickListener(this)
        binding.test4Answer4Btn.setOnClickListener(this)


        return binding.root
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.test4_answer1_btn -> {
                if(a1) {
//                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.testcheck_before1)
                    binding.test4Answer1Btn.setBackgroundResource(R.drawable.round_grey_stroke)
                    point1--
                } else {
                    binding.test4Answer1Btn.setBackgroundResource(R.drawable.round_green)
                    point1++
                }
                a1 = !a1
                setBtn()
            }
            R.id.test4_answer2_btn -> {
                if(a2) {
//                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.testcheck_before1)
                    binding.test4Answer2Btn.setBackgroundResource(R.drawable.round_grey_stroke)
                    point2--
                    point4--
                } else {
                    binding.test4Answer2Btn.setBackgroundResource(R.drawable.round_green)
                    point2++
                    point3 = point3 - 100
                    point4++
                }
                a2 = !a2
                setBtn()
            }
            R.id.test4_answer3_btn -> {
                if(a3) {
//                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.testcheck_before1)
                    binding.test4Answer3Btn.setBackgroundResource(R.drawable.round_grey_stroke)
                    point4--
                } else {
                    binding.test4Answer3Btn.setBackgroundResource(R.drawable.round_green)
                    point4++
                }
                a3 = !a3
                setBtn()
            }
            R.id.test4_answer4_btn -> {
                if(a4) {
//                    binding.test2Answer2Btn.setBackgroundResource(R.drawable.testcheck_before1)
                    binding.test4Answer4Btn.setBackgroundResource(R.drawable.round_grey_stroke)
                } else {
                    binding.test4Answer4Btn.setBackgroundResource(R.drawable.round_green)
                }
                a4 = !a4
                setBtn()
            }
            R.id.test4_next_btn -> {
                testActivity.addPoint(point1, point2, point3, point4)
                testActivity.changeFragment(0)
            }
        }
    }

    fun setBtn() {
        binding.test4NextBtn.isEnabled = !(!a1 && !a2 && !a3 && !a4)
        if (binding.test4NextBtn.isEnabled) {
            binding.test4NextBtn.setBackgroundResource(R.drawable.round_red)
            binding.test4NextBtn.setTextColor(Color.WHITE)
        } else {
            binding.test4NextBtn.setBackgroundResource(R.drawable.round_grey)
            binding.test4NextBtn.setTextColor(Color.parseColor("#A9A9A9"))
        }
    }
}
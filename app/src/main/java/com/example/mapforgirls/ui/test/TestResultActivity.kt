package com.example.mapforgirls.ui.test

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mapforgirls.R
import com.example.mapforgirls.databinding.ActivityTestResultBinding

class TestResultActivity : AppCompatActivity() {
    lateinit var binding : ActivityTestResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var point1 = intent.getIntExtra("point1", 0)
        var point2 = intent.getIntExtra("point2", 0)
        var point3 = intent.getIntExtra("point3", 0)
        var point4 = intent.getIntExtra("point4", 0)


        var arr = arrayOf(point1, point2, point3, point4)
        var result = arr.max()
        Log.d("dd", arr.toString())
        Log.d("dd", result.toString())

        if(point1 == result) {
            setFragment("point1")
        }else if (point2 == result){
            setFragment("point2")
        }else if (point3 == result){
            setFragment("point3")
        }else{
            setFragment("point4")
        }



    }

    fun setFragment(result: String) {
        when(result) {
            "point1" -> {supportFragmentManager.beginTransaction()
                .replace(R.id.test_result_frm, TestResult1Fragment())
                .commitAllowingStateLoss()
            }
            "point1" -> {supportFragmentManager.beginTransaction()
                .replace(R.id.test_result_frm, TestResult2Fragment())
                .commitAllowingStateLoss()
            }
            "point3" -> {supportFragmentManager.beginTransaction()
                .replace(R.id.test_result_frm, TestResult3Fragment())
                .commitAllowingStateLoss()
            }
            "point4" -> {supportFragmentManager.beginTransaction()
                .replace(R.id.test_result_frm, TestResult4Fragment())
                .commitAllowingStateLoss()
            }
        }
    }
}
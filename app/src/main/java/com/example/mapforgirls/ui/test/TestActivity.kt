package com.example.mapforgirls.ui.test

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mapforgirls.MapsFragment
import com.example.mapforgirls.R
import com.example.mapforgirls.databinding.ActivityColumnDetailBinding
import com.example.mapforgirls.databinding.ActivityTestBinding
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.fragment_test1.*


class TestActivity : AppCompatActivity() {
    lateinit var binding : ActivityTestBinding
    var point1 = 0
    var point2 = 0
    var point3 = 0
    var point4 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.test_frm, Test1Fragment())
            .commitAllowingStateLoss()
    }


    fun changeFragment(index: Int) {
        when(index) {
            2 -> supportFragmentManager!!.beginTransaction()
                .replace(R.id.test_frm, Test2Fragment())
                .commitAllowingStateLoss()
            3 -> supportFragmentManager!!.beginTransaction()
                .replace(R.id.test_frm, Test3Fragment())
                .commitAllowingStateLoss()
            4 -> supportFragmentManager!!.beginTransaction()
                .replace(R.id.test_frm, Test4Fragment())
                .commitAllowingStateLoss()
            0 -> {
                var intent = Intent(this, TestResultActivity::class.java)
                intent.putExtra("point1", point1)
                intent.putExtra("point2", point2)
                intent.putExtra("point3", point3)
                intent.putExtra("point4", point4)
                startActivity(intent)
            }
        }
    }

    fun addPoint(point1 : Int, point2 : Int, point3 : Int, point4 : Int) {
        this.point1 += point1
        this.point2 += point2
        this.point3 += point3
        this.point4 += point4
    }
}
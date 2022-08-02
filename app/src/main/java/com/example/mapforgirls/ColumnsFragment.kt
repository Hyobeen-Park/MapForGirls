package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mapforgirls.databinding.FragmentColumnsBinding

class ColumnsFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentColumnsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentColumnsBinding.inflate(inflater, container, false)

//        binding.columnTestTv.setOnClickListener(this)
//        binding.infoArea1Btn.setOnClickListener(this)
//        binding.infoArea2Btn.setOnClickListener(this)
//        binding.infoArea3Btn.setOnClickListener(this)
//        binding.infoArea4Btn.setOnClickListener(this)

        return binding.root
    }


    override fun onClick(v: View?) {
//        when(v?.id) {
//            R.id.info_test_tv -> {
//                activity?.let{
//                    val intent = Intent(context, TestActivity::class.java)
//                    startActivity(intent)
//                }
//            }
//            R.id.info_area1_btn -> {
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, Category1Fragment())
//                    .commitAllowingStateLoss()
//            }
//            R.id.info_area2_btn -> {
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, Category1Fragment())
//                    .commitAllowingStateLoss()
//            }
//            R.id.info_area3_btn -> {
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, Category1Fragment())
//                    .commitAllowingStateLoss()
//            }
//            R.id.info_area4_btn -> {
//                (context as MainActivity).supportFragmentManager.beginTransaction()
//                    .replace(R.id.main_frm, Category1Fragment())
//                    .commitAllowingStateLoss()
//            }
//        }
    }

}
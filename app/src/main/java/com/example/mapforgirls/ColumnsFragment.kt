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

        binding.columnsTestClayout.setOnClickListener(this)
        binding.columnsCategory1Clayout.setOnClickListener(this)
        binding.columnsCategory2Clayout.setOnClickListener(this)
        binding.columnsCategory3Clayout.setOnClickListener(this)
        binding.columnsCategory4Clayout.setOnClickListener(this)
        binding.columnsCategory5Clayout.setOnClickListener(this)
        binding.columnsCategory6Clayout.setOnClickListener(this)

        return binding.root
    }


    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.columns_test_clayout -> {
                activity?.let{
                    val intent = Intent(context, TestActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.columns_category1_clayout -> {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, Category1Fragment())
                    .commitAllowingStateLoss()
            }
            R.id.columns_category2_clayout -> {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, Category1Fragment())
                    .commitAllowingStateLoss()
            }
            R.id.columns_category3_clayout -> {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, Category1Fragment())
                    .commitAllowingStateLoss()
            }
            R.id.columns_category4_clayout -> {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, Category1Fragment())
                    .commitAllowingStateLoss()
            }
            R.id.columns_category5_clayout -> {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, Category1Fragment())
                    .commitAllowingStateLoss()
            }
            R.id.columns_category6_clayout -> {
                (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, Category1Fragment())
                    .commitAllowingStateLoss()
            }
        }
    }

}
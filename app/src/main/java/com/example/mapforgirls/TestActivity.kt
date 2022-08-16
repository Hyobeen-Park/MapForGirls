package com.example.mapforgirls

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.mapforgirls.databinding.ActivityTestBinding


class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //이미지뷰 변수.bringToFront(); -> 이거로 하단 이미지 상단view로 보이게 부탁드릴게요! ㅎㅎ
    }
}
package com.example.mapforgirls

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mapforgirls.databinding.FragmentCategory1Binding
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference


class Category1Fragment : Fragment() {
    lateinit var binding: FragmentCategory1Binding
    var columnList = ArrayList<ColumnData>()
    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategory1Binding.inflate(inflater, container, false)

        //카테고리 이름 받아오기
        val sharedPreferences = (context as MainActivity).getSharedPreferences("category", Context.MODE_PRIVATE)
        val category = sharedPreferences.getString("category", null)
        binding.category1TitleTv.text = category.toString()

        // 데이터베이스 연결
        database = FirebaseDatabase.getInstance().reference
        database.child("column").child("section1").child("items").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children) {
                    columnList.add(ColumnData(R.drawable.example, i.child("title").value.toString(),
                        i.child("author").value.toString(), i.child("content").value.toString()))
                }
                //리사이클러뷰 어댑터 적용
                val infoRVAdapter = ColumnsRVAdapter(columnList)
                binding.category1ColumnRv.adapter = infoRVAdapter

                // 리사이클러뷰 클릭 이벤트
                infoRVAdapter.setMyItemClickListener(object : ColumnsRVAdapter.MyItemClickListener {
                    override fun onItemClick(column: ColumnData) {
                        activity.let {
                            val intent = Intent(context, ColumnDetailActivity::class.java)
                            intent.putExtra("column", column)
                            startActivity(intent)
                        }
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "Error : " + error.toString())
            }
        })

        return binding.root
    }

//    // 데이터 읽어온 후 ArrayList에 저장
//    private fun setColumnDatabase() {
//        var list = ArrayList<Information>()
//        database.child("column").child("section1").child("items").addListenerForSingleValueEvent(object :
//            ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for(i in snapshot.children) {
//                    list.add(Information(R.drawable.example, i.child("title").value.toString(),
//                        i.child("author").value.toString(), i.child("content").value.toString()))
//                }
//                val categoryFragment = Category1Fragment()
//                Log.d("database", list.size.toString())
//                categoryFragment.setColumnList(list)
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("database", "Error : " + error.toString())
//            }
//        })
//    }


}
package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mapforgirls.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var authListener: AuthStateListener? = null
    private var database : DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authListener = AuthStateListener {
            val user = auth.currentUser
            val userInfo = getSharedPreferences("userInfo", MODE_PRIVATE)
            val editor = userInfo.edit()
            editor.putString("uid", user?.uid.toString())
            editor.apply()
        }

        setScrapDatabase()
        initBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()

        val userId = getSharedPreferences("userInfo", MODE_PRIVATE).getString("uid", "").toString()
        val scrappedColumn: List<Scrap>
        val columnDB = ColumnDatabase.getInstance(this@MainActivity)!!
        scrappedColumn = columnDB.columnDao().getScrappedColumn()

        Log.d("data", scrappedColumn.size.toString())
        //데베 삭제
        database.child("users").child(userId).child("scrap").removeValue()
        for(i in 0 until scrappedColumn.size) { //데베에 스크랩한 칼럼 정보 저장
            database.child("users").child(userId).child("scrap").child("item" + i).setValue(scrappedColumn[i])
        }
        columnDB.columnDao().initScrapTable()       //roomDB 데이터 삭제
    }

    private fun setScrapDatabase() {
        val userId = getSharedPreferences("userInfo", MODE_PRIVATE).getString("uid", "").toString()

        database.child("users").child(userId).child("scrap").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val columnDB = ColumnDatabase.getInstance(this@MainActivity)!!
                for(i in snapshot.children) {
                    columnDB.columnDao().scrapColumn(Scrap(i.child("sectionName").getValue().toString(), i.child("columnId").getValue().toString()))
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "Error : " + error.toString())
            }
        })
    }

    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, MapsFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.mapFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MapsFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.chattingFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ChattingFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.infoFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ColumnsFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
    // 뒤로가기 2번
    private var backPressedTime : Long = 0
    override fun onBackPressed() {
        // 2초내 다시 클릭하면 앱 종료
        if (System.currentTimeMillis() - backPressedTime < 2000) {
            finish()
            return
        }
        // 한 번 클릭했을 시 메시지
        Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        backPressedTime = System.currentTimeMillis()
    }
}
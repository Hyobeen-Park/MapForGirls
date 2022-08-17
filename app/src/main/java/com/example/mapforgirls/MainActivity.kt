package com.example.mapforgirls

import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.mapforgirls.data.entities.Scrap
import com.example.mapforgirls.data.local.ColumnDatabase
import com.example.mapforgirls.databinding.ActivityMainBinding
import com.example.mapforgirls.ui.main.myPage.MypageFragment
import com.example.mapforgirls.ui.main.chatting.ChattingFragment
import com.example.mapforgirls.ui.main.columns.ColumnsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var userRef : DatabaseReference? = null
    private var authListener: AuthStateListener? = null
    private var database : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var currentUser = auth.currentUser  // userInfo Shared Peferences가 없어지면 사용
    var userList = ArrayList<UserInfo>()
    var userEditor: SharedPreferences.Editor? = null

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener!!)  // 없어도 될 듯
        val userDataShared = getSharedPreferences("userData", MODE_PRIVATE)
        userEditor = userDataShared.edit()
        connectDatabase()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authListener = AuthStateListener {  // 없어도 될 듯
            val userInfoShared = getSharedPreferences("userInfo", MODE_PRIVATE)
            val editor = userInfoShared.edit()
            editor.putString("uid", currentUser?.uid.toString())
            editor.apply()
        }

        setScrapDatabase()
        initBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()

        initScrapDB()

    }

    private fun initScrapDB() {
        //데베 삭제
        val scrappedColumn: List<Scrap>
        val columnDB = ColumnDatabase.getInstance(this@MainActivity)!!
        scrappedColumn = columnDB.columnDao().getScrappedColumn()
        columnDB.columnDao().initScrapTable()       //roomDB 데이터 삭제

        Log.d("scrappedData", scrappedColumn.size.toString())

        val userId = getSharedPreferences("userInfo", MODE_PRIVATE).getString("uid", "").toString()
        database.child("users").child(userId).child("scrap").removeValue()

        database.child("users").child(userId).child("scrap").get().addOnSuccessListener {
            if (it.childrenCount.toInt() == 0) {
                for(i in 0 until scrappedColumn.size) { //데베에 스크랩한 칼럼 정보 저장
                    database.child("users").child(userId).child("scrap").child("item" + i).setValue(scrappedColumn[i])
                }
            } else {
                initScrapDB()
            }
        }

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
                Log.d("database", "Error : $error")
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
    private fun connectDatabase() {
        // 데이터베이스 연결
        userRef = database.child("users")
        userRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (user in snapshot.children) {
                    if (user.key.equals(currentUser?.uid)) {
                        val name = user.child("name").value.toString()
                        val email = user.child("email").value.toString()
                        val userType = user.child("userType").value.toString()
                        val profileUri =
                            if(user.child("profile").value != null)
                                user.child("profile").value.toString()
                            else
                                Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + R.drawable.img_girls).toString()

                        userEditor?.putString("name", name)
                        userEditor?.putString("email", email)
                        userEditor?.putString("userType", userType)
                        userEditor?.putString("profileUri", profileUri)
                        userEditor?.apply()

                        return
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "Error : $error")
            }
        })


    }

}
package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val userTypeShared = getSharedPreferences("userType", MODE_PRIVATE)
        val editor = userTypeShared.edit()

        user_pharmacist_iv.setOnClickListener {
            val intent = Intent(this, CertifyActivity::class.java)
            editor.putString("userType","1")
            editor.apply()
            startActivity(intent)
        }

        user_girl_iv.setOnClickListener {
            val intent = Intent(this, CertifyActivity::class.java)
            editor.putString("userType","2")
            editor.apply()
            startActivity(intent)
        }
    }
}
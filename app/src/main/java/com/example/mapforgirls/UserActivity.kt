package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        auth = FirebaseAuth.getInstance()

        user_girl_iv.setOnClickListener {
            val intent = Intent(this, CertifyActivity::class.java)
            startActivity(intent)
        }
    }
}
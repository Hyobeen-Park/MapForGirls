package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signup_email_et = findViewById<EditText>(R.id.signup_email_et)
        val signup_email_warn = findViewById<TextView>(R.id.signup_email_warn)
        val signup_pw_et = findViewById<TextView>(R.id.signup_pw_et)
        val signup_pw_warn = findViewById<TextView>(R.id.signup_pw_warn)
        val signup_pw2_et = findViewById<TextView>(R.id.signup_pw2_et)
        val signup_pw2_warn = findViewById<TextView>(R.id.signup_pw2_warn)
        val signup_certif_btn = findViewById<Button>(R.id.signup_certif_btn)
        val signup_certif_warn = findViewById<TextView>(R.id.signup_certif_warn)
        val signup_name_et = findViewById<EditText>(R.id.signup_name_et)
        val signup_name_warn = findViewById<TextView>(R.id.signup_name_warn)
        val signup_btn = findViewById<Button>(R.id.signup_btn)

        signup_btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

}
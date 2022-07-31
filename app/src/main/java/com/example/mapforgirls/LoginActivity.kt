package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val btn = findViewById<Button>(R.id.login_btn)
        btn.setOnClickListener{
            //Toast.makeText(applicationContext, "버튼 클릭", Toast.LENGTH_SHORT).show()}
            val intent = Intent(this, MainActivity::class.java)
            //intent.apply {
            //    this.putExtra("msg", text_main.text.toString())
            //}
            startActivity(intent)
        }


    }
}
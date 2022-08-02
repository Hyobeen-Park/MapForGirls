package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val login_btn = findViewById<Button>(R.id.login_btn)
        val login_email_et = findViewById<EditText>(R.id.login_email_et)
        val login_pw_et = findViewById<EditText>(R.id.login_pw_et)
        val login_signup_tvb = findViewById<TextView>(R.id.login_signup_tvb)
        val login_pw_tvb = findViewById<TextView>(R.id.login_pw_tvb)

        login_btn.setOnClickListener{
            signinAndSignup()
        }

        login_signup_tvb.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
    fun signinAndSignup(){
        auth?.createUserWithEmailAndPassword(login_email_et.text.toString(),login_pw_et.text.toString())
            ?.addOnCompleteListener{
                task ->
                    if(task.isSuccessful){
                        // 계정(아이디) 생성 성공
                        moveMainPage(task.result.user)
                    }else if(!task.exception?.message.isNullOrEmpty()){
                        // 로그인 에러가 났을 경우
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }else{
                        // 계정이 있을 경우 로그인 화면으로 나가기
                        signinEmail()
                    }
            }
        }
    fun signinEmail(){
        auth?.signInWithEmailAndPassword(login_email_et.text.toString(),login_pw_et.text.toString())
            ?.addOnCompleteListener{
                    task ->
                if(task.isSuccessful){
                    // 아이디와 패스워드가 맞았을 경우, 로그인
                    moveMainPage(task.result.user)
                }else{
                    // 아이디와 패스워드가 틀렸을 경우
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
    fun moveMainPage(user: FirebaseUser?){
        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
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
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val login_btn = findViewById<Button>(R.id.login_btn)
        val login_email_et = findViewById<EditText>(R.id.login_email_et)
        val login_email_warn = findViewById<TextView>(R.id.login_email_warn)
        val login_pw_warn = findViewById<TextView>(R.id.login_pw_warn)
        val login_pw_et = findViewById<EditText>(R.id.login_pw_et)
        val login_signup_tvb = findViewById<TextView>(R.id.login_signup_tvb)
        val login_pw_tvb = findViewById<TextView>(R.id.login_pw_tvb)

        login_btn.setOnClickListener{
            signinEmail()
        }

        login_signup_tvb.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    fun signinEmail(){  // 로그인하는 함수
        // 아이디와 비밀번호가 채워지지 않은 경우
        if(login_email_et.text.toString() == null || login_pw_et.text.toString() == null || login_email_et.text.toString().isEmpty() || login_pw_et.text.toString().isEmpty()) {
            Toast.makeText(this, "아이디와 비밀번호를 채워주세요.", Toast.LENGTH_LONG).show()
        } else if(!isEmail(login_email_et.text.toString())){  // 아이디가 이메일 형식이 아닐 경우
            Toast.makeText(this,"이메일 형식으로 입력해주세요.", Toast.LENGTH_LONG).show()
        } else {
            auth?.signInWithEmailAndPassword(
                login_email_et.text.toString(),
                login_pw_et.text.toString()
            )?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 아이디와 패스워드가 맞았을 경우, 로그인
                    moveMainPage(task.result.user)
                } else {  // 아이디와 패스워드가 틀렸을 경우
                    if (task.exception?.message.equals("The password is invalid or the user does not have a password.")) {  // 비밀번호가 틀렸을 경우
                        Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show()
                    } else if (task.exception?.message.equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {  // 존재하지 않는 계정일 경우
                        Toast.makeText(this, "계정이 존재하지 않습니다.", Toast.LENGTH_LONG).show()
                    } else {  // 로그인 오류
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    fun moveMainPage(user: FirebaseUser?){  // 메인 페이지로 이동하는 함수
        if(user != null){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    fun isEmail(email: String?): Boolean {  // 아이디가 이메일 형식이 맞는지 확인하는 함수
        var returnValue = false
        val regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$"
        val p: Pattern = Pattern.compile(regex)
        val m: Matcher = p.matcher(email)
        if (m.matches()) {
            returnValue = true
        }
        return returnValue
    }
}
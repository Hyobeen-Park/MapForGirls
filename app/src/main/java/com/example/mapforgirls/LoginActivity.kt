package com.example.mapforgirls

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    var activate_login : Boolean? = false
    var activate_pw : Boolean? = false

    override fun onStart() {
        super.onStart()
        auth?.currentUser?.reload()  // 현재 사용자가 로그인 되어있는지 확인
        login_btn.isEnabled = false  // 로그인 버튼 비활성화
    }

    override fun onCreate(savedInstanceState:Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        login_email_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //텍스트를 입력 후
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 전
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 중
                activate_login = isEmail(login_email_et.text.toString())  // 아이디가 이메일 형식일 경우에 로그인 버튼 활성화 준비
                isActive(activate_login, activate_pw)
            }
        })

        login_pw_et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //텍스트를 입력 후
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 전
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //텍스트 입력 중
                activate_pw = login_pw_et.length() > 0  // 비밀번호를 한 자리 이상 입력할 경우에 로그인 버튼 활성화 준비
                isActive(activate_login, activate_pw)
            }
        })

        login_btn.setOnClickListener{
            signinEmail()
        }

        login_signup_tvb.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signinEmail(){  // 로그인하는 함수
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
    private fun moveMainPage(user: FirebaseUser?){  // 메인 페이지로 이동하는 함수
        if(user != null){
            val intent = Intent(this, MainActivity::class.java)
            finishAffinity()  // 액티비티 스택을 비움
            startActivity(intent)
        }
    }
    private fun isEmail(email: String?): Boolean {  // 아이디가 이메일 형식이 맞는지 확인하는 함수
        var returnValue = false
        val p: Pattern = Patterns.EMAIL_ADDRESS
        val m: Matcher = p.matcher(email)
        if (m.find()) {
            returnValue = true
        }
        return returnValue
    }
    private fun isActive(activate_login: Boolean?, activate_pw : Boolean?){  // 로그인 버튼 활성화 함수
            login_btn.isEnabled = activate_login == true && activate_pw == true
    }
}
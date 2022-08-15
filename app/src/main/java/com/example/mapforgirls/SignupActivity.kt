package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.coroutines.tasks.await

class SignupActivity : AppCompatActivity() {
    var auth : FirebaseAuth? = null
    lateinit var loginActivity : LoginActivity
    // lateinit var database : DatabaseReference
    private val database by lazy { FirebaseDatabase.getInstance() }
    private var userRef : DatabaseReference? = null
    var temp : Int = 0
    var tempString : String? = null

    override fun onStart() {
        super.onStart()
        // readUserCount()
        // Check if user is signed in (non-null) and update UI accordingly.
        // val currentUser: FirebaseUser? = auth?.getCurrentUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        loginActivity = LoginActivity()
        auth = FirebaseAuth.getInstance()

        signup_btn.setOnClickListener {
            signup()
        }
    }

    private fun signup(){  // 회원가입하는 함수
        if(signup_email_et.text.toString().isEmpty() ||
            signup_pw_et.text.toString().isEmpty() ||
            signup_pw2_et.text.toString().isEmpty()
        ) {
            Toast.makeText(this, "아이디와 비밀번호를 채워주세요.", Toast.LENGTH_LONG).show()
        }else if(signup_name_et.text.toString().isEmpty())
        {
            Toast.makeText(this, "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show()
        }else if (signup_pw_et.text.toString() != signup_pw2_et.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다. ", Toast.LENGTH_LONG).show()
        }else {
            auth?.createUserWithEmailAndPassword(
                signup_email_et.text.toString(),
                signup_pw_et.text.toString()
            )
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 계정(아이디) 생성 성공
                        writeNewUser(
                            task.result.user?.uid.toString(),
                            signup_name_et.text.toString(),
                            task.result.user?.email.toString()
                        )
                        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show()
                        moveLoginPage()
                    } else {
                        if(!loginActivity.isEmail(signup_email_et.text.toString())){
                            Toast.makeText(this,"이메일 형식으로 입력해주세요.", Toast.LENGTH_LONG).show()
                        }else if (task.exception?.message.equals("The email address is already in use by another account.")) {  // 이미 이메일을 사용한 계정이 존재하는 경우
                            Toast.makeText(this, "이미 존재하는 이메일입니다.", Toast.LENGTH_LONG).show()
                        }else if(signup_pw_et.length() < 6){
                            Toast.makeText(this, "비밀번호는 6자리 이상이어야 합니다.", Toast.LENGTH_LONG).show()
                        }else {
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }
    private fun moveLoginPage(){  // 로그인 페이지로 이동하는 함수
        val intent = Intent(this, LoginActivity::class.java)
        finishAffinity()  // 액티비티 스택을 비움
        startActivity(intent)
    }
    /*
    private fun readUserCount() : Int {  // 현재 DB의 회원 수를 읽는 함수
        userRef = database.getReference("users")
        userRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@SignupActivity, "취소", Toast.LENGTH_LONG).show()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for (first_key in snapshot.children) {
                    if (first_key.key.equals("count")) {
                        temp = Integer.parseInt(first_key.value.toString())
                        temp++
                        tempString = first_key.value.toString()
                        Toast.makeText(this@SignupActivity, temp.toString(), Toast.LENGTH_LONG).show()
                    }
                    else{
                        // Toast.makeText(this@SignupActivity, "Error!!", Toast.LENGTH_LONG).show()
                    }

                }
            }
        })
        return temp
    }
     */
    private fun writeNewUser(uid: String, name: String, email: String) {  // DB에 회원을 작성하는 함수
        val user = UserInfo(name, email)
        // database.getReference().child("users").child(temp.toString()).setValue(user)
        database.reference.child("users").child(uid).setValue(user)
        //database.getReference().child("users").updateChildren("count", temp.toString());
    }

}
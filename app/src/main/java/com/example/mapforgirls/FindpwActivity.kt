package com.example.mapforgirls

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.HandlerCompat.postDelayed
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_findpw.*

class FindpwActivity : AppCompatActivity(){
    var auth : FirebaseAuth? = null
    private var userRef : DatabaseReference? = null
    private val database by lazy { FirebaseDatabase.getInstance() }
    var isExistEmail = false
    var emailAddress : String? = null
    lateinit var loginActivity : LoginActivity
    lateinit var signupActivity: SignupActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findpw)
        auth = FirebaseAuth.getInstance()
        loginActivity = LoginActivity()
        signupActivity = SignupActivity()

        findpw_btn.setOnClickListener {
            emailAddress = findpw_email_et.text.toString()
            resetPw()
            // MainScope().launch {}
        }
    }
    private fun resetPw() {
        /*
        suspendCoroutine<Boolean> {
            Handler(Looper.getMainLooper()).postDelayed({
                userRef = database.getReference("users")
                userRef!!.orderByChild("email").equalTo(emailAddress)
                    .addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {}
                        override fun onCancelled(error: DatabaseError) {}
                    })
            }, 500)
        }
         */

        auth?.sendPasswordResetEmail(emailAddress.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"이메일이 전송되었습니다.", Toast.LENGTH_LONG).show()
                    moveToLoginPage()
                }else if(!loginActivity.isEmail(emailAddress.toString())){
                    Toast.makeText(this,"이메일 형식으로 입력해주세요.", Toast.LENGTH_LONG).show()
                }else if(task.exception?.message.equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                    Toast.makeText(this, "존재하지 않는 이메일입니다.", Toast.LENGTH_LONG).show()
                }else{
                    Log.d("---------------------------------------WARN: ", task.exception?.message.toString())
                }
        }
    }
    private fun moveToLoginPage(){
            Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            finishAffinity()  // 액티비티 스택을 비움
            startActivity(intent)
        }, 1500) // 1.5초 정도 딜레이를 준 후 시작
    }
}
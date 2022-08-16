package com.example.mapforgirls

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.system.Os.remove
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.example.mapforgirls.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MypageFragment : Fragment(){
    lateinit var binding: FragmentMypageBinding
    private val database by lazy { FirebaseDatabase.getInstance() }
    private var userRef : DatabaseReference? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var name : String? = null
    private var currentUser = auth.currentUser
    var signupActivity = SignupActivity()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        /*
        val userInfo: SharedPreferences = (context as MainActivity).getSharedPreferences("userInfo", MODE_PRIVATE)
        val uid = userInfo.getString("uid", null)
        */
        readUserName(currentUser?.uid.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        binding.mypageGirlLogoutTv.setOnClickListener {
            auth.signOut()
            moveLoginPage()
        }

        binding.mypageGirlWithdrawTv.setOnClickListener {
            deleteUser()
            moveLoginPage()
        }


        return binding.root
    }


    private fun readUserName(uid: String){  // 회원 이름을 읽는 함수
        // var name : String? = null
        // var callback = listener
        userRef = database.getReference("users")
        userRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {  // iterator
                for (user in snapshot.children) {
                    if (user.key.equals(uid)) {
                        name = user.child("name").value.toString()
                        binding.mypageGirlNameTv.text = name
                        // callback?.loadPage(name.toString())
                        return
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "Error : $error")
            }
        })
    }
    private fun deleteUser(){
        currentUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
    }
    /*
    override fun loadPage(name : String){
        binding.mypageGirlNameTv.text = name
    }
     */
    private fun moveLoginPage(){  // 로그인 페이지로 이동하는 함수
        activity?.let{
            val intent = Intent(context, LoginActivity::class.java)
            finishAffinity(it)
            startActivity(intent)
        }
    }
}
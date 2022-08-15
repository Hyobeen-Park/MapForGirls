package com.example.mapforgirls

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mapforgirls.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_mypage.*


class MypageFragment : Fragment(){
    lateinit var binding: FragmentMypageBinding
    private val database by lazy { FirebaseDatabase.getInstance() }
    private var userRef : DatabaseReference? = null
    lateinit var auth: FirebaseAuth
    var name : String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val userInfo: SharedPreferences = (context as MainActivity).getSharedPreferences("userInfo", MODE_PRIVATE)
        val uid = userInfo.getString("uid", null)
        readUserName(uid.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun readUserName(uid: String){  // 회원 이름을 읽는 함수
        // var name : String? = null
        // var callback = listener
        userRef = database.getReference("users")
        userRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {  // iterator
                for (user in snapshot.children) {
                    Log.v(TAG, user.toString())
                    if (user.key.equals(uid)) {
                        name = user.child("name").value.toString()
                        binding.mypageGirlNameTv.text = name
                        // callback?.loadPage(name.toString())
                        return
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("database", "Error : " + error.toString())
            }
        })
    }
    /*
    override fun loadPage(name : String){
        binding.mypageGirlNameTv.text = name
    }
     */
}
package com.example.mapforgirls

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mapforgirls.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_mypage.*
import java.io.File


class MypageFragment : Fragment(){
    lateinit var binding: FragmentMypageBinding
    private var userRef : DatabaseReference? = null
    var database : DatabaseReference = FirebaseDatabase.getInstance().reference
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var currentUser = auth.currentUser
    private var imageUri: Uri? = null
    private var dialogwithUri : Uri = Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + R.drawable.img_girls)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var userData: SharedPreferences = (context as MainActivity).getSharedPreferences("userData", MODE_PRIVATE)
        var userEditor = userData.edit()
        var name = userData.getString("name", null)
        var email = userData.getString("email", null)
        var userType = userData.getString("userType", null)
        var profileUri = userData.getString("profileUri", null)

        binding = FragmentMypageBinding.inflate(inflater, container, false)


        binding.mypageGirlNameTv.text = name
        getFireBaseProfileImage(currentUser!!.uid)
        binding.mypageGirlProfileIv.setImageURI(imageUri)

        binding.mypageGirlEditProfileBtn.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java)
            startActivity(intent)
        }
        binding.mypageGirlLogoutTv.setOnClickListener {  // 로그아웃
            auth.signOut()
            moveLoginPage()
        }
        binding.mypageGirlWithdrawTv.setOnClickListener {  // 회원 탈퇴
            deleteUser()
            moveLoginPage()
        }

        return binding.root
    }

    /*
    private fun readUserName(uid: String){  // 회원 이름을 읽는 함수
        // var name : String? = null
        // var callback = listener
        userRef = database.child("users")
        userRef!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {  // iterator
                for (user in snapshot.children) {
                    if (user.key.equals(uid)) {
                        name = user.child("name").value.toString()
                        profileUri = user.child("profile").value.toString().toUri()
                        readUserProfile()
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
    */

    private fun deleteUser(){  // 회원을 DB에서 제거하는 함수
        currentUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
        database.child("users").child(currentUser!!.uid).removeValue()
    }

    private fun moveLoginPage(){  // 로그인 페이지로 이동하는 함수
        activity?.let{
            val intent = Intent(context, LoginActivity::class.java)
            // finishAffinity(it)
            startActivity(intent)
        }
    }
    /*
    private fun refreshFragment() {
        var ft: FragmentTransaction = requireFragmentManager().beginTransaction()
        // ft.detach(fragment).attach(fragment).commit()
        ft.detach(this).attach(this).commit()
    }
     */
    private fun getFireBaseProfileImage(uid: String) {  // 파이어베이스에서 프로필을 가져오는 함수
        val file: File? =
            (context as MainActivity).getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profile_img")
        if (!file?.isDirectory!!) {
            file.mkdir()
        }
        downloadImg(uid)
    }

    private fun downloadImg(uid : String) {  // 이미지를 다운받아 프로필에 불러오는 함수
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        storageRef.child("profile_img/profile$uid.jpg").downloadUrl.addOnSuccessListener { uri ->
            Log.d("오냐오냐", uri.toString())
            Glide.with((context as MainActivity))
                .load(uri)
                .into(mypageGirl_profile_iv)
            imageUri = uri
        }.addOnFailureListener { }
    }

}
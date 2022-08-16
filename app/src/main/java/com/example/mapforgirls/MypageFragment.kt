package com.example.mapforgirls

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.bumptech.glide.Glide
import com.example.mapforgirls.databinding.FragmentMypageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_mypage.*


class MypageFragment : Fragment(){
    lateinit var binding: FragmentMypageBinding
    private var userRef : DatabaseReference? = null
    var database : DatabaseReference = FirebaseDatabase.getInstance().reference
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var currentUser = auth.currentUser
    var uri : Uri = Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + R.drawable.img_girls)
    // var signupActivity = SignupActivity()

    companion object{

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // binding.mypageGirlProfileIv.setImageURI(uri)
        // readUserName(currentUser?.uid.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val userData: SharedPreferences = (context as MainActivity).getSharedPreferences("userData", MODE_PRIVATE)
        val name = userData.getString("name", null)
        val email = userData.getString("email", null)
        val userType = userData.getString("userType", null)
        val profileUri = userData.getString("profileUri", null)
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        binding.mypageGirlNameTv.text = name
        readUserProfile(profileUri.toString())
        binding.mypageGirlProfileIv.setImageURI(uri)

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
    private fun readUserProfile(profileUri : String){
        val storage = FirebaseStorage.getInstance("gs://map-for-girls.appspot.com")
        val storageRef = storage.reference
        storageRef.child("images//"+currentUser!!.uid+"//"+profileUri).downloadUrl.addOnSuccessListener{ uriStorage ->
            uri = uriStorage
            binding.mypageGirlProfileIv.setImageURI(uri)
        }
            /*
            .addOnSuccessListener { uri -> //이미지 로드 성공시
            Glide.with(getApplicationContext<FragmentActivity>())
                .load(uri)
                .into(mypageGirl_profile_iv)
        }.addOnFailureListener { //이미지 로드 실패시
            Toast.makeText(
                getApplicationContext(),
                "실패",
                Toast.LENGTH_SHORT
            ).show()
        }

             */
    }


    private fun deleteUser(){  // 회원을 DB에서 제거하는 함수
        currentUser?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
        database.child("users").child(currentUser!!.uid).removeValue()
    }
    /*
    private fun loadPage(name : String){
        binding.mypageGirlNameTv.text = name
    }
     */

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

}
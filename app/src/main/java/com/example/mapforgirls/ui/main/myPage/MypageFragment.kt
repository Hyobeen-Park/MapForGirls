package com.example.mapforgirls.ui.main.myPage

import android.content.ContentValues.TAG
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
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.mapforgirls.LoginActivity
import com.example.mapforgirls.MainActivity
import com.example.mapforgirls.ProfileActivity
import com.example.mapforgirls.R
import com.example.mapforgirls.data.entities.ColumnData
import com.example.mapforgirls.data.entities.Scrap
import com.example.mapforgirls.data.local.ColumnDatabase
import com.example.mapforgirls.databinding.FragmentMypageBinding
import com.example.mapforgirls.ui.main.columns.ColumnDetailActivity
import com.example.mapforgirls.ui.main.columns.ColumnsRVAdapter
import com.google.firebase.FirebaseException
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
    private var scrapList : List<Scrap>? = null
    var columnList : ArrayList<ColumnData>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMypageBinding.inflate(inflater, container, false)

        var userData: SharedPreferences = (context as MainActivity).getSharedPreferences("userData", MODE_PRIVATE)
        var userEditor = userData.edit()
        var name = userData.getString("name", null)
        var email = userData.getString("email", null)
        var userType = userData.getString("userType", null)
        var profileUri = userData.getString("profileUri", null)

        val columnDB = ColumnDatabase.getInstance(context as MainActivity)
        scrapList = columnDB!!.columnDao().getScrappedColumn()
        connectDatabase()
        adapter()

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



    private fun connectDatabase() {
        // 데이터베이스 연결
        for (i in 0 until scrapList!!.count()) {
            database.child("column").child(scrapList!![i].sectionName).child("items").child(scrapList!![i].ColumnId)
                .addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var img = activity?.resources!!.getIdentifier(snapshot.child("image").value.toString(), "drawable", activity?.packageName)
                        Log.d("--------------------데이터: ", "$img, ${snapshot.child("title").value.toString()}, ${snapshot.child("author").value.toString()}, ${snapshot.child("content").value.toString()}")
                        columnList?.add(
                            ColumnData(
                                scrapList!![i].sectionName, scrapList!![i].ColumnId,
                                img,
                                snapshot.child("title").value.toString(),
                                snapshot.child("author").value.toString(),
                                snapshot.child("content").value.toString()
                            )
                        )
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.d("database", "Error : " + error.toString())
                    }
                })
        }
        Log.d("--------------------칼럼 사이즈: " , columnList?.size.toString())
    }

    private fun adapter(){
        //리사이클러뷰 어댑터 적용
        val scrapRVAdapter = ScrapRVAdapter(columnList!!)
        binding.mypageGirlClipRv.adapter = scrapRVAdapter

        // 리사이클러뷰 클릭 이벤트
        scrapRVAdapter.setMyItemClickListener(object :
            ScrapRVAdapter.MyItemClickListener {
            override fun onItemClick(column: ColumnData) {
                activity.let {
                    val intent = Intent(context, ColumnDetailActivity::class.java)
                    intent.putExtra("column", column)
                    startActivity(intent)
                }
            }
        })
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
            Glide.with((context as MainActivity))
                .load(uri)
                .into(mypageGirl_profile_iv)
            imageUri = uri
        }.addOnFailureListener { }
    }

}
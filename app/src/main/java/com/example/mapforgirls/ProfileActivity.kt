package com.example.mapforgirls

import android.content.ContentValues.TAG
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.suspendCoroutine


class ProfileActivity : AppCompatActivity() {
    private val PICK_FROM_ALBUM = 1
    private var tempFile: File? = null
    private var imageUri: Uri? = null
    private var pathUri: String? = null
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private var database : DatabaseReference = FirebaseDatabase.getInstance().reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profile_newImage_btn.setOnClickListener {
            gotoAlbum()
        }

        profile_change_btn.setOnClickListener {
            // 데이터베이스에서 사진 변경, 닉네임 변경
            MainScope().launch {
                changeProfile()
            }
            Handler().postDelayed({
                // finish()
                onBackPressed()
            }, 1000) // 1초 정도 딜레이를 준 후 시작

            // supportFragmentManager.beginTransaction().replace(R.id.main_frm, MypageFragment()).commitAllowingStateLoss()
        }
    }

    private fun gotoAlbum() {  // 앨범 메소드
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) { // 코드가 틀릴경우
            Toast.makeText(this, "취소 되었습니다", Toast.LENGTH_LONG).show()
            if (tempFile != null) {
                if (tempFile!!.exists()) {
                    if (tempFile!!.delete()) {
                        Log.e(TAG, tempFile!!.absolutePath.toString() + " 삭제 성공")
                        tempFile = null
                    }
                }
            }
            return
        }
        when (requestCode) {
            PICK_FROM_ALBUM -> {
                // 코드 일치
                // Uri
                imageUri = data?.data
                pathUri = getPath(data?.data)
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : $imageUri")
                profile_image_iv.setImageURI(imageUri) // 이미지 띄움
            }
        }
    }

    private fun getPath(uri: Uri?): String? {  // uri 절대경로 가져오기
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursorLoader = CursorLoader(this, uri!!, proj, null, null, null)
        val cursor: Cursor = cursorLoader.loadInBackground()!!
        val index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(index)
    }


    private suspend fun changeProfile() {
        if (imageUri != null && profile_name_et.text != null) {
            // val file : Uri = Uri.fromFile(File(pathUri))  // path


            /*
            val storageReference: StorageReference = storage!!.reference
                .child("usersProfileImages").child("uid/" + file.lastPathSegment)
            storageReference.putFile(imageUri!!).addOnCompleteListener { task ->
                val imageUrl: Task<Uri> =
                    task.result.storage.downloadUrl
                while (!imageUrl.isComplete) {}

                val profileImageUrl = imageUrl.result.toString()

                // database에 저장
                database.child("users").child(currentUser!!.uid).setValue(profileImageUrl)
            }
             */
            suspendCoroutine<Boolean> {  // 동기 방식
                Handler(Looper.getMainLooper()).postDelayed({
                    // 스토리지에 방생성 후 선택한 이미지 넣음
                    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    val imgFileName = "IMAGE_" + timeStamp + "_.png"
                    val storageRef = storage.reference.child("images").child(currentUser!!.uid).child(imgFileName)

                    storageRef.putFile(imageUri!!).addOnSuccessListener {
                        Toast.makeText(this, "이미지 업로드", Toast.LENGTH_LONG).show()
                    }
                    database.child("users").child(currentUser!!.uid).child("profile").setValue(imgFileName)
                    database.child("users").child(currentUser!!.uid).child("name").setValue(profile_name_et.text.toString())
                    Toast.makeText(this, "프로필 변경 완료", Toast.LENGTH_LONG).show()
                }, 0)
            }
        } else if (profile_name_et.text == null) {
            //Toast.makeText(this, "어라?", Toast.LENGTH_LONG).show()
        } else {
            //Toast.makeText(this, "어라라?", Toast.LENGTH_LONG).show()
        }
    }

}
package com.example.mapforgirls

import android.content.ContentValues.TAG
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.content.CursorLoader
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_mypage.*
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
    var dialogwithUri : Uri = Uri.parse("android.resource://" + R::class.java.getPackage().name + "/" + R.drawable.img_girls)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        getFireBaseProfileImage(currentUser!!.uid)

        profile_newImage_btn.setOnClickListener {
            gotoAlbum()
        }
        profile_nomal_btn.setOnClickListener {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val desertRef = storageRef.child("profile_img/profile${currentUser?.uid}.jpg")
            desertRef.delete().addOnSuccessListener { }.addOnFailureListener { }
            imageUri != null
            profile_image_iv.setImageURI(dialogwithUri)
        }
        profile_change_btn.setOnClickListener {
            // ???????????????????????? ?????? ??????, ????????? ??????
            MainScope().launch {
                changeProfile()
            }
            Handler().postDelayed({
                // finish()
                onBackPressed()
            }, 2000) // 2??? ?????? ???????????? ??? ??? ??????
        }
    }

    private fun gotoAlbum() {  // ?????? ?????? ??????
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {  // resultCode??? ????????? ???
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_LONG).show()
            if (tempFile != null) {
                if (tempFile!!.exists()) {
                    if (tempFile!!.delete()) {
                        Log.e(TAG, tempFile!!.absolutePath.toString() + " ?????? ??????")
                        tempFile = null
                    }
                }
            }
            return
        }
        when (requestCode) {
            PICK_FROM_ALBUM -> {  // requestCode??? ????????? ???
                imageUri = data?.data
                pathUri = getPath(data?.data)
                Log.d(TAG, "PICK_FROM_ALBUM photoUri : $imageUri")
                profile_image_iv.setImageURI(imageUri) // ????????? ?????????
            }
        }
    }
    private fun getPath(uri: Uri?): String? {  // uri ??????????????? ???????????? ??????
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursorLoader = CursorLoader(this, uri!!, proj, null, null, null)
        val cursor: Cursor = cursorLoader.loadInBackground()!!
        val index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(index)
    }
    private fun changeProfile() {  // ????????? ?????? ??????
        if (imageUri != null && profile_name_et.text.isNotEmpty()) {
            createProfile_Photo_and_Delete()
            database.child("users").child(currentUser!!.uid).child("name").setValue(profile_name_et.text.toString())

        } else if (imageUri != null && profile_name_et.text.isEmpty()) {
            createProfile_Photo_and_Delete()
        } else if(imageUri == null && profile_name_et.text.isNotEmpty()) {
            database.child("users").child(currentUser!!.uid).child("name").setValue(profile_name_et.text.toString())
        }else{
            Log.d(TAG, "NOTHING")
        }
        Toast.makeText(this, "????????? ???????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
    }
    private fun createProfile_Photo_and_Delete() {  // ????????? ????????? ?????? ??? ?????? ???????????? ???????????? ??????
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val filename = "profile" + currentUser?.uid + ".jpg"
        val file = imageUri
        Log.d("??????", file.toString())
        val riversRef = storageRef.child("profile_img/$filename")
        val uploadTask = riversRef.putFile(file!!)
        val desertRef = storageRef.child("profile_img/profile${currentUser?.uid}.jpg")
        desertRef.delete().addOnSuccessListener { }.addOnFailureListener { }
        uploadTask.addOnFailureListener { }.addOnSuccessListener {
        }
    }
    private fun getFireBaseProfileImage(uid: String) {  // ???????????????????????? ???????????? ???????????? ??????
        val file: File? =
            getExternalFilesDir(Environment.DIRECTORY_PICTURES + "/profile_img")
        if (!file?.isDirectory!!) {
            file.mkdir()
        }
        downloadImg(uid)
    }
    private fun downloadImg(uid : String) {  // ???????????? ???????????? ???????????? ???????????? ??????
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        storageRef.child("profile_img/profile$uid.jpg").downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this)
                .load(uri)
                .into(profile_image_iv)
            imageUri = uri
        }.addOnFailureListener { }
    }

}
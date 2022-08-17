package com.example.mapforgirls.ui.main.chatting

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.mapforgirls.PharmacyData
import com.example.mapforgirls.data.model.ChatModel
import com.example.mapforgirls.databinding.ActivityChattingDetailBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChattingDetailActivity: AppCompatActivity() {
    lateinit var binding: ActivityChattingDetailBinding

    private var database : DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var userId: String
    private lateinit var destinationUid: String
    private var chatModel = ChatModel()
    private var chattingRoomId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChattingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = getSharedPreferences("userInfo", MODE_PRIVATE).getString("uid", "").toString()
        destinationUid = intent.getStringExtra("destinationUid")!!

        var pharmacist = intent.getSerializableExtra("pharmacist") as PharmacyData
        binding.chattingDetailPharmacistTv.text = pharmacist.pharmacyName + " 약사"

        checkChatttingRooms()
        setOnClickListener()

    }

    // 클릭 리스너
    private fun setOnClickListener() {
        // 전송
        binding.chattingDetailSendBtn.setOnClickListener {
            if(binding.chattingDetailChatEt.text.toString() != "") {
                chatModel.users!!.put(userId, true)
                chatModel.users!!.put(destinationUid, true)
                val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
                var time = dateFormat.format(Date(System.currentTimeMillis())).toString()
                var comment = ChatModel.Comment(userId, binding.chattingDetailChatEt.text.toString(), time)

                if (chattingRoomId == null) {       // 채팅창 없으면 새로 만들기
                    binding.chattingDetailSendBtn.isEnabled = false
                    database.child("chattingRooms").push().setValue(chatModel).addOnSuccessListener {
                        checkChatttingRooms()
                    }
                } else {    // 있으면 메시지만 저장
                    database.child("chattingRooms").child(chattingRoomId!!)
                        .child("comments").push().setValue(comment)
                }

//                val chatAdapter = ChatRVAdapter(userId)
//                chatAdapter.addItem(chattingRoomId!!)
//                binding.chattingDetailContentRv.adapter = chatAdapter
                binding.chattingDetailChatEt.text = null
            }
        }
    }

    // 같은 유저들끼리 이미 만들어진 채팅창이 있는지 확인
    private fun checkChatttingRooms(){
        database.child("chattingRooms").orderByChild("users/$userId").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children){
                        val chatModel = item.getValue<ChatModel>()
                        if(chatModel?.users!!.containsKey(destinationUid)){
                            chattingRoomId = item.key.toString()
                            binding.chattingDetailSendBtn.isEnabled = true

                            var chatList = ArrayList<ChatModel.Comment>()
                            database.child("chattingRooms").child(chattingRoomId!!).child("comments").addValueEventListener(object: ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    chatList.clear()
                                    for(i in snapshot.children) {
                                        chatList.add(i.getValue<ChatModel.Comment>()!!)
                                    }

                                    val chatAdapter = ChatRVAdapter(chatList, userId)
                                    binding.chattingDetailContentRv.adapter = chatAdapter
                                    binding.chattingDetailContentRv.scrollToPosition(chatList.size - 1)
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Log.d("chatRV", error.message)
                                }
                            })



                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("chattingRoom fail", error.message)
                }
            })
    }



}
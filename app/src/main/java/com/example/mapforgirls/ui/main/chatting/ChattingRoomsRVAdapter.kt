package com.example.mapforgirls.ui.main.chatting

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mapforgirls.PharmacyData
import com.example.mapforgirls.R
import com.example.mapforgirls.UserInfo
import com.example.mapforgirls.data.model.ChatModel
import com.example.mapforgirls.databinding.ActivityCertifyBinding.bind
import com.example.mapforgirls.databinding.DialogPharmacistBinding.bind
import com.example.mapforgirls.databinding.ItemChattingRoomBinding
import com.example.mapforgirls.databinding.ItemPharmacistBinding
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ChattingRoomsRVAdapter(val context: Context): RecyclerView.Adapter<ChattingRoomsRVAdapter.ViewHolder>() {
    val chatRoomList = ArrayList<ChatModel>()       // 채팅방 데이터들
    val chatRoomKeys = ArrayList<String>()          // 채팅방 키
    val userId = context.getSharedPreferences("userInfo", MODE_PRIVATE).getString("uid", "")
    val adapter = this

    val database = FirebaseDatabase.getInstance().reference

    init {
        getChattingRoomsData()
        Log.d("data", "inint")

        Log.d("data", chatRoomList.size.toString())
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.item_chatting_room, viewGroup, false)
        Log.d("data", "create")
        Log.d("data", chatRoomList.size.toString())
        return ViewHolder(ItemChattingRoomBinding.bind(view))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var destinationUid: String
        if(chatRoomList[position].users!!.keys.first() != userId) {
            destinationUid = chatRoomList[position].users!!.keys.first()
        } else {
            destinationUid = chatRoomList[position].users!!.keys.last()
        }
        database.child("users").orderByKey().equalTo(destinationUid)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    holder.destinationUser = snapshot.getValue<UserInfo>()!!
                    holder.binding.itemChattingPharmacistTv.text = snapshot.child("name").value.toString()
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("ChattinRoomRVAdapter", error.message)
                }
            })

//        holder.background.setOnClickListener()               //채팅방 항목 선택 시
//        {
//            try {
//                var intent = Intent(context, ChatRoomActivity::class.java)
//                intent.putExtra("ChatRoom", chatRooms.get(position))      //채팅방 정보
//                intent.putExtra("Opponent", holder.opponentUser)          //상대방 사용자 정보
//                intent.putExtra("ChatRoomKey", chatRoomKeys[position])     //채팅방 키 정보
//                context.startActivity(intent)                            //해당 채팅방으로 이동
//                (context as AppCompatActivity).finish()
//            }catch (e:Exception)
//            {
//                e.printStackTrace()
//                Toast.makeText(context,"채팅방 이동 중 문제가 발생하였습니다.",Toast.LENGTH_SHORT).show()
//            }
//        }

        if (chatRoomList[position].comments!!.size > 0) {         //채팅방 메시지가 존재하는 경우
            setLatestMessage(holder, position)        // 가장 최근 메시지
            countMessage(holder, position)
        }

        Log.d("data", "bind")
    }

    override fun getItemCount(): Int = chatRoomList.size

    inner class ViewHolder(val binding : ItemChattingRoomBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var destinationUser: UserInfo
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun getChattingRoomsData() {     //전체 채팅방 목록 초기화 및 업데이트
        database.child("chattingRooms")
            .orderByChild("users/$userId").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    chatRoomList.clear()
                    for (data in snapshot.children) {
                        chatRoomList.add(data.getValue<ChatModel>()!!)
                        chatRoomKeys.add(data.key!!)
                    }
                    Log.d("data", chatRoomList.size.toString())
                    notifyDataSetChanged()
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.d("ChattingRoomsRVAdapter", error.message)
                }
            })
        adapter.notifyDataSetChanged()
    }

    private fun setLatestMessage(holder: ViewHolder, position: Int) {
        var latest = chatRoomList[position].comments!!.values.sortedWith(compareBy({it.time})).last()   //가장 최근 메시지
        holder.binding.itemChattingContentTv.text = latest.message.toString()
        holder.binding.itemChattingDateTv.text = latest.time.toString()
    }

    private fun countMessage(holder: ViewHolder, position: Int) {
        //확인하지 않은 메시지 개수
    }

}
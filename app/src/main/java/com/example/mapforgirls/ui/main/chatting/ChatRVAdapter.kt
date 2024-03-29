package com.example.mapforgirls.ui.main.chatting

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mapforgirls.PharmacyData
import com.example.mapforgirls.R
import com.example.mapforgirls.data.entities.ColumnData
import com.example.mapforgirls.data.model.ChatModel
import com.example.mapforgirls.databinding.ActivityChattingDetailBinding
import com.example.mapforgirls.databinding.ItemMessageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

class ChatRVAdapter(private val chatList: ArrayList<ChatModel.Comment>, private val userId: String) : RecyclerView.Adapter<ChatRVAdapter.ViewHolder>(){
    private var database = FirebaseDatabase.getInstance().reference

//    fun init() {
//        database.child("chattingRooms").child(chattingRoomId).child("comments").addValueEventListener(object: ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                chatList.clear()
//                for(i in snapshot.children) {
//                    chatList.add(i.getValue<ChatModel.Comment>()!!)
//                }
//                notifyDataSetChanged()
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("chatRV", error.message)
//            }
//        })
//    }

    fun addItem(chat: ChatModel.Comment) {
        chatList.add(chat)
        notifyDataSetChanged()
    }

    fun removeItem(chattingRoomId: String) {
        chatList.clear()
        notifyDataSetChanged()
    }

    fun getChatList(): ArrayList<ChatModel.Comment> {
        return chatList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding : ItemMessageBinding = ItemMessageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(chatList[position].uid.equals(userId)){ // 본인 채팅
            holder.binding.itemMessageLlayout.gravity = Gravity.RIGHT
            holder.binding.itemMessageTv.background = ContextCompat.getDrawable(holder.binding.root.context, R.drawable.img_chat_girls)
        }else{ // 상대방 채팅
            holder.binding.itemMessageLlayout.gravity = Gravity.LEFT
            holder.binding.itemMessageTv.background = ContextCompat.getDrawable(holder.binding.root.context, R.drawable.img_chat_women_red)
        }

        holder.bind(chatList[position])
//        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(pharmacistList[position])}
    }

    override fun getItemCount(): Int = chatList.size

    inner class ViewHolder(val binding : ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatModel.Comment) {
            binding.itemMessageTv.text = chat.message
        }
    }

}
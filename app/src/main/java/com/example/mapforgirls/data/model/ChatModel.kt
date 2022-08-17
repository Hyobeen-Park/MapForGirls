package com.example.mapforgirls.data.model

class ChatModel(
    val users: HashMap<String, Boolean>? = HashMap(),        //채팅방 유저 정보
    val comments: HashMap<String, Comment>? = HashMap()) {      //채팅 내용들

    class Comment(
        val uid: String? = null,
        val message: String? = null,
        val time: String? = null
    )
}
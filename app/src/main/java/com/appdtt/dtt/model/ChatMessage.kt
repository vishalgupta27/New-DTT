package com.appdtt.dtt.model


data class ChatMessage(
    val senderId: String,
    val receiverId: String,
    val message: String
) {
    constructor() : this("", "", "") // Required empty constructor for Firebase
}



package com.appdtt.dtt.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.appdtt.dtt.R
import com.appdtt.dtt.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth

class ChatAdapter(private val context: Context, private val chatMessages: MutableList<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        if (chatMessage.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            // Display sender message on the right side of the screen
            holder.tvSenderMessage.visibility = View.VISIBLE
            holder.tvReceiverMessage.visibility = View.GONE
            holder.tvSenderMessage.text = chatMessage.message
        } else {
            // Display receiver message on the left side of the screen
            holder.tvSenderMessage.visibility = View.GONE
            holder.tvReceiverMessage.visibility = View.VISIBLE
            holder.tvReceiverMessage.text = chatMessage.message
        }
    }

    override fun getItemCount(): Int {
        return chatMessages.size
    }

    fun addMessage(chatMessage: ChatMessage) {
        chatMessages.add(chatMessage)
        notifyItemInserted(chatMessages.size - 1)
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSenderMessage: TextView = itemView.findViewById(R.id.tvSenderMessage)
        val tvReceiverMessage: TextView = itemView.findViewById(R.id.tvReceiverMessage)
    }
}

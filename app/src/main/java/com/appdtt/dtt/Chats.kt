package com.appdtt.dtt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appdtt.dtt.adapter.ChatAdapter
import com.appdtt.dtt.model.ChatMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class Chats : AppCompatActivity() {

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var senderId: String
    private lateinit var receiverId: String
    private lateinit var rvChat: RecyclerView
    private lateinit var btnSend: TextView
    private lateinit var etMessage: EditText

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val chatMessage = snapshot.getValue(ChatMessage::class.java)
            chatMessage?.let {
                chatAdapter.addMessage(chatMessage)
                rvChat.scrollToPosition(chatAdapter.itemCount - 1) // Scroll to the latest message
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            // Not needed for this implementation
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            // Not needed for this implementation
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            // Not needed for this implementation
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e(TAG, "Failed to read value.", error.toException())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

         rvChat = findViewById<RecyclerView>(R.id.rvChat)
         btnSend = findViewById<TextView>(R.id.btnSend)
         etMessage = findViewById<EditText>(R.id.etMessage)

        senderId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        receiverId = intent.getStringExtra(EXTRA_RECEIVER_ID).orEmpty()

        initView()
        initListeners()
    }

    private fun initView() {
        chatAdapter = ChatAdapter(this, mutableListOf())
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = chatAdapter

    }

    private fun initListeners() {
        btnSend.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        val message = etMessage.text.toString()
        if (message.isNotEmpty()) {
            val chatMessage = ChatMessage(senderId, receiverId, message)
            val database = FirebaseDatabase.getInstance()
            val chatRef = database.getReference("chats")
            chatRef.push().setValue(chatMessage)
                .addOnSuccessListener {
                    // Message sent successfully
                    etMessage.text.clear()
                }
                .addOnFailureListener { exception ->
                    // Failed to send message
                    Toast.makeText(
                        this,
                        "Failed to send message: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun onStart() {
        super.onStart()
        val database = FirebaseDatabase.getInstance()
        val chatRef = database.getReference("chats")
        chatRef.addChildEventListener(childEventListener)
    }

    override fun onStop() {
        super.onStop()
        val database = FirebaseDatabase.getInstance()
        val chatRef = database.getReference("chats")
        chatRef.removeEventListener(childEventListener)
    }

    companion object {
        private const val TAG = "ChatActivity"
        private const val EXTRA_RECEIVER_ID = "extra_receiver_id"

        fun start(context: Context, receiverId: String) {
            val intent = Intent(context, Chats::class.java)
            intent.putExtra(EXTRA_RECEIVER_ID, receiverId)
            context.startActivity(intent)
        }
    }
}

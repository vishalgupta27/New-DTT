package com.appdtt.dtt

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import com.appdtt.dtt.utils.Utility
import com.google.firebase.auth.FirebaseAuth

class forgotpassword : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)

        firebaseAuth = FirebaseAuth.getInstance()

        val reseteamils = findViewById<EditText>(R.id.resetemail)
        val reset = findViewById<Button>(R.id.btn_reset)

        reset.setOnClickListener {

            if (reseteamils.text.toString().isEmpty()) {
                Utility.showAlertDialog(this, null, "Enter Email")

            } else {
                firebaseAuth.sendPasswordResetEmail(reseteamils.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Utility.showAlertDialog(this, "", "Check your email")
                            // Create a Handler
                            val handler = Handler()
                            // Delayed Intent after 5 seconds
                            handler.postDelayed({
                                val intent = Intent(this, login::class.java)
                                startActivity(intent)
                                finish()
                            }, 5000)
                        } else {
                            Utility.showAlertDialog(this, "", "Invalid Email ")
                        }
                    }
            }
        }
    }
}
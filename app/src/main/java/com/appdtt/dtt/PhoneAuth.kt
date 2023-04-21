package com.appdtt.dtt


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneAuth : AppCompatActivity() {

    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    // Views
    private lateinit var phoneNumberEditText: EditText
    private lateinit var verifyCodeEditText: EditText
    private lateinit var sendCodeButton: Button
    private lateinit var verifyCodeButton: Button

    // Variables
    private lateinit var verificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_auth)

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance()

        // Initialize views
        phoneNumberEditText = findViewById(R.id.edit_text_phone_number)
        verifyCodeEditText = findViewById(R.id.edit_text_verification_code)
        sendCodeButton = findViewById(R.id.button_send_code)
        verifyCodeButton = findViewById(R.id.button_verify_code)

        // Send code button click listener
        sendCodeButton.setOnClickListener {
            val phoneNumber = phoneNumberEditText.text.toString().trim()

            // Check if phone number is valid
            if (phoneNumber.isEmpty() || phoneNumber.length != 10) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            } else {
                // Send verification code
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber("+91$phoneNumber")            // Phone number to verify
                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                    .setActivity(this)                 // Activity (for callback binding)
                    .setCallbacks(callbacks)           // OnVerificationStateChangedCallbacks
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            }
            }


        // Verify code button click listener
        verifyCodeButton.setOnClickListener {
            val code = verifyCodeEditText.text.toString().trim()

            // Check if code is valid
            if (code.isEmpty()) {
                Toast.makeText(this, "Please enter the verification code", Toast.LENGTH_SHORT).show()
            } else {
                // Verify code
                val credential = PhoneAuthProvider.getCredential(verificationId, code)
                signInWithPhoneAuthCredential(credential)
            }
        }
    }

    // Phone authentication callbacks
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            // Save the verification ID and resending token for later use
            this@PhoneAuth.verificationId = verificationId
            resendToken = token
            Toast.makeText(this@PhoneAuth, "Verification code sent", Toast.LENGTH_SHORT).show()
        }

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // Sign in with the received credential
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@PhoneAuth, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Sign in with phone authentication credential
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification successful", Toast.LENGTH_SHORT).show()
                    // Delay the navigation to the Dashboard activity by 5 seconds
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 4000)
                } else {
                    Toast.makeText(this, "Verification failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

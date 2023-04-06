package com.example.dtt


import android.Manifest
import android.annotation.SuppressLint

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.albums.utils.Utility.isValidEmail
import com.example.albums.utils.Utility.isValidPassword

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class login : AppCompatActivity() {
    var singUp : TextView? =null
    var singUps : TextView? =null
    var logIn : TextView? =null
    var logInLayout : LinearLayout? =null
    var singUpLayout : LinearLayout? =null
    var singIn : Button? =null

    var eMails : EditText? =null
    var passwordss : EditText? =null
    var passwords01 : EditText? =null
    var eMail : EditText? =null
    var passwords : EditText? =null
    var profile_image : ImageView? =null




    private val cameraRequest = 1888
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PICK_IMAGE = 2


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        singUp = findViewById(R.id.singUp)
        singUps = findViewById(R.id.singUps)
        logIn = findViewById(R.id.logIn)
        logInLayout = findViewById(R.id.logInLayout)
        singUpLayout = findViewById(R.id.singUpLayout)
        singIn = findViewById(R.id.singIn)

        passwordss = findViewById(R.id.passwordss)
        passwords01 = findViewById(R.id.passwords01)
        eMails = findViewById(R.id.eMails)
        eMail = findViewById(R.id.eMail)
        passwords = findViewById(R.id.passwords)



     /*   upload!!.setOnClickListener {
            Toast.makeText(this, "vlip uplaod", Toast.LENGTH_SHORT).show()
            openGallery()
        }*/
        //upload photo by gallery and take camera
        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), cameraRequest)

       /* profile_image!!.setOnClickListener {
            Toast.makeText(this, "vlip images", Toast.LENGTH_SHORT).show()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }*/



        singUp!!.setOnClickListener {
            singUp!!.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUp!!.setTextColor(resources.getColor(R.color.textColor,null))
            logIn!!.background = null
            singUpLayout!!.visibility = View.VISIBLE
            logInLayout!!.visibility = View.GONE
            logIn!!.setTextColor(resources.getColor(R.color.pinkColor,null))
        }
        logIn!!.setOnClickListener {
            singUp!!.background = null
            singUp!!.setTextColor(resources.getColor(R.color.pinkColor,null))
            logIn!!.background = resources.getDrawable(R.drawable.switch_trcks,null)
            singUpLayout!!.visibility = View.GONE
            logInLayout!!.visibility = View.VISIBLE
            logIn!!.setTextColor(resources.getColor(R.color.textColor,null))
        }
        singIn!!.setOnClickListener {
            logincheck()
        }
        singUps!!.setOnClickListener {
            signUpCheck()
        }
    }


    fun signUpCheck() {
        if (eMails!!.text.toString().isEmpty() || isValidEmail(eMails!!.text.toString())) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show()
            return
        } else if (passwordss!!.text.toString().isEmpty()|| !isValidPassword(passwordss!!.text.toString())) {
            passwordss!!.error = " Password Must Contains " +
                    "\n Minimum of one lowercase alphabet. "+
                    "\n Minimum one uppercase alphabet." +
                    "\n Minimum one numerical digit." +
                    "\n Length of password must be greater than or equal to 8."
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            return
        }

        if (passwords01!!.text.toString().isEmpty()) {
            Toast.makeText(this, "Enter Confirm Password", Toast.LENGTH_SHORT).show()
            return
        }
        if (!passwords01!!.text.toString().equals(passwordss!!.text.toString())) {
            Toast.makeText(this, "Password do not match", Toast.LENGTH_SHORT).show()
            return
        }
        Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show()

    }


     fun logincheck() {
        if (eMail!!.text.toString().isEmpty() || !isValidEmail(eMail!!.text.toString())) {
            Toast.makeText(this, "Enter valid Email address and Password", Toast.LENGTH_SHORT).show()
            return
        }
        else{
            // API Calls Start
            val loginss = MethodLogin.methodsinstance.loginData(eMail!!.text.toString(), passwords!!.text.toString())
            loginss?.enqueue(object : Callback<Map<Any, Any>?> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<Map<Any, Any>?>, response: Response<Map<Any, Any>?>
                ) {
                    val chat = response.body()
                    if (chat != null) {
                        Log.d("getchat", chat.toString())
                        val gson = Gson()
                        val objrespose = gson.fromJson(gson.toJson(chat), LoginRequestModel::class.java)
                        if (objrespose.status == true) {
                            val intent = Intent (this@login,MainActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@login, "Logged in Successfully!", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Toast.makeText(this@login, "Please enter and password", Toast.LENGTH_SHORT).show()}

                    }
                }

                override fun onFailure(call: Call<Map<Any, Any>?>, t: Throwable) {
                    Log.i("errorMessage", t.message!!)
                    Toast.makeText(this@login, t.message, Toast.LENGTH_SHORT).show()
                }
            })}
     }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequest) {
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            profile_image!!.setImageBitmap(photo)
        } else if (requestCode == REQUEST_PICK_IMAGE) {
            val uri = data?.data
            profile_image!!.setImageURI(uri)
        }
    }
    //for gallery
    fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also { intent ->
            intent.type = "image/*"
            intent.resolveActivity(packageManager)?.also {
                startActivityForResult(intent, REQUEST_PICK_IMAGE)

            }
        }
    }

}
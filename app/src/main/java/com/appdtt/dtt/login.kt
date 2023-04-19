package com.appdtt.dtt


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.appdtt.dtt.utils.Utility.isValidEmail
import com.appdtt.dtt.utils.Utility.isValidPassword
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.util.*


class login : AppCompatActivity() {

    private val TAG = "loginTag"
    var singUp: TextView? = null
    var signfb: TextView? = null
    var singUps: TextView? = null
    var logIn: TextView? = null
    var logInLayout: LinearLayout? = null
    var singUpLayout: LinearLayout? = null
    var singIn: Button? = null

    var eMails: EditText? = null
    var passwordss: EditText? = null
    var passwords01: EditText? = null
    var eMail: EditText? = null
    var passwords: EditText? = null
    var profile_image: ImageView? = null

    // Google login using Firebase
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code: Int = 123
    val Req_Codefb: Int = 321
    private lateinit var firebaseAuth: FirebaseAuth

    // Facebook login using Firebase
    lateinit var callbackManager: CallbackManager
    private val EMAIL = "email"

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        singUp = findViewById(R.id.singUp)
        signfb = findViewById(R.id.signfacebook)
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            KeysHashUtility.getKeys(this)
        }

         val forgotpswd = findViewById<TextView>(R.id.reset)
        forgotpswd.setOnClickListener {
           val intent = Intent(this@login,forgotpassword::class.java)
            startActivity(intent)
        }

        singUp!!.setOnClickListener {
            singUp!!.background = resources.getDrawable(R.drawable.switch_trcks, null)
            singUp!!.setTextColor(resources.getColor(R.color.textColor, null))
            logIn!!.background = null
            singUpLayout!!.visibility = View.VISIBLE
            logInLayout!!.visibility = View.GONE
            logIn!!.setTextColor(resources.getColor(R.color.pinkColor, null))
        }
        logIn!!.setOnClickListener {
            singUp!!.background = null
            singUp!!.setTextColor(resources.getColor(R.color.pinkColor, null))
            logIn!!.background = resources.getDrawable(R.drawable.switch_trcks, null)
            singUpLayout!!.visibility = View.GONE
            logInLayout!!.visibility = View.VISIBLE
            logIn!!.setTextColor(resources.getColor(R.color.textColor, null))
        }
        singIn!!.setOnClickListener {
            logincheck()
        }
        singUps!!.setOnClickListener {
            signUpCheck()
        }

        callbackManager = CallbackManager.Factory.create()
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            startActivity(Intent(this@login, MainActivity::class.java))
            finish()
        }

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {

                override fun onCancel() {
                }

                override fun onError(error: FacebookException) {

                }

                override fun onSuccess(result: LoginResult) {
                    Log.e(TAG, "onSuccess125: 545454")
                    startActivity(Intent(this@login, MainActivity::class.java))
                    finish()
                    val graphRequest =
                        GraphRequest.newMeRequest(result.accessToken) { obj, response ->
                            try {
                                if (obj!!.has("id")) {
                                    Log.d("FACEBOOKDATA", obj.getString("name"))
                                    Log.d("FACEBOOKDATA", obj.getString("email"))
                                    Log.d("FACEBOOKDATA",
                                        obj.getJSONObject("picture").getJSONObject("data")
                                            .getString("url"))
                                }
                            } catch (e: Exception) {
                                // Handle exception
                            }
                        }

                    val param = Bundle()
                    param.putString("fields", "name,email,id,picture.type(large)")
                    graphRequest.parameters = param
                    graphRequest.executeAsync()
                }
            })

        signfb = findViewById(R.id.signfacebook)
        signfb!!.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this@login, listOf("public_profile"))
        }


// SIGN IN GOOGLE
        val signgoogle = findViewById<TextView>(R.id.signgoogle)
        FirebaseApp.initializeApp(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        firebaseAuth = FirebaseAuth.getInstance()

        signgoogle.setOnClickListener { view: View? ->
            Toast.makeText(this, "Logging In", Toast.LENGTH_SHORT).show()
            signInGoogle()
        }


    }




    private fun signInGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, Req_Code)
    }

    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Req_Code) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }// for google

        else {
            if (requestCode == Req_Codefb){
                callbackManager.onActivityResult(requestCode, resultCode, data)
            }
        }// for facebook
    }

    private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // this is where we update the UI after Google signin takes place
    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
//                SavedPreference.setEmail(this, account.email.toString())
//                SavedPreference.setUsername(this, account.displayName.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun signUpCheck() {
        if (eMails!!.text.toString().isEmpty() || !isValidEmail(eMails!!.text.toString())) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show()
            return
        } else if (passwordss!!.text.toString()
                .isEmpty() || !isValidPassword(passwordss!!.text.toString())
        ) {
            passwordss!!.error = " Password Must Contains " +
                    "\n Minimum of one lowercase alphabet. " +
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
        else {
            firebaseAuth.createUserWithEmailAndPassword(eMails!!.text.toString(), passwords01!!.text.toString()).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Successfully Singed Up", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Singed Up Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun logincheck() {

        if (eMail!!.text.toString().isEmpty() || !isValidEmail(eMail!!.text.toString()))
        {
            Toast.makeText(this, "Enter valid Email address and Password", Toast.LENGTH_SHORT).show()
        }
        else {
            firebaseAuth.signInWithEmailAndPassword(
                eMail!!.text.toString(),
                passwords!!.text.toString()
            ).addOnCompleteListener(this)
            {
                if (it.isSuccessful) {
                    val intent = Intent(this@login, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Successfully LoggedIn", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, "Log In failed ", Toast.LENGTH_SHORT).show()
            }
        }

        /* } else {
            // API Calls Start
          *//*  val loginss = MethodLogin.methodsinstance.loginData(
                eMail!!.text.toString(),
                passwords!!.text.toString()
            )
            loginss?.enqueue(object : Callback<Map<Any, Any>?> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<Map<Any, Any>?>,
                    response: Response<Map<Any, Any>?>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            Log.d("getchat", response.body().toString())
                            val gson = Gson()
                            val objrespose = gson.fromJson(
                                gson.toJson(response.body()),
                                LoginRequestModel::class.java
                            )
                            Log.d("TAG", "onResponse168:${objrespose}")
                            if (objrespose.status) {
                                Log.d("TAG", "onResponse169:${objrespose.status}")
                                val intent = Intent(this@login, MainActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(
                                    this@login,
                                    "Logged in Successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Log.d("TAG", "onResponse179:${objrespose.status}")
                                Toast.makeText(
                                    this@login,
                                    "Enter correct Email and Password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                this@login,
                                "Response null aa raha hai",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: JSONException) {
                        // Log.d("TAG", "response2>>" + response)
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<Map<Any, Any>?>, t: Throwable) {
                    Log.i("errorMessage", t.message!!)
                    Toast.makeText(this@login, t.message, Toast.LENGTH_SHORT).show()
                }
            })*//*

        }*/
        // calling signInWithEmailAndPassword(email, pass)
        // function using Firebase auth object
        // On successful response Display a Toast

    }
    }

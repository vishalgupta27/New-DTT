package com.appdtt.dtt

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    var drawer: DrawerLayout? = null
    var toolbar: Toolbar? = null
    var navigationView: NavigationView? = null

    // declare the GoogleSignInClient
    lateinit var mGoogleSignInClient: GoogleSignInClient

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.NavigationView)
        toolbar = findViewById(R.id.toolbar)



        // call requestIdToken as follows
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        logout.setOnClickListener {
//            mGoogleSignInClient.signOut().addOnCompleteListener {
//                val intent = Intent(this, MainActivity::class.java)
//                Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
//                startActivity(intent)
//                finish()
//            }
//        }


        setSupportActionBar(toolbar)
        toolbar!!.setNavigationOnClickListener { toggleDrawer() }

        val actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer, R.string.open_drawer, R.string.close_drawer)
        drawer!!.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()


        navigationView!!.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item1_home -> {
                    homeload()
                    true
                }
                R.id.item2_Logout -> {
                    mGoogleSignInClient.signOut().addOnCompleteListener {
                        val intent = Intent(this, login::class.java)
                        Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        finish()
                    }
                    true
                }
                R.id.item3_locations -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item4_UIWidgets -> {
                    val ui = Intent (this, com.appdtt.dtt.Andriod_UI_Widget::class.java)
                        startActivity(ui)
                    Toast.makeText(this, "Click Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.item5_Animations-> {
                    val anim = Intent (this, com.appdtt.dtt.AnimationWithOthers::class.java)
                    startActivity(anim)
                    true
                }
                R.id.item6_Settings-> {
                    val intent = Intent(this,ImagesSwitchers::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item7_AudioCapture-> {
                    val intent = Intent(this,AudioCaptures::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item8_Bluetooth-> {
                    val intent = Intent(this,BluetoothCheck::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item9_DBSqLite-> {
                    val intent = Intent(this, DbSqLite::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item10_Chat-> {
                    val intent = Intent(this, Chats::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item11_others-> {
                    Toast.makeText(this, "others", Toast.LENGTH_SHORT).show()
                    true
                }
                
                else -> {
                    drawer!!.closeDrawer(GravityCompat.START)
                    false
                }
            }
        }


    }

    private fun toggleDrawer() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            // If drawer is open, close it
            drawer!!.closeDrawer(GravityCompat.START)
        } else {
            // If drawer is closed, open it
            drawer!!.openDrawer(GravityCompat.START)
        }
    }


    override fun onBackPressed() {
        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed()
        }
    }



    private fun homeload() {
        // Create a new instance of your Fragment
        val myFragment = Home()

// Begin a FragmentTransaction
        val transaction = supportFragmentManager.beginTransaction()

// Replace the container view with the Fragment
        transaction.replace(R.id.container, myFragment)

// Add the transaction to the back stack (optional)
        transaction.addToBackStack(null)

// Commit the transaction
        transaction.commit()

    }

}

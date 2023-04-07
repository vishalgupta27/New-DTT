package com.example.dtt

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    var drawer: DrawerLayout? = null
    var toolbar: Toolbar? = null
    var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawer = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.NavigationView)
        toolbar = findViewById(R.id.toolbar)


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
                    val logout = Intent(this,login::class.java)
                    startActivity(logout)
                    true
                }
                R.id.item3_locations -> {
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.item4_Settings -> {
                    Toast.makeText(this, "Click Settings", Toast.LENGTH_SHORT).show()
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

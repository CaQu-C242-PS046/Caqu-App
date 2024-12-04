package com.budi.caquapplicaton

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.budi.caquapplication.R
import com.budi.caquapplicaton.regist.LoginPage
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView = findViewById(R.id.nav_view)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)


        NavigationUI.setupWithNavController(bottomNavigationView, navController)


        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)

        if (accessToken == null) {
            Log.d("MainActivity", "Token tidak ditemukan, kembali ke login.")
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            finish()
        } else {
            Log.d("MainActivity", "Token ditemukan: $accessToken")
        }
    }
}

package com.example.allfavour.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.allfavour.R

class ConsumerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer)

        val toolbar = findViewById<Toolbar>(R.id.consumer_toolbar)
        setSupportActionBar(toolbar)

        var hostContainer: NavHostFragment =supportFragmentManager
            .findFragmentById(R.id.consumer_nav_host_fragment) as NavHostFragment? ?: return

        val navController: NavController = hostContainer.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)

    }

    fun setupBottomAppBar(navController: NavController){

    }
}

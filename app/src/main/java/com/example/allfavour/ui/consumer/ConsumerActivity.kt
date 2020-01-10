package com.example.allfavour.ui.consumer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.allfavour.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.NavigationMenu
import com.google.android.material.navigation.NavigationView

class ConsumerActivity : AppCompatActivity() {
    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consumer_nav_activity)

        val toolbar = findViewById<Toolbar>(R.id.consumer_toolbar)
        setSupportActionBar(toolbar)

        var hostContainer: NavHostFragment =supportFragmentManager
            .findFragmentById(R.id.consumer_nav_host_fragment) as NavHostFragment? ?: return

        val navController: NavController = hostContainer.navController

        setupActionBar(navController)
        setupSideNavigationMenu(navController)
        setupBottomAppBar(navController)
    }

    fun setupActionBar(navController: NavController){
        val drawerLayout : DrawerLayout? = findViewById(R.id.consumer_drawer_layout)

        val homeDestinations = setOf(
            R.id.consumer_search_dest,
            R.id.consumer_my_favours_dest,
            R.id.consumer_my_interests_dest,
            R.id.consumer_profile_dest,
            R.id.consumer_messages_dest)

        appBarConfiguration = AppBarConfiguration(
            homeDestinations,
            drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun setupSideNavigationMenu(navController: NavController){
        // the drawer when the height is too small
        val sideNavView = findViewById<NavigationView>(R.id.consumer_nav_view)

        sideNavView?.setupWithNavController(navController)
    }

    fun setupBottomAppBar(navController: NavController){
        val bottomNav = findViewById<BottomNavigationView>(R.id.consumer_bottom_nav_view)

        bottomNav?.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // this is for the top menu overflow menu
        // Todo: we can put the switch and the notifacations in the drawer aswell
        val retValue = super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.consumer_top_menu, menu)

    // val navigationView = findViewById<NavigationView>(R.id.consumer_nav_view)

    // if (navigationView == null) {
    //     menuInflater.inflate(R.menu.consumer_drawer_nav, menu)
    //     return true
    // }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.
        return item.onNavDestinationSelected(findNavController(R.id.consumer_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.consumer_nav_host_fragment).navigateUp(appBarConfiguration)
    }
}

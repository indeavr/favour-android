package com.example.allfavour.ui.provider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.allfavour.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class ProviderActivityDeprecated : AppCompatActivity() {

    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.provider_nav_host)

        val toolbar = findViewById<Toolbar>(R.id.provider_toolbar)
        setSupportActionBar(toolbar)

        var hostContainer: NavHostFragment =supportFragmentManager
            .findFragmentById(R.id.provider_nav_host) as NavHostFragment? ?: return

        val navController: NavController = hostContainer.navController

//        var navGraph = findViewById<Navigation>()

        setupActionBar(navController)
        setupSideNavigationMenu(navController)
        setupBottomAppBar(navController)
    }

    fun setupActionBar(navController: NavController){
        val drawerLayout : DrawerLayout? = findViewById(R.id.provider_drawer_layout)

        val homeDestinations = setOf(
            R.id.provider_search_dest,
            R.id.provider_profile_dest,
            R.id.provider_messages_dest)

        appBarConfiguration = AppBarConfiguration(
            homeDestinations,
            drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun setupSideNavigationMenu(navController: NavController){
        // the drawer when the height is too small
        val sideNavView = findViewById<NavigationView>(R.id.provider_nav_view)

        sideNavView?.setupWithNavController(navController)
    }

    fun setupBottomAppBar(navController: NavController){
        val bottomNav = findViewById<BottomNavigationView>(R.id.provider_bottom_nav_view)

        bottomNav?.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // this is for the top menu overflow menu
        // Todo: we can put the switch and the notifacations in the drawer aswell
        val retValue = super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.provider_top_menu, menu)
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
        return item.onNavDestinationSelected(findNavController(R.id.provider_nav_host))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.provider_nav_host).navigateUp(appBarConfiguration)
    }
}

package com.example.allfavour.ui.consumer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.*
import com.example.allfavour.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class ConsumerActivityDeprecated : AppCompatActivity() {
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consumer_nav_host)

        val toolbar = findViewById<Toolbar>(R.id.consumer_toolbar)
        setSupportActionBar(toolbar)

        var hostContainer: NavHostFragment =supportFragmentManager
            .findFragmentById(R.id.consumer_nav_host) as NavHostFragment? ?: return

        this.navController = hostContainer.navController

        setupActionBar()
        setupSideNavigationMenu()
        setupBottomAppBar()
    }

    fun setupActionBar(){
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

        setupActionBarWithNavController(this.navController, appBarConfiguration)
    }

    fun setupSideNavigationMenu(){
        // the drawer when the height is too small
        val sideNavView = findViewById<NavigationView>(R.id.consumer_nav_view)

        sideNavView?.setupWithNavController(this.navController)
    }

    fun setupBottomAppBar(){
        val bottomNav = findViewById<BottomNavigationView>(R.id.consumer_bottom_nav_view)

        bottomNav?.setupWithNavController(this.navController)
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
        if(item.itemId === R.id.consumer_to_provider_dest){
            val currentDest = this.navController.currentDestination

            var bundle = bundleOf("currentDest" to "profile")
            navController.navigate(R.id.consumer_to_provider_dest, bundle)
        }

        if(item.itemId === R.id.consumer_notifications_dest){
            var options = navOptions {
                anim{
                    enter = R.anim.slide_in_up
                    exit = R.anim.slide_out_down
                    popEnter = R.anim.slide_in_up
                    popExit = R.anim.slide_out_down
                }
            }
            navController.navigate(R.id.consumer_notifications_dest, null, options)

            return super.onOptionsItemSelected(item)
        }

        return item.onNavDestinationSelected(findNavController(R.id.consumer_nav_host))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return findNavController(R.id.consumer_nav_host).navigateUp(appBarConfiguration)
    }
}

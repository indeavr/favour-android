package com.example.allfavour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.*
import com.example.allfavour.graphql.GraphqlConnector
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main_nav_activity.*

interface ToolbarSetupable {
    fun setToolbar(toolbar: Toolbar?)
}

val consumerFragments: List<Int> = listOf(
    R.id.consumer_search_dest,
    R.id.consumer_messages_dest,
    R.id.consumer_profile_dest,
    R.id.consumer_my_interests_dest,
    R.id.consumer_my_favours_dest,
    R.id.consumer_my_account_dest
)

val providerFragments: List<Int> = listOf(
    R.id.provider_profile_dest,
    R.id.provider_search_dest,
    R.id.provider_messages_dest,
    R.id.provider_my_account_button
)

class MainActivity : AppCompatActivity(), ToolbarSetupable {
//    lateinit var mainNavController: NavController
    lateinit var authNavController: NavController
//    lateinit var consumerNavController: NavController
//    lateinit var providerNavController: NavController

    lateinit var mainAppBarConfiguration: AppBarConfiguration
    lateinit var authAppBarConfiguration: AppBarConfiguration
    lateinit var consumerAppBarConfiguration: AppBarConfiguration
    lateinit var providerAppBarConfiguration: AppBarConfiguration
    lateinit var currentConfig: AppBarConfiguration

    val mainWrapper: FrameLayout by lazy { main_wrapper }
    val consumerWrapper: FrameLayout by lazy { consumer_wrapper }
    val providerWrapper: FrameLayout by lazy { provider_wrapper }

    val mainNavController: NavController by lazy { findNavController(R.id.main_nav_activity) }
    val navMainFragment: Fragment by lazy { main_nav_activity }
    val consumerNavController: NavController by lazy { findNavController(R.id.consumer_nav_host) }
    val navConsumerFragment: Fragment by lazy { consumer_nav_host }

    val providerNavController: NavController by lazy { findNavController(R.id.provider_nav_host) }
    val navProviderFragment: Fragment by lazy { provider_nav_host }

    var currentController: NavController? = null

    override fun setToolbar(toolbar: Toolbar?) {
        setSupportActionBar(toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_nav_activity)
        GraphqlConnector.setup(applicationContext)

//        val loginIntent = Intent(this, LoginActivity::class.java)
//        startActivity(loginIntent)

//        mainNavControllerInit()
        currentController = mainNavController

        mainNavController.addOnDestinationChangedListener { _, destination, _ ->
            var a = destination

            if(consumerFragments.contains(a.id)){
                currentController = consumerNavController

                mainWrapper.visibility = View.INVISIBLE
                consumerWrapper.visibility = View.VISIBLE
                providerWrapper.visibility = View.INVISIBLE

                val drawerLayout: DrawerLayout? = findViewById(R.id.consumer_drawer_layout)
                val homeDestinations = setOf(
                    R.id.consumer_search_dest,
                    R.id.consumer_my_favours_dest,
                    R.id.consumer_my_interests_dest,
                    R.id.consumer_profile_dest,
                    R.id.consumer_messages_dest
                )

                currentConfig = AppBarConfiguration(
                    homeDestinations,
                    drawerLayout
                )

                consumerAppBarConfiguration = AppBarConfiguration(
                    homeDestinations,
                    drawerLayout
                )

                val toolbar = findViewById<Toolbar>(R.id.consumer_toolbar)

                setSupportActionBar(toolbar)

                setupActionBarWithNavController(consumerNavController, consumerAppBarConfiguration)

                // the drawer when the height is too small
                val sideNavView = findViewById<NavigationView>(R.id.consumer_nav_view)

                sideNavView?.setupWithNavController(consumerNavController)

                val bottomNav = findViewById<BottomNavigationView>(R.id.consumer_bottom_nav_view)

                bottomNav?.setupWithNavController(consumerNavController)
            }
            else if (providerFragments.contains(a.id)){
                currentController = providerNavController

                mainWrapper.visibility = View.INVISIBLE
                consumerWrapper.visibility = View.INVISIBLE
                providerWrapper.visibility = View.VISIBLE

                val drawerLayout: DrawerLayout? = findViewById(R.id.provider_drawer_layout)
                val homeDestinations = setOf(
                    R.id.provider_search_dest,
                    R.id.provider_profile_dest,
                    R.id.provider_messages_dest
                )

                currentConfig = AppBarConfiguration(
                    homeDestinations,
                    drawerLayout
                )

                providerAppBarConfiguration = AppBarConfiguration(
                    homeDestinations,
                    drawerLayout
                )

                val toolbar = findViewById<Toolbar>(R.id.provider_toolbar)

                setSupportActionBar(toolbar)

                setupActionBarWithNavController(providerNavController, providerAppBarConfiguration)

                // the drawer when the height is too small
                val sideNavView = findViewById<NavigationView>(R.id.provider_nav_view)

                sideNavView?.setupWithNavController(providerNavController)

                val bottomNav = findViewById<BottomNavigationView>(R.id.provider_bottom_nav_view)

                bottomNav?.setupWithNavController(providerNavController)
            }
        }

//        authNavControllerInit()
//        consumerNavControllerInit()
//        providerNavControllerInit()

//        val consumerActivityIntent = Intent(this, ConsumerActivity::class.java)
//        startActivity(consumerActivityIntent);
    }

    fun setupControllerChanged() {
        consumerNavController.addOnDestinationChangedListener { _, destination, arguments ->
            val toolbar = findViewById<Toolbar>(R.id.consumer_toolbar)

            setSupportActionBar(toolbar)
        }
    }

//    fun mainNavControllerInit() {
//        var hostContainer: NavHostFragment = supportFragmentManager
//            .findFragmentById(R.id.main_nav_activity) as NavHostFragment? ?: return
//
//        mainNavController = hostContainer.navController
//
//        mainAppBarConfiguration = AppBarConfiguration(mainNavController.graph)
//    }

    fun authNavControllerInit() {
        var hostContainer: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.auth_nav_host) as NavHostFragment? ?: return

        authNavController = hostContainer.navController

        authAppBarConfiguration = AppBarConfiguration(authNavController.graph)
    }

//    fun consumerNavControllerInit() {
//        var hostContainer: NavHostFragment = supportFragmentManager
//            .findFragmentById(R.id.consumer_nav_host) as NavHostFragment? ?: return
//
//        consumerNavController = hostContainer.navController
//    }

//    fun providerNavControllerInit() {
//        var hostContainer: NavHostFragment = supportFragmentManager
//            .findFragmentById(R.id.provider_nav_host) as NavHostFragment? ?: return
//
//        providerNavController = hostContainer.navController
//
//        val drawerLayout: DrawerLayout? = findViewById(R.id.provider_drawer_layout)
//
//        val homeDestinations = setOf(
//            R.id.provider_search_dest,
//            R.id.provider_profile_dest,
//            R.id.provider_messages_dest
//        )
//
//        providerAppBarConfiguration = AppBarConfiguration(
//            homeDestinations,
//            drawerLayout
//        )
//
//        setupActionBarWithNavController(providerNavController, providerAppBarConfiguration)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Have the NavigationUI look for an action or destination matching the menu
        // item id and navigate there if found.
        // Otherwise, bubble up to the parent.

        if (item.itemId === R.id.consumer_notifications_dest) {
            var options = navOptions {
                anim {
                    enter = R.anim.slide_in_up
                    exit = R.anim.slide_out_down
                    popEnter = R.anim.slide_in_up
                    popExit = R.anim.slide_out_down
                }
            }
            consumerNavController.navigate(R.id.consumer_notifications_dest, null, options)

            return super.onOptionsItemSelected(item)
        }

        if(item.itemId === R.id.consumer_to_provider_dest){
            var currentDestination = currentController!!.currentDestination
            if(currentDestination!!.id == R.id.consumer_profile_dest){
                currentController = providerNavController

                mainWrapper.visibility = View.INVISIBLE
                consumerWrapper.visibility = View.INVISIBLE
                providerWrapper.visibility = View.VISIBLE

                val drawerLayout: DrawerLayout? = findViewById(R.id.provider_drawer_layout)
                val homeDestinations = setOf(
                    R.id.provider_search_dest,
                    R.id.provider_profile_dest,
                    R.id.provider_messages_dest
                )

                currentConfig = AppBarConfiguration(
                    homeDestinations,
                    drawerLayout
                )

                providerAppBarConfiguration = AppBarConfiguration(
                    homeDestinations,
                    drawerLayout
                )

                val toolbar = findViewById<Toolbar>(R.id.provider_toolbar)

                setSupportActionBar(toolbar)

                setupActionBarWithNavController(providerNavController, providerAppBarConfiguration)

                // the drawer when the height is too small
                val sideNavView = findViewById<NavigationView>(R.id.provider_nav_view)

                sideNavView?.setupWithNavController(providerNavController)

                val bottomNav = findViewById<BottomNavigationView>(R.id.provider_bottom_nav_view)

                bottomNav?.setupWithNavController(providerNavController)

                currentController!!.navigate(R.id.provider_profile_dest)
            }
            return super.onOptionsItemSelected(item)
        }

        return item.onNavDestinationSelected(findNavController(R.id.main_nav_activity))
                || super.onOptionsItemSelected(item)
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

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return currentController!!.navigateUp(currentConfig)
    }

    override fun onBackPressed() {
        currentController?.let { if (it.popBackStack().not()) finish() }
            ?: finish()
    }
}

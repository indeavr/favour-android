package com.example.allfavour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.*
import com.example.allfavour.graphql.GraphqlConnector
import com.example.allfavour.ui.WelcomeFragmentDirections
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.main_nav_activity.*

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

val authFragments: Set<Int> = setOf(R.id.login) // TODO

class MainActivity : AppCompatActivity() {

    private val mainWrapper: FrameLayout by lazy { main_wrapper }
    private val authWrapper: FrameLayout by lazy { auth_wrapper }
    private val consumerWrapper: FrameLayout by lazy { consumer_wrapper }
    private val providerWrapper: FrameLayout by lazy { provider_wrapper }

    private val mainNavController: NavController by lazy { findNavController(R.id.main_nav_activity) }
    private val authNavController: NavController by lazy { findNavController(R.id.auth_nav_host) }
    private val consumerNavController: NavController by lazy { findNavController(R.id.consumer_nav_host) }
    private val providerNavController: NavController by lazy { findNavController(R.id.provider_nav_host) }

    private lateinit var currentController: NavController
    private lateinit var currentConfig: AppBarConfiguration

    var currentSide: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_nav_activity)
        GraphqlConnector.setup(applicationContext)

        currentController = mainNavController

//        // TODO: review this approach -->
//        NavController.OnDestinationChangedListener { controller, destination, _ ->
//            if (controller.graph.id != currentController.graph.id) {
//                if (consumerFragments.contains(destination.id)) {
//                    activateConsumerNavigation()
//                    println("Destination changed to consumer")
//
//                } else if (providerFragments.contains(destination.id)) {
//                    activateProviderNavigation()
//                    println("Destination changed to provider")
//                }
//            }
//        }

        mainNavController.addOnDestinationChangedListener { _, destination, _ ->
            if (consumerFragments.contains(destination.id)) {
                activateConsumerNavigation()
                println("Destination changed to consumer")

            } else if (providerFragments.contains(destination.id)) {
                activateProviderNavigation()
                println("Destination changed to provider")
            }
        }

        if (true) { //logged in
            if (true) { // hasPassedBasicForms
                mainNavController.navigate(R.id.basic_info_form_dest)
                return
            }

            if (true) { // consumer
                activateConsumerNavigation()
                val action = WelcomeFragmentDirections.consumerDest()
                mainNavController.navigate(action)

            } else if (false) { //provider
                activateProviderNavigation()
                val action = WelcomeFragmentDirections.providerDest()
                mainNavController.navigate(action)
            }
        } else {
            activateAuthNavigation()
            // TODO: refactor these actions
            val action = WelcomeFragmentDirections.authDest()
            mainNavController.navigate(action)

            // when authenticated successfully and side chosen (either from previously saved or by choosing for the first time)
            // consumer/provider  Navigation must be activated --> use mainNavController to redirect, because the the listener is attached to it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Top Menu create
        val retValue = super.onCreateOptionsMenu(menu)

        if (currentSide == "provider") {
            menuInflater.inflate(R.menu.provider_top_menu, menu)
        } else {
            menuInflater.inflate(R.menu.consumer_top_menu, menu)
        }

        return retValue
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return currentController.navigateUp(currentConfig)
    }

    override fun onBackPressed() {
        currentController.let { if (it.popBackStack().not()) finish() }
            ?: finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var options = navOptions {
            anim {
                enter = R.anim.slide_in_up
                exit = R.anim.slide_out_down
                popEnter = R.anim.slide_in_up
                popExit = R.anim.slide_out_down
            }
        }

        return when (item.itemId) {
            R.id.consumer_notifications_dest -> {
                currentController.navigate(R.id.consumer_notifications_dest, null, options)

                return super.onOptionsItemSelected(item)
            }
            R.id.provider_notifications_dest -> {
                currentController.navigate(R.id.provider_notifications_dest, null, options)

                return super.onOptionsItemSelected(item)
            }
            R.id.consumer_to_provider_dest -> {
                activateProviderNavigation()

                var currentDestination = consumerNavController.currentDestination

                when (currentDestination?.id) {
                    R.id.consumer_search_dest -> {
                        providerNavController.navigate(R.id.provider_search_dest)
                    }
                    R.id.consumer_messages_dest -> providerNavController.navigate(R.id.provider_messages_dest)
                    R.id.consumer_profile_dest -> {
                        providerNavController.navigate(R.id.provider_profile_dest)
                    }
                }

                return item.onNavDestinationSelected(findNavController(R.id.main_nav_activity))
                        || super.onOptionsItemSelected(item)
            }
            R.id.provider_to_consumer_dest -> {
                activateConsumerNavigation()

                var currentDestination = providerNavController.currentDestination

                when (currentDestination?.id) {
                    R.id.provider_search_dest -> {
                        consumerNavController.navigate(R.id.consumer_search_dest)
                    }
                    R.id.provider_messages_dest -> consumerNavController.navigate(R.id.consumer_messages_dest)
                    R.id.provider_profile_dest -> {
                        consumerNavController.navigate(R.id.consumer_profile_dest)
                    }
                }

                return item.onNavDestinationSelected(findNavController(R.id.main_nav_activity))
                        || super.onOptionsItemSelected(item)
            }
            else -> item.onNavDestinationSelected(findNavController(R.id.main_nav_activity))
                    || super.onOptionsItemSelected(item)
        }
    }

    fun activateProviderNavigation() {
        currentSide = "provider"
        currentController = providerNavController

        mainWrapper.visibility = View.INVISIBLE
        authWrapper.visibility = View.INVISIBLE
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

        val toolbar = findViewById<Toolbar>(R.id.provider_toolbar)

        setSupportActionBar(toolbar)

        setupActionBarWithNavController(providerNavController, currentConfig)

        // the drawer when the height is too small
        val sideNavView = findViewById<NavigationView>(R.id.provider_nav_view)

        sideNavView?.setupWithNavController(providerNavController)

        val bottomNav = findViewById<BottomNavigationView>(R.id.provider_bottom_nav_view)

        bottomNav?.setupWithNavController(providerNavController)
    }

    fun activateConsumerNavigation() {
        currentSide = "consumer"
        currentController = consumerNavController

        mainWrapper.visibility = View.INVISIBLE
        authWrapper.visibility = View.INVISIBLE
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

        val toolbar = findViewById<Toolbar>(R.id.consumer_toolbar)

        setSupportActionBar(toolbar)

        setupActionBarWithNavController(consumerNavController, currentConfig)

        // the drawer when the height is too small
        val sideNavView = findViewById<NavigationView>(R.id.consumer_nav_view)

        sideNavView?.setupWithNavController(consumerNavController)

        val bottomNav = findViewById<BottomNavigationView>(R.id.consumer_bottom_nav_view)

        bottomNav?.setupWithNavController(consumerNavController)
    }

    fun activateAuthNavigation() {
        currentSide = null
        currentController = authNavController

        mainWrapper.visibility = View.INVISIBLE
        authWrapper.visibility = View.VISIBLE
        consumerWrapper.visibility = View.INVISIBLE
        providerWrapper.visibility = View.INVISIBLE

        currentConfig = AppBarConfiguration(authNavController.graph)

//        setupActionBarWithNavController(authNavController, currentConfig)

        // the drawer when the height is too small
    }
}

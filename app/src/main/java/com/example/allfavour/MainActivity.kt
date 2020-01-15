package com.example.allfavour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.viewpager.widget.ViewPager
import com.example.allfavour.graphql.GraphqlConnector
import com.example.allfavour.ui.consumer.ConsumerBaseFragment
import com.example.allfavour.ui.provider.ProviderBaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_nav_activity.*
import java.util.*
import android.content.Intent


val cFragments: List<Int> = listOf(
    R.id.consumer_search_dest,
    R.id.consumer_messages_dest,
    R.id.consumer_profile_dest,
    R.id.consumer_my_interests_dest,
    R.id.consumer_my_favours_dest,
    R.id.consumer_my_account_dest
)

val pFragments: List<Int> = listOf(
    R.id.provider_profile_dest,
    R.id.provider_search_dest,
    R.id.provider_messages_dest,
    R.id.provider_my_account_button
)

val authFragments: Set<Int> = setOf(R.id.login) // TODO

interface WithBottomNavigationSwitcher {
    fun switchToNotificaitons()
}

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),
    ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener,
    WithBottomNavigationSwitcher {
    // list of base destination containers
    private val consumerFragments = listOf(
        ConsumerBaseFragment.newInstance(
            R.layout.consumer_search_nav_host,
            R.id.consumer_search_toolbar,
            R.id.consumer_search_nav_host
        ),
        ConsumerBaseFragment.newInstance(
            R.layout.consumer_profile_nav_host,
            R.id.consumer_profile_toolbar,
            R.id.consumer_profile_nav_host
        ),
        ConsumerBaseFragment.newInstance(
            R.layout.consumer_notifications_nav_host,
            R.id.consumer_notifications_toolbar,
            R.id.consumer_notifications_nav_host
        )
    )

    private val providerFragments = listOf(
        ProviderBaseFragment.newInstance(
            R.layout.provider_search_nav_host,
            R.id.provider_search_toolbar,
            R.id.provider_search_nav_host
        ),
        ProviderBaseFragment.newInstance(
            R.layout.provider_profile_nav_host,
            R.id.provider_profile_toolbar,
            R.id.provider_profile_nav_host
        )
    )

    // overall back stack of containers
    private val backStack = Stack<Int>()

    // maps of navigation_id to container index
    private val indexToConsumerPage =
        mapOf(
            0 to R.id.consumer_search_dest,
            1 to R.id.consumer_profile_dest,
            2 to R.id.consumer_notifications_dest
        )
    private val consumerPageToIndex =
        mapOf(
            R.id.consumer_search_dest to 0,
            R.id.consumer_profile_dest to 1,
            R.id.consumer_notifications_dest to 2
        )


    private val indexToProviderPage =
        mapOf(0 to R.id.provider_search_dest, 1 to R.id.provider_profile_dest)

    private val providerPageToIndex =
        mapOf(R.id.provider_search_dest to 0, R.id.provider_profile_dest to 1)

    private val mainWrapper: FrameLayout by lazy { main_wrapper }
    //    private val authWrapper: FrameLayout by lazy { auth_wrapper }
    private val consumerWrapper: FrameLayout by lazy { consumer_wrapper }
    private val providerWrapper: FrameLayout by lazy { provider_wrapper }

    private val mainNavController: NavController by lazy { findNavController(R.id.main_nav_activity) }

    private lateinit var currentController: NavController
    private lateinit var currentConfig: AppBarConfiguration

    var currentSide: String? = null

    val hasStartedFromAWebDeepLink: Boolean by lazy { intent.action == Intent.ACTION_VIEW && intent.data != null }
    val hasStartedFromAPendingDeepLink: Boolean by lazy { intent.action == null && intent.data == null }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_nav_activity)
        GraphqlConnector.setup(applicationContext)

        currentController = mainNavController
        intent.action == Intent.ACTION_VIEW
        mainNavController.addOnDestinationChangedListener { _, destination, _ ->
            if (cFragments.contains(destination.id)) {
                activateConsumerNavigation(destination.id)

            } else if (pFragments.contains(destination.id)) {
                activateProviderNavigation(destination.id)
            }
        }

        if (true) { //logged in
            if (false) { // hasPassedBasicForms
                mainNavController.navigate(R.id.basic_info_form_dest)
                return
            }

            if (true)  // consumer
                activateConsumerNavigation()
            else if (true) //provider
                activateProviderNavigation()

            navigateToCorrectDestination()
        } else {
            activateAuthNavigation()
//             TODO: refactor these actions
            val action = MainNavigationDirections.authNavigationDest()
            mainNavController.navigate(action)

            // when authenticated successfully and side chosen (either from previously saved or by choosing for the first time)
            // consumer/provider  Navigation must be activated --> use mainNavController to redirect, because the the listener is attached to it
        }


        // initialize backStack with home page index
        if (backStack.empty()) backStack.push(0)
    }

    fun navigateToCorrectDestination() {
        if (hasStartedFromAWebDeepLink || hasStartedFromAPendingDeepLink) {
            return // Navigation will be handled automagically by the NavController (it redirected / forced a relaunch of the app)
        }

        if (currentSide == "consumer") {
            val action = MainNavigationDirections.consumerSearchDest()
            mainNavController.navigate(action)
        } else {
            val action = MainNavigationDirections.providerSearchDest()
            mainNavController.navigate(action)
        }
    }


    inner class ViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment {
            return if (currentSide == "provider") providerFragments[position]
            else consumerFragments[position]
        }

        override fun getCount(): Int {
            return if (currentSide == "provider") providerFragments.size
            else consumerFragments.size
        }

    }

//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Top Menu create
//        val retValue = super.onCreateOptionsMenu(menu)
//
//        if (currentSide == "provider") {
//            menuInflater.inflate(R.menu.provider_top_menu, menu)
//        } else {
//            menuInflater.inflate(R.menu.consumer_top_menu, menu)
//        }
//
//        return retValue
//    }

//    override fun onSupportNavigateUp(): Boolean {
//        // Allows NavigationUI to support proper up navigation or the drawer layout
//        // drawer menu, depending on the situation
//        return currentController.navigateUp(currentConfig)
//    }

//    override fun onBackPressed() {
//        currentController.let { if (it.popBackStack().not()) finish() }
//            ?: finish()
//    }

//    override fun onBackPressed() {
//        if (backStack.size > 1) {
//            // remove current position from stack
//            backStack.pop()
//            // set the next item in stack as current
//            main_pager.currentItem = backStack.peek()
//
//        } else super.onBackPressed()
//    }

    override fun onBackPressed() {
        if (currentSide == "provider") {
            val fragment = providerFragments[provider_pager.currentItem]
            // check if the page navigates up
            val navigatedUp = fragment.onBackPressed()
            // if no fragments were popped
            if (!navigatedUp) {
                if (backStack.size > 1) {
                    // remove current position from stack
                    backStack.pop()
                    // set the next item in stack as current
                    provider_pager.currentItem = backStack.peek()

                } else super.onBackPressed()
            }
        } else {
            val fragment = consumerFragments[consumer_pager.currentItem]
            // check if the page navigates up
            val navigatedUp = fragment.onBackPressed()
            // if no fragments were popped
            if (!navigatedUp) {
                if (backStack.size > 1) {
                    // remove current position from stack
                    backStack.pop()
                    // set the next item in stack as current
                    consumer_pager.currentItem = backStack.peek()

                } else super.onBackPressed()
            }
        }
    }

    /// BottomNavigationView ItemSelected Implementation
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (currentSide == "provider") {
            val position = indexToProviderPage.values.indexOf(item.itemId)

            if (provider_pager.currentItem != position) setItem(position)
        } else {
            val position = indexToConsumerPage.values.indexOf(item.itemId)

            if (consumer_pager.currentItem != position) setItem(position)
        }

        return true
    }

    /// OnPageSelected Listener Implementation
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

    override fun onPageSelected(page: Int) {
        if (currentSide == "provider") {
            val itemId = indexToProviderPage[page] ?: R.id.provider_search_dest
            if (provider_bottom_nav_view.selectedItemId != itemId)
                provider_bottom_nav_view.selectedItemId = itemId
        } else {
            val itemId = indexToConsumerPage[page] ?: R.id.consumer_search_dest
            if (consumer_bottom_nav_view.selectedItemId != itemId)
                consumer_bottom_nav_view.selectedItemId = itemId
        }
    }


    private fun setItem(position: Int) {
        if (currentSide == "provider") provider_pager.currentItem = position
        else consumer_pager.currentItem = position

        backStack.push(position)
    }


    fun activateProviderNavigation(destination: Int = -1) {
        currentSide = "provider"
//        currentController = providerNavController

        val a = destination == R.id.provider_profile_dest
        val b = destination == R.id.provider_my_account_dest
        val c = destination == R.id.provider_profile_nav_host

        mainWrapper.visibility = View.INVISIBLE
//        authWrapper.visibility = View.INVISIBLE
        consumerWrapper.visibility = View.INVISIBLE
        providerWrapper.visibility = View.VISIBLE

        backStack.pop()

        val currentItem = providerPageToIndex[destination] ?: 0

        // force viewPager to create all fragments
        provider_pager.offscreenPageLimit = providerFragments.size

        consumer_pager.adapter = null
        consumer_pager.currentItem = -1
        // setup view pager
        provider_pager.adapter = ViewPagerAdapter()
        setItem(currentItem)

        provider_bottom_nav_view.selectedItemId = destination

        consumer_pager.removeOnPageChangeListener(this)
        provider_pager.addOnPageChangeListener(this)

        // check deeplink only after viewPager is setup
        provider_pager.post(this::checkDeepLink)

        provider_bottom_nav_view.setOnNavigationItemSelectedListener(this)

//
//        val toolbar = findViewById<Toolbar>(R.id.provider_toolbar)
//
//        setSupportActionBar(toolbar)
//
//        setupActionBarWithNavController(providerNavController, currentConfig)
//
//        // the drawer when the height is too small
//        val sideNavView = findViewById<NavigationView>(R.id.provider_nav_view)
//
//        sideNavView?.setupWithNavController(providerNavController)
//
//        val bottomNav = findViewById<BottomNavigationView>(R.id.provider_bottom_nav_view)
//
//        bottomNav?.setupWithNavController(providerNavController)
    }


    fun activateConsumerNavigation(destination: Int = -1) {
        currentSide = "consumer"

        mainWrapper.visibility = View.INVISIBLE
//        authWrapper.visibility = View.INVISIBLE
        consumerWrapper.visibility = View.VISIBLE
        providerWrapper.visibility = View.INVISIBLE

        val a = destination == R.id.consumer_profile_dest
        val b = destination == R.id.consumer_my_account_dest
        val c = destination == R.id.consumer_profile_nav_host

        val currentItem = consumerPageToIndex[destination] ?: 0

        // force viewPager to create all fragments
        consumer_pager.offscreenPageLimit = consumerFragments.size

        // setup view pager
        provider_pager.adapter = null
        provider_pager.currentItem = -1
        provider_pager.removeOnPageChangeListener(this)

        consumer_pager.adapter = ViewPagerAdapter()
        setItem(currentItem)
        consumer_bottom_nav_view.selectedItemId = destination

        consumer_pager.addOnPageChangeListener(this)


        // check deeplink only after viewPager is setup
        val success = consumer_pager.post(this::checkDeepLink)

        consumer_bottom_nav_view.setOnNavigationItemSelectedListener(this)

//        val toolbar = findViewById<Toolbar>(R.id.consumer_toolbar)
//
//        setSupportActionBar(toolbar)

//        setupActionBarWithNavController(consumerNavController, currentConfig)
//
//        // the drawer when the height is too small
//        val sideNavView = findViewById<NavigationView>(R.id.consumer_nav_view)
//
//        sideNavView?.setupWithNavController(consumerNavController)
//
//        val bottomNav = findViewById<BottomNavigationView>(R.id.consumer_bottom_nav_view)
////
//        bottomNav?.setupWithNavController(consumerNavController)
    }

    fun activateAuthNavigation() {
        currentSide = null
//        currentController = authNavController

        mainWrapper.visibility = View.INVISIBLE
//        authWrapper.visibility = View.VISIBLE
        consumerWrapper.visibility = View.INVISIBLE
        providerWrapper.visibility = View.INVISIBLE

//        currentConfig = AppBarConfiguration(authNavController.graph)

//        setupActionBarWithNavController(authNavController, currentConfig)

        // the drawer when the height is too small
    }

    fun BottomNavigationView.uncheckAllItems() {
        menu.setGroupCheckable(0, true, false)
        for (i in 0 until menu.size()) {
            menu.getItem(i).isChecked = false
        }
        menu.setGroupCheckable(0, true, true)
    }

    override fun switchToNotificaitons() {
        if (currentSide == "consumer") {
            setItem(2)
            consumer_bottom_nav_view.uncheckAllItems()
        } else {

        }
    }

    private fun checkDeepLink() {
        if (currentSide == "consumer") {
            consumerFragments.forEachIndexed { index, fragment ->
                val hasDeepLink = fragment.handleDeepLink(intent)
                if (hasDeepLink) setItem(index)
            }
        } else {
            providerFragments.forEachIndexed { index, fragment ->
                val hasDeepLink = fragment.handleDeepLink(intent)
                if (hasDeepLink) setItem(index)
            }
        }
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        if (currentSide == "consumer") {
            val position = indexToConsumerPage.values.indexOf(item.itemId)
            val fragment = consumerFragments[position]
            fragment.popToRoot()
        } else {
            val position = indexToProviderPage.values.indexOf(item.itemId)
            val fragment = providerFragments[position]
            fragment.popToRoot()
        }
    }
}

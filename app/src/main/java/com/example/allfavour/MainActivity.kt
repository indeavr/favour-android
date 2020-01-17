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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.navigation.navOptions


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
    fun setToolbar(toolbar: Toolbar)
    fun switchToProvider(destination: Int?)
    fun switchToConsumer(destination: Int?)
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
            R.layout.consumer_my_favours_nav_host,
            R.id.consumer_my_favours_toolbar,
            R.id.consumer_my_favours_nav_host
        ),
        ConsumerBaseFragment.newInstance(
            R.layout.consumer_my_interests_nav_host,
            R.id.consumer_my_interests_toolbar,
            R.id.consumer_my_interests_nav_host
        ),
        ConsumerBaseFragment.newInstance(
            R.layout.consumer_search_nav_host,
            R.id.consumer_search_toolbar,
            R.id.consumer_search_nav_host
        ),
        ConsumerBaseFragment.newInstance(
            R.layout.consumer_messages_nav_host,
            R.id.consumer_messages_toolbar,
            R.id.consumer_messages_nav_host
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
            0 to R.id.consumer_my_favours_dest,
            1 to R.id.consumer_my_interests_dest,
            2 to R.id.consumer_search_dest,
            3 to R.id.consumer_messages_dest,
            4 to R.id.consumer_profile_dest,
            5 to R.id.consumer_notifications_dest
        )
    private val consumerPageToIndex =
        mapOf(
            R.id.consumer_my_favours_dest to 0,
            R.id.consumer_my_interests_dest to 1,
            R.id.consumer_search_dest to 2,
            R.id.consumer_messages_dest to 3,
            R.id.consumer_profile_dest to 4,
            R.id.consumer_notifications_dest to 5
        )


    private val indexToProviderPage =
        mapOf(0 to R.id.provider_search_dest, 1 to R.id.provider_profile_dest)

    private val providerPageToIndex =
        mapOf(R.id.provider_search_dest to 0, R.id.provider_profile_dest to 1)

    private val mainWrapper: FrameLayout by lazy { main_wrapper }
    //    private val authWrapper: FrameLayout by lazy { auth_wrapper }
    private val consumerWrapper: FrameLayout by lazy { consumer_wrapper }
    private val providerWrapper: FrameLayout by lazy { provider_wrapper }

//    private val mainNavController: NavController by lazy { findNavController(R.id.main_nav_activity) }

    private lateinit var currentConfig: AppBarConfiguration

    var currentSide: String? = null

    val hasStartedFromAWebDeepLink: Boolean by lazy { intent.action == Intent.ACTION_VIEW && intent.data != null }
    val hasStartedFromAPendingDeepLink: Boolean by lazy { intent.action == null && intent.data == null }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_nav_activity)
        GraphqlConnector.setup(applicationContext)

        // initialize backStack with home page index
        if (backStack.empty()) backStack.push(0)

        if (true) { //logged in
            if (false) { // hasPassedBasicForms
                return
            }

            if (true)  // consumer
                switchToConsumer(null)
            else if (true) //provider
                switchToProvider(null)

        } else {
            activateAuthNavigation()
//             TODO: refactor these actions
//            val action = MainNavigationDirections.authNavigationDest()
//            mainNavController.navigate(action)

            // when authenticated successfully and side chosen (either from previously saved or by choosing for the first time)
            // consumer/provider  Navigation must be activated --> use mainNavController to redirect, because the the listener is attached to it
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
                    val index = backStack.peek()
                    val currentPage = indexToProviderPage[index]

                    // because when getting back from notifications, which is not part of the bottom_nav it doest get set automatically
                    provider_pager.currentItem = index
                    if (currentPage != null) {
                        provider_bottom_nav_view.selectedItemId = currentPage
                    }
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
                    val index = backStack.peek()
                    val currentPage = indexToConsumerPage[index]
                    // because when getting back from notifications, which is not part of the bottom_nav it doest get set automatically
                    consumer_pager.currentItem = index
                    if (currentPage != null) {
                        consumer_bottom_nav_view.selectedItemId = currentPage
                    }

                } else super.onBackPressed()
            }
        }
    }

    /// BottomNavigationView
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


    override fun onNavigationItemReselected(item: MenuItem) {
        if (currentSide == "consumer") {
            val position = indexToConsumerPage.values.indexOf(item.itemId)

            if (!item.isChecked) {
                setItem(position)
                item.isChecked = true
                return
            }
            val fragment = consumerFragments[position]
            fragment.popToRoot()
        } else {
            val position = indexToProviderPage.values.indexOf(item.itemId)

            if (!item.isChecked) {
                setItem(position)
                item.isChecked = true
                return
            }

            val fragment = providerFragments[position]
            fragment.popToRoot()
        }
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
//            val options = navOptions {
//                anim {
//                    enter = R.anim.slide_in_up
//                    exit = R.anim.slide_out_down
//                    popEnter = R.anim.slide_in_up
//                    popExit = R.anim.slide_out_down
//                }
//            }
//            mainNavController.navigate(
//                MainNavigationDirections.consumerNotificationsDest(),
//                options
//            )
            val position = consumerPageToIndex[R.id.consumer_notifications_dest]
            setItem(position!!)
            consumer_bottom_nav_view.uncheckAllItems()
        } else {
            // Todo
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

    override fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun switchToConsumer(destination: Int?) {
        currentSide = "consumer"

        mainWrapper.visibility = View.INVISIBLE
//        authWrapper.visibility = View.INVISIBLE
        consumerWrapper.visibility = View.VISIBLE
        providerWrapper.visibility = View.INVISIBLE

        val currentItem =
            consumerPageToIndex[destination] ?: consumerPageToIndex[R.id.consumer_search_dest]!!

        // force viewPager to create all fragments
        consumer_pager.offscreenPageLimit = consumerFragments.size

        // setup view pager
        provider_pager.adapter = null
        provider_pager.currentItem = -1
        provider_pager.removeOnPageChangeListener(this)

        consumer_pager.adapter = ViewPagerAdapter()
        setItem(currentItem)

        consumer_bottom_nav_view.selectedItemId = destination ?: R.id.consumer_search_dest

        consumer_pager.addOnPageChangeListener(this)

        // check deeplink only after viewPager is setup
        consumer_pager.post(this::checkDeepLink)

        consumer_bottom_nav_view.setOnNavigationItemSelectedListener(this)
        consumer_bottom_nav_view.setOnNavigationItemReselectedListener(this)
    }

    override fun switchToProvider(destination: Int?) {
        currentSide = "provider"

        mainWrapper.visibility = View.INVISIBLE
//        authWrapper.visibility = View.INVISIBLE
        consumerWrapper.visibility = View.INVISIBLE
        providerWrapper.visibility = View.VISIBLE

        backStack.pop()

        val currentItem = providerPageToIndex[destination] ?: 0

        // force viewPager to create all fragments
        provider_pager.offscreenPageLimit = providerFragments.size

        // clear consumer
        consumer_pager.adapter = null
        consumer_pager.currentItem = -1
        consumer_pager.removeOnPageChangeListener(this)

        // setup view pager
        provider_pager.adapter = ViewPagerAdapter()
        setItem(currentItem)
        provider_pager.addOnPageChangeListener(this)

        // check deeplink only after viewPager is setup
        provider_pager.post(this::checkDeepLink)

        provider_bottom_nav_view.selectedItemId = destination ?: R.id.provider_search_dest

        provider_bottom_nav_view.setOnNavigationItemSelectedListener(this)
        provider_bottom_nav_view.setOnNavigationItemReselectedListener(this)
    }
}

package com.example.allfavour

import android.accounts.Account
import android.accounts.AccountManager
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
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_nav_activity.*
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.navOptions
import com.example.allfavour.ui.consumer.ConsumerBaseFragment
import com.example.allfavour.ui.provider.ProviderBaseFragment
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import androidx.lifecycle.ViewModelProviders
import com.example.allfavour.ui.BasicInfoFormFragment
import com.example.allfavour.data.model.LoggedUser
import com.example.allfavour.data.model.PermissionsModel
import com.example.allfavour.ui.auth.AuthViewModelFactory
import com.example.allfavour.ui.auth.AuthenticationViewModel
import java.util.*


val pFragments: List<Int> = listOf(
    R.id.provider_search_dest,
    R.id.provider_messages_dest,
    R.id.provider_profile_dest,
    R.id.provider_my_interests_dest,
    R.id.provider_my_favours_dest,
    R.id.provider_my_account_dest
)

val coFragments: List<Int> = listOf(
    R.id.consumer_profile_dest,
    R.id.consumer_search_dest,
    R.id.consumer_messages_dest,
    R.id.consumer_my_account_button
)

val authFragments: Set<Int> = setOf(R.id.login) // TODO

interface DecoratedActivity {
    fun switchToNotificaitons()
    fun setToolbar(toolbar: Toolbar)
    fun handleGoogleLogin(account: GoogleSignInAccount?)
    fun toggleBottomNavVisibility(show: Boolean)
    fun navigateToConsumerOrProvider(side: String?)
    fun activateAuthNavigation()
}

class MainActivity : AppCompatActivity(),
    ViewPager.OnPageChangeListener,
    BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener,
    DecoratedActivity {
    // list of base destination containers
    // TODO lazy load
    private val providerFragments = listOf(
        ProviderBaseFragment.newInstance(
            R.layout.provider_my_favours_nav_host,
            R.id.provider_my_favours_toolbar,
            R.id.provider_my_favours_nav_host
        ),
        ProviderBaseFragment.newInstance(
            R.layout.provider_my_interests_nav_host,
            R.id.provider_my_interests_toolbar,
            R.id.provider_my_interests_nav_host
        ),
        ProviderBaseFragment.newInstance(
            R.layout.provider_search_nav_host,
            R.id.provider_search_toolbar,
            R.id.provider_search_nav_host
        ),
        ProviderBaseFragment.newInstance(
            R.layout.provider_messages_nav_host,
            R.id.provider_messages_toolbar,
            R.id.provider_messages_nav_host
        ),
        ProviderBaseFragment.newInstance(
            R.layout.provider_profile_nav_host,
            R.id.provider_profile_toolbar,
            R.id.provider_profile_nav_host
        ),
        ProviderBaseFragment.newInstance(
            R.layout.provider_notifications_nav_host,
            R.id.provider_notifications_toolbar,
            R.id.provider_notifications_nav_host
        )
    )

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
        )
    )

    // overall back stack of containers
    private val backStack = Stack<Int>()

    // maps of navigation_id to container index
    private val indexToProviderPage =
        mapOf(
            0 to R.id.provider_my_favours_dest,
            1 to R.id.provider_my_interests_dest,
            2 to R.id.provider_search_dest,
            3 to R.id.provider_messages_dest,
            4 to R.id.provider_profile_dest,
            5 to R.id.provider_notifications_dest
        )
    private val providerPageToIndex =
        mapOf(
            R.id.provider_my_favours_dest to 0,
            R.id.provider_my_interests_dest to 1,
            R.id.provider_search_dest to 2,
            R.id.provider_messages_dest to 3,
            R.id.provider_profile_dest to 4,
            R.id.provider_notifications_dest to 5
        )


    private val indexToConsumerage =
        mapOf(0 to R.id.consumer_search_dest, 1 to R.id.consumer_profile_dest)

    private val consumerToIndex =
        mapOf(R.id.consumer_search_dest to 0, R.id.consumer_profile_dest to 1)

    private val mainWrapper: FrameLayout by lazy { main_wrapper }
    private val authWrapper: FrameLayout by lazy { auth_wrapper }
    private val providerWrapper: FrameLayout by lazy { provider_wrapper }
    private val consumerWrapper: FrameLayout by lazy { consumer_wrapper }

    private val mainNavController: NavController by lazy { findNavController(R.id.main_nav_activity) }
    private lateinit var accountManager: AccountManager

    private lateinit var currentConfig: AppBarConfiguration

    var currentSide: String? = null

    val hasStartedFromAWebDeepLink: Boolean by lazy { intent.action == Intent.ACTION_VIEW && intent.data != null }
    val hasStartedFromAPendingDeepLink: Boolean by lazy { intent.action == null && intent.data == null }

    private lateinit var auth: FirebaseAuth
    private lateinit var authViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_nav_activity)
        GraphqlConnector.setup(applicationContext)

        authViewModel = ViewModelProviders.of(this, AuthViewModelFactory())
            .get(AuthenticationViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        accountManager = AccountManager.get(this)

        val currentUserId = AuthenticationProvider.getUserId(this)
        if (currentUserId != null) {
            authViewModel.userId = currentUserId
        }

        // initialize backStack with home page index
        if (backStack.empty()) backStack.push(0)

        mainNavController.addOnDestinationChangedListener { _, destination, _ ->
            if (pFragments.contains(destination.id)) {
                activateProviderNavigation(destination.id)

            } else if (coFragments.contains(destination.id)) {
                activateConsumerNavigation(destination.id)
            }
        }

        val authToken = AuthenticationProvider.getAuthToken(this.applicationContext)

        if (hasStartedFromAWebDeepLink || hasStartedFromAPendingDeepLink) {
            return // Navigation will be handled automagically by the NavController (it redirected / forced a relaunch of the app)
        }

        // TODO: this flow is not relevant anymore (replaced by @navigateToConsumerOrProvider)
        if (authToken != null) { //logged in
            if (true) { // hasPassedBasicForms
//                mainNavController.navigate(R.id.basic_info_form_dest)
                // return
            }

            if (true)  // provider
                activateProviderNavigation()
            else if (true) //consumer
                activateConsumerNavigation()

            navigateToCorrectDestination()
        } else {
            activateAuthNavigation()
//             TODO: refactor these actions
            val action = MainNavigationDirections.authNavigationDest()
            mainNavController.navigate(action)

            // when authenticated successfully and side chosen (either from previously saved or by choosing for the first time)
            // provider/consumer  Navigation must be activated --> use mainNavController to redirect, because the the listener is attached to it
        }
    }

    fun navigateToCorrectDestination() {
        if (hasStartedFromAWebDeepLink || hasStartedFromAPendingDeepLink) {
            return // Navigation will be handled automagically by the NavController (it redirected / forced a relaunch of the app)
        }

        if (currentSide == "provider") {
            val action = MainNavigationDirections.providerSearchDest()
            mainNavController.navigate(action)
        } else {
            val action = MainNavigationDirections.consumerSearchDest()
            mainNavController.navigate(action)
        }
    }


    inner class ViewPagerAdapter : FragmentPagerAdapter(supportFragmentManager) {

        override fun getItem(position: Int): Fragment {
            return if (currentSide == "consumer") {
                val fragment = supportFragmentManager.findFragmentByTag(position.toString())
                fragment ?: consumerFragments[position]
            } else {
                val fragment = supportFragmentManager.findFragmentByTag(position.toString())
                fragment ?: providerFragments[position]

            }
        }

        override fun getCount(): Int {
            return if (currentSide == "consumer") consumerFragments.size
            else providerFragments.size
        }
    }

    override fun onBackPressed() {
        if (currentSide == "consumer") {
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
                    val currentPage = indexToConsumerage[index]

                    // because when getting back from notifications, which is not part of the bottom_nav it doest get set automatically
                    consumer_pager.currentItem = index
                    if (currentPage != null) {
                        consumer_bottom_nav_view.selectedItemId = currentPage
                    }
                } else super.onBackPressed()
            }
        } else {
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
        }
    }

    /// BottomNavigationView
    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        if (currentSide == "consumer") {
            val position = indexToConsumerage.values.indexOf(item.itemId)

            if (consumer_pager.currentItem != position) setItem(position)
        } else {
            provider_bottom_nav_view.visibility = View.VISIBLE
            val position = indexToProviderPage.values.indexOf(item.itemId)

            if (provider_pager.currentItem != position) setItem(position)
        }

        return true
    }


    override fun onNavigationItemReselected(item: MenuItem) {
        if (currentSide == "provider") {
            val position = indexToProviderPage.values.indexOf(item.itemId)

            if (!item.isChecked) {
                setItem(position)
                item.isChecked = true
                return
            }
            val fragment = providerFragments[position]
            fragment.popToRoot()
        } else {
            val position = indexToConsumerage.values.indexOf(item.itemId)

            if (!item.isChecked) {
                setItem(position)
                item.isChecked = true
                return
            }

            val fragment = consumerFragments[position]
            fragment.popToRoot()
        }
    }

    /// OnPageSelected Listener Implementation
    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

    override fun onPageSelected(page: Int) {
        if (currentSide == "consumer") {
            val itemId = indexToConsumerage[page] ?: R.id.consumer_search_dest
            if (consumer_bottom_nav_view.selectedItemId != itemId)
                consumer_bottom_nav_view.selectedItemId = itemId
        } else {
            val itemId = indexToProviderPage[page] ?: R.id.provider_search_dest
            if (provider_bottom_nav_view.selectedItemId != itemId)
                provider_bottom_nav_view.selectedItemId = itemId
        }
    }

    private fun setItem(position: Int) {
        if (currentSide == "consumer") consumer_pager.currentItem = position
        else provider_pager.currentItem = position

        backStack.push(position)
    }

    fun activateConsumerNavigation(destination: Int = -1) {
        currentSide = "consumer"

        mainWrapper.visibility = View.INVISIBLE
        authWrapper.visibility = View.INVISIBLE
        providerWrapper.visibility = View.INVISIBLE
        consumerWrapper.visibility = View.VISIBLE

        backStack.pop()

        val currentItem = consumerToIndex[destination] ?: 0

        // force viewPager to create all fragments
        consumer_pager.offscreenPageLimit = consumerFragments.size

        // clear provider
        provider_pager.adapter = null
        provider_pager.currentItem = -1
        provider_pager.removeOnPageChangeListener(this)

        // setup view pager
        consumer_pager.adapter = ViewPagerAdapter()
        setItem(currentItem)
        consumer_pager.addOnPageChangeListener(this)

        // check deeplink only after viewPager is setup
        consumer_pager.post(this::checkDeepLink)

        if (consumer_bottom_nav_view.selectedItemId != destination)
            consumer_bottom_nav_view.selectedItemId = destination

        consumer_bottom_nav_view.setOnNavigationItemSelectedListener(this)
        consumer_bottom_nav_view.setOnNavigationItemReselectedListener(this)

//
//        val toolbar = findViewById<Toolbar>(R.id.consumer_toolbar)
//
//        setSupportActionBar(toolbar)
//
//        setupActionBarWithNavController(consumerNavController, currentConfig)
//
//        // the drawer when the height is too small
//        val sideNavView = findViewById<NavigationView>(R.id.consumer_nav_view)
//
//        sideNavView?.setupWithNavController(consumerNavController)
//
//        val bottomNav = findViewById<BottomNavigationView>(R.id.consumer_bottom_nav_view)
//
//        bottomNav?.setupWithNavController(consumerNavController)
    }

    fun activateProviderNavigation(destination: Int = -1) {
        currentSide = "provider"

        mainWrapper.visibility = View.INVISIBLE
        authWrapper.visibility = View.INVISIBLE
        providerWrapper.visibility = View.VISIBLE
        consumerWrapper.visibility = View.INVISIBLE

        val currentItem =
            providerPageToIndex[destination] ?: providerPageToIndex[R.id.provider_search_dest]!!

        // force viewPager to create all fragments
        provider_pager.offscreenPageLimit = providerFragments.size

        // setup view pager
        consumer_pager.adapter = null
        consumer_pager.currentItem = -1
        consumer_pager.removeOnPageChangeListener(this)

        provider_pager.adapter = ViewPagerAdapter()
        setItem(currentItem)

        if (provider_bottom_nav_view.selectedItemId != destination) {
            provider_bottom_nav_view.selectedItemId = destination
        }

        provider_pager.addOnPageChangeListener(this)

        // check deeplink only after viewPager is setup
        provider_pager.post(this::checkDeepLink)

        provider_bottom_nav_view.setOnNavigationItemSelectedListener(this)
        provider_bottom_nav_view.setOnNavigationItemReselectedListener(this)

//        val toolbar = findViewById<Toolbar>(R.id.provider_toolbar)
//
//        setSupportActionBar(toolbar)

//        setupActionBarWithNavController(providerNavController, currentConfig)
//
//        // the drawer when the height is too small
//        val sideNavView = findViewById<NavigationView>(R.id.provider_nav_view)
//
//        sideNavView?.setupWithNavController(providerNavController)
//
//        val bottomNav = findViewById<BottomNavigationView>(R.id.provider_bottom_nav_view)
////
//        bottomNav?.setupWithNavController(providerNavController)
    }

    override fun activateAuthNavigation() {
//        currentController = authNavController

        mainWrapper.visibility = View.INVISIBLE
        authWrapper.visibility = View.VISIBLE
        providerWrapper.visibility = View.INVISIBLE
        consumerWrapper.visibility = View.INVISIBLE

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
        if (currentSide == "provider") {
            val options = navOptions {
                anim {
                    enter = R.anim.slide_in_up
                    exit = R.anim.slide_out_down
                    popEnter = R.anim.slide_in_up
                    popExit = R.anim.slide_out_down
                }
            }
            mainNavController.navigate(
                MainNavigationDirections.providerNotificationsDest(),
                options
            )
            val position = providerPageToIndex[R.id.provider_notifications_dest]
            setItem(position!!)
            provider_bottom_nav_view.uncheckAllItems()
        } else {
            // Todo
        }
    }

    private fun checkDeepLink() {
        if (currentSide == "provider") {
            providerFragments.forEachIndexed { index, fragment ->
                val hasDeepLink = fragment.handleDeepLink(intent)
                if (hasDeepLink) setItem(index)
            }
        } else {
            consumerFragments.forEachIndexed { index, fragment ->
                val hasDeepLink = fragment.handleDeepLink(intent)
                if (hasDeepLink) setItem(index)
            }
        }
    }

    override fun setToolbar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    override fun handleGoogleLogin(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = auth.currentUser
                    user!!.getIdToken(false).addOnCompleteListener {
                        val token = it.result!!.token!!

                        authViewModel.loginWithGoogle(user.email!!, token)

                        authViewModel.registeredUser.observe(this, Observer<LoggedUser> {
                            val email = it.email
                            val password = ""

                            val account: Account = addOrFindAccount(email, password)
                            val context = this
                            with(accountManager) {
                                AuthenticationProvider.setAuthToken("FavourToken", context)
                                setAuthToken(account, "FavourToken", it.token)
                                setPassword(account, password)
                                setUserData(account, "FavourToken", "FavourToken")
                                setUserData(account, "userId", it.userId)
                                setUserData(account, "fullName", it.fullName)
                            }

                            if (it.permissions.sideChosen) {
                                navigateToConsumerOrProvider(null)
                            } else {
                                val chooseSideAction = MainNavigationDirections.chooseSideDest()
                                mainNavController.navigate(chooseSideAction)
                            }
                        })
                    }


                } else {
                }

                // ...
            }
    }

    private fun addOrFindAccount(email: String, password: String): Account {
        val accounts = accountManager.getAccountsByType("AllFavour")
        val account = if (accounts.isNotEmpty())
            accounts[0]
        else
            Account(email, "AllFavour")

        if (accounts.isEmpty()) {
            accountManager.addAccountExplicitly(account, password, null)
        } else {
            accountManager.setPassword(accounts[0], password)
        }
        return account
    }

    override fun navigateToConsumerOrProvider(inputSide: String?) {
        if (currentSide == null && inputSide != null) {
            currentSide = inputSide
        }

        val permissions = authViewModel.registeredUser.value!!.permissions

        if (currentSide == "consumer") {
            activateConsumerNavigation()
            val action = MainNavigationDirections.consumerSearchDest()
            mainNavController.navigate(action)
        } else {
            activateProviderNavigation()

            if (permissions.hasSufficientInfoProvider) { // has taken basic info form
                val action = MainNavigationDirections.providerSearchDest()
                mainNavController.navigate(action)
            } else {
                val action = MainNavigationDirections.providerBasicInfoFormDest()
                mainNavController.navigate(action)
            }
        }
    }

    override fun toggleBottomNavVisibility(show: Boolean) {
        if (currentSide == "consumer") {
            consumer_bottom_nav_view.visibility = if (show) View.VISIBLE else View.GONE
        } else {
            provider_bottom_nav_view.visibility = if (show) View.VISIBLE else View.GONE
        }
    }
}


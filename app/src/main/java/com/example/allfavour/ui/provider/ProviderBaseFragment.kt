package com.example.allfavour.ui.provider

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.example.allfavour.*

class ProviderBaseFragment : Fragment() {

    private val defaultInt = -1
    private var layoutRes: Int = -1
    private var toolbarId: Int = -1
    private var navHostId: Int = -1
    // root destinations
    private val rootDestinations = setOf(
        R.id.provider_search_dest,
//        R.id.consumer_my_favours_dest,
//        R.id.consumer_my_interests_dest,
        R.id.provider_profile_dest
//        R.id.consumer_messages_dest
    )
    // nav config with root destinations
    private val appBarConfig = AppBarConfiguration(rootDestinations)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        // extract arguments from bundle
        arguments?.let {
            layoutRes = it.getInt(KEY_LAYOUT)
            toolbarId = it.getInt(KEY_TOOLBAR)
            navHostId = it.getInt(KEY_NAV_HOST)

        } ?: return
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return if (layoutRes == defaultInt) null
        else inflater.inflate(layoutRes, container, false)
    }

    override fun onStart() {
        super.onStart()
        // return early if no arguments were parsed
        if (toolbarId == defaultInt || navHostId == defaultInt) return
        // find navController using navHostFragment
        val navController = requireActivity().findNavController(navHostId)
        // setup navigation with root destinations and toolbar
        val toolbar = activity!!.findViewById<Toolbar>(toolbarId)
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfig)
    }

    fun onBackPressed(): Boolean {
        return requireActivity()
            .findNavController(navHostId)
            .navigateUp(appBarConfig)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val toolbar = requireActivity().findViewById<Toolbar>(toolbarId)
        toolbar.inflateMenu(R.menu.provider_top_menu)
        toolbar.setOnMenuItemClickListener(this::onProviderItemSelected)

        super.onActivityCreated(savedInstanceState)
    }


    fun onProviderItemSelected(item: MenuItem): Boolean {
        val activity = requireActivity() as WithBottomNavigationSwitcher
        val controller = requireActivity().findNavController(navHostId)

        when (item.itemId) {
            R.id.provider_notifications_dest -> {
//                controller.navigate(
//                    MainNavigationDirections.consumerNotificationsDest(),
//                    options
//                )

                return super.onOptionsItemSelected(item)
            }
            R.id.provider_to_consumer_dest -> {
                when (navHostId) {
                    R.id.provider_search_nav_host -> {
                        activity.switchToConsumer(R.id.provider_search_dest)
//                        controller.navigate(ProviderSearchNavigationDirections.consumerSearchNavDest())
                    }
                    R.id.provider_profile_nav_host -> {
                        activity.switchToConsumer(R.id.consumer_profile_dest)
                        controller.navigate(ProviderProfileDirections.consumerProfileNavDest())
                    }
                    else -> {
                        activity.switchToConsumer(R.id.provider_search_dest)
//                        controller.navigate(ProviderSearchNavigationDirections.consumerSearchNavDest())
                    }
                }
            }
        }

        return true
    }

    fun popToRoot() {
        val navController =
            requireActivity().findNavController(navHostId)
        // navigate to the start destination
        navController.popBackStack(
            navController.graph.startDestination, false
        )
    }

    fun handleDeepLink(intent: Intent): Boolean =
        false

    companion object {

        private const val KEY_LAYOUT = "layout_key"
        private const val KEY_NAV_HOST = "nav_host_key"
        private const val KEY_TOOLBAR = "toolbar_key"

        fun newInstance(layoutRes: Int, toolbarId: Int, navHostId: Int) =
            ProviderBaseFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_LAYOUT, layoutRes)
                    putInt(KEY_TOOLBAR, toolbarId)
                    putInt(KEY_NAV_HOST, navHostId)
                }
            }
    }
}

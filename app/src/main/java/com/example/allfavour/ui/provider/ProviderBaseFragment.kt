package com.example.allfavour.ui.provider

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.example.allfavour.MainNavigationDirections
import com.example.allfavour.R
import com.example.allfavour.DecoratedActivity

class ProviderBaseFragment : Fragment() {

    private val defaultInt = -1
    private var layoutRes: Int = -1
    private var toolbarId: Int = -1
    private var navHostId: Int = -1
    // root destinations
    private val rootDestinations = setOf(
        R.id.provider_search_dest,
        R.id.provider_my_favours_dest,
        R.id.provider_my_offerings_dest,
        R.id.provider_profile_dest,
//        R.id.provider_notifications_dest
        R.id.provider_messages_dest
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val toolbar = requireActivity().findViewById<Toolbar>(toolbarId)
        (requireActivity() as DecoratedActivity).setToolbar(toolbar)
        toolbar.inflateMenu(R.menu.provider_top_menu)
        toolbar.setOnMenuItemClickListener(this::onProviderItemSelected)

        super.onActivityCreated(savedInstanceState)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onProviderItemSelected(item: MenuItem): Boolean {
        val mainNavController = requireActivity().findNavController(R.id.main_nav_activity)

        when (item.itemId) {
            R.id.provider_notifications_dest -> {
                (requireActivity() as DecoratedActivity).switchToNotificaitons()
            }
            R.id.provider_to_consumer_dest -> {
                when (navHostId) {
                    R.id.provider_search_nav_host -> {
                        mainNavController.navigate(MainNavigationDirections.consumerSearchDest())
                    }
                    R.id.provider_profile_nav_host -> {
                        mainNavController.navigate(MainNavigationDirections.consumerProfileDest())
                    }
                    else -> mainNavController.navigate(MainNavigationDirections.consumerSearchDest())
                }
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var a = R.layout.provider_profile_nav_host == layoutRes
        var b = R.layout.provider_notifications_nav_host == layoutRes
        var c = R.layout.provider_messages_nav_host == layoutRes
        var d = R.layout.provider_search_nav_host == layoutRes
        var da = R.layout.provider_my_favours_nav_host == layoutRes
        var k = R.layout.provider_my_interests_nav_host == layoutRes

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

    fun popToRoot() {
        val navController =
            requireActivity().findNavController(navHostId)
        // navigate to the start destination
        navController.popBackStack(
            navController.graph.startDestination, false
        )
    }

    fun handleDeepLink(intent: Intent): Boolean {
        var res = false
        try {
            res = requireActivity()
                .findNavController(navHostId)
                .handleDeepLink(intent)
        } catch (e: IllegalStateException) {
            println("tui kat stane")
        }

        return res
    }

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

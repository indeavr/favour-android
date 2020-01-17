package com.example.allfavour.ui.consumer

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.example.allfavour.ConsumerSearchNavigationDirections
import com.example.allfavour.MainNavigationDirections
import com.example.allfavour.R
import com.example.allfavour.WithBottomNavigationSwitcher
import com.example.allfavour.ui.provider.ProviderBaseFragmentDirections

class ConsumerBaseFragment : Fragment() {

    private val defaultInt = -1
    private var layoutRes: Int = -1
    private var toolbarId: Int = -1
    private var navHostId: Int = -1
    // root destinations
    private val rootDestinations = setOf(
        R.id.consumer_search_dest,
        R.id.consumer_my_favours_dest,
        R.id.consumer_my_interests_dest,
        R.id.consumer_profile_dest,
        R.id.consumer_notifications_dest,
        R.id.consumer_messages_dest
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
        (requireActivity() as WithBottomNavigationSwitcher).setToolbar(toolbar)
        toolbar.inflateMenu(R.menu.consumer_top_menu)
        toolbar.setOnMenuItemClickListener(this::onConsumerItemSelected)

        super.onActivityCreated(savedInstanceState)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    fun onConsumerItemSelected(item: MenuItem): Boolean {
        val activity = requireActivity() as WithBottomNavigationSwitcher
        val controller = requireActivity().findNavController(navHostId)

        when (item.itemId) {
            R.id.consumer_notifications_dest -> {
                activity.switchToNotificaitons()
            }
            R.id.consumer_to_provider_dest -> {
                when (navHostId) {
                    R.id.consumer_search_nav_host -> {
                        activity.switchToProvider(R.id.provider_search_dest)
                        controller.navigate(ConsumerSearchNavigationDirections.providerSearchNavDest())
                    }
                    R.id.consumer_profile_nav_host -> {
                        activity.switchToProvider(R.id.provider_profile_dest)
                        controller.navigate(ProviderBaseFragmentDirections.providerProfileNavDest2())
                    }
                    else ->{
                        activity.switchToProvider(R.id.provider_search_dest)
                        controller.navigate(ConsumerSearchNavigationDirections.providerSearchNavDest())
                    }
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
        var a = R.layout.consumer_profile_nav_host == layoutRes
//        var b = R.layout.consumer_notifications_nav_host == layoutRes
//        var c = R.layout.consumer_messages_nav_host == layoutRes
        var d = R.layout.consumer_search_nav_host == layoutRes
//        var da = R.layout.consumer_my_favours_nav_host == layoutRes
//        var k = R.layout.consumer_my_interests_nav_host == layoutRes

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
            ConsumerBaseFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_LAYOUT, layoutRes)
                    putInt(KEY_TOOLBAR, toolbarId)
                    putInt(KEY_NAV_HOST, navHostId)
                }
            }
    }
}

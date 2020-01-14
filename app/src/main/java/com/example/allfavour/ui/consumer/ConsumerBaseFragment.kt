package com.example.allfavour.ui.consumer

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import com.example.allfavour.MainNavigationDirections
import com.example.allfavour.R
import kotlinx.android.synthetic.main.main_nav_activity.*

class ConsumerBaseFragment : Fragment() {

    private val defaultInt = -1
    private var layoutRes: Int = -1
    private var toolbarId: Int = -1
    private var navHostId: Int = -1
    // root destinations
    private val rootDestinations = setOf(
        R.id.consumer_search_dest,
//        R.id.consumer_my_favours_dest,
//        R.id.consumer_my_interests_dest,
        R.id.consumer_profile_dest
//        R.id.consumer_messages_dest
    )
    // nav config with root destinations
    private val appBarConfig = AppBarConfiguration(rootDestinations)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // extract arguments from bundle
        arguments?.let {
            layoutRes = it.getInt(KEY_LAYOUT)
            toolbarId = it.getInt(KEY_TOOLBAR)
            navHostId = it.getInt(KEY_NAV_HOST)

        } ?: return
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val toolbar = requireActivity().findViewById<Toolbar>(toolbarId)
        toolbar.inflateMenu(R.menu.consumer_top_menu)
        toolbar.setOnMenuItemClickListener(this::onConsumerItemSelected)
        super.onActivityCreated(savedInstanceState)
    }


    fun onConsumerItemSelected(item: MenuItem): Boolean {
//        val currentDestination = requireActivity().findNavController(navHostId).currentDestination?.id;
        val mainNavController = requireActivity().findNavController(R.id.main_nav_activity)


//        val a = currentDestination == R.id.consumer_search_navigation
//        val b = currentDestination == R.id.consumer_search_dest

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
                mainNavController.navigate(MainNavigationDirections.consumerNotificationsDest(), options)

                return super.onOptionsItemSelected(item)
            }
            R.id.consumer_to_provider_dest -> {
                when (navHostId) {
                    R.id.consumer_search_nav_host -> {
                        mainNavController.navigate(MainNavigationDirections.providerSearchDest())
                    }
                    R.id.consumer_profile_nav_host -> {
                        mainNavController.navigate(MainNavigationDirections.providerProfileDest())
                    }
                }

                true
            }

            else -> true
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = requireActivity().findNavController(R.id.provider_search_nav_host)
        navController.navigate(R.id.provider_search_dest)

        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
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

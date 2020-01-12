package com.example.allfavour.ui.consumer

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.*

import com.example.allfavour.R
import com.example.allfavour.ToolbarSetupable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import java.lang.Exception

class ConsumerFragment : Fragment() {
    lateinit var appBarConfiguration: AppBarConfiguration

    val consumerNavController: NavController? by lazy { view?.findNavController() }

    companion object {
        fun newInstance() = ConsumerFragment()
    }

    private lateinit var viewModel: ConsumerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        throw Exception("onCreateView")

        return inflater.inflate(R.layout.consumer_nav_host, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        throw Exception("onCreate")


        val toolbar = activity?.findViewById<Toolbar>(R.id.consumer_toolbar)

        (activity as ToolbarSetupable).setToolbar(toolbar)

        val sideNavView = view?.findViewById<NavigationView>(R.id.consumer_nav_view)

        sideNavView?.setupWithNavController(consumerNavController!!)

        val bottomNav = view?.findViewById<BottomNavigationView>(R.id.consumer_bottom_nav_view)

        bottomNav?.setupWithNavController(consumerNavController!!)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConsumerViewModel::class.java)
        throw Exception("onActivityCreated")
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        throw Exception("onViewCreated")


    }
}

package com.example.allfavour.ui.provider.myFavours

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.example.allfavour.R
import com.example.allfavour.ui.provider.myFavours.active.ActiveFavoursFragment
import com.example.allfavour.ui.provider.myFavours.applications.ApplicationsFragment
import com.example.allfavour.ui.provider.myFavours.pending.PendingFavoursFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

val pages = listOf("Active", "Pending", "Applications")

class MyFavoursFragment : Fragment() {
    private val ARG_COUNT = "currentPosition"
    private var counter: Int = 0

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: MyFavoursPageAdapter

    companion object {
        fun newInstance() = MyFavoursFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COUNT, counter)
            }
        }
    }

    private lateinit var viewModel: MyFavoursViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: why let ?
        if (arguments != null) {
            arguments?.getInt(ARG_COUNT)?.let {
                counter = it
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.provider_my_favours_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MyFavoursPageAdapter(this)

        viewPager = view.findViewById(R.id.provider_my_favours_tab_pager)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.provider_my_favours_tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = pages[position]
        }.attach()
    }

}

//private const val ARG_OBJECT = "object"

class MyFavoursPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        return when (position) {
            0 -> ActiveFavoursFragment()
            1 -> PendingFavoursFragment()
            2 -> ApplicationsFragment()
            else -> ActiveFavoursFragment()
        }
    }
}

// Instances of this class are fragments representing a single
// object in our collection.
//class DemoObjectFragment : Fragment() {
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.provider_mf_active_fragment, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView?.text = getInt(ARG_OBJECT).toString()
//        }
//    }
//}
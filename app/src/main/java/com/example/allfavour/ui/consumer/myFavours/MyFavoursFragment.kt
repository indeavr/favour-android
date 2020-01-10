package com.example.allfavour.ui.consumer.myFavours

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

import com.example.allfavour.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyFavoursFragment : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var myFavoursAdapter: MyFavoursPageAdapter

    companion object {
        fun newInstance() = MyFavoursFragment()
    }

    private lateinit var viewModel: MyFavoursViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumer_my_favours_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        myFavoursAdapter = MyFavoursPageAdapter(this)
//        viewPager = view.findViewById(R.id.consumer_my_favours_tab_pager)
//        viewPager.adapter = myFavoursAdapter
//
//        val tabLayout = view.findViewById<TabLayout>(R.id.consumer_my_favours_tab_layout)
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = "OBJECT ${(position + 1)}"
//        }.attach()
    }

}

private const val ARG_OBJECT = "object"

class MyFavoursPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 100

    override fun createFragment(position: Int): Fragment {
        // Return a NEW fragment instance in createFragment(int)
        val fragment = DemoObjectFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}

// Instances of this class are fragments representing a single
// object in our collection.
class DemoObjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.consumer_mf_active_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            val textView: TextView = view.findViewById(android.R.id.text1)
            textView?.text = getInt(ARG_OBJECT).toString()
        }
    }
}
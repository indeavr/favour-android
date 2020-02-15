package com.example.allfavour.ui.consumer.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.allfavour.MainNavigationDirections

import com.example.allfavour.R
import com.example.allfavour.utility.HandleNotifications
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.basic_info_form_fragment.*
import kotlinx.android.synthetic.main.consumer_search_fragment.*
import kotlinx.android.synthetic.main.main_nav_activity.*

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumer_search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_notify.setOnClickListener {
            HandleNotifications.showNotification(requireContext())
        }

        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            requireActivity().findNavController(R.id.main_nav_activity)
                .navigate(MainNavigationDirections.authNavigationDest())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SearchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

package com.example.allfavour.ui.provider.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.example.allfavour.R
import com.example.allfavour.ui.consumer.addFavour.AddFavourFragment
import com.example.allfavour.ui.provider.profile.addOffering.AddOfferingFragment
import kotlinx.android.synthetic.main.consumer_search_fragment.*
import kotlinx.android.synthetic.main.provider_profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.provider_profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        provider_my_account_button.setOnClickListener {
//            val action = ProfileFragmentDirections.myAccountDestAction()
//            findNavController().navigate(action)
            // Doesn't work for some reason
            // Navigation.createNavigateOnClickListener(R.id.my_account_dest_action, null)
        }
        add_offering_button.setOnClickListener {
            //            navController.navigate(SearchFragmentDirections.actionConsumerAddFavour())

            val newFragment = AddOfferingFragment.newInstance()
//
            newFragment.show(fragmentManager!!, null)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }
}

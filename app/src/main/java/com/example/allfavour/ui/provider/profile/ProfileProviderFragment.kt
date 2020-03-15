package com.example.allfavour.ui.provider.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.navigation.navGraphViewModels

import com.example.allfavour.R
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.example.allfavour.ui.consumer.addFavour.AddFavourFragment
import com.example.allfavour.ui.provider.profile.addOffering.AddOfferingFragment
import com.example.allfavour.ui.provider.search.SearchViewModel
import com.example.allfavour.ui.provider.search.SearchViewModelFactory
import kotlinx.android.synthetic.main.consumer_search_fragment.*
import kotlinx.android.synthetic.main.provider_profile_fragment.*

class ProfileProviderFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileProviderFragment()
    }

    private val factory = ProfileViewModelFactory()
    private val viewModel: ProfileProviderViewModel by navGraphViewModels(R.id.provider_profile_navigation) { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.provider_profile_fragment, container, false)

        viewModel.getProvider(
            AuthenticationProvider.getUserId(requireActivity())
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val provider = viewModel.provider.value
        if(provider != null){
            provider_address.text = provider.location.address
            provider_phone_number.text = provider.phoneNumber
        }

        viewModel.provider.observe(viewLifecycleOwner, Observer {
            provider_address.text = it.location.address
            provider_phone_number.text = it.phoneNumber
        })

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
        // TODO: Use the ViewModel
    }
}

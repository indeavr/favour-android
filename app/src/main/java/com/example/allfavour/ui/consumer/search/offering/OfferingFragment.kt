package com.example.allfavour.ui.consumer.search.offering

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels

import com.example.allfavour.R
import com.example.allfavour.data.model.OfferingModel
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.example.allfavour.ui.consumer.search.OfferingsSearchFragmentDirections
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModelFactory
import kotlinx.android.synthetic.main.offering_fragment.*
import kotlinx.android.synthetic.main.provider_favour_fragment.*

class OfferingFragment : Fragment() {

    companion object {
        fun newInstance() = OfferingFragment()
    }

    private val navController: NavController by lazy { NavHostFragment.findNavController(this) }

    private val factory = OfferingsSearchViewModelFactory()
    private val viewModel: OfferingsSearchViewModel by navGraphViewModels(R.id.consumer_search_navigation) { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.offering_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val offeringId = arguments?.getString("offeringId")!!
        viewModel.setCurrentOffering(offeringId)

        viewModel.currentOffering.observe(viewLifecycleOwner, Observer {
            offering_provider_firstName.text = it.provider!!.firstName
        })

        offering_apply_button.setOnClickListener {
            navController.navigate(OfferingFragmentDirections.applyForOfferingDest())
        }
    }

}

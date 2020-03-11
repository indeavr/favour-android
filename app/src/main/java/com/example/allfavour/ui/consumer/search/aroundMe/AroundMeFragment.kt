package com.example.allfavour.ui.consumer.search.aroundMe

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels

import com.example.allfavour.R
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModel
import com.example.allfavour.ui.consumer.search.OfferingsSearchViewModelFactory

class AroundMeFragment : Fragment() {

    companion object {
        fun newInstance() = AroundMeFragment()
    }

    private val factory = OfferingsSearchViewModelFactory()
    private val viewModel: OfferingsSearchViewModel by navGraphViewModels(R.id.consumer_search_navigation) { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.offerings_around_me_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}

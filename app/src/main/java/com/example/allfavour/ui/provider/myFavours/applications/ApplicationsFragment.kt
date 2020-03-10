package com.example.allfavour.ui.provider.myFavours.applications

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.allfavour.R

class ApplicationsFragment : Fragment() {

    companion object {
        fun newInstance() = ApplicationsFragment()
    }

    private lateinit var viewModel: ApplicationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.provider_applications_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ApplicationsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

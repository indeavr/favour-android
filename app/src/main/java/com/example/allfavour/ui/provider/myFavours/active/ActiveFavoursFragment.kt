package com.example.allfavour.ui.provider.myFavours.active

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.allfavour.R

class ActiveFavoursFragment : Fragment() {

    companion object {
        fun newInstance() = ActiveFavoursFragment()
    }

    private lateinit var viewModel: ActiveFavoursViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.provider_active_favours_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ActiveFavoursViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

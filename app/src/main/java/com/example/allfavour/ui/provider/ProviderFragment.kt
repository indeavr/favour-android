package com.example.allfavour.ui.provider

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar

import com.example.allfavour.R
import com.example.allfavour.ToolbarSetupable

class ProviderFragment : Fragment() {

    companion object {
        fun newInstance() = ProviderFragment()
    }

    private lateinit var viewModel: ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.provider_nav_host, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toolbar = activity?.findViewById<Toolbar>(R.id.provider_toolbar)

        (activity as ToolbarSetupable).setToolbar(toolbar)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

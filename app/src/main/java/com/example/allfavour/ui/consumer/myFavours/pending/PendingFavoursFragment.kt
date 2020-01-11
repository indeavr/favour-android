package com.example.allfavour.ui.consumer.myFavours.pending

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.allfavour.R

class PendingFavoursFragment : Fragment() {

    companion object {
        fun newInstance() = PendingFavoursFragment()
    }

    private lateinit var viewModel: PendingFavoursViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumer_pending_favours_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PendingFavoursViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

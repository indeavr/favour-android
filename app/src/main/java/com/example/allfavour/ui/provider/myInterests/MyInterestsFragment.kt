package com.example.allfavour.ui.provider.myInterests

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.allfavour.R

class MyInterestsFragment : Fragment() {

    companion object {
        fun newInstance() = MyInterestsFragment()
    }

    private lateinit var viewModel: MyInterestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.provider_my_interests_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MyInterestsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

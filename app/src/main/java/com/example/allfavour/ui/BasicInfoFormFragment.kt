package com.example.allfavour.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.allfavour.MainNavigationDirections

import com.example.allfavour.R
import com.example.allfavour.databinding.BasicInfoFormFragmentBinding
import androidx.lifecycle.Observer


class BasicInfoFormFragment : Fragment() {
    val mainNavController: NavController? by lazy { activity?.findNavController(R.id.main_nav_activity) }

    private val viewModel: BasicInfoFormViewModel by lazy {
        ViewModelProviders.of(this).get(BasicInfoFormViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: BasicInfoFormFragmentBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.basic_info_form_fragment,
                container,
                false
            )

        viewModel.init()

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        setupSubmit()

        return binding.root
    }

    private fun setupSubmit() {
        viewModel.submittedSuccessfully.observe(this, Observer<Boolean> {
            val action = MainNavigationDirections.providerSearchDest()
            mainNavController?.navigate(action)
        })
    }

    companion object {
        fun newInstance() = BasicInfoFormFragment()
    }
}

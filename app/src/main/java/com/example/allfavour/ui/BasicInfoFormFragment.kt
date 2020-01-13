package com.example.allfavour.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController

import com.example.allfavour.R
import kotlinx.android.synthetic.main.basic_info_form_fragment.*

class BasicInfoFormFragment : Fragment() {

    val mainNavController: NavController? by lazy { activity?.findNavController(R.id.main_nav_activity) }

    companion object {
        fun newInstance() = BasicInfoFormFragment()
    }

    private lateinit var viewModel: BasicInfoFormViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.basic_info_form_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        basic_forms_submit_button.setOnClickListener {
            val action = BasicInfoFormFragmentDirections.consumerDest()
            mainNavController?.navigate(action)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BasicInfoFormViewModel::class.java)
        // TODO: Use the ViewModel
    }

}

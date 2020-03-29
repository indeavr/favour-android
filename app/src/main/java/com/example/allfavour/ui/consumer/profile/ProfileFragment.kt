package com.example.allfavour.ui.consumer.profile

import android.accounts.AccountManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.example.allfavour.DecoratedActivity

import com.example.allfavour.R
import com.example.allfavour.databinding.BasicInfoConsumerFormFragmentBinding
import com.example.allfavour.databinding.ConsumerProfileFragmentBinding
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.example.allfavour.ui.auth.AuthViewModelFactory
import com.example.allfavour.ui.auth.AuthenticationViewModel

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private val factory = ProfileConsumerViewModelFactory()
    private val viewModel: ProfileConsumerViewModel by navGraphViewModels(R.id.consumer_profile_navigation) { factory }

    private val authViewModel: AuthenticationViewModel by lazy {
        ViewModelProviders.of(
            this.requireActivity(),
            AuthViewModelFactory()
        ).get(AuthenticationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ConsumerProfileFragmentBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.consumer_profile_fragment,
                container,
                false
            )

        viewModel.userId = authViewModel.userId
        viewModel.getConsumer()

        binding.lifecycleOwner = this
        binding.viewmodel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        view.findViewById<Button>(R.id.consumer_my_account_button)?.setOnClickListener {
            logout()

            //            val action = ProfileFragmentDirections.myAccountDestAction()
//            findNavController().navigate(action)
            // Doesn't work for some reason
            // Navigation.createNavigateOnClickListener(R.id.my_account_dest_action, null)
        }
    }

    private fun logout() {
        (requireActivity() as DecoratedActivity).handleLogout()
    }

}

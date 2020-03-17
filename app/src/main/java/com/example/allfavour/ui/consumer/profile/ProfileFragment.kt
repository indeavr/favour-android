package com.example.allfavour.ui.consumer.profile

import android.accounts.AccountManager
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.allfavour.DecoratedActivity
import com.example.allfavour.MainActivity

import com.example.allfavour.R
import com.example.allfavour.services.authentication.AuthenticationProvider
import com.example.allfavour.ui.auth.LoginFragmentDirections

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumer_profile_fragment, container, false)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun logout() {
        val accountManager = AccountManager.get(context?.applicationContext)
        val accounts = accountManager.getAccountsByType("AllFavour")
        val oldToken = AuthenticationProvider.getAuthToken(context!!.applicationContext)
        accountManager.invalidateAuthToken("AllFavour", oldToken)
        AuthenticationProvider.invalidateToken(context!!.applicationContext)
        accountManager.clearPassword(accounts[0])
        (requireActivity() as DecoratedActivity).activateAuthNavigation()
        activity!!.findNavController(R.id.main_nav_activity).navigate(R.id.auth_navigation_dest)
    }

}

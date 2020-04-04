package com.example.allfavour.ui.auth

import android.accounts.Account
import android.accounts.AccountManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.allfavour.MainNavigationDirections

import com.example.allfavour.R
import com.example.allfavour.services.authentication.AuthenticationProvider
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.register_fragment_x.*

class RegisterFragment : GoogleLoginBaseFragment() {
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var accountManager: AccountManager
    private val mainNavController: NavController by lazy { requireActivity().findNavController(R.id.main_nav_activity) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.register_fragment_x, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity(), AuthViewModelFactory())
            .get(AuthenticationViewModel::class.java)
        accountManager = AccountManager.get(context)

        log_in_button.setOnClickListener {
            val email = email_input_field.text.toString()

            viewModel.saveEmailBeforeTransition(email)

            val action =
                RegisterFragmentDirections.actionRegisterFragmentToRegisterDetailsFragment()
            findNavController().navigate(action)
        }

        switch_register.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }

//        val observer = viewModel.authModel.observe(this) {
//            val email = emailField.text.toString()
//            val password = passwordField.text.toString()
//
//            val account: Account = addOrFindAccount(email, password)
//
//            with(accountManager) {
//                AuthenticationProvider.setAuthToken("FavourToken", context!!)
//                setAuthToken(account, "FavourToken", it.token)
//                setPassword(account, password)
//                setUserData(account, "FavourToken", "FavourToken")
//                setUserData(account, "userId", it.userId)
//                setUserData(account, "fullName", it.fullName)
//            }
//
//            mainNavController.navigate(MainNavigationDirections.consumerSearchDest())
//        }

        continue_with_google_button.setOnClickListener {
//            viewModel.authModel.removeObserver(observer)
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun addOrFindAccount(email: String, password: String): Account {
        val accounts = accountManager.getAccountsByType("AllFavour")
        val account = if (accounts.isNotEmpty())
            accounts[0]
        else
            Account(email, "AllFavour")

        if (accounts.isEmpty()) {
            accountManager.addAccountExplicitly(account, password, null)
        } else {
            accountManager.setPassword(accounts[0], password)
        }
        return account
    }

}

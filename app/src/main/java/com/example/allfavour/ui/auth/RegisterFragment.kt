package com.example.allfavour.ui.auth

import android.accounts.Account
import android.accounts.AccountManager
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
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

class RegisterFragment : Fragment() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var accountManager: AccountManager

    private val mainNavController: NavController by lazy { requireActivity().findNavController(R.id.main_nav_activity) }
    private  val authNavController: NavController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, AuthViewModelFactory())
            .get(RegisterViewModel::class.java)
        accountManager = AccountManager.get(context)

        registerButton.setOnClickListener {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            viewModel.register(email, password)
        }

        switchToLoginLabel.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action )
        }

        viewModel.registeredUser.observe(this) {
            val email = emailField.text.toString()
            val password = passwordField.text.toString()

            val account: Account = addOrFindAccount(email, password)

            with(accountManager) {
                AuthenticationProvider.setAuthToken("FavourToken", context!!)
                setAuthToken(account, "FavourToken", it.token)
                setPassword(account, password)
                setUserData(account, "FavourToken", "FavourToken")
                setUserData(account, "userId", it.userId)
            }

            mainNavController.navigate(MainNavigationDirections.consumerSearchDest())
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

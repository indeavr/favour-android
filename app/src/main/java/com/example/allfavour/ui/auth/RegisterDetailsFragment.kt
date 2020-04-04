package com.example.allfavour.ui.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.allfavour.DecoratedActivity
import com.example.allfavour.MainNavigationDirections
import com.example.allfavour.R
import com.example.allfavour.data.model.AuthModel
import com.example.allfavour.services.authentication.AuthenticationProvider
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.register_fragment_details.*

class RegisterDetailsFragment: Fragment() {
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var accountManager: AccountManager

    private val mainNavController: NavController by lazy { requireActivity().findNavController(R.id.main_nav_activity) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity(), AuthViewModelFactory())
            .get(AuthenticationViewModel::class.java)
        accountManager = AccountManager.get(context)

        email_input.text = SpannableStringBuilder(viewModel.email)
        register_button.setOnClickListener {

            val email = email_input.text.toString()
            val password = password_input.text.toString()
            val firstName = first_name_input.text.toString()
            val lastName = last_name_input.text.toString()

            viewModel.authModel.observe(viewLifecycleOwner, Observer<AuthModel> {
                (requireActivity() as DecoratedActivity).handleServerLogin(email, it)
            })

            viewModel.register(email,password, firstName, lastName)
        }

//        viewModel.authModel.observe(this) {
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

        viewModel.error.observe(this){
            val text = it
            val duration = Toast.LENGTH_SHORT

            val toast = Toast.makeText(this.activity, text, duration)
            toast.show()
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
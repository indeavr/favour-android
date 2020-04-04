package com.example.allfavour.ui.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NavigationRes
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.allfavour.DecoratedActivity
import com.example.allfavour.R
import com.example.allfavour.data.model.AuthModel
import kotlinx.android.synthetic.main.login_fragment_x.*

inline fun <reified T : ViewModel> NavController.viewModel(@NavigationRes navGraphId: Int): T {
    val storeOwner = getViewModelStoreOwner(navGraphId)
    return ViewModelProvider(storeOwner)[T::class.java]
}

class LoginFragment : GoogleLoginBaseFragment() {
    private lateinit var viewModel: AuthenticationViewModel
    private lateinit var accountManager: AccountManager

    private val mainNavController: NavController? by lazy { activity?.findNavController(R.id.main_nav_activity) }

    private lateinit var observer: Observer<AuthModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.login_fragment_x, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity(), AuthViewModelFactory())
            .get(AuthenticationViewModel::class.java)
        accountManager = AccountManager.get(context)
        log_in_button.setOnClickListener {
            val email = email_input_field.text.toString()
            val password = password_input_field.text.toString()

            viewModel.authModel.observe(viewLifecycleOwner, Observer<AuthModel> {
                (requireActivity() as DecoratedActivity).handleServerLogin(email, it)
            })

            viewModel.login(email, password)
        }

        switch_register.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

//        observer = viewModel.registeredUser.observe(this) {
//            val email = email_input_field.text.toString()
//            val password = password_input_field.text.toString()
//
//            if (email != null && password != null) {
//                val account: Account = addOrFindAccount(email, password)
//
//                with(accountManager) {
//                    AuthenticationProvider.setAuthToken("FavourToken", context!!)
//                    setAuthToken(account, "FavourToken", it.token)
//                    setPassword(account, password)
//                    setUserData(account, "FavourToken", "FavourToken")
//                    setUserData(account, "userId", it.userId)
//                    setUserData(account, "fullName", it.fullName)
//                }
//                mainNavController!!.navigate(MainNavigationDirections.consumerSearchDest())
//            }
//
//        }

        continue_with_google_button.setOnClickListener {
            //            viewModel.registeredUser.removeObserver(observer)
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
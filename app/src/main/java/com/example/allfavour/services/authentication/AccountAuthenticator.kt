package com.example.allfavour.services.authentication

import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.allfavour.MainActivity
import com.example.allfavour.data.AuthRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FavourAccountAuthenticator(
    val context: Context,
    val application: Application,
    val authRepository: AuthRepository
) :
    AbstractAccountAuthenticator(context) {

    override fun getAuthTokenLabel(authTokenType: String?): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun confirmCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        options: Bundle?
    ): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCredentials(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthToken(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        authTokenType: String?,
        options: Bundle?
    ): Bundle? {
        val am = AccountManager.get(application)
        var authToken = AuthenticationProvider.getAuthToken(context)

        var bundle = Bundle()

        if (authToken == null) {
            val password = am.getPassword(account)

            if (password != null) {
                GlobalScope.launch {
                    if (account != null) {
                        val loginResult = authRepository.login(account.name, password)

                        bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name)
                        bundle.putString(AccountManager.KEY_PASSWORD, password)
                        bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type)
                        bundle.putString(AccountManager.KEY_AUTHTOKEN, loginResult.token)
                        bundle.putString("AUTH_TOKEN_TYPE", authTokenType)

                        response?.onResult(bundle)
                    }
                }


                return null
            }

        } else {
            bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account?.name)
            bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
            bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken)

            println("authTokenType________________" + authTokenType)

            bundle.putString("FavourToken", authTokenType)

            return bundle
        }

        // From here user needs to login again
        val intent = Intent(this.context, MainActivity::class.java)

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, account?.type)
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, account?.name)

        bundle.putParcelable(AccountManager.KEY_INTENT, intent)
        return bundle
    }

    override fun hasFeatures(
        response: AccountAuthenticatorResponse?,
        account: Account?,
        features: Array<out String>?
    ): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editProperties(
        response: AccountAuthenticatorResponse?,
        accountType: String?
    ): Bundle {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAccount(
        response: AccountAuthenticatorResponse?,
        accountType: String?,
        authTokenType: String?,
        requiredFeatures: Array<out String>?,
        options: Bundle?
    ): Bundle {
        val intent = Intent(this.context, MainActivity::class.java)
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType)
//        intent.putExtra(ADD_ACCOUNT, true)
//        intent.putExtra(TOKEN_TYPE, authTokenType)

        println("authTokenType" + authTokenType)

//        intent.putExtra(AUTH_TYPE, authTokenType)
//        intent.putExtra(IS_ADDING_NEW_ACCOUNT, true)

        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)

        val bundle = Bundle()
        bundle.putParcelable(AccountManager.KEY_INTENT, intent)

        return bundle
    }
}
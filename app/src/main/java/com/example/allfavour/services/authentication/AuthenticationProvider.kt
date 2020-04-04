package com.example.allfavour.services.authentication

import android.accounts.AccountManager
import android.content.Context
import com.google.android.gms.common.util.CollectionUtils.isEmpty

object AuthenticationProvider {
    private var authToken: String? = null

    const val AUTH_TOKEN = "authToken"
    const val FIREBASE_TOKEN = "firebaseToken"
    const val USER_ID = "userId"
    const val FIREBASE_USER_ID = "firebaseUserId"
    const val FULL_NAME = "fullName"

    fun getUserId(context: Context): String? {
        val accountManager: AccountManager = AccountManager.get(context)
        //TODO: make Constants class --> (! strings wont work, must be a class)
        val accounts = accountManager.getAccountsByType("AllFavour")

        if (accounts == null || accounts.size == 0 || this.getAuthToken(context) == null) {
            return null
        }
        return accountManager.getUserData(accounts[0], USER_ID)
    }

    fun getFirebaseUserId(context: Context): String? {
        val accountManager: AccountManager = AccountManager.get(context)
        //TODO: make Constants class --> (! strings wont work, must be a class)
        val accounts = accountManager.getAccountsByType("AllFavour")

        if (accounts == null || accounts.size == 0 || this.getAuthToken(context) == null) {
            return null
        }
        return accountManager.getUserData(accounts[0], FIREBASE_USER_ID)
    }


    fun getUserFullname(context: Context): String {
        val accountManager: AccountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType("AllFavour")

        return accountManager.getUserData(accounts[0], FULL_NAME)
    }

    fun getAuthToken(context: Context): String? {
        val accountManager: AccountManager = AccountManager.get(context)
        val accounts = accountManager.getAccountsByType("AllFavour")
        if (accounts.count() == 0) {
            return null
        }
        return authToken ?: context.getSharedPreferences(
            AUTH_TOKEN,
            Context.MODE_PRIVATE
        ).getString("kur", null)
    }

    fun setAuthToken(authT: String, context: Context) {
        authToken = authT
        context.getSharedPreferences(AUTH_TOKEN, Context.MODE_PRIVATE).edit()
            .putString("kur", authT).apply()
    }

    fun invalidateToken(context: Context) {
        authToken = null
        context.getSharedPreferences(AUTH_TOKEN, Context.MODE_PRIVATE).edit()
            .remove("kur").apply()
    }
}
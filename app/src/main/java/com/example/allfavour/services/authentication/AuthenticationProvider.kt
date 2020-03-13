package com.example.allfavour.services.authentication

import android.accounts.AccountManager
import android.content.Context

object AuthenticationProvider {
    private var authToken: String? = null

    fun getUserId(context: Context): String {
        val accountManager: AccountManager = AccountManager.get(context)
        val accounts = accountManager.accounts

        return accountManager.getUserData(accounts[0], "userId")
    }

    fun getAuthToken(context: Context): String? {
        return authToken ?: context.getSharedPreferences(
            "authToken",
            Context.MODE_PRIVATE
        ).getString("kur", null)
    }

    fun setAuthToken(authT: String, context: Context) {
        authToken = authT
        context.getSharedPreferences("authToken", Context.MODE_PRIVATE).edit()
            .putString("kur", authT).apply()
    }

    fun invalidateToken(context: Context) {
        authToken = null
        context.getSharedPreferences("authToken", Context.MODE_PRIVATE).edit().remove("kur").apply()
    }
}
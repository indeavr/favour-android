package com.example.allfavour.graphql

import android.accounts.AccountManager
import android.content.Context
import com.example.allfavour.services.authentication.AuthenticationConsumer
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(val context: Context) : Interceptor {
    val accountManager = AccountManager.get(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val accounts = accountManager.getAccountsByType("AllFavour")

        val original = chain.request()

        if (accounts.isNotEmpty()) {
            val userId = accountManager.getUserData(accounts[0], "userId")

//            val newUrl = original
//                .url()
//                .newBuilder().addQueryParameter("userId", userId)
//                .build()

            val modifiedRequest = original
                .newBuilder()

            modifiedRequest.header("UserId", userId)
            val tokenType = accountManager.getUserData(accounts[0], "AllFavour")
            val account = accounts[0]
            val token =
                AuthenticationConsumer.getAuthToken(context) //accountManager.peekAuthToken(account, tokenType)
            if (token != null) {
                modifiedRequest
                    .header("Authorization", "Bearer $token")
                    .header("Accept", "application/javascript, application/json")
                    .method(original.method(), original.body())
            }
            return chain.proceed(modifiedRequest.build())
        }

        return chain.proceed(original)
    }
}
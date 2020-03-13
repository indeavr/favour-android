package com.example.allfavour.data

import LoginMutation
import LoginWithGoogleMutation
import RegisterMutation
import com.apollographql.apollo.coroutines.toDeferred
import com.example.allfavour.data.model.LoggedInUser
import com.example.allfavour.graphql.GraphqlConnector

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

    suspend fun login(username: String, password: String): LoggedInUser {
        val mutation = LoginMutation(username, password)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        return LoggedInUser(result.data()!!.login!!.userId, username, result.data()!!.login!!.token)
    }

    suspend fun register(username: String, password: String): LoggedInUser {
        val mutation = RegisterMutation(username, password)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        return LoggedInUser(
            result.data()!!.register!!.userId,
            username,
            result.data()!!.register!!.token
        )
    }

    suspend fun loginWithGoogle(username: String, serverToken: String): LoggedInUser {
        val mutation = LoginWithGoogleMutation(serverToken)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val user = result.data()

        return LoggedInUser(
            user!!.loginWithGoogle!!.userId,
            username,
            user.loginWithGoogle!!.token
        )
    }
}

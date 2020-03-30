package com.example.allfavour.data

import com.allfavour.graphql.api.LoginMutation
import com.allfavour.graphql.api.LoginWithGoogleMutation
import com.allfavour.graphql.api.RegisterMutation
import com.apollographql.apollo.coroutines.toDeferred
import com.example.allfavour.data.model.AuthModel
import com.example.allfavour.data.model.PermissionsModel
import com.example.allfavour.graphql.GraphqlConnector

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class AuthRepository {

    suspend fun login(username: String, password: String): AuthModel {
        val mutation = LoginMutation(username, password)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val authPayload = result.data()!!.login!!

        return AuthModel.fromGraphType(authPayload)
    }

    suspend fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): AuthModel {
        val mutation = RegisterMutation(username, password, firstName, lastName)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val authPayload = result.data()!!.register!!

        return AuthModel.fromGraphType(authPayload)
    }

    suspend fun loginWithGoogle(serverToken: String): AuthModel {
        val mutation = LoginWithGoogleMutation(serverToken)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val authPayload = result.data()!!.loginWithGoogle!!

        return AuthModel.fromGraphType(authPayload)
    }
}

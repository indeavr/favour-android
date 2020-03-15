package com.example.allfavour.data

import com.allfavour.graphql.api.LoginMutation
import com.allfavour.graphql.api.LoginWithGoogleMutation
import com.allfavour.graphql.api.RegisterMutation
import com.apollographql.apollo.coroutines.toDeferred
import com.example.allfavour.data.model.LoggedInUser
import com.example.allfavour.data.model.PermissionsModel
import com.example.allfavour.graphql.GraphqlConnector

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

    suspend fun login(username: String, password: String): LoggedInUser {
        val mutation = LoginMutation(username, password)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val login = result.data()!!.login!!

        val permissions = PermissionsModel(
            hasSufficientInfoConsumer = login.permissions.hasSufficientInfoConsumer,
            hasSufficientInfoProvider = login.permissions.hasSufficientInfoProvider,
            sideChosen = login.permissions.sideChosen
        )

        return LoggedInUser(login.userId, username, login.token, login.fullName, permissions)
    }

    suspend fun register(
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): LoggedInUser {
        val mutation = RegisterMutation(username, password, firstName, lastName)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val permissions = PermissionsModel(
            hasSufficientInfoConsumer = result.data()!!.register!!.permissions.hasSufficientInfoConsumer,
            hasSufficientInfoProvider = result.data()!!.register!!.permissions.hasSufficientInfoProvider,
            sideChosen = result.data()!!.register!!.permissions.sideChosen
        )

        return LoggedInUser(
            result.data()!!.register!!.userId,
            username,
            result.data()!!.register!!.token,
            result.data()!!.register!!.fullName,
            permissions
        )
    }

    suspend fun loginWithGoogle(username: String, serverToken: String): LoggedInUser {
        val mutation = LoginWithGoogleMutation(serverToken)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val user = result.data()

        val permissions = PermissionsModel(
            hasSufficientInfoConsumer = result.data()!!.loginWithGoogle!!.permissions.hasSufficientInfoConsumer,
            hasSufficientInfoProvider = result.data()!!.loginWithGoogle!!.permissions.hasSufficientInfoProvider,
            sideChosen = result.data()!!.loginWithGoogle!!.permissions.sideChosen
        )

        return LoggedInUser(
            user!!.loginWithGoogle!!.userId,
            username,
            user.loginWithGoogle!!.token,
            user.loginWithGoogle!!.fullName,
            permissions
        )
    }
}

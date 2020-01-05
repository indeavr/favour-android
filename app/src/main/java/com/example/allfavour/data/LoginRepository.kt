package com.example.allfavour.data

import LoginMutation
import RegisterMutation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.example.allfavour.data.model.LoggedInUser
import com.example.allfavour.graphql.GraphqlConnector

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository {

    suspend fun login(username: String, password: String): LoggedInUser {
        val mutation = RegisterMutation(username, password)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val fakeUser = LoggedInUser(result.data()!!.register!!.userId, username)
        return fakeUser
    }
}

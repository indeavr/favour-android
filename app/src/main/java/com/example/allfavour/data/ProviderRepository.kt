package com.example.allfavour.data

import com.allfavour.graphql.api.CreateProviderMutation
import com.apollographql.apollo.coroutines.toDeferred
import com.example.allfavour.data.model.Provider
import com.example.allfavour.graphql.GraphqlConnector
import com.example.allfavour.services.authentication.AuthenticationProvider

class ProviderRepository {

    suspend fun createProvider(userId: String, provider: Provider) {
        val mutation = CreateProviderMutation(userId, provider.toInputType())
        val task = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val result = task.data()

    }
}
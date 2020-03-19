package com.example.allfavour.data

import com.allfavour.graphql.api.CreateProviderMutation
import com.allfavour.graphql.api.MyOfferingsQuery
import com.allfavour.graphql.api.ProviderQuery
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.example.allfavour.data.model.ConsumerModel
import com.example.allfavour.data.model.OfferingModel
import com.example.allfavour.data.model.ProviderModel
import com.example.allfavour.graphql.GraphqlConnector

class ConsumerRepository {
    suspend fun createConsumer(userId: String, consumer: ConsumerModel) {
        val mutation = CreateProviderMutation(userId, consumer.toInputType())
        val task = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        val result = task.data()

    }

    suspend fun getProvider(userId: String): ProviderModel? {
        try {
            val query = ProviderQuery(userId)
            val task = GraphqlConnector.client.query(query).toDeferred().await()

            val result = task.data()?.provider ?: run {
                throw NullPointerException()
            }

            return ProviderModel.fromGraphType(result)

        } catch (e: ApolloException) {
            return null
        } catch (e: NullPointerException) {
            // you will end up here if repositories!! throws above. This will happen if your server sends a response
            // with missing fields or errors
            return null
        }
    }

//    suspend fun getMyOfferings(userId: String): ArrayList<OfferingModel>? {
//        val query = MyOfferingsQuery(userId)
//        val task = GraphqlConnector.client.query(query).toDeferred().await()
//
//        val myOfferings = task.data()?.myActiveOfferings ?: run {
//            throw NullPointerException()
//        }
//
//        return ArrayList(myOfferings.map {
//            OfferingModel.fromGraphType(it!!)
//        })
//    }
}
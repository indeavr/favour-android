package com.example.allfavour.data

import com.allfavour.graphql.api.ApplyForOfferingMutation
import com.allfavour.graphql.api.CreateOfferingMutation
import com.allfavour.graphql.api.OfferingsQuery
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.example.allfavour.data.model.ApplicationModel
import com.example.allfavour.data.model.OfferingModel
import com.example.allfavour.graphql.GraphqlConnector

class OfferingRepository {

    suspend fun getOfferings(): ArrayList<OfferingModel> {
        val query = OfferingsQuery()
        val result = GraphqlConnector.client.query(query).toDeferred().await()

        val receivedOfferings = result.data()!!.offerings!!

        val offerings = arrayListOf<OfferingModel>()

        // TODO: handle null location
        receivedOfferings.forEach {
            if (it != null) {
                offerings.add(OfferingModel.fromGraphType(it))
            }
        }

        return offerings
    }

    // maybe this will be an inputType
    suspend fun addOffering(userId: String, offering: OfferingModel) {
        val mutation = CreateOfferingMutation(userId, offering.toInputType())
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        print(result.data())
    }

    suspend fun applyForOffering(
        userId: String,
        offeringId: String,
        application: ApplicationModel
    ): Boolean? {
        try {
            val mutation = ApplyForOfferingMutation(userId, offeringId, application.toInputType())
            val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

            return true
        } catch (e: ApolloException) {
            return null
        } catch (e: NullPointerException) {
            // you will end up here if repositories!! throws above. This will happen if your server sends a response
            // with missing fields or errors
            return null
        }
    }
}
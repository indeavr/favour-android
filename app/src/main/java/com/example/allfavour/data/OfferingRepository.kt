package com.example.allfavour.data

import com.allfavour.graphql.api.CreateFavourMutation
import com.allfavour.graphql.api.CreateOfferingMutation
import com.allfavour.graphql.api.OfferingsQuery
import com.allfavour.graphql.api.type.LocationInput
import com.allfavour.graphql.api.type.OfferingInput
import com.apollographql.apollo.coroutines.toDeferred
import com.example.allfavour.data.model.LocationModel
import com.example.allfavour.data.model.Offering
import com.example.allfavour.graphql.GraphqlConnector

class OfferingRepository {

    suspend fun getOfferings(): ArrayList<Offering> {
        val query = OfferingsQuery()
        val result = GraphqlConnector.client.query(query).toDeferred().await()

        val receivedOfferings = result.data()!!.offerings!!

        val offerings = arrayListOf<Offering>()

        // TODO: handle null location
        receivedOfferings.forEach {
            val inputLocation = it!!.location!!

            val location = LocationModel(
                id = null,
                mapsId = inputLocation.id,
                address = inputLocation.address,
                country = inputLocation.country,
                latitude = inputLocation.latitude.toDouble(),
                longitude = inputLocation.longitude.toDouble(),
                town = inputLocation.town
            )

            offerings.add(
                Offering(
                    it.id,
                    it.title,
                    it.description,
                    it.money,
                    location
                )
            )
        }

        return offerings
    }

    // maybe this will be an inputType
    suspend fun addOffering(offering: Offering) {
        val locationInput = LocationInput(
            mapsId = offering.location!!.mapsId,
            id = "",
            country = offering.location!!.country!!,
            town = offering.location!!.town!!,
            address = offering.location!!.address!!,
            latitude = offering.location!!.latitude.toString(),
            longitude = offering.location!!.longitude.toString()
        )

        val input = OfferingInput("", offering.title, "", offering.money, "", locationInput)
        val mutation = CreateOfferingMutation(input)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        print(result.data())
    }
}
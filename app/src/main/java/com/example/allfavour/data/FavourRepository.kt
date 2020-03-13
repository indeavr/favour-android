package com.example.allfavour.data

import com.allfavour.graphql.api.CreateFavourMutation
import com.allfavour.graphql.api.FavoursQuery
import com.allfavour.graphql.api.type.FavourInputType
import com.allfavour.graphql.api.type.LocationInput
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.example.allfavour.data.model.Favour
import com.example.allfavour.data.model.LocationModel
import com.example.allfavour.graphql.GraphqlConnector

class FavourRepository {

    suspend fun getFavours(): ArrayList<Favour> {
        val query = FavoursQuery()
        val result = GraphqlConnector.client.query(query).toDeferred().await()

        val receivedFavours = result.data()!!.favours!!

        val favours = arrayListOf<Favour>()

        // TODO: handle null location
        receivedFavours.forEach {
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
            favours.add(
                Favour(
                    it.id,
                    it.title,
                    it.description,
                    it.money,
                    location
                )
            )
        }

        return favours
    }

    // maybe this will be an inputType
    suspend fun addFavour(favour: Favour) {
        val locationInput = LocationInput(
            mapsId = favour.location!!.mapsId,
            id = "",
            country = favour.location!!.country!!,
            town = favour.location!!.town!!,
            address = favour.location!!.address!!,
            latitude = favour.location!!.latitude.toString(),
            longitude = favour.location!!.longitude.toString()
        )

        val input = FavourInputType("", favour.title, "", favour.money, "", locationInput)
        val mutation = CreateFavourMutation(input)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        print(result.data())
    }
}

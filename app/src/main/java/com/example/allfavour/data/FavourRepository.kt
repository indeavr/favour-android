package com.example.allfavour.data

import CreateFavourMutation
import FavoursQuery
import com.apollographql.apollo.coroutines.toDeferred
import com.example.allfavour.data.model.Favour
import com.example.allfavour.graphql.GraphqlConnector
import type.FavourInputType

class FavourRepository {

    suspend fun getFavours(): ArrayList<Favour> {
        val query = FavoursQuery()
        val result = GraphqlConnector.client.query(query).toDeferred().await()

        val receivedFavours = result.data()!!.favours!!

        val favours = arrayListOf<Favour>()

        receivedFavours.forEach {
            favours.add(
                Favour(
                    it!!.id,
                    it.title,
                    it.description,
                    it.money
                )
            )
        }

        return favours
    }

    // maybe this will be an inputType
    suspend fun addFavour(favour: Favour) {
        val input = FavourInputType("", favour.title, "", favour.money, "", "")
        val mutation = CreateFavourMutation(input)
        val result = GraphqlConnector.client.mutate(mutation).toDeferred().await()

        print(result.data())
    }
}

package com.example.allfavour.data

import FavourQuery
import FavoursQuery
import com.apollographql.apollo.coroutines.toDeferred
import com.example.allfavour.data.model.Favour
import com.example.allfavour.graphql.GraphqlConnector

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
                    it.money
                )
            )
        }

        return favours
    }
}

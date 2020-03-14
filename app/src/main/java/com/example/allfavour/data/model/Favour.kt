package com.example.allfavour.data.model

import com.allfavour.graphql.api.type.FavourInputType


data class Favour(
    var id: String?,
    var title: String,
    var description: String,
    var money: Double,
    var location: LocationModel
) {
    fun toInputType(): FavourInputType {
        return FavourInputType(
            title = this.title,
            money = this.money,
            description = this.description,
            state = "",
            location = this.location.toInputType()
        )
    }
}
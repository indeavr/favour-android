package com.example.allfavour.data.model

import com.allfavour.graphql.api.type.OfferingInput

data class Offering(
    var id: String?,
    var title: String,
    var description: String,
    var money: Double,
    var location: LocationModel
) {
    fun toInputType(): OfferingInput {
        return OfferingInput(
            title = this.title,
            money = this.money,
            description = this.description,
            state = "",
            location = this.location.toInputType()
        )
    }
}
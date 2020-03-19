package com.example.allfavour.data.model

import com.allfavour.graphql.api.MyOfferingsQuery
import com.allfavour.graphql.api.OfferingsQuery
import com.allfavour.graphql.api.type.OfferingInput
import com.google.android.libraries.places.internal.it

data class OfferingModel(
    var id: String?,
    var title: String,
    var description: String,
    var money: Double,
    var location: LocationModel,
    var provider: ProviderModel? // null only on createProvider mutation & myOfferingsQuery
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

    companion object {
        fun fromGraphType(graphType: OfferingsQuery.Offering): OfferingModel {
            return OfferingModel(
                id = graphType.id,
                title = graphType.title,
                description = graphType.description,
                money = graphType.money,
                location = LocationModel.fromGraphType(graphType.location),
                provider = ProviderModel.fromGraphType(graphType.provider)
            )
        }

        fun fromGraphType(graphType: MyOfferingsQuery.Offering): OfferingModel {
            return OfferingModel(
                id = graphType.id,
                title = graphType.title,
                description = graphType.description,
                money = graphType.money,
                provider = null,
                location = LocationModel.fromGraphType(graphType.location)
            )
        }
    }
}
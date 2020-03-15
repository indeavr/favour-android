package com.example.allfavour.data.model

import com.allfavour.graphql.api.OfferingsQuery
import com.allfavour.graphql.api.ProviderQuery
import com.allfavour.graphql.api.type.LocationInput
import com.allfavour.graphql.api.type.ProviderInput

data class LocationModel(
    var id: String?,
    var mapsId: String, // Google Maps id
    var address: String,
    var country: String?,
    var town: String?,
    var longitude: Double,
    var latitude: Double
) {

    fun toInputType(): LocationInput {
        return LocationInput(
            mapsId = this.mapsId,
            country = this.country!!, // TODO: check this "!!"
            town = this.town!!,
            address = this.address,
            latitude = this.latitude.toString(),
            longitude = this.longitude.toString()
        )
    }

    companion object {
        fun fromGraphType(graphType: ProviderQuery.Location): LocationModel {
            return LocationModel(
                id = graphType.id,
                mapsId = graphType.mapsId,
                country = graphType.country, // TODO: check this "!!"
                town = graphType.town,
                address = graphType.address,
                latitude = graphType.latitude.toDouble(),
                longitude = graphType.longitude.toDouble()
            )
        }

        fun fromGraphType(graphType: OfferingsQuery.Location): LocationModel {
            return LocationModel(
                id = graphType.id,
                mapsId = graphType.mapsId,
                country = graphType.country, // TODO: check this "!!"
                town = graphType.town,
                address = graphType.address,
                latitude = graphType.latitude.toDouble(),
                longitude = graphType.longitude.toDouble()
            )
        }

        fun fromGraphType(graphType: OfferingsQuery.Location1): LocationModel {
            return LocationModel(
                id = graphType.id,
                mapsId = graphType.mapsId,
                country = graphType.country, // TODO: check this "!!"
                town = graphType.town,
                address = graphType.address,
                latitude = graphType.latitude.toDouble(),
                longitude = graphType.longitude.toDouble()
            )
        }
    }
}
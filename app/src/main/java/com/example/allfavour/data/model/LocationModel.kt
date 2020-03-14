package com.example.allfavour.data.model

import com.allfavour.graphql.api.type.LocationInput

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
}
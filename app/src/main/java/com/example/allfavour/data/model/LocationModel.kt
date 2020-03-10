package com.example.allfavour.data.model

data class LocationModel(
    var id: String?,
    var mapsId: String, // Google Maps id
    var address: String,
    var country: String?,
    var town: String?,
    var longitude: Double,
    var latitude: Double
)
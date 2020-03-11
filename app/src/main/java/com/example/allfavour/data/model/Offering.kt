package com.example.allfavour.data.model

data class Offering(
    var id: String?,
    var title: String,
    var description: String,
    var money: Double,
    var location: LocationModel?
) {

}
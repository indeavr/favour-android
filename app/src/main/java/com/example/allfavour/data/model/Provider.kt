package com.example.allfavour.data.model

import com.allfavour.graphql.api.type.ProviderInput

class Provider(
    var phoneNumber: String,
    var location: LocationModel,
    var sex: String
) {

    fun toInputType(): ProviderInput {
        return ProviderInput(
            phoneNumber = this.phoneNumber,
            location = this.location.toInputType(),
            sex = this.sex
        )
    }
}
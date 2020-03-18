package com.example.allfavour.data.model

import com.allfavour.graphql.api.OfferingsQuery
import com.allfavour.graphql.api.ProviderQuery
import com.allfavour.graphql.api.type.ProviderInput

data class ConsumerModel(
    var id: String,
    var phoneNumber: String,
    var location: LocationModel,
    var firstName: String?,
    var lastName: String?,
    var sex: String
) {
    fun toInputType(): ProviderInput {
        return ProviderInput(
            phoneNumber = this.phoneNumber,
            location = this.location.toInputType(),
            sex = this.sex
        )
    }

    companion object {
        fun fromGraphType(graphType: ProviderQuery.Provider): ProviderModel {
            return ProviderModel(
                id = graphType.id,
                phoneNumber = graphType.phoneNumber,
                sex = graphType.sex,
                location = LocationModel.fromGraphType(graphType.location),
                firstName = graphType.firstName,
                lastName = graphType.lastName
            )
        }

        fun fromGraphType(graphType: OfferingsQuery.Provider): ProviderModel {
            return ProviderModel(
                id = graphType.id,
                phoneNumber = graphType.phoneNumber,
                sex = graphType.sex,
                firstName = graphType.firstName,
                lastName = graphType.lastName,
                location = LocationModel.fromGraphType(graphType.location)
            )
        }
    }
}
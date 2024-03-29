package com.example.allfavour.data.model

import com.allfavour.graphql.api.OfferingsQuery
import com.allfavour.graphql.api.PersonConsumerQuery
import com.allfavour.graphql.api.ProviderQuery
import com.allfavour.graphql.api.type.PersonConsumerInput
import com.allfavour.graphql.api.type.ProviderInput

data class ConsumerModel(
    var id: String,
    var phoneNumber: String,
    var firstName: String?,
    var lastName: String?,
    var sex: String
) {
    fun toInputType(): PersonConsumerInput {
        return PersonConsumerInput(
            description = "",
            phoneNumber = this.phoneNumber,
            sex = this.sex
        )
    }

    companion object {
        fun fromGraphType(graphType: PersonConsumerQuery.PersonConsumer): ConsumerModel {
            return ConsumerModel(
                id = graphType.id,
                phoneNumber = graphType.phoneNumber,
                sex = graphType.sex,
                firstName = graphType.firstName,
                lastName = graphType.lastName
            )
        }

        fun fromGraphType(graphType: OfferingsQuery.Provider): ConsumerModel {
            return ConsumerModel(
                id = graphType.id,
                phoneNumber = graphType.phoneNumber,
                sex = graphType.sex,
                firstName = graphType.firstName,
                lastName = graphType.lastName
            )
        }
    }
}
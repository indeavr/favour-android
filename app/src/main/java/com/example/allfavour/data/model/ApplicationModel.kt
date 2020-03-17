package com.example.allfavour.data.model

import com.allfavour.graphql.api.MyOfferingsQuery
import com.allfavour.graphql.api.OfferingsQuery
import com.allfavour.graphql.api.type.ApplicationInput
import java.util.*

data class ApplicationModel(
    var id: String?,
    var message: String,
    var time: List<PeriodModel>,
    var applyTime: Date?,
    var consumer: String?
) {

    fun toInputType(): ApplicationInput {
        return ApplicationInput(
            message = this.message,
            time = this.time.map {
                it.toInputType()
            }
        )
    }

    companion object {
        fun fromGraphType(graphType: MyOfferingsQuery.Application): ApplicationModel {
            return ApplicationModel(
                id = graphType.id,
                message = graphType.message,
                time = graphType.time.map {
                    PeriodModel.fromGraphType(it!!)
                },
                consumer = null,
                applyTime = null
            )
        }
    }
}
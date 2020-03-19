package com.example.allfavour.data.model

import com.allfavour.graphql.api.MyOfferingsQuery
import com.google.android.libraries.places.internal.it

data class ActiveOfferingModel(
    var applications: List<ApplicationModel>,
    var offering: OfferingModel
) {
    companion object {

        fun fromGraphType(graphType: MyOfferingsQuery.MyActiveOffering): ActiveOfferingModel {
            return ActiveOfferingModel(
                applications = graphType.applications.map {
                    ApplicationModel.fromGraphType(it!!)
                },
                offering = OfferingModel.fromGraphType(graphType.offering)
            )
        }
    }
}
package com.example.allfavour.data.model

import java.util.*

data class OfferingApplicationModel(
    var id: String?,
    var message: String,
    var time: PeriodModel,
    var applyTime: Date?,
    var consumer: String?
) {

    fun toInputType() {

    }
}